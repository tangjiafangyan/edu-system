package com.edu.service;

import com.edu.dao.CourseElectionDao;
import com.edu.model.CourseElection;
import java.util.List;

public class CourseElectionService {
    private final CourseElectionDao dao = new CourseElectionDao();

    public List<CourseElection> list(String keyword) {
        return dao.search(keyword);
    }

    public CourseElection getById(Long id) {
        return dao.findById(id);
    }

    public CourseElection create(CourseElection election) {
        Long id = dao.insert(election);
        election.setId(id);
        return election;
    }

    public boolean delete(Long id) {
        return dao.delete(id) > 0;
    }

    public List<CourseElection> getStudentSchedule(Long studentId) {
        return dao.findByStudentId(studentId);
    }
}
