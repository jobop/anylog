package com.github.jobop.anylog.core.interactive.serializer;

public class SerializerPair {
	private Serializer serializer;
	private Unserializer unserializer;

	public SerializerPair(Serializer serializer, Unserializer unserializer) {
		this.serializer = serializer;
		this.unserializer = unserializer;
	}

	public Serializer getSerializer() {
		return serializer;
	}

	public Unserializer getUnserializer() {
		return unserializer;
	}

}
