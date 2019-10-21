package server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Logger;

/**
 * @ClassName SocketByteHandler
 * @Description
 * @Author zhihui
 * @Date 2019/8/13 15:47
 * @Version 1.0
 */
public class SocketByteHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = Logger.getLogger(SocketByteHandler.class.getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        ByteBuf result = (ByteBuf) msg;
        byte[] bytes = new byte[result.readableBytes()];
        result.readBytes(bytes);
        String msgStr = new String(bytes);
        LOGGER.info("Message from client: " + msgStr);
        String response = "I am OK";
//        ByteBuf encoded = ctx.alloc().buffer(response.length() * 4);
//        encoded.writeBytes(response.getBytes());
//        ctx.write(encoded);
        ctx.writeAndFlush(response);
//        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        ctx.flush();
    }
}
