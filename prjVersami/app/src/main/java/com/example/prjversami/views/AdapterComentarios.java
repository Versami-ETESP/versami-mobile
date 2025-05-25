package com.example.prjversami.views;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prjversami.R;
import com.example.prjversami.controllers.PublicacaoController;
import com.example.prjversami.entities.Comentario;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.ImagensUtil;

import java.util.List;

public class AdapterComentarios extends RecyclerView.Adapter{

    private List<Comentario> comentarios;
    private Context context;

    public AdapterComentarios(List<Comentario> comentarios, Context context) {
        this.comentarios = comentarios;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comentario, viewGroup,false);
        ViewHolderComentarios holder = new ViewHolderComentarios(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderComentarios holder = (ViewHolderComentarios) viewHolder;
        int idUsuarioLogado = context.getSharedPreferences("login", Context.MODE_PRIVATE).getInt("id", 0);
        PublicacaoController pc = new PublicacaoController(context);
        Comentario comentario = comentarios.get(i);
        Usuario user = comentario.getUser();

        holder.arrobaUser.setText(user.getUserLogin());
        holder.conteudo.setText(comentario.getContent());
        holder.curtida.setOnCheckedChangeListener(null);
        holder.curtida.setChecked(comentario.isLike());
        if(user.getUserImage() != null){
            holder.fotoPerfil.setImageBitmap(ImagensUtil.converteParaBitmap(user.getUserImage()));
        }else{
            holder.fotoPerfil.setBackgroundColor(Color.parseColor("#d3d3d3"));
            holder.fotoPerfil.setImageResource(R.drawable.user_icon_placeholder2);
        }

        holder.curtida.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(pc.adicionarCurtidaComentario(comentario.getIdComentario(), idUsuarioLogado)){
                        comentario.addLike();
                    }
                }else{
                    if(pc.removeCurtidaComentario(comentario.getIdComentario(), idUsuarioLogado)){
                        comentario.removeLike();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }
}
class ViewHolderComentarios extends RecyclerView.ViewHolder{

    final ImageView fotoPerfil;
    final TextView arrobaUser, conteudo;
    final CheckBox curtida;

    public ViewHolderComentarios(@NonNull View itemView) {
        super(itemView);
        fotoPerfil = itemView.findViewById(R.id.itemComentario_fotoPerfil);
        arrobaUser = itemView.findViewById(R.id.itemComentario_arroba);
        conteudo = itemView.findViewById(R.id.itemComentario_conteudo);
        curtida = itemView.findViewById(R.id.itemComentario_curtida);
    }
}
