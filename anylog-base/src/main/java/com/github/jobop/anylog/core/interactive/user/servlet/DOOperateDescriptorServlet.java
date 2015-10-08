package com.github.jobop.anylog.core.interactive.user.servlet;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import com.github.jobop.anylog.core.interactive.protocol.TransformCommand;
import com.github.jobop.anylog.core.vm.VirtualMachineManager;
import com.github.jobop.anylog.spi.TransformDescriptor;
import com.github.jobop.anylog.spi.impl.LineLogTransformDescriptor;

public class DOOperateDescriptorServlet extends VelocityViewServlet {
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

		boolean result = false;
		String pid = request.getParameter("pid");
		VirtualMachineManager.getInstance().connected(pid, "");
		String operateClassName = request.getParameter("operateClassName");
		Class<?> operateClass = null;
		try {
			operateClass = Class.forName(operateClassName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (null != operateClass) {
			Object obj = null;
			try {
				obj = operateClass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Method[] methods = operateClass.getMethods();
			for (Method method : methods) {
				String methodName = method.getName();
				if (methodName.startsWith("set")) {
					String fieldName = methodName.substring("set".length());
					String first = fieldName.substring(0, 1).toLowerCase();
					String rest = fieldName.substring(1, fieldName.length());
					fieldName = new StringBuffer(first).append(rest).toString();
					try {
						System.out.println("fieldName=" + fieldName);

						method.invoke(obj, new Object[] { request.getParameter(fieldName) });
					} catch (Exception e) {
						e.printStackTrace();
						ctx.put("result", result ? "success" : "fail");
						return getTemplate("result.vm");
					}
				}
			}

			TransformCommand command = new TransformCommand();
			command.setTransformDescriptor((TransformDescriptor) obj);
			System.out.println("###pid=" + pid);
			System.out.println("");
			result = VirtualMachineManager.getInstance().sendCommand(pid, command);
		}
		ctx.put("result", result ? "success" : "fail");
		return getTemplate("result.vm");
	}

	public static void main(String[] args) throws SecurityException, NoSuchFieldException {
		System.out.println(LineLogTransformDescriptor.class.getDeclaredField("lineNum").getType());
	}
}
