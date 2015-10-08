package com.github.jobop.anylog.core.interactive.serializer.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.github.jobop.anylog.core.interactive.serializer.Serializer;

public class JavaObjectSerializer implements Serializer {

	public byte[] serialize(Object obj) {
		byte[] resultByte = null;
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			baos.flush();
			oos.flush();
			resultByte = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (IOException e1) {
			}

			try {
				baos.close();
			} catch (IOException e) {
			}

		}
		return resultByte;
	}
}
