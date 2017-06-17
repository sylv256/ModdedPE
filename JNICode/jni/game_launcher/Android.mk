LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_CPP_EXTENSION := .cpp .cc
LOCAL_MODULE    := pesdk-game-launcher
LOCAL_SRC_FILES := launcher.cpp

ifeq ($(TARGET_ARCH_ABI),x86)
    LOCAL_CFLAGS += -ffast-math -mtune=atom -mssse3 -mfpmath=sse
endif

LOCAL_STATIC_LIBRARIES := android_native_app_glue
LOCAL_LDLIBS    := -L$(LOCAL_PATH)/$(TARGET_ARCH_ABI) -ldl -landroid -llog

include $(BUILD_SHARED_LIBRARY)
