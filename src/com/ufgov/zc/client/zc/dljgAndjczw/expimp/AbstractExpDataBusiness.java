package com.ufgov.zc.client.zc.dljgAndjczw.expimp;

import java.io.File;
import java.util.List;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.zc.dataexchange.model.CommonDataExchangeOperator;
import com.ufgov.zc.client.zc.dljgAndjczw.nobusiness.ExportWorkFlowData;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.publish.IDljgJczwDataExpDelegate;

public abstract class AbstractExpDataBusiness {
  protected RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  protected List<ZcBaseBill> businessData;

  protected String path;

  protected String busName;

  protected IDljgJczwDataExpDelegate dljgJczwDataExpDelegate = (IDljgJczwDataExpDelegate) ServiceFactory.create(IDljgJczwDataExpDelegate.class,

  "dljgJczwDataExpDelegate");

  protected StringBuffer log = new StringBuffer();

  protected void saveAsXml(Object inst, String path, String fileName) {

    CommonDataExchangeOperator.saveObjectToXmlFile(inst, path + File.separator + fileName + ".xml");

  }

  public String getDir() {
    return path + File.separator + busName;
  }

  protected void exportWorkFlow() {
    ExportWorkFlowData wfd = new ExportWorkFlowData(businessData, path);
    wfd.exportData();
    log.append(busName + "工作流信息 导出完毕\n");
  }
}
