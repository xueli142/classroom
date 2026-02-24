package org.example.classroom.exception.business;

import org.example.classroom.enums.UserErrorCode;

/**
 * 用户不存在异常
 * <p>
 * 当通过用户ID、用户名等查询用户时，用户不存在抛出此异常
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
public class UserNotFoundException extends org.example.classroom.exception.BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * 用户不存在
     *
     * @param userId 用户ID
     */
    public UserNotFoundException(String userId) {
        super(UserErrorCode.USER_NOT_FOUND, userId);
    }

    /**
     * 用户不存在（通过 uid）
     *
     * @param uid 用户标识
     * @return 异常实例
     */
    public static UserNotFoundException byUid(String uid) {
        return new UserNotFoundException(uid);
    }
}
