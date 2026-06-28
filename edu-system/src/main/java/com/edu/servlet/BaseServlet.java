package com.edu.servlet;

import com.edu.util.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Servlet 基类
 * 统一处理 JSON 请求/响应、路径参数解析、异常处理
 */
public abstract class BaseServlet extends HttpServlet {

    /**
     * 从请求路径中提取 ID
     * 例如 /api/colleges/123 → 123
     */
    protected Long extractId(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return null;
        }
        // 去掉首尾的 /
        String idStr = pathInfo.replaceAll("^/|/$", "");
        if (idStr.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 读取请求体 JSON 并转为 Map
     */
    protected Map<String, Object> readJsonBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String body = sb.toString();
        if (body.isEmpty()) {
            return new LinkedHashMap<>();
        }
        return JsonUtil.fromJsonToMap(body);
    }

    /**
     * 读取请求体 JSON 并转为指定类型
     */
    protected <T> T readJsonBody(HttpServletRequest req, Class<T> clazz) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return JsonUtil.fromJson(sb.toString(), clazz);
    }

    /**
     * 发送成功 JSON 响应
     */
    protected void sendSuccess(HttpServletResponse resp, Object data) throws IOException {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", data);
        sendJson(resp, HttpServletResponse.SC_OK, result);
    }

    /**
     * 发送错误 JSON 响应
     */
    protected void sendError(HttpServletResponse resp, int statusCode, String message) throws IOException {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", statusCode);
        result.put("message", message);
        result.put("data", null);
        sendJson(resp, statusCode, result);
    }

    /**
     * 输出 JSON
     */
    protected void sendJson(HttpServletResponse resp, int status, Object data) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(JsonUtil.toJson(data));
        out.flush();
    }

    /**
     * 获取查询参数 keyword
     */
    protected String getKeyword(HttpServletRequest req) {
        return req.getParameter("keyword");
    }
}
