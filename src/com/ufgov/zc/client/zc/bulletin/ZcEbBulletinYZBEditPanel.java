package com.ufgov.zc.client.zc.bulletin;

import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.zc.ZcEbBulletinConstants;
import com.ufgov.zc.common.zc.model.ZcEbBulletin;
import com.ufgov.zc.common.zc.model.ZcEbPack;

public class ZcEbBulletinYZBEditPanel extends AbstractZcEbBulletinEditPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 1579905355384351558L;

  public ZcEbBulletinYZBEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcEbBulletinYZBListPanel listPanel) {

    super(parent, listCursor, tabStatus, listPanel, ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_YZB);

  }

  protected String getModelName() {
    // TCJLODO Auto-generated method stub
    return ZcEbBulletinConstants.TITLE_ZC_EB_BULLETIN_YZB;
  }

  protected String getCompId() {
    // TCJLODO Auto-generated method stub
    return ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_YZB;
  }

  public String fetchSn(ZcEbBulletin sheet) {
    List<ZcEbPack> pack = zcEbBaseServiceDelegate.queryDataForList("ZcEbProj.getZcEbPackListByProjCode", sheet.getProjCode(), requestMeta);
    return pack.get(0).getEntrustCode();
  }

  protected String getBulletinType() {
    // TCJLODO Auto-generated method stub
    return ZcEbBulletinConstants.TYPE_BULLETIN_YZB;
  }

  public String getOpiWay() {
    StringBuffer sb = new StringBuffer();

    sb.append("'").append(ZcSettingConstants.PITEM_OPIWAY_GKZB).append("',");

    sb.append("'").append(ZcSettingConstants.PITEM_OPIWAY_YQZB).append("',");

    sb.append("'").append(ZcSettingConstants.PITEM_OPIWAY_JZXTP).append("'");

    return sb.toString();
  }

  public List<AbstractFieldEditor> createFieldEditors() {
    List<AbstractFieldEditor> fields = super.createFieldEditors();

    Integer[] allowMinutes = { 0, 10, 20, 30, 40, 50 };
    DateFieldEditor failureDate = new DateFieldEditor("公告截止时间", "failureDate", DateFieldEditor.TimeTypeH24, allowMinutes, true);
    fields.add(failureDate);

    return fields;
  }

  @Override
  protected boolean checkBeforeSave(boolean isSend) {
    if (super.checkBeforeSave(isSend)) {
      ZcEbBulletin bulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();
      if (bulletin.getFailureDate() == null || bulletin.getFailureDate().before(new Date())) {

        JOptionPane.showMessageDialog(this.parent, "请正确填写[公告截止时间]", "提示", JOptionPane.WARNING_MESSAGE);

        return false;
      }

      return true;
    }
    return false;
  }

  public String getSqlMapSelectedMold() {

    return "ZcEbBulletinWordMold.getZcEbBulletinWordMoldJLCGZX";

  }

  public String getSqlMapSelectedProj() {

    return "ZcEbProj.getZcEbProjForYZB";

  }

  protected void setFieldMoldNameStatus() {
    super.setFieldMoldNameStatus();
    this.findWordMoldCondition.setType(getBulletinType());
  }

}
