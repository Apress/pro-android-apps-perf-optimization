LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := fibonacci
LOCAL_ARM_MODE := thumb
LOCAL_SRC_FILES := com_apress_proandroid_ndk_all.c fibonacci.c
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := fibonarmcci
LOCAL_ARM_MODE := arm
LOCAL_SRC_FILES := com_apress_proandroid_ndk_all.c fibonacci.c
include $(BUILD_SHARED_LIBRARY)
