import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class SJF {
    private final List<Process> processes;
    private final int contextSwitchingTime;

    public SJF(List<Process> processes, int contextSwitchingTime) {
        this.processes = processes;
        this.contextSwitchingTime = contextSwitchingTime;
    }

    public void scheduleProcesses() {
        List<Process> arrivalSortedQueue = new ArrayList<>(processes);
        arrivalSortedQueue.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = arrivalSortedQueue.get(0).getArrivalTime();
        List<Integer> waitingTimes = new ArrayList<>();
        List<Integer> turnaroundTimes = new ArrayList<>();

        while (!arrivalSortedQueue.isEmpty()) {
            Process currentProcess = arrivalSortedQueue.get(0);
            arrivalSortedQueue.remove(currentProcess);

            if (currentProcess.getArrivalTime() > currentTime) {
                currentTime = currentProcess.getArrivalTime();
            }

            currentProcess.setWaitingTime(currentTime - currentProcess.getArrivalTime());
            currentProcess.setTurnaroundTime(currentProcess.getWaitingTime() + currentProcess.getBurstTime());

            currentTime += currentProcess.getBurstTime() + contextSwitchingTime;

            waitingTimes.add(currentProcess.getWaitingTime());
            turnaroundTimes.add(currentProcess.getTurnaroundTime());
        }

        processes.sort(Comparator.comparingInt(Process::getBurstTime));

        for (Process process : processes) {
            process.printProcessDetails();
        }
        Process.printAverageTimes(waitingTimes, turnaroundTimes);
    }

    /*public static void main(String[] args) {
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
    }*/
}