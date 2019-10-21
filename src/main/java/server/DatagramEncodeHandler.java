package server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @ClassName DatagramEncodeHandler
 * @Description
 * @Author zhihui
 * @Date 2019/8/19 16:33
 * @Version 1.0
 */
public class DatagramEncodeHandler extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        out.writeBytes(ByteBuffer.allocate(2));
        out.writeInt(msg.length());
        out.writeCharSequence(msg.subSequence(0, msg.length()), Charset.forName("UTF-8"));
    }
}
