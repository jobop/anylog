package com.github.jobop.anylog.core.interactive.user.servlet;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import com.github.jobop.anylog.spi.TransformDescriptor;

public class OperateDescriptorServlet extends VelocityViewServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 设置返回视图为text/html编码为gbk
	@Override
	protected void setContentType(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf8");
	}

	// 处理请求
	@Override
	protected Template handleRequest(HttpServletRequest request, HttpServletResponse response, Context ctx) {
		String pid = request.getParameter("pid");
		
		String operateClassName = request.getParameter("operate");
		List<String> fieldNameList = new ArrayList<String>();
		Class<?> operateClass = null;
		try {
			operateClass = Class.forName(operateClassName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (null != operateClass) {
			Method[] methods = operateClass.getMethods();
			for (Method method : methods) {
				String methodName = method.getName();
				if (methodName.startsWith("get") && !methodName.equals("getClass")) {
					String fieldName = methodName.substring("get".length());
					String first = fieldName.substring(0, 1).toLowerCase();
					String rest = fieldName.substring(1, fieldName.length());
					String newStr = new StringBuffer(first).append(rest).toString();
					fieldNameList.add(newStr);
				}
			}
		}

		ctx.put("pid", pid);
		ctx.put("operateClassName", operateClassName);
		ctx.put("fieldNameList", fieldNameList);
		// 列出所有spi

		// 调用父类的方法getTemplate()
		return getTemplate("operatedescriptor.vm");
	}
}
