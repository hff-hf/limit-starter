package com.cookie.client.inter;


/**
 * 服务降级策略接口
 *
 * @author cookie
 * @since 2023-03-19 10:34
 */
public interface Fallback<T> {

    default T getFallback() {
        return null;
    }
}
