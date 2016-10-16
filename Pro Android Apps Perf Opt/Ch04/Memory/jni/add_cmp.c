#include <stdint.h>

#define ADD_FUNC(_name, _typeres, _type1, _type2) \
_typeres _name (_type1 v1, _type2 v2) {return v1+v2;}

ADD_FUNC(add_32_32, int32_t, int32_t, int32_t)
ADD_FUNC(add_16_16, int16_t, int16_t, int16_t)
ADD_FUNC(add_16_32, int32_t, int16_t, int32_t)
ADD_FUNC(add_32_64, int64_t, int32_t, int64_t)

ADD_FUNC(add_32_float, int32_t, int32_t, float)
ADD_FUNC(add_float_float, float, float, float)
ADD_FUNC(add_double_double, double, double, double)
ADD_FUNC(add_float_double, double, float, double)

int16_t add_16_16_from_32_32 (int32_t v1, int32_t v2)
{
  return add_16_16(v1, v2);
}

#define CMP_FUNC(_name, _type1, _type2) \
  int _name (_type1 v1, _type2 v2) {return (v1 > v2) ? 1 : 0;}

CMP_FUNC(cmp_32_32, int32_t, int32_t)
CMP_FUNC(cmp_16_16, int16_t, int16_t)
CMP_FUNC(cmp_16_32, int16_t, int32_t)
CMP_FUNC(cmp_32_64, int32_t, int64_t)

CMP_FUNC(cmp_32_float, int32_t, float)
CMP_FUNC(cmp_float_float, float, float)
CMP_FUNC(cmp_double_double, float, double)
CMP_FUNC(cmp_float_double, float, double)
