package com.github.jobop.anylog.spi.impl;

import com.github.jobop.anylog.common.javassist.*;
import com.github.jobop.anylog.spi.TransformDescriptor;
import com.github.jobop.anylog.spi.TransformHandler;
import com.github.jobop.anylog.tools.MacroUtils;

import java.io.IOException;

public class MethodTimeTransformHandler implements TransformHandler {

	@Override
	public boolean canHandler(TransformDescriptor injectDescriptor) {
		if (null == injectDescriptor) {
			return false;
		}
		return MethodTimeTransformDescriptor.class.isAssignableFrom(injectDescriptor.getClass());
	}

	@Override
	public byte[] transform(TransformDescriptor injectDescriptor) {
        MethodTimeTransformDescriptor descriptor = (MethodTimeTransformDescriptor) injectDescriptor;
        String clzName = descriptor.getNeedInjectClassName();
        String methodName = descriptor.getMethodName();
		System.out.println("###" + clzName);
		System.out.println("###" + methodName);
		try {
			// 通过包名获取类文件
			CtClass ctClass = ClassPool.getDefault().get(descriptor.getNeedInjectClassName());
			ctClass.defrost();
            CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);

            StringBuffer sb = new StringBuffer();
            sb.append("\"" + clzName + ":" + methodName + "#" + "\"");
            sb.append("+");
            sb.append("Thread.currentThread().getName()");

            String key = sb.toString();

            if(descriptor.getLogParamsValue().equalsIgnoreCase("y")) {
            	// 记录方法入参
            	ctMethod.insertBefore(MacroUtils.safeInsertTemplate("com.github.jobop.anylog.tools.Trace.trace(" + key + " + \" params=\"  + com.github.jobop.anylog.common.fastjson.JSON.objsToJSONString($args));"));
            }
            // 记录起始时间
            ctMethod.insertBefore("com.github.jobop.anylog.tools.TimeRecorder.start(" + key + ");");
                        
            // 记录结束时间
            ctMethod.insertAfter(MacroUtils.safeInsertTemplate("com.github.jobop.anylog.tools.Trace.trace(" + key + " + \" execute time=\"  + com.github.jobop.anylog.tools.TimeRecorder.end(" + key + ") + \"ms\");"));

            if(descriptor.getLogReturnValue().equalsIgnoreCase("y")) {
            	// 记录方法返回值
            	ctMethod.insertAfter(MacroUtils.safeInsertTemplate("com.github.jobop.anylog.tools.Trace.trace(" + key + " + \" return value=\"  + com.github.jobop.anylog.common.fastjson.JSON.toJSONString($_));"));
            }
            
            return ctClass.toBytecode();
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

    public static void main(String[] args) {

    }


}
