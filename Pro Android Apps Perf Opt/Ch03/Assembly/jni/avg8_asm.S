.global avg8_asm
.func avg8_asm

.thumb_func
avg8_asm:
	uhadd8 r0, r0, r1
	bx    lr
.endfunc
.end
