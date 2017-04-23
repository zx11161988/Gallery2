LOCAL_PATH:=$(call my-dir)
include $(CLEAR_VARS)

OPENCV_LIB_TYPE:=STATIC
include D:/OpenCV-2.4.9-android-sdk/sdk/native/jni/OpenCV.mk

LOCAL_MODULE	:= jnisvm
LOCAL_C_INCLUDES += $(LOCAL_PATH)
LOCAL_SRC_FILES := \
	common.cpp jnilibsvm.cpp \
	lbph.cpp \
	libsvm/svm-train.cpp \
	libsvm/svm-predict.cpp \
	libsvm/svm.cpp

LOCAL_LDLIBS	+= -llog -ldl

include $(BUILD_SHARED_LIBRARY)

