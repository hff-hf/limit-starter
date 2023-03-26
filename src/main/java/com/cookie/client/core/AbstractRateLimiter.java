package com.cookie.client.core;

import com.cookie.client.exception.LimitException;
import com.cookie.client.inter.RateLimiter;
import com.cookie.constants.RuleConstant;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * 限流器抽象类
 *
 * @author cookie
 * @since 2023-03-19 10:37
 */
public abstract class AbstractRateLimiter implements RateLimiter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRateLimiter.class);

    protected RedisScript<Long> redisScript;

    @Autowired
    @Qualifier(value = "limitRedis")
    protected RedisTemplate<String, Serializable> limitRedisTemplate;

    @Override
    public boolean tryAcquire(ImmutableList<String> keys, int count) {
        return tryAcquire(keys, count, RuleConstant.DEFAULT_COUNT);
    }

    @Override
    public boolean tryAcquire(ImmutableList<String> keys, int count, long period) {
        return tryAcquire(keys, System.currentTimeMillis(), count, period);
    }

    @Override
    public boolean tryAcquire(ImmutableList<String> keys, long nowTime, int count, long period) {
        return acquire(keys, nowTime, count, period);
    }

    /**
     * 具体限流实现
     * @param keys keys
     * @param nowTime nowTime
     * @param limitCount limitCount
     * @param limitPeriod limitPeriod
     * @return 是否通过
     */
    protected abstract boolean acquire(ImmutableList<String> keys, long nowTime, int limitCount, long limitPeriod);

    /**
     * 自类实现
     * @return lua脚本位置
     */
    protected abstract String loadLuaScript();

    @PostConstruct
    public void initLua() {
        redisScript = new DefaultRedisScript<>(buildLuaScript(), Long.class);
    }

    private String buildLuaScript() {
        ClassPathResource resource = new ClassPathResource(loadLuaScript());
        try (InputStream inputStream = resource.getInputStream()) {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("读取 LUA 脚本失败！！");
            throw new LimitException("读取 LUA 脚本失败");
        }
    }

}
