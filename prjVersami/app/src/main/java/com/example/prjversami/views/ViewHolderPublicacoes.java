package com.example.prjversami.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prjversami.R;

public class ViewHolderPublicacoes extends RecyclerView.ViewHolder {

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
    final LinearLayout bookInfo;

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
    }
}
