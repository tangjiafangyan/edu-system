package com.edu.service;

import com.edu.dao.CourseDao;
import com.edu.model.Course;
import java.util.List;

public class CourseService {
    private final CourseDao dao = new CourseDao();

    public List<Course> list(String keyword) {
        return dao.search(keyword);
    }

    public Course getById(Long id) {
        return dao.findById(id);
    }

    public Course create(Course course) {
        Long id = dao.insert(course);
        course.setId(id);
        return course;
    }

    public Course update(Long id, Course course) {
        course.setId(id);
        dao.update(course);
        return course;
    }

    public boolean delete(Long id) {
        return dao.delete(id) > 0;
    }
}
