
import java.util.*;

public class SuzukiKasami {

    static class Token {

        int ln[];
        Queue<Integer> q;

        public Token(int number) {
            ln = new int[number];
            Arrays.fill(ln, 0);
            q = new LinkedList<>();
        }
    }

    static class Process {

        int id;
        int rn[];
        boolean hastoken;
        boolean incs;

        public Process(int id, int num, boolean hastoken) {
            this.id = id;
            this.incs = false;
            this.hastoken = hastoken;
            this.rn = new int[num];
            Arrays.fill(rn, 0);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of processes:");
        int num = sc.nextInt();
        Process process[] = new Process[num];
        Token token = new Token(num);
        for (int i = 0; i < num; i++) {
            process[i] = new Process(i, num, (i == 0));
        }

        System.out.println("Enter:\n1)Request\n2)Release\n3)exit\n");
        while (true) {
            System.out.print("Enter your choice:");
            int x = sc.nextInt();
            if (x == 3) {
                break;
            } else if (x == 1) {
                System.out.print("Enter the process Id:");
                int reqid = sc.nextInt();
                if (process[reqid].incs) {
                    System.out.print("Already in cs");
                    continue;
                }
                if (process[reqid].hastoken) {
                    process[reqid].incs = true;
                    System.out.println("Process " + reqid + " has token now it is entering critical section!");
                    continue;
                }
                process[reqid].rn[reqid]++;
                int sequencenumber = process[reqid].rn[reqid]++;

                System.out.println(reqid + "requesting for token: broadcasted request(" + reqid + "," + sequencenumber + ")");
                for (int i = 0; i < num; i++) {
                    if (i == reqid) {
                        continue;
                    }

                    process[i].rn[reqid] = Math.max(process[i].rn[reqid], sequencenumber);

                    if (process[i].hastoken && process[i].rn[reqid] == token.ln[reqid] + 1 && !process[i].incs) {
                        process[i].hastoken = false;
                        System.out.println("Token is being sent from process " + i + " to process " + reqid);
                        process[reqid].hastoken = true;
                        process[reqid].incs = true;
                        System.out.println("Process " + reqid + " has entered got the token and entered cs");

                    }

                }

            } else if (x == 2) {
                System.out.print("Enter the process to be released from cs");
                int relid = sc.nextInt();
                if (!process[relid].hastoken || !process[relid].incs) {
                    System.out.println(relid + " not In critical section or has token");
                    continue;
                }
                process[relid].incs = false;
                token.ln[relid] = process[relid].rn[relid];
                for (int i = 0; i < num; i++) {
                    if (process[relid].rn[i] == token.ln[i] + 1 && !token.q.contains(i)) {
                        token.q.add(i);
                    }
                }
                if (!token.q.isEmpty()) {
                    int proc = token.q.poll();
                    process[relid].hastoken = false;
                    process[proc].hastoken = true;

                } else {
                    System.out.println("Queue is empty");
                }

            }
        }
    }

}
