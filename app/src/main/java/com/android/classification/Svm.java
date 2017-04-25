package com.android.classification;

import com.android.gallery3d.face.FaceInfo;
import com.android.gallery3d.faceditor.Info;

/**
 * Created by jingxiang wu on 2017/3/21.
 */

public class Svm {
    // connect the native functions
    static{
        System.loadLibrary("jnisvm");
    }
    public int[] onClassfication(String[] trainPaths, int[] trainlabels, String[] predictpaths) {
        return jnitrain(trainPaths, trainlabels, predictpaths);
    }
    private native void jniSvmTrain(String cmd);
    private native void jniSvmPredict(String cmd);
    private native int[] jnitrain(String[] trainPaths, int[] trainlabels, String[] predictpaths);
    private native void jnipredict(String predictPath);
    //private natvie

}
