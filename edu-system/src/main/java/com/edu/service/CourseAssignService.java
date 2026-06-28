package com.edu.service;

import com.edu.dao.CourseAssignDao;
import com.edu.model.CourseAssign;
import java.util.List;

public class CourseAssignService {
    private final CourseAssignDao dao = new CourseAssignDao();

    public List<CourseAssign> list(String keyword) {
        return dao.search(keyword);
    }

    public CourseAssign getById(Long id) {
        return dao.findById(id);
    }

    public CourseAssign create(CourseAssign assign) {
        Long id = dao.insert(assign);
        assign.setId(id);
        return assign;
    }

    public boolean delete(Long id) {
        return dao.delete(id) > 0;
    }

    public List<CourseAssign> getClassSchedule(Long classgroupId) {
        return dao.findByClassgroupId(classgroupId);
    }
}
