package com.android.gallery3d.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.Hashtable;

/**
 * Created by jingxiang wu on 2017/3/9.
 */
public class FaceManager {
    private static String TAG = "FaceManager";
    public static boolean SUPPORT_FACE = true;
    private Context mContext;
    private FaceDatabaseHelper mDBHelper;
    private static Hashtable<String, FaceInfo> sFaceTable = new Hashtable<String, FaceInfo>();

    public FaceManager(Context context) {
        mContext = context;
        mDBHelper = new FaceDatabaseHelper(context);
    }

    public boolean getFace(String path) {
        return false;
    }

    public boolean detectFace(Bitmap bitmap, String path, String filePath) {
        FaceInfo faceInfo = sFaceTable.get(filePath);
        if (faceInfo == null) {
            long beginTime = System.currentTimeMillis();
            Log.d(TAG, "<detectFace> filePath = "+filePath);
            FaceDetection faceDetection = new FaceDetection();
            faceInfo = faceDetection.detectFace(bitmap);
            faceInfo.mFilePath = filePath;
            faceInfo.mPath = path;
            sFaceTable.put(filePath, faceInfo);
            Log.d(TAG, "<detectFace> UseTime =- "+(System.currentTimeMillis() - beginTime));
        } else {
            Log.d(TAG, "<detectFace> faceInfo = "+faceInfo.mHasFace);
        }
        return true;
    }
}
