package com.ufgov.zc.client.zc.flowconsole.business;import com.ufgov.zc.client.common.ServiceFactory;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;public class KaiQiBaoJiaNodeBusiness implements INodeBusiness {  private IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,  "zcEbBaseServiceDelegate");  @Override  public boolean isArrieCurrentNode(String projectCode, RequestMeta meta) {    Integer count = (Integer) zcEbBaseServiceDelegate.queryObject("consoleChart.countKaiQiBaoJiaByProjCode", projectCode, meta);    return count > 0;  }  @Override  public void showAddWindow() {    // TCJLODO Auto-generated method stub  }}