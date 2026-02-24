package org.example.classroom.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.example.classroom.exception.BaseException;
import org.example.classroom.exception.auth.AuthenticationException;
import org.example.classroom.exception.auth.AuthorizationException;
import org.example.classroom.exception.business.OperationNotAllowedException;
import org.example.classroom.exception.system.ExternalServiceException;
import org.example.classroom.exception.validation.ValidationException;
import org.example.classroom.transfer.dto.besides.ResponseDto;
import org.example.classroom.enums.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * <p>
 * 统一处理系统中抛出的各类异常，返回标准的 ResponseDto 格式响应
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理业务异常（BusinessException 及其子类）
     */
    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Void> handleBaseException(BaseException e) {
        log.warn("Business exception: code={}, message={}", e.getCode(), e.getMessage());
        return ResponseDto.error(e.getCode(), e.getFormattedMessage());
    }

    /**
     * 处理认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseDto<Void> handleAuthenticationException(AuthenticationException e) {
        log.warn("Authentication failed: {}", e.getMessage());
        return ResponseDto.error(e.getCode(), e.getFormattedMessage());
    }

    /**
     * 处理授权异常
     */
    @ExceptionHandler({AuthorizationException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseDto handleAuthorizationException(Exception e) {
        log.warn("Authorization failed: {}", e.getMessage());
        if (e instanceof AuthorizationException) {
            AuthorizationException ae = (AuthorizationException) e;
            return ResponseDto.error(ae.getCode(), ae.getFormattedMessage());
        }
        return ResponseDto.error(String.valueOf(ResponseCode.FORBIDDEN));
    }

    /**
     * 处理参数验证异常（@Valid 注解触发）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError) {
                        FieldError fieldError = (FieldError) error;
                        return fieldError.getField() + ": " + error.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .collect(Collectors.joining("; "));
        log.warn("Validation failed: {}", message);
        return ResponseDto.error(ResponseCode.BAD_REQUEST.getCode(), message);
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Void> handleBindException(BindException e) {
        String message = e.getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError) {
                        FieldError fieldError = (FieldError) error;
                        return fieldError.getField() + ": " + error.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .collect(Collectors.joining("; "));
        log.warn("Bind exception: {}", message);
        return ResponseDto.error(ResponseCode.BAD_REQUEST.getCode(), message);
    }

    /**
     * 处理约束违反异常（@Validated 单个参数验证）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        log.warn("Constraint violation: {}", message);
        return ResponseDto.error(ResponseCode.BAD_REQUEST.getCode(), message);
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String message = String.format("参数类型错误: %s 应为 %s 类型",
                e.getName(), e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown");
        log.warn("Type mismatch: {}", message);
        return ResponseDto.error(ResponseCode.BAD_REQUEST.getCode(), message);
    }

    /**
     * 处理 404 异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDto handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.warn("No handler found: {}", e.getRequestURL());
        return ResponseDto.error(String.valueOf(ResponseCode.NOT_FOUND));
    }

    /**
     * 处理操作不允许异常
     */
    @ExceptionHandler(OperationNotAllowedException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Void> handleOperationNotAllowedException(OperationNotAllowedException e) {
        log.warn("Operation not allowed: {}", e.getMessage());
        return ResponseDto.error(e.getCode(), e.getFormattedMessage());
    }

    /**
     * 处理外部服务异常
     */
    @ExceptionHandler(ExternalServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto<Void> handleExternalServiceException(ExternalServiceException e) {
        log.error("External service error: service={}, message={}",
                e.getServiceName(), e.getMessage(), e);
        return ResponseDto.error(e.getCode(), e.getFormattedMessage());
    }

    /**
     * 处理验证异常
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Void> handleValidationException(ValidationException e) {
        log.warn("Validation exception: {}", e.getMessage());
        return ResponseDto.error(e.getCode(), e.getFormattedMessage());
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Illegal argument: {}", e.getMessage());
        return ResponseDto.error(ResponseCode.BAD_REQUEST.getCode(),
                e.getMessage() != null ? e.getMessage() : "参数错误");
    }

    /**
     * 处理非法状态异常
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto<Void> handleIllegalStateException(IllegalStateException e) {
        log.error("Illegal state: {}", e.getMessage(), e);
        return ResponseDto.error(ResponseCode.INTERNAL_ERROR.getCode(),
                e.getMessage() != null ? e.getMessage() : "系统状态错误");
    }

    /**
     * 兜底处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto handleException(Exception e) {
        log.error("Unexpected error occurred", e);
        return ResponseDto.error(String.valueOf(ResponseCode.INTERNAL_ERROR));
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto handleRuntimeException(RuntimeException e) {
        log.error("Runtime error occurred: {}", e.getMessage(), e);
        return ResponseDto.error(String.valueOf(ResponseCode.INTERNAL_ERROR));
    }
}
