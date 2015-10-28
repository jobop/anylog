package com.github.jobop.anylog.spi.impl;

import java.io.IOException;

import com.github.jobop.anylog.common.javassist.CannotCompileException;
import com.github.jobop.anylog.common.javassist.ClassPool;
import com.github.jobop.anylog.common.javassist.CtClass;
import com.github.jobop.anylog.common.javassist.CtMethod;
import com.github.jobop.anylog.common.javassist.NotFoundException;

import com.github.jobop.anylog.spi.TransformDescriptor;
import com.github.jobop.anylog.spi.TransformHandler;
import com.github.jobop.anylog.tools.MacroUtils;

public class ElapsedTimeLogTransformHandler implements TransformHandler {
	
	
	@Override
	public boolean canHandler(TransformDescriptor injectDescriptor) {
		if (null == injectDescriptor) {
			return false;
		}
		return ElapsedTimeLogTransformDescriptor.class.isAssignableFrom(injectDescriptor.getClass());
	}

	@Override
	public byte[] transform(TransformDescriptor injectDescriptor) {
		ElapsedTimeLogTransformDescriptor descriptor = (ElapsedTimeLogTransformDescriptor) injectDescriptor;
		System.out.println("###" + descriptor.getNeedInjectClassName());
		System.out.println("###" + descriptor.getMethodName());
		System.out.println("###" + descriptor.getLineNumStrWillSplitByComma());
		try {
			// 通过包名获取类文件
			CtClass cc = ClassPool.getDefault().get(descriptor.getNeedInjectClassName());
			cc.defrost();
			// 获得指定方法名的方法
			CtMethod m = cc.getDeclaredMethod(descriptor.getMethodName());
			m.insertBefore(MacroUtils.safeInsertTemplate("com.github.jobop.anylog.tools.TreadLocalHolder.setElapsedTime();"));
			if(descriptor.getLineNumStrWillSplitByComma() != null && !"".equals(descriptor.getLineNumStrWillSplitByComma())){
				for (Integer lineNum : descriptor.splitLineNum()) {
					m.insertAt(lineNum, MacroUtils.safeInsertTemplate("com.github.jobop.anylog.tools.TreadLocalHolder.setElapsedTime();"));
				}
			}
			m.insertAfter(MacroUtils.safeInsertTemplate("com.github.jobop.anylog.tools.TreadLocalHolder.showElapsedTime();"));
			return cc.toBytecode();
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	
}
