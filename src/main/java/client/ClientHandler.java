package client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.nio.charset.Charset;
import java.util.logging.Logger;

/**
 * @ClassName ClientHanlder
 * @Description
 * @Author zhihui
 * @Date 2019/8/21 10:07
 * @Version 1.0
 */
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());

    private ChannelHandlerContext ctx;

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        String message = new String(bytes, Charset.forName("UTF-8"));
        LOGGER.info("Message from server: " + message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
//        ctx.writeAndFlush("{\"action\":\"Regist\",\"deviceId\":\"ROS\",\"serial\":\"placeholder\"}");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        this.ctx = ctx;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        super.userEventTriggered(ctx, evt);
        String remoteAddr = getRemoteAddr(ctx);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                LOGGER.info("Client " + remoteAddr + " READER_IDLE reading timeout.");
                ctx.disconnect();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                LOGGER.info("Client " + remoteAddr + " WRITE_IDLE writing timeout.");
                ctx.disconnect();
            } else if (event.state() == IdleState.ALL_IDLE) {
                LOGGER.info("Client " + remoteAddr + " ALL_IDLE all timeout.");
                ctx.disconnect();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        ctx.flush();
        LOGGER.info("Exception caught, connection closed, remote address: " + getRemoteAddr(ctx));
    }

    private String getRemoteAddr(ChannelHandlerContext ctx) {
        return ctx.channel().remoteAddress().toString();
    }
}
