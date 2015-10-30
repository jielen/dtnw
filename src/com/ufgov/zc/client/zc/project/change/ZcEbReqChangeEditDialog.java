package com.ufgov.zc.client.zc.project.change;import java.awt.BorderLayout;import java.awt.Color;import java.awt.Dialog;import java.awt.Dimension;import java.awt.Font;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.util.ArrayList;import java.util.Date;import java.util.List;import javax.swing.BorderFactory;import javax.swing.JComponent;import javax.swing.JLabel;import javax.swing.JOptionPane;import javax.swing.JPanel;import javax.swing.JTabbedPane;import javax.swing.JTextArea;import javax.swing.border.TitledBorder;import javax.swing.table.TableModel;import com.ufgov.zc.client.common.ListCursor;import com.ufgov.zc.client.common.MyTableModel;import com.ufgov.zc.client.common.ServiceFactory;import com.ufgov.zc.client.common.UIConstants;import com.ufgov.zc.client.common.WorkEnv;import com.ufgov.zc.client.common.ZcWorkFlowAdapter;import com.ufgov.zc.client.component.GkBaseDialog;import com.ufgov.zc.client.component.JFuncToolBar;import com.ufgov.zc.client.component.button.AuditPassButton;import com.ufgov.zc.client.component.button.CallbackButton;import com.ufgov.zc.client.component.button.DeleteButton;import com.ufgov.zc.client.component.button.EditButton;import com.ufgov.zc.client.component.button.ExitButton;import com.ufgov.zc.client.component.button.FuncButton;import com.ufgov.zc.client.component.button.InvalidButton;import com.ufgov.zc.client.component.button.IsSendToNextButton;import com.ufgov.zc.client.component.button.NextButton;import com.ufgov.zc.client.component.button.OpenNotepadButton;import com.ufgov.zc.client.component.button.PreviousButton;import com.ufgov.zc.client.component.button.SaveButton;import com.ufgov.zc.client.component.button.SendButton;import com.ufgov.zc.client.component.button.SuggestAuditPassButton;import com.ufgov.zc.client.component.button.TraceButton;import com.ufgov.zc.client.component.button.UnauditButton;import com.ufgov.zc.client.component.button.UntreadButton;import com.ufgov.zc.client.component.event.ValueChangeEvent;import com.ufgov.zc.client.component.event.ValueChangeListener;import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;import com.ufgov.zc.client.component.zc.fieldeditor.SelectFileFieldEditor;import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;import com.ufgov.zc.client.zc.ButtonStatus;import com.ufgov.zc.client.zc.ZcUtil;import com.ufgov.zc.client.zc.notepad.ZcNotepadDialog;import com.ufgov.zc.common.system.Guid;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.constants.ZcSettingConstants;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.system.model.AsFile;import com.ufgov.zc.common.system.util.DigestUtil;import com.ufgov.zc.common.system.util.ObjectUtil;import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;import com.ufgov.zc.common.zc.model.ZcBaseBill;import com.ufgov.zc.common.zc.model.ZcEbProj;import com.ufgov.zc.common.zc.model.ZcEbProjChangePack;import com.ufgov.zc.common.zc.model.ZcEbReqChange;import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;@SuppressWarnings("unchecked")public class ZcEbReqChangeEditDialog extends GkBaseDialog {  private static final long serialVersionUID = -56873481859200532L;  private final ZcEbReqChangeEditDialog self = this;  private final ZcEbReqEditPanel editPanel;  private final RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();  public IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,    "zcEbBaseServiceDelegate");  public ZcEbReqChangeEditDialog(ZcEbReqChangeListPanel listPanel, List beanList, int editingRow, String tabStatus, String title) {    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);    editPanel = new ZcEbReqEditPanel(new ListCursor(beanList, editingRow), tabStatus, listPanel);    setLayout(new BorderLayout());    add(editPanel);    this.setTitle(title);    this.setSize(UIConstants.SCREEN_WIDTH, UIConstants.SCREEN_HEIGHT);    this.moveToScreenCenter();    this.setVisible(true);    this.repaint();  }  private boolean yesConfirmed = true;  @Override  protected boolean dialogIsClosing() {    if (editPanel.isDataChanged() && yesConfirmed) {      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);      if (num == JOptionPane.YES_OPTION) {        return editPanel.doSave();      } else {        yesConfirmed = false;      }    }    return true;  }  private class ZcEbReqEditPanel extends AbstractMainSubEditPanel {    private static final long serialVersionUID = -2539657260090189021L;    private RequestMeta requestMeta = null;    private IForeignEntityHandler handler;    private final FuncButton invalidButton = new InvalidButton();    private final FuncButton previousButton = new PreviousButton();    private final FuncButton saveButton = new SaveButton();    private final FuncButton deleteButton = new DeleteButton();    private final FuncButton nextButton = new NextButton();    private final FuncButton exitButton = new ExitButton();    // 工作流送审    private final FuncButton sendButton = new SendButton();    //是否送主任审核    private final FuncButton isSendToNextButton = new IsSendToNextButton();    // 工作流收回    private final FuncButton callbackButton = new CallbackButton();    // 工作流填写意见审核通过    private final FuncButton suggestPassButton = new SuggestAuditPassButton();    // 工作流审核通过    private final FuncButton auditPassButton = new AuditPassButton();    // 工作流销审    private final FuncButton unAuditButton = new UnauditButton();    // 工作流退回    private final FuncButton unTreadButton = new UntreadButton();    private final FuncButton editButton = new EditButton();    // 工作流流程跟踪    private final FuncButton traceButton = new TraceButton();    public FuncButton openNotepadButton = new OpenNotepadButton();    private final ListCursor listCursor;    private ZcEbReqChange oldProj;    private final ZcEbReqChangeListPanel listPanel;    private SelectFileFieldEditor reasonFile;    private JTabbedPane itemTabPane = null;    private JTextArea changeReasonMemo = null;    ForeignEntityFieldEditor projectSelectEditor;    private final ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();    private String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;    private final List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();    private final String tabStatus;    public IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,      "zcEbBaseServiceDelegate");    public ZcEbReqEditPanel(ListCursor listCursor, String tabStatus, ZcEbReqChangeListPanel listPanel) {      super(ZcEbProj.class, listPanel.getBillElementMeta());      this.tabStatus = tabStatus;      this.listCursor = listCursor;      this.listPanel = listPanel;      this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "需求变更", TitledBorder.CENTER, TitledBorder.TOP,        new Font("宋体", Font.BOLD, 15), Color.BLUE));      this.colCount = 3;      requestMeta = listPanel.getRequestMeta();      getFieldEditors();      init();      refreshData();    }    @Override    public List<AbstractFieldEditor> createFieldEditors() {      return editorList;    }    public List<AbstractFieldEditor> getFieldEditors() {      ZcEbReqChange projChg = (ZcEbReqChange) listCursor.getCurrentObject();      String columNames[] = { "采购委托编号", "任务通知书号", "项目名称", "采购预算", "采购单位联系人", "采购单位电话" };      ProjectDetailHandler handler = new ProjectDetailHandler(columNames);      ElementConditionDto dto = new ElementConditionDto();      dto.setCoCodeFilter(requestMeta.getSvCoCode());      projectSelectEditor = new ForeignEntityFieldEditor("ZcEbProjChange.getReqChgEntrust", dto, 20, handler, columNames, "任务单号", "snCode");      TextFieldEditor zcMakeCode = new TextFieldEditor("项目编号", "zcMakeCode");      zcMakeCode.setEnabled(false);      TextFieldEditor zcMakeName = new TextFieldEditor("项目名称", "zcMakeName");      zcMakeName.setEnabled(false);      if (projChg != null && projChg.getChangeCode() != null && "".equals(projChg.getChangeCode())) {        TextFieldEditor chgId = new TextFieldEditor("变更单号", "changeCode");        editorList.add(chgId);      }      MoneyFieldEditor zcMoneySrcBiSum = new MoneyFieldEditor("采购预算", "zcMoneyBiSum");      zcMoneySrcBiSum.setEnabled(false);      TextFieldEditor remark = new TextFieldEditor("备注", "remark");      CompanyFieldEditor coCode = new CompanyFieldEditor("预算单位", "coCode");      coCode.setEnabled(false);      TextFieldEditor zcMakeLink = new TextFieldEditor("预算单位联系人", "zcMakeLink");      zcMakeLink.setEnabled(false);      TextFieldEditor zcMakeTel = new TextFieldEditor("预算单位电话", "zcMakeTel");      zcMakeTel.setEnabled(false);      DateFieldEditor inputDate = new DateFieldEditor("录入时间", "zcInputDate");      inputDate.setEnabled(false);      if (projChg != null && projChg.getChangeCode() != null) {        projectSelectEditor.setEnabled(false);      }      editorList.add(coCode);      editorList.add(projectSelectEditor);      editorList.add(zcMakeCode);      editorList.add(zcMakeName);      editorList.add(zcMoneySrcBiSum);      editorList.add(inputDate);      editorList.add(zcMakeLink);      editorList.add(zcMakeTel);      editorList.add(remark);      return editorList;    }    @Override    public JComponent createSubBillPanel() {      itemTabPane = new JTabbedPane();      itemTabPane.setTabPlacement(JTabbedPane.TOP);      itemTabPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);      itemTabPane.setPreferredSize(new Dimension(300, 200));      JPanel ctPanel = new JPanel();      ctPanel.setBounds(50, 50, 200, 180);      ctPanel.setLayout(null);      itemTabPane.addTab("变更原因", ctPanel);      JLabel label1 = new JLabel("变更原因说明：");      label1.setBounds(100, 22, 100, 24);      label1.setLayout(null);      ctPanel.add(label1);      JLabel xygh = new JLabel("(情况说明最多可输入500个汉字)");      xygh.setForeground(new Color(254, 70, 1));      xygh.setFont(new Font("宋体", Font.BOLD, 12));      xygh.setBounds(100, 42, 400, 48);      xygh.setLayout(null);      ctPanel.add(xygh);      changeReasonMemo = new JTextArea();      changeReasonMemo.setBounds(100, 84, 600, 280);      changeReasonMemo.setLayout(null);      changeReasonMemo.setBackground(Color.WHITE);      changeReasonMemo.setLineWrap(true);//wkw  实现自动换行      ctPanel.add(changeReasonMemo);      JLabel label2 = new JLabel("上传变更原因附件：");      label2.setBounds(100, 400, 140, 24);      label2.setLayout(null);//将缺省的布局管理器清除      ctPanel.add(label2);      reasonFile = new SelectFileFieldEditor("上传变更原因附件", "changeReasonFilename", "zcChangeFileBlobid", true, true, true, true);      reasonFile.setBounds(100, 436, 300, 24);      ctPanel.add(reasonFile);      reasonFile.addValueChangeListener(new ValueChangeListener() {        public void valueChanged(ValueChangeEvent e) {          fileCheck();        }      });      return itemTabPane;    }    private void addPackTablePanel(ZcEbReqChange proj) {      fieldEditorPanel.removeAll();      initFieldEditorPanel();      fieldEditorPanel.updateUI();    }    private void setPackDefaultValue(ZcEbProjChangePack pack, String status) {      pack.setId(Guid.genID());      pack.setChgID(oldProj.getChangeCode());    }    private void refreshAll(ZcEbReqChange afterSaveProj, boolean isRefreshButton) {      this.listCursor.setCurrentObject(afterSaveProj);      pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;      refreshData();    }    private void setMyEditingObject(ZcEbReqChange proj) {      this.setEditingObject(proj);      this.changeReasonMemo.setText(proj.getChangeReason());      this.reasonFile.setEditObject(proj);    }    public boolean isDataChanged() {      return !DigestUtil.digest(oldProj).equals(DigestUtil.digest(this.getProjWithoutTempId()));    }    private void refreshData() {      ZcEbReqChange proj = (ZcEbReqChange) this.listCursor.getCurrentObject();      if (proj == null) {        // 新增的单据        proj = new ZcEbReqChange();        proj.setStatus("0");        proj.setNd(requestMeta.getSvNd());        proj.setZcInputDate(new Date());        proj.setZcInputCode(requestMeta.getSvUserID());        proj.setCoCode(requestMeta.getSvCoCode());        listCursor.getDataList().add(proj);        pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;      }      this.setMyEditingObject(proj);      // 根据工作流模版设置字段是否可编辑      addPackTablePanel(proj);      updateWFEditorEditable(proj, requestMeta);      updateFieldEditorsEditable();      setButtonStatus();      // 根据工作流模版设置功能按钮是否可用      setButtonStatus(proj, requestMeta, this.listCursor);      listCursor.setCurrentObject(proj);      setOldObject();    }    private void setOldObject() {      oldProj = this.getProjWithoutTempId();    }    private ZcEbReqChange getProjWithoutTempId() {      return (ZcEbReqChange) ObjectUtil.deepCopy(listCursor.getCurrentObject());    }    @Override    public void initToolBar(JFuncToolBar toolBar) {      toolBar.setModuleCode("ZC");      toolBar.setCompoId(ZcEbReqChangeListPanel.compoId);      toolBar.add(editButton);      toolBar.add(saveButton);      toolBar.add(sendButton);      toolBar.add(suggestPassButton);      toolBar.add(auditPassButton);      toolBar.add(callbackButton);      toolBar.add(unAuditButton);      toolBar.add(unTreadButton);      toolBar.add(traceButton);      toolBar.add(invalidButton);      toolBar.add(deleteButton);      toolBar.add(previousButton);      toolBar.add(openNotepadButton);      toolBar.add(nextButton);      toolBar.add(exitButton);      editButton.addActionListener(new ActionListener() {        public void actionPerformed(ActionEvent e) {          // 修改          doEdit();        }      });      saveButton.addActionListener(new ActionListener() {        public void actionPerformed(ActionEvent e) {          // 保存          doSave();        }      });      invalidButton.addActionListener(new ActionListener() {        @Override        public void actionPerformed(ActionEvent e) {          // TODO Auto-generated method stub          doInvalid();        }      });      deleteButton.addActionListener(new ActionListener() {        public void actionPerformed(ActionEvent e) {          // 删除          doDelete();        }      });      sendButton.addActionListener(new ActionListener() {        public void actionPerformed(ActionEvent e) {          // 送审          doSend();        }      });      callbackButton.addActionListener(new ActionListener() {        public void actionPerformed(ActionEvent e) {          // 收回          doCallback();        }      });      suggestPassButton.addActionListener(new ActionListener() {        public void actionPerformed(ActionEvent arg0) {          // 填写意见审核          doSuggestPass();        }      });      auditPassButton.addActionListener(new ActionListener() {        public void actionPerformed(ActionEvent e) {          // 审核          doAudit();        }      });      unAuditButton.addActionListener(new ActionListener() {        public void actionPerformed(ActionEvent e) {          // 销审          doUnaudit();        }      });      unTreadButton.addActionListener(new ActionListener() {        public void actionPerformed(ActionEvent e) {          // 退回          doUntread();        }      });      traceButton.addActionListener(new ActionListener() {        public void actionPerformed(ActionEvent e) {          // 流程跟踪          doTrace();        }      });      exitButton.addActionListener(new ActionListener() {        public void actionPerformed(ActionEvent e) {          // 退出          doExit();        }      });      openNotepadButton.addActionListener(new ActionListener() {        public void actionPerformed(ActionEvent e) {          // 退出          doOpenNotepad();        }      });    }    private void doOpenNotepad() {      ZcEbReqChange bean = (ZcEbReqChange) ZcEbReqEditPanel.this.listCursor.getCurrentObject();      new ZcNotepadDialog(bean.getSn(), self);    }    //检查上传的变更原因文件内容    private void fileCheck() {      if (this.reasonFile == null)        return;      AsFile bidDoc = this.reasonFile.getFileUploader().getFile();      if (bidDoc == null)        return;      ZcEbReqChange curr = (ZcEbReqChange) listCursor.getCurrentObject();      curr.setZcChangeFileBlobid(bidDoc.getFileId());      //      curr.setChangeReasonFileName(bidDoc.getFileName());    }    //将变更原因内容同步到当前编辑的Object中    private void syncValue() {      ZcEbReqChange bean = (ZcEbReqChange) ZcEbReqEditPanel.this.listCursor.getCurrentObject();      if (changeReasonMemo.getText() != null) {        bean.setChangeReason(changeReasonMemo.getText());      }    }    public void doInvalid() {      boolean success = true;      String errorInfo = "";      int num = JOptionPane.showConfirmDialog(this, "是否作废", "作废确认", 0);      if (num != 0) {        return;      }      try {        requestMeta.setFuncId(invalidButton.getFuncId());        String chgId = ((ZcEbReqChange) listCursor.getCurrentObject()).getChangeCode();        if (chgId != null && !"".equals(chgId)) {          listPanel.zcEbProjectChangeServiceDelegate.updateInvalid(chgId, requestMeta);        }      } catch (Exception e) {        logger.error(e.getMessage(), e);        success = false;        errorInfo += e.getMessage();      }      if (success) {        JOptionPane.showMessageDialog(this, "作废成功！", "提示", JOptionPane.INFORMATION_MESSAGE);        this.listPanel.refreshCurrentTabData();        this.listCursor.removeCurrentObject();        this.refreshData();      } else {        JOptionPane.showMessageDialog(this, "作废失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);      }    }    public void doDelete() {      boolean success = true;      String errorInfo = "";      int num = JOptionPane.showConfirmDialog(this, "确定删除吗", "删除确认", 0);      if (num != 0) {        return;      }      try {        requestMeta.setFuncId(deleteButton.getFuncId());        ZcEbReqChange projChange = ((ZcEbReqChange) listCursor.getCurrentObject());        if (projChange.getChangeCode() != null && !"".equals(projChange.getChangeCode())) {          listPanel.zcEbProjectChangeServiceDelegate.deleteZcEbReqChange(projChange.getChangeCode(), requestMeta);        }      } catch (Exception e) {        logger.error(e.getMessage(), e);        success = false;        errorInfo += e.getMessage();      }      if (success) {        JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);        this.listPanel.refreshCurrentTabData();        this.listCursor.removeCurrentObject();        this.refreshData();      } else {        JOptionPane.showMessageDialog(this, "删除失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);      }    }    public boolean doSave() {      syncValue();      ZcEbReqChange proj = (ZcEbReqChange) listCursor.getCurrentObject();      if (!isDataChanged()) {        JOptionPane.showMessageDialog(this, "数据没有发生改变，不用保存.", "提示", JOptionPane.INFORMATION_MESSAGE);        return false;      }      if (!checkBeforeSave()) {        return false;      }      ZcEbReqChange afterSaveProj = null;      boolean success = true;      String errorInfo = "";      try {        requestMeta.setFuncId(saveButton.getFuncId());        afterSaveProj = listPanel.zcEbProjectChangeServiceDelegate.saveZcEbReqChange(proj, this.requestMeta);      } catch (Exception e) {        logger.error(e.getMessage(), e);        success = false;        errorInfo += e.getMessage();      }      if (success) {        JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);        this.refreshAll(afterSaveProj, false);        this.listPanel.refreshCurrentTabData();      } else {        JOptionPane.showMessageDialog(this, "保存失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);      }      return true;    }    private void doSend() {      if (!checkBeforeSave()) {        return;      }      if (isDataChanged()) {        JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);        return;      }      ZcEbReqChange proj = (ZcEbReqChange) this.listCursor.getCurrentObject();      ZcBaseBill afterBill = ZcWorkFlowAdapter.newCommitFN(proj, this, sendButton, requestMeta, null);      if (afterBill != null) {        ZcEbReqChange bill = listPanel.zcEbProjectChangeServiceDelegate.getZcEbReqChange(proj.getChangeCode(), requestMeta);        this.listCursor.setCurrentObject(bill);        this.refreshData();        this.listPanel.refreshCurrentTabData();      }    }    /*    * 收回    */    private void doCallback() {      ZcEbReqChange proj = (ZcEbReqChange) this.listCursor.getCurrentObject();      ZcBaseBill afterBill = ZcWorkFlowAdapter.callbackFN(proj, this, callbackButton, requestMeta, null);      if (afterBill != null) {        ZcEbReqChange bill = listPanel.zcEbProjectChangeServiceDelegate.getZcEbReqChange(proj.getChangeCode(), requestMeta);        this.listCursor.setCurrentObject(bill);        this.refreshData();        this.listPanel.refreshCurrentTabData();      }    }    /*    * 填写意见审核    */    private void doSuggestPass() {      if (isDataChanged()) {        JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);        return;      }      ZcEbReqChange proj = (ZcEbReqChange) this.listCursor.getCurrentObject();      ZcBaseBill afterBill = ZcWorkFlowAdapter.auditFN(proj, this, suggestPassButton, requestMeta, null);      if (afterBill != null) {        ZcEbReqChange bill = listPanel.zcEbProjectChangeServiceDelegate.getZcEbReqChange(proj.getChangeCode(), requestMeta);        this.listCursor.setCurrentObject(bill);        this.refreshData();        this.listPanel.refreshCurrentTabData();      }    }    /*    * 审核    */    private void doAudit() {      if (isDataChanged()) {        JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);        return;      }      ZcEbReqChange proj = (ZcEbReqChange) this.listCursor.getCurrentObject();      ZcBaseBill afterBill = ZcWorkFlowAdapter.auditFN(proj, this, suggestPassButton, requestMeta, null);      if (afterBill != null) {        ZcEbReqChange bill = listPanel.zcEbProjectChangeServiceDelegate.getZcEbReqChange(proj.getChangeCode(), requestMeta);        this.listCursor.setCurrentObject(bill);        this.refreshData();        this.listPanel.refreshCurrentTabData();      }    }    private boolean checkBeforeSave() {      ZcEbReqChange proj = (ZcEbReqChange) this.listCursor.getCurrentObject();      if (proj.getChangeReason() == null || "".equals(proj.getChangeReason())) {        JOptionPane.showMessageDialog(this, "变更原因不能为空 ！\n", "错误", JOptionPane.ERROR_MESSAGE);        return false;      }      if (proj.getChangeReasonFilename() == null || "".equals(proj.getChangeReasonFilename())) {        JOptionPane.showMessageDialog(this, "变更原因附件不能为空！\n", "错误", JOptionPane.ERROR_MESSAGE);        return false;      }      return true;    }    /*    * 销审    */    private void doUnaudit() {      ZcEbReqChange proj = (ZcEbReqChange) this.listCursor.getCurrentObject();      ZcBaseBill afterBill = ZcWorkFlowAdapter.unAuditFN(proj, this, unTreadButton, requestMeta, null);      if (afterBill != null) {        ZcEbReqChange bill = listPanel.zcEbProjectChangeServiceDelegate.getZcEbReqChange(proj.getChangeCode(), requestMeta);        this.listCursor.setCurrentObject(bill);        this.refreshData();        this.listPanel.refreshCurrentTabData();      }    }    /*    * 退回    */    private void doUntread() {      ZcEbReqChange proj = (ZcEbReqChange) this.listCursor.getCurrentObject();      ZcBaseBill afterBill = ZcWorkFlowAdapter.untreadFN(proj, this, unTreadButton, requestMeta, null);      if (afterBill != null) {        ZcEbReqChange bill = listPanel.zcEbProjectChangeServiceDelegate.getZcEbReqChange(proj.getChangeCode(), requestMeta);        this.listCursor.setCurrentObject(bill);        this.refreshData();        this.listPanel.refreshCurrentTabData();      }    }    /*    * 流程跟踪    */    private void doTrace() {      ZcBaseBill bean = (ZcBaseBill) this.listCursor.getCurrentObject();      if (bean == null) {        return;      }      ZcUtil.showTraceDialog(bean, listPanel.compoId);    }    private void doExit() {      self.closeDialog();    }    public void doEdit() {      this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;      updateFieldEditorsEditable();      setButtonStatus();    }    private void setButtonStatus() {      if (this.btnStatusList.size() == 0) {        ButtonStatus bs = new ButtonStatus();        bs.setButton(this.editButton);        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_NEW);        btnStatusList.add(bs);        bs = new ButtonStatus();        bs.setButton(this.saveButton);        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_NEW);        bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);        bs.addBillStatus(ZcSettingConstants.BILL_STATUS_DRAFT);        bs.addBillStatus(ZcSettingConstants.BILL_STATUS_NEW);        btnStatusList.add(bs);        bs = new ButtonStatus();        bs.setButton(this.deleteButton);        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_NEW);        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);        bs.addBillStatus("0");        btnStatusList.add(bs);        bs = new ButtonStatus();        bs.setButton(this.sendButton);        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);        //      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);        bs.addBillStatus("0");        btnStatusList.add(bs);        bs = new ButtonStatus();        bs.setButton(this.previousButton);        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);        bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);        btnStatusList.add(bs);        bs = new ButtonStatus();        bs.setButton(this.nextButton);        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);        bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);        btnStatusList.add(bs);        bs = new ButtonStatus();        bs.setButton(this.invalidButton);        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);        bs.addBillStatus(ZcSettingConstants.BILL_STATUS_AUDITED);        bs.addBillStatus(ZcSettingConstants.BILL_STATUS_DRAFT);        btnStatusList.add(bs);        bs = new ButtonStatus();        bs.setButton(this.isSendToNextButton);        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);        bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);        btnStatusList.add(bs);      }      ZcEbReqChange obj = (ZcEbReqChange) listCursor.getCurrentObject();      String billStatus = obj.getStatus();      ZcUtil.setButtonEnable(this.btnStatusList, billStatus, this.pageStatus, listPanel.compoId, obj.getProcessInstId());      //作废状态下，只有删除按钮，其他按钮应该隐藏      if ("invalid".equals(obj.getStatus())) {        toolBar.remove(editButton);        toolBar.remove(saveButton);        toolBar.remove(sendButton);        toolBar.remove(isSendToNextButton);        toolBar.remove(suggestPassButton);        toolBar.remove(auditPassButton);        toolBar.remove(callbackButton);        toolBar.remove(unAuditButton);        toolBar.remove(unTreadButton);        toolBar.remove(previousButton);        toolBar.remove(nextButton);      }    }    private class ProjectDetailHandler implements IForeignEntityHandler {      private final String columNames[];      public ProjectDetailHandler(String columNames[]) {        this.columNames = columNames;      }      //点击编辑框的时候自动执行该方法      public void excute(List selectedDatas) {        ZcEbReqChange change = (ZcEbReqChange) listCursor.getCurrentObject();        //TODO:        for (Object object : selectedDatas) {          ZcEbReqChange curr = (ZcEbReqChange) object;          change.setSn(curr.getSn());          change.setSnCode(curr.getSnCode());          change.setZcMakeName(curr.getZcMakeName());          change.setZcMakeCode(curr.getZcMakeCode());          change.setZcMoneyBiSum(curr.getZcMoneyBiSum());          change.setZcMakeLink(curr.getZcMakeLink());          change.setZcMakeTel(curr.getZcMakeTel());          change.setCoCode(curr.getCoCode());          change.setAgency(curr.getAgency());        }        listCursor.setCurrentObject(change);        setMyEditingObject(change);      }      @Override      public TableModel createTableModel(List showDatas) {        Object data[][] = new Object[showDatas.size()][columNames.length];        for (int i = 0; i < showDatas.size(); i++) {          //          ZcPProMake rowData = (ZcPProMake) showDatas.get(i);          ZcEbReqChange rowData = (ZcEbReqChange) showDatas.get(i);          int col = 0;          //弹出来的项目选择框信息填充          //TODO:          data[i][col++] = rowData.getZcMakeCode();          data[i][col++] = rowData.getSnCode();          data[i][col++] = rowData.getZcMakeName();          data[i][col++] = rowData.getZcMoneyBiSum();          data[i][col++] = rowData.getZcMakeLink();          data[i][col++] = rowData.getZcMakeTel();        }        MyTableModel model = new MyTableModel(data, columNames) {          private static final long serialVersionUID = 1821460782676810898L;          @Override          public boolean isCellEditable(int row, int colum) {            return false;          }          @Override          public Class getColumnClass(int column) {            if ((column >= 0) && (column < getColumnCount()) && this.getRowCount() > 0) {              for (int row = 0; row < this.getRowCount(); row++) {                if (getValueAt(row, column) != null) {                  return getValueAt(row, column).getClass();                }              }            }            return Object.class;          }        };        return model;      }      @Override      public boolean isMultipleSelect() {        return false;      }    }    @Override    protected void updateFieldEditorsEditable() {      super.updateFieldEditors();      if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW) || this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {        changeReasonMemo.setEditable(true);        reasonFile.getFileUploader().setEnabled(true);        reasonFile.getFileUploader().setUploadFileButton(true);        reasonFile.getFileUploader().setDelFileButton(false);      } else {        changeReasonMemo.setEditable(false);        reasonFile.getFileUploader().setEnabled(false);        reasonFile.getFileUploader().setDownloadFileButton(true);        reasonFile.getFileUploader().setUploadFileButton(false);        reasonFile.getFileUploader().setDelFileButton(false);        if (!ZcSettingConstants.BILL_STATUS_DRAFT.equals(this.tabStatus)) {          reasonFile.getFileUploader().setUploadFileButton(false);        }      }      reasonFile.getFileUploader().setButtonEnable();    }  }}