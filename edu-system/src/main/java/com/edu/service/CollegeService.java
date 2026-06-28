package com.edu.service;

import com.edu.dao.CollegeDao;
import com.edu.model.College;
import java.util.List;

public class CollegeService {
    private final CollegeDao dao = new CollegeDao();

    public List<College> list(String keyword) {
        return dao.search(keyword);
    }

    public College getById(Long id) {
        return dao.findById(id);
    }

    public College create(College college) {
        Long id = dao.insert(college);
        college.setId(id);
        return college;
    }

    public College update(Long id, College college) {
        college.setId(id);
        dao.update(college);
        return college;
    }

    public boolean delete(Long id) {
        return dao.delete(id) > 0;
    }
}
