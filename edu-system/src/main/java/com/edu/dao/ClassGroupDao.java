package com.edu.dao;

import com.edu.model.ClassGroup;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * 班级 DAO
 */
public class ClassGroupDao extends BaseDao<ClassGroup> {

    @Override
    protected String getTableName() {
        return "classgroup";
    }

    public Long insert(ClassGroup cg) {
        String sql = "INSERT INTO classgroup (name, speciality_id, grade) VALUES (?, ?, ?)";
        return insert(sql, cg.getName(), cg.getSpecialityId(), cg.getGrade());
    }

    public int update(ClassGroup cg) {
        String sql = "UPDATE classgroup SET name=?, speciality_id=?, grade=? WHERE id=?";
        return update(sql, cg.getName(), cg.getSpecialityId(), cg.getGrade(), cg.getId());
    }

    /**
     * 联合查询班级 + 专业 + 学院
     */
    public List<ClassGroup> findAllWithFullInfo() {
        String sql = "SELECT cg.*, s.name AS speciality_name, co.name AS college_name " +
                     "FROM classgroup cg " +
                     "LEFT JOIN speciality s ON cg.speciality_id = s.id " +
                     "LEFT JOIN college co ON s.college_id = co.id " +
                     "ORDER BY cg.id";
        try {
            return runner.query(sql, listHandler());
        } catch (SQLException e) {
            throw new RuntimeException("查询班级列表失败", e);
        }
    }

    /**
     * 根据关键词搜索
     */
    public List<ClassGroup> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAllWithFullInfo();
        }
        String sql = "SELECT cg.*, s.name AS speciality_name, co.name AS college_name " +
                     "FROM classgroup cg " +
                     "LEFT JOIN speciality s ON cg.speciality_id = s.id " +
                     "LEFT JOIN college co ON s.college_id = co.id " +
                     "WHERE cg.name LIKE ? OR s.name LIKE ? OR co.name LIKE ? " +
                     "ORDER BY cg.id";
        String kw = "%" + keyword + "%";
        try {
            return runner.query(sql, listHandler(), kw, kw, kw);
        } catch (SQLException e) {
            throw new RuntimeException("搜索班级失败", e);
        }
    }

    /**
     * 根据专业 ID 查询班级
     */
    public List<ClassGroup> findBySpecialityId(Long specialityId) {
        String sql = "SELECT cg.*, s.name AS speciality_name, co.name AS college_name " +
                     "FROM classgroup cg " +
                     "LEFT JOIN speciality s ON cg.speciality_id = s.id " +
                     "LEFT JOIN college co ON s.college_id = co.id " +
                     "WHERE cg.speciality_id = ?";
        try {
            return runner.query(sql, listHandler(), specialityId);
        } catch (SQLException e) {
            throw new RuntimeException("根据专业查询班级失败", e);
        }
    }
}
