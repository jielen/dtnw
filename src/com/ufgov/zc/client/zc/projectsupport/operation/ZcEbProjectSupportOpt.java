package com.ufgov.zc.client.zc.projectsupport.operation;

import java.util.List;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcEbProjSupport;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbEntrustBulletinServiceDelegate;

/**

 * @ClassName: ZcEbProjectSupportOpt

 * @Description: 项目招标情况业务操作类

 * @date: Sep 19, 2012 3:11:39 PM

 * @version: V1.0

 * @since: 1.0

 * @author: yuzz

 * @modify:

 */
public class ZcEbProjectSupportOpt {
  public IZcEbEntrustBulletinServiceDelegate bulletinServiceDelegate = (IZcEbEntrustBulletinServiceDelegate) ServiceFactory.create(
    IZcEbEntrustBulletinServiceDelegate.class, "zcEbEntrustBulletinServiceDelegate");

  public IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,
    "zcEbBaseServiceDelegate");

  public List getZcEbProjectSupport(ElementConditionDto dto, RequestMeta meta) {
    return bulletinServiceDelegate.getZcEbProjectSupport(dto, meta);
  }

  public List getZcEbPackHistory(ElementConditionDto dto, RequestMeta meta) {
    return bulletinServiceDelegate.getZcEbPackHistory(dto, meta);
  }


  public void newCommitFN(ZcEbProjSupport proj, RequestMeta meta) {
    bulletinServiceDelegate.newCommitFN(proj, meta);
  }

  public void auditFN(ZcEbProjSupport proj, RequestMeta meta) {
    bulletinServiceDelegate.auditFN(proj, meta);
  }

  public void untreadFN(ZcEbProjSupport proj, RequestMeta meta) {
    bulletinServiceDelegate.untreadFN(proj, meta);
  }

  public void callbackFN(ZcEbProjSupport proj, RequestMeta meta) {
    bulletinServiceDelegate.callbackFN(proj, meta);
  }

  public void unauditFN(ZcEbProjSupport proj, RequestMeta meta) {
    bulletinServiceDelegate.unauditFN(proj, meta);
  }

  public void updateSupplierIsSite(List zcEbProjSupportList, RequestMeta meta) {
    bulletinServiceDelegate.updateSupplierIsSite(zcEbProjSupportList, meta);
  }

  public IZcEbBaseServiceDelegate getBaseService() {
    return zcEbBaseServiceDelegate;
  }

}
