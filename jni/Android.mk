
LOCAL_PATH := $(call my-dir)

VLCROOT := $(LOCAL_PATH)/vlc
EXTROOT := $(LOCAL_PATH)/ext
LOCALLIBROOT := $(LOCAL_PATH)/arm-linux-androideabi

include $(CLEAR_VARS)

include $(call all-makefiles-under,$(LOCAL_PATH))

