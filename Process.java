import java.util.List;
import java.util.Scanner;
import java.util.*;

class Process {
    String name;
    int arrivalTime;
    int AGFactor;

    int burstTime;
    int priority;
    int waitingTime;
    int turnaroundTime;
    int quantum;
    int remainingTime;

    int random;

    int endTime;
    int initialBurstTime;

    public Process(String name, int arrivalTime, int burstTime, int priority) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.quantum = 0;
        this.remainingTime = burstTime;
        setRandom();
        AGfactor();
    }
    public void printProcessDetails() {
        System.out.println("--------------------------------------");
        System.out.println("Process Name: " + name);
        System.out.println("Arrival Time: " + arrivalTime);
        System.out.println("Burst Time: " + burstTime);
        System.out.println("Waiting Time: " + waitingTime);
        System.out.println("Turnaround Time: " + turnaroundTime);
        System.out.println("--------------------------------------");
    }


    public static Process inputProcessDetails(Scanner scanner, int processNumber) {
        System.out.println("Enter details for process " + processNumber);
        System.out.print("Enter Process Name: ");
        String name = scanner.next();

        System.out.print("Enter Process Arrival Time: ");
        int arrivalTime = scanner.nextInt();

        System.out.print("Enter Process Burst Time: ");
        int burstTime = scanner.nextInt();

        System.out.print("Enter Process Priority Number: ");
        int priority = scanner.nextInt();

        return new Process(name, arrivalTime, burstTime, priority);
    }
    // New method to print average waiting time and average turnaround time
    public static void printAverageTimes(List<Integer> waitingTimes, List<Integer> turnaroundTimes) {
        double averageWaitingTime = calculateAverage(waitingTimes);
        double averageTurnaroundTime = calculateAverage(turnaroundTimes);

        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }

    public int random_function(){
        Random rand = new Random();
        int num = rand.nextInt(20);
        return num + 1 ;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void set_ag_factor(){
        int random = random_function();
        if(random < 10){
            setAGFactor(arrivalTime+burstTime+random);
        } else if (random==10) {
            setAGFactor(priority + arrivalTime + burstTime);
        }
        else{
            setAGFactor(10 + arrivalTime + burstTime);
        }
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getPriority() {
        return priority;
    }
    public String getName() {
        return name;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }


    private static double calculateAverage(List<Integer> values) {
        int sum = 0;
        for (int value : values) {
            sum += value;
        }
        return (double) sum / values.size();
    }
    public void incrementPriority() {
        // Increment priority with a limit to avoid overflow
        final int MAX_PRIORITY = 10;

        if (priority < MAX_PRIORITY) {
            priority++;
        }
    }

    public void reduceBurstTime(int time) {
        this.burstTime -= time;
    }
    public Integer getWaitingTime() {

        return waitingTime;
    }


    public int randFunction(){
        Random rand = new Random();
        int num = rand.nextInt(20);
        return num + 1 ;
    }
    public void AGfactor(){
        if(random < 10){
            setAGFactor(arrivalTime+burstTime+random);
        } else if (random==10) {
            setAGFactor(priority + arrivalTime + burstTime);
        }
        else{
            setAGFactor(10 + arrivalTime + burstTime);
        }
    }
    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
    public int getBurstTime() {
        return burstTime;
    }
    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }
    public int getAGFactor() {
        return AGFactor;
    }
    public void setAGFactor(int AGFactor) {
        this.AGFactor = AGFactor;
    }
    public int getQuantum() {
        return quantum;
    }
    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }
    public int getRandom() {
        return random;
    }
    public void setRandom() {
        this.random = randFunction();
    }
    public int getInitialBurstTime() {
        return initialBurstTime;
    }
    public void setInitialBurstTime(int initialBurstTime) {
        this.initialBurstTime = initialBurstTime;
    }


}



