package com.edu.servlet;

import com.edu.model.Course;
import com.edu.service.CourseService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CourseServlet extends BaseServlet {
    private final CourseService service = new CourseService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id != null) {
            Course c = service.getById(id);
            if (c == null) sendError(resp, 404, "课程不存在");
            else sendSuccess(resp, c);
        } else {
            sendSuccess(resp, service.list(getKeyword(req)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sendSuccess(resp, service.create(readJsonBody(req, Course.class)));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id == null) { sendError(resp, 400, "缺少ID参数"); return; }
        sendSuccess(resp, service.update(id, readJsonBody(req, Course.class)));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id == null) { sendError(resp, 400, "缺少ID参数"); return; }
        if (service.delete(id)) sendSuccess(resp, "删除成功");
        else sendError(resp, 404, "课程不存在");
    }
}
