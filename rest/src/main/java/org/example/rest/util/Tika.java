package org.example.rest.util;

public class Tika {

    public static String detect(byte[] b) {
        return LazyHolder.TIKA.detect(b);
    }

    private static class LazyHolder {
        static final org.apache.tika.Tika TIKA = new org.apache.tika.Tika();
    }

    private Tika() {
    }
}
