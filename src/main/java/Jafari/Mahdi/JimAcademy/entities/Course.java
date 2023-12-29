package Jafari.Mahdi.JimAcademy.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Course {

    private int id;
    private List<Student> studentList;
    private Teacher teacherList;
    private String information;
    private List<Exam> examList;
    private List<Homework> homeworkList;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToMany
    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    @ManyToOne
    public Teacher getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(Teacher teacherList) {
        this.teacherList = teacherList;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @OneToMany
    public List<Exam> getExamList() {
        return examList;
    }

    public void setExamList(List<Exam> examList) {
        this.examList = examList;
    }

    @OneToMany
    public List<Homework> getHomeworkList() {
        return homeworkList;
    }

    public void setHomeworkList(List<Homework> homeworkList) {
        this.homeworkList = homeworkList;
    }
}
