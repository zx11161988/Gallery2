package com.android.gallery3d.face;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by jingxiang wu on 2017/3/9.
 */
public class Utils {
    private final static String TAG = "Utils";

    public static void closeSilently(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (IOException t) {
            Log.w(TAG, "close fail ", t);
        }
    }
}
