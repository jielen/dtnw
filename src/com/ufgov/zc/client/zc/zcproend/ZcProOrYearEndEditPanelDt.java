/**
 * 
 */
package com.ufgov.zc.client.zc.zcproend;

import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_FUND_CODE;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ORIGIN_CODE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
import com.ufgov.smartclient.component.table.JGroupableTable;
import com.ufgov.smartclient.component.table.fixedtable.JFixedTable;
import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcPProMakeToTableModelConverter;
import com.ufgov.zc.client.common.converter.zc.ZcXmcgHtToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.button.UpdateButton;
import com.ufgov.zc.client.component.button.zc.CommonButton;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.celleditor.MoneyCellEditor;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.NumberCellRenderer;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecelleditor.FileCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsValCellRenderer;
import com.ufgov.zc.client.component.ui.TableDisplay;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.datacache.CompanyDataCache;
import com.ufgov.zc.client.util.ListUtil;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.fa.card.ZcFaCardClientUtil;
import com.ufgov.zc.client.zc.zcppromake.ZcBudgetHandler;
import com.ufgov.zc.client.zc.ztb.table.tablecomponent.JTable;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.system.util.Utils;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.model.ZcPProMake;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.model.ZcXmcgHtBi;
import com.ufgov.zc.common.zc.model.ZcYearPlan;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcYearEndServiceDelegate;

/**
 * @author Administrator
 *
 */
public class ZcProOrYearEndEditPanelDt extends AbstractMainSubEditPanel{
  
  private static Logger logger=Logger.getLogger(ZcProOrYearEndEditPanelDt.class);

  protected FuncButton saveButton = new SaveButton();
  protected FuncButton editButton = new EditButton();
  private FuncButton exitButton = new ExitButton();
  private ListCursor listCursor;
  private ZcProOrYearEndListPanel listPanel;
  protected GkBaseDialog parent;
  protected RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();
  private JPanel headPanel, footPanel, centerPanel;
  private JComponent makeBiPanel;
  private Hashtable fieldEditorHst = new Hashtable();
  public ZcProOrYearEndEditPanelDt self=this;
  protected ElementConditionDto getBiDto = new ElementConditionDto();
  private JTablePanel newBiTablePanel,oldBiTablePanel;
  private JTablePanel newHtBiTablePanel,oldHtBiTablePanel;
  private JPanel htTablePanel;
  private JGroupableTable htTable;
  private String pageStatus=ZcSettingConstants.PAGE_STATUS_BROWSE;
  private IZcYearEndServiceDelegate zcYearEndServiceDelegate = (IZcYearEndServiceDelegate) ServiceFactory.create(IZcYearEndServiceDelegate.class,"zcYearEndServiceDelegate");
  private IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,

  "zcEbBaseServiceDelegate");
  public ZcProOrYearEndEditPanelDt(ZcProOrYearEndEditDialogDt parent, ListCursor listCursor, String tabStatus, ZcProOrYearEndListPanel listPanel) {
    super(ZcYearPlan.class, BillElementMeta.getBillElementMetaWithoutNd("ZC_PRO_END_YEAR_END"));
    this.listCursor = listCursor;
    this.listPanel = listPanel;
    this.parent = parent;
    this.colCount = 3;
    init();
    requestMeta.setCompoId(listPanel.getcompoId());
    refreshData();
  }
  
  private void refreshData() {
    // TODO Auto-generated method stub

    ZcYearPlan yp = (ZcYearPlan) this.listCursor.getCurrentObject();
    if (null != yp) {
      this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;
      try {
        ZcYearPlan yearPlan=zcYearEndServiceDelegate.getZcYearPlanByMakeCode(requestMeta,yp);
        yearPlan.getMake().setCoName(CompanyDataCache.getName(yearPlan.getMake().getCoCode()));
        listCursor.setCurrentObject(yearPlan);
        setEditingObject(yearPlan);
        if(yearPlan.getNewMake()!=null && (yearPlan.getNewMake().getBiList()==null || yearPlan.getNewMake().getBiList().size()==0)){
          //说明还没有挂接资金，直接进入编辑模式
          this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;          
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        logger.error(e.getMessage(), e);
        JOptionPane.showMessageDialog(this,  e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        return;
      }
    }else{
      return;
    }
    
    ZcYearPlan yearPlan = (ZcYearPlan) this.listCursor.getCurrentObject();
    if (yearPlan.getNewMake()!=null && yearPlan.getNewMake().getBiList() != null && yearPlan.getNewMake().getBiList().size() > 0) {
      if (ZcUtil.useBudget()) {
        String sumId = "";
        List biList=yearPlan.getNewMake().getBiList();
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
      //      
      getBiDto.setNd(requestMeta.getSvNd());
      getBiDto.setZcText2("1");//对应sql语句里是可用指标金额>0
      if(ZcUtil.isYsdw()){
        getBiDto.setCoCode(requestMeta.getSvCoCode());
      }else{
        getBiDto.setCoCode(yearPlan.getMake().getCoCode());
      }
      
    }
    refreshSubTableData();
    //翻译表头
    tranlateTableColumn();
    //设置表格编辑器
    setTableEditor();
    setButtonStatusWithoutWf();
    updateFieldEditorsEditable();
    updateTableEditable();

  }
  private void setTableEditor() {
    // TODO Auto-generated method stub
    setNewMakeBiEditor();
  }
  
  protected void updateFieldEditorsEditable() { 
      for (AbstractFieldEditor editor : fieldEditors) {
        editor.getFieldName();
        if (editor.getFieldName().equals("zcMakeJzSum")) {//结转的计划金额，这个值可以手工调整，应对那种合同还没有输入的情况
          if(pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)){
            editor.setEnabled(true);
          }else{
            editor.setEnabled(false);
          }
        } else {
          editor.setEnabled(false);
        }
      }    
  }
  private void setNewMakeBiEditor() {
    // TODO Auto-generated method stub
    JPageableFixedTable table=newBiTablePanel.getTable();
    table.setDefaultEditor(String.class, new TextCellEditor());
    if (ZcUtil.useBudget()) {
      String colNames[] = { "指标余额表ID", "指标来源", "发文文号", "资金性质","采购项目", "功能分类", "经济分类","是否监督使用","是否政府采购","指标总金额","指标可用金额" };
      ZcBudgetHandler budgetHandler = new ZcBudgetHandler(colNames, newBiTablePanel, this, listCursor, getBiDto);
      //只显示政府采购的指标，汽修、汽保可以显示全部指标，参见汽修、汽保的选择部件
      getBiDto.setDattr1("zfcg");
      ForeignEntityFieldCellEditor suEditor = new ForeignEntityFieldCellEditor("VwBudgetGp.getVwBudgetGp", getBiDto, 20, budgetHandler, colNames,
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

    }
  }

  private void updateTableEditable() {
    // TODO Auto-generated method stub
    oldBiTablePanel.getTable().setEnabled(false);    
    oldHtBiTablePanel.getTable().setEnabled(false);
    if(pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)){
      newBiTablePanel.getTable().setEnabled(true);
      newHtBiTablePanel.getTable().setEnabled(true);
    }else{
      newBiTablePanel.getTable().setEnabled(false);
      newHtBiTablePanel.getTable().setEnabled(false);
    }
  }

  private void tranlateTableColumn() {
    // TODO Auto-generated method stub

    ZcUtil.translateColName(oldBiTablePanel.getTable(), ZcPProMakeToTableModelConverter.biInfoWihtBudget);
    ZcUtil.translateColName(newBiTablePanel.getTable(), ZcPProMakeToTableModelConverter.biInfoWihtBudget);
    ZcUtil.translateColName(newHtBiTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getBiInfo());
    ZcUtil.translateColName(oldHtBiTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getBiInfo());
  }

  private void refreshSubTableData() {
    ZcYearPlan yearPlan = (ZcYearPlan) this.listCursor.getCurrentObject();
    //设置原计划资金表
    oldBiTablePanel.setTableModel(ZcPProMakeToTableModelConverter.convertSubBiTableData(yearPlan.getMake().getBiList()==null?new ArrayList():yearPlan.getMake().getBiList(), wfCanEditFieldMap));
    //设置新计划资金表
    newBiTablePanel.setTableModel(ZcPProMakeToTableModelConverter.convertSubBiTableData(yearPlan.getNewMake().getBiList()==null?new ArrayList():yearPlan.getNewMake().getBiList(), wfCanEditFieldMap));
    //设置合同列表
    htTable.setModel(ZcXmcgHtToTableModelConverter.convertToTableModel(yearPlan.getNewHtList()==null?new ArrayList():yearPlan.getNewHtList())); 
   
    ZcXmcgHt newHt;
    if(yearPlan.getNewHtList()==null||yearPlan.getNewHtList().size()==0){
      newHt=new ZcXmcgHt();
    }else{
      MyTableModel model = (MyTableModel) htTable.getModel();
      newHt=(ZcXmcgHt) model.getList().get(htTable.convertRowIndexToModel(0));
    }
    //设置合同原资金表
    refreshHtOldBiTable(newHt);
    //设置合同新资金表
    refreshHtNewBiTable(newHt);
    
  //设置合同默认选中第一行 
    htTable.setRowSelectionAllowed(true);
    if(newHt.getZcHtCode()!=null){
      htTable.setRowSelectionInterval(0, 0);
    }
    //设置合同列表的监听
    addHtTableListener();
  }
  private void addHtTableListener() {
    // TODO Auto-generated method stub
    htTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          if (htTable.getSelectedRows() != null && htTable.getSelectedRows().length > 0) {
            int row = htTable.getSelectedRow();
            MyTableModel model = (MyTableModel) htTable.getModel();
            ZcXmcgHt newHt=(ZcXmcgHt) model.getList().get(htTable.convertRowIndexToModel(row));
            //设置合同原资金表
            refreshHtOldBiTable(newHt);
            //设置合同新资金表
            refreshHtNewBiTable(newHt);
          }
        }
      }
    });
  }

  protected void refreshHtNewBiTable(ZcXmcgHt newHt) {
    // TODO Auto-generated method stub
    newHtBiTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubBiTableData(newHt.getBiList()==null?new ArrayList():newHt.getBiList(), false));
  }

  protected void refreshHtOldBiTable(ZcXmcgHt newHt) {
    // TODO Auto-generated method stub
    if(newHt.getZcHtCode()==null){
      oldHtBiTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubBiTableData(new ArrayList(), false));
      return;
    }
    String htCode=newHt.getZcHtCode().substring(0, newHt.getZcHtCode().length()-2);
//    logger.debug(htCode);
    ZcYearPlan yearPlan = (ZcYearPlan) this.listCursor.getCurrentObject();
    if(yearPlan.getHtList()==null || yearPlan.getHtList().size()==0){
      oldHtBiTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubBiTableData(new ArrayList(), false));
      return;      
    }
    boolean find=false;
    for(int i=0;i<yearPlan.getHtList().size();i++){
      ZcXmcgHt oldht=(ZcXmcgHt)yearPlan.getHtList().get(i);
      if(oldht.getZcHtCode().equals(htCode)){
        List htBiLst=buildHtBiLst(oldht);
        find=true;
        oldHtBiTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubBiTableData(oldht.getBiList()==null?new ArrayList():oldht.getBiList(), false));
      }
    }
    if(!find){
      oldHtBiTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubBiTableData(new ArrayList(), false));
    }
  }

  private List buildHtBiLst(ZcXmcgHt zcXmcgHt) {
    // TODO Auto-generated method stub

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
    zcXmcgHt.setBiList(tempList);
    return tempList;
  }

  public void setButtonStatusWithoutWf() {
    // TODO Auto-generated method stub
    if(pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)){
      editButton.setEnabled(false);
      saveButton.setEnabled(true);
    }else{
      editButton.setEnabled(true);
      saveButton.setEnabled(false);      
    }
    //因为在结项时，有时需要打开这个界面查看这些数据，但此时为了防止挂接资金，因此判定如果系统时间和结转的计划原年度一样，说明在结转年度，不是新年度，不让保存。
    ZcYearPlan yearPlan = (ZcYearPlan) this.listCursor.getCurrentObject();
    if(yearPlan.getMake().getNd().intValue()==requestMeta.getSvNd()){
      saveButton.setEnabled(false);
    }
  }
  protected void init() {
    initTitle();
    initToolBar(toolBar);
    fieldEditors = new ArrayList<AbstractFieldEditor>();
    headPanel = createHeadPanel();
//    footPanel = createFootPanel();
    makeBiPanel = createMakeBiPanel();

    workPanel.setLayout(new BorderLayout());

    workPanel.add(headPanel, BorderLayout.NORTH);
    workPanel.add(makeBiPanel, BorderLayout.CENTER);
//    workPanel.add(footPanel, BorderLayout.SOUTH);
    
    JPanel htPanel=createHtPanel();    
    
    JTabbedPane tb=new JTabbedPane(JTabbedPane.LEFT);    
    tb.add("采购计划", workPanel);
    tb.add("采购合同", htPanel);
    tb.addChangeListener(new ChangeListener() {
      
      @Override
      public void stateChanged(ChangeEvent e) {
        // TODO Auto-generated method stub
        JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
        int selectedIndex = tabbedPane.getSelectedIndex();
        switch (selectedIndex) {
        case 1:
//          logger.debug("you selected ht");
          setMakeBiToHtBi();
         break;
        }
      }
    });
    this.setLayout(new BorderLayout());
    this.add(tb, BorderLayout.CENTER);
    this.add(toolBar,BorderLayout.NORTH);
  }

  /**
   * 根据采购计划选择的资金情况，同步合同的资金情况
   */
  private void setMakeBiToHtBi() {
    // TODO Auto-generated method stub
    updateMakeBiToHtBi();
    removeHtBi();
    
    //刷新合同新资金表
    refreshNewHtBiTable();
  }
/**
 * 刷新合同新资金表
 */
  private void refreshNewHtBiTable() {
    // TODO Auto-generated method stub
    ZcYearPlan yearPlan = (ZcYearPlan) this.listCursor.getCurrentObject();
    if(yearPlan.getNewHtList()==null || yearPlan.getNewHtList().size()==0)return;

    int row = htTable.getSelectedRow();
    MyTableModel model = (MyTableModel) htTable.getModel();
    ZcXmcgHt newHt=(ZcXmcgHt) model.getList().get(htTable.convertRowIndexToModel(row));
    //设置合同新资金表
    refreshHtNewBiTable(newHt);
    ZcUtil.translateColName(newHtBiTablePanel.getTable(), ZcXmcgHtToTableModelConverter.getBiInfo());
  }

  /**
   * 将新合同中存在，而计划里的不存在的资金记录清除
   */
  private void removeHtBi() {
    ZcYearPlan yearPlan = (ZcYearPlan) this.listCursor.getCurrentObject();
    if(yearPlan.getNewHtList()==null || yearPlan.getNewHtList().size()==0)return;
    if(yearPlan.getNewMake().getBiList()==null){
      yearPlan.getNewMake().setBiList(new ArrayList());
    }
    for(int i=0;i<yearPlan.getNewHtList().size();i++){
      ZcXmcgHt ht=(ZcXmcgHt) yearPlan.getNewHtList().get(i);
      if(ht.getBiList()==null || ht.getBiList().size()==0){
        continue;
      }
      List newLst = (List) ObjectUtil.deepCopy(ht.getBiList());
      for(int k=0;k<ht.getBiList().size();k++){
        boolean find=false;
        ZcXmcgHtBi htBi =(ZcXmcgHtBi)ht.getBiList().get(k);
        for(int j=0;j<yearPlan.getNewMake().getBiList().size();j++){
          ZcPProMitemBi makeBi =(ZcPProMitemBi)yearPlan.getNewMake().getBiList().get(j);
          if(htBi.getZcPProMitemBi()!=null){
            if(htBi.getZcPProMitemBi().getTempId()!=null && htBi.getZcPProMitemBi().getTempId().equals(makeBi.getTempId())){
              find=true;
              break;
            }
            if(htBi.getZcPProMitemBi().getZcProBiSeq()!=null && htBi.getZcPProMitemBi().getZcProBiSeq().equals(makeBi.getZcProBiSeq())){
              find=true;
              break;
            }            
          }
        }
        if(!find){
          ht.getBiList().remove(k);
          k--;
        }
      }
    }
  }
  /**
   * 将结转后的计划上配置的资金同步给结转后的合同
   */
  private void updateMakeBiToHtBi() {
    // TODO Auto-generated method stub
    ZcYearPlan yearPlan = (ZcYearPlan) this.listCursor.getCurrentObject();
    if(yearPlan.getNewHtList()==null || yearPlan.getNewHtList().size()==0)return;
    if(yearPlan.getNewMake().getBiList()==null || yearPlan.getNewMake().getBiList().size()==0)return;
    for(int i=0;i<yearPlan.getNewMake().getBiList().size();i++){
      ZcPProMitemBi makeBi =(ZcPProMitemBi)yearPlan.getNewMake().getBiList().get(i);
      for(int j=0;j<yearPlan.getNewHtList().size();j++){
        boolean find=false;
        ZcXmcgHt ht=(ZcXmcgHt) yearPlan.getNewHtList().get(j);
        if(ht.getBiList()==null){
          ht.setBiList(new ArrayList());
        }
        for(int k=0;k<ht.getBiList().size();k++){
          ZcXmcgHtBi htBi =(ZcXmcgHtBi)ht.getBiList().get(k);
          if(htBi.getZcPProMitemBi()!=null){
            if(htBi.getZcPProMitemBi().getTempId()!=null && htBi.getZcPProMitemBi().getTempId().equals(makeBi.getTempId())){
              find=true;
              htBi.setZcCanUseSum(makeBi.getZcBiJhuaSum());
              htBi.setZcBiSum(makeBi.getZcBiSum());
              htBi.setZcPProMitemBi(makeBi);
              break;
            }
            if(htBi.getZcPProMitemBi().getZcProBiSeq()!=null && htBi.getZcPProMitemBi().getZcProBiSeq().equals(makeBi.getZcProBiSeq())){
              find=true;
              htBi.setZcCanUseSum(makeBi.getZcBiJhuaSum());
              htBi.setZcBiSum(makeBi.getZcBiSum());
              htBi.setZcPProMitemBi(makeBi);
              break;
            }
          }
        }
        if(!find){
          ZcXmcgHtBi newHtBi = new ZcXmcgHtBi();
          newHtBi.setZcPProMitemBi(makeBi);
          newHtBi.setZcBiNo(makeBi.getZcBiNo());
          newHtBi.setZcProBiSeq(makeBi.getZcProBiSeq());
          newHtBi.setZcBiBcsySum(BigDecimal.ZERO);
          newHtBi.setZcCanUseSum(makeBi.getZcBiJhuaSum());
          newHtBi.setZcBiSum(makeBi.getZcBiSum());
          newHtBi.setTempId(makeBi.getTempId());
          newHtBi.setZcHtCode(ht.getZcHtCode());
          newHtBi.setZcUseBiId(makeBi.getZcUseBiId());
          ht.getBiList().add(newHtBi);
        }
      }
    }
  }

  private JComponent createMakeBiPanel() {
    // TODO Auto-generated method stub
    JPanel p=new JPanel();
    p.setLayout(new BorderLayout());
    p.add(createOldBiTable(),BorderLayout.NORTH);
    p.add(createNewBiTable(),BorderLayout.CENTER);
    return p;
  }

  private JPanel createHtPanel() {
    // TODO Auto-generated method stub

    JPanel p=new JPanel();
    p.setLayout(new BorderLayout());
    p.add(createHtTable(),BorderLayout.NORTH);
    JPanel pp=new JPanel();
    pp.setLayout(new BorderLayout());
    pp.add(createOldHtBiTable(),BorderLayout.NORTH);
    pp.add(createNewHtBiTable(),BorderLayout.CENTER);
    p.add(pp,BorderLayout.CENTER);
    p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "结转项目资金管理-采购合同", TitledBorder.CENTER,
      TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));
    return p;
  }
  private JComponent createHtTable(){
    
/*    TableDisplay tb=new TableDisplay("");

    htTablePanel = new JPanel();
    
    htTable=tb.getTable();
    
    htTable.setShowCheckedColumn(false);

    JTabbedPane htTabPane =new JTabbedPane();
    
    htTablePanel.setLayout(new BorderLayout());
    htTablePanel.add(tb,BorderLayout.CENTER);
    
    htTabPane.addTab("结转的合同", htTablePanel);*/
    
    
    final JFixedTable newTable = SwingUtil.createTable(JFixedTable.class);
    newTable.setPreferredScrollableViewportSize(new Dimension(getScreenWidth(), 150));
    htTable=newTable;
    newTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    JScrollPane scrollPane = new JScrollPane();
    scrollPane.getViewport().setView(newTable);
    scrollPane.revalidate();
    scrollPane.repaint(); 
    JTabbedPane htTabPane =new JTabbedPane();
    htTabPane.addTab("结转后的合同", scrollPane);
    return htTabPane;
  }
  int getScreenWidth() {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

    GraphicsDevice gs = ge.getDefaultScreenDevice();

    GraphicsConfiguration gc = gs.getDefaultConfiguration();

    Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    return screenSize.width - 100;

  }
  private JComponent createOldHtBiTable(){

    oldHtBiTablePanel = new JTablePanel();

    oldHtBiTablePanel.init();

    oldHtBiTablePanel.getSearchBar().setVisible(false);

    oldHtBiTablePanel.setTablePreferencesKey(this.getClass().getName() + "_oldHtbiTable");

    oldHtBiTablePanel.getTable().setShowCheckedColumn(true);

    oldHtBiTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    JTabbedPane htBiTabPane =new JTabbedPane();
    
    htBiTabPane.addTab("原合同资金构成", oldHtBiTablePanel);
    
    return htBiTabPane;
  }
  
  private JComponent createNewHtBiTable() {
    // TODO Auto-generated method stub

    newHtBiTablePanel = new JTablePanel();

    newHtBiTablePanel.init();

    newHtBiTablePanel.getSearchBar().setVisible(false);

    newHtBiTablePanel.setTablePreferencesKey(this.getClass().getName() + "_newHtbiTable");

    newHtBiTablePanel.getTable().setShowCheckedColumn(true);

    newHtBiTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    JTabbedPane htBiTabPane =new JTabbedPane();
    
    htBiTabPane.addTab("结转后合同的资金构成", newHtBiTablePanel);

    JFuncToolBar biBottomToolBar = new JFuncToolBar();

    FuncButton addBtn1 = new SubaddButton(false);

    JButton insertBtn1 = new SubinsertButton(false);

    JButton delBtn1 = new SubdelButton(false);

    biBottomToolBar.add(addBtn1);

    biBottomToolBar.add(insertBtn1);

    biBottomToolBar.add(delBtn1);
    ZcUtil zu=new ZcUtil();
    
    //不使用插入按钮了，根据结转的计划里的资金自动调配给合同，然后要求人手工指定资金的使用金额
//    newHtBiTablePanel.add(biBottomToolBar, BorderLayout.SOUTH);

    addBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcXmcgHtBi htBi = new ZcXmcgHtBi();

        htBi.setTempId(Guid.genID());

        setHtBiDefaultValue(htBi);

        // setListDefaultValue(asVal);

        int rowNum = addSub(newHtBiTablePanel, htBi);

        newHtBiTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    insertBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcXmcgHtBi htbi = new ZcXmcgHtBi();

        htbi.setTempId(Guid.genID());

        setHtBiDefaultValue(htbi);

        int rowNum = insertSub(newHtBiTablePanel, htbi);

        newHtBiTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    delBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        Integer[] checkedRows = deleteBiSub(newHtBiTablePanel);

        // 从新计算采购预算

//        if (checkedRows.length > 0) {
//
//          self.caculateMoney(((BeanTableModel) newBiTablePanel.getTable().getModel()).getDataBeanList());
//
//        }

      }

    });
    
    return htBiTabPane;
  }
protected void setHtBiDefaultValue(ZcXmcgHtBi htBi) {
    // TODO Auto-generated method stub
    
  }

private JComponent createOldBiTable(){

  oldBiTablePanel = new JTablePanel();

  oldBiTablePanel.init();

  oldBiTablePanel.getSearchBar().setVisible(false);

  oldBiTablePanel.setTablePreferencesKey(this.getClass().getName() + "_oldbiTable");

  oldBiTablePanel.getTable().setShowCheckedColumn(true);

  oldBiTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

  JTabbedPane biTabPane =new JTabbedPane();
  
  biTabPane.addTab("原计划资金构成", oldBiTablePanel);
  
  return biTabPane;
}
  
  private JComponent createNewBiTable() {
    // TODO Auto-generated method stub

    newBiTablePanel = new JTablePanel(null, AsOptionMeta.getOptVal(ZcSettingConstants.ZC_OPTON_JIHUA_ZIJIN_HELP_MSG));

    newBiTablePanel.init();

    newBiTablePanel.getSearchBar().setVisible(false);

    newBiTablePanel.setTablePreferencesKey(this.getClass().getName() + "_newbiTable");

    newBiTablePanel.getTable().setShowCheckedColumn(true);

    newBiTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    JTabbedPane biTabPane =new JTabbedPane();
    
    biTabPane.addTab("结转后的资金构成", newBiTablePanel);

    JFuncToolBar biBottomToolBar = new JFuncToolBar();

    FuncButton addBtn1 = new SubaddButton(false);

    JButton insertBtn1 = new SubinsertButton(false);

    JButton delBtn1 = new SubdelButton(false);
    
    FuncButton addPtzjBtn = new CommonButton(null,"待安排指标", "sendBill.png");

    biBottomToolBar.add(addBtn1);

    biBottomToolBar.add(insertBtn1);

    biBottomToolBar.add(delBtn1);
    ZcUtil zu=new ZcUtil();
    if(zu.usePtzj()){
      biBottomToolBar.add(addPtzjBtn);
    }
    newBiTablePanel.add(biBottomToolBar, BorderLayout.SOUTH);

    addPtzjBtn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcYearPlan yearPlan = (ZcYearPlan) listCursor.getCurrentObject();
        ZcPProMake make=yearPlan.getNewMake();
        if(make.getBiList()==null){
          make.setBiList(new ArrayList<ZcPProMitemBi>());
        }
        boolean haveOne=false;
        for(int i=0;i<make.getBiList().size();i++){
          ZcPProMitemBi bi=(ZcPProMitemBi)make.getBiList().get(i);
          if(ZcPProMitemBi.DPTZJ.equals(bi.getOriginCode())){
            haveOne=true;
            break;
          }
        }
        if(!haveOne){
          ZcPProMitemBi zcPProMitemBi = new ZcPProMitemBi();
          zcPProMitemBi.setTempId(Guid.genID());
          zcPProMitemBi.setZcMakeCode(make.getZcMakeCode());
          setItemBiPtzjDefaultValue(zcPProMitemBi);

          int rowNum = addSub(newBiTablePanel, zcPProMitemBi);

          newBiTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);
          
        }

      }

    });
    addBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcPProMitemBi zcPProMitemBi = new ZcPProMitemBi();

        zcPProMitemBi.setTempId(Guid.genID());
        ZcYearPlan yearPlan = (ZcYearPlan) listCursor.getCurrentObject();
        ZcPProMake make=yearPlan.getNewMake();
        zcPProMitemBi.setZcMakeCode(make.getZcMakeCode());
        setItemBiDefaultValue(zcPProMitemBi);

        // setListDefaultValue(asVal);

        int rowNum = addSub(newBiTablePanel, zcPProMitemBi);

        newBiTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    insertBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcPProMitemBi zcPProMitemBi = new ZcPProMitemBi();

        zcPProMitemBi.setTempId(Guid.genID());
        ZcYearPlan yearPlan = (ZcYearPlan) listCursor.getCurrentObject();
        ZcPProMake make=yearPlan.getNewMake();
        zcPProMitemBi.setZcMakeCode(make.getZcMakeCode());

        setItemBiDefaultValue(zcPProMitemBi);

        int rowNum = insertSub(newBiTablePanel, zcPProMitemBi);

        newBiTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    delBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        Integer[] checkedRows = deleteBiSub(newBiTablePanel);

        // 从新计算采购预算

//        if (checkedRows.length > 0) {
//
//          self.caculateMoney(((BeanTableModel) newBiTablePanel.getTable().getModel()).getDataBeanList());
//
//        }

      }

    });
    
    return biTabPane;
  }

 
  /*
   * 
   * 从表的删除行方法
   */

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
        if (ZcUtil.useBudget()) {
          ZcYearPlan yearPlan = (ZcYearPlan) listCursor.getCurrentObject();
          if(yearPlan.getNewMake().getBiList()==null || yearPlan.getNewMake().getBiList().size()==0)break;
          
          ZcPProMitemBi bi = (ZcPProMitemBi) yearPlan.getNewMake().getBiList().get(selRows[i]);
          if (bi.getZcBiNo() != null && !"".equals(bi.getZcBiNo())) {
            getBiDto.setZcText3(getBiDto.getZcText3().replaceAll(",'" + bi.getZcBiNo() + "'", "").replaceAll("'" + bi.getZcBiNo() + "',", "")
              .replaceAll("'" + bi.getZcBiNo() + "'", ""));
          }
        }

        tableModel.deleteRow(selRows[i]);

      }

    }

    fitColumnWidth(tablePanel.getTable());

    return checkedRows;

  }
  public void setItemBiDefaultValue(ZcPProMitemBi zcPProMitemBi) {

    zcPProMitemBi.setOriginCode("99");// 指标来源,值集：ZC_VS_ORIGIN_NAME 
    zcPProMitemBi.setOriginName("自筹资金");
    zcPProMitemBi.setFundCode("0");//资金性质,值集：ZC_VS_FUND_NAME
    zcPProMitemBi.setFundName("自筹资金");//资金性质,值集：ZC_VS_FUND_NAME
    zcPProMitemBi.setPaytypeCode("A");//支付方式, 值集：ZC_VS_PAYTYPE_NAME
    zcPProMitemBi.setZcProBiSeq(ZcUtil.getSequenceNextVal("ZcEbUtil.getZcProBiNextSeqVal"));

  }
  public void setItemBiPtzjDefaultValue(ZcPProMitemBi zcPProMitemBi) {
    zcPProMitemBi.setOriginCode("100");// 指标来源,值集：ZC_VS_ORIGIN_NAME 
    zcPProMitemBi.setOriginName("待安排指标");
    zcPProMitemBi.setFundCode("100");//资金性质,值集：ZC_VS_FUND_NAME
    zcPProMitemBi.setFundName("待安排指标");//资金性质,值集：ZC_VS_FUND_NAME
    zcPProMitemBi.setPaytypeCode("A");//支付方式, 值集：ZC_VS_PAYTYPE_NAME
    zcPProMitemBi.setZcProBiSeq(ZcUtil.getSequenceNextVal("ZcEbUtil.getZcProBiNextSeqVal"));

  }
  private JPanel createHeadPanel() {
    createFieldEditors();    
    for (Iterator iter = fieldEditors.iterator(); iter.hasNext();) {
      AbstractFieldEditor editor = (AbstractFieldEditor) iter.next();
      editor.setEnabled(false);
    }    
    ZcFaCardClientUtil util = new ZcFaCardClientUtil();
    return util.createPanel(fieldEditors, 3);
  }
  private void initTitle() {
    // TODO Auto-generated method stub

    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "结转项目资金管理-采购计划", TitledBorder.CENTER,
      TitledBorder.TOP, new Font("宋体", Font.BOLD, 15), Color.BLUE));
  }
  protected static String getCompoId(){
    return "ZC_PRO_END_YEAR_END";
  }
  @Override
  public void initToolBar(JFuncToolBar toolBar) {
    // TODO Auto-generated method stub

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(getCompoId());

    toolBar.add(editButton);

    toolBar.add(saveButton);

    toolBar.add(exitButton);
    
    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        doSave();
      }
    });
    editButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        doEdit();
      }
    });
    exitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        doExit();
      }
    });
  }

  protected void doExit() {
    // TODO Auto-generated method stub
    this.parent.dispose();
  }
  protected void doEdit() {
    // TODO Auto-generated method stub
    if(isCanEdit()){
      this.pageStatus=ZcSettingConstants.PAGE_STATUS_EDIT;
      updateTableEditable();
      updateFieldEditorsEditable();
      setButtonStatusWithoutWf();
    }else{
      JOptionPane.showMessageDialog(this, "已经产生了资金支付，不能再调整挂接资金了", "错误", JOptionPane.ERROR_MESSAGE);
    }
  }
  /*
   * 检查当前单据是否已经有了结算单，如果有了结算单了，不能修改当前单据的资金，因为结转时不结转资金支付单，当前有支付单，说明挂配的资金已经进入到支付环节了，不能调整资金了。这种适用于资金挂配保存后的在修改
   */
  private boolean isCanEdit() {
    // TODO Auto-generated method stub
    ZcYearPlan yp = (ZcYearPlan) this.listCursor.getCurrentObject();
    
    List balLst=zcEbBaseServiceDelegate.queryDataForList("ZC_P_PRO_BAL.selectByMakeCode", yp.getNewMake().getZcMakeCode(), requestMeta);
    if(balLst!=null && balLst.size()>0){
      return false;
    }
    return true;
  }
  protected void doSave() {
    // TODO Auto-generated method stub
    //同步计划的资金构成到合同上面去
    setMakeBiToHtBi();
    //检查金额关系
    if(!checkBeforeSave()){
      return;
    }

    requestMeta.setFuncId(saveButton.getFuncId());
    boolean success=true;
    String errorInfo = "";
    try {

      requestMeta.setFuncId(saveButton.getFuncId());

      ZcYearPlan yp = (ZcYearPlan) ObjectUtil.deepCopy(this.listCursor.getCurrentObject());

      ZcYearPlan ypp = zcYearEndServiceDelegate.saveJieZhuanZiJinFN(yp, this.requestMeta);

      listCursor.setCurrentObject(ypp);

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      success = false;
      errorInfo += e.getMessage();
    }

    if (success) {

      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      this.listPanel.refreshCurrentTabData();
      pageStatus=ZcSettingConstants.PAGE_STATUS_BROWSE;
      refreshData();
    } else {
      JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误", JOptionPane.ERROR_MESSAGE);
    }    
  }
  private boolean checkBeforeSave() {
    // TODO Auto-generated method stub

    ZcYearPlan yp = (ZcYearPlan) this.listCursor.getCurrentObject();
    StringBuffer errorMsg=new StringBuffer();
    //采购计划金额>=采购合同金额
    if(yp.getZcMakeJzSum().compareTo(yp.getZcHtJzSum())==-1){
      errorMsg.append("【结转计划金额】必须大于等于【结转合同金额】。\n");
    }
    //采购计划金额==计划资金构成使用金额合计
    if(yp.getNewMake().getBiList()!=null){
      BigDecimal d=new BigDecimal(0);
      for(int i=0;i<yp.getNewMake().getBiList().size();i++){
        ZcPProMitemBi makeBi =(ZcPProMitemBi)yp.getNewMake().getBiList().get(i);
        if(makeBi.getZcBiJhuaSum()==null){
          errorMsg.append("采购计划结转后的资金构成中，请填写【本次采购使用金额(元)】.\n");
          break;
        }else if(makeBi.getZcBiJhuaSum().compareTo(makeBi.getZcBiDoSum())==1 && makeBi.getZcBiNo()!=null){
          errorMsg.append("采购计划结转后的资金构成中，【本次采购使用金额(元)】不能大于 【指标可用金额】。\n");
          break;          
        }        
        d=d.add(makeBi.getZcBiJhuaSum());
      }
      if(d.compareTo(yp.getZcMakeJzSum())!=0){
        errorMsg.append("采购计划结转后的资金构成中，【本次采购使用金额(元)】合计必须等于【结转计划金额】。\n");
      }
    }
    //采购合同金额==合同资金构成使用金额合计;结转后资金构成中,每条资金的合同使用金额<=合同可用金额;合同资金构成中，多个合同，同一条资金的合同使用额合计<=计划金额
    if(yp.getNewHtList()!=null){
      Hashtable syTotalHt=new Hashtable();//同一条资金的合同使用额合计哈希表

      Hashtable jhHt=new Hashtable();//计划金额哈希表
      
      String zichouKey="zichou";
      for(int j=0;j<yp.getNewHtList().size();j++){
        ZcXmcgHt ht=(ZcXmcgHt) yp.getNewHtList().get(j);
        if(ht.getBiList()==null){
          ht.setBiList(new ArrayList());
        }
        BigDecimal d=new BigDecimal(0);
        for(int k=0;k<ht.getBiList().size();k++){
          ZcXmcgHtBi htBi =(ZcXmcgHtBi)ht.getBiList().get(k);
          
          if(htBi.getZcBiBcsySum()==null)htBi.setZcBiBcsySum(new BigDecimal(0));
          
          String key=htBi.getZcBiNo()==null?zichouKey:htBi.getZcBiNo();
          Object obj=syTotalHt.get(key);
          if(obj==null){
            syTotalHt.put(key, htBi.getZcBiBcsySum());
          }else{            
            syTotalHt.put(key, htBi.getZcBiBcsySum().add((BigDecimal)obj));
          }
          jhHt.put(key, htBi.getZcCanUseSum());
          
          d=d.add(htBi.getZcBiBcsySum());
          
          if(htBi.getZcBiBcsySum().compareTo(htBi.getZcCanUseSum())==1){
            errorMsg.append("合同("+ht.getZcHtCode()+")的结转后资金构成中的【合同使用金额】必须小于等于【合同可用金额】。\n");
          }
        }
        if(d.compareTo(ht.getZcHtNum())!=0){
          errorMsg.append("合同("+ht.getZcHtCode()+")的结转后资金构成中的【合同使用金额】合计必须等于【合同金额】。\n");
        }
      }
      
      Enumeration<String> keys=jhHt.keys();
      while(keys.hasMoreElements()){
        String key=keys.nextElement();
        BigDecimal jh=(BigDecimal) jhHt.get(key);
        BigDecimal syTotal=(BigDecimal) syTotalHt.get(key);
        if(jh.compareTo(syTotal)==-1){
          if(key.equals(zichouKey)){
            errorMsg.append("合同的自筹资金的使用合计不能超过采购计划中自筹资金的【本次采购使用金额(元)】.\n");
          }else{
            errorMsg.append("合同中资金(指标编号:"+key+")的使用合计不能超过采购计划中对应指标的【本次采购使用金额(元)】.\n");
          }
        }
      }
      
    }
    
    if(errorMsg.length()>0){
      JOptionPane.showMessageDialog(this, errorMsg.toString(), "提示", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  @Override
  public List<AbstractFieldEditor> createFieldEditors() {
    // TODO Auto-generated method stub

    fieldEditors = new ArrayList<AbstractFieldEditor>();


    TextFieldEditor makeCodeField = new TextFieldEditor("原计划编号", "zcMakeCode");
    fieldEditors.add(makeCodeField);
    
    TextFieldEditor newMakeCodeField = new TextFieldEditor("结转后计划编号", "newMake.zcMakeCode");
    fieldEditors.add(newMakeCodeField);

    TextFieldEditor makeNameField = new TextFieldEditor("计划名称", "zcMakeName");
    fieldEditors.add(makeNameField);

    AsValFieldEditor makeStatusField = new AsValFieldEditor("计划状态", "zcMakeStatus", "ZC_VS_MAKE_STATUS");
//    fieldEditors.add(makeStatusField);

    MoneyFieldEditor zcMakeSumField = new MoneyFieldEditor("原计划金额", "zcMakeSum");
    fieldEditors.add(zcMakeSumField);

    MoneyFieldEditor zcHtSumField = new MoneyFieldEditor("原合同金额", "zcHtSum");
    fieldEditors.add(zcHtSumField);

    MoneyFieldEditor zcBalSumField = new MoneyFieldEditor("已结算金额", "zcBalSum");
    fieldEditors.add(zcBalSumField);

    MoneyFieldEditor zcMakeJzSumField = new MoneyFieldEditor("结转计划金额", "zcMakeJzSum");
    fieldEditors.add(zcMakeJzSumField);

    MoneyFieldEditor zcHtJzSumField = new MoneyFieldEditor("结转合同金额", "zcHtJzSum");
    fieldEditors.add(zcHtJzSumField);

    MoneyFieldEditor zcBalJzSumField = new MoneyFieldEditor("结算结转金额", "zcBalJzSum");
//    fieldEditors.add(zcBalJzSumField);

    MoneyFieldEditor zcBlHtSumField = new MoneyFieldEditor("补录合同金额", "zcBlHtSum");
//    fieldEditors.add(zcBlHtSumField);

    MoneyFieldEditor zcBlBalSumField = new MoneyFieldEditor("补录合同结算金额", "zcBlBalSum");
//    fieldEditors.add(zcBlBalSumField);

    MoneyFieldEditor zcBlHtJzSumField = new MoneyFieldEditor("补录合同结转金额", "zcBlHtJzSum");
//    fieldEditors.add(zcBlHtJzSumField);

    TextFieldEditor ndField = new TextFieldEditor("原计划年度", "make.nd");
    fieldEditors.add(ndField);
    TextFieldEditor coCodeField = new TextFieldEditor("采购单位编码", "make.coCode");
    fieldEditors.add(coCodeField);
    TextFieldEditor coNameField = new TextFieldEditor("采购单位名称", "make.coName");
    fieldEditors.add(coNameField);
    return fieldEditors;
  }

  @Override
  public JComponent createSubBillPanel() {
    // TODO Auto-generated method stub
    return null;
  }

}
