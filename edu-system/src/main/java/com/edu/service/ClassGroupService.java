package com.edu.service;

import com.edu.dao.ClassGroupDao;
import com.edu.model.ClassGroup;
import java.util.List;

public class ClassGroupService {
    private final ClassGroupDao dao = new ClassGroupDao();

    public List<ClassGroup> list(String keyword) {
        return dao.search(keyword);
    }

    public ClassGroup getById(Long id) {
        return dao.findById(id);
    }

    public ClassGroup create(ClassGroup classGroup) {
        Long id = dao.insert(classGroup);
        classGroup.setId(id);
        return classGroup;
    }

    public ClassGroup update(Long id, ClassGroup classGroup) {
        classGroup.setId(id);
        dao.update(classGroup);
        return classGroup;
    }

    public boolean delete(Long id) {
        return dao.delete(id) > 0;
    }
}
