package com.ufgov.zc.client.zc.flowconsole.business;

import java.util.ArrayList;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.zc.project.change.ZcEbProjectChangeEditDialog;
import com.ufgov.zc.client.zc.project.change.ZcEbProjectChangeListPanel;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;

public class ZcEbPurTypeChgNodeBusiness implements INodeBusiness {

  private IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,

  "zcEbBaseServiceDelegate");

  @Override
  public boolean isArrieCurrentNode(String zcMakeCode, RequestMeta meta) {

    Integer count = (Integer) zcEbBaseServiceDelegate.queryObject("consoleChart.countPurTypeChgByMakeCode", zcMakeCode, meta);

    return count > 0;

  }

  @Override
  public void showAddWindow() {

    // TCJLODO Auto-generated method stub

    new ZcEbProjectChangeEditDialog(new ZcEbProjectChangeListPanel(), new ArrayList(1), -1, "0", "采购方式变更");

  }

}
