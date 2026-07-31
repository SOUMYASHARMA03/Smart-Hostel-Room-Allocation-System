package service;

import java.util.List;

import datastructures.AllocationHistory;
import datastructures.StudentLinkedList;
import manager.RoomManager;
import manager.WaitingListManager;
import model.AllocationRecord;
import model.Room;
import model.Student;
import util.FileManager;

public class HostelService {

    // Singleton Instance
    private static HostelService instance;

    private static final String HISTORY_FILE = "data/history.txt";
    private static final String STUDENTS_FILE = "data/students.txt";

    private StudentLinkedList studentList;
    private RoomManager roomManager;
    private WaitingListManager waitingListManager;
    private AllocationHistory allocationHistory;
    private RoommateMatcher roommateMatcher;

    private int studentCounter;

    // Constructor
    private HostelService() {

        studentList = new StudentLinkedList();
        roomManager = new RoomManager();
        waitingListManager = new WaitingListManager();
        allocationHistory = new AllocationHistory();
        roommateMatcher = new RoommateMatcher();

        studentCounter = 1;

        FileManager.createDataFolder();
        FileManager.loadHistoryInto(allocationHistory, HISTORY_FILE);

        FileManager.loadStudentsInto(studentList, STUDENTS_FILE);
        rebuildRoomOccupancyFromStudents();
        advanceCounterPastLoadedStudents();
    }

    // Re-applies each loaded student's saved room allocation onto the
    // freshly-initialized RoomManager, so occupancy matches the students
    // file exactly (the rooms themselves are always regenerated fresh;
    // only who-is-in-which-room needs to be replayed).
    private void rebuildRoomOccupancyFromStudents() {

        for (Student student : studentList.toList()) {

            if (student.isRoomAllocated()) {
                roomManager.allocateStudent(student.getAllocatedRoom(), student.getStudentId());
            }
        }
    }

    // Ensures newly generated IDs (STxxx) never collide with IDs already
    // loaded from disk.
    private void advanceCounterPastLoadedStudents() {

        for (Student student : studentList.toList()) {

            String id = student.getStudentId();

            if (id != null && id.startsWith("ST")) {

                try {
                    int number = Integer.parseInt(id.substring(2));
                    if (number >= studentCounter) {
                        studentCounter = number + 1;
                    }
                } catch (NumberFormatException ignored) {
                    // Non-standard ID format, skip
                }
            }
        }
    }

    // Save the current student list to disk (called after every action
    // that adds, removes, or changes a student's allocation, so the
    // registry survives an app restart)
    private void persistStudents() {
        FileManager.saveStudents(studentList, STUDENTS_FILE);
    }

    // Save the current history stack to disk (called after every action
    // that changes it, so history survives an app restart)
    private void persistHistory() {
        FileManager.saveHistory(allocationHistory, HISTORY_FILE);
    }

    // Singleton Object
    public static HostelService getInstance() {

        if (instance == null) {
            instance = new HostelService();
        }

        return instance;
    }

    // Generate Student ID
    public String generateStudentId() {
        return String.format("ST%03d", studentCounter++);
    }

    // Register Student
    public Student registerStudent(
            String registrationNumber,
            String name,
            String gender,
            int year,
            String branch,
            String phoneNumber,
            String hostelPreference,
            String foodPreference,
            boolean acPreference,
            String studyHabit,
            String cleanliness,
            boolean medicalEmergency) {

        Student student = new Student(
                generateStudentId(),
                registrationNumber,
                name,
                gender,
                year,
                branch,
                phoneNumber,
                hostelPreference,
                foodPreference,
                acPreference,
                studyHabit,
                cleanliness,
                -1,
                studentCounter,
                medicalEmergency);

        studentList.addStudent(student);

        persistStudents();

        return student;
    }

    // Search Student
    public Student searchStudent(String studentId) {
        return studentList.searchStudentById(studentId);
    }

    // Remove Student
    public boolean removeStudent(String studentId) {

        Student student = studentList.searchStudentById(studentId);

        if (student != null && student.isRoomAllocated()) {
            vacateRoom(studentId);
        }

        boolean removed = studentList.removeStudent(studentId);

        if (removed) {
            persistStudents();
        }

        return removed;
    }

    // Update Phone Number
    public boolean updateStudentPhone(String studentId, String newPhone) {
        boolean updated = studentList.updatePhoneNumber(studentId, newPhone);

        if (updated) {
            persistStudents();
        }

        return updated;
    }

    // Allocate Best Available Room (by AC preference only)
    public boolean allocateRoom(String studentId) {

        Student student = studentList.searchStudentById(studentId);

        if (student == null || student.isRoomAllocated()) {
            return false;
        }

        Room room = roomManager.findAvailableRoom(student.isAcPreference());

        if (room == null) {
            waitingListManager.addStudent(student);
            return false;
        }

        return commitAllocation(student, room, "ALLOCATED");
    }

    // Smart Allocate: picks the available room whose current occupants are
    // the best lifestyle match for this student (falls back to any
    // available room, then to the waiting list).
    public boolean smartAllocateRoom(String studentId) {

        Student student = studentList.searchStudentById(studentId);

        if (student == null || student.isRoomAllocated()) {
            return false;
        }

        Room best = findBestRoom(student);

        if (best == null) {
            waitingListManager.addStudent(student);
            return false;
        }

        return commitAllocation(student, best, "SMART ALLOCATED");
    }

    // Allocate Specific Room
    public boolean allocateSpecificRoom(String studentId, int roomNumber) {

        Student student = studentList.searchStudentById(studentId);

        if (student == null || student.isRoomAllocated()) {
            return false;
        }

        if (!roomManager.roomExists(roomNumber)) {
            return false;
        }

        Room room = roomManager.searchRoom(roomNumber);

        return commitAllocation(student, room, "ALLOCATED");
    }

    private boolean commitAllocation(Student student, Room room, String action) {

        boolean allocated = roomManager.allocateStudent(room.getRoomNumber(), student.getStudentId());

        if (allocated) {
            student.setAllocatedRoom(room.getRoomNumber());
            student.setRoomAllocated(true);

            allocationHistory.pushRecord(
                    new AllocationRecord(student.getStudentId(), room.getRoomNumber(), action));

            persistHistory();
            persistStudents();
        }

        return allocated;
    }

    // Vacate Allocated Room
    public boolean vacateRoom(String studentId) {

        Student student = studentList.searchStudentById(studentId);

        if (student == null || !student.isRoomAllocated()) {
            return false;
        }

        int roomNumber = student.getAllocatedRoom();

        boolean removed = roomManager.vacateStudent(roomNumber, studentId);

        if (removed) {

            student.setAllocatedRoom(-1);
            student.setRoomAllocated(false);

            allocationHistory.pushRecord(
                    new AllocationRecord(studentId, roomNumber, "VACATED"));

            persistHistory();
            persistStudents();

            processWaitingList();

            return true;
        }

        return false;
    }

    // Process Waiting List: try to seat the highest-priority waiting
    // student into any room that just freed up.
    public void processWaitingList() {

        if (waitingListManager.isEmpty()) {
            return;
        }

        Student student = waitingListManager.peekStudent();

        if (student == null) {
            return;
        }

        Room room = roomManager.findAvailableRoom(student.isAcPreference());

        if (room == null) {
            return;
        }

        student = waitingListManager.removeStudent();

        if (student == null) {
            return;
        }

        boolean allocated = roomManager.allocateStudent(room.getRoomNumber(), student.getStudentId());

        if (allocated) {

            student.setAllocatedRoom(room.getRoomNumber());
            student.setRoomAllocated(true);

            allocationHistory.pushRecord(
                    new AllocationRecord(student.getStudentId(), room.getRoomNumber(),
                            "ALLOCATED FROM WAITING LIST"));

            persistHistory();
            persistStudents();
        }
    }

    // Undo the most recent allocation/vacate action from the history stack
    public AllocationRecord undoLastAction() {

        AllocationRecord record = allocationHistory.peekLastRecord();

        if (record == null) {
            return null;
        }

        Student student = studentList.searchStudentById(record.getStudentId());

        if (student == null) {
            AllocationRecord popped = allocationHistory.undoLastRecord();
            persistHistory();
            return popped;
        }

        if (record.getAction().startsWith("ALLOCATED")) {
            // Undo an allocation -> put the student back to unallocated
            roomManager.vacateStudent(record.getRoomNumber(), record.getStudentId());
            student.setAllocatedRoom(-1);
            student.setRoomAllocated(false);
        } else if (record.getAction().equals("VACATED")) {
            // Undo a vacate -> put the student back in that room, if free
            if (roomManager.allocateStudent(record.getRoomNumber(), record.getStudentId())) {
                student.setAllocatedRoom(record.getRoomNumber());
                student.setRoomAllocated(true);
            }
        }

        AllocationRecord popped = allocationHistory.undoLastRecord();
        persistHistory();
        persistStudents();
        return popped;
    }

    // Find Best Room for a student using the RoommateMatcher, among rooms
    // that still have a free bed. Prefers rooms that already have a good
    // lifestyle-compatible occupant, then falls back to matching AC
    // preference, then to any available room.
    public Room findBestRoom(Student student) {

        if (student == null) {
            return null;
        }

        Room best = null;
        int bestScore = -1;

        for (Room room : roomManager.getAllRooms()) {

            if (room.isFull()) {
                continue;
            }

            Student sampleOccupant = null;

            if (!room.getOccupants().isEmpty()) {
                sampleOccupant = studentList.searchStudentById(room.getOccupants().get(0));
            }

            int score = roommateMatcher.calculateRoomCompatibility(student, room, sampleOccupant);

            if (score > bestScore) {
                bestScore = score;
                best = room;
            }
        }

        return best;
    }

    // Display Students
    public void displayAllStudents() {
        studentList.displayAllStudents();
    }

    public void displayStudentsReverse() {
        studentList.displayStudentsReverse();
    }

    // Dashboard Statistics
    public int getTotalStudents() {
        return studentList.getTotalStudents();
    }

    public int getTotalRooms() {
        return roomManager.getTotalRooms();
    }

    public int getAvailableRooms() {
        return roomManager.getAvailableRoomCount();
    }

    public int getOccupiedRooms() {
        return roomManager.getOccupiedRoomCount();
    }

    public int getWaitingStudents() {
        return waitingListManager.getWaitingCount();
    }

    public List<Student> getAllStudents() {
        return studentList.toList();
    }

    // Getters
    public StudentLinkedList getStudentList() {
        return studentList;
    }

    public RoomManager getRoomManager() {
        return roomManager;
    }

    public WaitingListManager getWaitingListManager() {
        return waitingListManager;
    }

    public AllocationHistory getAllocationHistory() {
        return allocationHistory;
    }

    public RoommateMatcher getRoommateMatcher() {
        return roommateMatcher;
    }
}
