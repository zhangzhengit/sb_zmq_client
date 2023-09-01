package com.vo.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import com.vo.protobuf.ZMP;
import com.votool.ze.ZE;
import com.votool.ze.ZES;

import cn.hutool.core.collection.CollUtil;

/**
 *
 *
 * @author zhangzhen
 * @data Aug 7, 2020
 *
 */
public class ZMQMessageHandler {

	private static final ZE ZE = ZES.newZE("zmq-client-consumer-Thread-");

	public static void handle1(final ZMP zmp) {
		final String topic = zmp.getTopic();
		final Set<Method> methodSet = CCache1.getByTopic(topic);
		if (CollUtil.isEmpty(methodSet)) {
			return;
		}

		methodSet.forEach(method -> {

			final Object object = ZMC_C_C.getObjectByMethod(method);
			ZE.executeInQueue(() -> {
				try {
					method.invoke(object, zmp);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// FIXME 2023年9月1日 下午8:46:11 zhanghen: 通知server?
					System.err.println("invoke异常,method=" + method.getName() + "\t" + e);
					e.printStackTrace();
				}

			});
		});
	}

}
