package com.example.prjversami.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prjversami.R;
import com.example.prjversami.entities.Livro;
import com.example.prjversami.util.ImagensUtil;

import java.util.List;

public class AdapterTopLivros extends RecyclerView.Adapter{

    private List<Livro> livros;
    private Context context;

    public AdapterTopLivros(List<Livro> livros, Context context){
        this.livros = livros;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_explorar_livros, viewGroup, false);
        ViewHolderTopLivros holder = new ViewHolderTopLivros(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderTopLivros holder = (ViewHolderTopLivros) viewHolder;
        Livro livro = new Livro();

        livro = this.livros.get(i);

        holder.titulo.setText(livro.getTitle());
        if(livro.getCover() != null){
            holder.capa.setImageBitmap(ImagensUtil.converteParaBitmap(livro.getCover()));
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, InfoLivro.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.livros.size();
    }
}

class ViewHolderTopLivros extends RecyclerView.ViewHolder{

    final CardView container;
    final TextView titulo;
    final ImageView capa;

    public ViewHolderTopLivros(@NonNull View itemView) {
        super(itemView);
        container = itemView.findViewById(R.id.toplivro_container);
        titulo = itemView.findViewById(R.id.toplivro_titulo);
        capa = itemView.findViewById(R.id.toplivro_capa);
    }
}
