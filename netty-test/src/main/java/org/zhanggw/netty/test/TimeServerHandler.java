package org.zhanggw.netty.test;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {
	@Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		ChannelFuture future = ctx.writeAndFlush(new UnixTime());
		future.addListener(ChannelFutureListener.CLOSE);
//		future.addListener(new ChannelFutureListener() {
//			public void operationComplete(ChannelFuture f)
//					throws Exception {
//				ctx.close();
//			}
//		});
    }

    
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
    }
}
