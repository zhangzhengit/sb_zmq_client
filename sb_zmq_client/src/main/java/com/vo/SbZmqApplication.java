package com.vo;

import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vo.core.ZLog2;
import com.vo.core.ZMQComponent;
import com.vo.scanner.ClassMap;
import com.vo.socket.Connection;

/**
 *
 *
 * @author zhangzhen
 * @data Aug 7, 2020
 *
 */
//@SpringBootApplication
public class SbZmqApplication {

	private static final ZLog2 LOG = ZLog2.getInstance();

	public static void main(final String[] args) {
//		SpringApplication.run(SbZmqApplication.class, args);

		final String scanPackageName = "com.vo";

//		ZApplication.run(scanPackageName, args);

		scanZMQComponent(scanPackageName);

		final Connection connection = new Connection();
		connection.connect();
	}

	public static void scanZMQComponent(final String scanPackageName) {

		LOG.info("开始扫描带有{}的类", ZMQComponent.class.getSimpleName());
		final Set<Class<?>> csSet = ClassMap.scanPackageByAnnotation(scanPackageName, ZMQComponent.class);
		LOG.info("扫描到带有{}的类公有{}个", ZMQComponent.class.getSimpleName(),csSet.size());
		for (final Class<?> class1 : csSet) {

		}
	}
}
