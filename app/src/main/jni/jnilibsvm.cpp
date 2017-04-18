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
#include "common.h"
using namespace cv;

extern "C" void Java_com_android_classification_Svm_jnitrain(JNIEnv *env, jobject obj, jstring trainPath){
    const char *cmd = env->GetStringUTFChars(trainPath, 0);
	debug("jniPredict cmd = %s", cmd);
    Ptr<FaceRecognizer> model2 = createLBPHFaceRecognizer();
    	// free java object memory
    env->ReleaseStringUTFChars(trainPath, cmd);
}

extern "C" void Java_com_android_classification_Svm_jnipredict(JNIEnv *env, jobject obj, jstring predictPath){
	const char *cmd = env->GetStringUTFChars(predictPath, 0);
	debug("jniPredict cmd = %s", cmd);

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
	env->ReleaseStringUTFChars(predictPath, cmd);
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
