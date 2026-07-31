package manager;

import java.util.Collection;
import java.util.HashMap;
import model.Room;

public class RoomManager {

    // HashMap<Room Number, Room Object>
    private HashMap<Integer, Room> rooms;

    // Constructor
    public RoomManager() {
        rooms = new HashMap<>();
        initializeRooms();
    }

    // Create Default Rooms
    private void initializeRooms() {

        // Floor 1 (101–110)
        for (int i = 101; i <= 110; i++) {
            rooms.put(i, new Room(i, "A", 1, 2, false));
        }

        // Floor 2 (201–210)
        for (int i = 201; i <= 210; i++) {
            rooms.put(i, new Room(i, "A", 2, 2, true));
        }

        // Floor 3 (301–310)
        for (int i = 301; i <= 310; i++) {
            rooms.put(i, new Room(i, "A", 3, 3, true));
        }
    }

    // Add New Room
    public void addRoom(Room room) {
        rooms.put(room.getRoomNumber(), room);
    }

    // Search Room
    public Room searchRoom(int roomNumber) {
        return rooms.get(roomNumber);
    }

    // Check if Room Exists
    public boolean roomExists(int roomNumber) {
        return rooms.containsKey(roomNumber);
    }

    // Remove Room
    public boolean removeRoom(int roomNumber) {

        if (rooms.containsKey(roomNumber)) {
            rooms.remove(roomNumber);
            return true;
        }

        return false;
    }

    // Find First Available Room According to AC Preference
    public Room findAvailableRoom(boolean acPreference) {

        for (Room room : rooms.values()) {

            if (room.isAcRoom() == acPreference && room.isAvailable()) {
                return room;
            }

        }

        return null;
    }

    // Allocate Student
    public boolean allocateStudent(int roomNumber, String studentId) {

        Room room = rooms.get(roomNumber);

        if (room != null) {
            return room.addOccupant(studentId);
        }

        return false;
    }

    // Vacate Student
    public boolean vacateStudent(int roomNumber, String studentId) {

        Room room = rooms.get(roomNumber);

        if (room != null) {
            return room.removeOccupant(studentId);
        }

        return false;
    }

    // Display All Rooms
    public void displayAllRooms() {

        for (Room room : rooms.values()) {
            System.out.println(room);
            System.out.println("--------------------------------");
        }

    }

    // Display Available Rooms
    public void displayAvailableRooms() {

        for (Room room : rooms.values()) {

            if (room.isAvailable()) {
                System.out.println(room);
                System.out.println("--------------------------------");
            }

        }

    }

    // Display Occupied Rooms
    public void displayOccupiedRooms() {

        for (Room room : rooms.values()) {

            if (!room.isAvailable()) {
                System.out.println(room);
                System.out.println("--------------------------------");
            }

        }

    }

    // Count Available Rooms
    public int getAvailableRoomCount() {

        int count = 0;

        for (Room room : rooms.values()) {

            if (room.isAvailable()) {
                count++;
            }

        }

        return count;
    }

    // Count Occupied Rooms
    public int getOccupiedRoomCount() {

        int count = 0;

        for (Room room : rooms.values()) {

            if (!room.isAvailable()) {
                count++;
            }

        }

        return count;
    }

    // Total Rooms
    public int getTotalRooms() {
        return rooms.size();
    }

    // Return All Rooms
    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

}