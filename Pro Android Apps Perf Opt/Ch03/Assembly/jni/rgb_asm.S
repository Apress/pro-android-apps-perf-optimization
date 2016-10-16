.global argb8888_to_rgb565_asm
.func argb8888_to_rgb565_asm

argb8888_to_rgb565_asm:
	// r0=aaaaaaaarrrrrrrrggggggggbbbbbbbb
	// r1=undefined (scratch register)
	
	ubfx r1, r0, #3, #5
	
	// r1=000000000000000000000000000bbbbb
	
	lsr r0, r0, #10
	
	// r0=0000000000aaaaaaaarrrrrrrrgggggg
	
	bfi r1, r0, #5, #6
	
	// r1=000000000000000000000ggggggbbbbb
	
	lsr r0, r0, #9
	
	// r0=0000000000000000000aaaaaaaarrrrr
	
	bfi r1, r0, #11, #5
	
	// r1=0000000000000000rrrrrggggggbbbbb
	
	mov r0, r1
	
	// r0=0000000000000000rrrrrggggggbbbbb
	
	bx    lr
.endfunc
.end
