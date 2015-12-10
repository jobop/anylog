package com.github.jobop.anylog.core.vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.github.jobop.anylog.core.interactive.protocol.Command;
import com.github.jobop.anylog.core.interactive.protocol.CommandRet;
import com.github.jobop.anylog.core.interactive.protocol.TransformCommand;
import com.github.jobop.anylog.spi.TransformDescriptor;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

public class VirtualMachineManager {
	private static VirtualMachineManager virtualMachineManager = null;

	private static LoadingCache<String, VirtualMachineWrapper> vmCache = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).maximumSize(200).build(new CacheLoader<String, VirtualMachineWrapper>() {
		@Override
		public VirtualMachineWrapper load(String key) throws Exception {
			return new NullVirtualMachineWrapper();
		}
	});

	private VirtualMachineManager() {
		new RefreashVMThread().start();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("###running clear thread");
				List<VirtualMachineWrapper> vms = listVMs();
				for (VirtualMachineWrapper vm : vms) {
					if (vm.isConnected()) {
						try {
							System.out.println("###closing " + vm.getId());
							vm.disConnect();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}  

	public static VirtualMachineManager getInstance() {
		if (null == virtualMachineManager) {
			synchronized (VirtualMachineManager.class) {
				if (null == virtualMachineManager) {
					virtualMachineManager = new VirtualMachineManager();
				}
			}
		}
		return virtualMachineManager;
	}

	public VirtualMachineWrapper connected(String pid, String argus) {
		VirtualMachineWrapper wrapper = null;
		try {
			wrapper = vmCache.get(pid);
			wrapper.connect(argus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wrapper;
	}

	public void disConnected(String pid) {
		try {
			VirtualMachineWrapper wrapper = vmCache.get(pid);
			wrapper.disConnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CommandRet sendTransformCommand(String pid, TransformDescriptor transformDescriptor) {
		CommandRet ret = new CommandRet();
		try {
			TransformCommand command = new TransformCommand();
			command.setTransformDescriptor((TransformDescriptor) transformDescriptor);
			ret = sendCommand(pid, command);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ret.getRetCode() == 0) {
			try {
				VirtualMachineWrapper wrapper = vmCache.get(pid);
				if (null != wrapper) {
					wrapper.addDescriptor(transformDescriptor);
				}
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

		}
		return ret;
	}

	public List<TransformDescriptor> listTransformDescriptor(String pid) {
		List<TransformDescriptor> descs = new ArrayList<TransformDescriptor>();
		try {
			VirtualMachineWrapper wrapper = vmCache.get(pid);
			descs = wrapper.listTransformDescriptor();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return descs;
	}

	public CommandRet sendCommand(String pid, Command command) {
		CommandRet ret = new CommandRet();
		try {
			VirtualMachineWrapper wrapper = vmCache.get(pid);
			ret = wrapper.sendCommand(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public List<VirtualMachineWrapper> listVMs() {
		List<VirtualMachineWrapper> vmList = new ArrayList<VirtualMachineWrapper>();
		ConcurrentMap<String, VirtualMachineWrapper> vmMap = vmCache.asMap();
		for (Entry<String, VirtualMachineWrapper> entry : vmMap.entrySet()) {
			vmList.add(entry.getValue());
		}
		return vmList;
	}

	private void refreashVMList() throws ExecutionException {
		List<VirtualMachineDescriptor> vmList = VirtualMachine.list();
		for (VirtualMachineDescriptor vmd : vmList) {
			VirtualMachineWrapper wrapper = vmCache.get(vmd.id());

			if (NullVirtualMachineWrapper.class.isAssignableFrom(wrapper.getClass())) {
				wrapper = new VirtualMachineWrapper(vmd);
			}
			// 为空不为空都put一次，以更新缓存时间
			vmCache.put(vmd.id(), wrapper);
		}
	}

	// public void detachVM(String pid) {
	// try {
	// VirtualMachineWrapper wrapper = vmCache.get(pid);
	// wrapper.detach();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// public void loadAgent(String pid, String jarPath, String argus) {
	// try {
	// System.out.println("loading jar " + jarPath + " to " + pid);
	// VirtualMachineWrapper wrapper = vmCache.get(pid);
	// wrapper.loadAgent(jarPath == null ? DEFAULT_JAR_PATH : jarPath, argus);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// public void injectClass(String pid, String ClassName, String methodName,
	// String line, String logCode) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// public void releaseClass(String pid, String ClassName) {
	// // TODO Auto-generated method stub
	//
	// }

	private class RefreashVMThread extends Thread {
		public RefreashVMThread() {
			try {
				refreashVMList();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setName("load-vms-thread");
		}

		public void run() {
			try {
				while (true) {
					refreashVMList();
					Thread.sleep(20000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
