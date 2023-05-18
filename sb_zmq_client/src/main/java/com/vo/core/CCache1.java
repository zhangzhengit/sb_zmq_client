package com.vo.core;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multiset;
import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 *
 * @author zhangzhen
 * @data Aug 7, 2020
 * 
 */
public class CCache1 {

	private final static HashMultimap<String, Method> map = HashMultimap.create();
	
	public synchronized static Set<Entry<String, Method>> entries() {
		return map.entries();
	}
	
	public synchronized static Multiset<String> topics() {
		return map.keys();
	}

	public synchronized static void put(final String topic, final Method method) {
		map.put(topic, method);
	}

	public synchronized static Set<Method> getByTopic(final String topic) {
		return map.get(topic);
	}

}
