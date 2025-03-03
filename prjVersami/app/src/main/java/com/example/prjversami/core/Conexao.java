package com.example.prjversami.core;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {
    ResultSet result;
    Statement command;
    Connection connect;

    public Connection connectDB(Context context){
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Toast.makeText(context.getApplicationContext(), "Driver Correto", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(context.getApplicationContext(),"Driver Incorreto", Toast.LENGTH_SHORT).show();
        }

        try {
            String url = "jdbc:jtds:sqlserver://192.168.1.5:1433;databaseName=versami";
            connect = DriverManager.getConnection(url,"sa","Tc2088275");
            command = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            Toast.makeText(context.getApplicationContext(),"Conectado",Toast.LENGTH_SHORT).show();
        }catch(SQLException e){
            Toast.makeText(context.getApplicationContext(), "Erro de Conexão", Toast.LENGTH_SHORT).show();
        }

        return connect;
    }
}
