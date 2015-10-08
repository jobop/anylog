package com.github.jobop.anylog.core.provider;

import java.util.ServiceLoader;

import com.github.jobop.anylog.core.exception.NoTransformHandlerFoundException;
import com.github.jobop.anylog.spi.TransformDescriptor;
import com.github.jobop.anylog.spi.TransformHandler;

public class TransformHandlerProvider {
	// private static Map<InjectHandler> providerCache
	private static ServiceLoader<TransformHandler> serviceLoader = ServiceLoader.load(TransformHandler.class);

	public static TransformHandler getProvider(TransformDescriptor injectDescriptor) {
		try {
			for (TransformHandler handler : serviceLoader) {
				try {
					if (handler.canHandler(injectDescriptor)) {
						return handler;
					}
				} catch (Exception e) {
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.out.println("no TransformHandler Found ,please check the META-INF/services");
		throw new NoTransformHandlerFoundException("no TransformHandler Found ,please check the META-INF/services");
	}

	public static void main(String[] args) {

		// System.out.println(InjectHandlerProvider.getProvider(null).getClass().getName());

		ServiceLoader<TransformHandler> serviceLoader = ServiceLoader.load(TransformHandler.class);

		for (TransformHandler provider : serviceLoader) {
			System.out.println(provider.getClass().getName());
		}
	}
}
