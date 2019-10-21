package server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LoggingHandler;
import org.omg.CORBA.TCKind;

import java.awt.*;
import java.util.logging.Logger;

/**
 * @ClassName TcpServer
 * @Description
 * @Author zhihui
 * @Date 2019/8/13 15:36
 * @Version 1.0
 */
public class TcpServer {

    private static final Logger LOGGER = Logger.getLogger(TcpServer.class.getName());

    private static final String IP = "192.168.0.254";
    private static final int PORT = 8299;

    protected static final int MAX_GROUP_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    protected static final int MAX_THREAD_SIZE = 4;

    private static final EventLoopGroup acceptor = new NioEventLoopGroup(MAX_GROUP_SIZE);
    private static final EventLoopGroup worker = new NioEventLoopGroup(MAX_THREAD_SIZE);

    protected static void run() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(acceptor, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 2, 4, 0, 6));
//                        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                        pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                        pipeline.addLast("frameEncoder", new DatagramEncodeHandler());
                        pipeline.addLast(new SocketByteHandler());
                    }
                });
        bootstrap.bind(IP, PORT).sync();
        LOGGER.info("TCP server started.");
    }

    protected static void shutdown() {
        worker.shutdownGracefully();
        acceptor.shutdownGracefully();
    }

    public static void main(String[] args) throws InterruptedException {
        LOGGER.info("Starting up TCP server...");
        TcpServer.run();
//        TcpServer.shutdown();
    }
}
