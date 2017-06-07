//-------------------------------------------------------------
// Includes
//-------------------------------------------------------------

#include "Substrate.h"
#include <jni.h>
#include <string>
#include <cxxabi.h>

//-------------------------------------------------------------
// Variants
//-------------------------------------------------------------

bool mGameStarted=false;
JavaVM* mJvm;

//-------------------------------------------------------------
// Strcutures
//-------------------------------------------------------------

struct ScreenChooser
{
	void setStartMenuScreen();
};

//-------------------------------------------------------------
// Methods Definition
//-------------------------------------------------------------

std::string toString(JNIEnv* env, jstring jstr)
{
	char* rtn = NULL;
	jclass clsstring = env->FindClass("java/lang/String");
	jmethodID mid = env->GetMethodID(clsstring, "getBytes", "()[B");
	jbyteArray barr= (jbyteArray)env->CallObjectMethod(jstr,mid);
	jsize alen = env->GetArrayLength(barr);
	jbyte* ba = env->GetByteArrayElements(barr,JNI_FALSE);
	if(alen > 0)
	{
		rtn = (char*)malloc(alen+1);
		memcpy(rtn,ba,alen);
		rtn[alen]=0;
	}
	env->ReleaseByteArrayElements(barr,ba,0);
	std::string stemp(rtn);
	free(rtn);
	return stemp;
}

//-------------------------------------------------------------
// Hook Methods
//-------------------------------------------------------------

void (*setStartMenuScreen_)(void*);
void setStartMenuScreen(void*self)
{
	setStartMenuScreen_(self);
	mGameStarted=true;
}

//-------------------------------------------------------------
// Native Methods
//-------------------------------------------------------------

namespace NModAPI
{
	jboolean nativeIsGameStarted(JNIEnv*env,jobject thiz)
	{
		return mGameStarted;
	}
	
	void nativeSetDataDirectory(JNIEnv*env,jobject thiz,jstring directory)
	{
		void* image=dlopen("libminecraftpe.so",RTLD_LAZY);
	
		std::string& android_app_data_path = *((std::string*)dlsym(image,"_ZN19AppPlatform_android20ANDROID_APPDATA_PATHE"));
		android_app_data_path=toString(env,directory);
		
		dlclose(image);
	}
	jboolean nativeCallOnActivityFinish(JNIEnv*env,jobject thiz,jstring libname,jobject mainActivity)
	{
		void* image=dlopen(toString(env,libname).c_str(),RTLD_LAZY);
		void (*NMod_onActivityFinish)(JNIEnv*env,jobject thiz)=
		(void (*)(JNIEnv*,jobject)) dlsym(image,"NMod_onActivityFinish");
		if(NMod_onActivityFinish)
		{
			NMod_onActivityFinish(env,mainActivity);
		}
		dlclose(image);
	}
	jboolean nativeCallOnLoad(JNIEnv*env,jobject thiz,jstring libname,jstring mcVer,jstring apiVersion)
	{
		void* image=dlopen(toString(env,libname).c_str(),RTLD_LAZY);
		void (*NMod_onLoad)(JavaVM*,JNIEnv*,std::string const&,std::string const&)=
		(void (*)(JavaVM*,JNIEnv*,std::string const&,std::string const&)) dlsym(image,"NMod_onLoad");
		if(NMod_onLoad)
		{
			NMod_onLoad(mJvm,env,toString(env,mcVer),toString(env,apiVersion));
		}
		dlclose(image);
	}
	jboolean nativeCallOnActivityCreate(JNIEnv*env,jobject thiz,jstring libname,jobject mainActivity,jobject bundle)
	{
		void* image=dlopen(toString(env,libname).c_str(),RTLD_LAZY);
		void (*NMod_onActivityCreate)(JNIEnv*env,jobject thiz,jobject savedInstanceState)=
		(void (*)(JNIEnv*,jobject,jobject)) dlsym(image,"NMod_onActivityCreate");
		if(NMod_onActivityCreate)
		{
			NMod_onActivityCreate(env,mainActivity,bundle);
		}
		dlclose(image);
	}
	jstring nativeDemangle(JNIEnv*env,jobject thiz,jstring str)
	{
		char const* symbol_name = toString(env,str).c_str();
		if(symbol_name)
		{
			char const* ret = abi::__cxa_demangle(symbol_name,0,0,0);
			return env->NewStringUTF(ret);
		}
		return env->NewStringUTF("");
	}
	jboolean nativeCallOnDexLoaded(JNIEnv*env,jobject thiz,jstring libname,jobject dexClassLoader)
	{
		void* image=dlopen(toString(env,libname).c_str(),RTLD_LAZY);
		void (*NMod_onDexLoaded)(JNIEnv*env,jobject dexClassLoader)=
		(void (*)(JNIEnv*,jobject)) dlsym(image,"NMod_onDexLoaded");
		if(NMod_onDexLoaded)
		{
			NMod_onDexLoaded(env,dexClassLoader);
		}
		dlclose(image);
	}
}

JNIEXPORT jint JNI_OnLoad(JavaVM*vm,void*)
{
	mJvm=vm;
	//Register Hooks
	MSHookFunction((void*)&ScreenChooser::setStartMenuScreen,(void*)&setStartMenuScreen,(void**)&setStartMenuScreen_);
	//Register Natives
	JNINativeMethod methods[] =
	{
		{"nativeIsGameStarted", "()Z", (void *)&NModAPI::nativeIsGameStarted},
		{"nativeSetDataDirectory", "(Ljava/lang/String;)V", (void *)&NModAPI::nativeSetDataDirectory},
		{"nativeCallOnActivityFinish", "(Ljava/lang/String;Lcom/mojang/minecraftpe/MainActivity;)Z", (void *)&NModAPI::nativeCallOnActivityFinish},
		{"nativeCallOnLoad", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z", (void *)&NModAPI::nativeCallOnLoad},
		{"nativeCallOnActivityCreate", "(Ljava/lang/String;Lcom/mojang/minecraftpe/MainActivity;Landroid/os/Bundle;)Z", (void *)&NModAPI::nativeCallOnActivityCreate},
		{"nativeDemangle", "(Ljava/lang/String;)Ljava/lang/String;", (void *)&NModAPI::nativeDemangle},
		{"nativeCallOnDexLoaded", "(Ljava/lang/String;Ldalvik/system/DexClassLoader;)Z", (void *)&NModAPI::nativeCallOnDexLoaded},
	};
	JNIEnv* env = NULL;
	if (vm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK)
		return JNI_ERR;
	jclass cls = env->FindClass("Lcom/mcal/pesdk/nmod/NModLib;");
	if (cls == NULL)
		return JNI_ERR;
	if (env->RegisterNatives(cls,methods,sizeof(methods)/sizeof(methods[0])) < 0)
		return JNI_ERR;
	return JNI_VERSION_1_6;
}
