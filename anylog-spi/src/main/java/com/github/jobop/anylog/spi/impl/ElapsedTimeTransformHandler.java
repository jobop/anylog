package com.github.jobop.anylog.spi.impl;

import java.io.IOException;

import com.github.jobop.anylog.common.javassist.CannotCompileException;
import com.github.jobop.anylog.common.javassist.ClassPool;
import com.github.jobop.anylog.common.javassist.CtClass;
import com.github.jobop.anylog.common.javassist.CtMethod;
import com.github.jobop.anylog.common.javassist.NotFoundException;
import com.github.jobop.anylog.common.utils.ExceptionUtils;
import com.github.jobop.anylog.spi.TransformDescriptor;
import com.github.jobop.anylog.spi.TransformHandler;
import com.github.jobop.anylog.tools.MacroUtils;

public class ElapsedTimeTransformHandler implements TransformHandler {

	@Override
	public boolean canHandler(TransformDescriptor injectDescriptor) {
		return ElapsedTimeTransformDescriptor.class.isAssignableFrom(injectDescriptor.getClass());
	}

	@Override
	public byte[] transform(TransformDescriptor injectDescriptor) {
		ElapsedTimeTransformDescriptor descriptor = (ElapsedTimeTransformDescriptor) injectDescriptor;

		try {
			// 通过包名获取类文件
			CtClass cc = ClassPool.getDefault().get(descriptor.getNeedInjectClassName());
			cc.defrost();
			// 获得指定方法名的方法
			CtMethod m = cc.getDeclaredMethod(descriptor.getMethodName());
			String logName=descriptor.getNeedInjectClassName()+"."+descriptor.getMethodName();
			m.insertBefore(MacroUtils.safeInsertTemplate("com.github.jobop.anylog.common.profiler.Profiler.start(\""+logName+"\","+descriptor.getElaspeTime()+");"));
			m.insertAfter(MacroUtils.safeInsertTemplate("com.github.jobop.anylog.common.profiler.Profiler.stop(\""+logName+"\");"), true);
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
		}
		return null;
	}
}
