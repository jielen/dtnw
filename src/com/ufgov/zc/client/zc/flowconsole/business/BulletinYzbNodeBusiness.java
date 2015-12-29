package com.ufgov.zc.client.zc.flowconsole.business;

import java.util.ArrayList;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.zc.bulletin.ZcEbBulletinYZBEditDialog;
import com.ufgov.zc.client.zc.bulletin.ZcEbBulletinYZBListPanel;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;

public class BulletinYzbNodeBusiness implements INodeBusiness {

  private IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,

  "zcEbBaseServiceDelegate");

  @Override
  public boolean isArrieCurrentNode(String zcMakeCode, RequestMeta meta) {

    Integer count = (Integer) zcEbBaseServiceDelegate.queryObject("consoleChart.countBulletinYzbByProjCode", zcMakeCode, meta);

    return count > 0;

  }

  @Override
  public void showAddWindow() {

    // TCJLODO Auto-generated method stub

    new ZcEbBulletinYZBEditDialog(new ZcEbBulletinYZBListPanel(), new ArrayList(1), -1, "0");

  }

}
