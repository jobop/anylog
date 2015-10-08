package com.github.jobop.anylog.core.interactive.protocol;

import java.io.Serializable;

public interface Command extends Serializable {
	public int getCommandType();

	public Object getCommandContext();
}
