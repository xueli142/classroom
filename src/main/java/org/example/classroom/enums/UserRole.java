package org.example.classroom.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户角色枚举
 * <p>
 * 定义系统中的用户角色类型
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Getter
@AllArgsConstructor
public enum UserRole {

    /**
     * 管理员
     */
    ADMIN("admin", "管理员", 1),

    /**
     * 教师
     */
    TEACHER("teacher", "教师", 2),

    /**
     * 学生
     */
    STUDENT("student", "学生", 3);

    /**
     * 角色编码（存储在数据库）
     */
    private final String code;

    /**
     * 角色描述
     */
    private final String description;

    /**
     * 角色级别（数字越小权限越高）
     */
    private final int level;

    /**
     * 根据编码获取角色枚举
     *
     * @param code 角色编码
     * @return 角色枚举
     * @throws IllegalArgumentException 如果编码无效
     */
    public static UserRole fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (UserRole role : values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role code: " + code);
    }

    /**
     * 判断是否为管理员
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }

    /**
     * 判断是否为教师
     */
    public boolean isTeacher() {
        return this == TEACHER;
    }

    /**
     * 判断是否为学生
     */
    public boolean isStudent() {
        return this == STUDENT;
    }

    /**
     * 判断权限是否高于指定角色
     *
     * @param other 另一个角色
     * @return true 如果当前角色级别更高
     */
    public boolean hasHigherLevelThan(UserRole other) {
        return this.level < other.level;
    }
}
