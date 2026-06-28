package com.edu.dao;

import com.edu.model.Speciality;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * 专业 DAO
 */
public class SpecialityDao extends BaseDao<Speciality> {

    @Override
    protected String getTableName() {
        return "speciality";
    }

    public Long insert(Speciality s) {
        String sql = "INSERT INTO speciality (name, college_id, description) VALUES (?, ?, ?)";
        return insert(sql, s.getName(), s.getCollegeId(), s.getDescription());
    }

    public int update(Speciality s) {
        String sql = "UPDATE speciality SET name=?, college_id=?, description=? WHERE id=?";
        return update(sql, s.getName(), s.getCollegeId(), s.getDescription(), s.getId());
    }

    /**
     * 联合查询专业及所属学院
     */
    public List<Speciality> findAllWithCollege() {
        String sql = "SELECT s.*, c.name AS college_name " +
                     "FROM speciality s LEFT JOIN college c ON s.college_id = c.id " +
                     "ORDER BY s.id";
        try {
            return runner.query(sql, listHandler());
        } catch (SQLException e) {
            throw new RuntimeException("查询专业列表失败", e);
        }
    }

    /**
     * 根据关键词搜索
     */
    public List<Speciality> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAllWithCollege();
        }
        String sql = "SELECT s.*, c.name AS college_name " +
                     "FROM speciality s LEFT JOIN college c ON s.college_id = c.id " +
                     "WHERE s.name LIKE ? OR c.name LIKE ? " +
                     "ORDER BY s.id";
        String kw = "%" + keyword + "%";
        try {
            return runner.query(sql, listHandler(), kw, kw);
        } catch (SQLException e) {
            throw new RuntimeException("搜索专业失败", e);
        }
    }

    /**
     * 根据学院 ID 查询专业
     */
    public List<Speciality> findByCollegeId(Long collegeId) {
        String sql = "SELECT s.*, c.name AS college_name " +
                     "FROM speciality s LEFT JOIN college c ON s.college_id = c.id " +
                     "WHERE s.college_id = ?";
        try {
            return runner.query(sql, listHandler(), collegeId);
        } catch (SQLException e) {
            throw new RuntimeException("根据学院查询专业失败", e);
        }
    }
}
