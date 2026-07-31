//-------------DOUBLY LINKED LIST IMPLEMENTATION FOR STUDENT RECORDS------------------


package datastructures;

import java.util.ArrayList;
import java.util.List;

import model.Student;

public class StudentLinkedList {

    private StudentNode head;
    private StudentNode tail;
    private int size;

    // Constructor
    public StudentLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // Check if list is empty
    public boolean isEmpty() {
        return head == null;
    }

    // Return total students
    public int getTotalStudents() {
        return size;
    }

    // Add student at end
    public void addStudent(Student student) {

        StudentNode newNode = new StudentNode(student);

        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrevious(tail);
            tail = newNode;
        }

        size++;
    }

    // Display all students
    public void displayAllStudents() {

        if (isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        StudentNode current = head;

        while (current != null) {
            System.out.println(current.getStudent());
            System.out.println("--------------------------------");
            current = current.getNext();
        }
    }

    // Display in reverse
    public void displayStudentsReverse() {

        if (isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        StudentNode current = tail;

        while (current != null) {
            System.out.println(current.getStudent());
            System.out.println("--------------------------------");
            current = current.getPrevious();
        }
    }

    // Search by Student ID
    public Student searchStudentById(String studentId) {

        StudentNode current = head;

        while (current != null) {

            if (current.getStudent().getStudentId().equalsIgnoreCase(studentId)) {
                return current.getStudent();
            }

            current = current.getNext();
        }

        return null;
    }

    // Remove student
    public boolean removeStudent(String studentId) {

        StudentNode current = head;

        while (current != null) {

            if (current.getStudent().getStudentId().equalsIgnoreCase(studentId)) {

                // Only one node
                if (head == tail) {
                    head = tail = null;
                }

                // First node
                else if (current == head) {
                    head = head.getNext();
                    head.setPrevious(null);
                }

                // Last node
                else if (current == tail) {
                    tail = tail.getPrevious();
                    tail.setNext(null);
                }

                // Middle node
                else {
                    current.getPrevious().setNext(current.getNext());
                    current.getNext().setPrevious(current.getPrevious());
                }

                size--;
                return true;
            }

            current = current.getNext();
        }

        return false;
    }

    // Update phone number
    public boolean updatePhoneNumber(String studentId, String newPhone) {

        Student student = searchStudentById(studentId);

        if (student != null) {
            student.setPhoneNumber(newPhone);
            return true;
        }

        return false;
    }

    // Get head (needed later)
    public StudentNode getHead() {
        return head;
    }

    // Snapshot of all students, traversal order (for UI tables/reports)
    public List<Student> toList() {

        List<Student> list = new ArrayList<>();
        StudentNode current = head;

        while (current != null) {
            list.add(current.getStudent());
            current = current.getNext();
        }

        return list;
    }
}