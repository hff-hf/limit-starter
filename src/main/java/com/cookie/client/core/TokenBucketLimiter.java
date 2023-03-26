package com.cookie.client.core;

import com.google.common.collect.ImmutableList;

import java.util.Optional;

/**
 * 令牌桶限流
 *
 * @author cookie
 * @since 2023-03-19 11:23
 */
public class TokenBucketLimiter extends AbstractRateLimiter {

    @Override
    protected boolean acquire(ImmutableList<String> keys, long nowTime, int limitCount, long limitPeriod) {
        return Optional
                .ofNullable(limitRedisTemplate.execute(redisScript, keys, nowTime, limitCount, limitPeriod))
                .map(res -> res >= 0)
                .orElse(false);
    }

    @Override
    protected String loadLuaScript() {
        return "classpath:/script.Customer";
    }
}
