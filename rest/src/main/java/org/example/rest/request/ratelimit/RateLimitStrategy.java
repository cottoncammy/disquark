package org.example.rest.request.ratelimit;

// TODO
public interface RateLimitStrategy {

    static Object NONE = null;

    static Object GLOBAL = null;

    static Object BUCKET = null;

    static Object GLOBAL_AND_BUCKET = null;
}
