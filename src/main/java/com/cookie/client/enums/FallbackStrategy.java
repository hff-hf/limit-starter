package com.cookie.client.enums;

import lombok.Getter;

/**
 * 服务降级策略
 *
 * @author cookie
 * @since 2023-03-18 23:34
 */
@Getter
public enum FallbackStrategy {
    /**
     * 快速失败
     */
    FAST_FALL;
}
