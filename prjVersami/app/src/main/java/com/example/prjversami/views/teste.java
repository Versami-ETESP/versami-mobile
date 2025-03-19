package com.example.prjversami.views;

import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prjversami.R;
import com.example.prjversami.core.Conexao;
import com.example.prjversami.core.ImagensUtil;

import java.sql.SQLException;

public class teste extends AppCompatActivity {

    Conexao con;
    byte[] imagem;
    Bitmap imagemConvertida;
    ImageView teste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);
        teste = findViewById(R.id.testeIMG);

        receberImagem();

        teste.setImageBitmap(imagemConvertida);
    }

    private void receberImagem(){
        con = new Conexao();
        con.connectDB(getApplicationContext());

        try{
            con.result = con.command.executeQuery("Select fotoUsuario from tblUsuario where idUsuario = 6");

            if(con.result.next()) {
                imagem = con.result.getBytes("fotoUsuario");


                if(imagem != null){
                    imagemConvertida = ImagensUtil.converteParaBitmap(imagem);
                }else{
                    Log.d("DEBUG", "Imagem Nula");
                    Snackbar.make(findViewById(android.R.id.content), "Imagem Nula", Snackbar.LENGTH_LONG).show();
                }

            }else{
                Log.d("DEBUG", "Resultado em branco");
                Snackbar.make(findViewById(android.R.id.content), "Resultado em branco", Toast.LENGTH_LONG).show();}
        }catch (SQLException e){
            Log.e("Erro SQL", "Falha ao conectar ao banco de dados - " + e.getMessage());
            e.printStackTrace();
        }


    }


}