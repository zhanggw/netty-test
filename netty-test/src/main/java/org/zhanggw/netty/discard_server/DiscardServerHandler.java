package org.zhanggw.netty.discard_server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {		
        //((ByteBuf)msg).release();
		
//		ByteBuf inBuf = (ByteBuf)msg;
//		try {
//			while (inBuf.isReadable()) {
//				System.out.print((char) inBuf.readByte());
//	            System.out.flush();
//			}
//		} finally {
//			ReferenceCountUtil.release(msg);
//		}
		
		ByteBuf inBuf = (ByteBuf)msg;
		ctx.write(msg);
		ctx.flush();
    }
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
