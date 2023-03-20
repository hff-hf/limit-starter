package com.cookie.client.config;

import com.cookie.constants.RuleConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * 限流配置（配置文件加载类）
 *
 * @author cookie
 * @since 2023-03-18 23:53
 */
@ConfigurationProperties(prefix = RuleConstant.DEFAULT_PREFIX)
public class LimitConfig {
    /**
     * 是否开始配置文件形式，默认开启
     */
    private boolean enabled = RuleConstant.DEFAULT_ENABLE;

    /**
     * 加载切点
     */
    private String pointCut = "";

    @NestedConfigurationProperty //给使用配置文件用的
    private LimitRule limitRule = new LimitRule().init();

    /**
     * 具体方法实现限流
     */
    private Map<String, LimitRule> limitRuleMap = new HashMap<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPointCut() {
        return pointCut;
    }

    public void setPointCut(String pointCut) {
        this.pointCut = pointCut;
    }

    public LimitRule getLimitRule() {
        return limitRule;
    }

    public void setLimitRule(LimitRule limitRule) {
        this.limitRule = limitRule;
    }

    public Map<String, LimitRule> getLimitRuleMap() {
        return limitRuleMap;
    }

    public void setLimitRuleMap(Map<String, LimitRule> limitRuleMap) {
        this.limitRuleMap = limitRuleMap;
    }
}
