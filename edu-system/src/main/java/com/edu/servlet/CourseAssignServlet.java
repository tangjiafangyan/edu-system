package com.edu.servlet;

import com.edu.model.CourseAssign;
import com.edu.service.CourseAssignService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CourseAssignServlet extends BaseServlet {
    private final CourseAssignService service = new CourseAssignService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id != null) {
            CourseAssign ca = service.getById(id);
            if (ca == null) sendError(resp, 404, "必修课指派不存在");
            else sendSuccess(resp, ca);
        } else {
            // 支持 ?classgroupId= 参数查询班级课表
            String classgroupIdStr = req.getParameter("classgroupId");
            if (classgroupIdStr != null) {
                sendSuccess(resp, service.getClassSchedule(Long.parseLong(classgroupIdStr)));
            } else {
                sendSuccess(resp, service.list(getKeyword(req)));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sendSuccess(resp, service.create(readJsonBody(req, CourseAssign.class)));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractId(req);
        if (id == null) { sendError(resp, 400, "缺少ID参数"); return; }
        if (service.delete(id)) sendSuccess(resp, "删除成功");
        else sendError(resp, 404, "必修课指派不存在");
    }
}
