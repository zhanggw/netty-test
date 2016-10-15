package org.zhanggw.netty.test;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class TimeDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext arg0, ByteBuf arg1,
			List<Object> arg2) throws Exception {
		if(arg1.readableBytes() < 4) {
			return;
		}
		arg2.add(new UnixTime(arg1.readUnsignedInt()));
	}

}
