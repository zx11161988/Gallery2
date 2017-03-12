package com.android.gallery3d.face;

import android.graphics.Bitmap;
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
    private final static String DUMP_PATH = SD + "/GalleryIssue/";

    public static void dumpBitmap(Bitmap bitmap, String filename) {
        filename = filename + ".png";
        File path = new File(DUMP_PATH);
        if (!path.exists()) {
            path.mkdir();
        }
        File file = new File(DUMP_PATH, filename);
        OutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
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
}
