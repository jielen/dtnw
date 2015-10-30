package com.ufgov.zc.client.zc.zcebentrust;

import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_FUND_CODE;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ORIGIN_CODE;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_CATALOGUE_CODE;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_PITEM_ARR_DATE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.ZcEbEntrustToTableModelConverter;
import com.ufgov.zc.client.component.AsValComboBox;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JSaveableSplitPane;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.AcceptedButton;
import com.ufgov.zc.client.component.button.AddButton;
import com.ufgov.zc.client.component.button.BackButton;
import com.ufgov.zc.client.component.button.CallbackButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.OpenNotepadButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SendBillButton;
import com.ufgov.zc.client.component.button.SendButton;
import com.ufgov.zc.client.component.button.SuggestAuditPassButton;
import com.ufgov.zc.client.component.button.TraceButton;
import com.ufgov.zc.client.component.button.UnauditButton;
import com.ufgov.zc.client.component.button.UntreadButton;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.celleditor.DateCellEditor;
import com.ufgov.zc.client.component.table.celleditor.MoneyCellEditor;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.celleditor.zc.ZcBCatalogueCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.DateCellRenderer;
import com.ufgov.zc.client.component.table.cellrenderer.NumberCellRenderer;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecelleditor.FileCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsValCellRenderer;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.IntFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.OrgFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFilePathFieldEditor;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.notepad.ZcNotepadDialog;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.WFConstants;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityTreeHandler;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcEbAuditSheet;
import com.ufgov.zc.common.zc.model.ZcEbEntrust;
import com.ufgov.zc.common.zc.model.ZcEbEntrustDetail;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;
import com.ufgov.zc.common.zc.publish.IZcEbAuditSheetServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbEntrustServiceDelegate;

public class ZcEbEntrustEditDialog extends GkBaseDialog {

  private static final long serialVersionUID = 1L;

  private ZcEbEntrustEditDialog self = this;

  private ZcEbEntrustEditPanel editPanel;

  public ZcEbEntrustEditDialog(ZcEbEntrustListPanel listPanel, List beanList, int editingRow, String tabStatus) {
    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);

    editPanel = new ZcEbEntrustEditPanel(new ListCursor(beanList, editingRow), tabStatus, listPanel);

    setLayout(new BorderLayout());

    add(editPanel);

    this.setTitle("采购任务单");

    this.setSize(UIConstants.DIALOG_2_LEVEL_WIDTH, UIConstants.DIALOG_2_LEVEL_HEIGHT);

    this.moveToScreenCenter();

    this.setVisible(true);

  }

  public ZcEbEntrustEditDialog(Window parentWindow, List viewList, int row, String tabStatus) {

  }

  public ZcEbEntrustEditDialog() {

  }

  private boolean yesConfirmed = true;

  protected boolean dialogIsClosing() {

    if (editPanel.isDataChanged() && yesConfirmed) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        return editPanel.doSave();

      } else {

        yesConfirmed = false;

      }

    }

    return true;

  }

  public class ZcEbEntrustEditPanel extends AbstractMainSubEditPanel {

    private IZcEbEntrustServiceDelegate zcEbEntrustServiceDelegate = (IZcEbEntrustServiceDelegate) ServiceFactory.create(

    IZcEbEntrustServiceDelegate.class, "zcEbEntrustServiceDelegate");

    private IZcEbAuditSheetServiceDelegate zcEbAuditSheetServiceDelegate = (IZcEbAuditSheetServiceDelegate) ServiceFactory.create(

    IZcEbAuditSheetServiceDelegate.class, "zcEbAuditSheetServiceDelegate");

    public IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,

    "zcEbBaseServiceDelegate");

    private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

    private String compoId = "ZC_EB_ENTRUST";

    private FuncButton traceButton = new TraceButton();

    private FuncButton previousButton = new PreviousButton();

    private FuncButton nextButton = new NextButton();

    private FuncButton exitButton = new ExitButton();

    private FuncButton sendButton = new SendButton();

    // 受理

    private FuncButton acceptedButton = new AcceptedButton();

    //    private FuncButton sendBill = new CommonButton("fsendBill","发送","sendBill.png");

    // 发送

    private FuncButton sendBillButton = new SendBillButton();

    private FuncButton saveButton = new SaveButton();

    // 不受理

    private FuncButton backButton = new BackButton();

    private AddButton addButton = new AddButton();

    private EditButton editButton = new EditButton();

    private DeleteButton deleteButton = new DeleteButton();

    // 工作流收回

    private FuncButton callbackButton = new CallbackButton();

    private FuncButton auditPassButton = new SuggestAuditPassButton(); //审核通过

    private FuncButton unAuditButton = new UnauditButton(); // 销审

    private FuncButton unTreadButton = new UntreadButton(); // 退回

    public FuncButton openNotepadButton = new OpenNotepadButton();

    private ListCursor listCursor;

    private ZcEbEntrust oldEntrust;

    private String tabStatus;

    private ZcEbEntrustListPanel listPanel;

    private String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

    private String checkSn = null;

    public JSaveableSplitPane splitPane;

    //资金构成页签

    JTabbedPane biTabPane = null;

    //资金构成集合

    public List biList = new ArrayList();

    //任务明细

    JTabbedPane itemTabPane = null;

    private JTablePanel tablePanel = new JTablePanel();

    private JTablePanel biTablePanel = new JTablePanel();

    private JFuncToolBar subPackTableToolbar;

    private JFuncToolBar subTableToolbar;

    private AsValFieldEditor isDesSup;

    private AsValFieldEditor isPub;

    private AsValFieldEditor isCar;

    private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

    private List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    public ZcEbEntrustEditPanel(ListCursor listCursor, String tabStatus, ZcEbEntrustListPanel listPanel) {

      super(ZcEbEntrust.class, BillElementMeta.getBillElementMetaWithoutNd("ZC_EB_ENTRUST"));

      this.listCursor = listCursor;

      this.tabStatus = tabStatus;

      this.listPanel = listPanel;

      this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "采购任务单", TitledBorder.CENTER, TitledBorder.TOP,

      new Font("宋体", Font.BOLD, 15), Color.BLUE));

      this.colCount = 3;

      requestMeta.setCompoId(compoId);

      getFieldEditors();

      init();

      refreshData();

    }

    public ZcEbEntrustEditPanel() {

    }

    private void setOldObject() {

      oldEntrust = (ZcEbEntrust) ObjectUtil.deepCopy(listCursor.getCurrentObject());

    }

    public List<AbstractFieldEditor> getFieldEditors() {

      ZcEbEntrust zcEbEntrust = (ZcEbEntrust) listCursor.getCurrentObject();

      TextFieldEditor sn = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_SN), "sn");

      TextFieldEditor snCode = new TextFieldEditor("任务单编号", "snCode");

      TextFieldEditor makeCode = new TextFieldEditor("采购计划编号", "zcMakeCode");

      sn.setVisible(false);

      editorList.add(sn);

      editorList.add(snCode);

      editorList.add(makeCode);

      TextFieldEditor editor0 = new TextFilePathFieldEditor("采购项目", "zcMakeName");

      editorList.add(editor0);

      editor0.setEnabled(false);

      MoneyFieldEditor editor1 = new MoneyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MONEY_BI_SUM), "zcMoneyBiSum");

      editorList.add(editor1);

      editor1.setEnabled(false);

      AsValFieldEditor editor3 = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_PIFU_CGFS), "zcPifuCgfs",

      "ZC_VS_PITEM_OPIWAY") {

        private static final long serialVersionUID = 3035799363967147800L;

        protected void afterChange(AsValComboBox field) {
          changeWay(field);
        }
      };

      editorList.add(editor3);

      editor3.setEnabled(false);

      IForeignEntityTreeHandler companyHandler = new IForeignEntityTreeHandler() {

        @Override
        public void excute(List selectedDatas) {
        }

        @Override
        public boolean isMultipleSelect() {

          return false;

        }

        @Override
        public boolean isSelectLeaf() {

          return false;

        }

      };

      CompanyFieldEditor editor2 = new CompanyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_NAME), "coCode",

      companyHandler);

      editorList.add(editor2);

      editor2.setEnabled(false);

      // 采购单位主管业务处室

      OrgFieldEditor zcZgCsCode = new OrgFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_ZG_CS_CODE), "orgCode", true);

      zcZgCsCode.setVisible(false);

      editorList.add(zcZgCsCode);

      // 年度

      IntFieldEditor zcCoCodeNd = new IntFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_CODE_ND), "nd");

      editorList.add(zcCoCodeNd);

      zcCoCodeNd.setEnabled(false);

      TextFieldEditor editor4 = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_LINKMAN), "zcMakeLinkman");

      editorList.add(editor4);

      editor4.setEnabled(false);

      TextFieldEditor editor5 = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_TEL), "zcMakeTel");

      editorList.add(editor5);

      editor5.setEnabled(false);

      TextFieldEditor editor10 = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_REMARK), "remark");

      editorList.add(editor10);

      DateFieldEditor editor8 = new DateFieldEditor("下达时间", "zcWeitoDate");

      editorList.add(editor8);

      editor8.setEnabled(false);

      DateFieldEditor editor9 = new DateFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ENTRUST_DATE), "zcInputDate");

      editor9.setVisible(false);

      editorList.add(editor9);

      DateFieldEditor executeDate = new DateFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ENTRUST_DATE), "executeDate");
      executeDate.setEnabled(false);
      editorList.add(executeDate);

      TextFieldEditor editor12 = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_UNTREAD_REASON), "untreadReason");

      editorList.add(editor12);

      if (zcEbEntrust == null || zcEbEntrust.getStatus() == null || zcEbEntrust.getStatus() == zcEbEntrust.STATUS_DRAFT) {

        editor12.setEnabled(false);

      }

      isDesSup = new AsValFieldEditor("是否指定供应商", "isDesSup", "ZC_VS_YN");
      editorList.add(isDesSup);

      isPub = new AsValFieldEditor("是否公示", "isPub", "ZC_VS_YN");
      editorList.add(isPub);

      isCar = new AsValFieldEditor("是否车辆", "isCar", "ZC_VS_YN");
      editorList.add(isCar);

      AsValFieldEditor editor11 = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_STATUS), "status",

      "ZC_VS_ENTRUST_STATUS");

      editorList.add(editor11);

      editor11.setEnabled(false);

      return editorList;

    }

    @Override
    public List<AbstractFieldEditor> createFieldEditors() {

      return editorList;

    }

    @Override
    public void initToolBar(JFuncToolBar toolBar) {

      toolBar.setModuleCode("ZC");

      toolBar.setCompoId(compoId);

      toolBar.add(editButton);

      toolBar.add(deleteButton);

      toolBar.add(saveButton);

      // 受理

      toolBar.add(acceptedButton);

      // 不予受理

      toolBar.add(backButton);

      toolBar.add(unAuditButton);

      toolBar.add(sendBillButton);

      toolBar.add(openNotepadButton);

      toolBar.add(previousButton);

      toolBar.add(nextButton);

      toolBar.add(exitButton);

      addButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          doAdd();

        }

      });

      editButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          doEdit();

        }

      });

      deleteButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          doDelete();

        }

      });

      acceptedButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          doAccept();

        }

      });

      sendBillButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          doSendBill();

        }

      });

      backButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          doBack();

        }

      });

      saveButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          // 保存

          doSave();

        }

      });

      sendButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          doSend();

        }

      });

      auditPassButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          doAudit();

        }

      });

      callbackButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          // 收回

          doCallback();

        }

      });

      unAuditButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          doUnaudit();

        }

      });

      unTreadButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          doUntread();

        }

      });

      traceButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          doTrace();

        }

      });

      previousButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          doPrevious();

        }

      });

      nextButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          doNext();

        }

      });

      exitButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          doExit();

        }

      });

      openNotepadButton.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          doOpenNotepad();

        }

      });

    }

    private void doOpenNotepad() {

      ZcEbEntrust sheet = (ZcEbEntrust) this.listCursor.getCurrentObject();

      new ZcNotepadDialog(sheet.getSn(), self);

    }

    protected void doDelete() {

      int num = JOptionPane.showConfirmDialog(this, "是否删除当前单据", "删除确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        boolean success = true;

        ZcEbEntrust entrust = (ZcEbEntrust) this.listCursor.getCurrentObject();

        String errorInfo = "";

        try {
          requestMeta.setFuncId(deleteButton.getFuncId());
          if (entrust == null) {
            return;
          }
          /**
           * 校验已经生成批办单的任务单，不允许删除
           */
          if (entrust.getSn() != null && !"".equals(entrust.getSn())) {

            Map map = new HashMap();

            map.put("SN", entrust.getSn());

            List<ZcEbAuditSheet> list = zcEbBaseServiceDelegate.queryDataForList("ZcEbAuditSheet.read", map, requestMeta);

            if (list != null && list.size() >= 1) {

              for (int i = 0; i < list.size(); i++) {

                if (!ZcEbAuditSheet.STATUS_DRAFT.equals(list.get(i).getStatus())) {

                  JOptionPane.showMessageDialog(this, "<html><b><font size='3' color='red'>该任务单已经生成批办单,并且已经送审给业务处，不允许删除！" + "</font></b></html>\n",

                  "提示", JOptionPane.INFORMATION_MESSAGE);

                  return;

                } else {

                  //处于草稿状态的批办单，可以删除。

                  if (ZcEbAuditSheet.STATUS_DRAFT.equals(list.get(i).getStatus())) {

                    Map m = new HashMap();

                    m.put("sheetId", list.get(i).getSheetId());

                    zcEbAuditSheetServiceDelegate.deleteFN(m, requestMeta);

                  }

                }

              }

            }
          }

          this.zcEbEntrustServiceDelegate.deleteByPrimaryKey(entrust, this.requestMeta);
        } catch (Exception e) {

          logger.error(e.getMessage(), e);

          success = false;

          errorInfo += e.getMessage();

        }

        if (success) {

          this.listCursor.removeCurrentObject();

          JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

          this.refreshData();

          this.listPanel.refreshCurrentTabData();

        } else {

          JOptionPane.showMessageDialog(this, "删除失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

        }

      }

    }

    protected void doEdit() {

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;

      setButtonStatus();

      updateFieldEditorsEditable();

    }

    protected void doAdd() {

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;

      ZcEbEntrust trust = new ZcEbEntrust();

      setDefualtValue(trust, ZcSettingConstants.PAGE_STATUS_NEW);

      listCursor.setCurrentObject(trust);

      //新插入一行数据

      ZcEbEntrustDetail detail = new ZcEbEntrustDetail();

      detail.setSnd(Guid.genID());

      trust.getDetailList().add(detail);

      setEditingObject(trust);

      refreshData();

      updateFieldEditorsEditable();

      setDefualtValue(trust, this.pageStatus);

      setButtonStatus();

    }

    private void setButtonStatus() {

      if (this.btnStatusList.size() == 0) {

        ButtonStatus bs = new ButtonStatus();

        bs.setButton(this.addButton);

        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

        bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

        btnStatusList.add(bs);

        bs = new ButtonStatus();

        bs.setButton(this.editButton);

        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
        //        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);
        bs.addBillStatus(ZcEbEntrust.STATUS_DRAFT);

        bs.addBillStatus(ZcEbEntrust.STATUS_WAIT_ACCEPT);

        btnStatusList.add(bs);

        bs = new ButtonStatus();

        bs.setButton(this.saveButton);

        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_NEW);

        bs.addBillStatus(ZcEbEntrust.STATUS_DRAFT);

        bs.addBillStatus(ZcEbEntrust.STATUS_WAIT_ACCEPT);

        btnStatusList.add(bs);

        bs = new ButtonStatus();

        bs.setButton(this.deleteButton);

        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

        btnStatusList.add(bs);

        bs = new ButtonStatus();

        bs.setButton(this.sendBillButton);

        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

        bs.addBillStatus(ZcEbEntrust.STATUS_DRAFT);

        bs.addBillStatus(ZcEbEntrust.STATUS_WAIT_SEND);

        bs.addBillStatus(ZcEbEntrust.STATUS_UNACCEPT);

        btnStatusList.add(bs);

        bs = new ButtonStatus();

        bs.setButton(this.acceptedButton);

        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

        bs.addBillStatus(ZcEbEntrust.STATUS_DRAFT);

        bs.addBillStatus(ZcEbEntrust.STATUS_UNACCEPT);

        bs.addBillStatus(ZcEbEntrust.STATUS_WAIT_ACCEPT);

        btnStatusList.add(bs);

        bs = new ButtonStatus();

        bs.setButton(this.unAuditButton);

        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

        bs.addBillStatus(ZcEbEntrust.STATUS_ACCEPTED);

        btnStatusList.add(bs);

        bs = new ButtonStatus();

        bs.setButton(this.backButton);

        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

        bs.addBillStatus(ZcEbEntrust.STATUS_DRAFT);

        bs.addBillStatus(ZcEbEntrust.STATUS_WAIT_ACCEPT);

        bs.addBillStatus(ZcEbEntrust.STATUS_ACCEPTED);

        btnStatusList.add(bs);

        bs = new ButtonStatus();

        bs.setButton(this.exitButton);

        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_ALL);

        bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

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

      ZcEbEntrust obj = (ZcEbEntrust) (this.listCursor.getCurrentObject());

      String billStatus = obj.getStatus();

      ZcUtil.setButtonEnable(this.btnStatusList, billStatus, this.pageStatus, this.compoId, obj.getProcessInstId());

      setSubTableButton();

    }

    private void setSubTableButton() {
      ZcEbEntrust o = (ZcEbEntrust) this.listCursor.getCurrentObject();

      if ((this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT) && !ZcEbEntrust.STATUS_WAIT_ACCEPT.equals(o.getStatus()))
        || this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {
        if (this.subPackTableToolbar != null) {
          this.subPackTableToolbar.setEnabled(true);
        }
        if (this.subTableToolbar != null) {
          this.subTableToolbar.setEnabled(true);
        }

      } else {

        if (this.subPackTableToolbar != null) {
          this.subPackTableToolbar.setEnabled(false);

        }
        if (this.subTableToolbar != null) {
          this.subTableToolbar.setEnabled(false);

        }

      }

    }

    private void doPrevious() {

      listCursor.previous();

      refreshData();

    }

    private void doNext() {

      listCursor.next();

      refreshData();

    }

    public void doExit() {

      self.closeDialog();

    }

    private boolean checkBeforeSave() {

      List notNullBillElementList = BillElementMeta.getBillElementMetaWithoutNd("ZC_EB_ENTRUST").getNotNullBillElement();

      ZcEbEntrust zcEbEntrust = (ZcEbEntrust) this.listCursor.getCurrentObject();

      StringBuilder errorInfo = new StringBuilder();

      String validateInfo = ZcUtil.validateBillElementNull(zcEbEntrust, notNullBillElementList);

      if (validateInfo.length() != 0) {

        errorInfo.append("采购任务单").append("：\n").append(validateInfo.toString()).append("\n");

      }

      //校验商品的明细
      List detailList = (List) zcEbEntrust.getDetailList();

      if (detailList == null || detailList.size() == 0) {

        errorInfo.append("[任务明细]不能为空");

      } else {

        int detailsSize = detailList.size();

        for (int i = 0; i < detailsSize; i++) {

          int row = i + 1;

          ZcEbEntrustDetail zcEbEntrustDetail = (ZcEbEntrustDetail) detailList.get(i);

          if (zcEbEntrustDetail.getZcPitemName() == null || zcEbEntrustDetail.getZcCatalogueCode() == null

          || zcEbEntrustDetail.getZcItemSum() == null || zcEbEntrustDetail.getZcItemSum().doubleValue() == 0)

            errorInfo.append("任务明细第[" + row + "]行\n");

          if (zcEbEntrustDetail.getZcPitemName() == null) {

            errorInfo.append("[任务明细名称]不允许为空\n");

          }

          if (zcEbEntrustDetail.getZcCatalogueCode() == null) {

            errorInfo.append("[品目代码]不允许为空\n");

          }

          if (zcEbEntrustDetail.getZcItemSum() != null && zcEbEntrustDetail.getZcItemSum().doubleValue() > 0) {

          } else {

            errorInfo.append("[单项总计]必须大于0\n");

          }

          if (zcEbEntrustDetail.getZcPitemName() == null || zcEbEntrustDetail.getZcCatalogueCode() == null

          || zcEbEntrustDetail.getZcItemSum() == null || zcEbEntrustDetail.getZcItemSum().doubleValue() == 0) {

            errorInfo.append("\n");

          }

        }

      }
      //校验资金构成、商品明细的总金额是否相等

      List<ZcPProMitemBi> biList = (List) zcEbEntrust.getBiList();
      BigDecimal biSum = new BigDecimal("0");
      if (biList == null || biList.size() == 0) {

        errorInfo.append("[资金构成]不能为空\n");

      } else {
        for (ZcPProMitemBi bi : biList) {
          biSum = biSum.add(bi.getZcBiJhuaSum());
        }

      }

      if (zcEbEntrust.getZcMakeCode() != null && !zcEbEntrustServiceDelegate.checkUniqueZcMakeCode(zcEbEntrust, this.requestMeta)) {
        errorInfo.append("通知书编号已存在\n");
      }

      if (zcEbEntrust.getZcMoneyBiSum() != null && biSum.compareTo(zcEbEntrust.getZcMoneyBiSum()) != 0) {

        errorInfo.append("资金构成项目金额总和必须和任务单总金额相等\n");

      }
      if (errorInfo.length() != 0) {

        JOptionPane.showMessageDialog(this, errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);

        return true;

      }

      return false;

    }

    public boolean doSave() {

      if (checkBeforeSave()) {

        return false;

      }

      ZcEbEntrust afterSaveBill = (ZcEbEntrust) this.listCursor.getCurrentObject();

      if (!isDataChanged()) {

        JOptionPane.showMessageDialog(this, "数据没有发生改变，不用保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

        this.listPanel.refreshCurrentTabData();
        this.refreshAll(afterSaveBill, false);
        updateFieldEditorsEditable();

        return false;

      }

      String strSum = String.valueOf(afterSaveBill.getZcMoneyBiSum()).toString();

      if (strSum.length() > 16) {

        JOptionPane.showMessageDialog(this, "总价过大，无法保存！", "提示", JOptionPane.WARNING_MESSAGE);

        return true;

      }

      int countData = listPanel.zcEbEntrustServiceDelegate.getCountData(afterSaveBill.getSn(), this.requestMeta);

      if (!afterSaveBill.getSn().equals(this.checkSn) && countData > 0) {

        String info = "<html><b>[" + LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_SN) + "]</b> 为 <b>[" + afterSaveBill.getSn()

        + "]</b> 的采购任务单已经存在,请修改当前单据的 <b>[" + LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_SN) + "]</b>!</html>";

        JOptionPane.showMessageDialog(this, info, "提示", JOptionPane.WARNING_MESSAGE);

        return true;

      }

      boolean success = true;

      String errorInfo = "";

      try {

        requestMeta.setFuncId(saveButton.getFuncId());

        afterSaveBill = listPanel.zcEbEntrustServiceDelegate.saveFN(afterSaveBill, this.requestMeta);

      } catch (Exception e) {

        logger.error(e.getMessage(), e);

        success = false;

        errorInfo += e.getMessage();

      }

      if (success) {

        this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

        this.listPanel.refreshCurrentTabData();
        this.refreshAll(afterSaveBill, false);
        updateFieldEditorsEditable();

        JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      } else {

        JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

      return success;

    }

    private void doSendBill() {

      ZcEbEntrust afterSaveBill = null;

      String errorInfo = "";

      ZcEbEntrust entrust = (ZcEbEntrust) this.listCursor.getCurrentObject();

      if (entrust == null) {

        return;

      }

      entrust.setStatus(ZcEbEntrust.STATUS_WAIT_ACCEPT);

      entrust.setZcWeitoDate(this.requestMeta.getSysDate());

      boolean success = true;

      try {

        requestMeta.setFuncId(this.sendBillButton.getFuncId());

        afterSaveBill = this.zcEbEntrustServiceDelegate.saveFN(entrust, requestMeta);

      } catch (Exception e) {

        success = false;

        logger.error(e.getMessage(), e);

        errorInfo += e.getMessage();

      }

      if (success) {

        this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

        this.refreshAll(afterSaveBill, true);

        this.acceptedButton.setVisible(false);

        this.sendButton.setVisible(true);

        JOptionPane.showMessageDialog(this, "发送成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.listPanel.refreshCurrentTabData();

      } else {

        JOptionPane.showMessageDialog(this, "发送失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }

    private void doAccept() {

      String errorInfo = "";

      ZcEbEntrust entrust = (ZcEbEntrust) this.listCursor.getCurrentObject();

      if (entrust == null) {

        return;

      }

      if (entrust.getSn() == null || entrust.getSn().trim().length() == 0) {

        JOptionPane.showMessageDialog(this, "任务单编号不能为空!", "提示", JOptionPane.WARNING_MESSAGE);

        return;

      }

      ZcEbEntrust afterSaveBill = null;

      int countData = listPanel.zcEbEntrustServiceDelegate.getCountData(entrust.getSn(), this.requestMeta);

      if (!entrust.getSn().equals(this.checkSn) && countData > 0) {

        String info = "<html><b>[" + LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_SN) + "]</b> 为 <b>[" + entrust.getSn()

        + "]</b> 的采购任务单已经存在,请修改当前单据的 <b>[" + LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_SN) + "]</b>!</html>";

        JOptionPane.showMessageDialog(this, info, "提示", JOptionPane.WARNING_MESSAGE);

        return;

      }

      //entrust.setStatus("2");

      entrust.setStatus(ZcEbEntrust.STATUS_ACCEPTED);

      entrust.setExecuteDate(this.requestMeta.getSysDate());

      entrust.setLeaderId(this.requestMeta.getSvUserID());

      boolean success = true;

      try {

        requestMeta.setFuncId(acceptedButton.getFuncId());

        afterSaveBill = this.zcEbEntrustServiceDelegate.saveFN(entrust, requestMeta);

      } catch (Exception e) {

        setOldObject();

        success = false;

        logger.error(e.getMessage(), e);

        errorInfo += e.getMessage();

      }

      if (success) {

        this.refreshAll(afterSaveBill, true);

        //this.acceptedButton.setVisible(false);

        //this.sendButton.setVisible(true);

        JOptionPane.showMessageDialog(this, "受理成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.listPanel.refreshCurrentTabData();

      } else {

        JOptionPane.showMessageDialog(this, "受理失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }

    /*

     * 不受理

     */

    private void doBack() {

      ZcEbEntrust entrust = (ZcEbEntrust) this.listCursor.getCurrentObject();

      if (entrust == null) {

        return;

      }

      /**

       * 校验已经生成批办单的任务单，不允许不受理

       */

      if (entrust.getSn() != null && !"".equals(entrust.getSn())) {

        Map map = new HashMap();

        map.put("SN", entrust.getSn());

        List<ZcEbAuditSheet> list = zcEbBaseServiceDelegate.queryDataForList("ZcEbAuditSheet.read", map, requestMeta);

        if (list != null && list.size() >= 1) {

          for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getZcFzrUserId() != null) {

              JOptionPane.showMessageDialog(this, "<html><b><font size='3' color='red'>该任务单已经生成批办单,并且已经送审给经办人，不允许变更成不受理状态！" + "</font></b></html>\n",

              "提示", JOptionPane.INFORMATION_MESSAGE);

              return;

            } else {

              //处于草稿状态的批办单，可以删除。

              if (ZcEbAuditSheet.STATUS_DRAFT.equals(list.get(i).getStatus())) {

                Map m = new HashMap();

                m.put("sheetId", list.get(i).getSheetId());

                zcEbAuditSheetServiceDelegate.deleteFN(m, requestMeta);

              }

            }

          }

        }

      }

      ZcEbEntrust afterSaveBill = null;

      entrust.setStatus(ZcEbEntrust.STATUS_UNACCEPT);

      entrust.setExecuteDate(this.requestMeta.getSysDate());

      entrust.setLeaderId(this.requestMeta.getSvUserID());

      boolean success = true;

      try {

        requestMeta.setFuncId(backButton.getFuncId());

        entrust.setAuditorId(WorkEnv.getInstance().getCurrUserId());

        //this.zcEbEntrustServiceDelegate.updateZcEbEntrustStatusFN(entrust,requestMeta);

        afterSaveBill = this.zcEbEntrustServiceDelegate.saveFN(entrust, requestMeta);

      } catch (Exception ex) {

        success = false;

        logger.error(ex.getMessage(), ex);

        success = false;

        UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

      }

      if (success) {

        this.refreshAll(afterSaveBill, true);

        JOptionPane.showMessageDialog(this, "处理成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        //listCursor.removeCurrentObject();

        setOldObject();

        //this.acceptedButton.setVisible(false);

        this.backButton.setVisible(false);

        //this.saveButton.setVisible(false);

        listPanel.refreshCurrentTabData();

      }

    }

    private void doSend() {

      ZcEbEntrust entrust = (ZcEbEntrust) this.listCursor.getCurrentObject();

      if (entrust == null) {

        return;

      }

      ZcEbEntrust afterSaveBill = null;

      String errorInfo = "";

      boolean success = true;

      try {

        requestMeta.setFuncId(sendButton.getFuncId());

        afterSaveBill = this.zcEbEntrustServiceDelegate.newCommitFN(entrust, requestMeta);

      } catch (Exception e) {

        success = false;

        logger.error(e.getMessage(), e);

        errorInfo += e.getMessage();

      }

      if (success) {

        tabStatus = WFConstants.AUDIT_TAB_STATUS_TODO;

        this.refreshAll(afterSaveBill, true);

        JOptionPane.showMessageDialog(this, "送审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.listPanel.refreshCurrentTabData();

      } else {

        JOptionPane.showMessageDialog(this, "送审失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }

    /*

     * 收回

     */

    private void doCallback() {

      boolean success = true;

      ZcEbEntrust afterSaveBill = null;

      String errorInfo = "";

      try {

        requestMeta.setFuncId(this.callbackButton.getFuncId());

        ZcEbEntrust entrust = (ZcEbEntrust) this.listCursor.getCurrentObject();

        entrust.setAuditorId(WorkEnv.getInstance().getCurrUserId());

        //afterSaveProj = listPanel.zcEbProjectServiceDelegate.callbackFN(entrust, requestMeta);

      } catch (Exception e) {

        success = false;

        logger.error(e.getMessage(), e);

        errorInfo += e.getMessage();

      }

      if (success) {

        tabStatus = WFConstants.AUDIT_TAB_STATUS_TODO;

        this.refreshAll(afterSaveBill, true);

        JOptionPane.showMessageDialog(this, "收回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.listPanel.refreshCurrentTabData();

      } else {

        JOptionPane.showMessageDialog(this, "收回失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }

    private void doUnaudit() {

      ZcEbEntrust entrust = (ZcEbEntrust) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      if (entrust == null) {

        return;

      }

      entrust.setStatus(ZcEbEntrust.STATUS_DRAFT);

      ZcEbEntrust afterSaveBill = null;

      String errorInfo = "";

      boolean success = true;

      try {

        requestMeta.setFuncId(unAuditButton.getFuncId());

        afterSaveBill = this.zcEbEntrustServiceDelegate.unauditFN(entrust, requestMeta);

      } catch (Exception e) {

        success = false;

        logger.error(e.getMessage(), e);

        errorInfo += e.getMessage();

      }

      if (success) {

        this.refreshAll(afterSaveBill, true);

        JOptionPane.showMessageDialog(this, "销审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.listPanel.refreshCurrentTabData();

      } else {

        JOptionPane.showMessageDialog(this, "销审失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }

    private void doAudit() {

      ZcEbEntrust entrust = (ZcEbEntrust) this.listCursor.getCurrentObject();

      if (entrust == null) {

        return;

      }

      ZcEbEntrust afterSaveBill = null;

      String errorInfo = "";

      boolean success = true;

      try {

        requestMeta.setFuncId(this.auditPassButton.getFuncId());

        afterSaveBill = this.zcEbEntrustServiceDelegate.auditFN(entrust, requestMeta);

      } catch (Exception e) {

        success = false;

        logger.error(e.getMessage(), e);

        errorInfo += e.getMessage();

      }

      if (success) {

        tabStatus = WFConstants.AUDIT_TAB_STATUS_TODO;

        this.refreshAll(afterSaveBill, true);

        JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.listPanel.refreshCurrentTabData();

      } else {

        JOptionPane.showMessageDialog(this, "审核失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }

    private void doUntread() {

      ZcEbEntrust entrust = (ZcEbEntrust) this.listCursor.getCurrentObject();

      if (entrust == null) {

        return;

      }

      ZcEbEntrust afterSaveBill = null;

      String errorInfo = "";

      boolean success = true;

      try {

        requestMeta.setFuncId(this.unTreadButton.getFuncId());

        afterSaveBill = this.zcEbEntrustServiceDelegate.untreadFN(entrust, requestMeta);

      } catch (Exception e) {

        success = false;

        logger.error(e.getMessage(), e);

        errorInfo += e.getMessage();

      }

      if (success) {

        tabStatus = WFConstants.AUDIT_TAB_STATUS_TODO;

        this.refreshAll(afterSaveBill, true);

        JOptionPane.showMessageDialog(this, "退回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.listPanel.refreshCurrentTabData();

      } else {

        JOptionPane.showMessageDialog(this, "退回失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }

    private void doTrace() {

      ZcBaseBill bean = (ZcBaseBill) this.listCursor.getCurrentObject();

      if (bean == null) {

        return;

      }

      ZcUtil.showTraceDialog(bean, compoId);

    }

    public boolean isDataChanged() {

      return !DigestUtil.digest(oldEntrust).equals(DigestUtil.digest(listCursor.getCurrentObject()));

    }

    public JComponent createSubBillPanel() {

      //任务明细

      itemTabPane = new JTabbedPane();

      tablePanel.init();

      tablePanel.setTablePreferencesKey(this.getClass().getName() + "_table");

      tablePanel.getTable().setShowCheckedColumn(true);

      tablePanel.getSearchBar().setVisible(false);

      tablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(50, 0));

      this.subPackTableToolbar = new JFuncToolBar();

      JButton addBtn1 = new JButton("添加");

      JButton insertBtn1 = new JButton("插入");

      JButton delBtn1 = new JButton("删除");

      this.subPackTableToolbar.add(addBtn1);

      this.subPackTableToolbar.add(insertBtn1);

      this.subPackTableToolbar.add(delBtn1);

      tablePanel.add(this.subPackTableToolbar, BorderLayout.SOUTH);

      addBtn1.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          ZcEbEntrust entrust = (ZcEbEntrust) listCursor.getCurrentObject();
          ZcEbEntrustDetail detail = new ZcEbEntrustDetail();
          detail.setTempId(Guid.genID());
          detail.setSn(entrust.getSn());
          addSub(tablePanel, new ZcEbEntrustDetail());

        }

      });

      insertBtn1.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
          ZcEbEntrust entrust = (ZcEbEntrust) listCursor.getCurrentObject();
          ZcEbEntrustDetail detail = new ZcEbEntrustDetail();
          detail.setTempId(Guid.genID());
          detail.setSn(entrust.getSn());
          insertSub(tablePanel, new ZcEbEntrustDetail());

        }

      });

      delBtn1.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

          deleteSub(tablePanel);
          caculateAllItemSum();

        }

      });

      itemTabPane.setMinimumSize(new Dimension(240, 300));

      //资金构成

      biTabPane = new JTabbedPane();

      biTablePanel.init();

      biTablePanel.getSearchBar().setVisible(false);
      biTablePanel.setTablePreferencesKey(this.getClass().getName() + "_biTable");
      biTablePanel.getTable().setShowCheckedColumn(true);
      biTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));
      biTabPane.setMinimumSize(new Dimension(240, 150));
      biTabPane.setMinimumSize(new Dimension(240, 150));

      this.subTableToolbar = new JFuncToolBar();
      JButton addBtn = new JButton("添加");
      JButton insertBtn = new JButton("插入");
      JButton delBtn = new JButton("删除");
      this.subTableToolbar.add(addBtn);
      this.subTableToolbar.add(insertBtn);
      this.subTableToolbar.add(delBtn);
      biTablePanel.add(this.subTableToolbar, BorderLayout.SOUTH);

      addBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          addBiSub(biTablePanel);
        }
      });

      biTablePanel.getTable().setShowCheckedColumn(true);

      insertBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          insertBiSub(biTablePanel);
        }
      });

      delBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          deleteSub(biTablePanel);
        }
      });

      biTabPane.addTab("资金构成", biTablePanel);

      itemTabPane.addTab("任务明细", tablePanel);

      splitPane = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, biTabPane, itemTabPane);

      splitPane.setDividerDefaultLocation(this.getClass().getName() + "_splitPane_dividerLocation", 150);

      splitPane.setContinuousLayout(true);

      splitPane.setOneTouchExpandable(true);

      // 只显示向下的箭头

      //    splitPane.putClientProperty("toExpand", true);

      splitPane.setDividerSize(10);

      splitPane.setBackground(self.getBackground());

      return splitPane;

    }

    private void stopTableEditing() {

      JPageableFixedTable itemTable = this.tablePanel.getTable();
      JPageableFixedTable biTable = this.biTablePanel.getTable();
      if (itemTable.isEditing()) {

        itemTable.getCellEditor().stopCellEditing();

      }
      if (biTable.isEditing()) {
        biTable.getCellEditor().stopCellEditing();
      }

    }

    private void insertBiSub(JTablePanel tablePanel) {
      tablePanel.getTable().clearSelection();
      stopTableEditing();
      BeanTableModel editTableModel = (BeanTableModel) tablePanel.getTable().getModel();
      ZcPProMitemBi bean = new ZcPProMitemBi();
      bean.setFundCode("1");
      bean.setOriginCode("1");
      bean.setPaytypeCode("1");
      bean.setTempId(Guid.genID());
      ZcEbEntrust zb = (ZcEbEntrust) listCursor.getCurrentObject();
      //      bean.setSn(zb.getSn());
      editTableModel.insertRow(editTableModel.getRowCount(), bean);
    }

    private void addBiSub(JTablePanel tablePanel) {

      tablePanel.getTable().clearSelection();
      stopTableEditing();
      BeanTableModel editTableModel = (BeanTableModel) tablePanel.getTable().getModel();
      ZcPProMitemBi bean = new ZcPProMitemBi();
      bean.setFundCode("1");
      bean.setOriginCode("1");
      bean.setPaytypeCode("1");
      ZcEbEntrust zb = (ZcEbEntrust) listCursor.getCurrentObject();
      bean.setTempId(Guid.genID());
      editTableModel.insertRow(editTableModel.getRowCount(), bean);
    }

    private void refreshSubTableData(ZcEbEntrust entrust) {

      tablePanel.setTableModel(ZcEbEntrustToTableModelConverter.convertDetailToTableModel(entrust.getDetailList()));
      biTablePanel.setTableModel(ZcEbEntrustToTableModelConverter.convertSubBiTableData(entrust.getBiList()));
      ZcUtil.translateColName(this.tablePanel.getTable(), ZcEbEntrustToTableModelConverter.getItemInfo());
      ZcUtil.translateColName(this.biTablePanel.getTable(), ZcEbEntrustToTableModelConverter.biInfo);
      setTableItemEditor(this.tablePanel.getTable());
      setTableBiEditor(biTablePanel.getTable());
    }

    private void setTableItemEditor(JTable table) {

      table.setDefaultEditor(String.class, new TextCellEditor());

      SwingUtil.setTableCellEditor(table, FIELD_TRANS_ZC_PITEM_ARR_DATE, new DateCellEditor());

      SwingUtil.setTableCellRenderer(table, FIELD_TRANS_ZC_PITEM_ARR_DATE, new DateCellRenderer());

      SwingUtil.setTableCellEditor(table, FIELD_TRANS_ZC_CATALOGUE_CODE, new ZcBCatalogueCellEditor());

      SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_ZC_PITEM_ATTACH, new FileCellEditor("zcPitemAttachBlobid",

      (BeanTableModel) table.getModel()));

      SwingUtil.setTableCellEditor(table, "ZC_FIELD_ZC_ITEM_SUM", new MoneyCellEditor(false));

      SwingUtil.setTableCellRenderer(table, "ZC_FIELD_ZC_ITEM_SUM", new NumberCellRenderer());

      SwingUtil.setTableCellEditor(table, "ZC_MER_PRICE", new MoneyCellEditor(false));

      SwingUtil.setTableCellRenderer(table, "ZC_MER_PRICE", new NumberCellRenderer());
      //采购数量加上格式，保留二位小数，整数每三位加逗号，靠右显示
      SwingUtil.setTableCellEditor(table, "ZC_FIELD_ZC_CAIG_NUM", new MoneyCellEditor(false));
      //      SwingUtil.setTableCellRenderer(table, "ZC_FIELD_ZC_CAIG_NUM", new NumberCellRenderer());

      SwingUtil.setTableCellEditor(table, "ZC_FIELD_ZC_CAIG_UNIT", new TextCellEditor());

    }

    public void setTableBiEditor(JTable table) {

      table.setDefaultEditor(String.class, new TextCellEditor());

      SwingUtil.setTableCellEditor(table, "ZC_BI_JHUA_SUM", new MoneyCellEditor(false));

      SwingUtil.setTableCellRenderer(table, "ZC_BI_JHUA_SUM", new NumberCellRenderer());

      SwingUtil.setTableCellEditor(table, "ZC_BI_YJBA_SUM", new MoneyCellEditor(false));

      SwingUtil.setTableCellRenderer(table, "ZC_BI_YJBA_SUM", new NumberCellRenderer());

      SwingUtil.setTableCellEditor(table, FIELD_TRANS_FUND_CODE, new AsValComboBoxCellEditor("ZC_VS_FUND_NAME"));

      SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_FUND_CODE, new AsValCellRenderer("ZC_VS_FUND_NAME"));

      SwingUtil.setTableCellEditor(table, FIELD_TRANS_ORIGIN_CODE, new AsValComboBoxCellEditor("ZC_VS_ORIGIN_NAME"));

      SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_ORIGIN_CODE, new AsValCellRenderer("ZC_VS_ORIGIN_NAME"));

      SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_PAYTYPE_CODE, new AsValComboBoxCellEditor("ZC_VS_PAYTYPE_NAME"));

      SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_PAYTYPE_CODE, new AsValCellRenderer("ZC_VS_PAYTYPE_NAME"));

      //      FileCellEditor edit = new FileCellEditor("zcFundFileBlobid", (BeanTableModel) table.getModel());

      //      edit.setDownloadFileEnable(true);

      //      edit.setDeleteFileEnable(false);

      //      edit.setUploadFileEnable(false);

      SwingUtil.setTableCellEditor(table, "ZC_FUND_FILE", new FileCellEditor("zcFundFileBlobid", (BeanTableModel) table.getModel()));

    }

    private void refreshData() {

      ZcEbEntrust entrust = (ZcEbEntrust) listCursor.getCurrentObject();

      if (entrust == null) {

        //新增页面

        entrust = new ZcEbEntrust();

        entrust.setSn("自动编号");

        setDefualtValue(entrust, ZcSettingConstants.PAGE_STATUS_NEW);

        this.pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;

        //新插入一行数据

        ZcEbEntrustDetail item = new ZcEbEntrustDetail();

        ZcPProMitemBi bi = new ZcPProMitemBi();

        item.setSnd(Guid.genID());

        entrust.getDetailList().add(item);
        bi.setFundCode("1");
        bi.setOriginCode("1");
        bi.setPaytypeCode("1");
        biList.add(bi);
        entrust.setBiList(biList);
        List lst = new ArrayList();

        lst.add(entrust);

        this.listCursor.setDataList(lst, -1);

        this.checkSn = null;

      } else {

        List detailList = new ArrayList();

        if (entrust != null && (entrust.getDetailList() == null || entrust.getDetailList().size() == 0)) {

          detailList = zcEbEntrustServiceDelegate.getZcEbEntrustDetailBySn(entrust.getSn(), requestMeta);

          entrust.setDetailList(detailList);

        }

        this.checkSn = entrust.getSn();

      }
      //获取资金构成
      biList = fetchBiList(entrust);

      if (biList != null && biList.size() > 0) {
        entrust.setBiList(biList);
      }

      setEditingObject((ZcEbEntrust) this.listCursor.getCurrentObject());
      refreshSubTableData(entrust);
      setOldObject();

      addTableLisenter(this.tablePanel.getTable());

      updateFieldEditorsEditable();

      setButtonStatus();

    }

    @SuppressWarnings("unchecked")
    private void addTableLisenter(final JPageableFixedTable table) {

      final BeanTableModel model = (BeanTableModel) (table.getModel());

      final ZcEbEntrust zcEbEntrust = (ZcEbEntrust) listCursor.getCurrentObject();

      model.addTableModelListener(new TableModelListener() {

        public void tableChanged(TableModelEvent e) {

          if (e.getColumn() >= 0

            && (ZcElementConstants.FIELD_TRANS_ZC_ITEM_SUM.equals(model.getColumnIdentifier(e.getColumn()))

            || ZcElementConstants.FIELD_TRANS_ZC_CAIG_NUM.equals(model.getColumnIdentifier(e.getColumn())) || ZcElementConstants.FIELD_TRANS_ZC_MER_PRICE

            .equals(model.getColumnIdentifier(e.getColumn())))) {

            int k = table.getSelectedRow();

            if (k < 0)

              return;

            ZcEbEntrustDetail zcEbEntrustDetail = (ZcEbEntrustDetail) model.getBean(table.convertRowIndexToModel(k));

            BigDecimal price = new BigDecimal(0);

            BigDecimal num = BigDecimal.ZERO;

            if (zcEbEntrustDetail.getZcMerPrice() != null) {

              price = zcEbEntrustDetail.getZcMerPrice();

            }

            if (zcEbEntrustDetail.getZcCaigNum() != null) {

              num = zcEbEntrustDetail.getZcCaigNum();

            }
            zcEbEntrustDetail.setZcItemSum(price.multiply(num));

            model.fireTableRowsUpdated(k, k);

            caculateAllItemSum();

          }

        }

      });

    }

    protected void caculateAllItemSum() {

      ZcEbEntrust entrust = (ZcEbEntrust) this.listCursor.getCurrentObject();

      entrust.caculateAllItemSum();

      this.setEditingObject(entrust);

    }

    @Override
    protected void updateFieldEditorsEditable() {
      super.updateFieldEditors();
      BeanTableModel itemModel = (BeanTableModel) this.tablePanel.getTable().getModel();
      BeanTableModel biModel = (BeanTableModel) this.biTablePanel.getTable().getModel();
      if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {
        AsValFieldEditor pf = null;
        for (AbstractFieldEditor fd : this.fieldEditors) {
          if (fd.getFieldName() != null
            && (fd.getFieldName().equals("zcCoCodeNd") || fd.getFieldName().equals("status") || fd.getFieldName().equals("zcWeitoDate")
              || fd.getFieldName().equals("entrustDate") || fd.getFieldName().equals("snCode")) || fd.getFieldName().equals("sn")
            || fd.getFieldName().equals("zcMoneyBiSum") || fd.getFieldName().equals("executeDate") || fd.getFieldName().equals("nd")) {
            fd.setEnabled(false);
          } else {
            fd.setEnabled(true);
            if ("zcPifuCgfs".equals(fd.getFieldName())) {
              pf = (AsValFieldEditor) fd;
            }
          }

        }
        biModel.setEditable(true);
        itemModel.setEditable(true);
        this.changeWay(pf.getField());
      } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {
        AsValFieldEditor pf = null;

        ZcEbEntrust o = (ZcEbEntrust) this.listCursor.getCurrentObject();
        if (!ZcEbEntrust.STATUS_WAIT_ACCEPT.equals(o.getStatus())) {
          for (AbstractFieldEditor fd : this.fieldEditors) {
            if (fd.getFieldName() != null
              && (fd.getFieldName().equals("zcCoCodeNd") || fd.getFieldName().equals("status") || fd.getFieldName().equals("zcWeitoDate")
                || fd.getFieldName().equals("entrustDate") || fd.getFieldName().equals("snCode")) || fd.getFieldName().equals("sn")
              || fd.getFieldName().equals("zcMoneyBiSum") || fd.getFieldName().equals("executeDate") || fd.getFieldName().equals("nd")
              || fd.getFieldName().equals("zcMakeCode")) {
              fd.setEnabled(false);
            } else {
              fd.setEnabled(true);
              if ("zcPifuCgfs".equals(fd.getFieldName())) {
                pf = (AsValFieldEditor) fd;
              }
            }

          }
          biModel.setEditable(true);
          itemModel.setEditable(true);
        } else {
          for (AbstractFieldEditor fd : this.fieldEditors) {
            if (fd.getFieldName() != null
              && (fd.getFieldName().equals("isDesSup") || fd.getFieldName().equals("isPub") || fd.getFieldName().equals("isCar"))) {
              fd.setEnabled(true);
            } else {
              fd.setEnabled(false);
              if ("zcPifuCgfs".equals(fd.getFieldName())) {
                pf = (AsValFieldEditor) fd;
              }
            }

          }
          biModel.setEditable(false);
          itemModel.setEditable(false);
        }
        this.changeWay(pf.getField());
      } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_BROWSE)) {

        ZcEbEntrust o = (ZcEbEntrust) this.listCursor.getCurrentObject();

        if (ZcEbEntrust.STATUS_WAIT_ACCEPT.equals(o.getStatus())) {

          for (AbstractFieldEditor fd : this.fieldEditors) {

            //            if (fd.getFieldName() != null
            //              && (fd.getFieldName().equals("untreadReason") || fd.getFieldName().equals("remark") || fd.getFieldName().equals("zcMakeTel"))) {
            //              fd.setEnabled(true);
            //
            //            } else {

            fd.setEnabled(false);

            //            }

          }

        } else {

          for (AbstractFieldEditor fd : this.fieldEditors) {

            fd.setEnabled(false);

          }
        }
        biModel.setEditable(false);
        itemModel.setEditable(false);
      }

    }

    private void changeWay(AsValComboBox field) {

      if (field.getSelectedAsVal() == null || !ZcSettingConstants.PITEM_OPIWAY_YQZB.equals(field.getSelectedAsVal().getValId())) {
        isDesSup.setVisible(false);

      } else {
        isDesSup.setVisible(true);
      }

      if (field.getSelectedAsVal() == null || !ZcSettingConstants.PITEM_OPIWAY_DYLY.equals(field.getSelectedAsVal().getValId())) {
        isPub.setVisible(false);

      } else {
        isPub.setVisible(true);
      }

      if (field.getSelectedAsVal() == null || !ZcSettingConstants.PITEM_OPIWAY_XJ.equals(field.getSelectedAsVal().getValId())) {
        isCar.setVisible(false);

      } else {
        isCar.setVisible(true);
      }
      fieldEditorPanel.removeAll();
      initFieldEditorPanel();
      fieldEditorPanel.updateUI();
      this.repaint();
    }

    private void setDefualtValue(ZcEbEntrust entrust, String pageStatusNew) {

      entrust.setStatus(ZcEbEntrust.STATUS_DRAFT);

      entrust.setNd(this.requestMeta.getSvNd());
      entrust.setZcWeitoDate(this.requestMeta.getSysDate());

      entrust.setZcInputCode(this.requestMeta.getSvUserID());

      entrust.setZcInputDate(this.requestMeta.getSysDate());

      entrust.setAgency(this.requestMeta.getSvCoCode());

      entrust.setAgencyName(this.requestMeta.getSvCoName());
      entrust.setOrgCode(this.requestMeta.getSvOrgCode());

      entrust.setIsDesSup("Y");
      entrust.setIsPub("Y");

    }

    private void refreshAll(ZcEbEntrust entrust, boolean isRefreshButton) {

      biList = fetchBiList(entrust);

      if (biList != null && biList.size() > 0) {
        entrust.setBiList(biList);
      }

      this.listCursor.setCurrentObject(entrust);

      this.setEditingObject(entrust);

      this.refreshSubTableData(entrust);

      setButtonStatus();

      //      this.refreshData();

      setOldObject();

      //      if (isRefreshButton) {

      //        setButtonStatus(entrust, requestMeta, this.listCursor);

      //      }

      this.checkSn = entrust.getSn();

    }

    /**
     * <p> 获取资金构成 </p>
     * @param entrust
     * @return 
     * @return List
     * @author yuzz
     * @since Sep 20, 2012 10:07:33 AM
     */
    private List fetchBiList(ZcEbEntrust entrust) {
      List list = null;
      if (entrust.getZcMakeCode() != null && !entrust.getZcMakeCode().equals("自动生成")) {
        ElementConditionDto dto = new ElementConditionDto();
        dto.setZcText0(entrust.getZcMakeCode());
        list = zcEbBaseServiceDelegate.getForeignEntitySelectedData("ZC_P_PRO_MITEM_BI.getHtBiDetail", dto, requestMeta);
      }
      return list;
    }

  }

}
