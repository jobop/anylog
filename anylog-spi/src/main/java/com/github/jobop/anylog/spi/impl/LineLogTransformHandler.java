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

public class LineLogTransformHandler implements TransformHandler {

	@Override
	public boolean canHandler(TransformDescriptor injectDescriptor) {
		if (null == injectDescriptor) {
			return false;
		}
		return LineLogTransformDescriptor.class.isAssignableFrom(injectDescriptor.getClass());
	}

	@Override
	public byte[] transform(TransformDescriptor injectDescriptor) {
		LineLogTransformDescriptor descriptor = (LineLogTransformDescriptor) injectDescriptor;
		try {
			// 通过包名获取类文件
			CtClass cc = ClassPool.getDefault().get(descriptor.getNeedInjectClassName());
			cc.defrost();
			// 获得指定方法名的方法
			CtMethod m = cc.getDeclaredMethod(descriptor.getMethodName());
			m.insertAt(Integer.valueOf(descriptor.getLineNum()), MacroUtils.safeInsertTemplate(descriptor.getLineCode() + ";"));
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
