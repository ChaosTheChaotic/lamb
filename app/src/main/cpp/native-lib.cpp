#include <jni.h>
#include <string>
#include "libcrocou.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_chaosthechaotic_lamb_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_chaosthechaotic_lamb_CrocPollFgServ_recvCroc(
        JNIEnv* env,
        jobject /* this */,
        jstring code
        ) {
    const char* codeStrI = env->GetStringUTFChars(code, nullptr);
    char* codeStr = new char[strlen(codeStrI) + 1];
    strcpy(codeStr, codeStrI);
    const char* resultI = RecvText(codeStr);
    std::string res = resultI;
    return env->NewStringUTF(res.c_str());
}