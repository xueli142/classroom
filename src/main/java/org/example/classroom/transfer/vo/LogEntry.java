package org.example.classroom.transfer.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEntry {
    private String traceId;          // 链路追踪
    private String uri;
    private String httpMethod;
    private String classMethod;      // com.xx.XxController#save(Long)
    private Object[] args;           // 入参
    private Object result;           // 出参
    private Long cost;               // 耗时 ms
    private String exception;        // 异常堆栈
    private String user;             // 登录人
}

