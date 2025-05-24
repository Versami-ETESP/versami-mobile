package com.example.prjversami.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prjversami.R;
import com.example.prjversami.controllers.SearchController;
import com.example.prjversami.entities.Livro;
import com.example.prjversami.util.ImagensUtil;

public class InfoLivro extends AppCompatActivity {

    private TextView txtTitulo, txtAutor, txtGenero, txtDescricao;
    private ImageView imgCapa;
    private Button btnFavorito;
    private ProgressBar progressBar;
    private LinearLayout container;
    private Livro livro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_livro);
        personalizarActionBar();

        txtTitulo = findViewById(R.id.livroinfo_titulo);
        txtAutor = findViewById(R.id.livroinfo_autor);
        txtGenero = findViewById(R.id.livroinfo_genero);
        txtDescricao = findViewById(R.id.livroinfo_descricao);
        imgCapa = findViewById(R.id.livroinfo_capa);
        btnFavorito = findViewById(R.id.livroinfo_btnFavorito);
        progressBar = findViewById(R.id.livroinfo_progress);
        container = findViewById(R.id.livroinfo_container);

        //container.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getExtras();

        if(bundle == null){
            finish();
            return;
        }

        int idUser = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE).getInt("id", 0);
        int idLivro = bundle.getInt("idLivro");
        SearchController sc = new SearchController(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                livro = sc.obterInfoLivro(idLivro,idUser);

                if(livro == null){
                    finish();
                    return;
                }

                txtTitulo.setText(livro.getTitle());
                txtDescricao.setText(livro.getSummary());
                txtAutor.setText(livro.getAutor().getName());
                txtGenero.setText(livro.getGenre().getType());

                if(livro.getCover() != null)
                    imgCapa.setImageBitmap(ImagensUtil.converteParaBitmap(livro.getCover()));

                if(livro.isFavorite())
                    btnFavorito.setText("Favorito");

                //container.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        },200);

        btnFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!livro.isFavorite()){
                    if(sc.definirLivroFavorito(idLivro, idUser)){
                        livro.setFavorite(true);
                        btnFavorito.setText("Favorito");
                    }
                }else{
                    if(sc.removerLivroFavorito(idLivro, idUser)){
                        livro.setFavorite(false);
                        btnFavorito.setText("Adicionar aos favoritos");
                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void personalizarActionBar(){
        if(getSupportActionBar() == null)
            return;

        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
    }
}