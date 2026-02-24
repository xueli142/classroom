# 后端代码重构优化计划

## 项目信息
- **项目名称**: 实验室预约管理系统
- **技术栈**: Spring Boot 3.1.5 + MyBatis Plus + PostgreSQL + Redis
- **优化目标**: 规范化代码结构，添加异常处理、枚举类，提升代码可维护性

---

## 一、现有问题分析

### 1.1 异常处理问题
- ❌ 无统一的异常处理机制（缺少 @ControllerAdvice）
- ❌ Service 层返回 Boolean 表示失败，而非抛出异常
- ❌ 异常信息不友好，缺少错误码规范
- ❌ 无业务异常类层次结构

### 1.2 枚举与常量问题
- ❌ 全项目仅有 1 个枚举类 `ToolStatus`
- ❌ 角色名称使用字符串魔法值（"admin", "student", "teacher"）
- ❌ 预订状态使用字符串魔法值
- ❌ 常量散落各处，无统一管理

### 1.3 响应格式问题
- ❌ 部分接口返回 `ResponseDto<T>`，部分返回 `Boolean` 或 `Map`
- ❌ `UserController.logout()` 返回 `Map<String, Object>`
- ❌ 成功/失败状态判断依赖返回值而非异常

### 1.4 代码规范问题
- ❌ 缺少参数验证（@Valid/@Validated）
- ❌ Controller 层直接使用 @RequestParam 而非 DTO
- ❌ Service 实现类命名不统一（部分在 service 子目录）
- ❌ 日志输出使用 System.out.println

---

## 二、优化方案

### 2.1 异常体系设计

#### 2.1.1 基础异常类结构
```
exception/
├── BaseException.java              # 基础异常类
├── BusinessException.java         # 业务异常基类
├── validation/
│   ├── ValidationException.java   # 参数验证异常
│   └── ValidationError.java       # 验证错误详情
├── auth/
│   ├── AuthenticationException.java  # 认证异常
│   └── AuthorizationException.java   # 授权异常
├── business/
│   ├── UserNotFoundException.java    # 用户不存在
│   ├── ResourceNotFoundException.java # 资源不存在
│   ├── DuplicateResourceException.java # 资源重复
│   └── OperationNotAllowedException.java # 操作不允许
└── system/
    ├── ExternalServiceException.java  # 外部服务异常
    └── FileOperationException.java    # 文件操作异常
```

#### 2.1.2 全局异常处理器
```
handler/
└── GlobalExceptionHandler.java   # @ControllerAdvice 统一异常处理
```

### 2.2 枚举体系设计

#### 2.2.1 错误码枚举
```
enums/
├── ErrorCode.java               # 错误码枚举接口
├── ResponseCode.java            # 通用响应码
├── UserErrorCode.java           # 用户模块错误码
├── BookingErrorCode.java        # 预订模块错误码
├── ClassroomErrorCode.java      # 教室模块错误码
└── AuthErrorCode.java           # 认证授权错误码
```

#### 2.2.2 业务枚举
```
enums/
├── UserRole.java                # 用户角色枚举
├── BookingStatus.java           # 预订状态枚举
├── CourseSlotType.java          # 课程时段类型枚举
├── ClassroomStatus.java         # 教室状态枚举
├── ApprovalStatus.java          # 审批状态枚举
└── TermStatus.java              # 学期状态枚举
```

### 2.3 统一响应设计

#### 2.3.1 响应结构标准化
```java
// 成功响应
{
    "code": 200,
    "message": "操作成功",
    "data": { ... },
    "timestamp": 1234567890
}

// 失败响应
{
    "code": 1001,
    "message": "用户不存在",
    "data": null,
    "timestamp": 1234567890
}
```

#### 2.3.2 响应工具类
```
util/
└── ResponseUtils.java           # 统一响应构建工具
```

### 2.4 包结构优化

#### 2.4.1 新增包结构
```
org.example.classroom/
├── exception/                   # 异常类包
├── enums/                       # 枚举类包
├── handler/                     # 处理器包
├── constant/                    # 常量类包
│   ├── SecurityConstants.java
│   ├── JwtConstants.java
│   └── FileConstants.java
├── validation/                  # 验证相关
│   ├── annotation/             # 自定义验证注解
│   └── validator/              # 自定义验证器
└── aspect/                     # AOP 切面
    └── LoggingAspect.java
```

---

## 三、实施步骤

### Phase 1: 基础设施层（优先级：高）

#### 步骤 1.1: 创建异常体系
- [ ] 创建 `BaseException.java` 基础异常类
- [ ] 创建 `BusinessException.java` 业务异常类
- [ ] 创建具体业务异常类（UserNotFound, ResourceNotFound 等）
- [ ] 创建 `GlobalExceptionHandler.java` 全局异常处理器

#### 步骤 1.2: 创建错误码枚举
- [ ] 创建 `ErrorCode.java` 接口
- [ ] 创建 `ResponseCode.java` 通用响应码
- [ ] 创建各模块错误码枚举

#### 步骤 1.3: 创建业务枚举
- [ ] 创建 `UserRole.java` 替代字符串角色
- [ ] 创建 `BookingStatus.java` 替代字符串状态
- [ ] 创建其他业务枚举

### Phase 2: 响应规范化（优先级：高）

#### 步骤 2.1: 统一响应格式
- [ ] 创建 `ResponseUtils.java` 工具类
- [ ] 优化 `ResponseDto.java` 设计
- [ ] 全局异常处理器返回统一格式

#### 步骤 2.2: 重构 Controller 返回值
- [ ] 统一所有 Controller 方法返回 `ResponseDto<T>`
- [ ] 移除返回 Boolean/Map 的方法
- [ ] Service 层失败改为抛异常

### Phase 3: 参数验证（优先级：中）

#### 步骤 3.1: 添加验证注解
- [ ] DTO 类添加 `@NotNull`, `@NotBlank`, `@Pattern` 等注解
- [ ] 创建自定义验证注解（如 `@ValidPhone`）
- [ ] Controller 方法添加 `@Valid` / `@Validated`

#### 步骤 3.2: 验证异常处理
- [ ] 全局异常处理器处理 `MethodArgumentNotValidException`
- [ ] 返回友好的验证错误信息

### Phase 4: 常量提取（优先级：中）

#### 步骤 4.1: 提取常量类
- [ ] 创建 `SecurityConstants.java`
- [ ] 创建 `JwtConstants.java`
- [ ] 创建 `FileConstants.java`
- [ ] 移除代码中的魔法值

### Phase 5: Service 层重构（优先级：中）

#### 步骤 5.1: 重构返回值
- [ ] Service 方法失败时抛异常而非返回 false
- [ ] 使用 Optional 处理可能为空的结果
- [ ] 添加业务逻辑验证

#### 步骤 5.2: 事务管理
- [ ] 检查所有 `@Transactional` 注解
- [ ] 明确事务传播行为
- [ ] 添加事务回滚规则

### Phase 6: 日志规范化（优先级：低）

#### 步骤 6.1: 替换 System.out.println
- [ ] 引入 SLF4J Logger
- [ ] 创建日志切面（可选）
- [ ] 规范日志级别使用

---

## 四、详细设计

### 4.1 异常类设计示例

#### BaseException.java
```java
public abstract class BaseException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Object[] args;

    protected BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.args = null;
    }

    protected BaseException(ErrorCode errorCode, Object... args) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.args = args;
    }

    public int getCode() {
        return errorCode.getCode();
    }

    public String getMessageKey() {
        return errorCode.getKey();
    }
}
```

#### BusinessException.java
```java
public class BusinessException extends BaseException {
    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BusinessException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
```

#### UserNotFoundException.java
```java
public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String userId) {
        super(UserErrorCode.USER_NOT_FOUND, userId);
    }
}
```

### 4.2 错误码枚举示例

#### ErrorCode.java
```java
public interface ErrorCode {
    int getCode();
    String getKey();
    String getMessage();
}
```

#### ResponseCode.java
```java
@Getter
@AllArgsConstructor
public enum ResponseCode implements ErrorCode {
    SUCCESS(200, "success", "操作成功"),
    BAD_REQUEST(400, "bad_request", "请求参数错误"),
    UNAUTHORIZED(401, "unauthorized", "未授权"),
    FORBIDDEN(403, "forbidden", "禁止访问"),
    NOT_FOUND(404, "not_found", "资源不存在"),
    INTERNAL_ERROR(500, "internal_error", "系统内部错误");

    private final int code;
    private final String key;
    private final String message;
}
```

#### UserErrorCode.java
```java
@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(1001, "user.not_found", "用户不存在: %s"),
    INVALID_PASSWORD(1002, "user.invalid_password", "密码错误"),
    USER_DISABLED(1003, "user.disabled", "用户已被禁用"),
    DUPLICATE_USERNAME(1004, "user.duplicate_username", "用户名已存在"),
    INVALID_PHONE(1005, "user.invalid_phone", "手机号格式错误");

    private final int code;
    private final String key;
    private final String message;
}
```

### 4.3 全局异常处理器

#### GlobalExceptionHandler.java
```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseDto<Void> handleBusinessException(BusinessException e) {
        log.warn("Business exception: {}", e.getMessage());
        return ResponseDto.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseDto<Void> handleAuthenticationException(AuthenticationException e) {
        log.warn("Authentication failed: {}", e.getMessage());
        return ResponseDto.error(AuthErrorCode.AUTHENTICATION_FAILED, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream()
            .map(ObjectError::getDefaultMessage)
            .collect(Collectors.joining(", "));
        log.warn("Validation failed: {}", message);
        return ResponseDto.error(ResponseCode.BAD_REQUEST.getCode(), message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseDto<Void> handleException(Exception e) {
        log.error("Unexpected error", e);
        return ResponseDto.error(ResponseCode.INTERNAL_ERROR);
    }
}
```

### 4.4 业务枚举示例

#### UserRole.java
```java
@Getter
@AllArgsConstructor
public enum UserRole {
    ADMIN("admin", "管理员"),
    TEACHER("teacher", "教师"),
    STUDENT("student", "学生");

    private final String code;
    private final String description;

    public static UserRole fromCode(String code) {
        return Arrays.stream(values())
            .filter(r -> r.code.equals(code))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid role code: " + code));
    }
}
```

#### BookingStatus.java
```java
@Getter
@AllArgsConstructor
public enum BookingStatus {
    PENDING("pending", "待审批"),
    APPROVED("approved", "已批准"),
    REJECTED("rejected", "已拒绝"),
    CANCELLED("cancelled", "已取消"),
    COMPLETED("completed", "已完成");

    private final String code;
    private final String description;

    public static BookingStatus fromCode(String code) {
        return Arrays.stream(values())
            .filter(s -> s.code.equals(code))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid status code: " + code));
    }
}
```

---

## 五、迁移指南

### 5.1 Service 层重构示例

#### 重构前
```java
public LoginResponseDto login(LoginDto loginDto) {
    User user = userMapper.selectUserByUid(loginDto.getUid());
    if(user == null || !passwordUtil.matches(loginDto.getPassword(), user.getPassword())){
        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setMessage("账号不存在或密码错误");
        return responseDto;
    }
    // ...
}
```

#### 重构后
```java
public LoginResponseDto login(LoginDto dto) {
    User user = userMapper.selectUserByUid(dto.getUid())
        .orElseThrow(() -> new UserNotFoundException(dto.getUid()));

    if (!passwordUtil.matches(dto.getPassword(), user.getPassword())) {
        throw new InvalidPasswordException();
    }

    if (!user.getIsActive()) {
        throw new UserDisabledException(user.getUserId());
    }

    String jwt = jwtUtil.generateToken(user.getUserId(), user.getRole());
    return LoginResponseDto.builder()
        .token(jwt)
        .message("登录成功")
        .user(user)
        .build();
}
```

### 5.2 Controller 重构示例

#### 重构前
```java
@PostMapping("/changePassword")
public Boolean ChangePassword(@RequestBody ChangePasswordDto dto){
    return userService.changePassword(dto);
}
```

#### 重构后
```java
@PostMapping("/changePassword")
public ResponseDto<Void> changePassword(@Valid @RequestBody ChangePasswordDto dto) {
    userService.changePassword(dto);
    return ResponseUtils.success();
}
```

---

## 六、验证清单

### 功能验证
- [ ] 所有接口返回统一 ResponseDto 格式
- [ ] 异常情况正确抛出并返回错误码
- [ ] 参数验证正确生效
- [ ] 业务逻辑验证正确生效

### 代码质量
- [ ] 无 System.out.println
- [ ] 无魔法值（字符串、数字）
- [ ] 所有异常继承 BaseException
- [ ] Service 层不返回 Boolean 表示失败

### 测试覆盖
- [ ] 单元测试覆盖异常场景
- [ ] 集成测试覆盖 API 响应格式
- [ ] 边界条件测试

---

## 七、注意事项

1. **向后兼容性**: 逐步重构，避免一次性大改动
2. **数据库不变**: 不修改数据库结构
3. **API 兼容性**: 如需修改 API 响应格式，需同步调整前端
4. **测试先行**: 每个阶段完成后进行测试
5. **文档更新**: 及时更新 API 文档

---

## 八、时间估算（参考）

| 阶段 | 任务 | 预计文件数 | 预计工作量 |
|------|------|-----------|-----------|
| Phase 1 | 异常体系 + 错误码 | ~15 文件 | 基础架构 |
| Phase 2 | 响应规范化 | ~20 Controller | 重构工作 |
| Phase 3 | 参数验证 | ~30 DTO | 添加注解 |
| Phase 4 | 常量提取 | ~5 常量类 | 清理工作 |
| Phase 5 | Service 重构 | ~20 Service | 重构工作 |
| Phase 6 | 日志规范化 | 全项目 | 清理工作 |

**总计**: 约 90+ 文件需要创建或修改

---

*本计划可根据实际情况调整实施顺序和优先级*
