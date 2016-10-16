typedef int v4int __attribute__ ((vector_size (16)));

void add_buffers_vectorized (int* dst, const int* src, int size)
{
  v4int* dstv4int = (v4int*) dst;
  const v4int* srcv4int = (v4int*) src;
  int i;

  for (i = 0; i < size/4; i++) {
    *dstv4int++ += *srcv4int++;
  }
  
  // leftovers
  if (size & 0x3) {
    dst = (int*) dstv4int;
    src = (int*) srcv4int;
    
    switch (size & 0x3) {
    case 3: *dst++ += *src++;
    case 2: *dst++ += *src++;
    case 1:
    default:  *dst += *src;
    }
  }
}

void add_buffers (int* dst, const int* src, int size)
{
  while (size--) {
    *dst++ += *src++;
  }
}

void add_buffers_unrolled (int* dst, const int* src, int size)
{
  int i;

  for (i = 0; i < size/4; i++) {
    *dst++ += *src++;
    *dst++ += *src++;
    *dst++ += *src++;
    *dst++ += *src++;
    // GCC not really good at that... No LDM/STM generated
  }
  
  // leftovers
  if (size & 0x3) {
    switch (size & 0x3) {
    case 3: *dst++ += *src++;
    case 2: *dst++ += *src++;
    case 1:
    default:  *dst += *src;
    }
  }
}

void add_buffers_unrolled_prefetch (int* dst, const int* src, int size)
{
  int i;

  for (i = 0; i < size/8; i++) {
    __builtin_prefetch(dst + 8, 1, 0);
    __builtin_prefetch(src + 8, 0, 0);
    
    *dst++ += *src++;
    *dst++ += *src++;
    *dst++ += *src++;
    *dst++ += *src++;
    *dst++ += *src++;
    *dst++ += *src++;
    *dst++ += *src++;
    *dst++ += *src++;
  }
  
  // leftovers
  for (i = 0; i < (size & 0x7); i++) {
    *dst++ += *src++;
  }
}
