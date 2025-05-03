package com.example.prjversami.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

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
        String totCurtidas = controller.getCurtidas(publicacao.getIdPublicacao()).equals("") ? "0" : controller.getCurtidas(publicacao.getIdPublicacao());

        holder.content.setText(publicacao.getContent());
        holder.data.setText(publicacao.getPostDate());
        holder.profileName.setText(user.getUserName());
        holder.arroba.setText("@"+user.getUserLogin());
        holder.profileImage.setImageBitmap(ImagensUtil.converteParaBitmap(user.getUserImage()));
        holder.like.setText(totCurtidas);
        holder.like.setChecked(publicacao.isLike());
        holder.like.setOnCheckedChangeListener(null);

        // verifica se  a publicação possui livro vinculado. Se não houver ele oculta as informações

        if(publicacao.getBook() != null && publicacao.getBook() > 0){
            holder.bookName.setText(livro.getTitle());
            holder.bookImage.setImageBitmap(ImagensUtil.converteParaBitmap(livro.getCover()));
            holder.bookInfo.setVisibility(View.VISIBLE);
        }else{
            holder.bookInfo.setVisibility(View.GONE);
        }

        // evento que verifica a mudança de estado do checkbox de curtidas
        holder.like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    controller.setCurtidas(publicacao.getIdPublicacao());
                    holder.like.setText(controller.getCurtidas(publicacao.getIdPublicacao()));
                }else{
                    controller.removeCurtidas(publicacao.getIdPublicacao());
                    holder.like.setText(controller.getCurtidas(publicacao.getIdPublicacao()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.publicacao.size();
    }
}

