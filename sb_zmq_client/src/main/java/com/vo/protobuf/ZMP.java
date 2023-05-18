package com.vo.protobuf;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZMP {

	/**
	 * 唯一ID
	 */
	private String id;

	/**
	 * ZMPTypeEnum.type
	 */
	private int type;

	private String topic;

	private String content;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 延迟毫秒数，0表示不延迟
	 */
	private long delayMilliSeconds;

}
