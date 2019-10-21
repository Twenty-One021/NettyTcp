package client;

import util.IdGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * @ClassName TcpClient
 * @Description
 * @Author zhihui
 * @Date 2019/8/13 16:18
 * @Version 1.0
 */
public class TcpClient {

    private static final Logger LOGGER = Logger.getLogger(TcpClient.class.getName());

    private static final String IP = "192.168.0.254";
    private static final int PORT = 8099;
//    private static final int PORT = 8299;

    private static final String serial = IdGenerator.nextId();

    public static void sendMsg(byte[] msg) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(IP, PORT));
            socket.setKeepAlive(true);
            OutputStream out = socket.getOutputStream();
            ByteBuffer reserve = ByteBuffer.allocate(2);
//            reserve.putInt(0);
            ByteBuffer header = ByteBuffer.allocate(4);
            header.putInt(msg.length);
            out.write(reserve.array());
            out.write(header.array());
            out.write(msg);
            LOGGER.info("Message: ");
            out.flush();
            InputStream in = socket.getInputStream();
            byte[] buff = new byte[4096];
//            int read = in.read(buff);
            byte[] lenBytes = new byte[6];
            in.read(lenBytes, 0, 6);
            int len = 0;
            for (int i = 2; i < lenBytes.length; i++) {
                int shift = (4 - 1 - (i + 2)) * 8;
                int byteValue = lenBytes[i] & 0x000000FF;
                len += (byteValue << shift);
            }
            System.out.println(len);
            byte[] message = new byte[len];
            in.read(message);
            LOGGER.info("Message from server: " + new String(message));

            int read = in.read();
            System.out.println(read);

//            if (read > 6) {
//                int length = 0;
//                for (int i = 2; i < 6; i++) {
//                    int shift = (4 - 1 - (i + 2)) * 8;
//                    int i1 = (buff[i] & 0x000000FF) << shift;
//                    length += i1;
//                }
//                System.out.println(length);
//                String s = new String(buff, 6, read);
//                LOGGER.info("Message from server: " + s);
//            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.next();
            if ("q".equals(next)) {
                break;
            }
            sendMsg(next.getBytes());
        }
        sendMsg("Bye-bye.".getBytes());
    }
}
