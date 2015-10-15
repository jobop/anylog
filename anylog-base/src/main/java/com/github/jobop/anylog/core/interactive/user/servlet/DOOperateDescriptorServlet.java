package com.github.jobop.anylog.core.interactive.user.servlet;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import com.github.jobop.anylog.core.interactive.protocol.TransformCommand;
import com.github.jobop.anylog.core.vm.VirtualMachineManager;
import com.github.jobop.anylog.spi.TransformDescriptor;
import com.github.jobop.anylog.spi.impl.LineLogTransformDescriptor;
import com.google.common.collect.Maps;

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
		String resultMsg = "";
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
						ctx.put("resultMsg", e.getMessage());
						return getTemplate("result.vm");
					}
				}
			}

			TransformCommand command = new TransformCommand();
			command.setTransformDescriptor((TransformDescriptor) obj);
			System.out.println("###pid=" + pid);
			System.out.println("");
			ResultEnum enumResult = transForm(VirtualMachineManager.getInstance().sendCommand(pid, command));
			result = enumResult.getResult();
			resultMsg = enumResult.getResultMsg();
			System.out.println("resultMsg:" + resultMsg);
		}
		ctx.put("result", result ? "success" : "fail");
		ctx.put("resultMsg", resultMsg);
		return getTemplate("result.vm");
	}

	private ResultEnum transForm(int resultInt) {
		System.out.println("resultInt: " + resultInt);
		if(0 == resultInt)
			return ResultEnum.RESULT_SUCCESS;
		else if(1 == resultInt)
			return ResultEnum.RESULT_SECURITY_FAIL;
		else
			return ResultEnum.RESULT_FAIL;
	}

	public static void main(String[] args) throws SecurityException, NoSuchFieldException {
		System.out.println(LineLogTransformDescriptor.class.getDeclaredField("lineNum").getType());
	}
	
	public enum ResultEnum{
		RESULT_SUCCESS(true,"aop successs"),
		RESULT_FAIL(false,"aop successs"),
		RESULT_SECURITY_FAIL(false,"security can't pass");
		
		private boolean result;
		private String resultMsg;
		
		private ResultEnum(boolean result,String resultMsg){
			this.result = result;
			this.resultMsg = resultMsg;
		}
		public boolean getResult() {
			return result;
		}

		public void setResult(boolean result) {
			this.result = result;
		}

		public String getResultMsg() {
			return resultMsg;
		}
		public void setResultMsg(String resultMsg) {
			this.resultMsg = resultMsg;
		}
	}
}
