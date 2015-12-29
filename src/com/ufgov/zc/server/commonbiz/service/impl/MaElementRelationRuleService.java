package com.ufgov.zc.server.commonbiz.service.impl;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import java.util.Map;import java.util.TreeMap;import com.ufgov.zc.common.commonbiz.model.MaElementRelationRule;import com.ufgov.zc.common.commonbiz.model.MaElementRelationRuleDetail;import com.ufgov.zc.common.commonbiz.model.MaElementRelationRuleEntry;import com.ufgov.zc.server.commonbiz.dao.IMaElementRelationRuleDao;import com.ufgov.zc.server.commonbiz.service.IMaElementRelationRuleService;public class MaElementRelationRuleService implements IMaElementRelationRuleService {  private IMaElementRelationRuleDao maCpElementRelationDao;  public IMaElementRelationRuleDao getMaCpElementRelationDao() {    return maCpElementRelationDao;  }  public void setMaCpElementRelationDao(  IMaElementRelationRuleDao maCpElementRelationDao) {    this.maCpElementRelationDao = maCpElementRelationDao;  }  public void insertElementRelationRule(MaElementRelationRule relationRule) {    maCpElementRelationDao.insertElementRelationRule(relationRule);  }  public void updateElementRelationRule(MaElementRelationRule realtionRule) {    maCpElementRelationDao.updateElementRelationRule(realtionRule);  }  public String getElementRelationId() {    return maCpElementRelationDao.getElementRelationId();  }  public List getElementRelationRules(String compoId, String handleType, String ruleType) {    List list = maCpElementRelationDao.getElementRelationRule(compoId, handleType, ruleType);    if (list.isEmpty()) {      list = this.maCpElementRelationDao.getElementRelationRule("*", handleType, ruleType);    }    List ruleIdList = new ArrayList();    for (int i = 0; i < list.size(); i++) {      MaElementRelationRule rule = (MaElementRelationRule) list.get(i);      ruleIdList.add(rule.getRelationRuleId());    }    List ruleDetailList = this.maCpElementRelationDao.getElementRelationRuleDetail(ruleIdList);    Map entryMap = new TreeMap();    Map entryPuttedMap = new TreeMap();    for (int i = 0; i < ruleDetailList.size(); i++) {      MaElementRelationRuleDetail ruleDetail = (MaElementRelationRuleDetail) ruleDetailList.get(i);      String key = ruleDetail.getRelationRuleId();      String entryId = ruleDetail.getEntryId();      if (entryPuttedMap.get(entryId) != null) {        continue;      }      entryPuttedMap.put(entryId, entryId);      MaElementRelationRuleEntry entry = new MaElementRelationRuleEntry();      entry.setEntryId(entryId);      List entryList = (List) entryMap.get(key);      if (entryList == null) {        entryList = new ArrayList();        entryMap.put(key, entryList);      }      entryList.add(entry);    }    Map ruleDetailMap = new HashMap();    for (int i = 0; i < ruleDetailList.size(); i++) {      MaElementRelationRuleDetail ruleDetail = (MaElementRelationRuleDetail) ruleDetailList.get(i);      String key = ruleDetail.getRelationRuleId() + ruleDetail.getEntryId() + ruleDetail.getDirection().toLowerCase();      List detailList = (List) ruleDetailMap.get(key);      if (detailList == null) {        detailList = new ArrayList();        ruleDetailMap.put(key, detailList);      }      detailList.add(ruleDetail);    }    for (int i = 0; i < list.size(); i++) {      MaElementRelationRule rule = (MaElementRelationRule) list.get(i);      List entryList = (List) entryMap.get(rule.getRelationRuleId());      if (entryList != null) {        rule.setRuleEntryList(entryList);        for (int n = 0; n < entryList.size(); n++) {          MaElementRelationRuleEntry ruleEntry = (MaElementRelationRuleEntry) entryList.get(n);          List srcList = (List) ruleDetailMap.get(rule.getRelationRuleId() + ruleEntry.getEntryId() + MaElementRelationRuleDetail.DIRECTION_SRC);          List destList = (List) ruleDetailMap.get(rule.getRelationRuleId() + ruleEntry.getEntryId() + MaElementRelationRuleDetail.DIRECTION_DEST);          if (srcList != null) {            ruleEntry.setSrcElementList(srcList);          }          if (destList != null) {            ruleEntry.setDestElementList(destList);          }        }      }    }    return list;  }  public void deleteElementRelationRuleById(String ruleId) {    // TCJLODO Auto-generated method stub    maCpElementRelationDao.deleteElementRelationRuleById(ruleId);  }  public void deleteRelationEntityByEntryId(String entityId) {    // TCJLODO Auto-generated method stub    maCpElementRelationDao.deleteRelationEntryByEntryId(entityId);  }  public String getElementEntryId() {    return maCpElementRelationDao.getElementEntryId();  }  public void insertElementRelationEntry(MaElementRelationRuleEntry ruleEntry) {    // TCJLODO Auto-generated method stub    maCpElementRelationDao.insertElementRelationEntry(ruleEntry);  }  public void updateElementRelationEntry(MaElementRelationRuleEntry ruleEntry) {    // TCJLODO Auto-generated method stub    maCpElementRelationDao.updateElementRelationEntry(ruleEntry);  }}