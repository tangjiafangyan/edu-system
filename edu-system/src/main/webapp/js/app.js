/**
 * 教务管理系统 - Vue 3 主应用
 */

// ==================== 通用列表组件工厂 ====================
function createListComponent(config) {
    const { title, apiGroup, columns, formFields, refData } = config;
    return {
        template: `
        <div>
            <h3>${title}</h3>
            <div class="toolbar">
                <input type="search" v-model="keyword" @input="onSearch" placeholder="搜索..." style="width:260px">
                <button @click="openAdd">+ 新增</button>
            </div>
            <div v-if="loading" aria-busy="true">加载中...</div>
            <table v-else>
                <thead><tr>${columns.map(c => `<th>${c.label}</th>`).join('')}<th style="width:160px">操作</th></tr></thead>
                <tbody>
                    <tr v-if="list.length===0"><td colspan="${columns.length+1}" style="text-align:center">暂无数据</td></tr>
                    <tr v-for="row in list" :key="row.id">
                        ${columns.map(c => `<td>{{ row.${c.field} || '-' }}</td>`).join('')}
                        <td>
                            <button class="secondary small" @click="openEdit(row)">编辑</button>
                            <button class="contrast small" @click="confirmDelete(row)">删除</button>
                        </td>
                    </tr>
                </tbody>
            </table>

            <!-- 新增/编辑对话框 -->
            <dialog :open="showModal">
                <article>
                    <header><strong>{{ editingId ? '编辑' : '新增' }}${title.slice(0, -2)}</strong></header>
                    ${formFields.map(f => {
                        if (f.type === 'select') {
                            return `<label>${f.label}<select v-model="form.${f.field}"><option value="">--请选择--</option><option v-for="opt in ${f.options}" :key="opt.id" :value="opt.id">{{ opt.name }}</option></select></label>`;
                        }
                        return `<label>${f.label}<input type="${f.type||'text'}" v-model="form.${f.field}"></label>`;
                    }).join('')}
                    <footer>
                        <button class="secondary" @click="showModal=false">取消</button>
                        <button @click="save">保存</button>
                    </footer>
                </article>
            </dialog>

            <!-- 删除确认 -->
            <dialog :open="showDeleteConfirm">
                <article>
                    <header>确认删除</header>
                    <p>确定要删除这条记录吗？此操作不可撤销。</p>
                    <footer>
                        <button class="secondary" @click="showDeleteConfirm=false">取消</button>
                        <button class="contrast" @click="doDelete">确认删除</button>
                    </footer>
                </article>
            </dialog>
        </div>`,
        data() {
            const initForm = {};
            formFields.forEach(f => initForm[f.field] = f.default || '');
            return {
                list: [], loading: false, keyword: '',
                showModal: false, showDeleteConfirm: false,
                editingId: null, deletingId: null,
                form: { ...initForm },
                ...(refData || {})
            };
        },
        created() {
            this.loadData();
            // 加载引用数据
            if (refData) {
                for (const [key, loader] of Object.entries(refData.loaders || {})) {
                    loader().then(res => { this[key] = res.data; });
                }
            }
        },
        methods: {
            async loadData() {
                this.loading = true;
                try {
                    const res = await apiGroup.list(this.keyword);
                    this.list = res.data || [];
                } catch (e) { console.error(e); }
                this.loading = false;
            },
            onSearch() {
                clearTimeout(this._timer);
                this._timer = setTimeout(() => this.loadData(), 300);
            },
            openAdd() {
                this.editingId = null;
                formFields.forEach(f => this.form[f.field] = f.default || '');
                this.showModal = true;
            },
            openEdit(row) {
                this.editingId = row.id;
                formFields.forEach(f => this.form[f.field] = row[f.field] || '');
                this.showModal = true;
            },
            async save() {
                try {
                    if (this.editingId) {
                        await apiGroup.update(this.editingId, this.form);
                    } else {
                        await apiGroup.create(this.form);
                    }
                    this.showModal = false;
                    this.loadData();
                } catch (e) { console.error(e); alert('保存失败'); }
            },
            confirmDelete(row) {
                this.deletingId = row.id;
                this.showDeleteConfirm = true;
            },
            async doDelete() {
                try {
                    await apiGroup.delete(this.deletingId);
                    this.showDeleteConfirm = false;
                    this.loadData();
                } catch (e) { console.error(e); alert('删除失败'); }
            }
        }
    };
}

// ==================== 组件定义 ====================

// 仪表盘
const Dashboard = {
    template: `
    <div>
        <h2>教务管理系统</h2>
        <div class="grid">
            <article v-for="card in cards" :key="card.key" style="text-align:center;cursor:pointer" @click="goTo(card.path)">
                <h1>{{ card.count }}</h1>
                <p>{{ card.label }}</p>
            </article>
        </div>
    </div>`,
    data() { return { cards: [] }; },
    async created() {
        try {
            const res = await api.stats.overview();
            const data = res.data;
            this.cards = [
                { key:'colleges', label:'学院', count: data.colleges||0, path:'/colleges' },
                { key:'teachers', label:'教师', count: data.teachers||0, path:'/teachers' },
                { key:'specialities', label:'专业', count: data.specialities||0, path:'/specialities' },
                { key:'classgroups', label:'班级', count: data.classgroups||0, path:'/classgroups' },
                { key:'students', label:'学生', count: data.students||0, path:'/students' },
                { key:'courses', label:'课程', count: data.courses||0, path:'/courses' },
            ];
        } catch (e) { console.error(e); }
    },
    methods: {
        goTo(path) { this.$router.push(path); }
    }
};

// 统计页面
const StatsPage = {
    template: `
    <div>
        <h3>统计汇总 - 各专业各年级学生人数</h3>
        <div v-if="loading" aria-busy="true">加载中...</div>
        <table v-else>
            <thead><tr><th>专业</th><th>入学年份</th><th>学生人数</th></tr></thead>
            <tbody>
                <tr v-if="stats.length===0"><td colspan="3" style="text-align:center">暂无数据</td></tr>
                <tr v-for="(s,i) in stats" :key="i">
                    <td>{{ s.speciality_name }}</td>
                    <td>{{ s.grade }}</td>
                    <td>{{ s.student_count }}</td>
                </tr>
            </tbody>
        </table>
    </div>`,
    data() { return { stats: [], loading: false }; },
    async created() {
        this.loading = true;
        try { const res = await api.stats.speciality(); this.stats = res.data||[]; }
        catch(e) { console.error(e); }
        this.loading = false;
    }
};

// 班级课表页面
const ClassSchedule = {
    template: `
    <div>
        <h3>班级必修课表</h3>
        <label>选择班级：<select v-model="classgroupId" @change="loadSchedule">
            <option value="">--请选择--</option>
            <option v-for="cg in classgroups" :key="cg.id" :value="cg.id">{{ cg.name }} ({{ cg.collegeName }} - {{ cg.specialityName }})</option>
        </select></label>
        <table v-if="schedule.length">
            <thead><tr><th>课程</th><th>教师</th><th>学期</th><th>班级</th></tr></thead>
            <tbody>
                <tr v-for="s in schedule" :key="s.id">
                    <td>{{ s.courseName }}</td><td>{{ s.teacherName }}</td><td>{{ s.semester }}</td><td>{{ s.classgroupName }}</td>
                </tr>
            </tbody>
        </table>
    </div>`,
    data() { return { classgroupId:'', classgroups:[], schedule:[] }; },
    async created() {
        const res = await api.classgroups.list();
        this.classgroups = res.data||[];
    },
    async loadSchedule() {
        if (!this.classgroupId) { this.schedule=[]; return; }
        const res = await api.courseAssigns.byClass(this.classgroupId);
        this.schedule = res.data||[];
    }
};

// 个人选课页面
const StudentSchedule = {
    template: `
    <div>
        <h3>个人选修课表</h3>
        <label>选择学生：<select v-model="studentId" @change="loadSchedule">
            <option value="">--请选择--</option>
            <option v-for="st in students" :key="st.id" :value="st.id">{{ st.name }} ({{ st.studentNo }}) - {{ st.classgroupName }}</option>
        </select></label>
        <table v-if="schedule.length">
            <thead><tr><th>课程</th><th>教师</th><th>学期</th><th>学生</th></tr></thead>
            <tbody>
                <tr v-for="s in schedule" :key="s.id">
                    <td>{{ s.courseName }}</td><td>{{ s.teacherName }}</td><td>{{ s.semester }}</td><td>{{ s.studentName }}</td>
                </tr>
            </tbody>
        </table>
    </div>`,
    data() { return { studentId:'', students:[], schedule:[] }; },
    async created() {
        const res = await api.students.list();
        this.students = res.data||[];
    },
    async loadSchedule() {
        if (!this.studentId) { this.schedule=[]; return; }
        const res = await api.courseElections.byStudent(this.studentId);
        this.schedule = res.data||[];
    }
};

// 学院组件
const Colleges = createListComponent({
    title: '学院管理',
    apiGroup: api.colleges,
    columns: [
        { label:'ID', field:'id' },
        { label:'学院名称', field:'name' },
        { label:'院长', field:'dean' },
        { label:'描述', field:'description' },
    ],
    formFields: [
        { label:'学院名称', field:'name', type:'text' },
        { label:'院长', field:'dean', type:'text' },
        { label:'描述', field:'description', type:'text' },
    ]
});

// 教师组件
const Teachers = createListComponent({
    title: '教师管理',
    apiGroup: api.teachers,
    columns: [
        { label:'ID', field:'id' },
        { label:'姓名', field:'name' },
        { label:'职称', field:'title' },
        { label:'性别', field:'gender' },
        { label:'所属学院', field:'collegeName' },
        { label:'邮箱', field:'email' },
        { label:'电话', field:'phone' },
    ],
    formFields: [
        { label:'姓名', field:'name', type:'text' },
        { label:'职称', field:'title', type:'text' },
        { label:'性别', field:'gender', type:'text' },
        { label:'所属学院', field:'collegeId', type:'select', options:'colleges' },
        { label:'邮箱', field:'email', type:'email' },
        { label:'电话', field:'phone', type:'text' },
    ],
    refData: {
        colleges: [],
        loaders: {
            colleges: () => api.colleges.list()
        }
    }
});

// 专业组件
const Specialities = createListComponent({
    title: '专业管理',
    apiGroup: api.specialities,
    columns: [
        { label:'ID', field:'id' },
        { label:'专业名称', field:'name' },
        { label:'所属学院', field:'collegeName' },
        { label:'描述', field:'description' },
    ],
    formFields: [
        { label:'专业名称', field:'name', type:'text' },
        { label:'所属学院', field:'collegeId', type:'select', options:'colleges' },
        { label:'描述', field:'description', type:'text' },
    ],
    refData: {
        colleges: [],
        loaders: {
            colleges: () => api.colleges.list()
        }
    }
});

// 班级组件
const ClassGroups = createListComponent({
    title: '班级管理',
    apiGroup: api.classgroups,
    columns: [
        { label:'ID', field:'id' },
        { label:'班级名称', field:'name' },
        { label:'所属专业', field:'specialityName' },
        { label:'所属学院', field:'collegeName' },
        { label:'入学年份', field:'grade' },
    ],
    formFields: [
        { label:'班级名称', field:'name', type:'text' },
        { label:'所属专业', field:'specialityId', type:'select', options:'specialities' },
        { label:'入学年份', field:'grade', type:'number' },
    ],
    refData: {
        specialities: [],
        loaders: {
            specialities: () => api.specialities.list()
        }
    }
});

// 学生组件
const Students = createListComponent({
    title: '学生管理',
    apiGroup: api.students,
    columns: [
        { label:'ID', field:'id' },
        { label:'姓名', field:'name' },
        { label:'性别', field:'gender' },
        { label:'学号', field:'studentNo' },
        { label:'班级', field:'classgroupName' },
        { label:'专业', field:'specialityName' },
        { label:'学院', field:'collegeName' },
        { label:'邮箱', field:'email' },
    ],
    formFields: [
        { label:'姓名', field:'name', type:'text' },
        { label:'性别', field:'gender', type:'text' },
        { label:'学号', field:'studentNo', type:'text' },
        { label:'所属班级', field:'classgroupId', type:'select', options:'classgroups' },
        { label:'邮箱', field:'email', type:'email' },
        { label:'电话', field:'phone', type:'text' },
    ],
    refData: {
        classgroups: [],
        loaders: {
            classgroups: () => api.classgroups.list()
        }
    }
});

// 课程组件
const Courses = createListComponent({
    title: '课程管理',
    apiGroup: api.courses,
    columns: [
        { label:'ID', field:'id' },
        { label:'课程名称', field:'name' },
        { label:'课程编号', field:'code' },
        { label:'学分', field:'credit' },
        { label:'学时', field:'hours' },
        { label:'类型', field:'type' },
        { label:'描述', field:'description' },
    ],
    formFields: [
        { label:'课程名称', field:'name', type:'text' },
        { label:'课程编号', field:'code', type:'text' },
        { label:'学分', field:'credit', type:'number', default:'0' },
        { label:'学时', field:'hours', type:'number', default:'0' },
        { label:'类型', field:'type', type:'text', default:'必修' },
        { label:'描述', field:'description', type:'text' },
    ]
});

// 课程任务组件
const CourseTasks = createListComponent({
    title: '课程任务管理',
    apiGroup: api.courseTasks,
    columns: [
        { label:'ID', field:'id' },
        { label:'课程', field:'courseName' },
        { label:'教师', field:'teacherName' },
        { label:'学期', field:'semester' },
        { label:'上课地点', field:'location' },
        { label:'最大人数', field:'maxStudents' },
    ],
    formFields: [
        { label:'课程', field:'courseId', type:'select', options:'courses' },
        { label:'教师', field:'teacherId', type:'select', options:'teachers' },
        { label:'学期', field:'semester', type:'text', default:'2025-2026学年第2学期' },
        { label:'上课地点', field:'location', type:'text' },
        { label:'最大人数', field:'maxStudents', type:'number', default:'60' },
    ],
    refData: {
        courses: [], teachers: [],
        loaders: {
            courses: () => api.courses.list(),
            teachers: () => api.teachers.list()
        }
    }
});

// 必修课指派组件
const CourseAssigns = createListComponent({
    title: '必修课指派管理',
    apiGroup: api.courseAssigns,
    columns: [
        { label:'ID', field:'id' },
        { label:'课程', field:'courseName' },
        { label:'教师', field:'teacherName' },
        { label:'学期', field:'semester' },
        { label:'班级', field:'classgroupName' },
        { label:'专业', field:'specialityName' },
    ],
    formFields: [
        { label:'课程任务', field:'courseTaskId', type:'select', options:'courseTasks' },
        { label:'班级', field:'classgroupId', type:'select', options:'classgroups' },
    ],
    refData: {
        courseTasks: [], classgroups: [],
        loaders: {
            courseTasks: () => api.courseTasks.list(),
            classgroups: () => api.classgroups.list()
        }
    }
});

// 课程子任务组件
const CourseSubtasks = createListComponent({
    title: '课程子任务管理',
    apiGroup: api.courseSubtasks,
    columns: [
        { label:'ID', field:'id' },
        { label:'课程', field:'courseName' },
        { label:'教师', field:'teacherName' },
        { label:'班级', field:'classgroupName' },
    ],
    formFields: [
        { label:'课程任务', field:'courseTaskId', type:'select', options:'courseTasks' },
        { label:'班级', field:'classgroupId', type:'select', options:'classgroups' },
    ],
    refData: {
        courseTasks: [], classgroups: [],
        loaders: {
            courseTasks: () => api.courseTasks.list(),
            classgroups: () => api.classgroups.list()
        }
    }
});

// 选课组件
const CourseElections = createListComponent({
    title: '选修课管理',
    apiGroup: api.courseElections,
    columns: [
        { label:'ID', field:'id' },
        { label:'学生', field:'studentName' },
        { label:'学号', field:'studentNo' },
        { label:'课程', field:'courseName' },
        { label:'教师', field:'teacherName' },
        { label:'学期', field:'semester' },
    ],
    formFields: [
        { label:'学生', field:'studentId', type:'select', options:'students' },
        { label:'课程任务', field:'courseTaskId', type:'select', options:'courseTasks' },
    ],
    refData: {
        students: [], courseTasks: [],
        loaders: {
            students: () => api.students.list(),
            courseTasks: () => api.courseTasks.list()
        }
    }
});

// ==================== 路由配置 ====================
const routes = [
    { path: '/', component: Dashboard },
    { path: '/colleges', component: Colleges },
    { path: '/teachers', component: Teachers },
    { path: '/specialities', component: Specialities },
    { path: '/classgroups', component: ClassGroups },
    { path: '/students', component: Students },
    { path: '/courses', component: Courses },
    { path: '/course-tasks', component: CourseTasks },
    { path: '/course-assigns', component: CourseAssigns },
    { path: '/course-subtasks', component: CourseSubtasks },
    { path: '/course-elections', component: CourseElections },
    { path: '/stats', component: StatsPage },
    { path: '/class-schedule', component: ClassSchedule },
    { path: '/student-schedule', component: StudentSchedule },
];

const router = VueRouter.createRouter({
    history: VueRouter.createWebHashHistory(),
    routes
});

// ==================== 启动应用 ====================
const app = Vue.createApp({});
app.use(router);
app.mount('#app');
