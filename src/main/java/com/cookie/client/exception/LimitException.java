package com.cookie.client.exception;

/**
 * 限流异常类
 *
 * @author cookie
 * @since 2023-03-19 10:59
 */
public class LimitException extends RuntimeException {

    public LimitException(String message) {
        super(message);
    }

    public LimitException(Throwable cause) {
        super(cause);
    }
}
