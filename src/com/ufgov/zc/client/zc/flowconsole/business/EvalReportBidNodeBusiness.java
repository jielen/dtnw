package com.ufgov.zc.client.zc.flowconsole.business;import java.util.ArrayList;import com.ufgov.zc.client.common.ServiceFactory;import com.ufgov.zc.client.zc.eval.result.ZcEbEvalReportListPanel;import com.ufgov.zc.client.zc.eval.result.ZcEbReportDialog;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;/** * 评审报告 * @author Administrator * */public class EvalReportBidNodeBusiness implements INodeBusiness {  private IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,  "zcEbBaseServiceDelegate");  @Override  public boolean isArrieCurrentNode(String projectCode, RequestMeta meta) {    Integer count = (Integer) zcEbBaseServiceDelegate.queryObject("consoleChart.countEvalReportByProjCode", projectCode, meta);    return count > 0;  }  @Override  public void showAddWindow() {    // TCJLODO Auto-generated method stub    new ZcEbReportDialog(new ZcEbEvalReportListPanel(), new ArrayList(), -1, "0");  }}