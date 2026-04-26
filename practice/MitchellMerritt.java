
import java.util.*;

public class MitchellMerritt {

    static class Process {

        int id;
        int local;
        int publiclabel;
        int blockedon;

        public Process(int id) {
            this.id = id;
            this.local = 0;
            this.publiclabel = 0;
            this.blockedon = -1;
        }

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of processes:");
        int nump = sc.nextInt();
        Process process[] = new Process[nump];
        for (int i = 0; i < nump; i++) {
            process[i] = new Process(i);
        }
        int labelcounter = 10;
        while (true) {
            System.out.print("Enter your choice 1)add edge 2)transmit 3)exit:");
            int x = sc.nextInt();
            if (x == 3) {
                break;
            } else if (x == 1) {
                int u = sc.nextInt(), v = sc.nextInt();
                process[u].blockedon = v;
                labelcounter++;
                process[u].local = labelcounter;
                process[u].publiclabel = labelcounter;
            } else if (x == 2) {
                boolean ischanged = false;
                for (int u = 0; u < nump; u++) {
                    if (process[u].blockedon != -1) {
                        int v = process[u].blockedon;
                        if (process[u].local == process[v].publiclabel) {
                            System.out.println("Deadlock");
                            return;
                        }
                        if (process[u].publiclabel < process[v].publiclabel) {
                            process[u].publiclabel = process[v].publiclabel;
                            ischanged = true;
                        }
                    }
                }
                if (!ischanged) {
                    System.out.println("The graph is stabled");
                }
            }
        }
    }
}
