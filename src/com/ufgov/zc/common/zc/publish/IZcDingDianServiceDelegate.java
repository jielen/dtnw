package com.ufgov.zc.common.zc.publish;

import java.util.List;

import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.exception.DataExchangeException;
import com.ufgov.zc.common.zc.model.ZcDingdian;

public interface IZcDingDianServiceDelegate {
  
  List getMainLst(ElementConditionDto elementConditionDto, RequestMeta requestMeta);

  void cancelFn(ZcDingdian bill, RequestMeta requestMeta);

  ZcDingdian unAuditFN(ZcDingdian bill, RequestMeta requestMeta);

  ZcDingdian untreadFN(ZcDingdian bill, RequestMeta requestMeta);

  ZcDingdian auditFN(ZcDingdian bill, RequestMeta requestMeta) throws Exception;

  ZcDingdian updateFN(ZcDingdian bill, RequestMeta requestMeta) throws Exception;

  void commitFN(List beanList, RequestMeta requestMeta);

  void deleteListFN(List beanList, RequestMeta requestMeta);

  void deleteFN(ZcDingdian bill, RequestMeta requestMeta);

  ZcDingdian selectByPrimaryKey(String ddCode, RequestMeta requestMeta);

  ZcDingdian callbackFN(ZcDingdian bill, RequestMeta requestMeta);

  void deleteByPrimaryKeyFN(String ddCode, RequestMeta requestMeta);

  ZcDingdian newCommitFN(ZcDingdian bill, RequestMeta requestMeta);

  ZcDingdian sendPayFN(ZcDingdian bill, RequestMeta requestMeta) throws Exception;

  List queryExportsDatas(ElementConditionDto dto, RequestMeta meta);

  String importTransDatasFN(ZcDingdian bill, RequestMeta meta) throws DataExchangeException;

  String importDelDataFN(String id, RequestMeta meta) throws DataExchangeException;


}
