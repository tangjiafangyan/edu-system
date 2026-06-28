package com.edu.servlet;

import com.edu.model.CourseTask;
import com.edu.service.CourseTaskService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CourseTaskServlet extends BaseServlet {
    private final CourseTaskService service = new CourseTaskService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id != null) {
            CourseTask ct = service.getById(id);
            if (ct == null) sendError(resp, 404, "课程任务不存在");
            else sendSuccess(resp, ct);
        } else {
            sendSuccess(resp, service.list(getKeyword(req)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sendSuccess(resp, service.create(readJsonBody(req, CourseTask.class)));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id == null) { sendError(resp, 400, "缺少ID参数"); return; }
        sendSuccess(resp, service.update(id, readJsonBody(req, CourseTask.class)));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id == null) { sendError(resp, 400, "缺少ID参数"); return; }
        if (service.delete(id)) sendSuccess(resp, "删除成功");
        else sendError(resp, 404, "课程任务不存在");
    }
}
