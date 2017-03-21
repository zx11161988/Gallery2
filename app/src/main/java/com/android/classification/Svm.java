package com.android.classification;

/**
 * Created by jingxiang wu on 2017/3/21.
 */

public class Svm {
    // connect the native functions
    private native void jniSvmTrain(String cmd);
    private native void jniSvmPredict(String cmd);

}
