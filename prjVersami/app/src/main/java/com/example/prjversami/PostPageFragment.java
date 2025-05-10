package com.example.prjversami;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.content.FileProvider;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prjversami.entities.Livro;
import com.example.prjversami.entities.Publicacao;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.ImagensUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostPageFragment extends Fragment {


    private static final String ARG_ID_PUBLICACAO = "idPublicacao";
    private static final String ARG_CONTEUDO_PUBLIC = "conteudoPublicacao";
    private static final String ARG_ID_USER = "idUsuario";
    private static final String ARG_NOME_USER = "nomeUsuario";
    private static final String ARG_ARROBA_USER = "arrobaUsuaro";
    private static final String ARG_FOTO_USER = "fotoUsuario";
    private static final String ARG_CAPA_LIVRO = "capaLivro";
    private static final String ARG_NOME_LIVRO = "nomeLivro";
    private static final String ARG_DATA_PUBLIC = "dataPublicacao";
    private static final String ARG_TOT_CURTIDA = "totalCurtidas";
    private static final String ARG_CURTIU = "foiCurtido";
    private static final String ARG_TOT_COMENTARIOS = "totalComentarios";

    private Publicacao publicacao;

    private ImageView profileImage, bookImage;
    private TextView bookName, profileName, data, content, commentLabel, arroba;
    private CheckBox like;
    private LinearLayout bookInfo;

    public PostPageFragment() {
        // Required empty public constructor
    }


    public static PostPageFragment newInstance(Publicacao publicacao) {
        PostPageFragment fragment = new PostPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID_PUBLICACAO, publicacao.getIdPublicacao());
        args.putString(ARG_CONTEUDO_PUBLIC, publicacao.getContent());
        args.putString(ARG_DATA_PUBLIC, publicacao.getPostDate());
        args.putInt(ARG_TOT_CURTIDA, publicacao.getTotalLikes());
        args.putBoolean(ARG_CURTIU, publicacao.isLike());

        args.putInt(ARG_ID_USER,publicacao.getUsuario().getUserID());
        args.putString(ARG_NOME_USER, publicacao.getUsuario().getUserName());
        args.putString(ARG_ARROBA_USER, publicacao.getUsuario().getUserLogin());
        if(publicacao.getUsuario().getUserImage() != null)
            args.putByteArray(ARG_FOTO_USER, publicacao.getUsuario().getUserImage());

        if(publicacao.getLivro() != null){
            args.putString(ARG_NOME_LIVRO, publicacao.getLivro().getTitle());
            args.putByteArray(ARG_CAPA_LIVRO, publicacao.getLivro().getCover());
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.publicacao = new Publicacao();

            this.publicacao.setIdPublicacao(getArguments().getInt(ARG_ID_PUBLICACAO));
            this.publicacao.setContent(getArguments().getString(ARG_CONTEUDO_PUBLIC));
            this.publicacao.setPostDate(getArguments().getString(ARG_DATA_PUBLIC));
            this.publicacao.setTotalLikes(getArguments().getInt(ARG_TOT_CURTIDA));
            if(getArguments().getBoolean(ARG_CURTIU))
                this.publicacao.addLike();

            Usuario usuario = new Usuario();
            usuario.setUserID(getArguments().getInt(ARG_ID_USER));
            usuario.setUserName(getArguments().getString(ARG_NOME_USER));
            usuario.setUserLogin(getArguments().getString(ARG_ARROBA_USER));
            if(getArguments().containsKey(ARG_FOTO_USER))
                usuario.setUserImage(getArguments().getByteArray(ARG_FOTO_USER));
            this.publicacao.setUsuario(usuario);

            Livro livro = new Livro();
            if(getArguments().containsKey(ARG_NOME_LIVRO) && getArguments().containsKey(ARG_CAPA_LIVRO)){
                livro.setTitle(getArguments().getString(ARG_NOME_LIVRO));
                livro.setCover(getArguments().getByteArray(ARG_CAPA_LIVRO));
                this.publicacao.setLivro(livro);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_post_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileImage = view.findViewById(R.id.post_page_image);
        bookImage = view.findViewById(R.id.post_page_cover);
        bookName = view.findViewById(R.id.post_page_bookname);
        profileName = view.findViewById(R.id.post_page_name);
        arroba = view.findViewById(R.id.post_page_username);
        data = view.findViewById(R.id.post_page_date);
        content = view.findViewById(R.id.post_page_textcontent);
        commentLabel = view.findViewById(R.id.post_page_labelcomment);
        like = view.findViewById(R.id.post_page_like);
        bookInfo = view.findViewById(R.id.post_page_book);

        Usuario user = this.publicacao.getUsuario();
        Livro livro = this.publicacao.getLivro();

        profileName.setText(user.getUserName());
        arroba.setText(user.getUserLogin());
        data.setText(this.publicacao.getPostDate());
        content.setText(this.publicacao.getContent());
        like.setText(this.publicacao.getTotalLikes().toString());
        like.setChecked(this.publicacao.isLike());
        if(user.getUserImage() != null)
            profileImage.setImageBitmap(ImagensUtil.converteParaBitmap(user.getUserImage()));

        if(livro != null){
            bookInfo.setVisibility(View.VISIBLE);
            bookName.setText(livro.getTitle());
            bookImage.setImageBitmap(ImagensUtil.converteParaBitmap(livro.getCover()));
        }else{
            bookInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_post_page, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_post_page_compartilhar:
                Bitmap sticker = ImagensUtil.criarStickerPublicacao(getContext(), this.publicacao);
                compartilharNoStories(sticker);
                break;
            case R.id.menu_post_page_denunciar:
                Toast.makeText(getContext(), "Denunciar Publicacao", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void compartilharNoStories(Bitmap bitmap){
        try{
            File cachePath = new File(requireContext().getCacheDir(),"images");
            cachePath.mkdirs();
            File file = new File(cachePath, "sticker.jpeg");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();

            Uri stickerAssetUri = FileProvider.getUriForFile(requireContext(),
                    requireContext().getPackageName() + ".fileprovider",
                    file);

            String sourceApplication = "2259474054448179";
            Intent intent = new Intent("com.instagram.share.ADD_TO_STORY");
            intent.putExtra("source_application", sourceApplication);
            intent.setType("image/jpeg");
            intent.putExtra("interactive_asset_uri", stickerAssetUri);
            intent.putExtra("top_background_color", "#333333");
            intent.putExtra("bottom_background_color", "#333333");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            requireActivity().grantUriPermission(
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
            Snackbar.make(getView(), "Não foi possível compartilhar publicação. Tente mais tarde!", Snackbar.LENGTH_LONG).show();
        }
    }

    private void compartilharNoInsta(Bitmap bitmap){
        try{
            File cachePath = new File(requireContext().getCacheDir(),"images");
            cachePath.mkdirs();
            File file = new File(cachePath, "sticker.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            Uri stickerAssetUri = FileProvider.getUriForFile(requireContext(),
                    requireContext().getPackageName() + ".fileprovider",
                    file);

            ArrayList<Intent> targetedShareIntents = new ArrayList<>();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, stickerAssetUri);

            List<ResolveInfo> resInfo = requireContext().getPackageManager().queryIntentActivities(shareIntent, 0);

            if (!resInfo.isEmpty()) {
                String packageName = "com.instagram.android";

                Intent targetedShareIntent = new Intent(Intent.ACTION_SEND);
                targetedShareIntent.setType("image/*");
                targetedShareIntent.putExtra(Intent.EXTRA_STREAM, stickerAssetUri);
                targetedShareIntent.setPackage(packageName);
                targetedShareIntents.add(targetedShareIntent);

                Intent chooserIntent = Intent.createChooser(
                        targetedShareIntents.remove(0),
                        "Share"
                );

                chooserIntent.putExtra(
                        Intent.EXTRA_INITIAL_INTENTS,
                        targetedShareIntents.toArray(new Parcelable[targetedShareIntents.size()])
                );

                startActivity(chooserIntent);
            } else {
                // Instagram não está instalado
                Toast.makeText(getContext(), "Instagram application is not installed!", Toast.LENGTH_SHORT).show();
            }

        }catch (IOException e){
            Log.e("Erro ao compartilhar", e.getMessage());
            Snackbar.make(getView(), "Não foi possível compartilhar publicação. Tente mais tarde!", Snackbar.LENGTH_LONG).show();
        }
    }
}