package models;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import util.AnimatedText;
import util.DatabaseManager;
import util.Timer;
import util.ConsolePrinter;

public class Exam implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    // private final String examCreated;
    private final String examID;
    private String examSubject;
    private final int examSem;
    private final String createdBy;
    private int questionNum;
    private int totalMarks = 0;
    private int ExamDurationMinutes;
    private final List<Question> questions;   
    private final List<String> attemptTracker;//studetn ID
    
    List<Exam> exams = new DatabaseManager().loadExams();

    public Exam(Scanner sc, AnimatedText animate, Teacher teacher){
        boolean isDuplicate;
        animate.animateText("Enter Semester ", 25);
        this.examSem = sc.nextInt(); sc.nextLine();
        do {
            animate.animateText("Enter Subject ", 25);
            this.examSubject = sc.nextLine();
            
            isDuplicate = checkDuplicateExam(teacher, this.examSubject);
            if (isDuplicate) {
                animate.animateText("You have already created an exam on this subject", 25);
            }
        } while (isDuplicate);
                animate.animateText("Enter Exam Duration (In Minutes)", 25);
        this.ExamDurationMinutes = sc.nextInt();sc.nextLine();
        animate.animateText("Enter the Number of questions ", 25);
        this.questionNum = sc.nextInt();sc.nextLine();
        this.examID = this.ExamIDInitialize();
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        // this.examCreated = LocalDateTime.now().format(formatter);
        this.createdBy = teacher.getName();
        this.questions = new ArrayList<>(); 
        this.attemptTracker = new ArrayList<>();
    }

    public final String ExamIDInitialize() {
        int id = (int) exams.stream()
            .filter(exam -> exam.getSubject().equalsIgnoreCase(examSubject)).count() + 1;
        String formattedId = String.format("%02d", id);
        return this.examSubject.substring(0, 4).toUpperCase() + formattedId;
    }

    public final boolean checkDuplicateExam(Teacher teacher, String subject){
        return exams.stream()
            .filter(exam -> exam.getCreatedBy().equalsIgnoreCase(teacher.getName()) && 
                exam.getExamSem() == this.examSem)
            .anyMatch(exam -> exam.getSubject().equalsIgnoreCase(subject));
    }

    public void createExam(Scanner sc, AnimatedText animate){
        animate.animateText("Creating Exam", 25);
        for(int i = 0; i < questionNum; i++) {
            Question newQuestion = new Question(sc, animate);
            this.questions.add(newQuestion);
            this.totalMarks += newQuestion.getMarks();
        }   
    }
    
    public void resultsReport(AnimatedText animate) {
        List<Student> students = new DatabaseManager().loadStudents();
        Set<String> attemptSet = new HashSet<>(this.attemptTracker);

        if (this.attemptTracker.isEmpty()) {
            animate.animateText("No students have attempted this exam yet.", 25);
            return;
        }

        List<Integer> marksList = students.stream()
            .filter(student -> attemptSet.contains(student.getID()))
            .map(student -> student.getMarks(this.examID))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        int passedStudents = (int) marksList.stream()
            .filter(mark -> mark >= (0.35 * this.totalMarks))
            .count();

        int passRate = !attemptTracker.isEmpty() ? (passedStudents * 100) / attemptTracker.size() : 0;
        
        int averageMark = !marksList.isEmpty() ? 
            marksList.stream().mapToInt(Integer::intValue).sum() / marksList.size() : 0;

        int highestMark = marksList.stream()
            .mapToInt(Integer::intValue)
            .max()
            .orElse(0);
            
        int lowestMark = marksList.stream()
            .mapToInt(Integer::intValue)
            .min()
            .orElse(0);

        int aGrade = 0, bGrade = 0, cGrade = 0, dGrade = 0, fGrade = 0;
        for (int mark : marksList) {
            int percentage = (mark * 100) / this.totalMarks;

            if (percentage >= 85) aGrade++;
            else if (percentage >= 70) bGrade++;
            else if (percentage >= 55) cGrade++;
            else if (percentage >= 35) dGrade++;
            else fGrade++;
        }

        animate.animateText("=== Exam Results Report ===", 25);
        animate.animateText("Total students: " + attemptTracker.size(), 25);
        animate.animateText("Pass Rate: " + passRate + "%", 25);
        animate.animateText("Average Mark: " + averageMark, 25);
        animate.animateText("Highest Mark: " + highestMark, 25);
        animate.animateText("Lowest Mark: " + lowestMark, 25);
        animate.animateText("\nGrade Distribution:", 25);
        animate.animateText("A Grade (85%+): " + aGrade, 25);
        animate.animateText("B Grade (70-84%): " + bGrade, 25);
        animate.animateText("C Grade (55-69%): " + cGrade, 25);
        animate.animateText("D Grade (35-54%): " + dGrade, 25);
        animate.animateText("F Grade (0-34%): " + fGrade, 25);
    }

    public void displayQuestions(AnimatedText animate) {

        int[] index = {1};
        this.questions.forEach(question -> {
            System.out.println(index[0] + ". ");
            question.displayQuestion(animate);
            index[0]++;
        });
    }

    public void addQuestion(Scanner sc, AnimatedText animate) { 
        Question newQuestion = new Question(sc, animate);
        this.questions.add(newQuestion);
        this.questionNum++;
        this.totalMarks += newQuestion.getMarks();
    }

    public void deleteQuestion(Scanner sc, AnimatedText animate) {
        animate.animateText("Enter the Question number to remove ", 25);
        int indexToRemove = sc.nextInt();sc.nextLine();
        this.questions.removeIf(question -> questions.indexOf(question) == indexToRemove);
    }
    
    public void updateQuestion(Scanner sc,AnimatedText animate) {
        animate.animateText("Enter the Question number to update ", 25);
        Question questionUpdate = questions.get(sc.nextInt());sc.nextLine();
        animate.animateText(""" 
            Would you like to     
             \u2192 Change the question 
             \u2192 Change the options
             \u2192 Chnage the correct answer
             \u2192 Chnage the marks
             \u2192 Exit
            """, 25);
        switch(sc.nextLine()){
            case "question", "change question" ->{
                animate.animateText("Enter the new Question ", 25);
                questionUpdate.setQuestion(sc.nextLine());
            }
            case "options", "change options" ->{
                animate.animateText("How many option would you like to change ", 25);
                int n = sc.nextInt(); sc.nextLine();
                for(int i = 0; i < n; i++) {
                    animate.animateText("Enter the new option and number ", i);
                    questionUpdate.setOption(sc.nextLine(), sc.nextInt()); sc.nextLine();
                }
                questionUpdate.setOption(examID, totalMarks);
            }
            case "answer", "change answer" ->{
                animate.animateText("Entre the new correct answer ", 25);
                questionUpdate.setAnswer(sc.nextLine());
            }
            case "marks", "change marks" ->{
                animate.animateText("Enter the new marks ", 25);
                this.totalMarks -= questionUpdate.getMarks();
                questionUpdate.setMarks(sc.nextInt()); sc.nextLine();
                this.totalMarks += questionUpdate.getMarks();
            }
            case "exit", "back" ->{
                break;
            }
            default -> animate.animateText("Invalid input", 25);
        }
    }
    
    public void startExam(Scanner sc, AnimatedText animate, Student student) {
        if (getAttempt(student)) {
            animate.animateText("You have already attempted this exam!", 25);
            return;
        }

        int examMarks = 0;
        int questionsAnswered = 0;
        final boolean[] examCompleted = {false};
        
        Timer examTimer = new Timer(this.ExamDurationMinutes, () -> {
            System.out.println("\nForce submitting exam...");
            examCompleted[0] = true;
        });

        animate.animateText("""
            ### EXAM RULES

            **Time Limit:**
            - You have %d minutes to complete this exam
            - Exam auto-submits when time expires

            **Answer Format:**
            - Enter only a, b, c, or d
            - Invalid answers will require re-entry

            **Important Notes:**
            - One attempt per exam
            - No returning to previous questions
            - Results available immediately after submission
            - Dont press backspace, just re-type your option to change it
                """.formatted(this.ExamDurationMinutes), 10);
        animate.animateText("Total nuber of questions " + this.questionNum, 25);
        animate.animateText("Total Marks " + this.totalMarks, 25);
        animate.animateText("Press ENTER when you are ready to begin the exam. ", 25);
        sc.nextLine();
        
        examTimer.start();
        try {
            for (Question question : questions) {
                if (examCompleted[0]) {
                    break;
                }

                System.out.println("\nQuestion " + (questionsAnswered + 1) + " of " + questions.size());
                question.displayQuestion(animate);
                
                String answer = "";
                boolean validAnswer = false;
                
                while (!validAnswer && !examCompleted[0]) {
                    animate.animateText("Enter your answer (a/b/c/d): ", 25);
                    try {
                        answer = sc.nextLine().toLowerCase();
                        validAnswer = answer.matches("[a-d]");
                    } catch (Exception e) {
                        if (examCompleted[0]) break;
                    }
                }
                
                if (!examCompleted[0] && question.validateAnswer(answer)) {
                    examMarks += question.getMarks();
                }
                questionsAnswered++;
            }
        } finally {
            examTimer.stopTimer();
            
            // Calculate and display results
            double percentage = (examMarks * 100.0) / totalMarks;
            
            animate.animateText("\n=== Exam Completed ===", 25);
            animate.animateText("Questions Attempted: " + questionsAnswered + "/" + questions.size(), 25);
            animate.animateText("Total Marks Obtained: " + examMarks + "/" + totalMarks, 25);
            animate.animateText(String.format("Percentage: %.2f%%", percentage), 25);
            animate.animateText("Result: " + (percentage >= 35 ? "PASS" : "FAIL"), 25);

            // Save results
            student.setMarks(this.examID, examMarks);
            attemptTracker.add(student.getID());
            new DatabaseManager().saveExams(exams);
            new DatabaseManager().saveStudents(new DatabaseManager().loadStudents());
        }
    }

    public void stop() {
        ConsolePrinter.println("Exam finished!");
        // ...existing stop logic...
    }
    // getters
    public String getExamID(){ return this.examID; }
    public String getSubject(){ return this.examSubject; }
    public String getCreatedBy(){ return this.createdBy; }
    public int getExamTime(){ return this.ExamDurationMinutes; }
    public int getTotalMarks(){ return this.totalMarks; }
    public int getExamSem(){ return this.examSem; }
    public List<String> getAttemptTracker(){ return this.attemptTracker; }
    public boolean getAttempt(Student student){ return this.attemptTracker.contains(student.getID()); }

    // setters
    public void incrementQuestionNum(){ this.questionNum++; }
    public void setExamTime(int newTime){ this.ExamDurationMinutes = newTime; }
}