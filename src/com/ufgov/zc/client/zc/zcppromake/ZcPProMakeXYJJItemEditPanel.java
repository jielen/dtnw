package com.ufgov.zc.client.zc.zcppromake;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcPProMakeToTableModelConverter;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.event.ValueChangeEvent;
import com.ufgov.zc.client.component.event.ValueChangeListener;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.celleditor.IntCellEditor;
import com.ufgov.zc.client.component.table.celleditor.MoneyCellEditor;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.NumberCellRenderer;
import com.ufgov.zc.client.component.table.codecelleditor.FileCellEditor;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldEditor;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcBBrand;
import com.ufgov.zc.common.zc.model.ZcBCatalogue;
import com.ufgov.zc.common.zc.model.ZcBMerchandise;
import com.ufgov.zc.common.zc.model.ZcPProMake;
import com.ufgov.zc.common.zc.model.ZcPProMitem;

public class ZcPProMakeXYJJItemEditPanel extends AbstractMainSubEditPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 4060043744236199024L;

  private ZcPProMitem item;

  private ZcPProMakeEditPanel parent;

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private String compoId = "ZC_P_PRO_MAKE_XY_JJ";

  private JTablePanel jinjiaItemTablepanel;

  ForeignEntityFieldEditor zcCatalogueName;

  ForeignEntityFieldEditor zcBrandName;

  public ZcPProMakeXYJJItemEditPanel self = this;

  private String tabID;

  String catalogueCode;

  JFuncToolBar bottomToolBar2 = null;

  ZcPProMake zcPProMake;

  public BillElementMeta itemBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd("ZC_P_PRO_MITEM_MER2");

  public ZcPProMakeXYJJItemEditPanel(ZcPProMitem item, ZcPProMakeXYJJEditPanel parent) {
    super(item, "ZC_P_PRO_MAKE_XY_JJ");
    this.item = item;
    this.colCount = 4;
    this.parent = parent;
    this.tabID = item.getTempId();
    init();
    requestMeta.setCompoId(compoId);
    refreshData();
  }

  //将电子竞价合并在协议采购中的构造函数
  public ZcPProMakeXYJJItemEditPanel(ZcPProMitem item, ZcPProMakeXYEditPanel parent) {
    this.item = item;
    this.colCount = 4;
    this.parent = parent;
    this.tabID = item.getTempId();
    init();
    requestMeta.setCompoId(compoId);
    refreshData();
  }

  public void refreshData() {
    // TODO Auto-generated method stub
    setEditingObject(this.item);
    refreshSubBill();
    //updateFieldEditorsEditable();
    //    ZcPProMake zcPProMake = (ZcPProMake) parent1.listCursor.getCurrentObject();

    zcPProMake = (ZcPProMake) parent.listCursor.getCurrentObject();

    Long processInstId = zcPProMake.getProcessInstId();
    if (processInstId != null && processInstId.longValue() > 0) {
      setWFSubTableEditable(jinjiaItemTablepanel, false);
      for (int i = 0; i < this.fieldEditors.size(); i++) {
        this.fieldEditors.get(i).setEnabled(false);
      }
      //退回状态时，明细都可以编辑 add shijia 2011-10-14 
      if (zcPProMake.getZcMakeStatus().equals("0") && WorkEnv.getInstance().containRole(AsOptionMeta.getOptVal(ZcElementConstants.OPT_ZC_YSDWCG_ROLE))) {
        setWFSubTableEditable(jinjiaItemTablepanel, true);
        zcCatalogueName.setEnabled(true);
        zcBrandName.setEnabled(true);
      }
      //      List<AbstractFieldEditor> editorList = this.createFieldEditors();
      //      for (int i = 0; i < editorList.size(); i++) {
      //        editorList.get(i).setEnabled(false);
      //      }
    }
  }

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {
    // TODO Auto-generated method stub
    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    String[] catalogueColumNames = { "品目编码", "品目名称", "品目限额" };
    ZcCatalogueHandler catalogueHandler = new ZcCatalogueHandler(catalogueColumNames);
    ElementConditionDto elementCondtiontDto = new ElementConditionDto();
    elementCondtiontDto.setZcText0(String.valueOf(requestMeta.getSvNd()));
    zcCatalogueName = new ForeignEntityFieldEditor("selectPage.zcBCatalogue_selectByExampleLimit", elementCondtiontDto, 20, catalogueHandler,
      catalogueColumNames, "品目", "zcCatalogueName");
    editorList.add(zcCatalogueName);

    String[] brandColumNames = { "品牌编号", "品牌名称", "商品数量" };
    ZcEbBrandHandler brandHandler = new ZcEbBrandHandler(brandColumNames);
    elementCondtiontDto = new ElementConditionDto();
    zcBrandName = new ForeignEntityFieldEditor("ZcBrand.getBrandsForXieYi", elementCondtiontDto, 20, brandHandler, brandColumNames, "品牌", "zcBraName");
    zcBrandName.addValueChangeListener(new ValueChangeListener() {

      @Override
      public void valueChanged(ValueChangeEvent e) {
        //zcMakeCodeChange();
      }
    });
    zcBrandName.setEditable(false);
    editorList.add(zcBrandName);

    return editorList;
  }

  /*
   * 更新子表数据
   */
  private void refreshSubBill() {
    // TODO Auto-generated method stub
    jinjiaItemTablepanel.setTableModel(ZcPProMakeToTableModelConverter.convertJingJiaItemTableData(this.item.getMerList()));

    // 翻译表头列
    ZcUtil.translateColName(jinjiaItemTablepanel.getTable(), getItemInfo());
    setTableItemEditor(jinjiaItemTablepanel.getTable());

    // 设置从表监听 
    addItemTableLisenter(jinjiaItemTablepanel.getTable());

  }

  public List getItemInfo() {
    return ZcPProMakeToTableModelConverter.itemJJInfo;
  }

  public void setTableItemEditor(JTable table) {
    table.setDefaultEditor(String.class, new TextCellEditor());

    SwingUtil.setTableCellEditor(table, ZcElementConstants.FIELD_TRANS_ZC_PITEM_ATTACH, new FileCellEditor("zcPitemAttachBlobid",
      (BeanTableModel) table.getModel()));
    SwingUtil.setTableCellEditor(table, "ZC_FIELD_ZC_ITEM_SUM", new MoneyCellEditor(false));
    SwingUtil.setTableCellRenderer(table, "ZC_FIELD_ZC_ITEM_SUM", new NumberCellRenderer());
    SwingUtil.setTableCellEditor(table, "ZC_MER_PRICE", new MoneyCellEditor(false));
    SwingUtil.setTableCellRenderer(table, "ZC_MER_PRICE", new NumberCellRenderer());
    SwingUtil.setTableCellEditor(table, "ZC_FIELD_ZC_CAIG_NUM", new IntCellEditor(false));

    //String[] merColumNames = {"商品","品牌","型号","规格","价格"};
    //竞价选择商品对话框，显示列为：品牌、型号、价格、规格
    String[] merColumNames = { "品牌", "型号", "价格", "规格" };
    ZcEbMerHandler merHandler = new ZcEbMerHandler(merColumNames);
    ElementConditionDto merDto = new ElementConditionDto();
    ForeignEntityFieldCellEditor merEditor = new ForeignEntityFieldCellEditor("ZcBMerchandise.getMerchandise2", merDto, 20, merHandler,
      merColumNames, "商品", "zcMerName");
    SwingUtil.setTableCellEditor(table, "ZC_MER_NAME", merEditor);

  }

  @Override
  public JComponent createSubBillPanel() {
    // TODO Auto-generated method stub
    jinjiaItemTablepanel = new JTablePanel();
    jinjiaItemTablepanel.init();
    jinjiaItemTablepanel.getSearchBar().setVisible(false);
    jinjiaItemTablepanel.setTablePreferencesKey(this.getClass().getName() + "_jinjia_table");
    jinjiaItemTablepanel.getTable().setShowCheckedColumn(true);
    jinjiaItemTablepanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));
    JTabbedPane tabPane = new JTabbedPane();
    tabPane.addTab("商品明细", jinjiaItemTablepanel);
    bottomToolBar2 = new JFuncToolBar();
    FuncButton addBtn11 = new SubaddButton(false);
    JButton insertBtn11 = new SubinsertButton(false);
    JButton delBtn11 = new SubdelButton(false);
    bottomToolBar2.add(addBtn11);
    bottomToolBar2.add(insertBtn11);
    bottomToolBar2.add(delBtn11);
    jinjiaItemTablepanel.add(bottomToolBar2, BorderLayout.SOUTH);

    addBtn11.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ZcPProMitem zcPProMitem = new ZcPProMitem();
        zcPProMitem.setTempId(Guid.genID());
        addSub(jinjiaItemTablepanel, zcPProMitem);
      }
    });

    insertBtn11.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ZcPProMitem zcPProMitem = new ZcPProMitem();
        zcPProMitem.setTempId(Guid.genID());
        insertSub(jinjiaItemTablepanel, zcPProMitem);
      }
    });

    delBtn11.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        deleteSub(jinjiaItemTablepanel);
      }
    });
    return tabPane;
  }

  @Override
  public void initToolBar(JFuncToolBar toolBar) {
    // TODO Auto-generated method stub

  }

  /*
   * 品目选择外部部件处理类
   */
  public class ZcCatalogueHandler implements IForeignEntityHandler {

    public String columNames[];

    public ZcCatalogueHandler(String columNames[]) {
      this.columNames = columNames;
    }

    public void excute(List selectedDatas) {

      List itemList = zcPProMake.getItemList();
      ZcPProMitem item = (ZcPProMitem) itemList.get(0);
      catalogueCode = item.getZcCatalogueCode();
      for (int i = 0; i < selectedDatas.size(); i++) {
        ZcBCatalogue catalogue = (ZcBCatalogue) selectedDatas.get(i);
        item.setZcCatalogueCode(catalogue.getZcCatalogueCode());
        item.setZcCatalogueName(catalogue.getZcCatalogueName());
        zcPProMake.setZcMoneyBgZxs(new BigDecimal(catalogue.getZcZcgzStd() == null ? "0" : catalogue.getZcZcgzStd()));

        //王文辉修改，当品目切换时清空 品牌和明细列表
        item.setZcBraCode(null);
        item.setZcBraName(null);
        item.setMerList(new ArrayList());
        int num = JOptionPane.NO_OPTION;

        if (!catalogue.getZcCatalogueCode().equals(catalogueCode)) {
          if (catalogueCode != null) {
            catalogueCode = catalogue.getZcCatalogueCode();
            num = JOptionPane.showConfirmDialog(null, "您选择的商品品目发生了变化，点击 [是] 后竞价商品信息将被清空，需要重新填写！", "提示", 0);
          }
          if (num == JOptionPane.YES_OPTION) {
            itemList = zcPProMake.getItemList();
            List itList = new ArrayList();
            for (int k = 0; k < itemList.size(); k++) {
              ZcPProMitem mitem = (ZcPProMitem) itemList.get(k);
              mitem = new ZcPProMitem();
              itList.add(mitem);
            }
            zcPProMake.setItemList(itList);
          }
          catalogueCode = catalogue.getZcCatalogueCode();

        }
        setEditingObject(item);

      }

      itemList = zcPProMake.getItemList();
      for (int i = 0; i < itemList.size(); i++) {
        ZcPProMitem mitem = (ZcPProMitem) itemList.get(i);
        mitem.setZcCatalogueCode(item.getZcCatalogueCode());
        mitem.setZcCatalogueName(item.getZcCatalogueName());
      }
      self.parent.refreshJinJiaItemPanel();
    }

    @Override
    public TableModel createTableModel(List showDatas) {
      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        ZcBCatalogue rowData = (ZcBCatalogue) showDatas.get(i);
        int col = 0;
        data[i][col++] = rowData.getZcCatalogueCode();
        data[i][col++] = rowData.getZcCatalogueName();
        data[i][col++] = rowData.getZcZcgzStd();
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
      return true;
    }

    public boolean afterSelect() {
      return false;
    }
  }

  /*
   * 品牌选择外部部件处理类
   */
  public class ZcEbBrandHandler implements IForeignEntityHandler {

    public String columNames[];

    public ZcEbBrandHandler(String columNames[]) {
      this.columNames = columNames;
    }

    public void excute(List selectedDatas) {

      for (int i = 0; i < selectedDatas.size(); i++) {
        ZcBBrand brand = (ZcBBrand) selectedDatas.get(i);
        item.setZcBraCode(brand.getZcBraCode());
        item.setZcBraName(brand.getZcBraName());

        //王文辉添加，当品牌切换时，清空明细列表
        item.setMerList(new ArrayList());

        setEditingObject(item);
      }
      self.parent.refreshJinJiaItemPanel();
    }

    @Override
    public TableModel createTableModel(List showDatas) {
      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        ZcBBrand rowData = (ZcBBrand) showDatas.get(i);
        int col = 0;
        data[i][col++] = rowData.getZcBraCode();
        data[i][col++] = rowData.getZcBraName();
        data[i][col++] = rowData.getCountNum();
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
      if (item.getZcCatalogueCode() == null) {
        JOptionPane.showMessageDialog(self, "请先选择品目", "提示", JOptionPane.INFORMATION_MESSAGE);
        return false;
      } else {
        dto.setZcText0(item.getZcCatalogueCode());
        return true;
      }
    }
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
      JTable table = jinjiaItemTablepanel.getTable();

      BeanTableModel model = (BeanTableModel) table.getModel();
      int k = table.getSelectedRow();
      if (k < 0)
        return;
      int k2 = table.convertRowIndexToModel(k);
      ZcPProMitem mitem = (ZcPProMitem) (model.getBean(k2));

      if (selectedDatas.size() > 0) {
        ZcBMerchandise mer = (ZcBMerchandise) selectedDatas.get(0);
        mitem.setZcMerCode(mer.getZcMerCode());
        mitem.setZcMerName(mer.getZcMerName());
        mitem.setZcMerPrice(mer.getZcMerMPrice());
        mitem.setZcBaseGgyq(mer.getZcMerSpec());
        //清空已有的供应商
        mitem.setZcSuCode(null);
        mitem.setZcSuName(null);
        mitem.setZcCaigNum(null);
        mitem.setZcItemSum(null);
      }
      model.fireTableDataChanged();
    }

    @Override
    public TableModel createTableModel(List showDatas) {
      Object data[][] = new Object[showDatas.size()][columNames.length];

      for (int i = 0; i < showDatas.size(); i++) {

        ZcBMerchandise rowData = (ZcBMerchandise) showDatas.get(i);
        int col = 0;
        //        data[i][col++] = rowData.getZcMerName();
        //        data[i][col++] = rowData.getZcBraName();
        //        data[i][col++] = rowData.getZcMerSpec();
        //        data[i][col++] = rowData.getZcMerCollocate();
        //        data[i][col++] = rowData.getZcMerMPrice();

        //竞价选择商品时，弹出的对话框，显示列及顺序为：品牌、型号、价格、规格
        data[i][col++] = rowData.getZcBraName();
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

    public boolean beforeSelect(ElementConditionDto dto) {
      if (item.getZcBraCode() == null) {
        JOptionPane.showMessageDialog(self, "请先选择品牌", "提示", JOptionPane.INFORMATION_MESSAGE);
        return false;
      } else {
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
  }

  public void addItemTableLisenter(final JPageableFixedTable table) {
    final BeanTableModel model = (BeanTableModel) (table.getModel());
    model.addTableModelListener(new TableModelListener() {
      public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
          if (e.getColumn() >= 0
            && ("ZC_MER_PRICE".equals(model.getColumnIdentifier(e.getColumn())) || "ZC_FIELD_ZC_CAIG_NUM".equals(model.getColumnIdentifier(e
              .getColumn())))) {
            int k = table.getSelectedRow();
            if (k < 0)
              return;
            ZcPProMitem mitem = (ZcPProMitem) (model.getBean(table.convertRowIndexToModel(k)));
            BigDecimal caigNum = mitem.getZcCaigNum();
            BigDecimal merPrice = mitem.getZcMerPrice();
            if (caigNum != null && merPrice != null) {
              mitem.setZcItemSum((caigNum).multiply(merPrice));
              model.fireTableRowsUpdated(k, k);
            }
          }
        }
      }
    });

  }

  public String getTabID() {
    return tabID;
  }

  public void setTabID(String tabID) {
    this.tabID = tabID;
  }

  protected void setWFSubTableEditable(JTablePanel tablePanel, boolean isEnabled) {
    BeanTableModel tablemodel = (BeanTableModel) tablePanel.getTable().getModel();
    tablemodel.setEditable(isEnabled);
    Component[] components = tablePanel.getComponents();
    for (Component component : components) {
      if (component instanceof JFuncToolBar) {
        component.setEnabled(isEnabled);
      }
    }
  }

}
