import java.util.*;
import java.util.stream.*;
import java.io.*;
import java.nio.file.*;
import org.json.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ClassLogBook {
    private List<Teacher> teachers;
    private List<Student> students;
    private List<Subject> subjects;
    private Scanner scanner;
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String DEFAULT_EXCEL_PATH = "C:\\Users\\User\\Documents\\GitHub\\java-io-vockagolordy\\Книга1.xlsx";
    private static final String DEFAULT_JSON_PATH = "C:\\Users\\User\\Documents\\GitHub\\java-io-vockagolordy\\logbook_data.json";

    public ClassLogBook() {
        this.teachers = new ArrayList<>();
        this.students = new ArrayList<>();
        this.subjects = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        showInitialMenu();
        //Thread.sleep(2000);
        showMainMenu();
    }

    private void showInitialMenu() {
        while (true) {
            System.out.println("\nInitialization Menu:");
            System.out.println("1 - Load data from Excel (" + DEFAULT_EXCEL_PATH + ")");
            System.out.println("2 - Load data from JSON (" + DEFAULT_JSON_PATH + ")");
            System.out.println("0 - Exit");
            System.out.print("Select option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    loadFromExcelTemplate(DEFAULT_EXCEL_PATH);
                    return;
                case 2:
                    deserializeFromJson(DEFAULT_JSON_PATH);
                    return;
                case 0:
                    System.out.println("Exiting application.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showMainMenu() {
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1 - Teacher");
            System.out.println("2 - Student");
            System.out.println("3 - Administrator");
            System.out.println("4 - Save data to JSON");
            System.out.println("0 - Exit");
            System.out.print("Select role: ");

            int role = scanner.nextInt();
            scanner.nextLine();

            switch (role) {
                case 1:
                    teacherMenu();
                    break;
                case 2:
                    studentMenu();
                    break;
                case 3:
                    if (!checkAdminPassword()) {
                        System.out.println("Access denied. Wrong password.");
                        continue;
                    }
                    adminMenu();
                    break;
                case 4:
                    serializeToJson(DEFAULT_JSON_PATH);
                    System.out.println("Data saved to: " + DEFAULT_JSON_PATH);
                    break;
                case 0:
                    System.out.println("Exiting application.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private boolean checkAdminPassword() {
        System.out.print("\nEnter administrator password: ");
        String inputPassword = scanner.nextLine();
        return ADMIN_PASSWORD.equals(inputPassword);
    }

    private void teacherMenu() {
        System.out.print("\nEnter teacher name: ");
        String teacherName = scanner.nextLine();
        Teacher teacher = findTeacher(teacherName);

        if (teacher == null) {
            System.out.println("Teacher not found.");
            return;
        }

        while (true) {
            System.out.println("\nTeacher Menu (" + teacher.getName() + "):");
            System.out.println("1 - Show all scores");
            System.out.println("2 - Show scores by subject");
            System.out.println("3 - Show specific student's scores");
            System.out.println("4 - Add score");
            System.out.println("5 - Add student to subject");
            System.out.println("0 - Return to main menu");
            System.out.print("Select action: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    teacher.showAllScores();
                    break;
                case 2:
                    System.out.print("Enter subject name: ");
                    String subjectName = scanner.nextLine();
                    teacher.showScores(subjectName);
                    break;
                case 3:
                    System.out.print("Enter student name: ");
                    String studentName = scanner.nextLine();
                    teacher.showStudentScores(studentName);
                    break;
                case 4:
                    System.out.print("Enter subject name: ");
                    String scoreSubject = scanner.nextLine();
                    Subject subject = findSubject(scoreSubject);
                    if (subject == null) {
                        System.out.println("Subject not found.");
                        break;
                    }
                    System.out.print("Enter student name: ");
                    String scoreStudent = scanner.nextLine();
                    Student student = findStudent(scoreStudent);
                    if (student == null) {
                        System.out.println("Student not found.");
                        break;
                    }
                    System.out.print("Enter score: ");
                    int score = scanner.nextInt();
                    scanner.nextLine();
                    teacher.assignScore(subject, student, score);
                    break;
                case 5:
                    System.out.print("Enter subject name: ");
                    String addSubject = scanner.nextLine();
                    Subject subj = findSubject(addSubject);
                    if (subj == null) {
                        System.out.println("Subject not found.");
                        break;
                    }
                    System.out.print("Enter student name: ");
                    String addStudent = scanner.nextLine();
                    Student stud = findStudent(addStudent);
                    if (stud == null) {
                        System.out.println("Student not found.");
                        break;
                    }
                    teacher.addStudentToSubject(subj, stud);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void studentMenu() {
        System.out.print("\nEnter student name: ");
        String studentName = scanner.nextLine();
        Student student = findStudent(studentName);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        while (true) {
            System.out.println("\nStudent Menu (" + student.getName() + "):");
            System.out.println("1 - Show all scores");
            System.out.println("2 - Show scores by subject");
            System.out.println("0 - Return to main menu");
            System.out.print("Select action: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    student.showAllScores();
                    break;
                case 2:
                    System.out.print("Enter subject name: ");
                    String subjectName = scanner.nextLine();
                    student.showScores(subjectName);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void adminMenu() {
        while (true) {
            System.out.println("\nAdministrator Menu:");
            System.out.println("1 - Add teacher");
            System.out.println("2 - Add student");
            System.out.println("3 - Add subject");
            System.out.println("4 - Assign subject to teacher");
            System.out.println("0 - Return to main menu");
            System.out.print("Select action: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter teacher ID: ");
                    int teacherId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter teacher name: ");
                    String teacherName = scanner.nextLine();
                    teachers.add(new Teacher(teacherId, teacherName));
                    System.out.println("Teacher added successfully.");
                    break;
                case 2:
                    System.out.print("Enter student ID: ");
                    int studentId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter student name: ");
                    String studentName = scanner.nextLine();
                    System.out.print("Enter student grade: ");
                    int grade = scanner.nextInt();
                    scanner.nextLine();
                    students.add(new Student(studentId, grade, studentName));
                    System.out.println("Student added successfully.");
                    break;
                case 3:
                    System.out.print("Enter subject name: ");
                    String subjectName = scanner.nextLine();
                    System.out.print("Enter subject description: ");
                    String description = scanner.nextLine();
                    subjects.add(new Subject(subjectName, description));
                    System.out.println("Subject added successfully.");
                    break;
                case 4:
                    System.out.print("Enter teacher name: ");
                    String tName = scanner.nextLine();
                    Teacher t = findTeacher(tName);
                    if (t == null) {
                        System.out.println("Teacher not found.");
                        break;
                    }
                    System.out.print("Enter subject name: ");
                    String sName = scanner.nextLine();
                    Subject s = findSubject(sName);
                    if (s == null) {
                        System.out.println("Subject not found.");
                        break;
                    }
                    t.addSubjectADMIN(s);
                    System.out.println("Subject assigned to teacher successfully.");
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private Teacher findTeacher(String name) {
        for (Teacher teacher : teachers) {
            if (teacher.getName().equalsIgnoreCase(name)) {
                return teacher;
            }
        }
        return null;
    }

    private Student findStudent(String name) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                return student;
            }
        }
        return null;
    }

    private Subject findSubject(String name) {
        for (Subject subject : subjects) {
            if (subject.getName().equalsIgnoreCase(name)) {
                return subject;
            }
        }
        return null;
    }

    public void loadFromExcelTemplate(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet1 = workbook.getSheet("Лист1");
            if (sheet1 != null) {
                Iterator<Row> rowIterator = sheet1.iterator();

                if (rowIterator.hasNext()) rowIterator.next();

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    try {
                        int studentId = (int) row.getCell(0).getNumericCellValue();
                        String studentName = row.getCell(1).getStringCellValue();
                        int grade = (int) row.getCell(2).getNumericCellValue();
                        String subjectName = row.getCell(3).getStringCellValue();

                        Student student = students.stream()
                                .filter(s -> s.getID() == studentId)
                                .findFirst()
                                .orElseGet(() -> {
                                    Student newStudent = new Student(studentId, grade, studentName);
                                    students.add(newStudent);
                                    return newStudent;
                                });


                        Subject subject = subjects.stream()
                                .filter(s -> s.getName().equalsIgnoreCase(subjectName))
                                .findFirst()
                                .orElseGet(() -> {
                                    Subject newSubject = new Subject(subjectName, "");
                                    subjects.add(newSubject);
                                    return newSubject;
                                });

                        for (int i = 4; i <= 10; i++) {
                            Cell cell = row.getCell(i);
                            if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                                int score = (int) cell.getNumericCellValue();

                                if (!subject.getScores().containsKey(student)) {
                                    subject.getScores().put(student, new ArrayList<>());
                                }

                                subject.addScoreADMIN(student, score);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Ошибка обработки строки " + row.getRowNum() + ": " + e.getMessage());
                    }
                }
            }

            Sheet sheet2 = workbook.getSheet("Лист2");
            if (sheet2 != null) {
                Iterator<Row> rowIterator = sheet2.iterator();

                if (rowIterator.hasNext()) rowIterator.next();

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    try {
                        int teacherId = (int) row.getCell(0).getNumericCellValue();
                        String teacherName = row.getCell(1).getStringCellValue();
                        String subjectName = row.getCell(2).getStringCellValue();

                        Subject subject = subjects.stream()
                                .filter(s -> s.getName().equalsIgnoreCase(subjectName))
                                .findFirst()
                                .orElse(null);

                        if (subject != null) {
                            Teacher teacher = teachers.stream()
                                    .filter(t -> t.getID() == teacherId)
                                    .findFirst()
                                    .orElseGet(() -> {
                                        Teacher newTeacher = new Teacher(teacherId, teacherName);
                                        teachers.add(newTeacher);
                                        return newTeacher;
                                    });

                            teacher.addSubjectADMIN(subject);
                        }
                    } catch (Exception e) {
                        System.err.println("Ошибка обработки строки " + row.getRowNum() + ": " + e.getMessage());
                    }
                }
            }

            System.out.println("Данные успешно загружены из Excel файла");
        } catch (Exception e) {
            System.out.println("Ошибка загрузки Excel файла: " + e.getMessage());
        }
    }

    public void serializeToJson(String filePath) {
        try {
            JSONObject root = new JSONObject();

            JSONArray teachersArray = new JSONArray(
                    teachers.stream()
                            .map(teacher -> {
                                JSONObject teacherObj = new JSONObject();
                                teacherObj.put("id", teacher.getID());
                                teacherObj.put("name", teacher.getName());

                                JSONArray subjectsArray = new JSONArray(
                                        teacher.getSubjects().stream()
                                                .map(Subject::getName)
                                                .collect(Collectors.toList())
                                );
                                teacherObj.put("subjects", subjectsArray);

                                return teacherObj;
                            })
                            .collect(Collectors.toList())
            );
            root.put("teachers", teachersArray);

            JSONArray studentsArray = new JSONArray(
                    students.stream()
                            .map(student -> {
                                JSONObject studentObj = new JSONObject();
                                studentObj.put("id", student.getID());
                                studentObj.put("name", student.getName());
                                studentObj.put("grade", student.getGrade());

                                JSONArray studentSubjectsArray = new JSONArray(
                                        subjects.stream()
                                                .filter(subject -> subject.getScores().containsKey(student))
                                                .map(subject -> {
                                                    JSONObject subjectScore = new JSONObject();
                                                    subjectScore.put("subject", subject.getName());

                                                    JSONArray scoresArray = new JSONArray(
                                                            subject.getScores().get(student)
                                                    );
                                                    subjectScore.put("scores", scoresArray);

                                                    return subjectScore;
                                                })
                                                .collect(Collectors.toList())
                                );
                                studentObj.put("subjects", studentSubjectsArray);

                                return studentObj;
                            })
                            .collect(Collectors.toList())
            );
            root.put("students", studentsArray);

            JSONArray subjectsArray = new JSONArray(
                    subjects.stream()
                            .map(subject -> {
                                JSONObject subjectObj = new JSONObject();
                                subjectObj.put("name", subject.getName());
                                subjectObj.put("description", subject.getDescription());
                                return subjectObj;
                            })
                            .collect(Collectors.toList())
            );
            root.put("subjects", subjectsArray);

            try (FileWriter file = new FileWriter(filePath)) {
                file.write(root.toString(4));
                System.out.println("Данные успешно сохранены в JSON файл");
            }
        } catch (IOException e) {
            System.out.println("Ошибка сохранения в JSON: " + e.getMessage());
        }
    }

    public void deserializeFromJson(String filePath) {
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONObject root = new JSONObject(jsonString);

            teachers.clear();
            students.clear();
            subjects.clear();

            JSONArray subjectsArray = root.getJSONArray("subjects");
            subjectsArray.forEach(obj -> {
                JSONObject subjectObj = (JSONObject) obj;
                subjects.add(new Subject(
                        subjectObj.getString("name"),
                        subjectObj.getString("description")
                ));
            });

            JSONArray studentsArray = root.getJSONArray("students");
            studentsArray.forEach(obj -> {
                JSONObject studentObj = (JSONObject) obj;
                students.add(new Student(
                        studentObj.getInt("id"),
                        studentObj.getInt("grade"),
                        studentObj.getString("name")
                ));
            });

            JSONArray teachersArray = root.getJSONArray("teachers");
            teachersArray.forEach(obj -> {
                JSONObject teacherObj = (JSONObject) obj;
                Teacher teacher = new Teacher(
                        teacherObj.getInt("id"),
                        teacherObj.getString("name")
                );

                teacherObj.getJSONArray("subjects").forEach(subjName -> {
                    Subject subject = findSubject((String) subjName);
                    if (subject != null) {
                        teacher.addSubjectADMIN(subject);
                    }
                });

                teachers.add(teacher);
            });

            studentsArray.forEach(obj -> {
                JSONObject studentObj = (JSONObject) obj;
                Student student = findStudent(studentObj.getString("name"));

                if (student != null) {
                    studentObj.getJSONArray("subjects").forEach(subjObj -> {
                        JSONObject subjectScore = (JSONObject) subjObj;
                        Subject subject = findSubject(subjectScore.getString("subject"));

                        if (subject != null) {
                            subject.getScores().computeIfAbsent(student, k -> new ArrayList<>());
                            subjectScore.getJSONArray("scores").forEach(score -> {
                                subject.getScores().get(student).add((Integer) score);
                            });
                        }
                    });
                }
            });

            System.out.println("Данные успешно загружены из JSON файла");
        } catch (IOException e) {
            System.out.println("Ошибка загрузки из JSON: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ClassLogBook logBook = new ClassLogBook();
        logBook.run();
    }
}