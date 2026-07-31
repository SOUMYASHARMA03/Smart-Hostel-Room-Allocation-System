package manager;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import model.Student;

public class WaitingListManager {

    // Priority Queue
    private PriorityQueue<Student> waitingQueue;

    // Constructor
    public WaitingListManager() {

        waitingQueue = new PriorityQueue<>(new StudentPriorityComparator());

    }

    // Add Student to Waiting List
    public void addStudent(Student student) {

        if (student != null) {
            waitingQueue.offer(student);
        }

    }

    // Remove Highest Priority Student
    public Student removeStudent() {

        if (waitingQueue.isEmpty()) {
            return null;
        }

        return waitingQueue.poll();

    }

    // Peek Highest Priority Student
    public Student peekStudent() {

        if (waitingQueue.isEmpty()) {
            return null;
        }

        return waitingQueue.peek();

    }

    // Check Empty
    public boolean isEmpty() {

        return waitingQueue.isEmpty();

    }

    // Waiting Student Count
    public int getWaitingCount() {

        return waitingQueue.size();

    }

    // Clear Waiting List
    public void clearWaitingList() {

        waitingQueue.clear();

    }

    // Get All Waiting Students (For JTable)
    public List<Student> getWaitingStudents() {

        return new ArrayList<>(waitingQueue);

    }

    // Display Waiting List
    public void displayWaitingList() {

        if (waitingQueue.isEmpty()) {

            System.out.println("Waiting List is Empty.");

            return;

        }

        System.out.println("========= WAITING LIST =========");

        for (Student student : waitingQueue) {

            System.out.println(student);

            System.out.println("--------------------------------");

        }

    }

}