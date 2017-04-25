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

extern "C" jint * Java_com_android_classification_Svm_jnitrain(JNIEnv *env, jobject thiz,
     jobjectArray trainSetPaths, jintArray trainSetLabels,  jobjectArray predictSetPaths){
    //const char *cmd = env->GetStringUTFChars(trainPath, 0);
	//debug("jniTrain cmd = %s", cmd);
    int size=env->GetArrayLength(trainSetPaths);
    for(int i = 0; i < size; i++) {
        jstring obja=(jstring)env->GetObjectArrayElement(trainSetPaths,i);
        const char* chars=env->GetStringUTFChars(obja,NULL);//将jstring类型转换成char类型输出
        debug("jniPredict:trainSetPaths cmd = %s", chars);
        env->ReleaseStringUTFChars(obja, chars);
    }

    jint* labs = env->GetIntArrayElements(trainSetLabels,NULL);
    size = env->GetArrayLength(trainSetLabels);
    for(int i = 0; i < size; i++) {
        debug("jniPredict:trainSetLabels cmd = %d", labs[i]);
    }

    size=env->GetArrayLength(predictSetPaths);
    for(int i = 0; i < size; i++) {
        jstring obja=(jstring)env->GetObjectArrayElement(predictSetPaths,i);
        const char* chars=env->GetStringUTFChars(obja,NULL);//将jstring类型转换成char类型输出
        debug("jniPredict:predictSetPaths cmd = %s", chars);
        env->ReleaseStringUTFChars(obja, chars);
    }

    Ptr<FaceRecognizer> model2 = createLBPHFaceRecognizer();
    	// free java object memory
    //env->ReleaseStringUTFChars(trainPath, cmd);
    return labs;
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
