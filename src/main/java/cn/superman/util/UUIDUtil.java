package cn.superman.util;

import java.util.UUID;

public class UUIDUtil {
    private UUIDUtil() {
        throw new AssertionError("不要实例化工具类哦");
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String get32UUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
