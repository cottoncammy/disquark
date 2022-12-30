package org.example.rest.util;

public class Hex {

    public static String decode(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i += 2) {
            String sub = s.substring(i, i + 2);
            sb.append((char) Integer.parseInt(sub, 16));
        }
        return sb.toString();
    }

    private Hex() {}
}
