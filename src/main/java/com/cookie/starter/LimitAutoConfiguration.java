package com.cookie.starter;

import com.cookie.client.config.LimitConfig;
import com.cookie.client.core.TokenBucketLimiter;
import com.cookie.client.handler.AnnotationLimiterHandler;
import com.cookie.client.inter.RateLimiter;
import com.cookie.constants.RuleConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * starter
 *
 * @author cookie
 * @since 2023-03-18 23:56
 */
@Configuration
@EnableConfigurationProperties(value = LimitConfig.class)
@ConditionalOnProperty(prefix = RuleConstant.DEFAULT_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class LimitAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RateLimiter.class) // 为了后续扩展其他限流器
    public RateLimiter rateLimiter() {
        return new TokenBucketLimiter();
    }

    @Bean
    public AnnotationLimiterHandler annotationLimiterHandler() {
        return new AnnotationLimiterHandler();
    }
}
