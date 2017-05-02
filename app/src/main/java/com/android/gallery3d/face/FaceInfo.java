package com.android.gallery3d.face;

import android.graphics.RectF;

import com.android.gallery3d.glrenderer.BitmapTexture;

import java.util.ArrayList;

/**
 * Created by jingxiang wu on 2017/3/11.
 */
public class FaceInfo {
    public final static int TAG_UNDO_CLASSIFICATION = 0;
    public final static int TAG_MANUAL_CLASSIFICATION = 1;
    public final static int TAG_AUTO_CLASSIFICATION = 2;
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
        public int mStates =TAG_UNDO_CLASSIFICATION ;
        public RectF mRect;
        public String faceID;
        public String faceName;
        public String faceThumbNail;
        public String getFaceThumbNailPath;
    }
}
