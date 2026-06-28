package com.edu.dao;

import com.edu.model.CourseSubtask;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * 课程子任务 DAO（可选：课程任务关联到班级）
 */
public class CourseSubtaskDao extends BaseDao<CourseSubtask> {

    @Override
    protected String getTableName() {
        return "course_subtask";
    }

    public Long insert(CourseSubtask cs) {
        String sql = "INSERT INTO course_subtask (course_task_id, classgroup_id) VALUES (?, ?)";
        return insert(sql, cs.getCourseTaskId(), cs.getClassgroupId());
    }

    /**
     * 联合查询
     */
    public List<CourseSubtask> findAllWithJoinInfo() {
        String sql = "SELECT cs.*, c.name AS course_name, t.name AS teacher_name, cg.name AS classgroup_name " +
                     "FROM course_subtask cs " +
                     "LEFT JOIN course_task ct ON cs.course_task_id = ct.id " +
                     "LEFT JOIN course c ON ct.course_id = c.id " +
                     "LEFT JOIN teacher t ON ct.teacher_id = t.id " +
                     "LEFT JOIN classgroup cg ON cs.classgroup_id = cg.id " +
                     "ORDER BY cs.id";
        try {
            return runner.query(sql, listHandler());
        } catch (SQLException e) {
            throw new RuntimeException("查询课程子任务失败", e);
        }
    }

    /**
     * 根据关键词搜索
     */
    public List<CourseSubtask> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAllWithJoinInfo();
        }
        String sql = "SELECT cs.*, c.name AS course_name, t.name AS teacher_name, cg.name AS classgroup_name " +
                     "FROM course_subtask cs " +
                     "LEFT JOIN course_task ct ON cs.course_task_id = ct.id " +
                     "LEFT JOIN course c ON ct.course_id = c.id " +
                     "LEFT JOIN teacher t ON ct.teacher_id = t.id " +
                     "LEFT JOIN classgroup cg ON cs.classgroup_id = cg.id " +
                     "WHERE c.name LIKE ? OR cg.name LIKE ? OR t.name LIKE ? " +
                     "ORDER BY cs.id";
        String kw = "%" + keyword + "%";
        try {
            return runner.query(sql, listHandler(), kw, kw, kw);
        } catch (SQLException e) {
            throw new RuntimeException("搜索课程子任务失败", e);
        }
    }
}
