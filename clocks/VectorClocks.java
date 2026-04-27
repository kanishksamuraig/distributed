
import java.util.Scanner;

public class VectorClocks {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int numProcesses = sc.nextInt();

        // 2D Array: Rows are processes, Columns are the vector values
        int[][] vectorClocks = new int[numProcesses][numProcesses];

        System.out.println("\n--- Vector Event Simulator ---");
        System.out.println("Options:");
        System.out.println("1: Internal Event or Send Event");
        System.out.println("2: Receive Event");
        System.out.println("0: Exit");

        while (true) {
            System.out.print("\nEnter option: ");
            int choice = sc.nextInt();

            if (choice == 0) {
                break;
            }

            System.out.print("Enter process ID (0 to " + (numProcesses - 1) + "): ");
            int pId = sc.nextInt();

            if (choice == 1) {
                // RULE 1: Increment ONLY own index
                vectorClocks[pId][pId] += 1;
                printVector(pId, vectorClocks[pId]);

            } else if (choice == 2) {
                int[] receivedVector = new int[numProcesses];
                System.out.println("Enter the vector of the received message (space separated): ");
                for (int i = 0; i < numProcesses; i++) {
                    receivedVector[i] = sc.nextInt();
                }

                // RULE 2: Take max of each element, THEN increment own index
                for (int i = 0; i < numProcesses; i++) {
                    vectorClocks[pId][i] = Math.max(vectorClocks[pId][i], receivedVector[i]);
                }
                vectorClocks[pId][pId] += 1; // Increment own

                printVector(pId, vectorClocks[pId]);
            }
        }
        sc.close();
    }

    // Helper method to print the vector cleanly
    private static void printVector(int pId, int[] vector) {
        System.out.print("Process " + pId + " New Vector: [ ");
        for (int val : vector) {
            System.out.print(val + " ");
        }
        System.out.println("]");
    }
}
