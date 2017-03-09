package com.android.gallery3d.face;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jingxiang wu on 2017/3/9.
 */
public class FaceDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "FaceDatabaseHelper";
    private static final String DATABASE_NAME = "face.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_WIDGETS = "face";

    private static final String FIELD_ID = "id";
    private static final String FIELD_FACE_ID = "faceID";
    private static final String FIELD_FACE_NAME = "faceName";
    private static final String FIELD_PATH = "filePath";
    private static final String FIELD_BUCKID = "buckID";
    private static final String FIELD_RECT = "rect";
    private static final String FIELD_IMAGE_WIDTH = "imageWidth";
    private static final String FIELD_IMAGE_HEIGHT = "imageHeight";
    private static final String FIELD_BITMAP_BYTE = "imageData";

    public static class Entry {
        public int id;
        public int faceID;
        public String faceName;
        public String filePath;
        public int buckID;
        public String faceRect;
        public int imageWidth;
        public int imageHeight;
        public byte imageData[];
    }

    public FaceDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // id, faceId, faceName, Path，buckId, Rect, imageWidth, imageHeight. BitmapByte,
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_WIDGETS + " ("
                + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FIELD_FACE_ID + " INTEGER DEFAULT 0, "
                + FIELD_FACE_NAME + " TEXT, "
                + FIELD_BUCKID + " INTEGER,"
                + FIELD_PATH + " TEXT, "
                + FIELD_RECT + " TEXT, "
                + FIELD_IMAGE_WIDTH + " INTEGER, "
                + FIELD_IMAGE_HEIGHT + " INTEGER, "
                + FIELD_BITMAP_BYTE + " BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void  addFace() {

    }

    public Entry getFace() {
        return null;
    }
}
