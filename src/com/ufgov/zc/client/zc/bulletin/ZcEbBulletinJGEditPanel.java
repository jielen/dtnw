package com.ufgov.zc.client.zc.bulletin;

import java.util.List;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.zc.ZcEbBulletinConstants;
import com.ufgov.zc.common.zc.model.ZcEbBulletin;
import com.ufgov.zc.common.zc.model.ZcEbPack;

public class ZcEbBulletinJGEditPanel extends AbstractZcEbBulletinEditPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 1579905355384351558L;

  public ZcEbBulletinJGEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcEbBulletinJGListPanel listPanel) {

    super(parent, listCursor, tabStatus, listPanel, ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_JG);

  }

  protected String getModelName() {
    // TODO Auto-generated method stub
    return ZcEbBulletinConstants.TITLE_ZC_EB_BULLETIN_JG;
  }

  protected String getCompId() {
    // TODO Auto-generated method stub
    return ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_JG;
  }

  protected String getBulletinType() {
    // TODO Auto-generated method stub
    return ZcEbBulletinConstants.TYPE_BULLETIN_JG;
  }

  public String getOpiWay() {
    StringBuffer sb = new StringBuffer();

    sb.append("'").append(ZcSettingConstants.PITEM_OPIWAY_JZXTP).append("'");

    return sb.toString();
  }

  public String fetchSn(ZcEbBulletin sheet) {
    List<ZcEbPack> pack = zcEbBaseServiceDelegate.queryDataForList("ZcEbProj.getZcEbPackListByProjCode", sheet.getProjCode(), requestMeta);
    return pack.get(0).getEntrustCode();
  }

  public String getSqlMapSelectedMold() {

    return "ZcEbBulletinWordMold.getZcEbBulletinWordMoldJLCGZX";

  }

  protected void setFieldMoldNameStatus() {
    super.setFieldMoldNameStatus();
    this.findWordMoldCondition.setType(getBulletinType());
  }

}
