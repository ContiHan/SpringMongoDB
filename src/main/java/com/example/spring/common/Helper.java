package com.example.spring.common;

public class Helper {
    public static <E extends Enum<E>> boolean isValueInEnum(String value, Class<E> enumClass) {
        try {
            Enum.valueOf(enumClass, value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
