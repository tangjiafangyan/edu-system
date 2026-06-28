package com.edu.servlet;

import com.edu.model.CourseElection;
import com.edu.service.CourseElectionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CourseElectionServlet extends BaseServlet {
    private final CourseElectionService service = new CourseElectionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id != null) {
            CourseElection ce = service.getById(id);
            if (ce == null) sendError(resp, 404, "选课记录不存在");
            else sendSuccess(resp, ce);
        } else {
            // 支持 ?studentId= 查询学生选课
            String studentIdStr = req.getParameter("studentId");
            if (studentIdStr != null) {
                sendSuccess(resp, service.getStudentSchedule(Long.parseLong(studentIdStr)));
            } else {
                sendSuccess(resp, service.list(getKeyword(req)));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sendSuccess(resp, service.create(readJsonBody(req, CourseElection.class)));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id == null) { sendError(resp, 400, "缺少ID参数"); return; }
        if (service.delete(id)) sendSuccess(resp, "删除成功");
        else sendError(resp, 404, "选课记录不存在");
    }
}
