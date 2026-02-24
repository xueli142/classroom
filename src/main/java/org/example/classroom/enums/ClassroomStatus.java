package org.example.classroom.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 教室状态枚举
 * <p>
 * 定义教室的使用状态
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Getter
@AllArgsConstructor
public enum ClassroomStatus {

    /**
     * 空闲（可预订）
     */
    AVAILABLE("available", "空闲"),

    /**
     * 使用中（正在上课或活动）
     */
    OCCUPIED("occupied", "使用中"),

    /**
     * 维护中（不可预订）
     */
    MAINTENANCE("maintenance", "维护中"),

    /**
     * 已停用（不再使用）
     */
    DISABLED("disabled", "已停用"),

    /**
     * 预留（被预留，不可普通预订）
     */
    RESERVED("reserved", "预留");

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
    public static ClassroomStatus fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (ClassroomStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid classroom status code: " + code);
    }

    /**
     * 判断是否可以预订
     */
    public boolean isBookable() {
        return this == AVAILABLE;
    }

    /**
     * 判断是否可用（非停用、非维护）
     */
    public boolean isActive() {
        return this != MAINTENANCE && this != DISABLED;
    }
}
