package com.android.gallery3d.face;

import android.graphics.RectF;

import com.android.gallery3d.glrenderer.BitmapTexture;

import java.util.ArrayList;

/**
 * Created by jingxiang wu on 2017/3/11.
 */
public class FaceInfo {
    public String mFilePath;
    public String mPath;
    public boolean mHasFace;
    public int mFaceNumber;
    public int mBitmapWidth;
    public int mBitmapHeight;
    public float mScale;
    public BitmapTexture mFaceTexture;
    public ArrayList<Info> faceLists = new ArrayList<Info>();
    public static class Info {
        public RectF mRect;
        public String faceID;
        public String faceName;
        public String faceThumbNail;
        public String getFaceThumbNailPath;
    }
}
