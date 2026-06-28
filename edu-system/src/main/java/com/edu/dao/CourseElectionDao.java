package com.edu.dao;

import com.edu.model.CourseElection;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * 选修课 DAO（学生选课）
 */
public class CourseElectionDao extends BaseDao<CourseElection> {

    @Override
    protected String getTableName() {
        return "course_election";
    }

    public Long insert(CourseElection ce) {
        String sql = "INSERT INTO course_election (student_id, course_task_id) VALUES (?, ?)";
        return insert(sql, ce.getStudentId(), ce.getCourseTaskId());
    }

    /**
     * 联合查询（学生 + 课程任务 + 课程 + 教师）
     */
    public List<CourseElection> findAllWithFullInfo() {
        String sql = "SELECT ce.*, st.name AS student_name, st.student_no, " +
                     "c.name AS course_name, t.name AS teacher_name, ct.semester " +
                     "FROM course_election ce " +
                     "LEFT JOIN student st ON ce.student_id = st.id " +
                     "LEFT JOIN course_task ct ON ce.course_task_id = ct.id " +
                     "LEFT JOIN course c ON ct.course_id = c.id " +
                     "LEFT JOIN teacher t ON ct.teacher_id = t.id " +
                     "ORDER BY ce.id";
        try {
            return runner.query(sql, listHandler());
        } catch (SQLException e) {
            throw new RuntimeException("查询选课列表失败", e);
        }
    }

    /**
     * 根据关键词搜索
     */
    public List<CourseElection> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAllWithFullInfo();
        }
        String sql = "SELECT ce.*, st.name AS student_name, st.student_no, " +
                     "c.name AS course_name, t.name AS teacher_name, ct.semester " +
                     "FROM course_election ce " +
                     "LEFT JOIN student st ON ce.student_id = st.id " +
                     "LEFT JOIN course_task ct ON ce.course_task_id = ct.id " +
                     "LEFT JOIN course c ON ct.course_id = c.id " +
                     "LEFT JOIN teacher t ON ct.teacher_id = t.id " +
                     "WHERE st.name LIKE ? OR c.name LIKE ? OR t.name LIKE ? " +
                     "ORDER BY ce.id";
        String kw = "%" + keyword + "%";
        try {
            return runner.query(sql, listHandler(), kw, kw, kw);
        } catch (SQLException e) {
            throw new RuntimeException("搜索选课记录失败", e);
        }
    }

    /**
     * 根据学生 ID 查询选课
     */
    public List<CourseElection> findByStudentId(Long studentId) {
        String sql = "SELECT ce.*, st.name AS student_name, st.student_no, " +
                     "c.name AS course_name, t.name AS teacher_name, ct.semester " +
                     "FROM course_election ce " +
                     "LEFT JOIN student st ON ce.student_id = st.id " +
                     "LEFT JOIN course_task ct ON ce.course_task_id = ct.id " +
                     "LEFT JOIN course c ON ct.course_id = c.id " +
                     "LEFT JOIN teacher t ON ct.teacher_id = t.id " +
                     "WHERE ce.student_id = ?";
        try {
            return runner.query(sql, listHandler(), studentId);
        } catch (SQLException e) {
            throw new RuntimeException("查询学生选课失败", e);
        }
    }
}
