-- ========================================
-- 教务管理系统 - 测试数据
-- ========================================

USE edu_system;

-- 学院
INSERT INTO college (name, dean, description) VALUES
('信息工程学院', '张教授', '培养信息技术领域的高级专门人才'),
('数学与统计学院', '李教授', '培养数学与统计学领域人才');

-- 教师
INSERT INTO teacher (name, title, gender, college_id, email, phone) VALUES
('王老师', '教授',   '男', 1, 'wang@ncwu.edu.cn',   '13800001001'),
('刘老师', '副教授', '女', 1, 'liu@ncwu.edu.cn',    '13800001002'),
('陈老师', '讲师',   '男', 2, 'chen@ncwu.edu.cn',   '13800001003'),
('赵老师', '教授',   '女', 2, 'zhao@ncwu.edu.cn',   '13800001004');

-- 专业
INSERT INTO speciality (name, college_id, description) VALUES
('计算机科学与技术', 1, '培养计算机软硬件系统设计人才'),
('软件工程',         1, '培养大型软件开发与管理人才'),
('应用数学',         2, '培养数学应用与分析人才'),
('统计学',           2, '培养数据统计与分析人才');

-- 班级
INSERT INTO classgroup (name, speciality_id, grade) VALUES
('计科2022-1班', 1, 2022),
('计科2022-2班', 1, 2022),
('软件2022-1班', 2, 2022),
('软件2022-2班', 2, 2022),
('数学2022-1班', 3, 2022),
('数学2022-2班', 3, 2022),
('统计2022-1班', 4, 2022),
('统计2022-2班', 4, 2022);

-- 学生
INSERT INTO student (name, gender, student_no, classgroup_id, email, phone) VALUES
('张三', '男', '2022001', 1, 'zhangsan@ncwu.edu.cn',  '13900002001'),
('李四', '女', '2022002', 1, 'lisi@ncwu.edu.cn',      '13900002002'),
('王五', '男', '2022003', 2, 'wangwu@ncwu.edu.cn',    '13900002003'),
('赵六', '女', '2022004', 2, 'zhaoliu@ncwu.edu.cn',   '13900002004'),
('孙七', '男', '2022005', 3, 'sunqi@ncwu.edu.cn',     '13900002005'),
('周八', '女', '2022006', 3, 'zhouba@ncwu.edu.cn',    '13900002006'),
('吴九', '男', '2022007', 4, 'wujiu@ncwu.edu.cn',     '13900002007'),
('郑十', '女', '2022008', 4, 'zhengshi@ncwu.edu.cn',  '13900002008');

-- 课程
INSERT INTO course (name, code, credit, hours, type, description) VALUES
('Java程序设计',    'CS101', 4.0, 64, '必修', 'Java语言基础与进阶'),
('数据结构',        'CS102', 4.0, 64, '必修', '常见数据结构与算法'),
('数据库原理',      'CS103', 3.0, 48, '必修', '关系数据库理论与SQL'),
('软件工程',        'CS201', 3.0, 48, '必修', '软件开发生命周期与管理'),
('Web前端开发',     'CS202', 2.0, 32, '选修', 'HTML/CSS/JavaScript基础'),
('Python数据分析',  'CS203', 2.0, 32, '选修', 'Python数据处理与分析');

-- 课程任务
INSERT INTO course_task (course_id, teacher_id, semester, location, max_students) VALUES
(1, 1, '2025-2026学年第2学期', '教学楼A101', 60),
(2, 2, '2025-2026学年第2学期', '教学楼A102', 60),
(3, 1, '2025-2026学年第2学期', '教学楼A103', 50),
(4, 2, '2025-2026学年第2学期', '教学楼A104', 50),
(5, 3, '2025-2026学年第2学期', '教学楼B201', 40),
(6, 4, '2025-2026学年第2学期', '教学楼B202', 40);

-- 必修课程指派（指派给班级）
INSERT INTO course_assign (course_task_id, classgroup_id) VALUES
(1, 1), (1, 2),  -- Java程序设计 → 计科班
(2, 1), (2, 2),  -- 数据结构 → 计科班
(3, 1), (3, 2),  -- 数据库原理 → 计科班
(4, 3), (4, 4);  -- 软件工程 → 软件班
