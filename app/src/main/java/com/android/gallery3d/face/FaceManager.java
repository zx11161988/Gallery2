package com.android.gallery3d.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import com.android.gallery3d.glrenderer.GLCanvas;
import com.android.gallery3d.glrenderer.GLPaint;

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
    private static GLPaint sGLPaint = new GLPaint();
    public FaceManager(Context context) {
        mContext = context;
        mDBHelper = new FaceDatabaseHelper(context);
        sGLPaint.setLineWidth(2);
        sGLPaint.setColor(0xFFFFFFFF);
    }

    public static void drawFaceRect(String filePath, GLCanvas canvas, Rect rect, float displayScale) {
        Log.d(TAG, "drawFaceRect filePath = "+filePath);
        Log.d(TAG, "drawFaceRect rect= "+ rect.toShortString() +" || displayScale || " + displayScale);
        FaceInfo faceInfo = sFaceTable.get(filePath);
        if (null != faceInfo && faceInfo.mHasFace){
            int number = faceInfo.mFaceNumber;
            for (int i = 0; i < number; i++) {
                FaceInfo.Info info = faceInfo.faceLists.get(i);
                Log.d(TAG, "drawFaceRect = "+ info.mRect.toShortString());
                float left = info.mRect.left;
                float top = info.mRect.top;
                float right = info.mRect.right;
                float bottom = info.mRect.bottom;
                canvas.drawRect(left, top, right, bottom, sGLPaint);
                //canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, sGLPaint);
            }
        }

    }
    public boolean getFace(String path) {
        return false;
    }

    public boolean detectFace(Bitmap bitmap, String path, String filePath) {
        FaceInfo faceInfo = sFaceTable.get(filePath);
        if (faceInfo == null) {
            long beginTime = System.currentTimeMillis();
            Log.d(TAG, "<detectFace> filePath = "+filePath);
            Log.d(TAG, "<detectFace> bitmap = "+bitmap.getWidth() +" "+bitmap.getHeight() );
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
