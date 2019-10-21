package client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @ClassName aa
 * @Description
 * @Author zhihui
 * @Date 2019/8/21 10:05
 * @Version 1.0
 */
public class DatagramEncoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        out.writeBytes(ByteBuffer.allocate(2));
        out.writeInt(msg.length());
        out.writeCharSequence(msg.subSequence(0, msg.length()), Charset.forName("UTF-8"));
    }
}