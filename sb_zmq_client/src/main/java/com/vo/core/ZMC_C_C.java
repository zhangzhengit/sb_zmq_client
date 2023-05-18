package com.vo.core;

import com.google.common.collect.Maps;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;

/**
 * 
 *
 * @author zhangzhen
 * @data Aug 10, 2020
 * 
 */
public class ZMC_C_C {

	/**
	 * @ZMC的方法，此方法在的对象
	 */
	static ConcurrentMap<Method, Object> cc = Maps.newConcurrentMap();

	public static Object getObjectByMethod(final Method method) {
		return cc.get(method);
	}
	
	public static void put(final Method method,final Object object) {
		cc.put(method, object);
	}
}
