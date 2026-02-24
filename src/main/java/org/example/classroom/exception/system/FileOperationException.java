package org.example.classroom.exception.system;

import org.example.classroom.enums.SystemErrorCode;

/**
 * 文件操作异常
 * <p>
 * 当文件上传、删除、读取等操作失败时抛出此异常
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
public class FileOperationException extends org.example.classroom.exception.BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * 文件名
     */
    private final String filename;

    /**
     * 构造函数
     *
     * @param errorCode 错误码枚举
     * @param filename  文件名
     */
    public FileOperationException(SystemErrorCode errorCode, String filename) {
        super(errorCode, filename);
        this.filename = filename;
    }

    /**
     * 构造函数 - 包含原始异常
     *
     * @param errorCode 错误码枚举
     * @param filename  文件名
     * @param cause     原始异常
     */
    public FileOperationException(SystemErrorCode errorCode, String filename, Throwable cause) {
        super(errorCode, cause, filename);
        this.filename = filename;
    }

    /**
     * 文件上传失败
     *
     * @param filename 文件名
     */
    public static FileOperationException uploadFailed(String filename) {
        return new FileOperationException(SystemErrorCode.FILE_UPLOAD_FAILED, filename);
    }

    /**
     * 文件上传失败 - 包含原始异常
     *
     * @param filename 文件名
     * @param cause    原始异常
     */
    public static FileOperationException uploadFailed(String filename, Throwable cause) {
        return new FileOperationException(SystemErrorCode.FILE_UPLOAD_FAILED, filename, cause);
    }

    /**
     * 文件删除失败
     *
     * @param filename 文件名
     */
    public static FileOperationException deleteFailed(String filename) {
        return new FileOperationException(SystemErrorCode.FILE_DELETE_FAILED, filename);
    }

    /**
     * 文件不存在
     *
     * @param filename 文件名
     */
    public static FileOperationException notFound(String filename) {
        return new FileOperationException(SystemErrorCode.FILE_NOT_FOUND, filename);
    }

    /**
     * 文件格式不支持
     *
     * @param filename   文件名
     * @param extensions 支持的扩展名
     */
    public static FileOperationException unsupportedFormat(String filename, String... extensions) {
        return new FileOperationException(
                SystemErrorCode.FILE_FORMAT_NOT_SUPPORTED,
                filename + "（支持格式：" + String.join(", ", extensions) + "）"
        );
    }

    /**
     * 文件大小超限
     *
     * @param filename    文件名
     * @param maxSize     最大大小
     * @param actualSize  实际大小
     */
    public static FileOperationException sizeExceeded(String filename, String maxSize, String actualSize) {
        return new FileOperationException(
                SystemErrorCode.FILE_SIZE_EXCEEDED,
                String.format("%s（最大：%s，实际：%s）", filename, maxSize, actualSize)
        );
    }

    public String getFilename() {
        return filename;
    }
}
