package com.hci.electric.utils;

import java.util.Random;

public class Libraries {
    public static final String generateId(int length){
        String id = "";
        Random random = new Random();
        String template = "1234567890qwertyuiopasdfghjklzxcvbnm";
        for (int i = 0; i<length; i++){
            id += template.charAt(random.nextInt(template.length()));
        }

        return id;
    }
}
