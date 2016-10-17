package org.zhanggw.aio.client;

public class TimeClient {

	public static void main(String[] args) {
		int port = 10010;
		if(null != args && 0 < args.length) {
			port = Integer.valueOf(args[0]);
		}
		
		AsyncTimeClientHandler client = new AsyncTimeClientHandler("localhost", port);
		new Thread(client).start();
	}
}
