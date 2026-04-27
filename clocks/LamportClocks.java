
import java.util.Scanner;

public class LamportClocks {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int numProcesses = sc.nextInt();

        // Array to hold the current clock value for each process
        int[] clocks = new int[numProcesses];

        // Initialize all clocks to 0
        for (int i = 0; i < numProcesses; i++) {
            clocks[i] = 0;
        }

        System.out.println("\n--- Event Simulator ---");
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
                // RULE 1: Increment own clock
                clocks[pId] = clocks[pId] + 1;
                System.out.println("Process " + pId + " executed an event. New Clock: " + clocks[pId]);

            } else if (choice == 2) {
                System.out.print("Enter the timestamp of the received message: ");
                int msgTime = sc.nextInt();

                // RULE 2: Max(own clock, message clock) + 1
                clocks[pId] = Math.max(clocks[pId], msgTime) + 1;
                System.out.println("Process " + pId + " received message. New Clock: " + clocks[pId]);
            }

            // Print current state of all clocks
            System.out.print("Current Clock States: [ ");
            for (int i = 0; i < numProcesses; i++) {
                System.out.print("P" + i + ":" + clocks[i] + " ");
            }
            System.out.println("]");
        }
        sc.close();
    }
}
