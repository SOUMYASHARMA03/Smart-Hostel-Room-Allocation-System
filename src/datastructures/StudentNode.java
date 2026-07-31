package datastructures;

import model.Student;

public class StudentNode {

    private Student student;
    private StudentNode next;
    private StudentNode previous;

    // Constructor
    public StudentNode(Student student) {
        this.student = student;
        this.next = null;
        this.previous = null;
    }

    // Getters

    public Student getStudent() {
        return student;
    }

    public StudentNode getNext() {
        return next;
    }

    public StudentNode getPrevious() {
        return previous;
    }

    // Setters

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setNext(StudentNode next) {
        this.next = next;
    }

    public void setPrevious(StudentNode previous) {
        this.previous = previous;
    }
}