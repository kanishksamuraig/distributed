#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>

int main() {
    // We need TWO pipes
    int pipe1[2]; // Parent -> Child
    int pipe2[2]; // Child -> Parent

    pid_t pid;
    char parent_msg[] = "Hey Child, what's the secret code?";
    char child_msg[] = "The code is 42, Parent!";
    char buffer[100];

    // Create both pipes
    if (pipe(pipe1) == -1 || pipe(pipe2) == -1) {
        perror("Pipe creation failed");
        return 1;
    }

    // Fork the process
    pid = fork();

    if (pid < 0) {
        perror("Fork failed");
        return 1;
    }

    // ---------------- CHILD PROCESS ----------------
    if (pid == 0) {
        // Child reads from pipe1 and writes to pipe2
        close(pipe1[1]); // Close write end of pipe 1
        close(pipe2[0]); // Close read end of pipe 2

        // 1. Read message from Parent
        read(pipe1[0], buffer, sizeof(buffer));
        printf("Child heard: %s\n", buffer);

        // 2. Send reply to Parent
        printf("Child sending reply...\n");
        write(pipe2[1], child_msg, strlen(child_msg) + 1);

        // Clean up
        close(pipe1[0]);
        close(pipe2[1]);
    }
    // ---------------- PARENT PROCESS ----------------
    else {
        // Parent writes to pipe1 and reads from pipe2
        close(pipe1[0]); // Close read end of pipe 1
        close(pipe2[1]); // Close write end of pipe 2

        // 1. Send message to Child
        printf("Parent sending: %s\n", parent_msg);
        write(pipe1[1], parent_msg, strlen(parent_msg) + 1);

        // 2. Read reply from Child
        read(pipe2[0], buffer, sizeof(buffer));
        printf("Parent heard: %s\n", buffer);

        // Clean up
        close(pipe1[1]);
        close(pipe2[0]);

        wait(NULL); // Wait for child to finish
    }

    return 0;
}