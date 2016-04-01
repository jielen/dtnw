package com.ufgov.zc.server.zc.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ufgov.zc.common.system.constants.NumLimConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcDingdian;
import com.ufgov.zc.common.zc.model.ZcQb;
import com.ufgov.zc.server.system.util.NumLimUtil;
import com.ufgov.zc.server.zc.dao.ZcDingdianMapper;

public class ZcDingdianMapperImp extends SqlMapClientDaoSupport implements ZcDingdianMapper {

  
  public int deleteByPrimaryKey(String ddCode) {
    return getSqlMapClientTemplate().delete("ZcDingdianMapper.deleteByPrimaryKey", ddCode);  
  }

  
  public int insert(ZcDingdian record) {
     getSqlMapClientTemplate().insert("ZcDingdianMapper.insert", record);
     return 1;
  }

  
  public ZcDingdian selectByPrimaryKey(String ddCode) {
    return  (ZcDingdian) getSqlMapClientTemplate().queryForObject("ZcDingdianMapper.selectByPrimaryKey", ddCode);
  }

  
  public int updateByPrimaryKey(ZcDingdian record) {
    getSqlMapClientTemplate().update("ZcDingdianMapper.updateByPrimaryKey", record);
    return 1;
  }

  
  public List getMainLst(ElementConditionDto dto) {

    dto.setNumLimitStr(NumLimUtil.getInstance().getNumLimCondByCoType(dto.getWfcompoId(), NumLimConstants.FWATCH));
    List list = getSqlMapClientTemplate().queryForList("ZcDingdianMapper.getMainLst", dto);
    return list;
  }

}
