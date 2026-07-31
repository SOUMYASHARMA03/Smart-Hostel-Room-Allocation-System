package model;

public class Student {

    // Student Details
    private String studentId;
    private String registrationNumber;
    private String name;
    private String gender;
    private int year;
    private String branch;
    private String phoneNumber;

    // Hostel Preferences
    private String hostelPreference;
    private String foodPreference;
    private boolean acPreference;
    private String studyHabit;
    private String cleanliness;

    // Allocation Details
    private int allocatedRoom;
    private boolean roomAllocated;

    private int priority;
    private boolean medicalEmergency;

    // Constructor
    public Student(String studentId,
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
                   int allocatedRoom,
                   int priority,
                   boolean medicalEmergency) {

        this.studentId = studentId;
        this.registrationNumber = registrationNumber;
        this.name = name;
        this.gender = gender;
        this.year = year;
        this.branch = branch;
        this.phoneNumber = phoneNumber;

        this.hostelPreference = hostelPreference;
        this.foodPreference = foodPreference;
        this.acPreference = acPreference;
        this.studyHabit = studyHabit;
        this.cleanliness = cleanliness;

        this.allocatedRoom = allocatedRoom;
        this.roomAllocated = allocatedRoom != -1;

        this.priority = priority;
        this.medicalEmergency = medicalEmergency;
    }

    // ---------------------------------------------------------------------
    
    
        // ==========================
    // Getters
    // ==========================

    public String getStudentId() {
        return studentId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getYear() {
        return year;
    }

    public String getBranch() {
        return branch;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getHostelPreference() {
        return hostelPreference;
    }

    public String getFoodPreference() {
        return foodPreference;
    }

    public boolean isAcPreference() {
        return acPreference;
    }

    public String getStudyHabit() {
        return studyHabit;
    }

    public String getCleanliness() {
        return cleanliness;
    }

    public int getAllocatedRoom() {
        return allocatedRoom;
    }

    public boolean isRoomAllocated() {
        return roomAllocated;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isMedicalEmergency() {
        return medicalEmergency;
    }
    
    
    // ----------------------------------------------------------------------------
    
    
        // ==========================
    // Setters
    // ==========================

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setHostelPreference(String hostelPreference) {
        this.hostelPreference = hostelPreference;
    }

    public void setFoodPreference(String foodPreference) {
        this.foodPreference = foodPreference;
    }

    public void setAcPreference(boolean acPreference) {
        this.acPreference = acPreference;
    }

    public void setStudyHabit(String studyHabit) {
        this.studyHabit = studyHabit;
    }

    public void setCleanliness(String cleanliness) {
        this.cleanliness = cleanliness;
    }

    public void setAllocatedRoom(int allocatedRoom) {
        this.allocatedRoom = allocatedRoom;
    }

    public void setRoomAllocated(boolean roomAllocated) {
        this.roomAllocated = roomAllocated;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setMedicalEmergency(boolean medicalEmergency) {
        this.medicalEmergency = medicalEmergency;
    }

    // ==========================
    // Display Student Details
    // ==========================

    @Override
    public String toString() {

        return "Student ID       : " + studentId +
                "\nRegistration No : " + registrationNumber +
                "\nName            : " + name +
                "\nGender          : " + gender +
                "\nYear            : " + year +
                "\nBranch          : " + branch +
                "\nPhone Number    : " + phoneNumber +
                "\nHostel          : " + hostelPreference +
                "\nFood            : " + foodPreference +
                "\nAC Preference   : " + (acPreference ? "Yes" : "No") +
                "\nStudy Habit     : " + studyHabit +
                "\nCleanliness     : " + cleanliness +
                "\nAllocated Room  : " +
                (roomAllocated ? allocatedRoom : "Not Allocated") +
                "\nPriority        : " + priority +
                "\nMedical Case    : " +
                (medicalEmergency ? "Yes" : "No");
    }

}
    
    
    
    
    
    
    
    