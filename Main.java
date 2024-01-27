import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose a scheduling algorithm:");
        System.out.println("1. Non-Preemptive Shortest-Job-First (SJF)");
        System.out.println("2. Shortest-Remaining-Time-First (SRTF)");
        System.out.println("3. Non-Preemptive Priority Scheduling");
        System.out.println("4. AG Scheduling");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                nonPreemptiveSJF();
                break;
            case 2:
                shortestRemainingTimeFirst();
                break;
            case 3:
                nonPreemptivePriorityScheduling();
                break;
            case 4:
                agScheduling();
                break;
            default:
                System.out.println("Invalid choice");
        }

        scanner.close();
    }

    private static void nonPreemptiveSJF() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();

        System.out.print("Enter the context switching time: ");
        int contextSwitchingTime = scanner.nextInt();

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < numProcesses; i++) {
            Process process = Process.inputProcessDetails(scanner, i);
            processes.add(process);
        }

        SJF sjf = new SJF(processes, contextSwitchingTime);
        sjf.scheduleProcesses();

        scanner.close();
    }

    private static void shortestRemainingTimeFirst() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int numberOfProcesses = scanner.nextInt();

        Process[] proc = new Process[numberOfProcesses];

        SRTFScheduler.inputProcesses(proc, scanner); // Call the method to input processes

        SRTFScheduler.PrintSRTF(proc, numberOfProcesses);

        scanner.close();
    }

    private static void nonPreemptivePriorityScheduling() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int numberOfProcesses = scanner.nextInt();

        List<Process> procList = new ArrayList<>();

        for (int i = 0; i < numberOfProcesses; i++) {
            procList.add(Process.inputProcessDetails(scanner, i + 1));
        }

        PriorityScheduling scheduler = new PriorityScheduling(procList);
        scheduler.scheduleProcesses();

        scanner.close();
    }

    private static void agScheduling() {
   /*  List<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", "Red", 0, 17, 4));
        processes.add(new Process("P2", "Blue", 3, 6, 9));
        processes.add(new Process("P3", "Green", 4, 10, 3));
        processes.add(new Process("P4", "Yellow", 29, 4, 8));

        int timeQuantum = 4;

        AGScheduler scheduler = new AGScheduler(processes, timeQuantum);

        scheduler.printTable();

        scheduler.execute();*/
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();

        List<Process> processes = new ArrayList<>();

        for (int i = 1; i <= numProcesses; i++) {
            System.out.println("Enter details for Process " + i + ":");
            System.out.print("Name: ");
            String name = scanner.next();

            System.out.print("Arrival Time: ");
            int arrivalTime = scanner.nextInt();

            System.out.print("Burst Time: ");
            int burstTime = scanner.nextInt();

            System.out.print("Priority: ");
            int priority = scanner.nextInt();


            processes.add(new Process(name, arrivalTime, burstTime, priority));
        }

        System.out.print("Enter the quantum time: ");
        int quantum = scanner.nextInt();

        AGScheduler scheduler = new AGScheduler(processes, quantum);
        scheduler.printTable();
        scheduler.execute();
        scanner.close();
    }

}