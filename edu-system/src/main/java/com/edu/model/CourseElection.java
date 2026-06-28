package com.edu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * 选修课实体（学生选课）
 */
public class CourseElection {
    private Long id;
    private Long studentId;
    private Long courseTaskId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // 联合查询携带信息
    private String studentName;
    private String studentNo;
    private String courseName;
    private String teacherName;
    private String semester;

    public CourseElection() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getCourseTaskId() { return courseTaskId; }
    public void setCourseTaskId(Long courseTaskId) { this.courseTaskId = courseTaskId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
}
