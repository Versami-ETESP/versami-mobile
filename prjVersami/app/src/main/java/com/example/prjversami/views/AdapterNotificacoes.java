package com.example.prjversami.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prjversami.R;
import com.example.prjversami.controllers.NotificacaoController;
import com.example.prjversami.entities.Notificacao;

import java.util.List;

public class AdapterNotificacoes extends RecyclerView.Adapter {

    private List<Notificacao> notificacoes;
    private Context context;

    public AdapterNotificacoes(List<Notificacao> notificacoes, Context context) {
        this.context = context;
        this.notificacoes = notificacoes;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notificacao, viewGroup, false);
        ViewHolderNotificacoes holder = new ViewHolderNotificacoes(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderNotificacoes holder = (ViewHolderNotificacoes) viewHolder;
        Notificacao notificacao = this.notificacoes.get(i);

        holder.msgNotificacao.setText(notificacao.getMsgNotificacao());

        switch (notificacao.getTipoNotificacao()) {
            case NotificacaoController.CURTIDA_COMENTARIO:
            case NotificacaoController.CURTIDA_POST:
                holder.imageView.setImageResource(R.drawable.ic_favorite);
                holder.imageView.setBackgroundColor(ContextCompat.getColor(context, R.color.red_like));

                if(notificacao.getTipoNotificacao() == NotificacaoController.CURTIDA_POST)
                    holder.tipoNotificacao.setText("Nova Curtida na sua Publicação");

                if(notificacao.getTipoNotificacao() == NotificacaoController.CURTIDA_COMENTARIO)
                    holder.tipoNotificacao.setText("Nova Curtida no seu Comentario");
                break;
            case NotificacaoController.COMENTARIO:
                holder.tipoNotificacao.setText("Novo Comentário na sua Publicação");
                holder.imageView.setImageResource(R.drawable.ic_comment_image);
                holder.imageView.setBackgroundColor(ContextCompat.getColor(context, R.color.green_comment));
                break;
            case NotificacaoController.SEGUIU:
                holder.tipoNotificacao.setText("Novo Seguidor");
                holder.imageView.setImageResource(R.drawable.ic_person_add);
                holder.imageView.setBackgroundColor(ContextCompat.getColor(context, R.color.versami_blue));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return this.notificacoes.size();
    }

    public List<Notificacao> getNotificacoes() {
        return notificacoes;
    }
}

class ViewHolderNotificacoes extends RecyclerView.ViewHolder {

    final ImageView imageView;
    final TextView tipoNotificacao;
    final TextView msgNotificacao;
    final LinearLayoutCompat container;

    public ViewHolderNotificacoes(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.itemNotificacao_imagem);
        tipoNotificacao = itemView.findViewById(R.id.itemNotificacao_txtTipo);
        msgNotificacao = itemView.findViewById(R.id.itemNotificacao_mensagem);
        container = itemView.findViewById(R.id.itemNotificacao_container);
    }
}
