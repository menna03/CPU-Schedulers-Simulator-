import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class SRTFScheduler {
    static void inputProcesses(Process[] proc, Scanner scanner) {
        for (int i = 0; i < proc.length; i++) {
            proc[i] = Process.inputProcessDetails(scanner, i + 1);
        }
    }
    static void STRF(Process[] proc, int number_of_processes, int[] waitingTime) {
        int[] remainingTime = new int[number_of_processes];
        for (int i = 0; i < number_of_processes; i++)
            remainingTime[i] = proc[i].burstTime;

        int complete = 0, t = 0;
        boolean check;

        while (complete != number_of_processes) {
            int minRemainingTime = Integer.MAX_VALUE;
            int shortest = -1;
            check = false;

            for (int j = 0; j < number_of_processes; j++) {
                if (proc[j].arrivalTime <= t && remainingTime[j] < minRemainingTime && remainingTime[j] > 0) {
                    minRemainingTime = remainingTime[j];
                    shortest = j;
                    check = true;
                }
            }

            if (!check) {
                t++;
                continue;
            }

            remainingTime[shortest]--;
            if (remainingTime[shortest] == 0) {
                complete++;

                proc[shortest].turnaroundTime = t + 1 - proc[shortest].arrivalTime;
                proc[shortest].waitingTime = proc[shortest].turnaroundTime - proc[shortest].burstTime;

                waitingTime[shortest] = proc[shortest].waitingTime;

            }
            t++;

            // Aging: Increment priority of waiting processes
            for (Process waitingProcess : proc) {
                if (waitingProcess != proc[shortest]) {
                    waitingProcess.priority++;
                }
            }
        }
    }

    static void PrintSRTF(Process[] proc, int n) {
        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];
        STRF(proc, n, waitingTime);

        for (int i = 0; i < n; i++) {
            proc[i].printProcessDetails();
            System.out.println("########################################################");

            turnaroundTime[i] = proc[i].turnaroundTime;
        }

        // Convert arrays to lists
        List<Integer> waitingTimesList = new ArrayList<>();
        List<Integer> turnaroundTimesList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            waitingTimesList.add(waitingTime[i]);
            turnaroundTimesList.add(turnaroundTime[i]);
        }

        Process.printAverageTimes( waitingTimesList, turnaroundTimesList);
    }
    /*public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int numberOfProcesses = scanner.nextInt();

        Process[] proc = new Process[numberOfProcesses];

        for (int i = 0; i < numberOfProcesses; i++) {
            proc[i] = Process.inputProcessDetails(scanner, i + 1);
        }
        SRTFScheduler.PrintSRTF(proc, numberOfProcesses);

        scanner.close();
    }
    /*
    *     public static void main(String[] args) {
        Process proc[] = { new Process("P1", 0, 8, 1),
                new Process("P2", 1, 4, 2),
                new Process("P3", 2, 9, 1),
                new Process("P4", 3, 5, 3)};

        findavgTime(proc, proc.length);
    }*/
}
