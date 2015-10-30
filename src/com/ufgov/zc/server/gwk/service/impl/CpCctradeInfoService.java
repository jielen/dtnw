package com.ufgov.zc.server.gwk.service.impl;import java.util.List;import com.ufgov.zc.common.gwk.model.CpCctradeInfo;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.server.gwk.dao.ICpCctradeInfoDao;import com.ufgov.zc.server.gwk.service.ICpCctradeInfoService;import com.ufgov.zc.server.system.util.NumUtil;import com.ufgov.zc.server.system.util.RequestMetaUtil;public class CpCctradeInfoService implements ICpCctradeInfoService {  private ICpCctradeInfoDao cpCctradeInfoDao;  public ICpCctradeInfoDao getCpCctradeInfoDao() {    return cpCctradeInfoDao;  }  public void setCpCctradeInfoDao(ICpCctradeInfoDao cpCctradeInfoDao) {    this.cpCctradeInfoDao = cpCctradeInfoDao;  }  public void insert(List tradeInfoList) {    for (int i = 0; i < tradeInfoList.size(); i++) {      CpCctradeInfo trade = (CpCctradeInfo) tradeInfoList.get(i);      trade.setTradeNum(NumUtil.getInstance().getNo(RequestMetaUtil.getCompoId(), "TRADE_NUM", trade));    }    cpCctradeInfoDao.insert(tradeInfoList);  }  public List getTradeInfo(ElementConditionDto dto) {    return cpCctradeInfoDao.getTradeInfo(dto);  }  public void deleteTradeInfo(List tradeInfoList) {    cpCctradeInfoDao.deleteTradeInfo(tradeInfoList);  }  public void auditTradeInfo(List tradeInfoList) {    for (int i = 0; i < tradeInfoList.size(); i++) {      CpCctradeInfo trade = (CpCctradeInfo) tradeInfoList.get(i);      trade.setStatus("20");    }    cpCctradeInfoDao.updateTradeInfoStatus(tradeInfoList);  }  public void blankoutTradeInfo(List tradeInfoList) {    for (int i = 0; i < tradeInfoList.size(); i++) {      CpCctradeInfo trade = (CpCctradeInfo) tradeInfoList.get(i);      trade.setStatus("50");    }    cpCctradeInfoDao.updateTradeInfoStatus(tradeInfoList);  }}