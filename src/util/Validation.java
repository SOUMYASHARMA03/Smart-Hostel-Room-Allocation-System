package util;

public class Validation {

    // Validate Name
    public static boolean isValidName(String name) {

        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        return name.matches("[A-Za-z ]+");
    }

    // Validate Registration Number
    public static boolean isValidRegistrationNumber(String regNo) {

        return regNo != null && !regNo.trim().isEmpty();
    }

    // Validate Phone Number
    public static boolean isValidPhoneNumber(String phone) {

        return phone != null && phone.matches("\\d{10}");
    }

    // Validate Academic Year
    public static boolean isValidYear(int year) {

        return year >= 1 && year <= 4;
    }

    // Validate Branch
    public static boolean isValidBranch(String branch) {

        return branch != null && !branch.trim().isEmpty();
    }

    // Validate Hostel Preference
    public static boolean isValidHostelPreference(String hostel) {

        return hostel != null && !hostel.trim().isEmpty();
    }

    // Validate Food Preference
    public static boolean isValidFoodPreference(String food) {

        return food.equalsIgnoreCase("Veg")
                || food.equalsIgnoreCase("Non-Veg");
    }

    // Validate Study Habit
    public static boolean isValidStudyHabit(String habit) {

        return habit.equalsIgnoreCase("Early Bird")
                || habit.equalsIgnoreCase("Night Owl");
    }

    // Validate Cleanliness
    public static boolean isValidCleanliness(String cleanliness) {

        return cleanliness.equalsIgnoreCase("High")
                || cleanliness.equalsIgnoreCase("Medium")
                || cleanliness.equalsIgnoreCase("Low");
    }

    // Validate Complete Student Form
    public static boolean validateStudentData(
            String name,
            String regNo,
            String phone,
            int year,
            String branch,
            String hostel,
            String food,
            String studyHabit,
            String cleanliness) {

        return isValidName(name)
                && isValidRegistrationNumber(regNo)
                && isValidPhoneNumber(phone)
                && isValidYear(year)
                && isValidBranch(branch)
                && isValidHostelPreference(hostel)
                && isValidFoodPreference(food)
                && isValidStudyHabit(studyHabit)
                && isValidCleanliness(cleanliness);
    }

}