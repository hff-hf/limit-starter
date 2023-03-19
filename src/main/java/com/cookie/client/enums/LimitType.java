package com.cookie.client.enums;

import lombok.Getter;

/**
 * 限流类型
 *
 * @author cookie
 * @since 2023-03-18 23:29
 */
@Getter
public enum LimitType {

    /**
     * 用户自定义
     */
    CUSTOMER,

    /**
     * IP
     */
    IP;
}
