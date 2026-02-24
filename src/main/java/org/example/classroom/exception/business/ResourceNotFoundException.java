package org.example.classroom.exception.business;

import org.example.classroom.enums.ErrorCode;

/**
 * 资源不存在异常
 * <p>
 * 当查询的资源不存在时抛出此异常
 * <p>
 * 使用示例：
 * <pre>{@code
 * throw ResourceNotFoundException.of("教室", classroomId);
 * }</pre>
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
public class ResourceNotFoundException extends org.example.classroom.exception.BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * 资源类型
     */
    private final String resourceType;

    /**
     * 资源ID
     */
    private final String resourceId;

    /**
     * 构造函数
     *
     * @param errorCode   错误码枚举
     * @param resourceType 资源类型
     * @param resourceId  资源ID
     */
    public ResourceNotFoundException(ErrorCode errorCode, String resourceType, String resourceId) {
        super(errorCode, resourceType, resourceId);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    /**
     * 创建资源不存在异常
     *
     * @param resourceType 资源类型（如：教室、课程、预订等）
     * @param resourceId  资源ID
     */
    public static ResourceNotFoundException of(String resourceType, String resourceId) {
        return new ResourceNotFoundException(
                org.example.classroom.enums.BusinessErrorCode.RESOURCE_NOT_FOUND,
                resourceType,
                resourceId
        );
    }

    /**
     * 教室不存在
     */
    public static ResourceNotFoundException classroom(String classroomId) {
        return of("教室", classroomId);
    }

    /**
     * 课程不存在
     */
    public static ResourceNotFoundException course(String courseId) {
        return of("课程", courseId);
    }

    /**
     * 课程时段不存在
     */
    public static ResourceNotFoundException courseSlot(String slotId) {
        return of("课程时段", slotId);
    }

    /**
     * 预订记录不存在
     */
    public static ResourceNotFoundException booking(String bookingId) {
        return of("预订记录", bookingId);
    }

    /**
     * 学期不存在
     */
    public static ResourceNotFoundException term(String termId) {
        return of("学期", termId);
    }

    /**
     * 设备不存在
     */
    public static ResourceNotFoundException thing(String thingId) {
        return of("设备", thingId);
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }
}
