package com.ufgov.zc.client.zc.dljgAndjczw.nobusiness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.zc.dljgAndjczw.expimp.AbstractExpDataBusiness;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.publish.IDljgJczwDataExpDelegate;

public class ExportWorkFlowData extends AbstractExpDataBusiness {
  public static final String fileName = "workflow";

  private IDljgJczwDataExpDelegate dljgJczwDataExpDelegate = (IDljgJczwDataExpDelegate) ServiceFactory.create(IDljgJczwDataExpDelegate.class,

  "dljgJczwDataExpDelegate");

  private List<Long> instanceIds;

  private String path;

  public ExportWorkFlowData(List<ZcBaseBill> baseBillList, String path) {
    instanceIds = getInstanceIds(baseBillList);
    this.path = path;
  }

  public String getBusName() {
    // TCJLODO Auto-generated method stub
    return "工作流";
  }

  public List<ZcBaseBill> getBusinessData() {

    return null;
  }

  public void exportData() {
    // TCJLODO Auto-generated method stub
    List workflowList = dljgJczwDataExpDelegate.getWorkFlowInforData(instanceIds, requestMeta);

    saveAsXml(workflowList, path, fileName);
  }

  public String getDataType() {
    // TCJLODO Auto-generated method stub
    return null;
  }

  private List<Long> getInstanceIds(List<ZcBaseBill> blist) {

    List<Long> insIdList = new ArrayList<Long>();

    for (Iterator<ZcBaseBill> iterator = blist.iterator(); iterator.hasNext();) {

      ZcBaseBill po = iterator.next();

      insIdList.add(po.getProcessInstId());

    }

    return insIdList;

  }
}
