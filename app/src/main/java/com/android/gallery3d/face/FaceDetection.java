package com.android.gallery3d.face;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.util.Log;

/**
 * Created by jingxiang wu on 2017/3/10.
 */
public class FaceDetection {
    private static String TAG = "FaceDetection";
    private final static float IMAGE_WIDTH = 256;
    private final static int IMAGE_HEIGHT = 512;
    private int mBitmapWidth;
    private int mBitmapHeight;
    public float mScale = 1.0f;
    public static FaceDetector.Face[] mFaces = new FaceDetector.Face[3];

    public FaceDetection() {

    }

    public FaceInfo detectFace(Bitmap bitmap) {
        Bitmap faceBitmap = prepareBitmap(bitmap);
        Log.d(TAG, "faceBitmap = "+faceBitmap.getWidth() +" "+faceBitmap.getHeight());
        if (faceBitmap != null) {
            FaceDetector detector = new FaceDetector(faceBitmap.getWidth(), faceBitmap.getHeight(), mFaces.length);
            Bitmap temface = faceBitmap.copy(Bitmap.Config.RGB_565, true);
            int numberface = detector.findFaces(temface, mFaces);
            temface.recycle();
            if (bitmap != faceBitmap) {
                faceBitmap.recycle();
                faceBitmap = null;
            }
            return handleFace(numberface);
        }
        return null;
    }

    private FaceInfo handleFace(int numberFace) {
        Log.d(TAG, "numberFace = "+numberFace);
        FaceInfo faceInfo = new FaceInfo();
        faceInfo.mFaceNumber = numberFace;
        if (numberFace <= 0) {
            faceInfo.mHasFace = false;
            return faceInfo;
        }
        faceInfo.mHasFace = true;
        faceInfo.mBitmapWidth = mBitmapWidth;
        faceInfo.mBitmapHeight = mBitmapHeight;
        float scale = 1 / mScale;
        for (int i = 0; i < numberFace; i++) {
            FaceDetector.Face f = mFaces[i];
            PointF midPoint = new PointF();
            int r = (int) (f.eyesDistance() * scale) * 2;
            f.getMidPoint(midPoint);
            midPoint.x *= scale;
            midPoint.y *= scale;
            int midX = (int) midPoint.x;
            int midY = (int) midPoint.y;
            Rect imageRect = new Rect(0, 0, mBitmapWidth, mBitmapHeight);
            RectF faceRect = new RectF(midX, midY, midX, midY);
            faceRect.inset(-r, -r);
            if (faceRect.left < 0) {
                faceRect.inset(-faceRect.left, -faceRect.left);
            }
            if (faceRect.top < 0) {
                faceRect.inset(-faceRect.top, -faceRect.top);
            }
            if (faceRect.right > imageRect.right) {
                faceRect.inset(faceRect.right - imageRect.right, faceRect.right - imageRect.right);
            }
            if (faceRect.bottom > imageRect.bottom) {
                faceRect.inset(faceRect.bottom - imageRect.bottom, faceRect.bottom - imageRect.bottom);
            }
            FaceInfo.Info info = new FaceInfo.Info();
            info.faceID = Integer.toString(i);
            info.faceName = info.faceID + "No name";
            info.mRect = faceRect;
            faceInfo.faceLists.add(info);
            Log.d(TAG, "<handleFace > face: " + i + " faceRect = " + faceRect.toShortString());
        }
        return faceInfo;
    }
public static int index = 0;
    private Bitmap prepareBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        mBitmapWidth = bitmap.getWidth();
        mBitmapHeight = bitmap.getHeight();
        if (mBitmapWidth > IMAGE_WIDTH) {
            mScale = IMAGE_WIDTH / mBitmapWidth;
            Log.d(TAG, "scale = " + mScale);
            if (mScale * mBitmapHeight < 1.0f) {
                mScale = 1.0f / mBitmapHeight;
            }
            Matrix matrix = new Matrix();
            matrix.setScale(mScale, mScale);
            Bitmap faceBitmap = Bitmap.createBitmap(bitmap, 0, 0, mBitmapWidth, mBitmapHeight, matrix, true);
            //Utils.dumpBitmap(bitmap, "dump"+(index++));
            //Log.d(TAG, "dump ="+ " dump"+index +"  "+faceBitmap.getConfig().toString());
            return faceBitmap;
        }
        return bitmap;
    }

}
