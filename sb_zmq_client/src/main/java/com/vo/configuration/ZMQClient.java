package com.vo.configuration;

import java.util.Date;

import com.vo.anno.ZComponent;
import com.vo.core.ZContext;
import com.vo.core.ZIDGenerator;
import com.vo.protobuf.ZMP;
import com.vo.protobuf.ZMPTypeEnum;
import com.vo.socket.Connection;

import cn.hutool.core.util.StrUtil;

/**
 *
 * 用于发送消息
 *
 * @author zhangzhen
 * @data Aug 10, 2020
 *
 */
@ZComponent
public final class ZMQClient {


	public void send(final String topic, final String message) {
		this.send(topic, message, ZIDGenerator.generateId_UUID(), 0L);
	}

	public void send(final String topic, final String message, final long delayMilliSeconds) {
		this.send(topic, message, ZIDGenerator.generateId_UUID(), delayMilliSeconds);
	}

	public void send(final String topic, final String message,final String messageId, final long delayMilliSeconds) {
		ZMQClient.check(topic, message, messageId, delayMilliSeconds);

		final ZMP zmp = ZMP.builder()
				.id(messageId)
				.topic(topic)
				.type(ZMPTypeEnum.MESSAGE.getType())
				.content(message)
				.delayMilliSeconds(delayMilliSeconds)
				.createTime(new Date())
				.build();

		final Connection con = ZContext.getBean(Connection.class);
		con.send(zmp);
	}

	private static void check(final String topic, final String message, final String messageId,
			final long delayMilliSeconds) {
		if (StrUtil.isBlank(topic)) {
			throw new IllegalArgumentException("topic不能为空");
		}
		if (StrUtil.isBlank(message)) {
			throw new IllegalArgumentException("message不能为空");
		}
		if (StrUtil.isBlank(messageId)) {
			throw new IllegalArgumentException("messageId不能为空");
		}
		if (delayMilliSeconds < 0) {
			throw new IllegalArgumentException("delayMilliSeconds不能小于0");
		}
	}

}
