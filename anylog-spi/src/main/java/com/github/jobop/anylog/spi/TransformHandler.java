package com.github.jobop.anylog.spi;

public interface TransformHandler {

	public boolean canHandler(TransformDescriptor injectDescriptor);

	public byte[] transform(TransformDescriptor injectDescriptor);
}
