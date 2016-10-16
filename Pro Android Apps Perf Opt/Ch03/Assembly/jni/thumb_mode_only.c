
unsigned int wont_compile_in_arm_mode()
{
  unsigned int value;
  
  /*
    The following statement would not compile in ARM mode because 0x01010101
    is not a valid operand in ARM mode.
    It would be valid in Thumb mode for the armeabi-v7a ABI.
  */
  asm("mov %[value], #0x01010101"
      : [value] "=r" (value));

  return value;
}
