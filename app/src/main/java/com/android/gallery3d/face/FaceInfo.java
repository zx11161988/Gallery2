package com.android.gallery3d.face;

import android.graphics.RectF;

import java.util.HashMap;

/**
 * Created by jingxiang wu on 2017/3/11.
 */
public class FaceInfo {
    public String mFilePath;
    public String mPath;
    public boolean mHasFace;
    public int mFaceNumber;
    public HashMap<String, Info> mFaceMap = new HashMap<String, Info>();

    public static class Info {
        public RectF mRect;
        public int mBitmapWidth;
        public int mBitmapHeight;
        public float mScale;
        public String faceID;
        public String faceName;
    }
}
