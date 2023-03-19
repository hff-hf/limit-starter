package com.cookie.constants;

import java.util.concurrent.TimeUnit;

/**
 * 限流规则常量
 *
 * @author cookie
 * @since 2023-03-18 23:43
 */
public class RuleConstant {
    /**
     * 前缀--(配置文件 and 缓存key)
     */
    public static final String DEFAULT_PREFIX = "limit";

    /**
     * 默认开启配置文件形式限流（优先使用配置文件限流规则）
     */
    public static final boolean DEFAULT_ENABLE = Boolean.TRUE;

    /**
     * 默认一个周期内 最大访问数 10次
     */
    public static final int DEFAULT_COUNT = 10;

    /**
     * 默认周期为 1
     */
    public static final int DEFAULT_PERIOD = 1;

    /**
     * 一个周期单位默认为 秒
     */
    public static final TimeUnit DEFAULT_UNIT = TimeUnit.SECONDS;
}
