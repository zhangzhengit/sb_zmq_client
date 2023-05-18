package com.vo.netty;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Multiset;
import com.vo.core.CCache1;
import com.vo.core.ZIDGenerator;
import com.vo.core.ZMExecutorService;
import com.vo.core.ZMMessageHandler;
import com.vo.protobuf.ZMP;
import com.vo.protobuf.ZMPTypeEnum;
import com.vo.protobuf.ZPU;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 *
 * @author zhangzhen
 * @data Aug 7, 2020
 * 
 */
@Component
public final class ZMQClientHandler extends ChannelInboundHandlerAdapter {

	@Autowired
	private ZMMessageHandler zmMessageHandler;

	private	final BlockingQueue<ZMP> queue = new LinkedBlockingQueue<>();
	
	public void writeAndFlush(final ZMP zmp) {
		this.queue.add(zmp);
	}
	
	@PostConstruct
	private void waf1() {
		System.out.println(Thread.currentThread().getName() + "\t" + LocalDateTime.now() + "\t"
				+ "ZMQClientHandler.writeAndFlush()");
		System.out.println();
		
		ZMExecutorService.execute(() -> {
			
			while (true) {
				try {
					final ZMP zmp = this.queue.take();
					if (zmp != null) {
						writeAndFlush1(zmp);
					}
					
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		});
		
	}

	private static void writeAndFlush1(final ZMP zmp) {
		final byte[] wan = ZPU.wanzhangbytearray(ZPU.serialize(zmp));
		ZMQClientHandler.writeAndFlush1(wan);
	}
	
	private static void writeAndFlush1(final byte[] bs) {
		ZMCC.get().writeAndFlush(Unpooled.copiedBuffer(bs));
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		ZMCC.set(ctx);

		final Multiset<String> topics = CCache1.topics();
		topics.forEach(topic -> {
			final ZMP initZMP = ZMP.builder().id(ZIDGenerator.generateId_UUID()).type(ZMPTypeEnum.INIT.getType())
					.topic(topic).build();

			this.writeAndFlush(initZMP);
		});

	}

	@Override
	public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
		
		if (msg instanceof ZMP) {
			final ZMP zmp = (ZMP) msg;
			final String topic = zmp.getTopic();
			final Set<Method> methodSet = CCache1.getByTopic(topic);
			if (CollUtil.isEmpty(methodSet)) {
				return;
			}

			this.zmMessageHandler.handleAsync(zmp);

		} else {

		}

	}

}