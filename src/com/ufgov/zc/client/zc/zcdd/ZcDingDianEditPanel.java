package com.ufgov.zc.client.zc.zcdd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
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
import com.ufgov.zc.client.common.converter.zc.ZcDingDianToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.GkCommentDialog;
import com.ufgov.zc.client.component.GkCommentUntreadDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JSaveableSplitPane;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.AddButton;
import com.ufgov.zc.client.component.button.CallbackButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.ImportButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.PrintButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SaveSendButton;
import com.ufgov.zc.client.component.button.SendButton;
import com.ufgov.zc.client.component.button.SendGkButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.button.SuggestAuditPassButton;
import com.ufgov.zc.client.component.button.TraceButton;
import com.ufgov.zc.client.component.button.UnauditButton;
import com.ufgov.zc.client.component.button.UntreadButton;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.celleditor.DateCellEditor;
import com.ufgov.zc.client.component.table.celleditor.MoneyCellEditor;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.DateCellRenderer;
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
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.SelectFileFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextAreaFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.zcppromake.ZcBudgetHandler;
import com.ufgov.zc.common.budget.model.VwBudgetGp;
import com.ufgov.zc.common.commonbiz.model.Company;
import com.ufgov.zc.common.commonbiz.model.EAcc;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.WFConstants;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.system.util.Utils;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityTreeHandler;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcDingdian;
import com.ufgov.zc.common.zc.model.ZcDingdianBi;
import com.ufgov.zc.common.zc.model.ZcDingdianItem;
import com.ufgov.zc.common.zc.model.ZcEbDaibian;
import com.ufgov.zc.common.zc.model.ZcEbSupplier;
import com.ufgov.zc.common.zc.model.ZcPProMitem;
import com.ufgov.zc.common.zc.publish.IZcDingDianServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbSupplierServiceDelegate;

public class ZcDingDianEditPanel  extends AbstractMainSubEditPanel {

  private static final Logger logger = Logger.getLogger(ZcDingDianEditPanel.class);

  protected String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  protected RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();
 
  protected FuncButton saveButton = new SaveButton();

  protected FuncButton addButton = new AddButton();

  protected FuncButton editButton = new EditButton();

  private FuncButton traceButton = new TraceButton();

  protected FuncButton callbackButton = new CallbackButton();

  protected FuncButton deleteButton = new DeleteButton();

  private FuncButton previousButton = new PreviousButton();

  private FuncButton nextButton = new NextButton();

  private FuncButton exitButton = new ExitButton();

  private FuncButton saveAndSendButton = new SaveSendButton();

  protected FuncButton sendButton = new SendButton();

  public FuncButton printButton = new PrintButton();

  public FuncButton importButton = new ImportButton();
  //送国库
  private FuncButton sendGkButton = new SendGkButton();
  
  // 工作流填写意见审核通过
  protected FuncButton suggestPassButton = new SuggestAuditPassButton();

  // 工作流销审
  protected FuncButton unAuditButton = new UnauditButton();

  // 工作流退回
  protected FuncButton unTreadButton = new UntreadButton();

  protected ListCursor<ZcDingdian> listCursor;

  private ZcDingdian oldZcDingDian;

  public ZcDingDianListPanel listPanel;

  protected JTablePanel biTablePanel = new JTablePanel(null, AsOptionMeta.getOptVal(ZcSettingConstants.ZC_OPTON_JIHUA_ZIJIN_HELP_MSG));

  protected JTablePanel itemTablePanel = new JTablePanel();

  protected ZcDingDianEditPanel self = this;

  protected GkBaseDialog parent;

  private ElementConditionDto dtoForBidWinner = new ElementConditionDto();

  private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  private BillElementMeta mainBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd("ZC_DINGDIAN");

  private BillElementMeta biBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd("ZC_DINGDIAN_BI");

  private BillElementMeta itemBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd("ZC_DINGDIAN_ITEM");

  protected IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,

  "zcEbBaseServiceDelegate");

  public IZcDingDianServiceDelegate zcDingDianServiceDelegate = (IZcDingDianServiceDelegate) ServiceFactory.create(IZcDingDianServiceDelegate.class,

  "zcDingDianServiceDelegate");

  protected ElementConditionDto getDto = new ElementConditionDto();

  private BigDecimal bai = new BigDecimal("100");
  
  private ElementConditionDto eaccDto = new ElementConditionDto();

  //

  private IZcEbSupplierServiceDelegate zcEbSupplierServiceDelegate = (IZcEbSupplierServiceDelegate) ServiceFactory.create(

  IZcEbSupplierServiceDelegate.class, "zcEbSupplierServiceDelegate");

  //资金表合计值
  private JLabel biSummaryLabel;

  protected boolean budgetFlag = "Y".equals(AsOptionMeta.getOptVal(ZcSettingConstants.OPT_ZC_USE_BUDGET_INTERFACE));

  private MoneyFieldEditor realDingDianSum=null;

  public ZcDingDianEditPanel(ZcDingDianDialog parent, ListCursor listCursor, String tabStatus, ZcDingDianListPanel listPanel) {
    // TCJLODO Auto-generated constructor stub
    super(ZcDingDianEditPanel.class, BillElementMeta.getBillElementMetaWithoutNd(getCompoId()));

    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), LangTransMeta.translate(getCompoId()),
      TitledBorder.CENTER, TitledBorder.TOP,

      new Font("宋体", Font.BOLD, 15), Color.BLUE));

    this.listCursor = listCursor;

    this.listPanel = listPanel;

    this.parent = parent;

    this.colCount = 3;

    init();

    requestMeta.setCompoId(getCompoId());
    
    if(ZcUtil.isCgdb()){
      //用于代编代报里，置换requestmeta里的信息
      resetRequestMeta();
    }
    
    refreshData();
  }

  private void refreshData() {
    // TCJLODO Auto-generated method stub

    ZcDingdian dd = (ZcDingdian) listCursor.getCurrentObject();

    if (dd != null && !"".equals(ZcUtil.safeString(dd.getDdCode()))) {//列表页面双击进入

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

      dd = zcDingDianServiceDelegate.selectByPrimaryKey(dd.getDdCode(), this.requestMeta);

      if ((dd.getBiList() == null || dd.getBiList().size() == 0) && ZcUtil.isYsdw()) {
        //默认新增一行
        dd.setBiList(new ArrayList());
        ZcDingdianBi bi = new ZcDingdianBi();
        setBIDefaultValue(bi);
        dd.getBiList().add(bi);        
      } else {
        if (budgetFlag) {
          String sumId = "";
          for (int i = 0; i < dd.getBiList().size(); i++) {
            ZcDingdianBi bi = (ZcDingdianBi) dd.getBiList().get(i);
            if (bi.getZcBiNo() == null || "".equals(bi.getZcBiNo())) {
              bi.setZcBiDoSum(BigDecimal.ZERO);
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
      }
      if (budgetFlag) {
        getDto.setZcMakeCode(dd.getDdCode());
      } 
      listCursor.setCurrentObject(dd);
      this.setEditingObject(dd);
    } else {//新增按钮进入

      this.pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;

      dd = new ZcDingdian();

      setDefaultValue(dd);

      listCursor.getDataList().add(dd);

      listCursor.setCurrentObject(dd);

      this.setEditingObject(dd);      

    }
    if (budgetFlag) {
      getDto.setNd(requestMeta.getSvNd());
      getDto.setZcText2("1");//对应sql语句里是可用指标金额>0
      getDto.setCoCode(dd.getCoCode() != null ? dd.getCoCode() : requestMeta.getSvCoCode());
    }
    refreshSubData(dd);
    setOldObject();

    setButtonStatus();

    updateFieldEditorsEditable();

  }

  protected void updateFieldEditorsEditable() {

    ZcDingdian dd = (ZcDingdian) listCursor.getCurrentObject();

    Long processInstId = dd.getProcessInstId();

    if (processInstId != null && processInstId.longValue() > 0) {

      // 工作流的单据

      wfCanEditFieldMap = BillElementMeta.getWfCanEditField(dd, requestMeta);

      if ("cancel".equals(this.oldZcDingDian.getStatus())) {// 撤销单据设置字段为不可编辑

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
      for (AbstractFieldEditor editor : fieldEditors) {
        if (pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT) || pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)) {
          if ("zheRangLv".equals(editor.getFieldName()) || "ddName".equals(editor.getFieldName()) || "remark".equals(editor.getFieldName())
            || "suLinkMan".equals(editor.getFieldName()) || "suLinkTel".equals(editor.getFieldName()) || "wxDate".equals(editor.getFieldName())
            || "coLinkMan".equals(editor.getFieldName()) || "coCode".equals(editor.getFieldName())) {
            editor.setEnabled(true);
          } else {
            editor.setEnabled(false);
          }
          isEdit = true;
        } else {
          editor.setEnabled(false);
        }
        //        logger.debug("===" + editor.getFieldName() + "=" + editor.isEnabled());
      }
    }

    setWFSubTableEditable(biTablePanel, isEdit);

    setWFSubTableEditable(itemTablePanel, isEdit);

  }

  private void setDefaultValue(ZcDingdian dd) {
    // TCJLODO Auto-generated method stub
    dd.setStatus(ZcSettingConstants.WF_STATUS_DRAFT);
    dd.setNd(this.requestMeta.getSvNd());
    dd.setInputDate(this.requestMeta.getSysDate());
    dd.setExcutor(requestMeta.getSvUserID());
    dd.setExcutorName(requestMeta.getSvUserName());

    dd.setSupplier(requestMeta.getSvUserID());
    dd.setSupplierName(requestMeta.getSvUserName());
     

    //设置收账户信息
    setBankInfo(dd); 

    dd.setBiList(new ArrayList());

    dd.setItemList(new ArrayList());

    // 新增数据默认插入一行

    ZcDingdianItem item = new ZcDingdianItem();
    //
    setItemDefaultValue(item);
    //    ZcPProMitem item=new ZcPProMitem();

    dd.getItemList().add(item);
  }

  //设置收账户信息
  private void setBankInfo(ZcDingdian dd) {
    // TCJLODO Auto-generated method stub
    boolean isPayToCgzx = false;
    String isPayToCgzxStr=AsOptionMeta.getOptVal("OPT_ZC_PAY_TO_CGZX");
    if("Y".equalsIgnoreCase(isPayToCgzxStr)){
      isPayToCgzx=true;
    }
    if (isPayToCgzx) {
      dd.setSuBankAccount(AsOptionMeta.getOptVal("OPT_ZC_PAY_TO_CGZX_ACCACOUNT"));
      dd.setSuBank(AsOptionMeta.getOptVal("OPT_ZC_PAY_TO_CGZX_ACCBANK"));
      dd.setSuBankShoukuanren(AsOptionMeta.getOptVal("OPT_ZC_PAY_TO_CGZX_SHOUKUANREN"));
    } else {
      IZcEbSupplierServiceDelegate zcEbSupplierServiceDelegate = (IZcEbSupplierServiceDelegate) ServiceFactory.create(
        IZcEbSupplierServiceDelegate.class, "zcEbSupplierServiceDelegate");
      ZcEbSupplier supplier = zcEbSupplierServiceDelegate.getSupplierById(requestMeta.getSvUserID(), requestMeta);
      if(supplier!=null){
        dd.setSuBank(supplier.getBankName());
        dd.setSuBankAccount(supplier.getAccCode());
        dd.setSuBankShoukuanren(supplier.getBankCode()==null?supplier.getName():supplier.getBankCode());
      }
    }
  }

  protected void setButtonStatus() {
    ZcDingdian dd = (ZcDingdian) listCursor.getCurrentObject();
    if (WFConstants.AUDIT_TAB_STATUS_CANCEL.equals(dd.getStatus())) {
      setCancelStatus(listCursor);
    } else {
      setButtonStatus(dd, requestMeta, this.listCursor);
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

      bs.addBillStatus(ZcSettingConstants.WF_STATUS_DRAFT);

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

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);
      
      bs = new ButtonStatus();

      bs.setButton(this.sendGkButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_AUDITED);

      btnStatusList.add(bs);

      bs = new ButtonStatus();
      bs.setButton(this.importButton);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_NEW);
      btnStatusList.add(bs);

    }

    ZcDingdian dd = (ZcDingdian) this.listCursor.getCurrentObject();

    String billStatus = dd.getStatus();

    ZcUtil.setButtonEnable(this.btnStatusList, billStatus, this.pageStatus, getCompoId(), dd.getProcessInstId());

  }

  protected void setOldObject() {

    oldZcDingDian = (ZcDingdian) ObjectUtil.deepCopy(listCursor.getCurrentObject());

  }

  public void setSumLabelText() {

    JTablePanel tablePanel = biTablePanel;

    ZcDingdian dd = (ZcDingdian) this.listCursor.getCurrentObject();

    List<ZcDingdianBi> biList = dd.getBiList();

    BigDecimal bisum = BigDecimal.ZERO;//实际合计

    for (Iterator iterator = biList.iterator(); iterator.hasNext();) {

      ZcDingdianBi ddBi = (ZcDingdianBi) iterator.next();

      BigDecimal b = ddBi.getZcBiJhuaSum();

      b = b == null ? BigDecimal.ZERO : b;

      bisum = bisum.add(b);

    }

    biSummaryLabel.setText("合计：    合计金额(" + bisum + ") ");

  }

  private void refreshSubData(ZcDingdian dd) {

    biTablePanel.setTableModel(new ZcDingDianToTableModelConverter().convertSubBiTableData(dd.getBiList(), false));

    itemTablePanel.setTableModel(new ZcDingDianToTableModelConverter().convertSubItemTableData(dd.getItemList(),ZcUtil.isGys(),ZcUtil.isYsdw()));

    //    itemTablePanel.setTableModel(new ZcPProMakeToTableModelConverter().convertSubItemTableData(dd.getItemList(),ZcPProMakeToTableModelConverter.itemXyInfo,wfCanEditFieldMap));

    ZcUtil.translateColName(biTablePanel.getTable(), ZcDingDianToTableModelConverter.getBiInfo());

    ZcUtil.translateColName(itemTablePanel.getTable(), ZcDingDianToTableModelConverter.getItemInfo());

    // 设置从表属性 

    setTablePorperty();

    if (ZcUtil.isGys()) {

      JPageableFixedTable ta = itemTablePanel.getTable();

      hideCol(ta, ZcDingdianItem.COL_ITEM_VAL );

      hideCol(ta, ZcDingdianItem.COL_ITEM_BI);

      hideCol(ta, ZcDingdianItem.COL_ITEM_OTHER);

    }

    setSumLabelText();
  }

  protected void hideCol(JTable table, String colName) {

    TableColumn tc = table.getColumn(colName);

    table.getColumnModel().removeColumn(tc);

  }

  private void setTablePorperty() {
    // TCJLODO Auto-generated method stub
    setItemTableEditor(itemTablePanel.getTable());
    addItemTableLisenter(itemTablePanel.getTable());
    //
    //    addBiTableLisenter(biTablePanel.getTable());
    //
    setBiTableEditor(biTablePanel.getTable());
  }

  private void setBiTableEditor(JPageableFixedTable table) {
    // TCJLODO Auto-generated method stub

    table.setDefaultEditor(String.class, new TextCellEditor());
    if (budgetFlag) {

      String colNames[] = { "指标余额表ID", "指标来源", "发文文号", "资金性质","采购项目", "功能分类", "经济分类","是否监督使用","是否政府采购","指标总金额","指标可用金额" };
//      String colNames[] = { "指标余额表ID", "可用金额", "资金性质", "指标类型", "指标来源", "业务处室", "用途", "文号标题", "功能分类" };
      ZcBudgetHandler budgetHandler = new ZcBudgetHandler(colNames, biTablePanel, this, listCursor, getDto);

      ForeignEntityFieldCellEditor suEditor = new ForeignEntityFieldCellEditor("VwBudgetGp.getVwBudgetGp", getDto, 20, budgetHandler, colNames,
        "资金构成", "sumId");

      SwingUtil.setTableCellEditor(table, "ZC_BI_NO", suEditor);

      SwingUtil.setTableCellEditor(table, "ZC_BI_SUM", new MoneyCellEditor(false));

      SwingUtil.setTableCellRenderer(table, "ZC_BI_SUM", new NumberCellRenderer());

      SwingUtil.setTableCellEditor(table, "ZC_BI_DO_SUM", new MoneyCellEditor(false));

      SwingUtil.setTableCellRenderer(table, "ZC_BI_DO_SUM", new NumberCellRenderer());
    }

    SwingUtil.setTableCellEditor(table, "ZC_BI_JHUA_SUM", new MoneyCellEditor(false));

    SwingUtil.setTableCellRenderer(table, "ZC_BI_JHUA_SUM", new NumberCellRenderer());

//    SwingUtil.setTableCellEditor(table, "ZC_BI_YJBA_SUM", new MoneyCellEditor(false));
//
//    SwingUtil.setTableCellRenderer(table, "ZC_BI_YJBA_SUM", new NumberCellRenderer());
//
//    SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_FUND_CODE, new AsValComboBoxCellEditor("ZC_VS_FUND_NAME"));
//
//    SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_FUND_CODE, new AsValCellRenderer("ZC_VS_FUND_NAME"));
//
//    SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_ORIGIN_CODE, new AsValComboBoxCellEditor("ZC_VS_ORIGIN_NAME"));
//
//    SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_ORIGIN_CODE, new AsValCellRenderer("ZC_VS_ORIGIN_NAME"));
//
//    SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_PAYTYPE_CODE, new AsValComboBoxCellEditor("ZC_VS_PAYTYPE_NAME"));
//
//    SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_PAYTYPE_CODE, new AsValCellRenderer("ZC_VS_PAYTYPE_NAME"));

    SwingUtil.setTableCellEditor(table, "ZC_FUND_FILE", new FileCellEditor("zcFundFileBlobid", (BeanTableModel) table.getModel()));

    String status = ((ZcDingdian) listCursor.getCurrentObject()).getStatus();

    if (!"exec".equals(status) && !"yjz".equals(status)) {

//      table.getTableHeader().getColumnModel().removeColumn(table.getColumn("ZC_BI_YJBA_SUM"));

    }
    eaccDto.setNd(requestMeta.getSvNd());

    String colNames[] = { "代码", "名称" };
    ZcEaccHandler budgetHandler = new ZcEaccHandler(colNames);

    ForeignEntityFieldCellEditor suEditor = new ForeignEntityFieldCellEditor("EAcc.getEAccLstForPayDialog", eaccDto, 20, budgetHandler, colNames,
      "经济分类", "name");
    SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_OUTLAY_NAME, suEditor);

  }

  private void setItemTableEditor(JPageableFixedTable table) {
    // TCJLODO Auto-generated method stub
    SwingUtil.setTableCellEditor(table, ZcDingdianItem.COL_ITEM_VAL, new MoneyCellEditor());
    SwingUtil.setTableCellRenderer(table, ZcDingdianItem.COL_ITEM_VAL, new NumberCellRenderer());
    
    SwingUtil.setTableCellEditor(table, ZcDingdianItem.COL_ITEM_BI, new MoneyCellEditor());
    SwingUtil.setTableCellRenderer(table, ZcDingdianItem.COL_ITEM_BI, new NumberCellRenderer());
    
    SwingUtil.setTableCellEditor(table, ZcDingdianItem.COL_ITEM_OTHER, new MoneyCellEditor());
    SwingUtil.setTableCellRenderer(table, ZcDingdianItem.COL_ITEM_OTHER, new NumberCellRenderer());

    SwingUtil.setTableCellEditor(table, ZcDingdianItem.COL_ITEM_TOTAL_SUM, new MoneyCellEditor());
    SwingUtil.setTableCellRenderer(table, ZcDingdianItem.COL_ITEM_TOTAL_SUM, new NumberCellRenderer()); 
    
//    SwingUtil.setTableCellEditor(table, ZcDingdianItem.FIELD_TRANS_QB_ITEM_TYPE, new AsValComboBoxCellEditor("ZC_VS_QB_ITEM_TYPE"));
//    SwingUtil.setTableCellRenderer(table, ZcDingdianItem.FIELD_TRANS_QB_ITEM_TYPE, new AsValCellRenderer("ZC_VS_QB_ITEM_TYPE"));   
 
  }

  private void addItemTableLisenter(final JPageableFixedTable table) {
    // TCJLODO Auto-generated method stub

    final BeanTableModel model = (BeanTableModel) (table.getModel());

    model.addTableModelListener(new TableModelListener() {

      public void tableChanged(TableModelEvent e) {


        if (e.getType() == TableModelEvent.UPDATE) {

          if (e.getColumn() >= 0 && table.getSelectedRow() >= 0) {
            ZcDingdian dd = (ZcDingdian) listCursor.getCurrentObject();
            int k = table.getSelectedRow();
            if (ZcDingdianItem.COL_ITEM_BI.equals(model.getColumnIdentifier(e.getColumn()))
              || ZcDingdianItem.COL_ITEM_OTHER.equals(model.getColumnIdentifier(e.getColumn()))) {

              ZcDingdianItem item = (ZcDingdianItem) (model.getBean(table.convertRowIndexToModel(k)));

              BigDecimal bbm = item.getItemBi()==null?BigDecimal.ZERO:item.getItemBi();

              BigDecimal bom = item.getItemOther()==null?BigDecimal.ZERO:item.getItemOther();
                
              item.setItemVal(bbm.add(bom));
                //                model.fireTableRowsUpdated(k, k);  

            } else if (ZcDingdianItem.COL_ITEM_TOTAL_SUM.equals(model.getColumnIdentifier(e.getColumn()))) {

              ZcDingdianItem item = (ZcDingdianItem) (model.getBean(table.convertRowIndexToModel(k)));

              

              BigDecimal num = BigDecimal.ZERO;
              for (int i = 0; i < table.getRowCount(); i++) {
                ZcDingdianItem row = (ZcDingdianItem) (model.getBean(table.convertRowIndexToModel(i)));
                if (row.getItemTotalSum() != null) {
                  num = num.add(row.getItemTotalSum());
                }
              }
              dd.setDdSum(num);
              self.setEditingObject(listCursor.getCurrentObject());

              //              }          
            }
          }
        }
      }

    });
  }
 

  private void setItemDefaultValue(ZcDingdianItem item) {
    // TCJLODO Auto-generated method stub
    item.setItemTotalSum(new BigDecimal(0));
    
  }

  public static  String getCompoId() {
    // TCJLODO Auto-generated method stub
    return "ZC_DINGDIAN";
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel#initToolBar(com.ufgov.zc.client.component.JFuncToolBar)
   */
  @Override
  public void initToolBar(JFuncToolBar toolBar) {
    // TCJLODO Auto-generated method stub

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(getCompoId());

    toolBar.add(editButton);

    toolBar.add(saveButton);

    toolBar.add(sendButton);

    toolBar.add(saveAndSendButton);

    toolBar.add(suggestPassButton);

    toolBar.add(sendGkButton);

    toolBar.add(unAuditButton);

    toolBar.add(unTreadButton);

    toolBar.add(callbackButton);

    toolBar.add(deleteButton);

//    toolBar.add(importButton);

//    toolBar.add(printButton);

    toolBar.add(traceButton);

    toolBar.add(previousButton);

    toolBar.add(nextButton);

    toolBar.add(exitButton);

 
    editButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doEdit();

      }

    });

    sendGkButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doSendGk();

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

    suggestPassButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        // 填写意见审核

        doSuggestPass();

      }

    });

    unAuditButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 销审

        //        doUnAudit();

      }

    });

    unTreadButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 退回

        doUnTread();

      }

    });

    printButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doPrintButton();

      }

    });
  } 

 

  private void doSendGk() {

    ZcDingdian dd = (ZcDingdian) listCursor.getCurrentObject();

    StringBuffer sb = new StringBuffer();
    sb.append("本次支付供应商账户").append(dd.getSuBankAccount()).append("的金额如下：\n");
    for (ZcDingdianBi bi : (List<ZcDingdianBi>) dd.getBiList()) {
      if (bi.getZcBiNo() != null && bi.getZcBiNo().length() > 0 && bi.getZcBiJhuaSum() != null && bi.getZcBiJhuaSum().longValue() > 0) {
        sb.append("指标文号为").append(bi.getSenddocName()).append(" 拨款金额为").append(bi.getZcBiJhuaSum().toString()).append("元\n");
      }
    }
    sb.append("确定要生成拨款单吗？");

    int i = JOptionPane.showConfirmDialog(this, sb.toString(), "提示", JOptionPane.YES_NO_OPTION);
    if (i != 0) {
      return;
    }

    String msg = "";
    try {
      dd.setComment("同意");
      dd.setAuditorId(WorkEnv.getInstance().getCurrUserId());
      dd = zcDingDianServiceDelegate.sendPayFN(dd, requestMeta);
      JOptionPane.showMessageDialog(this, "提交成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.listPanel.refreshCurrentTabData();
      refreshData();
    } catch (Exception e) {
      msg = e.getMessage();
      logger.error(e.getMessage(), e);
      JOptionPane.showMessageDialog(this, "提交失败！\n" + msg, "错误", JOptionPane.ERROR_MESSAGE);
    }
    

    
  }
  protected void doSend() {

    boolean success = true;

    ZcDingdian afterSaveBill = null;

    if (this.isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    try {

      requestMeta.setFuncId(this.sendButton.getFuncId());

      ZcDingdian dd = (ZcDingdian) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      dd.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      afterSaveBill = zcDingDianServiceDelegate.newCommitFN(dd, requestMeta);

    } catch (Exception ex) {

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {
      this.listCursor.setCurrentObject(afterSaveBill);
      refreshData();

      JOptionPane.showMessageDialog(this, "送审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

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

        listCursor.setCurrentObject(oldZcDingDian);

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

        listCursor.setCurrentObject(oldZcDingDian);

      }

    }

    listCursor.next();

    refreshData();

  }

  public boolean doSave() {

    if (!isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据没有发生改变，不用保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return true;

    }

    if (!checkBeforeSave()) {

      return false;

    }

    boolean success = true;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(saveButton.getFuncId());

      ZcDingdian inData = (ZcDingdian)this.listCursor.getCurrentObject();
      System.out.println("before="+inData.getCoCode()+inData.getCoName());

      ZcDingdian dd = zcDingDianServiceDelegate.updateFN(inData, this.requestMeta);

      System.out.println("after="+dd.getCoCode()+dd.getCoName());
      
      listCursor.setCurrentObject(dd);

    } catch (Exception e) {

      logger.error(e.getMessage(), e);

      success = false;

      errorInfo += e.getMessage();

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      refreshData();

      this.listPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

    return success;

  }

  /**

   * 保存前校验

   * @param cpApply

   * @return

   */

  protected boolean checkBeforeSave() {

    List mainNotNullList = mainBillElementMeta.getNotNullBillElement();

    List biNotNullList = biBillElementMeta.getNotNullBillElement();

    List itemNotNullList = itemBillElementMeta.getNotNullBillElement();

    ZcDingdian dd = (ZcDingdian) this.listCursor.getCurrentObject();

    StringBuilder errorInfo = new StringBuilder();

    String mainValidateInfo = ZcUtil.validateBillElementNull(dd, mainNotNullList);

    String biValidateInfo = ZcUtil.validateDetailBillElementNull(dd.getBiList(), biNotNullList, false);

    String itemValidateInfo = ZcUtil.validateDetailBillElementNull(dd.getItemList(), itemNotNullList, false);

    if (mainValidateInfo.length() != 0) {

      errorInfo.append("\n").append(mainValidateInfo.toString()).append("\n");

    }

    if (biValidateInfo.length() != 0 && ZcUtil.isYsdw()) {

      errorInfo.append("资金构成：\n").append(biValidateInfo.toString()).append("\n");

    }

    if (itemValidateInfo.length() != 0) {

      errorInfo.append("保险明细：\n").append(itemValidateInfo.toString()).append("\n");

    }
    List itemList = dd.getItemList();

    if (itemList == null || itemList.size() == 0)
      errorInfo.append("保险明细不可为空\n").append(itemValidateInfo.toString()).append("\n");
    

    if (errorInfo.length() != 0) {

      JOptionPane.showMessageDialog(this, errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);

      return false;

    }
    if(ZcUtil.isGys()){
      for (Object o : dd.getItemList()) {

        ZcDingdianItem bi = (ZcDingdianItem) o;
        if(bi.getItemType()==null){
          JOptionPane.showMessageDialog(this, "请填写险种", "提示", JOptionPane.WARNING_MESSAGE);
          return false;          
        }
      }
    }

    if (ZcUtil.isYsdw()) {
      BigDecimal sum = new BigDecimal(0);

      for (Object o : dd.getBiList()) {

        ZcDingdianBi bi = (ZcDingdianBi) o;

        BigDecimal biSum = (BigDecimal) ObjectUtils.defaultIfNull(bi.getZcBiJhuaSum(), new BigDecimal(0));

        sum = sum.add(biSum);

      }
      if (sum.compareTo(dd.getDdSum()) != 0) {
        errorInfo.append("资金构成：\n预算金额合计应等于保险单折让后的金额！\n");
      }
      String outLayIsLeaf = checkOutLay();
      if (outLayIsLeaf != null && outLayIsLeaf.trim().length() > 0) {
        errorInfo.append(outLayIsLeaf).append("\n");
      }
      //支付时，如果选择的指标的资金性质不是经费拨款，则进行收入教研，要计算这个预算单位的收入是否够当前支付的指标金额
      String shourouInfo = checkShouRu();
      if (shourouInfo != null && shourouInfo.trim().length() > 0) {
        errorInfo.append(shourouInfo).append("\n");
      }
      if (errorInfo.length() != 0) {
        JOptionPane.showMessageDialog(this, errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);
        return false;

      }
    }
    if (ZcUtil.isYsdw() && !calcBeforeSave()) {

      return false;

    }

    return true;
  }

  /**
   * 支付时，如果选择的指标的资金性质不是经费拨款，则进行收入教研，要计算这个预算单位的收入是否够当前支付的指标金额
   * @return
   */
  private String checkShouRu() {
    // TCJLODO Auto-generated method stub
    if("Y".equalsIgnoreCase(AsOptionMeta.getOptVal("OPT_ZC_SHOURU "))){
      //获取不受收入控制的资金性质
      String zjxzStr=AsOptionMeta.getOptVal("OPT_ZC_NO_SHOURU_ZJXZ");
      zjxzStr=zjxzStr==null?"":zjxzStr;
      String[] zjxzArray=zjxzStr.split(",");
      
      StringBuffer biStrs = new StringBuffer();
      StringBuffer error=new StringBuffer();
      BigDecimal zhifu=BigDecimal.ZERO;
      ZcDingdian dd = (ZcDingdian) this.listCursor.getCurrentObject();
      for (int i = 0; i < dd.getBiList().size(); i++) {
        ZcDingdianBi bi = (ZcDingdianBi) dd.getBiList().get(i);
        boolean flag=false;
        if (bi.getZcBiNo() != null && !bi.getZcBiNo().startsWith(ZcSettingConstants.No_BI)) {//自筹资金不需要检查经济分类是否末级
          
          for (String zjxzCode : zjxzArray) {
            if(zjxzCode.equals(bi.getFundCode())){
              flag=true;//这条指标不属于收入控制的范围
            }
          }
          if (!flag) {//这条指标属于收入控制的范围
            zhifu=zhifu.add(bi.getZcBiJhuaSum());
            biStrs.append(bi.getZcBiNo()).append(",");
          }
        }
      }
      if(zhifu.doubleValue()>0){
        //获取单位的收入情况
        ElementConditionDto dto=new ElementConditionDto();
        dto.setNd(requestMeta.getSvNd());
        dto.setCoCode(requestMeta.getSvCoCode());
        BigDecimal shouru=(BigDecimal) zcEbBaseServiceDelegate.queryObject("VwBudgetGp.selectShouRou", dto, requestMeta);
        if(zhifu.compareTo(shouru)==1){
          error.append("您单位的当前可用收入数为：").append(shouru.doubleValue()).append("\n");
          error.append("当前受收入控制的指标支付金额为：").append(zhifu.doubleValue()).append("\n");
          error.append("指标编号：").append(biStrs).append("\n");
          error.append("请暂停支付当前单据或重新选择不受收入控制的指标进行支付").append("\n");
        }
      }
      if (error.length() > 0) {       
        return error.toString();
      }
    }
    return null;
  }
  /**
   * 检查经济分类是否末级
   * @return
   */
  private String checkOutLay() {
    // TCJLODO Auto-generated method stub
    boolean check = "Y".equalsIgnoreCase(AsOptionMeta.getOptVal("OPT_ZC_CHECK_OUT_LAY_IS_LEAF"));

    if (check) {
      String rtn = "";
      ZcDingdian dd = (ZcDingdian) this.listCursor.getCurrentObject();
      for (int i = 0; i < dd.getBiList().size(); i++) {
        ZcDingdianBi bi = (ZcDingdianBi) dd.getBiList().get(i);
        if (bi.getZcBiNo() != null && !bi.getZcBiNo().startsWith(ZcSettingConstants.No_BI)) {//自筹资金不需要检查经济分类是否末级
          if (bi.getOutLayIsLeaf() == null || ("0".equalsIgnoreCase(bi.getOutLayIsLeaf()) || "N".equalsIgnoreCase(bi.getOutLayIsLeaf()))) {
            rtn += bi.getZcBiNo() + " ";
          }
        }
      }
      if (rtn.trim().length() > 0) {
        rtn = "以下指标支付时，需选择末级经济科目:\n" + rtn.trim();
        return rtn;
      }
    }

    return null;
  }
  protected void doDelete() {

    requestMeta.setFuncId(deleteButton.getFuncId());

    ZcDingdian dd = (ZcDingdian) this.listCursor.getCurrentObject();

    if (dd.getDdCode() == null || "".equalsIgnoreCase(dd.getDdCode())) {

      JOptionPane.showMessageDialog(this, "尚未保存到数据库，无需删除！", "提示", JOptionPane.ERROR_MESSAGE);

      return;

    }

    int num = JOptionPane.showConfirmDialog(this, "是否删除当前单据", "删除确认", 0);

    if (num == JOptionPane.YES_OPTION) {

      boolean success = true;

      String errorInfo = "";

      try {

        requestMeta.setFuncId(deleteButton.getFuncId());

        zcDingDianServiceDelegate.deleteFN(dd, this.requestMeta);

      } catch (Exception e) {

        logger.error(e.getMessage(), e);

        success = false;

        errorInfo += e.getMessage();

      }

      if (success) {

        this.listCursor.removeCurrentObject();

        JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

//        this.refreshData();

        this.listPanel.refreshCurrentTabData();
        
        doExit();

      } else {

        JOptionPane.showMessageDialog(this, "删除失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

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

    ZcDingdian afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.callbackButton.getFuncId());

      ZcDingdian dd = (ZcDingdian) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      dd.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      afterSaveBill = zcDingDianServiceDelegate.callbackFN(dd, requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      refreshData();

      JOptionPane.showMessageDialog(this, "收回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "收回失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  protected void doSuggestPass() {

    if (!checkBeforeSave()) {

      return;

    }
    if(notSameCoBudget()){
      JOptionPane.showMessageDialog(this, "指标对应的预算单位不是当前采购单位，请在资金构成中重新选择本单位的预算指标.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    ZcDingdian dd = (ZcDingdian) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

    requestMeta.setFuncId(this.suggestPassButton.getFuncId());

    executeAudit(dd, ZcSettingConstants.IS_GOON_AUDIT_YES, null);

  }

/**
 * 判断当前审批单据的cocode与其资金的cocode是否一致，如果不一致，则提示，不能进行审批
 * 为什么这么做，因为丹徒的支付是供应商发起，然后采购单位匹配资金，有时候匹配了资金后，采购单位会退回给供应商
 * 这是供应商如果重新选择了新的采购单位，这时单据上的资金时上一个采购单位的，所以为了防止这种情况发生，在采购单位
 * 审批时，对主单的cocode和资金表的cocode进行校验
 * @return
 */
  private boolean notSameCoBudget() {
    // TCJLODO Auto-generated method stub
    if(ZcUtil.isYsdw() || ZcUtil.isCgdb()){
      ZcDingdian bean = (ZcDingdian) this.listCursor.getCurrentObject();
      if(bean.getBiList()==null || bean.getBiList().size()==0)return false;
      List idLst=new ArrayList();
      StringBuffer idbf=new StringBuffer();
      for(int i=0;i<bean.getBiList().size();i++){
        ZcDingdianBi bi=(ZcDingdianBi) bean.getBiList().get(i);
        if(bi.getZcBiNo()==null||bi.getZcBiNo().startsWith("NoBi")){
          continue;
        }
        if(!bi.getZcBiNo().startsWith("NoBi")){
          if(idbf.length()>0){
            idbf.append(",");
          }
          idbf.append("'").append(bi.getZcBiNo()).append("'");
        }
      }
      if(idbf.length()==0){//没有指标，只有自筹资金的情况下，这个值是空的，因此默认给置成空的
        idbf.append("''");
      }
      List budgetLst=zcEbBaseServiceDelegate.queryDataForList("VwBudgetGp.getVwBudgetGpByBiNoLst", idbf.toString(), requestMeta);
      if(budgetLst==null){
        return false;
      }
      for(int i=0;i<budgetLst.size();i++){
        VwBudgetGp bg=(VwBudgetGp)budgetLst.get(i);
        if(!bean.getCoCode().equals(bg.getEnCode())){
          return true;
        }
      }
    }
    return false;
  }
  protected void executeAudit(ZcDingdian dd, int isGoonAudit, String defaultMsg) {

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

      dd.setComment(commentDialog.getComment());

      dd.setAuditorId(WorkEnv.getInstance().getCurrUserId());

//      zcDingDianServiceDelegate.updateFN(ht, requestMeta);

      dd=zcDingDianServiceDelegate.auditFN(dd, requestMeta);

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

  protected boolean calcBeforeSave() {

    ZcDingdian dd = (ZcDingdian) this.listCursor.getCurrentObject();

    String errorinfo = Calc(dd);

    if (errorinfo != null && !"".equals(errorinfo)) {

      //errorinfo = errorinfo +;

      JOptionPane.showMessageDialog(this, errorinfo, "提示", JOptionPane.ERROR_MESSAGE);

      return false;

    } else {

      return true;

    }

  }

  protected String Calc(ZcDingdian dd) {

    StringBuffer sb = new StringBuffer();

    List<ZcDingdianBi> biList = dd.getBiList();

    BigDecimal shiJiTotal = BigDecimal.ZERO;//实际合计

    BigDecimal czxZijin = BigDecimal.ZERO;//使用-财政性资金

    BigDecimal zcZijin = BigDecimal.ZERO;//使用-自筹资金

    for (Iterator iterator = biList.iterator(); iterator.hasNext();) {

      ZcDingdianBi ddBi = (ZcDingdianBi) iterator.next();

      BigDecimal shiyongSum = ddBi.getZcBiJhuaSum();//本次使用金额

      shiyongSum = shiyongSum == null ? BigDecimal.ZERO : shiyongSum;

      if (ZcDingdianBi.TYPE_CZYS.equals(ddBi.getZcZjType())) {//财政预算
        if (shiyongSum.compareTo(ddBi.getZcBiDoSum()) > 0) {

          sb.append("资金构成中【本次采购使用金额】不能超过【可用金额】！\n");

          return sb.toString();
        }
        czxZijin = czxZijin.add(shiyongSum);
      } else {
        zcZijin = zcZijin.add(shiyongSum);
      }
      shiJiTotal = shiJiTotal.add(shiyongSum);
    }

    if (shiJiTotal.doubleValue() <= 0) {

      sb.append("资金构成中【本次采购使用金额】必须填写！\n");

      return sb.toString();

    }

    if (ZcUtil.isYsdw()) {
      List<ZcDingdianItem> itemLst = dd.getItemList();

      BigDecimal itemCzxzjSum = BigDecimal.ZERO;//财政性资金

      BigDecimal itemZczjSum = BigDecimal.ZERO;//自筹资金

      for (Iterator iterator = itemLst.iterator(); iterator.hasNext();) {

        ZcDingdianItem item = (ZcDingdianItem) iterator.next();
        BigDecimal _itemSum = item.getItemVal() == null ? BigDecimal.ZERO : item.getItemVal();
        BigDecimal _czxzjSum = item.getItemBi() == null ? BigDecimal.ZERO : item.getItemBi();
        BigDecimal _zczjSum = item.getItemOther() == null ? BigDecimal.ZERO : item.getItemOther();

        itemCzxzjSum = itemCzxzjSum.add(_czxzjSum);
        itemZczjSum = itemZczjSum.add(_zczjSum);
      }

      if (czxZijin.compareTo(itemCzxzjSum) != 0) {

        sb.append("资金构成中【预算指标】之和必须等于保险明细中【资金划分的预算指标】之和！\n");

        return sb.toString();
      }

      if (zcZijin.compareTo(itemZczjSum) != 0) {

        sb.append("资金构成中【自筹资金】之和必须等于保险明细中【资金划分中自筹资金】之和！\n");

        return sb.toString();
      }
    }

    return sb.toString();

  }

  protected String getHtNumLabel() {
    return LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_NUM);
  }

  /*

   * 销审

   */

  protected void doUnAudit() {
    /**
     * 合同备案，销审添加校验,做了支付申请确认单和验收单的，不让销审。
     * 
     */
    ZcDingdian dd = (ZcDingdian) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
    List billlist = zcEbBaseServiceDelegate.queryDataForList("ZC_HT_PRE_PAY_BILL.abatorgenerated_selectByHtCode", dd.getDdCode(), requestMeta);

    if (billlist != null && billlist.size() > 0) {
      JOptionPane.showMessageDialog(this, "该合同已经做了支付申请确认单，不允许销审！", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    List yanShouBillList = zcEbBaseServiceDelegate.queryDataForList("ZcEbYanShou.getEbYanShouBillByPackCode", dd.getDdCode(), requestMeta);
    if (yanShouBillList != null && yanShouBillList.size() > 0) {
      JOptionPane.showMessageDialog(this, "该合同已经做了验收单，不允许销审！", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    boolean success = true;

    ZcDingdian afterSaveBill = null;

    String errorInfo = "";

    int i = JOptionPane.showConfirmDialog(this, "是否确定消审？", "确认", JOptionPane.INFORMATION_MESSAGE);

    if (i != 0) {

      return;

    }

    try {

      requestMeta.setFuncId(unAuditButton.getFuncId());

      dd.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      afterSaveBill = zcDingDianServiceDelegate.unAuditFN(dd, requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      this.listCursor.setCurrentObject(afterSaveBill);

      refreshData();

      JOptionPane.showMessageDialog(this, "销审成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "销审失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

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

    return !DigestUtil.digest(oldZcDingDian).equals(DigestUtil.digest(listCursor.getCurrentObject()));

  }

  /*

   * 退回

   */

  protected void doUnTread() {


    GkCommentUntreadDialog commentDialog = new GkCommentUntreadDialog(DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),

    ModalityType.APPLICATION_MODAL);

    if (commentDialog.cancel) {

      return;

    }

    boolean success = true;

    ZcDingdian afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(unTreadButton.getFuncId());

      ZcDingdian dd = (ZcDingdian) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      dd.setAuditorId(WorkEnv.getInstance().getCurrUserId());

      dd.setComment(commentDialog.getComment());

      afterSaveBill = zcDingDianServiceDelegate.untreadFN(dd, requestMeta);

    } catch (Exception e) {

      success = false;

      logger.error(e.getMessage(), e);

      errorInfo += e.getMessage();

    }

    if (success) {

      this.listCursor.setCurrentObject(afterSaveBill);

      refreshData();

      JOptionPane.showMessageDialog(this, "退回成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "退回失败 ！" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

  }

  private void doPrintButton() {

  }

  private void doEdit() {

    this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;

    updateFieldEditorsEditable();

    setButtonStatus();

  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel#createFieldEditors()
   */
  @Override
  public List<AbstractFieldEditor> createFieldEditors() {

    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    AsValFieldEditor ddStatus = new AsValFieldEditor(LangTransMeta.translate(ZcDingdian.COL_STATUS), "status",ZcDingdian.ZC_VS_DD_STATUS);

    AutoNumFieldEditor ddCode = new AutoNumFieldEditor(LangTransMeta.translate(ZcDingdian.COL_DD_CODE), "ddCode");

    TextFieldEditor ddName = new TextFieldEditor(LangTransMeta.translate(ZcDingdian.COL_DD_NAME), "ddName");

    IForeignEntityTreeHandler companyHandler = new IForeignEntityTreeHandler() {
      @Override
      public void excute(List selectedDatas) {
        if (selectedDatas != null && selectedDatas.size() > 0) {
          Company company = (Company) selectedDatas.get(0);
          selectCompany(company);
        }
      }

      @Override
      public boolean isMultipleSelect() {
        return false;
      }

      @Override
      public boolean isSelectLeaf() {
        return true;
      }
    };

    CompanyFieldEditor coName = new CompanyFieldEditor(LangTransMeta.translate(ZcDingdian.COL_CO_CODE), "coCode", companyHandler);

    TextFieldEditor suName = new TextFieldEditor(LangTransMeta.translate(ZcDingdian.COL_SUPPLIER_NAME), "supplierName");

    TextFieldEditor zcSuBankName = new TextFieldEditor(LangTransMeta.translate(ZcDingdian.COL_SU_BANK), "suBank");

    TextFieldEditor zcSuAccCode = new TextFieldEditor(LangTransMeta.translate(ZcDingdian.COL_SU_BANK_ACCOUNT), "suBankAccount");

    TextFieldEditor shoukuanren = new TextFieldEditor(LangTransMeta.translate(ZcDingdian.COL_SU_BANK_SHOUKUANREN),
      "suBankShoukuanren");

    TextFieldEditor zcSuLinkman = new TextFieldEditor(LangTransMeta.translate(ZcDingdian.COL_SU_LINK_MAN), "suLinkMan");

    TextFieldEditor zcSuLinktel = new TextFieldEditor(LangTransMeta.translate(ZcDingdian.COL_SU_LINK_TEL), "suLinkTel");

    MoneyFieldEditor ddSum = new MoneyFieldEditor("金额", "ddSum");

    realDingDianSum = new MoneyFieldEditor("折后金额[=金额X(1-折让率)]", "realDingDianSum");

   

    TextAreaFieldEditor remark = new TextAreaFieldEditor("备注信息", "remark",200,2,5);

    DateFieldEditor inputDate = new DateFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_INPUT_DATE), "inputDate");
    
//    SelectFileFieldEditor excelModelFile=new SelectFileFieldEditor("保险明细模板下载","detailModelFileName","detailModelFileId",false,false,false,true);

    editorList.add(ddCode);
    editorList.add(ddName);
    editorList.add(ddStatus);

    editorList.add(ddSum);
    editorList.add(coName);
    editorList.add(inputDate);

    editorList.add(suName);
    editorList.add(zcSuLinkman);
    editorList.add(zcSuLinktel);

    editorList.add(zcSuBankName);
    editorList.add(zcSuAccCode);
    editorList.add(shoukuanren);

    editorList.add(remark);
//    editorList.add(excelModelFile);

    return editorList;

  }


  protected void selectCompany(Company company) {
    // TCJLODO Auto-generated method stub
    ZcDingdian dd=(ZcDingdian)listCursor.getCurrentObject();
    dd.setCoCode(company.getCode());
    dd.setCoName(company.getName());
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel#createSubBillPanel()
   */
  @Override
  public JComponent createSubBillPanel() {
    // TCJLODO Auto-generated method stub
    final JTabbedPane topTabPane = new JTabbedPane();

    JTabbedPane biTabPane = new JTabbedPane();

    biTablePanel.init();

    biTablePanel.getSearchBar().setVisible(false);

    biTablePanel.setTablePreferencesKey(this.getClass().getName() + "_biTable");

    biTablePanel.getTable().setShowCheckedColumn(true);

    biTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    biTabPane.addTab("资金构成", biTablePanel);

    JFuncToolBar bottomToolBar1 = new JFuncToolBar();

    FuncButton addBtn1 = new SubaddButton(false);

    JButton insertBtn1 = new SubinsertButton(false);

    JButton delBtn1 = new SubdelButton(false);

    bottomToolBar1.add(addBtn1);

    bottomToolBar1.add(insertBtn1);

    bottomToolBar1.add(delBtn1);

    //    biTablePanel.add(bottomToolBar1, BorderLayout.SOUTH);

    biTablePanel.add(bottomToolBar1, BorderLayout.SOUTH);

    biSummaryLabel = new JLabel("合计");

    biSummaryLabel.setName("_SUM_LABEL");

    biSummaryLabel.setForeground(Color.black);

    biSummaryLabel.setFont(new Font("宋体", Font.BOLD, 12));

    JPanel p = new JPanel();

    p.setBackground(new Color(99, 184, 255));

    p.setName("_SUM_PANEL");

    p.add(biSummaryLabel, BorderLayout.CENTER, -1);

//    biTablePanel.add(p, BorderLayout.SOUTH, -1);

    addBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcDingdianBi ddBi = new ZcDingdianBi();
        setBIDefaultValue(ddBi);

        int rowNum = addSub(biTablePanel, ddBi);

        biTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    insertBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcDingdianBi ddBi = new ZcDingdianBi();

        setBIDefaultValue(ddBi);

        int rowNum = insertSub(biTablePanel, ddBi);

        biTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    delBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        Integer[] checkedRows = deleteBiSub(biTablePanel);

        // 从新计算采购预算

        if (checkedRows.length > 0) {

          self.caculateMoney(((BeanTableModel<ZcDingdianBi>) biTablePanel.getTable().getModel()).getDataBeanList());

        }

      }

    });

    JTabbedPane itemTabPane = new JTabbedPane();

    itemTablePanel.init();

    itemTablePanel.setPanelId(this.getClass().getName() + "_itemTablePanel");

    itemTablePanel.getSearchBar().setVisible(false);

    itemTablePanel.setTablePreferencesKey(this.getClass().getName() + "_itemTable");

    itemTablePanel.getTable().setShowCheckedColumn(true);

    itemTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    itemTabPane.addTab("保险明细", itemTablePanel);

    JGroupableTableHeader itemTableHeader = itemTablePanel.getTable().getTableHeader();

    //    itemTableHeader.addColumnGroup("采购预算资金", new String[] { "ZC_ITEM_SUM", "BUDGET_BI_MONEY", "BUDGET_OTHER_MONEY" });

    itemTableHeader.addColumnGroup("资金划分", new String[] { ZcElementConstants.FIELD_TRANS_QB_ITEM_VAL, ZcElementConstants.FIELD_TRANS_QB_ITEM_BI, ZcElementConstants.FIELD_TRANS_QB_ITEM_OTHER });

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

        ZcDingdianItem item = new ZcDingdianItem();

        item.setDdCode(Guid.genID());
        setItemDefaultValue(item);

        int rowNum = addSub(itemTablePanel, item);

        itemTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    insertBtn2.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcDingdianItem item = new ZcDingdianItem();

        item.setDdCode(Guid.genID());
        setItemDefaultValue(item);

        int rowNum = insertSub(itemTablePanel, item);

        itemTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    delBtn2.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        deleteSub(itemTablePanel);

      }

    });

    biTabPane.setMinimumSize(new Dimension(240, 150));

    itemTabPane.setMinimumSize(new Dimension(240, 150));

    JSaveableSplitPane splitPane = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, biTabPane, itemTabPane);

    splitPane.setDividerDefaultLocation(this.getClass().getName() + "_splitPane_dividerLocation", 200);

    splitPane.setContinuousLayout(true);

    if (ZcUtil.isGys()) {

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

    topTabPane.addTab("保险单明细", splitPane);

    //    topTabPane.addTab("付款明细", detailTablePanel);

    //    topTabPane.addTab("合同文本", wordPane);
    if (ZcUtil.isGys()) {
      return itemTabPane;
    }
    return topTabPane;
  }

  protected void setBIDefaultValue(ZcDingdianBi ddBi) {
    // TCJLODO Auto-generated method stub
    ddBi.setZcProBiSeq(Guid.genID());
    ddBi.setFundCode("0");
    ddBi.setFundName("自筹资金");
    ddBi.setZcZjType(ZcDingdianBi.TYPE_ZCZJ);
  }

  protected void caculateMoney(List<ZcDingdianBi> biList) {

    setSumLabelText();

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
          ZcDingdianBi bi = (ZcDingdianBi) ((ZcDingdian) listCursor.getCurrentObject()).getBiList().get(selRows[i]);
          if (ZcDingdianBi.TYPE_CZYS.equals(bi.getZcZjType()) && bi.getZcBiNo() != null && !"".equals(bi.getZcBiNo())) {
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

  public void doExit() {
    // TCJLODO Auto-generated method stub

    /*if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave()) {

          return;

        }

      }

    }*/

    this.parent.dispose();

  }
  /*
   * 
   */
  private class ZcEaccHandler implements IForeignEntityHandler {
    private String columNames[];

    public ZcEaccHandler(String columNames[]) {
      this.columNames = columNames;
    }

    public boolean beforeSelect(ElementConditionDto dto) {

      JTable table = biTablePanel.getTable();

      BeanTableModel model = (BeanTableModel) table.getModel();

      int k = table.getSelectedRow();

      if (k < 0)

        return false;

      int k2 = table.convertRowIndexToModel(k);

      ZcDingdianBi item = (ZcDingdianBi) model.getBean(k2);

      //      dto.setZcText0(item.getOutLayCode());
      dto.setZcText0(getOldOutLayCode(item.getZcBiNo()));

      return true;

    }

    public void excute(List selectedDatas) {

      JTable table = biTablePanel.getTable();

      BeanTableModel model = (BeanTableModel) table.getModel();

      int k = table.getSelectedRow();

      if (k < 0)

        return;
      int k2 = table.convertRowIndexToModel(k);

      ZcDingdianBi item = (ZcDingdianBi) model.getBean(k2);

      for (Object object : selectedDatas) {
        EAcc e = (EAcc) object;
        item.setOutlayCode(e.getChrId());
        item.setOutlayName(e.getName());
        item.setOutLayIsLeaf(e.getIsLowest());
        return;
      }
    }

    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];
      for (int i = 0; i < showDatas.size(); i++) {
        EAcc e = (EAcc) showDatas.get(i);
        int col = 0;
        data[i][col++] = e.getCode();
        data[i][col++] = e.getName();

      }
      MyTableModel model = new MyTableModel(data, columNames) {
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
  private Map<String, String> oldOutLayCodeMap=new HashMap<String, String>();
  private String getOldOutLayCode(String zcBiNo) {
    // TCJLODO Auto-generated method stub
    if(oldOutLayCodeMap.get(zcBiNo)!=null){
      return oldOutLayCodeMap.get(zcBiNo);
    }
    VwBudgetGp gp = (VwBudgetGp) zcEbBaseServiceDelegate.queryObject("VwBudgetGp.getVwBudgetGpByBiNo", zcBiNo, requestMeta);

    oldOutLayCodeMap.put(zcBiNo, gp.getBsiId());
    return gp.getBsiId();
  } 
  
  /**
   * //用于代编代报里，置换requestmeta里的信息
   */
    private void resetRequestMeta() {
      // TCJLODO Auto-generated method stub
      
      ZcDingdian ht = (ZcDingdian) listCursor.getCurrentObject();
      if(ht.getCoCode()!=null && ht.getDdCode()!=null && !ht.getDdCode().equals("自动编号")){        
        RequestMeta meta = (RequestMeta) ObjectUtil.deepCopy(this.requestMeta);
        ElementConditionDto dto=new ElementConditionDto();
        dto.setZcText0(requestMeta.getSvUserID());
        dto.setZcText1(ht.getCoCode());
        dto.setNd(requestMeta.getSvNd());
        ZcEbDaibian daibian=(ZcEbDaibian) zcEbBaseServiceDelegate.queryObject("ZcEbDaiBian.getCompanyByDaibian", dto, meta);
        if(daibian!=null){
          meta.setSvUserID(daibian.getCoUser());
          meta.setSvUserName(daibian.getEmpName());
          meta.setSvCoCode(daibian.getCoCode());
          meta.setSvCoName(daibian.getCoName());
          meta.setEmpCode(daibian.getCoUser());
          meta.setEmpName(daibian.getEmpName());    
          meta.setSvPoCode(daibian.getPosiCode());
          meta.setSvOrgPoCode(daibian.getOrgPosiCode());
          requestMeta=meta;
        }
      }
    }
}
