package com.winter.autumn.conf;

import com.winter.autumn.serializer.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author HuangShk
 * @date 2022/3/11 11:45
 */
public class Config {

	private final static Properties properties;

	static {
		properties = new Properties();
		InputStream in = Config.class.getResourceAsStream( "/application.properties" );
		try {
			properties.load( in );
		} catch ( IOException e ) {
			throw new ExceptionInInitializerError();
		}
	}

	public static Serializer.Algorithm getAlgorithm() {
		String algorithm = properties.getProperty( "serialize.algorithm", "JAVA" );
		return Serializer.Algorithm.valueOf( algorithm );
	}

}