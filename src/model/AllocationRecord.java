package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AllocationRecord {

    private String studentId;
    private int roomNumber;
    private String action;
    private LocalDateTime timestamp;

    // Constructor
    public AllocationRecord(String studentId, int roomNumber, String action) {
        this.studentId = studentId;
        this.roomNumber = roomNumber;
        this.action = action;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor used when reloading a record from a saved history file,
    // where the original timestamp must be preserved instead of "now".
    public AllocationRecord(String studentId, int roomNumber, String action, LocalDateTime timestamp) {
        this.studentId = studentId;
        this.roomNumber = roomNumber;
        this.action = action;
        this.timestamp = timestamp;
    }

    // Getters

    public String getStudentId() {
        return studentId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getAction() {
        return action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return "Student ID : " + studentId +
                "\nRoom No.   : " + roomNumber +
                "\nAction     : " + action +
                "\nTime       : " + timestamp.format(formatter);
    }
}