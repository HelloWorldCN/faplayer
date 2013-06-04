
LOCAL_PATH := $(call my-dir)

VLCROOT := $(LOCAL_PATH)/vlc
EXTROOT := $(LOCAL_PATH)/ext
LOCALEABILIBROOT := $(LOCAL_PATH)/arm-linux-androideabi

ifeq ($(APP_ABI),armeabi-v7a)
    ifeq ($(FPU),neon)
        LOCALLIBROOT := $(LOCAL_PATH)/cross-compile/build/ffmpeg/neon
    else
        LOCALLIBROOT := $(LOCAL_PATH)/cross-compile/build/ffmpeg/armv7
    endif
else
    LOCALLIBROOT := $(LOCAL_PATH)/cross-compile/build/ffmpeg/vfp
endif

include $(CLEAR_VARS)

include $(call all-makefiles-under,$(LOCAL_PATH))

