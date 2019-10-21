import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @ClassName Test
 * @Description
 * @Author zhihui
 * @Date 2019/8/13 14:31
 * @Version 1.0
 */
public class Test {

    public static void main(String[] args) {
//        System.out.println(Integer.toHexString(17));
//        byte[] bytes = "I am OK".getBytes(Charset.forName("GBK"));
//        for (int i = 0; i < bytes.length; i++) {
//            System.out.println(bytes[i]);
//        }

        ByteBuffer header = ByteBuffer.allocate(4);
        header.putInt(256);
        for (int i = 0; i < header.capacity(); i++) {
            System.out.println(header.get(i));
        }
//        System.out.println(1);
        System.out.println(Integer.toHexString(255));
    }
}
