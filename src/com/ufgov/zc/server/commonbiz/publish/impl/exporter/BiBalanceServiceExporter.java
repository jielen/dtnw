package com.ufgov.zc.server.commonbiz.publish.impl.exporter;import java.math.BigDecimal;import java.util.List;import java.util.Map;import com.ufgov.zc.common.bi.model.BiCarryReviewResult;import com.ufgov.zc.common.commonbiz.model.BiBalance;import com.ufgov.zc.common.commonbiz.publish.IBiBalanceServiceDelegate;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.system.dto.PrintObject;import com.ufgov.zc.server.system.SpringContext;public class BiBalanceServiceExporter implements IBiBalanceServiceDelegate {  private IBiBalanceServiceDelegate delegate = (IBiBalanceServiceDelegate) SpringContext  .getBean("biBalanceServiceDelegate");  public List getBiBalance(ElementConditionDto dto, RequestMeta requestMeta) {    return this.delegate.getBiBalance(dto, requestMeta);  }  public List getBiBalanceInfo(ElementConditionDto dto, RequestMeta requestMeta) {    return this.delegate.getBiBalanceInfo(dto, requestMeta);  }  public List getAllBiBalance(ElementConditionDto dto, RequestMeta requestMeta) {    return this.delegate.getAllBiBalance(dto, requestMeta);  }  public List getBiBalanceForCd(ElementConditionDto dto,  RequestMeta requestMeta) {    return this.delegate.getBiBalanceForCd(dto, requestMeta);  }  public BiBalance getBiBalanceById(String id, RequestMeta requestMeta) {    return this.delegate.getBiBalanceById(id, requestMeta);  }  public int updateBiBalanceForDpAdjust(BigDecimal adjustMoney,  String biBalanceId, RequestMeta requestMeta) {    return this.delegate.updateBiBalanceForDpAdjust(adjustMoney,    biBalanceId, requestMeta);  }  public List getBiBalanceForAm(ElementConditionDto dto,  RequestMeta requestMeta) {    return this.delegate.getBiBalanceForAm(dto, requestMeta);  }  public List getBiBalanceForAmPvmDpEdit(ElementConditionDto dto, RequestMeta requestMeta) {    return this.delegate.getBiBalanceForAmPvmDpEdit(dto, requestMeta);  }  public List getBiBalanceForDbiTbi(ElementConditionDto dto,  RequestMeta requestMeta) {    return this.delegate.getBiBalanceForDbiTbi(dto, requestMeta);  }  public void updateBiBalanceForAm(BigDecimal adjustMoney,  String biBalanceId, RequestMeta requestMeta) {    this.delegate.updateBiBalanceForAm(adjustMoney, biBalanceId,    requestMeta);  }  public String updateBiBalanceForPreAmToPay(String biBalanceIds, int nd,  RequestMeta requestMeta) {    return this.delegate.updateBiBalanceForPreAmToPay(biBalanceIds, nd,    requestMeta);  }  public List getCarryDownInfo(Map params, RequestMeta requestMeta) {    return this.delegate.getCarryDownInfo(params, requestMeta);  }  public byte[] generateCarryDownDoc(List carryDownInfo, int nd,  RequestMeta requestMeta) {    return this.delegate.generateCarryDownDoc(carryDownInfo, nd,    requestMeta);  }  public byte[] generateCarryDownDoc(List carryDownInfo, byte[] template,  int nd, RequestMeta requestMeta) {    return this.delegate.generateCarryDownDoc(carryDownInfo, template, nd,    requestMeta);  }  public List getGeneratedDocList(int nd, RequestMeta requestMeta) {    return this.delegate.getGeneratedDocList(nd, requestMeta);  }  public void insertBiCarryReviewResult(BiCarryReviewResult biCarryReviewResult,  RequestMeta requestMeta) {    this.delegate.insertBiCarryReviewResult(biCarryReviewResult, requestMeta);  }  public List getBiCarryReviewResult(String coCode, int nd, RequestMeta requestMeta) {    return this.delegate.getBiCarryReviewResult(coCode, nd, requestMeta);  }  public PrintObject genBiCdMoneyCheckPrintObjectFN(String coCode, int nd, RequestMeta requestMeta) {    return this.delegate.genBiCdMoneyCheckPrintObjectFN(coCode, nd, requestMeta);  }  public List getBiBalanceByDto(ElementConditionDto dto, RequestMeta requestMeta) {    return delegate.getBiBalanceByDto(dto, requestMeta);  }  public List getBiBalanceForDz(ElementConditionDto dto, RequestMeta requestMeta) {    return delegate.getBiBalanceForDz(dto, requestMeta);  }  public void updateBiBalanceOperateType(List biBalanceIdList, String operateType, RequestMeta requestMeta) {    delegate.updateBiBalanceOperateType(biBalanceIdList, operateType, requestMeta);  }}