package org.example.classroom.util;

public final class ExceptionUtils {

    /**
     * 把异常堆栈转成字符串，供日志使用
     */
    public static String getStackTrace(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(512);
        sb.append(throwable.toString()).append("\n");
        for (StackTraceElement e : throwable.getStackTrace()) {
            sb.append("\tat ").append(e).append('\n');
        }
        // 逐层打印 cause
        Throwable cause = throwable.getCause();
        while (cause != null) {
            sb.append("Caused by: ").append(cause).append('\n');
            for (StackTraceElement e : cause.getStackTrace()) {
                sb.append("\tat ").append(e).append('\n');
            }
            cause = cause.getCause();
        }
        return sb.toString();
    }

    private ExceptionUtils() {}
}