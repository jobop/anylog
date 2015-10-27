package com.github.jobop.anylog.core.interactive.system;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.github.jobop.anylog.common.utils.ExceptionUtils;
import com.github.jobop.anylog.core.instrumentation.AnyLogClassTransformer;
import com.github.jobop.anylog.core.instrumentation.RestoreClassTransformer;
import com.github.jobop.anylog.core.interactive.protocol.Command;
import com.github.jobop.anylog.core.interactive.protocol.CommandRet;
import com.github.jobop.anylog.core.interactive.serializer.Serializer;
import com.github.jobop.anylog.core.interactive.serializer.SerializerFactory;
import com.github.jobop.anylog.core.interactive.serializer.Unserializer;
import com.github.jobop.anylog.core.utils.ClassUtils;
import com.github.jobop.anylog.spi.TransformDescriptor;

public class VMSocketServer {
	private Unserializer unserializer = SerializerFactory.getInstance().getDefaultSerializerPair().getUnserializer();
	private Serializer serializer = SerializerFactory.getInstance().getDefaultSerializerPair().getSerializer();
	private ServerSocket serverSocket = null;
	private boolean started = false;
	private Thread handleThread = null;
	private Instrumentation inst = null;
	private Set<String> transformedClassSet = new HashSet<String>();

	public VMSocketServer(int port, Instrumentation inst) {
		try {
			serverSocket = new ServerSocket(port);
			this.inst = inst;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void start() {
		if (!started) {
			started = true;
			handleThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (started) {
						try {
							Socket socket = serverSocket.accept();
							receive(socket);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			handleThread.setDaemon(true);
			handleThread.start();
		}

	}

	public synchronized void shutdown() {
		if (started) {
			started = false;
			if (null != serverSocket) {
				try {
					serverSocket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				serverSocket = null;
			}
		}
	}

	public void receive(Socket socket) {
		CommandRet ret = new CommandRet();
		DataInputStream input = null;
		DataOutputStream output = null;
		try {
			// 这里启动一个异常记录器，避免传参
			ExceptionUtils.enable();
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			byte[] commandByte = new byte[1024];
			int lenth = input.read(commandByte);
			Command command = (Command) unserializer.unserialize(Arrays.copyOfRange(commandByte, 0, lenth));
			handlerCommand(command);

			ret.setRetCode(ExceptionUtils.hasExceptions() ? 1 : 0);
			ret.setRetMsg(ExceptionUtils.dumpMsg());

			output.write(serializer.serialize(ret));
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ExceptionUtils.disable();
			try {
				output.close();
			} catch (Exception e) {
			}
			try {
				input.close();
			} catch (Exception e) {
			}
			try {
				socket.close();
			} catch (Exception e) {
			}
		}

	}

	public int handlerCommand(Command command) throws UnmodifiableClassException {
		try {
			int type = command.getCommandType();
			if (type == 1) {
				TransformDescriptor transformDescriptor = (TransformDescriptor) command.getCommandContext();
				doTransform(transformDescriptor);
			} else if (type == 2) {
				System.out.println("###closing anylog.... ");
				restoreTransform();
				shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionUtils.addThrowable(e);
		}
		return 0;
	}

	private void doTransform(TransformDescriptor transformDescriptor) throws UnmodifiableClassException, ClassNotFoundException {
		// FIXME:这里还可以帮助预先load class。避免迟加载时，Transformer已经被remove掉而不起作用，但是对于通配符无效
		// if
		// (!ClassUtils.checkClassExist(transformDescriptor.getNeedInjectClassName()))
		// {
		// throw new
		// ClassNotFoundException(transformDescriptor.getNeedInjectClassName());
		// }
		
		// 添加字节码转换器,让其既有万能AOP功能
		ClassFileTransformer transformer = new AnyLogClassTransformer(transformDescriptor, transformedClassSet);
		System.out.println("###addTransformer");
		inst.addTransformer(transformer, true);

		Class<?>[] classes = inst.getAllLoadedClasses();
		// 如果load过了，才retransformClasses，要不load两次，貌似会有问题
		for (Class<?> clazz : classes) {
			if (clazz.getName().equals(transformDescriptor.getNeedInjectClassName())) {
				System.out.println("###retransforming classes...");
				inst.retransformClasses(new Class[] { clazz });
			}
		}
		System.out.println("###removeTransformer");
		// FIXME: 这里remove掉，对迟加载类会不起作用,但是如果不remove，会导致重复修改某个类的话，会多次起作用。
		inst.removeTransformer(transformer);

	}

	private void restoreTransform() throws UnmodifiableClassException {
		// 添加字节码转换器,让其既有万能AOP功能
		RestoreClassTransformer transformer = new RestoreClassTransformer(transformedClassSet);
		System.out.println("###addRestoreClassTransformer");
		inst.addTransformer(transformer, true);

		Class<?>[] classes = inst.getAllLoadedClasses();
		// 如果load过了，才retransformClasses，要不load两次，貌似会有问题
		for (Class<?> clazz : classes) {

			if (transformedClassSet.contains(clazz.getName())) {
				System.out.println("###retransforming classes...");

				inst.retransformClasses(new Class[] { clazz });
			}
		}
		System.out.println("###removeRestoreClassTransformer");
		inst.removeTransformer(transformer);
		transformedClassSet.clear();
	}
}
