package org.example.classroom.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备状态枚举
 * <p>
 * 定义设备/物品的状态
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Getter
@AllArgsConstructor
public enum ThingStatus {

    /**
     * 可用（可以借出）
     */
    AVAILABLE("available", "可用"),

    /**
     * 借出中（已被借出）
     */
    BORROWED("borrowed", "借出中"),

    /**
     * 维护中（正在维护，不可借出）
     */
    MAINTENANCE("maintenance", "维护中"),

    /**
     * 已损坏（设备损坏，不可使用）
     */
    DAMAGED("damaged", "已损坏"),

    /**
     * 已丢失（设备丢失）
     */
    LOST("lost", "已丢失"),

    /**
     * 已报废（设备报废，不再使用）
     */
    SCRAPPED("scrapped", "已报废");

    /**
     * 状态编码
     */
    private final String code;

    /**
     * 状态描述
     */
    private final String description;

    /**
     * 根据编码获取状态枚举
     *
     * @param code 状态编码
     * @return 状态枚举
     * @throws IllegalArgumentException 如果编码无效
     */
    public static ThingStatus fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (ThingStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid thing status code: " + code);
    }

    /**
     * 判断是否可以借出
     */
    public boolean isBorrowable() {
        return this == AVAILABLE;
    }

    /**
     * 判断是否可用（非损坏、非丢失、非报废）
     */
    public boolean isActive() {
        return this != DAMAGED && this != LOST && this != SCRAPPED;
    }
}
