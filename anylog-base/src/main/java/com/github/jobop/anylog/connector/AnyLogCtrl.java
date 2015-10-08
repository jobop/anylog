package com.github.jobop.anylog.connector;

import com.github.jobop.anylog.core.interactive.user.WebUserServer;

public class AnyLogCtrl {

	public static void main(String[] args) throws InterruptedException {
		// 启动http服务
		WebUserServer server = new WebUserServer(Integer.valueOf(args[0]));
		server.startup();
	}

}
