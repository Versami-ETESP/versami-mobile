package com.example.prjversami.core;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validacao {

    public static boolean nameValidatio(String name){
        boolean result;

        if(name.length() < 3){
            result = false;
        }else{
            result = true;
        }
        return result;
    }

    public static boolean emailValidation(String email){
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean passValidation(String pass){
        return pass.length() >= 8;
    }

    public static boolean birthValidation(Instant birth){
        Instant today = Instant.now();
        long anos = ChronoUnit.YEARS.between(birth,today);

        return anos >= 13;
    }

    public static boolean passConfirm(String pass, String confirm){
        return pass.equals(confirm);
    }

}
