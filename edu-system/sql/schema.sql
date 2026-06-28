-- ========================================
-- 教务管理系统 - 数据库建表脚本
-- ========================================

CREATE DATABASE IF NOT EXISTS edu_system
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE edu_system;

-- 1. 学院表
CREATE TABLE IF NOT EXISTS college (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    dean       VARCHAR(50),
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. 教师表
CREATE TABLE IF NOT EXISTS teacher (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    title      VARCHAR(50) COMMENT '职称',
    gender     VARCHAR(10),
    college_id BIGINT NOT NULL,
    email      VARCHAR(100),
    phone      VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (college_id) REFERENCES college(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 专业表
CREATE TABLE IF NOT EXISTS speciality (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    college_id  BIGINT NOT NULL,
    description TEXT,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (college_id) REFERENCES college(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 班级表
CREATE TABLE IF NOT EXISTS classgroup (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    speciality_id BIGINT NOT NULL,
    grade         INT COMMENT '入学年份',
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (speciality_id) REFERENCES speciality(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. 学生表
CREATE TABLE IF NOT EXISTS student (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(50) NOT NULL,
    gender        VARCHAR(10),
    student_no    VARCHAR(50) UNIQUE NOT NULL COMMENT '学号',
    classgroup_id BIGINT NOT NULL,
    email         VARCHAR(100),
    phone         VARCHAR(20),
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (classgroup_id) REFERENCES classgroup(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. 课程表
CREATE TABLE IF NOT EXISTS course (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    code        VARCHAR(50) UNIQUE NOT NULL COMMENT '课程编号',
    credit      DECIMAL(3,1) COMMENT '学分',
    hours       INT COMMENT '学时',
    type        VARCHAR(20) DEFAULT '必修' COMMENT '必修/选修',
    description TEXT,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. 课程任务表（课程指派给教师）
CREATE TABLE IF NOT EXISTS course_task (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id    BIGINT NOT NULL,
    teacher_id   BIGINT NOT NULL,
    semester     VARCHAR(50) COMMENT '学期',
    location     VARCHAR(100) COMMENT '上课地点',
    max_students INT DEFAULT 60 COMMENT '最大学生数',
    description  TEXT,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id)  REFERENCES course(id) ON DELETE RESTRICT,
    FOREIGN KEY (teacher_id) REFERENCES teacher(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. 必修课程指派表（指派给班级）
CREATE TABLE IF NOT EXISTS course_assign (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_task_id BIGINT NOT NULL,
    classgroup_id  BIGINT NOT NULL,
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_task_id) REFERENCES course_task(id) ON DELETE RESTRICT,
    FOREIGN KEY (classgroup_id)  REFERENCES classgroup(id) ON DELETE RESTRICT,
    UNIQUE KEY uk_task_class (course_task_id, classgroup_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9. 课程子任务表（可选：课程任务关联到班级）
CREATE TABLE IF NOT EXISTS course_subtask (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_task_id BIGINT NOT NULL,
    classgroup_id  BIGINT NOT NULL,
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_task_id) REFERENCES course_task(id) ON DELETE RESTRICT,
    FOREIGN KEY (classgroup_id)  REFERENCES classgroup(id) ON DELETE RESTRICT,
    UNIQUE KEY uk_subtask_class (course_task_id, classgroup_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 10. 选修课表（学生选课）
CREATE TABLE IF NOT EXISTS course_election (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id     BIGINT NOT NULL,
    course_task_id BIGINT NOT NULL,
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id)     REFERENCES student(id) ON DELETE RESTRICT,
    FOREIGN KEY (course_task_id) REFERENCES course_task(id) ON DELETE RESTRICT,
    UNIQUE KEY uk_student_task (student_id, course_task_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
