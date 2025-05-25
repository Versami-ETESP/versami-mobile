package com.example.prjversami.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prjversami.R;
import com.example.prjversami.entities.Livro;
import com.example.prjversami.util.ImagensUtil;

import java.util.List;

public class AdapaterResultadoLivros extends RecyclerView.Adapter{

    private List<Livro> livros;
    private Context context;

    public AdapaterResultadoLivros(List<Livro> livros, Context context) {
        this.livros = livros;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_resultado_pesquisa, viewGroup, false);
        ViewHolderResultadoLivros holder = new ViewHolderResultadoLivros(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderResultadoLivros holder = (ViewHolderResultadoLivros) viewHolder;
        Livro livro = this.livros.get(i);
        holder.titulo.setText(livro.getTitle());
        if(livro.getCover() != null)
            holder.imageView.setImageBitmap(ImagensUtil.converteParaBitmap(livro.getCover()));

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InfoLivro.class);
                intent.putExtra("idLivro", livro.getBookID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.livros.size();
    }
}
class ViewHolderResultadoLivros extends RecyclerView.ViewHolder{

    final LinearLayoutCompat container;
    final ImageView imageView;
    final TextView titulo;

    public ViewHolderResultadoLivros(@NonNull View itemView) {
        super(itemView);
        container = itemView.findViewById(R.id.resultadoItem_container);
        titulo = itemView.findViewById(R.id.resultadoItem_titulo);
        imageView = itemView.findViewById(R.id.resultadoItem_imagem);
    }
}
