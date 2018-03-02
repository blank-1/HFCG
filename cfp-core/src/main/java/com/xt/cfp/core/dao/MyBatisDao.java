package com.xt.cfp.core.dao;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.io.Serializable;
import java.util.List;

/**
 * MyBatis的Dao基类
 * @author Patrick Wang
 */
public class MyBatisDao extends SqlSessionDaoSupport{
	
	public void insert(String key, Object object) {
		getSqlSession().insert(key, object);
	}
	
	public int update(String key, Object object) {
		return getSqlSession().update(key, object);
	}
	
	public void delete(String key, Serializable id) {
		getSqlSession().delete(key, id);
	}
	
	public void delete(String key, Object object) {
		getSqlSession().delete(key, object);
	}
	
	public <T> T get(String key, Object params) {
		return (T) getSqlSession().selectOne(key, params);
	}
	
	public <T> List<T> getList(String key) {
		return getSqlSession().selectList(key);
	}
	
	public <T> List<T> getList(String key, Object params) {
		return getSqlSession().selectList(key, params);
	}


    public <T> List<T> getListForPaging(String key, Object params, int pageNum, int pageSize) {
        RowBounds rowBounds = new RowBounds((pageNum - 1) * pageSize, pageSize);
        return getSqlSession().selectList(key, params, rowBounds);
    }

    public Integer count(String key, Object params) {
        return getSqlSession().selectOne("count_" + key, params);
    }
}
