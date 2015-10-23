package com.github.jobop.anylog.core.instrumentation;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Set;

import com.github.jobop.anylog.common.utils.ExceptionUtils;
import com.github.jobop.anylog.common.utils.JavassistUtils;
import com.github.jobop.anylog.core.provider.TransformHandlerProvider;
import com.github.jobop.anylog.spi.TransformDescriptor;
import com.github.jobop.anylog.spi.TransformHandler;
import com.github.jobop.anylog.tools.MacroUtils;

public class AnyLogClassTransformer implements ClassFileTransformer {
	private TransformDescriptor injectDescriptor;
	private Set<String> transformedClassSet;

	public AnyLogClassTransformer(TransformDescriptor injectDescriptor, Set<String> transformedClassSet) {
		super();
		this.injectDescriptor = MacroUtils.translate(injectDescriptor);
		this.transformedClassSet = transformedClassSet;
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		// 如果加载Business类才拦截
		if (!injectDescriptor.getNeedInjectClassName().replace(".", "/").equals(className)) {

			return null;
		}
		byte[] resultByte = null;
		try {
			JavassistUtils.addJavassistClassPath(classBeingRedefined);

			TransformHandler injectHandler = TransformHandlerProvider.getProvider(injectDescriptor);
			System.out.println("###injectHandler=" + injectHandler.getClass().getName());
			transformedClassSet.add(injectDescriptor.getNeedInjectClassName());
			resultByte = injectHandler.transform(injectDescriptor);
		} catch (Throwable e) {
			e.printStackTrace();
			ExceptionUtils.addThrowable(e);
		} finally {
			JavassistUtils.cleanJavassistClassPath();
		}
		return resultByte;
	}

}
