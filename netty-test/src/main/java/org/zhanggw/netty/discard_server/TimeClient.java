package org.zhanggw.netty.discard_server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {

	public static void main(String[] args) {
		String host = "localhost";
		int port = 10010;
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap(); 
			b.group(workGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast(new TimeClientHandler());
					
				}
			}).option(ChannelOption.SO_KEEPALIVE, true);
			
			ChannelFuture f = b.connect(host, port);
			try {
				f.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			workGroup.shutdownGracefully();
		}
	}

}
