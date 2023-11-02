package com.example.quemefaltaapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        boolean valid = matcher.matches();
        return valid;
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

    public void CopyText(Context context, String text){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Texto copiado", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Texto copiado en el portapapeles", Toast.LENGTH_LONG).show();
    }

    public String generateID(){
        String uuidString = UUID.randomUUID().toString();
        String last10Characters = uuidString.substring(uuidString.length() - 10);
        return last10Characters;
    }
}
