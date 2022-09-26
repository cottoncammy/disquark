package org.example.rest.request.ratelimit;


public interface RateLimitingStrategy {

    static Object NONE = null;

    static Object GLOBAL = null;

    static Object BUCKET = null;

    static Object GLOBAL_AND_BUCKET = null;
}
