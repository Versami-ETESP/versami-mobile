package com.example.prjversami.core;

import android.content.Context;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validacao {

    public static boolean nameValidation(String name) {
        return name.length() >= 3;
    }

    public static boolean emailValidation(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean passValidation(String pass) {
        return pass.length() >= 8;
    }

    public static boolean birthValidation(String birth) {
        SimpleDateFormat formatedDate = new SimpleDateFormat("dd/MM/yyyy");
        Calendar birthDate = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        int age = 0;

        try{
            birthDate.setTime(formatedDate.parse(birth));

            age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

            if(today.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH) || (today.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH)
                    && today.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH))){

                age--;

            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return age >= 13;
    }

    public static boolean passConfirm(String pass, String confirm) {
        return pass.equals(confirm);
    }

    public static boolean userExist(String user, String table, Conexao con, Context tela){
        con.connectDB(tela);
        boolean findUser = false;
        try{
            con.result = con.command.executeQuery("SELECT arroba_usuario FROM "+table+" WHERE arroba_usuario like '"+user+"'");
            findUser = con.result.next();

        }catch(SQLException e){
            Toast.makeText(tela, "Erro de Conexão. Tente mais tarde", Toast.LENGTH_SHORT).show();
        }

        return findUser;
    }

}
