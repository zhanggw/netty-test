package org.zhanggw.aio.server;

public class TimeServer {

	public static void main(String[] args) {
		int port = 10010;
		if(null != args && 0 < args.length) {
			port = Integer.valueOf(args[0]);
		}
		
		AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
		new Thread(timeServer).start();
	}

}
