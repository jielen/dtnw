/**
 * 
 */
package com.ufgov.zc.client.zc.eval.result;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.JDateTextField;
import com.ufgov.smartclient.component.table.cellrenderer.NumberCellRenderer;
import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ParentWindowAware;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.ZcEbEvalBidTeamStToTableModelConverter;
import com.ufgov.zc.client.common.converter.zc.ZcEbEvalPackResultToTableModelConverter;
import com.ufgov.zc.client.common.converter.zc.ZcXunjiaDetailToTableModelConverter;
import com.ufgov.zc.client.component.DateField;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.GkCommentDialog;
import com.ufgov.zc.client.component.GkCommentUntreadDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.AgreeButton;
import com.ufgov.zc.client.component.button.AuditFinalPassButton;
import com.ufgov.zc.client.component.button.AuditPassButton;
import com.ufgov.zc.client.component.button.CallbackButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.DisagreeButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.ImportExpertOpinionButton;
import com.ufgov.zc.client.component.button.IsSendToNextButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.OpenNotepadButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.PrintButton;
import com.ufgov.zc.client.component.button.PrintPreviewButton;
import com.ufgov.zc.client.component.button.PrintSettingButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SendButton;
import com.ufgov.zc.client.component.button.SendToProcurementUnitButton;
import com.ufgov.zc.client.component.button.SendToXieBanButton;
import com.ufgov.zc.client.component.button.SuggestAuditPassButton;
import com.ufgov.zc.client.component.button.TraceButton;
import com.ufgov.zc.client.component.button.UnauditButton;
import com.ufgov.zc.client.component.button.UntreadButton;
import com.ufgov.zc.client.component.event.ValueChangeEvent;
import com.ufgov.zc.client.component.event.ValueChangeListener;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.celleditor.DateCellEditor;
import com.ufgov.zc.client.component.table.celleditor.MoneyCellEditor;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.celleditor.zc.ZcEmExpertTypeSelectionCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.DateCellRenderer;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsMapCellRenderer;
import com.ufgov.zc.client.component.table.codecellrenderer.AsValCellRenderer;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.SelectFileFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextAreaFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.client.print.PrintSettingDialog;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.notepad.ZcNotepadDialog;
import com.ufgov.zc.client.zc.project.integration_dt.ZcEbProjectEditPanel_dt;
import com.ufgov.zc.client.zc.ztb.ChangeRMB;
import com.ufgov.zc.common.commonbiz.model.Company;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.WFConstants;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.exception.BaseException;
import com.ufgov.zc.common.system.exception.OtherException;
import com.ufgov.zc.common.system.model.AsFile;
import com.ufgov.zc.common.system.model.AsVal;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.RfqSinupPack;
import com.ufgov.zc.common.zc.model.ZcEbEntrust;
import com.ufgov.zc.common.zc.model.ZcEbEvalReport;
import com.ufgov.zc.common.zc.model.ZcEbExpertOpinion;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.model.ZcEbPackEvalResult;
import com.ufgov.zc.common.zc.model.ZcEbPlan;
import com.ufgov.zc.common.zc.model.ZcEbProj;
import com.ufgov.zc.common.zc.model.ZcEbRfqPack;
import com.ufgov.zc.common.zc.model.ZcEbXunJiaBaoJia;
import com.ufgov.zc.common.zc.model.ZcEbYanShouBill;
import com.ufgov.zc.common.zc.model.ZcXunJiaDetail;
import com.ufgov.zc.common.zc.model.ZcXunJiaSummary;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbEntrustServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbEvalServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbPlanServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbProjServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbRfqServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbXjbjServiceDelegate;

/**
 * @author Administrator
 *
 */
public class ZcEbEvalReportOffLineEditPanel extends AbstractMainSubEditPanel implements ParentWindowAware {

  private static final Logger logger = Logger.getLogger(ZcEbEvalReportEditPanel.class);

  private final FuncButton deleteButton = new DeleteButton();

  private final FuncButton saveButton = new SaveButton();

  private final FuncButton sendButton = new SendButton();

  //导入专家评审意见
  private final ImportExpertOpinionButton importOpinion = new ImportExpertOpinionButton();

  private final FuncButton auditPassButton = new AuditPassButton();

  private final FuncButton suggestPassButton = new SuggestAuditPassButton();

  //工作流终审

  private final FuncButton auditFinalPassButton = new AuditFinalPassButton();

  private final FuncButton callbackButton = new CallbackButton();

  private final FuncButton unTreadButton = new UntreadButton();

  private final FuncButton unAuditButton = new UnauditButton();

  private final FuncButton traceButton = new TraceButton();

  private final FuncButton exitButton = new ExitButton();

  private final FuncButton editButton = new EditButton();

  private final PrintButton printButton = new PrintButton();

  private final PrintPreviewButton printPreviewButton = new PrintPreviewButton();

  private final PrintSettingButton printSettingButton = new PrintSettingButton();

  private final IsSendToNextButton isSendToNextButton = new IsSendToNextButton();

  public FuncButton openNotepadButton = new OpenNotepadButton();

  //同意

  private final FuncButton agreeButton = new AgreeButton();

  //不同意

  private final FuncButton disagreeButton = new DisagreeButton();

  //送协办人审核

  private final FuncButton sendToXieBanButton = new SendToXieBanButton();

  //送采购单位确认

  private final FuncButton sendToProcurementUnitButton = new SendToProcurementUnitButton();

  private final JTablePanel tablePanel1 = new JTablePanel();

  private ZcEbEvalReportOffLineListPanel listPanel;

  private final JTabbedPane tabbedPane = new JTabbedPane();

  private final ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  private String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  private final GkBaseDialog parent;

  private final ElementConditionDto packDto = new ElementConditionDto();

  //上一页、下一页

  private final FuncButton previousButton = new PreviousButton();

  private final FuncButton nextButton = new NextButton();

  // 评标组意见

  private JTextArea bidEvalOpinionArea;

  // 评标结果

  private Window parentWindow;

  private ZcEbEvalReportEditPanel self;

  protected static String getCompoId() {
    // TODO Auto-generated method stub
    return "ZC_EB_EVAL_REPORT_OFF_LINE";
  }

  private final ElementConditionDto elementConditionDto = new ElementConditionDto();

  private final ElementConditionDto bidelementConditionDto = new ElementConditionDto();

  private final RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private final ZcEbEvalPackResultToTableModelConverter modelConverter1 = new ZcEbEvalPackResultToTableModelConverter();

  private final IZcEbEvalServiceDelegate zcEbEvalServiceDelegate = (IZcEbEvalServiceDelegate) ServiceFactory.create(IZcEbEvalServiceDelegate.class,

  "zcEbEvalServiceDelegate");

  private final IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,

  "zcEbBaseServiceDelegate");

  protected IZcEbProjServiceDelegate zcEbProjServiceDelegate = (IZcEbProjServiceDelegate) ServiceFactory.create(IZcEbProjServiceDelegate.class,

  "zcEbProjServiceDelegate");

  private ZcEbEvalReport oldZcEbEvalReport;

  private ForeignEntityFieldEditor packSelectEdit;

  private SelectFileFieldEditor reportAttachFile;

  private SelectFileFieldEditor signReportAttach;

  protected ListCursor listCursor;

  private String sqlMap = "";

  private final IZcEbPlanServiceDelegate zcEbPlanServiceDelegate = (IZcEbPlanServiceDelegate) ServiceFactory.create(IZcEbPlanServiceDelegate.class,

  "zcEbPlanServiceDelegate");

  private final IZcEbEntrustServiceDelegate zcEbEntrustServiceDelegate = (IZcEbEntrustServiceDelegate) ServiceFactory.create(

  IZcEbEntrustServiceDelegate.class, "zcEbEntrustServiceDelegate");

  public IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,

  "baseDataServiceDelegate");

  //标段的状态

  private String packStatus;

  // 采购方式

  private String purtype;

  private static final String DAN_YI = "3";

  protected JTablePanel memberTablePanel = new JTablePanel();

  List memberList = new ArrayList();

  private boolean isManyWinner = false;

  // 是否废标
  private boolean isCancel = false;

  //是否从询价开标界面打开的
  private boolean isFromRfqPanel = false;

  private ZcEbProj proj = null;

  private ZcEbRfqPack rfqPack = null;

  //默认的底部面板
  private JPanel defaultSubPanel = null;

  private static final String CloseDeal = "成交";

  private static final String NotCloseDeal = "未成交";

  private final String sumTabTitle = "供应商询价汇总表";

  private final String listTabTitle = "供应商询价一览表";

  HashMap<String, JComboBox> isCloseDealComboxMap = new HashMap<String, JComboBox>();

  public Window getParentWindow() {

    return parentWindow;

  }

  public void setParentWindow(Window parentWindow) {

    this.parentWindow = parentWindow;

  }

  public String getSql(String str) {

    if (str.equals("Y")) {
      sqlMap = "ZcEbPackResult.getZcEbPackEvalResultList";
    } else {
      sqlMap = "ZcEbPackResult.getBidSupplierList";
    }

    return sqlMap;

  }

  static {

    LangTransMeta.init("ZC%");

  }

  public ZcEbEvalReportOffLineEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcEbEvalReportOffLineListPanel listPanel) {

    super(ZcEbYanShouBill.class, listPanel.getBillElementMeta());

    this.listCursor = listCursor;

    this.listPanel = listPanel;

    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LangTransMeta.translate(getCompoId()),

    TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));

    this.parent = parent;

    this.colCount = 3;

    init();

    requestMeta.setCompoId(getCompoId());

    refreshData();

  }

  /**
   * 从询价开标界面打开评标报告
   * @param parent
   * @param listCursor
   * @param tabStatus
   * @param listPanel
   * @param billElementMeta
   * @param zcEbProj
   * @param zcEbRfqPack 
   */
  public ZcEbEvalReportOffLineEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcEbEvalReportOffLineListPanel listPanel,
    BillElementMeta billElementMeta, ZcEbProj zcEbProj, ZcEbRfqPack zcEbRfqPack) {

    super(ZcEbYanShouBill.class, billElementMeta);

    this.listCursor = listCursor;

    this.listPanel = listPanel;

    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LangTransMeta.translate("评审报告管理"),

    TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));

    this.parent = parent;

    this.colCount = 3;

    this.isFromRfqPanel = true;
    this.proj = zcEbProj;
    this.rfqPack = zcEbRfqPack;

    init();

    requestMeta.setCompoId(getCompoId());

    refreshData();

  }

  public ZcEbEvalReportOffLineEditPanel(GkBaseDialog parent, ListCursor listCursor, ZcEbEvalReport zcEbEvalReport) {

    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LangTransMeta.translate("评审报告管理"),

    TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));

    this.parent = parent;

    this.listCursor = listCursor;

    this.colCount = 3;

    init();

    requestMeta.setCompoId(getCompoId());

    refreshData();

  }

  @Override
  public void init() {

    this.initToolBar(toolBar);

    this.setLayout(new BorderLayout());

    this.add(toolBar, BorderLayout.NORTH);

    this.add(workPanel, BorderLayout.CENTER);

    initFieldEditorPanel();

    tablePanel1.init();

    tablePanel1.getSearchBar().setVisible(false);

    tablePanel1.setPanelId("1");
    workPanel.setLayout(new BorderLayout());

    workPanel.add(fieldEditorPanel, BorderLayout.NORTH);

    workPanel.add(createSubBillPanel(), BorderLayout.CENTER);

  }

  private void createXunJiaPanel() {

    workPanel.remove(defaultSubPanel);

    ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) listCursor.getCurrentObject();

    ElementConditionDto dto = new ElementConditionDto();

    dto.setZcText0(zcEbEvalReport.getProjCode());

    dto.setPackCode(zcEbEvalReport.getPackCode());

    dto.setStatus("done");

    IZcEbXjbjServiceDelegate zcebXjbjServiceDelegate = (IZcEbXjbjServiceDelegate) ServiceFactory.create(IZcEbXjbjServiceDelegate.class,
      "zcebXjbjServiceDelegate");
    List<ZcEbXunJiaBaoJia> xjbjList = zcebXjbjServiceDelegate.getXunJiaBaoJiaByCondition(dto, requestMeta);

    dto.setProjCode(zcEbEvalReport.getProjCode());

    //     获取询价报价的明细
    IZcEbRfqServiceDelegate zcEbRfqServiceDelegate = (IZcEbRfqServiceDelegate) ServiceFactory.create(IZcEbRfqServiceDelegate.class,
      "zcEbRfqServiceDelegate");

    List<ZcXunJiaDetail> xjdList = zcEbRfqServiceDelegate.getXunjiaDetaiList(dto, requestMeta);
    if (xjdList == null) {
      xjdList = new ArrayList<ZcXunJiaDetail>();
    }

    dto = new ElementConditionDto();
    dto.setProjCode(zcEbEvalReport.getProjCode());
    dto.setPackCode(zcEbEvalReport.getPackCode());

    ZcEbRfqPack zcEbRfqPack = zcEbRfqServiceDelegate.getZcEbRfqPack(dto, requestMeta);

    zcEbEvalReport.setProviderCode(zcEbRfqPack.getWinBidProviderCode());
    zcEbEvalReport.setProviderName(zcEbRfqPack.getWinBidProviderName());
    zcEbEvalReport.setBidSum(zcEbRfqPack.getWinBidSum());
    zcEbEvalReport.setPackCode(zcEbRfqPack.getPackCode());
    zcEbEvalReport.setPackName(zcEbRfqPack.getPackName());
    zcEbEvalReport.setBidDate(zcEbRfqPack.getOpenBidDate());

    List<ZcXunJiaSummary> xjSummaryList = collectSameProviderPrice(xjbjList, xjdList, zcEbRfqPack);

    JPanel summaryListPanel = makeSummaryListPanel(xjSummaryList);
    JTabbedPane enquiryListTabPane = makeEnquiryListTabPane(zcEbRfqPack, xjSummaryList, xjdList);

    JTabbedPane tabPane = new JTabbedPane();

    tabPane.addTab(this.sumTabTitle, summaryListPanel);

    tabPane.addTab(this.listTabTitle, enquiryListTabPane);

    workPanel.add(tabPane, BorderLayout.CENTER);

  }

  //创建询价供应商报价详细一览面板

  private JTabbedPane makeEnquiryListTabPane(ZcEbRfqPack zcEbRfqPack, List<ZcXunJiaSummary> xjSummaryList, List<ZcXunJiaDetail> xjdList) {

    JTabbedPane tabPane = new JTabbedPane();

    if (xjSummaryList != null && xjSummaryList.size() > 0) {

      RfqSinupPack spp = null;

      ZcXunJiaSummary sp = null;

      String title = null;

      JPanel p = null;

      String spCode = null;

      String totalPrice = null;

      List<ZcXunJiaDetail> detailsForCurr = null;

      for (int i = 0; i < xjSummaryList.size(); i++) {

        sp = (ZcXunJiaSummary) xjSummaryList.get(i);

        spCode = sp.getProviderCode();

        detailsForCurr = new ArrayList<ZcXunJiaDetail>();

        //找到当前供应商的detail

        for (int j = 0; j < xjdList.size(); j++) {

          if (spCode.equals(xjdList.get(j).getProviderCode())) {

            detailsForCurr.add(xjdList.get(j));

          }

        }

        totalPrice = getTotalMoney(spCode, detailsForCurr);

        title = ZcUtil.substring(sp.getProviderName(), 16, "...") + "(总价:" + totalPrice + ")";

        p = new JPanel(new BorderLayout());

        spp = new RfqSinupPack();

        spp.setProviderCode(sp.getProviderCode());

        spp.setProviderName(sp.getProviderName());

        p.add(createTopInfoPanel(sp, spp, totalPrice, zcEbRfqPack), BorderLayout.NORTH);

        p.add(createEnquiryTablePanel(spp, detailsForCurr), BorderLayout.CENTER);

        tabPane.add(title, p);

      }

      getLowestPrice(xjSummaryList, zcEbRfqPack);

    } else {

      JPanel empty = new JPanel(new BorderLayout());

      JLabel label = new JLabel("当前没有相关数据需要显示...");

      label.setPreferredSize(new Dimension(300, 30));

      empty.add(label, BorderLayout.CENTER);

      tabPane.setLayout(new BorderLayout());

      tabPane.add(empty, BorderLayout.CENTER);

    }

    return tabPane;

  }

  private void getLowestPrice(List<ZcXunJiaSummary> list, ZcEbRfqPack zcEbRfqPack) {

    if (list.size() > 0) {

      ZcXunJiaSummary lowestPrice = getMinPriceProvider(list);

      zcEbRfqPack.setBidMinPriceProvider(lowestPrice.getProviderName());

      zcEbRfqPack.setBidMinPrice(lowestPrice.getTotalPrice());

    }

  }

  /**

   * 获取最低价最早供货的供应商

   * @param list

   * @return

   */

  private ZcXunJiaSummary getMinPriceProvider(List list) {

    ZcXunJiaSummary min = null;

    for (int i = 0; i < list.size(); i++) {

      ZcXunJiaSummary curr = (ZcXunJiaSummary) list.get(i);

      if (min == null) {

        min = curr;

        continue;

      }

      if (curr.getTotalPrice() == null) {

        curr.setTotalPrice(new BigDecimal("0"));

      }

      int compare = min.getTotalPrice().compareTo(curr.getTotalPrice());

      if (compare > 0) {

        min = curr;

      } else if (compare == 0) {

        if (min.getGongHuoDate() != null && curr.getGongHuoDate() != null) {

          if (min.getGongHuoDate().after(curr.getGongHuoDate())) {

            min = curr;

          }

        } else if (min.getGongHuoDate() == null && curr.getGongHuoDate() != null) {

          min = curr;

        }

      }

    }

    return min;

  }

  private JTablePanel createEnquiryTablePanel(RfqSinupPack rfqSinupPack, List<ZcXunJiaDetail> xjdList) {

    JTablePanel enquiryTablePanel = new JTablePanel();

    enquiryTablePanel.init();

    enquiryTablePanel.setTableModel(ZcXunjiaDetailToTableModelConverter.convertSubBiTableData(xjdList));

    enquiryTablePanel.getSearchBar().setVisible(false);

    ZcUtil.translateColName(enquiryTablePanel.getTable(), ZcXunjiaDetailToTableModelConverter.getBillDetailInfo());

    setEnquiryTablePropety(enquiryTablePanel);

    return enquiryTablePanel;

  }

  private void setEnquiryTablePropety(JTablePanel enquiryTablePanel) {

    SwingUtil.setTableCellEditor(enquiryTablePanel.getTable(), "HAVE_XIAN_HUO", new AsValComboBoxCellEditor("VS_Y/N"));

    SwingUtil.setTableCellRenderer(enquiryTablePanel.getTable(), "HAVE_XIAN_HUO", new AsValCellRenderer("VS_Y/N"));

    SwingUtil.setTableCellEditor(enquiryTablePanel.getTable(), "GONG_HUO_DATE", new DateCellEditor());

    AsValComboBoxCellEditor acbc = new AsValComboBoxCellEditor("V_SP_UNIT");

    SwingUtil.setTableCellEditor(enquiryTablePanel.getTable(), "SP_UNIT", acbc);

    SwingUtil.setTableCellRenderer(enquiryTablePanel.getTable(), "SP_UNIT", new AsValCellRenderer("V_SP_UNIT"));

  }

  private JPanel createTopInfoPanel(ZcXunJiaSummary sp, RfqSinupPack signPack, String totalPrice, ZcEbRfqPack zcEbRfqPack) {

    JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 2, 2));

    infoPanel.add(new JLabel("供应商:"));

    JTextField field = new JTextField();

    field.setPreferredSize(new Dimension(200, 20));

    field.setText(signPack.getProviderName());

    field.setEditable(false);

    infoPanel.add(field);

    infoPanel.add(new JLabel("  总报价:"));

    field = new JTextField();

    field.setPreferredSize(new Dimension(120, 20));

    field.setText(totalPrice);

    field.setEditable(false);

    infoPanel.add(field);

    infoPanel.add(new JLabel("(元)  报价时间:"));

    JTextField dField = new JDateTextField();

    dField.setPreferredSize(new Dimension(120, 20));

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    if (sp.getBjDate() != null) {

      dField.setText(sdf.format(sp.getBjDate()));

    } else {

      dField.setText(null);

    }

    dField.setEditable(false);

    dField.setEnabled(false);

    infoPanel.add(dField);

    infoPanel.add(new JLabel("  完成供货时间:"));

    JTextField dField2 = new JTextField();

    dField2.setPreferredSize(new Dimension(80, 20));

    sdf = new SimpleDateFormat("yyyy-MM-dd");

    if (sp.getGongHuoDate() != null) {

      dField2.setText(sdf.format(sp.getGongHuoDate()));

    } else {

      dField2.setText("未提供...");

    }

    dField2.setEditable(false);

    dField2.setEnabled(false);

    infoPanel.add(dField2);

    infoPanel.add(new JLabel("  成交状态:"));

    JComboBox comb = makeComboBox(signPack.getProviderCode());

    if (signPack.getProviderCode() != null && signPack.getProviderCode().equals(zcEbRfqPack.getWinBidProviderCode())) {

      comb.setSelectedItem(CloseDeal);

    } else {

      comb.setSelectedItem(NotCloseDeal);

    }

    comb.setEnabled(false);

    comb.setEditable(false);

    infoPanel.add(comb);

    return infoPanel;

  }

  private JComboBox makeComboBox(String combID) {

    JComboBox comb = new JComboBox();

    comb.setName(combID);

    comb.addItem(CloseDeal);

    comb.addItem(NotCloseDeal);

    //将引用存起来，这样方便在汇总页中修改成交商家后，可以直接使用这里的引用对对应的对象进行update.

    isCloseDealComboxMap.put(combID, comb);

    return comb;

  }

  private String getTotalMoney(String spCode, List<ZcXunJiaDetail> list) {

    String result = null;

    ZcXunJiaDetail detail = null;

    BigDecimal sum = BigDecimal.valueOf(0);

    for (int i = 0; i < list.size(); i++) {

      detail = list.get(i);

      if (spCode.equals(detail.getProviderCode()) && detail.getSpTotalSum() != null) {

        sum = sum.add(detail.getSpTotalSum());

      }

    }

    result = ChangeRMB.doChange(sum.toPlainString());

    return result;

  }

  //创建询价汇总面板

  private JPanel makeSummaryListPanel(List<ZcXunJiaSummary> xjSummaryList) {

    JPanel p = new JPanel(new BorderLayout());

    p.add(createSummaryTablePanel(xjSummaryList), BorderLayout.CENTER);

    return p;

  }

  //填充询价汇总面板

  private JTablePanel createSummaryTablePanel(List<ZcXunJiaSummary> xjSummaryList) {

    JTablePanel summaryTablePanel = new JTablePanel();

    summaryTablePanel.init();

    summaryTablePanel.setTableModel(ZcXunjiaDetailToTableModelConverter.convertSummaryTableData(xjSummaryList,
      ZcXunjiaDetailToTableModelConverter.getSummaryInfo()));

    summaryTablePanel.getSearchBar().setVisible(false);

    ZcUtil.translateColName(summaryTablePanel.getTable(), ZcXunjiaDetailToTableModelConverter.getSummaryInfo());

    setXunJiaSummaryTableProperty(summaryTablePanel);

    return summaryTablePanel;

  }

  private void setXunJiaSummaryTableProperty(JTablePanel summaryTablePanel) {

    SwingUtil.setTableCellEditor(summaryTablePanel.getTable(), "IS_CLOSED_DEAL", new AsValComboBoxCellEditor("VS_Y/N"));

    SwingUtil.setTableCellRenderer(summaryTablePanel.getTable(), "IS_CLOSED_DEAL", new AsValCellRenderer("VS_Y/N"));

    SwingUtil.setTableCellEditor(summaryTablePanel.getTable(), "HAVE_XIAN_HUO", new AsValComboBoxCellEditor("VS_Y/N"));

    SwingUtil.setTableCellRenderer(summaryTablePanel.getTable(), "HAVE_XIAN_HUO", new AsValCellRenderer("VS_Y/N"));

    SwingUtil.setTableCellEditor(summaryTablePanel.getTable(), "GONG_HUO_DATE", new DateCellEditor());

    SwingUtil.setTableCellRenderer(summaryTablePanel.getTable(), "BJ_DATE", new DateCellRenderer(DateCellRenderer.SS));

  }

  //根据供应商的报价明细，按每个供应商汇总生成汇总结果。
  private List<ZcXunJiaSummary> collectSameProviderPrice(List<ZcEbXunJiaBaoJia> xjbjList, List<ZcXunJiaDetail> xjdList, ZcEbRfqPack zcEbRfqPack) {

    ZcXunJiaSummary sum = null;
    List<ZcXunJiaSummary> xjSummaryList = new ArrayList<ZcXunJiaSummary>();

    //如果没有中标人，那么暂时将暂时中标人设置为一个不可能存在的值

    String winnerCode = zcEbRfqPack.getWinBidProviderCode() == null ? "##@##" : zcEbRfqPack.getWinBidProviderCode();

    /**

     * 先按供应商进行分组，同一个供应商的报价明细分到一组，然后汇总每个供应商报价的总和。

     */

    for (int n = 0; n < xjbjList.size(); n++) {

      ZcEbXunJiaBaoJia bj = xjbjList.get(n);

      sum = new ZcXunJiaSummary();

      List<ZcXunJiaDetail> xunJiaDetailList = new ArrayList<ZcXunJiaDetail>();

      for (int m = 0; m < xjdList.size(); m++) {

        ZcXunJiaDetail xj1 = xjdList.get(m);

        if (bj.getProjCode().equals(xj1.getProjCode()) && bj.getSupplierCode().equals(xj1.getProviderCode())) {

          xunJiaDetailList.add(xj1);

        }

      }

      sum.setXunJiaDetailList(xunJiaDetailList);

      //询价报价存在多个明细的话：只要有一个明细有现货，汇总结果就是有现货；供货时间取最早的供货时间

      String haveXianhuo = "N";

      Date gonghuoDate = xunJiaDetailList.get(0).getGongHuoDate();

      BigDecimal totalPrice = new BigDecimal(0.0);

      for (ZcXunJiaDetail detail : xunJiaDetailList) {

        if ("Y".equals(detail.getHaveXianHuo())) {

          haveXianhuo = "Y";

        }

        if (gonghuoDate != null) {

          if (detail.getGongHuoDate() != null && gonghuoDate.after(detail.getGongHuoDate())) {

            gonghuoDate = detail.getGongHuoDate();

          }

        }

        totalPrice = totalPrice.add(detail.getSpTotalSum());

      }

      sum.setHaveXianHuo(haveXianhuo);

      sum.setGongHuoDate(gonghuoDate);

      sum.setIsClosedDeal(winnerCode.equals(bj.getSupplierCode()) ? "Y" : "N");

      sum.setProviderCode(bj.getSupplierCode());

      sum.setProviderName(bj.getSupplierName());

      sum.setTotalPrice(totalPrice);

      sum.setLinkMan(bj.getManager());

      sum.setLinkTel(bj.getPhone());

      sum.setRemark(bj.getRemark());

      sum.setBjDate(bj.getBjDate());

      xjSummaryList.add(sum);

    }
    return xjSummaryList;

  }

  @Override
  public JComponent createSubBillPanel() {

    defaultSubPanel = new JPanel(new GridLayout(2, 1));

    tabbedPane.add("评标结果", tablePanel1);

    /**

     * commentTapPanel的初始化

     */

    JTabbedPane commentTabPane = new JTabbedPane();

    // 评审专家组

    JPanel optionPanel1 = new JPanel(new BorderLayout());

    bidEvalOpinionArea = new JTextArea();

    bidEvalOpinionArea.setRows(3);

    bidEvalOpinionArea.setLineWrap(true);

    JScrollPane groupPanel = new JScrollPane(bidEvalOpinionArea);

    optionPanel1.add(groupPanel, BorderLayout.CENTER);

    commentTabPane.add("评审专家组意见", optionPanel1);
    /**
     * 
     */
    memberTablePanel.init();
    memberTablePanel.getSearchBar().setVisible(false);
    //    commentTabPane.add("评审专家", memberTablePanel);

    defaultSubPanel.add(tabbedPane);

    defaultSubPanel.add(commentTabPane);

    bidEvalOpinionArea.setEditable(true);

    return defaultSubPanel;

  }

  public void refreshData() {

    ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) listCursor.getCurrentObject();

    if (zcEbEvalReport == null) {

      zcEbEvalReport = new ZcEbEvalReport();
      zcEbEvalReport.setStatus("0");
      zcEbEvalReport.setIsOffLine("Y");
      zcEbEvalReport.setExecutor(requestMeta.getSvUserID());
      zcEbEvalReport.setExecutorName(requestMeta.getSvUserName());
      zcEbEvalReport.setExecuteDate(requestMeta.getSysDate());
      zcEbEvalReport.setNd(requestMeta.getSvNd());
      zcEbEvalReport.setOrgCode(requestMeta.getSvOrgCode());
      zcEbEvalReport.setAgency(requestMeta.getSvCoCode());
      zcEbEvalReport.setZcEbPack(new ZcEbPack());
      pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;
      listCursor.setCurrentObject(zcEbEvalReport);
      listCursor.getDataList().add(zcEbEvalReport);
      List resultList = new ArrayList();
      zcEbEvalReport.setPackEvalResultList(resultList);
      refreshSubData();
    }
    if (zcEbEvalReport.getProjCode() != null && zcEbEvalReport.getPackCode() != null) {
      Map<String, String> para = new HashMap<String, String>();
      para.put("packCode", zcEbEvalReport.getPackCode());
      para.put("projCode", zcEbEvalReport.getProjCode());
      ZcEbPack zcEbPack = (ZcEbPack) zcEbBaseServiceDelegate.queryObject("ZcEbProj.getZcEbPackByPackCode", para, requestMeta);
      zcEbEvalReport.setZcEbPack(zcEbPack);
      zcEbEvalReport.setPackStatus(zcEbPack.getStatus());
      zcEbEvalReport.setFailReason(zcEbPack.getFailedReason());
      packStatus = zcEbPack.getStatus();

      if (zcEbEvalReport.getProcessInstId() != null && zcEbEvalReport.getProcessInstId() > 0) {
        pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;
      }
      if (ZcSettingConstants.ZC_CGFS_XJ.equals(zcEbEvalReport.getPurType())) {
        createXunJiaPanel();
      } else {

        elementConditionDto.setPackCode(zcEbEvalReport.getPackCode());
        elementConditionDto.setProjCode(zcEbEvalReport.getProjCode());
        bidelementConditionDto.setPackCode(zcEbEvalReport.getPackCode());
        bidelementConditionDto.setProjectCode(zcEbEvalReport.getProjCode());
        packDto.setDattr1(zcEbEvalReport.getProjCode());
        List resultList = zcEbEvalServiceDelegate.getZcEbPackEvalResultList(elementConditionDto, requestMeta);
        zcEbEvalReport.setPackEvalResultList(resultList);
        refreshSubData();
        if (zcEbEvalReport != null) {
          bidEvalOpinionArea.setText(zcEbEvalReport.getExpertOpinion());
        }
      }
    }
    setEditingObject(zcEbEvalReport);
    setOldObject();
    updateWFEditorEditable(zcEbEvalReport, requestMeta);

    if (WFConstants.AUDIT_TAB_STATUS_CANCEL.equals(zcEbEvalReport.getStatus())) {
      setCancelStatus(listCursor);
    } else {
      setButtonStatus(zcEbEvalReport, requestMeta, listCursor);
    }

    Long processInstId = zcEbEvalReport.getProcessInstId();

    if (processInstId == null || processInstId.longValue() < 0) {

      sendToProcurementUnitButton.setVisible(false);

      sendToXieBanButton.setVisible(false);

      //评标管理->评审报告，草稿数据的功能按钮控制有误
      if (editButton.isVisible()) {
        editButton.setEnabled(false);
      }
    }
    if (isFromRfqPanel) {
      setProj(proj);
      if (proj.getPackList() != null && proj.getPackList().size() > 0) {
        for (Object obj : proj.getPackList()) {
          ZcEbPack pack = (ZcEbPack) obj;
          if (pack.getPackCode().equals(this.rfqPack.getPackCode())) {
            setPack(pack);
            break;
          }
        }
      }
      createXunJiaPanel();
    }
    updateFieldEditorsEditable();
    setButtonStatus();
  }

  public void refreshSubData() {
    ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) listCursor.getCurrentObject();
    tablePanel1.setTableModel(modelConverter1.convertEvalResultTableData(zcEbEvalReport.getPackEvalResultList()));
    ZcUtil.translateColName(tablePanel1.getTable(), ZcEbEvalPackResultToTableModelConverter.getBillDetailInfo());
    setEvalResultTabledetailEditor(tablePanel1.getTable());
    refreshMemberTableData();
  }

  protected void refreshMemberTableData() {
    ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) listCursor.getCurrentObject();
    if (zcEbEvalReport.getProjCode() != null && zcEbEvalReport.getPackCode() != null) {
      Map map = new HashMap();
      map.put("projCode", zcEbEvalReport.getProjCode());
      map.put("packCode", zcEbEvalReport.getPackCode());
      memberList = zcEbBaseServiceDelegate.queryDataForList("ZcEbEvalBidTeam.getEvalBidTeamMemberByMap", map, requestMeta);
    }
    ZcEbEvalBidTeamStToTableModelConverter mc = new ZcEbEvalBidTeamStToTableModelConverter();
    memberTablePanel.setTableModel(mc.convertMembersDataToTableModel(memberList));
    BeanTableModel tableModel = (BeanTableModel) memberTablePanel.getTable().getModel();
    tableModel.setEditable(false);
    setMemberTableProperty(memberTablePanel.getTable());
  }

  private void setEvalResultTabledetailEditor(final JPageableFixedTable table) {

    List list = zcEbBaseServiceDelegate.queryDataForList("ZcEbPackResult.getComBoxList", bidelementConditionDto, requestMeta);

    table.setDefaultEditor(String.class, new TextCellEditor());

    SwingUtil.setTableCellEditor(table, "PROVIDER_CODE", new AsValComboBoxCellEditor("PROVIDER_CODE", list));

    SwingUtil.setTableCellRenderer(table, "PROVIDER_CODE", new AsMapCellRenderer("PROVIDER_CODE", getValMap(list)));

    AsValComboBoxCellEditor asValComboBoxCellEditor = new AsValComboBoxCellEditor("VS_ZC_EB_COMPLIANCE_RESULT");

    SwingUtil.setTableCellEditor(table, "COMPLIANCE_EVAL_VALUE", asValComboBoxCellEditor);

    SwingUtil.setTableCellRenderer(table, "COMPLIANCE_EVAL_VALUE", new AsValCellRenderer("VS_ZC_EB_COMPLIANCE_RESULT"));

    if (DAN_YI.equals(purtype)) {

      NumberCellRenderer numberCellRenderer = new NumberCellRenderer();

      SwingUtil.setTableCellRenderer(table, "EVAL_SCORE", numberCellRenderer);

      SwingUtil.setTableCellEditor(table, "EVAL_SCORE", new MoneyCellEditor(false, false));

    }

    AsValComboBoxCellEditor asValComboBoxCellEditor2 = new AsValComboBoxCellEditor("VS_ZC_EB_EVAL_RESULT");

    SwingUtil.setTableCellEditor(table, "EVALRESULT", asValComboBoxCellEditor2);

    SwingUtil.setTableCellRenderer(table, "EVALRESULT", new AsValCellRenderer("VS_ZC_EB_EVAL_RESULT"));

  }

  protected void setMemberTableProperty(final JPageableFixedTable table) {

    ZcUtil.translateColName(table, "ZC_EB_");
    table.setDefaultEditor(String.class, new TextCellEditor());

    SwingUtil.setTableCellEditor(table, "SEX", new AsValComboBoxCellEditor("VS_SEX"));

    SwingUtil.setTableCellRenderer(table, "SEX", new AsValCellRenderer("VS_SEX"));

    SwingUtil.setTableCellEditor(table, "EXPERT_TYPE_NAME", new ZcEmExpertTypeSelectionCellEditor());

  }

  //将list转成map

  private Map getValMap(List list) {

    Map valMap = new HashMap();

    for (int i = 0; i < list.size(); i++) {

      AsVal asVal = (AsVal) list.get(i);

      valMap.put(asVal.getValId(), asVal.getVal());

    }
    return valMap;

  }

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {

    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    String projColumNames[] = { "项目编号", "项目名称", "预算", "采购类型", "负责人", "立项时间", "是否分包", "电话", "邮件", "传真", "是否发布采购公告", "是否发布采购结果" };

    ZcEbProjFnHandler handler1 = new ZcEbProjFnHandler(projColumNames);

    ElementConditionDto dto = new ElementConditionDto();

    dto.setZcText0(requestMeta.getSvUserID());

    dto.setZcText1("offLine");

    dto.setNd(requestMeta.getSvNd());

    ForeignEntityFieldEditor editor = new ForeignEntityFieldEditor("ZcEbEval.getZcEbProj", dto, 20, handler1, projColumNames, LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_PROJECT_CODE), "projCode");

    editorList.add(editor);

    TextFieldEditor editor1 = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PROJECT_NAME), "projName");

    editor1.setEnabled(false);

    editorList.add(editor1);

    //

    // TextFieldEditor editor2 = new

    // TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PROJECT_COMPANY),

    // "purPeople");

    // editorList.add(editor2);

    String pakeColumNames[] = { "分包编号", "分包名称" };

    ZcEbPackHandler handler2 = new ZcEbPackHandler(pakeColumNames);
    packDto.setZcText0("offLine");
    packSelectEdit = new ForeignEntityFieldEditor("ZcEbEval.getZcEbPack", packDto, 20, handler2, pakeColumNames, LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_PACK_CODE), "packName");

    packSelectEdit.setEnabled(false);

    editorList.add(packSelectEdit);

    MoneyFieldEditor packBudget = new MoneyFieldEditor("分包采购预算", "zcEbPack.packBudget");

    packBudget.setEnabled(false);

    editorList.add(packBudget);

    //    editorList.add(editor1);
    //
    //    editor1.setEnabled(false);

    AsValFieldEditor editor3 = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_PIFU_CGFS), "purType",
      "ZC_VS_PITEM_OPIWAY");

    editorList.add(editor3);

    DateFieldEditor editor4 = new DateFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_BID_DATE), "bidDate", DateField.TimeTypeH24,
      null, true, true, false);

    editorList.add(editor4);

    TextFieldEditor editor5 = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_BID_LOCATION), "bidLocation");

    editorList.add(editor5);

    TextFieldEditor editor6 = new TextFieldEditor("开标负责人", "executorName");
    editor6.setEnabled(false);
    editorList.add(editor6);

    reportAttachFile = new SelectFileFieldEditor("评标报告附件", "reportAttach", "reportAttachBlobid", true, true, true, true);

    reportAttachFile.addValueChangeListener(new ValueChangeListener() {

      public void valueChanged(ValueChangeEvent e) {

        setreportAttachContent();

      }

    });

    editorList.add(reportAttachFile);

    signReportAttach = new SelectFileFieldEditor("签名附件", "signReportAttach", "signReportAttachBlobid", true, true, true, true);

    signReportAttach.addValueChangeListener(new ValueChangeListener() {

      public void valueChanged(ValueChangeEvent e) {

        setsignReportAttachContent();

      }

    });

    editorList.add(signReportAttach);

    //    AsValFieldEditor status = new AsValFieldEditor("单据状态", "status", "ZC_VS_REPORT_STATUS");

    //    editorList.add(status);

    AsValFieldEditor packStatus = new AsValFieldEditor("分包状态", "packStatus","VS_ZC_PACK_STATUS");
    editorList.add(packStatus);
    
    TextAreaFieldEditor fail= new TextAreaFieldEditor("废标原因", "failReason",50,2,5);

    editorList.add(fail);
    return editorList;

  }

  private void setbidEvalOpinionArea() {

    ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) listCursor.getCurrentObject();

    List<ZcEbExpertOpinion> zcEbExpertOpinionList = new ArrayList<ZcEbExpertOpinion>();

    Map<String, String> map = new HashMap<String, String>();

    map.put("projCode", zcEbEvalReport.getProjCode());

    map.put("packCode", zcEbEvalReport.getPackCode());

    zcEbExpertOpinionList = zcEbEvalServiceDelegate.getEbExpertOpinionList(map, requestMeta);

    if (zcEbExpertOpinionList != null && zcEbExpertOpinionList.size() > 0) {

      StringBuffer Buffer = new StringBuffer();

      for (int i = 0; i < zcEbExpertOpinionList.size(); i++) {

        Buffer.append(zcEbExpertOpinionList.get(i).getExpertName() + ":" + "\n" + "\t" + zcEbExpertOpinionList.get(i).getOpinion() + "\n");

      }

      bidEvalOpinionArea.setText(Buffer.toString());

    }

    zcEbEvalReport.setExpertOpinion(bidEvalOpinionArea.getText());

  }

  private void setreportAttachContent() {

    ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) listCursor.getCurrentObject();

    if (this.reportAttachFile == null) {

      zcEbEvalReport.setReportAttachBlobid(null);

      return;

    }

    AsFile bidDoc = this.reportAttachFile.getFileUploader().getFile();

    if (bidDoc == null) {

      zcEbEvalReport.setReportAttachBlobid(null);

      return;

    }

    zcEbEvalReport.setReportAttachBlobid(bidDoc.getFileId());

  }

  private void setOldObject() {

    oldZcEbEvalReport = (ZcEbEvalReport) ObjectUtil.deepCopy(listCursor.getCurrentObject());

  }

  private void setsignReportAttachContent() {

    ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) listCursor.getCurrentObject();

    if (this.signReportAttach == null)

      return;

    AsFile bidDoc = this.signReportAttach.getFileUploader().getFile();

    if (bidDoc == null) {

      return;

    }

    zcEbEvalReport.setSignReportAttachBlobid(bidDoc.getFileId());

  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(getCompoId());

    toolBar.add(editButton);

    toolBar.add(deleteButton);

    toolBar.add(saveButton);

    toolBar.add(sendButton);

    toolBar.add(suggestPassButton);

    //    toolBar.add(importOpinion);

    toolBar.add(callbackButton);

    toolBar.add(auditPassButton);

    //    toolBar.add(isSendToNextButton);

    //    toolBar.add(sendToProcurementUnitButton);

    //    toolBar.add(agreeButton);

    //    toolBar.add(disagreeButton);

    //    toolBar.add(sendToXieBanButton);

    //    toolBar.add(auditFinalPassButton);

    toolBar.add(unAuditButton);

    toolBar.add(unTreadButton);

    toolBar.add(traceButton);

//    toolBar.add(printButton);
//
//    toolBar.add(printSettingButton);
//
//    toolBar.add(openNotepadButton);

    toolBar.add(previousButton);

    toolBar.add(nextButton);

    toolBar.add(exitButton);

    importOpinion.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        setbidEvalOpinionArea();

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

    exitButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doExit();

      }

    });

    saveButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doSave(true);

      }

    });

    sendButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doSend();

      }

    });

    isSendToNextButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doSendNext();

      }

    });

    suggestPassButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        // 填写意见审核

        doSuggestPass();

      }

    });

    auditPassButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doAuditPass();

      }

    });

    unTreadButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doUnTread();

      }

    });

    traceButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doTrace();

      }

    });

    printButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 打印

        doPrint();

      }

    });

    printPreviewButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 打印预览

        doPrintPreview();

      }

    });

    printSettingButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doPrintSetting();

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

        // 销审

        doUnaudit();

      }

    });

    auditFinalPassButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 终审

        doAuditFinal();

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

    previousButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        // TODO Auto-generated method stub

        doPrevious();

      }

    });

    nextButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        // TODO Auto-generated method stub

        doNext();

      }

    });

    openNotepadButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doOpenNotepad();

      }

    });

  }

  private void doOpenNotepad() {

    ZcEbEvalReport sheet = (ZcEbEvalReport) this.listCursor.getCurrentObject();

    String sn = fetchSn(sheet);

    if (sn != null) {

      new ZcNotepadDialog(sn, parent);

    }

  }

  public String fetchSn(ZcEbEvalReport sheet) {

    String sn = null;

    if (sheet.getProjCode() == null || "".equals(sheet.getProjCode())) {

      JOptionPane.showMessageDialog(this, "项目为空不能记录相关信息 ！", "错误", JOptionPane.ERROR_MESSAGE);

      return sn;
    }

    ZcEbProj proj = zcEbProjServiceDelegate.getZcEbProjByProjCode(sheet.getProjCode(), requestMeta);

    if (proj == null) {

      JOptionPane.showMessageDialog(this, "项目为空不能记录相关信息 ！", "错误", JOptionPane.ERROR_MESSAGE);

      return sn;
    }

    List packs = proj.getPackList();

    if (packs == null || packs.size() == 0 || ((ZcEbPack) (packs.get(0))).getEntrustCode() == null) {

      JOptionPane.showMessageDialog(this, "请先创建标段信息，再记录相关信息 ！", "错误", JOptionPane.ERROR_MESSAGE);

      return sn;
    }

    sn = ((ZcEbPack) (packs.get(0))).getEntrustCode();

    return sn;
  }

  public void doPrevious() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave(true)) {

          return;

        }

      } else {

        listCursor.setCurrentObject(oldZcEbEvalReport);

      }

    }

    listCursor.previous();

    refreshData();

    setButtonStatus();

  }

  public void doNext() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave(true)) {

          return;

        }

      } else {

        listCursor.setCurrentObject(oldZcEbEvalReport);

      }

    }

    listCursor.next();

    refreshData();

    setButtonStatus();

  }

  /**

   * 送协办人

   */

  private void doSendToXieBan() {

    ZcEbEvalReport bill = (ZcEbEvalReport) this.listCursor.getCurrentObject();

    requestMeta.setFuncId(this.sendToXieBanButton.getFuncId());

    executeAudit(bill, ZcSettingConstants.SEND_TO_XIE_BAN, null);

  }

  /**

   * 同意

   */

  private void doAgree() {

    ZcEbEvalReport bill = (ZcEbEvalReport) this.listCursor.getCurrentObject();

    requestMeta.setFuncId(this.agreeButton.getFuncId());

    Integer auditFlag = bill.getIsGoonAudit();

    auditFlag = ZcUtil.getAuditFlagValue(auditFlag, 0, requestMeta);

    executeAudit(bill, auditFlag, null);

  }

  /**

   * 不同意

   */

  private void doDisagree() {

    ZcEbEvalReport bill = (ZcEbEvalReport) this.listCursor.getCurrentObject();

    requestMeta.setFuncId(this.disagreeButton.getFuncId());

    Integer auditFlag = bill.getIsGoonAudit();

    auditFlag = ZcUtil.getAuditFlagValue(auditFlag, 1, requestMeta);

    executeAudit(bill, auditFlag, ZcSettingConstants.AUDIT_DISAGREE_DEFULT_MESSAGE);

  }

  /*

   * 填写意见审核

   */

  private void doSuggestPass() {

    ZcEbEvalReport bill = (ZcEbEvalReport) this.listCursor.getCurrentObject();

    requestMeta.setFuncId(this.suggestPassButton.getFuncId());

    String jianShenRoleId = AsOptionMeta.getOptVal("OPT_ZC_CGZX_JSKY_ROLE");//监审组员角色

    if (WorkEnv.getInstance().containRole(jianShenRoleId)) {//如果是监审员，则不修改审批状态

      Integer auditFlag = bill.getIsGoonAudit();

      executeAudit(bill, auditFlag, null);

    } else {

      executeAudit(bill, ZcSettingConstants.IS_GOON_AUDIT_NO, null);

    }

  }

  private void doSendToProcurementUnit() {

    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    GkCommentDialog commentDialog = new GkCommentDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),

    ModalityType.APPLICATION_MODAL);

    if (commentDialog.cancel) {

      return;

    }

    boolean success = true;

    ZcEbEvalReport afterBill = null;

    String errorInfo = "";

    ZcEbEvalReport bill = (ZcEbEvalReport) this.listCursor.getCurrentObject();

    try {

      requestMeta.setFuncId(this.sendToProcurementUnitButton.getFuncId());

      bill.setComment(commentDialog.getComment());

      bill.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      //if (requestMeta.getSvRoleId().equalsIgnoreCase("CGZX_FZR")) {

      bill.setIsGoonAudit(1);

      //}

      zcEbEvalServiceDelegate.saveZcEbEvalReportFN(bill, requestMeta);

      afterBill = zcEbEvalServiceDelegate.sendToProcurementUnitFN(bill, requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      afterBill = zcEbEvalServiceDelegate.getZcEbEvalReport(bill.getReportCode(), requestMeta);

      this.setEditingObject(afterBill);

      refreshData();

      if (listPanel != null) {

        this.listPanel.refreshCurrentTabData();

      }

    } else {

      JOptionPane.showMessageDialog(this, "审核失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  private void doAuditFinal() {

    ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) this.listCursor.getCurrentObject();

    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    requestMeta.setFuncId(auditFinalPassButton.getFuncId());

    executeAudit(zcEbEvalReport, ZcSettingConstants.SEND_TO_FINAL, null);

  }

  private void doUnaudit() {

    boolean success = true;

    ZcEbEvalReport afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(unAuditButton.getFuncId());

      ZcEbEvalReport bill = (ZcEbEvalReport) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      bill.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      bill.setIsGoonAudit(ZcSettingConstants.IS_GOON_AUDIT_NO);

      zcEbEvalServiceDelegate.saveZcEbEvalReportFN(bill, requestMeta);

      zcEbEvalServiceDelegate.unauditFN(bill, requestMeta);

      afterSaveBill = zcEbEvalServiceDelegate.getZcEbEvalReport(bill.getReportCode(), requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "销审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      listCursor.setCurrentObject(afterSaveBill);

      if (listPanel != null) {

        this.listPanel.refreshCurrentTabData();

      }

      refreshData();

    } else {

      JOptionPane.showMessageDialog(this, "销审失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  private void doCallback() {

    ZcEbEvalReport bill = (ZcEbEvalReport) this.listCursor.getCurrentObject();

    boolean success = true;

    ZcEbEvalReport afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.callbackButton.getFuncId());

      bill.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      zcEbEvalServiceDelegate.callbackFN(bill, requestMeta);

      afterSaveBill = zcEbEvalServiceDelegate.getZcEbEvalReport(bill.getReportCode(), requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "收回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      listCursor.setCurrentObject(afterSaveBill);

      if (this.listPanel != null) {

        this.listPanel.refreshCurrentTabData();

      }

      refreshData();

    } else {

      JOptionPane.showMessageDialog(this, "收回失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  /**

   * 

   * @Description: 根据展示列表页面对象的审批状态设置按钮的状态

   * @return void 返回类型

   * @since 1.0

   */

  private void setButtonStatus() {

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

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.sendButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      //bs.addBillStatus("0");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.deleteButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
      bs.addBillStatus("0");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.traceButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.unTreadButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.exitButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_ALL);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

    }
    ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) listCursor.getCurrentObject();

    String billStatus = zcEbEvalReport.getStatus();

    ZcUtil.setButtonEnable(this.btnStatusList, billStatus, this.pageStatus, getCompoId(), zcEbEvalReport.getProcessInstId());

  }

  @Override
  protected void updateFieldEditorsEditable() {

    super.updateFieldEditors();

    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {
        if (fd.getFieldName().equals("projName") || fd.getFieldName().equals("bidDate") || fd.getFieldName().equals("bidLocation")|| fd.getFieldName().equals("packStatus")) {
          fd.setEnabled(false);
          continue;
        }
        fd.setEnabled(true);

      }

    } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_BROWSE)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {

        fd.setEnabled(false);

      }

    } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {

      if (WorkEnv.getInstance().containRole(AsOptionMeta.getOptVal(ZcElementConstants.OPT_ZC_YSDWCG_ROLE))) {

        for (AbstractFieldEditor fd : this.fieldEditors) {

          fd.setEnabled(false);

        }

      } else {

        for (AbstractFieldEditor fd : this.fieldEditors) {
          if (fd.getFieldName().equals("projName") || fd.getFieldName().equals("bidDate") || fd.getFieldName().equals("bidLocation")|| fd.getFieldName().equals("packStatus")) {
            fd.setEnabled(false);
            continue;
          }

          fd.setEnabled(true);

        }

      }

    }

    updateTableEditable();

  }

  private void updateTableEditable() {

    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {

      tablePanel1.getTable().setEnabled(true);

    } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_BROWSE)) {

      tablePanel1.getTable().setEnabled(false);

    } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {

      tablePanel1.getTable().setEnabled(true);

    }

  }

  private void doEdit() {

    this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;

    updateFieldEditorsEditable();

    setButtonStatus();

  }

  public void doDelete() {

    int num = JOptionPane.showConfirmDialog(this, "是否删除当前单据", "删除确认", 0);

    if (num == JOptionPane.YES_OPTION) {

      boolean success = true;

      ZcEbEvalReport bill = null;

      String errorInfo = "";

      try {

        requestMeta.setFuncId(deleteButton.getFuncId());

        bill = (ZcEbEvalReport) this.listCursor.getCurrentObject();

        zcEbEvalServiceDelegate.deleteOfflineZcEbEvalReport(bill, requestMeta);

      } catch (Exception e) {

        logger.error(e.getMessage(), e);

        success = false;

        errorInfo += e.getMessage();

      }

      if (success) {

        this.listCursor.removeCurrentObject();

        JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        refreshData();

        if (this.listPanel != null) {

          this.listPanel.refreshCurrentTabData();

        }

      } else {

        JOptionPane.showMessageDialog(this, "删除失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }
  }

  public boolean doSave(boolean isShowConfirm) {
    ZcEbEvalReport afterBill = (ZcEbEvalReport) this.listCursor.getCurrentObject();
    if (ZcSettingConstants.ZC_CGFS_XJ.equals(afterBill.getPurType())) {
      JOptionPane.showMessageDialog(this, "不支持询价的线下评标报告！", "提示", JOptionPane.INFORMATION_MESSAGE);
      return false;
    }
    if (!checkBeforeSave()) {
      return false;
    }

    boolean success = true;

    try {
      afterBill.setExpertOpinion(bidEvalOpinionArea.getText());
      afterBill=zcEbEvalServiceDelegate.saveOffLineEvalResultFN(afterBill, requestMeta);

    } catch (BaseException ex) {

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

      return false;

    }

    if (success && isShowConfirm) {

      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

      this.listCursor.setCurrentObject(afterBill);

      refreshData();

      if (listPanel != null) {

        this.listPanel.refreshCurrentTabData();

      }

      setButtonStatus();

      return true;

    }

    return false;

  }

  public void doSend() {

    //送审前先保存

    this.doSave(false);

    ZcEbEvalReport bill = (ZcEbEvalReport) this.listCursor.getCurrentObject();

    boolean success = true;

    ZcEbEvalReport afterBill;

    try {

      GkCommentDialog commentDialog = new GkCommentDialog(this.getParentWindow(), ModalityType.APPLICATION_MODAL);

      if (commentDialog.cancel) {

        return;

      }

      bill.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      bill.setComment(commentDialog.getComment());

      requestMeta.setFuncId(this.sendButton.getFuncId());

      zcEbEvalServiceDelegate.newCommitFN(bill, requestMeta);

    } catch (BaseException ex) {

      success = false;

      logger.error(ex.getStackTraceMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    } catch (OtherException ex) {

      success = false;

      logger.error(ex.getStackTraceMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    } catch (Exception ex) {

      success = false;

      logger.error(ex.getMessage(), ex);

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "送审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      afterBill = zcEbEvalServiceDelegate.getZcEbEvalReport(bill.getReportCode(), requestMeta);

      listCursor.setCurrentObject(afterBill);

      if (listPanel != null) {

        this.listPanel.refreshCurrentTabData();

      }

      refreshData();

    } else {

      JOptionPane.showMessageDialog(this, "送审失败", "提示", JOptionPane.INFORMATION_MESSAGE);

    }
  }

  /**
   * 
   * 是否送主任审核
   */

  private void doSendNext() {

    ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) this.listCursor.getCurrentObject();

    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    requestMeta.setFuncId(isSendToNextButton.getFuncId());

    executeAudit(zcEbEvalReport, ZcSettingConstants.IS_GOON_AUDIT_YES, null);

  }

  private void executeAudit(ZcEbEvalReport report, Integer isGoonAudit, String defaultMsg) {

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

    ZcEbEvalReport afterBill;

    boolean success = true;

    String errorInfo = "";

    try {

      isGoonAudit = isGoonAudit == null ? 0 : isGoonAudit;

      report.setIsGoonAudit(isGoonAudit);

      report.setComment(commentDialog.getComment());

      report.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      zcEbEvalServiceDelegate.saveZcEbEvalReportFN(report, requestMeta);

      afterBill = zcEbEvalServiceDelegate.auditFN(report, requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "审核成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      afterBill = zcEbEvalServiceDelegate.getZcEbEvalReport(report.getReportCode(), requestMeta);

      this.listCursor.setCurrentObject(afterBill);

      refreshData();

      if (listPanel != null) {

        this.listPanel.refreshCurrentTabData();

      }

    } else {

      JOptionPane.showMessageDialog(this, "审核失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  /**

   * 

   * @Description: 审核

   * @return void 返回类型

   * @since 1.0

   */

  public void doAuditPass() {

    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    requestMeta.setFuncId(auditPassButton.getFuncId());

    ZcEbEvalReport bill = (ZcEbEvalReport) this.listCursor.getCurrentObject();

    executeAudit(bill, ZcSettingConstants.IS_GOON_AUDIT_NO, null);

  }

  /**

   * 

   * @Description: 流程跟踪

   * @return void 返回类型

   * @since 1.0

   */

  private void doTrace() {

    ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) this.listCursor.getCurrentObject();

    if (null != zcEbEvalReport) {

      ZcUtil.showTraceDialog(zcEbEvalReport, getCompoId());

    } else {

      JOptionPane.showMessageDialog(this, "流程跟踪对象不能为空！", "提示", JOptionPane.INFORMATION_MESSAGE);

    }

  }

  /**

   * 

   * @Description: 退回

   * @return void 返回类型

   * @since 1.0

   */

  public void doUnTread() {

    ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) this.listCursor.getCurrentObject();

    boolean success = true;

    ZcEbEvalReport afterBill = null;

    try {

      requestMeta.setFuncId(this.unTreadButton.getFuncId());

      GkCommentUntreadDialog commentDialog = new GkCommentUntreadDialog(this.getParentWindow(), ModalityType.APPLICATION_MODAL);

      if (commentDialog.cancel) {

        return;

      }

      zcEbEvalReport.setComment("退回:" + commentDialog.getComment());

      afterBill = zcEbEvalServiceDelegate.untreadFN(zcEbEvalReport, requestMeta);

      afterBill = zcEbEvalServiceDelegate.getZcEbEvalReport(afterBill.getReportCode(), requestMeta);

    } catch (BaseException ex) {

      success = false;

      logger.error(ex.getStackTraceMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    } catch (OtherException ex) {

      success = false;

      logger.error(ex.getStackTraceMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    } catch (Exception ex) {

      success = false;

      logger.error(ex.getMessage(), ex);

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "处理成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listCursor.setCurrentObject(afterBill);

      refreshData();

      if (listPanel != null) {

        this.listPanel.refreshCurrentTabData();

      }

    } else {

      JOptionPane.showMessageDialog(this, "该评审报告还未审批不允许退回！", "提示", JOptionPane.INFORMATION_MESSAGE);

    }

  }

  private void doPrint() {

    ZcEbEvalReport bill = (ZcEbEvalReport) this.listCursor.getCurrentObject();
    if (bill.getReportCode() == null || bill.getReportCode().length() == 0) {
      JOptionPane.showMessageDialog(this, "请先保存评审数据再进行打印", "错误", 0);
      return;
    }

    this.requestMeta.setFuncId(this.printPreviewButton.getFuncId());

    this.requestMeta.setPageType(getCompoId() + "_L");

    try {

      String condition = " t.REPORT_CODE='" + bill.getReportCode() + "'";

    } catch (Exception e) {

      logger.error(e.getMessage(), e);

      JOptionPane.showMessageDialog(this, "打印出错！\n" + e.getMessage(), "错误", 0);

    }

  }

  private void doPrintPreview() {

    ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) this.listCursor.getCurrentObject();

    this.requestMeta.setFuncId(this.printPreviewButton.getFuncId());

    this.requestMeta.setPageType(getCompoId() + "_L");

    try {

      String reportCode = zcEbEvalReport.getReportCode();

      String url = WorkEnv.getInstance().getWebRoot() + "app/page/eval/toEvalReportPrint.do?reportCode=" + reportCode;

      ZcUtil.anyBrowse(url);

    } catch (Exception e) {

      logger.error(e.getMessage(), e);

      JOptionPane.showMessageDialog(this, "打印预览出错！\n" + e.getMessage(), "错误", 0);

    }

  }

  private void getValObject() {

    ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) this.listCursor.getCurrentObject();

    zcEbEvalReport.setPurTypeVal(AsValDataCache.getName("ZC_EB_PUR_TYPE", zcEbEvalReport.getPurType()));

    for (int i = 0; i < zcEbEvalReport.getPackEvalResultList().size(); i++) {

      ZcEbPackEvalResult zcEbPackEvalResult = (ZcEbPackEvalResult) zcEbEvalReport.getPackEvalResultList().get(i);

      zcEbPackEvalResult.setEvalResultVal(AsValDataCache.getName("VS_ZC_EB_EVAL_RESULT", zcEbPackEvalResult.getEvalResult()));

      if (new BigDecimal("0").equals(zcEbPackEvalResult.getEvalScore()))

        zcEbPackEvalResult.setEvalScore(null);

      zcEbPackEvalResult.setComplianceUnpassReason(AsValDataCache.getName("Y/N", zcEbPackEvalResult.getComplianceUnpassReason()));

    }

  }

  public boolean checkBeforeSave() {

    ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) this.listCursor.getCurrentObject();

    if (null == zcEbEvalReport.getProjCode() || "".equals(zcEbEvalReport.getProjCode())) {

      JOptionPane.showMessageDialog(this, "请选择一个采购项目", "提示", JOptionPane.INFORMATION_MESSAGE);

      return false;

    }

    if (null == zcEbEvalReport.getPackCode() || "".equals(zcEbEvalReport.getPackCode())) {

      JOptionPane.showMessageDialog(this, "请选择分包", "提示", JOptionPane.INFORMATION_MESSAGE);

      return false;

    }

    if (null == zcEbEvalReport.getReportCode()) {

      if (reportIsExists(zcEbEvalReport.getProjCode(), zcEbEvalReport.getPackCode())) {

        JOptionPane.showMessageDialog(null, "已经存在该项目： " + zcEbEvalReport.getProjName() + zcEbEvalReport.getPackName() + " 的评标报告!");

        return false;

      }

    }    
    if (!setWinProvider(zcEbEvalReport)) {
      return false;
    }//if (zcEbEvalReport.getFailReason()!=null && zcEbEvalReport.getFailReason().trim().length()>0) {
    if (!ZcSettingConstants.ZC_CGFS_XJ.equals(zcEbEvalReport.getPurType()) && (zcEbEvalReport.getFailReason()==null || zcEbEvalReport.getFailReason().trim().length()==0) ) {

      if ((null == bidEvalOpinionArea.getText() || "".equals(bidEvalOpinionArea.getText())) && zcEbEvalReport.getReportAttachBlobid() == null) {
        JOptionPane.showMessageDialog(this, "评审专家组意见或评审报告附件必须有一个填写！", "提示", JOptionPane.INFORMATION_MESSAGE);
        return false;
      }
    }
    if (zcEbEvalReport.getFailReason()!=null && zcEbEvalReport.getFailReason().trim().length()>0) {
      zcEbEvalReport.setPackStatus(ZcEbPack.PACK_STATUS_CRAP);
    }else{
      zcEbEvalReport.setPackStatus(ZcEbPack.PACK_STATUS_COMPLETED);
    }
    return true;

  }

  public boolean setWinProvider(ZcEbEvalReport zcEbEvalReport) {
    if (ZcSettingConstants.ZC_CGFS_XJ.equals(zcEbEvalReport.getPurType())) {
      return true;
    }

    List resultList = zcEbEvalReport.getPackEvalResultList();
    zcEbEvalReport.setProviderCode("");
    zcEbEvalReport.setProviderName("");
    zcEbEvalReport.setBidSum(null);
    //是否是定点采购，允许有多个预中标供应商
    if (zcEbEvalReport.getFailReason()!=null && zcEbEvalReport.getFailReason().trim().length()>0) {
      int size = 0;
      for (int i = 0; i < resultList.size(); i++) {
        ZcEbPackEvalResult result = (ZcEbPackEvalResult) resultList.get(i);
        if (ZcEbPack.PACK_EVAL_STATUS_YU_ZHONGBIAO.equals(result.getEvalResult()) || ZcEbPack.PACK_EVAL_STATUS_ZHONGBIAO.equals(result.getEvalResult())) {
          size++;
        }
      }
      if (size > 0) {
        JOptionPane.showMessageDialog(this, "如果选择了中标供应商，则不能填写废标原因！", "提示", JOptionPane.INFORMATION_MESSAGE);
        return false;
      } else {
        return true;
      }

    } else if (isManyWinner) {
      int size = 0;
      for (int i = 0; i < resultList.size(); i++) {
        ZcEbPackEvalResult result = (ZcEbPackEvalResult) resultList.get(i);
        if (ZcEbPack.PACK_EVAL_STATUS_YU_ZHONGBIAO.equals(result.getEvalResult())) {
          size++;
        }
      }
      if (size == 0) {
        JOptionPane.showMessageDialog(this, "请选择中标供应商，如果是废标，则请填充废标原因！", "提示", JOptionPane.INFORMATION_MESSAGE);
        return false;
      } else {
        return true;
      }

    } else {
      int zb = 0;
      int yzb = 0;
      for (int i = 0; i < resultList.size(); i++) {
        ZcEbPackEvalResult result = (ZcEbPackEvalResult) resultList.get(i);
        if (ZcEbPack.PACK_EVAL_STATUS_YU_ZHONGBIAO.equals(result.getEvalResult())) {
          yzb++;
          zcEbEvalReport.setProviderCode(result.getProviderCode());
          zcEbEvalReport.setProviderName(result.getProviderName());
          zcEbEvalReport.setBidSum(result.getProviderTotalPrice());
        }
      }

      for (int i = 0; i < resultList.size(); i++) {
        ZcEbPackEvalResult result = (ZcEbPackEvalResult) resultList.get(i);
        if (ZcEbPack.PACK_EVAL_STATUS_ZHONGBIAO.equals(result.getEvalResult())) {
          zb++;
          zcEbEvalReport.setProviderCode(result.getProviderCode());
          zcEbEvalReport.setProviderName(result.getProviderName());
          zcEbEvalReport.setBidSum(result.getProviderTotalPrice());
        }
      }
      if ((zb + yzb) == 1) {
        return true;
      } else {
        JOptionPane.showMessageDialog(this, "请选择中标供应商，如果是废标，则请填充废标原因！", "提示", JOptionPane.INFORMATION_MESSAGE);
        return false;
      }
    }

  }

  public void doExit() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave(true)) {

          return;

        }

      }

    }

    this.parent.dispose();

  }

  private void doPrintSetting() {

    this.requestMeta.setFuncId(this.printSettingButton.getFuncId());

    this.requestMeta.setPageType(getCompoId() + "_L");

    new PrintSettingDialog(this.requestMeta);

  }

  public boolean isDataChanged() {

    //    return !DigestUtil.digest(oldZcEbEvalReport).equals(DigestUtil.digest(this.getEditingObject()));

    return false;

  }

  private boolean reportIsExists(String ProjCode, String packCode) {

    ElementConditionDto dto = new ElementConditionDto();

    dto.setProjCode(ProjCode);

    dto.setPackCode(packCode);

    List list = new ArrayList();

    list = zcEbEvalServiceDelegate.getZcEbEvalReportList(dto, requestMeta);

    if (null != list && list.size() > 0) {

      return true;

    }

    return false;

  }

  public void setProj(ZcEbProj proj) {

    if (proj == null)
      return;

    ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) listCursor.getCurrentObject();

    zcEbEvalReport.setProjCode(proj.getProjCode());

    zcEbEvalReport.setProjName(proj.getProjName());
    
    zcEbEvalReport.setExecutor(proj.getAttn());
    
    zcEbEvalReport.setExecutorName(proj.getAttnName());

    ElementConditionDto dto = new ElementConditionDto();

    dto.setProjCode(proj.getProjCode());

    List list = zcEbPlanServiceDelegate.getZcEbPlan(dto, requestMeta);

    if (list != null && list.size() > 0) {

      ZcEbPlan temp = (ZcEbPlan) list.get(0);

      zcEbEvalReport.setBidLocation(temp.getOpenBidAddress());

      zcEbEvalReport.setBidDate(temp.getOpenBidTime());

    }

    setEditingObject(zcEbEvalReport);

    elementConditionDto.setProjectCode(proj.getProjCode());

    bidelementConditionDto.setProjectCode(proj.getProjCode());

    packDto.setDattr1(proj.getProjCode());

    packSelectEdit.setEnabled(true);
  }

  // 设置选择项目的web实体

  private class ZcEbProjFnHandler implements IForeignEntityHandler {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private final String columNames[];

    public ZcEbProjFnHandler(String columNames[]) {

      this.columNames = columNames;

    }

    public void excute(List selectedDatas) {

      for (Object object : selectedDatas) {
        ZcEbProj proj = (ZcEbProj) object;

        setProj(proj);

      }

    }

    /*

     * 清空外部实体对应的数据

     */

    public void afterClear() {

      ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) listCursor.getCurrentObject();

      zcEbEvalReport.setProjCode(null);

      zcEbEvalReport.setPackCode(null);

      zcEbEvalReport.setPackName(null);

      zcEbEvalReport.setProjName(null);

      zcEbEvalReport.setBidLocation(null);

      zcEbEvalReport.setBidDate(null);

      packDto.setDattr1(null);

      elementConditionDto.setProjectCode(null);

      bidelementConditionDto.setProjectCode(null);

      packSelectEdit.setEnabled(false);

      List resultList = new ArrayList();

      zcEbEvalReport.setPackEvalResultList(resultList);

      setEditingObject(zcEbEvalReport);

      refreshData();

    }

    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        ZcEbProj rowData = (ZcEbProj) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getProjCode();

        data[i][col++] = rowData.getProjName();

        data[i][col++] = rowData.getProjSum();

        data[i][col++] = AsValDataCache.getName("ZC_EB_PUR_TYPE", rowData.getPurType());

        data[i][col++] = rowData.getManager();

        data[i][col++] = sdf.format(rowData.getProjDate());

        data[i][col++] = AsValDataCache.getName("ZC_VS_YN", rowData.getIsSplitPack());

        data[i][col++] = rowData.getPhone();

        data[i][col++] = rowData.getEmail();

        data[i][col++] = rowData.getFax();

        data[i][col++] = AsValDataCache.getName("ZC_VS_YN", rowData.getIsPubPurBulletin());

        data[i][col++] = AsValDataCache.getName("ZC_VS_YN", rowData.getIsPubPurResult());

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

    /*

     * 设置外部实体数据条件

     */

    public Boolean beforeSelect(ElementConditionDto dto) {
      ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) listCursor.getCurrentObject();
      if (zcEbEvalReport.getProjCode() != null) {
        int result = JOptionPane.showConfirmDialog(self, "确认重新选择项目吗?", "选择确认", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
          listCursor.setCurrentObject(null);
          refreshData();
        }else{
          return false;
        }
      }
      return true;
    }

  }

  public void setPack(ZcEbPack pack) {
    if (pack == null)
      return;

    ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) listCursor.getCurrentObject();

    zcEbEvalReport.setPackCode(pack.getPackCode());

    zcEbEvalReport.setPackName(pack.getPackName());

    zcEbEvalReport.setPurType(pack.getPurType());
    isCancel = ZcEbPack.PACK_STATUS_CRAP.equals(pack.getStatus());
    
    zcEbEvalReport.setPackStatus(pack.getStatus());
    
    zcEbEvalReport.setFailReason(pack.getFailedReason());

    if (zcEbEvalReport.getZcEbPack() == null) {
      zcEbEvalReport.setZcEbPack(new ZcEbPack());
    }
    zcEbEvalReport.getZcEbPack().setPackBudget(pack.getPackBudget());

    //设置采购人编号

    ZcEbEntrust zcEbEntrust = zcEbEntrustServiceDelegate.getZcEbtrustBySn(pack.getEntrustCode(), requestMeta);
    //协议采购可以是多人中标
    isManyWinner = "Z02".equals(zcEbEntrust.getZcFukuanType());
    zcEbEvalReport.setCoCode(zcEbEntrust.getCoCode());

    if (!"".equals(zcEbEvalReport.getCoCode()) && zcEbEvalReport.getCoCode() != null) {

      Company company = baseDataServiceDelegate.getCompanyByCoCode(requestMeta.getSvNd(), zcEbEvalReport.getCoCode(), requestMeta);
      if (company != null) {
        zcEbEvalReport.setCoName(company.getName());
      }
    }
    zcEbEvalReport.setZcCoLinkMan(zcEbEntrust.getZcMakeLinkman());
    zcEbEvalReport.setAgency(zcEbEntrust.getAgency());
    Map<String, String> map = new HashMap<String, String>();
    map.put("proj_code", zcEbEvalReport.getProjCode());
    map.put("pack_code", pack.getPackCode());
    map.put("off_line", "off_line");
    List<ZcEbPackEvalResult> list = zcEbEvalServiceDelegate.getZcEbPackEvalResult(map, requestMeta);
    for (int i = 0; i < list.size(); i++) {
      ZcEbPackEvalResult zcEbEvalReportResult = list.get(i);
      zcEbEvalReportResult.setIsComplianceResult("Y");
      zcEbEvalReportResult.setTempId(Guid.genID());
    }
    zcEbEvalReport.setPackEvalResultList(list);
    // zcEbEvalReport.setBidLocation(pack.getBidLocation());
    elementConditionDto.setPackCode(pack.getPackCode());
    bidelementConditionDto.setPackCode(pack.getPackCode());
    setEditingObject(zcEbEvalReport);

    refreshSubData();
  }

  // 设置选择分包的web实体

  private class ZcEbPackHandler implements IForeignEntityHandler {

    private final String columNames[];

    public ZcEbPackHandler(String columNames[]) {

      this.columNames = columNames;

    }

    /*

     * 设置外部实体数据条件

     */

    public Boolean beforeSelect(ElementConditionDto dto) {

      ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) listCursor.getCurrentObject();

      if (null == zcEbEvalReport.getProjCode()) {

        JOptionPane.showMessageDialog(self, "请先选择一个采购项目 ！", "提示", JOptionPane.INFORMATION_MESSAGE);

        return false;

      }
      zcEbEvalReport.setPackEvalResultList(new ArrayList<ZcEbPackEvalResult>());
      refreshSubData();

      return true;

    }

    /*

     * 清空外部实体对应的数据

     */

    public void afterClear() {

      ZcEbEvalReport zcEbEvalReport = (ZcEbEvalReport) listCursor.getCurrentObject();

      zcEbEvalReport.setPackCode(null);

      zcEbEvalReport.setPackName(null);

      zcEbEvalReport.setPurType(null);

      zcEbEvalReport.setCoCode(null);

      zcEbEvalReport.setCoName(null);

      zcEbEvalReport.setZcCoLinkMan(null);

      zcEbEvalReport.setAgency(null);

      elementConditionDto.setPackCode(null);

      bidelementConditionDto.setPackCode(null);

      List resultList = new ArrayList();

      zcEbEvalReport.setPackEvalResultList(resultList);

      setEditingObject(zcEbEvalReport);

      refreshData();

    }

    public void excute(List selectedDatas) {

      for (Object object : selectedDatas) {
        ZcEbPack pack = (ZcEbPack) object;
        setPack(pack);
      }

    }

    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        ZcEbPack rowData = (ZcEbPack) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getPackName();

        data[i][col++] = rowData.getPackDesc();

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
