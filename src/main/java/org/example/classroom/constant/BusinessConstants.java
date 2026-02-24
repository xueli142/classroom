package org.example.classroom.constant;

/**
 * 业务相关常量
 * <p>
 * 定义业务逻辑相关的常量
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
public final class BusinessConstants {

    private BusinessConstants() {
        throw new UnsupportedOperationException("Constant class cannot be instantiated");
    }

    // ========== 预订相关 ==========
    /**
     * 预订默认状态
     */
    public static final String DEFAULT_BOOKING_STATUS = "pending";

    /**
     * 批量插入批次大小
     */
    public static final int BATCH_SIZE = 1000;

    /**
     * 默认分页大小
     */
    public static final long DEFAULT_PAGE_SIZE = 10L;

    /**
     * 默认分页页码
     */
    public static final long DEFAULT_PAGE_NUMBER = 1L;

    // ========== 时间相关 ==========
    /**
     * 一小时的毫秒数
     */
    public static final long ONE_HOUR_MILLIS = 3600000L;

    /**
     *一天的毫秒数
     */
    public static final long ONE_DAY_MILLIS = 86400000L;

    /**
     * 一周的毫秒数
     */
    public static final long ONE_WEEK_MILLIS = 604800000L;

    // ========== 数据库相关 ==========
    /**
     * 逻辑删除标记 - 未删除
     */
    public static final int DELETED_NO = 0;

    /**
     * 逻辑删除标记 - 已删除
     */
    public static final int DELETED_YES = 1;

    // ========== 正则表达式 ==========
    /**
     * 手机号正则表达式（中国大陆）
     */
    public static final String REGEX_PHONE = "^1[3-9]\\d{9}$";

    /**
     * 邮箱正则表达式
     */
    public static final String REGEX_EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    /**
     * 学号正则表达式（数字）
     */
    public static final String REGEX_STUDENT_ID = "^\\d{8,12}$";

    /**
     * 工号正则表达式（数字）
     */
    public static final String REGEX_TEACHER_ID = "^\\d{6,10}$";

    // ========== 业务规则 ==========
    /**
     * 课程最大学生数
     */
    public static final int MAX_COURSE_STUDENTS = 200;

    /**
     * 教室最大容量
     */
    public static final int MAX_CLASSROOM_CAPACITY = 500;

    /**
     * 最小预订提前时间（小时）
     */
    public static final int MIN_BOOKING_ADVANCE_HOURS = 2;

    /**
     * 最大预订提前时间（天）
     */
    public static final int MAX_BOOKING_ADVANCE_DAYS = 30;
}
