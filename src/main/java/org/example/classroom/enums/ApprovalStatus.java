package org.example.classroom.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审批状态枚举
 * <p>
 * 定义需要审批的项目的状态
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Getter
@AllArgsConstructor
public enum ApprovalStatus {

    /**
     * 待审核（提交后等待审核）
     */
    REVIEWING("reviewing", "待审核"),

    /**
     * 已通过（审核通过）
     */
    PUBLISHED("published", "已通过"),

    /**
     * 已拒绝（审核未通过）
     */
    REJECTED("rejected", "已拒绝"),

    /**
     * 请假（用户请假）
     */
    LEAVE("leave", "请假");

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
    public static ApprovalStatus fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (ApprovalStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid approval status code: " + code);
    }

    /**
     * 判断是否为终态（不可再修改）
     */
    public boolean isFinalState() {
        return this == PUBLISHED || this == REJECTED;
    }

    /**
     * 判断是否可以撤销
     */
    public boolean canWithdraw() {
        return this == REVIEWING;
    }
}
