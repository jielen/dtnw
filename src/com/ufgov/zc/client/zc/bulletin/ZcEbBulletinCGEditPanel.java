package com.ufgov.zc.client.zc.bulletin;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JSaveableSplitPane;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.zc.ZcEbBulletinConstants;
import com.ufgov.zc.common.zc.model.ZcEbBulletin;
import com.ufgov.zc.common.zc.model.ZcEbPack;

public class ZcEbBulletinCGEditPanel extends AbstractZcEbBulletinEditPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 1579905355384351558L;

  public ZcEbBulletinCGEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcEbBulletinCGListPanel listPanel) {

    super(parent, listCursor, tabStatus, listPanel, ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_CG);

  }

  @Override
  protected void init() {
    isShowPanel = ((getBulletinType().equals(ZcEbBulletinConstants.TYPE_BULLETIN_BID) || getBulletinType().equals(
      ZcEbBulletinConstants.TYPE_BULLETIN_XUN_JIA_BID)) && AsOptionMeta.getOptVal("OPT_ZC_CGZX_CODE").equals(requestMeta.getSvCoCode()));

    this.initToolBar(toolBar);

    this.setLayout(new BorderLayout());

    this.add(toolBar, BorderLayout.NORTH);
    this.add(workPanel, BorderLayout.CENTER);
    JComponent tabTable = createSubBillPanel();

    if (this.billClass != null && this.eleMeta != null) {
      initFieldEditorPanel(this.billClass, this.eleMeta);
    } else {
      initFieldEditorPanel();
    }
    workPanel.setLayout(new BorderLayout());

    splitPane = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, fieldEditorPanel, tabTable);
    //    splitPane.setDividerDefaultLocation(this.getClass().getName() + "_splitPane_dividerLocation", 150);
    splitPane.setContinuousLayout(true);
    splitPane.setOneTouchExpandable(true);
    // 只显示向下的箭头
    //      splitPane.putClientProperty("toExpand", true);
    splitPane.setDividerSize(5);
    splitPane.setDividerLocation(200);
    workPanel.add(splitPane, BorderLayout.CENTER);
    workPanel.repaint();

  }

  @Override
  protected String getModelName() {
    // TCJLODO Auto-generated method stub
    return ZcEbBulletinConstants.TITLE_ZC_EB_BULLETIN_CG;
  }

  @Override
  protected String getCompId() {
    // TCJLODO Auto-generated method stub
    return ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_CG;
  }

  @Override
  protected String getBulletinType() {
    // TCJLODO Auto-generated method stub
    return ZcEbBulletinConstants.TYPE_BULLETIN_CG;
  }

  @Override
  public String fetchSn(ZcEbBulletin sheet) {
    //变更公告projCode存放的是分包的编号
    List<ZcEbPack> pack = zcEbBaseServiceDelegate.queryDataForList("ZcEbProj.getZcEbPackListByProjCode", sheet.getProjCode(), requestMeta);
    return pack.get(0).getEntrustCode();
  }

  @Override
  public String getSqlMapSelectedMold() {

    return "ZcEbBulletinWordMold.getZcEbBulletinWordMoldJLCGZX";

  }

  @Override
  protected void setFieldMoldNameStatus() {
    super.setFieldMoldNameStatus();
    this.findWordMoldCondition.setType(getBulletinType());
  }

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {
    List<AbstractFieldEditor> fields = super.createFieldEditors();

    DateFieldEditor df = new DateFieldEditor("招标文件公告时间", "zcEbPlan.sellStartTime", DateFieldEditor.TimeTypeH24);
    fields.add(df);

    DateFieldEditor df2 = new DateFieldEditor("点击投标截止时间", "zcEbPlan.sellEndTime", DateFieldEditor.TimeTypeH24);
    fields.add(df2);

    DateFieldEditor failureDate = new DateFieldEditor("投标截止时间", "zcEbPlan.bidEndTime", DateFieldEditor.TimeTypeH24);
    fields.add(failureDate);

    DateFieldEditor openBidTime = new DateFieldEditor("开标时间", "zcEbPlan.openBidTime", DateFieldEditor.TimeTypeH24);
    fields.add(openBidTime);

    TextFieldEditor openBidAddress = new TextFieldEditor("开标地点", "zcEbPlan.openBidAddress");
    fields.add(openBidAddress);

    ZcEbBulletin tin = (ZcEbBulletin) listCursor.getCurrentObject();
    if (tin != null && "exec".equals(tin.getBulletinStatus()) && WorkEnv.getInstance().containRole(ZcSettingConstants.ROLE_GYS_NORMAL)) {
      AsValFieldEditor status = new AsValFieldEditor("抽取专家纪检状态", "isExtrac", "VS_ZC_EB_BULLETIN_IS_EXTRAC");
      status.setEnabled(false);
      fields.add(status);
    }

    return fields;
  }

  @Override
  public String getOpiWay() {

    StringBuffer sb = new StringBuffer();

    sb.append("'").append(ZcSettingConstants.PITEM_OPIWAY_GKZB).append("',");

    sb.append("'").append(ZcSettingConstants.PITEM_OPIWAY_YQZB).append("',");

    sb.append("'").append(ZcSettingConstants.PITEM_OPIWAY_XJ).append("',");

    sb.append("'").append(ZcSettingConstants.PITEM_OPIWAY_JZXTP).append("'");

    return sb.toString();
  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {
    super.initToolBar(toolBar);

    overxxjjButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doExtrac(3);

      }

    });

    overxxzjButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doExtrac(2);

      }

    });
  }

  private void doExtrac(int i) {

    String info = "送信息处抽专家";
    if (i == 1) {
      info = "送信息处抽纪检";
    } else if (i == 2) {
      info = "完成抽专家";
    } else if (i == 3) {
      info = "完成抽纪检";
    }

    int num = JOptionPane.showConfirmDialog(this, "确认" + info + "？", "确认", 0);

    if (num != JOptionPane.YES_OPTION) {
      return;
    }

    String isExtrac = "";
    ZcEbBulletin zcEbBulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();
    String old = zcEbBulletin.getIsExtrac();

    //    Date now = (Date) this.zcEbBaseServiceDelegate.queryObject("ZcEbPlan.getSysdate", null, requestMeta);
    //
    //    if (zcEbBulletin.getZcEbPlan() == null || now.compareTo(zcEbBulletin.getZcEbPlan().getOpenBidTime()) > 0 || now.compareTo(zcEbBulletin.getZcEbPlan().getSellEndTime()) < 0) {
    //
    //      JOptionPane.showMessageDialog(this, "时间已过，不能" + info, " 提示", JOptionPane.INFORMATION_MESSAGE);
    //      return;
    //    }
    if (zcEbBulletin.getIsExtrac() == null || "".equals(zcEbBulletin.getIsExtrac())) {
      isExtrac = "00";
    } else {
      isExtrac = zcEbBulletin.getIsExtrac();
    }

    try {
      if (i == 0) {
        isExtrac = "1" + isExtrac.charAt(1);
      } else if (i == 1) {
        isExtrac = isExtrac.charAt(0) + "1";
      } else if (i == 2) {
        isExtrac = "2" + isExtrac.charAt(1);
      } else if (i == 3) {
        isExtrac = isExtrac.charAt(0) + "2";
      }
      zcEbBulletin.setIsExtrac(isExtrac);

      listPanel.getIZcEbBulletinServiceDelegate().updateIsExtrac(zcEbBulletin, requestMeta);
      listCursor.setCurrentObject(zcEbBulletin);
    } catch (Exception e) {
      zcEbBulletin.setIsExtrac(old);
      JOptionPane.showMessageDialog(this, info + "失败 ！\n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
      return;
    }
    refreshExtracButton();

    JOptionPane.showMessageDialog(this, info + "成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

    this.listPanel.refreshCurrentTabData();
  }

  @Override
  protected void refreshExtracButton() {
    ZcEbBulletin zcEbBulletin = (ZcEbBulletin) this.listCursor.getCurrentObject();
    String isExtrac = "00";
    if (zcEbBulletin == null || !"exec".equals(zcEbBulletin.getBulletinStatus()) || (!"tocom".equals(tabStatus) && !"toext".equals(tabStatus))) {
      isExtrac = "22";
    } else if (zcEbBulletin.getIsExtrac() != null && !"".equals(zcEbBulletin.getIsExtrac())) {
      isExtrac = zcEbBulletin.getIsExtrac();
    }

    if (isExtrac.startsWith("0")) {
      overxxzjButton.setVisible(false);
    } else if (isExtrac.startsWith("1")) {
      overxxzjButton.setVisible(true);
    } else {
      overxxzjButton.setVisible(false);
    }

    if (isExtrac.endsWith("0")) {
      overxxjjButton.setVisible(false);
    } else if (isExtrac.endsWith("1")) {
      overxxjjButton.setVisible(true);
    } else {
      overxxjjButton.setVisible(false);
    }

  }
}
