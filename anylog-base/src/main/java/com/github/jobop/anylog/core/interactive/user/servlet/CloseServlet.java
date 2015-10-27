package com.github.jobop.anylog.core.interactive.user.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.tools.view.VelocityViewServlet;

import com.github.jobop.anylog.core.vm.VirtualMachineManager;

public class CloseServlet extends VelocityViewServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 设置返回视图为text/html编码为gbk
	@Override
	protected void setContentType(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf8");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//
		String pid = request.getParameter("pid");
		VirtualMachineManager.getInstance().disConnected(pid);
		// // 输出所有vm
		// List<VirtualMachineWrapper> wrapper =
		// VirtualMachineManager.getInstance().listVMs();
		// ctx.put("wrappers", wrapper);
		// // 调用父类的方法getTemplate()
		// return getTemplate("menu.vm");

		try {
			response.sendRedirect("/menu.html");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
