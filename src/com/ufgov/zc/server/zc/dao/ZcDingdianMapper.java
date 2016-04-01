package com.ufgov.zc.server.zc.dao;

import java.util.List;

import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcDingdian;

public interface ZcDingdianMapper { 
    int deleteByPrimaryKey(String ddCode); 
    int insert(ZcDingdian record);  
    ZcDingdian selectByPrimaryKey(String ddCode);  
    int updateByPrimaryKey(ZcDingdian record);
    List getMainLst(ElementConditionDto dto);
}