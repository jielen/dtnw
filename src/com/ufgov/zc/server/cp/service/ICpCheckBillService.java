package com.ufgov.zc.server.cp.service;import java.util.Date;import java.util.List;import java.util.Map;import com.ufgov.zc.common.cp.model.CpDzCoResult;import com.ufgov.zc.common.system.dto.ElementConditionDto;public interface ICpCheckBillService {  List getCpInterfaceCollectTempchx(String bankCode, Date startDate, Date endDate, String paytypeCode,  List differflagList);  List getCpInterfaceAmountTempchx(String bankCode, Date startDate, Date endDate, String paytypeCode,  List differflagList);  List getCpInterfacePayoutTempchx(String bankCode, Date startDate, Date endDate, String paytypeCode,  List differflagList);  List getCpDzCoDateTime(int nd);  String checkCpInterfaceCollect(Date startDate, Date endDate, String paytypeCode, String payBankCode);  String checkCpInterfaceAmount(Date startDate, Date endDate, String paytypeCode, String payBankCode);  String checkCpInterfacePayout(Date startDate, Date endDate, String paytypeCode, String payBankCode);  List getCpDzCoAccount(String coCode, Date startDate, Date endDate, String curMonth, String sumMonth);  void checkForCpDzCoAccount(CpDzCoResult dzCoResult);  public void updatecpDzCoDatetime(List list);  List getCpDzCoResultQueryList(ElementConditionDto elementDto);  Map getDzDateMap(ElementConditionDto elementConditionDto);  Map getDzResultMap(ElementConditionDto elementConditionDto);  List getDzMonth(ElementConditionDto elementConditionDto);}