package com.vo.core;

import cn.hutool.core.lang.UUID;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 
 *
 * @author zhangzhen
 * @data Aug 7, 2020
 * 
 */
public class ZIDGenerator {

	public static String generateId_UUID() {
		// TODO 太长
		return UUID.randomUUID().toString();
	}

	private static final AtomicLong s = new AtomicLong(0L);

	public static String generateId_TIME() {
		// time long值
		// TODO 改用snowflake
		final Long v = Long.valueOf(System.nanoTime() * System.nanoTime() + s.incrementAndGet());
		return String.valueOf(v);
	}
	
//	public static void main(final String[] args) {
////		test_1();
//		test_v2();
//	}
//	
//	public static void test_v2() {
//		System.out.println(
//				Thread.currentThread().getName() + "\t" + LocalDateTime.now() + "\t" + "ZIDGenerator.test_v2()");
//		System.out.println();
//		
//		final int n = 10000 * 20;
//		for (int i = 1; i <= n; i++) {
//			final String generateId_TIME = generateId_TIME();
//			System.out.println(generateId_TIME);
//			System.out.println(Long.parseLong(generateId_TIME));
//		}
//
//
//	}
//	
//	public static void test_1() {
//		System.out.println(
//				Thread.currentThread().getName() + "\t" + LocalDateTime.now() + "\t" + "ZIDGenerator.test_1()");
//		System.out.println();
//		
//		final int n = 10000 * 50;
//		// ps
//		final AtomicLong s = new AtomicLong(0L);
//		final AtomicLong s2 = new AtomicLong(Long.MAX_VALUE);
//
//		final long diyiciTime = System.nanoTime();
//		final long t1 = System.currentTimeMillis();
//		final Set<Long> idSet = IntStream.range(1, n + 1)
//			.parallel()
//			.mapToObj(x -> {
//				final long time = System.nanoTime();
////				final long time = getTime(diyiciTime);
////				return Long.valueOf(time + s.incrementAndGet() * System.nanoTime());
//				return Long.valueOf(System.nanoTime() * System.nanoTime() + s.incrementAndGet());
//			}).collect(Collectors.toSet());
//		
//		final long t2 = System.currentTimeMillis();
//		System.out.println("idSet.size = " + idSet.size());
//		System.out.println("n = " + n + "\t" + "ms = " + (t2 - t1));
//		
//		
//		// 1
////		final Set<Long> set = Sets.newHashSet();
////		for (int i = 1; i <= n; i++) {
////			final long nanoTime = System.nanoTime();
////			set.add(nanoTime);
////		}
////
////		System.out.println("set.size() = " + set.size());
////		for (final Long id : set) {
////			System.out.println(id);
////		}
////		System.out.println("set.size() = " + set.size());
//
//	}
//
//	private static long getTime(final long shangciTime) {
//		final long bencitime = System.nanoTime();
//		if(shangciTime != bencitime) {
//			return System.nanoTime();
//		}
//		return bencitime;
//	}
}

