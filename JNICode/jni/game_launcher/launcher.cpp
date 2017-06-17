#include <jni.h>
#include <android/native_activity.h>
#include <android/log.h>
#include <dlfcn.h>

void (*mOnCreateFunc)(ANativeActivity*,void*,size_t) = 0;
void (*mFinishFunc)(ANativeActivity*) = 0;
void (*mMainFunc)(struct android_app*) = 0;

extern "C" void ANativeActivity_onCreate(ANativeActivity* activity,void* savedState, size_t savedStateSize)
{
	__android_log_print(ANDROID_LOG_INFO,"pesdk-game-launcher","Native on android activity created.");
	mOnCreateFunc(activity,savedState,savedStateSize);
	__android_log_print(ANDROID_LOG_INFO,"pesdk-game-launcher","Reflected on create message to libminecraftpe.so");
}

extern "C" void ANativeActivity_finish(ANativeActivity* activity)
{
	__android_log_print(ANDROID_LOG_INFO,"pesdk-game-launcher","Native on android activity finished.");
	mFinishFunc(activity);
	__android_log_print(ANDROID_LOG_INFO,"pesdk-game-launcher","Reflected on finish message to libminecraftpe.so");
}

extern "C" JNIEXPORT jint JNI_OnLoad(JavaVM*,void*)
{
	return JNI_VERSION_1_6;
}

extern "C" JNIEXPORT void JNICALL Java_org_mcal_pesdk_nativeapi_LibraryLoader_nativeOnLauncherLoaded(JNIEnv*env,jobject thiz,jstring libPath)
{
	__android_log_print(ANDROID_LOG_INFO,"pesdk-game-launcher","Native on launcher loaded.");
	const char * mNativeLibPath = env->GetStringUTFChars(libPath, 0);
	if(mNativeLibPath == NULL)
		__android_log_print(ANDROID_LOG_ERROR,"pesdk-game-launcher","Cannot get native lib path from jstring.");
	void* imageMCPE=(void*) dlopen(mNativeLibPath,RTLD_LAZY);
	if(imageMCPE == NULL)
		__android_log_print(ANDROID_LOG_ERROR,"pesdk-game-launcher","Cannot dlopen libminecraftpe.so");
	mOnCreateFunc = (void(*)(ANativeActivity*,void*,size_t)) dlsym(imageMCPE,"ANativeActivity_onCreate");
	mFinishFunc = (void(*)(ANativeActivity*)) dlsym(imageMCPE,"ANativeActivity_finish");
	mMainFunc =(void(*)(struct android_app*)) dlsym(imageMCPE,"android_main");
	dlclose(imageMCPE);
	env->ReleaseStringUTFChars(libPath,mNativeLibPath);
	__android_log_print(ANDROID_LOG_INFO,"pesdk-game-launcher","Native on launcher loaded done.");
}

extern "C" void android_main(struct android_app* state)
{
	__android_log_print(ANDROID_LOG_INFO,"pesdk-game-launcher","Native on android main.");
	mMainFunc(state);
	__android_log_print(ANDROID_LOG_INFO,"pesdk-game-launcher","Reflected on android main.");
}
