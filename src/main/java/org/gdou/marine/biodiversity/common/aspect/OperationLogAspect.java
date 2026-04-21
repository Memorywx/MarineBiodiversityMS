package org.gdou.marine.biodiversity.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.gdou.marine.biodiversity.common.annotation.LogOperation;
import org.gdou.marine.biodiversity.entity.OperationLog;
import org.gdou.marine.biodiversity.mapper.OperationLogMapper;
import org.gdou.marine.biodiversity.security.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
@Component
public class OperationLogAspect {

    private final OperationLogMapper operationLogMapper;
    private final ObjectMapper objectMapper;

    public OperationLogAspect(OperationLogMapper operationLogMapper, ObjectMapper objectMapper) {
        this.operationLogMapper = operationLogMapper;
        this.objectMapper = objectMapper;
    }

    @Pointcut("@annotation(org.gdou.marine.biodiversity.common.annotation.LogOperation)")
    public void logPointCut() {}

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        LogOperation logAnnotation = method.getAnnotation(LogOperation.class);

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        OperationLog operationLog = new OperationLog();
        operationLog.setOperation(logAnnotation.value());
        if (request != null) {
            operationLog.setMethod(request.getMethod());
            operationLog.setRequestUrl(request.getRequestURI());
            operationLog.setIpAddress(getIpAddress(request));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser user) {
            operationLog.setUserId(user.getId());
            operationLog.setUsername(user.getUsername());
        }

        try {
            String params = objectMapper.writeValueAsString(point.getArgs());
            if (params.length() > 2000) {
                params = params.substring(0, 2000) + "...";
            }
            operationLog.setRequestParams(params);
        } catch (Exception e) {
            operationLog.setRequestParams("参数序列化失败");
        }

        Object result;
        try {
            result = point.proceed();
            operationLog.setStatus(1);
        } catch (Throwable e) {
            operationLog.setStatus(0);
            operationLog.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            operationLogMapper.insert(operationLog);
        }
        return result;
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
