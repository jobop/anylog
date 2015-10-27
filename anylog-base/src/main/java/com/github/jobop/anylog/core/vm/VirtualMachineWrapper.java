package com.github.jobop.anylog.core.vm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import com.github.jobop.anylog.common.constans.Constans;
import com.github.jobop.anylog.common.utils.ConfigUtils;
import com.github.jobop.anylog.common.utils.FileUtils;
import com.github.jobop.anylog.common.utils.PortUtils;
import com.github.jobop.anylog.core.exception.NotConnectedException;
import com.github.jobop.anylog.core.interactive.protocol.CloseCommand;
import com.github.jobop.anylog.core.interactive.protocol.Command;
import com.github.jobop.anylog.core.interactive.protocol.CommandRet;
import com.github.jobop.anylog.core.interactive.system.VMSocketClient;
import com.github.jobop.anylog.core.utils.ArgusUtils;
import com.github.jobop.anylog.spi.TransformDescriptor;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

public class VirtualMachineWrapper {
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private WriteLock wl = lock.writeLock();
	private ReadLock rl = lock.readLock();

	private VirtualMachineDescriptor vmd;
	private VirtualMachine vm;
	private VMSocketClient client;

	private boolean attached;
	private boolean connected;
	private String agentedJarPath = new File(ConfigUtils.getStringValue(Constans.AGENT_JAR_PATH)).getAbsolutePath();
	private String spiJarPath = new File(ConfigUtils.getStringValue(Constans.SPI_JAR_PATH)).getAbsolutePath();
	private String toolJarPath = new File(ConfigUtils.getStringValue(Constans.TOOL_JAR_PATH)).getAbsolutePath();
	private String commonJarPath = new File(ConfigUtils.getStringValue(Constans.COMMON_JAR_PATH)).getAbsolutePath();
	private List<String> agentedJars = new ArrayList<String>();
	private List<TransformDescriptor> descriptors = new ArrayList<TransformDescriptor>();

	public VirtualMachineWrapper() {
	}

	public VirtualMachineWrapper(VirtualMachineDescriptor vmd) {
		this.vmd = vmd;
	}

	public String getDisplayName() {
		return vmd.displayName();
	}

	public String getId() {
		return vmd.id();
	}

	public synchronized void connect(String arg) throws Exception {
		if (isConnected()) {
			return;
		}
		int port = PortUtils.generateAvaliablePort();
		System.out.println("### pid=" + getId() + " port=" + port);
		if (!isAgented()) {
			Map<String, String> argusMap = initArgus(arg, port);
			loadAgent(agentedJarPath, ArgusUtils.map2argus(argusMap));
		}
		client = new VMSocketClient();
		client.connect(port);
		connected = true;
	}

	private Map<String, String> initArgus(String arg, int port) {
		Map<String, String> argusMap = ArgusUtils.args2map(arg);
		argusMap.put(ArgusUtils.PORT, String.valueOf(port));
		//
		argusMap.put(ArgusUtils.SYSTEM_JARS, FileUtils.findProviderJarFiles(ConfigUtils.getStringValue(Constans.PROVIDER_PATH)) + toolJarPath + "&" + commonJarPath + "&" + spiJarPath);

		argusMap.put(Constans.TRACE_LOG_PATH, new File(ConfigUtils.getStringValue(Constans.TRACE_LOG_PATH)).getAbsolutePath());
		argusMap.put(Constans.THIS_PID, getId());
		return argusMap;
	}

	public synchronized CommandRet sendCommand(Command command) {
		if (!isConnected()) {
			throw new NotConnectedException("please reconnect!");
		}
		return client.send(command);
	}

	private synchronized void attach() throws AttachNotSupportedException, IOException {
		this.vm = VirtualMachine.attach(vmd);
		attached = true;
	}

	private synchronized void detach() throws IOException {
		if (!attached) {
			return;
		}
		attached = false;
		vm.detach();
	}

	private synchronized void loadAgent(String jarPath, String arg) throws Exception {
		if (!attached) {
			this.attach();
		}
		vm.loadAgent(jarPath, arg);
		agentedJars.add(jarPath);
	}

	public boolean isAttached() {
		return attached;
	}

	public boolean isAgented() {
		return isAttached() && agentedJars.size() > 0;
	}

	public boolean isConnected() {
		return isAgented() && connected;
	}

	public synchronized void disConnect() {
		if (isConnected()) {
			try {
				sendCommand(new CloseCommand());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				client = null;
				connected = false;
			}
		}
		if (isAgented()) {
			agentedJars.clear();
		}
		if (isAttached()) {
			try {
				detach();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		clearDescriptor();
	}

	public void addDescriptor(TransformDescriptor descriptor) {
		try {
			wl.lock();
			descriptors.add(descriptor);
		} finally {
			wl.unlock();
		}
	}

	public void clearDescriptor() {
		try {
			wl.lock();
			descriptors.clear();
		} finally {
			wl.unlock();
		}
	}

	public List<TransformDescriptor> listTransformDescriptor() {
		try {
			rl.lock();
			List<TransformDescriptor> newList = new ArrayList<TransformDescriptor>();
			newList.addAll(descriptors);
			return newList;
		} finally {
			rl.unlock();
		}
	}
}
