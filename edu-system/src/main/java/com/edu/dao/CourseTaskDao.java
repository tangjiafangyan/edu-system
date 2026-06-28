package com.edu.dao;

import com.edu.model.CourseTask;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * 课程任务 DAO
 */
public class CourseTaskDao extends BaseDao<CourseTask> {

    @Override
    protected String getTableName() {
        return "course_task";
    }

    public Long insert(CourseTask ct) {
        String sql = "INSERT INTO course_task (course_id, teacher_id, semester, location, max_students, description) VALUES (?, ?, ?, ?, ?, ?)";
        return insert(sql, ct.getCourseId(), ct.getTeacherId(), ct.getSemester(), ct.getLocation(), ct.getMaxStudents(), ct.getDescription());
    }

    public int update(CourseTask ct) {
        String sql = "UPDATE course_task SET course_id=?, teacher_id=?, semester=?, location=?, max_students=?, description=? WHERE id=?";
        return update(sql, ct.getCourseId(), ct.getTeacherId(), ct.getSemester(), ct.getLocation(), ct.getMaxStudents(), ct.getDescription(), ct.getId());
    }

    /**
     * 联合查询（课程 + 教师）
     */
    public List<CourseTask> findAllWithJoinInfo() {
        String sql = "SELECT ct.*, c.name AS course_name, c.code AS course_code, t.name AS teacher_name " +
                     "FROM course_task ct " +
                     "LEFT JOIN course c ON ct.course_id = c.id " +
                     "LEFT JOIN teacher t ON ct.teacher_id = t.id " +
                     "ORDER BY ct.id";
        try {
            return runner.query(sql, listHandler());
        } catch (SQLException e) {
            throw new RuntimeException("查询课程任务列表失败", e);
        }
    }

    /**
     * 根据关键词搜索
     */
    public List<CourseTask> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAllWithJoinInfo();
        }
        String sql = "SELECT ct.*, c.name AS course_name, c.code AS course_code, t.name AS teacher_name " +
                     "FROM course_task ct " +
                     "LEFT JOIN course c ON ct.course_id = c.id " +
                     "LEFT JOIN teacher t ON ct.teacher_id = t.id " +
                     "WHERE c.name LIKE ? OR t.name LIKE ? OR ct.semester LIKE ? " +
                     "ORDER BY ct.id";
        String kw = "%" + keyword + "%";
        try {
            return runner.query(sql, listHandler(), kw, kw, kw);
        } catch (SQLException e) {
            throw new RuntimeException("搜索课程任务失败", e);
        }
    }
}
