LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := chapter3
LOCAL_SRC_FILES := com_apress_proandroid_assembly_AssemblyActivity.c gcd.c rgb.c avg8.c vector.c

ifeq ($(TARGET_ARCH_ABI),armeabi)
LOCAL_SRC_FILES += gcd_asm.S
endif

ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
LOCAL_SRC_FILES += gcd_asm.S rgb_asm.S.arm avg8_asm.S thumb_mode_only.c
endif

LOCAL_STATIC_LIBRARIES := cpufeatures

include $(BUILD_SHARED_LIBRARY)

$(call import-module,android/cpufeatures)
