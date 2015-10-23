package com.github.jobop.anylog.spi.impl;

import java.io.IOException;

import com.github.jobop.anylog.common.javassist.CannotCompileException;
import com.github.jobop.anylog.common.javassist.ClassPool;
import com.github.jobop.anylog.common.javassist.CtClass;
import com.github.jobop.anylog.common.javassist.CtConstructor;
import com.github.jobop.anylog.common.javassist.NotFoundException;
import com.github.jobop.anylog.common.utils.ExceptionUtils;
import com.github.jobop.anylog.spi.TransformDescriptor;
import com.github.jobop.anylog.spi.TransformHandler;
import com.github.jobop.anylog.tools.MacroUtils;

public class TraceExceptionTransformHandler implements TransformHandler {

	@Override
	public boolean canHandler(TransformDescriptor injectDescriptor) {
		if (null == injectDescriptor) {
			return false;
		}
		boolean canHandler = true;
		if (!TraceExceptionTransformDescriptor.class.isAssignableFrom(injectDescriptor.getClass())) {
			canHandler = false;
		}
		String clazzName = injectDescriptor.getNeedInjectClassName();
		Class<?> clazz = null;
		try {
			clazz = Class.forName(clazzName);
		} catch (ClassNotFoundException e) {
			canHandler = false;
		}
		if (null != clazz && !Throwable.class.isAssignableFrom(clazz)) {
			canHandler = false;
		}
		return canHandler;
	}

	@Override
	public byte[] transform(TransformDescriptor injectDescriptor) {
		TraceExceptionTransformDescriptor descriptor = (TraceExceptionTransformDescriptor) injectDescriptor;

		try {
			// 通过包名获取类文件
			CtClass cc = ClassPool.getDefault().get(descriptor.getNeedInjectClassName());
			// 获得无参构造方法
			CtConstructor[] constructors = cc.getConstructors();
			for (CtConstructor constructor : constructors) {
				constructor.insertAfter(MacroUtils.safeInsertTemplate("printStackTrace(com.github.jobop.anylog.tools.Trace.out);"));
			}
			return cc.toBytecode();
		} catch (NotFoundException e) {
			ExceptionUtils.addThrowable(e);
			e.printStackTrace();
		} catch (CannotCompileException e) {
			ExceptionUtils.addThrowable(e);
			e.printStackTrace();
		} catch (IOException e) {
			ExceptionUtils.addThrowable(e);
			e.printStackTrace();
			// 忽略异常处理
		}
		return null;

	}

}
