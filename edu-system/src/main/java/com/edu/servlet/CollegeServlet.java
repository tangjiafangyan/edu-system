package com.edu.servlet;

import com.edu.model.College;
import com.edu.service.CollegeService;
import com.edu.util.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CollegeServlet extends BaseServlet {
    private final CollegeService service = new CollegeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id != null) {
            College college = service.getById(id);
            if (college == null) {
                sendError(resp, 404, "学院不存在");
            } else {
                sendSuccess(resp, college);
            }
        } else {
            sendSuccess(resp, service.list(getKeyword(req)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        College college = readJsonBody(req, College.class);
        College created = service.create(college);
        sendSuccess(resp, created);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id == null) {
            sendError(resp, 400, "缺少ID参数");
            return;
        }
        College college = readJsonBody(req, College.class);
        College updated = service.update(id, college);
        sendSuccess(resp, updated);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id == null) {
            sendError(resp, 400, "缺少ID参数");
            return;
        }
        boolean deleted = service.delete(id);
        if (deleted) {
            sendSuccess(resp, "删除成功");
        } else {
            sendError(resp, 404, "学院不存在");
        }
    }
}
