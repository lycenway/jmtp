#include <objbase.h>
#include <atlbase.h>
#include <jni.h>

#include "jwpd.h"

jobject ConvertGuidToJava(JNIEnv* env, GUID guid)
{
	jclass cls;
	jmethodID mid;
	jshortArray jData4;

	cls = env->FindClass("be/derycke/pieter/com/Guid");
	mid = env->GetMethodID(cls, "<init>", "(JII[S)V");
	
	jData4 = env->NewShortArray(8);
	for(int i = 0; i < 8; i++) {
		jshort jsBuffer = guid.Data4[i];
		env->SetShortArrayRegion(jData4, i, 1, &jsBuffer);
	}

	return env->NewObject(cls, mid, (jlong)guid.Data1, (jint)guid.Data2, (jint)guid.Data3, jData4);
}

jobject ConvertPropertyKeyToJava(JNIEnv* env, PROPERTYKEY key)
{
	jclass cls;
	jmethodID mid;

	cls = env->FindClass("be/derycke/pieter/wpd/PropertyKey");
	mid = env->GetMethodID(cls, "<init>", "(Lbe/derycke/pieter/com/Guid;J)V");

	return env->NewObject(cls, mid, ConvertGuidToJava(env, key.fmtid), (jlong)key.pid);
}

PROPVARIANT ConvertJavaToPropVariant(JNIEnv* env, jobject jobjPropVariant)
{
	//variabelen
	jclass cls;
	jmethodID mid;
	PROPVARIANT pv;
	jstring jsValue;
	LPWSTR wszBuffer;
	LPWSTR wszValue;


	//methode implementatie
	PropVariantInit(&pv);
	cls = env->FindClass("be/derycke/pieter/wpd/PropVariant");
	
	mid = env->GetMethodID(cls, "getVt", "()I");
	pv.vt = env->CallIntMethod(jobjPropVariant, mid);

	mid = env->GetMethodID(cls, "getPwszVal", "()Ljava/lang/String;");
	jsValue = (jstring)env->CallObjectMethod(jobjPropVariant, mid);
	wszBuffer = (WCHAR*)env->GetStringChars(jsValue, NULL);
	wszValue = (WCHAR*)CoTaskMemAlloc(sizeof(WCHAR) * (wcslen(wszBuffer) + 1));
	wcscpy_s(wszValue, wcslen(wszBuffer) + 1, wszBuffer);
	env->ReleaseStringChars(jsValue, (jchar*)wszBuffer);
	pv.pwszVal = wszValue;
	
	return pv;
}

jobject ConvertPropVariantToJava(JNIEnv* env, PROPVARIANT pv)
{
	//variabelen
	jclass cls;
	jmethodID mid;
	jstring jsValue;


	//methode implementatie
	cls = env->FindClass("be/derycke/pieter/wpd/PropVariant");
	mid = env->GetMethodID(cls, "<init>", "(ILjava/lang/String;)V");
	jsValue = env->NewString((jchar*)pv.pwszVal, wcslen(pv.pwszVal));
	return env->NewObject(cls, mid, pv.vt, jsValue);
}

inline jlong ConvertComReferenceToPointer(JNIEnv* env, jobject jobjReference)
{
	jmethodID mid;

	mid = env->GetMethodID(env->FindClass("be/derycke/pieter/com/COMReference"), "getMemoryAddress", "()J");
	return env->CallLongMethod(jobjReference, mid);
}

//De COMReference opvragen van een COMReferencable object
jobject RetrieveCOMReferenceFromCOMReferenceable(JNIEnv* env, jobject jobjCOMReferenceable)
{
	jmethodID mid = env->GetMethodID(env->FindClass("be/derycke/pieter/com/COMReferenceable"), 
		"getReference", "()Lbe/derycke/pieter/com/COMReference;");
	return env->CallObjectMethod(jobjCOMReferenceable, mid);
}

jlong GetComReference(JNIEnv* env, jobject obj, const char* fieldName)
{
	jclass cls;
	jobject reference;
	jmethodID mid;

	cls = env->GetObjectClass(obj);
	reference = env->GetObjectField(obj, env->GetFieldID(cls, fieldName, "Lbe/derycke/pieter/com/COMReference;"));
	return ConvertComReferenceToPointer(env, reference);
}