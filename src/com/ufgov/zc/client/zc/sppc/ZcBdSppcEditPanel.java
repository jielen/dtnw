package com.ufgov.zc.client.zc.sppc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.AddButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.EnableButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.InvalidButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.PauseEvalExpertButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AutoNumFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CheckBoxFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcBdSppc;

@SuppressWarnings("unchecked")
public class ZcBdSppcEditPanel extends AbstractMainSubEditPanel {

  private static final Logger logger = Logger.getLogger(ZcBdSppcEditPanel.class);

  protected RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private String compoId = "ZC_XYGH_SPPC";

  private FuncButton addButton = new AddButton();

  private FuncButton editButton = new EditButton();

  private FuncButton previousButton = new PreviousButton();

  private FuncButton saveButton = new SaveButton();

  private FuncButton deleteButton = new DeleteButton();

  private FuncButton nextButton = new NextButton();

  private FuncButton exitButton = new ExitButton();

  // 送审
  private FuncButton auditButton = new EnableButton();

  // 销审
  private FuncButton unAuditButton = new PauseEvalExpertButton();

  // 作废
  private FuncButton cancelButton = new InvalidButton();

  protected ListCursor listCursor;

  protected ZcBdSppc oldBean;

  protected ZcBdSppcListPanel listPanel;

  protected JTablePanel itemTablePanel = new JTablePanel();

  private ZcBdSppcEditPanel self = this;

  private GkBaseDialog parent;

  ElementConditionDto elementConditionDto = new ElementConditionDto();

  protected String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  public CompanyFieldEditor coCode = null;

  public CheckBoxFieldEditor flagLocal = null;

  public ZcBdSppcEditPanel() {

  }

  public ZcBdSppcEditPanel(Class billClass, BillElementMeta eleMeta) {
    super(billClass, eleMeta);
  }

  public ZcBdSppcEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcBdSppcListPanel listPanel) {
    super(ZcBdSppc.class, listPanel.getBillElementMeta());
    initSppcEditPanel(parent, listCursor, listPanel);
  }

  protected void initSppcEditPanel(GkBaseDialog parent, ListCursor listCursor,

  ZcBdSppcListPanel listPanel) {

    String title = "商品批次";

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

    return createMainFieldEditors(editorList);

  }

  /**
   * 创建主表字段
   * @param editorList
   * @return
   */
  protected List<AbstractFieldEditor> createMainFieldEditors(List<AbstractFieldEditor> editorList) {

    AutoNumFieldEditor billCode = new AutoNumFieldEditor(LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_SPPC_ID), "zcSppcID", false);

    editorList.add(billCode);

    TextFieldEditor billName = new TextFieldEditor(LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_SPPC_NA), "zcSppcNa");

    editorList.add(billName);

    DateFieldEditor zcBgnDate = new DateFieldEditor(LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_BGN_DATE), "zcBgnDate");

    editorList.add(zcBgnDate);

    DateFieldEditor zcEndDate = new DateFieldEditor(LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_END_DATE), "zcEndDate");

    editorList.add(zcEndDate);

    AsValFieldEditor zcSppcNd = new AsValFieldEditor(LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_SPPC_ND), "zcSppcNd", "ZC_VS_SPPC_YARE");

    editorList.add(zcSppcNd);

    AsValFieldEditor zcSppcStatus = new AsValFieldEditor(LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_SPPC_STATUS), "zcSppcStatus", "ZC_VS_SPPC_STATUS");

    editorList.add(zcSppcStatus);

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

    toolBar.add(auditButton);

    toolBar.add(unAuditButton);

    toolBar.add(cancelButton);

    toolBar.add(deleteButton);

    toolBar.add(previousButton);

    toolBar.add(nextButton);

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

    auditButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        // 送审
        doAudit();
      }
    });

    unAuditButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        doUnAudit();
      }
    });

    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        doCancel();
      }
    });

    previousButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 上一页
        doPrevious();
      }
    });

    nextButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 下一页
        doNext();
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
   * @param afterSaveBill
   * @param isRefreshButton
   */
  private void refreshAll(ZcBdSppc afterSaveBill, boolean isRefreshButton) {

    pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

    this.listCursor.setCurrentObject(afterSaveBill);

    refreshData();

  }

  /**

   * 保存前校验

   * @param 

   * @return

   */

  private boolean checkBeforeSave() {

    stopTableEditing();

    ZcBdSppc bill = (ZcBdSppc) listCursor.getCurrentObject();

    StringBuffer errStr = new StringBuffer();

    if (bill.getZcSppcNa() == null || "".equals(bill.getZcSppcNa())) {
      errStr.append("<html><b><font size='3' color='red'>商品批次名称不能为空！</font></b></html>\n");

    }

    if (bill.getZcBgnDate() == null) {

      errStr.append("<html><b><font size='3' color='red'>请选择商品批次启用时间！</font></b></html>\n");

    }

    if (bill.getZcEndDate() == null) {

      errStr.append("<html><b><font size='3' color='red'>请选择商品批次失效时间！</font></b></html>\n");

    }

    if (bill.getZcBgnDate() != null && bill.getZcEndDate() != null) {
      if (bill.getZcBgnDate().compareTo(bill.getZcEndDate()) > 0)
        errStr.append("<html><b><font size='3' color='red'>商品批次启用时间不能大于失效时间！</font></b></html>\n");
    }

    if (errStr.toString().length() > 0) {

      errStr.append("");

      JOptionPane.showMessageDialog(this, errStr.toString(), "提示", JOptionPane.ERROR_MESSAGE);

      return false;

    }

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
    ZcBdSppc afterBill = (ZcBdSppc) listCursor.getCurrentObject();

    boolean isNew;

    if (afterBill == null) {

      isNew = true;

      afterBill = new ZcBdSppc();

      afterBill.setZcSppcStatus("0");

      afterBill.setZcSppcNd(String.valueOf(requestMeta.getSvNd()));

      pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;

      listCursor.setCurrentObject(afterBill);

      listCursor.getDataList().add(afterBill);

    }

    this.setEditingObject(afterBill);

    setOldObject();

    //   setButtonStatus(afterBill, requestMeta, listCursor);

    setButtonStatus();

    updateFieldEditorsEditable();

    this.fitTable();
  }

  /**
   * 存储老的bean数据
   */
  protected void setOldObject() {
    oldBean = (ZcBdSppc) ObjectUtil.deepCopy(listCursor.getCurrentObject());
  }

  /**
   * 设置工具条上按钮的可用性
   */
  protected void setButtonStatus() {
    if (this.btnStatusList.size() == 0) {

      ButtonStatus bs = new ButtonStatus();

      bs = new ButtonStatus();

      bs.setButton(this.editButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus("0");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.saveButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_NEW);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.deleteButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus("0");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.auditButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus("0");

      bs.addBillStatus("suspended");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.unAuditButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus("exec");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.previousButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.nextButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.cancelButton);

      bs.addBillStatus("suspended");
      bs.addBillStatus("exec");

      btnStatusList.add(bs);

    }

    ZcBdSppc obj = (ZcBdSppc) this.listCursor.getCurrentObject();

    String billStatus = obj.getZcSppcStatus();

    ZcUtil.setButtonEnable(this.btnStatusList, billStatus, this.pageStatus, this.compoId, obj.getProcessInstId());
    //liubo
    if ("0".equals(billStatus)) {
      this.previousButton.setVisible(false);
      this.nextButton.setVisible(false);
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

        if ("zcSppcStatus".equals(fd.getFieldName())) {

          fd.setEnabled(false);

        } else {

          fd.setEnabled(true);

        }

      }
    } else {
      for (AbstractFieldEditor fd : this.fieldEditors) {
        fd.setEnabled(false);
      }
    }
  }

  /****************************************************************************************************
   * 以下用来实现增、删、改、启用等主表的按钮功能
   ***************************************************************************************************/

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

    if (!isDataChanged()) {
      pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      setButtonStatus();

      return true;

    }

    boolean success = true;

    ZcBdSppc updBean = new ZcBdSppc();

    String errorInfo = "";

    try {

      requestMeta.setFuncId(saveButton.getFuncId());

      ZcBdSppc inData = (ZcBdSppc) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      updBean = this.listPanel.getZcBdSppcServiceDelegate().updateZcBdSppcByPrimaryKey(inData, requestMeta);

    } catch (Exception e) {

      logger.error(e.getMessage(), e);

      success = false;

      errorInfo += e.getMessage();

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      refreshAll(updBean, true);

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

      ZcBdSppc zcBdSppc = null;

      String errorInfo = "";

      try {

        requestMeta.setFuncId(deleteButton.getFuncId());

        zcBdSppc = (ZcBdSppc) this.listCursor.getCurrentObject();

        if (!"0".equals(zcBdSppc.getZcSppcStatus()))

          JOptionPane.showMessageDialog(this, "非编辑状态单据，不可以删除！", "提示", JOptionPane.ERROR_MESSAGE);

        this.listPanel.getZcBdSppcServiceDelegate().deleteByPrimaryKey(zcBdSppc.getZcSppcID(), requestMeta);

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

  private void doAudit() {
    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    int num = JOptionPane.showConfirmDialog(this, "确认启用本次商品批次?", "启用确认", 0);

    if (num == JOptionPane.NO_OPTION) {

      return;

    }

    boolean success = true;

    ZcBdSppc afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.auditButton.getFuncId());

      ZcBdSppc zcBdSppc = (ZcBdSppc) this.listCursor.getCurrentObject();

      zcBdSppc.setZcSppcStatus("exec");

      afterSaveBill = this.listPanel.getZcBdSppcServiceDelegate().updateZcBdSppcByPrimaryKey(zcBdSppc, requestMeta);

    } catch (Exception ex) {

      errorInfo += ex.getMessage();

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "商品批次启用成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    }
  }

  private void doUnAudit() {
    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    int num = JOptionPane.showConfirmDialog(this, "确认暂停本次商品批次?", "暂停确认", 0);

    if (num == JOptionPane.NO_OPTION) {

      return;

    }

    boolean success = true;

    ZcBdSppc afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.auditButton.getFuncId());

      ZcBdSppc zcBdSppc = (ZcBdSppc) this.listCursor.getCurrentObject();

      zcBdSppc.setZcSppcStatus("suspended");

      afterSaveBill = this.listPanel.getZcBdSppcServiceDelegate().updateZcBdSppcByPrimaryKey(zcBdSppc, requestMeta);

    } catch (Exception ex) {

      errorInfo += ex.getMessage();

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "商品暂停成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    }
  }

  private void doCancel() {
    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    int num = JOptionPane.showConfirmDialog(this, "确认作废本次商品批次?", "作废确认", 0);

    if (num == JOptionPane.NO_OPTION) {

      return;

    }

    boolean success = true;

    ZcBdSppc afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.auditButton.getFuncId());

      ZcBdSppc zcBdSppc = (ZcBdSppc) this.listCursor.getCurrentObject();

      zcBdSppc.setZcSppcStatus("cancel");

      zcBdSppc = this.listPanel.getZcBdSppcServiceDelegate().updateZcBdSppcByPrimaryKey(zcBdSppc, requestMeta);
      listCursor.setCurrentObject(zcBdSppc);

    } catch (Exception ex) {

      errorInfo += ex.getMessage();

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      refreshData();

      JOptionPane.showMessageDialog(this, "商品作废成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

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

  private void doPrevious() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave()) {

          return;

        }

      } else {

        listCursor.setCurrentObject(oldBean);

      }

    }

    listCursor.previous();

    refreshData();

  }

  private void doNext() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave()) {

          return;

        }

      } else {

        listCursor.setCurrentObject(oldBean);

      }

    }

    listCursor.next();

    refreshData();

  }

}