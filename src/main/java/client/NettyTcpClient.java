package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import util.IdGenerator;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @ClassName NettyTcpClient
 * @Description
 * @Author zhihui
 * @Date 2019/8/21 9:55
 * @Version 1.0
 */
public class NettyTcpClient {

//    private static final String SERVER_HOST = "192.168.0.254";
//    private static final int SERVER_PORT = 8000;
    private static final String SERVER_HOST = "134.175.91.116";
    private static final int SERVER_PORT = 25566;

//    private static final String serial = IdGenerator.nextId();
    private static final String serial = "cd69633846974349b751e04772713999";

    private static final String DATAGRAM_R = "{\"action\":\"Regist\",\"deviceId\":\"ROS\",\"serial\":\"placeholder\"}";
    private static final String DATAGRAM_C = "{\"action\":\"ArriveCrossing\",\"serial\":\"serial_ph\",\"x\":x_ph,\"y\":y_ph,\"no\":no_ph}";
    private static final String DATAGRAM_HB = "{\"action\":\"HEARTBEAT\",\"serial\":\"serial_ph\"}";
    private static final String DATAGRAM_RS = "{\"action\":\"RosState\",\"serial\":\"serial_ph\",\"content\":{\"pavement\":\"pavement_ph\",\"powerNode\":\"powerNode_ph\",\"powerTemp\":\"powerTemp_ph\",\"power\":\"power_ph\",\"infrared\":\"infrared_ph\",\"boxTemp\":\"boxTemp_ph\",\"stray\":\"stray_ph\",\"powerCharge\":\"powerCharge_ph\"}}";
    private static final String DATAGRAM_RR = "{\"action\":\"RosRealStat\",\"serial\":\"serial_ph\",\"content\":{\"x\":x_ph,\"y\":y_ph,\"speed\":{\"carSpeed\":carSpeed_ph,\"flSpeed\":flSpeed_ph,\"frSpeed\":frSpeed_ph,\"rlSpeed\":rlSpeed_ph,\"rrSpeed\":rrSpeed_ph},\"direction\":{\"carDirection\":\"carDirection_ph\",\"flTire\":flTire_ph, \"frTire\":frTire_ph,\"rlTire\":rlTire_ph,\"rrTire\":rrTire_ph},\"model\":\"model_ph\",\"mapNode\":\"mapNode_ph\",\"voltage\":\"voltage_ph\",\"current\":\"current_ph\",\"odometry\":\"odometry_ph\"}}";

    private static final String MOVE_RESP = "{\"action\":\"Move\",\"angle\":\"0\",\"mode\":\"2\",\"msg\":\"MoveActionResponse\",\"result\":\"0000\",\"serial\":\"cd69633846974349b751e04772713999\",\"speed\":\"3\"}";
    private static final String POSITION_GET = "{\"action\":\"GSPositionParamGet\",\"msg\":\"GSPositionParamAddAction\",\"orientation\":\"3\",\"result\":\"0000\",\"serial\":\"cd69633846974349b751e04772713999\",\"x\":\"0\",\"y\":\"2\"}";

    private static int crossingCounter = 0;

    public static void main(String[] args) {
        ClientHandler clientHandler = new ClientHandler();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 2, 4, 0, 6));
                            pipeline.addLast("frameEncoder", new DatagramEncoder());
                            pipeline.addLast("handler", clientHandler);
                        }
                    });
            ChannelFuture future = bootstrap.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT)).sync();

            String msg = null;
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String next = scanner.next();
                switch (next) {
                    case "register":
                        msg = DATAGRAM_R.replace("placeholder", serial);
                        break;
                    case "heartbeat":
                        msg = DATAGRAM_HB.replace("serial_ph", serial);
                        break;
                    case "cross":
                        msg = DATAGRAM_C.replace("serial_ph", serial)
                                .replace("no_ph", crossingCounter++ + "")
                                .replace("x_ph", 114.0000000 + "")
                                .replace("y_ph", 23.0000000 + "")
                                .replace("no_ph", (++crossingCounter) + "");
                        break;
                    case "move":
                        msg = MOVE_RESP;
                        break;
                    case "pget":
                        msg = POSITION_GET;
                        break;
                    case "rosstate":
                    case "rosrealstate":
                    default:
                        break;
                }
                if (msg != null) {
                    future.channel().writeAndFlush(msg);
                }
            }
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
        }
    }
}
