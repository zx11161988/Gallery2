package com.android.gallery3d.face;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.FaceDetector;

/**
 * Created by jingxiang wu on 2017/3/10.
 */
public class FaceDetection {
    private final static int IMAGE_WIDTH = 512;
    private final static int IMAGE_HEIGHT = 512;
    public float mScale = 1.0f;
    FaceDetector.Face[] mFaces = new FaceDetector.Face[3];

    public FaceDetection() {

    }

    public boolean detectFace(Bitmap bitmap) {
        Bitmap faceBitmap = prepareBitmap(bitmap);
        if (faceBitmap != null) {
            FaceDetector detector = new FaceDetector(faceBitmap.getWidth(),faceBitmap.getHeight(), mFaces.length);
            detector.findFaces(faceBitmap, mFaces);
        }
    }

    private Bitmap prepareBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        if (bitmap.getWidth() > IMAGE_WIDTH) {
            mScale = IMAGE_WIDTH / bitmap.getWidth();
            if (mScale * bitmap.getHeight() < 1.0f) {
                mScale = 1.0f / bitmap.getHeight()；
            }
            Matrix matrix = new Matrix();
            matrix.setScale(mScale, mScale);
            Bitmap faceBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return faceBitmap;
        }
    }
}
