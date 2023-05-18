package com.vo.configuration;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 
 *
 * @author zhangzhen
 * @data Aug 7, 2020
 * 
 */
@Configuration
@ComponentScan(basePackages = { "com.vo" })
public class ZMQConfiguration {

	@Autowired
	private ZMQTaskExecutorProperties zmqTaskExecutorProperties;

	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {

		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(zmqTaskExecutorProperties.getCorePoolSize());
		executor.setMaxPoolSize(zmqTaskExecutorProperties.getMaxPoolSize());
		executor.setQueueCapacity(zmqTaskExecutorProperties.getQueueCapacity());
		executor.setKeepAliveSeconds(zmqTaskExecutorProperties.getKeepAliveTime());
		executor.setThreadNamePrefix(zmqTaskExecutorProperties.getThreadNamePrefix());
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}

}
