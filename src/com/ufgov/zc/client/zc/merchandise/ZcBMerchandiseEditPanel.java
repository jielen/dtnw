package com.ufgov.zc.client.zc.merchandise;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;

import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcBMerchandiseToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.AddButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.EnableButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.InvalidButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.PauseEvalExpertButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.celleditor.IntCellEditor;
import com.ufgov.zc.client.component.table.celleditor.MoneyCellEditor;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.NumberCellRenderer;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AutoNumFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CheckBoxFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.FileFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextAreaFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.commonbiz.publish.IBaseDataServiceDelegate;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZCDiYuCtg;
import com.ufgov.zc.common.zc.model.ZcBBrand;
import com.ufgov.zc.common.zc.model.ZcBCatalogue;
import com.ufgov.zc.common.zc.model.ZcBMerDiscount;
import com.ufgov.zc.common.zc.model.ZcBMerchandise;
import com.ufgov.zc.common.zc.model.ZcBdSppc;
import com.ufgov.zc.common.zc.model.ZcEbSupplier;

@SuppressWarnings("unchecked")
public class ZcBMerchandiseEditPanel extends AbstractMainSubEditPanel {

  private static final Logger logger = Logger.getLogger(ZcBMerchandiseEditPanel.class);

  protected RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private String compoId = "ZC_B_MERCHANDISE";

  private FuncButton addButton = new AddButton();

  private FuncButton editButton = new EditButton();

  private FuncButton previousButton = new PreviousButton();

  private FuncButton saveButton = new SaveButton();

  private FuncButton deleteButton = new DeleteButton();

  private FuncButton nextButton = new NextButton();

  private FuncButton exitButton = new ExitButton();

  // 送审
  private FuncButton auditButton = new EnableButton();

  // 销审
  private FuncButton unAuditButton = new PauseEvalExpertButton();

  // 作废
  private FuncButton cancelButton = new InvalidButton();

  //子表添加按钮
  private FuncButton addBtn1 = new SubaddButton(false);

  //子表插入按钮
  private JButton insertBtn1 = new SubinsertButton(false);

  //子表删除按钮
  private JButton delBtn1 = new SubdelButton(false);

  private ForeignEntityFieldEditor pcId;

  private ForeignEntityFieldEditor brandId;

  protected ListCursor listCursor;

  protected ZcBMerchandise oldBean;

  protected ZcBMerchandiseListPanel listPanel;

  private JTabbedPane jTabbedPane = new JTabbedPane();

  private JTablePanel itemTablePanel = new JTablePanel();

  private JFuncToolBar bottomToolBar1 = null;

  private ZcBMerchandiseEditPanel self = this;

  private GkBaseDialog parent;

  ElementConditionDto elementConditionDto = new ElementConditionDto();

  protected String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

  private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  public CompanyFieldEditor coCode = null;

  public CheckBoxFieldEditor flagLocal = null;

  public IBaseDataServiceDelegate baseDataServiceDelegate = (IBaseDataServiceDelegate) ServiceFactory.create(IBaseDataServiceDelegate.class,

  "baseDataServiceDelegate");

  ElementConditionDto elementDto = new ElementConditionDto();

  public ZcBMerchandiseEditPanel() {

  }

  public ZcBMerchandiseEditPanel(Class billClass, BillElementMeta eleMeta) {
    super(billClass, eleMeta);
  }

  public ZcBMerchandiseEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcBMerchandiseListPanel listPanel) {
    super(ZcBMerchandise.class, listPanel.getBillElementMeta());
    initPinpEditPanel(parent, listCursor, listPanel);
  }

  protected void initPinpEditPanel(GkBaseDialog parent, ListCursor listCursor,

  ZcBMerchandiseListPanel listPanel) {

    String title = LangTransMeta.translate("ZC_B_MERCHANDISE_TITLE");

    this.listCursor = listCursor;

    this.listPanel = listPanel;

    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title,

    TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));

    this.parent = parent;

    this.colCount = 3;

    init();

    requestMeta.setCompoId(compoId);

    elementConditionDto.setCoCode(WorkEnv.getInstance().getCurrCoCode());

    refreshData();

  }

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {

    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    return createMainFieldEditors(editorList);

  }

  /**
   * 创建主表字段
   * @param editorList
   * @return
   */
  protected List<AbstractFieldEditor> createMainFieldEditors(List<AbstractFieldEditor> editorList) {

    //商品代码、商品名称、状态
    AutoNumFieldEditor zcMerCode = new AutoNumFieldEditor(LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_MER_CODE), "zcMerCode", false);

    editorList.add(zcMerCode);

    TextFieldEditor zcMerName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_MER_NAME), "zcMerName");

    editorList.add(zcMerName);

    AsValFieldEditor zcMerchandiseStatus = new AsValFieldEditor("状态", "zcMerStatus", "ZC_VS_MER_STATUS");

    editorList.add(zcMerchandiseStatus);

    //地域代码、地域名称，商品图片
    String dyColumNames[] = { "地域代码", "地域名称" };

    DySelectedHandler dyHandler = new DySelectedHandler(dyColumNames);

    ForeignEntityFieldEditor dyId = new ForeignEntityFieldEditor("ZC_B_DIYUCTG.selectZcDiYuCtgForeignList", elementDto, 10, dyHandler, dyColumNames,

    LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_DIYU_DAIMA), "zcDiyuDaima");

    //editorList.add(dyId);  

    TextFieldEditor zcDiyuName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_DIYU_NAME), "zcDiyuName");

    //editorList.add(zcDiyuName);

    FileFieldEditor zcMerPic = new FileFieldEditor("商品图片", "zcMerPic", "zcMerPicBlobid");

    editorList.add(zcMerPic);

    //商品批次代码、商品批次名称、年度
    String pcColumNames[] = { "商品批次代码", "商品批次名称", "年度" };

    PcSelectedHandler pcHandler = new PcSelectedHandler(pcColumNames);

    pcId = new ForeignEntityFieldEditor("ZcBdSppc.selectEnableSppc", elementDto, 10, pcHandler, pcColumNames,

    "商品批次代码", "zcProjId");

    editorList.add(pcId);

    TextFieldEditor zcProjName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_SPPC_NA), "zcProjName");

    editorList.add(zcProjName);

    TextFieldEditor zcYear = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_SPPC_ND), "zcYear");

    editorList.add(zcYear);

    //品目代码、品目名称、媒体价格
    String catLogueColumNames[] = { "品目代码", "品目名称" };

    CatalogueSelectedHandler catHandler = new CatalogueSelectedHandler(catLogueColumNames);

    ForeignEntityFieldEditor catId = new ForeignEntityFieldEditor("ZcBCatalogue.getZcBCatalogue", elementDto, 10, catHandler, catLogueColumNames,

    LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_CATALOGUE_CODE), "zcCatalogueCode");

    editorList.add(catId);

    TextFieldEditor zcCatName = new TextFieldEditor(LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_CATALOGUE_NAME), "zcCatalogueName");

    editorList.add(zcCatName);

    MoneyFieldEditor zcMerTax = new MoneyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_MER_TAX), "zcMerTax");

    editorList.add(zcMerTax);

    //品牌代码、品牌名称、市场价格
    String brandColumNames[] = { "品牌代码", "品牌名称" };

    BrandSelectedHandler brandHandler = new BrandSelectedHandler(brandColumNames);

    brandId = new ForeignEntityFieldEditor("ZcBrand.getPinpList", elementDto, 10, brandHandler, brandColumNames,

    "品牌代码", "zcBraCode");

    brandId.setEnabled(false);

    editorList.add(brandId);

    TextFieldEditor zcBraName = new TextFieldEditor(LangTransMeta

    .translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_BRA_NAME), "zcBraName");

    editorList.add(zcBraName);

    MoneyFieldEditor zcMerMPrice = new MoneyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MER_M_PRICE), "zcMerMPrice");

    editorList.add(zcMerMPrice);

    //是否节能节水、是否绿色环保、计量单位
    AsValFieldEditor zcIsJnjs = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_IS_JNJS), "zcIsJnjs", "ZC_VS_YN");

    editorList.add(zcIsJnjs);

    AsValFieldEditor zcMerIsLshb = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MER_IS_LSHB), "zcMerIsLshb",
      "ZC_VS_YN");

    editorList.add(zcMerIsLshb);

    TextFieldEditor zcMerUnit = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_MER_UNIT), "zcMerUnit");

    editorList.add(zcMerUnit);

    //是否自主创新产品、是否市局共享产品
    AsValFieldEditor zcMerIsZzcx = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MER_IS_ZZCX), "zcMerIsZzcx",
      "ZC_VS_YN");

    editorList.add(zcMerIsZzcx);

    AsValFieldEditor zcIsShared = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_IS_SHARED), "zcIsShared",
      "ZC_VS_YN");

    editorList.add(zcIsShared);
    //规格型号
    TextAreaFieldEditor zcMerSpec = new TextAreaFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MER_SPEC), "zcMerSpec", 100, 1,
      5);

    editorList.add(zcMerSpec);
    //备注
    TextAreaFieldEditor zcRemark = new TextAreaFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_REMARK), "zcRemark", 999, 2, 5);

    editorList.add(zcRemark);
    //详细配置
    TextAreaFieldEditor zcMerCollocate = new TextAreaFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FIELD_ZC_MER_COLLOCATE),
      "zcMerCollocate", 999, 2, 5);

    editorList.add(zcMerCollocate);

    return editorList;
  }

  @Override
  public JComponent createSubBillPanel() {
    return createItemPanel();

  }

  private JTabbedPane createItemPanel() {

    itemTablePanel.init();

    itemTablePanel.getSearchBar().setVisible(false);

    itemTablePanel.setTablePreferencesKey(this.getClass().getName() + "_biTable");

    itemTablePanel.getTable().setShowCheckedColumn(true);

    itemTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    bottomToolBar1 = new JFuncToolBar();

    bottomToolBar1.add(addBtn1);

    bottomToolBar1.add(insertBtn1);

    bottomToolBar1.add(delBtn1);

    itemTablePanel.add(bottomToolBar1, BorderLayout.SOUTH);

    addBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcBMerDiscount detail = new ZcBMerDiscount();

        detail.setTempId(Guid.genID());

        setdetailMerDefaultValue(detail, "ADD_TYPE");

        int rowNum = addSub(itemTablePanel, detail);

        itemTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    insertBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcBMerDiscount detail = new ZcBMerDiscount();

        detail.setTempId(Guid.genID());

        setdetailMerDefaultValue(detail, "INSERT_TYPE");

        int rowNum = insertSub(itemTablePanel, detail);

        itemTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    delBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        Integer[] checkedRows = deleteSub(itemTablePanel);

      }

    });

    jTabbedPane.add("商品供货商及价格", itemTablePanel);

    return jTabbedPane;

  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(compoId);

    toolBar.add(addButton);

    toolBar.add(editButton);

    toolBar.add(saveButton);

    toolBar.add(auditButton);

    toolBar.add(unAuditButton);

    toolBar.add(deleteButton);

    toolBar.add(previousButton);

    toolBar.add(nextButton);

    toolBar.add(exitButton);

    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        // 新增
        doAdd();
      }
    });

    editButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doEdit();
      }
    });

    saveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        // 保存
        doSave();
      }
    });

    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        // 删除
        doDelete();
      }
    });

    auditButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        // 送审
        doAudit();
      }
    });

    unAuditButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        doUnAudit();
      }
    });

    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopTableEditing();
        doCancel();
      }
    });

    previousButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 上一页
        doPrevious();
      }
    });

    nextButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 下一页
        doNext();
      }
    });

    exitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 退出
        doExit();
      }
    });
  }

  /**********************************************************************************
   * 以下处理的是按钮调用的方法
   **********************************************************************************/
  /**
   * 刷新list页面数据
   * @param afterSaveBill
   * @param isRefreshButton
   */
  private void refreshAll(ZcBMerchandise afterSaveBill, boolean isRefreshButton) {

    pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

    this.listCursor.setCurrentObject(afterSaveBill);

    refreshData();

  }

  /**

   * 保存前校验

   * @param 

   * @return

   */

  private boolean checkBeforeSave() {

    List mainNotNullList = ((BillElementMeta) this.listPanel.getBillElementMeta()).getNotNullBillElement();

    ZcBMerchandise bill = (ZcBMerchandise) listCursor.getCurrentObject();

    StringBuffer errStr = new StringBuffer();

    StringBuffer subErrStr = new StringBuffer();

    String mainValidateInfo = ZcUtil.validateBillElementNull(bill, mainNotNullList);

    if (mainValidateInfo.length() != 0) {

      errStr.append(LangTransMeta.translate("ZC_B_MERCHANDISE_TITLE")).append("：\n").append(mainValidateInfo.toString()).append("\n");

    }

    if (bill.getItemList() == null || bill.getItemList().size() == 0) {

      errStr.append("商品明细：\n不允许为空!\n");

    }

    int i = 1;
    for (Object o : bill.getItemList()) {

      ZcBMerDiscount item = (ZcBMerDiscount) o;

      String suCode = (String) ObjectUtils.defaultIfNull(item.getZcSuCode(), "");

      String suName = (String) ObjectUtils.defaultIfNull(item.getZcSuName(), "");

      int lowerLimit = item.getZcTreatyLowerLimit();

      int upperLimit = item.getZcTreatyUpperLimit();

      BigDecimal rate = (BigDecimal) ObjectUtils.defaultIfNull(item.getZcTreatyDiscountRate(), new BigDecimal(0));

      if ("".equals(suCode) || "".equals(suName)) {

        subErrStr.append("第" + i + "行    供应信息不能为空！\n");

      }

      if (lowerLimit <= 0) {

        subErrStr.append("第" + i + "行　协议数量下限不能小于等于０！\n");

      }

      if (upperLimit <= 0) {

        subErrStr.append("第" + i + "行　协议数量上限不能小于等于０！\n");

      }

      if (lowerLimit >= upperLimit) {

        subErrStr.append("第" + i + "行　协议数量上限不能小于等于协议数量下限！\n");

      }

      if (rate.compareTo(new BigDecimal(0)) < 0 || rate.compareTo(new BigDecimal(100)) >= 0) {

        subErrStr.append("第" + i + "行　协议商品折扣率不能小于0大于等于100！\n");

      }

      i++;

    }

    if (subErrStr.toString().length() > 0) {

      errStr.append("商品明细：\n");
      errStr.append(subErrStr.toString());

    }
    if (errStr.toString().length() > 0) {

      errStr.append("");

      JOptionPane.showMessageDialog(this, errStr.toString(), "提示", JOptionPane.ERROR_MESSAGE);

      return false;

    }

    return true;

  }

  /**
   * 
   */
  private void stopTableEditing() {
    JPageableFixedTable itemTable = this.itemTablePanel.getTable();
    if (itemTable.isEditing()) {
      itemTable.getCellEditor().stopCellEditing();
    }
  }

  /**
   * 判断数据是否已经被别人改变
   * @return
   */
  public boolean isDataChanged() {
    stopTableEditing();
    return !DigestUtil.digest(oldBean).equals(DigestUtil.digest(listCursor.getCurrentObject()));

  }

  /**
   * 刷新数据
   */
  public void refreshData() {
    ZcBMerchandise afterBill = (ZcBMerchandise) listCursor.getCurrentObject();

    boolean isNew;

    if (afterBill == null) {

      isNew = true;

      afterBill = new ZcBMerchandise();

      afterBill.setZcMerStatus("0");

      afterBill.setZcIsJnjs("N");

      afterBill.setZcIsShared("N");

      afterBill.setZcMerIsLshb("N");

      afterBill.setZcMerIsZzcx("N");

      pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;

      listCursor.setCurrentObject(afterBill);

      listCursor.getDataList().add(afterBill);

    }

    this.setEditingObject(afterBill);

    itemTablePanel.setTableModel(ZcBMerchandiseToTableModelConverter.convertSubBiTableData(afterBill.getItemList()));

    // 翻译从表表头列

    ZcUtil.translateColName(itemTablePanel.getTable(), ZcBMerchandiseToTableModelConverter.getBillDetailInfo());

    // 设置从表列类型

    setTabledetailEditor(itemTablePanel.getTable());

    setOldObject();

    setButtonStatus();

    updateFieldEditorsEditable();

    this.fitTable();
  }

  /**
   * 设置明细
   * @param table
   */
  private void setTabledetailEditor(JPageableFixedTable table) {

    table.setDefaultEditor(String.class, new TextCellEditor());

    String columNames[] = { "供应商代码", "供应商名称" };

    ZcBSupplierHandler handler = new ZcBSupplierHandler(columNames);

    ForeignEntityFieldCellEditor supplier = new ForeignEntityFieldCellEditor(

    "ZcEbSupplier.getEnableSupplierList", elementConditionDto, 20, handler, columNames, "选择供应商", "name");

    SwingUtil.setTableCellEditor(table, "zcSuName", supplier);

    SwingUtil.setTableCellEditor(table, "zcTreatyUpperLimit", new IntCellEditor(false));

    SwingUtil.setTableCellEditor(table, "zcTreatyLowerLimit", new IntCellEditor(false));

    SwingUtil

    .setTableCellEditor(table, "zcTreatyDiscountRate", new MoneyCellEditor(false));

    SwingUtil

    .setTableCellRenderer(table, "zcTreatyDiscountRate", new NumberCellRenderer());
  }

  /**
   * 存储老的bean数据
   */
  protected void setOldObject() {
    oldBean = (ZcBMerchandise) ObjectUtil.deepCopy(listCursor.getCurrentObject());
  }

  /**
   * 设置工具条上按钮的可用性
   */
  protected void setButtonStatus() {
    if (this.btnStatusList.size() == 0) {

      ButtonStatus bs = new ButtonStatus();

      bs = new ButtonStatus();

      bs.setButton(this.editButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus("0");

      bs.addBillStatus("suspended");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.saveButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_NEW);

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.deleteButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus("0");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.auditButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus("0");

      bs.addBillStatus("suspended");

      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.unAuditButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus("exec");

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

    ZcBMerchandise obj = (ZcBMerchandise) this.listCursor.getCurrentObject();

    String billStatus = obj.getZcMerStatus();

    ZcUtil.setButtonEnable(this.btnStatusList, billStatus, this.pageStatus, this.compoId, obj.getProcessInstId());
  }

  /**
   * 设置主表的字段是否可以编辑
   */
  @Override
  protected void updateFieldEditorsEditable() {

    super.updateFieldEditors();

    if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW) || this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {

      for (AbstractFieldEditor fd : this.fieldEditors) {

        if ("zcProjName".equals(fd.getFieldName()) || "zcBraName".equals(fd.getFieldName()) || "zcCatalogueName".equals(fd.getFieldName())
          || "zcYear".equals(fd.getFieldName()) || "zcDiyuName".equals(fd.getFieldName())) {

          fd.setEnabled(false);

        } else {

          fd.setEnabled(true);

        }

      }
      this.itemTablePanel.getTable().setEnabled(true);
      setSubButtonFlag(true);
    } else {
      for (AbstractFieldEditor fd : this.fieldEditors) {
        fd.setEnabled(false);
      }
      this.itemTablePanel.getTable().setEnabled(false);
      setSubButtonFlag(false);
    }
  }

  private void setSubButtonFlag(boolean flag) {

    addBtn1.setEnabled(flag);

    insertBtn1.setEnabled(flag);

    delBtn1.setEnabled(flag);

  }

  /**
   * 
   * @return
   */
  private int getPreviousUpperLimit(String type) {
    //鼠标选择的行
    int selectRow = itemTablePanel.getTable().getSelectedRow();
    int selectCol = itemTablePanel.getTable().getSelectedColumn();
    //总共行数
    int rowCount = itemTablePanel.getTable().getRowCount();

    BeanTableModel<ZcBMerDiscount> model = (BeanTableModel<ZcBMerDiscount>) itemTablePanel.getTable().getModel();

    ZcBMerDiscount upRowObj = model.getBean(selectRow);

    int previousUpperLimit = 0;

    //对于新增的单据，没有子表数据
    if (selectRow < 0 && rowCount == 0) {
      previousUpperLimit = 0;
    } else if (selectRow < 0 && rowCount > 0) { //修改的单据，有子表数据，但是没有选择子表数据
      previousUpperLimit = (Integer) itemTablePanel.getTable().getValueAt(rowCount - 1, selectCol);
    } else { //选择了子表数据，1:插入的，在选择的后面添加 2:增加的，在记录最后面添加
      if (type.endsWith("INSERT_TYPE")) {
        previousUpperLimit = (Integer) itemTablePanel.getTable().getValueAt(selectRow, selectCol);
      } else {
        previousUpperLimit = (Integer) itemTablePanel.getTable().getValueAt(rowCount - 1, selectCol);
      }
    }

    return previousUpperLimit + 1;

  }

  /**
   * 设置子表的默认值
   * 
   * @param detail：
   */
  private void setdetailMerDefaultValue(ZcBMerDiscount detail, String type) {

    ZcBMerchandise bill = (ZcBMerchandise) listCursor.getCurrentObject();

    //    int preUpperLimit = getPreviousUpperLimit(type);

    detail.setZcMerCode(bill.getZcMerCode());

    detail.setZcTreatyLowerLimit(0);

    detail.setZcTreatyUpperLimit(0);

    detail.setZcTreatyDiscountRate(new BigDecimal(0));

  }

  /****************************************************************************************************
   * 以下用来实现增、删、改、启用等主表的按钮功能
   ***************************************************************************************************/

  public void doEdit() {

    this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;

    updateFieldEditorsEditable();

    setButtonStatus();

  }

  /**
   * 新增
   */
  private void doAdd() {
    if (this.doExit()) {

      this.listPanel.doAdd();

    }
  }

  public boolean doSave() {

    if (!isDataChanged()) {

      if (!checkBeforeSave()) {

        return false;

      }

      pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      setButtonStatus();

      return true;

    }

    if (!checkBeforeSave()) {

      return false;

    }

    boolean success = true;

    ZcBMerchandise updBean = new ZcBMerchandise();

    String errorInfo = "";

    try {

      requestMeta.setFuncId(saveButton.getFuncId());

      ZcBMerchandise inData = (ZcBMerchandise) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      updBean = this.listPanel.getZcBMerchandiseServiceDelegate().updateByPrimaryKey(inData, requestMeta);

    } catch (Exception e) {

      logger.error(e.getMessage(), e);

      success = false;

      errorInfo += e.getMessage();

    }

    if (success) {

      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      refreshAll(updBean, true);

      this.listPanel.refreshCurrentTabData();

    } else {

      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

    }

    return true;

  }

  protected void doDelete() {

    int num = JOptionPane.showConfirmDialog(this, "是否删除当前单据", "删除确认", 0);

    if (num == JOptionPane.YES_OPTION) {

      boolean success = true;

      ZcBMerchandise zcBMerchandise = null;

      String errorInfo = "";

      try {

        requestMeta.setFuncId(deleteButton.getFuncId());

        zcBMerchandise = (ZcBMerchandise) this.listCursor.getCurrentObject();

        if (!"0".equals(zcBMerchandise.getZcMerStatus())) {
          JOptionPane.showMessageDialog(this, "非编辑状态单据，不可以删除！", "提示", JOptionPane.ERROR_MESSAGE);
          return;

        }

        this.listPanel.getZcBMerchandiseServiceDelegate().deleteMerchandise(zcBMerchandise, requestMeta);

      } catch (Exception e) {

        logger.error(e.getMessage(), e);

        success = false;

        errorInfo += e.getMessage();

      }

      if (success) {

        JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

        this.listCursor.removeCurrentObject();

        refreshData();

        this.listPanel.refreshCurrentTabData();

      } else {

        JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);

      }

    }
  }

  private void doAudit() {
    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    int num = JOptionPane.showConfirmDialog(this, "确认启用此商品?", "审核", 0);

    if (num == JOptionPane.NO_OPTION) {

      return;

    }

    boolean success = true;

    ZcBMerchandise afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.auditButton.getFuncId());

      ZcBMerchandise zcBMerchandise = (ZcBMerchandise) this.listCursor.getCurrentObject();

      zcBMerchandise.setZcMerStatus("exec");

      afterSaveBill = this.listPanel.getZcBMerchandiseServiceDelegate().updateByPrimaryKey(zcBMerchandise, requestMeta);

    } catch (Exception ex) {

      errorInfo += ex.getMessage();

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "商品启用成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    }
  }

  private void doUnAudit() {
    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    int num = JOptionPane.showConfirmDialog(this, "确认暂停该商品?", "审核", 0);

    if (num == JOptionPane.NO_OPTION) {

      return;

    }

    boolean success = true;

    ZcBMerchandise afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.auditButton.getFuncId());

      ZcBMerchandise zcBMerchandise = (ZcBMerchandise) this.listCursor.getCurrentObject();

      zcBMerchandise.setZcMerStatus("suspended");

      afterSaveBill = this.listPanel.getZcBMerchandiseServiceDelegate().updateByPrimaryKey(zcBMerchandise, requestMeta);

    } catch (Exception ex) {

      errorInfo += ex.getMessage();

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      refreshAll(afterSaveBill, true);

      JOptionPane.showMessageDialog(this, "商品暂停成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    }
  }

  private void doCancel() {
    if (isDataChanged()) {

      JOptionPane.showMessageDialog(this, "数据发生改变，请先保存.", "提示", JOptionPane.INFORMATION_MESSAGE);

      return;

    }

    int num = JOptionPane.showConfirmDialog(this, "确认启用本次商品?", "审核", 0);

    if (num == JOptionPane.NO_OPTION) {

      return;

    }

    boolean success = true;

    ZcBMerchandise afterSaveBill = null;

    String errorInfo = "";

    try {

      requestMeta.setFuncId(this.auditButton.getFuncId());

      ZcBMerchandise zcBMerchandise = (ZcBMerchandise) this.listCursor.getCurrentObject();

      zcBMerchandise.setZcMerStatus("cancel");

      this.listPanel.getZcBMerchandiseServiceDelegate().updateByPrimaryKey(zcBMerchandise, requestMeta);

    } catch (Exception ex) {

      errorInfo += ex.getMessage();

      logger.error(ex.getMessage(), ex);

      success = false;

      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());

    }

    if (success) {

      refreshData();

      JOptionPane.showMessageDialog(this, "商品作废成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      this.listPanel.refreshCurrentTabData();

    }
  }

  public boolean doExit() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave()) {

          return false;

        }

      }

    }

    this.parent.dispose();

    return true;

  }

  private void doPrevious() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave()) {

          return;

        }

      } else {

        listCursor.setCurrentObject(oldBean);

      }

    }

    listCursor.previous();

    refreshData();

  }

  private void doNext() {

    if (isDataChanged()) {

      int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存", "保存确认", 0);

      if (num == JOptionPane.YES_OPTION) {

        if (!doSave()) {

          return;

        }

      } else {

        listCursor.setCurrentObject(oldBean);

      }

    }

    listCursor.next();

    refreshData();

  }

  //===========================================以下为外部部件===================================

  /**
   * 供应商外部部件，明细用到
   * @author Administrator
   *
   */
  private class ZcBSupplierHandler implements IForeignEntityHandler {

    private String columNames[];

    public ZcBSupplierHandler(String columNames[]) {

      this.columNames = columNames;

    }

    public void excute(List selectedDatas) {

      JTable table = itemTablePanel.getTable();

      BeanTableModel model = (BeanTableModel) table.getModel();

      int k = table.getSelectedRow();

      if (k < 0)

        return;

      int k2 = table.convertRowIndexToModel(k);

      ZcBMerDiscount subBean = (ZcBMerDiscount) (model.getBean(k2));

      if (selectedDatas.size() > 0) {

        ZcEbSupplier supplier = (ZcEbSupplier) selectedDatas.get(0);

        subBean.setZcSuCode(supplier.getCode());

        subBean.setZcSuName(supplier.getName());

        ForeignEntityFieldCellEditor se = (ForeignEntityFieldCellEditor) table.getCellEditor(table.getSelectedRow(), table.getSelectedColumn());
        se.getEditor().setValue(supplier.getName());
      }
      stopTableEditing();
      model.fireTableDataChanged();

    }

    @Override
    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        ZcEbSupplier rowData = (ZcEbSupplier) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getCode();

        data[i][col++] = rowData.getName();

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

      return false;

    }
  }

  /**
   * 地域外部部件
   * @author Administrator
   *
   */
  private class DySelectedHandler implements IForeignEntityHandler {

    private final String columNames[];

    public DySelectedHandler(String columNames[]) {

      this.columNames = columNames;

    }

    public void excute(List selectedDatas) {

      ZcBMerchandise bill = (ZcBMerchandise) listCursor.getCurrentObject();

      for (Object object : selectedDatas) {

        ZCDiYuCtg dyBean = (ZCDiYuCtg) object;

        bill.setZcDiyuDaima(dyBean.getDiYuCode());

        bill.setZcDiyuName(dyBean.getDiYuName());
      }

      setEditingObject(bill);
    }

    public TableModel createTableModel(List showDatas) {
      Object data[][] = new Object[showDatas.size()][columNames.length];

      String dyColumNames[] = { "地域代码", "地域名称" };

      for (int i = 0; i < showDatas.size(); i++) {

        ZCDiYuCtg rowData = (ZCDiYuCtg) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getDiYuCode();

        data[i][col++] = rowData.getDiYuName();

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

  /**
   * 商品批次外部部件
   * @author Administrator
   *
   */
  private class PcSelectedHandler implements IForeignEntityHandler {

    private final String columNames[];

    public PcSelectedHandler(String columNames[]) {

      this.columNames = columNames;

    }

    public void excute(List selectedDatas) {

      ZcBMerchandise bill = (ZcBMerchandise) listCursor.getCurrentObject();

      for (Object object : selectedDatas) {

        ZcBdSppc pcBean = (ZcBdSppc) object;

        bill.setZcProjId(pcBean.getZcSppcID());

        bill.setZcProjName(pcBean.getZcSppcNa());

        bill.setZcYear(pcBean.getZcSppcNd());

        bill.setZcBraCode("");

        bill.setZcBraName("");
      }

      elementDto.setZcText1(bill.getZcProjId());

      brandId.setEnabled(true);

      listCursor.setCurrentObject(bill);

      setEditingObject(bill);
    }

    public TableModel createTableModel(List showDatas) {
      Object data[][] = new Object[showDatas.size()][columNames.length];

      String pcColumNames[] = { "商品批次代码", "商品批次名称", "年度" };

      for (int i = 0; i < showDatas.size(); i++) {

        ZcBdSppc rowData = (ZcBdSppc) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getZcSppcID();

        data[i][col++] = rowData.getZcSppcNa();

        data[i][col++] = rowData.getZcSppcNd();
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

  /**
   * 中标品牌外部部件
   * @author Administrator
   *
   */
  private class BrandSelectedHandler implements IForeignEntityHandler {

    private final String columNames[];

    public BrandSelectedHandler(String columNames[]) {

      this.columNames = columNames;

    }

    public void excute(List selectedDatas) {

      ZcBMerchandise bill = (ZcBMerchandise) listCursor.getCurrentObject();

      for (Object object : selectedDatas) {

        ZcBBrand brandBean = (ZcBBrand) object;

        bill.setZcBraCode(brandBean.getZcBraCode());

        bill.setZcBraName(brandBean.getZcBraName());
      }

      setEditingObject(bill);
    }

    public TableModel createTableModel(List showDatas) {

      ZcBMerchandise bill = (ZcBMerchandise) listCursor.getCurrentObject();

      if (null == bill.getZcProjId()) {

        JOptionPane.showMessageDialog(self, "请先选择一个商品批次  ！", "提示", JOptionPane.INFORMATION_MESSAGE);

        return new MyTableModel(null, columNames);

      }

      Object data[][] = new Object[showDatas.size()][columNames.length];

      String sheetColumNames[] = { "品牌代码", "品牌名称" };

      for (int i = 0; i < showDatas.size(); i++) {

        ZcBBrand rowData = (ZcBBrand) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getZcBraCode();

        data[i][col++] = rowData.getZcBraName();

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

  /**
   * 协议供货品目外部部件
   * @author Administrator
   *
   */
  private class CatalogueSelectedHandler implements IForeignEntityHandler {

    private final String columNames[];

    public CatalogueSelectedHandler(String columNames[]) {

      this.columNames = columNames;

    }

    public void excute(List selectedDatas) {

      ZcBMerchandise bill = (ZcBMerchandise) listCursor.getCurrentObject();

      for (Object object : selectedDatas) {

        ZcBCatalogue bean = (ZcBCatalogue) object;

        bill.setZcCatalogueCode(bean.getZcCatalogueCode());

        bill.setZcCatalogueName(bean.getZcCatalogueName());
      }

      setEditingObject(bill);
    }

    public TableModel createTableModel(List showDatas) {
      Object data[][] = new Object[showDatas.size()][columNames.length];

      String sheetColumNames[] = { "品牌代码", "品牌名称" };

      for (int i = 0; i < showDatas.size(); i++) {

        ZcBCatalogue rowData = (ZcBCatalogue) showDatas.get(i);

        int col = 0;

        data[i][col++] = rowData.getZcCatalogueCode();

        data[i][col++] = rowData.getZcCatalogueName();

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