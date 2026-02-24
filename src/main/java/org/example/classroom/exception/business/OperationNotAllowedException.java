package org.example.classroom.exception.business;

import org.example.classroom.enums.BusinessErrorCode;

/**
 * 操作不允许异常
 * <p>
 * 当当前状态下不允许执行某操作时抛出此异常
 * <p>
 * 使用场景：
 * <ul>
 *   <li>删除已批准的预订</li>
 *   <li>修改已完成的课程</li>
 *   <li>取消已开始的考试</li>
 * </ul>
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
public class OperationNotAllowedException extends org.example.classroom.exception.BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * 操作不允许
     *
     * @param operation 操作名称
     * @param reason   原因
     */
    public OperationNotAllowedException(String operation, String reason) {
        super(BusinessErrorCode.OPERATION_NOT_ALLOWED, operation, reason);
    }

    /**
     * 操作不允许 - 指定当前状态
     *
     * @param operation 操作名称
     * @param currentStatus 当前状态
     */
    public OperationNotAllowedException(String operation, String currentStatus, String requiredStatus) {
        super(BusinessErrorCode.INVALID_STATUS, operation, currentStatus, requiredStatus);
    }

    /**
     * 不能删除已批准的预订
     */
    public static OperationNotAllowedException cannotDeleteApprovedBooking() {
        return new OperationNotAllowedException("删除预订", "预订已批准，不能删除");
    }

    /**
     * 不能修改已完成的课程
     */
    public static OperationNotAllowedException cannotModifyCompletedCourse() {
        return new OperationNotAllowedException("修改课程", "课程已完成，不能修改");
    }

    /**
     * 不能取消已开始的预订
     */
    public static OperationNotAllowedException cannotCancelStartedBooking() {
        return new OperationNotAllowedException("取消预订", "预订已开始，不能取消");
    }

    /**
     * 教室已被占用
     *
     * @param classroomId 教室ID
     */
    public static OperationNotAllowedException classroomOccupied(String classroomId) {
        return new OperationNotAllowedException("预订教室", "教室 " + classroomId + " 在该时间段已被占用");
    }

    /**
     * 课程已达到最大人数
     */
    public static OperationNotAllowedException courseFull(String courseId) {
        return new OperationNotAllowedException("加入课程", "课程 " + courseId + " 已达到最大人数");
    }

    /**
     * 不在选课时间内
     */
    public static OperationNotAllowedException notInSelectionPeriod() {
        return new OperationNotAllowedException("选课", "当前不在选课时间范围内");
    }

    /**
     * 自定义操作不允许
     *
     * @param operation 操作描述
     * @param reason    原因
     */
    public static OperationNotAllowedException custom(String operation, String reason) {
        return new OperationNotAllowedException(operation, reason);
    }
}
