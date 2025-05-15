import java.util.*;

public class Teacher implements User {
    public String name;
    public int id;
    private List<Subject> subjects;

    public Teacher(int id, String name) {
        this.id = id;
        this.name = name;
        this.subjects = new ArrayList<>();
    }

    public Teacher() {
        this.id = 0;
        this.name = "noname";
        this.subjects = new ArrayList<>();
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
        System.out.println("Success!");
    }

    public void addSubjectADMIN(Subject subject) {
        subjects.add(subject);
    }

    public void removeSubject(Subject subject) {
        subjects.remove(subject);
        System.out.println("Success!");
    }

    public void addStudentToSubject(Subject subject, Student student) {
        if (subjects.contains(subject) && !subject.contains(student)) {
            subject.addStudent(student);
            System.out.println("Success!");
        } else {
            System.out.println("Error: Teacher doesn't teach this subject or student is already enrolled");
        }
    }

    public void assignScore(Subject subject, Student student, int score) {
        if (subjects.contains(subject) && subject.contains(student)) {
            subject.addScore(this, student, score);
            System.out.println("Success!");
        } else {
            System.out.println("Error: Teacher doesn't teach this subject or student isn't enrolled");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getID() {
        return id;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void showStudentScores(String name) {
        System.out.println(name + "'s scores in subjects:");
        boolean studentFound = false;

        for (Subject subject : subjects) {
            for (Map.Entry<Student, List<Integer>> entry : subject.getScores().entrySet()) {
                Student student = entry.getKey();
                if (student.getName().equalsIgnoreCase(name)) {
                    studentFound = true;
                    List<Integer> scores = entry.getValue();
                    System.out.println("Subject: " + subject.getName() + " - Scores: " + scores);
                }
            }
        }

        if (!studentFound) {
            System.out.println("Student " + name + " not found in teacher " + this.name + "'s subjects");
        }
    }

    public void showStudentScores(String name, Subject subject) {
        /*
        if (!subjects.contains(subject)) {
            System.out.println("Error: Teacher " + this.name + " doesn't teach " + subject.getName());
            return;
        }
        */

        System.out.println(name + "'s scores in " + subject.getName() + ":");
        boolean studentFound = false;

        for (Map.Entry<Student, List<Integer>> entry : subject.getScores().entrySet()) {
            Student student = entry.getKey();
            if (student.getName().equalsIgnoreCase(name)) {
                studentFound = true;
                List<Integer> scores = entry.getValue();
                System.out.println("Scores: " + scores);
                break;
            }
        }

        if (!studentFound) {
            System.out.println("Student " + name + " not found in " + subject.getName());
        }
    }

    @Override
    public void showAllScores() {
        System.out.println("All students' scores:");
        for (Subject subject : subjects) {
            System.out.println("Subject: " + subject.getName());
            for (Map.Entry<Student, List<Integer>> entry : subject.getScores().entrySet()) {
                Student student = entry.getKey();
                List<Integer> scores = entry.getValue();
                System.out.println("Student: " + student.getName() + " - Scores: " + scores);
            }
            System.out.println();
        }
    }

    @Override
    public void showScores(String subjectName) {
        for (Subject subject : subjects) {
            if (subject.getName().equalsIgnoreCase(subjectName)) {
                System.out.println("Scores for " + subjectName + ":");
                for (Map.Entry<Student, List<Integer>> entry : subject.getScores().entrySet()) {
                    Student student = entry.getKey();
                    List<Integer> scores = entry.getValue();
                    System.out.println("Student: " + student.getName() + " - Scores: " + scores);
                }
                return;
            }
        }
        System.out.println("Subject " + subjectName + " not taught by teacher " + name);
    }
}