package com.edu.dao;

import com.edu.model.CourseAssign;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * 必修课程指派 DAO
 */
public class CourseAssignDao extends BaseDao<CourseAssign> {

    @Override
    protected String getTableName() {
        return "course_assign";
    }

    public Long insert(CourseAssign ca) {
        String sql = "INSERT INTO course_assign (course_task_id, classgroup_id) VALUES (?, ?)";
        return insert(sql, ca.getCourseTaskId(), ca.getClassgroupId());
    }

    /**
     * 联合查询（课程任务 + 课程 + 教师 + 班级 + 专业）
     */
    public List<CourseAssign> findAllWithFullInfo() {
        String sql = "SELECT ca.*, c.name AS course_name, t.name AS teacher_name, " +
                     "cg.name AS classgroup_name, s.name AS speciality_name, ct.semester " +
                     "FROM course_assign ca " +
                     "LEFT JOIN course_task ct ON ca.course_task_id = ct.id " +
                     "LEFT JOIN course c ON ct.course_id = c.id " +
                     "LEFT JOIN teacher t ON ct.teacher_id = t.id " +
                     "LEFT JOIN classgroup cg ON ca.classgroup_id = cg.id " +
                     "LEFT JOIN speciality s ON cg.speciality_id = s.id " +
                     "ORDER BY ca.id";
        try {
            return runner.query(sql, listHandler());
        } catch (SQLException e) {
            throw new RuntimeException("查询必修课指派列表失败", e);
        }
    }

    /**
     * 根据关键词搜索
     */
    public List<CourseAssign> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAllWithFullInfo();
        }
        String sql = "SELECT ca.*, c.name AS course_name, t.name AS teacher_name, " +
                     "cg.name AS classgroup_name, s.name AS speciality_name, ct.semester " +
                     "FROM course_assign ca " +
                     "LEFT JOIN course_task ct ON ca.course_task_id = ct.id " +
                     "LEFT JOIN course c ON ct.course_id = c.id " +
                     "LEFT JOIN teacher t ON ct.teacher_id = t.id " +
                     "LEFT JOIN classgroup cg ON ca.classgroup_id = cg.id " +
                     "LEFT JOIN speciality s ON cg.speciality_id = s.id " +
                     "WHERE c.name LIKE ? OR cg.name LIKE ? OR t.name LIKE ? " +
                     "ORDER BY ca.id";
        String kw = "%" + keyword + "%";
        try {
            return runner.query(sql, listHandler(), kw, kw, kw);
        } catch (SQLException e) {
            throw new RuntimeException("搜索必修课指派失败", e);
        }
    }

    /**
     * 查询班级必修课表
     */
    public List<CourseAssign> findByClassgroupId(Long classgroupId) {
        String sql = "SELECT ca.*, c.name AS course_name, t.name AS teacher_name, " +
                     "cg.name AS classgroup_name, s.name AS speciality_name, ct.semester " +
                     "FROM course_assign ca " +
                     "LEFT JOIN course_task ct ON ca.course_task_id = ct.id " +
                     "LEFT JOIN course c ON ct.course_id = c.id " +
                     "LEFT JOIN teacher t ON ct.teacher_id = t.id " +
                     "LEFT JOIN classgroup cg ON ca.classgroup_id = cg.id " +
                     "LEFT JOIN speciality s ON cg.speciality_id = s.id " +
                     "WHERE ca.classgroup_id = ?";
        try {
            return runner.query(sql, listHandler(), classgroupId);
        } catch (SQLException e) {
            throw new RuntimeException("查询班级课表失败", e);
        }
    }
}
