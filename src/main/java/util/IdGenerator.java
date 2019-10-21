package util;

import java.util.UUID;

/**
 * @ClassName IdGenerator
 * @Description
 * @Author zhihui
 * @Date 2019/8/21 9:28
 * @Version 1.0
 */
public class IdGenerator {

    public static String nextId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
