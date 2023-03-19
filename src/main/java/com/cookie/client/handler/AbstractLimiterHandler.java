package com.cookie.client.handler;

import com.cookie.client.config.LimitRule;
import com.cookie.client.enums.FallbackStrategy;
import com.cookie.client.enums.LimitType;
import com.cookie.client.inter.RateLimiter;
import com.cookie.constants.RuleConstant;
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
    private RateLimiter rateLimiter;

    protected Object handler(LimitRule limitRule, Supplier<T> fun) {
        buildLimitRule(limitRule);
        ImmutableList<String> keys = ImmutableList.of(limitRule.getCacheKey());
        if (rateLimiter.tryAcquire(keys, limitRule.getCount(), limitRule.getPeriod(), limitRule.getUnit())) {
            LOGGER.info("获取令牌成功～～～，开始执行目标方法.....");
            return fun.get();
        }
        LOGGER.info("获取令牌失败，开始执行服务降级策略.....");
        // 目前只有快速失败
        HashMap<String, String> map = new HashMap<>();
        map.put("500", "操作太频繁");
        return map;
    }

    private void buildLimitRule(LimitRule limitRule) {
        limitRule.setPrefix(StringUtils.isBlank(limitRule.getPrefix()) ? RuleConstant.DEFAULT_PREFIX : limitRule.getPrefix());
        limitRule.setCount(limitRule.getCount() < 1 ? RuleConstant.DEFAULT_COUNT : limitRule.getCount());
        limitRule.setPeriod(limitRule.getPeriod() < 1 ? RuleConstant.DEFAULT_PERIOD : RuleConstant.DEFAULT_UNIT.toSeconds(limitRule.getPeriod()));
        limitRule.setLimitType(Optional.ofNullable(limitRule.getLimitType()).orElse(LimitType.CUSTOMER));
        limitRule.setFallbackStrategy(Optional.ofNullable(limitRule.getFallbackStrategy()).orElse(FallbackStrategy.FAST_FALL));
        String cacheKey;
        // 使用switch方便后续扩展
        switch (limitRule.getLimitType()) {
            case IP:
                // 工具类 获取Ip -- TODO
                cacheKey = "Ip";
                break;
            default:
                cacheKey = StringUtils.isBlank(limitRule.getCacheKey()) ? limitRule.getDefaultKey() : limitRule.getCacheKey();
        }
        limitRule.setCacheKey(cacheKey);
    }
}