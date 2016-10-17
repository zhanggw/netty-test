package org.zhanggw.netty.serialization.protobuffer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class SubscribeRequestServer {

	private int port;
	
	public SubscribeRequestServer(int port) {
		this.port = port;
	}
	
	public void start() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workGroup)
						.channel(NioServerSocketChannel.class)
						.option(ChannelOption.SO_BACKLOG, 128)
						.childOption(ChannelOption.SO_KEEPALIVE, true)
						.childHandler(new ChannelInitializer<NioSocketChannel>() {

							@Override
							protected void initChannel(NioSocketChannel ch)
									throws Exception {
								//decoders
								ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
								ch.pipeline().addLast(new ProtobufDecoder(SubscribeMessagePro.SubscribeRequest.getDefaultInstance()));
								//encoders
								ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
								ch.pipeline().addLast(new ProtobufEncoder());
								//own
								ch.pipeline().addLast(new SubscribeRequestServerHandler());
							}
						});
		ChannelFuture future;
		try {
			future = serverBootstrap.bind(port).sync();
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		int port = 10010;
		new SubscribeRequestServer(port).start();
	}

	public static class SubscribeRequestServerHandler extends ChannelInboundHandlerAdapter {
		@Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			SubscribeMessagePro.SubscribeRequest request = (SubscribeMessagePro.SubscribeRequest)msg;
			if("Lilinfeng".equalsIgnoreCase(request.getUserName())) {
				System.out.println("service accept client subscript req: " + request.toString());
				ctx.writeAndFlush(response(request.getSubReqId()));
			}
	    }
		
		private SubscribeMessagePro.SubscribeResponse response(int requestId) {
			SubscribeMessagePro.SubscribeResponse.Builder builder = SubscribeMessagePro.SubscribeResponse.newBuilder();
			builder.setSubReqId(requestId);
			builder.setRecpCode(0);
			builder.setDesc("Netty book order succeed, 3 days later, sent to the desiginated address");
			return builder.build();
		}
		
		@Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	            throws Exception {
	        cause.printStackTrace();
	        ctx.close();
	    }
	}
}
