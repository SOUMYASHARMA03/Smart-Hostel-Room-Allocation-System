package service;

import model.Room;
import model.Student;

// Scores how well a student fits an existing room, and how compatible
// two students would be as roommates, using their lifestyle preferences.
public class RoommateMatcher {

    // Score a student against an already-occupied room, using one
    // representative occupant (the first one found) as the comparison point.
    // Returns 0-100. A room with free beds but no occupants yet scores
    // a neutral baseline so it can still be picked.
    public int calculateRoomCompatibility(Student student, Room room, Student sampleOccupant) {

        if (student == null || room == null) {
            return 0;
        }

        if (sampleOccupant == null) {
            // Empty / new room: only AC preference match matters
            return room.isAcRoom() == student.isAcPreference() ? 70 : 40;
        }

        return calculateStudentCompatibility(student, sampleOccupant);
    }

    // Score compatibility between two students, 0-100.
    public int calculateStudentCompatibility(Student a, Student b) {

        if (a == null || b == null) {
            return 0;
        }

        int score = 0;

        // Food preference (25 pts)
        if (a.getFoodPreference() != null &&
                a.getFoodPreference().equalsIgnoreCase(b.getFoodPreference())) {
            score += 25;
        }

        // Study habit (25 pts)
        if (a.getStudyHabit() != null &&
                a.getStudyHabit().equalsIgnoreCase(b.getStudyHabit())) {
            score += 25;
        }

        // Cleanliness (25 pts)
        if (a.getCleanliness() != null &&
                a.getCleanliness().equalsIgnoreCase(b.getCleanliness())) {
            score += 25;
        }

        // AC preference (15 pts)
        if (a.isAcPreference() == b.isAcPreference()) {
            score += 15;
        }

        // Same academic year is a mild bonus (10 pts)
        if (a.getYear() == b.getYear()) {
            score += 10;
        }

        return score;
    }

    public String compatibilityLabel(int score) {

        if (score >= 80) return "Excellent Match";
        if (score >= 60) return "Good Match";
        if (score >= 40) return "Fair Match";
        return "Low Match";
    }
}
