package com.example.prjversami.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prjversami.R;
import com.example.prjversami.controllers.PublicacaoController;
import com.example.prjversami.entities.Livro;
import com.example.prjversami.entities.Publicacao;
import com.example.prjversami.entities.Usuario;
import com.example.prjversami.util.ImagensUtil;
import com.example.prjversami.util.NavigationUtil;

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

        Usuario user = publicacao.getUsuario();
        Livro livro = publicacao.getLivro();

        holder.content.setText(publicacao.getContent());
        holder.data.setText(publicacao.getPostDate());
        holder.profileName.setText(user.getUserName());
        holder.arroba.setText("@"+user.getUserLogin());
        holder.commentLabel.setText(publicacao.getTotalComentarios().toString());
        holder.like.setText(publicacao.getTotalLikes().toString());
        holder.like.setOnCheckedChangeListener(null);
        holder.like.setChecked(publicacao.isLike());

        if(user.getUserImage() != null){
            holder.profileImage.setImageBitmap(ImagensUtil.converteParaBitmap(user.getUserImage()));
        }else{
            holder.profileImage.setImageResource(R.drawable.user_icon_placeholder2);
            holder.profileImage.setBackgroundColor(Color.parseColor("#D3D3D3"));
        }


        // verifica se  a publicação possui livro vinculado. Se não houver ele oculta as informações

        if(livro != null){
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
                    publicacao.addLike();
                    publicacao.setTotalLikes(publicacao.getTotalLikes()+1);
                    holder.like.setText(publicacao.getTotalLikes().toString());
                }else{
                    controller.removeCurtidas(publicacao.getIdPublicacao());
                    publicacao.removeLike();
                    publicacao.setTotalLikes(publicacao.getTotalLikes()-1);
                    holder.like.setText(publicacao.getTotalLikes().toString());
                }
            }
        });

        holder.userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(user.getUserID() == null) return;

                int id = user.getUserID();
                Fragment fragment = ProfileFragment.newInstance(id);
                NavigationUtil.carregarFragment(((FragmentActivity) context).getSupportFragmentManager(), R.id.fragment_container, fragment);
            }
        });

        holder.postContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostPage.class);
                intent.putExtra("idPublicacao", publicacao.getIdPublicacao());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.publicacao.size();
    }
} // fim classe adapter

class ViewHolderPublicacoes extends RecyclerView.ViewHolder {

    final ImageView profileImage;
    final ImageView bookImage;
    final TextView bookName;
    final TextView profileName;
    final TextView data;
    final TextView content;
    final TextView commentLabel;
    final TextView arroba;
    final ImageButton comments;
    final CheckBox like;
    final LinearLayout bookInfo, commentButton;
    final ConstraintLayout userInfo, postContainer;

    public ViewHolderPublicacoes(@NonNull View itemView) {
        super(itemView);
        profileImage = itemView.findViewById(R.id.profile_post_image);
        bookImage = itemView.findViewById(R.id.profile_post_cover);
        bookName = itemView.findViewById(R.id.profile_post_bookname);
        profileName = itemView.findViewById(R.id.profile_post_name);
        data = itemView.findViewById(R.id.profile_post_date);
        content = itemView.findViewById(R.id.profile_post_textcontent);
        like = itemView.findViewById(R.id.profile_post_like);
        comments = itemView.findViewById(R.id.profile_post_comment);
        commentLabel = itemView.findViewById(R.id.profile_post_labelcomment);
        arroba = itemView.findViewById(R.id.profile_post_username);
        bookInfo = itemView.findViewById(R.id.profile_post_book);
        userInfo = itemView.findViewById(R.id.profile_post_clickuser);
        commentButton = itemView.findViewById(R.id.profile_post_commentbutton);
        postContainer = itemView.findViewById(R.id.profile_post_container);
    }
} // fim classe view holder

