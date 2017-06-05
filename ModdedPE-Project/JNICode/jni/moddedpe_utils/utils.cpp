//-------------------------------------------------------------
// Includes
//-------------------------------------------------------------

#include "Substrate.h"
#include <jni.h>
#include <vector>
#include <string>
#include <fstream>
#include <cxxabi.h>
#include <sstream>

#include "mcpe/client/gui/screen/ScreenChooser.h"
#include "mcpe/client/resources/Localization.h"
#include "mcpe/client/resources/I18n.h"

//-------------------------------------------------------------
// Structure Definition
//-------------------------------------------------------------

struct LanguageBean
{
	LanguageBean(std::string name,std::string translation)
	{
		this->name=name;
		this->translation=translation;
	}
	
	std::string name;
	std::string translation;
	bool operator==(LanguageBean const&b)
	{
		return b.name==name&&b.translation==translation;
	}
};

//-------------------------------------------------------------
// Variant Definition
//-------------------------------------------------------------

std::vector<LanguageBean> mLanguageBeans;
bool mGameStarted=false;
JavaVM* mJvm;

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

inline std::string getStringFromLanguageBean(std::string const&languageName,std::string const&textKey)
{
	for(LanguageBean const&bean:mLanguageBeans)
	{
		if(bean.name==languageName)
		{
			if(bean.translation.find("=")!=std::string::npos)
			{
				if(textKey==bean.translation.substr(0,bean.translation.find("=")))
					return bean.translation.substr(bean.translation.find("=")+1,bean.translation.length());
			}
		}
	}
	return textKey;
}

inline std::string getStringFromLanguageBean(std::string const&languageName,std::string const&textKey,std::vector<std::string>textList)
{
	std::string text=getStringFromLanguageBean(languageName,textKey);
	if(text==textKey)
		return textKey;
	
	for(size_t index=0;index<textList.size();++index)
	{
		std::stringstream stream;
		stream<<index+1;
		std::string str;
		stream>>str;
		while(text.find("%"+str+"$s")!=std::string::npos)
		{
			text.replace(text.find("%"+str+"$s"),4,textList[index]);
		}
	}
	return text;
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

void (*loadFromResourcePackManager_)(Localization*,ResourcePackManager&, std::vector<std::string, std::allocator<std::string> > const&);
void loadFromResourcePackManager(Localization*self,ResourcePackManager&manager, std::vector<std::string, std::allocator<std::string> > const&others)
{
	loadFromResourcePackManager_(self,manager,others);
	for(LanguageBean const& bean:mLanguageBeans)
	{
		if(bean.name==self->getFullLanguageCode())
			self->appendTranslations(bean.translation,others,others,bean.translation);
	}
}

std::string (*getI18n_)(std::string const&);
std::string getI18n(std::string const&key)
{
	std::string result=getStringFromLanguageBean(I18n::getCurrentLanguage()->getFullLanguageCode(),key);
	if(result==key)
		return getI18n_(key);
	return result;
}

std::string (*getI18nl_)(std::string const&,std::vector<std::string> const&);
std::string getI18nl(std::string const&key,std::vector<std::string> const&list)
{
	std::string result=getStringFromLanguageBean(I18n::getCurrentLanguage()->getFullLanguageCode(),key,list);
	if(result==key)
		return getI18nl_(key,list);
	return result;
}

//-------------------------------------------------------------
// Native Interface
//-------------------------------------------------------------

extern "C"
{
	JNIEXPORT jboolean Java_com_mcal_pesdk_nativeapi_NativeUtils_nativeIsGameStarted(JNIEnv*env,jobject thiz)
	{
		return mGameStarted;
	}
	
	JNIEXPORT void Java_com_mcal_pesdk_nativeapi_NativeUtils_nativeSetDataDirectory(JNIEnv*env,jobject thiz,jstring directory)
	{
		void* image=dlopen("libminecraftpe.so",RTLD_LAZY);
	
		std::string& android_app_data_path = *((std::string*)dlsym(image,"_ZN19AppPlatform_android20ANDROID_APPDATA_PATHE"));
		android_app_data_path=toString(env,directory);
		
		dlclose(image);
	}
	JNIEXPORT void Java_com_mcal_pesdk_nmod_NModLoader_nativeAppendTranslation(JNIEnv*env,jobject thiz,jstring name,jstring translation)
	{
		std::string nameN=toString(env,name);
		std::string translationN=toString(env,translation);
		
		LanguageBean bean(nameN,translationN);
		mLanguageBeans.emplace_back(bean);
	}
	JNIEXPORT void Java_com_mcal_pesdk_nmod_NModLoader_nativeCallOnActivityFinish(JNIEnv*env,jobject thiz,jstring libname,jobject mainActivity)
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
	JNIEXPORT void Java_com_mcal_pesdk_nmod_NModLoader_nativeCallOnLoad(JNIEnv*env,jobject thiz,jstring libname,jstring mcVer,jstring apiVersion)
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
	JNIEXPORT void Java_com_mcal_pesdk_nmod_NModLoader_nativeCallOnActivityCreate(JNIEnv*env,jobject thiz,jstring libname,jobject mainActivity,jobject bundle)
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
	JNIEXPORT jstring Java_com_mcal_pesdk_nativeapi_NativeUtils_nativeDemangle(JNIEnv*env,jobject thiz,jstring str)
	{
		char const* symbol_name = toString(env,str).c_str();
		if(symbol_name)
		{
			char const* ret = abi::__cxa_demangle(symbol_name,0,0,0);
			return env->NewStringUTF(ret);
		}
		return env->NewStringUTF("");
	}
	JNIEXPORT void Java_com_mcal_pesdk_nmod_NModLoader_nativeCallOnDexLoaded(JNIEnv*env,jobject thiz,jstring libname,jobject dexClassLoader)
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
	
	void* image=dlopen("libminecraftpe.so",RTLD_LAZY);
	
	MSHookFunction((void*)(std::string (*)(std::string const&,std::vector<std::string> const&))&I18n::get,(void*)&getI18nl,(void**)&getI18nl_);
	MSHookFunction((void*)(std::string (*)(std::string const&))&I18n::get,(void*)&getI18n,(void**)&getI18n_);
	MSHookFunction((void*)&ScreenChooser::setStartMenuScreen,(void*)&setStartMenuScreen,(void**)&setStartMenuScreen_);

	dlclose(image);
	return JNI_VERSION_1_6;
}
