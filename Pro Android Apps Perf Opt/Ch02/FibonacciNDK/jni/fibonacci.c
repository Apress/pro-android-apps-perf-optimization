#include "fibonacci.h"

uint64_t recursive (unsigned int n)
{
    if (n > 1) return recursive(n-2) + recursive(n-1);
    return (uint64_t)n;
}

uint64_t iterativeFaster (unsigned int n) {
  if (n > 1) {
    uint64_t a, b = 1;
    n--;
    a = n & 1;
    n /= 2;
    while (n-- > 0) {
      a += b;
      b += a;
    }
    return b;
  }
  return (uint64_t)n;
}
