package com.cookie.client.core;

import com.google.common.collect.ImmutableList;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 令牌桶限流
 *
 * @author cookie
 * @since 2023-03-19 11:23
 */
public class TokenBucketLimiter extends AbstractRateLimiter {

    @Override
    protected boolean acquire(ImmutableList<String> keys, int limitCount, long limitPeriod, TimeUnit timeUnit) {
        return Optional
                .ofNullable(limitRedisTemplate.execute(redisScript, keys, limitCount, limitPeriod, timeUnit))
                .map(res -> res == 1)
                .orElse(false);
    }

    @Override
    protected String loadLuaScript() {
        return "classpath:/script/TokenBucket.lua";
    }
}
