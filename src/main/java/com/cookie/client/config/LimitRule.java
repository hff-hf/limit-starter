package com.cookie.client.config;

import com.cookie.client.enums.FallbackStrategy;
import com.cookie.client.enums.LimitType;
import com.cookie.constants.RuleConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * 限流规则
 *
 * @author cookie
 * @since 2023-03-18 23:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LimitRule {

    /**
     * 默认是类名+方法名
     */
    private String defaultKey;

    private String prefix;

    private String cacheKey;

    private int count;

    /**
     * 周期 单位 毫秒
     */
    private long period;

    private TimeUnit unit;

    private LimitType limitType;

    private FallbackStrategy fallbackStrategy;


    /**
     * 加载默认值
     */
    public LimitRule init() {
        this.setPrefix(RuleConstant.DEFAULT_PREFIX);
        this.setCount(RuleConstant.DEFAULT_COUNT);
        this.setPeriod(RuleConstant.DEFAULT_PERIOD);
        this.setUnit(TimeUnit.SECONDS);
        this.setLimitType(LimitType.CUSTOMER);
        this.setFallbackStrategy(FallbackStrategy.FAST_FALL);
        return this;
    }
}
