package com.ufgov.zc.server.cp.service;import java.util.List;import java.util.Map;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.system.dto.PrintObject;public interface ICpPayTransferVouService {  public void genCpPayTransferVou(ElementConditionDto condition, List cpVoucherList);  public List getCpPayTransferVou(ElementConditionDto dto);  public void cancelCpPayTransferVou(List cpPayBalBillList);  public void updateCpPayTransferVouStatus(Map params);  public PrintObject genCpPayTransferVouPrintObject(List cpPayBalBillList);  public void updateCpPayTransferVouPrintTimes(String ids);  public List bankDataExportAmve(List cpPayTransferVouList);  public List bankDataExportDmve(List cpPayTransferVouList);  public void increaseExpTimes(List idList);}