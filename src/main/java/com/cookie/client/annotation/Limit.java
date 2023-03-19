package com.cookie.client.annotation;

import com.cookie.client.enums.FallbackStrategy;
import com.cookie.client.enums.LimitType;
import com.cookie.constants.RuleConstant;

import java.util.concurrent.TimeUnit;

/**
 * 限流注解
 *
 * @author cookie
 * @since 2023-03-18 23:35
 */
public @interface Limit {
    /**
     * 注解名字
     */
    String name() default "";

    /**
     * 缓存key前缀
     */
    String prefix() default RuleConstant.DEFAULT_PREFIX;

    /**
     * 缓存key
     */
    String cacheKey() default "";

    /**
     * 一个周期内最大访问数
     */
    int count() default RuleConstant.DEFAULT_COUNT;

    /**
     * 周期
     */
    long period() default RuleConstant.DEFAULT_PERIOD;

    /**
     * 限流类型 （自定义 Ip）默认自定义
     */
    LimitType limitType() default LimitType.CUSTOMER;

    /**
     * 超出周期限定后 服务拒绝策略
     */
    FallbackStrategy fallbackStrategy() default FallbackStrategy.FAST_FALL;
}
