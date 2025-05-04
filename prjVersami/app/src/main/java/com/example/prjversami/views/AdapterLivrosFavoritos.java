package com.example.prjversami.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prjversami.R;
import com.example.prjversami.entities.Livro;
import com.example.prjversami.util.ImagensUtil;

import java.util.List;

public class AdapterLivrosFavoritos extends RecyclerView.Adapter {

    private List<Livro> livros;
    private Context context;

    public AdapterLivrosFavoritos(List<Livro> livros, Context context){
        this.livros = livros;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_livro_favorito, viewGroup, false);
        ViewHolderLivroFavorito holder = new ViewHolderLivroFavorito(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderLivroFavorito holder = (ViewHolderLivroFavorito) viewHolder;
        Livro livro = livros.get(i);

        holder.nomeLivro.setText(livro.getTitle());

        if(livro.getCover() != null)
            holder.capaLivro.setImageBitmap(ImagensUtil.converteParaBitmap(livro.getCover()));
    }

    @Override
    public int getItemCount() {
        return this.livros.size();
    }
} // fim da classe Adapter

class ViewHolderLivroFavorito extends RecyclerView.ViewHolder{

    final TextView nomeLivro;
    final ImageView capaLivro;


    public ViewHolderLivroFavorito(@NonNull View itemView) {
        super(itemView);
        nomeLivro = itemView.findViewById(R.id.livroFavorito_nome);
        capaLivro = itemView.findViewById(R.id.livroFavorito_capa);
    }
} // fim da classe view holder
