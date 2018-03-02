package com.external.yongyou.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * MyBatis的Dao基类
 * @author Patrick Wang
 */
public class MyBatisYongYouDao extends SqlSessionDaoSupport{
	
	public void insert(String key, Object object) {
		getSqlSession().insert(key, object);
	}
	
	public void update(String key,Object object) {
		getSqlSession().update(key, object);
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
