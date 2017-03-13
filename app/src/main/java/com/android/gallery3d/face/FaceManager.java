package com.android.gallery3d.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.android.gallery3d.glrenderer.BitmapTexture;
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

    private void createTexture(FaceInfo faceInfo) {
        if (faceInfo != null && faceInfo.mHasFace) {
            Bitmap faceBitmap = Bitmap.createBitmap(faceInfo.mBitmapWidth, faceInfo.mBitmapHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas();
            Paint paint = new Paint();
            paint.setAlpha(50);
            canvas.drawBitmap(faceBitmap, 0,0, paint);
            //paint.setAlpha(0x00);
            for (int i = 0; i < faceInfo.mFaceNumber; i++) {
                //RectF faceRectF = faceInfo.faceLists.get(i).mRect;
               // canvas.drawRect(faceRectF, paint);
            }
            BitmapTexture texture = new BitmapTexture(faceBitmap);
            faceInfo.mFaceTexture = texture;
        }
    }

    public static void drawFace(String filePath, GLCanvas canvas, int x, int y, int width, int height) {
        Log.d(TAG, "drawFaceRect filePath = "+filePath);
        FaceInfo faceInfo = sFaceTable.get(filePath);
        if (null != faceInfo && faceInfo.mHasFace && faceInfo.mFaceTexture != null){
            canvas.drawTexture(faceInfo.mFaceTexture, x, y, width, height);
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
        if (faceInfo != null) {
            createTexture(faceInfo);
        }
        return true;
    }
}
