
import java.util.*;

public class LamportMutex {

    // Helper class to store requests
    // implements Comparable so the PriorityQueue knows how to sort them
    static class Request implements Comparable<Request> {

        int timestamp;
        int pId;

        public Request(int timestamp, int pId) {
            this.timestamp = timestamp;
            this.pId = pId;
        }

        // CRITICAL EXAM LOGIC: How to sort the queue
        @Override
        public int compareTo(Request other) {
            if (this.timestamp == other.timestamp) {
                // If timestamps are a tie, the lower Process ID wins
                return this.pId - other.pId;
            }
            // Otherwise, sort by lowest timestamp
            return this.timestamp - other.timestamp;
        }

        @Override
        public String toString() {
            return "(Time:" + timestamp + ", P" + pId + ")";
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int numProcesses = sc.nextInt();

        // The shared Priority Queue simulating the network's knowledge
        PriorityQueue<Request> queue = new PriorityQueue<>();

        // Array to keep track of Lamport Clocks for each process
        int[] clocks = new int[numProcesses];
        Arrays.fill(clocks, 0);

        System.out.println("\n--- Lamport Mutual Exclusion Simulator ---");
        System.out.println("1: Process requests Critical Section (CS)");
        System.out.println("2: Process releases Critical Section (CS)");
        System.out.println("0: Exit");

        while (true) {
            System.out.print("\nEnter choice: ");
            int choice = sc.nextInt();

            if (choice == 0) {
                break;
            }

            if (choice == 1) {
                System.out.print("Enter Process ID requesting CS: ");
                int pId = sc.nextInt();

                // Increment clock before requesting
                clocks[pId]++;
                Request req = new Request(clocks[pId], pId);

                // Add to queue
                queue.add(req);
                System.out.println("Process " + pId + " sent REQ with timestamp " + clocks[pId]);
                System.out.println("Replies received from all other processes.");

            } else if (choice == 2) {
                System.out.print("Enter Process ID releasing CS: ");
                int pId = sc.nextInt();

                // Check if this process is actually at the top of the queue
                if (!queue.isEmpty() && queue.peek().pId == pId) {
                    Request removed = queue.poll(); // Remove from top
                    System.out.println("Process " + pId + " released the CS.");

                    // Increment clock for release event
                    clocks[pId]++;
                } else {
                    System.out.println("ERROR: Process " + pId + " cannot release. It is not in the CS!");
                }
            }

            // Print the current state
            System.out.println("\n--- Current State ---");
            System.out.println("Queue: " + queue);
            if (!queue.isEmpty()) {
                System.out.println("*** Process " + queue.peek().pId + " is currently executing in the CS ***");
            } else {
                System.out.println("*** Critical Section is EMPTY ***");
            }
        }
        sc.close();
    }
}
