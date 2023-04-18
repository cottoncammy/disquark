package io.disquark.rest.util;

public class Hex {

    public static String encode(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static byte[] decode(String s) {
        int len = s.length();
        byte[] b = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            b[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return b;
    }

    private Hex() {
    }
}
