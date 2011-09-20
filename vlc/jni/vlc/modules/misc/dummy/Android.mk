
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

ifeq ($(BUILD_WITH_ARM),1)
LOCAL_ARM_MODE := arm
endif

LOCAL_MODULE := dummy_plugin

LOCAL_CFLAGS += \
    -std=gnu99 \
    -DHAVE_CONFIG_H \
    -DMODULE_STRING=\"dummy\" \
    -DMODULE_NAME=dummy

LOCAL_C_INCLUDES += \
    $(VLCROOT) \
    $(VLCROOT)/include

LOCAL_SRC_FILES := \
    aout.c \
    decoder.c \
    dummy.c \
    encoder.c \
    input.c \
    interface.c \
    renderer.c \
    vout.c

include $(BUILD_STATIC_LIBRARY)
