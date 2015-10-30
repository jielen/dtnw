package com.ufgov.zc.client.zc.zcxmcghtzx_backup;

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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;

import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.JGroupableTableHeader;
import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.ZcEbEntrustToTableModelConverter;
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
import com.ufgov.zc.client.component.button.OpenNotepadButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.PrintButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SaveSendButton;
import com.ufgov.zc.client.component.button.SelectMoldButton;
import com.ufgov.zc.client.component.button.SendButton;
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
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AutoNumFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.FileFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.IntFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.util.StringUtil;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.WordFileUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.notepad.ZcNotepadDialog;
import com.ufgov.zc.client.zc.zcxmcght.AbstractZcXmcgHtEditPanel;
import com.ufgov.zc.client.zc.zcxmcght.BiddingWinnerHandler;
import com.ufgov.zc.client.zc.zcxmcght.ZcEbWordMoldFnHandler;
import com.ufgov.zc.client.zc.zcxmcght.ZcPackSelectedHandler;
import com.ufgov.zc.client.zc.ztb.ChangeRMB;
import com.ufgov.zc.client.zc.ztb.activex.WordPane;
import com.ufgov.zc.common.budget.model.VwBudgetGp;
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
import com.ufgov.zc.common.system.util.StringTools;
import com.ufgov.zc.common.system.util.Utils;
import com.ufgov.zc.common.zc.model.SupplierPriceDetail;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcEbBulletinWordMold;
import com.ufgov.zc.common.zc.model.ZcEbNotice;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.model.ZcEbProj;
import com.ufgov.zc.common.zc.model.ZcEbProjZbFile;
import com.ufgov.zc.common.zc.model.ZcEbSupplier;
import com.ufgov.zc.common.zc.model.ZcHtModel;
import com.ufgov.zc.common.zc.model.ZcHtPrePayBillItem;
import com.ufgov.zc.common.zc.model.ZcPProMitem;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;
import com.ufgov.zc.common.zc.model.ZcTBchtItem;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.model.ZcXmcgHtBi;
import com.ufgov.zc.common.zc.model.ZcebZbItem;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbProjServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbSupplierServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcXmcgHtServiceDelegate;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**

 * 

 * @author Administrator

 *

 */

public class ZcXmcgHtEditPanel extends AbstractZcXmcgHtEditPanel {

  public static final String PATH = ZcUtil.dir + "ht/";

  private static final long serialVersionUID = -4923939712990784916L;

  private static final String CONTRACT_TEMPLETE_AS_FILE_ID_EXT = "_view";

  private static final Logger logger = Logger.getLogger(ZcXmcgHtEditPanel.class);

  protected String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  private JSaveableSplitPane myWordPane;

  //合同模板，真实客户模板file_id和给客户显示模板file_id的后缀对应
  protected RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

//  private String compoId = "ZC_XMCG_HT";

  protected FuncButton saveButton = new SaveButton();

  protected FuncButton editButton = new EditButton();

  protected FuncButton addButton = new AddButton();

  private FuncButton traceButton = new TraceButton();

  protected FuncButton callbackButton = new CallbackButton();

  protected FuncButton deleteButton = new DeleteButton();

  private FuncButton previousButton = new PreviousButton();

  private FuncButton nextButton = new NextButton();

  private FuncButton exitButton = new ExitButton();

  public FuncButton openNotepadButton = new OpenNotepadButton();

  private FuncButton saveAndSendButton = new SaveSendButton();

  protected FuncButton sendButton = new SendButton();

  protected LoadMoldButton loadMoldButton = new LoadMoldButton();

  protected SelectMoldButton selectMoldButton = new SelectMoldButton();

  public FuncButton isSendToNextButton = new IsSendToNextButton();

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

  private byte[] oldHtWord;

  private ZcXmcgHtListPanel listPanel;

  protected JTablePanel biTablePanel = new JTablePanel();

  protected JTablePanel itemTablePanel = new JTablePanel();

  protected JTablePanel detailTablePanel = new JTablePanel();

  protected ZcXmcgHtEditPanel self = this;

  protected GkBaseDialog parent;

  private ElementConditionDto dtoForBidWinner = new ElementConditionDto();

  private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  protected com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor biddingWinner = null;

  //  private com.ufgov.zc.client.component.zc.fieldeditor.foreignentity.ForeignEntityFieldEditor zcSuName = null;

  private BillElementMeta mainBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd("ZC_XMCG_HT");

  //  private BillElementMeta biBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd("ZC_XMCG_HT_BI");

  private BillElementMeta itemBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd("ZC_T_BCHT_ITEM");

  private String moldColumNames[] = { "模板编号", "模板名称", "公告模板类型", "状态", "备注" };

  private ZcEbWordMoldFnHandler handlera = new ZcEbWordMoldFnHandler(moldColumNames, self);

  protected IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,

  "zcEbBaseServiceDelegate");

  public IZcXmcgHtServiceDelegate zcXmcgHtServiceDelegate = (IZcXmcgHtServiceDelegate) ServiceFactory.create(IZcXmcgHtServiceDelegate.class,

  "zcXmcgHtServiceDelegate");

  protected IZcEbProjServiceDelegate zcEbProjServiceDelegate = (IZcEbProjServiceDelegate) ServiceFactory.create(IZcEbProjServiceDelegate.class,

  "zcEbProjServiceDelegate");

  private ElementConditionDto moldDto = new ElementConditionDto();

  private ForeignEntityFieldEditor fieldMoldName = new ForeignEntityFieldEditor("ZcEbBulletinWordMold.getZcGongHuoHtWordMold", moldDto, 20, handlera,

  moldColumNames, "载入模板", "moldName");

  protected WordPane wordPane = new WordPane();

  protected WordPane noticeWordPane = new WordPane();

  protected WordPane zbWordPane = new WordPane();

  private AsFile file = new AsFile();

  private String wordFileId;

  protected boolean budgetFlag = "Y".equals(AsOptionMeta.getOptVal(ZcSettingConstants.OPT_ZC_USE_BUDGET_INTERFACE));

  protected boolean isCancel = false;

  protected ElementConditionDto getDto = new ElementConditionDto();

  private BigDecimal bai = new BigDecimal("100");

  protected boolean isCar = false;

  //

  private IZcEbSupplierServiceDelegate zcEbSupplierServiceDelegate = (IZcEbSupplierServiceDelegate) ServiceFactory.create(

  IZcEbSupplierServiceDelegate.class, "zcEbSupplierServiceDelegate");

  //资金构成数据
  public List biList = new ArrayList();

  public ZcXmcgHtEditPanel(GkBaseDialog parent, ListCursor<ZcXmcgHt> listCursor, String tabStatus, ZcXmcgHtListPanel listPanel) {

    super(ZcXmcgHt.class, BillElementMeta.getBillElementMetaWithoutNd(getCompoId()));

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
          setOldFile(wordPane.getOpenFile());

        }

      }

    });

    noticeWordPane.addPropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, new PropertyChangeListener() {

      public void propertyChange(PropertyChangeEvent evt) {

        //打开文件完成之后的回调函数
        boolean isSuccess = (Boolean) evt.getNewValue();

        if (isSuccess) {

          //下面一句是为了打开word后刷新窗口
          self.parent.setSize(self.parent.getSize().width - 1, self.parent.getSize().height - 1);

        }

      }

    });

    zbWordPane.addPropertyChangeListener(WordPane.EVENT_NAME_OPEN_CALLBACK, new PropertyChangeListener() {

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

  protected void init() {

    this.initToolBar(toolBar);

    this.setLayout(new BorderLayout());

    this.add(toolBar, BorderLayout.NORTH);

    initFieldEditorPanel(ZcXmcgHt.class, getMainBillElementMeta());

    JComponent tabTable = createSubBillPanel();

    fieldEditorPanel.setMinimumSize(new Dimension(540, 150));

    tabTable.setMinimumSize(new Dimension(240, 300));

    myWordPane = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, fieldEditorPanel, tabTable);

    myWordPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "采购合同", TitledBorder.CENTER, TitledBorder.TOP,

    new Font("宋体", Font.BOLD, 15), Color.BLUE));

    myWordPane.setDividerDefaultLocation(this.getClass().getName() + "_splitPane_dividerLocation_top", 150);

    myWordPane.setContinuousLayout(true);

    myWordPane.setOneTouchExpandable(true);

    myWordPane.setDividerSize(5);

    myWordPane.setDividerLocation(200);

    myWordPane.setBackground(self.getBackground());

    this.add(myWordPane, BorderLayout.CENTER);

  }

  private void setButtonStatus() {
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

      bs.setButton(this.editButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.addButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.saveButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_ALL);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.exitButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_ALL);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.sendButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.suggestPassButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.auditPassButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.callbackButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.unAuditButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.unTreadButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

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

  protected JTablePanel[] getSubTables() {

    return new JTablePanel[] { biTablePanel, itemTablePanel };

  }

  public List<AbstractFieldEditor> createFieldEditors() {

    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    TextFieldEditor zcMakeName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_NAME), "zcPProMake.zcMakeName");

    MoneyFieldEditor zcMoneyBiSum = new MoneyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MONEY_BI_SUM),

    "zcPProMake.zcMoneyBiSum");

    String columNames[] = { "项目代码", "采购项目", "分包名称", "供应商名称", "中标金额" };

    ZcPackSelectedHandler packSelectHandler = new ZcPackSelectedHandler(columNames, self);

    ElementConditionDto elementCondtiontDto = new ElementConditionDto();

    //    elementCondtiontDto.setBillStatus("exec");

    elementCondtiontDto.setUserId(requestMeta.getSvUserID());

    ForeignEntityFieldEditor packCode = new ForeignEntityFieldEditor("selectPage.selectPackForHtBySupplier", elementCondtiontDto, 20,
      packSelectHandler,

      columNames, "中标分包", "packName");

    //    packCode.setEditable(false);

    zcMakeName.setEnabled(false);

    zcMoneyBiSum.setEnabled(false);

    AsValFieldEditor zcProjType = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FUKUAN_TYPE), "zcCgLeixing",

    "ZC_VS_FUKUAN_TYPE");

    zcProjType.setEnabled(false);

    AutoNumFieldEditor zcHtCode = new AutoNumFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_CODE), "zcHtCode");

    TextFieldEditor zcHtName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_NAME), "zcHtName");

    AsValFieldEditor zcHtStatus = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_STATUS), "zcHtStatus",
      getHtStatus());

    zcHtStatus.setEnabled(false);

    //    zcHtStatus.setVisible(false);

    CompanyFieldEditor zcCoCode = new CompanyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_CODE), "coCode");

    zcCoCode.setEnabled(false);

    IntFieldEditor zcCoCodeNd = new IntFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_CODE_ND), "nd", 4);

    zcCoCodeNd.setEnabled(false);

    if (isGys()) {

      zcCoCodeNd.setVisible(false);

      zcMoneyBiSum.setVisible(false);

    }

    getBiddingWinner();

    ZcXmcgHt ht = (ZcXmcgHt) this.listCursor.getCurrentObject();

    TextFieldEditor zcSuLinkman = new TextFieldEditor("联系人", "zcSuLinkman");

    zcSuLinkman.setVisible(false);

    TextFieldEditor zcSuName = new TextFieldEditor("中标供应商", "zcSuName");

    MoneyFieldEditor zcHtNum = new MoneyFieldEditor(getHtNumLabel(), "zcHtNum");

    //添加工期和附件
    DateFieldEditor zcProLimitStartDate = new DateFieldEditor("工期开始时间", "zcProLimitStartDate");
    DateFieldEditor zcProLimitEndDate = new DateFieldEditor("工期结束时间", "zcProLimitEndDate");
    FileFieldEditor zcImpFile = new FileFieldEditor("合同文本", "zcImpFile", "zcImpFileBlobid");

    AsValFieldEditor zcFukuanType = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.ZC_PAY_TYPE), "zcFukuanType", "ZC_VS_PAY_TYPE");

    DateFieldEditor zcSgnDate = new DateFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_SGN_DATE), "zcSgnDate");

    TextFieldEditor zcDeliveryDate = new TextFieldEditor("交货日期", "zcDeliveryDate");

    TextFieldEditor zcDeliveryType = new TextFieldEditor("交货方式", "zcDeliveryType");

    TextFieldEditor zcDeliveryAddr = new TextFieldEditor("交货地点", "zcDeliveryAddr");

    TextFieldEditor zcMemo = new TextFieldEditor("备注信息", "zcMemo");

    DateFieldEditor zcInputDate = new DateFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_INPUT_DATE), "zcInputDate");

    TextFieldEditor lxr = new TextFieldEditor("单位联系人", "zcPProMake.zcMakeLinkman");

    FileFieldEditor htwb = new FileFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CON_TEXT), "zcConText", "zcConTextBlobid");

    editorList.add(packCode);
    editorList.add(zcHtName);
    editorList.add(zcHtCode);
    editorList.add(zcHtStatus);

    editorList.add(zcHtNum);
    //    editorList.add(zcFukuanType);
    editorList.add(zcDeliveryDate);
    editorList.add(zcDeliveryType);

    editorList.add(zcDeliveryAddr);
    editorList.add(zcSgnDate);
    editorList.add(zcProLimitStartDate);
    editorList.add(zcProLimitEndDate);

    editorList.add(zcSuName);
    editorList.add(zcCoCode);
    editorList.add(zcMakeName);
    editorList.add(zcImpFile);

    editorList.add(zcMemo);
    editorList.add(zcInputDate);

    //    editorList.add(zcMoneyBiSum);
    //    editorList.add(lxr);
    //  editorList.add(zcCoCodeNd);

    //项目采购才显示中标
    //    if (ht != null && ZcSettingConstants.PROJECT_BUY_CODE.equals(ht.getZcFukuanType()))
    //      editorList.add(biddingWinner);
    //
    //    editorList.add(zcHtStatus);

    return editorList;

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

  /* protected void loadCommonEditors(List<AbstractFieldEditor> editorList) {

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

   }*/

  public void zcMakeCodeChange() {

    ZcXmcgHt zcXmcgHt = (ZcXmcgHt) this.listCursor.getCurrentObject();

    String zcMakeCode = zcXmcgHt.getZcMakeCode();

    dtoForBidWinner.setProjectCode(zcMakeCode);
    //    ZcPProMake zcPProMake = getZcPProMakeByZcMakeCode(zcMakeCode);
    //    zcXmcgHt.setZcPProMake(zcPProMake);
    zcXmcgHt.setZcMakeCode(zcMakeCode);
    zcXmcgHt.setZcCoCode(zcXmcgHt.getCoCode());
    zcXmcgHt.getZcPProMake().setZcMoneyBiSum(zcXmcgHt.getZcHtNum());
    zcXmcgHt.getZcPProMake().setZcMakeCode(zcXmcgHt.getZcBidCode());
    zcXmcgHt.getZcPProMake().setZcMakeName(zcXmcgHt.getZcBidContent());
    zcXmcgHt.setPackName(zcXmcgHt.getZcZbCode());
    zcXmcgHt.setZcZbCode(zcXmcgHt.getZcSuCode());
    //    zcXmcgHt.setNd(zcPProMake.getNd());
    //    zcXmcgHt.setZcFukuanType(zcPProMake.getZcFukuanType());
    List<ZcTBchtItem> itemHtList = self.listPanel.zcXmcgHtServiceDelegate.getZcItemZxByPackCode(zcXmcgHt.getZcBidCode(), zcXmcgHt.getZcPProMake()
      .getZcPifuCgfs(), requestMeta);

    List<ZcXmcgHtBi> biHtList = new ArrayList<ZcXmcgHtBi>();

    List<ZcHtPrePayBillItem> payBiList = new ArrayList<ZcHtPrePayBillItem>();

    //带出商品内容

    if (itemHtList == null) {

      itemHtList = new ArrayList<ZcTBchtItem>();

    }

    if (zcXmcgHt.getZcSuCode() != null) {

      ZcEbSupplier supp = zcEbSupplierServiceDelegate.getSupplierById(zcXmcgHt.getZcSuCode(), requestMeta);

      zcXmcgHt.setZcSuLinkman(supp.getLinkMan());

      zcXmcgHt.setZcSuTel(supp.getPhone());

      zcXmcgHt.setZcGnw(supp.getGnw());

      zcXmcgHt.setZcSnw(supp.getSnw());

      zcXmcgHt.setZcIsZxqyZb(supp.getIsZxqy());

    }
    BigDecimal htnum = BigDecimal.ZERO;

    for (Iterator iterator = itemHtList.iterator(); iterator.hasNext();) {

      ZcTBchtItem zcTBchtItem = (ZcTBchtItem) iterator.next();

      zcTBchtItem.setZcItemSum(BigDecimal.ZERO);
      htnum = htnum.add(zcTBchtItem.getZcCaigMoney());

    }

    zcXmcgHt.setItemList(itemHtList);

    zcXmcgHt.setBiList(biHtList);
    zcXmcgHt.setPayBiList(payBiList);
    zcXmcgHt.setZcHtNum(htnum);

    zcXmcgHt.setZcCgLeixing("Z01");

    if ("2".equals(zcXmcgHt.getZcPProMake().getZcPifuCgfs())) {
      moldDto.setType("HT");
    } else if ("3".equals(zcXmcgHt.getZcPProMake().getZcPifuCgfs())) {
      moldDto.setType("HTJ");
    } else if ("4".equals(zcXmcgHt.getZcPProMake().getZcPifuCgfs())) {
      moldDto.setType("HTD");
    } else if ("5".equals(zcXmcgHt.getZcPProMake().getZcPifuCgfs())) {
      moldDto.setType("HTX");
    } else {
      moldDto.setType("HT");
    }

    this.doOpenNotice(zcXmcgHt.getZcBidCode());

    this.doOpenZb(zcXmcgHt.getZcMakeCode());

    setEditingObject(zcXmcgHt);

    if (zcXmcgHt.getZcBidCode() != null) {
      ElementConditionDto dto = new ElementConditionDto();
      dto.setZcText0(zcXmcgHt.getZcBidCode());
      biList = zcEbBaseServiceDelegate.getForeignEntitySelectedData("ZC_P_PRO_MITEM_BI.getHtBiDetailByPackCode", dto, requestMeta);
    }

    self.refreshSubData(biList, itemHtList, payBiList);

    listCursor.setCurrentObject(zcXmcgHt);

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

      htBi.getZcPProMitemBi().setZcBiJhuaSum(bi.getZcBiJhuaSum());

      htBi.getZcPProMitemBi().setZcBiUsedSum(bi.getZcBiUsedSum());

      htBi.getZcPProMitemBi().setZcBiDoSum(bi.getZcBiDoSum());

      //      htBi.getZcPProMitemBi().setFundCode(bi.getFundCode());

      htBi.getZcPProMitemBi().setOriginCode(bi.getOriginCode());

      htBi.getZcPProMitemBi().setPaytypeCode(bi.getPaytypeCode());

      htBi.getZcPProMitemBi().setPaytypeName(bi.getPaytypeName());

      htBi.getZcPProMitemBi().setSenddocCode(bi.getSenddocCode());

      htBi.getZcPProMitemBi().setSenddocName(bi.getSenddocName());

      htBi.getZcPProMitemBi().setZcProBiSeq(bi.getZcProBiSeq());
      htBi.getZcPProMitemBi().setZcBiNo(bi.getZcBiNo());
      htBi.getZcPProMitemBi().setZcUseBiId(bi.getZcUseBiId());

      htBi.setZcProBiSeq(bi.getZcProBiSeq());

      htBi.setZcBiBcsySum(BigDecimal.ZERO);

      htBi.setZcCanUseSum(bi.getZcBiDoSum());
      htBi.setZcBiSum(bi.getZcBiSum());

      // 预算单位
      htBi.getZcPProMitemBi().setCoCode(bi.getCoCode());
      htBi.getZcPProMitemBi().setCoName(bi.getCoName());
      // 资金性质
      htBi.getZcPProMitemBi().setFundCode(bi.getFundCode());
      htBi.getZcPProMitemBi().setFundName(bi.getFundName());
      // 功能分类
      htBi.getZcPProMitemBi().setbAccCode(bi.getbAccCode());
      htBi.getZcPProMitemBi().setbAccName(bi.getbAccName());
      // 项目类别
      htBi.getZcPProMitemBi().setProjectTypeCode(bi.getProjectTypeCode());
      htBi.getZcPProMitemBi().setProjectTypeName(bi.getProjectTypeName());

      // 指标来源
      htBi.getZcPProMitemBi().setOriginCode(bi.getOriginCode());
      htBi.getZcPProMitemBi().setOriginName(bi.getOriginName());
      // 年度
      htBi.getZcPProMitemBi().setNd(bi.getNd());

      // 业务处室
      htBi.getZcPProMitemBi().setOrgCode(bi.getOrgCode());
      htBi.getZcPProMitemBi().setOrgName(bi.getOrgName());

      biHtList.add(htBi);

    }

  }

  public JComponent createSubBillPanel() {

    final JTabbedPane topTabPane = new JTabbedPane();

    JTabbedPane biTabPane = new JTabbedPane();

    this.createSubInfo();

    biTablePanel.init();

    biTablePanel.getSearchBar().setVisible(false);

    biTablePanel.setTablePreferencesKey(this.getClass().getName() + "_biTable");

    biTablePanel.getTable().setShowCheckedColumn(true);

    biTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    //    biTabPane.addTab("资金构成", biTablePanel);

    JFuncToolBar bottomToolBar1 = new JFuncToolBar();

    FuncButton addBtn1 = new SubaddButton(false);

    JButton insertBtn1 = new SubinsertButton(false);

    JButton delBtn1 = new SubdelButton(false);

    bottomToolBar1.add(addBtn1);

    bottomToolBar1.add(insertBtn1);

    bottomToolBar1.add(delBtn1);

    if (!budgetFlag) {
      biTablePanel.add(bottomToolBar1, BorderLayout.SOUTH);
    }

    if (budgetFlag) {
      biTablePanel.add(bottomToolBar1, BorderLayout.SOUTH);
    }

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

    biTablePanel.setEnabled(false);
    JTabbedPane itemTabPane = new JTabbedPane();

    itemTablePanel.init();

    itemTablePanel.setPanelId(this.getClass().getName() + "_itemTablePanel");

    itemTablePanel.getSearchBar().setVisible(false);

    itemTablePanel.setTablePreferencesKey(this.getClass().getName() + "_itemTable");

    itemTablePanel.getTable().setShowCheckedColumn(true);

    itemTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    itemTabPane.addTab("商品构成", itemTablePanel);

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

    biTabPane.setMinimumSize(new Dimension(240, 150));

    itemTabPane.setMinimumSize(new Dimension(240, 300));

    JSaveableSplitPane splitPane = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, biTabPane, itemTabPane);

    splitPane.setDividerDefaultLocation(this.getClass().getName() + "_splitPane_dividerLocation", 150);

    splitPane.setContinuousLayout(true);
    //    if (!isCancel) {
    //      biTabPane.setVisible(false);
    //    }

    /* if (isGys()) {

       biTabPane.setVisible(false);

       splitPane.setOneTouchExpandable(false);

     } else {

       splitPane.setOneTouchExpandable(true);

     }
    */
    // 只显示向下的箭头

    //    splitPane.putClientProperty("toExpand", true);

    splitPane.setDividerSize(10);

    //    splitPane.setDividerLocation(260);

    splitPane.setBackground(self.getBackground());

    //    topTabPane.addTab("合同明细", splitPane);

    //    topTabPane.addTab("合同明细", itemTabPane);

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

        } else if (index == 2) {

          ZcXmcgHt ht = (ZcXmcgHt) self.listCursor.getCurrentObject();

          if (ht.getZcBidCode() != null && noticeWordPane.getOpenFile() == null) {

            doOpenNotice(ht.getZcBidCode());

          }

        } else if (index == 3) {

          ZcXmcgHt ht = (ZcXmcgHt) self.listCursor.getCurrentObject();

          if (ht.getZcBidCode() != null && zbWordPane.getOpenFile() == null) {

            doOpenZb(ht.getZcMakeCode());

          }

        }

      }

    });
    //    topTabPane.addTab("付款明细", detailTablePanel);

    //    topTabPane.addTab("合同文本", wordPane);
    //    topTabPane.addTab("成交通知书", noticeWordPane);
    //    topTabPane.addTab("招标文件", zbWordPane);

    ZcXmcgHt ht = (ZcXmcgHt) self.listCursor.getCurrentObject();

    if (ht != null) {

      List list = getWFNodeEnableFunc(ht, requestMeta);

      if (!list.contains(editButton.getFuncId())) {

        bottomToolBar1.setEnabled(false);

        bottomToolBar2.setVisible(false);

      }

    }

    //    return topTabPane;
    return itemTabPane;

  }

  private void createSubInfo() {

    detailTablePanel.init();

    detailTablePanel.getSearchBar().setVisible(false);

    detailTablePanel.setTablePreferencesKey(this.getClass().getName() + "_biTable");

    detailTablePanel.getTable().setShowCheckedColumn(true);

    detailTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    //	    jTabbedPane.addTab("付款明细", detailTablePanel);
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

        deleteSub(detailTablePanel);

      }

    });
  }

  protected void refreshSubData(List biList) {

    biTablePanel.setTableModel(ZcEbEntrustToTableModelConverter.convertSubBiTableData(biList));
    ZcUtil.translateColName(this.biTablePanel.getTable(), ZcEbEntrustToTableModelConverter.biInfo);
    setTableBiEditor(biTablePanel.getTable());

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

  protected void setdetailBiDefaultValue(ZcHtPrePayBillItem detail) {
    detail.setPayYear(this.requestMeta.getSvNd() + "");
    detail.setPayMonth("01");
  }

  public void setItemBiDefaultValue(ZcXmcgHtBi zcXmcgHtBi) {

    zcXmcgHtBi.getZcPProMitemBi().setFundCode("3");//其他

    zcXmcgHtBi.getZcPProMitemBi().setOriginCode("4");//其他

    zcXmcgHtBi.getZcPProMitemBi().setPaytypeCode("3");//自行支付      
    if (this.budgetFlag) {
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

    if (sum.compareTo(beanData.getZcPProMake().getZcMoneyBiSum()) != 0) {

      beanData.getZcPProMake().setZcMoneyBiSum(sum);

      beanData.setZcHtNum(htSum);

    }
    setSumLabelText();
    this.setEditingObject(beanData);

  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(getCompoId());

    toolBar.add(addButton);

    toolBar.add(editButton);

    toolBar.add(saveButton);

    toolBar.add(sendButton);

    toolBar.add(saveAndSendButton);

    toolBar.add(suggestPassButton);

    toolBar.add(auditPassButton);

    toolBar.add(isSendToNextButton);

    toolBar.add(agreeButton);

    toolBar.add(disagreeButton);

    toolBar.add(sendToXieBanButton);

    toolBar.add(unAuditButton);

    toolBar.add(unTreadButton);

    toolBar.add(callbackButton);

    toolBar.add(deleteButton);

    toolBar.add(selectMoldButton);

    //    toolBar.add(loadMoldButton);

    toolBar.add(printButton);

    toolBar.add(traceButton);

    toolBar.add(openNotepadButton);

    toolBar.add(previousButton);

    toolBar.add(nextButton);

    toolBar.add(exitButton);

    loadMoldButton.setEnabled(false);

    selectMoldButton.setEnabled(false);

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

    selectMoldButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doSelectMoldButton();

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

    openNotepadButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doOpenNotepad();

      }

    });

  }

  protected void doAdd() {
    // TODO Auto-generated method stub
    this.listCursor.setCurrentObject(null);
    refreshData();
  }

  private void doOpenNotepad() {

    ZcXmcgHt sheet = (ZcXmcgHt) this.listCursor.getCurrentObject();

    String sn = fetchSn(sheet);

    if (sn != null) {

      new ZcNotepadDialog(sn, parent);

    }

  }

  public String fetchSn(ZcXmcgHt sheet) {

    String sn = null;

    if (sheet.getZcMakeCode() == null || "".equals(sheet.getZcMakeCode())) {

      JOptionPane.showMessageDialog(this, "项目为空不能记录相关信息 ！", "错误", JOptionPane.ERROR_MESSAGE);

      return sn;
    }

    ZcEbProj proj = zcEbProjServiceDelegate.getZcEbProjByProjCode(sheet.getZcMakeCode(), requestMeta);

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

    boolean success = true;

    String errorInfo = "";

    try {

      ht.setZcConText(String.valueOf(isGoonAudit));

      ht.setComment(commentDialog.getComment());

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      zcXmcgHtServiceDelegate.updateZcXmcgHtFN(ht, this.requestMeta);

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
    ZcXmcgHt zcXmcgHt = (ZcXmcgHt) listCursor.getCurrentObject();

    super.updateFieldEditors();

    boolean flag = false;

    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {

        if ("zcBidContent_zcHtCode_nd_zcPProMake.zcMakeName_zcHtStatus_zcPProMake.zcMoneyBiSum_pName_pBiSum_zcCgLeixing_coCode_zcInputDate_zcSuName"
          .indexOf(fd.getFieldName()) == -1) {

          fd.setEnabled(true);

        } else {

          fd.setEnabled(false);

        }

        if ("zcMakeCode".equals(fd.getFieldName())) {

          ForeignEntityFieldEditor efe = (ForeignEntityFieldEditor) fd;

          efe.setEnabled(true);

          efe.setEditable(false);

        }
        if ("zcSuName".equals(fd.getFieldName()) && "Z01".equals(zcXmcgHt.getZcFukuanType())) {
          fd.setEnabled(true);

        }
        if (isGys()) {

          //          zcSuName.setEnabled(false);

        }

      }

      flag = true;

    } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_BROWSE)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {

        fd.setEnabled(false);
      }

      flag = false;

    } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {

        //fd.setEnabled(true);

        //flag = true;

        if ("zcBidContent_zcHtCode_nd_zcPProMake.zcMakeName_zcHtStatus_zcPProMake.zcMoneyBiSum_zcBidContent_pName_pBiSum_zcCgLeixing_coCode_zcInputDate_zcSuName"
          .indexOf(fd

          .getFieldName()) == -1) {

          fd.setEnabled(true);
        } else {

          fd.setEnabled(false);

        }

        if ("zcMakeCode".equals(fd.getFieldName())) {

          ForeignEntityFieldEditor efe = (ForeignEntityFieldEditor) fd;

          efe.setEnabled(true);

          efe.setEditable(false);

        }
        //        if ("zcSuName".equals(fd.getFieldName())) {
        //          fd.setEnabled("Z01".equals(zcXmcgHt.getZcFukuanType()));
        //        }
      }

      if (isGys()) {

        //        zcSuName.setEnabled(false);

      }

      flag = true;

    }

    setWFSubTableEditable(biTablePanel, false);

    setWFSubTableEditable(itemTablePanel, flag);

  }

  private void doEdit() {
    if (this.budgetFlag && "".equals(((ZcXmcgHt) listCursor.getCurrentObject()).getZcHtStatus())) {
      setWFSubTableEditable(itemTablePanel, true);
      this.saveButton.setEnabled(true);
      this.editButton.setEnabled(false);
      return;
    }

    this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;

    updateFieldEditorsEditable();

    setButtonStatus();
    JFuncToolBar bottomToolBar1 = (JFuncToolBar) biTablePanel.getComponent(biTablePanel.getComponentCount() - (budgetFlag ? 1 : 2));
    bottomToolBar1.setEnabled(true);

  }

  protected void doSend() {

    boolean success = true;

    ZcXmcgHt afterSaveBill = null;

    if (this.isDataChanged() && !doSave()) {

      return;

    }

    try {

      requestMeta.setFuncId(this.sendButton.getFuncId());

      ZcXmcgHt ht = (ZcXmcgHt) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      if (ht.getZcConTextBlobid() == null) {

        JOptionPane

        .showMessageDialog(this, "合同文本没有生成，请选择合同模板，并且进行填充！", "提示", JOptionPane.INFORMATION_MESSAGE);

        return;

      }

      ht.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      afterSaveBill = zcXmcgHtServiceDelegate.newCommitZxFN(ht, requestMeta);

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

    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    if (calcBeforeSave()) {

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

    boolean success = true;

    ZcXmcgHt afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(unAuditButton.getFuncId());

      ZcXmcgHt ht = (ZcXmcgHt) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

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

    if (checkBeforeSave()) {

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

      zcXmcgHtServiceDelegate.updateZcXmcgHtFN(ht, this.requestMeta);

      afterSaveBill = zcXmcgHtServiceDelegate.untreadZxFN(ht, requestMeta);

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

      zcXmcgHtServiceDelegate.updateZcXmcgHtFN(ht, this.requestMeta);

      afterSaveBill = zcXmcgHtServiceDelegate.callbackZxFN(ht, requestMeta);

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
    if (wordPane != null && wordPane.isDocOpened()) {

      wordPane.closeNotSave();

    }
    if (noticeWordPane != null && noticeWordPane.isDocOpened()) {

      noticeWordPane.closeNotSave();

    }
    if (zbWordPane != null && zbWordPane.isDocOpened()) {

      zbWordPane.closeNotSave();

    }

    this.parent.dispose();

  }

  private void doSelectMoldButton() {

    fieldMoldName.getField().handleClick(null);

  }

  private void doLoadMoldButton() {

    if (checkBeforeSave()) {

      return;

    }

    ZcXmcgHt zcXmcgHt = (ZcXmcgHt) this.listCursor.getCurrentObject();
    if (zcXmcgHt.getZcHtCode() == null || "".equals(zcXmcgHt.getZcHtCode())) {

      JOptionPane.showMessageDialog(this, "请保存合同主信息,再进行填充！", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    if (wordPane != null) {

      wordPane.closeNotSave();

    }

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

    ZcHtModel mod = zcXmcgHtServiceDelegate.getContractContentZx(zcXmcgHt.getZcMakeCode(), zcXmcgHt.getZcBidCode(), requestMeta, actFileId);
    this.HtToModel(zcXmcgHt, mod);

    file = mod.getFile();
    file.setFileName(PATH + actFileId + ".doc");
    String modname = actFileId + "_mod.doc";

    // 创建临时文件

    boolean isSucceed = WordFileUtil.createFile(PATH, PATH + modname, this, file.getFileContent());
    File f = new File(PATH);

    Configuration configuration = new Configuration();
    configuration.setDefaultEncoding("utf-8");

    OutputStream out = null;
    Writer writer = null;
    try {
      configuration.setDirectoryForTemplateLoading(f);
      Template template = configuration.getTemplate(modname);
      out = new FileOutputStream(file.getFileName());
      writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
      template.process(mod, writer);
      writer.flush();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {
      }
    }

    if (isSucceed) {

      wordPane.open(file.getFileName());

    }

  }

  protected boolean calcBeforeSave() {

    ZcXmcgHt zcXmcgHt = (ZcXmcgHt) this.listCursor.getCurrentObject();

    if (zcXmcgHt.getZcPProMake().getZcMoneyBiSum().compareTo(zcXmcgHt.getZcHtNum()) < 0) {

      JOptionPane.showMessageDialog(this, "合同金额不能超过预算金额", "提示", JOptionPane.ERROR_MESSAGE);

      return true;
    }

    return false;

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

    //    List biNotNullList = self.biBillElementMeta.getNotNullBillElement();

    List itemNotNullList = self.itemBillElementMeta.getNotNullBillElement();

    ZcXmcgHt zcXmcgHt = (ZcXmcgHt) this.listCursor.getCurrentObject();

    StringBuilder errorInfo = new StringBuilder();

    String mainValidateInfo = ZcUtil.validateBillElementNull(zcXmcgHt, mainNotNullList);

    //    String biValidateInfo = ZcUtil.validateDetailBillElementNull(zcXmcgHt.getBiList(), biNotNullList, false);

    String itemValidateInfo = ZcUtil.validateDetailBillElementNull(zcXmcgHt.getItemList(), itemNotNullList, false);

    if (mainValidateInfo.length() != 0) {

      errorInfo.append("采购合同：\n").append(mainValidateInfo.toString()).append("\n");

    }

    //    if (biValidateInfo.length() != 0) {
    //
    //      errorInfo.append("资金构成：\n").append(biValidateInfo.toString()).append("\n");
    //
    //    }

    if (itemValidateInfo.length() != 0) {

      errorInfo.append("商品构成：\n").append(itemValidateInfo.toString()).append("\n");

    }

    if (errorInfo.length() == 0) {

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

        if (htmoney.subtract(itemsum).abs().multiply(new BigDecimal("200")).compareTo(BigDecimal.ONE) > 0) {
          errorInfo.append("商品构成中【金额】之和必须等于【" + getHtNumLabel() + "】！");
        }

      }

    }

    if (errorInfo.length() != 0) {

      JOptionPane.showMessageDialog(this, errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);

      return true;

    }

    return false;

  }

  public boolean doSave() {

    if (!isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据没有发生改变，不用保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      loadMoldButton.setEnabled(true);

      selectMoldButton.setEnabled(true);
      this.editButton.setEnabled(true);

      return true;

    }

    if (checkBeforeSave()) {

      return false;

    }

    if (!isGys() && calcBeforeSave()) {

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
      if (!budgetFlag) {
        makeToHtBi(inData);
      }

      ZcXmcgHt zcXmcgHt = zcXmcgHtServiceDelegate.updateZcXmcgHtFN(inData, this.requestMeta);

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

    return true;

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

        zcXmcgHtServiceDelegate.deleteByPrimaryKeyFN(zcXmcgHt.getZcHtCode(), budgetFlag, WorkEnv.getInstance().getWebRoot(), this.requestMeta);

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

    if (!saveButton.isVisible() || !saveButton.isEnabled()) {
      return false;
    }

    if (wordPane != null && wordPane.isDocOpened() && wordPane.isDirty()) {
      try {
        wordPane.save();
        FileInputStream fs = new FileInputStream(wordPane.getOpenFile());
        int size = fs.available();
        if (oldHtWord.length != size) {
          return true;
        }
        byte a[] = new byte[size];
        fs.read(a);
        fs.close();
        for (int i = 0; i < oldHtWord.length; i++) {
          if (oldHtWord[i] != a[i]) {
            return true;
          }
        }
      } catch (Exception e) {
      }
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

      zcXmcgHt = zcXmcgHtServiceDelegate.selectZxByPrimaryKey(zcXmcgHt.getZcHtCode(), this.requestMeta);

      if (zcXmcgHt.getZcPProMake().getZcMakeCode() != null) {

        zcXmcgHt.setZcMakeCode(zcXmcgHt.getZcPProMake().getZcMakeCode());

      }

      List<ZcXmcgHtBi> tempList = new ArrayList<ZcXmcgHtBi>();

      zcXmcgHt.setBiList(tempList);

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
      zcXmcgHt.setAgency(AsOptionMeta.getOptVal(ZcElementConstants.OPT_ZC_CGZX_CODE));
      //      zcXmcgHt.setOrgCode(WorkEnv.getInstance().getOrgCode());

      zcXmcgHt.setBiList(new ArrayList<ZcXmcgHtBi>());

      zcXmcgHt.setItemList(new ArrayList<ZcTBchtItem>());

      zcXmcgHt.setPayBiList(new ArrayList<ZcHtPrePayBillItem>());

      zcXmcgHt.setExecuteDate(this.requestMeta.getTransDate());
      zcXmcgHt.setExecutor(this.requestMeta.getSvUserID());
      zcXmcgHt.setExecutorName(this.requestMeta.getSvUserName());

      listCursor.getDataList().add(zcXmcgHt);

      listCursor.setCurrentObject(zcXmcgHt);

      this.setEditingObject(zcXmcgHt);

    }

    if (zcXmcgHt.getZcBidCode() != null) {
      ElementConditionDto dto = new ElementConditionDto();
      dto.setZcText0(zcXmcgHt.getZcBidCode());
      biList = zcEbBaseServiceDelegate.getForeignEntitySelectedData("ZC_P_PRO_MITEM_BI.getHtBiDetailByPackCode", dto, requestMeta);
    }
    biTablePanel.setTableModel(ZcEbEntrustToTableModelConverter.convertSubBiTableData(biList));
    ZcUtil.translateColName(this.biTablePanel.getTable(), ZcEbEntrustToTableModelConverter.biInfo);
    setTableBiEditor(biTablePanel.getTable());

    itemTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubZxItemTableData(zcXmcgHt.getItemList()));

    // 翻译从表表头列
    if (!budgetFlag) {
    } else {
      getDto.setNd(requestMeta.getSvNd());
      getDto.setZcText2("1");
      getDto.setCoCode(requestMeta.getSvCoCode());

    }

    ZcUtil.translateColName(itemTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getZxItemInfo());

    // 设置从表监听 

    addItemTableLisenter(itemTablePanel.getTable());

    setItemTableEditor(itemTablePanel.getTable());

    //    this.refreshSubData(zcXmcgHt.getPayBiList());

    Long processInstId = zcXmcgHt.getProcessInstId();

    if (processInstId == null || processInstId.longValue() < 0) {

      sendToXieBanButton.setVisible(false);

    }

    //    setWordButtonStataus(zcXmcgHt);

    if (isGys()) {

      JPageableFixedTable ta = itemTablePanel.getTable();

      hideCol(ta, "ZC_ITEM_SUM");

      hideCol(ta, "BUDGET_BI_MONEY");

      hideCol(ta, "BUDGET_OTHER_MONEY");

      hideCol(ta, "ZC_ITEM_VAL");

      hideCol(ta, "ZC_HT_BI_MONEY");

      hideCol(ta, "ZC_HT_OTHER_MONEY");

      hideCol(ta, "ZC_HT_GK_MONEY");

    }

    setSumLabelText();

    setOldObject();

    setButtonStatus();

    updateWFEditorEditable();

  }

  public void updateWFEditorEditable() {

    ZcXmcgHt ht = (ZcXmcgHt) listCursor.getCurrentObject();

    Long processInstId = ht.getProcessInstId();

    isEdit = false;

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

          editor.setEnabled(true);

        } else {

          editor.setEnabled(false);

        }

      }

      // 子表的设置

      updateWFSubTableEditable();

    } else {
      for (AbstractFieldEditor editor : fieldEditors) {
        logger.debug("===" + editor.getFieldName() + "=" + editor.isEnabled());
        if (pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT) || pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {
          if ("packName".equals(editor.getFieldName()) || "zcHtName".equals(editor.getFieldName()) || "zcSuLinkman".equals(editor.getFieldName())
            || "zcProLimitStartDate".equals(editor.getFieldName()) || "zcProLimitEndDate".equals(editor.getFieldName())
            || "zcImpFileBlobid".equals(editor.getFieldName()) || "zcSgnDate".equals(editor.getFieldName())
            || "zcDeliveryDate".equals(editor.getFieldName()) || "zcDeliveryType".equals(editor.getFieldName())
            || "zcDeliveryAddr".equals(editor.getFieldName()) || "zcMemo".equals(editor.getFieldName()) || "zcConText".equals(editor.getFieldName())) {
            editor.setEnabled(true);
            isEdit = true;
          } else {
            editor.setEnabled(false);
          }
        } else {
          editor.setEnabled(false);
        }
        logger.debug("===" + editor.getFieldName() + "=" + editor.isEnabled());
      }
    }

    setWFSubTableEditable(biTablePanel, false);

    setWFSubTableEditable(itemTablePanel, isEdit);

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

  private void refreshSubData(List biList, List itemList, List payBiList) {

    biTablePanel.setTableModel(ZcEbEntrustToTableModelConverter.convertSubBiTableData(biList));
    ZcUtil.translateColName(this.biTablePanel.getTable(), ZcEbEntrustToTableModelConverter.biInfo);
    setTableBiEditor(biTablePanel.getTable());

    itemTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubZxItemTableData(itemList));

    ZcUtil.translateColName(itemTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getZxItemInfo());

    // 设置从表监听 

    addItemTableLisenter(itemTablePanel.getTable());

    setItemTableEditor(itemTablePanel.getTable());

    int height = itemTablePanel.getTable().getRowHeight();

    if (isGys()) {

      JPageableFixedTable ta = itemTablePanel.getTable();

      hideCol(ta, "ZC_ITEM_SUM");

      hideCol(ta, "BUDGET_BI_MONEY");

      hideCol(ta, "BUDGET_OTHER_MONEY");

      hideCol(ta, "ZC_ITEM_VAL");

      hideCol(ta, "ZC_HT_BI_MONEY");

      hideCol(ta, "ZC_HT_OTHER_MONEY");

      hideCol(ta, "ZC_HT_GK_MONEY");

    }

    height = itemTablePanel.getTable().getRowHeight();

    itemTablePanel.getTable().setRowHeight(18);

    itemTablePanel.getTable().sizeColumnsToFit(0);
    updateFieldEditorsEditable();

  }

  private void refreshItemData(List itemList) {

    itemTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubItemTableData(itemList, isCar));

    ZcUtil.translateColName(itemTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getItemInfo()); // 翻译从表表头列

    addItemTableLisenter(itemTablePanel.getTable()); // 设置从表监听 

    setOldObject();

    setItemTableEditor(itemTablePanel.getTable());

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
              if (isCar) {
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

  }

  /**

   * 返回当前用户是否供应商

   * @return

   */

  public boolean isGys() {

    if (WorkEnv.getInstance().containRole(ZcSettingConstants.ROLE_GYS_HUIYUAN)
      || WorkEnv.getInstance().containRole(ZcSettingConstants.ROLE_GYS_NORMAL)) {

      return true;

    }
    return false;

  }

  protected void hideCol(JTable table, String colName) {

    TableColumn tc = table.getColumn(colName);

    table.getColumnModel().removeColumn(tc);

  }

  protected void setOldObject() {

    oldZcXmcgHt = (ZcXmcgHt) ObjectUtil.deepCopy(listCursor.getCurrentObject());

  }

  protected void setOldFile(File f) {

    try {
      if (wordPane != null && wordPane.isDocOpened()) {
        FileInputStream fs = new FileInputStream(f);
        int i = fs.available();
        oldHtWord = new byte[i];
        fs.read(oldHtWord);
        fs.close();
      }
    } catch (Exception e) {

    }

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

  public void doOpenNotice(String packCode) {
    if (packCode == null || "".equals(packCode.trim())) {
      return;
    }

    if (noticeWordPane != null && noticeWordPane.isDocOpened()) {

      noticeWordPane.close();

    }

    List<ZcEbNotice> list = (List<ZcEbNotice>) this.zcEbBaseServiceDelegate.queryDataForList("ZcEbNotice.getZcEbNoticeByPackCode", packCode,
      requestMeta);
    if (list == null || list.size() == 0) {
      return;
    }
    String fileID = list.get(0).getFileID();

    if (fileID != null && !fileID.equals("")) {

      WordFileUtil.setDir(null);
      String fileName = WordFileUtil.loadMold(fileID);

      noticeWordPane.open(fileName);

    }
  }

  private void doOpenZb(String projCode) {
    if (projCode == null || "".equals(projCode.trim())) {
      return;
    }

    if (zbWordPane != null && zbWordPane.isDocOpened()) {

      zbWordPane.close();

    }

    ZcEbProjZbFile file = (ZcEbProjZbFile) this.zcEbBaseServiceDelegate.queryObject("ZcEbProjZbFile.getZcEbProjZbFileByProjCode", projCode,
      requestMeta);
    if (file == null) {
      return;
    }
    String fileID = file.getWordFileId();

    if (fileID != null && !fileID.equals("")) {

      WordFileUtil.setDir(null);
      String fileName = WordFileUtil.loadMold(fileID);

      zbWordPane.openAndProtect(fileName, ZcSettingConstants.WORD_PASSWORD);

    }
  }

  public String getPageStatus() {

    return pageStatus;

  }

  public ZcXmcgHtListPanel getListPanel() {

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

    if ("2".equals(zcXmcgHt.getZcPProMake().getZcPifuCgfs())) {
      moldDto.setType("HT");
    } else if ("3".equals(zcXmcgHt.getZcPProMake().getZcPifuCgfs())) {
      moldDto.setType("HTJ");
    } else if ("4".equals(zcXmcgHt.getZcPProMake().getZcPifuCgfs())) {
      moldDto.setType("HTD");
    } else if ("5".equals(zcXmcgHt.getZcPProMake().getZcPifuCgfs())) {
      moldDto.setType("HTX");
    } else {
      moldDto.setType("HT");
    }
  }

  public static String getCompoId() {
    return "ZC_XMCG_HT";
  }

  //  protected String getProjectSqlId() {
  //    return "selectPage.selectProjectForZxCaiGouHt";
  //  }

  public BillElementMeta getMainBillElementMeta() {
    return mainBillElementMeta;
  }

  public boolean budgetExcuce(List selectedDatas) {

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
  }

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
        if (budgetFlag) {
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
    return "ZC_VS_COMPLETION_STATUS";
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

  private void HtToModel(ZcXmcgHt ht, ZcHtModel mod) {
    mod.setCoName(ht.getCompanyName());
    mod.setPackName(ht.getZcBidContent());
    mod.setZcHtCode(ht.getZcHtCode());
    mod.setZcHtNum(StringTools.getNumberFormat(ht.getZcHtNum()));
    mod.setZcHtNumUpp(ht.getZcHtNumString());
    mod.setZcSgnDate(ht.getZcSgnDateString());
    mod.setZcSuName(ht.getZcSuName());
    mod.setZcDeliveryAddr(ht.getZcDeliveryAddr());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
    if (ht.getZcProLimitStartDate() != null) {
      mod.setZcProLimitStartDate(sdf.format(ht.getZcProLimitStartDate()));
    }
    if (ht.getZcProLimitEndDate() != null) {
      mod.setZcProLimitEndDate(sdf.format(ht.getZcProLimitEndDate()));
    }
    if (ht.getZcDeliveryDate() != null) {
      mod.setZcDeliveryDate(ht.getZcDeliveryDate());
    }

    if (mod.getItem() == null) {
      return;
    }

    for (int i = 0; i < mod.getItem().size(); i++) {
      SupplierPriceDetail d = (SupplierPriceDetail) mod.getItem().get(i);
      d.setProdName(StringUtil.freeMarkFillWordChar(d.getProdName()));
      d.setProdBrand(StringUtil.freeMarkFillWordChar(d.getProdBrand()));
      d.setProdType(StringUtil.freeMarkFillWordChar(d.getProdType()));
      d.setProdMainParam(StringUtil.freeMarkFillWordChar(d.getProdMainParam()));
    }

  }
}
