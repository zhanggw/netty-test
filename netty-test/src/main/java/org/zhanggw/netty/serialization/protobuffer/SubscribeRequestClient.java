package org.zhanggw.netty.serialization.protobuffer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class SubscribeRequestClient {

	public void start(String host, int port) {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<NioSocketChannel>() {

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
						ch.pipeline().addLast(new SubscribeRequestClientHandler());
					}
				});
		
		try {
			ChannelFuture f = bootstrap.connect(host, port).sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		int port = 10010;
		new SubscribeRequestClient().start("172.30.251.139", port);
	}

	
	public static class SubscribeRequestClientHandler extends ChannelInboundHandlerAdapter{
		@Override
	    public void channelActive(ChannelHandlerContext ctx) throws Exception {
	        for(int i = 0; i < 10; ++i) {
	        	ctx.writeAndFlush(request(i));
	        }
	    }
		
		private SubscribeMessagePro.SubscribeRequest request(int i) {
			SubscribeMessagePro.SubscribeRequest.Builder builder = SubscribeMessagePro.SubscribeRequest.newBuilder();
			builder.setSubReqId(i);
			builder.setAddress("park park");
			builder.setProductName("netty book");
			builder.setUserName("Lilinfeng");
			return builder.build();
		}
		
		@Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	        System.out.println("response: " + msg);
	    }
	}
}
