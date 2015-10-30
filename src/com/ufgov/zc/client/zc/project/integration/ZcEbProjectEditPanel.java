package com.ufgov.zc.client.zc.project.integration;

import java.awt.Color;
import java.awt.Component;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.GkCommentDialog;
import com.ufgov.zc.client.component.GkCommentUntreadDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
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
import com.ufgov.zc.client.component.button.IsSendToNextButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.OpenNotepadButton;
import com.ufgov.zc.client.component.button.PreviousButton;
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
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AutoNumFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.OrgFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFilePathFieldEditor;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.notepad.ZcNotepadDialog;
import com.ufgov.zc.client.zc.project.integration.zbbook.services.ZbBookOperatorsService;
import com.ufgov.zc.common.commonbiz.model.WfAware;
import com.ufgov.zc.common.console.model.AsEmp;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.WFConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcEbBulletin;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.model.ZcEbPackReq;
import com.ufgov.zc.common.zc.model.ZcEbPackSupplier;
import com.ufgov.zc.common.zc.model.ZcEbPlan;
import com.ufgov.zc.common.zc.model.ZcEbProj;
import com.ufgov.zc.common.zc.publish.IZcEbBulletinServiceDelegate;

@SuppressWarnings("unchecked")
public class ZcEbProjectEditPanel extends AbstractMainSubEditPanel {
  protected static final Logger logger = Logger.getLogger(ZcEbProjectEditPanel.class);

  protected String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  private boolean zbBookRedoFlag = false; //招标书重做标志

  private static final long serialVersionUID = -2539657260090189021L;

  private RequestMeta requestMeta;

  private FuncButton addButton = new AddButton();

  private FuncButton previousButton = new PreviousButton();

  private FuncButton saveButton = new SaveButton();

  protected FuncButton editButton = new EditButton();

  private FuncButton deleteButton = new DeleteButton();

  private FuncButton nextButton = new NextButton();

  private FuncButton exitButton = new ExitButton();

  // 工作流送审
  private FuncButton sendButton = new SendButton();

  //是否送主任审核
  private FuncButton isSendToNextButton = new IsSendToNextButton();

  // 工作流收回
  private FuncButton callbackButton = new CallbackButton();

  // 工作流填写意见审核通过
  private FuncButton suggestPassButton = new SuggestAuditPassButton();

  // 工作流销审
  private FuncButton unAuditButton = new UnauditButton();

  // 工作流退回
  private FuncButton unTreadButton = new UntreadButton();

  // 工作流流程跟踪
  private FuncButton traceButton = new TraceButton();

  //同意
  private FuncButton agreeButton = new AgreeButton();

  //不同意
  private FuncButton disagreeButton = new DisagreeButton();

  //送协办人审核
  private FuncButton sendToXieBanButton = new SendToXieBanButton();

  // 工作流终审
  private FuncButton auditFinalPassButton = new AuditFinalPassButton();

  //送采购单位确认
  private FuncButton sendToProcurementUnitButton = new SendToProcurementUnitButton();

  public FuncButton openNotepadButton = new OpenNotepadButton();

  protected ListCursor listCursor;

  private ZcEbProj oldProj;

  private String tabStatus;

  private ZcEbProjectListPanel listPanel;

  private int packSelectRow = 0;

  private ZcEbProjectEditFrame dself;

  public List<ZcEbBulletin> bulletinList = new ArrayList<ZcEbBulletin>();

  private String purType;

  protected DateFieldEditor openDt;

  private BillElementMeta detailPackBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd(ZcEbProjectListPanel.compoId + "_PACK");

  private BillElementMeta detailPackReqBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd(ZcEbProjectListPanel.compoId + "_PACKREQ");

  private final BillElementMeta detailPackQuaBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd(ZcEbProjectListPanel.compoId + "_PACKQUA");

  protected IZcEbBulletinServiceDelegate zcEbBulletinServiceDelegate = (IZcEbBulletinServiceDelegate) ServiceFactory.create(
    IZcEbBulletinServiceDelegate.class, "zcEbBulletinServiceDelegate");

  private ZcEbProjectDividerPackPanel zcEbProjectDividerPackPanel;

  public ZcEbProjectEditPanel(ListCursor listCursor, String tabStatus, ZcEbProjectListPanel listPanel, ZcEbProjectEditFrame dself) {
    super(ZcEbProj.class, listPanel.getBillElementMeta());
    this.listCursor = listCursor;
    this.tabStatus = tabStatus;
    this.listPanel = listPanel;
    this.dself = dself;
    if (listCursor == null || listCursor.getCurrentObject() == null) {
      pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;
      sendButton.setEnabled(false);
      saveButton.setEnabled(true);
      sendToXieBanButton.setEnabled(false);
      sendToProcurementUnitButton.setEnabled(false);
    } else {
      pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;
      saveButton.setEnabled(false);
      if ("draft".equals(this.tabStatus)) {
        sendToXieBanButton.setEnabled(false);
        sendToProcurementUnitButton.setEnabled(false);
        sendButton.setEnabled(true);
      }
    }
    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LangTransMeta.translate("ZC_EB_PROJ_TITLE"),
      TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));
    this.colCount = 3;
    init();
    requestMeta = listPanel.getRequestMeta();
    refreshData();
    this.setSize(workPanel.getSize().width - 1, workPanel.getSize().height - 1);
    workPanel.setSize(new Dimension(workPanel.getSize().width - 1, workPanel.getSize().height - 1));
  }

  public String getPurType() {
    return purType;
  }

  public void setPurType(String purType) {
    this.purType = purType;
  }

  @Override
  public JComponent createSubBillPanel() {
    zcEbProjectDividerPackPanel = new ZcEbProjectDividerPackPanel(this);
    return zcEbProjectDividerPackPanel;
  }

  private void setOldObject() {
    oldProj = this.getProjWithoutTempId();
  }

  private ZcEbProj getProjWithoutTempId() {
    ZcEbProj temp = (ZcEbProj) ObjectUtil.deepCopy(listCursor.getCurrentObject());
    List<ZcEbPack> packs = temp.getPackList();
    for (ZcEbPack pack : packs) {
      List<ZcEbPackReq> reqs = pack.getRequirementDetailList();
      for (ZcEbPackReq req : reqs) {
        req.setTempId(null);
      }
    }
    return temp;
  }

  @Override
  public void setEditingObject(WfAware editingObject) {
    super.setEditingObject(editingObject);
  }

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {
    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();
    AutoNumFieldEditor reqCodeEditor = new AutoNumFieldEditor("项目编号", "projCode");
    editorList.add(reqCodeEditor);
    TextFieldEditor projName = new TextFilePathFieldEditor("项目名称", "projName");
    editorList.add(projName);
    AsValFieldEditor status = new AsValFieldEditor("状态", "status", ConditionFieldConstants.ZC_VS_PROJ_STATUS);
    status.setEnabled(false);
    editorList.add(status);
    AsValFieldEditor zcPifuCgfs = new AsValFieldEditor("采购方式", "purType", "ZC_VS_PITEM_OPIWAY");
    zcPifuCgfs.setEnabled(false);
    editorList.add(zcPifuCgfs);
    MoneyFieldEditor zcMoneyBiSum = new MoneyFieldEditor("项目预算", "projSum");
    zcMoneyBiSum.setEnabled(false);
    editorList.add(zcMoneyBiSum);
    DateFieldEditor zcWeitoDate = new DateFieldEditor("立项时间", "projDate");
    zcWeitoDate.setEnabled(false);
    editorList.add(zcWeitoDate);
    OrgFieldEditor zcOrg = new OrgFieldEditor("负责部门", "org", false);
    editorList.add(zcOrg);
    TextFieldEditor zcMakeLinkman = new TextFieldEditor("标书制作负责人", "manager", false);
    editorList.add(zcMakeLinkman);
    TextFieldEditor zcMakeTel = new TextFieldEditor("标书制作负责人电话", "phone");
    editorList.add(zcMakeTel);
    //    TextFieldEditor email = new TextFieldEditor("标书制作负责人电子邮件", "email");
    //    editorList.add(email);
    //    TextFieldEditor fax = new TextFieldEditor("标书制作负责人传真", "fax");
    //    editorList.add(fax);

    TextFieldEditor zcAttnName = new TextFieldEditor("开标负责人", "attnName", false);
    editorList.add(zcAttnName);
    TextFieldEditor zcAttnPhone = new TextFieldEditor("开标负责人电话", "attnPhone");
    editorList.add(zcAttnPhone);
    //    TextFieldEditor attnEmail = new TextFieldEditor("开标负责人电子邮件", "attnEmail");
    //    editorList.add(attnEmail);
    //    TextFieldEditor attnFax = new TextFieldEditor("开标负责人传真", "attnFax");
    //    editorList.add(attnFax);
    TextFieldEditor lxr = new TextFieldEditor("单位联系人", "zcMakeLinkman");
    editorList.add(lxr);

    TextFieldEditor remark = new TextFieldEditor("备注", "remark");
    editorList.add(remark);
    TextFieldEditor inputor = new TextFieldEditor("创建人", "inputor");
    inputor.setEnabled(false);
    editorList.add(inputor);

    Integer[] allowMinutes = { 0, 10, 20, 30, 40, 50 };
    DateFieldEditor df = new DateFieldEditor("招标文件公告时间", "plan.sellStartTime", DateFieldEditor.TimeTypeH24, allowMinutes, true);
    df.setEnabled(false);
    editorList.add(df);
    df = new DateFieldEditor("点击投标截止时间", "plan.sellEndTime", DateFieldEditor.TimeTypeH24, allowMinutes, true);
    df.setEnabled(false);
    editorList.add(df);
    openDt = new DateFieldEditor("开标时间", "plan.openBidTime", DateFieldEditor.TimeTypeH24, allowMinutes, true);
    openDt.setEnabled(false);
    editorList.add(openDt);
    return editorList;
  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(ZcEbProjectListPanel.compoId);
    //    toolBar.add(addButton);只能在列表界面新增
    toolBar.add(editButton);
    toolBar.add(saveButton);
    toolBar.add(sendButton);
    toolBar.add(isSendToNextButton);
    toolBar.add(suggestPassButton);
    toolBar.add(auditFinalPassButton);
    toolBar.add(sendToProcurementUnitButton);
    toolBar.add(agreeButton);
    toolBar.add(disagreeButton);
    toolBar.add(sendToXieBanButton);
    toolBar.add(callbackButton);
    toolBar.add(unAuditButton);
    toolBar.add(unTreadButton);
    toolBar.add(traceButton);
    toolBar.add(deleteButton);
    toolBar.add(openNotepadButton);
    toolBar.add(previousButton);
    toolBar.add(nextButton);
    toolBar.add(exitButton);
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 新增
        doAdd();
      }
    });
    saveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 保存
        doSave();
      }
    });
    editButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doEdit();
      }
    });
    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 删除
        doDelete();
      }
    });
    sendButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 送审
        doSend();
      }
    });
    isSendToNextButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 送审
        doSendNext();
      }
    });
    callbackButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 收回
        doCallback();
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
        // 销审
        doUnaudit();
      }
    });
    unTreadButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 退回
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
    auditFinalPassButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 终审
        doAudit();
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
    sendToProcurementUnitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 送采购单位
        doSendToProcurementUnit();
      }
    });
    openNotepadButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 打开记事本
        doOpenNotepad();
      }
    });
  }

  private void doExit() {
    if (oldProj != null && oldProj.getProjCode() != null) {
      try {
        if ("_1_2_3_4_6_".indexOf(((ZcEbProj) listCursor.getCurrentObject()).getPurType()) != -1) {
          dself.getzBPanel().getBookService().toCheckZbBookSaved(oldProj);
        }
      } catch (Exception e2) {
        e2.printStackTrace();
        JOptionPane.showMessageDialog(null, "本次退出时没有和服务器端进行同步，本地更新可能没有更新到服务器，\n可以下次再进行同步，无法同步原因:" + e2.getMessage());
      }
    }
    dself.closeWindow();
  }

  private void refreshAll(ZcEbProj afterSaveProj, boolean isRefreshButton) {
    this.listCursor.setCurrentObject(afterSaveProj);
    this.setEditingObject(afterSaveProj);
    this.refreshSubTableData(afterSaveProj);
    setOldObject();
    if (isRefreshButton) {
      setButtonStatus(afterSaveProj, requestMeta, this.listCursor);
    }
  }

  public void refreshPlan(ZcEbPlan plan) {
    ZcEbProj afterSaveProj = (ZcEbProj) listCursor.getCurrentObject();
    afterSaveProj.setPlan(plan);
    listCursor.setCurrentObject(afterSaveProj);
    setEditingObject(afterSaveProj);
  }

  /*

  * 新增

  */

  private void doAdd() {
    sendButton.setEnabled(false);
    this.listCursor = new ListCursor(new ArrayList(1), -1);
    refreshData();
  }

  /**
   * 保存前校验
   *
   * @param ZcEbRequirement
   * @return
   */
  private boolean checkBeforeSave() {
    List notNullBillElementList = this.listPanel.getBillElementMeta().getNotNullBillElement();
    List notNullPackBillElementList = detailPackBillElementMeta.getNotNullBillElement();
    List notNullPackReqBillElementList = detailPackReqBillElementMeta.getNotNullBillElement();
    List notNullPackQuaBillElementList = detailPackQuaBillElementMeta.getNotNullBillElement();
    ZcEbProj zcEbProj = (ZcEbProj) this.listCursor.getCurrentObject();
    StringBuilder errorInfo = new StringBuilder();
    String validateInfo = ZcUtil.validateBillElementNull(zcEbProj, notNullBillElementList);
    String validatePackInfo = ZcUtil.validateDetailBillElementNull(zcEbProj.getPackList(), notNullPackBillElementList, false);
    String validatePackReqInfo = new String();
    String validatePackQuaInfo = "";
    boolean checkPurType = checkPurType(zcEbProj);
    if (!checkPurType) {
      errorInfo.append("同一项目下的所有标段的采购方式必须一致：\n");
    }
    for (ZcEbPack pack : ((List<ZcEbPack>) (zcEbProj.getPackList()))) {
      validatePackReqInfo = ZcUtil.validateDetailBillElementNull(pack.getRequirementDetailList(), notNullPackReqBillElementList, false);

      if (ZcSettingConstants.PITEM_OPIWAY_XJ.equals(pack.getPurType()) && "Y".equals(pack.getEntrust().getIsCar())) {
        if (pack.getPackMaxPrice() == null || pack.getPackMaxPrice().compareTo(BigDecimal.ZERO) <= 0) {
          validatePackReqInfo = validatePackReqInfo + "车辆采购必须填写最高限价\n";
        } else if (pack.getPackMaxPrice().compareTo(pack.getPackBudget()) > 0) {
          validatePackReqInfo = validatePackReqInfo + "最高限价应不高于预算\n";
        }
        if (pack.getRequirementDetailList() != null) {
          for (ZcEbPackReq req : (List<ZcEbPackReq>) pack.getRequirementDetailList()) {
            if (req.getRequirementDetail() != null && req.getRequirementDetail().getArrDate() == null) {
              validatePackReqInfo = validatePackReqInfo + "车辆采购必须填写要求到货日期\n";
            }
          }
        }
      }

      if (validatePackReqInfo.length() != 0) {
        validatePackReqInfo = pack.getPackName() + ":" + validatePackReqInfo;
        break;
      }

/*      if (ZcSettingConstants.PITEM_OPIWAY_XJ.equals(zcEbProj.getPurType())) {
        validatePackQuaInfo = ZcUtil.validateDetailBillElementNull(pack.getPackQua(), notNullPackQuaBillElementList, false);
        if (validatePackQuaInfo.length() != 0) {
          validatePackQuaInfo = pack.getPackName() + ":" + validatePackQuaInfo;
          break;
        }
      }*/
    }

    if (ZcSettingConstants.PITEM_OPIWAY_XJ.equals(zcEbProj.getPurType()) && zcEbProj.getPlan().getOpenBidTime() == null) {
      validateInfo = validateInfo + "询价项目开标时间不能为空\n";
    }

    if (validateInfo.length() != 0) {
      errorInfo.append("立项划分标段：\n").append(validateInfo.toString()).append("\n");
    }
    if (validatePackInfo.length() != 0) {
      errorInfo.append("项目划分标段：\n").append(validatePackInfo.toString()).append("\n");
    }
    if (validatePackReqInfo.length() != 0) {
      errorInfo.append("标段需求明细：\n").append(validatePackReqInfo.toString()).append("\n");
    }
    if (validatePackQuaInfo.length() != 0) {
      errorInfo.append("供应商资质：\n").append(validatePackQuaInfo.toString()).append("\n");
    }

    if (errorInfo.length() != 0) {
      JOptionPane.showMessageDialog(this, errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);
      return true;
    }
    if (ZcSettingConstants.PITEM_OPIWAY_YQZB.equals(zcEbProj.getPurType()) || ZcSettingConstants.PITEM_OPIWAY_DYLY.equals(zcEbProj.getPurType())) {

      for (ZcEbPack pack : ((List<ZcEbPack>) (zcEbProj.getPackList()))) {
        if (pack.getSupplierList() == null || pack.getSupplierList().size() == 0) {
          errorInfo.append("分包:").append(pack.getPackName()).append("需要指定供应商");
        } else if (ZcSettingConstants.PITEM_OPIWAY_DYLY.equals(zcEbProj.getPurType()) && pack.getSupplierList().size() > 1) {
          errorInfo.append("分包:").append(pack.getPackName()).append("只能指定唯一供应商");
        } else {
          for (int i = 0; i < pack.getSupplierList().size(); i++) {
            ZcEbPackSupplier supplier = (ZcEbPackSupplier) pack.getSupplierList().get(i);
            if (supplier.getZcEbSupplier() == null || supplier.getZcEbSupplier().getCode() == null || "".equals(supplier.getZcEbSupplier().getCode())) {
              errorInfo.append("分包:").append(pack.getPackName()).append("第").append(i + 1).append("行未选择供应商");
            }
          }
        }
      }
    }

    if (errorInfo.length() != 0) {
      JOptionPane.showMessageDialog(this, "供应商列表\n" + errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);
      return true;
    }
    return false;

  }

  /**
   * 检查项目是否缺少招标书和评标方法
   *
   * @param projCode
   * @return
   */
  private boolean toCheckZbFileAndEvalElements() {
    ZcEbProj zcEbProj = (ZcEbProj) this.listCursor.getCurrentObject();
    String projCode = zcEbProj.getProjCode();
    ZbBookOperatorsService bookService = new ZbBookOperatorsService(null);
    return bookService.toCheckZbFileAndEvalElements(projCode);
  }

  private boolean toCheckPlan() {
    ZcEbProj zcEbProj = (ZcEbProj) this.listCursor.getCurrentObject();
    if (zcEbProj.getPlan() == null || zcEbProj.getPlan().getPlanCode() == null || zcEbProj.getPlan().getPlanCode().length() == 0) {
      JOptionPane.showMessageDialog(this, "当前项目未制定执行计划");
      return true;
    }
    return false;
  }

  /**
   * @return String 返回类型
   * @Description: 校验采购方式，所有分包的采购方式，必须一致，而且要和项目的采购方式一致。
   * @since 1.0
   */
  private boolean checkPurType(ZcEbProj zcEbProj) {
    List<ZcEbPack> packList = zcEbProj.getPackList();
    String purType = zcEbProj.getPurType();
    if (packList.size() > 0) {
      for (ZcEbPack pack : packList) {
        //为空时直接比较
        if (pack.getPurType() == null) {
          return purType != null;
        }
        if (!pack.getPurType().equals(purType)) {
          return false;
        }
      }
    }
    return true;
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
      String projCode = ((ZcEbProj) listCursor.getCurrentObject()).getProjCode();
      if (projCode != null) {
        listPanel.zcEbProjectServiceDelegate.delete(projCode, this.requestMeta);
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      success = false;
      errorInfo += e.getMessage();
    }
    if (success) {
      String projCode = ((ZcEbProj) listCursor.getCurrentObject()).getProjCode();
      JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.listPanel.refreshCurrentTabData();
      this.listCursor.removeCurrentObject();
      this.refreshData();
      //      dself.removeXJtab();
      //      dself.refreshCurrentTab(projCode, "del");
    } else {
      JOptionPane.showMessageDialog(this, "删除失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  public void doEdit() {
    ZcEbProj proj = (ZcEbProj) this.listCursor.getCurrentObject();
    //草稿状态调用新加的方法
    if (!"draft".equals(this.tabStatus)) {
      updateEditorEditable(true);
      //            updateFieldEditorsEditable();

    } else {
      updateEditorEditable(true);
    }
    saveButton.setEnabled(true);
    sendButton.setEnabled(false);
    editButton.setEnabled(false);

    zcEbProjectDividerPackPanel.setButtonEnabled(proj);

  }

  public boolean doSave() {
    if (checkBeforeSave()) {
      return false;
    }
    if (!isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据没有发生改变，不用保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      saveButton.setEnabled(false);
      sendButton.setEnabled(true);
      editButton.setEnabled(true);
      //更新编辑状态
      updateEditorEditable(false);
      return false;
    }
    ZcEbProj afterSaveProj = null;
    boolean success = true;
    String errorInfo = "";
    try {
      ZcEbProj proj = (ZcEbProj) listCursor.getCurrentObject();
      if (proj.getPackList().size() > 1) {
        proj.setIsSplitPack("Y");
      } else {
        proj.setIsSplitPack("N");
      }
      requestMeta.setFuncId(saveButton.getFuncId());
      afterSaveProj = listPanel.zcEbProjectServiceDelegate.save(proj, this.requestMeta);
      sendButton.setEnabled(true);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      success = false;
      errorInfo += e.getMessage();
    }
    if (success) {
      this.refreshAll(afterSaveProj, false);
      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      saveButton.setEnabled(false);
      editButton.setEnabled(true);
      this.listPanel.refreshCurrentTabData();
      ZcEbProj proj = (ZcEbProj) listCursor.getCurrentObject();
      //            dself.refreshCurrentTab(proj.getProjCode(), "save");
      this.dself.doCreateNewTab(afterSaveProj, zbBookRedoFlag);
      //更新编辑状态
      updateEditorEditable(false);
    } else {
      JOptionPane.showMessageDialog(this, "保存失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
    return true;
  }

  /**
   * 送采购单位
   */
  private void doSendToProcurementUnit() {
    ZcEbProj project = (ZcEbProj) this.listCursor.getCurrentObject();
    requestMeta.setFuncId(this.sendToProcurementUnitButton.getFuncId());
    executeAudit(project, ZcSettingConstants.IS_GOON_AUDIT_YES, null);
  }

  /*

  * 送审

  */

  private void doSend() {

    boolean success = true;
    ZcEbProj afterSaveProj = null;
    String errorInfo = "";
    ZcEbProj proj = (ZcEbProj) this.listCursor.getCurrentObject();

    if ("_1_2_3_4_6_".indexOf(proj.getPurType())!=-1 && toCheckZbFileAndEvalElements()) {
      return;
    }

    if (toCheckPlan()) {
      return;
    }

    try {
      requestMeta.setFuncId(this.sendButton.getFuncId());
      proj.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      afterSaveProj = listPanel.zcEbProjectServiceDelegate.newCommitFN(proj, requestMeta);
      listCursor.setCurrentObject(afterSaveProj);
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      refreshData();
      JOptionPane.showMessageDialog(this, "送审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "送审失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void doSendNext() {
    if (isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    ZcEbProj proj = (ZcEbProj) this.listCursor.getCurrentObject();
    requestMeta.setFuncId(this.isSendToNextButton.getFuncId());
    executeAudit(proj, ZcSettingConstants.IS_GOON_AUDIT_YES, null);
  }

  private void executeAudit(ZcEbProj proj, Integer isGoonAudit, String defaultMsg) {
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
    ZcEbProj afterSaveProj = null;
    try {
      isGoonAudit = isGoonAudit == null ? 0 : isGoonAudit;
      proj.setIsGoonAudit(isGoonAudit);
      proj.setComment(commentDialog.getComment());
      proj.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      listPanel.zcEbProjectServiceDelegate.save(proj, requestMeta);
      afterSaveProj = listPanel.zcEbProjectServiceDelegate.auditFN(proj, requestMeta);
      listCursor.setCurrentObject(afterSaveProj);
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      this.refreshData();
      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
    } else {
      JOptionPane.showMessageDialog(this, "审核失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  /*

  * 收回

  */

  private void doCallback() {
    boolean success = true;
    ZcEbProj afterSaveProj = null;
    String errorInfo = "";
    try {
      requestMeta.setFuncId(this.callbackButton.getFuncId());
      ZcEbProj proj = (ZcEbProj) this.listCursor.getCurrentObject();
      proj.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      afterSaveProj = listPanel.zcEbProjectServiceDelegate.callbackFN(proj, requestMeta);
      listCursor.setCurrentObject(afterSaveProj);
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      this.refreshData();
      JOptionPane.showMessageDialog(this, "收回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.listPanel.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "收回失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  /*

  * 填写意见审核

  */

  private void doSuggestPass() {
    ZcEbProj project = (ZcEbProj) this.listCursor.getCurrentObject();
    requestMeta.setFuncId(this.suggestPassButton.getFuncId());
    String jianShenRoleId = AsOptionMeta.getOptVal("OPT_ZC_CGZX_JSKY_ROLE");//监审组员角色
    if (WorkEnv.getInstance().containRole(jianShenRoleId)) {//如果是监审员，则不修改审批状态
      Integer auditFlag = project.getIsGoonAudit();
      executeAudit(project, auditFlag, null);
    } else {
      executeAudit(project, ZcSettingConstants.IS_GOON_AUDIT_NO, null);
    }
  }

  /**
   * 送协办人
   */
  private void doSendToXieBan() {
    ZcEbProj project = (ZcEbProj) this.listCursor.getCurrentObject();
    requestMeta.setFuncId(this.sendToXieBanButton.getFuncId());
    executeAudit(project, ZcSettingConstants.SEND_TO_XIE_BAN, null);
  }

  /**
   * 同意
   */
  private void doAgree() {
    ZcEbProj project = (ZcEbProj) this.listCursor.getCurrentObject();
    requestMeta.setFuncId(this.agreeButton.getFuncId());
    Integer auditFlag = project.getIsGoonAudit();
    auditFlag = ZcUtil.getAuditFlagValue(auditFlag, 0, requestMeta);
    executeAudit(project, auditFlag, null);
  }

  /**
   * 不同意
   */
  private void doDisagree() {
    ZcEbProj project = (ZcEbProj) this.listCursor.getCurrentObject();
    requestMeta.setFuncId(this.disagreeButton.getFuncId());
    Integer auditFlag = project.getIsGoonAudit();
    auditFlag = ZcUtil.getAuditFlagValue(auditFlag, 1, requestMeta);
    executeAudit(project, auditFlag, ZcSettingConstants.AUDIT_DISAGREE_DEFULT_MESSAGE);
  }

  /*

  * 终审

  */

  private void doAudit() {
    if (checkBeforeSave()) {
      return;
    }
    if (isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    boolean success = true;
    ZcEbProj afterSaveProj = null;
    String errorInfo = "";
    try {
      requestMeta.setFuncId(this.auditFinalPassButton.getFuncId());
      ZcEbProj proj = (ZcEbProj) this.listCursor.getCurrentObject();
      proj.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      proj.setIsGoonAudit(ZcSettingConstants.SEND_TO_FINAL);
      afterSaveProj = listPanel.zcEbProjectServiceDelegate.auditFN(proj, requestMeta);
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      this.refreshAll(afterSaveProj, true);
      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.listPanel.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "审核失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  /*

  * 销审

  */

  private void doUnaudit() {
    requestMeta.setFuncId(unAuditButton.getFuncId());
    boolean success = true;
    ZcEbProj afterSaveProj = null;
    String errorInfo = "";
    try {
      ZcEbProj proj = (ZcEbProj) this.listCursor.getCurrentObject();
      proj.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      afterSaveProj = listPanel.zcEbProjectServiceDelegate.unAuditFN(proj, requestMeta);
      this.listCursor.setCurrentObject(afterSaveProj);
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      this.refreshData();
      JOptionPane.showMessageDialog(this, "销审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.listPanel.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "销审失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  /*

  * 退回

  */

  private void doUntread() {
    if (checkBeforeSave()) {
      return;
    }
    if (isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    GkCommentUntreadDialog commentDialog = new GkCommentUntreadDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
      ModalityType.APPLICATION_MODAL);
    if (commentDialog.cancel) {
      return;
    }
    boolean success = true;
    ZcEbProj afterSaveProj = null;
    String errorInfo = "";
    try {
      requestMeta.setFuncId(unTreadButton.getFuncId());
      ZcEbProj proj = (ZcEbProj) this.listCursor.getCurrentObject();
      proj.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      proj.setComment(commentDialog.getComment());
      afterSaveProj = listPanel.zcEbProjectServiceDelegate.untreadFN(proj, requestMeta);
      listCursor.setCurrentObject(afterSaveProj);
    } catch (Exception e) {
      success = false;
      logger.error(e.getMessage(), e);
      errorInfo += e.getMessage();
    }
    if (success) {
      this.refreshData();
      JOptionPane.showMessageDialog(this, "退回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.listPanel.refreshCurrentTabData();
    } else {
      JOptionPane.showMessageDialog(this, "退回失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }
  }

  /*

  * 流程跟踪

  */

  private void doTrace() {
    ZcBaseBill bean = (ZcBaseBill) this.listCursor.getCurrentObject();
    if (bean == null) {
      return;
    }
    ZcUtil.showTraceDialog(bean, ZcEbProjectListPanel.compoId);
  }

  public boolean isDataChanged() {
    return !DigestUtil.digest(oldProj).equals(DigestUtil.digest(this.getProjWithoutTempId()));
  }

  private void refreshSubTableData(ZcEbProj zcEbProj) {

    zcEbProjectDividerPackPanel.refreshSubTableData(zcEbProj);
  }

  public void doOpenNotepad() {
    ZcEbProj proj = (ZcEbProj) this.listCursor.getCurrentObject();
    if (proj == null) {
      JOptionPane.showMessageDialog(this, "项目为空不能记录相关信息 ！", "错误", JOptionPane.ERROR_MESSAGE);
      return;
    }
    List packs = proj.getPackList();
    if (packs == null || packs.size() == 0 || ((ZcEbPack) (packs.get(0))).getEntrustCode() == null) {
      JOptionPane.showMessageDialog(this, "请先创建标段信息，再记录相关信息 ！", "错误", JOptionPane.ERROR_MESSAGE);
      return;
    }
    new ZcNotepadDialog(((ZcEbPack) (packs.get(0))).getEntrustCode(), dself);
  }

  private void refreshData() {
    ZcEbProj proj = (ZcEbProj) this.listCursor.getCurrentObject();
    //是否新增数据
    boolean isNew = false;
    if (proj == null) {
      //新增数据
      isNew = true;
      // 新增的单据
      proj = new ZcEbProj();
      proj.setProjDate(new Date());
      proj.setStatus(ZcSettingConstants.PROJECT_STATUS_DRAFT);
      proj.setIsPubPurBulletin("Y");
      proj.setIsPubPurResult("Y");
      proj.setNd(WorkEnv.getInstance().getTransNd());
      proj.setOrg(this.requestMeta.getSvOrgCode());
      proj.setManager(this.requestMeta.getSvUserName());
      proj.setManagerCode(this.requestMeta.getSvUserID());
      proj.setInputor(this.requestMeta.getSvUserName());
      proj.setInputorId(this.requestMeta.getSvUserID());

      AsEmp empManager = new AsEmp();
      empManager.setUserId(this.requestMeta.getSvUserID());
      List list = listPanel.baseDataServiceDelegate.getAsEmpByUserID(empManager, requestMeta);
      if (list.size() > 0) {
        empManager = (AsEmp) list.get(0);
        proj.setFax(empManager.getFax());
        proj.setPhone(empManager.getPhone());
        proj.setEmail(empManager.getEmail());
      }
      proj.setInputDate(new Date());
      listCursor.getDataList().add(proj);
    } else {
      // 打开已有的单据
      proj = listPanel.zcEbProjectServiceDelegate.getZcEbProjByProjCode(proj.getProjCode(), requestMeta);
      listCursor.setCurrentObject(proj);
    }
    if (proj.getPlan() == null) {
      proj.setPlan(new ZcEbPlan());
    }
    this.setEditingObject(proj);
    refreshSubTableData(proj);
    setOldObject();
    // 根据工作流模版设置字段是否可编辑
    updateWFEditorEditable(proj, requestMeta);
    //不是新增则设置默认编辑状态
    if (!isNew) {
      updateEditorEditable(false);
    }
    // 根据工作流模版设置功能按钮是否可用
    if (WFConstants.AUDIT_TAB_STATUS_CANCEL.equals(proj.getStatus())) {
      this.setCancelStatus(listCursor);
    } else {
      setButtonStatus(proj, requestMeta, this.listCursor);
    }
  }

  //设置编辑状态
  private void updateEditorEditable(boolean isEdit) {
    ZcEbProj proj = (ZcEbProj) this.listCursor.getCurrentObject();

    //除去个别不能修改数据，其他设置状态
    for (AbstractFieldEditor editor : fieldEditors) {
      if (!"projCode".equals(editor.getFieldName()) && !"status".equals(editor.getFieldName()) && !"purType".equals(editor.getFieldName())
        && !"projSum".equals(editor.getFieldName()) && !"inputor".equals(editor.getFieldName()) && !"org".equals(editor.getFieldName())
        && !"projDate".equals(editor.getFieldName()) && !"plan.openBidTime".equals(editor.getFieldName())
        && !"plan.sellEndTime".equals(editor.getFieldName()) && !"plan.sellStartTime".equals(editor.getFieldName())) {

        editor.setEnabled(isEdit);
      }
      if (proj != null && "5".equals(proj.getPurType()) && "plan.openBidTime".equals(editor.getFieldName())) {
        editor.setEnabled(isEdit);
      }
    }

    // 子表的设置
    updateSubTableEditable(isEdit);

  }

  //设置按钮状态
  protected void updateSubTableEditable(boolean isEdit) {

    if (getSubTables() != null) {

      if (isEdit) {

        for (JTablePanel tablePanel : getSubTables()) {

          setWFSubTableEditable(tablePanel, true);

        }

      } else {

        for (JTablePanel tablePanel : getSubTables()) {

          setWFSubTableEditable(tablePanel, false);

        }

      }

    }

    boolean flag = true;
    if (!ZcSettingConstants.PAGE_STATUS_NEW.equals(pageStatus)) {
      flag = false;
    }

    Component[] components = getSubTables()[0].getComponents();

    for (Component component : components) {

      if (component instanceof JFuncToolBar) {

        component.setVisible(flag);

      }

    }
  }

  @Override
  protected JTablePanel[] getSubTables() {
    return zcEbProjectDividerPackPanel.getSubTables();
  }

  public ZcEbProj getOldProj() {
    return oldProj;
  }

  public FuncButton getEditButton() {
    return editButton;
  }

  public FuncButton getSaveButton() {
    return saveButton;
  }

  public String getPageStatus() {
    return pageStatus;
  }

  public void setPageStatus(String pageStatus) {
    this.pageStatus = pageStatus;
  }

  public boolean isZbBookRedoFlag() {
    return zbBookRedoFlag;
  }

  public void setZbBookRedoFlag(boolean zbBookRedoFlag) {
    this.zbBookRedoFlag = zbBookRedoFlag;
  }
}