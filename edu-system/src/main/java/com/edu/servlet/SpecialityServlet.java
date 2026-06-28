package com.edu.servlet;

import com.edu.model.Speciality;
import com.edu.service.SpecialityService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SpecialityServlet extends BaseServlet {
    private final SpecialityService service = new SpecialityService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id != null) {
            Speciality s = service.getById(id);
            if (s == null) {
                sendError(resp, 404, "专业不存在");
            } else {
                sendSuccess(resp, s);
            }
        } else {
            sendSuccess(resp, service.list(getKeyword(req)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Speciality s = readJsonBody(req, Speciality.class);
        sendSuccess(resp, service.create(s));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id == null) { sendError(resp, 400, "缺少ID参数"); return; }
        sendSuccess(resp, service.update(id, readJsonBody(req, Speciality.class)));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id == null) { sendError(resp, 400, "缺少ID参数"); return; }
        if (service.delete(id)) { sendSuccess(resp, "删除成功"); }
        else { sendError(resp, 404, "专业不存在"); }
    }
}
