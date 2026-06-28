package com.edu.servlet;

import com.edu.service.StudentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 统计汇总 Servlet
 */
public class StatsServlet extends BaseServlet {
    private final StudentService studentService = new StudentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) pathInfo = "/";

        switch (pathInfo.replaceAll("^/|/$", "")) {
            case "speciality":
                // GET /api/stats/speciality - 各专业各年级学生人数统计
                sendSuccess(resp, studentService.statsBySpecialityAndGrade());
                break;
            default:
                // 仪表盘概览数据
                java.util.Map<String, Object> overview = new java.util.LinkedHashMap<>();
                overview.put("colleges", new com.edu.dao.CollegeDao().count("SELECT COUNT(*) FROM college"));
                overview.put("teachers", new com.edu.dao.TeacherDao().count("SELECT COUNT(*) FROM teacher"));
                overview.put("specialities", new com.edu.dao.SpecialityDao().count("SELECT COUNT(*) FROM speciality"));
                overview.put("classgroups", new com.edu.dao.ClassGroupDao().count("SELECT COUNT(*) FROM classgroup"));
                overview.put("students", new com.edu.dao.StudentDao().count("SELECT COUNT(*) FROM student"));
                overview.put("courses", new com.edu.dao.CourseDao().count("SELECT COUNT(*) FROM course"));
                sendSuccess(resp, overview);
                break;
        }
    }
}
