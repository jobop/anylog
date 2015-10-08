package com.github.jobop.anylog.core.interactive.user;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import com.github.jobop.anylog.common.constans.Constans;

/**
 * 内嵌jetty用于发布用户界面，这里頁面只是一种交互方式,而不是依赖于jetty作为容器。
 * 
 * @author nevynzheng
 * 
 */
public class WebUserServer implements UserServer {
	private Server server;

	// 启动服务，监听来自客户端的请求

	public WebUserServer(int port) {

		String rootPath = new File("").getAbsolutePath();
		String sysSep = Constans.SEPARATOR;

		System.setProperty("user.timezone", "Asia/Shanghai");
		System.setProperty("org.eclipse.jetty.util.URI.charset", "GBK");
		System.setProperty("env", "local");
		System.out.println("###rootPath=" + rootPath);

		server = new Server();

		Connector connector = new SelectChannelConnector();
		connector.setPort(port);
		server.addConnector(connector);

		WebAppContext webapp = new WebAppContext();
		webapp.setDescriptor(rootPath + sysSep + "web" + sysSep + "web.xml");
		List<String> overrideDescriptors = new ArrayList<String>();
		overrideDescriptors.add(rootPath + sysSep + "web" + sysSep + "web.xml");
		webapp.setOverrideDescriptors(overrideDescriptors);

		webapp.setContextPath("/");
		webapp.setResourceBase(rootPath + sysSep + "web");

		server.setHandler(webapp);

	}

	public void startup() {
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {

	}

}
