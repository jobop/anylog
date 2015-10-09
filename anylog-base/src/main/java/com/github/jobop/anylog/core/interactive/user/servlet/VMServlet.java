package com.github.jobop.anylog.core.interactive.user.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import com.github.jobop.anylog.spi.TransformDescriptor;
import com.github.jobop.anylog.spi.annotations.SpiDesc;

public class VMServlet extends VelocityViewServlet {
	private static ServiceLoader<TransformDescriptor> serviceLoader = ServiceLoader.load(TransformDescriptor.class);
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

		List<ClassWrapper> descriptors = new ArrayList<ClassWrapper>();
		for (TransformDescriptor descriptor : serviceLoader) {
			ClassWrapper wrapper = new ClassWrapper();
			wrapper.setClassName(descriptor.getClass().getName());
			SpiDesc descAnnotation = descriptor.getClass().getAnnotation(SpiDesc.class);
			if (null != descAnnotation) {
				wrapper.setClassDesc(descAnnotation.desc());
			} else {
				wrapper.setClassDesc("该功能无描述");
			}
			descriptors.add(wrapper);
		}
		ctx.put("descriptors", descriptors);
		ctx.put("pid", pid);
		// 列出所有spi

		// 调用父类的方法getTemplate()
		return getTemplate("vm.vm");
	}
}
