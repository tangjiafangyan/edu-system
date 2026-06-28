package com.edu.servlet;

import com.edu.model.Teacher;
import com.edu.service.TeacherService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TeacherServlet extends BaseServlet {
    private final TeacherService service = new TeacherService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id != null) {
            Teacher teacher = service.getById(id);
            if (teacher == null) {
                sendError(resp, 404, "教师不存在");
            } else {
                sendSuccess(resp, teacher);
            }
        } else {
            sendSuccess(resp, service.list(getKeyword(req)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Teacher teacher = readJsonBody(req, Teacher.class);
        Teacher created = service.create(teacher);
        sendSuccess(resp, created);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id == null) {
            sendError(resp, 400, "缺少ID参数");
            return;
        }
        Teacher teacher = readJsonBody(req, Teacher.class);
        Teacher updated = service.update(id, teacher);
        sendSuccess(resp, updated);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id == null) {
            sendError(resp, 400, "缺少ID参数");
            return;
        }
        if (service.delete(id)) {
            sendSuccess(resp, "删除成功");
        } else {
            sendError(resp, 404, "教师不存在");
        }
    }
}
