package com.example.quemefaltaapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }

    public static String capitalizeFirstLetter(String input) {
        String[] words = input.split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                String firstLetter = word.substring(0, 1).toUpperCase();
                String restOfWord = word.substring(1).toLowerCase();
                result.append(firstLetter).append(restOfWord).append(" ");
            }
        }

        return result.toString().trim();
    }
}
