/*
 * *
 *  Copyright 2012 by Beijing UFIDA Government Affairs Software Co.,Ltd.,
 *  All rights reserved.
 *
 *  版权所有：北京用友政务软件有限公司
 *  未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 *  侵权者将受到法律追究。
 * /
 */

package com.ufgov.zc.client.zc.diyuctg;

import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.*;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.*;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZCDiYuCtg;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>PURPOSE:
 * <p>DESCRIPTION:
 * <p>CALLED BY:	qianmingjin
 * <p>CREATE DATE: 12-3-22
 * <p>UPDATE DATE: 12-3-22
 * <p>UPDATE USER: qianmingjin
 * <p>HISTORY:		1.0
 *
 * @author qianmingjin
 * @version 1.0
 * @see
 * @since java 1.5.0
 */
public class ZCDiYuCtgEditPanel extends AbstractMainSubEditPanel {

  private static final Logger logger = Logger.getLogger(ZCDiYuCtgEditPanel.class);

  protected RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private String compoId = "ZC_DIYU_CTG";

  private FuncButton addButton = new AddButton();

  private FuncButton editButton = new EditButton();

  private FuncButton previousButton = new PreviousButton();

  private FuncButton saveButton = new SaveButton();

  private FuncButton deleteButton = new DeleteButton();

  private FuncButton nextButton = new NextButton();

  private FuncButton exitButton = new ExitButton();

  protected ListCursor listCursor;

  protected ZCDiYuCtg oldBean;

  protected ZCDiYuCtgListPanel listPanel;

  protected JTablePanel itemTablePanel = new JTablePanel();

  private GkBaseDialog parent;

  ElementConditionDto elementConditionDto = new ElementConditionDto();

  protected String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  public CompanyFieldEditor coCode = null;

  public CheckBoxFieldEditor flagLocal = null;

  public RequestMeta getRequestMeta() {
    return requestMeta;
  }

  public void setRequestMeta(RequestMeta requestMeta) {
    this.requestMeta = requestMeta;
  }

  public ZCDiYuCtgEditPanel(Class billClass, BillElementMeta eleMeta) {
    super(billClass, eleMeta);
  }

  public ZCDiYuCtgEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZCDiYuCtgListPanel listPanel, String pageStatus) {
    super(ZCDiYuCtg.class, listPanel.getBillElementMeta());
    this.pageStatus = pageStatus;
    initDiYuCtgEditPanel(parent, listCursor, listPanel);
  }

  protected void initDiYuCtgEditPanel(GkBaseDialog parent, ListCursor listCursor,

  ZCDiYuCtgListPanel listPanel) {

    String title = "地域分类";

    this.listCursor = listCursor;

    this.listPanel = listPanel;

    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title,

    TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));

    this.parent = parent;

    this.colCount = 2;

    init();

    requestMeta.setCompoId(compoId);

    elementConditionDto.setCoCode(WorkEnv.getInstance().getCurrCoCode());

    refreshData();

  }

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {

    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    TextFieldEditor editor0 = new TextFieldEditor("地域代码", "diYuCode");
    editorList.add(editor0);
    editor0 = new TextFieldEditor("地域名称", "diYuName");
    editorList.add(editor0);
    return editorList;

  }

  @Override
  public JComponent createSubBillPanel() {
    JPanel subMainPanel = new JPanel();
    subMainPanel.setLayout(new BorderLayout());
    return subMainPanel;

  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(compoId);

    toolBar.add(addButton);

    toolBar.add(editButton);

    toolBar.add(saveButton);

    toolBar.add(deleteButton);

    toolBar.add(exitButton);

    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        // 新增
        doAdd();
      }
    });

    editButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doEdit();
      }
    });

    saveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        // 保存
        doSave();
      }
    });

    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        // 删除
        doDelete();
      }
    });

    exitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 退出
        doExit();
      }
    });
  }

  /**********************************************************************************
   * 以下处理的是按钮调用的方法
   **********************************************************************************/
  /**
   * 刷新list页面数据
   *
   * @param afterSaveBill
   * @param isRefreshButton
   */
  private void refreshAll(ZCDiYuCtg afterSaveBill, boolean isRefreshButton) {

    pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

    this.listCursor.setCurrentObject(afterSaveBill);

    refreshData();

  }

  /**
   * 保存前校验
   *
   * @param
   * @return
   */

  private boolean checkBeforeSave() {
    stopTableEditing();
    return true;

  }

  /**
   *
   */
  private void stopTableEditing() {
    JPageableFixedTable itemTable = this.itemTablePanel.getTable();
    if (itemTable.isEditing()) {
      itemTable.getCellEditor().stopCellEditing();
    }
  }

  /**
   * 判断数据是否已经被别人改变
   *
   * @return
   */
  public boolean isDataChanged() {
    stopTableEditing();
    return !DigestUtil.digest(oldBean).equals(DigestUtil.digest(listCursor.getCurrentObject()));
  }

  /**
   * 刷新数据
   */
  public void refreshData() {
    ZCDiYuCtg afterBill = (ZCDiYuCtg) listCursor.getCurrentObject();
    if (afterBill == null) {
      afterBill = new ZCDiYuCtg();
      afterBill.setBillStatus("0");
      pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;
      listCursor.setCurrentObject(afterBill);
      listCursor.getDataList().add(afterBill);
    }
    this.setEditingObject(afterBill);
    setOldObject();
    setButtonStatus();

    updateFieldEditorsEditable();

    this.fitTable();
  }

  /**
   * 存储老的bean数据
   */
  protected void setOldObject() {
    oldBean = (ZCDiYuCtg) ObjectUtil.deepCopy(listCursor.getCurrentObject());
  }

  /**
   * 设置工具条上按钮的可用性
   */
  protected void setButtonStatus() {
    if (ZcSettingConstants.PAGE_STATUS_BROWSE.equals(this.pageStatus)) {
      editButton.setEnabled(true);
      saveButton.setEnabled(false);
      deleteButton.setEnabled(false);
    }
    if (ZcSettingConstants.PAGE_STATUS_EDIT.equals(this.pageStatus)) {
      editButton.setEnabled(false);
      saveButton.setEnabled(true);
      deleteButton.setEnabled(true);
    }
    if (ZcSettingConstants.PAGE_STATUS_NEW.equals(this.pageStatus)) {
      editButton.setEnabled(false);
      saveButton.setEnabled(true);
      deleteButton.setEnabled(false);
    }
  }

  /**
   * 设置主表的字段是否可以编辑
   */
  @Override
  protected void updateFieldEditorsEditable() {
    super.updateFieldEditors();
    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW) || this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {
      for (AbstractFieldEditor fd : this.fieldEditors) {
        fd.setEnabled(true);
      }
    } else {
      for (AbstractFieldEditor fd : this.fieldEditors) {
        fd.setEnabled(false);
      }
    }
  }

  /**
   * *************************************************************************************************
   * 以下用来实现增、删、改、启用等主表的按钮功能
   * *************************************************************************************************
   */

  public void doEdit() {

    this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;

    updateFieldEditorsEditable();

    setButtonStatus();

  }

  /**
   * 新增
   */
  private void doAdd() {
    if (this.doExit()) {

      this.listPanel.doAdd();

    }
  }

  public boolean doSave() {

    if (!checkBeforeSave()) {

      return false;

    }

    boolean success = true;

    ZCDiYuCtg bean = null;

    String errorInfo = "";

    try {
      requestMeta.setFuncId(saveButton.getFuncId());

      ZCDiYuCtg zcDiYuCtg = (ZCDiYuCtg) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      bean = this.listPanel.getZcDiYuCtgserviceDelegate().saveZCDiYuCtg(zcDiYuCtg, requestMeta, pageStatus);

    } catch (Exception e) {

      logger.error(e.getMessage(), e);

      success = false;

      errorInfo += e.getMessage();

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      refreshAll(bean, true);

      this.listPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }
    return true;
  }

  protected void doDelete() {

    int num = JOptionPane.showConfirmDialog(this, "是否删除当前单据", "删除确认", 0);

    if (num == JOptionPane.YES_OPTION) {

      boolean success = true;

      ZCDiYuCtg zcDiYuCtg = null;

      String errorInfo = "";

      try {

        requestMeta.setFuncId(deleteButton.getFuncId());

        zcDiYuCtg = (ZCDiYuCtg) this.listCursor.getCurrentObject();

        if (!ZcSettingConstants.PAGE_STATUS_EDIT.equals(pageStatus)) {

          JOptionPane.showMessageDialog(this, "非编辑状态单据，不可以删除！", "提示", JOptionPane.ERROR_MESSAGE);
          return;
        }

        this.listPanel.getZcDiYuCtgserviceDelegate().deleteZCDiYuCtgList(zcDiYuCtg.getDiYuCode(), requestMeta);

      } catch (Exception e) {

        logger.error(e.getMessage(), e);

        success = false;

        errorInfo += e.getMessage();

      }

      if (success) {

        JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.listCursor.removeCurrentObject();

        refreshData();

        this.listPanel.refreshCurrentTabData();

      } else {

        JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }
  }

  public boolean doExit() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave()) {

          return false;

        }

      }

    }

    this.parent.dispose();

    return true;

  }
}
