/** * ZcPProMakeJinJiaEditPanel.java * com.ufgov.gk.client.zc.zcppromake * Administrator * 2010-11-13 */package com.ufgov.zc.client.zc.zcppromake;import java.awt.Dimension;import java.math.BigDecimal;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import javax.swing.JCheckBox;import javax.swing.JComponent;import javax.swing.JOptionPane;import javax.swing.JTabbedPane;import javax.swing.JTable;import javax.swing.event.TableModelEvent;import javax.swing.event.TableModelListener;import javax.swing.table.TableModel;import org.apache.commons.lang.ObjectUtils;import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;import com.ufgov.zc.client.common.MyTableModel;import com.ufgov.zc.client.common.WorkEnv;import com.ufgov.zc.client.common.converter.zc.ZcPProMakeToTableModelConverter;import com.ufgov.zc.client.component.JFuncToolBar;import com.ufgov.zc.client.component.JTablePanel;import com.ufgov.zc.client.component.table.BeanTableModel;import com.ufgov.zc.client.component.table.celleditor.IntCellEditor;import com.ufgov.zc.client.component.table.celleditor.MoneyCellEditor;import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;import com.ufgov.zc.client.component.table.cellrenderer.NumberCellRenderer;import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;import com.ufgov.zc.client.component.table.codecellrenderer.AsValCellRenderer;import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;import com.ufgov.zc.client.component.zc.fieldeditor.CheckBoxFieldEditor;import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;import com.ufgov.zc.client.util.SwingUtil;import com.ufgov.zc.client.zc.ZcUtil;import com.ufgov.zc.common.system.RequestMeta;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;import com.ufgov.zc.common.zc.model.ZcBBrand;import com.ufgov.zc.common.zc.model.ZcBMerchandise;import com.ufgov.zc.common.zc.model.ZcPProMake;import com.ufgov.zc.common.zc.model.ZcPProMitem;import com.ufgov.zc.common.zc.model.ZcPProMitemBaoJia;import com.ufgov.zc.common.zc.model.ZcPProMitemService;/** * @author Administrator * */public class ZcPProMakeJinJiaEditPanel extends AbstractMainSubEditPanel {  /**   *    */  private static final long serialVersionUID = 1135130800058404602L;  private ZcPProMitemBaoJia baojia;  private String compoId = "ZC_P_PRO_MAKE";  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();  private ZcPProMakeJinJiaEditPanel self = this;  private ZcPProMakeEditPanel parent;  private JTablePanel jinjiaTablepanel;  private JTablePanel serviceTablepanel;  private CheckBoxFieldEditor chengJiaoBox;  private boolean isDingHuo;  private ElementConditionDto brandDto;  private ElementConditionDto merDto;  private HashMap<BigDecimal, ZcPProMitem> bjRelationMap;  /* (non-Javadoc)   * @see com.ufgov.gk.client.component.zc.AbstractMainSubEditPanel#createFieldEditors()   */  public ZcPProMakeJinJiaEditPanel(ZcPProMitemBaoJia baojia, ZcPProMakeEditPanel parent) {    this.baojia = baojia;    this.colCount = 4;    this.parent = parent;    init();    requestMeta.setCompoId(compoId);    refreshData();  }  public ZcPProMakeJinJiaEditPanel(ZcPProMitemBaoJia bj, boolean isDingHuo, String compoId) {    // TCJLODO Auto-generated constructor stub    this.baojia = bj;    this.colCount = 4;    this.isDingHuo = isDingHuo;    requestMeta.setCompoId(compoId);    init();    refreshData();  }  public void refreshData() {    setEditingObject(this.baojia);    //更新子表数据    refreshSubBill();    updateFieldEditorsEditable();  }  protected void updateFieldEditorsEditable() {    super.updateFieldEditors();    if (ZcPProMitemBaoJia.BAO_JIA_STATUS_DRAFT.equals(this.baojia.getBaoJiaStatus())) {      for (AbstractFieldEditor fd : this.fieldEditors) {        if (fd.getFieldName() != null&& (fd.getFieldName().equals("gongHuoStatus") || fd.getFieldName().equals("noGongHuoReason"))) {          fd.setEnabled(true);        } else {          fd.setEnabled(false);        }      }      this.jinjiaTablepanel.getTable().setEnabled(true);      this.serviceTablepanel.getTable().setEnabled(true);    } else {      for (AbstractFieldEditor fd : this.fieldEditors) {        fd.setEnabled(false);      }      this.jinjiaTablepanel.getTable().setEnabled(true);      this.serviceTablepanel.getTable().setEnabled(true);    }  }  /*   * 更新子表数据   */  private void refreshSubBill() {    // TCJLODO Auto-generated method stub    jinjiaTablepanel.setTableModel(ZcPProMakeToTableModelConverter.convertBaoJiaItemTableData(this.baojia.getBaoJiaDetailList()));    // 翻译表头列    ZcUtil.translateColName(jinjiaTablepanel.getTable(), ZcPProMakeToTableModelConverter.getBaoJiaItemInfo());    setTableItemEditor(jinjiaTablepanel.getTable());    addItemTableLisenter(jinjiaTablepanel.getTable());        ZcPProMakeToTableModelConverter c=new ZcPProMakeToTableModelConverter();    serviceTablepanel.setTableModel(c.convertXyServiceItem(this.baojia.getServiceList(), c.serviceInfo, wfCanEditFieldMap));    ZcUtil.translateColName(serviceTablepanel.getTable(), c.serviceInfo);    setTableServiceEditor(serviceTablepanel.getTable());  }  private void setTableServiceEditor(JPageableFixedTable table) {    table.setDefaultEditor(String.class, new TextCellEditor());    SwingUtil.setTableCellEditor(table, ZcPProMitemService.COL_IS_AGREE, new AsValComboBoxCellEditor("VS_Y/N"));    SwingUtil.setTableCellRenderer(table, ZcPProMitemService.COL_IS_AGREE, new AsValCellRenderer("VS_Y/N"));  }  private void setTableItemEditor(JPageableFixedTable table) {    // TCJLODO Auto-generated method stub      table.setDefaultEditor(String.class, new TextCellEditor());    SwingUtil.setTableCellEditor(table, "ZC_FIELD_ZC_ITEM_SUM", new MoneyCellEditor(false));    SwingUtil.setTableCellRenderer(table, "ZC_FIELD_ZC_ITEM_SUM", new NumberCellRenderer());    SwingUtil.setTableCellEditor(table, "ZC_FIELD_ZC_MER_PRICE", new MoneyCellEditor(false));    SwingUtil.setTableCellRenderer(table, "ZC_FIELD_ZC_MER_PRICE", new NumberCellRenderer());    SwingUtil.setTableCellEditor(table, "ZC_FIELD_ZC_CAIG_NUM", new IntCellEditor(false));    String[] brandColumNames = { "品牌编号", "品牌名称", "商品数量" };    ZcEbBrandHandler brandHandler = new ZcEbBrandHandler(brandColumNames);    this.brandDto = new ElementConditionDto();    ForeignEntityFieldCellEditor packEditor = new ForeignEntityFieldCellEditor(    "ZcBrand.selectBrandForBaoJia", this.brandDto, 20, brandHandler, brandColumNames, "品牌", "zcBraName");    SwingUtil.setTableCellEditor(table, "ZC_BRA_NAME", packEditor);    String[] merColumNames = { "商品", "品牌", "规格", "价格" };    ZcEbMerHandler merHandler = new ZcEbMerHandler(merColumNames);    this.merDto = new ElementConditionDto();    ForeignEntityFieldCellEditor merEditor = new ForeignEntityFieldCellEditor(    "ZcBMerchandise.getMerchandiseForBaoJia", this.merDto, 20, merHandler, merColumNames, "商品", "zcMerName");    SwingUtil.setTableCellEditor(table, "ZC_MER_NAME", merEditor);  }  public void addItemTableLisenter(final JPageableFixedTable table) {    final BeanTableModel model = (BeanTableModel) table.getModel();    model.addTableModelListener(new TableModelListener() {      public void tableChanged(TableModelEvent e) {        if (e.getType() == TableModelEvent.UPDATE) {          if (e.getColumn() >= 0          && ("ZC_P_PROMAKE_PRICE".equals(model.getColumnIdentifier(e.getColumn()))          || "ZC_FIELD_ZC_CAIG_NUM".equals(model.getColumnIdentifier(e.getColumn())) || "ZC_MER_PRICE".equals(model.getColumnIdentifier(e.getColumn())))) {            int k = table.getSelectedRow();            if (k < 0)              return;            ZcPProMitem item = (ZcPProMitem) model.getBean(table.convertRowIndexToModel(k));            BigDecimal caigNum = item.getZcCaigNum() == null ? BigDecimal.ZERO : item.getZcCaigNum();            BigDecimal merPrice = item.getZcMerPrice() == null ? new BigDecimal(0) : item.getZcMerPrice();            if (caigNum != null && merPrice != null) {              item.setZcItemSum(caigNum.multiply(merPrice));              model.fireTableRowsUpdated(k, k);            }          }        }      }    });  }  @Override  public List<AbstractFieldEditor> createFieldEditors() {    // TCJLODO Auto-generated method stub    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();    TextFieldEditor zcMakeName = new TextFieldEditor("供应商", "suName");    zcMakeName.setEnabled(false);    editorList.add(zcMakeName);    MoneyFieldEditor zcMoneyBiSum = new MoneyFieldEditor("总价", "totalPrice");    zcMoneyBiSum.setEnabled(false);    editorList.add(zcMoneyBiSum);    DateFieldEditor df = new DateFieldEditor("竞价提交时间", "baoJiaSubmitDate", DateFieldEditor.TimeTypeH24);    df.setEnabled(false);    editorList.add(df);    AsValFieldEditor baoJiaStatus = new AsValFieldEditor("状态", "baoJiaStatus", "ZC_VS_BAO_JIA_STATUS");    baoJiaStatus.setEnabled(false);    editorList.add(baoJiaStatus);    if (this.isDingHuo) {      CheckBoxFieldEditor dingHuoBox = new CheckBoxFieldEditor("不供货", "gongHuoStatus") {        @Override        protected void checkBoxFieldActionPerformed(JCheckBox field) {          if (field.isSelected()) {            //清空报价价格             clearBaoJiaPrice();          }        }      };      editorList.add(dingHuoBox);      TextFieldEditor noGongHuoField = new TextFieldEditor("不供货原因", "noGongHuoReason");      editorList.add(noGongHuoField);    }    return editorList;  }  /*   * 清空报价价格    */  protected void clearBaoJiaPrice() {    // TCJLODO Auto-generated method stub    this.baojia.setZeroPrice();    this.baojia.setGongHuo(false);    this.setEditingObject(this.baojia);    this.jinjiaTablepanel.getTable().repaint();  }  /* (non-Javadoc)   * @see com.ufgov.gk.client.component.zc.AbstractMainSubEditPanel#createSubBillPanel()   */  @Override  public JComponent createSubBillPanel() {    // TCJLODO Auto-generated method stub    jinjiaTablepanel = new JTablePanel();    jinjiaTablepanel.init();    jinjiaTablepanel.setPanelId(this.getClass().getName() + "_jinjiaTablepanel");    jinjiaTablepanel.getSearchBar().setVisible(false);    jinjiaTablepanel.setTablePreferencesKey(this.getClass().getName() + "_jinjia_table");    jinjiaTablepanel.getTable().setShowCheckedColumn(true);    jinjiaTablepanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));    serviceTablepanel = new JTablePanel();    serviceTablepanel.init();    serviceTablepanel.setPanelId(this.getClass().getName() + "_serviceTablepanel");    serviceTablepanel.getSearchBar().setVisible(false);    serviceTablepanel.setTablePreferencesKey(this.getClass().getName() + "_serviceTable");    serviceTablepanel.getTable().setShowCheckedColumn(true);    serviceTablepanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));        JTabbedPane tabPane = new JTabbedPane();    tabPane.addTab("报价明细", jinjiaTablepanel);    tabPane.addTab("服务条款", serviceTablepanel);    return tabPane;  }  /* (non-Javadoc)   * @see com.ufgov.gk.client.component.zc.AbstractMainSubEditPanel#initToolBar(com.ufgov.gk.client.component.JFuncToolBar)   */  @Override  public void initToolBar(JFuncToolBar toolBar) {    // TCJLODO Auto-generated method stub  }  public void setChengJiaoCheckBoxEnable(boolean enable) {    if (this.chengJiaoBox != null) {      this.chengJiaoBox.setEnabled(enable);    }  }/*   * 品牌选择外部部件处理类   */  private class ZcEbBrandHandler implements IForeignEntityHandler {    private String columNames[];    public ZcEbBrandHandler(String columNames[]) {      this.columNames = columNames;    }    public void excute(List selectedDatas) {      // TCJLODO Auto-generated method stub      JTable table = jinjiaTablepanel.getTable();      BeanTableModel model = (BeanTableModel) table.getModel();      int k = table.getSelectedRow();      if (k < 0)        return;      int k2 = table.convertRowIndexToModel(k);      ZcPProMitem item = (ZcPProMitem) (model.getBean(k2));      if (selectedDatas.size() > 0) {        ZcBBrand brand = (ZcBBrand) selectedDatas.get(0);        item.setZcBraCode(brand.getZcBraCode());        item.setZcBraName(brand.getZcBraName());        //清空已有的商品        item.setZcMerCode(null);        item.setZcMerName(null);      }      model.fireTableDataChanged();    }    @Override    public TableModel createTableModel(List showDatas) {      Object data[][] = new Object[showDatas.size()][columNames.length];      for (int i = 0; i < showDatas.size(); i++) {        ZcBBrand rowData = (ZcBBrand) showDatas.get(i);        int col = 0;        data[i][col++] = rowData.getZcBraCode();        data[i][col++] = rowData.getZcBraName();        data[i][col++] = rowData.getCountNum();      }      MyTableModel model = new MyTableModel(data, columNames) {        public boolean isCellEditable(int row, int colum) {          return false;        }      };      return model;    }    @Override    public boolean isMultipleSelect() {      // TCJLODO Auto-generated method stub      return false;    }    public boolean beforeSelect(ElementConditionDto dto) {      JTable table = jinjiaTablepanel.getTable();      BeanTableModel model = (BeanTableModel) table.getModel();      int k = table.getSelectedRow();      if (k < 0)        return false;      int k2 = table.convertRowIndexToModel(k);      ZcPProMitem item = (ZcPProMitem) (model.getBean(k2));      if (item.getZcCatalogueCode() == null) {        JOptionPane.showMessageDialog(self, "请先选择品目", "提示", JOptionPane.INFORMATION_MESSAGE);        return false;      } else {        dto.setZcText0(item.getZcCatalogueCode());        dto.setZcText1(requestMeta.getSvUserID());        return true;      }    }  }  /*   * 商品选择外部部件处理类   */  private class ZcEbMerHandler implements IForeignEntityHandler {    private String columNames[];    public ZcEbMerHandler(String columNames[]) {      this.columNames = columNames;    }    public void excute(List selectedDatas) {      // TCJLODO Auto-generated method stub      JTable table = jinjiaTablepanel.getTable();      BeanTableModel model = (BeanTableModel) table.getModel();      int k = table.getSelectedRow();      if (k < 0)        return;      int k2 = table.convertRowIndexToModel(k);      ZcPProMitem item = (ZcPProMitem) (model.getBean(k2));      if (selectedDatas.size() > 0) {        ZcBMerchandise mer = (ZcBMerchandise) selectedDatas.get(0);        item.setZcMerCode(mer.getZcMerCode());        item.setZcMerName(mer.getZcMerName());        item.setZcMerPrice(mer.getZcMerMPrice());      }      model.fireTableDataChanged();    }    @Override    public TableModel createTableModel(List showDatas) {      Object data[][] = new Object[showDatas.size()][columNames.length];      for (int i = 0; i < showDatas.size(); i++) {        ZcBMerchandise rowData = (ZcBMerchandise) showDatas.get(i);        int col = 0;        data[i][col++] = rowData.getZcMerName();        data[i][col++] = rowData.getZcBraName();        data[i][col++] = rowData.getZcMerSpec();        data[i][col++] = rowData.getZcMerMPrice();      }      MyTableModel model = new MyTableModel(data, columNames) {        public boolean isCellEditable(int row, int colum) {          return false;        }      };      return model;    }    @Override    public boolean isMultipleSelect() {      // TCJLODO Auto-generated method stub      return false;    }    public boolean beforeSelect(ElementConditionDto dto) {      JTable table = jinjiaTablepanel.getTable();      BeanTableModel model = (BeanTableModel) table.getModel();      int k = table.getSelectedRow();      if (k < 0)        return false;      int k2 = table.convertRowIndexToModel(k);      ZcPProMitem item = (ZcPProMitem) (model.getBean(k2));      if (item.getZcBraCode() == null) {        JOptionPane.showMessageDialog(self, "请先选择品牌", "提示", JOptionPane.INFORMATION_MESSAGE);        return false;      } else if (isConfirmMer(item)) {        JOptionPane.showMessageDialog(self, "商品已经确定，不能变更", "提示", JOptionPane.INFORMATION_MESSAGE);        return false;      } else {        dto.setZcText0(ZcBMerchandise.ENABLE);        dto.setZcText1(item.getZcCatalogueCode());        dto.setZcText2(item.getZcBraCode());        dto.setZcText3(requestMeta.getSvUserID());        return true;      }    }  }  private boolean isConfirmMer(ZcPProMitem bjDetail) {    // TCJLODO Auto-generated method stub    if (this.bjRelationMap == null)      return false;    ZcPProMitem item = this.bjRelationMap.get(bjDetail.getZcPitemCode());    if (item.getZcMerCode() != null) {      return true;    }    return false;  }  public void setBjRelation(HashMap<BigDecimal, ZcPProMitem> bjRelationMap) {    // TCJLODO Auto-generated method stub    this.bjRelationMap = bjRelationMap;  }  public JTablePanel getJinjiaTablepanel() {    return jinjiaTablepanel;  }  public void setJinjiaTablepanel(JTablePanel jinjiaTablepanel) {    this.jinjiaTablepanel = jinjiaTablepanel;  }  public ZcPProMitemBaoJia getBaojia() {    return baojia;  }  public void setBaojia(ZcPProMitemBaoJia baojia) {    this.baojia = baojia;  }}