package com.example.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class ConvertString {
    public static String convert(String chuoi) {
        if (chuoi == null || chuoi.isEmpty()) {
            return "";
        }

        
        String normalizedString = Normalizer.normalize(chuoi, Normalizer.Form.NFD);

        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String noDiacritics = pattern.matcher(normalizedString).replaceAll("");

        String result = noDiacritics.toLowerCase();

        result = result.replaceAll("đ", "d");
        
        result = result.replaceAll("[^a-z0-9\\s-]", " "); 

        result = result.replaceAll("\\s+", "-");

        result = result.replaceAll("-+", "-");

        result = result.replaceAll("^-|-$", "");

        return result;
    }
}
