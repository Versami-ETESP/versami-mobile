package com.example.prjversami.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class ImagensUtil {

    public static byte[] converteParaBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap converteParaBitmap(byte[] image) {

        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
