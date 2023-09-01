package com.vo.configuration;

import com.vo.anno.ZConfigurationProperties;
import com.vo.validator.ZNotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@ZConfigurationProperties(prefix = "zmq.server")
public class ZMQServerConfigurationProperties {

	@ZNotNull
	private String host;

	@ZNotNull
	private Integer port;

}
