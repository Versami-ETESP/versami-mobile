package com.example.prjversami.views;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prjversami.R;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.ImagensUtil;
import com.example.prjversami.util.NavigationUtil;

import java.util.List;

public class AdapterResultadoUsuarios extends RecyclerView.Adapter{

    private List<Usuario> usuarios;
    private Context context;
    private FragmentManager fragmentManager;

    public AdapterResultadoUsuarios(List<Usuario> usuarios, Context context, FragmentManager fm) {
        this.usuarios = usuarios;
        this.context = context;
        this.fragmentManager = fm;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_resultado_pesquisa, viewGroup, false);
        ViewHolderResultadoUsuarios holder = new ViewHolderResultadoUsuarios(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderResultadoUsuarios holder = (ViewHolderResultadoUsuarios) viewHolder;
        Usuario user = usuarios.get(i);

        holder.titulo.setText(user.getUserName());
        if(user.getUserImage() != null)
            holder.imageView.setImageBitmap(ImagensUtil.converteParaBitmap(user.getUserImage()));
        else
            holder.imageView.setImageResource(R.drawable.user_icon_placeholder2);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText pesquisa = ((Activity)context).findViewById(R.id.search_pesquisar);
                pesquisa.setVisibility(View.GONE);
                Fragment fragment = ProfileFragment.newInstance(user.getUserID());
                NavigationUtil.carregarFragment(fragmentManager, R.id.search_framelayout,fragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.usuarios.size();
    }
}
class ViewHolderResultadoUsuarios extends RecyclerView.ViewHolder{

    final LinearLayoutCompat container;
    final ImageView imageView;
    final TextView titulo;

    public ViewHolderResultadoUsuarios(@NonNull View itemView) {
        super(itemView);
        container = itemView.findViewById(R.id.resultadoItem_container);
        titulo = itemView.findViewById(R.id.resultadoItem_titulo);
        imageView = itemView.findViewById(R.id.resultadoItem_imagem);
    }
}
