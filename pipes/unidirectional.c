#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>

int main() {
    int fd[2]; // fd[0] = read, fd[1] = write
    pid_t pid;
    char write_msg[] = "Hello Child, this is your Parent!";
    char read_msg[100];

    // 1. Create the pipe BEFORE forking
    if (pipe(fd) == -1) {
        perror("Pipe failed");
        return 1;
    }

    // 2. Fork to create a child process
    pid = fork();

    if (pid < 0) {
        perror("Fork failed");
        return 1;
    }

    // ---------------- CHILD PROCESS ----------------
    if (pid == 0) {
        // Child is the READER. Close the write end.
        close(fd[1]);

        // Read from the pipe
        read(fd[0], read_msg, sizeof(read_msg));
        printf("Child Process Received: %s\n", read_msg);

        // Close read end when done
        close(fd[0]);
    }
    // ---------------- PARENT PROCESS ----------------
    else {
        // Parent is the WRITER. Close the read end.
        close(fd[0]);

        printf("Parent Process Sending: %s\n", write_msg);
        // Write to the pipe
        write(fd[1], write_msg, strlen(write_msg) + 1); // +1 to include null terminator

        // Close write end when done
        close(fd[1]);

        // Wait for child to finish so we don't create a zombie process
        wait(NULL);
    }

    return 0;
}