package com.cookie.client.inter;

import com.google.common.collect.ImmutableList;


/**
 * 限流接口
 *
 * @author cookie
 * @since 2023-03-19 10:31
 */
public interface RateLimiter {

    boolean tryAcquire(ImmutableList<String> keys, int count);

    boolean tryAcquire(ImmutableList<String> keys, int count, long period);

    boolean tryAcquire(ImmutableList<String> keys, long nowTime, int count, long period);
}
