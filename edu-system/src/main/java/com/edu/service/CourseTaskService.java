package com.edu.service;

import com.edu.dao.CourseTaskDao;
import com.edu.model.CourseTask;
import java.util.List;

public class CourseTaskService {
    private final CourseTaskDao dao = new CourseTaskDao();

    public List<CourseTask> list(String keyword) {
        return dao.search(keyword);
    }

    public CourseTask getById(Long id) {
        return dao.findById(id);
    }

    public CourseTask create(CourseTask task) {
        Long id = dao.insert(task);
        task.setId(id);
        return task;
    }

    public CourseTask update(Long id, CourseTask task) {
        task.setId(id);
        dao.update(task);
        return task;
    }

    public boolean delete(Long id) {
        return dao.delete(id) > 0;
    }
}
