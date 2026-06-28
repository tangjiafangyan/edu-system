package com.edu.service;

import com.edu.dao.SpecialityDao;
import com.edu.model.Speciality;
import java.util.List;

public class SpecialityService {
    private final SpecialityDao dao = new SpecialityDao();

    public List<Speciality> list(String keyword) {
        return dao.search(keyword);
    }

    public Speciality getById(Long id) {
        return dao.findById(id);
    }

    public Speciality create(Speciality speciality) {
        Long id = dao.insert(speciality);
        speciality.setId(id);
        return speciality;
    }

    public Speciality update(Long id, Speciality speciality) {
        speciality.setId(id);
        dao.update(speciality);
        return speciality;
    }

    public boolean delete(Long id) {
        return dao.delete(id) > 0;
    }
}
