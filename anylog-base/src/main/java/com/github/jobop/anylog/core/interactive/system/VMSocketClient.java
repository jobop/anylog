package com.github.jobop.anylog.core.interactive.system;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.github.jobop.anylog.core.interactive.protocol.Command;
import com.github.jobop.anylog.core.interactive.serializer.Serializer;
import com.github.jobop.anylog.core.interactive.serializer.SerializerFactory;

public class VMSocketClient {
	private String LOCAL_IP = "127.0.0.1";
	private String ip;
	private int port;
	private Serializer serializer = SerializerFactory.getInstance().getDefaultSerializerPair().getSerializer();

	public void connect(int port) {
		connect(LOCAL_IP, port);
	}

	public void connect(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	// 这里网络交换比较简单，不考虑性能问题
	public int send(Command command) {
		Socket socket = null;
		int result = 0;
		DataInputStream input = null;
		DataOutputStream output = null;
		try {
			socket = new Socket(ip, port);
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			byte[] sendBytes = serializer.serialize(command);
			output.write(sendBytes);
			result = input.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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

		return result;
	}

	public void setSerializer(Serializer serializer) {
		this.serializer = serializer;
	}

}
