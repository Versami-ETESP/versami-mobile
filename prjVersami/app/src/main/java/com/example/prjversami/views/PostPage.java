package com.example.prjversami.views;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prjversami.R;
import com.example.prjversami.controllers.CriarPostController;
import com.example.prjversami.controllers.PublicacaoController;
import com.example.prjversami.entities.Comentario;
import com.example.prjversami.entities.Livro;
import com.example.prjversami.entities.Publicacao;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.ImagensUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PostPage extends AppCompatActivity {

    private ImageView profileImage, bookImage;
    private TextView bookName, profileName, data, content, commentLabel, arroba, labelSemComent;
    private EditText editComentarios;
    private RecyclerView recyclerComentarios;
    private CheckBox like;
    private LinearLayout bookInfo;
    private View root;
    private Publicacao publicacao;
    private List<Comentario> comentarios;
    private AdapterComentarios adapter;
    private int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_page);

        personalizarActionBar();

        ConstraintLayout containerPost = findViewById(R.id.post_page_container);
        ProgressBar progressBar = findViewById(R.id.post_page_progress);
        root = findViewById(R.id.post_page_root);
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
        labelSemComent = findViewById(R.id.post_page_labelSemComentarios);
        editComentarios = findViewById(R.id.post_page_editComentario);
        recyclerComentarios = findViewById(R.id.post_page_comentarios);

        recyclerComentarios.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        editComentarios.setVisibility(View.GONE);
        recyclerComentarios.setVisibility(View.GONE);
        containerPost.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getExtras();

        if(bundle == null){
            finish();
            return;
        }


        PublicacaoController pc = new PublicacaoController(getApplicationContext());
        int idPublic = bundle.getInt("idPublicacao");
        idUser = getSharedPreferences("login",MODE_PRIVATE).getInt("id",0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                publicacao = pc.obterPublicacao(idPublic);
                comentarios = pc.listarComentarios(idPublic,idUser);
                adapter = new AdapterComentarios(comentarios,getApplicationContext());
                recyclerComentarios.setAdapter(adapter);

                Usuario user = publicacao.getUsuario();
                Livro livro = publicacao.getLivro();

                profileName.setText(user.getUserName());
                arroba.setText("@"+user.getUserLogin());
                data.setText(publicacao.getPostDate());
                content.setText(publicacao.getContent());
                like.setText(publicacao.getTotalLikes().toString());
                commentLabel.setText(publicacao.getTotalComentarios().toString());
                like.setChecked(publicacao.isLike());
                if(user.getUserImage() != null){
                    profileImage.setImageBitmap(ImagensUtil.converteParaBitmap(user.getUserImage()));
                }else{
                    profileImage.setBackgroundColor(Color.parseColor("#d3d3d3"));
                    profileImage.setImageResource(R.drawable.user_icon_placeholder2);
                }


                if(livro != null){
                    bookInfo.setVisibility(View.VISIBLE);
                    bookName.setText(livro.getTitle());
                    bookImage.setImageBitmap(ImagensUtil.converteParaBitmap(livro.getCover()));
                }else{
                    bookInfo.setVisibility(View.GONE);
                }

                editComentarios.setVisibility(View.VISIBLE);
                recyclerComentarios.setVisibility(View.VISIBLE);
                containerPost.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);


                if(comentarios.isEmpty()){
                    recyclerComentarios.setVisibility(View.GONE);
                    labelSemComent.setVisibility(View.VISIBLE);
                    Log.d("Comentarios vazios", "Passei aqui");
                }else{
                    labelSemComent.setVisibility(View.GONE);
                    recyclerComentarios.setVisibility(View.VISIBLE);
                    Log.d("Comentarios com dados", "Passei aqui");
                }
            }
        },200);

        editComentarios.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String comentario = editComentarios.getText().toString();

                if(i == EditorInfo.IME_ACTION_SEND){
                    Comentario comentObj = criarComentario(idPublic, comentario);

                    if(comentObj == null){
                        Snackbar.make(root,"Comentário inválido. Verifique os campos.",Snackbar.LENGTH_LONG).show();
                        return false;
                    }
                    CriarPostController cpc = new CriarPostController(getApplicationContext());

                    if(!cpc.postarComentario(comentObj, idPublic)){
                        Snackbar.make(root,"Erro ao publicar o comentário. Tente novamente mais tarde!",Snackbar.LENGTH_LONG).show();
                        return false;
                    }
                    if(recyclerComentarios.getVisibility() == View.GONE){
                        labelSemComent.setVisibility(View.GONE);
                        recyclerComentarios.setVisibility(View.VISIBLE);
                    }
                    comentarios.add(comentObj);
                    publicacao.setTotalComentarios(publicacao.getTotalComentarios()+1);
                    commentLabel.setText(publicacao.getTotalComentarios().toString());
                    adapter.notifyItemInserted(comentarios.size() - 1);
                    recyclerComentarios.scrollToPosition(comentarios.size() -1);
                    editComentarios.setText("");
                    return true;
                }
                return false;
            }
        });
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
                denunciarPublicacao();
                break;
            case R.id.menu_post_page_deletar:
                excluirPublicação();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemExcluir = menu.findItem(R.id.menu_post_page_deletar);
        MenuItem itemDenunciar = menu.findItem(R.id.menu_post_page_denunciar);
        MenuItem itemCompartilhar = menu.findItem(R.id.menu_post_page_compartilhar);

        this.idUser = getSharedPreferences("login",MODE_PRIVATE).getInt("id",0);
        int idDonoPublic = getIntent().getExtras().getInt("idDonoPublic");

        if(idDonoPublic == this.idUser){
            itemExcluir.setVisible(true);
            itemCompartilhar.setVisible(true);
            itemDenunciar.setVisible(false);
        }else{
            itemExcluir.setVisible(false);
            itemCompartilhar.setVisible(false);
            itemDenunciar.setVisible(true);
        }
        return true;
    }

    private void excluirPublicação(){
        AlertDialog.Builder excluir = new AlertDialog.Builder(this);
        excluir.setTitle("Excluir Publicação");
        excluir.setMessage("Você tem certeza que deseja excluir essa publicação?");
        excluir.setNegativeButton("Não", null);
        excluir.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PublicacaoController pc = new PublicacaoController(getApplicationContext());
                if(pc.excluirPublicacaoBD(publicacao.getIdPublicacao())){
                    Intent intent = new Intent(PostPage.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    //todo corrigir isso
                }else{
                    Snackbar.make(root, "Não foi possível excluir sua publicação. Tente novamente mais tarde",Snackbar.LENGTH_LONG).show();
                }
            }
        });
        excluir.create().show();
    }

    private void denunciarPublicacao(){
        AlertDialog.Builder denuncia = new AlertDialog.Builder(this);
        denuncia.setTitle("Denunciar Publicação");
        denuncia.setMessage("Você tem certeza que deseja denunciar essa publicação?");
        denuncia.setNegativeButton("Não", null);
        denuncia.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PublicacaoController pc = new PublicacaoController(getApplicationContext());
                if(pc.criarDenuncia(idUser, publicacao.getIdPublicacao())){
                    Snackbar.make(root, "Denúncia enviada com sucesso. Vamos analisar em breve",Snackbar.LENGTH_LONG).show();
                }else{
                    Snackbar.make(root, "Não foi possível enviar sua denúncia. Tente novamente mais tarde",Snackbar.LENGTH_LONG).show();
                }
            }
        });
        denuncia.create().show();
    }

    private Comentario criarComentario(int idPublicacao, String comentario){
        if(comentario.isEmpty()){
            editComentarios.setError("Os comentários devem conter algum texto.");
            return null;
        }

        if(comentario.length() > 248){
            editComentarios.setError("Comentário devem ter no máximo 248 caracteres");
            return null;
        }
        SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
        Bitmap fotoPerfil = ImagensUtil.pegarImagem("imagemPerfil.jpeg",getApplicationContext());

        Usuario user = new Usuario();
        user.setUserID(pref.getInt("id",0));
        user.setUserName(pref.getString("nome",""));
        user.setUserLogin(pref.getString("arroba",""));
        if(fotoPerfil != null)
            user.setUserImage(ImagensUtil.converteParaBytes(fotoPerfil));

        return new Comentario(comentario,user);
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