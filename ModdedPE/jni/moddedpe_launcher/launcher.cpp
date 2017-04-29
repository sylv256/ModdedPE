#include <jni.h>
#include <android/native_activity.h>
#include <dlfcn.h>

#include "Substrate.h"

ANativeActivity* native_activity;
void* saved_state;
size_t saved_state_size;

extern "C" void ANativeActivity_onCreate(ANativeActivity* activity,void* savedState, size_t savedStateSize)
{
	native_activity = activity;
	saved_state = savedState;
	saved_state_size = savedStateSize;
}

extern "C" void ANativeActivity_finish(ANativeActivity* activity)
{
	//Finishing MCPE
	void* imageMCPE=(void*) dlopen("libminecraftpe.so",RTLD_LAZY);
	void (*finishFunc)(ANativeActivity*) = (void(*)(ANativeActivity*)) dlsym(imageMCPE,"ANativeActivity_finish");
	finishFunc(activity);
	dlclose(imageMCPE);
}

extern "C" JNIEXPORT jint JNI_OnLoad(JavaVM*,void*)
{
	return JNI_VERSION_1_6;
}

extern "C" JNIEXPORT void Java_com_mcal_ModdedPE_nativeapi_GameLauncher_launch(JNIEnv*env,jobject thiz)
{
	//Starting MCPE by calling ANativeActivity_onCreate in libminecraftpe.so
	void* imageMCPE=(void*) dlopen("libminecraftpe.so",RTLD_LAZY);
	void (*onCreateFunc)(ANativeActivity*,void*,size_t) = (void(*)(ANativeActivity*,void*,size_t)) dlsym(imageMCPE,"ANativeActivity_onCreate");
	onCreateFunc(native_activity,saved_state,saved_state_size);
	dlclose(imageMCPE);
}


extern "C" void android_main(struct android_app* state)
{
	//Starting MCPE
	//The game works successfully although you don' call android_main
	void* imageMCPE=(void*) dlopen("libminecraftpe.so",RTLD_LAZY);
	void (*mainFunc)(struct android_app*) =(void(*)(struct android_app*)) dlsym(imageMCPE,"android_main");
	mainFunc(state);
	dlclose(imageMCPE);
}
