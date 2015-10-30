package com.ufgov.zc.client.zc.project.control;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcEbProjectControlToTableModelConverter;
import com.ufgov.zc.client.component.DateField;
import com.ufgov.zc.client.component.JFuncPopupMenu;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.Menu.ConformityBidMenuItem;
import com.ufgov.zc.client.component.Menu.EcbjMenuItem;
import com.ufgov.zc.client.component.Menu.EvalControlButton;
import com.ufgov.zc.client.component.Menu.FinishBidMenuItem;
import com.ufgov.zc.client.component.Menu.FuncMenuButton;
import com.ufgov.zc.client.component.Menu.FuncMenuItem;
import com.ufgov.zc.client.component.Menu.OpenBidMenuItem;
import com.ufgov.zc.client.component.Menu.PauseBidMenuItem;
import com.ufgov.zc.client.component.Menu.RecoverBidMenuItem;
import com.ufgov.zc.client.component.Menu.RejectBidMenuItem;
import com.ufgov.zc.client.component.Menu.ShowBjButton;
import com.ufgov.zc.client.component.Menu.ShowBjMenuItem;
import com.ufgov.zc.client.component.Menu.ShowDetailbjMenuItem;
import com.ufgov.zc.client.component.Menu.ShowEvalComplResultDetalItem;
import com.ufgov.zc.client.component.Menu.ShowEvalComplResultMenuItem;
import com.ufgov.zc.client.component.Menu.ShowEvalResultButton;
import com.ufgov.zc.client.component.Menu.ShowEvalResultDetailMenuItem;
import com.ufgov.zc.client.component.Menu.ShowEvalScoreResultMenuItem;
import com.ufgov.zc.client.component.Menu.SumComplMenuItem;
import com.ufgov.zc.client.component.Menu.SumEvalResult;
import com.ufgov.zc.client.component.Menu.SumScoreMenuItem;
import com.ufgov.zc.client.component.Menu.TechnicalBidMenuItem;
import com.ufgov.zc.client.component.button.ChangeCgTypeButton;
import com.ufgov.zc.client.component.button.ComplCallBackButton;
import com.ufgov.zc.client.component.button.CreateEvalReportButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FormulaParamButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AutoNumFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.NewLineFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextAreaFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.util.GridBagLayoutTools;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcEbFormula;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.model.ZcEbPlan;
import com.ufgov.zc.common.zc.model.ZcEbProjPackVO;
import com.ufgov.zc.common.zc.model.ZcEbProjZbFile;
import com.ufgov.zc.common.zc.model.ZcEbSupplierPack;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbEcbjServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbEvalBidTeamServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbEvalServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbFormulaServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbPlanServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbProjChangeServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbSignupServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbSupplierServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbZbFileServiceDelegate;

public class ZcEbProjectControlSubEditPanel extends AbstractMainSubEditPanel {

  protected static final Logger logger = Logger.getLogger(ZcEbProjectControlSubEditPanel.class);

  public IZcEbFormulaServiceDelegate zcEbFormulaServiceDelegate = (IZcEbFormulaServiceDelegate) ServiceFactory.create(
    IZcEbFormulaServiceDelegate.class, "zcEbFormulaServiceDelegate");

  public IZcEbSignupServiceDelegate zcEbSignupServiceDelegate = (IZcEbSignupServiceDelegate) ServiceFactory.create(IZcEbSignupServiceDelegate.class,
    "zcEbSignupServiceDelegate");

  public IZcEbSupplierServiceDelegate zcEbSupplierServiceDelegate = (IZcEbSupplierServiceDelegate) ServiceFactory.create(
    IZcEbSupplierServiceDelegate.class, "zcEbSupplierServiceDelegate");

  public IZcEbEvalServiceDelegate zcEbEvalServiceDelegate = (IZcEbEvalServiceDelegate) ServiceFactory.create(IZcEbEvalServiceDelegate.class,
    "zcEbEvalServiceDelegate");

  public IZcEbEcbjServiceDelegate zcEbEcbjServiceDelegate = (IZcEbEcbjServiceDelegate) ServiceFactory.create(IZcEbEcbjServiceDelegate.class,
    "zcEbEcbjServiceDelegate");

  public IZcEbProjChangeServiceDelegate zcEbProjChangeServiceDelegate = (IZcEbProjChangeServiceDelegate) ServiceFactory.create(
    IZcEbProjChangeServiceDelegate.class, "zcEbProjChangeServiceDelegate");

  public IZcEbEvalBidTeamServiceDelegate zcEbEvalBidTeamServiceDelegate = (IZcEbEvalBidTeamServiceDelegate) ServiceFactory.create(
    IZcEbEvalBidTeamServiceDelegate.class, "zcEbEvalBidTeamServiceDelegate");

  public IZcEbPlanServiceDelegate zcEbPlanServiceDelegate = (IZcEbPlanServiceDelegate) ServiceFactory.create(IZcEbPlanServiceDelegate.class,
    "zcEbPlanServiceDelegate");

  public IZcEbZbFileServiceDelegate zcEbZbFileServiceDelegate = (IZcEbZbFileServiceDelegate) ServiceFactory.create(IZcEbZbFileServiceDelegate.class,
    "zcEbZbFileServiceDelegate");

  public IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,
    "zcEbBaseServiceDelegate");

  public IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,
    "baseDataServiceDelegate");

  private static final long serialVersionUID = -2539657260090189021L;

  public RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private FuncMenuItem currMenuItem = null;

  private FuncButton saveButton = new SaveButton();

  /**
   * ---------------------------评标控制菜单----------------------------
   */
  private FuncMenuButton evalControlButton = new EvalControlButton();//评标控制按钮

  private FuncMenuItem openBidMenuItem = new OpenBidMenuItem();

  private FuncMenuItem conformityBidMenuItem = new ConformityBidMenuItem();

  private FuncMenuItem technicalBidMenuItem = new TechnicalBidMenuItem();

  private FuncMenuItem ecbjMenuItem = new EcbjMenuItem();

  private FuncMenuItem rejectBidMenuItem = new RejectBidMenuItem();

  private FuncMenuItem pauseBidMenuItem = new PauseBidMenuItem();

  private FuncMenuItem recoverBidMenuItem = new RecoverBidMenuItem();

  private FuncMenuItem finishBidMenuItem = new FinishBidMenuItem();

  /**
   * ---------------------------汇总评审结果-------------------------
   */
  private FuncMenuButton sumEvalResultButton = new SumEvalResult();//汇总评审结果

  private FuncMenuItem sumComplMenuItem = new SumComplMenuItem();//汇总符合性评审结果

  private FuncMenuItem sumScorelMenuItem = new SumScoreMenuItem();//汇总技术打分评审结果

  //通用参数设置
  private FormulaParamButton paramSetButton = new FormulaParamButton();

  //生成评审报告
  private CreateEvalReportButton createEvalReportButton = new CreateEvalReportButton();

  /**
   * --------------------------查看供应商报价信息----------------------
   */
  private FuncMenuButton showBjButton = new ShowBjButton();//汇总评审结果

  private FuncMenuItem showBjMenuItem = new ShowBjMenuItem();//汇总符合性评审结果

  private FuncMenuItem showDetailbjMenuItem = new ShowDetailbjMenuItem();//汇总技术打分评审结果

  /**
   * --------------------------------查看专家评审结果菜单-----------------------------
   */
  private FuncMenuButton evalPrintButton = new ShowEvalResultButton();//打印供应商评审结果按钮。

  private FuncMenuItem scoreResultMenuItem = new ShowEvalScoreResultMenuItem();//打分汇总按钮

  private FuncMenuItem comlResutMenuItem = new ShowEvalComplResultMenuItem();//符合性汇总按钮

  private FuncMenuItem comlDetailMenuItem = new ShowEvalComplResultDetalItem();//符合性详细结果按钮

  private FuncMenuItem evalResultDetailMenuItem = new ShowEvalResultDetailMenuItem();//打分详细按钮

  //收回符合性评审结果
  private FuncButton complCallBackButton = new ComplCallBackButton();

  private FuncButton exitButton = new ExitButton();

  public FuncButton changeCgTypeButton = new ChangeCgTypeButton();//现场采购方式变更

  private ListCursor listCursor;

  private List<ZcEbSupplierPack> initSupplierPackList = new ArrayList<ZcEbSupplierPack>();

  private List<ZcEbSupplierPack> supplierPackList = new ArrayList<ZcEbSupplierPack>();

  public ZcEbProjectControlListPanel listPanel;

  public Date allowOpenBidDate = null;

  public ZcEbProjPackVO currVO = null;

  public ZcEbFormula zcEbFormula;

  List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

  public ZcEbProjectControlEditSubDialog parent;

  public ZcEbProjControlSubEditPanelService panelService;

  public ZcEbProjectControlSubEditPanel(ZcEbProjectControlEditSubDialog parent, ListCursor listCursor, String tabStatus,
    ZcEbProjectControlListPanel listPanel) {
    super(ZcEbPack.class, listPanel.getBillElementMeta());
    this.listCursor = listCursor;
    this.listPanel = listPanel;
    this.parent = parent;
    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "标段控制", TitledBorder.CENTER, TitledBorder.TOP,
      new Font("宋体", Font.BOLD, 15), Color.BLUE));
    this.colCount = 4;
    initFieldEditors();
    currVO = (ZcEbProjPackVO) this.listCursor.getCurrentObject();
    panelService = new ZcEbProjControlSubEditPanelService(this);
    init();
    initOpenBidDate();
    initFormula();
    refreshData();
  }

  private void initOpenBidDate() {
    String projCode = currVO.getProjCode();
    ElementConditionDto dto = new ElementConditionDto();
    dto.setProjCode(projCode);
    dto.setPackCode(currVO.getZcEbPack().getPackCode());
    dto.setNd(requestMeta.getSvNd());
    List plans = zcEbBaseServiceDelegate.queryDataForList("ZcEbPlan.getZcEbPackPlan", dto, requestMeta);
    if (plans != null && plans.size() > 0) {
      ZcEbPlan plan = (ZcEbPlan) plans.get(0);
      this.allowOpenBidDate = plan.getOpenBidTime();
      currVO.setZcEbPlan(plan);
    } else {
      JOptionPane.showMessageDialog(this, "对应的采购计划不存在，将无法确定标段开标时间，请检查数据一致性...", "提示", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  private void initFormula() {
    ElementConditionDto dto = new ElementConditionDto();
    dto.setPackCode(currVO.getZcEbPack().getPackCode());
    dto.setProjCode(currVO.getProjCode());
    zcEbFormula = zcEbFormulaServiceDelegate.getZcEbFormulaByPackCode(dto, requestMeta);
    if (zcEbFormula == null) {
//      JOptionPane.showMessageDialog(this, "评审方法没有设置！", "错误", JOptionPane.ERROR_MESSAGE);
      return;
    }
  }

  @Override
  public JComponent createSubBillPanel() {

    return panelService.createSubBillPanel();
  }

  ZcEbProjZbFile zbFile = null;

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {
    return editorList;
  }

  private void initFieldEditors() {
    AutoNumFieldEditor projDaima = new AutoNumFieldEditor("通知书编号", "zcEbPack.entrust.zcMakeCode");
    projDaima.setEnabled(false);
    editorList.add(projDaima);
    TextFieldEditor entrustCode = new TextFieldEditor("任务单号", "zcEbPack.entrust.snCode");
    entrustCode.setEnabled(false);
    editorList.add(entrustCode);
    TextFieldEditor projName = new TextFieldEditor("项目名称", "zcEbPack.entrust.zcMakeName");
    projName.setEnabled(false);
    editorList.add(projName);
    //      zcMoneyBiSum = new MoneyFieldEditor("项目预算", "zcEbPack.entrust.zcMoneyBiSum");
    //      zcMoneyBiSum.setEnabled(false);
    //      editorList.add(zcMoneyBiSum);
    TextFieldEditor packName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_NAME), "zcEbPack.packDesc");
    packName.setEnabled(false);
    editorList.add(packName);
    AsValFieldEditor status = new AsValFieldEditor("分包状态", "zcEbPack.status", "VS_ZC_PACK_STATUS");
    status.setEnabled(false);
    editorList.add(status);
    //      packBudget = new MoneyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_EB_FIELD_PACK_BUDGET), "zcEbPack.packBudget");
    //      packBudget.setEnabled(false);
    //      editorList.add(packBudget);
    AsValFieldEditor zcPifuCgfs = new AsValFieldEditor("采购方式", "zcEbPack.purType", "ZC_VS_PITEM_OPIWAY");
    zcPifuCgfs.setEnabled(false);
    editorList.add(zcPifuCgfs);
  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(ZcEbProjectControlListPanel.compoId);
    /**

     * 评标控制菜单

     */
    toolBar.add(evalControlButton);
    JFuncPopupMenu controlMenu = new JFuncPopupMenu("ZC", ZcEbProjectControlListPanel.compoId);
    controlMenu.add(openBidMenuItem);
    controlMenu.add(conformityBidMenuItem);
    controlMenu.add(technicalBidMenuItem);
    controlMenu.add(ecbjMenuItem);
    controlMenu.add(rejectBidMenuItem);
//    controlMenu.add(pauseBidMenuItem);
//    controlMenu.add(recoverBidMenuItem);
    controlMenu.add(finishBidMenuItem);
    evalControlButton.addMenu(controlMenu);

    toolBar.add(paramSetButton);
    toolBar.add(createEvalReportButton);

    /**

     * 汇总评审结果菜单

     */
    toolBar.add(sumEvalResultButton);
    JFuncPopupMenu sumEvalResultMenu = new JFuncPopupMenu("ZC", ZcEbProjectControlListPanel.compoId);
    sumEvalResultMenu.add(sumComplMenuItem);
    sumEvalResultMenu.add(sumScorelMenuItem);
    sumEvalResultButton.addMenu(sumEvalResultMenu);
    /**

     * 查看专家评审结果

     */
    toolBar.add(evalPrintButton);
    JFuncPopupMenu printMenu = new JFuncPopupMenu("ZC", ZcEbProjectControlListPanel.compoId);
    printMenu.add(comlDetailMenuItem);
    printMenu.add(comlResutMenuItem);
    printMenu.add(evalResultDetailMenuItem);
    printMenu.add(scoreResultMenuItem);
    evalPrintButton.addMenu(printMenu);
    /**

     * 查看供应商报价

     */
    toolBar.add(showBjButton);
    JFuncPopupMenu showBjMenu = new JFuncPopupMenu("ZC", ZcEbProjectControlListPanel.compoId);
    showBjMenu.add(showBjMenuItem);
    showBjMenu.add(showDetailbjMenuItem);
    showBjButton.addMenu(showBjMenu);
    toolBar.add(complCallBackButton);
    toolBar.add(changeCgTypeButton);
    toolBar.add(exitButton);
    addActionListener();

  }

  private void addActionListener() {
    openBidMenuItem.addActionListener(new ControlAction("doOpenBid", "开标", this));
    conformityBidMenuItem.addActionListener(new ControlAction("doConformityBid", "符合性评标", this));
    technicalBidMenuItem.addActionListener(new ControlAction("doTechnicalBid", "技术打分", this));
    ecbjMenuItem.addActionListener(new ControlAction("doEcbj", "二次报价", this));
    rejectBidMenuItem.addActionListener(new ControlAction("doRejectBid", "废标", this));
    pauseBidMenuItem.addActionListener(new ControlAction("doPauseBid", "暂停", this));
    recoverBidMenuItem.addActionListener(new ControlAction("doRecoverBid", "恢复", this));
    finishBidMenuItem.addActionListener(new ControlAction("doFinishBid", "结束评标", this));
    paramSetButton.addActionListener(new ControlAction("doSetParam", "设置评审参数", this));
    complCallBackButton.addActionListener(new ControlAction("doCallComplSum", "重置为符合性评审状态", this));

    sumComplMenuItem.addActionListener(new ControlAction("doComplSum", "汇总符合性评审结果", this));
    sumScorelMenuItem.addActionListener(new ControlAction("doScoreSum", "汇总评分性评审结果", this));

    scoreResultMenuItem.addActionListener(new ControlAction("doShowEvalResult", "评分汇总表", this));

    evalResultDetailMenuItem.addActionListener(new ControlAction("doShowEvalResultDetal", "显示专家评分表", this));

    showBjMenuItem.addActionListener(new ControlAction("doShowBjResult", "显示供应商报价信息", this));

    showDetailbjMenuItem.addActionListener(new ControlAction("doShowYlbResult", "显示供应商报价详细信息", this));

    changeCgTypeButton.addActionListener(new ControlAction("doChangeCgTypeButton", "变更采购方式", this));

    comlResutMenuItem.addActionListener(new ControlAction("doShowEvalComplResult", "显示符合性评审汇总表", this));

    comlDetailMenuItem.addActionListener(new ControlAction("doShowEvalComplResultDetal", "显示符合性评审详细结果表", this));

    createEvalReportButton.addActionListener(new ControlAction("doCreateEvalReport", "创建评审报告", this));
    exitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doExit();
      }
    });
  }

  /**
   * 检查当前标段是否进行了开标
   *
   * @return
   */
  private boolean toCheckHavingOpenBidProvider() {
    int openBidProviderNum = ((Integer) zcEbBaseServiceDelegate.queryObject("ZcEbOpenBid.getZcEbSuccessOpenBidNum", this.currVO.getZcEbPack()
      .getPackCode(), requestMeta)).intValue();
    if (openBidProviderNum == 0) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * 检查当前标段是否有供应商参与投标
   *
   * @return
   */
  public boolean toCheckHavingBidProvider() {
    //  if (joinBidSupplierList == null || joinBidSupplierList.size() < 1) {
    //    return false;
    //  } else {
    //    return true;
    //  }
    return true;
  }

  private void doExit() {
    //    if (isDataChanged()) {
    //      int num = JOptionPane.showConfirmDialog(this, "当前数据已被修改，是否保存？", "保存确认", 0);
    //      if (num == JOptionPane.YES_OPTION) {
    //        doUpdateIsSubmitBidDoc();
    //      }
    //    }
    parent.closeDialog();
  }

  /**
   * 根据不同的标段状态，供应商信息和专家评标信息应该不同。
   */
  public void refreshAll(ZcEbProjPackVO vo, boolean isRefreshButton) {
    this.listCursor.setCurrentObject(vo);
    this.currVO = vo;
    this.setEditingObject(vo);
    if (isRefreshButton) {
      setButtonStatus(vo.getZcEbPack(), requestMeta, this.listCursor);
    }
  }

  //  private void doUpdateIsSubmitBidDoc() {
  //    for (int i = 0; i < supplierPackList.size(); i++) {
  //      ZcEbSupplierPack sp = supplierPackList.get(i);
  //      ZcEbSignup signup = new ZcEbSignup();
  //      signup.setIsSubmitBidDoc(sp.getIsSubmitBidDoc());
  //      signup.setSignupId(sp.getSignupID());
  //      zcEbSignupServiceDelegate.updateZcEbSignupPropertyFN(signup, requestMeta);
  //      ZcEbSignupPackDetail signupDetail = new ZcEbSignupPackDetail();
  //      signupDetail.setIsSubmitBidDoc(sp.getIsSubmitBidDoc());
  //      signupDetail.setSignupId(sp.getSignupID());
  //      signupDetail.setSignupPackId(sp.getSignupPackID());
  //      zcEbSignupServiceDelegate.updateZcEbSignupPackPropertyFN(signupDetail, requestMeta);
  //    }
  //  }

  /**
   * 重新读取数据，刷新页面
   */
  private void refreshData() {
    String packCode = "";
    ZcEbPack cPack = (ZcEbPack) currVO.getZcEbPack();
    if (!cPack.getProjCode().equals(((ZcEbProjPackVO) this.listCursor.getCurrentObject()).getZcEbPack().getProjCode())) {
      cPack = ((ZcEbProjPackVO) this.listCursor.getCurrentObject()).getZcEbPack();
      currVO = (ZcEbProjPackVO) this.listCursor.getCurrentObject();
    } else {
      packCode = cPack.getPackCode() + "";
    }
    this.setEditingObject(currVO);

    panelService.refreshSubTable(cPack);
    this.updateChangeStatusButton(cPack);
    doUpdateButtonsEnable();
  }

  public void doUpdateButtonsEnable() {
    if (!toCheckHavingBidProvider()) {
      showBjButton.setEnabled(false);
      evalPrintButton.setEnabled(false);
      sumEvalResultButton.setEnabled(false);
      changeCgTypeButton.setEnabled(false);
    } else {
      showBjButton.setEnabled(true);
      evalPrintButton.setEnabled(true);
      sumEvalResultButton.setEnabled(true);
      evalControlButton.setEnabled(true);
      changeCgTypeButton.setEnabled(true);
    }
    //评标结束、废标、暂停、延期不能改采购方式
    if ("4".equals(currVO.getZcEbPack().getStatus()) || "5".equals(currVO.getZcEbPack().getStatus()) || "6".equals(currVO.getZcEbPack().getStatus())
      || "7".equals(currVO.getZcEbPack().getStatus())) {
      changeCgTypeButton.setEnabled(false);
      panelService.setEditStatus(false);
    }else{
      panelService.setEditStatus(true);
    }
  }

  List<AbstractFieldEditor> moreFieldEditorList;

  GridBagLayoutTools tool = null;

  public JPanel makeFieldEditorsPanel() {
    JPanel edPanel = new JPanel();
    moreFieldEditorList = new ArrayList<AbstractFieldEditor>();
    createMoreFieldEditorList(moreFieldEditorList);
    tool = null;
    tool = new GridBagLayoutTools();
    tool.setColCount(4);
    tool.setFieldEditorList(moreFieldEditorList);
    tool.layoutFieldEditorPanel(edPanel, ZcEbProjPackVO.class, "ZC_EB_PROJ_CTRL");
    tool.setCurrEditingObject(currVO);
    tool.setOldObject(currVO);
    edPanel.repaint();
    return edPanel;
  }

  private void createMoreFieldEditorList(List<AbstractFieldEditor> editorList) {
    if (editorList == null) {
      return;
    }
    TextFieldEditor projCode = new TextFieldEditor("项目编号", "projCode");
    projCode.setEnabled(false);
    editorList.add(projCode);
    //      AsValFieldEditor pjStatus = new AsValFieldEditor("项目状态", "zcEbProj.status", "ZC_VS_PROJ_STATUS");
    //      pjStatus.setEnabled(false);
    //      editorList.add(pjStatus);
    AsValFieldEditor purType = new AsValFieldEditor("采购方式", "zcEbPack.purType", "ZC_VS_PITEM_OPIWAY");
    purType.setEnabled(false);
    editorList.add(purType);
    TextFieldEditor buyerName = new TextFieldEditor("采购单位", "zcEbPack.entrust.coName");
    buyerName.setEnabled(false);
    editorList.add(buyerName);
    //      AsValFieldEditor isCheckQualification = new AsValFieldEditor("是否资格预审", "zcEbPack.isCheckQualification", "VS_Y/N");
    //      isCheckQualification.setEnabled(false);
    //      editorList.add(isCheckQualification);
    AsValFieldEditor analyseType = new AsValFieldEditor("评审方法", "zcEbPack.analyseType", "VS_ZC_ANALYSE_TYPE");
    analyseType.setEnabled(false);
    editorList.add(analyseType);
//    MoneyFieldEditor bidDeposit = new MoneyFieldEditor("投标保证金", "zcEbPack.bidDeposit");
//    bidDeposit.setEnabled(false);
//    editorList.add(bidDeposit);
    AsValFieldEditor isFailed = new AsValFieldEditor("是否失败包组", "zcEbPack.isFailed", "VS_Y/N");
    isFailed.setEnabled(false);
    editorList.add(isFailed);
    NewLineFieldEditor newLine = new NewLineFieldEditor();
    editorList.add(newLine);
    TextFieldEditor openBidAddr = new TextFieldEditor("开标地点", "zcEbPlan.openBidAddress", 3, false);
    openBidAddr.setEnabled(false);
    editorList.add(openBidAddr);
    DateFieldEditor openBidTime = new DateFieldEditor("开标时间", "zcEbPlan.openBidTime",DateField.TimeTypeH24);
    openBidTime.setEnabled(false);
    editorList.add(openBidTime);

    TextAreaFieldEditor packDesc = new TextAreaFieldEditor("标段说明", "zcEbPack.packDesc", 400, 3, 6);
    packDesc.setEnabled(false);
    editorList.add(packDesc);
    TextAreaFieldEditor failReason = new TextAreaFieldEditor("失败原因", "zcEbPack.failedReason", 400, 3, 6);
    editorList.add(failReason);
  }

  public void updateChangeStatusButton(ZcEbPack pack) {
    this.disableAllButtons();
    if (ZcSettingConstants.PACK_STATUS_DRAFT.equals(pack.getStatus())) {//等待开标
      if (panelService.joinBidSupplierList == null || panelService.joinBidSupplierList.size() == 0){
        this.currMenuItem = null;
      }else{
        this.openBidMenuItem.setEnabled(true);
        this.currMenuItem = this.openBidMenuItem;
      }
    } else if (ZcSettingConstants.PACK_STATUS_OPEN_BID.equals(pack.getStatus())) {//已经开标
      this.conformityBidMenuItem.setEnabled(true);
      this.currMenuItem = this.conformityBidMenuItem;
    } else if (ZcSettingConstants.PACK_STATUS_FU_HE_EVAL.equals(pack.getStatus())) {//符合性评标
      //如果是最低报价法，就不需要设置评审参数了
      if (ZcSettingConstants.FORMULA_FACTOR_LOWERPRICE_TYPE.equals(zcEbFormula.getFactorType())) {
        this.technicalBidMenuItem.setEnabled(false);
      } else {
        this.technicalBidMenuItem.setEnabled(true);
      }
      this.currMenuItem = this.technicalBidMenuItem;
      this.sumComplMenuItem.setEnabled(true);
      if (!ZcSettingConstants.PITEM_OPIWAY_GKZB.equals(pack.getPurType())) {
        this.ecbjMenuItem.setEnabled(true);
      }
      this.finishBidMenuItem.setEnabled(true);
    } else if (ZcSettingConstants.PACK_STATUS_TECH_EVAL.equals(pack.getStatus())) {//技术性评标
      this.complCallBackButton.setEnabled(true);
      this.finishBidMenuItem.setEnabled(true);
      this.sumScorelMenuItem.setEnabled(true);
      this.paramSetButton.setEnabled(true);
      if (!ZcSettingConstants.PITEM_OPIWAY_GKZB.equals(pack.getPurType())) {
        this.ecbjMenuItem.setEnabled(true);
      }
      this.currMenuItem = this.finishBidMenuItem;
    } else if (ZcSettingConstants.PACK_STATUS_EVAL_COMPLETE.equals(pack.getStatus())
      || ZcSettingConstants.PACK_STATUS_CANCEL.equals(pack.getStatus())) {//结束评标了，就不可以废标/暂停了
      this.rejectBidMenuItem.setEnabled(false);
      this.pauseBidMenuItem.setEnabled(false);
      this.recoverBidMenuItem.setEnabled(false);
      this.saveButton.setEnabled(false);
      this.ecbjMenuItem.setEnabled(false);
      this.createEvalReportButton.setEnabled(true);
      this.currMenuItem = null;
      ZcEbProjectControlToTableModelConverter.allowToModidy = false;
    } else if (ZcSettingConstants.PACK_STATUS_SUSPENDED.equals(pack.getStatus())) {//如果为暂停，那么只有恢复按钮可以使用
      this.recoverBidMenuItem.setEnabled(true);
      this.pauseBidMenuItem.setEnabled(false);
      this.saveButton.setEnabled(false);
      this.ecbjMenuItem.setEnabled(false);
      this.currMenuItem = this.recoverBidMenuItem;
      ZcEbProjectControlToTableModelConverter.allowToModidy = false;
    }
  }

  private void disableAllButtons() {
    this.conformityBidMenuItem.setEnabled(false);
    this.technicalBidMenuItem.setEnabled(false);
    this.finishBidMenuItem.setEnabled(false);
    this.openBidMenuItem.setEnabled(false);
    this.rejectBidMenuItem.setEnabled(true);
    this.pauseBidMenuItem.setEnabled(true);
    this.recoverBidMenuItem.setEnabled(false);
    this.saveButton.setEnabled(true);
    this.sumComplMenuItem.setEnabled(false);
    this.sumScorelMenuItem.setEnabled(false);
    this.complCallBackButton.setEnabled(false);
    this.ecbjMenuItem.setEnabled(false);
    this.paramSetButton.setEnabled(false);
    this.createEvalReportButton.setEnabled(false);
    ZcEbProjectControlToTableModelConverter.allowToModidy = true;
  }

}
