#include <jni.h>
#include <string>
#include "libcrocou.h"

char* jstrToChar(jstring str, JNIEnv* env) {
    const char* strI = env->GetStringUTFChars(str, nullptr);
    char* rtstr = new char[strlen(strI) + 1];
    strcpy(rtstr, strI);
    return rtstr;
}

jstring charToJstring(const char* str, JNIEnv *env) {
    std::string s = str;
    return env->NewStringUTF(s.c_str());
}
extern "C" JNIEXPORT jstring JNICALL
Java_com_chaosthechaotic_lamb_CrocPollFgServ_recvCroc(
        JNIEnv* env,
        jobject /* this */,
        jstring code) {
    const char* codeStr = env->GetStringUTFChars(code, nullptr);
    char* result = RecvText(const_cast<char*>(codeStr));
    env->ReleaseStringUTFChars(code, codeStr);

    if (result == nullptr) {
        return env->NewStringUTF("");
    }

    jstring javaResult = env->NewStringUTF(result);
    free(result);
    return javaResult;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_chaosthechaotic_lamb_CrocPollFgServ_sndCroc(
        JNIEnv* env,
        jobject /* this */,
        jstring msg,
        jstring code) {
    const char* msgStr = env->GetStringUTFChars(msg, nullptr);
    const char* codeStr = env->GetStringUTFChars(code, nullptr);

    char* result = SendText(const_cast<char*>(msgStr), const_cast<char*>(codeStr));

    env->ReleaseStringUTFChars(msg, msgStr);
    env->ReleaseStringUTFChars(code, codeStr);

    if (result == nullptr) {
        return env->NewStringUTF("");
    }

    jstring javaResult = env->NewStringUTF(result);
    free(result);
    return javaResult;
}