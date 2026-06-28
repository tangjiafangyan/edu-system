/**
 * API 请求封装
 * 统一管理所有后端 API 调用
 */
const BASE_URL = '/edu-system/api';

const api = {
    /**
     * 通用 GET 请求
     */
    async get(url, params = {}) {
        const query = new URLSearchParams(params).toString();
        const fullUrl = query ? `${BASE_URL}${url}?${query}` : `${BASE_URL}${url}`;
        const res = await axios.get(fullUrl);
        return res.data;
    },

    /**
     * 通用 POST 请求
     */
    async post(url, data) {
        const res = await axios.post(`${BASE_URL}${url}`, data);
        return res.data;
    },

    /**
     * 通用 PUT 请求
     */
    async put(url, data) {
        const res = await axios.put(`${BASE_URL}${url}`, data);
        return res.data;
    },

    /**
     * 通用 DELETE 请求
     */
    async del(url) {
        const res = await axios.delete(`${BASE_URL}${url}`);
        return res.data;
    },

    /* ========== 学院 ========== */
    colleges: {
        list:  (kw)     => api.get('/colleges', kw ? { keyword: kw } : {}),
        get:   (id)     => api.get(`/colleges/${id}`),
        create:(data)   => api.post('/colleges', data),
        update:(id,data)=> api.put(`/colleges/${id}`, data),
        delete:(id)     => api.del(`/colleges/${id}`),
    },

    /* ========== 教师 ========== */
    teachers: {
        list:  (kw)     => api.get('/teachers', kw ? { keyword: kw } : {}),
        get:   (id)     => api.get(`/teachers/${id}`),
        create:(data)   => api.post('/teachers', data),
        update:(id,data)=> api.put(`/teachers/${id}`, data),
        delete:(id)     => api.del(`/teachers/${id}`),
    },

    /* ========== 专业 ========== */
    specialities: {
        list:  (kw)     => api.get('/specialities', kw ? { keyword: kw } : {}),
        get:   (id)     => api.get(`/specialities/${id}`),
        create:(data)   => api.post('/specialities', data),
        update:(id,data)=> api.put(`/specialities/${id}`, data),
        delete:(id)     => api.del(`/specialities/${id}`),
    },

    /* ========== 班级 ========== */
    classgroups: {
        list:  (kw)     => api.get('/classgroups', kw ? { keyword: kw } : {}),
        get:   (id)     => api.get(`/classgroups/${id}`),
        create:(data)   => api.post('/classgroups', data),
        update:(id,data)=> api.put(`/classgroups/${id}`, data),
        delete:(id)     => api.del(`/classgroups/${id}`),
    },

    /* ========== 学生 ========== */
    students: {
        list:  (kw)     => api.get('/students', kw ? { keyword: kw } : {}),
        get:   (id)     => api.get(`/students/${id}`),
        create:(data)   => api.post('/students', data),
        update:(id,data)=> api.put(`/students/${id}`, data),
        delete:(id)     => api.del(`/students/${id}`),
    },

    /* ========== 课程 ========== */
    courses: {
        list:  (kw)     => api.get('/courses', kw ? { keyword: kw } : {}),
        get:   (id)     => api.get(`/courses/${id}`),
        create:(data)   => api.post('/courses', data),
        update:(id,data)=> api.put(`/courses/${id}`, data),
        delete:(id)     => api.del(`/courses/${id}`),
    },

    /* ========== 课程任务 ========== */
    courseTasks: {
        list:  (kw)     => api.get('/course-tasks', kw ? { keyword: kw } : {}),
        get:   (id)     => api.get(`/course-tasks/${id}`),
        create:(data)   => api.post('/course-tasks', data),
        update:(id,data)=> api.put(`/course-tasks/${id}`, data),
        delete:(id)     => api.del(`/course-tasks/${id}`),
    },

    /* ========== 必修课指派 ========== */
    courseAssigns: {
        list:  (kw)     => api.get('/course-assigns', kw ? { keyword: kw } : {}),
        get:   (id)     => api.get(`/course-assigns/${id}`),
        create:(data)   => api.post('/course-assigns', data),
        delete:(id)     => api.del(`/course-assigns/${id}`),
        byClass: (cid)  => api.get('/course-assigns', { classgroupId: cid }),
    },

    /* ========== 课程子任务 ========== */
    courseSubtasks: {
        list:  (kw)     => api.get('/course-subtasks', kw ? { keyword: kw } : {}),
        get:   (id)     => api.get(`/course-subtasks/${id}`),
        create:(data)   => api.post('/course-subtasks', data),
        delete:(id)     => api.del(`/course-subtasks/${id}`),
    },

    /* ========== 选修课 ========== */
    courseElections: {
        list:  (kw)     => api.get('/course-elections', kw ? { keyword: kw } : {}),
        get:   (id)     => api.get(`/course-elections/${id}`),
        create:(data)   => api.post('/course-elections', data),
        delete:(id)     => api.del(`/course-elections/${id}`),
        byStudent: (sid) => api.get('/course-elections', { studentId: sid }),
    },

    /* ========== 统计 ========== */
    stats: {
        overview:    () => api.get('/stats'),
        speciality:  () => api.get('/stats/speciality'),
    }
};
