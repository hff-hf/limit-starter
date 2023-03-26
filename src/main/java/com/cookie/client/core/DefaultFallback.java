package com.cookie.client.core;

import com.cookie.client.inter.Fallback;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认服务降级（快速失败）
 *
 * @author cookie
 * @since 2023-03-23 20:58
 */
public class DefaultFallback implements Fallback<Map<String, String>> {
    @Override
    public Map<String, String> getFallback() {
        Map<String, String> map = new HashMap<>(2);
        map.put("code", "3241");
        map.put("msg", "接口限流中....");
        return map;
    }
}
