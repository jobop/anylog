package com.github.jobop.anylog.core.interactive.serializer.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.github.jobop.anylog.core.interactive.serializer.Unserializer;

public class JavaObjectUnserializer implements Unserializer {

	public Object unserialize(byte[] b) {
		Object resultObj = null;
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			bais = new ByteArrayInputStream(b);
			ois = new ObjectInputStream(bais);
			resultObj = ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				ois.close();
			} catch (IOException e) {
			}

			try {
				bais.close();
			} catch (IOException e) {
			}

		}
		return resultObj;
	}

}
