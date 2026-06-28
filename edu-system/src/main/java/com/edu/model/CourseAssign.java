package com.edu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * 必修课程指派实体（指派给班级）
 */
public class CourseAssign {
    private Long id;
    private Long courseTaskId;
    private Long classgroupId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // 联合查询时携带的关联信息
    private String courseName;
    private String teacherName;
    private String classgroupName;
    private String specialityName;
    private String semester;

    public CourseAssign() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCourseTaskId() { return courseTaskId; }
    public void setCourseTaskId(Long courseTaskId) { this.courseTaskId = courseTaskId; }
    public Long getClassgroupId() { return classgroupId; }
    public void setClassgroupId(Long classgroupId) { this.classgroupId = classgroupId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public String getClassgroupName() { return classgroupName; }
    public void setClassgroupName(String classgroupName) { this.classgroupName = classgroupName; }
    public String getSpecialityName() { return specialityName; }
    public void setSpecialityName(String specialityName) { this.specialityName = specialityName; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
}
