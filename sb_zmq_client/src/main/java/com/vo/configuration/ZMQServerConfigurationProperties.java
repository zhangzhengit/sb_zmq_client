package com.vo.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 
 *
 * @author zhangzhen
 * @data Aug 7, 2020
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "zmq")
public class ZMQServerConfigurationProperties {

	private String host = "localhost";

	private int port = 9591;

}
