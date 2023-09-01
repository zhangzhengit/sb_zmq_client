package com.vo.configuration;

import cn.hutool.core.util.ArrayUtil;
import com.vo.annotation.ZMQConsumer;
import com.vo.core.CCache1;
import com.vo.core.ZMC_C_C;
import com.vo.core.ZMQComponent;
import com.vo.protobuf.ZMP;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 *
 * @author zhangzhen
 * @data Aug 7, 2020
 *
 */
public class ZMQApplicationContextAware {

	public static void pc(final Object c) {

		final Class<? extends Object> cls = c.getClass();

		System.out.println("----------getDeclaredMethods-----------");
		final Method[] ms = cls.getDeclaredMethods();
		for (final Method method : ms) {
			final ZMQConsumer[] zA = method.getDeclaredAnnotationsByType(ZMQConsumer.class);
			for (final ZMQConsumer za1 : zA) {
				final String topic = za1.topic();
				System.out.println("cls = " + cls.getSimpleName() + "\t" + method.getName() + "\t" + topic);

				final Parameter[] parameters = method.getParameters();
				if (ArrayUtil.isEmpty(parameters) || parameters.length != 1 || parameters[0].getType() != ZMP.class) {
					throw new IllegalArgumentException(ZMQConsumer.class.getCanonicalName() + " 方法必须有且只有一个ZMP 参数");
				}

				CCache1.put(topic, method);
				ZMC_C_C.put(method, c);
			}
		}
	}

}
