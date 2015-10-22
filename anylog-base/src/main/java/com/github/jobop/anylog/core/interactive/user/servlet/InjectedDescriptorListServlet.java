package com.github.jobop.anylog.core.interactive.user.servlet;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import com.github.jobop.anylog.core.vm.VirtualMachineManager;
import com.github.jobop.anylog.spi.TransformDescriptor;

public class InjectedDescriptorListServlet extends VelocityViewServlet {
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
		List<TransformDescriptor> descs = VirtualMachineManager.getInstance().listTransformDescriptor(pid);

		List<ClassWrapper> cws = new ArrayList<ClassWrapper>();
		for (TransformDescriptor desc : descs) {
			ClassWrapper cw = new ClassWrapper();
			List<FieldWrapper> fieldList = new ArrayList<FieldWrapper>();
			Class<?> operateClass = desc.getClass();
			if (null != operateClass) {
				Method[] methods = operateClass.getMethods();
				for (Method method : methods) {
					String methodName = method.getName();
					if (methodName.startsWith("get") && !methodName.equals("getClass")) {
						String fieldName = methodName.substring("get".length());
						String first = fieldName.substring(0, 1).toLowerCase();
						String rest = fieldName.substring(1, fieldName.length());
						String newStr = new StringBuffer(first).append(rest).toString();
						FieldWrapper wrapper = new FieldWrapper();
						wrapper.setFieldName(newStr);
						try {
							wrapper.setValue(method.invoke(desc, new Object[] {}));
						} catch (Exception e) {
							e.printStackTrace();
						}
						fieldList.add(wrapper);
					}
				}
			}
			cw.setClassName(operateClass.getName());
			cw.setFieldWrappers(fieldList);
			cws.add(cw);
		}

		ctx.put("cws", cws);
		ctx.put("pid", pid);

		// 调用父类的方法getTemplate()
		return getTemplate("injectedDescriptorList.vm");
	}
}
