#include <objbase.h>

#include "be_derycke_pieter_com_COM.h"
#include "be_derycke_pieter_com_COMReference.h"

#include "jwpd.h"

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved)
{
	CoInitializeEx(NULL, COINIT_MULTITHREADED);
	return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved)
{
	CoUninitialize();
}

JNIEXPORT jobject JNICALL Java_be_derycke_pieter_com_COM_CoCreateInstance
	(JNIEnv* env, jclass guidCls, jobject rclsidObj, jlong pUnkOuter, 
	jlong dwClsContext, jobject riidObj)
{
	HRESULT hr;
	IID rclsid;
	IID riid;
	LPVOID reference;
	jclass cls;
	jmethodID mid;

	rclsid = ConvertToGUID(env, rclsidObj);
	riid = ConvertToGUID(env, riidObj);

	//niet 100% want een dword is een unsigned long een een jlong een signed long
	//maar doet het voor nu wel
	hr = CoCreateInstance(rclsid, (LPUNKNOWN)pUnkOuter, dwClsContext, riid, &reference);

	if(SUCCEEDED(hr))
	{
		//smart reference object aanmaken
		cls = env->FindClass("be/derycke/pieter/com/COMReference");
		mid = env->GetMethodID(cls, "<init>", "(J)V");

		return env->NewObject(cls, mid, reference);
	}
	else {
		ThrowCOMException(env, L"Couldn't create the COM-object", hr);
		return NULL;
	}
}

JNIEXPORT jlong JNICALL Java_be_derycke_pieter_com_COMReference_release
	(JNIEnv* env, jobject obj)
{
	jfieldID fid = env->GetFieldID(env->GetObjectClass(obj), "pIUnknown", "J");
	LPUNKNOWN pointer = (LPUNKNOWN)env->GetLongField(obj, fid);

	__try
	{
		return pointer->Release();
	}
	__except(GetExceptionCode() == EXCEPTION_ACCESS_VIOLATION)
	{
		return 0;
	}
}

JNIEXPORT jlong JNICALL Java_be_derycke_pieter_com_COMReference_addRef
	(JNIEnv* env, jobject obj)
{
	jfieldID fid = env->GetFieldID(env->GetObjectClass(obj), "pIUnknown", "J");
	LPUNKNOWN pointer = (LPUNKNOWN)env->GetLongField(obj, fid);

	__try
	{
	return pointer->AddRef();
	}
	__except(GetExceptionCode() == EXCEPTION_ACCESS_VIOLATION)
	{
		return 0;
	}
}