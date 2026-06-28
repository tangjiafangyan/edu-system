package com.edu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * 课程子任务实体（可选：课程任务进一步关联到班级）
 */
public class CourseSubtask {
    private Long id;
    private Long courseTaskId;
    private Long classgroupId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // 联合查询携带信息
    private String courseName;
    private String teacherName;
    private String classgroupName;

    public CourseSubtask() {}

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
}
