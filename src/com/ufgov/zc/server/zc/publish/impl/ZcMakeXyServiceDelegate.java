/**
 * 
 */
package com.ufgov.zc.server.zc.publish.impl;

import java.util.List;

import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcPProMake;
import com.ufgov.zc.common.zc.publish.IZcMakeXyServiceDelegate;
import com.ufgov.zc.server.zc.service.IZcMakeXyService;

/**
 * @author Administrator
 *
 */
public class ZcMakeXyServiceDelegate implements IZcMakeXyServiceDelegate {
  
  private IZcMakeXyService makeXyService;

  /* (non-Javadoc)
   * @see com.ufgov.zc.common.zc.publish.IZcMakeXyServiceDelegate#getMainDataLst(com.ufgov.zc.common.system.dto.ElementConditionDto, com.ufgov.zc.common.system.RequestMeta)
   */
 
  public List getMainDataLst(ElementConditionDto elementConditionDto, RequestMeta meta) {
    return makeXyService.getMainDataLst(elementConditionDto, meta);
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.common.zc.publish.IZcMakeXyServiceDelegate#selectByPrimaryKey(java.lang.String, com.ufgov.zc.common.system.RequestMeta)
   */
 
  public ZcPProMake selectByPrimaryKey(String id, RequestMeta meta) {
    return makeXyService.selectByPrimaryKey(id, meta);
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.common.zc.publish.IZcMakeXyServiceDelegate#saveFN(com.ufgov.zc.common.zc.model.ZcPProMake, com.ufgov.zc.common.system.RequestMeta)
   */
 
  public ZcPProMake saveFN(ZcPProMake bill, RequestMeta meta) {
    return makeXyService.saveFN(bill, meta);
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.common.zc.publish.IZcMakeXyServiceDelegate#deleteByPrimaryKeyFN(java.lang.String, com.ufgov.zc.common.system.RequestMeta)
   */
 
  public void deleteByPrimaryKeyFN(String id, RequestMeta meta) {
    makeXyService.deleteByPrimaryKeyFN(id, meta);
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.common.zc.publish.IZcMakeXyServiceDelegate#unAuditFN(com.ufgov.zc.common.zc.model.ZcPProMake, com.ufgov.zc.common.system.RequestMeta)
   */
 
  public ZcPProMake unAuditFN(ZcPProMake bill, RequestMeta meta) {
    return makeXyService.unAuditFN(bill, meta);
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.common.zc.publish.IZcMakeXyServiceDelegate#untreadFN(com.ufgov.zc.common.zc.model.ZcPProMake, com.ufgov.zc.common.system.RequestMeta)
   */
 
  public ZcPProMake untreadFN(ZcPProMake bill, RequestMeta meta) {
    return makeXyService.untreadFN(bill, meta);
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.common.zc.publish.IZcMakeXyServiceDelegate#auditFN(com.ufgov.zc.common.zc.model.ZcPProMake, com.ufgov.zc.common.system.RequestMeta)
   */
 
  public ZcPProMake auditFN(ZcPProMake bill, RequestMeta meta) throws Exception {
    return makeXyService.auditFN(bill, meta);
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.common.zc.publish.IZcMakeXyServiceDelegate#newCommitFN(com.ufgov.zc.common.zc.model.ZcPProMake, com.ufgov.zc.common.system.RequestMeta)
   */
 
  public ZcPProMake newCommitFN(ZcPProMake bill, RequestMeta meta) {
    return makeXyService.newCommitFN(bill, meta);
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.common.zc.publish.IZcMakeXyServiceDelegate#callbackFN(com.ufgov.zc.common.zc.model.ZcPProMake, com.ufgov.zc.common.system.RequestMeta)
   */
 
  public ZcPProMake callbackFN(ZcPProMake bill, RequestMeta meta) {
    return makeXyService.callbackFN(bill, meta);
  }

  public IZcMakeXyService getMakeXyService() {
    return makeXyService;
  }

  public void setMakeXyService(IZcMakeXyService makeXyService) {
    this.makeXyService = makeXyService;
  }

}
