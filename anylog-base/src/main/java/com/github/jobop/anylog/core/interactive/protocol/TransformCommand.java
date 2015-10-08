package com.github.jobop.anylog.core.interactive.protocol;

import com.github.jobop.anylog.spi.TransformDescriptor;

public class TransformCommand implements Command {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	TransformDescriptor transformDescriptor;

	@Override
	public int getCommandType() {
		return 1;
	}

	@Override
	public Object getCommandContext() {
		return transformDescriptor;
	}

	public void setTransformDescriptor(TransformDescriptor transformDescriptor) {
		this.transformDescriptor = transformDescriptor;
	}

	
}
