package com.github.jobop.anylog.core.interactive.protocol;

public class CloseCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getCommandType() {
		return 2;
	}

	@Override
	public Object getCommandContext() {
		return null;
	}

}
