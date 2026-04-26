#include <stdio.h>
#include <mpi.h>

int main(int argc, char *argv[]) {
    int rank, size;
    
    // Contiguous 2D arrays
    int A[2][2] = {{5, 6}, {7, 8}};
    int B[2][2] = {{1, 2}, {3, 4}};
    int C[2][2];

    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    // Safety check
    if (size < 2) {
        if (rank == 0) {
            printf("Please run with at least 2 processes (e.g., mpirun -np 2)\n");
        }
        MPI_Finalize();
        return 0;
    }

    // --------------------------------------------------------
    // RANK 0: THE MASTER
    // --------------------------------------------------------
    if (rank == 0) {
        printf("Master (Rank 0) is distributing work...\n");

        // 1. Send Row 1 of A and B to Rank 1
        // Parameters: (data_pointer, count, data_type, destination_rank, tag, communicator)
        MPI_Send(&A[1][0], 2, MPI_INT, 1, 0, MPI_COMM_WORLD);
        MPI_Send(&B[1][0], 2, MPI_INT, 1, 1, MPI_COMM_WORLD);

        // 2. Master processes its own share (Row 0)
        for(int j = 0; j < 2; j++) {
            C[0][j] = A[0][j] - B[0][j];
        }

        // 3. Receive the calculated Row 1 back from Rank 1
        MPI_Recv(&C[1][0], 2, MPI_INT, 1, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        // 4. Print the final combined result
        printf("\nFinal Matrix Subtraction Result:\n");
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                printf("%d ", C[i][j]);
            }
            printf("\n");
        }
    } 
    // --------------------------------------------------------
    // RANK 1: THE WORKER
    // --------------------------------------------------------
    else if (rank == 1) {
        int rowA[2], rowB[2], rowC[2];

        // 1. Receive Row 1 from Master
        // Note: The tags (0 and 1) must match the Master's send tags!
        MPI_Recv(&rowA, 2, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        MPI_Recv(&rowB, 2, MPI_INT, 0, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        // 2. Worker does the math for its specific row
        for(int j = 0; j < 2; j++) {
            rowC[j] = rowA[j] - rowB[j];
        }

        // 3. Send the finished row back to Master (Tag 2)
        MPI_Send(&rowC, 2, MPI_INT, 0, 2, MPI_COMM_WORLD);
    }

    MPI_Finalize();
    return 0;
}

// sudo apt update
// sudo apt install openmpi-bin libopenmpi-dev

//mpicc your_program.c -o your_program
//mpirun -np 4 ./your_program