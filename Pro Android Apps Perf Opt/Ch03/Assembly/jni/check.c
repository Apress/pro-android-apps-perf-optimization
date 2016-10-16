#include "cpu-features.h"

static int has_features(uint64_t features,
			uint64_t mask)
{
  return ((features & mask) == mask);
}

static void (*my_function)(int* dst, const int* src, int size);

extern void neon_function(int* dst, const int* src, int size);

extern void default_function(int* dst, const int* src, int size);

int init () {
  AndroidCpuFamily cpu = android_getCpuFamily();
  
  uint64_t features = android_getCpuFeatures();
  
  int count = android_getCpuCount();
  
  if (cpu == ANDROID_CPU_FAMILY_ARM) {
    if (has_features(features,
		     ANDROID_CPU_ARM_FEATURE_ARMv7|
		     ANDROID_CPU_ARM_FEATURE_NEON))
      {
	// for example set some function pointers to use NEON-optimized functions
	my_function = neon_function;
      }
    else
      {
	// use default functions here
	my_function = default_function; // no NEON instructions
      }
  }
}

void call_function(int* dst, const int* src, int size)
{
  my_function(dst, src, size);
}
