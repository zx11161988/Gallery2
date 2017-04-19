package com.android.gallery3d.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.android.classification.Svm;
import com.android.gallery3d.glrenderer.BitmapTexture;
import com.android.gallery3d.glrenderer.GLCanvas;
import com.android.gallery3d.glrenderer.GLPaint;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by jingxiang wu on 2017/3/9.
 */
public class FaceManager {
    private static String TAG = "FaceManager";
    public static boolean SUPPORT_FACE = true;
    private Context mContext;
    private FaceDatabaseHelper mDBHelper;
    private Hashtable<String, FaceInfo> mFaceTable = new Hashtable<String, FaceInfo>();
    private static GLPaint sGLPaint = new GLPaint();
    private static int sThumbNailWidth = 92;
    private static int sThumbNailHeight = 112;
    private Svm mSvm;
    public FaceManager(Context context) {
        mContext = context;
        mDBHelper = new FaceDatabaseHelper(context);
        sGLPaint.setLineWidth(2);
        sGLPaint.setColor(0xFFFFFFFF);
        mSvm = new Svm();
        mSvm.test();

    }
    public FaceInfo getFaceInfo(String filePath) {
        return mFaceTable.get(filePath);
    }

    public boolean supportedOperations(String filePath) {
        Log.d(TAG, "supportedOperations filePath = "+filePath);
        FaceInfo faceInfo = mFaceTable.get(filePath);
        if (SUPPORT_FACE && faceInfo != null) {
            return faceInfo.mHasFace;
        } else {
            return false;
        }
    }

    private void createTexture(FaceInfo faceInfo, Bitmap background) {
        if (faceInfo != null && faceInfo.mHasFace) {
            Bitmap faceBitmap = Bitmap.createBitmap(faceInfo.mBitmapWidth, faceInfo.mBitmapHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(faceBitmap);
            Paint paint = new Paint();
            //paint.setAlpha(255);
            //canvas.drawBitmap(background, 0,0, paint);
            canvas.drawColor(Color.TRANSPARENT);
            paint.setAlpha(0);
            paint.setColor(0xFFFFFFFF);;
            for (int i = 0; i < faceInfo.mFaceNumber; i++) {
                RectF faceRectF = faceInfo.faceLists.get(i).mRect;
                float[] points = new float[]{
                            faceRectF.left, faceRectF.top,
                            faceRectF.right,faceRectF.top,
                            faceRectF.right,faceRectF.top,
                            faceRectF.right,faceRectF.bottom,
                            faceRectF.right,faceRectF.bottom,
                            faceRectF.left,faceRectF.bottom,
                            faceRectF.left,faceRectF.bottom,
                            faceRectF.left, faceRectF.top
                    };
                canvas.drawLines(points, paint);
                //canvas.drawRect(faceRectF, paint);
            }
            BitmapTexture texture = new BitmapTexture(faceBitmap);
            texture.setOpaque(false);
            faceInfo.mFaceTexture = texture;
        }
    }

    public void drawFace(String filePath, GLCanvas canvas, int x, int y, int width, int height) {
        Log.d(TAG, "drawFaceRect filePath = "+filePath);
        FaceInfo faceInfo = mFaceTable.get(filePath);
        if (null != faceInfo && faceInfo.mHasFace && faceInfo.mFaceTexture != null){
            canvas.drawTexture(faceInfo.mFaceTexture, x, y, width, height);
        }
    }

    public synchronized ArrayList<FaceInfo.Info> getFaceList() {
        if (mFaceTable != null) {
            ArrayList<FaceInfo.Info> list = new ArrayList<FaceInfo.Info>();
            Iterator<Map.Entry<String, FaceInfo>> it = mFaceTable.entrySet().iterator();
                while(it.hasNext()) {
                    FaceInfo faceInfo = it.next().getValue();
                    if (faceInfo.mHasFace) {
                        list.addAll(faceInfo.faceLists);
                    }
                }
            return list;
        }
        return null;
    }

    public synchronized boolean detectFace(Bitmap bitmap, String path, String filePath) {
        FaceInfo faceInfo = mFaceTable.get(filePath);
        if (faceInfo == null) {
            long beginTime = System.currentTimeMillis();
            Log.d(TAG, "<detectFace> filePath = "+filePath);
            Log.d(TAG, "<detectFace> bitmap = "+bitmap.getWidth() +" "+bitmap.getHeight() );
            FaceDetection faceDetection = new FaceDetection();
            faceInfo = faceDetection.detectFace(bitmap);
            faceInfo.mFilePath = filePath;
            faceInfo.mPath = path;
            mFaceTable.put(filePath, faceInfo);
            Log.d(TAG, "<detectFace> UseTime =- "+(System.currentTimeMillis() - beginTime));
        } else {
            Log.d(TAG, "<detectFace> faceInfo = "+faceInfo.mHasFace);
        }
        if (faceInfo != null && faceInfo.mFaceTexture == null) {
            createTexture(faceInfo, bitmap);
            createFaceThumbNail(faceInfo, bitmap);
        }

        return true;
    }
    public static  int index = 0;
    private void createFaceThumbNail(FaceInfo faceInfo, Bitmap background) {
        if (!faceInfo.mHasFace) {
            return;
        }
        int size = faceInfo.faceLists.size();
        for(int i = 0; i < size; i++) {
            FaceInfo.Info info = faceInfo.faceLists.get(i);
            RectF faceRectF = info.mRect;
            int x = (int) faceRectF.left;
            int y = (int) faceRectF.top;
            int width = (int) faceRectF.width();
            int height = (int) faceRectF.height();
            Bitmap face = Bitmap.createBitmap(background, x, y, width, height);
            face = Utils.resizeAndCropCenter(face, sThumbNailWidth, sThumbNailHeight, true);
            String[] c = faceInfo.mFilePath.split("\\/|\\.");
            info.faceThumbNail = c[c.length -2]+"_"+info.faceID;
            info.getFaceThumbNailPath = Utils.DUMP_PATH + ""+info.faceThumbNail+ "."+ Utils.SUFFIX;
            Log.d(TAG, "<detectFace> faceInfo = "+ info.faceThumbNail);
            Utils.dumpBitmap(face, info.faceThumbNail);
        }
    }
}
