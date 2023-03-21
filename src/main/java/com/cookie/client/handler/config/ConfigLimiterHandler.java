package com.cookie.client.handler.config;

import com.cookie.client.config.LimitConfig;
import com.cookie.client.config.LimitRule;
import com.cookie.client.handler.AbstractLimiterHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * 配置文件限流拦截器
 *
 * @author cookie
 * @since 2023-03-20 22:40
 */
public class ConfigLimiterHandler extends AbstractLimiterHandler<Object> implements MethodInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigLimiterHandler.class);

    @Autowired
    private LimitConfig limitConfig;

    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        String className = invocation.getClass().getName(); // 类名
        String methodName = invocation.getMethod().getName(); // 方法名
        if (Objects.isNull(limitConfig.getLimitRuleMap().get(methodName))) {
            return invocation.proceed();
        }
        // 获取配置文件中的 限流规则
        LimitRule limitRule = limitConfig.getLimitRuleMap().get(methodName);
        limitRule.setDefaultKey(StringUtils.join(className, ":", methodName));
        return handler(limitConfig.getLimitRuleMap().get(methodName), () -> {
            try {
                return invocation.proceed();
            } catch (Throwable e) {
                LOGGER.error("[ConfigLimitHandler限流交易请求],目标方法执行失败 :{}", e.getMessage());
            }
            return null;
        });
    }
}
