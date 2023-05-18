package com.vo.core;

import cn.hutool.core.collection.CollUtil;
import com.vo.protobuf.ZMP;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 
 *
 * @author zhangzhen
 * @data Aug 7, 2020
 * 
 */
@Component
@SuppressWarnings("static-method")
public class ZMMessageHandler {

	@Async
	public void handleAsync(final ZMP zmp) {
		handle1(zmp);
	}

	private void handle1(final ZMP zmp) {
		final String topic = zmp.getTopic();
		final Set<Method> methodSet = CCache1.getByTopic(topic);
		if (CollUtil.isEmpty(methodSet)) {
			return;
		}

		methodSet.parallelStream().forEach(method -> {

			final Object object = ZMC_C_C.getObjectByMethod(method);
			try {
				method.invoke(object, zmp);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				System.err.println("invoke异常,method=" + method.getName() + "\t" + e);
			}
		});
	}

}
