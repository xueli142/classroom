package org.example.classroom.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 学期状态枚举
 * <p>
 * 定义学期的状态
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Getter
@AllArgsConstructor
public enum TermStatus {

    /**
     * 未开始（学期尚未开始）
     */
    NOT_STARTED("not_started", "未开始"),

    /**
     * 进行中（学期正在进行）
     */
    IN_PROGRESS("in_progress", "进行中"),

    /**
     * 已结束（学期已结束）
     */
    ENDED("ended", "已结束"),

    /**
     * 假期中（假期期间）
     */
    BREAK("break", "假期中");

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
    public static TermStatus fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (TermStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid term status code: " + code);
    }

    /**
     * 判断是否可以进行中
     */
    public boolean canStart() {
        return this == NOT_STARTED;
    }

    /**
     * 判断是否可以结束
     */
    public boolean canEnd() {
        return this == IN_PROGRESS;
    }

    /**
     * 判断是否可以预订教室
     */
    public boolean isBookable() {
        return this == IN_PROGRESS;
    }
}
