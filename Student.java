import java.util.*;

public class Student implements User {
    public String name;
    public int id;
    public int grade;
    public Map<Subject, List<Integer>> scores;

    public Student() {
        this.id = 0;
        this.name = "noname";
        this.grade = 0;
        this.scores = new HashMap<>();
    }

    public Student(int id, int grade, String name) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.scores = new HashMap<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(int grade) {
        this.grade = grade;

    }
    @Override
    public String getName() {
        return name;
    }

    public int getGrade(){
        return grade;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void showAllScores() {
        if (scores.isEmpty()) {
            System.out.println("There are no scores for " + name);
            return;
        }

        System.out.println("Scores for " + name + ":");
        for (Map.Entry<Subject, List<Integer>> entry : scores.entrySet()) {
            Subject subject = entry.getKey();
            List<Integer> subjectScores = entry.getValue();
            if (!subjectScores.isEmpty()) {
                System.out.println("Subject: " + subject.getName() + " - Scores: " + subjectScores);
            }
        }
    }

    @Override
    public void showScores(String subjectName) {
        for (Map.Entry<Subject, List<Integer>> entry : scores.entrySet()) {
            Subject subject = entry.getKey();
            if (subject.getName().equalsIgnoreCase(subjectName)) {
                List<Integer> scores = entry.getValue();
                System.out.println("Scores by subject " + subjectName + ": " + scores);
                return;
            }
        }
        System.out.println("There is no scores in " + subjectName + " for student " + name + ".");
    }

}
