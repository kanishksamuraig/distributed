
import java.util.*;

public class SuzukiKasami {

    static class Token {

        int[] LN;
        Queue<Integer> Q;

        public Token(int numProcesses) {
            LN = new int[numProcesses];
            Arrays.fill(LN, 0);
            Q = new LinkedList<>();
        }
    }

    static class Process {

        int id;
        int[] RN;
        boolean hasToken;
        boolean inCS;

        public Process(int id, int numProcesses, boolean hasToken) {
            this.id = id;
            this.RN = new int[numProcesses];
            Arrays.fill(this.RN, 0);
            this.hasToken = hasToken;
            this.inCS = false;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int numProcesses = sc.nextInt();

        Process[] p = new Process[numProcesses];

        // Let Process 0 start with the Token initially
        for (int i = 0; i < numProcesses; i++) {
            p[i] = new Process(i, numProcesses, (i == 0));
        }

        Token token = new Token(numProcesses);

        System.out.println("\n--- Suzuki-Kasami Simulator ---");
        System.out.println("Process 0 starts with the Token.");
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
                int reqId = sc.nextInt();

                if (p[reqId].inCS) {
                    System.out.println("Process " + reqId + " is already in the CS!");
                    continue;
                }

                // If it already has the token, it just enters the CS
                if (p[reqId].hasToken) {
                    p[reqId].inCS = true;
                    System.out.println("*** Process " + reqId + " already has the token and ENTERED the CS! ***");
                    continue;
                }

                // Rule 1: Increment its own Request Number
                p[reqId].RN[reqId]++;
                int seqNum = p[reqId].RN[reqId];
                System.out.println("Process " + reqId + " broadcasts REQ(" + reqId + ", " + seqNum + ")");

                // Rule 2: Broadcast to everyone
                for (int i = 0; i < numProcesses; i++) {
                    if (i == reqId) {
                        continue;
                    }

                    // Everyone updates their knowledge of reqId's sequence number
                    p[i].RN[reqId] = Math.max(p[i].RN[reqId], seqNum);

                    // If someone has the idle token and sees the new request, send it
                    if (p[i].hasToken && !p[i].inCS && p[i].RN[reqId] == token.LN[reqId] + 1) {
                        System.out.println(" -> Process " + i + " sends the Token to Process " + reqId);
                        p[i].hasToken = false;
                        p[reqId].hasToken = true;
                        p[reqId].inCS = true;
                        System.out.println("*** Process " + reqId + " received the Token and ENTERED the CS! ***");
                    }
                }

            } else if (choice == 2) {
                System.out.print("Enter Process ID releasing CS: ");
                int relId = sc.nextInt();

                if (!p[relId].inCS || !p[relId].hasToken) {
                    System.out.println("ERROR: Process " + relId + " is not in the CS or doesn't have the token.");
                    continue;
                }

                System.out.println("Process " + relId + " released the CS.");
                p[relId].inCS = false;

                // Rule 3: Update the Token's Last Number array
                token.LN[relId] = p[relId].RN[relId];

                // Check everyone else: if they made a request that isn't in the queue yet, add them
                for (int i = 0; i < numProcesses; i++) {
                    if (p[relId].RN[i] == token.LN[i] + 1 && !token.Q.contains(i)) {
                        token.Q.add(i);
                        System.out.println(" -> Added Process " + i + " to the Token's wait queue.");
                    }
                }

                // If the queue isn't empty, pass the token to the next person
                if (!token.Q.isEmpty()) {
                    int nextId = token.Q.poll();
                    System.out.println(" -> Passing Token from " + relId + " to Process " + nextId + " from the queue.");
                    p[relId].hasToken = false;
                    p[nextId].hasToken = true;
                    // Note: The next process will officially enter the CS on its next turn/check
                } else {
                    System.out.println(" -> Token queue is empty. Process " + relId + " keeps the idle Token.");
                }
            }
        }
        sc.close();
    }
}
