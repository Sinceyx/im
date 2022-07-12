package com.winter.autumn.serializer;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author HuangShk
 * @date 2022/3/11 11:55
 */

public interface Serializer {

	byte[] serialize(Object o);

	<T> T deserialize(Class<T> clazz,byte[] bytes);

	@Slf4j
	enum Algorithm implements Serializer {

		JAVA{
			@Override
			public byte[] serialize ( Object o ) {
				log.debug( "use JAVA serialize" );
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(baos);
					oos.writeObject(o);
					return baos.toByteArray();
				} catch ( IOException e ) {
					e.printStackTrace();
					throw new RuntimeException( "序列化异常", e );
				}
			}

			@Override
			public <T> T deserialize ( Class<T> clazz, byte[] bytes ) {
				log.debug( "use JAVA deserialize" );
				try {
					ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
					return (T) ois.readObject();
				} catch ( IOException|ClassNotFoundException e ) {
					e.printStackTrace();
					throw new RuntimeException( "序列化异常", e );
				}
			}
		},

		JSON {
			@Override
			public byte[] serialize ( Object o ) {

				log.debug( "use JSON serialize" );
				return new GsonBuilder().registerTypeAdapter( Class.class, new ClassCodec() ).create().toJson( o ).getBytes( StandardCharsets.UTF_8);
			}

			@Override
			public <T> T deserialize ( Class<T> clazz, byte[] bytes ) {
				log.debug( "use JSON deserialize" );

				return new GsonBuilder().registerTypeAdapter( Class.class, new ClassCodec() ).create().fromJson( new String( bytes ,StandardCharsets.UTF_8), clazz );
			}
		}
	}
	class ClassCodec implements JsonSerializer<Class<?>> , JsonDeserializer<Class<?>>{

		@Override
		public Class<?> deserialize ( JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext ) throws JsonParseException {
			try {
				return 	Class.forName( jsonElement.getAsString() );
			} catch ( ClassNotFoundException e ) {
				throw new JsonParseException( "反序列化失败", e );
			}
		}

		@Override
		public JsonElement serialize ( Class<?> aClass, Type type, JsonSerializationContext jsonSerializationContext ) {
			return new JsonPrimitive( aClass.getName() );
		}
	}
 }
