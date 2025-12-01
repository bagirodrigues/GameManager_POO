package com.gamemanager.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }


    public static boolean isPositiveInteger(String value) {
        if (!isNotEmpty(value)) {
            return false;
        }

        try {
            int number = Integer.parseInt(value.trim());
            return number > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isPositiveDouble(String value) {
        if (!isNotEmpty(value)) {
            return false;
        }

        try {
            double number = Double.parseDouble(value.trim());
            return number >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDate(String date) {
        if (!isNotEmpty(date)) {
            return false;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate.parse(date.trim(), formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidImageUrl(String url) {
        if (!isNotEmpty(url)) {
            return false;
        }

        String lowerUrl = url.toLowerCase().trim();
        return lowerUrl.matches("^https?://.+\\.(jpg|jpeg|png|gif|bmp|webp)(\\?.*)?$");
    }

    public static boolean isValidRating(double rating) {
        return rating >= 0 && rating <= 10;
    }

    public static boolean isValidRating(String rating) {
        if (!isPositiveDouble(rating)) {
            return false;
        }

        try {
            double value = Double.parseDouble(rating.trim());
            return isValidRating(value);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidLength(String value, int minLength, int maxLength) {
        if (value == null) {
            return false;
        }

        int length = value.trim().length();
        return length >= minLength && length <= maxLength;
    }

    public static boolean isValidGameTitle(String title) {
        return isNotEmpty(title) && isValidLength(title, 1, 100);
    }

    public static boolean isValidGenre(String genre) {
        return isNotEmpty(genre) && isValidLength(genre, 1, 50);
    }

    public static String sanitizeString(String input) {
        if (input == null) {
            return "";
        }

        return input.trim()
                .replaceAll("[<>\"']", "")
                .replaceAll("\\s+", " ");
    }
}