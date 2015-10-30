package com.ufgov.zc.client.zc.pinp;

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
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
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
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcBBrand;
import com.ufgov.zc.common.zc.model.ZcBdSppc;
import com.ufgov.zc.common.zc.model.ZcEbSupplier;

@SuppressWarnings("unchecked")
public class ZcBdPinpEditPanel extends AbstractMainSubEditPanel {

  private static final Logger logger = Logger.getLogger(ZcBdPinpEditPanel.class);

  protected RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private String compoId = "ZC_ZB_PINP";

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

  protected ZcBBrand oldBean;

  protected ZcBdPinpListPanel listPanel;

  protected JTablePanel itemTablePanel = new JTablePanel();

  private ZcBdPinpEditPanel self = this;

  private GkBaseDialog parent;

  ElementConditionDto elementConditionDto = new ElementConditionDto();

  protected String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  public CompanyFieldEditor coCode = null;

  public CheckBoxFieldEditor flagLocal = null;

  public IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(
    IBaseDataServiceDelegate.class,

    "baseDataServiceDelegate");

  ElementConditionDto elementDto = new ElementConditionDto();

  public ZcBdPinpEditPanel() {

  }

  public ZcBdPinpEditPanel(Class billClass, BillElementMeta eleMeta) {
    super(billClass, eleMeta);
  }

  public ZcBdPinpEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcBdPinpListPanel listPanel) {
    super(ZcBBrand.class, listPanel.getBillElementMeta());
    initPinpEditPanel(parent, listCursor, listPanel);
  }

  protected void initPinpEditPanel(GkBaseDialog parent, ListCursor listCursor,

  ZcBdPinpListPanel listPanel) {

    String title = LangTransMeta.translate("ZC_ZB_PINP_TITLE");

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

    AutoNumFieldEditor zcBraCode = new AutoNumFieldEditor(LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_BRA_CODE), "zcBraCode", false);

    editorList.add(zcBraCode);

    TextFieldEditor zcBraName = new TextFieldEditor(LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_BRA_NAME), "zcBraName");

    editorList.add(zcBraName);

    String suColumNames[] = { "供应商代码", "供应商名称" };

    SuSelectedHandler suHandler = new SuSelectedHandler(suColumNames);

    ForeignEntityFieldEditor suId = new ForeignEntityFieldEditor("ZcEbSupplier.getZcEbSupplierList", elementDto, 10,
      suHandler, suColumNames,

      "供应商代码", "zcSuCode");

    editorList.add(suId);

    TextFieldEditor zcSuName = new TextFieldEditor(LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_SU_NAME), "zcSuName");

    editorList.add(zcSuName);

    String pcColumNames[] = { "商品批次代码", "商品批次名称", "年度" };

    PcSelectedHandler pcHandler = new PcSelectedHandler(pcColumNames);

    ForeignEntityFieldEditor pcId = new ForeignEntityFieldEditor("ZcBdSppc.selectEnableSppc", elementDto, 10,
      pcHandler, pcColumNames,

      "商品批次代码", "zcSppcID");

    editorList.add(pcId);

    TextFieldEditor zcSppcNa = new TextFieldEditor(LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_SPPC_NA), "zcSppcNa");

    editorList.add(zcSppcNa);

    TextFieldEditor zcYear = new TextFieldEditor(LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_SPPC_ND), "zcYear");

    editorList.add(zcYear);

    AsValFieldEditor zcSppcStatus = new AsValFieldEditor(LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_ZBPP_STATUS), "zcZbPpStatus", "ZC_VS_PINP_STATUS");

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
  private void refreshAll(ZcBBrand afterSaveBill, boolean isRefreshButton) {

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

    ZcBBrand bill = (ZcBBrand) listCursor.getCurrentObject();

    StringBuffer errStr = new StringBuffer();

    if (bill.getZcBraName() == null || "".equals(bill.getZcBraName())) {
      errStr.append("<html><b><font size='3' color='red'>品牌名称不能为空！</font></b></html>\n");
    }

    if (bill.getZcSppcID() == null || "".equals(bill.getZcSppcID())) {
      errStr.append("<html><b><font size='3' color='red'>商品批次信息不能为空！</font></b></html>\n");
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
    ZcBBrand afterBill = (ZcBBrand) listCursor.getCurrentObject();

    boolean isNew;

    if (afterBill == null) {

      isNew = true;

      afterBill = new ZcBBrand();

      afterBill.setZcZbPpStatus("0");

      // afterBill.setZcYear(String.valueOf(requestMeta.getSvNd()));

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
    oldBean = (ZcBBrand) ObjectUtil.deepCopy(listCursor.getCurrentObject());
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

    }

    ZcBBrand obj = (ZcBBrand) this.listCursor.getCurrentObject();

    String billStatus = obj.getZcZbPpStatus();

    ZcUtil.setButtonEnable(this.btnStatusList, billStatus, this.pageStatus, this.compoId, obj.getProcessInstId());
  }

  /**
   * 设置主表的字段是否可以编辑
   */
  @Override
  protected void updateFieldEditorsEditable() {

    super.updateFieldEditors();

    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)
      || this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {

        if ("zcZbPpStatus".equals(fd.getFieldName()) || "zcSuName".equals(fd.getFieldName())
          || "zcYear".equals(fd.getFieldName()) || "zcSppcNa".equals(fd.getFieldName())) {

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

    if (!isDataChanged()) {

      if (!checkBeforeSave()) {

        return false;

      }

      pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      setButtonStatus();

      return true;

    }

    if (!checkBeforeSave()) {

      return false;

    }

    boolean success = true;

    ZcBBrand updBean = new ZcBBrand();

    String errorInfo = "";

    try {

      requestMeta.setFuncId(saveButton.getFuncId());

      ZcBBrand inData = (ZcBBrand) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      updBean = this.listPanel.getZcBdPinpServiceDelegate().updateByPrimaryKey(inData, requestMeta);

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

      ZcBBrand zcBdPinp = null;

      String errorInfo = "";

      try {

        requestMeta.setFuncId(deleteButton.getFuncId());

        zcBdPinp = (ZcBBrand) this.listCursor.getCurrentObject();

        if (!"0".equals(zcBdPinp.getZcZbPpStatus()))

          JOptionPane.showMessageDialog(this, "非编辑状态单据，不可以删除！", "提示", JOptionPane.ERROR_MESSAGE);

        this.listPanel.getZcBdPinpServiceDelegate().deleteByPrimaryKey(zcBdPinp.getZcBraCode(), requestMeta);

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

    int num = JOptionPane.showConfirmDialog(this, "确认启用此品牌?", "审核", 0);

    if (num == JOptionPane.NO_OPTION) {

      return;

    }

    boolean success = true;

    ZcBBrand afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.auditButton.getFuncId());

      ZcBBrand zcBdPinp = (ZcBBrand) this.listCursor.getCurrentObject();

      zcBdPinp.setZcZbPpStatus("exec");

      afterSaveBill = this.listPanel.getZcBdPinpServiceDelegate().updateByPrimaryKey(zcBdPinp, requestMeta);

    } catch (Exception ex) {

      errorInfo += ex.getMessage();

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "品牌启用成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    }
  }

  private void doUnAudit() {
    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    int num = JOptionPane.showConfirmDialog(this, "确认暂停本次品牌?", "审核", 0);

    if (num == JOptionPane.NO_OPTION) {

      return;

    }

    boolean success = true;

    ZcBBrand afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.auditButton.getFuncId());

      ZcBBrand zcBdPinp = (ZcBBrand) this.listCursor.getCurrentObject();

      zcBdPinp.setZcZbPpStatus("suspended");

      afterSaveBill = this.listPanel.getZcBdPinpServiceDelegate().updateByPrimaryKey(zcBdPinp, requestMeta);

    } catch (Exception ex) {

      errorInfo += ex.getMessage();

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "品牌暂停成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    }
  }

  private void doCancel() {
    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    int num = JOptionPane.showConfirmDialog(this, "确认启用本次品牌?", "审核", 0);

    if (num == JOptionPane.NO_OPTION) {

      return;

    }

    boolean success = true;

    ZcBBrand afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.auditButton.getFuncId());

      ZcBBrand zcBdPinp = (ZcBBrand) this.listCursor.getCurrentObject();

      zcBdPinp.setZcZbPpStatus("cancel");

      this.listPanel.getZcBdPinpServiceDelegate().updateByPrimaryKey(zcBdPinp, requestMeta);

    } catch (Exception ex) {

      errorInfo += ex.getMessage();

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      refreshData();

      JOptionPane.showMessageDialog(this, "品牌作废成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

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

  //===========================================以下为外部部件===================================
  /**
   * 品牌中标商外部部件
   * @author Administrator
   *
   */
  private class SuSelectedHandler implements IForeignEntityHandler {

    private final String columNames[];

    public SuSelectedHandler(String columNames[]) {

      this.columNames = columNames;

    }

    public void afterClear() {
      ZcBBrand bill = (ZcBBrand) listCursor.getCurrentObject();
      bill.setZcSuCode("");
      bill.setZcSuName("");
      setEditingObject(bill);
    }

    public void excute(List selectedDatas) {

      ZcBBrand bill = (ZcBBrand) listCursor.getCurrentObject();

      for (Object object : selectedDatas) {

        ZcEbSupplier suBean = (ZcEbSupplier) object;

        bill.setZcSuCode(suBean.getCode());

        bill.setZcSuName(suBean.getName());
      }

      setEditingObject(bill);
    }

    public TableModel createTableModel(List showDatas) {
      Object data[][] = new Object[showDatas.size()][columNames.length];

      String sheetColumNames[] = { "供应商代码", "供应商名称" };

      for (int i = 0; i < showDatas.size(); i++) {

        ZcEbSupplier rowData = (ZcEbSupplier) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getCode();

        data[i][col++] = rowData.getName();

      }

      MyTableModel model = new MyTableModel(data, columNames) {

        @Override
        public boolean isCellEditable(int row, int colum) {

          return false;

        }

      };

      return model;

    }

    public boolean isMultipleSelect() {

      return false;

    }

  }

  /**
   * 商品批次外部部件
   * @author Administrator
   *
   */
  private class PcSelectedHandler implements IForeignEntityHandler {

    private final String columNames[];

    public PcSelectedHandler(String columNames[]) {

      this.columNames = columNames;

    }
    
    public void afterClear() {
      ZcBBrand bill = (ZcBBrand) listCursor.getCurrentObject();
      bill.setZcSppcID("");
      bill.setZcSppcNa("");
      setEditingObject(bill);
    }

    public void excute(List selectedDatas) {

      ZcBBrand bill = (ZcBBrand) listCursor.getCurrentObject();

      for (Object object : selectedDatas) {

        ZcBdSppc pcBean = (ZcBdSppc) object;

        bill.setZcSppcID(pcBean.getZcSppcID());

        bill.setZcSppcNa(pcBean.getZcSppcNa());

        bill.setZcYear(pcBean.getZcSppcNd());
      }

      setEditingObject(bill);
    }

    public TableModel createTableModel(List showDatas) {
      Object data[][] = new Object[showDatas.size()][columNames.length];

      String pcColumNames[] = { "商品批次代码", "商品批次名称", "年度" };

      for (int i = 0; i < showDatas.size(); i++) {

        ZcBdSppc rowData = (ZcBdSppc) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getZcSppcID();

        data[i][col++] = rowData.getZcSppcNa();

        data[i][col++] = rowData.getZcSppcNd();
      }

      MyTableModel model = new MyTableModel(data, columNames) {

        @Override
        public boolean isCellEditable(int row, int colum) {

          return false;

        }

      };

      return model;

    }

    public boolean isMultipleSelect() {

      return false;

    }

  }

}