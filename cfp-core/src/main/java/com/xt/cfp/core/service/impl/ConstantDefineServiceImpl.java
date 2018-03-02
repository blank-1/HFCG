package com.xt.cfp.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.ConstantDefine;
import com.xt.cfp.core.service.ConstantDefineCached;
import com.xt.cfp.core.service.ConstantDefineService;
import com.xt.cfp.core.util.Pagination;

@Service
public class ConstantDefineServiceImpl implements ConstantDefineService {
	@Autowired
	private MyBatisDao myBatisDao;
	
    @Autowired
    private ConstantDefineCached constantDefineCached;
    
    @Override
    public List<ConstantDefine> findAll() {
        return myBatisDao.getList("CONSTANTDEFINE.findAll");
    }

    @Override
    public Pagination<ConstantDefine> findAllByPage(int page, int limit,ConstantDefine constantDefine) {
        Pagination<ConstantDefine> pagination = new Pagination<ConstantDefine>();
        pagination.setCurrentPage(page);
        pagination.setPageSize(limit);
        List<ConstantDefine> constantDefines = myBatisDao.getListForPaging("findConstantDefineByPage", constantDefine, pagination.getOffset(), pagination.getLimit());
        pagination.setRows(constantDefines);
        pagination.setTotal(myBatisDao.count("findConstantDefineByPage", constantDefine));
        return pagination;
    }

    @Override
    public void addConstantDefine(ConstantDefine constantDefine) {

        myBatisDao.insert("CONSTANTDEFINE.insert", constantDefine);
        constantDefineCached.reset();
    }

    @Override
    public void updateConstantDefine(ConstantDefine constantDefine){
    	myBatisDao.update("CONSTANTDEFINE.update", constantDefine);
        constantDefineCached.reset();
    }

    @Override
    public ConstantDefine findConstantByTypeCodeAndValue(ConstantDefine constantDefine) {
        return myBatisDao.get("CONSTANTDEFINE.findConstantByTypeCodeAndValue", constantDefine);
    }

    @Override
    public List<ConstantDefine> findByTypeCodeAndParentConstant(String constantTypeCode,long parentConstant) {
        ConstantDefine constantDefine=new ConstantDefine();
        constantDefine.setConstantTypeCode(constantTypeCode);
        constantDefine.setParentConstant(parentConstant);
        return myBatisDao.getList("CONSTANTDEFINE.findByTypeCodeAndParentConstant", constantDefine);
    }

    @Override
    public Pagination<ConstantDefine> getConstantDefinePaging(int pageNo, int pageSize, ConstantDefine constantDefine, Map<String, Object> customParams) {
        Pagination<ConstantDefine> re = new Pagination<ConstantDefine>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("constantDefine", constantDefine);
        params.put("customParams", customParams);

        int totalCount = this.myBatisDao.count("getConstantDefinePaging", params);
        List<ConstantDefine> _constantDefine = this.myBatisDao.getListForPaging("getConstantDefinePaging", params, pageNo, pageSize);

        re.setTotal(totalCount);
        re.setRows(_constantDefine);

        return re;
    }

    @Override
    public ConstantDefine findByTypeCodeAndValueAndParentValue(String constantTypeCode, String key, String parentkey) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("constantTypeCode", constantTypeCode);
        params.put("key", key);
        params.put("parentKey", parentkey);
        return myBatisDao.get("CONSTANTDEFINE.findByTypeCodeAndValueAndParentValue", params);
    }


    @Override
    public ConstantDefine findById(long constantDefineId) {
        return myBatisDao.get("CONSTANTDEFINE.findById", constantDefineId);
    }

	@Override
	public List<ConstantDefine> findsById(long constantDefineId) {
		return myBatisDao.getList("CONSTANTDEFINE.findsById", constantDefineId);
	}

    @Override
    public List<ConstantDefine> getConstantDefinesByType(String typeCode) {
        return myBatisDao.getList("CONSTANTDEFINE.getConstantDefinesByType", typeCode);
    }

    @Override
    public ConstantDefine doFeesTypeEcho(String constantValue) {
        return myBatisDao.get("CONSTANTDEFINE.doFeesTypeEcho", constantValue);
    }

	@Override
	public List<ConstantDefine> getConstantDefinesByTypeNeedSort(String typeCode) {
		return myBatisDao.getList("CONSTANTDEFINE.getConstantDefinesByTypeNeedSort", typeCode);
	}

}
