package com.zyh.easyapplyresume.selfannotation.controller.RateLimit;

public class RateLimitException extends RuntimeException {
    
    public RateLimitException(String message) {
        super(message);
    }
}
