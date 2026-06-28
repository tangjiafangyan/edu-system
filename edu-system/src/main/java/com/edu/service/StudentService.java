package com.edu.service;

import com.edu.dao.StudentDao;
import com.edu.model.Student;
import java.util.List;
import java.util.Map;

public class StudentService {
    private final StudentDao dao = new StudentDao();

    public List<Student> list(String keyword) {
        return dao.search(keyword);
    }

    public Student getById(Long id) {
        return dao.findById(id);
    }

    public Student create(Student student) {
        Long id = dao.insert(student);
        student.setId(id);
        return student;
    }

    public Student update(Long id, Student student) {
        student.setId(id);
        dao.update(student);
        return student;
    }

    public boolean delete(Long id) {
        return dao.delete(id) > 0;
    }

    public List<Map<String, Object>> statsBySpecialityAndGrade() {
        return dao.statsBySpecialityAndGrade();
    }
}
