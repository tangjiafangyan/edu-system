package com.edu.service;

import com.edu.dao.TeacherDao;
import com.edu.model.Teacher;
import java.util.List;

public class TeacherService {
    private final TeacherDao dao = new TeacherDao();

    public List<Teacher> list(String keyword) {
        return dao.search(keyword);
    }

    public Teacher getById(Long id) {
        return dao.findByIdWithCollege(id);
    }

    public Teacher create(Teacher teacher) {
        Long id = dao.insert(teacher);
        teacher.setId(id);
        return teacher;
    }

    public Teacher update(Long id, Teacher teacher) {
        teacher.setId(id);
        dao.update(teacher);
        return teacher;
    }

    public boolean delete(Long id) {
        return dao.delete(id) > 0;
    }
}
