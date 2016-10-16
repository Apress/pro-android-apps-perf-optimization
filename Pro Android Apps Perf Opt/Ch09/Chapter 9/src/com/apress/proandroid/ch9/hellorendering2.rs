#pragma version(1)
#pragma rs java_package_name(com.apress.proandroid.ch9)

#include "rs_graphics.rsh"

// we removed init() as it was empty anyway

float red = 0.0f; // initialized to 1.0f

float green; // purposely not initialized

static float blue; // purposely not initialized, static

const float alpha = 1.0f; // constant

int root() {
    // Clear the background color
    blue = rsRand(1.0f);
    rsgClearColor(red, green, blue, alpha);

    // 50 frames per second = 20 milliseconds per frame
    return 20;
}
