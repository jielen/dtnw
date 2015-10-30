package com.ufgov.zc.server.cp.dao;import java.util.List;import java.util.Map;import com.ufgov.zc.common.cp.model.CpPayTransferList;import com.ufgov.zc.common.cp.model.CpPayTransferRelation;import com.ufgov.zc.common.cp.model.CpPayTransferVou;import com.ufgov.zc.common.system.dto.ElementConditionDto;public interface ICpPayTransferVouDao {  public List getCpPayTransferVou(ElementConditionDto dto);  public int updateCpvoucherByPayTransferVouId(CpPayTransferVou bill);  public void insertCpPayTransferVou(CpPayTransferVou bill);  public void insertCpPayTransferList(CpPayTransferList balList);  public void insertCpPayTransferRelation(CpPayTransferRelation balRelation);  public void deleteCpPayTransferVou(CpPayTransferVou bill);  public void deleteCpPayTransferListByVouNo(CpPayTransferVou bill);  public void deleteCpPayRelationByVouNo(CpPayTransferVou bill);  public void updateCpPayTransferVouStatus(Map params);  public void updateCpPayTransferVouPrintTimes(String ids);  public void increaseExpTimes(List idList);  public int updateCpvoucherByPayTransferVouIdForDelete(CpPayTransferVou bill);}