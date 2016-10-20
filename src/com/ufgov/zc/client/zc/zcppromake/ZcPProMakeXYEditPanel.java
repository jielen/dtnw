package com.ufgov.zc.client.zc.zcppromake;

import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_ATTR2;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_CATALOGUE_CODE;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_CO_CODE_ND;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_CO_NAME;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_EB_XY_END_DATE;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_IS_IMP;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_MAKE_CODE;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_MAKE_LINKMAN;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_MAKE_NAME;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_MAKE_STATUS;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_MONEY_BI_SUM;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_PITEM_ARR_DATE;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ZC_ZG_CS_CODE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.apache.commons.lang.ObjectUtils;

import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcPProMakeToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JBasePanel;
import com.ufgov.zc.client.component.JClosableTabbedPane;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JSaveableSplitPane;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.button.SubinsertPinPaiButton;
import com.ufgov.zc.client.component.button.zc.CommonButton;
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
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AutoNumFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.FileFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.OrgFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFilePathFieldEditor;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.commonbiz.model.Company;
import com.ufgov.zc.common.commonbiz.model.WfAware;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.WFConstants;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcPProBalConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.constants.ZcValSetConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.system.util.Utils;
import com.ufgov.zc.common.zc.checkrule.BaseRule;
import com.ufgov.zc.common.zc.checkrule.ZcMakeCheckRuleBySX;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityTreeHandler;
import com.ufgov.zc.common.zc.model.ZcBCatalogue;
import com.ufgov.zc.common.zc.model.ZcBMerchandise;
import com.ufgov.zc.common.zc.model.ZcEbSupplier;
import com.ufgov.zc.common.zc.model.ZcPProMake;
import com.ufgov.zc.common.zc.model.ZcPProMitem;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;
import com.ufgov.zc.common.zc.model.ZcPProMitemService;

public class ZcPProMakeXYEditPanel extends ZcPProMakeEditPanel {

  private static final long serialVersionUID = 12312312L;

  //协议商品市场价

  public BigDecimal zcMerPrice;

  AsValFieldEditor zcFukuanType; //采购类型

  public ZcPProMakeXYEditPanel selfXY = this;

  //  public JTablePanel jingJiaItemTablePanel = new JTablePanel("jingJiaItemTablePanel"); //竞价面板

  public ZcPProMakeXYJJItemEditPanel itemPanel;

  public static String lastActivedTabID = ""; //用来记录增加品牌的TAB_id

  private Map<String, ZcPProMakeXYJJItemEditPanel> tabID_TabPanelMap = new HashMap<String, ZcPProMakeXYJJItemEditPanel>();

  //竞价面板中放置增加、删除、修改按钮

  JFuncToolBar bottomToolBar3 = null;

  JButton insertPinpaiBtn = new SubinsertPinPaiButton();

  List<ZcEbSupplier> suplist;

  ElementConditionDto queryDto;
   
  JTablePanel peijianTablePanel = new JTablePanel();
  
  JTablePanel serviceTablePanel = new JTablePanel();

  private ElementConditionDto peiJianDto=new ElementConditionDto();

  private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

  public String getTitle() {

    return LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_XY_TITEL);

  }

  public static String getCompoId() {

    // 协议供货

    return "ZC_P_PRO_MAKE_XY";

  }

  public ZcPProMakeXYEditPanel(GkBaseDialog parent, ListCursor listCursor, String tabStatus, ZcPProMakeListPanel listPanel) {

    super(ZcPProMake.class, BillElementMeta.getBillElementMetaWithoutNd("ZC_P_PRO_MAKE_XY"));

    this.listCursor = listCursor;

    this.tabStatus = tabStatus;

    this.listPanel = listPanel;

    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), getTitle(), TitledBorder.CENTER, TitledBorder.TOP,

    new Font("宋体", Font.BOLD, 15), Color.BLUE));

    this.parent = parent;

    this.colCount = 3;

    init();
    requestMeta.setCompoId(getCompoId()); 
    refreshData(); 
 

  }

  public void setDeftValue(ZcPProMake zcPProMake) {

    //其他

    zcPProMake.setZcPitemOpiway("6");

    //其他

    zcPProMake.setZcPifuCgfs("6");

    zcPProMake.setZcMakeStatus("0");

    zcPProMake.setZcIsBudget("N");

    zcPProMake.setZcIsImp("N");

    zcPProMake.setAgency(AsOptionMeta.getOptVal(ZcElementConstants.OPT_ZC_CGZX_CODE));
    
    zcPProMake.setAgencyName(AsOptionMeta.getOptVal(ZcElementConstants.OPT_ZC_CGZX_NAME));

    // 政府集中采购

    zcPProMake.setZcMakeSequence("1");

    zcPProMake.setZcFukuanType(ZcPProMake.CAIGOU_TYPE_XIEYI); //新建的时候，采购类型默认为协议供货

    zcPProMake.setZcYepFlag("00");
    zcPProMake.setCoCode(this.requestMeta.getSvCoCode());
    zcPProMake.setZcInputDate(this.requestMeta.getSysDate());
    zcPProMake.setNd(this.requestMeta.getSvNd());
    zcPProMake.setBiList(new ArrayList());
    zcPProMake.setItemList(new ArrayList());      

    initOrg(zcPProMake);

  }

  public List getItemInfo() {

    return ZcPProMakeToTableModelConverter.subItemXyInfo;

  }

  public BaseRule getZcMakeCheckRule() {

    return ZcMakeCheckRuleBySX.getInstance();

  }

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {

    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    AutoNumFieldEditor zcMakeCode = new AutoNumFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_MAKE_CODE), "zcMakeCode");
    TextFieldEditor zcMakeName = new TextFilePathFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_MAKE_NAME), "zcMakeName");
    AsValFieldEditor zcMakeStatus = new AsValFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_MAKE_STATUS), "zcMakeStatus", "ZC_VS_MAKE_STATUS"); 

    IForeignEntityTreeHandler companyHandler = new IForeignEntityTreeHandler() {
      @Override
      public void excute(List selectedDatas) {
        ZcPProMake zcPProMake = (ZcPProMake) listCursor.getCurrentObject();
        if (selectedDatas != null && selectedDatas.size() > 0) {
          Company company = (Company) selectedDatas.get(0);
          zcPProMake.setOrgCode(company.getForgCode());
          setEditingObject(zcPProMake);
        }
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

    CompanyFieldEditor zcCoCode = new CompanyFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_CO_NAME), "coCode", companyHandler, getCompoId(),"CO_CODE"); 

    TextFieldEditor zcCoCodeNd = new TextFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_CO_CODE_ND), "nd");

    OrgFieldEditor zcZgCsCode = new OrgFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_ZG_CS_CODE), "orgCode", false);

    TextFieldEditor zcMakeLinkman = new TextFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_MAKE_LINKMAN), "zcMakeLinkman");

    TextFieldEditor zcMakeTel = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_TEL), "zcMakeTel");

    TextFieldEditor zcAttr2 = new TextFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_ATTR2), "zcAttr2");//采购单位负责人

    zcMoneyBiSum = new MoneyFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_MONEY_BI_SUM), "zcMoneyBiSum"); 

    AsValFieldEditor zcIsImp = new AsValFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_IS_IMP), "zcIsImp", "VS_Y/N");

    FileFieldEditor zcImpFile = new FileFieldEditor("进出口相关附件", "zcImpFile", "zcImpFileBlobid");

    zcFukuanType = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FUKUAN_TYPE), "zcFukuanType","ZC_VS_FUKUAN_TYPE");

    //    editorList.add(zcFukuanType);

    df = new DateFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_EB_XY_END_DATE), "zcXieYiEndDate", DateFieldEditor.TimeTypeH24);

    df.setEnabled(false);
 

    DateFieldEditor zcInputDate = new DateFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_INPUT_DATE), "zcInputDate"); 
    editorList.add(zcMakeName);
    editorList.add(zcMoneyBiSum);
    editorList.add(zcMakeStatus);
    
    editorList.add(zcCoCode);
    editorList.add(zcMakeCode);
    editorList.add(zcZgCsCode);

    editorList.add(zcMakeLinkman);
    editorList.add(zcAttr2);
    editorList.add(zcMakeTel);

//    editorList.add(zcFukuanType);
    editorList.add(df);
    editorList.add(zcInputDate);

    return editorList;

  }

  public void addItemTableLisenter(final JPageableFixedTable table) {

    final BeanTableModel model = (BeanTableModel) table.getModel();
    model.addTableModelListener(new TableModelListener() {
      public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
          if (e.getColumn() >= 0
          && ("ZC_P_PROMAKE_PRICE".equals(model.getColumnIdentifier(e.getColumn()))
          || "ZC_FIELD_ZC_CAIG_NUM".equals(model.getColumnIdentifier(e.getColumn())) || "ZC_MER_PRICE".equals(model.getColumnIdentifier(e.getColumn())))) {
            int k = table.getSelectedRow();
            if (k < 0)
              return;
            ZcPProMitem item = (ZcPProMitem) model.getBean(table.convertRowIndexToModel(k));
            BigDecimal caigNum = item.getZcCaigNum() == null ? BigDecimal.ZERO : item.getZcCaigNum();
            BigDecimal merPrice = item.getZcMerPrice() == null ? new BigDecimal(0) : item.getZcMerPrice();
            if (caigNum != null && merPrice != null) {
              item.setZcItemSum(caigNum.multiply(merPrice));
              model.fireTableRowsUpdated(k, k);
            }
            // 在此判断如果资金构成列表中，预算金额之和为0则，设置采购预算为当前计划明细 总价的 合计 wangwei
            // update start
            ZcPProMake beanData = (ZcPProMake) self.listCursor.getCurrentObject();         
              List<ZcPProMitem> its = beanData.getItemList();
              BigDecimal sumi = BigDecimal.ZERO;
              if (its != null) {
                for (ZcPProMitem zcPProMitem : its) {
                  sumi = sumi.add((BigDecimal) ObjectUtils.defaultIfNull(zcPProMitem.getZcItemSum(), new BigDecimal("0.00")));
                }
              }
              its = beanData.getPeiJianList();
              if (its != null) {
                for (ZcPProMitem zcPProMitem : its) {
                  sumi = sumi.add((BigDecimal) ObjectUtils.defaultIfNull(zcPProMitem.getZcItemSum(), new BigDecimal("0.00")));
                }
              }
              // 设置采购预算金额
              beanData.setZcMoneyBiSum(sumi);
              beanData.getBiList().size();
              self.setEditingObject(beanData);  
            // wangwei updated end
          }
        }
      }
    });
  }
  /*public void refreshJinJiaItemPanel() {

    createSubJingJiaItemPanel();

  }*/

  public void refreshItemPanel() {

    createSubItemPanel();

  }

  JBasePanel createSubItemPanel() {

    //    this.itemTabPane.remove(this.jingJiaItemTablePanel);

    this.itemTabPane.remove(this.itemTablePanel);

    itemTablePanel.init();

    itemTablePanel.setPanelId(this.getClass().getName() + "_itemTablePanel");

    itemTablePanel.getSearchBar().setVisible(false);

    itemTablePanel.setTablePreferencesKey(this.getClass().getName() + "_itemTable");

    itemTablePanel.getTable().setShowCheckedColumn(true);

    itemTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    itemTabPane.addTab("计划明细", itemTablePanel);

    itemBottomToolBar = new JFuncToolBar();

    FuncButton addBtn11 = new SubaddButton(false);

    JButton insertBtn11 = new SubinsertButton(false);

    JButton delBtn11 = new SubdelButton(false);

    itemBottomToolBar.add(addBtn11);

    itemBottomToolBar.add(insertBtn11);

    itemBottomToolBar.add(delBtn11);

    itemTablePanel.add(itemBottomToolBar, BorderLayout.SOUTH);

    addBtn11.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcPProMitem zcPProMitem = new ZcPProMitem();

        zcPProMitem.setTempId(Guid.genID());

        setItemDefaultValue(zcPProMitem);

        addSub(itemTablePanel, zcPProMitem);

      }

    });

    insertBtn11.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcPProMitem zcPProMitem = new ZcPProMitem();

        zcPProMitem.setTempId(Guid.genID());

        setItemDefaultValue(zcPProMitem);

        insertSub(itemTablePanel, zcPProMitem);

      }

    });

    delBtn11.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        deleteSub(itemTablePanel);

      }

    });

    return this.itemTablePanel;

  }
 

  //选择或者新建tab时，记录最新tab ID

  private void addActionListener(final JClosableTabbedPane tabPanel) {

    tabPanel.addChangeListener(new ChangeListener() {

      public void stateChanged(ChangeEvent e) {

        JClosableTabbedPane pane = (JClosableTabbedPane) e.getSource();

        Object tmp = pane.getSelectedComponent();

        if (tmp instanceof ZcPProMakeXYJJItemEditPanel) {

          ZcPProMakeXYJJItemEditPanel tab = (ZcPProMakeXYJJItemEditPanel) pane.getSelectedComponent();

          //JOptionPane.showMessageDialog(null, "kkkkkkkkkk=" + tab.getTabID());

          lastActivedTabID = tab.getTabID();//点击某个tab时，记录当前的tabid

        } else {

          ZcPProMake make = (ZcPProMake) listCursor.getCurrentObject();

          ZcPProMitem mitem = (ZcPProMitem) make.getItemList().get(0);

          lastActivedTabID = mitem.getTempId();//新增tab时，设置此tab为最后激活tab

        }

      }

    });

  }

  //刷新品牌面板

  public void refreshPinpaiPanel(ZcPProMitem item) {

    ZcPProMake make = (ZcPProMake) this.listCursor.getCurrentObject();

    if (make.getItemList().size() != 0) {

      for (int i = 0; i < make.getItemList().size(); i++) {

        ZcPProMitem mitem = (ZcPProMitem) make.getItemList().get(i);

        if (mitem.getZcCatalogueCode() != null && mitem.getZcCatalogueName() != null) {

          item.setZcCatalogueCode(mitem.getZcCatalogueCode());

          item.setZcCatalogueName(mitem.getZcCatalogueName());

          break;

        }

      }

    }

    make.getItemList().add(item);

    refreshJinJiaItemPanel();

  }

  public void assemJinJiaItemPanel(ZcPProMitem item, JClosableTabbedPane itemTabPanel) {

    String braName = "";

    if (item.getZcBraName() != null && !"".equals(item.getZcBraName())) {

      braName = item.getZcBraName();

    } else {

      braName = "品牌";

    }

    itemPanel = new ZcPProMakeXYJJItemEditPanel(item, this.selfXY);

    lastActivedTabID = item.getTempId();

    tabID_TabPanelMap.put(item.getTempId(), itemPanel);

    //this.itemPanelMap.put(item, itemPanel);

    ZcPProMake make = (ZcPProMake) this.listCursor.getCurrentObject();

    itemTabPanel.addTab(braName, itemPanel, null, item.getTempId(), getZcPProMake());//addTab(ZcUtil.substring(merName, 16, "..."), null, itemPanel, merName);

  }

  public ZcPProMake getZcPProMake() {

    return (ZcPProMake) this.listCursor.getCurrentObject();

  }


  public void setTableServiceEditor(JTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());
    SwingUtil.setTableCellEditor(table, ZcPProMitemService.COL_IS_AGREE, new AsValComboBoxCellEditor("VS_Y/N"));
    SwingUtil.setTableCellRenderer(table, ZcPProMitemService.COL_IS_AGREE, new AsValCellRenderer("VS_Y/N"));

    serviceTablePanel.getTable().getColumn(ZcPProMitemService.COL_SERVICE_CONTENT).setMinWidth(1000);
  }
  @Override
  public void setTableItemEditor(JTable table) {

    table.setDefaultEditor(String.class, new TextCellEditor());
    SwingUtil.setTableCellEditor(table, FIELD_TRANS_ZC_PITEM_ARR_DATE, new DateCellEditor());
    SwingUtil.setTableCellRenderer(table, FIELD_TRANS_ZC_PITEM_ARR_DATE, new DateCellRenderer());
    SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_ZC_PITEM_ATTACH, new FileCellEditor("zcPitemAttachBlobid",(BeanTableModel) table.getModel()));
    SwingUtil.setTableCellEditor(table, "ZC_FIELD_ZC_ITEM_SUM", new MoneyCellEditor(false));
    SwingUtil.setTableCellRenderer(table, "ZC_FIELD_ZC_ITEM_SUM", new NumberCellRenderer()); 
    SwingUtil.setTableCellEditor(table, "ZC_MER_PRICE", new MoneyCellEditor(false));
    SwingUtil.setTableCellRenderer(table, "ZC_MER_PRICE", new NumberCellRenderer());
    SwingUtil.setTableCellEditor(table, "ZC_FIELD_ZC_CAIG_NUM", new MoneyCellEditor(false));
    String[] brandColumNames = { "品牌编号", "品牌名称", "商品数量" };
    ZcEbBrandHandler brandHandler = new ZcEbBrandHandler(brandColumNames);
    this.brandDto = new ElementConditionDto();
    String[] merColumNames = { "品牌","商品","型号","协议价格(元)","规格" };
    ZcEbMerHandler merHandler = new ZcEbMerHandler(merColumNames);
    Dimension dialogSize=new Dimension(1000, 500);
    merDto.setZcText1("zhujian");
    ForeignEntityFieldCellEditor merEditor = new ForeignEntityFieldCellEditor("ZcBMerchandise.selectMerByCatalogue", this.merDto, 20, merHandler,merColumNames, "商品", "zcMerName",dialogSize);
    SwingUtil.setTableCellEditor(table, "ZC_MER_NAME", merEditor);
    ZcCataLogueHandler catalogueHandler = new ZcCataLogueHandler() {
      @Override
      public void excute(List selectedDatas) {
        JTable table = itemTablePanel.getTable();
        BeanTableModel model = (BeanTableModel) table.getModel();
        int k = table.getSelectedRow();
        if (k < 0)
          return;
        int k2 = table.convertRowIndexToModel(k);
        ZcPProMitem item = (ZcPProMitem) (model.getBean(k2));
        if (selectedDatas.size() > 0) {
          ZcBCatalogue su = (ZcBCatalogue) selectedDatas.get(0);
          item.setZcCatalogueCode(su.getZcCatalogueCode());
          item.setZcCatalogueName(su.getZcCatalogueName());
          merDto.setZcText0(su.getZcCatalogueCode()); 
          item.setZcMerCode(null);
          item.setZcMerName(null);
          item.setZcMerPrice(null);
          item.setZcBraCode(null);
          item.setZcBraName(null);
          item.setZcBaseGgyq(null);
          item.setZcCaigNum(null);
          item.setZcItemSum(null);   
          clearPeijian();
          model.fireTableDataChanged();
        }
      }

      public void afterClear() {
        JTable table = itemTablePanel.getTable();
        BeanTableModel model = (BeanTableModel) table.getModel();
        int k = table.getSelectedRow();
        if (k < 0)
          return;
        int k2 = table.convertRowIndexToModel(k);
        ZcPProMitem item = (ZcPProMitem) (model.getBean(k2));
        item.setZcCatalogueCode(null);
        item.setZcCatalogueName(null);
        item.setZcMerCode(null);
        item.setZcMerName(null);
        item.setZcMerPrice(null);
        item.setZcBraCode(null);
        item.setZcBraName(null);
        item.setZcBaseGgyq(null);
        item.setZcCaigNum(null);
        item.setZcItemSum(null); 
        merDto.setZcText0(null); 
        clearPeijian();
        model.fireTableDataChanged();
      }
    };
    ElementConditionDto cataDto=new ElementConditionDto();
//    SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_ZC_CATALOGUE_CODE, new ZcBCatalogueCellEditor(false, true));
    ForeignEntityFieldCellEditor catalogueEditor = new ForeignEntityFieldCellEditor("ZcBCatalogue.getXyCatalogue", cataDto, 20, catalogueHandler,catalogueHandler.columNames, "品目", "zcCatalogueName");
    SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_ZC_CATALOGUE_NAME, catalogueEditor);
  }

  protected void clearPeijian() {
    ZcPProMake make=(ZcPProMake) listCursor.getCurrentObject();
    make.getPeiJianList().clear();
    peiJianDto.getCodeList().clear();
    refreshSubPeijianTable();
    setTablePeiJianEditor(peijianTablePanel.getTable());
    addItemTableLisenter(peijianTablePanel.getTable()); 
  }
  

  private void refreshSubPeijianTable() {
    ZcPProMakeToTableModelConverter conveter = new ZcPProMakeToTableModelConverter();
    ZcPProMake make=(ZcPProMake) listCursor.getCurrentObject();
    peijianTablePanel.setTableModel(conveter.convertXyItem(make.getPeiJianList(), conveter.peiJianInfo, wfCanEditFieldMap));
    ZcUtil.translateColName(peijianTablePanel.getTable(), conveter.peiJianInfo);
  }

  public void setTablePeiJianEditor(JTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());
    SwingUtil.setTableCellEditor(table, FIELD_TRANS_ZC_PITEM_ARR_DATE, new DateCellEditor());
    SwingUtil.setTableCellRenderer(table, FIELD_TRANS_ZC_PITEM_ARR_DATE, new DateCellRenderer());
    SwingUtil.setTableCellEditor(table, FIELD_TRANS_ZC_CATALOGUE_CODE, new ZcBCatalogueCellEditor(false, false));
    SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_ZC_PITEM_ATTACH, new FileCellEditor("zcPitemAttachBlobid", (BeanTableModel) table.getModel()));

    MoneyCellEditor mce = new MoneyCellEditor(false);
    mce.getField().setEnabled(false);
    SwingUtil.setTableCellEditor(table, "ZC_FIELD_ZC_ITEM_SUM", mce);
    SwingUtil.setTableCellRenderer(table, "ZC_FIELD_ZC_ITEM_SUM", new NumberCellRenderer());
    SwingUtil.setTableCellEditor(table, "BUDGET_BI_MONEY", new MoneyCellEditor(false));
    SwingUtil.setTableCellRenderer(table, "BUDGET_BI_MONEY", new NumberCellRenderer());
    SwingUtil.setTableCellEditor(table, "BUDGET_OTHER_MONEY", new MoneyCellEditor(false));
    SwingUtil.setTableCellRenderer(table, "BUDGET_OTHER_MONEY", new NumberCellRenderer());
    SwingUtil.setTableCellEditor(table, "ZC_MER_PRICE", new MoneyCellEditor(false));
    SwingUtil.setTableCellRenderer(table, "ZC_MER_PRICE", new NumberCellRenderer());
    SwingUtil.setTableCellEditor(table, "ZC_FIELD_ZC_CAIG_NUM", new MoneyCellEditor(false)); 
    String[] merColumNames = { "商品", "品牌", "规格", "价格" };
    ZcPeiJianHandler peijianHandler = new ZcPeiJianHandler(merColumNames); 
    Dimension dialogSize=new Dimension(1000, 500);
    peiJianDto.setZcText1("peijian");
    ForeignEntityFieldCellEditor merEditor = new ForeignEntityFieldCellEditor("ZcBMerchandise.selectMerByCatalogue", peiJianDto, 20, peijianHandler,merColumNames, "配件", "zcMerName",dialogSize);
    SwingUtil.setTableCellEditor(table, "ZC_MER_NAME", merEditor); 
  }
  /*

   * 商品选择外部部件处理类

   */

  public class ZcEbMerHandler implements IForeignEntityHandler {

    public String columNames[];

    public ZcEbMerHandler(String columNames[]) {

      this.columNames = columNames;

    }

    public void excute(List selectedDatas) {
      JTable table = itemTablePanel.getTable();
      BeanTableModel model = (BeanTableModel) table.getModel();
      int k = table.getSelectedRow();
      if (k < 0)
        return;

      int k2 = table.convertRowIndexToModel(k);
      ZcPProMitem item = (ZcPProMitem) (model.getBean(k2));
      DecimalFormat df = new DecimalFormat("0.00");
      if (selectedDatas.size() > 0) {
        ZcBMerchandise mer = (ZcBMerchandise) selectedDatas.get(0);
        item.setZcMerCode(mer.getZcMerCode());
        item.setZcMerName(mer.getZcMerName());
        item.setZcPitemName(mer.getZcMerName());
        item.setZcMerPrice(mer.getZcMerMPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        item.setZcBaseGgyq(mer.getZcMerSpec() == null ? mer.getZcMerCollocate() : mer.getZcMerSpec());
        //        item.setZcBaseGgyq(mer.getZcMerSpec());
        item.setZcBraCode(mer.getZcBraCode());
        item.setZcBraName(mer.getZcBraName());
        item.setZcCaigUnit(mer.getZcMerUnit());
        if(item.getZcCatalogueCode()==null){
          item.setZcCatalogueCode(mer.getZcCatalogueCode());
          item.setZcCatalogueName(mer.getZcCatalogueName());
        }
        peiJianDto.setZcText1(mer.getZcMerCode());
        //清空已有的供应商
        item.setZcSuCode(null);
        item.setZcSuName(null);
        item.setZcCaigNum(null);
        item.setZcItemSum(null);
        clearPeijian();
        ForeignEntityFieldCellEditor se = (ForeignEntityFieldCellEditor) table.getCellEditor(table.getSelectedRow(), table.getSelectedColumn());
        se.getEditor().setValue(mer.getZcMerName());
      }
      model.fireTableDataChanged();

    }

    @Override
    public TableModel createTableModel(List showDatas) {
      Object data[][] = new Object[showDatas.size()][columNames.length];
      for (int i = 0; i < showDatas.size(); i++) {
        ZcBMerchandise rowData = (ZcBMerchandise) showDatas.get(i);
        int col = 0;
        data[i][col++] = rowData.getZcBraName();
        data[i][col++] = rowData.getZcMerName();
        data[i][col++] = rowData.getZcMerSpec();
        data[i][col++] = rowData.getZcMerMPrice();
        data[i][col++] = rowData.getZcMerCollocate();
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

    public void afterClear() {
      JTable table = itemTablePanel.getTable();
      BeanTableModel model = (BeanTableModel) table.getModel();
      int k = table.getSelectedRow();
      if (k < 0)
        return;
      int k2 = table.convertRowIndexToModel(k);
      ZcPProMitem item = (ZcPProMitem) (model.getBean(k2));
      item.setZcMerCode(null);
      item.setZcMerName(null);
      item.setZcMerPrice(null);
      item.setZcBraCode(null);
      item.setZcBraName(null);
      item.setZcBaseGgyq(null);
      item.setZcCaigNum(null);
      item.setZcItemSum(null); 
      clearPeijian();
      model.fireTableDataChanged();
    }
    public boolean beforeSelect(ElementConditionDto dto) {

      /* JTable table = itemTablePanel.getTable();

       BeanTableModel model = (BeanTableModel) table.getModel();

       int k = table.getSelectedRow();

       if (k < 0)

         return false;

       int k2 = table.convertRowIndexToModel(k);

       ZcPProMitem item = (ZcPProMitem) (model.getBean(k2));

       if (item.getZcBraCode() == null) {

         JOptionPane.showMessageDialog(self, "请先选择品牌", "提示", JOptionPane.INFORMATION_MESSAGE);

         return false;

       }

        应夏处要求，先将商品品目/品牌/商品/规格等输入次序功能进行暂时屏蔽，改为可用手工填写

        * Update By FengYan 2011-06-14

       if (item.getZcBraCode() == null) {

         JOptionPane.showMessageDialog(self, "请先选择品牌", "提示", JOptionPane.INFORMATION_MESSAGE);

         return false;

       }

       /* else {

         dto.setZcText0(ZcBMerchandise.ENABLE);

         dto.setZcText1(item.getZcCatalogueCode());

         if (item.getZcBraCode().indexOf(",") == -1)

           dto.setZcText2(item.getZcBraCode());

         else {

           String[] brands = item.getZcBraCode().split(",");

           List brandList = null;

           for (int i = 0; i < brands.length; i++) {



           }

         }

         dto.setZcText4(item.getZcBraCode());

         return true;

       }

       

       dto.setZcText0(ZcBMerchandise.ENABLE);

       dto.setZcText1(item.getZcCatalogueCode());

       if (item.getZcBraCode().indexOf(",") == -1)

         dto.setZcText2(item.getZcBraCode());

       else {

         String[] brands = item.getZcBraCode().split(",");

         List brandList = null;

         for (int i = 0; i < brands.length; i++) {

         }

       }

       dto.setZcText4(item.getZcBraCode());*/

      return true;

    }

  }

  /*

   * 供应商选择外部部件处理类

   */

  public class ZcEbSupplierHandler implements IForeignEntityHandler {

    public String columNames[];

    public ZcEbSupplierHandler(String columNames[]) {

      this.columNames = columNames;

    }

    // 添加清空操作
    public void afterClear() {

      JTable table = itemTablePanel.getTable();

      BeanTableModel model = (BeanTableModel) table.getModel();

      int k = table.getSelectedRow();

      if (k < 0)

        return;

      int k2 = table.convertRowIndexToModel(k);

      ZcPProMitem item = (ZcPProMitem) (model.getBean(k2));

      item.setZcSuCode("");

      item.setZcSuName("");

      item.setZcMerPrice(BigDecimal.ZERO);

      item.setZcItemSum(BigDecimal.ZERO);

      ForeignEntityFieldCellEditor se = (ForeignEntityFieldCellEditor) table.getCellEditor(table.getSelectedRow(), table.getSelectedColumn());
      se.getEditor().setValue("");

      model.fireTableDataChanged();

    }

    public void excute(List selectedDatas) {

      JTable table = itemTablePanel.getTable();

      BeanTableModel model = (BeanTableModel) table.getModel();

      int k = table.getSelectedRow();

      if (k < 0)

        return;

      int k2 = table.convertRowIndexToModel(k);

      ZcPProMitem item = (ZcPProMitem) (model.getBean(k2));

      zcMerPrice = item.getZcMerPrice();

      if (selectedDatas.size() > 0) {

        ZcEbSupplier su = (ZcEbSupplier) selectedDatas.get(0);

        item.setZcSuCode(su.getCode());

        item.setZcSuName(su.getName());

        BigDecimal xyPrice = new BigDecimal(su.getDisPrice().doubleValue());

        xyPrice.setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal cgNum = item.getZcCaigNum();

        //        BigDecimal xyPrice = disRate.multiply(zcMerPrice);

        BigDecimal xyPriceSum = xyPrice.multiply(cgNum);

        item.setZcMerPrice(xyPrice);

        item.setZcItemSum(xyPriceSum);

        ForeignEntityFieldCellEditor se = (ForeignEntityFieldCellEditor) table.getCellEditor(table.getSelectedRow(), table.getSelectedColumn());
        se.getEditor().setValue(su.getName());

        model.fireTableDataChanged();

      }

    }

    @Override
    public TableModel createTableModel(List showDatas) {

      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        ZcEbSupplier rowData = (ZcEbSupplier) showDatas.get(i);

        int col = 0;

        DecimalFormat df = new DecimalFormat("0.00");

        data[i][col++] = rowData.getName();

        data[i][col++] = (100 - rowData.getZcMerXyDisRate()) + "%";

        data[i][col++] = df.format(rowData.getDisPrice().doubleValue());

        data[i][col++] = rowData.getLinkMan();

        data[i][col++] = rowData.getLinkManPhone();

        data[i][col++] = rowData.getAddress();

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

    public boolean beforeSelect(ElementConditionDto dto) {

      JTable table = itemTablePanel.getTable();

      BeanTableModel model = (BeanTableModel) table.getModel();

      int k = table.getSelectedRow();

      if (k < 0)

        return false;

      int k2 = table.convertRowIndexToModel(k);

      ZcPProMitem item = (ZcPProMitem) (model.getBean(k2));

      if (item.getZcMerCode() == null) {

        JOptionPane.showMessageDialog(self, "请先选择商品", "提示", JOptionPane.INFORMATION_MESSAGE);

        return false;

      }

      if (item.getZcCaigNum() == null || item.getZcCaigNum().compareTo(BigDecimal.ZERO) <= 0) {

        JOptionPane.showMessageDialog(self, "请先填写采购数量", "提示", JOptionPane.INFORMATION_MESSAGE);

        return false;

      }

      else {

        dto.setZcText0(ZcEbSupplier.ENABLE);

        dto.setZcText1(item.getZcMerCode());

        dto.setZcText2(item.getZcBraCode());

        dto.setZcText3(item.getZcCaigNum().toString());

        dto.setZcText4(item.getZcSuCode());

        queryDto.setZcText0(ZcEbSupplier.ENABLE);

        queryDto.setZcText1(item.getZcMerCode());

        queryDto.setZcText2(item.getZcBraCode());

        queryDto.setZcText3(item.getZcCaigNum().toString());

        queryDto.setZcText4(item.getZcSuCode());

        return true;

      }

    }

  }

  public boolean isMultipleSelect() {

    return false;

  }

  /*   wangkewei  分两种情况，采购类型分别为协议供货G01和电子竞价G03 */

  @Override
  protected void refreshData() {

    ZcPProMake zcPProMake = (ZcPProMake) listCursor.getCurrentObject();

    this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

    if (zcPProMake != null) {
      zcPProMake = getZcPProMakeServiceDelegate().selectByPrimaryKey(zcPProMake.getZcMakeCode(), this.requestMeta);
      List biList = getZcPProMakeServiceDelegate().getZcPProMitemBi(zcPProMake.getZcMakeCode(), ZcUtil.useBudget(), requestMeta);      
      zcPProMake.setBiList(biList);
      listCursor.setCurrentObject(zcPProMake);
      if (biList != null && biList.size() > 0) {
        if (ZcUtil.useBudget()) {
          String sumId = "";
          for (int i = 0; i < biList.size(); i++) {
            ZcPProMitemBi bi = (ZcPProMitemBi) biList.get(i);
            if ("A".equals(bi.getPaytypeCode())) {
              bi.setZcBiPayType("2");
              bi.setZcBiDoSum(null);
              continue;
            }
            bi.setZcBiPayType("1");
            if (sumId.length() > 0) {
              sumId = sumId + ",'" + bi.getZcBiNo() + "'";
            } else {
              sumId = "'" + bi.getZcBiNo() + "'";
            }
          }
          getBiDto.setZcText3(sumId);
        }
      }
      if (ZcUtil.useBudget()) {
        getBiDto.setZcMakeCode(zcPProMake.getZcMakeCode());
      }
 
    } else {
      this.pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;
      zcPProMake = new ZcPProMake();
      setDeftValue(zcPProMake);
       //新增数据默认插入一行
       ZcPProMitem item = new ZcPProMitem();
       item.setTempId(Guid.genID());
       setItemDefaultValue(item);
       zcPProMake.getItemList().add(item);
      ZcPProMitemBi bi = new ZcPProMitemBi();
      setItemBiDefaultValue(bi);
      zcPProMake.getBiList().add(bi);
      listCursor.getDataList().add(zcPProMake);
      listCursor.setCurrentObject(zcPProMake); 
    }
    if (ZcUtil.useBudget()) {
      getBiDto.setNd(requestMeta.getSvNd());
      getBiDto.setZcText2("1");
      getBiDto.setCoCode(zcPProMake.getCoCode() != null ? zcPProMake.getCoCode() : requestMeta.getSvCoCode());
    }

    this.setEditingObject(zcPProMake);    

    if (zcPProMake.getProcessInstId() != null && zcPProMake.getProcessInstId().longValue() > 0) {
      // 工作流的单据
      wfCanEditFieldMap = BillElementMeta.getWfCanEditField(zcPProMake, requestMeta);
    }
    ZcPProMakeToTableModelConverter conveter = new ZcPProMakeToTableModelConverter();

    itemTablePanel.setTableModel(conveter.convertXyItem(zcPProMake.getItemList(), conveter.newXyItemInfo, wfCanEditFieldMap));    

    peijianTablePanel.setTableModel(conveter.convertXyPeiJianItem(zcPProMake.getPeiJianList(), conveter.peiJianInfo, wfCanEditFieldMap));

    biTablePanel.setTableModel(ZcPProMakeToTableModelConverter.convertSubBiTableData(zcPProMake.getBiList(), wfCanEditFieldMap));
    
    serviceTablePanel.setTableModel(conveter.convertXyServiceItem(zcPProMake.getServiceList(), conveter.serviceInfo, wfCanEditFieldMap));

    //刷新竞价信息和成交信息

    refreshJinJiaChengJiao();

    ZcUtil.translateColName(itemTablePanel.getTable(), conveter.newXyItemInfo);
    ZcUtil.translateColName(peijianTablePanel.getTable(), conveter.peiJianInfo);
    ZcUtil.translateColName(serviceTablePanel.getTable(), conveter.serviceInfo);

    setTableItemEditor(itemTablePanel.getTable()); // 设置从表列类型
    addItemTableLisenter(itemTablePanel.getTable()); // 设置从表监听 
    
    setTablePeiJianEditor(peijianTablePanel.getTable());
    addItemTableLisenter(peijianTablePanel.getTable());

    ZcUtil.translateColName(biTablePanel.getTable(), ZcPProMakeToTableModelConverter.biInfoWihtBudget);

    // 设置从表列类型
    setTableBiEditor(biTablePanel.getTable());
    // 设置从表监听 
    addBiTableLisenter(biTablePanel.getTable());
    
    setTableServiceEditor(serviceTablePanel.getTable());
     
    setOldObject();
    // 根据工作流模版设置功能按钮是否可用
    setButtonStatus();
    // 根据工作流模版设置字段是否可编辑
    updateFieldEditorsEditable();
    this.fitTable();

    //不是供应商，且不是最后的成交场状态时
    if(!ZcUtil.isGys() && !ZcPProMake.ZC_STATUS_JIN_JIA_SUCSESS.equals(zcPProMake.getZcMakeStatus())){
      JTable table=serviceTablePanel.getTable();
      TableColumn tc = table.getColumn(ZcPProMitemService.COL_IS_AGREE);
      table.getColumnModel().removeColumn(tc);
    }

    //退回状态时，明细都可以编辑 add shijia 2011-10-14

 
    /*  if (ZcPProMake.CAIGOU_TYPE_DZJJ.equals(zcPProMake.getZcFukuanType())) {

        //创建竞价商品面板

        refreshJinJiaItemPanel();

        //刷新竞价信息和成交信息

        refreshJinJiaChengJiao();

        //设置旧值。判断页面值是否改变

        setOldObject();

      }  */
     
  }
  public void initToolBar(JFuncToolBar toolBar) {

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(getCompoId());

//    toolBar.add(addButton);
    
  toolBar.add(editButton);

    toolBar.add(saveButton);

    toolBar.add(deleteButton);

//    toolBar.add(chengJiaoButton);

//    toolBar.add(jingJiaGongGaoButton);

//    toolBar.add(chengJiaoGongGaoButton);

    toolBar.add(sendButton);

    toolBar.add(suggestPassButton);

//    toolBar.add(suggestPassCZButton);

    toolBar.add(auditPassButton);

    toolBar.add(callbackButton);

    toolBar.add(unAuditButton);

    toolBar.add(unTreadButton);

    toolBar.add(cancelButton);// 撤销

    toolBar.add(traceButton);

    toolBar.add(previousButton);

    toolBar.add(nextButton);

//    toolBar.add(printButton);

//    toolBar.add(confirmSupplyButton);// 确认供货

//    toolBar.add(printPreviewButton);

//    toolBar.add(printSettingButton);

    toolBar.add(exitButton);
    cancelButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        stopTableEditing(); 
        doCancel();
      }
    });
    editButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        stopTableEditing(); 
        doEdit();
      }
    });
    addButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        stopTableEditing();

        // 新增

        doAdd();

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

    chengJiaoButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        stopTableEditing();

        // 成交

        doChengJiao();

      }

    });

    sendButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        stopTableEditing();

        // 送审

        doSend();

      }

    });

    callbackButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        stopTableEditing();

        // 收回

        doCallback();

      }

    });

    suggestPassButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        stopTableEditing();

        // 填写意见审核

        doSuggestPass();

      }

    });

    suggestPassCZButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        stopTableEditing();

        doSuggestCZPass();

      }

    });

    auditPassButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        stopTableEditing();

        // 审核

        doAudit();

      }

    });

    unAuditButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        stopTableEditing();

        // 销审

        doUnaudit();

      }

    });

    unTreadButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        stopTableEditing();

        // 退回

        doUntread();

      }

    });

    traceButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 流程跟踪

        doTrace();

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

    cancelButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doCancel();

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

        doPrintPreview();

      }

    });

    printSettingButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doPrintSetting();

      }

    });

    jingJiaGongGaoButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doJingjiaGongGao();

      }

    });

    chengJiaoGongGaoButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doChengjiaoGongGao();

      }

    });

    confirmSupplyButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doConfirmSupply();

      }

    });

  }
  protected void doEdit() {
    this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;

    updateFieldEditorsEditable();

    setButtonStatus();
  }

  protected void setButtonStatus() {
    ZcPProMake qb = (ZcPProMake) listCursor.getCurrentObject();
    if (WFConstants.AUDIT_TAB_STATUS_CANCEL.equals(qb.getZcMakeStatus())) {
      setCancelStatus(listCursor);
    } else {
      setButtonStatus(qb, requestMeta, this.listCursor);
    }
  }

  protected void setButtonStatus(WfAware baseBill, RequestMeta requestMeta,  ListCursor listCursor) {

    Long processInstId = baseBill.getProcessInstId();

    if (processInstId == null || processInstId.longValue() < 0) {

      // 新增单据,草稿单据或不挂接工作流的单据

      Component[] funcs = toolBar.getComponents();

      String funcId;

      for (Component func : funcs) {

        funcId = ((FuncButton) func).getFuncId();

        if ("fauditfinal" == funcId || "fcallback" == funcId

        || "fautocommit" == funcId || "funaudit" == funcId

        || "funtread" == funcId

        || "fshowinstancetrace" == funcId

        || "f_uncollectcreate" == funcId 
//        || "fnew" == funcId

        || "fconfirmsup" == funcId || "fmanualcommit" == funcId

        || "fsendnextcommit" == funcId

        || funcId != null && funcId.startsWith("fmanualcommit")) {

          func.setVisible(false);

        }

      }
      setButtonStatusWithoutWf();

    } else {

      // 流程已经启动

      List enableFuncs = this.getWFNodeEnableFunc(baseBill, requestMeta);

      ZcUtil.setWfNodeEnableFunc(toolBar, enableFuncs, processInstId,requestMeta);
     setJiaoYiBtnStatus();
    }
  }
  //设置当前的成交与作废按钮
  private void setJiaoYiBtnStatus() {
    String funcId="";
    ZcPProMake bill=(ZcPProMake) listCursor.getCurrentObject();
    for (Component button : toolBar.getComponents()) {
      funcId = ((FuncButton) button).getFuncId();
      if("fchengjiao".equals(funcId)){
        if(isJinJiaEndTime(1) && ZcPProMake.ZC_STATUS_JIN_JIA_ING.equals(bill.getZcMakeStatus())){
          button.setVisible(true);
          cancelButton.setVisible(true);
        }else{
          button.setVisible(false);
          cancelButton.setVisible(false);
        }
      }
    }
  }
//判断当前单据是否正在竞价
 /* public boolean isJinJiaEndTime() {
    ZcPProMake bill=(ZcPProMake) listCursor.getCurrentObject();
    //正在竞价，并且已经在竞价时间接受时间之后了
    if((ZcPProMake.ZC_STATUS_JIN_JIA_ING.equals(bill.getZcMakeStatus())||ZcPProMake.ZC_STATUS_JIN_JIA_SUCSESS.equals(bill.getZcMakeStatus()))&& !isJinJiaEndTime(1)){
      return true;
    }
    return false;
  }*/

  public void doCancel() {
    boolean success = true;
    requestMeta.setFuncId(this.cancelButton.getFuncId());
    ZcPProMake make = (ZcPProMake) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
    int num = JOptionPane.showConfirmDialog(this, "确实要作废本次协议采购吗？");
    if (num == JOptionPane.YES_OPTION) {
      try {
        make.setZcMakeStatus(ZcPProMake.ZC_STATUS_CANCEL);
        setChengJiaostatus(null, false);
        this.getZcPProMakeServiceDelegate().CancelMakeFN(make, requestMeta);
      } catch (Exception ex) {
        logger.error(ex.getMessage(), ex);
        success = false;
        UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
      }

      if (success) {
        this.refreshAll(make, true);
        JOptionPane.showMessageDialog(this, "作废成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        this.listPanel.refreshCurrentTabData();
      }
    }
  }
   
  public void setButtonStatusWithoutWf() {

    if (this.btnStatusList.size() == 0) {

      ButtonStatus bs = new ButtonStatus();

      bs.setButton(this.addButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();
      
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
      bs.setButton(this.chengJiaoButton);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
      bs.addPageStatus(ZcPProMake.ZC_STATUS_JIN_JIA_ING);
      btnStatusList.add(bs);
      
      bs = new ButtonStatus();
      bs.setButton(this.cancelButton);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
      bs.addPageStatus(ZcPProMake.ZC_STATUS_JIN_JIA_ING);
      btnStatusList.add(bs);

      bs = new ButtonStatus();

      bs.setButton(this.exitButton);

      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_ALL);

      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

      btnStatusList.add(bs);

      bs = new ButtonStatus();
      bs.setButton(this.sendButton);
      bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
      bs.addBillStatus(ZcSettingConstants.BILL_STATUS_DRAFT);
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

    }
    ZcPProMake qb = (ZcPProMake) this.listCursor.getCurrentObject();
    String billStatus = qb.getZcMakeStatus();
    ZcUtil.setButtonEnable(btnStatusList, billStatus, this.pageStatus, getCompoId(), qb.getProcessInstId());
  }

  protected void updateFieldEditorsEditable() {

    ZcPProMake qb = (ZcPProMake) listCursor.getCurrentObject();
    Long processInstId = qb.getProcessInstId();
    if (processInstId != null && processInstId.longValue() > 0) {
      // 工作流的单据
      wfCanEditFieldMap = BillElementMeta.getWfCanEditField(qb, requestMeta);
      if ("cancel".equals(this.oldZcPProMake.getZcMakeStatus())) {// 撤销单据设置字段为不可编辑
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
          if ("zcMakeName".equals(editor.getFieldName()) || "zcMakeLinkman".equals(editor.getFieldName()) || "zcAttr2".equals(editor.getFieldName())
            || "zcMakeTel".equals(editor.getFieldName()) || "zcXieYiEndDate".equals(editor.getFieldName())){
            editor.setEnabled(true);
          } else {
            editor.setEnabled(false);
          }
          isEdit = true;
        } else {
          editor.setEnabled(false);
        }
      }
    }
    setWFSubTableEditable(biTablePanel, isEdit);
    setWFSubTableEditable(itemTablePanel, isEdit);
    setWFSubTableEditable(peijianTablePanel, isEdit);
    setWFSubTableEditable(serviceTablePanel, isEdit);
  }
  @Override
  public JTablePanel[] getSubTables() {

    ZcPProMake zcPPro = (ZcPProMake) this.listCursor.getCurrentObject();

    if (ZcPProMake.CAIGOU_TYPE_XIEYI.equals(zcPPro.getZcFukuanType())) {

      return new JTablePanel[] { biTablePanel, itemTablePanel };

    } else {

      //      return new JTablePanel[] { biTablePanel, jingJiaItemTablePanel, itemTablePanel };

      return new JTablePanel[] { biTablePanel, itemTablePanel };

    }

  }
 
  public JComponent createSubBillPanel() {

    biTabPane = new JTabbedPane();

    biTablePanel = new JTablePanel(null, AsOptionMeta.getOptVal(ZcSettingConstants.ZC_OPTON_JIHUA_ZIJIN_HELP_MSG));

    biTablePanel.init();

    biTablePanel.getSearchBar().setVisible(false);

    biTablePanel.setTablePreferencesKey(this.getClass().getName() + "_biTable");

    biTablePanel.getTable().setShowCheckedColumn(true);

    biTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    biTabPane.addTab("资金构成", biTablePanel);

    biBottomToolBar = new JFuncToolBar();

    FuncButton addBtn1 = new SubaddButton(false);

    JButton insertBtn1 = new SubinsertButton(false);

    JButton delBtn1 = new SubdelButton(false);

    FuncButton addPtzjBtn = new CommonButton(null, "待安排指标", "sendBill.png");

    biBottomToolBar.add(addBtn1);

    biBottomToolBar.add(insertBtn1);

    biBottomToolBar.add(delBtn1);
    ZcUtil zu = new ZcUtil();
    if (zu.usePtzj()) {
      biBottomToolBar.add(addPtzjBtn);
    }

    biTablePanel.add(biBottomToolBar, BorderLayout.SOUTH);

    addPtzjBtn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcPProMake make = (ZcPProMake) listCursor.getCurrentObject();
        if (make.getBiList() == null) {
          make.setBiList(new ArrayList<ZcPProMitemBi>());
        }
        boolean haveOne = false;
        for (int i = 0; i < make.getBiList().size(); i++) {
          ZcPProMitemBi bi = (ZcPProMitemBi) make.getBiList().get(i);
          if (ZcPProMitemBi.DPTZJ.equals(bi.getOriginCode())) {
            haveOne = true;
            break;
          }
        }
        if (!haveOne) {
          ZcPProMitemBi zcPProMitemBi = new ZcPProMitemBi();
          zcPProMitemBi.setTempId(Guid.genID());

          setItemBiPtzjDefaultValue(zcPProMitemBi);

          int rowNum = addSub(biTablePanel, zcPProMitemBi);

          biTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

        }

      }

    });
    addBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcPProMitemBi zcPProMitemBi = new ZcPProMitemBi();

        zcPProMitemBi.setTempId(Guid.genID());

        setItemBiDefaultValue(zcPProMitemBi);

        // setListDefaultValue(asVal);

        int rowNum = addSub(biTablePanel, zcPProMitemBi);

        biTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    insertBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcPProMitemBi zcPProMitemBi = new ZcPProMitemBi();

        zcPProMitemBi.setTempId(Guid.genID());

        setItemBiDefaultValue(zcPProMitemBi);

        int rowNum = insertSub(biTablePanel, zcPProMitemBi);

        biTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    delBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        Integer[] checkedRows = deleteBiSub(biTablePanel);

        // 从新计算采购预算

        if (checkedRows.length > 0) {

          self.caculateMoney(((BeanTableModel) biTablePanel.getTable().getModel()).getDataBeanList());

        }

      }

    });

    itemTabPane = new JTabbedPane();

    itemTablePanel.init();

    itemTablePanel.setPanelId(this.getClass().getName() + "_itemTablePanel");

    itemTablePanel.getSearchBar().setVisible(false);

    itemTablePanel.setTablePreferencesKey(this.getClass().getName() + "_itemTable");

    itemTablePanel.getTable().setShowCheckedColumn(true);

    itemTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    itemTabPane.addTab("主商品", itemTablePanel);

    // JGroupableTableHeader itemTableHeader =
    // itemTablePanel.getTable().getTableHeader();

    // itemTableHeader.addColumnGroup("采购预算资金", new String[] {
    // "ZC_MER_PRICE", "ZC_FIELD_ZC_ITEM_SUM" });

    itemBottomToolBar = new JFuncToolBar();

    FuncButton addBtn11 = new SubaddButton(false);

    JButton insertBtn11 = new SubinsertButton(false);

    JButton delBtn11 = new SubdelButton(false);

    itemBottomToolBar.add(addBtn11);

    itemBottomToolBar.add(insertBtn11);

    itemBottomToolBar.add(delBtn11);

    itemTablePanel.add(itemBottomToolBar, BorderLayout.SOUTH);

    addBtn11.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        stopTableEditing();
        
        if(existItem()){
          JOptionPane.showMessageDialog(self, "只能选择一种商品", "提示", JOptionPane.INFORMATION_MESSAGE);
          return;
        }

        ZcPProMitem zcPProMitem = new ZcPProMitem();

        zcPProMitem.setTempId(Guid.genID());

        setItemDefaultValue(zcPProMitem);

        addSub(itemTablePanel, zcPProMitem);

      }

    });

    insertBtn11.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        stopTableEditing();
        
        if(existItem()){
          JOptionPane.showMessageDialog(self, "只能选择一种商品", "提示", JOptionPane.INFORMATION_MESSAGE);
          return;
        }

        ZcPProMitem zcPProMitem = new ZcPProMitem();

        zcPProMitem.setTempId(Guid.genID());

        setItemDefaultValue(zcPProMitem);

        insertSub(itemTablePanel, zcPProMitem);

      }

    });

    delBtn11.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        stopTableEditing();

        deleteSub(itemTablePanel);
        clearPeijian();

      }

    });

    peijianTablePanel.init();

    peijianTablePanel.setPanelId(this.getClass().getName() + "_peijianTablePanel");

    peijianTablePanel.getSearchBar().setVisible(false);

    peijianTablePanel.setTablePreferencesKey(this.getClass().getName() + "_peijianTable");

    peijianTablePanel.getTable().setShowCheckedColumn(true);

    peijianTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    itemTabPane.addTab("配件", peijianTablePanel);  
    
    JFuncToolBar peijianToolBar = new JFuncToolBar();

    FuncButton addPjBtn = new SubaddButton(false);

    JButton insertPjBtn = new SubinsertButton(false);

    JButton delPjBtn = new SubdelButton(false);

    peijianToolBar.add(addPjBtn);

    peijianToolBar.add(insertPjBtn);

    peijianToolBar.add(delPjBtn);

    peijianTablePanel.add(peijianToolBar, BorderLayout.SOUTH);

    addPjBtn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {        
        stopTableEditing();
        ZcPProMitem zcPProMitem = new ZcPProMitem();
        zcPProMitem.setTempId(Guid.genID());
        setItemDefaultValue(zcPProMitem);
        addSub(peijianTablePanel, zcPProMitem);
      }
    });

    insertPjBtn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {        
        stopTableEditing();
        ZcPProMitem zcPProMitem = new ZcPProMitem();
        zcPProMitem.setTempId(Guid.genID());
        setItemDefaultValue(zcPProMitem);
        insertSub(peijianTablePanel, zcPProMitem);
      }
    });

    delPjBtn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {        
        stopTableEditing();
        deleteSub(peijianTablePanel);
      }
    });

    serviceTablePanel.init();

    serviceTablePanel.setPanelId(this.getClass().getName() + "_serviceTablePanel");

    serviceTablePanel.getSearchBar().setVisible(false);

    serviceTablePanel.setTablePreferencesKey(this.getClass().getName() + "_serviceTable");

    serviceTablePanel.getTable().setShowCheckedColumn(true);

    serviceTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    itemTabPane.addTab("服务条款", serviceTablePanel);  
    
    JFuncToolBar serviceToolBar = new JFuncToolBar();

    FuncButton addSrBtn = new SubaddButton(false);

    JButton insertSrBtn = new SubinsertButton(false);

    JButton delSrBtn = new SubdelButton(false);

    serviceToolBar.add(addSrBtn);

    serviceToolBar.add(insertSrBtn);

    serviceToolBar.add(delSrBtn);

    serviceTablePanel.add(serviceToolBar, BorderLayout.SOUTH);

    addSrBtn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {        
        stopTableEditing();
        ZcPProMitemService zcPProMitem = new ZcPProMitemService();
        zcPProMitem.setServiceCode(Guid.genID());
        zcPProMitem.setIsAgree("Y"); 
        addSub(serviceTablePanel, zcPProMitem);
      }
    });

    insertSrBtn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {        
        stopTableEditing();
        ZcPProMitemService zcPProMitem = new ZcPProMitemService();
        zcPProMitem.setServiceCode(Guid.genID());
        zcPProMitem.setIsAgree("Y"); 
        insertSub(serviceTablePanel, zcPProMitem);
      }
    });

    delSrBtn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {        
        stopTableEditing();
        deleteSub(serviceTablePanel);
      }
    });
    biTabPane.setMinimumSize(new Dimension(240, 150));

    itemTabPane.setMinimumSize(new Dimension(240, 200));

    splitPane = new JSaveableSplitPane(JSplitPane.VERTICAL_SPLIT, biTabPane, itemTabPane);

    splitPane.setDividerDefaultLocation(this.getClass().getName() + "_splitPane_dividerLocation", 260);

    splitPane.setContinuousLayout(true);

    splitPane.setOneTouchExpandable(true);

    // 只显示向下的箭头

    // splitPane.putClientProperty("toExpand", true);

    splitPane.setDividerSize(10);

    // splitPane.setDividerLocation(260);

    splitPane.setBackground(self.getBackground());

    return splitPane;

  }

  /*
   * 
   * 商品选择外部部件处理类
   */

  protected boolean existItem() {
    ZcPProMake make = (ZcPProMake) listCursor.getCurrentObject();
    if(make.getItemList()!=null && make.getItemList().size()>0){
      return true;
    }
    return false;
  }

  public class ZcPeiJianHandler implements IForeignEntityHandler {
    public String columNames[];
    public ZcPeiJianHandler(String columNames[]) {
      this.columNames = columNames;
    }

    public void excute(List selectedDatas) {
      JTable table = peijianTablePanel.getTable();
      BeanTableModel model = (BeanTableModel) table.getModel();
      int k = table.getSelectedRow();
      if (k < 0)
        return;

      int k2 = table.convertRowIndexToModel(k);
      ZcPProMitem item = (ZcPProMitem) model.getBean(k2);
      if (selectedDatas.size() > 0) {
        ZcBMerchandise mer = (ZcBMerchandise) selectedDatas.get(0);
        item.setZcMerCode(mer.getZcMerCode());
        item.setZcMerName(mer.getZcMerName()); 
        item.setZcPitemName(mer.getZcMerName());
        item.setZcMerPrice(mer.getZcMerMPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        item.setZcBaseGgyq(mer.getZcMerSpec() == null ? mer.getZcMerCollocate() : mer.getZcMerSpec());
        //        item.setZcBaseGgyq(mer.getZcMerSpec());
        item.setZcBraCode(mer.getZcBraCode());
        item.setZcBraName(mer.getZcBraName());
        item.setZcCaigUnit(mer.getZcMerUnit());
        if(item.getZcCatalogueCode()==null){
          item.setZcCatalogueCode(mer.getZcCatalogueCode());
          item.setZcCatalogueName(mer.getZcCatalogueName());
        }
        // 清空已有的供应商
        item.setZcSuCode(null);
        item.setZcSuName(null);
      }
      model.fireTableDataChanged();
    }

    public void afterClear() {
      JTable table = peijianTablePanel.getTable();
      BeanTableModel model = (BeanTableModel) table.getModel();
      int k = table.getSelectedRow();
      if (k < 0)
        return;
      int k2 = table.convertRowIndexToModel(k);
      ZcPProMitem item = (ZcPProMitem) (model.getBean(k2));
      item.setZcMerCode(null);
      item.setZcMerName(null);
      item.setZcMerPrice(null);
      item.setZcBraCode(null);
      item.setZcBraName(null);
      item.setZcBaseGgyq(null);
      item.setZcCaigNum(null);
      item.setZcItemSum(null);  
      model.fireTableDataChanged();
    }
    @Override
    public TableModel createTableModel(List showDatas) {
      Object data[][] = new Object[showDatas.size()][columNames.length];
      for (int i = 0; i < showDatas.size(); i++) {
        ZcBMerchandise rowData = (ZcBMerchandise) showDatas.get(i);
        int col = 0;
        data[i][col++] = rowData.getZcMerName();
        data[i][col++] = rowData.getZcBraName();
        data[i][col++] = rowData.getZcMerSpec();
        data[i][col++] = rowData.getZcMerMPrice();
      }

      MyTableModel model = new MyTableModel(data, columNames) {
        @Override
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

    public boolean beforeSelect(ElementConditionDto dto) {
      JTable table = peijianTablePanel.getTable();
      BeanTableModel model = (BeanTableModel) table.getModel();
      int k = table.getSelectedRow();
      if (k < 0){
        return false;
      }
      ZcPProMake beanData = (ZcPProMake) self.listCursor.getCurrentObject();
      List<ZcPProMitem> bis = beanData.getItemList();
      dto.getCodeList().clear();
      if(bis==null){
        bis=new ArrayList<ZcPProMitem>();
      }
      for(int i=0;i<bis.size();i++){
        ZcPProMitem it=bis.get(i);
        if(it.getZcMerCode()!=null && it.getZcMerCode().trim().length()>0){
          dto.getCodeList().add(it.getZcMerCode());
        }
      } 
      if(dto.getCodeList().size()==0){
        JOptionPane.showMessageDialog(self, "请选择商品后再选择配件！", "提示", JOptionPane.INFORMATION_MESSAGE);
        return false;        
      }
      return true;
    }

  }

  public boolean doSave() {
    if (!isDataChanged()) {
      JOptionPane.showMessageDialog(this, "数据没有发生改变，不用保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
      return true;
    }
    boolean success = true;
    String errorInfo = "";
    ZcPProMake make = (ZcPProMake) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
    if (listPanel.checkBeforeSave(make, self)) {
      return false;
    }
    //去除子表的空行 
    clearEmpty();
    try {
//      requestMeta.setFuncId(saveButton.getFuncId());
      ZcPProMake inData = (ZcPProMake) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());
      ZcPProMake zcPProMake = this.listPanel.getZcPProMakeServiceDelegate().updateZcPProMakeFN(inData, ZcUtil.useBudget(),WorkEnv.getInstance().getWebRoot(), this.requestMeta);
      listCursor.setCurrentObject(zcPProMake);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      success = false;
      errorInfo += e.getMessage();
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      refreshData();
      this.listPanel.refreshCurrentTabData();
      sendButton.setEnabled(true);
      deleteButton.setEnabled(true);
    } else {
      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  private void clearEmpty() {
    ZcPProMake make = (ZcPProMake) this.listCursor.getCurrentObject();
    List l=new ArrayList();
    if(make.getPeiJianList()!=null){
      for(int i=0;i<make.getPeiJianList().size();i++){
        ZcPProMitem item=(ZcPProMitem)make.getPeiJianList().get(i);
        if(item.getZcMerName()!=null){
          l.add(item);
        }
      }
      make.getPeiJianList().clear();
      make.getPeiJianList().addAll(l);
    }
    l=new ArrayList();
    if(make.getServiceList()!=null){
      for(int i=0;i<make.getServiceList().size();i++){
        ZcPProMitemService s=(ZcPProMitemService)make.getServiceList().get(i);
        if(s.getServiceContent()!=null && s.getServiceContent().trim().length()>0){
          l.add(s);
        }
      }
      make.getServiceList().clear();
      make.getServiceList().addAll(l);
    }
  }

  protected void doDelete() {
    int num = JOptionPane.showConfirmDialog(this, "是否删除当前单据", "删除确认", 0);
    if (num == JOptionPane.YES_OPTION) {
      boolean success = true;
      ZcPProMake zcPProMake = null;
      String errorInfo = "";
      try {
        requestMeta.setFuncId(deleteButton.getFuncId());
        zcPProMake = (ZcPProMake) this.listCursor.getCurrentObject();
        // if (!"0".equals(zcPProMake.getZcMakeStatus()))       
        // JOptionPane.showMessageDialog(this, "非编辑状态单据，不可以删除！", "提示",JOptionPane.ERROR_MESSAGE);
        this.listPanel.getZcPProMakeServiceDelegate().deleteByPrimaryKeyFN(zcPProMake.getZcMakeCode(), ZcUtil.useBudget(),WorkEnv.getInstance().getWebRoot(), this.requestMeta);
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        success = false;
        errorInfo += e.getMessage();
      }
      if (success) {
        this.listCursor.removeCurrentObject();
        JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        doExit();
      } else {
        JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  public void stopTableEditing() {
    JPageableFixedTable biTable = this.biTablePanel.getTable();
    if (biTable.isEditing()) {
      biTable.getCellEditor().stopCellEditing();
    }
    JPageableFixedTable itemTable = this.itemTablePanel.getTable();
    if (itemTable.isEditing()) {
      itemTable.getCellEditor().stopCellEditing();
    }
    JPageableFixedTable peijianTable = peijianTablePanel.getTable();
    if (peijianTable.isEditing()) {
      peijianTable.getCellEditor().stopCellEditing();
    }
  }
 
}
