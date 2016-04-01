package com.ufgov.zc.server.zc.dao;

import java.util.List;

import com.ufgov.zc.common.zc.model.ZcDingdianBi;

public interface ZcDingdianBiMapper { 
    int insert(ZcDingdianBi record);  
    int insertList(List biLst);  
    List getBiLst(String ddCode);
    void deleteBiByDdCode(String Code);
}