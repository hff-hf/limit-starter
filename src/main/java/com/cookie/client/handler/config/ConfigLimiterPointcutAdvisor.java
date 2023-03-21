package com.cookie.client.handler.config;

import com.cookie.client.config.LimitConfig;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 配置限流 切点
 *
 * @author cookie
 * @since 2023-03-20 22:43
 */
public class ConfigLimiterPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    @Autowired
    private LimitConfig limitConfig;

    @Override
    public Pointcut getPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(limitConfig.getPointCut());
        return pointcut;
    }
}
