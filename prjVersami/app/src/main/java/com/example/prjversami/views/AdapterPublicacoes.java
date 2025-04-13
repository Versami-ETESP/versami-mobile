package com.example.prjversami.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prjversami.R;
import com.example.prjversami.controllers.PublicacaoController;
import com.example.prjversami.entities.Livro;
import com.example.prjversami.entities.Publicacao;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.ImagensUtil;

import java.util.List;

public class AdapterPublicacoes extends RecyclerView.Adapter {

    private Context context;
    private List<Publicacao> publicacao;
    private PublicacaoController controller;

    public AdapterPublicacoes(List<Publicacao> publicacoes, Context context){
        this.publicacao = publicacoes;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_profile_posts, viewGroup, false);
        ViewHolderPublicacoes holder = new ViewHolderPublicacoes(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderPublicacoes holder = (ViewHolderPublicacoes) viewHolder;
        Publicacao publicacao = this.publicacao.get(i);
        controller = new PublicacaoController(this.context);

        Usuario user = controller.pegarUsuario(publicacao.getUser());
        Livro livro = controller.pegarLivro(publicacao.getBook());

        holder.content.setText(publicacao.getContent());
        holder.data.setText(publicacao.getPostDate());
        holder.profileName.setText(user.getUserName());
        holder.arroba.setText("@"+user.getUserLogin());
        holder.profileImage.setImageBitmap(ImagensUtil.converteParaBitmap(user.getUserImage()));

        if(publicacao.getBook() != null && publicacao.getBook() > 0){
            holder.bookName.setText(livro.getTitle());
            holder.bookImage.setImageBitmap(ImagensUtil.converteParaBitmap(livro.getCover()));
            holder.bookInfo.setVisibility(View.VISIBLE);
        }else{
            holder.bookInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return this.publicacao.size();
    }
}

