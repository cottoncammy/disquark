package org.example.rest.request.ratelimit;

public interface RateLimitStrategy {

    static RateLimitStrategy global() {

    }

    static RateLimitStrategy bucket() {

    }

    static RateLimitStrategy GLOBAL_AND_BUCKET = null;

    static RateLimitStrategy NO_OP = null;
}
