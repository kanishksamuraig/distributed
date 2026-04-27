
import java.util.*;

public class MitchellMerritt {

    static class Process {

        int id;
        int local;
        int publicLabel;
        int blockedOn; // The ID of the process this one is waiting for (-1 if none)

        public Process(int id) {
            this.id = id;
            this.local = id;
            this.publicLabel = id;
            this.blockedOn = -1; // Active initially
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int numProcesses = sc.nextInt();

        Process[] p = new Process[numProcesses];
        for (int i = 0; i < numProcesses; i++) {
            p[i] = new Process(i);
        }

        // A counter to simulate generating a "Globally Unique, Larger Label"
        // We start it at 10 to clearly distinguish it from Process IDs
        int labelCounter = 10;

        System.out.println("\n--- Mitchell-Merritt Deadlock Detection ---");
        System.out.println("1: Block (Process U waits for Process V)");
        System.out.println("2: Transmit (Propagate labels one step)");
        System.out.println("0: Exit");

        while (true) {
            System.out.print("\nEnter choice: ");
            int choice = sc.nextInt();

            if (choice == 0) {
                break;
            }

            if (choice == 1) {
                System.out.print("Enter Process ID that is blocking (U): ");
                int u = sc.nextInt();
                System.out.print("Enter Process ID it is waiting for (V): ");
                int v = sc.nextInt();

                // Rule 1: Block. Generate a new label larger than any current public label.
                labelCounter++;
                p[u].local = labelCounter;
                p[u].publicLabel = labelCounter;
                p[u].blockedOn = v;

                System.out.println("Process " + u + " is now waiting on Process " + v);
                System.out.println("Process " + u + " generated new labels -> Local: " + p[u].local + ", Public: " + p[u].publicLabel);

            } else if (choice == 2) {
                System.out.println("--- Transmitting Labels ---");
                boolean labelChanged = false;

                // Rule 3 & 4: Transmit and Detect
                // We loop through all processes to see if anyone needs to update their public label
                for (int i = 0; i < numProcesses; i++) {
                    Process u = p[i];

                    if (u.blockedOn != -1) {
                        Process v = p[u.blockedOn];

                        // DETECT RULE: Did my own local label complete a cycle and come back?
                        if (v.publicLabel == u.local) {
                            System.out.println("🚨 DEADLOCK DETECTED BY PROCESS " + u.id + "! 🚨");
                            System.out.println("Cycle completed because Process " + v.id + " passed back Process " + u.id + "'s local label (" + u.local + ").");
                            return; // End the simulation
                        }

                        // TRANSMIT RULE: If the guy I am waiting for has a bigger public label, I take it.
                        if (v.publicLabel > u.publicLabel) {
                            u.publicLabel = v.publicLabel;
                            labelChanged = true;
                            System.out.println("Process " + u.id + " adopted public label " + u.publicLabel + " from Process " + v.id);
                        }
                    }
                }

                if (!labelChanged) {
                    System.out.println("No labels were updated this cycle. Graph is stable.");
                }

                // Print current state
                System.out.print("Current State -> ");
                for (int i = 0; i < numProcesses; i++) {
                    System.out.print("P" + i + "[L:" + p[i].local + ", P:" + p[i].publicLabel + "] ");
                }
                System.out.println();
            }
        }
        sc.close();
    }
}
