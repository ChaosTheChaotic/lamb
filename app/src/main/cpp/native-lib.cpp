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
        jstring code
        ) {
    return charToJstring(RecvText(jstrToChar(code, env)), env);
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_chaosthechaotic_lamb_CrocPollFgServ_sndCroc(
        JNIEnv* env,
        jobject /* this */,
        jstring msg,
        jstring code) {
    return charToJstring(SendText(jstrToChar(msg, env), jstrToChar(code, env)), env);
}