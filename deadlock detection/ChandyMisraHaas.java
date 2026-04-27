
import java.util.*;

public class ChandyMisraHaas {

    // The Wait-For Graph: wfg[u][v] = 1 means U is waiting for V
    static int[][] wfg;
    static int numProcesses;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        numProcesses = sc.nextInt();

        wfg = new int[numProcesses][numProcesses];

        System.out.println("\n--- Chandy-Misra-Haas Deadlock Detection ---");
        System.out.println("1: Add Edge (Process U blocks, waiting for Process V)");
        System.out.println("2: Initiate Probe (Check for deadlock from a specific Process)");
        System.out.println("0: Exit");

        while (true) {
            System.out.print("\nEnter choice: ");
            int choice = sc.nextInt();

            if (choice == 0) {
                break;
            }

            if (choice == 1) {
                System.out.print("Enter blocking Process (U): ");
                int u = sc.nextInt();
                System.out.print("Enter Process it is waiting for (V): ");
                int v = sc.nextInt();

                // Add the directed edge to the Wait-For Graph
                wfg[u][v] = 1;
                System.out.println("Edge added: P" + u + " is waiting for P" + v);

            } else if (choice == 2) {

                System.out.print("Enter Initiator Process ID to start probe: ");
                int initiator = sc.nextInt();

                System.out.println("Initiating probe from P" + initiator + "...");

                // Array to prevent infinite loops if there is a cycle that DOESN'T include the initiator
                boolean[] visited = new boolean[numProcesses];
                visited[initiator] = true;

                // Start the recursive probe passing
                boolean deadlock = sendProbe(initiator, initiator, visited);

                if (!deadlock) {
                    System.out.println("Graph searched. No deadlock detected for Initiator P" + initiator + ".");
                }
            }
        }
        sc.close();
    }

    // CRITICAL EXAM LOGIC: The recursive edge-chasing function
    static boolean sendProbe(int initiator, int sender, boolean[] visited) {
        boolean foundCycle = false;

        // Look for anyone the current 'sender' is waiting for
        for (int receiver = 0; receiver < numProcesses; receiver++) {
            if (wfg[sender][receiver] == 1) {

                // Print the exact Probe format for the examiner
                System.out.println(" -> Probe(" + initiator + ", " + sender + ", " + receiver + ") sent.");

                // RULE 3: Detection! Did the probe come back to the initiator?
                if (receiver == initiator) {
                    System.out.println("\n🚨 DEADLOCK DETECTED! 🚨");
                    System.out.println("The probe completed a full cycle and returned to Initiator P" + initiator + "!");
                    return true;
                }

                // RULE 2: Propagation! If we haven't visited this node on this probe run, forward it.
                if (!visited[receiver]) {
                    visited[receiver] = true;
                    // Recursive call: The receiver now becomes the sender
                    if (sendProbe(initiator, receiver, visited)) {
                        foundCycle = true;
                    }
                }
            }
        }
        return foundCycle;
    }
}
