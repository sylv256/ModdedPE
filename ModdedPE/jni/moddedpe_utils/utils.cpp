#include "Substrate.h"
#include <jni.h>
#include <vector>
#include <string>
#include <fstream>
#include <cxxabi.h>

#include "mcpe/client/ClientInputCallbacks.h"
#include "mcpe/client/ClientInstance.h"
#include "mcpe/client/MinecraftGame.h"
#include "mcpe/client/gui/screen/ScreenChooser.h"
#include "mcpe/client/renderer/BlockTessellator.h"
#include "mcpe/block/BlockGraphics.h"
#include "mcpe/block/Block.h"
#include "mcpe/item/Item.h"
#include "mcpe/item/ItemInstance.h"
#include "mcpe/util/BlockPos.h"
#include "mcpe/util/Vec3.h"
#include "mcpe/level/Level.h"
#include "mcpe/entity/player/LocalPlayer.h"
#include "mcpe/client/settings/Options.h"
#include "mcpe/client/resources/Localization.h"
#include "NeighborUtil.h"

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

std::vector<LanguageBean> languageBeans;

bool started=false;
bool redstoneDot=false;
bool hideDebugText=false;
bool autoSaveLevel=false;
bool selectAllInLeft=false;
bool disableTextureIsoTropic=false;
JavaVM* jvm;

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

inline bool shouldTessellateRedstoneDot(BlockSource&region,BlockPos const&pos)
{
	if(!redstoneDot)
		return false;
	
	NeighborUtil neighbor(region);
	
	if(neighbor.isShouldRedstoneWireBlockConnectionBlockAround(pos))
		return false;
	
	if(!neighbor.isFullBlockAt({pos.x,pos.y+1,pos.z}))
		if(neighbor.isRedStoneWireBlockAround({pos.x,pos.y+1,pos.z}))
			return false;
	
	if(!neighbor.isFullBlockAt({pos.x+1,pos.y,pos.z}))
	{
		if(neighbor.isRedStoneWireBlockAt({pos.x+1,pos.y-1,pos.z}))
			return false;
	}
	if(!neighbor.isFullBlockAt({pos.x-1,pos.y,pos.z}))
	{
		if(neighbor.isRedStoneWireBlockAt({pos.x-1,pos.y-1,pos.z}))
			return false;
	}
	if(!neighbor.isFullBlockAt({pos.x,pos.y,pos.z-1}))
	{
		if(neighbor.isRedStoneWireBlockAt({pos.x,pos.y-1,pos.z-1}))
			return false;
	}
	if(!neighbor.isFullBlockAt({pos.x,pos.y,pos.z+1}))
	{
		if(neighbor.isRedStoneWireBlockAt({pos.x,pos.y-1,pos.z+1}))
			return false;
	}
	return true;
}

//REDSTONE DOT
bool (*tessellateInWorld_)(BlockTessellator*tessellator,Block const&block,BlockPos const&pos,uchar aux,bool wtf);
bool tessellateInWorld(BlockTessellator*tessellator,Block const&block,BlockPos const&pos,uchar aux,bool wtf)
{
	if(&block==Block::mRedStoneDust)
	{
		if(shouldTessellateRedstoneDot(tessellator->getRegion(),pos))
			tessellator->_setShapeAndTessellate(Vec3(0.3125,0.001,0.3125),Vec3(0.6875,0.003,0.6875),block,pos,aux);
		else
			return tessellateInWorld_(tessellator,block,pos,aux,wtf);
		return true;
	}
	return tessellateInWorld_(tessellator,block,pos,aux,wtf);
}

void (*renderFaceDown_)(BlockTessellator*,Block const&, Vec3 const&, TextureUVCoordinateSet const&);
void renderFaceDown(BlockTessellator*self,Block const&b, Vec3 const&pos, TextureUVCoordinateSet const&tex)
{
	if(&b==Block::mRedStoneDust)
	{
		if(shouldTessellateRedstoneDot(self->getRegion(),pos))
			return;
		else
			return renderFaceDown_(self,b,pos,tex);
	}
	else
		return renderFaceDown_(self,b,pos,tex);
}

void (*setStartMenuScreen_)(void*);
void setStartMenuScreen(void*self)
{
	setStartMenuScreen_(self);
	started=true;
}

void (*levelTick_)(Level*);
void levelTick(Level*self)
{
	levelTick_(self);
	
	if(autoSaveLevel)
	{
		static unsigned char timer;
		if(!((++timer)%20))
		{
			self->saveGameData();
			self->savePlayers();
			self->saveBiomeData();
			self->saveLevelData();
			self->_saveAllMapData();
			self->saveDirtyChunks();
			self->_saveSomeChunks();
			self->_saveDimensionStructures();
			self->saveAutonomousEntities();
		}
	}
}

extern "C"
{
	JNIEXPORT jboolean Java_com_mcal_ModdedPE_nativeapi_Utils_nativeIsGameStarted(JNIEnv*env,jobject thiz)
	{
		return started;
	}
	
	JNIEXPORT void Java_com_mcal_ModdedPE_nativeapi_Utils_nativeSetRedstoneDot(JNIEnv*env,jobject thiz,jboolean z)
	{
		redstoneDot=z;
	}
	JNIEXPORT void Java_com_mcal_ModdedPE_nativeapi_Utils_nativeSetHideDebugText(JNIEnv*env,jobject thiz,jboolean z)
	{
		hideDebugText=z;
	}
	JNIEXPORT void Java_com_mcal_ModdedPE_nativeapi_Utils_nativeSetAutoSaveLevel(JNIEnv*env,jobject thiz,jboolean z)
	{
		autoSaveLevel=z;
	}
	JNIEXPORT void Java_com_mcal_ModdedPE_nativeapi_Utils_nativeSetSelectAllInLeft(JNIEnv*env,jobject thiz,jboolean z)
	{
		selectAllInLeft=z;
	}
	JNIEXPORT void Java_com_mcal_ModdedPE_nativeapi_Utils_nativeSetDisableTextureIsotropic(JNIEnv*env,jobject thiz,jboolean z)
	{
		disableTextureIsoTropic=z;
	}
	JNIEXPORT void Java_com_mcal_ModdedPE_nativeapi_Utils_nativeSetDataDirectory(JNIEnv*env,jobject thiz,jstring directory)
	{
		void* image=dlopen("libminecraftpe.so",RTLD_LAZY);
	
		std::string& android_app_data_path = *((std::string*)dlsym(image,"_ZN19AppPlatform_android20ANDROID_APPDATA_PATHE"));
		android_app_data_path=toString(env,directory);
		
		dlclose(image);
	}
	JNIEXPORT void Java_com_mcal_ModdedPE_nmod_NModLoader_nativeAppendTranslation(JNIEnv*env,jobject thiz,jstring name,jstring translation)
	{
		std::string nameN=toString(env,name);
		std::string translationN=toString(env,translation);
		
		LanguageBean bean(nameN,translationN);
		languageBeans.emplace_back(bean);
	}
	JNIEXPORT void Java_com_mcal_ModdedPE_nmod_NModLoader_nativeCallOnActivityFinish(JNIEnv*env,jobject thiz,jstring libname,jobject mainActivity)
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
	JNIEXPORT void Java_com_mcal_ModdedPE_nmod_NModLoader_nativeCallOnLoad(JNIEnv*env,jobject thiz,jstring libname,jstring mcVer,jstring moddedpeVer)
	{
		void* image=dlopen(toString(env,libname).c_str(),RTLD_LAZY);
		void (*NMod_onLoad)(JavaVM*jvm,JNIEnv* env,std::string const& mcVersionName,std::string const& moddedpeVersionName)=
		(void (*)(JavaVM*jvm,JNIEnv* env,std::string const& mcVersionName,std::string const& moddedpeVersionName)) dlsym(image,"NMod_onLoad");
		if(NMod_onLoad)
		{
			NMod_onLoad(jvm,env,toString(env,mcVer),toString(env,moddedpeVer));
		}
		dlclose(image);
	}
	JNIEXPORT void Java_com_mcal_ModdedPE_nmod_NModLoader_nativeCallOnActivityCreate(JNIEnv*env,jobject thiz,jstring libname,jobject mainActivity,jobject bundle)
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
	JNIEXPORT jstring Java_com_mcal_ModdedPE_nativeapi_Utils_nativeDemangle(JNIEnv*env,jobject thiz,jstring str)
	{
		char const* symbol_name = toString(env,str).c_str();
		if(symbol_name)
		{
			char const* ret = abi::__cxa_demangle(symbol_name,0,0,0);
			return env->NewStringUTF(ret);
		}
		return env->NewStringUTF("");
	}
	
}

void (*handleRenderDebugButtonPress_)(ClientInputCallbacks*,ClientInstance&);
void handleRenderDebugButtonPress(ClientInputCallbacks*self,ClientInstance&client)
{
	if(!hideDebugText)
		handleRenderDebugButtonPress_(self,client);
}

void (*loadLocalization_)(Localization *self, const std::string &languageName);
void loadLocalization(Localization *self, const std::string &languageName)
{
	loadLocalization_(self,languageName);
	
	for(LanguageBean const& bean:languageBeans)
	{
		if(bean.name==languageName)
			self->appendTranslations(bean.translation);
	}
}

bool (*allowOffhand_)(ItemInstance*);
bool allowOffhand(ItemInstance* self)
{
	if(selectAllInLeft)
		return true;
	return allowOffhand_(self);
}

bool (*isTextureIsotropic_)(BlockGraphics*,signed char);
bool isTextureIsotropic(BlockGraphics* self,signed char side)
{
	if(disableTextureIsoTropic)
		return false;
	return isTextureIsotropic_(self,side);
}

JNIEXPORT jint JNI_OnLoad(JavaVM*vm,void*)
{
	jvm=vm;
	
	MSHookFunction((void*)((void(BlockTessellator::*)(Block const&,BlockPos const&,unsigned char,bool))&BlockTessellator::tessellateInWorld),(void*)&tessellateInWorld,(void**)&tessellateInWorld_);
	MSHookFunction((void*)&Level::tick,(void*)&levelTick,(void**)&levelTick_);
	MSHookFunction((void*)&Localization::_load,(void*)&loadLocalization,(void**)&loadLocalization_);
	MSHookFunction((void*)&BlockTessellator::renderFaceDown,(void*)&renderFaceDown,(void**)&renderFaceDown_);
	MSHookFunction((void*)&ScreenChooser::setStartMenuScreen,(void*)&setStartMenuScreen,(void**)&setStartMenuScreen_);
	MSHookFunction((void*)&ClientInputCallbacks::handleRenderDebugButtonPress,(void*)&handleRenderDebugButtonPress,(void**)&handleRenderDebugButtonPress_);
	MSHookFunction((void*)&ItemInstance::isOffhandItem,(void*)&allowOffhand,(void**)&allowOffhand_);
	MSHookFunction((void*)&BlockGraphics::isTextureIsotropic,(void*)&isTextureIsotropic,(void**)&isTextureIsotropic_);
	return JNI_VERSION_1_6;
}
