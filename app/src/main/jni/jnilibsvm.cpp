//==========================================================================
// 2015/08/31: yctung: add this new test for libSVM in jni interface 
//==========================================================================
#include <opencv2/core/core.hpp>
//#include <opencv2/objdetect/detection_based_tracker.hpp>
#include "opencv2/contrib/contrib.hpp"
#include "opencv2/highgui/highgui.hpp"
#include <jni.h>
#include <string.h>
#include <android/log.h>
#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include "./libsvm/svm-train.h"
#include "./libsvm/svm-predict.h"
#include "lbph.h"
#include "common.h"
using namespace cv;

extern "C" jintArray Java_com_android_classification_Svm_jnitrain(JNIEnv *env, jobject thiz,
     jobjectArray trainSetPaths, jintArray trainSetLabels,  jobjectArray predictSetPaths){
    //const char *cmd = env->GetStringUTFChars(trainPath, 0);
	//debug("jniTrain cmd = %s", cmd);
    int size=env->GetArrayLength(trainSetPaths);
    vector<Mat> images;
    for(int i = 0; i < size; i++) {
        jstring obja=(jstring)env->GetObjectArrayElement(trainSetPaths,i);
        const char* chars=env->GetStringUTFChars(obja, NULL);//将jstring类型转换成char类型输出
        debug("jniPredict2:trainSetPaths cmd = %s", chars);
        Mat greymat, colormat;
        colormat = imread(chars);
        if(colormat.data) {
            LOGD("---------------------------------------------- image Loaded");
            cvtColor(colormat, greymat, CV_BGR2GRAY);
            images.push_back(greymat);
        }
        env->ReleaseStringUTFChars(obja, chars);
    }

    jint* labs = env->GetIntArrayElements(trainSetLabels,NULL);
    size = env->GetArrayLength(trainSetLabels);
    vector<int> labels;
    for(int i = 0; i < size; i++) {
        debug("jniPredict2:trainSetLabels cmd = %d", labs[i]);
        labels.push_back(labs[i]);
    }

    Ptr<FaceRecognizer> model = createLBPHFaceRecognizer();
    model->train(images, labels);
    size=env->GetArrayLength(predictSetPaths);
    jintArray jint_arr = env->NewIntArray(size);
    jint *elems = env->GetIntArrayElements(jint_arr, NULL);
    for(int i = 0; i < size; i++) {
        jstring obja=(jstring)env->GetObjectArrayElement(predictSetPaths,i);
        const char* chars=env->GetStringUTFChars(obja,NULL);//将jstring类型转换成char类型输出
        Mat img = imread(chars, CV_LOAD_IMAGE_GRAYSCALE);
        int predicted = model->predict(img);
        debug("jniPredict2:predictSetPaths cmd = %s predicted= %d", chars, predicted);
        elems[i] = predicted;
        env->ReleaseStringUTFChars(obja, chars);
    }
    env->ReleaseIntArrayElements(jint_arr, elems, 0);
    return jint_arr;
}

// helper function to be called in Java for making svm-train
extern "C" void Java_com_android_classification_Svm_jniSvmTrain(JNIEnv *env, jobject obj, jstring cmdIn){
	const char *cmd = env->GetStringUTFChars(cmdIn, 0);
	debug("jniSvmTrain cmd = %s", cmd);

	std::vector<char*> v;

	// add dummy head to meet argv/command format
	std::string cmdString = std::string("dummy ")+std::string(cmd);

	cmdToArgv(cmdString, v);

	// make svm train by libsvm
	svmtrain::main(v.size(),&v[0]);


	// free vector memory
	for(int i=0;i<v.size();i++){
		free(v[i]);
	}

	// free java object memory
	env->ReleaseStringUTFChars(cmdIn, cmd);
}

// helper function to be called in Java for making svm-predict
extern "C" void Java_com_android_classification_Svm_jniSvmPredict(JNIEnv *env, jobject obj, jstring cmdIn){
	const char *cmd = env->GetStringUTFChars(cmdIn, 0);
	debug("jniSvmPredict cmd = %s", cmd);

	std::vector<char*> v;

	// add dummy head to meet argv/command format
	std::string cmdString = std::string("dummy ")+std::string(cmd);

	cmdToArgv(cmdString, v);

	// make svm train by libsvm
	svmpredict::main(v.size(),&v[0]);


	// free vector memory
	for(int i=0;i<v.size();i++){
		free(v[i]);
	}

	// free java object memory
	env->ReleaseStringUTFChars(cmdIn, cmd);
}



/*
*  just some test functions -> can be removed
*/
extern "C" JNIEXPORT int JNICALL Java_com_android_classification_Svm_testInt(JNIEnv * env, jobject obj){
	return 5566;
}

extern "C" void Java_com_android_classification_Svm_testLog(JNIEnv *env, jobject obj, jstring logThis){
	const char * szLogThis = env->GetStringUTFChars(logThis, 0);
	debug("%s",szLogThis);

	env->ReleaseStringUTFChars(logThis, szLogThis);
} 
