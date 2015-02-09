package org.itas.core.net.nio;

import org.itas.core.Rule;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@Sharable
public class MessageEncoder extends MessageToByteEncoder<Message> implements Rule {

    public MessageEncoder() {
    }

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		out.writeBytes(msg.pack());
	}

}
