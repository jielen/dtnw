package com.ufgov.zc.client.zc.project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Desktop;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.GkCommentDialog;
import com.ufgov.zc.client.component.GkCommentUntreadDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JSaveableSplitPane;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.AddButton;
import com.ufgov.zc.client.component.button.AgreeButton;
import com.ufgov.zc.client.component.button.AuditFinalPassButton;
import com.ufgov.zc.client.component.button.CallbackButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.DisagreeButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.OpenNotepadButton;
import com.ufgov.zc.client.component.button.PrintFormulaButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SendButton;
import com.ufgov.zc.client.component.button.SendToProcurementUnitButton;
import com.ufgov.zc.client.component.button.SendToXieBanButton;
import com.ufgov.zc.client.component.button.SuggestAuditPassButton;
import com.ufgov.zc.client.component.button.TraceButton;
import com.ufgov.zc.client.component.button.UnauditButton;
import com.ufgov.zc.client.component.button.UntreadButton;
import com.ufgov.zc.client.component.ui.conditionitem.ConditionFieldConstants;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.ZcEbEntrustHandler;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.EntrustFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.notepad.ZcNotepadDialog;
import com.ufgov.zc.client.zc.project.reqfile.component.ReqFilePanel;
import com.ufgov.zc.client.zc.ztb.docautogenerate.model.FormulaWordDataModel;
import com.ufgov.zc.client.zc.ztb.docautogenerate.operate.GenWordUtil;
import com.ufgov.zc.common.commonbiz.model.WfAware;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.WFConstants;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.system.util.Utils;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcEbAuditSheet;
import com.ufgov.zc.common.zc.model.ZcEbEntrust;
import com.ufgov.zc.common.zc.model.ZcEbRequirement;
import com.ufgov.zc.common.zc.model.ZcEbRequirementDetail;
import com.ufgov.zc.common.zc.model.ZcEbSupplier;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;

public class ZcEbRequirementEditPanel extends AbstractMainSubEditPanel {
  protected static final Logger logger = Logger.getLogger(ZcEbRequirementEditPanel.class);

  private static final long serialVersionUID = -2539657260090189021L;

  private ListCursor listCursor;

  /**
   * 按钮初始化
   */
  private final FuncButton addButton = new AddButton();

  private final FuncButton saveButton = new SaveButton();

  private final FuncButton deleteButton = new DeleteButton();

  private final FuncButton exitButton = new ExitButton();

  private final FuncButton editButton = new EditButton();

  // 工作流送审
  private final FuncButton sendButton = new SendButton();

  // 工作流收回
  private final FuncButton callbackButton = new CallbackButton();

  // 工作流填写意见审核通过
  private final FuncButton suggestPassButton = new SuggestAuditPassButton();

  // 工作流终审
  private final FuncButton auditFianlPassButton = new AuditFinalPassButton();

  // 工作流销审
  private final FuncButton unAuditButton = new UnauditButton();

  // 工作流退回
  private final FuncButton unTreadButton = new UntreadButton();

  // 工作流流程跟踪
  private final FuncButton traceButton = new TraceButton();

  public FuncButton openNotepadButton = new OpenNotepadButton();

  //送采购单位确认
  private final FuncButton sendToProcurementUnitButton = new SendToProcurementUnitButton();

  //同意
  private final FuncButton agreeButton = new AgreeButton();

  //不同意
  private final FuncButton disagreeButton = new DisagreeButton();

  //送协办人审核
  private final FuncButton sendToXieBanButton = new SendToXieBanButton();

  private final FuncButton printFormulaButton = new PrintFormulaButton();

  private final RequestMeta requestMeta;

  private ZcEbRequirement oldRequirement;

  private final String tabStatus;

  private String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  public ZcEbRequirementListPanel listPanel;

  private final JTabbedPane tabPane = new JTabbedPane();

  private final JPanel reqConfimPanel = new JPanel();

  JSaveableSplitPane splitPane;

  private ReqFilePanel reqFilePanel;

  private ZcEbReqDividePackPanel reqDividePackPanel;

  public ZcEbReqFormulasEditPanel zcEbReqFormulasEditPanel;

  public ZcEbRequirement zcEbRequirement;

  private final GkBaseDialog parent;

  public String sn = "";

  EntrustFieldEditor entrustFieldEditor;

  public ZcEbRequirementEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcEbRequirementListPanel listPanel) {
    super(ZcEbRequirement.class, listPanel.getBillElementMeta());
    this.listCursor = listCursor;
    this.zcEbRequirement = (ZcEbRequirement) listCursor.getCurrentObject();
    this.tabStatus = tabStatus;
    this.listPanel = listPanel;
    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
      LangTransMeta.translate(ZcElementConstants.TITLE_TRANS_ZC_EB_REQUIREMENT), TitledBorder.CENTER, TitledBorder.TOP,
      new Font("宋体", Font.BOLD, 15), Color.BLUE));
    this.colCount = 3;
    this.parent = parent;

    requestMeta = listPanel.getRequestMeta();
    init();
    refreshData();
    if (zcEbRequirement == null) {
      this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;
      updateTableEditable();
      entrustFieldEditor.setEnabled(true);
    } else {
      entrustFieldEditor.setEnabled(false);
    }
    // 根据工作流模版设置功能按钮是否可用
    this.deleteButton.setVisible(false);
    this.agreeButton.setVisible(false);
    this.disagreeButton.setVisible(false);
  }

  @Override
  protected void init() {

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
    splitPane.setContinuousLayout(false);
    splitPane.setOneTouchExpandable(true);
    // 只显示向下的箭头
    //      splitPane.putClientProperty("toExpand", true);
    splitPane.setDividerSize(5);
    splitPane.setDividerLocation(130);
    workPanel.add(splitPane, BorderLayout.CENTER);
    workPanel.repaint();

  }

  @Override
  public JComponent createSubBillPanel() {

    reqDividePackPanel = new ZcEbReqDividePackPanel();

    reqDividePackPanel.addPropertyChangeListener("packUpdate", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        //标段变更之后的回调函数
        boolean isSuccess = (Boolean) evt.getNewValue();
        ZcEbRequirement requirement = (ZcEbRequirement) listCursor.getCurrentObject();
        if (isSuccess) {
          if (requirement != null && requirement.getPackList() != null) {
            if (zcEbReqFormulasEditPanel != null) {
              zcEbReqFormulasEditPanel.refreshZcEbReqFormulaJTabbedPanePackInfo(requirement.getPackList());
            }
          }
        }

      }

    });
    reqDividePackPanel.addPropertyChangeListener("packInsert", new PropertyChangeListener() {

      public void propertyChange(PropertyChangeEvent evt) {
        //标段变更之后的回调函数
        boolean isSuccess = (Boolean) evt.getNewValue();
        ZcEbRequirement requirement = (ZcEbRequirement) listCursor.getCurrentObject();
        if (isSuccess) {

          if (requirement != null && requirement.getPackList() != null) {
            if (zcEbReqFormulasEditPanel != null) {
              zcEbReqFormulasEditPanel.refreshZcEbReqFormulaJTabbedPanePackInfo(requirement.getPackList());
            }
            reqFilePanel.refreshLeftTree(requirement);
          }
        }

      }

    });
    reqDividePackPanel.addPropertyChangeListener("packDelete", new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        //标段变更之后的回调函数
        boolean isSuccess = (Boolean) evt.getNewValue();
        ZcEbRequirement requirement = (ZcEbRequirement) listCursor.getCurrentObject();
        if (isSuccess) {

          if (requirement != null && requirement.getPackList() != null) {
            if (zcEbReqFormulasEditPanel != null) {
              zcEbReqFormulasEditPanel.refreshZcEbReqFormulaJTabbedPanePackInfo(requirement.getPackList());
            }
            reqFilePanel.refreshLeftTree(requirement);

          }
        }

      }

    });
    tabPane.addTab("分包的划分", reqDividePackPanel);
    reqConfimPanel.setLayout(new BorderLayout());
    tabPane.addTab("业务需求", reqConfimPanel);
    return tabPane;
  }

  private void setDetailDefaultValue(ZcEbRequirementDetail bean, String type) {
    bean.setTempId(Guid.genID());
    bean.setType(type);
    ZcEbRequirement temp = (ZcEbRequirement) this.listCursor.getCurrentObject();
    bean.setSn(temp.getSn());
    bean.setReqCode(temp.getReqCode());
  }

  public void setOldObject() {
    if (zcEbReqFormulasEditPanel != null) {
      zcEbReqFormulasEditPanel.setOldObject();
    }
    oldRequirement = (ZcEbRequirement) ObjectUtil.deepCopy(listCursor.getCurrentObject());

  }

  ZcEbEntrustHandler handler = new ZcEbEntrustHandler() {
    @Override
    public boolean isMultipleSelect() {

      return false;

    }

    @Override
    public void excute(List selectedDatas) {

      for (Object obj : selectedDatas) {

        ZcEbEntrust m = (ZcEbEntrust) obj;

        zcEbRequirement = (ZcEbRequirement) listCursor.getCurrentObject();
        //保存SN的值，因为listCursor.getCurrentObject()中取不到。
        sn = m.getSn();

        zcEbRequirement.getZcEbEntrust().setZcPifuCgfs(m.getZcPifuCgfs());

        zcEbRequirement.getZcEbEntrust().setSn(m.getSn());

        zcEbRequirement.setSn(m.getSn());

        zcEbRequirement.getZcEbEntrust().setZcMakeName(m.getZcMakeName());

        zcEbRequirement.getZcEbEntrust().setZcMakeCode(m.getZcMakeCode());

        zcEbRequirement.getZcEbEntrust().setCoCode(m.getCoCode());

        zcEbRequirement.getZcEbEntrust().setCoName(m.getCoName());

        zcEbRequirement.getZcEbEntrust().setNd(m.getNd());

        zcEbRequirement.getZcEbEntrust().setZcMakeLinkman(m.getZcMakeLinkman());

        zcEbRequirement.getZcEbEntrust().setZcMakeTel(m.getZcMakeTel());

        zcEbRequirement.getZcEbEntrust().setZcMoneyBiSum(m.getZcMoneyBiSum());

        zcEbRequirement.getZcEbAuditSheet().setSuperintendentOrg(m.getOrgCode());

        zcEbRequirement.getZcEbEntrust().setSnCode(m.getSnCode());

        ZcEbRequirement zcEbReq = listPanel.zcEbRequirementServiceDelegate.getZcEbRequirementByUseBudget(m.getSn(), requestMeta);

        if (zcEbReq != null) {
          zcEbRequirement.getZcEbEntrust().setUseBudget(zcEbReq.getZcEbEntrust().getUseBudget());
        } else {
          zcEbRequirement.getZcEbEntrust().setUseBudget(m.getZcMoneyBiSum());
        }

        IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,
          "zcEbBaseServiceDelegate");
        ElementConditionDto dto = new ElementConditionDto();
        dto.setZcText0(m.getSnCode());
        dto.setNd(requestMeta.getSvNd());
        List shs = zcEbBaseServiceDelegate.queryDataForList("ZcEbAuditSheet.list", dto, requestMeta);
        if (shs != null && shs.size() > 0) {
          zcEbRequirement.setZcEbAuditSheet((ZcEbAuditSheet) shs.get(0));
        }
        // zcEbRequirement.getFormulaPackList()

        //        getAllDetailListBySN();
        //              ZcEbRequirementEditPanel.this.refreshData();
        setEditingObject(zcEbRequirement);
        listCursor.setCurrentObject(zcEbRequirement);
        /**
         * 业务需求
         */
        reqConfimPanel.removeAll();
        reqFilePanel = new ReqFilePanel(zcEbRequirement, ZcEbRequirementEditPanel.this);
        reqConfimPanel.add(reqFilePanel);
        reqConfimPanel.repaint();

        //创建评标方法的页签
        if (tabPane.getComponentCount() == 2 && (!ZcSettingConstants.PITEM_OPIWAY_XJ.equals(zcEbRequirement.getZcEbEntrust().getZcPifuCgfs()))) {
          boolean formulaCanDelete = false;
          System.out.println("requestMeta.getSvCoCode()" + requestMeta.getSvCoCode());
          System.out.println("requirement.getZcEbEntrust().getCoCode()" + zcEbRequirement.getZcEbEntrust().getCoCode());
          if (requestMeta.getSvCoCode().equals(zcEbRequirement.getZcEbEntrust().getCoCode())) {
            formulaCanDelete = true;
          }
          zcEbReqFormulasEditPanel = new ZcEbReqFormulasEditPanel(zcEbRequirement.getFormulaPackList(), zcEbRequirement.getPackList(),
            zcEbRequirement, false, formulaCanDelete);
          tabPane.add("评标方法定制", zcEbReqFormulasEditPanel);
        }

        if (tabPane.getComponentCount() == 3
          && (ZcSettingConstants.PITEM_OPIWAY_XJ.equals(zcEbRequirement.getZcEbEntrust().getZcPifuCgfs()))) {
          tabPane.remove(zcEbReqFormulasEditPanel);

        }
      }

    }

    public void afterClear() {

      zcEbRequirement = (ZcEbRequirement) listCursor.getCurrentObject();

      zcEbRequirement.getZcEbEntrust().setZcPifuCgfs(null);

      zcEbRequirement.getZcEbEntrust().setZcMakeCode(null);

      zcEbRequirement.getZcEbEntrust().setZcMakeName(null);

      zcEbRequirement.getZcEbEntrust().setCoCode(null);

      zcEbRequirement.getZcEbEntrust().setCoName(null);

      zcEbRequirement.getZcEbEntrust().setZcMakeLinkman(null);

      zcEbRequirement.getZcEbEntrust().setZcMakeTel(null);

      zcEbRequirement.getZcEbEntrust().setZcMoneyBiSum(null);

      zcEbRequirement.getZcEbEntrust().setOrgCode(null);

      //      zcEbRequirement.setEntrustDetailList(new ArrayList());

      //      asdJTabelPanel.setTableModel(ZcEbAuditSheetToTableModelConverter
      //        .convertZcEbAuditSheetDetailToTableMode(auditSheet.getEntrustDetailList()));
      //
      //      translateSubTableColumn();

      setEditingObject(zcEbRequirement);

    }

  };

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {
    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();
    ElementConditionDto entrustDto = new ElementConditionDto();

    entrustDto.setCoCode(requestMeta.getSvCoCode());
    entrustFieldEditor = new EntrustFieldEditor("ZcEbEntrust.getEntrutsList", entrustDto, 20, handler, handler.getColumNames(),

    LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_TRANS_SN), "zcEbEntrust.snCode");

    entrustFieldEditor.setEnabled(true);
    editorList.add(entrustFieldEditor);
    
    TextFieldEditor zcMakeName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_NAME), "zcEbEntrust.zcMakeName");
    zcMakeName.setEnabled(false);
    editorList.add(zcMakeName);
    AsValFieldEditor status = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_EB_STATUS), "status",
      ConditionFieldConstants.ZC_VS_REQUIREMENT_STATUS);//"ZC_VS_REQUIREMENT_STATUS");
    status.setEnabled(false);
    editorList.add(status);
    // TextFieldEditor sn = new TextFieldEditor("任务编号", "zcEbEntrust.zcMakeCode");


    CompanyFieldEditor zcCoCode = new CompanyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_NAME), "zcEbEntrust.coCode");
    zcCoCode.setEnabled(false);
    editorList.add(zcCoCode);
    
    TextFieldEditor sn = new TextFieldEditor("采购计划编号", "zcEbEntrust.zcMakeCode");
    sn.setEnabled(false);
    editorList.add(sn);
    MoneyFieldEditor zcMoneyBiSum = new MoneyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MONEY_BI_SUM),
      "zcEbEntrust.zcMoneyBiSum");
    zcMoneyBiSum.setEnabled(false);
    editorList.add(zcMoneyBiSum);

    AsValFieldEditor zcPifuCgfs = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_PIFU_CGFS),
      "zcEbEntrust.zcPifuCgfs", "ZC_VS_PITEM_OPIWAY");
    zcPifuCgfs.setEnabled(false);
    editorList.add(zcPifuCgfs);

    TextFieldEditor zcMakeLinkman = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_EB_ENTRUST_LINK_MAN),
      "zcEbEntrust.zcMakeLinkman");
    zcMakeLinkman.setEnabled(false);
    editorList.add(zcMakeLinkman);

    TextFieldEditor zcMakeTel = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_TEL), "zcEbEntrust.zcMakeTel");
    zcMakeTel.setEnabled(false);
    editorList.add(zcMakeTel);

    MoneyFieldEditor zcUseMoneyBiSum = new MoneyFieldEditor("可用预算", "zcEbEntrust.useBudget");
    zcUseMoneyBiSum.setEnabled(false);
    editorList.add(zcUseMoneyBiSum);

    TextFieldEditor jinBanRen = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_TRANS_SUPERINTENDENT_NAME),
      "zcEbAuditSheet.superintendentName");
    jinBanRen.setEnabled(false);
    editorList.add(jinBanRen);
    //    OrgFieldEditor superintendentOrg = new OrgFieldEditor("采购中心负责处室", "zcEbAuditSheet.superintendentOrg", true);
    //    superintendentOrg.setEnabled(false);
    //    editorList.add(superintendentOrg);
    //    ZcEbRequirement temp = (ZcEbRequirement) this.listCursor.getCurrentObject();
    //    if (ZcSettingConstants.PITEM_OPIWAY_DYLY.equals(temp.getZcEbEntrust().getZcPifuCgfs())) {
    //      AbstractFieldEditor provider = new ProviderFieldEditor(this, "指定供应商", "providerName");
    //      editorList.add(provider);
    //    }

    //    TextFieldEditor remark = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_REMARK), "remark");
    //    editorList.add(remark);

    //    ZcEbRequirement temp = (ZcEbRequirement) this.listCursor.getCurrentObject();
    //    if (ZcSettingConstants.PITEM_OPIWAY_DYLY.equals(temp.getZcEbEntrust().getZcPifuCgfs())) {
    //      AbstractFieldEditor provider = new ProviderFieldEditor(this, "指定供应商", "providerName");
    //      editorList.add(provider);
    //    }

    //    TextFieldEditor remark = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_REMARK), "remark");
    //    editorList.add(remark);
    return editorList;
  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(listPanel.compoId);
    toolBar.add(addButton);
    toolBar.add(editButton);
    toolBar.add(saveButton);
    toolBar.add(sendButton);
    toolBar.add(suggestPassButton);
    toolBar.add(auditFianlPassButton);
    toolBar.add(sendToProcurementUnitButton);
    toolBar.add(agreeButton);
    toolBar.add(disagreeButton);
    toolBar.add(sendToXieBanButton);
    //    toolBar.add(callbackButton);
    toolBar.add(unAuditButton);
    toolBar.add(unTreadButton);
    toolBar.add(traceButton);
    toolBar.add(deleteButton);
    toolBar.add(openNotepadButton);

    toolBar.add(printFormulaButton);
    toolBar.add(exitButton);
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopEditing();
        // 新增
        doAdd();
      }
    });

    editButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopEditing();
        // 新增
        doEdit();
      }
    });

    saveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopEditing();
        doSave();
      }
    });
    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doDelete();
      }
    });
    sendButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doSend();
      }
    });
    auditFianlPassButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doAudit();
      }
    });
    suggestPassButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        // 填写意见审核
        doSuggestPass();
      }
    });
    unAuditButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doUnaudit();
      }
    });
    callbackButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 收回
        doCallback();
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
    exitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doExit();
      }
    });
    sendToProcurementUnitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 送采购单位确认
        doSendToProcurementUnit();
      }
    });
    agreeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 同意
        doAgree();
      }
    });
    disagreeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 不同意
        doDisagree();
      }
    });
    sendToXieBanButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 送协办人
        doSendToXieBan();
      }
    });
    printFormulaButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 打印评标方法
        doPrintFormula();
      }
    });

    openNotepadButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doOpenNotepad();

      }

    });
  }

  private void doOpenNotepad() {

    ZcEbRequirement sheet = (ZcEbRequirement) this.listCursor.getCurrentObject();

    new ZcNotepadDialog(sheet.getSn(), parent);

  }

  private void doPrintFormula() {
    ZcEbRequirement zcEbRequirement = (ZcEbRequirement) this.listCursor.getCurrentObject();
    GenWordUtil util = new GenWordUtil(new FormulaWordDataModel(zcEbRequirement.getSn()), "manyWordTableTemplate.ftl");
    try {
      Desktop.getDesktop().open(util.outWordFilePath());
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "创建打印文件失败 ！" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void executeAudit(ZcEbRequirement requirement, Integer isGoonAudit, String defaultMsg) {
    if (checkBeforeSave()) {
      return;
    }
    GkCommentDialog commentDialog = null;
    if (defaultMsg == null) {
      commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
        ModalityType.APPLICATION_MODAL);
    } else {
      commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
        ModalityType.APPLICATION_MODAL, defaultMsg);
    }
    if (commentDialog.cancel) {
      return;
    }
    boolean success = true;
    String errorInfo = "";
    ZcEbRequirement afterSaveRequirement = null;
    try {
      isGoonAudit = isGoonAudit == null ? 0 : isGoonAudit;
      requirement.setIsGoonAudit(isGoonAudit);
      requirement.setComment(commentDialog.getComment());
      requirement.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      //      listPanel.zcEbRequirementServiceDelegate.saveFN(requirement, requestMeta);
      afterSaveRequirement = listPanel.zcEbRequirementServiceDelegate.auditFN(requirement, requestMeta);
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      this.refreshAll(afterSaveRequirement, true);
      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.listPanel.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "审核失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * 送采购单位确认
   */
  private void doSendToProcurementUnit() {
    ZcEbRequirement zcEbRequirement = (ZcEbRequirement) this.listCursor.getCurrentObject();
    requestMeta.setFuncId(this.sendToProcurementUnitButton.getFuncId());
    executeAudit(zcEbRequirement, ZcSettingConstants.IS_GOON_AUDIT_YES, null);
  }

  /**
   * 送协办人
   */
  private void doSendToXieBan() {
    ZcEbRequirement zcEbRequirement = (ZcEbRequirement) this.listCursor.getCurrentObject();
    requestMeta.setFuncId(this.sendToXieBanButton.getFuncId());
    executeAudit(zcEbRequirement, ZcSettingConstants.SEND_TO_XIE_BAN, null);
  }

  /**
   * 同意
   */
  private void doAgree() {
    ZcEbRequirement zcEbRequirement = (ZcEbRequirement) this.listCursor.getCurrentObject();
    requestMeta.setFuncId(this.agreeButton.getFuncId());
    Integer auditFlag = zcEbRequirement.getIsGoonAudit();
    auditFlag = ZcUtil.getAuditFlagValue(auditFlag, 0, requestMeta);
    executeAudit(zcEbRequirement, auditFlag, null);
  }

  /**
   * 不同意
   */
  private void doDisagree() {
    ZcEbRequirement zcEbRequirement = (ZcEbRequirement) this.listCursor.getCurrentObject();
    requestMeta.setFuncId(this.disagreeButton.getFuncId());
    Integer auditFlag = zcEbRequirement.getIsGoonAudit();
    auditFlag = ZcUtil.getAuditFlagValue(auditFlag, 1, requestMeta);
    executeAudit(zcEbRequirement, auditFlag, ZcSettingConstants.AUDIT_DISAGREE_DEFULT_MESSAGE);
  }

  private void stopEditing() {

    reqDividePackPanel.stopEditing();
  }

  public boolean doExit() {

    if (reqFilePanel != null) {
      reqFilePanel.closeWordPane();
    }
    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave()) {

          return false;

        }

      }
    }
    parent.dispose();
    return true;

  }

  private void refreshAll(ZcEbRequirement afterSaveBill, boolean isRefreshButton) {
    this.listCursor.setCurrentObject(afterSaveBill);
    this.refreshSubTableData(afterSaveBill);
    setOldObject();
    if (isRefreshButton) {
      setButtonStatus(afterSaveBill, requestMeta, this.listCursor);
    }
  }

  /*

  * 新增

  */

  private void doAdd() {
    this.listCursor = new ListCursor(new ArrayList(1), -1);
    //this.listCursor.setCurrentObject(null);
    refreshData();
  }

  /**
   * 送审前校验
   */
  private boolean checkBeforeSend() {
    if (!listPanel.zcEbRequirementServiceDelegate.isExistZbEbReqFileByRegCode(zcEbRequirement.getReqCode(), requestMeta)) {

      JOptionPane.showMessageDialog(this, "需求文件未保存，不能送审！", "提示", JOptionPane.WARNING_MESSAGE);

      return true;

    }
    return false;
  }

  /**
   * 保存前校验
   *
   * @param ZcEbRequirement
   * @return
   */
  private boolean checkBeforeSave() {
    ZcEbRequirement zcEbRequirement = (ZcEbRequirement) this.listCursor.getCurrentObject();

    List notNullBillElementList = this.listPanel.getBillElementMeta().getNotNullBillElement();

    StringBuilder errorInfo = new StringBuilder();
    String validateInfo = ZcUtil.validateBillElementNull(zcEbRequirement, notNullBillElementList);
    //    if (ZcSettingConstants.PITEM_OPIWAY_DYLY.equals(zcEbRequirement.getZcEbEntrust().getZcPifuCgfs())
    //      && (zcEbRequirement.getProviderCode() == null || "".equals(zcEbRequirement.getProviderCode()))) {
    //      validateInfo = validateInfo + "供应商不能为空\n";
    //    }

    if (validateInfo.length() != 0) {
      errorInfo.append(LangTransMeta.translate(ZcElementConstants.TITLE_TRANS_ZC_EB_REQUIREMENT)).append("：\n").append(validateInfo.toString())
        .append("\n");
    }

    if (errorInfo.length() != 0) {
      JOptionPane.showMessageDialog(this, errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);
      return true;
    }

    if (!reqDividePackPanel.checkBeforeSave()) {

      return true;
    }

    //    if (!listPanel.zcEbRequirementServiceDelegate.isExistZbEbReqFileByRegCode(zcEbRequirement.getReqCode(), requestMeta)) {
    //
    //      JOptionPane.showMessageDialog(this, "需求文件未上传，不能送审！", "提示", JOptionPane.WARNING_MESSAGE);
    //
    //      return true;
    //
    //    }

    if (zcEbReqFormulasEditPanel != null && !zcEbReqFormulasEditPanel.checkBeforeSave()) {
      return true;
    }

    /*    HashMap m = new HashMap();

        m.put("SN", zcEbRequirement.getSn());

        m.put("STATUS", ZcSettingConstants.ZC_EB_DUTY_AUDIT_SHEET_STATUS_ATTN);

        List lst = listPanel.zcEbBaseServiceDelegate.queryDataForList("ZcEbAuditSheet.getZcEbAuditSheetReq", m, requestMeta);

        if (lst == null || lst.size() == 0) {

          JOptionPane.showMessageDialog(this, "采购中心经办人尚未审核确认！", "提示", JOptionPane.WARNING_MESSAGE);

          return true;
        }*/

    return false;
  }

  public void doDelete() {
    boolean success = true;
    String errorInfo = "";
    int num = JOptionPane.showConfirmDialog(this, "确定删除吗", "删除确认", 0);
    if (num != 0) {
      return;
    }
    try {
      requestMeta.setFuncId(deleteButton.getFuncId());
      listPanel.zcEbRequirementServiceDelegate.deleteFN(((ZcEbRequirement) listCursor.getCurrentObject()).getReqCode(), this.requestMeta);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      success = false;
      errorInfo += e.getMessage();
    }
    if (success) {
      this.listPanel.refreshCurrentTabData();
      this.listCursor.removeCurrentObject();
      this.refreshData();
      JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
    } else {
      JOptionPane.showMessageDialog(this, "删除失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  public boolean doSave() {
    ZcEbRequirement currObj = (ZcEbRequirement) listCursor.getCurrentObject();

    //    if (checkBeforeSave()) {
    //      return false;
    //    }
    if (!isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据没有发生改变，不用保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return false;
    }
    if (currObj.getPackList() == null || currObj.getPackList().size() < 1) {
      JOptionPane.showMessageDialog(this, "请先进行分包的划分！.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return false;

    }
    //因为页面上没有sn字段，所以手工赋值
    ZcEbRequirement afterSaveRequirement = null;
    boolean success = true;
    String errorInfo = "";
    try {
      //      requestMeta.setFuncId(saveButton.getFuncId());
      currObj.setSn(currObj.getZcEbEntrust().getSn());
      if (reqFilePanel != null) {
        reqFilePanel.setZcEbReqFile();
      }
      afterSaveRequirement = listPanel.zcEbRequirementServiceDelegate.saveFN(currObj, requestMeta);
      currObj.setReqCode(afterSaveRequirement.getReqCode());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      success = false;
      errorInfo += e.getMessage();
    }
    if (success) {
      this.refreshSubTableData(afterSaveRequirement);
      this.setEditingObject(afterSaveRequirement);
      this.listCursor.setCurrentObject(afterSaveRequirement);
      setOldObject();
      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      listPanel.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
    return true;
  }

  public void doEdit() {
    this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;
    updateFieldEditorsEditable();
    updateTableEditable();
  }

  private void updateTableEditable() {
    ZcEbRequirement temp = (ZcEbRequirement) this.listCursor.getCurrentObject();

    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW) || this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {

      if (ZcSettingConstants.PITEM_OPIWAY_DYLY.equals(temp.getZcEbEntrust().getZcPifuCgfs())) {

        for (AbstractFieldEditor fd : this.fieldEditors) {

          if ("providerName".equals(fd.getFieldName())) {

            fd.setEnabled(true);

          } else {

            fd.setEnabled(false);

          }
        }
      }
      for (JTablePanel tablePanel : getSubTables()) {

        setWFSubTableEditable(tablePanel, true);

      }
      if (reqFilePanel != null) {
        reqFilePanel.saveButton.setVisible(true);
        reqFilePanel.editReqFileButton.setVisible(true);
      }
      if (zcEbReqFormulasEditPanel != null) {
        zcEbReqFormulasEditPanel.doEdit(true);
      }
      if (reqDividePackPanel != null) {
        reqDividePackPanel.doEdit(true);
      }

    } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_BROWSE)) {

      if (ZcSettingConstants.PITEM_OPIWAY_DYLY.equals(temp.getZcEbEntrust().getZcPifuCgfs())) {

        for (AbstractFieldEditor fd : this.fieldEditors) {

          fd.setEnabled(false);

        }
      }

      for (JTablePanel tablePanel : getSubTables()) {

        setWFSubTableEditable(tablePanel, false);

      }
      if (reqFilePanel != null) {
        reqFilePanel.saveButton.setVisible(false);
        reqFilePanel.editReqFileButton.setVisible(false);
      }
      if (zcEbReqFormulasEditPanel != null) {
        zcEbReqFormulasEditPanel.doEdit(false);
      }
      if (reqDividePackPanel != null) {
        reqDividePackPanel.doEdit(false);
      }
    }

  }

  /*

  * 填写意见审核

  */

  private void doSuggestPass() {
    ZcEbRequirement zcEbRequirement = (ZcEbRequirement) this.listCursor.getCurrentObject();
    requestMeta.setFuncId(this.suggestPassButton.getFuncId());
    executeAudit(zcEbRequirement, 0, null);
  }

  private void doSend() {
    if (checkBeforeSave() || checkBeforeSend()) {
      return;
    }
    boolean success = true;
    ZcEbRequirement afterSaveBill = null;
    String errorInfo = "";
    try {
      requestMeta.setFuncId(sendButton.getFuncId());
      ZcEbRequirement requirement = (ZcEbRequirement) this.listCursor.getCurrentObject();
      requirement.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      afterSaveBill = listPanel.zcEbRequirementServiceDelegate.newCommitFN(requirement, requestMeta);
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      this.refreshAll(afterSaveBill, true);
      JOptionPane.showMessageDialog(this, "送审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      listPanel.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "送审失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  /*

  * 销审

  */

  private void doUnaudit() {
    boolean success = true;
    ZcEbRequirement afterSaveBill = null;
    String errorInfo = "";
    try {
      requestMeta.setFuncId(unAuditButton.getFuncId());
      ZcEbRequirement requirement = (ZcEbRequirement) this.listCursor.getCurrentObject();
      requirement.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      afterSaveBill = listPanel.zcEbRequirementServiceDelegate.unAuditFN(requirement, requestMeta);
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      this.refreshAll(afterSaveBill, true);
      JOptionPane.showMessageDialog(this, "销审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.listPanel.refreshCurrentTabData();
      refreshData();
    } else {
      JOptionPane.showMessageDialog(this, "销审失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  /*

  * 终审

  */

  private void doAudit() {
    if (checkBeforeSave()) {
      return;
    }
    boolean success = true;
    ZcEbRequirement afterSaveBill = null;
    String errorInfo = "";
    try {
      requestMeta.setFuncId(this.auditFianlPassButton.getFuncId());
      ZcEbRequirement requirement = (ZcEbRequirement) this.listCursor.getCurrentObject();
      requirement.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      requirement.setIsGoonAudit(ZcSettingConstants.SEND_TO_FINAL);
      //listPanel.zcEbRequirementServiceDelegate.save(requirement, requestMeta);
      afterSaveBill = listPanel.zcEbRequirementServiceDelegate.auditFN(requirement, requestMeta);
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      this.refreshAll(afterSaveBill, true);
      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.listPanel.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "审核失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  /*

  * 退回

  */

  private void doUntread() {
    //    if (checkBeforeSave()) {
    //      return;
    //    }
    GkCommentUntreadDialog commentDialog = new GkCommentUntreadDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
      ModalityType.APPLICATION_MODAL);
    if (commentDialog.cancel) {
      return;
    }
    boolean success = true;
    ZcEbRequirement afterSaveBill = null;
    String errorInfo = "";
    try {
      requestMeta.setFuncId(unTreadButton.getFuncId());
      ZcEbRequirement requirement = (ZcEbRequirement) this.listCursor.getCurrentObject();
      requirement.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      requirement.setComment(commentDialog.getComment());
      afterSaveBill = listPanel.zcEbRequirementServiceDelegate.untreadFN(requirement, requestMeta);
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      this.refreshAll(afterSaveBill, true);
      JOptionPane.showMessageDialog(this, "退回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.listPanel.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "退回失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  /*

  * 收回

  */

  private void doCallback() {
    boolean success = true;
    ZcEbRequirement afterSaveBill = null;
    String errorInfo = "";
    try {
      requestMeta.setFuncId(this.callbackButton.getFuncId());
      ZcEbRequirement requirement = (ZcEbRequirement) this.listCursor.getCurrentObject();
      requirement.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      afterSaveBill = listPanel.zcEbRequirementServiceDelegate.callbackFN(requirement, requestMeta);
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      this.refreshAll(afterSaveBill, true);
      JOptionPane.showMessageDialog(this, "收回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.listPanel.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "收回失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void doTrace() {
    ZcBaseBill bean = (ZcBaseBill) this.listCursor.getCurrentObject();
    if (bean == null) {
      return;
    }
    ZcUtil.showTraceDialog(bean, listPanel.compoId);
  }

  public boolean isDataChanged() {

    stopEditing();
    if (!saveButton.isVisible()) {
      return false;
    }
    boolean b = !DigestUtil.digest(oldRequirement).equals(DigestUtil.digest(listCursor.getCurrentObject()));
    boolean formulaChanged = false;
    if (zcEbReqFormulasEditPanel != null) {

      formulaChanged = zcEbReqFormulasEditPanel.isDataChanged();
    }

    return b | formulaChanged;
  }

  private void refreshData() {
    ZcEbRequirement requirement = (ZcEbRequirement) listCursor.getCurrentObject();
    if (requirement == null) {
      requirement = new ZcEbRequirement();
      requirement.setStatus("0");
      listCursor.getDataList().add(requirement);
      requirement.setExecuteDate(requestMeta.getSysDate());
      requirement.setExecutor(requestMeta.getSvUserID());
    } else {
      ZcEbRequirement temp = listPanel.zcEbRequirementServiceDelegate.getZcEbRequirementByReqCode(requirement.getReqCode(), requestMeta);
      requirement = temp;
      listCursor.setCurrentObject(requirement);
      if ("0".equals(requirement.getStatus())) {
        requirement.setExecuteDate(requestMeta.getSysDate());
        requirement.setExecutor(requestMeta.getSvUserID());
      }
    }

    Long processInstId = requirement.getProcessInstId();
    if (processInstId != null && processInstId.longValue() > 0) {
      wfCanEditFieldMap = BillElementMeta.getWfCanEditField(requirement, requestMeta);
    }
    refreshSubTableData(requirement);

    //    /**
    //     * 业务需求
    //     */
    //
    if (reqFilePanel == null) {
      reqFilePanel = new ReqFilePanel(requirement, this);
      reqConfimPanel.add(reqFilePanel);

    }

    if (requirement.getFormulaPackList() == null) {
      List formulaPackList = new ArrayList();
      requirement.setFormulaPackList(formulaPackList);
    }
    //创建评标方法的页签
    if (tabPane.getComponentCount() == 2
      && (!ZcSettingConstants.PITEM_OPIWAY_XJ.equals(requirement.getZcEbEntrust().getZcPifuCgfs())) && requirement.getReqCode() != null) {
      boolean formulaCanDelete = false;
      System.out.println("requestMeta.getSvCoCode()" + requestMeta.getSvCoCode());
      System.out.println("requirement.getZcEbEntrust().getCoCode()" + requirement.getZcEbEntrust().getCoCode());
      if (requestMeta.getSvCoCode().equals(requirement.getZcEbEntrust().getCoCode())) {
        formulaCanDelete = true;
      }
      zcEbReqFormulasEditPanel = new ZcEbReqFormulasEditPanel(requirement.getFormulaPackList(), requirement.getPackList(), requirement, false,
        formulaCanDelete);
      tabPane.add("评标方法定制", zcEbReqFormulasEditPanel);
    }
    // 根据工作流模版设置字段是否可编辑
    this.setEditingObject(requirement);
    updateTableEditable();
    updateWFEditorEditable(requirement, requestMeta);
    if (WFConstants.AUDIT_TAB_STATUS_CANCEL.equals(requirement.getStatus())) {
      setCancelStatus(this.listCursor);
    } else {
      setButtonStatus((ZcEbRequirement) listCursor.getCurrentObject(), requestMeta, this.listCursor);
    }
    setOldObject();
    tabPane.repaint();
    if ( ZcSettingConstants.PITEM_OPIWAY_XJ.equals(requirement.getZcEbEntrust().getZcPifuCgfs())) {
      printFormulaButton.setVisible(false);
    } else {
      printFormulaButton.setVisible(true);
    }
  }

  @Override
  protected void updateWFEditorEditable(WfAware baseBill, RequestMeta requestMeta) {
    Long processInstId = baseBill.getProcessInstId();
    if (processInstId != null && processInstId.longValue() > 0) {
      // 工作流的单据
      wfCanEditFieldMap = BillElementMeta.getWfCanEditField(baseBill, requestMeta);
      for (AbstractFieldEditor editor : fieldEditors) {
        //工作流中定义可编辑的字段
        if (wfCanEditFieldMap != null && wfCanEditFieldMap.containsKey(Utils.getDBColNameByFieldName(editor.getEditObject(), editor.getFieldName()))) {
          editor.setEnabled(true);
        } else {
          editor.setEnabled(false);
        }
      }
    }
  }

  @Override
  protected JTablePanel[] getSubTables() {

    return reqDividePackPanel.getSubTables();
  }

  @Override
  protected void updateFieldEditors() {
    for (AbstractFieldEditor editor : fieldEditors) {
      editor.setEditObject(editingObject);
    }
  }

  private void refreshSubTableData(ZcEbRequirement requirement) {
    reqDividePackPanel.setZcEbRequirement(requirement);
    if (requirement.getPackList() == null) {
      requirement.setPackList(new ArrayList());
    }
    reqDividePackPanel.refreshSubPackTablePanel(requirement.getPackList());
    if (zcEbReqFormulasEditPanel != null) {
      zcEbReqFormulasEditPanel.refreshData(requirement);
    }
    if (reqFilePanel != null) {
      reqFilePanel.refreshLeftTree(requirement);

    }
    listCursor.setCurrentObject(requirement);
    reqDividePackPanel.getPackBudget();
  }

  public void onClickProvider(ZcEbSupplier supplier) {
    if (supplier != null) {
      ZcEbRequirement requirement = (ZcEbRequirement) listCursor.getCurrentObject();
      requirement.setProviderCode(supplier.getCode());
      requirement.setProviderName(supplier.getName());
      listCursor.setCurrentObject(requirement);
      this.setEditingObject(requirement);
    }
  }

  public void onClearProvider() {
    ZcEbRequirement requirement = (ZcEbRequirement) listCursor.getCurrentObject();
    requirement.setProviderCode("");
    requirement.setProviderName("");
    listCursor.setCurrentObject(requirement);
    this.setEditingObject(requirement);
  }
}
