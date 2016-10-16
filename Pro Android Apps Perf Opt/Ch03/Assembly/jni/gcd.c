unsigned int gcd (unsigned int a, unsigned int b)
{
  while (a != b) {
    if (a > b) {
      a -= b;
    } else {
      b -= a;
    }
  }
  
  return a;
}

unsigned int gcd2 (unsigned int a, unsigned int b)
{
  if (a != b) {
    while (1) {
      if (a > b) {
	a -= b;
      } else {
	b -= a;
      }
      if (a == b) break;
    }
  }
  
  return a;
}

int gcd_tests(int loops)
{
  int x = 0;
  
  while (loops--) {
    x += gcd(loops * 7919, loops * 3313);
  }
  
  return x;
}

int gcd_asm_tests(int loops)
{
  int x = 0;
  
#if defined(__ARM_EABI__)
  while (loops--) {
    x += gcd_asm(loops * 7919, loops * 3313);
  }
#endif
  
  return x;
}
