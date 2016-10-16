#include "com_apress_proandroid_ndk_Fibonacci.h"
#include "com_apress_proandroid_ndk_FibonacciNative.h"
#include "fibonacci.h"

/*
 * Class:     com_apress_proandroid_ndk_Fibonacci
 * Method:    recursiveNative
 * Signature: (I)J
 */
jlong JNICALL Java_com_apress_proandroid_ndk_Fibonacci_recursiveNative
  (JNIEnv *env, jclass clazz, jint n)
{
  return recursive(n);
}

/*
 * Class:     com_apress_proandroid_ndk_FibonacciNative
 * Method:    recursive
 * Signature: (I)J
 */
jlong JNICALL Java_com_apress_proandroid_ndk_FibonacciNative_recursive
  (JNIEnv *env, jclass clazz, jint n)
{
  return recursive(n);
}
/*
 * Class:     com_apress_proandroid_ndk_FibonacciNative
 * Method:    iterativeFaster
 * Signature: (I)J
 */
jlong JNICALL Java_com_apress_proandroid_ndk_FibonacciNative_iterativeFaster
  (JNIEnv *env, jclass clazz, jint n)
{
  return iterativeFaster(n);
}
