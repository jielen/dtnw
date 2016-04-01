/**
 * 
 */
package com.ufgov.zc.server.zc.service;

import java.util.List;

import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcPProMake;

/**
 * @author Administrator
 *
 */
public interface IZcMakeXyService {


  List getMainDataLst(ElementConditionDto elementConditionDto, RequestMeta meta);

  ZcPProMake selectByPrimaryKey(String id, RequestMeta meta);

  ZcPProMake saveFN(ZcPProMake bill, RequestMeta meta);

  void deleteByPrimaryKeyFN(String id, RequestMeta meta);

  ZcPProMake unAuditFN(ZcPProMake bill, RequestMeta meta);

  ZcPProMake untreadFN(ZcPProMake bill, RequestMeta meta);

  ZcPProMake auditFN(ZcPProMake bill, RequestMeta meta) throws Exception;

  ZcPProMake newCommitFN(ZcPProMake bill, RequestMeta meta);

  ZcPProMake callbackFN(ZcPProMake bill, RequestMeta meta);

}
