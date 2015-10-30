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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcPProMakeToTableModelConverter;
import com.ufgov.zc.client.component.AsValComboBox;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JBasePanel;
import com.ufgov.zc.client.component.JClosableTabbedPane;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.button.SubinsertPinPaiButton;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.celleditor.DateCellEditor;
import com.ufgov.zc.client.component.table.celleditor.MoneyCellEditor;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.celleditor.zc.ZcBCatalogueCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.DateCellRenderer;
import com.ufgov.zc.client.component.table.cellrenderer.NumberCellRenderer;
import com.ufgov.zc.client.component.table.codecelleditor.FileCellEditor;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AutoNumFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.FileFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.OrgFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFilePathFieldEditor;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.commonbiz.model.Company;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcPProBalConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.model.AsVal;
import com.ufgov.zc.common.zc.checkrule.BaseRule;
import com.ufgov.zc.common.zc.checkrule.ZcMakeCheckRuleBySX;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityTreeHandler;
import com.ufgov.zc.common.zc.model.ZcBMerchandise;
import com.ufgov.zc.common.zc.model.ZcEbSupplier;
import com.ufgov.zc.common.zc.model.ZcPProMake;
import com.ufgov.zc.common.zc.model.ZcPProMitem;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;

public class ZcPProMakeXYEditPanel extends ZcPProMakeEditPanel {

  private static final long serialVersionUID = 12312312L;

  //协议商品市场价

  public BigDecimal zcMerPrice;

  AsValFieldEditor zcFukuanType; //采购类型

  public ZcPProMakeXYEditPanel selfXY = this;

  public JTablePanel jingJiaItemTablePanel = new JTablePanel("jingJiaItemTablePanel"); //竞价面板

  public ZcPProMakeXYJJItemEditPanel itemPanel;

  public static String lastActivedTabID = ""; //用来记录增加品牌的TAB_id

  private Map<String, ZcPProMakeXYJJItemEditPanel> tabID_TabPanelMap = new HashMap<String, ZcPProMakeXYJJItemEditPanel>();

  //竞价面板中放置增加、删除、修改按钮

  JFuncToolBar bottomToolBar3 = null;

  JButton insertPinpaiBtn = new SubinsertPinPaiButton();

  List<ZcEbSupplier> suplist;

  ElementConditionDto queryDto;

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

    unAuditButton.setVisible(false);

    refreshData();

    //start

    ZcPProMake make1 = (ZcPProMake) this.listCursor.getCurrentObject();

    if ("exec".equals(make1.getZcMakeStatus())) { //如果是终审状态，直接将销审按钮设置为可见

      //      unAuditButton.setVisible(true);

    }

    //end

    stopTableEditing();

    this.haveInitFlag = true;

    sendButton.setEnabled(false);

    ZcPProMake make = (ZcPProMake) listCursor.getCurrentObject();

    Long processInstId = make.getProcessInstId();

    if (make.getZcMakeStatus().equals("0") && make.getProcessInstId() != null && make.getProcessInstId().longValue() > 0) {

      sendButton.setEnabled(true);

      deleteButton.setEnabled(true);

    }
    if (make.getZcMakeStatus().equals("0") && make.getProcessInstId() == null) {

      deleteButton.setEnabled(false);
      sendButton.setEnabled(true);

    }

    //    if (make.getZcMakeStatus().equals("0") || (addButton.isVisible() && saveButton.isVisible() && deleteButton.isVisible())) {
    //
    //      updateWFSubTableEditable(true);
    //
    //    } else {
    //
    //      updateWFSubTableEditable(false);
    //
    //    }
    //    if ("40".equals(make.getZcMakeStatus()) && this.suggestPassButton.isVisible()) {
    //      setWFSubTableEditable(this.itemTablePanel, true);
    //    }

  }

  public void setDeftValue(ZcPProMake zcPProMake) {

    //其他

    zcPProMake.setZcPitemOpiway("6");

    //其他

    zcPProMake.setZcPifuCgfs("6");

    zcPProMake.setZcMakeStatus("0");

    zcPProMake.setZcIsBudget("N");

    zcPProMake.setZcIsImp("N");

    // 政府集中采购

    zcPProMake.setZcMakeSequence("1");

    zcPProMake.setZcFukuanType(ZcPProMake.CAIGOU_TYPE_XIEYI); //新建的时候，采购类型默认为协议供货

    zcPProMake.setZcYepFlag("00");

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

    zcMakeStatus.setEnabled(false);

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

    CompanyFieldEditor zcCoCode = new CompanyFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_CO_NAME), "coCode", companyHandler, getCompoId(),

    "CO_CODE");

    zcCoCode.setEnabled(true);

    TextFieldEditor zcCoCodeNd = new TextFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_CO_CODE_ND), "nd");

    OrgFieldEditor zcZgCsCode = new OrgFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_ZG_CS_CODE), "orgCode", false);

    TextFieldEditor zcMakeLinkman = new TextFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_MAKE_LINKMAN), "zcMakeLinkman");

    TextFieldEditor zcMakeTel = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_TEL), "zcMakeTel");

    TextFieldEditor zcAttr2 = new TextFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_ATTR2), "zcAttr2");

    zcMoneyBiSum = new MoneyFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_MONEY_BI_SUM), "zcMoneyBiSum");

    zcMoneyBiSum.setEnabled(false);

    AsValFieldEditor zcIsImp = new AsValFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_IS_IMP), "zcIsImp", "VS_Y/N");

    FileFieldEditor zcImpFile = new FileFieldEditor("进出口相关附件", "zcImpFile", "zcImpFileBlobid");

    zcFukuanType = new AsValFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_FUKUAN_TYPE), "zcFukuanType",

    "ZC_VS_FUKUAN_TYPE_XY") {

      private static final long serialVersionUID = 635104481592418539L;

      @Override
      protected void afterChange(AsValComboBox field) {

        AsVal av = field.getSelectedAsVal();

        if (av == null) {

          return;

        }

        String valId = av.getValId();

        if (haveInitFlag) {

          if (ZcPProMake.CAIGOU_TYPE_XIEYI.equals(valId)) { // 协议供货

            ZcPProMake beanData = (ZcPProMake) listCursor.getCurrentObject();

            beanData.setAgency(null);

            beanData.setAgencyName(null);

            df.setEnabled(false); //当为协议供货的时候，竞价截止时间不可用

            refreshItemPanel();

          } else { //当为电子竞价的时候，创建电子竞价项目明细子面板

            df.setEnabled(true);

            refreshJinJiaItemPanel();//创建竞价商品面板

          }

        }

        self.refreshJinJiaChengJiao();

      }

    };

    //    editorList.add(zcFukuanType);

    df = new DateFieldEditor(LangTransMeta.translate(FIELD_TRANS_ZC_EB_XY_END_DATE), "zcXieYiEndDate", DateFieldEditor.TimeTypeH24);

    df.setEnabled(false);

    ZcPProHandler zcPProHandler = new ZcPProHandler(self, this.listCursor);

    ElementConditionDto proDto = new ElementConditionDto();

    proDto.setZcText0(this.requestMeta.getEmpCode());

    ForeignEntityFieldEditor proName = new ForeignEntityFieldEditor("ZC_P_PRO.getZcPPro", proDto, 20, zcPProHandler,

    zcPProHandler.getColumNames(), "预算项目", "proName");

    DateFieldEditor zcInputDate = new DateFieldEditor(LangTransMeta.translate(ZcPProBalConstants.FIELD_TRANS_ZC_INPUT_DATE), "zcInputDate");
    zcInputDate.setEnabled(false);
    //  editorList.add(df);
    //  editorList.add(zcMakeTel);

    //    editorList.add(zcCoCodeNd);
    editorList.add(zcCoCode);
    editorList.add(zcMakeCode);
    editorList.add(zcMakeStatus);

    editorList.add(zcMakeLinkman);
    editorList.add(proName);
    editorList.add(zcIsImp);

    editorList.add(zcAttr2);
    editorList.add(zcMakeName);
    editorList.add(zcImpFile);

    editorList.add(zcZgCsCode);
    editorList.add(zcMoneyBiSum);
    editorList.add(zcInputDate);

    return editorList;

  }

  public void refreshJinJiaItemPanel() {

    createSubJingJiaItemPanel();

  }

  public void refreshItemPanel() {

    createSubItemPanel();

  }

  JBasePanel createSubItemPanel() {

    this.itemTabPane.remove(this.jingJiaItemTablePanel);

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

  JBasePanel createSubJingJiaItemPanel() {

    //this.itemTabPane = new JClosableTabbedPane();

    this.itemTabPane.remove(this.jingJiaItemTablePanel);

    this.itemTabPane.remove(this.itemTablePanel);

    this.jingJiaItemTablePanel = new JTablePanel(this.getClass().getName() + "_jingjiaItem_panel");

    ZcPProMake make = (ZcPProMake) this.listCursor.getCurrentObject();

    JClosableTabbedPane itemTabPanel = null;

    if (make.getItemList() != null && make.getItemList().size() > 0) {

      itemTabPanel = new JClosableTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

      if (make.getItemList().size() > 0) {

        for (int i = 0; i < make.getItemList().size(); i++) {

          ZcPProMitem item = (ZcPProMitem) make.getItemList().get(i);

          assemJinJiaItemPanel(item, itemTabPanel);

        }

      } else {

        ZcPProMitem item = new ZcPProMitem();

        item.setTempId(Guid.genID());

        item.setZcBraName("");

        item.setZcPitemName("电子竞价项目明细");

        assemJinJiaItemPanel(item, itemTabPanel);

      }

      this.jingJiaItemTablePanel.setLayout(new BorderLayout());

      this.jingJiaItemTablePanel.add(itemTabPanel, BorderLayout.CENTER);

      addActionListener(itemTabPanel);//tab选择或者新建时记录最后激活的tabID 

      int order = itemTabPanel.getComponentZOrder(tabID_TabPanelMap.get(lastActivedTabID));

      itemTabPanel.setSelectedIndex(order);

    }

    assembJinJiaItemTabs("竞价项目明细", "竞价项目明细", this.jingJiaItemTablePanel);

    bottomToolBar3 = new JFuncToolBar();

    insertPinpaiBtn = new SubinsertPinPaiButton(false);

    bottomToolBar3.add(insertPinpaiBtn);

    ZcPProMake zcPProMake = (ZcPProMake) listCursor.getCurrentObject();

    Long processInstId = zcPProMake.getProcessInstId();

    if (processInstId != null && processInstId.longValue() > 0) {

      bottomToolBar3.setEnabled(false);

    }

    jingJiaItemTablePanel.add(bottomToolBar3, BorderLayout.SOUTH);

    insertPinpaiBtn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcPProMitem item = new ZcPProMitem();

        item.setTempId(Guid.genID());

        item.setZcBraName("");

        item.setZcPitemName("电子竞价项目明细");

        lastActivedTabID = item.getTempId();//新增tab时，设置此tab为最后激活tab

        refreshPinpaiPanel(item);

      }

    });

    return this.jingJiaItemTablePanel;

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

  @Override
  public void setTableItemEditor(JTable table) {

    table.setDefaultEditor(String.class, new TextCellEditor());

    SwingUtil.setTableCellEditor(table, FIELD_TRANS_ZC_PITEM_ARR_DATE, new DateCellEditor());

    SwingUtil.setTableCellRenderer(table, FIELD_TRANS_ZC_PITEM_ARR_DATE, new DateCellRenderer());

    SwingUtil.setTableCellEditor(table, FIELD_TRANS_ZC_CATALOGUE_CODE, new ZcBCatalogueCellEditor(false, true));

    SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_ZC_PITEM_ATTACH, new FileCellEditor("zcPitemAttachBlobid",

    (BeanTableModel) table.getModel()));

    SwingUtil.setTableCellEditor(table, "ZC_FIELD_ZC_ITEM_SUM", new MoneyCellEditor(false));

    SwingUtil.setTableCellRenderer(table, "ZC_FIELD_ZC_ITEM_SUM", new NumberCellRenderer());

    //    SwingUtil.setTableCellEditor(table, "BUDGET_BI_MONEY", new MoneyCellEditor(false));

    //    SwingUtil.setTableCellRenderer(table, "BUDGET_BI_MONEY", new NumberCellRenderer());

    //    SwingUtil.setTableCellEditor(table, "BUDGET_OTHER_MONEY", new MoneyCellEditor(false));

    //    SwingUtil.setTableCellRenderer(table, "BUDGET_OTHER_MONEY", new NumberCellRenderer());

    SwingUtil.setTableCellEditor(table, "ZC_MER_PRICE", new MoneyCellEditor(false));

    SwingUtil.setTableCellRenderer(table, "ZC_MER_PRICE", new NumberCellRenderer());

    SwingUtil.setTableCellEditor(table, "ZC_FIELD_ZC_CAIG_NUM", new MoneyCellEditor(false));

    String[] brandColumNames = { "品牌编号", "品牌名称", "商品数量" };

    ZcEbBrandHandler brandHandler = new ZcEbBrandHandler(brandColumNames);

    this.brandDto = new ElementConditionDto();

    ForeignEntityFieldCellEditor packEditor = new ForeignEntityFieldCellEditor("ZcBrand.getBrandsForXieYi", this.brandDto, 20, brandHandler,

    brandColumNames, "品牌", "zcBraName");

    SwingUtil.setTableCellEditor(table, "ZC_BRA_NAME", packEditor);

    String[] merColumNames = { "商品", "品牌", "型号", "规格", "协议价格(元)" };

    ZcEbMerHandler merHandler = new ZcEbMerHandler(merColumNames);

    this.merDto = new ElementConditionDto();

    ForeignEntityFieldCellEditor merEditor = new ForeignEntityFieldCellEditor("ZcBMerchandise.getMerchandise2", this.merDto, 20, merHandler,

    merColumNames, "商品", "zcMerName");

    SwingUtil.setTableCellEditor(table, "ZC_MER_NAME", merEditor);

    //    String[] suColumNames = { "名称", "协议折扣率", "协议价格(元)", "联系人", "电话", "地址" };
    //
    //    ZcEbSupplierHandler suHandler = new ZcEbSupplierHandler(suColumNames);
    //
    //    this.merDto = new ElementConditionDto();
    //
    //    this.queryDto = new ElementConditionDto();
    //
    //    ForeignEntityFieldCellEditor suEditor = new ForeignEntityFieldCellEditor("ZcEbSupplier.getZcSuDisByMerNo", this.merDto, 20, suHandler,
    //
    //    suColumNames, "供应商", "name");
    //
    //    SwingUtil.setTableCellEditor(table, "ZC_SU_NAME", suEditor);

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

        //        item.setZcMerPrice(mer.getZcMerMPrice().setScale(2, BigDecimal.ROUND_HALF_UP));

        item.setZcBaseGgyq(mer.getZcMerCollocate());
        item.setZcBaseGgyq(mer.getZcMerSpec());

        //清空已有的供应商

        item.setZcSuCode(null);

        item.setZcSuName(null);

        item.setZcCaigNum(null);

        item.setZcItemSum(null);

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

        data[i][col++] = rowData.getZcMerName();

        data[i][col++] = rowData.getZcBraName();

        data[i][col++] = rowData.getZcMerSpec();

        data[i][col++] = rowData.getZcMerCollocate();

        data[i][col++] = rowData.getZcMerMPrice();

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

      if (item.getZcBraCode() == null) {

        JOptionPane.showMessageDialog(self, "请先选择品牌", "提示", JOptionPane.INFORMATION_MESSAGE);

        return false;

      }

      /* 应夏处要求，先将商品品目/品牌/商品/规格等输入次序功能进行暂时屏蔽，改为可用手工填写

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

      */

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

      /* 应夏处要求，先将商品品目/品牌/商品/规格等输入次序功能进行暂时屏蔽，改为可用手工填写

       * Update By FengYan 2011-06-14

      if (item.getZcMerCode() == null) {

        JOptionPane.showMessageDialog(self, "请先选择商品", "提示", JOptionPane.INFORMATION_MESSAGE);

        return false;

      } else if (item.getZcCaigNum() == null || item.getZcCaigNum() < 1) {

        JOptionPane.showMessageDialog(self, "请先填写采购数量", "提示", JOptionPane.INFORMATION_MESSAGE);

        return false;

      } else {

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

      */

      //协议供货选择商品后，选择供应商时，不需要提示输入采购数量

      /*if (item.getZcCaigNum() == null || item.getZcCaigNum() < 1) {

        //JOptionPane.showMessageDialog(self, "请先填写采购数量", "提示", JOptionPane.INFORMATION_MESSAGE);

        return false;

      } */

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

  //此处 addItemTableLisenter 暂不使用，使用父类方法  Update By FengYan 2011-06-14

  //  public void addItemTableLisenter(final JPageableFixedTable table) {

  //    final BeanTableModel model = (BeanTableModel) (table.getModel());

  //    IZcEbSupplierServiceDelegate zcEbSupplierServiceDelegate = (IZcEbSupplierServiceDelegate) ServiceFactory.create(

  //      IZcEbSupplierServiceDelegate.class, "zcEbSupplierServiceDelegate");

  //    suplist = zcEbSupplierServiceDelegate.getZcEbSup(queryDto, requestMeta);

  //    int disRate;

  //    if (suplist.size() == 1) {

  //      ZcEbSupplier sup = suplist.get(0);

  //

  //      BigDecimal xyPrice = sup.getZcMerXyPrice();

  //      disRate = sup.getZcMerXyDisRate();

  //      model.fireTableDataChanged();

  //    } else {

  //      disRate = 0;

  //    }

  //    model.addTableModelListener(new TableModelListener() {

  //      public void tableChanged(TableModelEvent e) {

  //        if (e.getType() == TableModelEvent.UPDATE) {

  //          if (e.getColumn() >= 0

  //            && ("ZC_MER_PRICE".equals(model.getColumnIdentifier(e.getColumn())) || "ZC_FIELD_ZC_CAIG_NUM".equals(model.getColumnIdentifier(e

  //              .getColumn())))) {

  //            int k = table.getSelectedRow();

  //            if (k < 0)

  //              return;

  //            ZcPProMitem item = (ZcPProMitem) model.getBean(table.convertRowIndexToModel(k));

  //            Integer caigNum = item.getZcCaigNum() == null ? 0 : item.getZcCaigNum();

  //            BigDecimal merPrice = item.getZcMerPrice() == null ? new BigDecimal(0) : item.getZcMerPrice();

  //            if (caigNum != null && merPrice != null) {

  //              item.setZcItemSum(new BigDecimal(caigNum).multiply(merPrice));

  //              model.fireTableRowsUpdated(k, k);

  //            }

  //            //在此判断如果资金构成列表中，预算金额之和为0则，设置采购预算为当前计划明细 总价的 合计 wangwei update start

  //            ZcPProMake beanData = (ZcPProMake) self.listCursor.getCurrentObject();

  //            List<ZcPProMitemBi> bis = beanData.getBiList();

  //            BigDecimal sum = BigDecimal.ZERO;

  //            if (bis != null) {

  //              for (ZcPProMitemBi bi : bis) {

  //                sum = sum.add((BigDecimal) ObjectUtils.defaultIfNull(bi.getZcBiJhuaSum(), new BigDecimal("0.00")));

  //              }

  //            }

  //            if (sum.compareTo(BigDecimal.ZERO) <= 0) {

  //              List<ZcPProMitem> its = beanData.getItemList();

  //              BigDecimal sumi = BigDecimal.ZERO;

  //              if (its != null) {

  //                for (ZcPProMitem zcPProMitem : its) {

  //                  sumi = sumi.add((BigDecimal) ObjectUtils.defaultIfNull(zcPProMitem.getZcItemSum(), new BigDecimal("0.00")));

  //                }

  //              }

  //              beanData.setZcMoneyBiSum(sumi);

  //              self.setEditingObject(beanData);

  //            }

  //            //wangwei updated end

  //          }

  //        }

  //      }

  //    });

  //  }

  public boolean isMultipleSelect() {

    return false;

  }

  /*   wangkewei  分两种情况，采购类型分别为协议供货G01和电子竞价G03 */

  @Override
  protected void refreshData() {

    ZcPProMake zcPProMake = (ZcPProMake) listCursor.getCurrentObject();

    boolean isNew;

    if (zcPProMake != null) {

      isNew = false;

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

      zcFukuanType.setEnabled(false); //如果不是新增进入的话，直接将采购方式设置为不可用

      if (ZcPProMake.CAIGOU_TYPE_DZJJ.equals(zcPProMake.getZcFukuanType())) {

        df.setEnabled(true);

        df.setEditable(true);

      }

    } else {

      isNew = true;

      zcPProMake = new ZcPProMake();

      setDeftValue(zcPProMake);

      zcPProMake.setCoCode(this.requestMeta.getSvCoCode());
      zcPProMake.setZcInputDate(this.requestMeta.getSysDate());

      zcPProMake.setOrgCode(WorkEnv.getInstance().getCurrCompany().getForgCode());

      zcPProMake.setNd(this.requestMeta.getSvNd());

      zcPProMake.setBiList(new ArrayList());

      zcPProMake.setItemList(new ArrayList());

      zcPProMake.setZcFukuanType(ZcPProMake.CAIGOU_TYPE_XIEYI);

      /* if (ZcPProMake.CAIGOU_TYPE_DZJJ.equals(zcPProMake.getZcFukuanType())) {

         zcPProMake.setZcFukuanType("G03");

       }

       //新增数据默认插入一行

       ZcPProMitem item = new ZcPProMitem();

       if (ZcPProMake.CAIGOU_TYPE_DZJJ.equals(zcPProMake.getZcFukuanType())) {

         item.setZcBraName("");

         item.setZcPitemName("电子竞价项目明细");

         item.setTempId(Guid.genID());

       }

       setItemDefaultValue(item);

       zcPProMake.getItemList().add(item);*/

      ZcPProMitemBi bi = new ZcPProMitemBi();

      setItemBiDefaultValue(bi);

      zcPProMake.getBiList().add(bi);

      listCursor.getDataList().add(zcPProMake);

      listCursor.setCurrentObject(zcPProMake);

      initOrg(zcPProMake);

    }
    if (ZcUtil.useBudget()) {
      getBiDto.setNd(requestMeta.getSvNd());
      getBiDto.setZcText2("1");
      getBiDto.setCoCode(zcPProMake.getCoCode() != null ? zcPProMake.getCoCode() : requestMeta.getSvCoCode());
    }

    this.setEditingObject(zcPProMake);

    if (ZcPProMake.CAIGOU_TYPE_DZJJ.equals(zcPProMake.getZcFukuanType())) {

      biTablePanel.setTableModel(ZcPProMakeToTableModelConverter.convertSubBiTableData(zcPProMake.getBiList(), wfCanEditFieldMap));

      itemTablePanel.setTableModel(ZcPProMakeToTableModelConverter.convertJingJiaItemTableData(zcPProMake.getItemList()));

      jingJiaItemTablePanel.setTableModel(ZcPProMakeToTableModelConverter.convertJingJiaItemTableData(zcPProMake.getItemList()));

    } else if (ZcPProMake.CAIGOU_TYPE_XIEYI.equals(zcPProMake.getZcFukuanType())) {

      List newItemList = new ArrayList();

      List itemList = zcPProMake.getItemList();

      for (int i = 0; i < itemList.size(); i++) {

        ZcPProMitem item = (ZcPProMitem) itemList.get(i);

        if (item.getZcSuCode() != null && WorkEnv.getInstance().getCurrUserId() != null) {

          if (item.getZcSuCode().trim().equals(WorkEnv.getInstance().getCurrUserId().trim())) {

            newItemList.add(item);

          }
        }

      }

      if (zcPProMake.getProcessInstId() != null && zcPProMake.getProcessInstId().longValue() > 0) {

        // 工作流的单据

        wfCanEditFieldMap = BillElementMeta.getWfCanEditField(zcPProMake, requestMeta);
      }

      itemTablePanel.setTableModel(new ZcPProMakeToTableModelConverter().convertSubItemTableData(newItemList.size() > 0 ? newItemList : itemList,

      getItemInfo(), wfCanEditFieldMap));

      biTablePanel.setTableModel(ZcPProMakeToTableModelConverter.convertSubBiTableData(zcPProMake.getBiList(), wfCanEditFieldMap));

      //刷新竞价信息和成交信息

      refreshJinJiaChengJiao();

    }

    // 翻译从表表头列

    if (ZcPProMake.CAIGOU_TYPE_XIEYI.equals(zcPProMake.getZcFukuanType())) {

      ZcUtil.translateColName(itemTablePanel.getTable(), getItemInfo());

      setTableItemEditor(itemTablePanel.getTable()); // 设置从表列类型

      addItemTableLisenter(itemTablePanel.getTable()); // 设置从表监听 

    }

    ZcUtil.translateColName(biTablePanel.getTable(), ZcPProMakeToTableModelConverter.biInfoWihtBudget);

    // 设置从表列类型

    setTableBiEditor(biTablePanel.getTable());

    // 设置从表监听 

    addBiTableLisenter(biTablePanel.getTable());

    setOldObject();

    // 根据工作流模版设置功能按钮是否可用

    setButtonStatus(zcPProMake, requestMeta, this.listCursor);

    // 根据工作流模版设置字段是否可编辑

    updateWFEditorEditable(zcPProMake, requestMeta);

    this.fitTable();

    // 草稿状态设置【批复采购方式】只读

    if ("0".equals(zcPProMake.getZcMakeStatus()) || (addButton.isVisible() && saveButton.isVisible() && deleteButton.isVisible())) {

      if (zcPifuCgfs != null) {

        zcPifuCgfs.setEnabled(false);

      }

      chengJiaoButton.setEnabled(false);

    }

    //退回状态时，明细都可以编辑 add shijia 2011-10-14

    ZcPProMake make = (ZcPProMake) listCursor.getCurrentObject();

    Long processInstId = make.getProcessInstId();
    String cgzgshRoleId = AsOptionMeta.getOptVal(ZcElementConstants.OPT_ZC_CGZG_SH_ROLE);
    if (make.getZcMakeStatus().equals("0") && processInstId != null && processInstId.longValue() > 0
      && WorkEnv.getInstance().containRole(cgzgshRoleId)) {

      if (make.getZcMakeStatus().equals("0") && processInstId != null && processInstId.longValue() > 0 && WorkEnv.getInstance().containRole("cg_zgsh")) {

        BeanTableModel biModel = (BeanTableModel) biTablePanel.getTable().getModel();

        biModel.setEditable(false);

        BeanTableModel itmodel = (BeanTableModel) itemTablePanel.getTable().getModel();

        itmodel.setEditable(false);

      }

      //在终审和未提交状态设置【成交】按钮不可用

      if ("exec".equals(zcPProMake.getZcMakeStatus())) {

        chengJiaoButton.setEnabled(true);

      }

      if (ZcPProMake.CAIGOU_TYPE_DZJJ.equals(zcPProMake.getZcFukuanType())) {

        //创建竞价商品面板

        refreshJinJiaItemPanel();

        //刷新竞价信息和成交信息

        refreshJinJiaChengJiao();

        //设置旧值。判断页面值是否改变

        setOldObject();

      }

      if (ZcPProMake.CAIGOU_TYPE_XIEYI.equals(zcPProMake.getZcFukuanType()) && isNew) {

        biTablePanel.getTable().setRowSelectionInterval(0, 0);

        itemTablePanel.getTable().setRowSelectionInterval(0, 0);

      }

      //电子竞价截止时间是否可编辑的判断  add by humina

      if (ZcPProMake.CAIGOU_TYPE_DZJJ.equals(zcPProMake.getZcFukuanType()) && "0".equals(zcPProMake.getZcMakeStatus())) {

        df.setEnabled(true);

        df.setEditable(false);

      }

      biTabPane.repaint();

      itemTabPane.repaint();

      //add shijia 20111020 
      if (make.getZcMakeStatus().equals("0") && WorkEnv.getInstance().containRole(AsOptionMeta.getOptVal(ZcElementConstants.OPT_ZC_YSDWCG_ROLE))) {

        insertPinpaiBtn.setEnabled(true);

      }

    }
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

  protected void updateWFSubTableEditable(boolean isEdit) {

    // 默认的方法是判断isEdit属性（主表中是否存在可编辑字段）,如果主表中存在可编辑字段，就设置从表字段都可编辑.

    // 如果这个逻辑满足不了业务，可以在实现类里覆盖updateWFSubTableEditable方法，自己做判断.

    if (getSubTables() != null) {

      if (isEdit) {

        for (JTablePanel tablePanel : getSubTables()) {

          setWFSubTableEditable(tablePanel, true);

          //          jingJiaItemTablePanel.setEnabled(true);

          Component[] components = jingJiaItemTablePanel.getComponents();

          for (Component component : components) {

            if (component instanceof JFuncToolBar) {

              component.setEnabled(true);

            }

          }

        }

      } else {

        for (JTablePanel tablePanel : getSubTables()) {
          if ("sa".equals(WorkEnv.getInstance().getCurrUserId()) && tablePanel == biTablePanel) {
            setWFSubTableEditable(tablePanel, true);
            continue;
          }

          setWFSubTableEditable(tablePanel, false);

          Component[] components = jingJiaItemTablePanel.getComponents();

          for (Component component : components) {

            if (component instanceof JFuncToolBar) {

              component.setEnabled(false);

            }

          }

          //          jingJiaItemTablePanel.setEnabled(false);

        }

      }

    }

  }

}
