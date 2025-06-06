package com.example.prjversami.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prjversami.R;
import com.example.prjversami.controllers.LoginController;
import com.example.prjversami.controllers.PerfilController;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.Compartilha;
import com.example.prjversami.util.Conexao;
import com.example.prjversami.util.ImagensUtil;
import com.example.prjversami.util.Validacao;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class EditarPerfil extends AppCompatActivity {

    EditText txtNome, txtArroba, txtBio;
    ImageView imgCapa, imgPerfil;
    ImageButton btnAlterarCapa, btnAlterarPerfil;
    Button btnSalvar, btnEditar;
    ProgressBar progressBar;
    View view;

    byte[] capa, perfil;
    boolean alterou = false;

    private static final int REQUEST_FOTO_PERFIL = 100;
    private static final int REQUEST_FOTO_CAPA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        personalizarActionBar();

        view = findViewById(R.id.editPerfil_view);
        txtNome = findViewById(R.id.editPerfil_txtNome);
        txtArroba = findViewById(R.id.editPerfil_txtArroba);
        txtBio = findViewById(R.id.editPerfil_bio);
        imgCapa = findViewById(R.id.editPerfil_capa);
        imgPerfil = findViewById(R.id.editPerfil_fotoPerfil);
        btnAlterarCapa = findViewById(R.id.editPerfil_editarCapa);
        btnAlterarPerfil = findViewById(R.id.editPerfil_editarPerfil);
        btnEditar = findViewById(R.id.editPerfil_btnEditar);
        btnSalvar = findViewById(R.id.editPerfil_btnSalvar);
        progressBar = findViewById(R.id.editPerfil_progress);

        visibilityItens(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        PerfilController pc = new PerfilController(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Usuario user;
                SharedPreferences pref = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);

                if (pref.getInt("id", 0) > 0) {
                    user = pc.obtemPerfil(pref.getInt("id", 0));

                    if(user == null)
                        return;

                    txtNome.setText(user.getUserName());
                    txtArroba.setText(user.getUserLogin());

                    if (user.getUserImage() != null)
                        imgPerfil.setImageBitmap(ImagensUtil.converteParaBitmap(user.getUserImage()));

                    if (user.getUserCover() != null)
                        imgCapa.setImageBitmap(ImagensUtil.converteParaBitmap(user.getUserCover()));

                    if (user.getUserBio() != null || user.getUserBio() != "")
                        txtBio.setText(user.getUserBio());

                    visibilityItens(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        },200);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK && data != null){
            Uri uri = data.getData();

            if(!ImagensUtil.verificarTamanhoImagem(uri, getApplicationContext())){
                Snackbar.make(view,"Arquivo acima do limite permitido. Max: 2MB",Snackbar.LENGTH_LONG).show();
                return;
            }

            try(InputStream input = getContentResolver().openInputStream(uri)){

                Bitmap bitmap = BitmapFactory.decodeStream(input);

                switch (requestCode){
                    case REQUEST_FOTO_PERFIL:
                        if(bitmap.getHeight() != bitmap.getWidth()){
                            Snackbar.make(view,"Imagem ajustada para melhor aparência.",Snackbar.LENGTH_LONG).show();
                            bitmap = ImagensUtil.recorteImagemPerfil(bitmap);
                        }
                        this.imgPerfil.setImageBitmap(bitmap);
                        this.perfil = ImagensUtil.converteParaBytes(bitmap);
                        break;
                    case REQUEST_FOTO_CAPA:
                        Snackbar.make(view,"Imagem ajustada para melhor aparência.",Snackbar.LENGTH_LONG).show();
                        bitmap = ImagensUtil.recorteImagemCapa(bitmap);
                        this.imgCapa.setImageBitmap(bitmap);
                        this.capa = ImagensUtil.converteParaBytes(bitmap);
                        break;
                }
            }catch (IOException e){
                Log.e("Erro ao obter imagem", e.getMessage());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if(this.alterou) Compartilha.setEditouPerfil(true);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void personalizarActionBar(){
        if(getSupportActionBar() == null)
            return;

        getSupportActionBar().setTitle("Editar Perfil");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
    }

    public void visibilityItens(int value){
        txtNome.setVisibility(value);
        txtArroba.setVisibility(value);
        txtBio.setVisibility(value);
        imgCapa.setVisibility(value);
        imgPerfil.setVisibility(value);
        btnEditar.setVisibility(value);
    }

    public boolean validaDados(String nome, String arrobaNovo, String bio){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String arrobaAtual = pref.getString("arroba","usuario");

        if(!Validacao.nameValidation(nome)){
            txtNome.setError("O nome precisa ter ao menos 3 caracteres");
            return false;
        }

        if(bio.length() > 255){
            txtBio.setError("A biografia deve ter no máximo 255 caracteres");
            return false;
        }

        if(!arrobaAtual.equals(arrobaNovo) && Validacao.userExist(arrobaNovo, new Conexao(), getApplicationContext())){
            txtArroba.setError("Nome de usuário não disponível!");
            return false;
        }

        return true;
    }

    public void habilitarEdicao(View v){
        // Habilitando campos e tornado botoes visiveis
        this.btnSalvar.setVisibility(View.VISIBLE);
        this.btnAlterarPerfil.setVisibility(View.VISIBLE);
        this.btnAlterarCapa.setVisibility(View.VISIBLE);
        this.txtNome.setEnabled(true);
        this.txtArroba.setEnabled(true);
        this.txtBio.setEnabled(true);

        // alterando cor do botao

        this.btnEditar.setBackgroundResource(R.drawable.rounded_button_access);
    }

    public void desabilitarEdicao(){
        // Habilitando campos e tornado botoes visiveis
        this.btnSalvar.setVisibility(View.GONE);
        this.btnAlterarPerfil.setVisibility(View.GONE);
        this.btnAlterarCapa.setVisibility(View.GONE);
        this.txtNome.setEnabled(false);
        this.txtArroba.setEnabled(false);
        this.txtBio.setEnabled(false);

        // alterando cor do botao

        this.btnEditar.setBackgroundResource(R.drawable.rounded_button_versamicolor);
    }

    public void abrirFotoPerfil(View v) {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Selecione uma image,"), REQUEST_FOTO_PERFIL);
    }

    public void abrirFotoCapa(View v) {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Selecione uma image,"), REQUEST_FOTO_CAPA);
    }

    public void salvarAlteracoes(View v){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String nome = this.txtNome.getText().toString(),
               arroba_novo = this.txtArroba.getText().toString(),
               bio = this.txtBio.getText().toString();

        if(!validaDados(nome, arroba_novo, bio))
            return;

        PerfilController pc = new PerfilController(getApplicationContext());

        Usuario usuario = new Usuario();
        usuario.setUserID(pref.getInt("id", 0));
        usuario.setUserName(nome);
        usuario.setUserLogin(arroba_novo);
        usuario.setUserBio(bio);

        int opcao = 4;

        if(this.perfil != null && this.capa != null){
            opcao = 1;
            usuario.setUserImage(this.perfil);
            usuario.setUserCover(this.capa);
        }else if(this.perfil != null && this.capa == null){
            opcao = 2;
            usuario.setUserImage(this.perfil);
        }else if(this.perfil == null && this.capa != null){
            opcao = 3;
            usuario.setUserCover(this.capa);
        }

        if(pc.atualizarDados(usuario, opcao)){
            LoginController lc = new LoginController(getApplicationContext());
            lc.guardarDados(usuario.getUserID(), nome, arroba_novo, this.perfil);
            desabilitarEdicao();
            Snackbar.make(v, "Dados atualizados com sucesso!", Snackbar.LENGTH_LONG).show();
            this.alterou = true;
        }else{
            Snackbar.make(v, "Dados não atualizados. Tente mais tarde!", Snackbar.LENGTH_LONG).show();
        }
    }
}