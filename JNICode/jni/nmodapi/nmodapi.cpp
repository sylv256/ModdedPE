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

bool mGameStarted = false;
JavaVM* mJvm = NULL;
std::string* mAddrAndroidAppDataPath = NULL;
std::string mMCPENativeLibPath;

//-------------------------------------------------------------
// Methods Definition
//-------------------------------------------------------------

std::string toString(JNIEnv* env, jstring j_str)
{
	const char * c_str = env->GetStringUTFChars(j_str, 0);
	std::string cpp_str = c_str;
	env->ReleaseStringUTFChars(j_str,c_str);
	return cpp_str;
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
		*mAddrAndroidAppDataPath = toString(env,directory);
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
	jboolean nativeCallOnLoad(JNIEnv*env,jobject thiz,jstring libname,jstring mcVer,jstring apiVersion,jstring libminecraftpePath)
	{
		void* image=dlopen(toString(env,libname).c_str(),RTLD_LAZY);
		void (*NMod_onLoad)(JavaVM*,JNIEnv*,std::string const&,std::string const&,std::string const&)=
		(void (*)(JavaVM*,JNIEnv*,std::string const&,std::string const&,std::string const&)) dlsym(image,"NMod_onLoad");
		if(NMod_onLoad)
		{
			NMod_onLoad(mJvm,env,toString(env,mcVer),toString(env,apiVersion),toString(env,libminecraftpePath));
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

//-------------------------------------------------------------
// Register Natives
//-------------------------------------------------------------

extern "C" JNIEXPORT jboolean JNICALL Java_org_mcal_pesdk_nmod_NModLib_nativeRegisterNatives(JNIEnv*env,jobject thiz,jclass cls)
{
	JNINativeMethod methods[] =
	{
		{"nativeCallOnActivityFinish", "(Ljava/lang/String;Lcom/mojang/minecraftpe/MainActivity;)Z", (void *)&NModAPI::nativeCallOnActivityFinish},
		{"nativeCallOnLoad", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z", (void *)&NModAPI::nativeCallOnLoad},
		{"nativeCallOnActivityCreate", "(Ljava/lang/String;Lcom/mojang/minecraftpe/MainActivity;Landroid/os/Bundle;)Z", (void *)&NModAPI::nativeCallOnActivityCreate},
		{"nativeCallOnDexLoaded", "(Ljava/lang/String;Ldalvik/system/DexClassLoader;)Z", (void *)&NModAPI::nativeCallOnDexLoaded}
	};
	
	if (env->RegisterNatives(cls,methods,sizeof(methods)/sizeof(JNINativeMethod)) < 0)
		return JNI_FALSE;
	return JNI_TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_mcal_pesdk_nativeapi_NativeUtils_nativeRegisterNatives(JNIEnv*env,jobject thiz,jclass cls)
{
	JNINativeMethod methods[] =
	{
		{"nativeIsGameStarted", "()Z", (void *)&NModAPI::nativeIsGameStarted},
		{"nativeSetDataDirectory", "(Ljava/lang/String;)V", (void *)&NModAPI::nativeSetDataDirectory},
		{"nativeDemangle", "(Ljava/lang/String;)Ljava/lang/String;", (void *)&NModAPI::nativeDemangle}
	};
	
	if (env->RegisterNatives(cls,methods,sizeof(methods)/sizeof(JNINativeMethod)) < 0)
		return JNI_FALSE;
	return JNI_TRUE;
}

extern "C" JNIEXPORT void JNICALL Java_org_mcal_pesdk_nativeapi_LibraryLoader_nativeOnNModAPILoaded(JNIEnv*env,jobject thiz,jstring libPath)
{
	const char* mNativeLibPath = toString(env,libPath).c_str();
	mMCPENativeLibPath = mNativeLibPath;
	void* imageMCPE = (void*)dlopen(mNativeLibPath,RTLD_LAZY);
	mAddrAndroidAppDataPath = ((std::string*)dlsym(imageMCPE,"_ZN19AppPlatform_android20ANDROID_APPDATA_PATHE"));
	void* ptr_setStartMenuScreen = (void*)dlsym(imageMCPE,"_ZN13ScreenChooser18setStartMenuScreenEv");
	MSHookFunction(ptr_setStartMenuScreen,(void*)&setStartMenuScreen,(void**)&setStartMenuScreen_);
	dlclose(imageMCPE);
}

//-------------------------------------------------------------
// On Load
//-------------------------------------------------------------

JNIEXPORT jint JNI_OnLoad(JavaVM*vm,void*)
{
	mJvm=vm;
	return JNI_VERSION_1_6;
}
