package com.example.prjversami.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prjversami.R;
import com.example.prjversami.entities.Livro;
import com.example.prjversami.entities.Publicacao;
import com.example.prjversami.entities.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImagensUtil {

    public static byte[] converteParaBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        boolean sucesso = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap converteParaBitmap(byte[] image) {

        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static void salvarImagem(byte[] bytes, String nomeArquivo, Context context){
        try (FileOutputStream fos = context.openFileOutput(nomeArquivo, Context.MODE_PRIVATE)) {
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap pegarImagem(String nomeArquivo, Context context){
        try (FileInputStream fis = context.openFileInput(nomeArquivo)) {
            return BitmapFactory.decodeStream(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap criarStickerPublicacao(Context context, Publicacao publicacao){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.sticker_layout, null);

        TextView nomeUser, arrobaUser, conteudoPost, nomeLivro;
        ImageView imagemUser, imagemLivro;
        LinearLayout containerLivro;

        nomeUser = view.findViewById(R.id.sticker_name);
        arrobaUser = view.findViewById(R.id.sticker_username);
        conteudoPost = view.findViewById(R.id.sticker_textcontent);
        nomeLivro = view.findViewById(R.id.sticker_bookname);
        imagemUser = view.findViewById(R.id.sticker_image);
        imagemLivro = view.findViewById(R.id.sticker_cover);
        containerLivro = view.findViewById(R.id.sticker_book);

        Livro livro = publicacao.getLivro();
        Usuario user = publicacao.getUsuario();

        if(user.getUserImage() != null){
            imagemUser.setImageBitmap(ImagensUtil.converteParaBitmap(user.getUserImage()));
        }else{
            imagemUser.setImageResource(R.drawable.user_icon_placeholder2);
            imagemUser.setBackgroundColor(Color.parseColor("#D3D3D3"));
        }

        nomeUser.setText(user.getUserName());
        arrobaUser.setText("@"+user.getUserLogin());
        conteudoPost.setText(publicacao.getContent());

        if(livro != null){
            containerLivro.setVisibility(View.VISIBLE);
            nomeLivro.setText(livro.getTitle());
            imagemLivro.setImageBitmap(ImagensUtil.converteParaBitmap(livro.getCover()));
        }else{
            containerLivro.setVisibility(View.GONE);
        }

        int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        bitmap = ImagensUtil.arredondarBitmap(bitmap);

        return bitmap;
    }

    public static Bitmap arredondarBitmap(Bitmap bitmapOriginal){
        float radius = 48f;

        Bitmap rounded = Bitmap.createBitmap(bitmapOriginal.getWidth(), bitmapOriginal.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas roundedCanvas = new Canvas(rounded);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        RectF rect = new RectF(0f, 0f, bitmapOriginal.getWidth(), bitmapOriginal.getHeight());
        roundedCanvas.drawRoundRect(rect, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        roundedCanvas.drawBitmap(bitmapOriginal, 0f, 0f, paint);

        return rounded;
    }
}
