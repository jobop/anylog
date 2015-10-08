package com.github.jobop.anylog.spi.impl;

import com.github.jobop.anylog.spi.TransformDescriptor;
import com.github.jobop.anylog.spi.TransformHandler;

public class NoTransformHandler implements TransformHandler {

	@Override
	public boolean canHandler(TransformDescriptor injectDescriptor) {
		return injectDescriptor == null;
	}

	@Override
	public byte[] transform(TransformDescriptor injectDescriptor) {
		return null;
	}

}
