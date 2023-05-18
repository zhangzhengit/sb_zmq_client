package com.vo.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 *
 * @author zhangzhen
 * @data Aug 7, 2020
 * 
 */
public class ZMExecutorService {

	private final static ExecutorService ES = Executors.newFixedThreadPool(50);

	public static void execute(final Runnable command) {
		ES.execute(command);
	}

}
