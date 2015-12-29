package com.ufgov.zc.client.zc.consult;

import java.awt.Color;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.DateField;
import com.ufgov.zc.client.component.GkCommentDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.CallbackButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SendButton;
import com.ufgov.zc.client.component.button.SuggestAuditPassButton;
import com.ufgov.zc.client.component.button.TraceButton;
import com.ufgov.zc.client.component.button.UnauditButton;
import com.ufgov.zc.client.component.button.UntreadButton;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextAreaFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcEbConsult;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;

public class ZcEbConsultEditPanel extends AbstractMainSubEditPanel {

  private Logger logger = Logger.getLogger(ZcEbConsultEditPanel.class);

  protected ListCursor<ZcEbConsult> listCursor;

  private ZcEbConsult oldBean;

  private String tabStatus;

  private String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  ZcEbConsultListPanel listPanel;

  ZcEbConsultDialog dialog;

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private String compoId = "ZC_EB_CONSULT";

  private final FuncButton saveButton = new SaveButton();

  private final FuncButton sendButton = new SendButton();

  private final FuncButton callbackButton = new CallbackButton();

  private final FuncButton suggestPassButton = new SuggestAuditPassButton();

  private final FuncButton unAuditButton = new UnauditButton();

  private final FuncButton unTreadButton = new UntreadButton();

  private final FuncButton traceButton = new TraceButton();

  private final FuncButton deleteButton = new DeleteButton();

  private final FuncButton editButton = new EditButton();

  private final FuncButton exitButton = new ExitButton();

  public IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,
    "zcEbBaseServiceDelegate");

  public ZcEbConsultEditPanel(ZcEbConsultDialog self, ListCursor listCursor, String tabStatus, ZcEbConsultListPanel listPanel) {

    super(ZcEbConsult.class, BillElementMeta.getBillElementMetaWithoutNd("ZC_EB_CONSULT"));

    this.listCursor = listCursor;

    this.listPanel = listPanel;

    this.tabStatus = tabStatus;
    dialog = self;

    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "咨询建议", TitledBorder.CENTER, TitledBorder.TOP,

    new Font("宋体", Font.BOLD, 15), Color.BLUE));

    this.colCount = 3;

    init();

    requestMeta.setCompoId(compoId);

    refreshData();
  }

  private void refreshData() {

    ZcEbConsult bean = this.listCursor.getCurrentObject();

    if (bean == null) {// 新增页面

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;

      bean = new ZcEbConsult();

      bean.setNd(requestMeta.getSvNd());

      bean.setStatus("0");

      bean.setExecutor(WorkEnv.getInstance().getCurrUserId());
      bean.setExecutorName(WorkEnv.getInstance().getCurrUserName());

      List lst = new ArrayList();

      lst.add(bean);

      this.listCursor.setDataList(lst, -1);

    } else {
      bean = listPanel.getZcEbConsultDelegate().findConsultById(bean.getId(), requestMeta);

      listCursor.setCurrentObject(bean);
      this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

    }

    this.setEditingObject(bean);

    listCursor.setCurrentObject(bean);
    updateFieldEditorsEditable();
    setButtonStatus(bean, requestMeta, listCursor);
    setButtonStatus();

    setOldObject();

  }

  private void setOldObject() {

    oldBean = (ZcEbConsult) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

  }

  public boolean isDataChanged() {
    if (saveButton.isVisible() && saveButton.isEnabled()) {
      return !DigestUtil.digest(oldBean).equals(DigestUtil.digest(listCursor.getCurrentObject()));
    } else {
      return false;
    }

  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {
    // TCJLODO Auto-generated method stub

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(compoId);

    toolBar.add(editButton);

    toolBar.add(saveButton);

    toolBar.add(sendButton);

    toolBar.add(deleteButton);

    toolBar.add(suggestPassButton);

    toolBar.add(callbackButton);

    toolBar.add(unAuditButton);

    toolBar.add(unTreadButton);

    toolBar.add(traceButton);

    toolBar.add(exitButton);

    editButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // TCJLODO Auto-generated method stub
        doEdit();
      }

    });

    saveButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // TCJLODO Auto-generated method stub
        doSave();
      }

    });

    sendButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // TCJLODO Auto-generated method stub
        doSend();
      }

    });

    deleteButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // TCJLODO Auto-generated method stub
        doDelete();
      }

    });

    suggestPassButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // TCJLODO Auto-generated method stub
        doAudit();
      }

    });

    callbackButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // TCJLODO Auto-generated method stub
        doCallBack();
      }

    });

    unAuditButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // TCJLODO Auto-generated method stub
        doUnAudit();
      }

    });

    unTreadButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // TCJLODO Auto-generated method stub
        doUnTread();
      }

    });

    traceButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // TCJLODO Auto-generated method stub
        doTrace();
      }

    });

    exitButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // TCJLODO Auto-generated method stub
        doExit();
      }

    });

  }

  private void doExit() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave()) {

          return;

        }

      }

    }

    this.dialog.dispose();
  }

  private void doTrace() {
    // TCJLODO Auto-generated method stub
    ZcEbConsult bean = (ZcEbConsult) listCursor.getCurrentObject();
    ZcUtil.showTraceDialog(bean, compoId);
  }

  private void doUnTread() {

    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;

    }
    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),

    ModalityType.APPLICATION_MODAL);

    if (commentDialog.cancel) {

      return;

    }

    ZcEbConsult bean = (ZcEbConsult) listCursor.getCurrentObject();
    requestMeta.setFuncId(unTreadButton.getFuncId());
    bean.setComment(commentDialog.getComment());
    bean.setAuditorId(WorkEnv.getInstance().getCurrUserId());
    try {
      bean = listPanel.getZcEbConsultDelegate().untreadFN(bean, requestMeta);

      JOptionPane.showMessageDialog(this, "退回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      refreshData();
      setButtonStatus(bean, requestMeta, listCursor);
      this.listPanel.refreshCurrentTabData();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);

      JOptionPane.showMessageDialog(this, "退回失败 ！" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
    }

  }

  private void doUnAudit() {

    ZcEbConsult bean = (ZcEbConsult) listCursor.getCurrentObject();
    requestMeta.setFuncId(unAuditButton.getFuncId());
    bean.setComment("");
    bean.setAuditorId(WorkEnv.getInstance().getCurrUserId());
    try {
      bean = listPanel.getZcEbConsultDelegate().unAuditFN(bean, requestMeta);

      JOptionPane.showMessageDialog(this, "销审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      refreshData();
      setButtonStatus(bean, requestMeta, listCursor);
      this.listPanel.refreshCurrentTabData();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);

      JOptionPane.showMessageDialog(this, "销审失败 ！" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
    }

  }

  private void doCallBack() {

    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;

    }
    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),

    ModalityType.APPLICATION_MODAL);

    if (commentDialog.cancel) {

      return;

    }

    ZcEbConsult bean = (ZcEbConsult) listCursor.getCurrentObject();
    requestMeta.setFuncId(callbackButton.getFuncId());
    bean.setComment(commentDialog.getComment());
    bean.setAuditorId(WorkEnv.getInstance().getCurrUserId());
    try {
      bean = listPanel.getZcEbConsultDelegate().callbackFN(bean, requestMeta);

      JOptionPane.showMessageDialog(this, "收回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      refreshData();
      setButtonStatus(bean, requestMeta, listCursor);
      this.listPanel.refreshCurrentTabData();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);

      JOptionPane.showMessageDialog(this, "收回失败 ！" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
    }

  }

  private void doAudit() {

    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;

    }


    ZcEbConsult bean = (ZcEbConsult) listCursor.getCurrentObject();

    if (WorkEnv.getInstance().containRole("CG_CGZX_FZR")) {
      if (bean.getReplier() == null || "".equals(bean.getReplier()) || bean.getReplierName() == null || "".equals(bean.getReplierName())) {
        JOptionPane.showMessageDialog(this, "答复人不能为空", "提示", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
    }

    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),

    ModalityType.APPLICATION_MODAL);

    if (commentDialog.cancel) {

      return;

    }

    requestMeta.setFuncId(this.suggestPassButton.getFuncId());
    bean.setComment(commentDialog.getComment());
    bean.setAuditorId(WorkEnv.getInstance().getCurrUserId());
    try {
      bean = listPanel.getZcEbConsultDelegate().auditFN(bean, requestMeta);

      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      refreshData();
      setButtonStatus(bean, requestMeta, listCursor);
      this.listPanel.refreshCurrentTabData();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);

      JOptionPane.showMessageDialog(this, "审核失败 ！" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
    }

  }

  private void doDelete() {

    ZcEbConsult bean = (ZcEbConsult) listCursor.getCurrentObject();
    if (bean.getId() == null || bean.getId().length() == 0) {

      JOptionPane.showMessageDialog(this, "未保存数据不能删除！", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    requestMeta.setFuncId(deleteButton.getFuncId());
    try {
      listPanel.getZcEbConsultDelegate().deleteConsultById(bean.getId(), requestMeta);

      JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listCursor.removeCurrentObject();
      refreshData();
      this.listPanel.refreshCurrentTabData();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);

      JOptionPane.showMessageDialog(this, "删除失败 ！" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
    }

  }

  private void doSend() {

    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;

    }

    ZcEbConsult bean = (ZcEbConsult) listCursor.getCurrentObject();
    requestMeta.setFuncId(sendButton.getFuncId());
    bean.setAuditorId(WorkEnv.getInstance().getCurrUserId());
    try {
      bean = listPanel.getZcEbConsultDelegate().newCommitFN(bean, requestMeta);

      JOptionPane.showMessageDialog(this, "送审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      refreshData();
      setButtonStatus(bean, requestMeta, listCursor);
      this.listPanel.refreshCurrentTabData();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);

      JOptionPane.showMessageDialog(this, "送审失败 ！" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
    }

  }

  private boolean doSave() {

    if (!checkBeforSave()) {
      return false;
    }

    ZcEbConsult bean = (ZcEbConsult) listCursor.getCurrentObject();
    requestMeta.setFuncId(saveButton.getFuncId());
    bean.setComment("");
    try {
      bean = listPanel.getZcEbConsultDelegate().updateConsult(bean, requestMeta);

      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      listCursor.setCurrentObject(bean);

      refreshData();
      setButtonStatus(bean, requestMeta, listCursor);
      this.listPanel.refreshCurrentTabData();
      return true;
    } catch (Exception e) {
      logger.error(e.getMessage(), e);

      JOptionPane.showMessageDialog(this, "保存失败 ！" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
    }
    return false;

  }

  private boolean checkBeforSave() {
    ZcEbConsult bean = (ZcEbConsult) listCursor.getCurrentObject();
    List notNullField = this.eleMeta.getNotNullBillElement();
    String error = ZcUtil.validateBillElementNull(bean, notNullField);

    if (error.length() > 0) {

      JOptionPane.showMessageDialog(this, error, "提示", JOptionPane.INFORMATION_MESSAGE);
      return false;

    }
    return true;
  }

  private void doEdit() {
    // TCJLODO Auto-generated method stub
    this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;
    updateFieldEditorsEditable();
    setButtonStatus();
  }

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {
    // TCJLODO Auto-generated method stub
    List<AbstractFieldEditor> fieldEditors = new ArrayList<AbstractFieldEditor>();

    TextFieldEditor title = new TextFieldEditor("标题", "title");

    AsValFieldEditor type = new AsValFieldEditor("类型", "type", "ZC_VS_CONSULT_TYPE");

    AsValFieldEditor status = new AsValFieldEditor("状态", "status", "ZC_VS_CONSULT_STATUS");

    TextFieldEditor executorName = new TextFieldEditor("提出人", "executorName");

    String[] columNames = { "姓名", "业务处" };
    YwHandler handler = new YwHandler(columNames);
    ElementConditionDto elementCondtiontDto = new ElementConditionDto();

    ForeignEntityFieldEditor replierName = new ForeignEntityFieldEditor("ZcEbConsult.findYWUser", elementCondtiontDto, 20, handler, columNames,
      "回复人", "replierName");

    DateFieldEditor executeDate = new DateFieldEditor("提出时间", "executeDate", DateField.TimeTypeH24);

    DateFieldEditor replyDate = new DateFieldEditor("答复时间", "replyDate", DateField.TimeTypeH24);

    TextAreaFieldEditor problem = new TextAreaFieldEditor("内容", "problem", 500, 8, 5);

    TextAreaFieldEditor reply = new TextAreaFieldEditor("回复", "reply", 500, 8, 5);

    fieldEditors.add(title);
    fieldEditors.add(type);
    fieldEditors.add(status);

    fieldEditors.add(executorName);
    fieldEditors.add(executeDate);

    fieldEditors.add(problem);
    fieldEditors.add(replierName);
    fieldEditors.add(replyDate);

    fieldEditors.add(reply);
    return fieldEditors;
  }

  @Override
  protected void updateFieldEditorsEditable() {
    // TCJLODO Auto-generated method stub
    super.updateFieldEditors();
    if (ZcSettingConstants.PAGE_STATUS_NEW.equals(this.pageStatus)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {
        if ("title".equals(fd.getFieldName()) || "type".equals(fd.getFieldName()) || "problem".equals(fd.getFieldName())) {
          fd.setEnabled(true);
        } else {
          fd.setEnabled(false);
        }

      }

    } else if (ZcSettingConstants.PAGE_STATUS_EDIT.equals(this.pageStatus)) {
      ZcEbConsult bean = (ZcEbConsult) this.listCursor.getCurrentObject();
      if (bean.getProcessInstId() != null && bean.getProcessInstId().compareTo(0l) > 0) {
        updateWFEditorEditable(bean, requestMeta);
      } else {

        for (AbstractFieldEditor fd : this.fieldEditors) {
          if ("title".equals(fd.getFieldName()) || "type".equals(fd.getFieldName()) || "problem".equals(fd.getFieldName())) {
            fd.setEnabled(true);
          } else {
            fd.setEnabled(false);
          }
        }
      }

    } else if (ZcSettingConstants.PAGE_STATUS_BROWSE.equals(this.pageStatus)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {
        fd.setEnabled(false);
      }
    }
  }

  @Override
  public JComponent createSubBillPanel() {
    // TCJLODO Auto-generated method stub
    return null;
  }

  private void setButtonStatus() {

    if (this.btnStatusList.size() == 0) {

      ButtonStatus bs = new ButtonStatus();

      bs = new ButtonStatus();

      bs.setButton(this.editButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      bs.addBillStatus("0");

      bs.addBillStatus("3");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.saveButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_NEW);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.deleteButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.sendButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus("0");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.suggestPassButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.unTreadButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

    }

    ZcEbConsult bean = (ZcEbConsult) this.listCursor.getCurrentObject();

    String billStatus = bean.getStatus();

    ZcUtil.setButtonEnable(this.btnStatusList, billStatus, this.pageStatus, compoId, bean.getProcessInstId());

  }

  class YwHandler implements IForeignEntityHandler {
    String columNames[];

    YwHandler(String columNames[]) {
      this.columNames = columNames;
    }

    public void excute(List selectedDatas) {
      // TCJLODO Auto-generated method stub
      if (selectedDatas.size() > 0) {
        Map emp = (Map) selectedDatas.get(0);
        ZcEbConsult bean = listCursor.getCurrentObject();
        bean.setReplier((String) emp.get("USER_ID"));
        bean.setReplierName((String) emp.get("USER_NAME"));
        setEditingObject(bean);
      }
    }

    public boolean isMultipleSelect() {
      // TCJLODO Auto-generated method stub
      return false;
    }

    public TableModel createTableModel(List showDatas) {
      // TCJLODO Auto-generated method stub

      Object data[][] = new Object[showDatas.size()][columNames.length];
      for (int i = 0; i < showDatas.size(); i++) {
        Map bean = (Map) showDatas.get(i);
        int col = 0;
        data[i][col++] = (String) bean.get("USER_NAME");
        data[i][col++] = (String) bean.get("ORG_NAME");
      }
      MyTableModel model = new MyTableModel(data, columNames) {
        public boolean isCellEditable(int row, int colum) {
          return false;
        }
      };
      return model;
    }

  }

}
