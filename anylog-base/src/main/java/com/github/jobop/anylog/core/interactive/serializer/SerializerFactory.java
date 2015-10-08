package com.github.jobop.anylog.core.interactive.serializer;

import java.util.HashMap;
import java.util.Map;

import com.github.jobop.anylog.core.interactive.serializer.impl.JavaObjectSerializer;
import com.github.jobop.anylog.core.interactive.serializer.impl.JavaObjectUnserializer;

/**
 * 
 * @author zhengw
 * 
 */
public class SerializerFactory {

	private static String DEFAULT_SERIALIZER_KEY = "javaProtocol";
	private static SerializerFactory factory = null;
	private static Map<String, SerializerPair> serializerMap = new HashMap<String, SerializerPair>();
	static {
		serializerMap.put(DEFAULT_SERIALIZER_KEY, new SerializerPair(new JavaObjectSerializer(), new JavaObjectUnserializer()));
	}

	private SerializerFactory() {

	}

	public final static SerializerFactory getInstance() {
		if (null == factory) {
			synchronized (SerializerFactory.class) {
				if (null == factory) {
					factory = new SerializerFactory();
				}
			}
		}
		return factory;
	}

	public SerializerPair getSerializerPair(String serializerKey) {
		return (SerializerPair) serializerMap.get(serializerKey);
	}

	public SerializerPair getDefaultSerializerPair() {
		return getSerializerPair(DEFAULT_SERIALIZER_KEY);
	}
}
