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

public class ZcEbBulletinOpnEditPanel extends AbstractZcEbBulletinEditPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 1579905355384351558L;

  public ZcEbBulletinOpnEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcEbBulletinOpnListPanel listPanel) {

    super(parent, listCursor, tabStatus, listPanel, ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_OPN);

  }

  protected String getModelName() {
    // TODO Auto-generated method stub
    return ZcEbBulletinConstants.TITLE_ZC_EB_BULLETIN_OPN;
  }

  protected String getCompId() {
    // TODO Auto-generated method stub
    return ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_OPN;
  }

  protected String getBulletinType() {
    // TODO Auto-generated method stub
    return ZcEbBulletinConstants.TYPE_BULLETIN_OPN;
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

    DateFieldEditor failureDate = new DateFieldEditor("公告截止日期", "failureDate", DateFieldEditor.TimeTypeH24);

    fields.add(failureDate);
    return fields;
  }

  protected boolean checkBeforeSave(boolean isSend) {
    if (super.checkBeforeSave(isSend)) {
      ZcEbBulletin bulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();
      if (bulletin.getFailureDate() == null || bulletin.getFailureDate().before(new Date())) {

        JOptionPane.showMessageDialog(this.parent, "请正确填写[公告截止日期]", "提示", JOptionPane.WARNING_MESSAGE);

        return false;
      }

      return true;
    }
    return false;
  }

  public void doReplaceBookMarks() {
    if (!checkBeforeSave(false)) {
      return;
    }
    if (replaceValue == null || replaceValue.equals("")) {
      return;
    }
    ZcEbBulletin bulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();

    String midStr = replaceValue.replaceAll("\\$", "#");
    //公告发布时间
    String reg = "YEAR#####[^@]*@@@@@";
    String rep = "YEAR#####" + (bulletin.getFailureDate().getYear()%10) + "@@@@@";
    midStr = midStr.replaceAll(reg, rep);

    reg = "MONTH#####[^@]*@@@@@";
    rep = "MONTH#####" + (bulletin.getFailureDate().getMonth() + 1) + "@@@@@";
    midStr = midStr.replaceAll(reg, rep);

    reg = "DAY#####[^@]*@@@@@";
    rep = "DAY#####" + (bulletin.getFailureDate().getDate()) + "@@@@@";
    midStr = midStr.replaceAll(reg, rep);

    replaceValue = midStr.replaceAll("#", "\\$");
    super.doReplaceBookMarks();
  }
}
