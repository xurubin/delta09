
#include "jni.h"
#define JNIEXPORT __declspec(dllexport)
#include "jftd2xx.h"

#include <windows.h>
#include "ftd2xx.h"

#ifndef INVALID_HANDLE_VALUE
#define INVALID_HANDLE_VALUE (-1)
#endif


/* GLoabl variables */
static JavaVM *javavm;
static jclass JFTD2XXCls; // JFTD2XX class object reference
static jfieldID FT_Handle; // FT_Handle field object reference


/** Get object handle */
static jint
get_handle(JNIEnv *env, jobject obj) {
	return (*env)->GetIntField(env, obj, FT_Handle);
}

/** Set object handle */
static void
set_handle(JNIEnv *env, jobject obj, jint val) {
	(*env)->SetIntField(env, obj, FT_Handle, val);
}


/** Throw exception */
static void
io_exception(JNIEnv *env, const char *msg) {
	// jclass exc = (*env)->FindClass(env, "java/lang/RuntimeException");
	jclass exc = (*env)->FindClass(env, "java/io/IOException");
	if (exc == 0) return;
	(*env)->ThrowNew(env, exc, msg);
	(*env)->DeleteLocalRef(env, exc);
}

/** Initialize JFTD2XX driver objects */
jint JNICALL
JNI_OnLoad(JavaVM *jvm, void *reserved) {
	JNIEnv *env;
	jclass cls;
	
	if ((*jvm)->GetEnv(jvm, (void **)&env, JNI_VERSION_1_2)) {
		return JNI_ERR; // not supported
	}

	// cls = (*env)->GetObjectClass(env, obj);
	cls = (*env)->FindClass(env, "JFTD2XX");
	if (cls == 0) return JNI_ERR;
	JFTD2XXCls = (*env)->NewWeakGlobalRef(env, cls); // weak allows library unloading
	(*env)->DeleteLocalRef(env, cls);
	if (JFTD2XXCls == 0) return JNI_ERR;

	FT_Handle = (*env)->GetFieldID(env, JFTD2XXCls, "handle", "I");
	if (FT_Handle == 0) return JNI_ERR;


	javavm = jvm; // initialize jvm pointer

	return JNI_VERSION_1_2;
}

/** Finalize JFTD2XX driver */
void JNICALL
JNI_OnUnLoad(JavaVM *jvm, void *reserved) {
	JNIEnv *env;

	if ((*jvm)->GetEnv(jvm, (void **)&env, JNI_VERSION_1_2)) {
		return; // not supported
	}

	(*env)->DeleteWeakGlobalRef(env, JFTD2XXCls);

//	fprintf(stderr,  "Bye!\n");
//	fflush(stderr);
}

void JNICALL
Java_JFTD2XX_open(JNIEnv *env, jobject obj, jint dn) {
	//printf("open\n");
	jint hnd = get_handle(env, obj);

	if (hnd != (jint)INVALID_HANDLE_VALUE) // previously initialized!
		io_exception(env, "device already opened");
	else {
		FT_HANDLE h;
		FT_STATUS st = FT_Open(dn, &h);

		if (FT_SUCCESS(st)) set_handle(env, obj, (jint)h);
		else io_exception(env, "open device failed");
	}
}


void JNICALL
Java_JFTD2XX_close(JNIEnv *env, jobject obj) {
	jint hnd = get_handle(env, obj);

	if (hnd != (jint)INVALID_HANDLE_VALUE) {
		FT_STATUS st = FT_Close((FT_HANDLE)hnd);
		if (!FT_SUCCESS(st)) io_exception(env, "close device failed");
		else set_handle(env, obj, (jint)INVALID_HANDLE_VALUE);
	}
}


jint JNICALL
Java_JFTD2XX_read(JNIEnv *env, jobject obj, jbyteArray arr, jint off, jint len) {
	FT_STATUS st;
	volatile DWORD ret;
	jint hnd = get_handle(env, obj);
	int alen = (*env)->GetArrayLength(env, arr);
	jbyte *buf;

	if (arr == 0) {
		jclass exc = (*env)->FindClass(env, "java/lang/NullPointerException");
		if (exc != 0) (*env)->ThrowNew(env, exc, NULL);
		(*env)->DeleteLocalRef(env, exc);
		return 0;
	} else if ((off < 0) || (off > alen) || (len < 0)
		|| ((off + len) > alen) || ((off + len) < 0)) {
		jclass exc = (*env)->FindClass(env, "java/lang/IndexOutOfBoundsException");
		if (exc != 0) (*env)->ThrowNew(env, exc, NULL);
		(*env)->DeleteLocalRef(env, exc);
		return 0;
	} else if (len == 0) return 0;

	buf = (*env)->GetByteArrayElements(env, arr, 0);

	if (!FT_SUCCESS(st = FT_Read((FT_HANDLE)hnd, (LPVOID)(buf+off), len, (LPDWORD)&ret)))
		io_exception(env, "read device failed");

	(*env)->ReleaseByteArrayElements(env, arr, buf, 0);
	return (jint)ret;
}

jint JNICALL
Java_JFTD2XX_write(JNIEnv *env, jobject obj, jbyteArray arr, jint off, jint len) {
	FT_STATUS st;
	volatile DWORD ret;
	jint hnd = get_handle(env, obj);
	int alen = (*env)->GetArrayLength(env, arr);
	jbyte *buf;

	if (arr == 0) {
		jclass exc = (*env)->FindClass(env, "java/lang/NullPointerException");
		if (exc != 0) (*env)->ThrowNew(env, exc, NULL);
		(*env)->DeleteLocalRef(env, exc);
		return 0;
	} else if ((off < 0) || (off > alen) || (len < 0)
		|| ((off + len) > alen) || ((off + len) < 0)) {
		jclass exc = (*env)->FindClass(env, "java/lang/IndexOutOfBoundsException");
		if (exc != 0) (*env)->ThrowNew(env, exc, NULL);
		(*env)->DeleteLocalRef(env, exc);
		return 0;
	} else if (len == 0) return 0;

 	buf = (*env)->GetByteArrayElements(env, arr, 0);

	if (!FT_SUCCESS(st = FT_Write((FT_HANDLE)hnd, (LPVOID)(buf+off), len, (LPDWORD)&ret)))
		io_exception(env, "write device failed");

	(*env)->ReleaseByteArrayElements(env, arr, buf, 0);
	return (jint)ret;
}

jint JNICALL
Java_JFTD2XX_getQueueStatus(JNIEnv *env, jobject obj) {
	FT_STATUS st;
	jint hnd = get_handle(env, obj);
	DWORD r;

	if (!FT_SUCCESS(st = FT_GetQueueStatus((FT_HANDLE)hnd, &r)))
		io_exception(env, "GetQueueStatus failed");

	return (jint)r;
}

void JNICALL
Java_JFTD2XX_setLatencyTimer(JNIEnv *env, jobject obj, jint tmr) {
	FT_STATUS st;
	jint hnd = get_handle(env, obj);

	if (!FT_SUCCESS(st = FT_SetLatencyTimer((FT_HANDLE)hnd, (UCHAR)tmr)))
		io_exception(env, "setLatencyTimer failed");
}
