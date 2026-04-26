#include <stdio.h>
#include <omp.h> // CRITICAL: You must include this header!

int main() {
    // 1. We just declare normal arrays. No MPI_Init needed!
    int A[2][2] = {{5, 6}, {7, 8}};
    int B[2][2] = {{1, 2}, {3, 4}};
    int C[2][2];

    printf("Starting OpenMP Matrix Subtraction...\n");

    // 2. The Magic Line: This tells the compiler to divide the iterations 
    // of the loop directly below it among the available CPU threads.
    #pragma omp parallel for
    for (int i = 0; i < 2; i++) {
        
        // Let's grab the thread ID just so the examiner can see it working!
        int thread_id = omp_get_thread_num();
        printf(" -> Thread %d is computing row %d\n", thread_id, i);

        // The inner loop does the actual math
        for (int j = 0; j < 2; j++) {
            C[i][j] = A[i][j] - B[i][j];
        }
    }

    // 3. Print the final result
    printf("\nFinal Matrix Subtraction Result:\n");
    for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 2; j++) {
            printf("%d ", C[i][j]);
        }
        printf("\n");
    }

    return 0;
}
//gcc -fopenmp openmp.c -o openmp
