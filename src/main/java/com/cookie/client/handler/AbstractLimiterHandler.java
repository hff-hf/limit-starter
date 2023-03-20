package com.cookie.client.handler;

import com.cookie.client.config.LimitConfig;
import com.cookie.client.config.LimitRule;
import com.cookie.client.inter.RateLimiter;
import com.cookie.client.util.WebUtil;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 限流器抽象类
 *
 * @author cookie
 * @since 2023-03-19 16:06
 */
public abstract class AbstractLimiterHandler<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLimiterHandler.class);

    @Autowired
    private LimitConfig limitConfig;

    @Autowired
    private RateLimiter rateLimiter;

    protected Object handler(LimitRule limitRule, Supplier<T> function) {
        buildLimitRule(limitRule);
        ImmutableList<String> keys = ImmutableList.of(limitRule.getCacheKey());
        if (rateLimiter.tryAcquire(keys, limitRule.getCount(), limitRule.getPeriod())) {
            LOGGER.info("获取令牌成功～～～，开始执行目标方法.....");
            return function.get();
        }
        LOGGER.info("获取令牌失败，开始执行服务降级策略.....");
        // 目前只有快速失败
        HashMap<String, String> map = new HashMap<>();
        map.put("500", "操作太频繁");
        return map;
    }

    private void buildLimitRule(LimitRule limitRule) {
        limitRule.setPrefix(StringUtils.isBlank(limitRule.getPrefix()) ? limitConfig.getLimitRule().getPrefix() : limitRule.getPrefix());
        limitRule.setCount(limitRule.getCount() < 1 ? limitConfig.getLimitRule().getCount() : limitRule.getCount());
        limitRule.setPeriod(limitRule.getPeriod() < 1 ? limitConfig.getLimitRule().getPeriod() : limitConfig.getLimitRule().getUnit().toSeconds(limitRule.getPeriod()));
        limitRule.setLimitType(Optional.ofNullable(limitRule.getLimitType()).orElse(limitConfig.getLimitRule().getLimitType()));
        limitRule.setFallbackStrategy(Optional.ofNullable(limitRule.getFallbackStrategy()).orElse(limitConfig.getLimitRule().getFallbackStrategy()));
        String cacheKey;
        // 使用switch方便后续扩展
        switch (limitRule.getLimitType()) {
            case IP:
                cacheKey = StringUtils.join(limitRule.getPrefix(), ":", WebUtil.remoteIp());
                break;
            default:
                cacheKey = StringUtils.join(limitRule.getPrefix(), ":", StringUtils.isBlank(limitRule.getCacheKey()) ? limitRule.getDefaultKey() : limitRule.getCacheKey());
        }
        limitRule.setCacheKey(cacheKey);
    }
}
