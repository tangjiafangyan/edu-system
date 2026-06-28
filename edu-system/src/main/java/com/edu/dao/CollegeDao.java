package com.edu.dao;

import com.edu.model.College;

/**
 * 学院 DAO
 */
public class CollegeDao extends BaseDao<College> {

    @Override
    protected String getTableName() {
        return "college";
    }

    /**
     * 插入学院
     */
    public Long insert(College c) {
        String sql = "INSERT INTO college (name, dean, description) VALUES (?, ?, ?)";
        return insert(sql, c.getName(), c.getDean(), c.getDescription());
    }

    /**
     * 更新学院
     */
    public int update(College c) {
        String sql = "UPDATE college SET name=?, dean=?, description=? WHERE id=?";
        return update(sql, c.getName(), c.getDean(), c.getDescription(), c.getId());
    }

    /**
     * 根据关键词模糊搜索
     */
    public java.util.List<College> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        String sql = "SELECT * FROM college WHERE name LIKE ? OR dean LIKE ? OR description LIKE ?";
        String kw = "%" + keyword + "%";
        return queryList(sql, kw, kw, kw);
    }
}
