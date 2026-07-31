package manager;

import java.util.Comparator;
import model.Student;

public class StudentPriorityComparator implements Comparator<Student> {

    @Override
    public int compare(Student s1, Student s2) {

        // Medical emergency gets highest priority
        if (s1.isMedicalEmergency() && !s2.isMedicalEmergency())
            return -1;

        if (!s1.isMedicalEmergency() && s2.isMedicalEmergency())
            return 1;

        // Higher academic year gets higher priority
        if (s1.getYear() != s2.getYear()) {
            return Integer.compare(s2.getYear(), s1.getYear());
        }

        // Lower priority value gets preference (registration order)
        return Integer.compare(s1.getPriority(), s2.getPriority());
    }
}