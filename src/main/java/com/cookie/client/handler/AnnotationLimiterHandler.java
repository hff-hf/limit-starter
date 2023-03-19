package com.cookie.client.handler;

import com.cookie.client.annotation.Limit;
import com.cookie.client.config.LimitRule;
import com.cookie.client.exception.LimitException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * AOP切面注解 -- 拦截器
 *
 * @author cookie
 * @since 2023-03-19 12:20
 */
@Aspect
@Component
public class AnnotationLimiterHandler extends AbstractLimiterHandler<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationLimiterHandler.class);

    @Around("@annotation(limit)")
    public Object limitAround(ProceedingJoinPoint joinPoint, Limit limit) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getName();
        String methodName = methodSignature.getMethod().getName();
        String defaultKet = StringUtils.join(className, ":", methodName);
        // 构建限流规则
        LimitRule limitRule = LimitRule.builder()
                .defaultKey(defaultKet)
                .prefix(limit.prefix())
                .cacheKey(limit.cacheKey())
                .count(limit.count())
                .period(limit.period())
                .limitType(limit.limitType())
                .fallbackStrategy(limit.fallbackStrategy())
                .build();
        return handler(limitRule, () -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                LOGGER.error("目标服务执行异常");
                throw new LimitException(e);
            }
        });
    }
}
