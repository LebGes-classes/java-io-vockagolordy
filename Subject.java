import java.util.*;

public class Subject {
    private String name;
    private String description;
    private Map<Student, List<Integer>> studentScores; // Хранит студентов и их оценки

    public Subject(String name, String description) {
        this.name = name;
        this.description = description;
        this.studentScores = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean contains(Student student) {
        return studentScores.containsKey(student);
    }

    public Map<Student, List<Integer>> getScores() {
        return studentScores;
    }

    public List<Integer> getStudentScores(Student student) {
        return studentScores.getOrDefault(student, new ArrayList<>());
    }

    public void addStudent(Student student) {
        studentScores.putIfAbsent(student, new ArrayList<>());
    }

    public void addScore(Teacher teacher, Student student, int score) {
        if (isTeacherAuthorized(teacher)) {
            studentScores.get(student).add(score);
        } else {
            System.out.println("You do not have rights to apply changes.");
        }
    }

    public void addScoreADMIN(Student student, int score) {
        studentScores.get(student).add(score);
    }

    private boolean isTeacherAuthorized(Teacher teacher) {
        return teacher.getSubjects().contains(this);
    }
}
