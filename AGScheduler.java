import java.util.*;

class AGScheduler {
    List<Process> processes;
    int quantum;
    int startTime;
    int currentTime;
    Queue<Process> readyQueue;
    List<String> quantumHistory;
    List<Process> deadList;
    int processTime;
    Map<Process, Integer> turnAroundTimeMap = new HashMap<>();
    Map<Process, Integer> waitingTimeMap = new HashMap<>();

    private void setAllQuantum(List<Process> processes) {
        for (Process p : processes) {
            p.setQuantum(quantum);
            p.setInitialBurstTime(p.getBurstTime());
        }
    }
    public double meanOfQuantum(List<Process> processes, int currentTime) {
        int sumOfQuantum = 0;
        int size = 0;

        for (Process p : processes) {
            if (isValidProcess(p, currentTime)) {
                sumOfQuantum += p.quantum;
                size++;
            }
        }
        return calculateMean(sumOfQuantum, size);
    }
    private boolean isValidProcess(Process process, int currentTime) {
        return process.arrivalTime >= currentTime && process.burstTime > 0;
    }
    private double calculateMean(int sumOfQuantum, int size) {
        return (size == 0) ? 0 : (double) sumOfQuantum / size;
    }
    public AGScheduler(List<Process> processes, int quantum) {
        this.processes = processes;
        this.quantum = quantum;
        this.readyQueue = new LinkedList<>();
        this.deadList = new LinkedList<>();
        this.quantumHistory = new LinkedList<>();
        currentTime = 0;
        startTime = 0;
        setAllQuantum(processes);
    }
    public void execute() {
        while (processesInExecution()) {
            int nextProcessIndex = nextIndex();
            if (nextProcessIndex == -1) {
                if (!readyQueue.isEmpty()) {
                    executeNonPreemptive(readyQueue.poll());
                }
            } else {
                executeNonPreemptive(processes.get(nextProcessIndex));
            }
        }
        printTurnaroundAndWaitingTimes();
        printQuantumHistory();
    }
    private boolean processesInExecution() {
        return !processes.isEmpty();
    }
    private int nextIndex() {
        int maxAG = 1000;
        int index = -1;
        for (int i = 0; i < processes.size(); i++) {
            Process currentProcess = processes.get(i);
            if (currentProcess.arrivalTime <= currentTime && currentProcess.getAGFactor() < maxAG && !isInReadyQueue(currentProcess)) {
                index = i;
                maxAG = currentProcess.getAGFactor();
            }
        }
        return index;
    }
    private boolean isInReadyQueue(Process currentProcess) {
        return readyQueue.contains(currentProcess);
    }

    //---------------------------------------------------Preemptive---------------------------------------------------------
    private void executePreemptive(Process currentProcess) {
        Process newProcess = findNewProcess(currentProcess);

        while (newProcess == null && currentProcess.getBurstTime() > 0 && !isQuantumFinished(currentProcess, processTime)) {
            currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
            processTime++;
            currentTime++;
            newProcess = findNewProcess(currentProcess);
        }

        if (isExecutionFinished(currentProcess)) {
            handleExecutionFinished(currentProcess);
        } else if (isQuantumFinished(currentProcess, processTime)) {
            handleQuantumFinished(currentProcess);
        } else if (newProcess != null) {
            handleNewProcess(newProcess, currentProcess);
        }
    }

    private void handleExecutionFinished(Process currentProcess) {
        deadList.add(currentProcess);
        currentProcess.setQuantum(0);
        processTime = 0;
        updateTurnaroundAndWaitingTimes(currentProcess);
        processes.remove(currentProcess);
        currentProcess.setEndTime(currentTime);
        printOutput(currentProcess, startTime, currentProcess.endTime);
    }

    private void handleQuantumFinished(Process currentProcess) {
        currentProcess.setQuantum(currentProcess.getQuantum() + ((int) Math.ceil(0.1 * meanOfQuantum(processes, currentTime))));
        processTime = 0;
        currentProcess.setEndTime(currentTime);
        readyQueue.add(currentProcess);
        printOutput(currentProcess, startTime, currentProcess.endTime);
    }

    private void handleNewProcess(Process newProcess, Process currentProcess) {
        currentProcess.setQuantum(currentProcess.getQuantum() + (currentProcess.getQuantum() - processTime));
        readyQueue.add(currentProcess);
        currentProcess.setEndTime(currentTime);
        processTime = 0;
        printOutput(currentProcess, startTime, currentProcess.endTime);
        executeNonPreemptive(newProcess);
    }

    //---------------------------------------------------NonPreemptive---------------------------------------------------------
    private void executeNonPreemptive(Process currentProcess) {
        int halfQuantum = calculateHalfQuantum(currentProcess);
        startTime = currentTime;
        currentTime += halfQuantum;
        processTime = halfQuantum;
        currentProcess.setBurstTime(currentProcess.getBurstTime() - halfQuantum);

        if (isExecutionFinished(currentProcess)) {
            handleExecutionCompletion(currentProcess);
        } else {
            executePreemptive(currentProcess);
        }
    }

    private int calculateHalfQuantum(Process currentProcess) {
        return Math.min((int) Math.ceil(currentProcess.getQuantum() / 2.0), currentProcess.getBurstTime());
    }

    private void handleExecutionCompletion(Process currentProcess) {
        processes.remove(currentProcess);
        deadList.add(currentProcess);
        currentProcess.setQuantum(0);
        updateTurnaroundAndWaitingTimes(currentProcess);
        currentProcess.setEndTime(currentTime);
        printOutput(currentProcess, startTime, currentProcess.endTime);
    }

    //----------------------------------------------------------------------------------------------------------------------
    private void printQuantumHistory() {
        System.out.println("*****************************************************************");
        System.out.println("######################## Quantum History ########################");
        for (String entry : quantumHistory) {
            System.out.println(entry);
        }
    }
    private boolean isQuantumFinished(Process currentProcess, int processTime) {
        return processTime == currentProcess.quantum;
    }
    private void removeFromQueue(Process p) {
        if (!readyQueue.isEmpty()) {
            System.out.println("Displaying process in queue: " + readyQueue.element().name);
        }
        readyQueue.removeIf(process -> process != p);
        if (!readyQueue.isEmpty()) {
            System.out.println("Updated queue with process: " + readyQueue.element().name);
        }
    }
    private Process findNewProcess(Process currentProcess) {
        int maxAG = currentProcess.getAGFactor();
        int index = -1;
        for (int i = 0; i < processes.size(); i++) {
            if (processes.get(i).arrivalTime <= currentTime && processes.get(i).getAGFactor() < maxAG) {
                index = i;
                maxAG = processes.get(i).getAGFactor();
            }
        }
        return (index == -1) ? null : handleNewProcess(processes.get(index));
    }
    private Process handleNewProcess(Process process) {
        removeFromQueue(process);
        return process;
    }
    private boolean isExecutionFinished(Process currentProcess) {
        return currentProcess.burstTime == 0;
    }
    private int calculateTurnaroundTime(int arrivalTime) {
        return currentTime - arrivalTime;
    }
    private int calculateWaitingTime(int turnaroundTime, int initialBurstTime) {
        return turnaroundTime - initialBurstTime;
    }
    private void updateTurnaroundAndWaitingTimes(Process currentProcess) {
        int arrivalTime = currentProcess.arrivalTime;
        int initialBurstTime = currentProcess.initialBurstTime;

        int turnaroundTime = calculateTurnaroundTime(arrivalTime);
        int waitingTime = calculateWaitingTime(turnaroundTime, initialBurstTime);

        turnAroundTimeMap.put(currentProcess, turnaroundTime);
        waitingTimeMap.put(currentProcess, waitingTime);
    }
    private double calculateAverage(Map<Process, Integer> map) {
        return map.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }
    private void printOutput(Process currentProcess, int startTime, int endTime) {
        System.out.println("######################## PROCESS DETAILS ########################");
        System.out.println("Name: " + currentProcess.name);
        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);
        System.out.println("Quantum: " + currentProcess.getQuantum());
        System.out.println("*****************************************************************");
        quantumHistory.add("Process name -> " + currentProcess.name);
        quantumHistory.add("Remaining Quantum -> " + currentProcess.getQuantum());
    }
    private void printTurnaroundAndWaitingTimes() {
        System.out.println("*****************************************************************");
        System.out.println("######## Waiting Time & Turnaround Time for Each Process ########");
        for (Map.Entry<Process, Integer> entry : turnAroundTimeMap.entrySet()) {
            Process process = entry.getKey();
            int turnaroundTime = entry.getValue();
            int waitingTime = turnaroundTime - process.getInitialBurstTime();
            System.out.println("Process name: " + process.name);
            System.out.println("Process Turnaround time: " + turnaroundTime);
            System.out.println("Process Waiting time: " + waitingTime);
            System.out.println("*****************************************************************");
        }
        double avgWaitingTime = calculateAverage(waitingTimeMap);
        double avgTurnaroundTime = calculateAverage(turnAroundTimeMap);
        System.out.println("Average waiting time: " + avgWaitingTime);
        System.out.println("Average turn around: " + avgTurnaroundTime);
        System.out.println("*****************************************************************");
    }
    public void printTable() {
        System.out.println("process\tBurst Time\tArrival Time\tPriority\tQuantum\t\tRandom Function\tAG Factor");
        for (Process p : processes) {
            System.out.printf("%s\t\t%d\t\t\t%d\t\t\t\t%d\t\t\t%d\t\t\t%d\t\t\t\t\t%d%n",
                    p.name, p.burstTime, p.arrivalTime, p.priority, quantum, p.getRandom(), p.getAGFactor());
        }
        System.out.println("********************************************************************************************");
        System.out.println("********************************************************************************************");
    }
}
