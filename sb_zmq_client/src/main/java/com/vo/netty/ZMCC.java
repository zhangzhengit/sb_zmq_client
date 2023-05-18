package com.vo.netty;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 *
 * @author zhangzhen
 * @data Aug 10, 2020
 * 
 */
public class ZMCC {
	
	private static ChannelHandlerContext ctx1;

	public static ChannelHandlerContext get() {
		if (ctx1 != null) {
			return ctx1;
		}

		while (ctx1 == null) {

		}
		return ctx1;
	}

	public static void set(final ChannelHandlerContext ctx) {
		ctx1 = ctx;
	}
	
}
