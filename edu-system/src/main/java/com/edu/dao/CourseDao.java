package com.edu.dao;

import com.edu.model.Course;

/**
 * 课程 DAO
 */
public class CourseDao extends BaseDao<Course> {

    @Override
    protected String getTableName() {
        return "course";
    }

    public Long insert(Course c) {
        String sql = "INSERT INTO course (name, code, credit, hours, type, description) VALUES (?, ?, ?, ?, ?, ?)";
        return insert(sql, c.getName(), c.getCode(), c.getCredit(), c.getHours(), c.getType(), c.getDescription());
    }

    public int update(Course c) {
        String sql = "UPDATE course SET name=?, code=?, credit=?, hours=?, type=?, description=? WHERE id=?";
        return update(sql, c.getName(), c.getCode(), c.getCredit(), c.getHours(), c.getType(), c.getDescription(), c.getId());
    }

    /**
     * 模糊搜索
     */
    public java.util.List<Course> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        String sql = "SELECT * FROM course WHERE name LIKE ? OR code LIKE ? OR type LIKE ?";
        String kw = "%" + keyword + "%";
        return queryList(sql, kw, kw, kw);
    }
}
