package com.edu.service;

import com.edu.dao.CourseSubtaskDao;
import com.edu.model.CourseSubtask;
import java.util.List;

public class CourseSubtaskService {
    private final CourseSubtaskDao dao = new CourseSubtaskDao();

    public List<CourseSubtask> list(String keyword) {
        return dao.search(keyword);
    }

    public CourseSubtask getById(Long id) {
        return dao.findById(id);
    }

    public CourseSubtask create(CourseSubtask subtask) {
        Long id = dao.insert(subtask);
        subtask.setId(id);
        return subtask;
    }

    public boolean delete(Long id) {
        return dao.delete(id) > 0;
    }
}
