package com.cookie.client.handler.config;

import com.cookie.client.handler.AbstractLimiterHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 配置文件限流拦截器
 *
 * @author cookie
 * @since 2023-03-20 22:40
 */
public class ConfigLimiterHandler extends AbstractLimiterHandler<Object> implements MethodInterceptor {

    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {

        return null;
    }
}
