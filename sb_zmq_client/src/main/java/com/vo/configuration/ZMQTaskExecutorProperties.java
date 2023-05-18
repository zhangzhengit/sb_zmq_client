package com.vo.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  
 *
 * @author zhangzhen
 * @data Aug 10, 2020
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "zmq.async.executor")
class ZMQTaskExecutorProperties {

	private int corePoolSize = 50;
	private int maxPoolSize = 50;
	private int queueCapacity = 55000;
	private int keepAliveTime = 60 * 60;
	private String threadNamePrefix = "zmq-client-Thread-";

}
