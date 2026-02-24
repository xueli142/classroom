package org.example.classroom.constant;

/**
 * 文件相关常量
 * <p>
 * 定义文件上传、下载相关的常量
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
public final class FileConstants {

    private FileConstants() {
        throw new UnsupportedOperationException("Constant class cannot be instantiated");
    }

    // ========== 文件上传目录 ==========
    /**
     * 图片上传目录
     */
    public static final String IMAGE_UPLOAD_DIR = "src/main/resources/static/image";

    /**
     * 默认图片访问路径前缀
     */
    public static final String DEFAULT_IMAGE_PATH_PREFIX = "/image/";

    // ========== 文件大小限制 ==========
    /**
     * 最大文件大小（10MB）
     */
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    /**
     * 最大图片大小（5MB）
     */
    public static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;

    // ========== 允许的文件扩展名 ==========
    /**
     * 允许的图片扩展名
     */
    public static final String[] ALLOWED_IMAGE_EXTENSIONS = {
            ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"
    };

    /**
     * 允许的文档扩展名
     */
    public static final String[] ALLOWED_DOCUMENT_EXTENSIONS = {
            ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx"
    };

    // ========== MIME 类型 ==========
    /**
     * JPEG MIME 类型
     */
    public static final String MIME_TYPE_JPEG = "image/jpeg";

    /**
     * PNG MIME 类型
     */
    public static final String MIME_TYPE_PNG = "image/png";

    /**
     * GIF MIME 类型
     */
    public static final String MIME_TYPE_GIF = "image/gif";

    // ========== 错误消息 ==========
    /**
     * 文件上传失败消息
     */
    public static final String MSG_UPLOAD_FAILED = "文件上传失败";

    /**
     * 文件删除失败消息
     */
    public static final String MSG_DELETE_FAILED = "文件删除失败";

    /**
     * 文件不存在消息
     */
    public static final String MSG_FILE_NOT_FOUND = "文件不存在";

    /**
     * 文件格式不支持消息
     */
    public static final String MSG_FORMAT_NOT_SUPPORTED = "文件格式不支持";

    /**
     * 文件大小超限消息
     */
    public static final String MSG_SIZE_EXCEEDED = "文件大小超过限制";

    // ========== 请求参数 ==========
    /**
     * 文件上传请求参数名
     */
    public static final String PARAM_FILE = "file";

    /**
     * 用户 ID 请求参数名
     */
    public static final String PARAM_USER_ID = "userId";

    /**
     * 旧文件名请求参数名
     */
    public static final String PARAM_OLD_NAME = "oldName";
}
