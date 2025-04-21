package com.example.prjversami.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prjversami.R;

public class ViewHolderBuscaLivro extends RecyclerView.ViewHolder {

    final ImageView capa;
    final TextView nomeLivro;
    final CheckBox marcado;

    public ViewHolderBuscaLivro(@NonNull View itemView) {
        super(itemView);
        capa = itemView.findViewById(R.id.buscalivro_capa);
        nomeLivro = itemView.findViewById(R.id.buscalivro_nome);
        marcado = itemView.findViewById(R.id.buscalivro_checkbox);
    }
}
