package com.example.prjversami.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prjversami.R;
import com.example.prjversami.entities.Livro;

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

        Bundle bundle = getIntent().getExtras();

        if(bundle == null){
            finish();
            return;
        }


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