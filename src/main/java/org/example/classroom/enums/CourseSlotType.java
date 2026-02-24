package org.example.classroom.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 课程时段类型枚举
 * <p>
 * 定义课程时段的类型（如：理论课、实验课等）
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Getter
@AllArgsConstructor
public enum CourseSlotType {

    /**
     * 理论课
     */
    LECTURE("lecture", "理论课"),

    /**
     * 实验课
     */
    LAB("lab", "实验课"),

    /**
     * 实践课
     */
    PRACTICE("practice", "实践课"),

    /**
     * 研讨课
     */
    SEMINAR("seminar", "研讨课"),

    /**
     * 考试
     */
    EXAM("exam", "考试"),

    /**
     * 其他
     */
    OTHER("other", "其他");

    /**
     * 类型编码
     */
    private final String code;

    /**
     * 类型描述
     */
    private final String description;

    /**
     * 根据编码获取类型枚举
     *
     * @param code 类型编码
     * @return 类型枚举
     * @throws IllegalArgumentException 如果编码无效
     */
    public static CourseSlotType fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (CourseSlotType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid course slot type code: " + code);
    }
}
