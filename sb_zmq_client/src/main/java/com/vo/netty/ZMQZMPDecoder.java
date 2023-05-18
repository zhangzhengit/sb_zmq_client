package com.vo.netty;

import com.vo.protobuf.ZMP;
import com.vo.protobuf.ZPU;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.time.LocalDateTime;
import java.util.List;

/**
 *  
 *
 * @author zhangzhen
 * @data Aug 10, 2020
 * 
 */
public class ZMQZMPDecoder extends ByteToMessageDecoder{

	@Override
	protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
		
		final int readableBytes = in.readableBytes();
		if (readableBytes < ZPU.L_LENGTH) {
			return;
		}
		
		in.markReaderIndex();

		final byte[] lba = new byte[ZPU.L_LENGTH];
		in.readBytes(lba);
		
		final int vlength = ZPU.byteArrayToInt(lba);
		if (vlength <= 0) {
			in.resetReaderIndex();
			return;
		}

		final int readerIndex = in.readerIndex();
		final int writerIndex = in.writerIndex();
		if (readerIndex + vlength > writerIndex) {
			in.resetReaderIndex();
			return;
		}

		final byte[] vba = new byte[vlength];
		in.readBytes(vba);
		
		final ZMP zmp = ZPU.deserialize(vba, ZMP.class);
		out.add(zmp);
	}

}
