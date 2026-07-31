package model;

import java.util.ArrayList;

public class Room {

    // Room Details
    private int roomNumber;
    private String block;
    private int floor;
    private int capacity;
    private int occupiedBeds;
    private boolean acRoom;

    private ArrayList<String> occupants;

    // Constructor
    public Room(int roomNumber, String block, int floor,
                int capacity, boolean acRoom) {

        this.roomNumber = roomNumber;
        this.block = block;
        this.floor = floor;
        this.capacity = capacity;
        this.acRoom = acRoom;
        this.occupiedBeds = 0;
        this.occupants = new ArrayList<>();
    }

    // Getters

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getBlock() {
        return block;
    }

    public int getFloor() {
        return floor;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getOccupiedBeds() {
        return occupiedBeds;
    }

    public boolean isAcRoom() {
        return acRoom;
    }

    // Room Status

    public boolean isAvailable() {
        return occupiedBeds < capacity;
    }

    public boolean isFull() {
        return occupiedBeds >= capacity;
    }

   // Add Student to Room
public boolean addOccupant(String studentId) {

    if (isAvailable()) {
        occupants.add(studentId);
        occupiedBeds++;
        return true;
    }

    return false;
}

// Remove Student from Room
public boolean removeOccupant(String studentId) {

    if (occupants.remove(studentId)) {
        occupiedBeds--;
        return true;
    }

    return false;
}

// Get Occupants
public ArrayList<String> getOccupants() {
    return occupants;
}
    // Display Room Details

    @Override
    public String toString() {

        String status = isAvailable() ? "Available" : "Full";

        return "Room Number   : " + roomNumber +
                "\nBlock        : " + block +
                "\nFloor        : " + floor +
                "\nCapacity     : " + capacity +
                "\nOccupied     : " + occupiedBeds +
                "\nAC Room      : " + (acRoom ? "Yes" : "No") +
                "\nStatus       : " + status +
                "\nOccupants    : " + occupants;
    }
}