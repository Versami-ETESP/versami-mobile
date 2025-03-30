package com.example.prjversami.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
}
