package com.edu.dao;

import com.edu.model.Student;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 学生 DAO
 */
public class StudentDao extends BaseDao<Student> {

    @Override
    protected String getTableName() {
        return "student";
    }

    public Long insert(Student st) {
        String sql = "INSERT INTO student (name, gender, student_no, classgroup_id, email, phone) VALUES (?, ?, ?, ?, ?, ?)";
        return insert(sql, st.getName(), st.getGender(), st.getStudentNo(), st.getClassgroupId(), st.getEmail(), st.getPhone());
    }

    public int update(Student st) {
        String sql = "UPDATE student SET name=?, gender=?, student_no=?, classgroup_id=?, email=?, phone=? WHERE id=?";
        return update(sql, st.getName(), st.getGender(), st.getStudentNo(), st.getClassgroupId(), st.getEmail(), st.getPhone(), st.getId());
    }

    /**
     * 联合查询学生 + 班级 + 专业 + 学院
     */
    public List<Student> findAllWithFullInfo() {
        String sql = "SELECT st.*, cg.name AS classgroup_name, s.name AS speciality_name, co.name AS college_name " +
                     "FROM student st " +
                     "LEFT JOIN classgroup cg ON st.classgroup_id = cg.id " +
                     "LEFT JOIN speciality s ON cg.speciality_id = s.id " +
                     "LEFT JOIN college co ON s.college_id = co.id " +
                     "ORDER BY st.id";
        try {
            return runner.query(sql, listHandler());
        } catch (SQLException e) {
            throw new RuntimeException("查询学生列表失败", e);
        }
    }

    /**
     * 根据关键词搜索
     */
    public List<Student> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAllWithFullInfo();
        }
        String sql = "SELECT st.*, cg.name AS classgroup_name, s.name AS speciality_name, co.name AS college_name " +
                     "FROM student st " +
                     "LEFT JOIN classgroup cg ON st.classgroup_id = cg.id " +
                     "LEFT JOIN speciality s ON cg.speciality_id = s.id " +
                     "LEFT JOIN college co ON s.college_id = co.id " +
                     "WHERE st.name LIKE ? OR st.student_no LIKE ? OR cg.name LIKE ? OR s.name LIKE ? " +
                     "ORDER BY st.id";
        String kw = "%" + keyword + "%";
        try {
            return runner.query(sql, listHandler(), kw, kw, kw, kw);
        } catch (SQLException e) {
            throw new RuntimeException("搜索学生失败", e);
        }
    }

    /**
     * 统计各专业各年级学生人数
     */
    public List<Map<String, Object>> statsBySpecialityAndGrade() {
        String sql = "SELECT s.name AS speciality_name, cg.grade, COUNT(*) AS student_count " +
                     "FROM student st " +
                     "JOIN classgroup cg ON st.classgroup_id = cg.id " +
                     "JOIN speciality s ON cg.speciality_id = s.id " +
                     "GROUP BY s.name, cg.grade " +
                     "ORDER BY s.name, cg.grade";
        try {
            return runner.query(sql, new org.apache.commons.dbutils.handlers.MapListHandler());
        } catch (SQLException e) {
            throw new RuntimeException("统计学生人数失败", e);
        }
    }

    /**
     * 根据班级 ID 查询学生
     */
    public List<Student> findByClassgroupId(Long classgroupId) {
        String sql = "SELECT st.*, cg.name AS classgroup_name, s.name AS speciality_name, co.name AS college_name " +
                     "FROM student st " +
                     "LEFT JOIN classgroup cg ON st.classgroup_id = cg.id " +
                     "LEFT JOIN speciality s ON cg.speciality_id = s.id " +
                     "LEFT JOIN college co ON s.college_id = co.id " +
                     "WHERE st.classgroup_id = ?";
        try {
            return runner.query(sql, listHandler(), classgroupId);
        } catch (SQLException e) {
            throw new RuntimeException("根据班级查询学生失败", e);
        }
    }
}
