package com.edu.dao;

import com.edu.util.DBUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.List;

/**
 * 通用 DAO 基类
 * 使用泛型 + 反射 + Apache Commons DBUtils 提供通用 CRUD 操作
 * 使用 GenerousBeanProcessor 自动将数据库 snake_case 列名映射到 camelCase 属性
 */
public abstract class BaseDao<T> {

    protected final QueryRunner runner = new QueryRunner(DBUtil.getDataSource());
    protected final Class<T> entityClass;
    protected final RowProcessor rowProcessor;

    @SuppressWarnings("unchecked")
    public BaseDao() {
        // 通过反射获取泛型参数的实际类型
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) pt.getActualTypeArguments()[0];
        // GenerousBeanProcessor: 自动 snake_case → camelCase 转换
        this.rowProcessor = new BasicRowProcessor(new GenerousBeanProcessor());
    }

    /**
     * 查询全部记录
     */
    public List<T> findAll() {
        String sql = "SELECT * FROM " + getTableName();
        try {
            return runner.query(sql, new BeanListHandler<>(entityClass, rowProcessor));
        } catch (SQLException e) {
            throw new RuntimeException("查询列表失败: " + getTableName(), e);
        }
    }

    /**
     * 根据 ID 查询单条记录
     */
    public T findById(Long id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        try {
            return runner.query(sql, new BeanHandler<>(entityClass, rowProcessor), id);
        } catch (SQLException e) {
            throw new RuntimeException("查询详情失败: " + getTableName(), e);
        }
    }

    /**
     * 插入记录，返回自增主键
     */
    public Long insert(String sql, Object... params) {
        try {
            return runner.insert(sql, new ScalarHandler<Long>(), params);
        } catch (SQLException e) {
            throw new RuntimeException("插入记录失败: " + getTableName(), e);
        }
    }

    /**
     * 更新记录
     */
    public int update(String sql, Object... params) {
        try {
            return runner.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException("更新记录失败: " + getTableName(), e);
        }
    }

    /**
     * 删除记录
     */
    public int delete(Long id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";
        try {
            return runner.update(sql, id);
        } catch (SQLException e) {
            throw new RuntimeException("删除记录失败: " + getTableName(), e);
        }
    }

    /**
     * 执行自定义查询（返回对象列表）
     */
    public List<T> queryList(String sql, Object... params) {
        try {
            return runner.query(sql, new BeanListHandler<>(entityClass, rowProcessor), params);
        } catch (SQLException e) {
            throw new RuntimeException("自定义查询失败", e);
        }
    }

    /**
     * 执行自定义查询（返回单个对象）
     */
    public T queryOne(String sql, Object... params) {
        try {
            return runner.query(sql, new BeanHandler<>(entityClass, rowProcessor), params);
        } catch (SQLException e) {
            throw new RuntimeException("自定义查询失败", e);
        }
    }

    /**
     * 统计记录数
     */
    public long count(String sql, Object... params) {
        try {
            Long result = runner.query(sql, new ScalarHandler<Long>(), params);
            return result != null ? result : 0;
        } catch (SQLException e) {
            throw new RuntimeException("统计查询失败", e);
        }
    }

    /**
     * 子类重写此方法返回表名
     */
    protected abstract String getTableName();

    /**
     * 创建 BeanListHandler（使用 GenerousBeanProcessor 实现 snake_case → camelCase）
     */
    protected BeanListHandler<T> listHandler() {
        return new BeanListHandler<>(entityClass, rowProcessor);
    }

    /**
     * 创建 BeanHandler（使用 GenerousBeanProcessor 实现 snake_case → camelCase）
     */
    protected BeanHandler<T> beanHandler() {
        return new BeanHandler<>(entityClass, rowProcessor);
    }
}
