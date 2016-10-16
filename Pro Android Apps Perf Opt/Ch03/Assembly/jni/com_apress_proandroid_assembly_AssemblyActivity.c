#include "com_apress_proandroid_assembly_AssemblyActivity.h"

jint JNICALL Java_com_apress_proandroid_assembly_AssemblyActivity_getGCD
(JNIEnv *env, jclass clazz, jint a, jint b)
{
  return gcd(a, b);
}

jint JNICALL Java_com_apress_proandroid_assembly_AssemblyActivity_getGCDAsm
(JNIEnv *env, jclass clazz, jint a, jint b)
{
#if defined(__ARM_EABI__)
  return gcd_asm(a, b);
#else
  return gcd(a, b);
#endif
}

jint JNICALL Java_com_apress_proandroid_assembly_AssemblyActivity_convertARGB8888
  (JNIEnv *env, jclass clazz, jint color)
{
  return argb8888_to_rgb565(color);
}

jint JNICALL Java_com_apress_proandroid_assembly_AssemblyActivity_convertARGB8888Asm
  (JNIEnv *env, jclass clazz, jint color)
{
#if defined(__ARM_ARCH_7A__)
  return argb8888_to_rgb565_asm(color);
#else
  return argb8888_to_rgb565(color);
#endif
}

jint JNICALL Java_com_apress_proandroid_assembly_AssemblyActivity_computeAverage8
  (JNIEnv *env, jclass clazz, jint a, jint b)
{
  return avg8(a, b);
}

jint JNICALL Java_com_apress_proandroid_assembly_AssemblyActivity_computeAverage8Faster
  (JNIEnv *env, jclass clazz, jint a, jint b)
{
  return avg8_faster(a, b);
}

jint JNICALL Java_com_apress_proandroid_assembly_AssemblyActivity_computeAverage8Fastest
  (JNIEnv *env, jclass clazz, jint a, jint b)
{
  return avg8_fastest(a, b);
}

jint JNICALL Java_com_apress_proandroid_assembly_AssemblyActivity_computeAverage8Asm
  (JNIEnv *env, jclass clazz, jint a, jint b)
{
#if defined(__ARM_ARCH_7A__)
  return avg8_asm(a, b);
#else
  return avg8_fastest(a, b); // to make it compile and return something correct
#endif
}

void JNICALL Java_com_apress_proandroid_assembly_AssemblyActivity_runNativeGCDTests
(JNIEnv *env, jclass clazz, int loops)
{
  gcd_tests(loops);
}

void JNICALL Java_com_apress_proandroid_assembly_AssemblyActivity_runNativeGCDAsmTests
(JNIEnv *env, jclass clazz, int loops)
{
  gcd_asm_tests(loops);
}

void JNICALL Java_com_apress_proandroid_assembly_AssemblyActivity_runNativeARGB8888Tests
(JNIEnv *env, jclass clazz, int loops)
{
  argb8888_tests(loops);
}

void JNICALL Java_com_apress_proandroid_assembly_AssemblyActivity_runNativeARGB8888AsmTests
(JNIEnv *env, jclass clazz, int loops)
{
  argb8888_asm_tests(loops);
}

void JNICALL Java_com_apress_proandroid_assembly_AssemblyActivity_runNativeAverage8Tests
(JNIEnv *env, jclass clazz, int loops)
{
  avg8_tests(loops);
}

void JNICALL Java_com_apress_proandroid_assembly_AssemblyActivity_runNativeAverage8AsmTests
(JNIEnv *env, jclass clazz, int loops)
{
  avg8_asm_tests(loops);
}
