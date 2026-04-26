/* math.x */
struct numbers {
    int a;
    int b;
};

program MATH_PROG {
    version MATH_VERS {
        int ADD(numbers) = 1; /* 1 is the procedure number */
    } = 1; /* 1 is the version number */
} = 0x23451111; /* A random unique ID for your program */