
import java.util.*;

public class ChandyMisraHaas {

    static int numprocess;
    static int wfg[][];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the no of processes");
        numprocess = sc.nextInt();
        wfg = new int[numprocess][numprocess];

        for (int rows[] : wfg) {
            Arrays.fill(rows, 0);
        }
        System.out.println("Enter one of the choices:\n1)Add an edge\n2)Send probe\n3) Exit");

        while (true) {
            System.out.print("Enter your choice:");
            int x = sc.nextInt();
            if (x == 3) {
                break;
            } else if (x == 1) {
                System.out.print("Enter the start and the dest node as u v:");
                int u = sc.nextInt();
                int v = sc.nextInt();
                wfg[u][v] = 1;
            } else if (x == 2) {
                System.out.println("Probe Starting or initiated from node:");
                int initiator = sc.nextInt();
                boolean visited[] = new boolean[numprocess];
                visited[initiator] = true;
                if (sendprobe(initiator, initiator, visited)) {
                    System.out.println("Cycle detected!!!");
                } else {
                    System.out.println("No cycles or deadlocks");
                }
            }
        }
    }

    static boolean sendprobe(int initiator, int sender, boolean[] visited) {
        boolean detected = false;
        for (int reciever = 0; reciever < numprocess; reciever++) {
            if (wfg[sender][reciever] == 1) {
                if (reciever == initiator) {
                    return true;
                }
                if (!visited[reciever]) {
                    visited[reciever] = true;
                    if (sendprobe(initiator, reciever, visited)) {
                        detected = true;
                    }
                }
            }
        }
        return detected;
    }

}
