package com.ufgov.zc.server.zc.dao;

import java.util.List;

import com.ufgov.zc.common.zc.model.ZcDingdianItem;

public interface ZcDingdianItemMapper { 
    int insert(ZcDingdianItem record); 
    int insertList(List itemlst);
    void deleteItemByDdCode(String Code);
    List getItemLst(String Code);
}