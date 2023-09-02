package com.vo.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.vo.configuration.ZMQServerConfigurationProperties;
import com.vo.core.CCache1;
import com.vo.core.ZContext;
import com.vo.core.ZIDGenerator;
import com.vo.core.ZLog2;
import com.vo.core.ZMQMessageHandler;
import com.vo.protobuf.ZMP;
import com.vo.protobuf.ZMPTypeEnum;
import com.vo.protobuf.ZPU;
import com.votool.ze.ZES;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 到服务器的连接
 *
 * @author zhangzhen
 * @date 2023年8月2日
 *
 */
public class Connection {

	private static final ZLog2 LOG = ZLog2.getInstance();

	private static final int SIZE = 1024 * 100;

	private final static com.votool.ze.ZE ZE = ZES.newZE("zmq-client-Thread-");

	private final Queue<String> waitingId = new LinkedBlockingQueue<>();
	/**
	 * 读取到的响应结果
	 */
//	Queue<Message> readQ = new LinkedBlockingQueue<>();
	private final List<ZMP> readQ = new Vector<>();

	private final AtomicBoolean connected = new AtomicBoolean(false);

	private BufferedOutputStream bufferedOutputStream;
	private SocketChannel socketChannel;

	public synchronized void connect() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "Connection.connect()");
		final ZMQServerConfigurationProperties properties = ZContext.getBean(ZMQServerConfigurationProperties.class);

		LOG.info("开始连接服务端,host={},port={}", properties.getHost(), properties.getPort());

		if (this.connected.get()) {
			LOG.warn("已经连接成功了服务端,return,host={},port={}", properties.getHost(), properties.getPort());
			return;
		}

		try {

			LOG.info("开始连接服务端-Socket开始连接,host={},port={}", properties.getHost(), properties.getPort());

//			final Socket socket = new Socket(properties.getHost(), properties.getPort());
//			final OutputStream outputStream = socket.getOutputStream();
//			this.bufferedOutputStream = new BufferedOutputStream(outputStream);

			this.socketChannel = SocketChannel.open();
		    this.socketChannel.connect(new InetSocketAddress(properties.getHost(), properties.getPort()));

			// 设置 已连接 状态
			this.connected.set(true);

			LOG.info("连接服务端-Socket连接成功,host={},port={}", properties.getHost(), properties.getPort());

			LOG.info("开始发送INIT消息");


			// 发送 INIT 消息
			final Multiset<String> topics = CCache1.topics();
			topics.forEach(topic -> {
				final ZMP initZMP = ZMP.builder().id(ZIDGenerator.generateId_UUID()).type(ZMPTypeEnum.INIT.getType())
						.topic(topic).build();

				LOG.info("开始发送INIT消息,topic={}", topic);
//				final ZMP zmpR = this.sendAndGetWith(initZMP, 1000 * 4);

				final byte[] wan = ZPU.wanzhangbytearray(ZPU.serialize(initZMP));
				final ByteBuffer buffer = ByteBuffer.wrap(wan);
				while (buffer.hasRemaining()) {
					try {
						this.socketChannel.write(buffer);
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
				LOG.info("服务端处理INIT消息成功,topic={},response={}", topic);
			});

			ZE.executeInQueue(() -> {

				this.wT();
			});

//			ZE.executeInQueue(() -> {
//				this.read(socket);
//			});

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}


	private void wT() {
		while (true) {

			final int LENGTH = 4;

			final ByteBuffer lengthBuffer = ByteBuffer.allocate(LENGTH);
			try {

				int lenghtREAD = 0;
				while (lenghtREAD < LENGTH) {
					final int lenghtT = this.socketChannel.read(lengthBuffer);
					if (lenghtT == -1) {
						continue;
					}
					lenghtREAD += lenghtT;
				}

				final int lengA = ZPU.byteArrayToInt(lengthBuffer.array());

				final ByteBuffer dataBuffer = ByteBuffer.allocate(lengA);

				int dataRead = 0;
				while (dataRead < lengA) {
					final int dataReadT = this.socketChannel.read(dataBuffer);
					if (dataReadT == -1) {
						continue;
					}

					dataRead += dataReadT;
				}

				System.out.println("dataRead = " + dataRead);

				final ZMP messageR = ZPU.deserialize(dataBuffer.array(), ZMP.class);

				handleMessage(messageR);

			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}


	public synchronized void send(final ZMP message) {

		if (StrUtil.isEmpty(message.getId())) {
			throw new IllegalArgumentException("id不能为空");
		}

		if (message.getType() == null) {
			throw new IllegalArgumentException("type不能为空");
		}

		final byte[] ba = ZPU.serialize(message);
		final byte[] wanzhangbytearray = ZPU.wanzhangbytearray(ba);
		try {
//			this.bufferedOutputStream.write(wanzhangbytearray);
//			this.bufferedOutputStream.flush();

			final ByteBuffer b = ByteBuffer.wrap(wanzhangbytearray);
			while (b.hasRemaining()) {
				this.socketChannel.write(b);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 发送消息并且一直等到消息响应，根据消息ID来判断响应来了。
	 *
	 * @param message
	 * @return
	 */
//	public ZMP sendAndGetWithDH(final ZMP message) {
////		return this.sendAndGetWithDH(message, Integer.MAX_VALUE);
//		return message;
//	}

	/**
	 * 发送消息并且一直等到消息响应，根据消息ID来判断响应来了。
	 *
	 * @param message
	 * @param timeout	超时毫秒数，在此值范围内等待。
	 * @return
	 *
	 */
	public ZMP sendAndGetWith(final ZMP message, final int timeout) {

		this.waitingId.add(message.getId());

		this.send(message);

		final int wams = timeout <= 0 ? Integer.MAX_VALUE : timeout;
		for (int i = 1; i <= wams; i++) {

			Connection.sleep1MS();
			final Optional<ZMP> findAny = this.readQ.stream().filter(m -> m.getId().equals(message.getId()))
					.findAny();
			if (findAny.isPresent()) {

				final ZMP mR = findAny.get();

				this.readQ.remove(mR);

				return mR;
			}
		}

		return null;
	}


	private static void sleep1MS() {
		try {
			Thread.sleep(1);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取响应结果，一直while(true)循环读取服务端发来的消息
	 *
	 * @param socket
	 */
	private void read(final Socket socket) {
		try {

//			SocketChannel socketChannel = new

			final InputStream inputStream = socket.getInputStream();
//			final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, SIZE);
			final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

			final int length = 4;
			final byte[] lBA = new byte[length];

			while (true) {

				final int read = bufferedInputStream.read(lBA);
				if (read == -1) {
					System.out.println("read == -1");
					continue;
				}

				final int dataLength = ZPU.byteArrayToInt(lBA);

				if (dataLength > 0) {
					final byte[] dataBA = new byte[dataLength];
					final int read2 = bufferedInputStream.read(dataBA);
					if (read2 == -1) {
						continue;
					}
					if (read2 != dataLength) {
						continue;
					}

					System.out.println("read2 = " + read2);
					final ZMP messageR = ZPU.deserialize(dataBA, ZMP.class);
					System.out.println("messageR = " + messageR);
					this.readQ.add(messageR);
					handleMessage(messageR);
				}
			}

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	static int messageRX  = 0;
	public static void handleMessage(final ZMP message) {
		// FIXME 2023年9月1日 下午8:41:05 zhanghen: 异步有bug，比实际发送的少几条
//		ZE.executeInQueue(() -> {
			h(message);
//		});
	}

	static ConcurrentMap<Object, Object> dhMap = Maps.newConcurrentMap();


	private static void h(final ZMP message) {
		final ZMPTypeEnum typeEnum = ZMPTypeEnum.valueOfType(message.getType());
		switch (typeEnum) {
		case INIT_OK:
			LOG.info("初始化INIT消息成功,message={}", message);
			break;

		case MESSAGE:
			final String topic = message.getTopic();
			final Set<Method> methodSet = CCache1.getByTopic(topic);
			if (CollUtil.isEmpty(methodSet)) {
				return;
			}
			messageRX++;
			System.out.println("messageRX = " + messageRX);
//			this.zmMessageHandler.handleAsync(message);
			ZMQMessageHandler.handle1(message);

			break;

		default:
			break;
		}
	}

}
