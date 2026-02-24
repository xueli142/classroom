package org.example.classroom.exception.business;

import org.example.classroom.enums.ErrorCode;

/**
 * 资源重复异常
 * <p>
 * 当创建或更新资源时，资源已存在（如用户名重复、教室编号重复等）
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
public class DuplicateResourceException extends org.example.classroom.exception.BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * 资源类型
     */
    private final String resourceType;

    /**
     * 资源标识
     */
    private final String resourceIdentifier;

    /**
     * 构造函数
     *
     * @param errorCode          错误码枚举
     * @param resourceType       资源类型
     * @param resourceIdentifier 资源标识
     */
    public DuplicateResourceException(ErrorCode errorCode, String resourceType, String resourceIdentifier) {
        super(errorCode, resourceType, resourceIdentifier);
        this.resourceType = resourceType;
        this.resourceIdentifier = resourceIdentifier;
    }

    /**
     * 创建资源重复异常
     *
     * @param resourceType       资源类型
     * @param resourceIdentifier 资源标识
     */
    public static DuplicateResourceException of(String resourceType, String resourceIdentifier) {
        return new DuplicateResourceException(
                org.example.classroom.enums.BusinessErrorCode.DUPLICATE_RESOURCE,
                resourceType,
                resourceIdentifier
        );
    }

    /**
     * 用户名已存在
     */
    public static DuplicateResourceException username(String username) {
        return of("用户名", username);
    }

    /**
     * 手机号已存在
     */
    public static DuplicateResourceException phone(String phone) {
        return of("手机号", phone);
    }

    /**
     * 教室编号已存在
     */
    public static DuplicateResourceException classroomNumber(String number) {
        return of("教室编号", number);
    }

    /**
     * 课程名称已存在
     */
    public static DuplicateResourceException courseName(String name) {
        return of("课程名称", name);
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceIdentifier() {
        return resourceIdentifier;
    }
}
