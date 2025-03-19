package com.example.prjversami.views;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prjversami.R;
import com.example.prjversami.controllers.CadastroController;
import com.example.prjversami.core.ImagensUtil;
import com.example.prjversami.models.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class cadastro2 extends AppCompatActivity {

    Usuario user;
    Button btnCadastrar;
    EditText userName;
    ImageView userImage;
    CadastroController cad;
    byte[] img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro2);

        getSupportActionBar().hide();

        btnCadastrar = findViewById(R.id.cadastro_btnFinalizar);
        userName = findViewById(R.id.cadastro_txtUserName);
        userImage = findViewById(R.id.cadastro_UserImage);

        cad = new CadastroController(getApplicationContext(), userName);
        cad.inputValidate2();

        // Solicita no app a permissão para ler o armazenamento do dispositivo - Tem que inserir a permissão no manifest também
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
         * Este método recebe o URI(o endereço) da imagem escolhida pelo usuário e
         * exibe ela em um ImageView com bordas arredondadas.
         * Essa imagem é convetida em um array de bytes para armazenamento no banco de dados.
         */
        if (requestCode == 12 && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream input = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                roundedDrawable.setCircular(true);
                userImage.setImageDrawable(roundedDrawable);
                img = ImagensUtil.converteParaBytes(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * O método 'abrirArquivos' está conectado ao image view do activity, que ao clicar
     * cria um intent que permite o usuario selecionar um arquivo da galeria de imagens do dispositivo.
     * Esse intent vai enviar o resultado da operação para o 'onActivityResult'.
     */
    public void abrirArquivos(View v) {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Selecione uma image,"), 12);
    }

    /*
     * O método 'cadastrar' está ligado ao botão presente no Activity. Sua função é preencher o objeto user
     * com as informações do usuário recebidas pelo Intent.putExtra e pelos dados obtidos na própria activity.
     * Após isso, o objeto será usado como parâmetro para cadastrar as informações no banco de dados.
     */

    public void cadastrar(View v) {

        if (userName.getText().toString().isEmpty() || !cad.userLoginAvaliable())
            return;

        Bundle dados = getIntent().getExtras();
        if (dados == null) {
            Snackbar.make(v, "Erro ao recuperar os dados do cadastro!", Snackbar.LENGTH_LONG).show();
            return;
        }

        user = new Usuario(
                dados.getString("nome"),
                userName.getText().toString(),
                dados.getString("email"),
                dados.getString("nasc"),
                dados.getString("senha"));

        if (this.img != null)
            user.setUserImage(img);

        if (cad.register(user)) {
            Intent tela = new Intent(cadastro2.this, login.class);
            startActivity(tela);
            finish();
        } else {
            Snackbar.make(v, "Erro ao cadastrar usuário. Tente novamente mais tarde.", Snackbar.LENGTH_LONG).show();
        }
    }
}