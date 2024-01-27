import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class PriorityScheduling {
    private List<Process> processes;

    // Constructor to initialize with a list of processes
    public PriorityScheduling(List<Process> processes) {
        this.processes = processes;
    }

    // Method to schedule the processes
    public void scheduleProcesses() {
        // Sort the processes based on their arrival time and priority
        Collections.sort(processes, (p1, p2) -> {
            if (p1.getArrivalTime() == p2.getArrivalTime()) {
                return p1.getPriority() - p2.getPriority();
            }
            return p1.getArrivalTime() - p2.getArrivalTime();
        });

        int currentTime = 0;

        // Lists to store information for output
        List<String> executionOrder = new ArrayList<>();
        List<Integer> waitingTimes = new ArrayList<>();
        List<Integer> turnaroundTimes = new ArrayList<>();

        while (!processes.isEmpty()) {
            Process selectedProcess = null;

            // Iterate through processes to find the highest priority process that has arrived
            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime) {
                    selectedProcess = process;
                    break;
                }
            }

            // Execute the selected process if found
            if (selectedProcess != null) {
                executeProcess(selectedProcess, currentTime);
                currentTime += selectedProcess.getBurstTime();

                // Store information for output
                executionOrder.add(selectedProcess.getName());
                waitingTimes.add(selectedProcess.getWaitingTime());
                turnaroundTimes.add(selectedProcess.getTurnaroundTime());
            } else {
                // No process is ready to execute, consider idle time
                currentTime++;
            }

            // Age the waiting processes
            ageWaitingProcesses();
        }

        // Print the results
        System.out.println("Processes Execution Order: " + String.join(" -> ", executionOrder));

        // Calculate and print average waiting time and average turnaround time
        double avgWaitingTime = waitingTimes.stream().mapToDouble(Integer::doubleValue).average().orElse(0);
        double avgTurnaroundTime = turnaroundTimes.stream().mapToDouble(Integer::doubleValue).average().orElse(0);
        System.out.println("Average Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
    }

    // Execute a process, print the execution details, remove it from the list
    private void executeProcess(Process process, int currentTime) {
        process.setWaitingTime(currentTime - process.getArrivalTime());
        process.setTurnaroundTime(process.getWaitingTime() + process.getBurstTime());

        System.out.println("Process " + process.getName() + " executed from " + currentTime + " to " + (currentTime + process.getBurstTime()));
        process.printProcessDetails();
        System.out.println("##############################");


        processes.remove(process);
    }

    // Age the waiting processes
    private void ageWaitingProcesses() {
        for (Process process : processes) {
            process.incrementPriority();
        }
    }

    /*public static void main(String[] args) {
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
        // Creating a list of processes for testing
        List<Process> processesList = new ArrayList<>();
        processesList.add(new Process("P1", 0, 10, 3));
        processesList.add(new Process("P2", 0, 1, 1));
        processesList.add(new Process("P3", 0, 2, 4));
        processesList.add(new Process("P4", 0, 1, 5));
        processesList.add(new Process("P5", 0, 5, 2));

        // Creating an instance of the PriorityScheduling and scheduling the processes
        PriorityScheduling scheduler = new PriorityScheduling(processesList);
        scheduler.scheduleProcesses();}*/
}
