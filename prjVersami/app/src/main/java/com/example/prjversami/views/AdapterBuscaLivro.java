package com.example.prjversami.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.prjversami.R;
import com.example.prjversami.entities.Livro;
import com.example.prjversami.util.Compartilha;
import com.example.prjversami.util.ImagensUtil;
import java.util.List;

public class AdapterBuscaLivro extends RecyclerView.Adapter{

    private List<Livro> livros;
    private Context context;

    public AdapterBuscaLivro(List<Livro> livros, Context context){
        this.livros = livros;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_busca_livro, viewGroup, false);
        ViewHolderBuscaLivro holder = new ViewHolderBuscaLivro(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderBuscaLivro holder = (ViewHolderBuscaLivro) viewHolder;
        Livro livro = livros.get(i);

        holder.nomeLivro.setText(livro.getTitle());
        if(livro.getCover() != null)
            holder.capa.setImageBitmap(ImagensUtil.converteParaBitmap(livro.getCover()));

        holder.marcado.setOnCheckedChangeListener(null);
        holder.marcado.setChecked(livro.equals(Compartilha.getLivro())); // só vai deixar marcado uma opção na lista
        holder.marcado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Compartilha.setLivro(livro); // salva o livro na classe compartilha
                    notifyDataSetChanged();
                }else{
                    Compartilha.setLivro(null);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.livros.size();
    }

}
