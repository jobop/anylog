package com.github.jobop.anylog.common.utils;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class PortUtils {
	private static int MAX_PORT = 65535;

	public static boolean isPortAvaliable(int port) {
		Socket socket = null;
		try {
			socket = new Socket("127.0.0.1", port);
		} catch (UnknownHostException ex) {
		} catch (IOException ex) {
			socket = null;
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
			}
			return false;
		}
		return true;
	}

	public static int generateAvaliablePort() {
		for (int i = 0; i < 5; i++) {
			int port = new Random().nextInt(MAX_PORT);
			if (isPortAvaliable(port)) {
				return port;
			}
		}

		throw new RuntimeException("can not generate availbale port in 5 times!");

	}
	public static void main(String[] args) {
		System.out.println(generateAvaliablePort());
	}
}
