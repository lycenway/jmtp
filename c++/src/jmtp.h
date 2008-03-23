#pragma once

#include <objbase.h>
#include <atlbase.h>
#include <jni.h>

inline GUID ConvertToGUID(JNIEnv* env, jobject jGuid)
{
	GUID guid;
	jmethodID mid;
	jclass cls;

	cls = env->GetObjectClass(jGuid);

	mid	= env->GetMethodID(cls, "getData1", "()J");
	guid.Data1 = env->CallLongMethod(jGuid, mid);

	mid = env->GetMethodID(cls, "getData2", "()I");
	guid.Data2 = env->CallIntMethod(jGuid, mid);

	mid = env->GetMethodID(cls, "getData3", "()I");
	guid.Data3 = env->CallIntMethod(jGuid, mid);

	mid = env->GetMethodID(cls, "getData4", "()[S");
	jshort* data4 = env->GetShortArrayElements((jshortArray)env->CallObjectMethod(jGuid, mid), NULL);
	for(int i = 0; i < 8; i++)
		guid.Data4[i] = data4[i];

	return guid;
}

inline PROPERTYKEY ConvertToPROPERTYKEY(JNIEnv* env, jobject jKey)
{
	PROPERTYKEY key;
	jmethodID mid;
	jclass cls;
	jobject jGuid;

	cls = env->FindClass("be/derycke/pieter/wpd/PropertyKey");

	mid = env->GetMethodID(cls, "getPid", "()J");
	key.pid = env->CallLongMethod(jKey, mid);

	mid = env->GetMethodID(cls, "getFmtid", "()Lbe/derycke/pieter/com/Guid;");
	jGuid = env->CallObjectMethod(jKey, mid);
	key.fmtid = ConvertToGUID(env, jGuid);

	return key;
}

jobject ConvertGuidToJava(JNIEnv* env, GUID guid);

jobject ConvertPropertyKeyToJava(JNIEnv* env, PROPERTYKEY key);

PROPVARIANT ConvertJavaToPropVariant(JNIEnv* env, jobject jobjPropVariant);

jobject ConvertPropVariantToJava(JNIEnv* env, PROPVARIANT pv);

jobject RetrieveCOMReferenceFromCOMReferenceable(JNIEnv* env, jobject jobjCOMReferenceable);

jlong GetComReference(JNIEnv* env, jobject obj, const char* fieldName);

inline jlong ConvertComReferenceToPointer(JNIEnv* env, jobject jobjReference);

void ThrowCOMException(JNIEnv* env, LPWSTR message, HRESULT hr);