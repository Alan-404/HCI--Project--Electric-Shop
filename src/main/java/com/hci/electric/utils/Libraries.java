package com.hci.electric.utils;

import java.util.Random;

import com.hci.electric.middlewares.Jwt;

public class Libraries {
    private final String preHeader = "Bearer ";
    private final String splitToken = " ";
    private final Jwt jwt = new Jwt();
    public static final String generateId(int length){
        String id = "";
        Random random = new Random();
        String template = "1234567890qwertyuiopasdfghjklzxcvbnm";
        for (int i = 0; i<length; i++){
            id += template.charAt(random.nextInt(template.length()));
        }

        return id;
    }


    public boolean checkToken(String header){
        if (header == null || header.startsWith(this.preHeader)){
            return false;
        }
        String accountId =this.jwt.extractAccountId(header.split(this.splitToken)[1]);
        if (accountId == null){
            return false;
        }
        return true;
    }
}
