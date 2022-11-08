package com.tz.conf;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author lhucstart
 * @date 2022-11-08 14:16
 */
@Slf4j
@Component
@Aspect
public class RequestLogAspect {

    @Around("execution(* com.tz.controller..*.*(..)) ")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (!ObjectUtils.isEmpty(attributes)) {
            HttpServletRequest request = attributes.getRequest();
            log.info("拦截请求URI====={}", request.getRequestURI());
        }
        Object[] args = proceedingJoinPoint.getArgs();
        Object obj = proceedingJoinPoint.proceed();
        String reqStr = Arrays.toString(args);
        String objStr = JSONUtil.toJsonStr(obj);
        log.info("reqJson: {}", reqStr);
        log.info("obj:{}", objStr);
        return obj;
    }

}
