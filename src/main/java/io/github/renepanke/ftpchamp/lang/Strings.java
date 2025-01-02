package io.github.renepanke.ftpchamp.lang;

import java.util.LinkedList;
import java.util.List;

import static io.github.renepanke.ftpchamp.lang.Bools.not;

public class Strings {

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isNotEmpty(String s) {
        return s != null && !s.isEmpty();
    }

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isNotBlank(String s) {
        return not(isBlank(s));
    }

    public static List<String> splitAt(String s, char delimiter) {
        if (isBlank(s)) return new LinkedList<>();
        List<String> list = new LinkedList<>();
        String currentBuffer = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == delimiter) {
                list.add(currentBuffer);
                currentBuffer = "";
            }
            else {
                currentBuffer += s.charAt(i);
            }
        }
        return list;
    }
}
