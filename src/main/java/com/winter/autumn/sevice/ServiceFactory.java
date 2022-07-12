package com.winter.autumn.sevice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author HuangShk
 * @date 2022/5/11 9:54
 */
public class ServiceFactory {

	private static final Map<String, Object> SERVICE_CACHE = new ConcurrentHashMap<>();

	static {
		try {
			InputStream resource = ServiceFactory.class.getResourceAsStream( "/application.properties" );
			Properties properties = new Properties();
			properties.load( resource );
			Iterator<String> iterator = properties.stringPropertyNames().iterator();
			while ( iterator.hasNext() ) {
				String next = iterator.next();
				if ( next.endsWith( "Service" ) ) {
					SERVICE_CACHE.put( next, Class.forName( properties.getProperty( next ) ).getDeclaredConstructor().newInstance() );
				}
			}
		} catch ( IOException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e ) {
			e.printStackTrace();
		}
	}

	public static Object  getService(String serviceName){
		return SERVICE_CACHE.get( serviceName );
	}

}
