package org.example.demo5.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 内部ID生成工具类
 * 提供了多种生成唯一ID的方法
 */

public final class IdGeneratorUtil {

    /**
     * 私有构造函数，防止被实例化
     */
    private IdGeneratorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 生成一个128位的UUID字符串，并移除所有连字符。
     * 适用于生成内部唯一ID，例如 user_id。
     *
     * @return 一个不带连字符的UUID字符串，例如 "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    // 假设你还需要一个更短的ID，可以使用UUID的部分
    // 警告：这种方法不能保证全局唯一性，仅在某些特定场景下使用
    public static String generateShortUuid() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    // 如果你使用的是雪花算法，这里可以添加相应的方法
    // public static Long generateSnowflakeId() {
    //     // ... 调用雪花算法库的代码
    // }
}