package com.edu.servlet;

import com.edu.model.CourseSubtask;
import com.edu.service.CourseSubtaskService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CourseSubtaskServlet extends BaseServlet {
    private final CourseSubtaskService service = new CourseSubtaskService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id != null) {
            CourseSubtask cs = service.getById(id);
            if (cs == null) sendError(resp, 404, "课程子任务不存在");
            else sendSuccess(resp, cs);
        } else {
            sendSuccess(resp, service.list(getKeyword(req)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sendSuccess(resp, service.create(readJsonBody(req, CourseSubtask.class)));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id == null) { sendError(resp, 400, "缺少ID参数"); return; }
        if (service.delete(id)) sendSuccess(resp, "删除成功");
        else sendError(resp, 404, "课程子任务不存在");
    }
}
