package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import datastructures.AllocationHistory;
import datastructures.StudentLinkedList;
import datastructures.StudentNode;
import manager.RoomManager;
import model.AllocationRecord;
import model.Room;
import model.Student;

public class FileManager {

    // Save Student Records
    public static void saveStudents(StudentLinkedList studentList, String filePath) {

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            StudentNode current = studentList.getHead();

            while (current != null) {

                Student s = current.getStudent();

                writer.write(
                        s.getStudentId() + "," +
                        s.getRegistrationNumber() + "," +
                        s.getName() + "," +
                        s.getGender() + "," +
                        s.getYear() + "," +
                        s.getBranch() + "," +
                        s.getPhoneNumber() + "," +
                        s.getHostelPreference() + "," +
                        s.getFoodPreference() + "," +
                        s.isAcPreference() + "," +
                        s.getStudyHabit() + "," +
                        s.getCleanliness() + "," +
                        s.getAllocatedRoom() + "," +
                        s.getPriority() + "," +
                        s.isMedicalEmergency());

                writer.newLine();

                current = current.getNext();
            }

            writer.close();

            System.out.println("Students saved successfully.");

        } catch (IOException e) {

            System.out.println("Error saving students.");

        }

    }

    // Save Room Records
    public static void saveRooms(RoomManager roomManager, String filePath) {

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            for (Room room : roomManager.getAllRooms()) {

                writer.write(
                        room.getRoomNumber() + "," +
                        room.getBlock() + "," +
                        room.getFloor() + "," +
                        room.getCapacity() + "," +
                        room.getOccupiedBeds() + "," +
                        room.isAcRoom());

                writer.newLine();
            }

            writer.close();

            System.out.println("Rooms saved successfully.");

        } catch (IOException e) {

            System.out.println("Error saving rooms.");

        }

    }

    // Save Allocation History (chronological order, oldest first, matching
    // the Stack's internal push order so it can be replayed on load)
    public static void saveHistory(AllocationHistory history, String filePath) {

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            for (AllocationRecord record : history.toChronologicalList()) {

                writer.write(
                        record.getStudentId() + "," +
                        record.getRoomNumber() + "," +
                        record.getAction() + "," +
                        record.getTimestamp());

                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {

            System.out.println("Error saving allocation history.");

        }

    }

    // Load Allocation History back into a Stack, in the same order it was
    // saved, so the most recent action ends up back on top.
    public static void loadHistoryInto(AllocationHistory history, String filePath) {

        File file = new File(filePath);

        if (!file.exists()) {
            return;
        }

        try {

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split(",", 4);

                if (parts.length < 4) {
                    continue;
                }

                String studentId = parts[0];
                int roomNumber = Integer.parseInt(parts[1]);
                String action = parts[2];
                LocalDateTime timestamp = LocalDateTime.parse(parts[3]);

                history.pushRecord(new AllocationRecord(studentId, roomNumber, action, timestamp));
            }

            reader.close();

        } catch (IOException | NumberFormatException | java.time.format.DateTimeParseException e) {

            System.out.println("Error loading allocation history.");

        }

    }

    // Load Student Records back into a StudentLinkedList, in the same
    // order they were saved (registration order preserved).
    public static void loadStudentsInto(StudentLinkedList studentList, String filePath) {

        File file = new File(filePath);

        if (!file.exists()) {
            return;
        }

        try {

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] p = line.split(",", -1);

                if (p.length < 15) {
                    continue;
                }

                Student student = new Student(
                        p[0],                       // studentId
                        p[1],                       // registrationNumber
                        p[2],                       // name
                        p[3],                       // gender
                        Integer.parseInt(p[4]),     // year
                        p[5],                       // branch
                        p[6],                       // phoneNumber
                        p[7],                       // hostelPreference
                        p[8],                       // foodPreference
                        Boolean.parseBoolean(p[9]), // acPreference
                        p[10],                      // studyHabit
                        p[11],                      // cleanliness
                        Integer.parseInt(p[12]),    // allocatedRoom
                        Integer.parseInt(p[13]),    // priority
                        Boolean.parseBoolean(p[14])); // medicalEmergency

                studentList.addStudent(student);
            }

            reader.close();

        } catch (IOException | NumberFormatException e) {

            System.out.println("Error loading students.");

        }

    }

    // Create Data Folder
    public static void createDataFolder() {

        File folder = new File("data");

        if (!folder.exists()) {
            folder.mkdir();
        }

    }

}