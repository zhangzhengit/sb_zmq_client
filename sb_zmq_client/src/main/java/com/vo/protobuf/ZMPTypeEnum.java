package com.vo.protobuf;

import java.util.HashMap;

import com.google.common.collect.Maps;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 *
 * @author zhangzhen
 * @data Aug 7, 2020
 *
 */
@Getter
@AllArgsConstructor
public enum ZMPTypeEnum {

	INIT(1, "初始化消费者topic到服务端"),

	INIT_OK(11, "服务端回应 初始化消息 初始化OK"),

	MESSAGE(2, "message"),

	;

	static HashMap<Integer, ZMPTypeEnum> newHashMap = Maps.newHashMap();

	static {
		final ZMPTypeEnum[] vs = values();
		for (final ZMPTypeEnum zmpTypeEnum : vs) {
			newHashMap.put(zmpTypeEnum.getType(), zmpTypeEnum);

		}
	}

	public static ZMPTypeEnum valueOfType(final Integer type) {
		return newHashMap.get(type);
	}

	private Integer type;
	private String desc;
}
