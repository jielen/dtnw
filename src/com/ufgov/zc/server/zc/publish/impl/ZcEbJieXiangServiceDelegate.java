/**
 * 
 */
package com.ufgov.zc.server.zc.publish.impl;

import java.util.List;
import java.util.Map;

import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcEbJieXiang;
import com.ufgov.zc.common.zc.publish.IZcEbJieXiangServiceDelegate;
import com.ufgov.zc.server.zc.service.IZcEbJieXiangService;

/**
 * @author Administrator
 *
 */
public class ZcEbJieXiangServiceDelegate extends ZcEbAuditSheetServiceDelegate implements IZcEbJieXiangServiceDelegate {

  private IZcEbJieXiangService zcEbJieXiangService;
 
  /* (non-Javadoc)
   * @see com.ufgov.zc.common.zc.publish.IZcEbJieXiangServiceDelegate#getList(com.ufgov.zc.common.system.dto.ElementConditionDto, com.ufgov.zc.common.system.RequestMeta)
   */
  
  public List getList(ElementConditionDto elementConditionDto, RequestMeta requestMeta) {
    // TCJLODO Auto-generated method stub
    return zcEbJieXiangService.getList(elementConditionDto, requestMeta);
  }

  public IZcEbJieXiangService getZcEbJieXiangService() {
    return zcEbJieXiangService;
  }

  public void setZcEbJieXiangService(IZcEbJieXiangService zcEbJieXiangService) {
    this.zcEbJieXiangService = zcEbJieXiangService;
  }

  
  public ZcEbJieXiang readJieXiang(Map para, RequestMeta requestMeta) {
    // TCJLODO Auto-generated method stub
    return zcEbJieXiangService.readJieXiang(para, requestMeta);
  }

 

  
  public void cancelCaiGou(ZcEbJieXiang jiexiang, RequestMeta requestMeta) {
    // TCJLODO Auto-generated method stub
    zcEbJieXiangService.cancelCaiGou(jiexiang, requestMeta);
  }

  
  public void unFinishCaiGou(ZcEbJieXiang jiexiang, RequestMeta requestMeta) {
    // TCJLODO Auto-generated method stub
    zcEbJieXiangService.unFinishCaiGou(jiexiang, requestMeta);
    
  }

  
  public void finishCaiGou(ZcEbJieXiang jiexiang, RequestMeta requestMeta) {
    // TCJLODO Auto-generated method stub
    zcEbJieXiangService.finishCaiGou(jiexiang, requestMeta);
    
  }


}
