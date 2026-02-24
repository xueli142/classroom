package org.example.classroom.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 预订状态枚举
 * <p>
 * 定义预订记录的状态
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Getter
@AllArgsConstructor
public enum BookingStatus {

    /**
     * 待审批（提交后等待管理员/教师审批）
     */
    PENDING("pending", "待审批"),

    /**
     * 已批准（预订被接受）
     */
    APPROVED("approved", "已批准"),

    /**
     * 已拒绝（预订被拒绝）
     */
    REJECTED("rejected", "已拒绝"),

    /**
     * 已取消（用户主动取消）
     */
    CANCELLED("cancelled", "已取消"),

    /**
     * 已完成（预订的课程/活动已结束）
     */
    COMPLETED("completed", "已完成"),

    /**
     * 进行中（当前正在进行）
     */
    IN_PROGRESS("in_progress", "进行中");

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
    public static BookingStatus fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (BookingStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid booking status code: " + code);
    }

    /**
     * 判断是否为终态（不可再修改）
     */
    public boolean isFinalState() {
        return this == COMPLETED || this == CANCELLED || this == REJECTED;
    }

    /**
     * 判断是否可以取消
     */
    public boolean canCancel() {
        return this == PENDING || this == APPROVED;
    }

    /**
     * 判断是否可以进行中
     */
    public boolean canStart() {
        return this == APPROVED;
    }
}
