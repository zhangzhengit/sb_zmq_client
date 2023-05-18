package com.vo.protobuf;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 *
 * @author zhangzhen
 * @data Aug 7, 2020
 * 
 */
@Getter
@AllArgsConstructor
public enum ZMPTypeEnum {

	INIT(1, "init"),

	MESSAGE(2, "message"),
	
	;

	private int type;
	private String desc;
}
