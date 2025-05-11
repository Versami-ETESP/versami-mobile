package com.example.prjversami.views;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prjversami.R;
import com.example.prjversami.controllers.PublicacaoController;
import com.example.prjversami.entities.Livro;
import com.example.prjversami.entities.Publicacao;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.ImagensUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PostPage extends AppCompatActivity {

    private ImageView profileImage, bookImage;
    private TextView bookName, profileName, data, content, commentLabel, arroba;
    private CheckBox like;
    private LinearLayout bookInfo;
    private Publicacao publicacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_page);

        personalizarActionBar();

        profileImage = findViewById(R.id.post_page_image);
        bookImage = findViewById(R.id.post_page_cover);
        bookName = findViewById(R.id.post_page_bookname);
        profileName = findViewById(R.id.post_page_name);
        arroba = findViewById(R.id.post_page_username);
        data = findViewById(R.id.post_page_date);
        content = findViewById(R.id.post_page_textcontent);
        commentLabel = findViewById(R.id.post_page_labelcomment);
        like = findViewById(R.id.post_page_like);
        bookInfo = findViewById(R.id.post_page_book);
        ConstraintLayout containerPost = findViewById(R.id.post_page_container);
        ProgressBar progressBar = findViewById(R.id.post_page_progress);

        containerPost.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getExtras();

        if(bundle == null){
            finish();
            return;
        }

        PublicacaoController pc = new PublicacaoController(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                publicacao = pc.obterPublicacao(bundle.getInt("idPublicacao"));
                Usuario user = publicacao.getUsuario();
                Livro livro = publicacao.getLivro();

                profileName.setText(user.getUserName());
                arroba.setText(user.getUserLogin());
                data.setText(publicacao.getPostDate());
                content.setText(publicacao.getContent());
                like.setText(publicacao.getTotalLikes().toString());
                like.setChecked(publicacao.isLike());
                if(user.getUserImage() != null)
                    profileImage.setImageBitmap(ImagensUtil.converteParaBitmap(user.getUserImage()));

                if(livro != null){
                    bookInfo.setVisibility(View.VISIBLE);
                    bookName.setText(livro.getTitle());
                    bookImage.setImageBitmap(ImagensUtil.converteParaBitmap(livro.getCover()));
                }else{
                    bookInfo.setVisibility(View.GONE);
                }

                containerPost.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        },200);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fragment_post_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_post_page_compartilhar:
                Bitmap bitmap = ImagensUtil.criarStickerPublicacao(getApplicationContext(), this.publicacao);
                compartilharNoStories(bitmap);
                break;
            case R.id.menu_post_page_denunciar:
                Toast.makeText(getApplicationContext(), "Denunciar publicação", Toast.LENGTH_LONG).show();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void personalizarActionBar(){
        if(getSupportActionBar() == null)
            return;

        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
    }

    private void compartilharNoStories(Bitmap bitmap){
        try{
            File cachePath = new File(getApplicationContext().getCacheDir(),"images");
            cachePath.mkdirs();
            File file = new File(cachePath, "sticker.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            Uri stickerAssetUri = FileProvider.getUriForFile(getApplicationContext(),
                    getApplicationContext().getPackageName() + ".fileprovider",
                    file);

            String sourceApplication = "2259474054448179";
            Intent intent = new Intent("com.instagram.share.ADD_TO_STORY");
            intent.putExtra("source_application", sourceApplication);
            intent.setType("image/png");
            intent.putExtra("interactive_asset_uri", stickerAssetUri);
            intent.putExtra("top_background_color", "#61C1D1");
            intent.putExtra("bottom_background_color", "#61C1D1");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            getApplicationContext().grantUriPermission(
                    "com.instagram.android", stickerAssetUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Log.d("PermissionDebug", "Permissão concedida para URI: " + stickerAssetUri.toString());

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.e("IntentDebug", "Instagram não encontrado: " + e.getMessage());
            } catch (Exception e) {
                Log.e("IntentDebug", "Erro ao iniciar Intent: " + e.getMessage());
            }

        }catch (IOException e){
            Log.e("Erro ao compartilhar", e.getMessage());
            View view = findViewById(android.R.id.content);
            Snackbar.make(view, "Não foi possível compartilhar publicação. Tente mais tarde!", Snackbar.LENGTH_LONG).show();
        }
    }
}