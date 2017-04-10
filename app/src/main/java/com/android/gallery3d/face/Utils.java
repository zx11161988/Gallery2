package com.android.gallery3d.face;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by jingxiang wu on 2017/3/9.
 */
public class Utils {
    private final static String TAG = "Utils";
    private static final String SD = Environment.getExternalStorageDirectory().toString();
    private final static String DUMP_PATH = SD + "/.face/";
    private static long[] sCrcTable = new long[256];
    private static final long INITIALCRC = 0xFFFFFFFFFFFFFFFFL;

    public static void dumpBitmap(Bitmap bitmap, String filename) {
        filename = filename + ".jpg";
        File path = new File(DUMP_PATH);
        if (!path.exists()) {
            path.mkdir();
        }
        File file = new File(DUMP_PATH, filename);
        OutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (IOException e) {
            Log.e(TAG, "dump IOExption ");
        } finally {
            closeSilently(fos);
        }
    }

    public static void closeSilently(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (IOException t) {
            Log.w(TAG, "close fail ", t);
        }
    }

    public static byte[] getBytes(String in) {
        byte[] result = new byte[in.length() * 2];
        int output = 0;
        for (char ch : in.toCharArray()) {
            result[output++] = (byte) (ch & 0xFF);
            result[output++] = (byte) (ch >> 8);
        }
        return result;
    }

    public static final long crc64Long(byte[] buffer) {
        long crc = INITIALCRC;
        for (int k = 0, n = buffer.length; k < n; ++k) {
            crc = sCrcTable[(((int) crc) ^ buffer[k]) & 0xff] ^ (crc >> 8);
        }
        return crc;
    }

    public static Bitmap resizeAndCropCenter(Bitmap bitmap, int targetWidth, int targetHeight, boolean recycle) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        if (w == targetWidth && h == targetHeight) return bitmap;

        // scale the image so that the shorter side equals to the target;
        // the longer side will be center-cropped.
        float scale = (float)targetWidth/w;
        if (scale * h < targetHeight) {
            scale = (float)targetHeight/h;
        }
        Bitmap target = Bitmap.createBitmap(targetWidth, targetHeight, getConfig(bitmap));
        int width = Math.round(scale * bitmap.getWidth());
        int height = Math.round(scale * bitmap.getHeight());
        Canvas canvas = new Canvas(target);
        canvas.translate((targetWidth - width) / 2f, (targetHeight - height) / 2f);
        canvas.scale(scale, scale);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        if (recycle) bitmap.recycle();
        return target;
    }

    private static Bitmap.Config getConfig(Bitmap bitmap) {
        Bitmap.Config config = bitmap.getConfig();
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }
        return config;
    }
}
