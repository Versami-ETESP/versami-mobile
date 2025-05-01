package com.example.prjversami.util;

import android.util.Log;

import java.security.MessageDigest;

public class Criptografia {

    public static String criptografaString(String senha){
        String hash = "";
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = messageDigest.digest(senha.getBytes("UTF-8"));

            StringBuilder stringBuilder = new StringBuilder();

            for(byte b : bytes){
                stringBuilder.append(String.format("%02x",0xFF & b));
            }
            hash = stringBuilder.toString();

        }catch (Exception e){
            Log.e("Erro na criptografia", e.getMessage());
        }
        return hash;
    }
}
