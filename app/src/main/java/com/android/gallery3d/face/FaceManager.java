package com.android.gallery3d.face;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by jingxiang wu on 2017/3/9.
 */
public class FaceManager {
    public static boolean SUPPORT_FACE = true;
    private Context mContext;
    private FaceDatabaseHelper mDBHelper;


    public FaceManager(Context context) {
        mContext = context;
        mDBHelper = new FaceDatabaseHelper(context);
    }

    public boolean getFace(String path,) {

    }

    public boolean detectFace(Bitmap bitmap, String path, String filePath) {

    }
}
