package com.edu.dao;

import com.edu.model.Teacher;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * 教师 DAO
 */
public class TeacherDao extends BaseDao<Teacher> {

    @Override
    protected String getTableName() {
        return "teacher";
    }

    /**
     * 插入教师
     */
    public Long insert(Teacher t) {
        String sql = "INSERT INTO teacher (name, title, gender, college_id, email, phone) VALUES (?, ?, ?, ?, ?, ?)";
        return insert(sql, t.getName(), t.getTitle(), t.getGender(), t.getCollegeId(), t.getEmail(), t.getPhone());
    }

    /**
     * 更新教师
     */
    public int update(Teacher t) {
        String sql = "UPDATE teacher SET name=?, title=?, gender=?, college_id=?, email=?, phone=? WHERE id=?";
        return update(sql, t.getName(), t.getTitle(), t.getGender(), t.getCollegeId(), t.getEmail(), t.getPhone(), t.getId());
    }

    /**
     * 联合查询教师及所属学院
     */
    public List<Teacher> findAllWithCollege() {
        String sql = "SELECT t.*, c.name AS college_name " +
                     "FROM teacher t LEFT JOIN college c ON t.college_id = c.id " +
                     "ORDER BY t.id";
        try {
            return runner.query(sql, listHandler());
        } catch (SQLException e) {
            throw new RuntimeException("查询教师列表(含学院)失败", e);
        }
    }

    /**
     * 根据关键词搜索（联合查询）
     */
    public List<Teacher> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAllWithCollege();
        }
        String sql = "SELECT t.*, c.name AS college_name " +
                     "FROM teacher t LEFT JOIN college c ON t.college_id = c.id " +
                     "WHERE t.name LIKE ? OR t.title LIKE ? OR c.name LIKE ? " +
                     "ORDER BY t.id";
        String kw = "%" + keyword + "%";
        try {
            return runner.query(sql, listHandler(), kw, kw, kw);
        } catch (SQLException e) {
            throw new RuntimeException("搜索教师失败", e);
        }
    }

    /**
     * 查询单个教师（含学院信息）
     */
    public Teacher findByIdWithCollege(Long id) {
        String sql = "SELECT t.*, c.name AS college_name " +
                     "FROM teacher t LEFT JOIN college c ON t.college_id = c.id " +
                     "WHERE t.id = ?";
        try {
            return runner.query(sql, beanHandler(), id);
        } catch (SQLException e) {
            throw new RuntimeException("查询教师详情失败", e);
        }
    }

    /**
     * 根据学院 ID 查询教师
     */
    public List<Teacher> findByCollegeId(Long collegeId) {
        String sql = "SELECT t.*, c.name AS college_name " +
                     "FROM teacher t LEFT JOIN college c ON t.college_id = c.id " +
                     "WHERE t.college_id = ?";
        try {
            return runner.query(sql, listHandler(), collegeId);
        } catch (SQLException e) {
            throw new RuntimeException("根据学院查询教师失败", e);
        }
    }
}
