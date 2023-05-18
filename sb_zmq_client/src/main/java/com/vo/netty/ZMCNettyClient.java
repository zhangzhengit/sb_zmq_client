package com.vo.netty;

import com.vo.configuration.ZMQServerConfigurationProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 
 *
 * @author zhangzhen
 * @data Aug 7, 2020
 * 
 */
@Component
@Order(value = 1)
public class ZMCNettyClient implements ApplicationRunner, DisposableBean {

	@Autowired
	private ZMQClientHandler zmClientHandler;
	@Autowired
	private ZMQServerConfigurationProperties zmqsp;

	private EventLoopGroup group;

	@Override
	public void run(final ApplicationArguments args) throws Exception {
		System.out.println(Thread.currentThread().getName() + "\t" + LocalDateTime.now() + "\t" + "ZMCNettyClient.run()");
		System.out.println();
		
		System.out.println("zmqsp = " + zmqsp);
		
		final Bootstrap bootstrap = new Bootstrap();
		group = new NioEventLoopGroup();
		bootstrap.group(group);
		bootstrap.channel(NioSocketChannel.class);

		bootstrap.remoteAddress(new InetSocketAddress(this.zmqsp.getHost(), this.zmqsp.getPort()));
		bootstrap.option(ChannelOption.SO_BACKLOG, 20000);
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.option(ChannelOption.SO_REUSEADDR, true);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

		bootstrap.handler(channelInitializer(this.zmClientHandler));

		try {
			final ChannelFuture future = bootstrap.connect().sync();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static ChannelInitializer<SocketChannel> channelInitializer(final ZMQClientHandler zmClientHandler) {
		return new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(final SocketChannel ch) throws Exception {
				final ChannelPipeline p = ch.pipeline();
				p.addLast(new IdleStateHandler(2, 2, 2, TimeUnit.MINUTES));
				p.addLast(new ZMQZMPDecoder());
				p.addLast(zmClientHandler);
			}
		};
	}

	@Override
	public void destroy() throws Exception {
		System.out.println(Thread.currentThread().getName() + "\t" + LocalDateTime.now() + "\t" + "ZMCNettyClient.destroy()");
		System.out.println();

		group.shutdownGracefully().sync();
	}

}
