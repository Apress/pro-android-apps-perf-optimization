unsigned int avg8 (unsigned int a, unsigned int b)
{
  return
    ((a >> 1) & 0x7F7F7F7F) +
    ((b >> 1) & 0x7F7F7F7F) +
    (a & b & 0x01010101);
}

unsigned int avg8_faster (unsigned int a, unsigned int b)
{
  return
    (((a ^ b) & 0xFEFEFEFE) >> 1) +
    (a & b);
}

unsigned int avg8_fastest (unsigned int a, unsigned int b)
{
#if defined(__ARM_ARCH_7A__)
  unsigned int avg;
  
  asm("uhadd8 %[average], %[val1], %[val2]"
      : [average] "=r" (avg)
      : [val1] "r" (a), [val2] "r" (b));
  
  return avg;
#else
  return avg8_faster(a, b);
#endif
}

int avg8_tests(int loops)
{
  int x = 0;
  
  while (loops--) {
    x += avg8(loops * 7919, loops * 3313);
  }
  
  return x;
}

int avg8_asm_tests(int loops)
{
  int x = 0;
  
#if defined(__ARM_ARCH_7A__)
  while (loops--) {
    x += avg8_asm(loops * 7919, loops * 3313);
  }
#endif
  
  return x;
}
