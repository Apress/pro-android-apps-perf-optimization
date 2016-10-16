.global gcd_asm
.func gcd_asm
	
gcd_asm:
	cmp   r0, r1
	subgt r0, r0, r1
	sublt r1, r1, r0
	bne   gcd_asm
	bx    lr
.endfunc
.end
