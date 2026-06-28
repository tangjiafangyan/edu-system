package com.edu.servlet;

import com.edu.model.ClassGroup;
import com.edu.service.ClassGroupService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ClassGroupServlet extends BaseServlet {
    private final ClassGroupService service = new ClassGroupService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id != null) {
            ClassGroup cg = service.getById(id);
            if (cg == null) sendError(resp, 404, "班级不存在");
            else sendSuccess(resp, cg);
        } else {
            sendSuccess(resp, service.list(getKeyword(req)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ClassGroup cg = readJsonBody(req, ClassGroup.class);
        sendSuccess(resp, service.create(cg));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id == null) { sendError(resp, 400, "缺少ID参数"); return; }
        sendSuccess(resp, service.update(id, readJsonBody(req, ClassGroup.class)));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id == null) { sendError(resp, 400, "缺少ID参数"); return; }
        if (service.delete(id)) sendSuccess(resp, "删除成功");
        else sendError(resp, 404, "班级不存在");
    }
}
