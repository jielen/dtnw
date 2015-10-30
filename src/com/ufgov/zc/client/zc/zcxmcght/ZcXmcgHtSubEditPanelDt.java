package com.ufgov.zc.client.zc.zcxmcght;

import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_FUND_CODE;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ORIGIN_CODE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;

import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.JGroupableTableHeader;
import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcHtPrePayBillToTableModelConverter;
import com.ufgov.zc.client.common.converter.zc.ZcXmcgHtToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.GkCommentDialog;
import com.ufgov.zc.client.component.GkCommentUntreadDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JSaveableSplitPane;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.AddButton;
import com.ufgov.zc.client.component.button.AgreeButton;
import com.ufgov.zc.client.component.button.AuditPassButton;
import com.ufgov.zc.client.component.button.CallbackButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.DisagreeButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.IsSendToNextButton;
import com.ufgov.zc.client.component.button.LoadMoldButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.PrintButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SaveSendButton;
import com.ufgov.zc.client.component.button.SelectMoldButton;
import com.ufgov.zc.client.component.button.SendButton;
import com.ufgov.zc.client.component.button.SendRecordButton;
import com.ufgov.zc.client.component.button.SendToProcurementUnitButton;
import com.ufgov.zc.client.component.button.SendToXieBanButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.button.SuggestAuditPassButton;
import com.ufgov.zc.client.component.button.TraceButton;
import com.ufgov.zc.client.component.button.UnauditButton;
import com.ufgov.zc.client.component.button.UntreadButton;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.celleditor.MoneyCellEditor;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.NumberCellRenderer;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecelleditor.FileCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsValCellRenderer;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AutoNumFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.FileFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.IntFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.WordFileUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.zcppromake.ZcBudgetHandler;
import com.ufgov.zc.client.zc.ztb.ChangeRMB;
import com.ufgov.zc.client.zc.ztb.activex.WordPane;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.WFConstants;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcPProBalConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.model.AsFile;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.system.util.Utils;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcEbAuditSheet;
import com.ufgov.zc.common.zc.model.ZcEbBulletinWordMold;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.model.ZcEbPackReq;
import com.ufgov.zc.common.zc.model.ZcEbProj;
import com.ufgov.zc.common.zc.model.ZcEbSupplier;
import com.ufgov.zc.common.zc.model.ZcHtPrePayBillItem;
import com.ufgov.zc.common.zc.model.ZcPProMake;
import com.ufgov.zc.common.zc.model.ZcPProMitem;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;
import com.ufgov.zc.common.zc.model.ZcTBchtItem;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.model.ZcXmcgHtBi;
import com.ufgov.zc.common.zc.model.ZcebZbItem;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbProjServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbSupplierServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcXmcgHtChgServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcXmcgHtServiceDelegate;

public class ZcXmcgHtSubEditPanelDt extends AbstractMainSubEditPanel {

  public static final String PATH = ZcUtil.dir + "ht/";

  private static final long serialVersionUID = -4923939712990784916L;

  private static final String CONTRACT_TEMPLETE_AS_FILE_ID_EXT = "_view";

  private static final Logger logger = Logger.getLogger(ZcXmcgHtSubEditPanelDt.class);

  protected String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  private JSaveableSplitPane myWordPane;

  //合同模板，真实客户模板file_id和给客户显示模板file_id的后缀对应
  protected RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  protected static String compoId = "ZC_XMCG_HT_SUB_DT";

  protected FuncButton saveButton = new SaveButton();

  protected FuncButton addButton = new AddButton();

  protected FuncButton editButton = new EditButton();

  private FuncButton traceButton = new TraceButton();

  protected FuncButton callbackButton = new CallbackButton();

  protected FuncButton deleteButton = new DeleteButton();

  private FuncButton previousButton = new PreviousButton();

  private FuncButton nextButton = new NextButton();

  private FuncButton exitButton = new ExitButton();

  protected FuncButton sendButton = new SendButton();

  protected LoadMoldButton loadMoldButton = new LoadMoldButton();

  protected SelectMoldButton selectMoldButton = new SelectMoldButton();

  public FuncButton isSendToNextButton = new IsSendToNextButton();

  //送备案
  protected FuncButton sendRecordButton = new SendRecordButton();

  //送采购单位确认
  private FuncButton sendToProcurementUnitButton = new SendToProcurementUnitButton();

  //同意

  private FuncButton agreeButton = new AgreeButton();

  //不同意
  private FuncButton disagreeButton = new DisagreeButton();

  //送协办人审核

  private FuncButton sendToXieBanButton = new SendToXieBanButton();

  public FuncButton printButton = new PrintButton();

  // 工作流填写意见审核通过
  protected FuncButton suggestPassButton = new SuggestAuditPassButton();

  // 工作流审核通过
  protected FuncButton auditPassButton = new AuditPassButton();

  // 工作流销审
  protected FuncButton unAuditButton = new UnauditButton();

  // 工作流退回
  protected FuncButton unTreadButton = new UntreadButton();

  protected ListCursor<ZcXmcgHt> listCursor;

  private ZcXmcgHt oldZcXmcgHt;

  public ZcXmcgHtSubListPanelDt listPanel;

  protected JTablePanel biTablePanel = new JTablePanel();
  protected JTablePanel makeBiTablePanel = new JTablePanel();

  protected JTablePanel itemTablePanel = new JTablePanel();

  protected JTablePanel detailTablePanel = new JTablePanel();

  protected ZcXmcgHtSubEditPanelDt self = this;

  protected GkBaseDialog parent;

  private ElementConditionDto dtoForBidWinner = new ElementConditionDto();

  private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  protected com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor biddingWinner = null;

  //  private com.ufgov.zc.client.component.zc.fieldeditor.foreignentity.ForeignEntityFieldEditor zcSuName = null;

  private BillElementMeta mainBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd("ZC_XMCG_HT");

  private BillElementMeta biBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd("ZC_XMCG_HT_BI");

  private BillElementMeta itemBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd("ZC_T_BCHT_ITEM");

  protected IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,

  "zcEbBaseServiceDelegate");

  public IZcXmcgHtServiceDelegate zcXmcgHtServiceDelegate = (IZcXmcgHtServiceDelegate) ServiceFactory.create(IZcXmcgHtServiceDelegate.class,

  "zcXmcgHtServiceDelegate");
  
  public IZcEbProjServiceDelegate projService=(IZcEbProjServiceDelegate)ServiceFactory.create(IZcEbProjServiceDelegate.class, "zcEbProjServiceDelegate");

  protected IZcXmcgHtChgServiceDelegate zcXmcgHtChgServiceDelegate = (IZcXmcgHtChgServiceDelegate) ServiceFactory.create(
    IZcXmcgHtChgServiceDelegate.class,

    "zcXmcgHtChgServiceDelegate");

  protected WordPane wordPane = new WordPane();

  private AsFile file = new AsFile();

  private String wordFileId;

  protected ElementConditionDto getDto = new ElementConditionDto();

  private BigDecimal bai = new BigDecimal("100");


  private ElementConditionDto htDto;
  

  protected ElementConditionDto getBiDto = new ElementConditionDto();
  
  //

  private IZcEbSupplierServiceDelegate zcEbSupplierServiceDelegate = (IZcEbSupplierServiceDelegate) ServiceFactory.create(

  IZcEbSupplierServiceDelegate.class, "zcEbSupplierServiceDelegate");

  //资金表合计值
  private JLabel biSummaryLabel;

//  protected boolean isCar = false;

  public ZcXmcgHtSubEditPanelDt(GkBaseDialog parent, ListCursor<ZcXmcgHt> listCursor, String tabStatus, ZcXmcgHtSubListPanelDt listPanel,String compoId) {

    super(ZcXmcgHt.class, BillElementMeta.getBillElementMetaWithoutNd(compoId));

    this.compoId=compoId;
    
    this.listCursor = listCursor;

    this.listPanel = listPanel;

    this.parent = parent;

    this.colCount = 4;

    init();

    requestMeta.setCompoId(getCompoId());

    wordPane.addPropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, new PropertyChangeListener() {

      public void propertyChange(PropertyChangeEvent evt) {

        //打开文件完成之后的回调函数
        boolean isSuccess = (Boolean) evt.getNewValue();

        if (isSuccess) {

          //下面一句是为了打开word后刷新窗口
          self.parent.setSize(self.parent.getSize().width - 1, self.parent.getSize().height - 1);

        }

      }

    });

    refreshData();

  }

  /**
   * 是否补充合同,主合同不需要操作指标，补充合同需要操作指标,补充合同的edit界面，这个方法要重写，返回true
   */
  protected boolean isSubHt() {
    return true;
  }

  protected void init() {

    this.initToolBar(toolBar);

    this.setLayout(new BorderLayout());

    this.add(toolBar, BorderLayout.NORTH);

    initFieldEditorPanel(ZcXmcgHt.class, getMainBillElementMeta());

    JComponent tabTable = createSubBillPanel();

    fieldEditorPanel.setMinimumSize(new Dimension(540, 150));

    tabTable.setMinimumSize(new Dimension(240, 300));

    myWordPane = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, fieldEditorPanel, tabTable);

    myWordPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LangTransMeta.translate(getCompoId()),
      TitledBorder.CENTER, TitledBorder.TOP,

      new Font("宋体", Font.BOLD, 15), Color.BLUE));

    myWordPane.setDividerDefaultLocation(this.getClass().getName() + "_splitPane_dividerLocation_top", 150);

    myWordPane.setContinuousLayout(true);

    myWordPane.setOneTouchExpandable(true);

    myWordPane.setDividerSize(5);

    myWordPane.setDividerLocation(200);

    myWordPane.setBackground(self.getBackground());

    this.add(myWordPane, BorderLayout.CENTER);

  }

  protected JTablePanel[] getSubTables() {

    return new JTablePanel[] { biTablePanel, itemTablePanel, detailTablePanel };

  }

  public List<AbstractFieldEditor> createFieldEditors() {

    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    TextFieldEditor zcMakeName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_NAME), "zcPProMake.zcMakeName");

    MoneyFieldEditor zcMoneyBiSum = new MoneyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MONEY_BI_SUM),

    "zcPProMake.zcMoneyBiSum");

    String columNames[] = { "项目代码", "采购项目", "供应商名称", "采购方式" };

    ZcEbProjFnHandler projHandler = new ZcEbProjFnHandler(columNames, self);

    ElementConditionDto elementCondtiontDto = new ElementConditionDto();

    elementCondtiontDto.setBillStatus("exec");

    //elementCondtiontDto.setCoCodeFilter(requestMeta.getSvUserID());

    elementCondtiontDto.setCoCode(requestMeta.getSvCoCode());

    ForeignEntityFieldEditor zcMakeCode = new ForeignEntityFieldEditor(getProjectSqlId(), elementCondtiontDto, 20, projHandler,

    columNames, "采购计划编号", "zcMakeCode");

    zcMakeCode.setEditable(false);

    zcMakeName.setEnabled(false);

    zcMoneyBiSum.setEnabled(false);

    AsValFieldEditor zcProjType = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FUKUAN_TYPE), "zcCgLeixing",

    "ZC_VS_FUKUAN_TYPE");

    zcProjType.setEnabled(false);

    AutoNumFieldEditor zcHtCode = new AutoNumFieldEditor("补充合同代码", "zcHtCode");

    TextFieldEditor zcHtName = new TextFieldEditor("补充合同名称", "zcHtName");

    AsValFieldEditor zcHtStatus = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_STATUS), "zcHtStatus",

    getHtStatus());

    zcHtStatus.setEnabled(false);

//    zcHtStatus.setVisible(false);

    CompanyFieldEditor zcCoCode = new CompanyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_NAME), "coCode");

    zcCoCode.setEnabled(false);

    IntFieldEditor zcCoCodeNd = new IntFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_CODE_ND), "nd", 4);

    zcCoCodeNd.setEnabled(false);

    if (isGys()) {

      zcCoCodeNd.setVisible(false);

      zcMoneyBiSum.setVisible(false);

    }

    getBiddingWinner();

    ZcXmcgHt ht = (ZcXmcgHt) this.listCursor.getCurrentObject();

    TextFieldEditor zcSuBankName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_SU_BANK_NAME), "zcSuBankName");
    TextFieldEditor zcSuAccCode = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_SU_ACC_CODE), "zcSuAccCode");

    TextFieldEditor zcSuCode = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_SU_BANK_CODE), "zcSuBankCode");

    TextFieldEditor zcSuLinkman = new TextFieldEditor("联系人", "zcSuLinkman");

    zcSuLinkman.setVisible(false);

    //      getZcSuNameAndOthers(zcSuBankName, zcSuAccCode, zcSuCode, zcSuLinkman);

    TextFieldEditor zcSuName = new TextFieldEditor("中标供应商", "zcSuName");

    zcSuName.setEnabled(false);

    //补充合同和采购合同去掉银行账号，名称
    //      editorList.add(zcSuBankName);
    //
    //      editorList.add(zcSuAccCode);

    MoneyFieldEditor zcHtNum = new MoneyFieldEditor(getHtNumLabel(), "zcHtNum");

    //添加工期和附件
    DateFieldEditor zcProLimitStartDate = new DateFieldEditor("工期开始时间", "zcProLimitStartDate");
    DateFieldEditor zcProLimitEndDate = new DateFieldEditor("工期结束时间", "zcProLimitEndDate");
    FileFieldEditor zcImpFile = new FileFieldEditor("电子附件", "zcImpFile", "zcImpFileBlobid");

    AsValFieldEditor zcFukuanType = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.ZC_PAY_TYPE), "zcFukuanType", "ZC_VS_PAY_TYPE");

    DateFieldEditor zcSgnDate = new DateFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_SGN_DATE), "zcSgnDate");

    TextFieldEditor zcDeliveryDate = new TextFieldEditor("交货日期", "zcDeliveryDate");

    TextFieldEditor zcDeliveryType = new TextFieldEditor("交货方式", "zcDeliveryType");

    TextFieldEditor zcDeliveryAddr = new TextFieldEditor("交货地点", "zcDeliveryAddr");

    TextFieldEditor zcMemo = new TextFieldEditor("备注信息", "zcMemo");
    DateFieldEditor zcInputDate = new DateFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_INPUT_DATE), "zcInputDate");
    zcInputDate.setEnabled(false);


    TextFieldEditor pName = new TextFieldEditor("主合同名称", "pName");

    MoneyFieldEditor pBiSum = new MoneyFieldEditor("主合同金额", "pBiSum");

    String coluNames[] = { "主合同代码", "主合同名称", "主合同金额","采购单位","供应商" };

    ZcPHtHandler htHandler = new ZcPHtHandler(coluNames);

    htDto = new ElementConditionDto();
    if(ZcUtil.isGys()){
      htDto.setZcText4(requestMeta.getSvUserID());
    }
    if(ZcUtil.isYsdw()){
      htDto.setCoCode(requestMeta.getSvCoCode());
    }
    ForeignEntityFieldEditor pCode = new ForeignEntityFieldEditor("ZC_XMCG_HT.getMainHtByMakeCode", htDto, 20, htHandler,

    coluNames, "选择主合同", "zcParhtCode");


    pName.setEnabled(false);
    pBiSum.setEnabled(false);

    editorList.add(pCode);
    editorList.add(pName);
    editorList.add(pBiSum);
    editorList.add(zcCoCode);

    editorList.add(zcHtCode);
    editorList.add(zcHtName);
    editorList.add(zcHtNum);
    editorList.add(zcSuName);
    
    editorList.add(zcSgnDate);
    editorList.add(zcDeliveryAddr);
    editorList.add(zcDeliveryDate);
    editorList.add(zcDeliveryType);

    editorList.add(zcImpFile);
    editorList.add(zcMemo);
    editorList.add(zcInputDate);
    editorList.add(zcHtStatus);
    
    
    
    
    return editorList;
  }

  public boolean isCgzx() {
    // TODO Auto-generated method stub
    return ZcUtil.isCgzx();
  }

  protected void getBiddingWinner() {

    String columNames2[] = { "分包编号", LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_NAME) };

    BiddingWinnerHandler handler2 = new BiddingWinnerHandler(columNames2, self);

    dtoForBidWinner.setStatus("exec");

    dtoForBidWinner.setProjectCode("默认设置为该不可能的中标号");

    biddingWinner = new com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor("selectPage.selectPackForCaiGouHt", dtoForBidWinner,

    20, handler2, columNames2, LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_NAME), "zcBidContent") {

      private static final long serialVersionUID = -7737549222488261602L;

      public Object getValue() {

        return this.field.getText();

      }

    };

    biddingWinner.setEnabled(false);

  }

  //  private void getZcSuNameAndOthers(TextFieldEditor zcSuBankName, TextFieldEditor zcSuAccCode, TextFieldEditor zcSuCode, TextFieldEditor zcSuLinkman) {
  //
  //    String columNames[] = { "供应商代码", "供应商名称", "联系人", "银行名称", "银行账号" };
  //
  //    Object editors[] = { zcSuCode, zcSuLinkman, zcSuBankName, zcSuAccCode };
  //
  //    ElementConditionDto elementCondtiontDto = new ElementConditionDto();
  //
  //    elementCondtiontDto.setBillStatus("exec");
  //
  //    zcSuName = new com.ufgov.zc.client.component.zc.fieldeditor.foreignentity.ForeignEntityFieldEditor("ZcEbSupplier.getSimpleZcEbSupplier",
  //
  //    elementCondtiontDto, columNames, editors, "中标供应商", "zcSuName") {
  //
  //      private static final long serialVersionUID = 1L;
  //
  //      public void afterSelect() {
  //
  //      };
  //
  //    };
  //
  //    zcSuName.setEnabled(false);
  //
  //  }

  protected void loadCommonEditors(List<AbstractFieldEditor> editorList) {

    TextFieldEditor zcMakeName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_NAME), "zcPProMake.zcMakeName");

    MoneyFieldEditor zcMoneyBiSum = new MoneyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MONEY_BI_SUM),

    "zcPProMake.zcMoneyBiSum");

    String columNames[] = { "项目代码", "采购项目", "供应商名称", "采购方式" };

    ZcEbProjFnHandler projHandler = new ZcEbProjFnHandler(columNames, self);

    ElementConditionDto elementCondtiontDto = new ElementConditionDto();

    elementCondtiontDto.setBillStatus("exec");

    //elementCondtiontDto.setCoCodeFilter(requestMeta.getSvUserID());

    elementCondtiontDto.setCoCode(requestMeta.getSvCoCode());

    ForeignEntityFieldEditor zcMakeCode = new ForeignEntityFieldEditor(getProjectSqlId(), elementCondtiontDto, 20, projHandler,

    columNames, "采购计划编号", "zcMakeCode");

    zcMakeCode.setEditable(false);

    zcMakeName.setEnabled(false);

    zcMoneyBiSum.setEnabled(false);

    editorList.add(zcMakeCode);

    editorList.add(zcMakeName);

    editorList.add(zcMoneyBiSum);

    AsValFieldEditor zcProjType = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FUKUAN_TYPE), "zcCgLeixing",

    "ZC_VS_FUKUAN_TYPE");

    zcProjType.setEnabled(false);

    editorList.add(zcProjType);

    AutoNumFieldEditor zcHtCode = new AutoNumFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_CODE), "zcHtCode");

    editorList.add(zcHtCode);

    TextFieldEditor zcHtName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_NAME), "zcHtName");

    editorList.add(zcHtName);

    AsValFieldEditor zcHtStatus = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_STATUS), "zcHtStatus",

    getHtStatus());

    zcHtStatus.setEnabled(false);

    zcHtStatus.setVisible(false);

    editorList.add(zcHtStatus);

    CompanyFieldEditor zcCoCode = new CompanyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_CODE), "coCode");

    zcCoCode.setEnabled(false);

    editorList.add(zcCoCode);

    IntFieldEditor zcCoCodeNd = new IntFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_CODE_ND), "nd", 4);

    zcCoCodeNd.setEnabled(false);

    editorList.add(zcCoCodeNd);

    if (isGys()) {

      zcCoCodeNd.setVisible(false);

      zcMoneyBiSum.setVisible(false);

    }

  }

  public void zcMakeCodeChange() {

    ZcXmcgHt zcXmcgHt = (ZcXmcgHt) this.listCursor.getCurrentObject();

    String zcMakeCode = zcXmcgHt.getZcMakeCode();

    dtoForBidWinner.setProjectCode(zcMakeCode);
    ZcPProMake zcPProMake = getZcPProMakeByZcMakeCode(zcMakeCode);
    zcXmcgHt.setZcPProMake(zcPProMake);
    zcXmcgHt.setZcMakeCode(zcMakeCode);
    zcXmcgHt.setZcCoCode(zcPProMake.getCoCode());
    //    zcXmcgHt.setNd(zcPProMake.getNd());
    zcXmcgHt.setZcFukuanType(zcPProMake.getZcFukuanType());
    List<ZcTBchtItem> itemHtList = new ArrayList<ZcTBchtItem>();

    List<ZcXmcgHtBi> biHtList = this.getBiList(zcXmcgHt);

    List<ZcHtPrePayBillItem> payBiList = new ArrayList<ZcHtPrePayBillItem>();

    //带出商品内容
    if(ZcUtil.isYsdw()){
      List<ZcPProMitem> itemMakeList = self.listPanel.ZcPProMakeServiceDelegate.getZcPProMitem(zcMakeCode, requestMeta);
      if (itemMakeList != null) {
        buildZcTBchtItems(itemMakeList, itemHtList);
        for (Iterator iterator = itemHtList.iterator(); iterator.hasNext();) {
          ZcTBchtItem zcTBchtItem = (ZcTBchtItem) iterator.next();
          zcTBchtItem.setZcItemSum(BigDecimal.ZERO);
        }
        zcXmcgHt.setItemList(itemHtList);
      }
    }else if(ZcUtil.isGys() || ZcUtil.isCgzx()){
      buildHtItems(zcXmcgHt);
    }

    if (zcXmcgHt.getZcSuCode() != null) {

      ZcEbSupplier supp = zcEbSupplierServiceDelegate.getSupplierById(zcXmcgHt.getZcSuCode(), requestMeta);

      zcXmcgHt.setZcSuLinkman(supp.getLinkMan());

      zcXmcgHt.setZcSuTel(supp.getPhone());

      zcXmcgHt.setZcGnw(supp.getGnw());

      zcXmcgHt.setZcSnw(supp.getSnw());

      zcXmcgHt.setZcIsZxqyZb(supp.getIsZxqy());

    }


    zcXmcgHt.setBiList(biHtList);
    zcXmcgHt.setPayBiList(payBiList);
    setEditingObject(zcXmcgHt);

    self.refreshSubData(biHtList, zcXmcgHt.getItemList(), payBiList);

    listCursor.setCurrentObject(zcXmcgHt);
    
    updateFieldEditorsEditable();

  }

  private void buildHtItems(ZcXmcgHt zcXmcgHt) {
    // TODO Auto-generated method stub
    // get the project
    if(zcXmcgHt.getProjCode()==null)return;
    ZcEbProj proj=projService.getZcEbProjByProjCode(zcXmcgHt.getProjCode(), requestMeta);
    if(proj==null||proj.getPackList()==null)return;
    ArrayList detailIdLst=new ArrayList();
    for(int i=0;i<proj.getPackList().size();i++){
      ZcEbPack pack=(ZcEbPack) proj.getPackList().get(i);
      proj.setPurType(pack.getPurType());
      if(pack.getPackCode().equals(zcXmcgHt.getZcBidCode())){
        if(pack.getRequirementDetailList()==null)continue;
        for(int j=0;j<pack.getRequirementDetailList().size();j++){
          ZcEbPackReq req=(ZcEbPackReq)pack.getRequirementDetailList().get(j);
          detailIdLst.add(req.getDetailCode());
        }
      }
    }
    //获取分包对应的需求明细
    ElementConditionDto dto=new ElementConditionDto();
    dto.setPmAdjustCodeList(detailIdLst);
    List reqDetailLst=zcEbBaseServiceDelegate.queryDataForList("ZcEbProj.getReqDetailByDetailCodes", dto, requestMeta);
    dto.setZcText0(zcXmcgHt.getZcSuCode());
    List xjbjLst=zcEbBaseServiceDelegate.queryDataForList("ZC_EB_XUNJIA_BAOJIA.getBaojiaDetailByDetailCodes", dto, requestMeta);
    zcXmcgHt.setItemList(new ArrayList());
    for (int i=0;i<reqDetailLst.size();i++) {
      HashMap map=(HashMap) reqDetailLst.get(i);
      ZcTBchtItem htItem = new ZcTBchtItem();
      htItem.setZcSpName((String) map.get("NAME"));//商品名称
      htItem.setZcCatalogueCode((String) map.get("ZC_CATALOGUE_CODE"));//品目代码
      htItem.setZcCatalogueName((String) map.get("ZC_CATALOGUE_NAME"));//品目名称NUM

      //如果是询价的话，获取供应商的询价报价单
      if(ZcSettingConstants.PITEM_OPIWAY_XJ.equalsIgnoreCase(proj.getPurType())){
        setBjDetail(htItem,(BigDecimal)map.get("DETAIL_CODE"),xjbjLst);
      }else{
        BigDecimal t1=getBigDecimal(map.get("NUM"));
        htItem.setZcCaigShl(t1);
        BigDecimal t2=getBigDecimal(map.get("MER_PRICE"));
        htItem.setZcCaigPrice(t2);
        BigDecimal t3=t1.multiply(t2);
        htItem.setZcCaigMoney(t3);
      }
      htItem.setZcMerSpec((String) map.get("BASE_REQ"));
      htItem.setZcItemVal(BigDecimal.ZERO);
      htItem.setZcHtBiMoney(new BigDecimal(0.00));
      htItem.setZcHtOtherMoney(new BigDecimal(0.00));
      htItem.setZcHtGkMoney(new BigDecimal(0.00));
      zcXmcgHt.getItemList().add(htItem);
    }
  }

  private void setBjDetail(ZcTBchtItem htItem, BigDecimal detailCode, List xjbjLst) {
    // TODO Auto-generated method stub
    for (int i=0;i<xjbjLst.size();i++) {
      HashMap map=(HashMap) xjbjLst.get(i);
      String xjCode=(String) map.get("XJ_CODE");      
      if(detailCode.compareTo(new BigDecimal(xjCode))==0){
        BigDecimal t1=getBigDecimal(map.get("SP_NUM"));
        BigDecimal t2=getBigDecimal(map.get("SP_PRICE"));
        BigDecimal t3=t1.multiply(t2);
        htItem.setZcCaigMoney(t3);
        htItem.setZcCaigShl(t1);
        htItem.setZcCaigPrice(t2);
        htItem.setZcCaigJldw((String) map.get("UNIT"));
      }
    }
  }

  private BigDecimal getBigDecimal(Object object) {
    // TODO Auto-generated method stub
    BigDecimal t=new BigDecimal(0);
    if(object==null) return null;
    try {
      if(object instanceof String){
        t=new BigDecimal((String)object);
      }else if(object instanceof Integer){
        t=new BigDecimal((Integer)object);
      }else if(object instanceof Long){
        t=new BigDecimal((Long)object);
      }else if(object instanceof Double){
        t=new BigDecimal((Double)object);
      }else if(object instanceof BigDecimal){
        return (BigDecimal)object;
      }else{
        return null;
      }
    } catch (Exception e) {
      // TODO: handle exception
      logger.error(e.getMessage(),e);
      return null;
    }
    return t;
  }

  private List<ZcXmcgHtBi> getBiList(ZcXmcgHt zcXmcgHt) {

    if (!isSubHt()) {
      ElementConditionDto dto = new ElementConditionDto();
      dto.setZcText0(zcXmcgHt.getZcMakeCode());
      List<ZcPProMitemBi> biMakeList = self.listPanel.ZcPProMakeServiceDelegate.getMitemBiWithHtBi(dto, requestMeta);
      Map param = new HashMap();
      param.put("ZC_HT_CODE", zcXmcgHt.getZcHtCode());
      for (int i = 0; i < biMakeList.size(); i++) {
        param.put("ZC_PRO_BI_SEQ", biMakeList.get(i).getZcProBiSeq());
        BigDecimal sum = (BigDecimal) this.zcEbBaseServiceDelegate.queryObject("ZC_XMCG_HT_BI.getCanUseMoney", param, this.requestMeta);
        biMakeList.get(i).setZcBiDoSum(sum);
      }
      List<ZcXmcgHtBi> biHtList = new ArrayList<ZcXmcgHtBi>();
      buildZcXmcgHtBi(biMakeList, biHtList, zcXmcgHt.getZcMakeCode());
      return biHtList;
    } else {
      return zcXmcgHt.getBiList();
    }
  }

  /**

   * 根据计划编号查找对应的计划

   * @param makeCode

   * @return

   */

  private ZcPProMake getZcPProMakeByZcMakeCode(String makeCode) {

    ZcPProMake zcPProMake = self.listPanel.ZcPProMakeServiceDelegate.selectByPrimaryKey(makeCode, requestMeta);

    return zcPProMake;

  }

  /**

   * 根据项目编号，分包编号，查找对应的需求确认明细，做为合同明细使用

   * @param projCode

   * @param packCode

   * @return

   */

  private List<ZcPProMitem> getRequirementDetail(String projCode, String packCode) {

    Map<String, String> param = new HashMap<String, String>();

    param.put("proj_code", projCode);

    param.put("pack_code", packCode);

    List<ZcPProMitem> itemMakeList = zcEbBaseServiceDelegate.queryDataForList("ZC_P_PRO_MITEM.selectHtMx", param, requestMeta);

    return itemMakeList;

  }

  /**

   * 根据项目编号，分包编号，查找对应的批办单数据

   * @param projCode

   * @param packCode

   * @return

   */

  private ZcEbAuditSheet getZcEbAuditSheetByProjCodePackCode(String projCode, String packCode) {

    ElementConditionDto wwdto = new ElementConditionDto();

    wwdto.setProjCode(projCode);

    wwdto.setPackCode(packCode);

    List<ZcEbAuditSheet> wwLis = zcEbBaseServiceDelegate.getForeignEntitySelectedData("ZcEbGuiDang.selectSheetList", wwdto, requestMeta);

    if (wwLis != null && wwLis.size() > 0) {

      return (ZcEbAuditSheet) wwLis.get(0);

    }

    return null;

  }

  private void loadMoreCommonEditors(List<AbstractFieldEditor> editorList) {
    //添加工期和附件
    DateFieldEditor zcProLimitStartDate = new DateFieldEditor("工期开始时间", "zcProLimitStartDate");
    DateFieldEditor zcProLimitEndDate = new DateFieldEditor("工期结束时间", "zcProLimitEndDate");
    FileFieldEditor zcImpFile = new FileFieldEditor("电子附件", "zcImpFile", "zcImpFileBlobid");

    editorList.add(zcProLimitStartDate);
    editorList.add(zcProLimitEndDate);
    editorList.add(zcImpFile);

    AsValFieldEditor zcFukuanType = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.ZC_PAY_TYPE), "zcFukuanType", "ZC_VS_PAY_TYPE");

    editorList.add(zcFukuanType);

    DateFieldEditor zcSgnDate = new DateFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_SGN_DATE), "zcSgnDate");

    editorList.add(zcSgnDate);

    DateFieldEditor zcDeliveryDate = new DateFieldEditor("交货日期", "zcDeliveryDate");

    editorList.add(zcDeliveryDate);

    TextFieldEditor zcDeliveryType = new TextFieldEditor("交货方式", "zcDeliveryType");

    editorList.add(zcDeliveryType);

    TextFieldEditor zcDeliveryAddr = new TextFieldEditor("交货地点", "zcDeliveryAddr");

    editorList.add(zcDeliveryAddr);

    //    FileFieldEditor fieldFileName = new FileFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CON_TEXT), "zcConText",

    //      "zcConTextBlobid");

    //    editorList.add(fieldFileName);

    TextFieldEditor zcMemo = new TextFieldEditor("备注信息", "zcMemo");

    editorList.add(zcMemo);

  }

  public static void buildZcTBchtItems(List<ZcPProMitem> itemMakeList, List<ZcTBchtItem> itemHtList) {

    for (ZcPProMitem item : itemMakeList) {

      ZcTBchtItem htItem = new ZcTBchtItem();

      htItem.setZcSpName(item.getZcPitemName());//商品名称

      htItem.setZcCatalogueCode(item.getZcCatalogueCode());//品目代码

      htItem.setZcCatalogueName(item.getZcCatalogueName());//品目名称

      htItem.setZcItemSum(item.getZcItemSum() == null ? BigDecimal.ZERO : item.getZcItemSum());

      htItem.setBudgetBiMoney(item.getBudgetBiMoney() == null ? BigDecimal.ZERO : item.getBudgetBiMoney());

      htItem.setBudgetOtherMoney(item.getBudgetOtherMoney() == null ? BigDecimal.ZERO : item.getBudgetOtherMoney());
      htItem.setZcItemVal(BigDecimal.ZERO);

      htItem.setZcHtBiMoney(new BigDecimal(0.00));

      htItem.setZcHtOtherMoney(new BigDecimal(0.00));

      htItem.setZcHtGkMoney(new BigDecimal(0.00));

      htItem.setZcCaigShl(item.getZcCaigNum());

      //品牌
      htItem.setZcBraName(item.getZcBraName());

      htItem.setZcMerSpec(item.getZcBaseGgyq());

      htItem.setZcCaigPrice(item.getZcMerPrice());

      htItem.setZcCaigMoney(item.getZcItemSum());

      itemHtList.add(htItem);

    }

  }

  public static void buildZcXmcgHtBi(List<ZcPProMitemBi> biMakeList, List<ZcXmcgHtBi> biHtList, String zcMakeCode) {

    for (ZcPProMitemBi bi : biMakeList) {

      ZcXmcgHtBi htBi = new ZcXmcgHtBi();
      htBi.setZcPProMitemBi(bi);

      //      htBi.getZcPProMitemBi().setZcBiJhuaSum(bi.getZcBiJhuaSum());
      //
      //      htBi.getZcPProMitemBi().setZcBiUsedSum(bi.getZcBiUsedSum());
      //
      //      htBi.getZcPProMitemBi().setZcBiDoSum(bi.getZcBiDoSum());
      //
      //      //      htBi.getZcPProMitemBi().setFundCode(bi.getFundCode());  
      //
      //      htBi.getZcPProMitemBi().setOriginCode(bi.getOriginCode());
      //
      //      htBi.getZcPProMitemBi().setPaytypeCode(bi.getPaytypeCode());
      //
      //      htBi.getZcPProMitemBi().setPaytypeName(bi.getPaytypeName());
      //
      //      htBi.getZcPProMitemBi().setSenddocCode(bi.getSenddocCode());
      //
      //      htBi.getZcPProMitemBi().setSenddocName(bi.getSenddocName());
      //
      //      htBi.getZcPProMitemBi().setZcProBiSeq(bi.getZcProBiSeq());
      //      htBi.getZcPProMitemBi().setZcBiNo(bi.getZcBiNo());
      //      htBi.getZcPProMitemBi().setZcUseBiId(bi.getZcUseBiId());

      htBi.setZcProBiSeq(bi.getZcProBiSeq());

      htBi.setZcBiBcsySum(BigDecimal.ZERO);

      htBi.setZcCanUseSum(bi.getZcBiDoSum());
      htBi.setZcBiSum(bi.getZcBiSum());

      //      // 预算单位
      //      htBi.getZcPProMitemBi().setCoCode(bi.getCoCode());
      //      htBi.getZcPProMitemBi().setCoName(bi.getCoName());
      //      // 资金性质
      //      htBi.getZcPProMitemBi().setFundCode(bi.getFundCode());
      //      htBi.getZcPProMitemBi().setFundName(bi.getFundName());
      //      // 功能分类
      //      htBi.getZcPProMitemBi().setbAccCode(bi.getbAccCode());
      //      htBi.getZcPProMitemBi().setbAccName(bi.getbAccName());
      //      // 项目类别
      //      htBi.getZcPProMitemBi().setProjectTypeCode(bi.getProjectTypeCode());
      //      htBi.getZcPProMitemBi().setProjectTypeName(bi.getProjectTypeName());
      //
      //      // 指标来源
      //      htBi.getZcPProMitemBi().setOriginCode(bi.getOriginCode());
      //      htBi.getZcPProMitemBi().setOriginName(bi.getOriginName());
      //      // 年度
      //      htBi.getZcPProMitemBi().setNd(bi.getNd());
      //
      //      // 业务处室
      //      htBi.getZcPProMitemBi().setOrgCode(bi.getOrgCode());
      //      htBi.getZcPProMitemBi().setOrgName(bi.getOrgName());

      biHtList.add(htBi);

    }

  }

  public JComponent createSubBillPanel() {
    final JTabbedPane topTabPane = new JTabbedPane();


    this.createSubInfo();
    
    _createBiTalbePanel();

    _createItemTablePanel();

    JTabbedPane biTabPane = new JTabbedPane();

    biTabPane.addTab("使用新资金", biTablePanel);
    biTabPane.addTab("使用原采购计划资金", makeBiTablePanel);
    
    JTabbedPane itemTabPane = new JTabbedPane();

    itemTabPane.addTab("商品构成", itemTablePanel);

   

    biTabPane.setMinimumSize(new Dimension(240, 150));

    itemTabPane.setMinimumSize(new Dimension(240, 300));

    JSaveableSplitPane splitPane = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, biTabPane, itemTabPane);

    splitPane.setDividerDefaultLocation(this.getClass().getName() + "_splitPane_dividerLocation", 150);

    splitPane.setContinuousLayout(true);

    if (isGys()) {

      biTabPane.setVisible(false);

      splitPane.setOneTouchExpandable(false);

    } else {

      splitPane.setOneTouchExpandable(true);

    }

    // 只显示向下的箭头

    //    splitPane.putClientProperty("toExpand", true);

    splitPane.setDividerSize(10);

    //    splitPane.setDividerLocation(260);

    splitPane.setBackground(self.getBackground());

    topTabPane.addTab("合同明细", splitPane);

    topTabPane.addChangeListener(new ChangeListener() {

      public void stateChanged(ChangeEvent e) {

        int index = topTabPane.getSelectedIndex();

        if (index == 1) {

          ZcXmcgHt ht = (ZcXmcgHt) self.listCursor.getCurrentObject();

          if (ht.getZcConTextBlobid() != null && wordPane.getOpenFile() == null) {

            ZcEbBulletinWordMold w = new ZcEbBulletinWordMold();

            w.setFileID(ht.getZcConTextBlobid());

            doOpenMold(null, w);

          }

        }

      }

    });
    //    topTabPane.addTab("付款明细", detailTablePanel);

    //    topTabPane.addTab("合同文本", wordPane);
    if (isGys()) {
      return itemTabPane;
    }
    return topTabPane;

  }

  private void _createItemTablePanel() {
    // TODO Auto-generated method stub
    itemTablePanel.init();

    itemTablePanel.setPanelId(this.getClass().getName() + "_itemTablePanel");

    itemTablePanel.getSearchBar().setVisible(false);

    itemTablePanel.setTablePreferencesKey(this.getClass().getName() + "_itemTable");

    itemTablePanel.getTable().setShowCheckedColumn(true);

    itemTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));


    JGroupableTableHeader itemTableHeader = itemTablePanel.getTable().getTableHeader();

    itemTableHeader.addColumnGroup("采购预算资金", new String[] { "ZC_ITEM_SUM", "BUDGET_BI_MONEY", "BUDGET_OTHER_MONEY" });

    itemTableHeader.addColumnGroup("实际采购资金", new String[] { "ZC_ITEM_VAL", "ZC_HT_BI_MONEY", "ZC_HT_OTHER_MONEY" });

    JFuncToolBar bottomToolBar2 = new JFuncToolBar();

    FuncButton addBtn2 = new SubaddButton(false);

    JButton insertBtn2 = new SubinsertButton(false);

    JButton delBtn2 = new SubdelButton(false);

    bottomToolBar2.add(addBtn2);

    bottomToolBar2.add(insertBtn2);

    bottomToolBar2.add(delBtn2);

    itemTablePanel.add(bottomToolBar2, BorderLayout.SOUTH);

    addBtn2.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcTBchtItem zcTBchtItem = new ZcTBchtItem();

        zcTBchtItem.setTempId(Guid.genID());

        int rowNum = addSub(itemTablePanel, zcTBchtItem);

        itemTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    insertBtn2.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcTBchtItem zcTBchtItem = new ZcTBchtItem();

        zcTBchtItem.setTempId(Guid.genID());

        int rowNum = insertSub(itemTablePanel, zcTBchtItem);

        itemTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    delBtn2.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        deleteSub(itemTablePanel);

      }

    });
  }

  private void _createBiTalbePanel() {
    // TODO Auto-generated method stub

    _createMakeBiTablePanel();
    
    _createNewBiTablePanel();
    
  }

  private void _createNewBiTablePanel() {
    // TODO Auto-generated method stub

    biTablePanel.init();

    biTablePanel.getSearchBar().setVisible(false);

    biTablePanel.setTablePreferencesKey(this.getClass().getName() + "_biTable");

    biTablePanel.getTable().setShowCheckedColumn(true);

    biTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    JFuncToolBar bottomToolBar1 = new JFuncToolBar();

    FuncButton addBtn1 = new SubaddButton(false);

    JButton insertBtn1 = new SubinsertButton(false);

    JButton delBtn1 = new SubdelButton(false);

    bottomToolBar1.add(addBtn1);

    bottomToolBar1.add(insertBtn1);

    bottomToolBar1.add(delBtn1);

    biSummaryLabel = new JLabel("合计");

    biSummaryLabel.setName("_SUM_LABEL");

    biSummaryLabel.setForeground(Color.black);

    biSummaryLabel.setFont(new Font("宋体", Font.BOLD, 12));

    JPanel p = new JPanel();

    p.setBackground(new Color(99, 184, 255));

    p.setName("_SUM_PANEL");

    p.add(biSummaryLabel, BorderLayout.CENTER, -1);

/*    if (isSubHt()) {
      JPanel pp=new JPanel();
      pp.setLayout(new BorderLayout());
      pp.add(bottomToolBar1,BorderLayout.WEST);
      pp.add(p,BorderLayout.CENTER);
      biTablePanel.add(pp, BorderLayout.SOUTH);
    }else{
      biTablePanel.add(p, BorderLayout.SOUTH);
    }*/
    biTablePanel.add(bottomToolBar1, BorderLayout.SOUTH);
    addBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcXmcgHtBi zcXmcgHtBi = new ZcXmcgHtBi();

        zcXmcgHtBi.setTempId(Guid.genID());

        setItemBiDefaultValue(zcXmcgHtBi);

        int rowNum = addSub(biTablePanel, zcXmcgHtBi);

        biTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    insertBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcXmcgHtBi zcXmcgHtBi = new ZcXmcgHtBi();

        zcXmcgHtBi.setTempId(Guid.genID());

        setItemBiDefaultValue(zcXmcgHtBi);

        int rowNum = insertSub(biTablePanel, zcXmcgHtBi);

        biTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    delBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        Integer[] checkedRows = deleteBiSub(biTablePanel);

        // 从新计算采购预算

        if (checkedRows.length > 0) {

          self.caculateMoney(((BeanTableModel<ZcXmcgHtBi>) biTablePanel.getTable().getModel()).getDataBeanList());

        }

      }

    });
  }

  private void _createMakeBiTablePanel() {
    // TODO Auto-generated method stub

    makeBiTablePanel.init();

    makeBiTablePanel.getSearchBar().setVisible(false);

    makeBiTablePanel.setTablePreferencesKey(this.getClass().getName() + "_makebiTable");

    makeBiTablePanel.getTable().setShowCheckedColumn(true);

    makeBiTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    JFuncToolBar bottomToolBar1 = new JFuncToolBar();

    FuncButton addBtn1 = new SubaddButton(false);

    JButton insertBtn1 = new SubinsertButton(false);

    JButton delBtn1 = new SubdelButton(false);

    bottomToolBar1.add(addBtn1);

    bottomToolBar1.add(insertBtn1);

    bottomToolBar1.add(delBtn1);

    biSummaryLabel = new JLabel("合计");

    biSummaryLabel.setName("_SUM_LABEL");

    biSummaryLabel.setForeground(Color.black);

    biSummaryLabel.setFont(new Font("宋体", Font.BOLD, 12));

    JPanel p = new JPanel();

    p.setBackground(new Color(99, 184, 255));

    p.setName("_SUM_PANEL");

    p.add(biSummaryLabel, BorderLayout.CENTER, -1);

/*    if (isSubHt()) {
      JPanel pp=new JPanel();
      pp.setLayout(new BorderLayout());
      pp.add(bottomToolBar1,BorderLayout.WEST);
      pp.add(p,BorderLayout.CENTER);
      biTablePanel.add(pp, BorderLayout.SOUTH);
    }else{
      biTablePanel.add(p, BorderLayout.SOUTH);
    }*/
    makeBiTablePanel.add(bottomToolBar1, BorderLayout.SOUTH);
    addBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcXmcgHtBi zcXmcgHtBi = new ZcXmcgHtBi();

        zcXmcgHtBi.setTempId(Guid.genID());

        setItemBiDefaultValue(zcXmcgHtBi);

        int rowNum = addSub(makeBiTablePanel, zcXmcgHtBi);

        makeBiTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    insertBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcXmcgHtBi zcXmcgHtBi = new ZcXmcgHtBi();

        zcXmcgHtBi.setTempId(Guid.genID());

        setItemBiDefaultValue(zcXmcgHtBi);

        int rowNum = insertSub(makeBiTablePanel, zcXmcgHtBi);

        makeBiTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    delBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        Integer[] checkedRows = deleteBiSub(makeBiTablePanel);

        // 从新计算采购预算

        if (checkedRows.length > 0) {

          self.caculateMoney(((BeanTableModel<ZcXmcgHtBi>) makeBiTablePanel.getTable().getModel()).getDataBeanList());

        }

      }

    });
  }

  private void createSubInfo() {

    detailTablePanel.init();

    detailTablePanel.getSearchBar().setVisible(false);

    detailTablePanel.setTablePreferencesKey(this.getClass().getName() + "_biTable");

    detailTablePanel.getTable().setShowCheckedColumn(true);

    detailTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    //      jTabbedPane.addTab("付款明细", detailTablePanel);
    JFuncToolBar bottomToolBar1 = new JFuncToolBar();

    FuncButton addBtn1 = new SubaddButton(false);

    FuncButton insertBtn1 = new SubinsertButton(false);

    FuncButton delBtn1 = new SubdelButton(false);

    bottomToolBar1.add(addBtn1);

    bottomToolBar1.add(insertBtn1);

    bottomToolBar1.add(delBtn1);

    detailTablePanel.add(bottomToolBar1, BorderLayout.SOUTH);

    addBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcHtPrePayBillItem detail = new ZcHtPrePayBillItem();

        detail.setTempId(Guid.genID());

        setdetailBiDefaultValue(detail);

        int rowNum = addSub(detailTablePanel, detail);

        detailTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    insertBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcHtPrePayBillItem detail = new ZcHtPrePayBillItem();

        detail.setTempId(Guid.genID());

        setdetailBiDefaultValue(detail);

        int rowNum = insertSub(detailTablePanel, detail);

        detailTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    delBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        Integer[] checkedRows = deleteSub(detailTablePanel);

      }

    });
  }

  protected void refreshSubData(List biList) {
    detailTablePanel.setTableModel(ZcHtPrePayBillToTableModelConverter.convertSubBiTableData(biList, true));
    ZcUtil.translateColName(detailTablePanel.getTable(), ZcHtPrePayBillToTableModelConverter.getBillDetailInfo());
    //    setOldObject();
    setTableEditor(detailTablePanel.getTable());
    detailTablePanel.repaint();
    addItemTableLisenter(detailTablePanel.getTable());
  }

  private void setTableEditor(JTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());
    SwingUtil.setTableCellEditor(table, "PAY_MONEY", new MoneyCellEditor());
    SwingUtil.setTableCellRenderer(table, "PAY_MONEY", new NumberCellRenderer());
    SwingUtil.setTableCellEditor(table, "PAY_YEAR", new AsValComboBoxCellEditor("ZC_PAY_YEAR"));
    SwingUtil.setTableCellRenderer(table, "PAY_YEAR", new AsValCellRenderer("ZC_PAY_YEAR"));
    SwingUtil.setTableCellEditor(table, "PAY_MONTH", new AsValComboBoxCellEditor("ZC_PAY_MONTH"));
    SwingUtil.setTableCellRenderer(table, "PAY_MONTH", new AsValCellRenderer("ZC_PAY_MONTH"));
    SwingUtil.setTableCellEditor(table, "STATUS", new AsValComboBoxCellEditor("ZC_VS_PAY_BILL_ITEM_STATUS"));
    SwingUtil.setTableCellRenderer(table, "STATUS", new AsValCellRenderer("ZC_VS_PAY_BILL_ITEM_STATUS"));
  }

  protected void setdetailBiDefaultValue(ZcHtPrePayBillItem detail) {
    detail.setPayYear(this.requestMeta.getSvNd() + "");
    detail.setPayMonth("01");
  }

  public void setItemBiDefaultValue(ZcXmcgHtBi zcXmcgHtBi) {

    zcXmcgHtBi.getZcPProMitemBi().setFundCode("3");//其他

    zcXmcgHtBi.getZcPProMitemBi().setOriginCode("4");//其他

    zcXmcgHtBi.getZcPProMitemBi().setPaytypeCode("3");//自行支付        
    if (this.isSubHt()) {
      zcXmcgHtBi.setPaytypeCode("A");
      zcXmcgHtBi.setFundCode("A");
    }

  }

  protected void caculateMoney(List<ZcXmcgHtBi> biList) {

    BigDecimal sum = BigDecimal.ZERO;

    BigDecimal htSum = BigDecimal.ZERO;

    for (ZcXmcgHtBi bi : biList) {

      sum = sum.add((BigDecimal) ObjectUtils.defaultIfNull(bi.getZcPProMitemBi().getZcBiJhuaSum(), new BigDecimal("0.00")));

      htSum = htSum.add((BigDecimal) ObjectUtils.defaultIfNull(bi.getZcBiBcsySum(), new BigDecimal("0.00")));

    }

    ZcXmcgHt beanData = (ZcXmcgHt) this.listCursor.getCurrentObject();

/*    if (sum.compareTo(beanData.getZcPProMake().getZcMoneyBiSum()) != 0) {

      beanData.setZcHtNum(htSum);

    }*/
    setSumLabelText();
    this.setEditingObject(beanData);

  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(getCompoId());

    toolBar.add(editButton);

    toolBar.add(saveButton);

    toolBar.add(sendButton);
    //
    //    toolBar.add(saveAndSendButton);
    //
    toolBar.add(suggestPassButton);
    //
    //    toolBar.add(auditPassButton);
    //
    //    toolBar.add(isSendToNextButton);
    //
    //    toolBar.add(sendToProcurementUnitButton);
    //
    //    toolBar.add(agreeButton);
    //
    //    toolBar.add(disagreeButton);
    //
    //    toolBar.add(sendToXieBanButton);
    //
    //    toolBar.add(sendRecordButton);
    //
    //    toolBar.add(unAuditButton);

    toolBar.add(unTreadButton);

    toolBar.add(callbackButton);

    toolBar.add(deleteButton);
    //
    //    toolBar.add(selectMoldButton);
    //
    //    toolBar.add(loadMoldButton);

    toolBar.add(printButton);

    toolBar.add(traceButton);

    toolBar.add(previousButton);

    toolBar.add(nextButton);

    toolBar.add(exitButton);

    loadMoldButton.setEnabled(false);

    selectMoldButton.setEnabled(false);

    editButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doEdit();

      }

    });

    sendButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doSend();

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

    saveButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doSave();

      }

    });

    deleteButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doDelete();

      }

    });

    traceButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doTrace();

      }

    });

    callbackButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        doCallback();

      }

    });

    auditPassButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        stopTableEditing();

        // 审核

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

        // 销审

        doUnAudit();

      }

    });

    unTreadButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 退回

        doUnTread();

      }

    });


    loadMoldButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doLoadMoldButton();

      }

    });

    printButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doPrintButton();

      }

    });

    isSendToNextButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doSendNext();

      }

    });

    sendToProcurementUnitButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 送采购单位确认
        doSendToProcurementUnit();

      }

    });

    sendRecordButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 送采购处备案

        doSendRecord();

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

  }

  /**

   * 送协办人

   */

  private void doSendToXieBan() {

    ZcXmcgHt ht = (ZcXmcgHt) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

    requestMeta.setFuncId(this.sendToXieBanButton.getFuncId());

    executeAudit(ht, ZcSettingConstants.IS_GOON_AUDIT_NO, null);

  }

  /**

   * 同意

   */

  private void doAgree() {

    ZcXmcgHt ht = (ZcXmcgHt) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

    requestMeta.setFuncId(this.agreeButton.getFuncId());

    String auditFlag = ht.getZcConText();

    if (auditFlag == null || "".equals(auditFlag)) {

      auditFlag = "0";

    }

    auditFlag = String.valueOf(ZcUtil.getAuditFlagValue(Integer.valueOf(auditFlag), 0, requestMeta));

    executeAudit(ht, Integer.valueOf(auditFlag) + ZcSettingConstants.IS_GOON_AUDIT_YES, null);

  }

  /**

   * 不同意

   */

  private void doDisagree() {

    ZcXmcgHt ht = (ZcXmcgHt) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

    requestMeta.setFuncId(this.disagreeButton.getFuncId());

    String auditFlag = ht.getZcConText();

    if (auditFlag == null || "".equals(auditFlag)) {

      auditFlag = "0";

    }

    auditFlag = String.valueOf(ZcUtil.getAuditFlagValue(Integer.valueOf(auditFlag), 1, requestMeta));

    executeAudit(ht, Integer.valueOf(auditFlag) - ZcSettingConstants.IS_GOON_AUDIT_YES, ZcSettingConstants.AUDIT_DISAGREE_DEFULT_MESSAGE);

  }

  /**

   * 送采购处备案

   */

  private void doSendRecord() {

    ZcXmcgHt ht = (ZcXmcgHt) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

    requestMeta.setFuncId(this.sendRecordButton.getFuncId());

    executeAudit(ht, ZcSettingConstants.IS_GOON_AUDIT_NO, null);

  }

  /**

   * 送采购单位确认

   */

  private void doSendToProcurementUnit() {

    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    if (!calcBeforeSave()) {

      return;

    }

    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),

    ModalityType.APPLICATION_MODAL);

    if (commentDialog.cancel) {

      return;

    }

    boolean success = true;

    ZcXmcgHt afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.sendToProcurementUnitButton.getFuncId());

      ZcXmcgHt ht = (ZcXmcgHt) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      ht.setZcConText("1");

      ht.setComment(commentDialog.getComment());

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      afterSaveBill = zcXmcgHtServiceDelegate.sendToProcurementUnitFN(ht, requestMeta);

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

  private void doSendNext() {

    requestMeta.setFuncId(this.isSendToNextButton.getFuncId());

    ZcXmcgHt ht = (ZcXmcgHt) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

    executeAudit(ht, ZcSettingConstants.IS_GOON_AUDIT_YES, null);

  }

  protected void executeAudit(ZcXmcgHt ht, int isGoonAudit, String defaultMsg) {

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

    if (isSubHt() && ZcUtil.isYsdw()) {
      makeToHtBi(ht);
    }
    boolean success = true;

    String errorInfo = "";

    try {

      ht.setZcConText(String.valueOf(isGoonAudit));

      ht.setComment(commentDialog.getComment());

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      zcXmcgHtServiceDelegate.updateZcXmcgHtFN(ht, isSubHt(), WorkEnv.getInstance().getWebRoot(), requestMeta);

      zcXmcgHtServiceDelegate.auditFN(ht, requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      this.refreshData();

      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "审核失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  private void doPrintButton() {

    try {

      this.wordPane.print();

    } catch (RuntimeException e) {

      // TODO Auto-generated catch block

      e.printStackTrace();

      JOptionPane.showMessageDialog(this, "请先切换至合同文本页签，并确认合同文本加载完毕，再进行打印！", "提示", JOptionPane.INFORMATION_MESSAGE);

    }

  }

  protected void updateFieldEditorsEditable() {

    ZcXmcgHt ht = (ZcXmcgHt) listCursor.getCurrentObject();

    Long processInstId = ht.getProcessInstId();

    if (processInstId != null && processInstId.longValue() > 0) {

      // 工作流的单据

      wfCanEditFieldMap = BillElementMeta.getWfCanEditField(ht, requestMeta);

      if ("cancel".equals(this.oldZcXmcgHt.getZcHtStatus())) {// 撤销单据设置字段为不可编辑

        wfCanEditFieldMap = null;

      }

      for (AbstractFieldEditor editor : fieldEditors) {

        // 工作流中定义可编辑的字段

        if (wfCanEditFieldMap != null && wfCanEditFieldMap.containsKey(Utils.getDBColNameByFieldName(editor.getEditObject(), editor.getFieldName()))) {

          isEdit = true;
          this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;

          editor.setEnabled(true);

        } else {

          editor.setEnabled(false);

        }

      }
      //工作流中该节点选中了保存按钮可用，则当前状态当前人可用编辑
      if (saveButton.isVisible() && saveButton.isEnabled()) {
        isEdit = true;
        this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;

      }

    } else {
      isEdit = true;
      for (AbstractFieldEditor editor : fieldEditors) {
        if (pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT) || pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {
          if ("pName".equals(editor.getFieldName())
            || "zcParhtCode".equals(editor.getFieldName())
            ||"zcHtNum".equals(editor.getFieldName())
            || "zcProLimitStartDate".equals(editor.getFieldName()) || "zcProLimitEndDate".equals(editor.getFieldName())
            || "zcImpFile".equals(editor.getFieldName()) || "zcSgnDate".equals(editor.getFieldName())
            || "zcDeliveryDate".equals(editor.getFieldName()) || "zcDeliveryType".equals(editor.getFieldName())
            || "zcDeliveryAddr".equals(editor.getFieldName()) || "zcMemo".equals(editor.getFieldName()) || "zcHtName".equals(editor.getFieldName())) {
            editor.setEnabled(true);
          }else if(isCar()&&ZcUtil.isYsdw() && ("zcSuName".equals(editor.getFieldName())||"zcHtNum".equals(editor.getFieldName()))){
            editor.setEnabled(true);
          }else {         
            editor.setEnabled(false);
          }
        } else {
          editor.setEnabled(false);
        }
      }
    }

    setWFSubTableEditable(biTablePanel, isEdit);

    setWFSubTableEditable(itemTablePanel, isEdit);

  }

  protected boolean isCar() {
    // TODO Auto-generated method stub

    ZcXmcgHt bean = (ZcXmcgHt) this.listCursor.getCurrentObject();
    if(bean!=null && bean.getZcPProMake()!=null && "Y".equalsIgnoreCase(bean.getZcPProMake().getIsCar())){
      return true;
    }
    return false;
  }

  private void doEdit() {

    this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;

    updateFieldEditorsEditable();

    setButtonStatus();

  }

  protected void doSend() {

    boolean success = true;

    ZcXmcgHt afterSaveBill = null;

    if (this.isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    try {

      requestMeta.setFuncId(this.sendButton.getFuncId());

      ZcXmcgHt ht = (ZcXmcgHt) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      afterSaveBill = zcXmcgHtServiceDelegate.newCommitFN(ht, false, requestMeta);

    } catch (Exception ex) {

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      this.refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "送审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    }

  }

  protected void doSuggestPass() {

    if (ZcUtil.isYsdw() && !calcBeforeSave()) {

      return;

    }

    ZcXmcgHt ht = (ZcXmcgHt) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

    requestMeta.setFuncId(this.suggestPassButton.getFuncId());

    String jianShenRoleId = AsOptionMeta.getOptVal("OPT_ZC_CGZX_JSKY_ROLE");//监审组员角色

    if (WorkEnv.getInstance().containRole(jianShenRoleId)) {//如果是监审员，则不修改审批状态

      String auditFlag = ht.getZcConText();

      auditFlag = auditFlag == null ? "0" : auditFlag;

      executeAudit(ht, Integer.valueOf(auditFlag), null);

    } else {

      executeAudit(ht, ZcSettingConstants.IS_GOON_AUDIT_NO, null);

    }

  }

  /*

   * 审核

   */

  protected void doAudit() {

    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    if (checkBeforeSave()) {

      return;

    }

    boolean success = true;

    ZcXmcgHt afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.auditPassButton.getFuncId());

      ZcXmcgHt ht = (ZcXmcgHt) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      afterSaveBill = zcXmcgHtServiceDelegate.auditFN(ht, requestMeta);

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

   * 销审

   */

  protected void doUnAudit() {
    /**
     * 合同备案，销审添加校验,做了支付申请确认单和验收单的，不让销审。
     * 
     */
    ZcXmcgHt ht = (ZcXmcgHt) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
    List billlist = zcEbBaseServiceDelegate.queryDataForList("ZC_HT_PRE_PAY_BILL.abatorgenerated_selectByHtCode", ht.getZcHtCode(), requestMeta);

    if (billlist != null && billlist.size() > 0) {
      JOptionPane.showMessageDialog(this, "该合同已经做了支付申请确认单，不允许销审！", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    List yanShouBillList = zcEbBaseServiceDelegate.queryDataForList("ZcEbYanShou.getEbYanShouBillByPackCode", ht.getZcHtCode(), requestMeta);
    if (yanShouBillList != null && yanShouBillList.size() > 0) {
      JOptionPane.showMessageDialog(this, "该合同已经做了验收单，不允许销审！", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    boolean success = true;

    ZcXmcgHt afterSaveBill = null;

    String errorInfo = "";

    int i = JOptionPane.showConfirmDialog(this, "是否确定消审？", "确认", JOptionPane.INFORMATION_MESSAGE);

    if (i != 0) {

      return;

    }

    try {

      requestMeta.setFuncId(unAuditButton.getFuncId());

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      afterSaveBill = zcXmcgHtServiceDelegate.unAuditFN(ht, requestMeta);

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

  /*

   * 退回

   */

  protected void doUnTread() {

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

    ZcXmcgHt afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(unTreadButton.getFuncId());

      ZcXmcgHt ht = (ZcXmcgHt) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      ht.setComment(commentDialog.getComment());

      ht.setZcConText("0");

      zcXmcgHtServiceDelegate.updateZcXmcgHtFN(ht, false, WorkEnv.getInstance().getWebRoot(), requestMeta);

      afterSaveBill = zcXmcgHtServiceDelegate.untreadFN(ht, requestMeta);

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

   * 流程跟踪

   */

  private void doTrace() {

    ZcBaseBill bean = (ZcBaseBill) this.listCursor.getCurrentObject();

    if (bean == null) {

      return;

    }

    ZcUtil.showTraceDialog(bean, getCompoId());

  }

  /*

   * 收回

   */

  protected void doCallback() {

    boolean success = true;

    ZcXmcgHt afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.callbackButton.getFuncId());

      ZcXmcgHt ht = (ZcXmcgHt) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      ht.setZcConText("0");

      zcXmcgHtServiceDelegate.updateZcXmcgHtFN(ht, false, WorkEnv.getInstance().getWebRoot(), requestMeta);

      afterSaveBill = zcXmcgHtServiceDelegate.callbackFN(ht, requestMeta);

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

  protected void doPrevious() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave()) {

          return;

        }

      } else {

        listCursor.setCurrentObject(oldZcXmcgHt);

      }

    }

    listCursor.previous();

    refreshData();

  }

  protected void doNext() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave()) {

          return;

        }

      } else {

        listCursor.setCurrentObject(oldZcXmcgHt);

      }

    }

    listCursor.next();

    refreshData();

  }

  public void doExit() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave()) {

          return;

        }

      }

    }
    if (this.wordPane != null && wordPane.isDocOpened()) {
      wordPane.closeNotSave();
    }

    this.parent.dispose();

  }


  private void doLoadMoldButton() {

    if (checkBeforeSave()) {

      return;

    }

    if (wordPane != null) {

      wordPane.closeNotSave();

    }

    ZcXmcgHt zcXmcgHt = (ZcXmcgHt) this.listCursor.getCurrentObject();

    if (wordFileId == null || wordFileId.equals(zcXmcgHt.getZcConTextBlobid())) {

      JOptionPane.showMessageDialog(this, "请先选择加载模板,再进行填充！", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    String money = zcXmcgHt.getZcHtNum() == null ? "" : zcXmcgHt.getZcHtNum().toString();

    String chineseMoney = ChangeRMB.doChange(money);

    zcXmcgHt.setZcHtNumString(chineseMoney);

    String companyName = zcXmcgHtServiceDelegate.getCompaneyName(zcXmcgHt.getCoCode(), requestMeta);

    zcXmcgHt.setCompanyName(companyName);

    String actFileId = wordFileId.substring(0, wordFileId.length() - CONTRACT_TEMPLETE_AS_FILE_ID_EXT.length());

    file = zcXmcgHtServiceDelegate.getContractContent(zcXmcgHt, requestMeta, actFileId);

    file.setFileName(PATH + actFileId + ".doc");

    // 创建临时文件

    boolean isSucceed = WordFileUtil.createFile(PATH, file.getFileName(), this, file.getFileContent());

    if (isSucceed) {

      wordPane.open(file.getFileName());

    }

  }

  protected boolean calcBeforeSave() {

    ZcXmcgHt zcXmcgHt = (ZcXmcgHt) this.listCursor.getCurrentObject();

    String errorinfo = Calc(zcXmcgHt);

    if (errorinfo != null && !"".equals(errorinfo)) {

      //errorinfo = errorinfo +;

      JOptionPane.showMessageDialog(this, errorinfo, "提示", JOptionPane.ERROR_MESSAGE);

      return false;

    } else {

      return true;

    }

  }

  protected String Calc(ZcXmcgHt zcXmcgHt) {

    StringBuffer sb = new StringBuffer();

    List<ZcXmcgHtBi> biList = zcXmcgHt.getBiList();

    BigDecimal shiJiTotal = BigDecimal.ZERO;//实际合计

    BigDecimal jiHuaTotal = BigDecimal.ZERO;//预算合计

    BigDecimal czxsy = BigDecimal.ZERO;//财政性资金

    BigDecimal qtsy = BigDecimal.ZERO;//其它资金

    BigDecimal czxys = BigDecimal.ZERO;//财政性资金

    BigDecimal qtys = BigDecimal.ZERO;//其它资金

    for (Iterator iterator = biList.iterator(); iterator.hasNext();) {

      ZcXmcgHtBi zcXmcgHtBi = (ZcXmcgHtBi) iterator.next();

      BigDecimal benciShiYongSum = zcXmcgHtBi.getZcBiBcsySum();//本次使用金额

      benciShiYongSum = benciShiYongSum == null ? BigDecimal.ZERO : benciShiYongSum;

      if (!isSubHt()) {
        zcXmcgHtBi.setZcBiNo(zcXmcgHtBi.getZcPProMitemBi().getZcBiNo());
      }

      if (zcXmcgHtBi.getZcBiNo() != null && !"".equals(zcXmcgHtBi.getZcBiNo())) {
        if (!isSubHt() && benciShiYongSum.compareTo(zcXmcgHtBi.getZcCanUseSum()) > 0) {

          sb.append("资金构成中【合同使用金额】不能超过【合同可用金额】！");

          return sb.toString();
        }
        czxsy = czxsy.add(benciShiYongSum);
      } else {
        qtsy = qtsy.add(benciShiYongSum);
      }

      shiJiTotal = shiJiTotal.add(benciShiYongSum);

      //      本次项目预算金额
      BigDecimal jiHuaSum = isSubHt() ? zcXmcgHtBi.getZcBiBcsySum() : zcXmcgHtBi.getZcPProMitemBi().getZcBiJhuaSum();

      jiHuaSum = jiHuaSum == null ? BigDecimal.ZERO : jiHuaSum;

      jiHuaTotal = jiHuaTotal.add(jiHuaSum);

    }

    if (shiJiTotal.doubleValue() <= 0) {

      sb.append("资金构成中【合同使用金额】必须填写！");

      return sb.toString();

    }

    List<ZcTBchtItem> item = zcXmcgHt.getItemList();

    BigDecimal itemsum = BigDecimal.ZERO;

    BigDecimal ysitemsum = BigDecimal.ZERO;

    for (Iterator iterator = item.iterator(); iterator.hasNext();) {

      ZcTBchtItem zcTBchtItem = (ZcTBchtItem) iterator.next();

      BigDecimal b = zcTBchtItem.getZcItemVal();

      b = b == null ? BigDecimal.ZERO : b;

      itemsum = itemsum.add(b);

      b = zcTBchtItem.getZcItemSum();

      b = b == null ? BigDecimal.ZERO : b;

      ysitemsum = ysitemsum.add(b);

      BigDecimal bumoney = zcTBchtItem.getBudgetBiMoney();//商品构成 一行记录的 财政预算列

      BigDecimal himoney = zcTBchtItem.getZcHtBiMoney();//商品构成 一行记录的 实际财政金额列
      bumoney = bumoney == null ? BigDecimal.ZERO : bumoney;

      himoney = himoney == null ? BigDecimal.ZERO : himoney;

      czxys = czxys.add(himoney);

      if (himoney.doubleValue() > bumoney.doubleValue()) {

        sb.append("商品构成中【采购预算资金财政性资金】必须大于或等于商品构成中【实际采购资金财政性资金】！");

        return sb.toString();

      }

      BigDecimal buotherm = zcTBchtItem.getBudgetOtherMoney();

      BigDecimal htotherm = zcTBchtItem.getZcHtOtherMoney();

      buotherm = buotherm == null ? BigDecimal.ZERO : buotherm;

      htotherm = htotherm == null ? BigDecimal.ZERO : htotherm;

      qtys = qtys.add(htotherm);

      if (htotherm.doubleValue() > buotherm.doubleValue()) {

        sb.append("商品构成中【预算其它金额】必须大于或等于商品构成中【实际其它金额】！");

        return sb.toString();

      }

    }

    if (itemsum.doubleValue() <= 0) {

      sb.append("商品构成中【实际总计】不能为空！");

      return sb.toString();

    }

    BigDecimal htmoney = zcXmcgHt.getZcHtNum();

    htmoney = htmoney == null ? BigDecimal.ZERO : htmoney;

    if (htmoney.doubleValue() != itemsum.doubleValue()) {

      sb.append("【" + getHtNumLabel() + "】与商品构成中【实际总计金额】之和必须必须相等！");

      return sb.toString();

    }

    if (htmoney.doubleValue() != shiJiTotal.doubleValue()) {

      sb.append("【" + getHtNumLabel() + "】与资金构成中【合同使用金额】之和必须必须相等！");

      return sb.toString();

    }

    if (czxsy.compareTo(czxys) < 0) {

      sb.append("资金构成中【财政性资金】之和必须大于或等于商品构成中【实际财政性资金】之和！");

      return sb.toString();
    }

    if (qtsy.compareTo(qtys) < 0) {

      sb.append("资金构成中【其它资金】之和必须大于或等于商品构成中【实际其它资金】之和！");

      return sb.toString();
    }

    if (ysitemsum.doubleValue() < itemsum.doubleValue()) {

      sb.append("商品构成中【预算总计】之和必须大于或等于商品构成中【实际总计金额】之和！");

      return sb.toString();

    }

    return sb.toString();

  }

  protected void refreshAll(ZcXmcgHt afterSaveBill, boolean isRefreshButton) {

    this.listCursor.setCurrentObject(afterSaveBill);

    refreshData();

    if (isRefreshButton) {

      setButtonStatus(afterSaveBill, requestMeta, this.listCursor);

    }

  }

  /**

   * 保存前校验

   * @param cpApply

   * @return

   */

  protected boolean checkBeforeSave() {

    List mainNotNullList = getMainBillElementMeta().getNotNullBillElement();

    List biNotNullList = self.biBillElementMeta.getNotNullBillElement();

    List itemNotNullList = self.itemBillElementMeta.getNotNullBillElement();

    ZcXmcgHt zcXmcgHt = (ZcXmcgHt) this.listCursor.getCurrentObject();

    StringBuilder errorInfo = new StringBuilder();

    String mainValidateInfo = ZcUtil.validateBillElementNull(zcXmcgHt, mainNotNullList);

    String biValidateInfo = ZcUtil.validateDetailBillElementNull(zcXmcgHt.getBiList(), biNotNullList, false);

    String itemValidateInfo = ZcUtil.validateDetailBillElementNull(zcXmcgHt.getItemList(), itemNotNullList, false);

    if (mainValidateInfo.length() != 0) {

      errorInfo.append("采购合同：\n").append(mainValidateInfo.toString()).append("\n");

    }

    if (biValidateInfo.length() != 0) {

      errorInfo.append("资金构成：\n").append(biValidateInfo.toString()).append("\n");

    }

    if (itemValidateInfo.length() != 0) {

      errorInfo.append("商品构成：\n").append(itemValidateInfo.toString()).append("\n");

    }

    List<ZcTBchtItem> item = zcXmcgHt.getItemList();

    BigDecimal itemsum = BigDecimal.ZERO;

    for (Iterator iterator = item.iterator(); iterator.hasNext();) {

      ZcTBchtItem zcTBchtItem = (ZcTBchtItem) iterator.next();

      BigDecimal mxMoney = zcTBchtItem.getZcCaigMoney();

      mxMoney = mxMoney == null ? BigDecimal.ZERO : mxMoney;

      itemsum = itemsum.add(mxMoney);

    }

    BigDecimal htmoney = zcXmcgHt.getZcHtNum();

    htmoney = htmoney == null ? BigDecimal.ZERO : htmoney;

    if (itemsum.doubleValue() != htmoney.doubleValue()) {

      errorInfo.append("商品构成中【金额】之和必须等于【" + getHtNumLabel() + "】！");

    }

    StringBuffer payErr = new StringBuffer();
    /* if (!"1".equals(zcXmcgHt.getZcFukuanType())) {
       if (zcXmcgHt.getPayBiList() == null || !(zcXmcgHt.getPayBiList().size() + "").equals(zcXmcgHt.getZcFukuanType())) {
         payErr.append("分次付款方式与付款明细不符！\n");
       }
     } else if (zcXmcgHt.getPayBiList() != null && zcXmcgHt.getPayBiList().size() > 1) {
       payErr.append("付款方式与付款明细不符！\n");
     }
    */
    /*  
      if (payErr.length() == 0 && zcXmcgHt.getPayBiList() != null && zcXmcgHt.getPayBiList().size() > 0) {
        BigDecimal sum = new BigDecimal("0");
        String time = "";
        for (Object item : zcXmcgHt.getPayBiList()) {
          ZcHtPrePayBillItem bill = (ZcHtPrePayBillItem) item;

          if (bill.getPayMoney() == null) {
            payErr.append("第" + bill.getPayOrder() + "次付款的支付金额不能为空 ！\n");
          } else {
            sum = sum.add(bill.getPayMoney());

            BigDecimal percent = bill.getPayMoney().divide(zcXmcgHt.getZcHtNum(), 4, 4).multiply(bai);
            bill.setPercent(percent.doubleValue() + "%");
          }
          if (bill.getPayYear() == null || bill.getPayMonth() == null || "".equals(bill.getPayMonth())) {
            payErr.append("第" + bill.getPayOrder() + "次付款的支付年月不能为空 ！\n");
            time = "";
          } else {
            if (!"".equals(time) && (bill.getPayYear() + bill.getPayMonth()).compareTo(time) < 0) {
              payErr.append("第" + bill.getPayOrder() + "次付款的支付年月应晚于上次支付年月 ！\n");
            }
            time = bill.getPayYear() + bill.getPayMonth();
          }
        }
        if (zcXmcgHt.getZcHtNum() != null && sum.compareTo(zcXmcgHt.getZcHtNum()) != 0) {
          payErr.append("分次付款的总金额不等于合同金额！\n");
        }

      }
      if (payErr.length() > 0) {
        errorInfo.append("付款明细：\n").append(payErr);
      }
      */

    if (errorInfo.length() != 0) {

      JOptionPane.showMessageDialog(this, errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);

      return false;

    }

    return true;

  }

  public boolean doSave() {

    if (!isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据没有发生改变，不用保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return true;

    }

    if (!checkBeforeSave()) {

      return false;

    }

    if (ZcUtil.isYsdw() && !calcBeforeSave()) {

      return false;

    }
    //采购中心填充全部采购合同时，需要指定资金构成
    if (ZcUtil.isCgzx() && createByCgzx() && !calcBeforeSave()) {
      return false;
    }

    boolean success = true;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(saveButton.getFuncId());

      ZcXmcgHt inData = (ZcXmcgHt) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      wordPane.save();

      String fileId = saveBulletinContent();

      if (fileId != null && !"".equals(fileId)) {

        inData.setZcConTextBlobid(fileId);

      }
      if (!isSubHt() && ZcUtil.isYsdw()) {
        makeToHtBi(inData);
      }
      //
      ZcXmcgHt zcXmcgHt = zcXmcgHtServiceDelegate.updateZcXmcgHtFN(inData, isSubHt()&&ZcUtil.useBudget(), WorkEnv.getInstance().getWebRoot(), this.requestMeta);

      listCursor.setCurrentObject(zcXmcgHt);

    } catch (Exception e) {

      logger.error(e.getMessage(), e);

      success = false;

      errorInfo += e.getMessage();

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      refreshData();

      this.listPanel.refreshCurrentTabData();

      loadMoldButton.setEnabled(true);

      selectMoldButton.setEnabled(true);
      this.editButton.setEnabled(true);

    } else {

      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

    return success;

  }

  /**
   * 是否由采购中心填充全部采购合同，目前适用于扬中地区
   * @return
   */
  private boolean createByCgzx() {
    return ZcUtil.isCreateHtByCgzx();
  }

  protected String saveBulletinContent() {

    String fileID = "";

    if (file == null)

      return null;

    String fileName = file.getFileName();

    if (fileName != null && fileName.length() != 0)

      fileID = WordFileUtil.uploadBulletinWordConstent(fileName);

    return fileID;

  }

  protected void doDelete() {

    requestMeta.setFuncId(deleteButton.getFuncId());

    ZcXmcgHt zcXmcgHt = (ZcXmcgHt) this.listCursor.getCurrentObject();

    if (zcXmcgHt.getZcHtCode() == null || "".equalsIgnoreCase(zcXmcgHt.getZcHtCode())) {

      JOptionPane.showMessageDialog(this, "尚未保存到数据库，无需删除！", "提示", JOptionPane.ERROR_MESSAGE);

      return;

    }

    int num = JOptionPane.showConfirmDialog(this, "是否删除当前单据", "删除确认", 0);

    if (num == JOptionPane.YES_OPTION) {

      boolean success = true;

      String errorInfo = "";

      try {

        requestMeta.setFuncId(deleteButton.getFuncId());

        //        if (!"0".equals(zcXmcgHt.getZcHtStatus())) {
        //
        //          JOptionPane.showMessageDialog(this, "非编辑状态单据，不可以删除！", "提示", JOptionPane.ERROR_MESSAGE);
        //
        //          return;
        //
        //        }

        zcXmcgHtServiceDelegate.deleteByPrimaryKeyFN(zcXmcgHt.getZcHtCode(), isSubHt(), WorkEnv.getInstance().getWebRoot(), this.requestMeta);

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

        JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }

  }

  private void stopTableEditing() {

    JPageableFixedTable biTable = this.biTablePanel.getTable();

    if (biTable.isEditing()) {

      biTable.getCellEditor().stopCellEditing();

    }

    JPageableFixedTable itemTable = this.itemTablePanel.getTable();

    if (itemTable.isEditing()) {

      itemTable.getCellEditor().stopCellEditing();

    }

  }

  public boolean isDataChanged() {

    stopTableEditing();
    if (!this.saveButton.isVisible() || !saveButton.isEnabled()) {
      return false;
    }

    return !DigestUtil.digest(getOldZcXmcgHt()).equals(DigestUtil.digest(listCursor.getCurrentObject()));

  }

  public ZcXmcgHt getOldZcXmcgHt() {
    return oldZcXmcgHt;
  }

  public void setOldZcXmcgHt(ZcXmcgHt oldZcXmcgHt) {
    this.oldZcXmcgHt = oldZcXmcgHt;
  }

  protected void refreshData() {

    ZcXmcgHt zcXmcgHt = (ZcXmcgHt) listCursor.getCurrentObject();

    if (zcXmcgHt != null && !"".equals(ZcUtil.safeString(zcXmcgHt.getZcHtCode()))) {//列表页面双击进入

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

      zcXmcgHt = zcXmcgHtServiceDelegate.selectByPrimaryKey(zcXmcgHt.getZcHtCode(), this.requestMeta);

      if (zcXmcgHt.getZcPProMake().getZcMakeCode() != null) {

        zcXmcgHt.setZcMakeCode(zcXmcgHt.getZcPProMake().getZcMakeCode());

      }

      List<ZcXmcgHtBi> tempList = new ArrayList<ZcXmcgHtBi>();

      for (int i = 0; i < zcXmcgHt.getBiList().size(); i++) {

        ZcXmcgHtBi bi = (ZcXmcgHtBi) zcXmcgHt.getBiList().get(i);

        ZcPProMitemBi zcPProMitemBi = bi.getZcPProMitemBi();

        bi.setZcUseBiId(zcPProMitemBi.getZcUseBiId());

        bi.setZcProBiSeq(zcPProMitemBi.getZcProBiSeq());

        if (zcPProMitemBi.getZcBiUsedSum() == null) {

          zcPProMitemBi.setZcBiUsedSum(BigDecimal.ZERO);

        }

        if (bi.getZcBiBcsySum() == null) {

          bi.setZcBiBcsySum(BigDecimal.ZERO);

        }

        tempList.add(bi);

      }
      if (isSubHt() && ZcUtil.useBudget()) {
        tempList = this.zcEbBaseServiceDelegate.queryDataForList("ZC_XMCG_HT_BI.ibatorgenerated_selectBiByHtCode", zcXmcgHt.getZcHtCode(),
          this.requestMeta);

        String sumId = "";
        for (int i = 0; i < tempList.size(); i++) {
          ZcXmcgHtBi bi = (ZcXmcgHtBi) tempList.get(i);
          if (bi.getZcBiNo() == null || "".equals(bi.getZcBiNo())) {
            bi.setZcBiDoSum(null);
            continue;
          }

          if (sumId.length() > 0) {
            sumId = sumId + ",'" + bi.getZcBiNo() + "'";
          } else {
            sumId = "'" + bi.getZcBiNo() + "'";
          }
        }
        getDto.setZcText3(sumId);
      }

      if (tempList != null && tempList.size() > 0) {
        zcXmcgHt.setBiList(tempList);
      } else {
        zcXmcgHt.setBiList(this.getBiList(zcXmcgHt));
      }

      findMainHt(zcXmcgHt);

      listCursor.setCurrentObject(zcXmcgHt);

      this.setEditingObject(zcXmcgHt);
    } else {//新增按钮进入

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;

      zcXmcgHt = new ZcXmcgHt();

      zcXmcgHt.setZcHtStatus("0");

      zcXmcgHt.setNd(this.requestMeta.getSvNd());
      zcXmcgHt.setZcInputDate(this.requestMeta.getSysDate());

      zcXmcgHt.setZcCgLeixing(ZcSettingConstants.PROJECT_BUY_CODE);

      zcXmcgHt.setBiList(new ArrayList<ZcXmcgHtBi>());
      
      zcXmcgHt.setMakeBiList(new ArrayList<ZcXmcgHtBi>());

      zcXmcgHt.setItemList(new ArrayList<ZcTBchtItem>());

      zcXmcgHt.setPayBiList(new ArrayList<ZcHtPrePayBillItem>());

      // 新增数据默认插入一行

      ZcXmcgHtBi bi = new ZcXmcgHtBi();

      setItemBiDefaultValue(bi);

      zcXmcgHt.getBiList().add(bi);

      listCursor.getDataList().add(zcXmcgHt);

      listCursor.setCurrentObject(zcXmcgHt);

      this.setEditingObject(zcXmcgHt);

    }

    if (!isSubHt()) {
      biTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubBiTableData(zcXmcgHt.getBiList(), false));
    } else {
      biTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubBiTableDataForSubHt(zcXmcgHt.getBiList(), wfCanEditFieldMap));
    }

    itemTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubItemTableData(zcXmcgHt.getItemList(), isCar()));

    // 翻译从表表头列
    if (!isSubHt()) {
      ZcUtil.translateColName(biTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getBiInfo());
    } else {
      ZcUtil.translateColName(biTablePanel.getTable(), ZcXmcgHtToTableModelConverter.biInfoForSubHtWihtBugdet);

      getDto.setNd(requestMeta.getSvNd());
      getDto.setZcText2("1");
      getDto.setCoCode(requestMeta.getSvCoCode());

    }

    ZcUtil.translateColName(itemTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getItemInfo());

    // 设置从表监听 

    addItemTableLisenter(itemTablePanel.getTable());

    addBiTableLisenter(biTablePanel.getTable());

    setBiTableEditor(biTablePanel.getTable());

    setItemTableEditor(itemTablePanel.getTable());

    this.refreshSubData(zcXmcgHt.getPayBiList());

    Long processInstId = zcXmcgHt.getProcessInstId();

    if (processInstId == null || processInstId.longValue() < 0) {

      sendToProcurementUnitButton.setVisible(false);

      sendToXieBanButton.setVisible(false);

    }

    setWordButtonStataus(zcXmcgHt);

    if (isGys()) {

      JPageableFixedTable ta = itemTablePanel.getTable();

      hideCol(ta, "ZC_ITEM_SUM");

      hideCol(ta, "BUDGET_BI_MONEY");

      hideCol(ta, "BUDGET_OTHER_MONEY");

      hideCol(ta, "ZC_ITEM_VAL");

      hideCol(ta, "ZC_HT_BI_MONEY");

      hideCol(ta, "ZC_HT_OTHER_MONEY");

      //      hideCol(ta, "ZC_HT_GK_MONEY");

    }

    setSumLabelText();

    setOldObject();

    setButtonStatus();

    updateFieldEditorsEditable();
  }

  //如果是编制状态的，且没有填充过模板则设置模板按钮为可用
  protected void setWordButtonStataus(ZcXmcgHt zcXmcgHt) {

    Long processInstId = zcXmcgHt.getProcessInstId();

    if (processInstId == null || processInstId.longValue() < 0) {

      if (zcXmcgHt.getZcConTextBlobid() == null) {

        this.selectMoldButton.setEnabled(true);

        this.loadMoldButton.setEnabled(true);

      }

    }

  }

  protected void setButtonStatus() {
    ZcXmcgHt ht = (ZcXmcgHt) listCursor.getCurrentObject();
    if (WFConstants.AUDIT_TAB_STATUS_CANCEL.equals(ht.getZcHtStatus())) {
      setCancelStatus(listCursor);
    } else {
      setButtonStatus(ht, requestMeta, this.listCursor);
    }
  }

  public void setButtonStatusWithoutWf() {

    if (this.btnStatusList.size() == 0) {

      ButtonStatus bs = new ButtonStatus();

      bs.setButton(this.addButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

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

      bs.setButton(this.exitButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_ALL);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.sendButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.suggestPassButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.auditPassButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.callbackButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.unAuditButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.unTreadButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      bs = new ButtonStatus();

      bs.setButton(this.printButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_AUDITED);

      btnStatusList.add(bs);

    }

    ZcXmcgHt ht = (ZcXmcgHt) this.listCursor.getCurrentObject();

    String billStatus = ht.getZcHtStatus();

    ZcUtil.setButtonEnable(this.btnStatusList, billStatus, this.pageStatus, getCompoId(), ht.getProcessInstId());

  }

  private void refreshSubData(List biList, List itemList, List payBiList) {

    if (!isSubHt()) {
      biTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubBiTableData(biList, false));
    } else {
      biTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubBiTableDataForSubHt(biList, wfCanEditFieldMap));
    }

    itemTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubItemTableData(itemList, isCar()));

    // 翻译从表表头列
    if (!isSubHt()) {
      ZcUtil.translateColName(biTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getBiInfo());
    } else {
      ZcUtil.translateColName(biTablePanel.getTable(), ZcXmcgHtToTableModelConverter.biInfoForSubHtWihtBugdet);
    }

    ZcUtil.translateColName(itemTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getItemInfo());

    // 设置从表监听 

    addItemTableLisenter(itemTablePanel.getTable());

    addBiTableLisenter(biTablePanel.getTable());

    refreshSubData(payBiList);

    //    setOldObject();

    setBiTableEditor(biTablePanel.getTable());

    setItemTableEditor(itemTablePanel.getTable());

    int height = itemTablePanel.getTable().getRowHeight();
    //
    //    if (isGys()) {
    //
    //      JPageableFixedTable ta = itemTablePanel.getTable();
    //
    //      hideCol(ta, "ZC_ITEM_SUM");
    //
    //      hideCol(ta, "BUDGET_BI_MONEY");
    //
    //      hideCol(ta, "BUDGET_OTHER_MONEY");
    //
    //      hideCol(ta, "ZC_ITEM_VAL");
    //
    //      hideCol(ta, "ZC_HT_BI_MONEY");
    //
    //      hideCol(ta, "ZC_HT_OTHER_MONEY");
    //
    //      hideCol(ta, "ZC_HT_GK_MONEY");
    //
    //    }

    height = itemTablePanel.getTable().getRowHeight();

    System.out.println("height:" + height);

    itemTablePanel.getTable().setRowHeight(18);

    itemTablePanel.getTable().sizeColumnsToFit(0);

  }

  private void refreshItemData(List itemList) {

    itemTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubItemTableData(itemList, isCar()));

    ZcUtil.translateColName(itemTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getItemInfo()); // 翻译从表表头列

    addItemTableLisenter(itemTablePanel.getTable()); // 设置从表监听 

    setOldObject();

    setItemTableEditor(itemTablePanel.getTable());

  }

  public void addBiTableLisenter(JPageableFixedTable table) {

    final JPageableFixedTable mytable = table;

    final BeanTableModel model = (BeanTableModel) (table.getModel());

    model.addTableModelListener(new TableModelListener() {

      public void tableChanged(TableModelEvent e) {

        if (e.getColumn() >= 0

        && ("ZC_BI_JHUA_SUM".equals(model.getColumnIdentifier(e.getColumn())) || "ZC_BI_BCSY_SUM".equals(model.getColumnIdentifier(e.getColumn())))) {

          self.caculateMoney(model.getDataBeanList());

        }

      }

    });

  }

  public void addItemTableLisenter(final JPageableFixedTable table) {

    final BeanTableModel model = (BeanTableModel) (table.getModel());

    model.addTableModelListener(new TableModelListener() {

      public void tableChanged(TableModelEvent e) {
        ZcXmcgHt ht = (ZcXmcgHt) listCursor.getCurrentObject();

        if (e.getType() == TableModelEvent.UPDATE) {

          if (e.getColumn() >= 0

          && ("BUDGET_BI_MONEY".equals(model.getColumnIdentifier(e.getColumn())) || "BUDGET_OTHER_MONEY".equals(model.getColumnIdentifier(e

          .getColumn())))) {// || "ZC_HT_BI_MONEY".equals(model.getColumnIdentifier(e.getColumn())) || "ZC_HT_OTHER_MONEY".equals(model.getColumnIdentifier(e.getColumn()))

            int k = table.getSelectedRow();

            if (k < 0)

              return;

            ZcTBchtItem item = (ZcTBchtItem) (model.getBean(table.convertRowIndexToModel(k)));

            BigDecimal bbm = item.getBudgetBiMoney();

            BigDecimal bom = item.getBudgetOtherMoney();

            if (bbm != null && bom != null) {

              item.setZcItemSum(bbm.add(bom));

              model.fireTableRowsUpdated(k, k);

            }

          }

          if (e.getColumn() >= 0

          && ("ZC_HT_BI_MONEY".equals(model.getColumnIdentifier(e.getColumn())) || "ZC_HT_OTHER_MONEY".equals(model.getColumnIdentifier(e

          .getColumn())))) {

            int k = table.getSelectedRow();

            if (k < 0)

              return;

            ZcTBchtItem item = (ZcTBchtItem) (model.getBean(table.convertRowIndexToModel(k)));

            BigDecimal hbm = item.getZcHtBiMoney();

            BigDecimal hom = item.getZcHtOtherMoney();

            if (hbm != null && hom != null) {

              item.setZcItemVal(hbm.add(hom));

              //              item.setZcHtGkMoney(hbm.add(hom));

              model.fireTableRowsUpdated(k, k);
              if (isCar()) {
                BigDecimal num = BigDecimal.ZERO;
                for (int i = 0; i < table.getRowCount(); i++) {
                  ZcTBchtItem row = (ZcTBchtItem) (model.getBean(table.convertRowIndexToModel(i)));
                  if (row.getZcItemVal() != null) {
                    num = num.add(row.getZcItemVal());
                  }
                }
                listCursor.getCurrentObject().setZcHtNum(num);
                self.setEditingObject(listCursor.getCurrentObject());

              }
            }

          }

          if (e.getColumn() >= 0

          && ("PAY_MONEY".equals(model.getColumnIdentifier(e.getColumn())))) {

            int k = table.getSelectedRow();

            if (k < 0)

              return;

            ZcHtPrePayBillItem item = (ZcHtPrePayBillItem) model.getBean(table.convertRowIndexToModel(k));
            BigDecimal payMoeny = item.getPayMoney();
            payMoeny.setScale(4, BigDecimal.ROUND_HALF_UP);
            if (payMoeny != null && ht.getZcHtNum() != null) {
              if (payMoeny.compareTo(ht.getZcHtNum()) == 1) {
                JOptionPane.showMessageDialog(self, "支付金额不能大于合同总金额.", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
              }

              if (ht.getZcHtNum() != null && ht.getZcHtNum().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal percent = payMoeny.divide(ht.getZcHtNum(), 4, 4).multiply(bai);
                item.setPercent(percent.doubleValue() + "%");
              }

            }

            model.fireTableRowsUpdated(k, k);

          }

        }

      }

    });

  }

  public void refreshSubItemData(List<ZcebZbItem> zbItemList) {

    List<ZcTBchtItem> itemList = ((ZcXmcgHt) self.listCursor.getCurrentObject()).getItemList();

    itemList.clear();

    //将中标管理中的标的
    for (int i = 0; i < zbItemList.size(); i++) {

      ZcebZbItem zbi = zbItemList.get(i);

      ZcTBchtItem item = new ZcTBchtItem();

      item.setZcCatalogueCode(zbi.getCatalogueCode());//品目代码

      item.setZcCatalogueName(zbi.getCatalogueName());

      item.setZcSpName(zbi.getSpName());//商品名称

      item.setZcIsJnjs(zbi.getIsJnjs());//是否节能节水

      item.setZcMerIsLshb(zbi.getIsLshb());//是否绿色环保

      item.setZcMerIsZzcx(zbi.getIsZzcx());//是否自主创新

      item.setZcCaigShl(zbi.getAmount());//数量

      item.setZcMerSpec(zbi.getSpSpec());//商品规格

      item.setZcItemVal(zbi.getSums());//总金额

      item.setBudgetBiMoney(new BigDecimal(0.00));

      item.setBudgetOtherMoney(new BigDecimal(0.00));

      item.setZcHtBiMoney(new BigDecimal(0.00));

      item.setZcHtOtherMoney(new BigDecimal(0.00));

      item.setZcHtGkMoney(new BigDecimal(0.00));

      itemList.add(item);

    }

    refreshItemData(itemList);

    if (isGys()) {

      JPageableFixedTable ta = itemTablePanel.getTable();

      hideCol(ta, "ZC_ITEM_SUM");

      hideCol(ta, "BUDGET_BI_MONEY");

      hideCol(ta, "BUDGET_OTHER_MONEY");

      hideCol(ta, "ZC_ITEM_VAL");

      hideCol(ta, "ZC_HT_BI_MONEY");

      hideCol(ta, "ZC_HT_OTHER_MONEY");//

      hideCol(ta, "ZC_HT_GK_MONEY");

    }

  }

  protected void setBiTableEditor(JTable table) {

    if (isSubHt()) {//补充合同需要选择指标,填充资金
      if(ZcUtil.useBudget()){
        String colNames[] = { "指标余额表ID", "指标来源", "发文文号", "资金性质","采购项目", "功能分类", "经济分类","是否监督使用","是否政府采购","指标总金额","指标可用金额" };
        ZcBudgetHandler budgetHandler = new ZcBudgetHandler(colNames, biTablePanel, this, listCursor, getDto);

        ForeignEntityFieldCellEditor suEditor = new ForeignEntityFieldCellEditor("VwBudgetGp.getVwBudgetGp", getDto, 20, budgetHandler, colNames,
          "资金构成", "sumId");

        SwingUtil.setTableCellEditor(table, "ZC_BI_NO", suEditor);

        SwingUtil.setTableCellEditor(table, "ZC_BI_SUM", new MoneyCellEditor(false));

        SwingUtil.setTableCellRenderer(table, "ZC_BI_SUM", new NumberCellRenderer());

        SwingUtil.setTableCellEditor(table, "ZC_BI_DO_SUM", new MoneyCellEditor(false));

        SwingUtil.setTableCellRenderer(table, "ZC_BI_DO_SUM", new NumberCellRenderer());

        SwingUtil.setTableCellEditor(table, "ZC_BI_JHUA_SUM", new MoneyCellEditor(false));

        SwingUtil.setTableCellRenderer(table, "ZC_BI_JHUA_SUM", new NumberCellRenderer());

        SwingUtil.setTableCellEditor(table, "ZC_BI_YJBA_SUM", new MoneyCellEditor(false));

        SwingUtil.setTableCellRenderer(table, "ZC_BI_YJBA_SUM", new NumberCellRenderer());
        
      }else{

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

        SwingUtil.setTableCellEditor(table, "ZC_FUND_FILE", new FileCellEditor("zcFundFileBlobid", (BeanTableModel) table.getModel()));

//        String status = ((ZcPProMake) listCursor.getCurrentObject()).getZcMakeStatus();
    //
//        if (!"exec".equals(status) && !"yjz".equals(status)) {
    //
//          table.getTableHeader().getColumnModel().removeColumn(table.getColumn("ZC_BI_YJBA_SUM"));
    //
//        }
        
      }
    }else{
      
      ZcXmcgHtToTableModelConverter.setBiTableEditor(table);
      String[] biColumNames = { "指标余额表ID", "本次项目预算金额", "累计执行金额", "资金性质", "指标来源", "支付方式" };
      ZcVBiDetailHandler biHandler = new ZcVBiDetailHandler(biColumNames, biTablePanel, self);
  
      ZcXmcgHt ht = (ZcXmcgHt) listCursor.getCurrentObject();
  
      ElementConditionDto merDto = new ElementConditionDto();
  
      merDto.setZcText0(ht.getZcMakeCode());
  
      ForeignEntityFieldCellEditor biEditor = new ForeignEntityFieldCellEditor("ZC_P_PRO_MITEM_BI.getForeignBiDetail", merDto, 20, biHandler,
  
      biColumNames, "资金构成", "zcBiNo");
  
      SwingUtil.setTableCellEditor(table, "ZC_BI_NO", biEditor);
    }

  }

  protected void setItemTableEditor(JTable table) {
    ZcXmcgHtToTableModelConverter.setItemTableEditor(table);
  }

  public void setSumLabelText() {

    JTablePanel tablePanel = biTablePanel;

    ZcXmcgHt zcXmcgHt = (ZcXmcgHt) this.listCursor.getCurrentObject();

    List<ZcXmcgHtBi> biList = zcXmcgHt.getBiList();

    BigDecimal bisum = BigDecimal.ZERO;//实际合计

    BigDecimal yssum = BigDecimal.ZERO;//预算合计

    for (Iterator iterator = biList.iterator(); iterator.hasNext();) {

      ZcXmcgHtBi zcXmcgHtBi = (ZcXmcgHtBi) iterator.next();

      BigDecimal b = zcXmcgHtBi.getZcPProMitemBi().getZcBiJhuaSum();

      b = b == null ? BigDecimal.ZERO : b;

      bisum = bisum.add(b);

      b = zcXmcgHtBi.getZcBiBcsySum();

      b = b == null ? BigDecimal.ZERO : b;

      yssum = yssum.add(b);

    }

    biSummaryLabel.setText("合计：    预算合计金额(" + bisum + ")   实际合计金额(" + yssum + ") ");

  }

  protected void hideCol(JTable table, String colName) {

    TableColumn tc = table.getColumn(colName);

    table.getColumnModel().removeColumn(tc);

  }

  protected void setOldObject() {

    oldZcXmcgHt = (ZcXmcgHt) ObjectUtil.deepCopy(listCursor.getCurrentObject());

  }

  public String doOpenMold(List valueList, ZcEbBulletinWordMold bulletinMold) {

    if (wordPane != null) {

      wordPane.close();

    }

    WordFileUtil.setDir("ht");

    wordFileId = bulletinMold.getFileID();

    String moldFileName = WordFileUtil.loadMold(bulletinMold.getFileID());

    file = new AsFile();

    file.setFileName(moldFileName);

    if (moldFileName == null || moldFileName.length() == 0) { //没有成功载入模板，关闭wordPane  

      if (wordPane != null)

        wordPane.close();

      return "";

    }

    wordPane.open(moldFileName);

    return "";

  }

  public String getPageStatus() {

    return pageStatus;

  }

  public ZcXmcgHtSubListPanelDt getListPanel() {

    // TODO Auto-generated method stub

    return self.listPanel;

  }

  public RequestMeta getRequestMeta() {

    // TODO Auto-generated method stub

    return self.requestMeta;

  }

  public ListCursor getListCursor() {

    // TODO Auto-generated method stub

    return self.listCursor;

  }

  public ElementConditionDto getDtoForBidWinner() {

    // TODO Auto-generated method stub

    return self.dtoForBidWinner;

  }

  void loadSubCommonEditors(List<AbstractFieldEditor> editorList) {
  }

  protected void findMainHt(ZcXmcgHt zcXmcgHt) {
  }
  
  public static String getCompoId() {
    return compoId;
  }

  protected String getProjectSqlId() {
    if (isGys() || isCgzx()) {
      return "selectPage.selectProjectForZxCaiGouHt";
    }
    return "selectPage.selectProjectForCaiGouHt";
  }

  public BillElementMeta getMainBillElementMeta() {
    return mainBillElementMeta;
  }

 /* public boolean budgetExcuce(List selectedDatas) {

    JTable table = biTablePanel.getTable();

    BeanTableModel model = (BeanTableModel) table.getModel();

    int k = table.getSelectedRow();

    if (k < 0)

      return false;

    int k2 = table.convertRowIndexToModel(k);

    if (selectedDatas.size() > 0) {

      VwBudgetGp gp = (VwBudgetGp) selectedDatas.get(0);
      ZcXmcgHt ht = (ZcXmcgHt) listCursor.getCurrentObject();
      ZcXmcgHtBi bi = (ZcXmcgHtBi) ht.getBiList().get(k2);
      String sumId = "";
      if (bi.getZcBiNo() != null) {
        sumId = bi.getZcBiNo();
      }
      ;
      bi.setZcBiNo(gp.getSumId() + "");
      bi.setZcBiDoSum(gp.getCanuseMoney());
      bi.setZcBiSum(gp.getBudgetMoney());
      // 预算单位
      if (gp.getEnCode() != null) {
        bi.setCoCode(gp.getEnCode());
        bi.setCoName(gp.getEnName());
      }
      // 资金性质
      if (gp.getMkCode() != null) {
        bi.setFundCode(gp.getMkCode());
        bi.setFundName(gp.getMkName());
      }
      // 功能分类
      if (gp.getBsCode() != null) {
        bi.setbAccCode(gp.getBsCode());
        bi.setbAccName(gp.getBsName());
      }
      // 项目类别
      if (gp.getBiCode() != null) {
        bi.setProjectTypeCode(gp.getBiCode());
        bi.setProjectTypeName(gp.getBiName());
      }
      // 付款方式
      if (gp.getPkCode() != null) {
        bi.setPaytypeCode(gp.getPkCode());
        bi.setPaytypeName(gp.getPkName());
      }
      // 指标来源
      if (gp.getBlCode() != null) {
        bi.setOriginCode(gp.getBlCode());
        bi.setOriginName(gp.getBlName());
      }
      // 业务处室
      if (gp.getMbId() != null) {
        bi.setOrgCode(gp.getMbId());
        bi.setOrgName(gp.getMbName());
      }
      // 年度
      bi.setNd(gp.getSetYear() + "");
      // 文号
      if (gp.getFileCode() != null) {
        bi.setSenddocCode(gp.getFileCode());
        bi.setSenddocName(gp.getFileName());
      }
      if (getDto.getZcText3() != null && !"".equals(getDto.getZcText3())) {
        getDto
          .setZcText3(getDto.getZcText3().replaceAll(",'" + sumId + "'", "").replaceAll("'" + sumId + "',", "").replaceAll("'" + sumId + "'", ""));
      }
      if (getDto.getZcText3() == null || "".equals(getDto.getZcText3())) {
        getDto.setZcText3("'" + gp.getSumId() + "'");
      } else {
        getDto.setZcText3(getDto.getZcText3() + ",'" + gp.getSumId() + "'");
      }

      setEditingObject(ht);

    }
    return false;
  }*/

  public boolean budgetAfterClear() {
    JTable table = biTablePanel.getTable();

    BeanTableModel model = (BeanTableModel) table.getModel();

    int k = table.getSelectedRow();

    if (k < 0)

      return false;

    int k2 = table.convertRowIndexToModel(k);

    ZcXmcgHt ht = (ZcXmcgHt) listCursor.getCurrentObject();
    ZcXmcgHtBi bi = (ZcXmcgHtBi) ht.getBiList().get(k2);
    if (bi.getZcBiNo() != null && !"".equals(bi.getZcBiNo())) {
      getDto.setZcText3(getDto.getZcText3().replaceAll(",'" + bi.getZcBiNo() + "'", "").replaceAll("'" + bi.getZcBiNo() + "',", "")
        .replaceAll("'" + bi.getZcBiNo() + "'", ""));
    }
    bi.setZcBiNo("");
    bi.setZcBiDoSum(null);
    bi.setZcBiSum(null);
    // 预算单位
    bi.setCoCode("");
    bi.setCoName("");
    // 资金性质
    bi.setFundCode("A");
    bi.setFundName("");
    // 功能分类
    bi.setbAccCode("");
    bi.setbAccName("");
    // 项目类别
    bi.setProjectTypeCode("");
    bi.setProjectTypeName("");
    // 付款方式
    bi.setPaytypeCode("A");
    bi.setPaytypeName("");
    // 指标来源
    bi.setOriginCode("4");
    bi.setOriginName("");
    // 年度
    bi.setNd("");
    // 文号
    bi.setSenddocCode("");
    bi.setSenddocName("");
    // 业务处室
    bi.setOrgCode("");
    bi.setOrgName("");

    setEditingObject(ht);
    return false;
  }

  protected void makeToHtBi(ZcXmcgHt ht) {
    List<ZcXmcgHtBi> htbi = ht.getBiList();
    for (int i = 0; i < htbi.size(); i++) {
      ZcXmcgHtBi bi = htbi.get(i);

      bi.setZcBiNo(bi.getZcPProMitemBi().getZcBiNo());
      bi.setZcBiDoSum(bi.getZcPProMitemBi().getZcBiDoSum());
      bi.setZcUseBiId(bi.getZcPProMitemBi().getZcUseBiId());
      //      bi.setZcBiSum(bi.getZcPProMitemBi().getZcBiSum());
      // 预算单位
      bi.setCoCode(bi.getZcPProMitemBi().getCoCode());
      bi.setCoName(bi.getZcPProMitemBi().getCoName());
      // 资金性质
      bi.setFundCode(bi.getZcPProMitemBi().getFundCode());
      bi.setFundName(bi.getZcPProMitemBi().getFundName());
      // 功能分类
      bi.setbAccCode(bi.getZcPProMitemBi().getbAccCode());
      bi.setbAccName(bi.getZcPProMitemBi().getbAccName());
      // 项目类别
      bi.setProjectTypeCode(bi.getZcPProMitemBi().getProjectTypeCode());
      bi.setProjectTypeName(bi.getZcPProMitemBi().getProjectTypeName());
      // 付款方式
      bi.setPaytypeCode(bi.getZcPProMitemBi().getPaytypeCode());
      bi.setPaytypeName(bi.getZcPProMitemBi().getPaytypeName());
      // 指标来源
      bi.setOriginCode(bi.getZcPProMitemBi().getOriginCode());
      bi.setOriginName(bi.getZcPProMitemBi().getOriginName());
      // 年度
      bi.setNd(bi.getZcPProMitemBi().getNd());
      // 文号
      bi.setSenddocCode(bi.getZcPProMitemBi().getSenddocCode());
      bi.setSenddocName(bi.getZcPProMitemBi().getSenddocName());
      // 业务处室
      bi.setOrgCode(bi.getZcPProMitemBi().getOrgCode());
      bi.setOrgName(bi.getZcPProMitemBi().getOrgName());
      bi.setOutlayCode(bi.getZcPProMitemBi().getOutlayCode());
      bi.setOutlayName(bi.getZcPProMitemBi().getOutlayName());
    }
  }

  protected Integer[] deleteBiSub(JTablePanel tablePanel) {

    JPageableFixedTable table = tablePanel.getTable();

    Integer[] checkedRows;

    // 是否显示行选择框

    if (tablePanel.getTable().isShowCheckedColumn()) {

      checkedRows = table.getCheckedRows();

    } else {

      int[] selectedRows = table.getSelectedRows();

      checkedRows = new Integer[selectedRows.length];

      for (int i = 0; i < checkedRows.length; i++) {

        checkedRows[i] = selectedRows[i];

      }

    }

    if (checkedRows.length == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", "提示",

      JOptionPane.INFORMATION_MESSAGE);

    } else {

      if (table.isEditing()) {

        table.getCellEditor().stopCellEditing();

      }

      BeanTableModel tableModel = (BeanTableModel) table.getModel();

      int[] selRows = new int[checkedRows.length];

      for (int i = 0; i < selRows.length; i++) {

        selRows[i] = table.convertRowIndexToModel(checkedRows[i]);

      }

      Arrays.sort(selRows);

      for (int i = selRows.length - 1; i >= 0; i--) {
        if (isSubHt()) {
          ZcXmcgHtBi bi = (ZcXmcgHtBi) ((ZcXmcgHt) listCursor.getCurrentObject()).getBiList().get(selRows[i]);
          if (bi.getZcBiNo() != null && !"".equals(bi.getZcBiNo())) {
            getDto.setZcText3(getDto.getZcText3().replaceAll(",'" + bi.getZcBiNo() + "'", "").replaceAll("'" + bi.getZcBiNo() + "',", "")
              .replaceAll("'" + bi.getZcBiNo() + "'", ""));
          }
        }

        tableModel.deleteRow(selRows[i]);

      }

    }

    fitColumnWidth(tablePanel.getTable());

    return checkedRows;

  }

  protected String getHtNumLabel() {
    return LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_NUM);
  }

  protected String getHtStatus() {
    return "ZC_VS_HT_STATUS";
    //    return "ZC_VS_COMPLETION_STATUS";
  }

  /*

   * 从表的添加行方法

   */

  protected int addSub(JTablePanel tablePanel, Serializable bean) {

    tablePanel.getTable().clearSelection();

    if (tablePanel.getTable().isEditing()) {

      tablePanel.getTable().getCellEditor().stopCellEditing();

    }

    BeanTableModel editTableModel = (BeanTableModel) tablePanel.getTable()

    .getModel();

    if (bean instanceof ZcHtPrePayBillItem) {
      ((ZcHtPrePayBillItem) bean).setPayOrder(editTableModel.getRowCount() + 1 + "");
    }

    editTableModel.insertRow(editTableModel.getRowCount(), bean);

    fitColumnWidth(tablePanel.getTable());

    return editTableModel.getRowCount() - 1;

  }

  /*

   * 从表的插入行方法

   */

  protected int insertSub(JTablePanel tablePanel, Serializable bean) {

    if (tablePanel.getTable().isEditing()) {

      tablePanel.getTable().getCellEditor().stopCellEditing();

    }

    BeanTableModel editTableModel = (BeanTableModel) tablePanel.getTable()

    .getModel();

    int selectedRow = tablePanel.getTable().getSelectedRow();

    if (selectedRow != -1) {

      editTableModel.insertRow(selectedRow + 1, bean);

      selectedRow = selectedRow + 1;

    } else {

      editTableModel.insertRow(editTableModel.getRowCount(), bean);

      selectedRow = editTableModel.getRowCount() - 1;

    }

    if (bean instanceof ZcHtPrePayBillItem) {
      ((ZcHtPrePayBillItem) bean).setPayOrder(selectedRow + 1 + "");
      for (int i = selectedRow; i < editTableModel.getRowCount(); i++) {
        ((ZcHtPrePayBillItem) editTableModel.getBean(i)).setPayOrder(i + 1 + "");
      }
    }

    fitColumnWidth(tablePanel.getTable());

    return selectedRow;

  }

  /*

   * 从表的删除行方法

   */

  protected Integer[] deleteSub(JTablePanel tablePanel) {

    JPageableFixedTable table = tablePanel.getTable();

    Integer[] checkedRows;

    // 是否显示行选择框

    if (tablePanel.getTable().isShowCheckedColumn()) {

      checkedRows = table.getCheckedRows();

    } else {

      int[] selectedRows = table.getSelectedRows();

      checkedRows = new Integer[selectedRows.length];

      for (int i = 0; i < checkedRows.length; i++) {

        checkedRows[i] = selectedRows[i];

      }

    }

    if (checkedRows.length == 0) {

      JOptionPane.showMessageDialog(this, "没有选择数据！", "提示",

      JOptionPane.INFORMATION_MESSAGE);

    } else {

      if (table.isEditing()) {

        table.getCellEditor().stopCellEditing();

      }

      BeanTableModel tableModel = (BeanTableModel) table.getModel();

      int[] selRows = new int[checkedRows.length];

      for (int i = 0; i < selRows.length; i++) {

        selRows[i] = table.convertRowIndexToModel(checkedRows[i]);

      }

      Arrays.sort(selRows);

      for (int i = selRows.length - 1; i >= 0; i--) {

        tableModel.deleteRow(selRows[i]);

      }
      if (tableModel.getRowCount() > 0 && tableModel.getBean(0) instanceof ZcHtPrePayBillItem) {

        for (int i = 0; i < tableModel.getRowCount(); i++) {
          ((ZcHtPrePayBillItem) tableModel.getBean(i)).setPayOrder(i + 1 + "");
        }
      }

    }

    fitColumnWidth(tablePanel.getTable());

    return checkedRows;

  }

  public boolean isGys() {
    // TODO Auto-generated method stub
    return ZcUtil.isGys();
  }

  public class ZcPHtHandler implements IForeignEntityHandler {

    private String columNames[];

    public ZcPHtHandler(String columNames[]) {

      this.columNames = columNames;

    }

    //

    public void excute(List selectedDatas) {

      // TODO Auto-generated method stub

      if (selectedDatas != null && selectedDatas.size() > 0) {

        ZcXmcgHt temp = (ZcXmcgHt) selectedDatas.get(0);

        ZcXmcgHt zcXmcgHt = (ZcXmcgHt) getListCursor().getCurrentObject();

        zcXmcgHt.setZcParhtCode(temp.getZcHtCode());
        zcXmcgHt.setpName(temp.getZcHtName());
        zcXmcgHt.setpBiSum(temp.getZcHtNum());
        zcXmcgHt.setZcSuName(temp.getZcSuName());
        zcXmcgHt.setZcSuCode(temp.getZcSuCode());
        zcXmcgHt.setZcZbCode(temp.getZcSuCode());
        zcXmcgHt.setZcZbName(temp.getZcSuName());
        zcXmcgHt.setCoName(temp.getCoName());
        zcXmcgHt.setCoCode(temp.getCoCode());
        
        zcXmcgHt.setProjCode(temp.getProjCode());
        zcXmcgHt.setProjName(temp.getProjName());
        
        zcXmcgHt.setZcMakeCode(temp.getZcPProMake().getZcMakeCode());
        zcXmcgHt.setZcPProMake(temp.getZcPProMake());
        

        setEditingObject(zcXmcgHt);

      }

    }

    public void afterClear() {

      ZcXmcgHt zcXmcgHt = (ZcXmcgHt) getListCursor().getCurrentObject();

      zcXmcgHt.setZcParhtCode("");
      zcXmcgHt.setpName("");
      zcXmcgHt.setpBiSum(BigDecimal.ZERO);
      zcXmcgHt.setZcSuName("");
      zcXmcgHt.setCoName("");
      zcXmcgHt.setCoCode("");
      
      setEditingObject(zcXmcgHt);

    }

    @Override
    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        ZcXmcgHt rowData = (ZcXmcgHt) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getZcHtCode();

        data[i][col++] = rowData.getZcHtName();

        data[i][col++] = rowData.getZcHtNum();

        data[i][col++] = rowData.getCoName();

        data[i][col++] = rowData.getZcSuName();

      }

      MyTableModel model = new MyTableModel(data, columNames) {

        public boolean isCellEditable(int row, int colum) {

          return false;

        }

      };

      return model;

    }

    @Override
    public boolean isMultipleSelect() {

      // TODO Auto-generated method stub

      return false;

    }

  }

}
