package com.github.jobop.anylog.spi;

import java.util.ArrayList;

public class Errors extends ArrayList<String> {
	private static final long serialVersionUID = 1L;

	@Override
	public boolean add(String e) {
		return super.add(e);
	}

	public String dumpAllErrors() {
		StringBuilder sb = new StringBuilder();
		while (this.iterator().hasNext()) {
			sb.append(this.iterator().next()).append(";");
		}
		return sb.toString();
	}
}
