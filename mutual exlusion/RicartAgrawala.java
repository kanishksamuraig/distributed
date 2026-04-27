import java.util.*;

public class RicartAgrawala {

    static class Process {
        int id;
        int clock = 0;
        boolean isRequesting = false;
        boolean inCS = false;
        int requestTimestamp = 0;
        int repliesReceived = 0;
        Queue<Integer> deferredReplies = new LinkedList<>();

        public Process(int id) {
            this.id = id;
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

        System.out.println("\n--- Ricart-Agrawala Simulator ---");
        System.out.println("1: Process requests Critical Section (CS)");
        System.out.println("2: Process releases Critical Section (CS)");
        System.out.println("0: Exit");

        while (true) {
            System.out.print("\nEnter choice: ");
            int choice = sc.nextInt();

            if (choice == 0) break;

            if (choice == 1) {
                System.out.print("Enter Process ID requesting CS: ");
                int reqId = sc.nextInt();
                Process reqP = p[reqId];

                if (reqP.isRequesting || reqP.inCS) {
                    System.out.println("Process " + reqId + " is already requesting or in CS!");
                    continue;
                }

                // Rule 1: Update clock and state
                reqP.clock++;
                reqP.requestTimestamp = reqP.clock;
                reqP.isRequesting = true;
                reqP.repliesReceived = 0;

                System.out.println("Process " + reqId + " broadcasts REQ with timestamp " + reqP.requestTimestamp);

                // Rule 2: Evaluate against all other processes
                for (int i = 0; i < numProcesses; i++) {
                    if (i == reqId) continue;
                    
                    Process otherP = p[i];
                    
                    // Check if the other process defers
                    boolean otherDefers = otherP.inCS || 
                        (otherP.isRequesting && 
                        (otherP.requestTimestamp < reqP.requestTimestamp || 
                        (otherP.requestTimestamp == reqP.requestTimestamp && otherP.id < reqP.id)));

                    if (otherDefers) {
                        otherP.deferredReplies.add(reqId);
                        System.out.println(" -> Process " + i + " DEFERRED the reply.");
                    } else {
                        reqP.repliesReceived++;
                        System.out.println(" -> Process " + i + " replied immediately.");
                    }
                }

                // Rule 3: Entry condition check
                if (reqP.repliesReceived == numProcesses - 1) {
                    reqP.inCS = true;
                    reqP.isRequesting = false;
                    System.out.println("*** Process " + reqId + " received all replies and ENTERED the CS! ***");
                } else {
                    System.out.println("Process " + reqId + " is WAITING for deferred replies.");
                }

            } else if (choice == 2) {
                System.out.print("Enter Process ID releasing CS: ");
                int relId = sc.nextInt();
                Process relP = p[relId];

                if (!relP.inCS) {
                    System.out.println("Process " + relId + " is not in the CS!");
                    continue;
                }

                System.out.println("Process " + relId + " released the CS.");
                relP.inCS = false;
                relP.clock++; // Increment clock on release event

                // Rule 3: Send all deferred replies
                while (!relP.deferredReplies.isEmpty()) {
                    int waitingId = relP.deferredReplies.poll();
                    Process waitingP = p[waitingId];
                    waitingP.repliesReceived++;
                    System.out.println(" -> Sent deferred reply to Process " + waitingId);

                    // If the waiting process now has all replies, it enters CS
                    if (waitingP.repliesReceived == numProcesses - 1) {
                        waitingP.inCS = true;
                        waitingP.isRequesting = false;
                        System.out.println("*** Process " + waitingId + " received all deferred replies and ENTERED the CS! ***");
                    }
                }
            }
        }
        sc.close();
    }
}