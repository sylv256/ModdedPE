//==================================================================================================
// Includes
//==================================================================================================

#include <jni.h>
#include <android/native_activity.h>
#include <dlfcn.h>
#include <string>

//==================================================================================================
// Methods
//==================================================================================================

inline std::string toString(JNIEnv* env, jstring j_str)
{
    //DO NOT RELEASE.
    const char * c_str = env->GetStringUTFChars(j_str, nullptr);
    std::string cpp_str = c_str;
    return cpp_str;
}

bool hook_isGameLicensed()
{
    return true;
}

//==================================================================================================
// Method Pointers
//==================================================================================================

void (*mOnCreateFunc)(ANativeActivity*,void*,size_t) = nullptr;
void (*mFinishFunc)(ANativeActivity*) = nullptr;
void (*mMainFunc)(struct android_app*) = nullptr;
void (*MSHookFunction)(void *symbol, void *replace, void **result) = nullptr;

//==================================================================================================
// Variants
//==================================================================================================

bool isSafeMode;
JavaVM* mJvm = NULL;
std::string mMinecraftNativeLibPath;
std::string* mAndroidAppDataPath;

//==================================================================================================
// NModAPI
//==================================================================================================

extern "C" JNIEXPORT void JNICALL Java_org_mcal_pesdk_nativeapi_NativeUtils_nativeSetDataDirectory(JNIEnv*env,jobject,jstring directory)
{
    *mAndroidAppDataPath = toString(env,directory);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_mcal_pesdk_nmod_NModLib_nativeCallOnActivityFinish(JNIEnv*env,jobject,jstring libName,jobject mainActivity)
{
    jboolean result = JNI_FALSE;
    void* image = dlopen(toString(env,libName).c_str(),RTLD_LAZY);
    void (*NMod_onActivityFinish)(JNIEnv*,jobject) = (void (*)(JNIEnv*,jobject)) dlsym(image,"NMod_OnActivityFinish");
    if(NMod_onActivityFinish)
    {
        NMod_onActivityFinish(env,mainActivity);
        result = JNI_TRUE;
    }
    dlclose(image);
    return result;
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_mcal_pesdk_nmod_NModLib_nativeCallOnLoad(JNIEnv*env,jobject thiz,jstring libname,jstring mcVer,jstring apiVersion)
{
    jboolean result = JNI_FALSE;
    void* image = dlopen(toString(env,libname).c_str(),RTLD_LAZY);
    void (*NMod_onLoad)(JavaVM*,JNIEnv*,const char*,const char*,const char*) = (void (*)(JavaVM*,JNIEnv*,const char*,const char*,const char*)) dlsym(image,"NMod_OnLoad");
    if(NMod_onLoad)
    {
        NMod_onLoad(mJvm,env,toString(env,mcVer).c_str(),toString(env,apiVersion).c_str(),mMinecraftNativeLibPath.c_str());
        result = JNI_TRUE;
    }
    dlclose(image);
    return result;
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_mcal_pesdk_nmod_NModLib_nativeCallOnActivityCreate(JNIEnv*env,jobject thiz,jstring libName,jobject mainActivity,jobject bundle)
{
    jboolean result = JNI_FALSE;
    void* image=dlopen(toString(env,libName).c_str(),RTLD_LAZY);
    void (*NMod_onActivityCreate)(JNIEnv*,jobject,jobject) = (void (*)(JNIEnv*,jobject,jobject)) dlsym(image,"NMod_OnActivityCreate");
    if(NMod_onActivityCreate)
    {
        NMod_onActivityCreate(env,mainActivity,bundle);
        result = JNI_TRUE;
    }
    dlclose(image);
    return result;
}

extern "C" JNIEXPORT void JNICALL Java_org_mcal_pesdk_nativeapi_LibraryLoader_nativeOnPESdkLoaded(JNIEnv* env,jobject,jstring libPath,jboolean safeMode)
{
    mMinecraftNativeLibPath = toString(env,libPath);
    isSafeMode = safeMode;

    void* imageMCPE = dlopen(mMinecraftNativeLibPath.c_str(),RTLD_LAZY);

    mOnCreateFunc = (void(*)(ANativeActivity*,void*,size_t)) dlsym(imageMCPE,"ANativeActivity_onCreate");
    mFinishFunc = (void(*)(ANativeActivity*)) dlsym(imageMCPE,"ANativeActivity_finish");
    mMainFunc =(void(*)(struct android_app*)) dlsym(imageMCPE,"android_main");

    if(!safeMode)
    {
        mAndroidAppDataPath = ((std::string*)dlsym(imageMCPE,"_ZN19AppPlatform_android20ANDROID_APPDATA_PATHE"));
        void* isGameLicensed_ptr = dlsym(imageMCPE,"_ZNK15OfferRepository14isGameLicensedEv");
        MSHookFunction(isGameLicensed_ptr,(void*)&hook_isGameLicensed, nullptr);
    }
    dlclose(imageMCPE);
}

//==================================================================================================
// NativeActivity Reflections
//==================================================================================================

extern "C" void android_main(struct android_app* state)
{
    mMainFunc(state);
}

extern "C" void ANativeActivity_onCreate(ANativeActivity* activity, void* savedState, size_t savedStateSize)
{
    mOnCreateFunc(activity,savedState,savedStateSize);
}

extern "C" void ANativeActivity_finish(ANativeActivity* activity)
{
    mFinishFunc(activity);
}

extern "C" JNIEXPORT jint JNI_OnLoad(JavaVM* vm,void*)
{
    mJvm = vm;

    void* image = dlopen("libsubstrate.so",RTLD_LAZY);
    MSHookFunction = (void(*)(void*,void*,void**))dlsym(image,"MSHookFunction");
    dlclose(image);

    return JNI_VERSION_1_6;
}