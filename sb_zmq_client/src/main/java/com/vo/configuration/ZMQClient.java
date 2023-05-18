package com.vo.configuration;

import com.vo.core.ZIDGenerator;
import com.vo.netty.ZMQClientHandler;
import com.vo.protobuf.ZMP;
import com.vo.protobuf.ZMPTypeEnum;

import cn.hutool.core.util.StrUtil;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * 用于发送消息
 *
 * @author zhangzhen
 * @data Aug 10, 2020
 *
 */
@Component
public final class ZMQClient {

	@Autowired
	private ZMQClientHandler zmClientHandler;

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

		this.zmClientHandler.writeAndFlush(zmp);
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
