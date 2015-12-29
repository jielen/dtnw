package com.ufgov.zc.client.zc.bulletin;

import java.util.List;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.zc.ZcEbBulletinConstants;
import com.ufgov.zc.common.zc.model.ZcEbBulletin;
import com.ufgov.zc.common.zc.model.ZcEbPack;

public class ZcEbBulletinCJEditPanel extends AbstractZcEbBulletinEditPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 1579905355384351558L;

  public ZcEbBulletinCJEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcEbBulletinCJListPanel listPanel) {

    super(parent, listCursor, tabStatus, listPanel, ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_CJ);

  }

  public String fetchSn(ZcEbBulletin sheet) {
    List<ZcEbPack> pack = zcEbBaseServiceDelegate.queryDataForList("ZcEbProj.getZcEbPackListByProjCode", sheet.getProjCode(), requestMeta);
    return pack.get(0).getEntrustCode();
  }

  protected String getModelName() {
    // TCJLODO Auto-generated method stub
    return ZcEbBulletinConstants.TITLE_ZC_EB_BULLETIN_CJ;
  }

  protected String getCompId() {
    // TCJLODO Auto-generated method stub
    return ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_CJ;
  }

  protected String getBulletinType() {
    // TCJLODO Auto-generated method stub
    return ZcEbBulletinConstants.TYPE_BULLETIN_CJ;
  }

  public String getOpiWay() {
    StringBuffer sb = new StringBuffer();

    sb.append("'").append(ZcSettingConstants.PITEM_OPIWAY_JZXTP).append("'");

    return sb.toString();
  }

  public String getSqlMapSelectedMold() {

    return "ZcEbBulletinWordMold.getZcEbBulletinWordMoldJLCGZX";

  }

  protected void setFieldMoldNameStatus() {
    super.setFieldMoldNameStatus();
    this.findWordMoldCondition.setType(getBulletinType());
  }

}
