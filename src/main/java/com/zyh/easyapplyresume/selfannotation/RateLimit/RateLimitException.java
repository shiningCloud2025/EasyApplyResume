package com.zyh.easyapplyresume.selfannotation.RateLimit;

public class RateLimitException extends RuntimeException {
    
    public RateLimitException(String message) {
        super(message);
    }
}
