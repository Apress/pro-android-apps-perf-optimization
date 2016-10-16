unsigned int argb8888_to_rgb565 (unsigned int color)
{
  /*
    input:  aaaaaaaarrrrrrrrggggggggbbbbbbbb
    
    output: 0000000000000000rrrrrggggggbbbbb
  */
  
  return
    /* red   */ ((color >> 8) & 0xF800) |
    /* green */ ((color >> 5) & 0x07E0) |
    /* blue  */ ((color >> 3) & 0x001F);
}

int argb8888_tests(int loops)
{
  int x = 0;
  
  while (loops--) {
    x += argb8888_to_rgb565(loops * 7919);
  }
  
  return x;
}

int argb8888_asm_tests(int loops)
{
  int x = 0;
  
  while (loops--) {
    x += argb8888_to_rgb565(loops * 7919);
  }
  
  return x;
}
