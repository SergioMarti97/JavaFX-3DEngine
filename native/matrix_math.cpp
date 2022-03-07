#include "matrix_math.h"
#include <iostream>

JNIEXPORT jfloat JNICALL Java_matrix_math_quickInverseSqrt
  (JNIEnv *, jobject thisObject, jfloat number) {

    for (int i = 0; i < 16; i++) {
        std::cout << "Hello " << number << " from C++ !! " << i << std::endl;
    }

    return 0;

  }