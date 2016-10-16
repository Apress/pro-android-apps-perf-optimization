#pragma version(1)
#pragma rs java_package_name(com.apress.proandroid.ch9)

#include "rs_graphics.rsh"

// Invoked automatically when the script is created
void init() {
    // do whatever you need to do here...
}

int root() {
	float r = rsRand(1.0f);
	float g = rsRand(1.0f);
	float b = rsRand(1.0f);
	
    // Clear the background color
    rsgClearColor(r, g, b, 1.0f); // alpha is 1.0 (opaque)

    // 50 frames per second = 20 milliseconds per frame
    return 20;
}
