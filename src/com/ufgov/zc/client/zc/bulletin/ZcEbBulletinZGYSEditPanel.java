package com.ufgov.zc.client.zc.bulletin;

import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.common.zc.ZcEbBulletinConstants;
import com.ufgov.zc.common.zc.model.ZcEbBulletin;
import com.ufgov.zc.common.zc.model.ZcEbEntrust;

public class ZcEbBulletinZGYSEditPanel extends AbstractZcEbBulletinEditPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 1579905355384351558L;

  public ZcEbBulletinZGYSEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcEbBulletinZGYSListPanel listPanel) {

    super(parent, listCursor, tabStatus, listPanel, ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_ZGYS);

  }

  protected String getModelName() {
    // TCJLODO Auto-generated method stub
    return ZcEbBulletinConstants.TITLE_ZC_EB_BULLETIN_ZGYS;
  }

  protected String getCompId() {
    // TCJLODO Auto-generated method stub
    return ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_ZGYS;
  }

  protected String getBulletinType() {
    // TCJLODO Auto-generated method stub
    return ZcEbBulletinConstants.TYPE_BULLETIN_ZGYS;
  }

  public String getSqlMapSelectedProj() {

    return "ZcEbProj.getZcEbSheet";

  }

  public String getSqlMapSelectedMold() {

    return "ZcEbBulletinWordMold.getZcEbBulletinWordMoldJLCGZX";

  }

  protected void setFieldMoldNameStatus() {
    super.setFieldMoldNameStatus();
    this.findWordMoldCondition.setType(getBulletinType());
  }
  
  public String fetchSn(ZcEbBulletin sheet){
	  String sn = null;
	  if(sheet.getProjCode() == null){
		  JOptionPane.showMessageDialog(this, "项目为空不能记录相关信息 ！", "错误", JOptionPane.ERROR_MESSAGE);
		  return sn;
	  }
	  ZcEbEntrust entrust = (ZcEbEntrust) this.zcEbBaseServiceDelegate.
			  queryObject("ZcEbEntrust.getZcEbEntrustBySnCode", sheet.getProjCode(), requestMeta);
	  sn = entrust.getSn();
	  return sn;
  }

  public List<AbstractFieldEditor> createFieldEditors() {
    List<AbstractFieldEditor> fields = super.createFieldEditors();

    Integer[] allowMinutes = { 0, 10, 20, 30, 40, 50 };

    DateFieldEditor failureDate = new DateFieldEditor("公告截止时间", "failureDate", DateFieldEditor.TimeTypeH24, allowMinutes, true);

    fields.add(failureDate);

    return fields;
  }

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

}
