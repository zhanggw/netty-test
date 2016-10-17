package org.zhanggw.aio.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

	private AsynchronousSocketChannel asynchronousSocketChannel;
	
	public ReadCompletionHandler(AsynchronousSocketChannel asynchronousSocketChannel) {
		this.asynchronousSocketChannel = asynchronousSocketChannel;
	}
	
	public void completed(Integer result, ByteBuffer attachment) {
		attachment.flip();
		byte[] body = new byte[attachment.remaining()];
		attachment.get(body);
		try {
			String req = new String(body, "UTF-8");
			System.out.println("the time server receive order: " + req);
			String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(req) ? new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
			doWrite(currentTime);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	
	public void failed(Throwable exc, ByteBuffer attachment) {
		try {
			asynchronousSocketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	private void doWrite(String currentTime) {
		if(currentTime != null && currentTime.trim().length() > 0) {
			byte[] bytes = currentTime.getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			asynchronousSocketChannel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

				public void completed(Integer result, ByteBuffer attachment) {
					if(attachment.hasRemaining()) {
						asynchronousSocketChannel.write(attachment, attachment, this);
					}
				}

				public void failed(Throwable exc, ByteBuffer attachment) {
					try {
						asynchronousSocketChannel.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
			});
		}
	}
}
