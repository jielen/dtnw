package com.ufgov.zc.client.zc.zcpprobichange;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.table.TableModel;

import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcXmcgHtToTableModelConverter;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.BeanUtil;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcPProBalChg;
import com.ufgov.zc.common.zc.model.ZcPProBalChgBi;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.model.ZcXmcgHtBi;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;

public class ZcHtChangePanel extends AbstractMainSubEditPanel {

  private JTablePanel biTablePanel = new JTablePanel();

  JFuncToolBar bottomToolBar1 = new JFuncToolBar();

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private ZcHtChangePanel self = this;
  private int count = 0;

  private ZcPProBalChg zcPProBalChg;

  public IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,

  "zcEbBaseServiceDelegate");

  public ZcHtChangePanel(ZcXmcgHt zcXmcgHt, ZcPProBalChg balChg, int count) {
    this.colCount = 4;
    this.zcPProBalChg = balChg;
    this.count = count;
    init();
    this.setEditingObject(zcXmcgHt);
    refreshData(zcXmcgHt);
  }

  public List<AbstractFieldEditor> createFieldEditors() {
    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    TextFieldEditor zcHtCode = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_CODE), "zcHtCode");

    editorList.add(zcHtCode);

    TextFieldEditor zcHtName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_NAME), "zcHtName");

    editorList.add(zcHtName);

    MoneyFieldEditor zcHtNum = new MoneyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_NUM), "zcHtNum");
    editorList.add(zcHtNum);

    TextFieldEditor zcSuName = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_SU_NAME), "zcSuName");
    editorList.add(zcSuName);

    for (AbstractFieldEditor ad : editorList) {
      ad.setEnabled(false);
    }
    return editorList;
  }

  public JComponent createSubBillPanel() {

    JTabbedPane biTabPane = new JTabbedPane();
    biTablePanel.init();

    biTablePanel.getSearchBar().setVisible(false);

    biTablePanel.setTablePreferencesKey(this.getClass().getName() + "_biTable");

    biTablePanel.getTable().setShowCheckedColumn(true);

    biTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(60, 0));

    biTabPane.addTab("资金构成", biTablePanel);

    FuncButton addBtn1 = new SubaddButton(false);

    JButton insertBtn1 = new SubinsertButton(false);

    JButton delBtn1 = new SubdelButton(false);

    bottomToolBar1.add(addBtn1);

    bottomToolBar1.add(insertBtn1);

    bottomToolBar1.add(delBtn1);

    biTablePanel.add(bottomToolBar1, BorderLayout.SOUTH);

    addBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcXmcgHtBi zcXmcgHtBi = new ZcXmcgHtBi();

        zcXmcgHtBi.setTempId(Guid.genID());

        //        setItemBiDefaultValue(zcXmcgHtBi);

        int rowNum = addSub(biTablePanel, zcXmcgHtBi);

        biTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    insertBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        ZcXmcgHtBi zcXmcgHtBi = new ZcXmcgHtBi();

        zcXmcgHtBi.setTempId(Guid.genID());

        //        setItemBiDefaultValue(zcXmcgHtBi);

        int rowNum = insertSub(biTablePanel, zcXmcgHtBi);

        biTablePanel.getTable().setRowSelectionInterval(rowNum, rowNum);

      }

    });

    delBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        JPageableFixedTable table = biTablePanel.getTable();

        Integer[] selectedRows = table.getCheckedRows();
        BeanTableModel tableModel = (BeanTableModel) table.getModel();

        List dataList = tableModel.getDataBeanList();

        for (Integer checkedRow : selectedRows) {

          int accordDataRow = table.convertRowIndexToModel(checkedRow);
          ZcXmcgHtBi bi = (ZcXmcgHtBi) dataList.get(accordDataRow);
          if (bi.getZcBiYjjsSum() != null && bi.getZcBiYjjsSum().intValue() > 0) {
            JOptionPane.showMessageDialog(self, "已经结算的指标，不允许删除!", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
          }
        }
        Integer[] checkedRows = deleteSub(biTablePanel);
      }

    });

    return biTabPane;
  }

  public void initToolBar(JFuncToolBar toolBar) {

  }

  private void refreshData(ZcXmcgHt zcXmcgHt) {
    List<ZcXmcgHtBi> list = null;
    if(count == 0){
    	list = zcEbBaseServiceDelegate.queryDataForList("ZC_XMCG_HT_BI.selectByHtCode", zcXmcgHt.getZcHtCode(), requestMeta);
    }else{
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("ZC_HT_CODE", zcXmcgHt.getZcHtCode());
    	map.put("BAL_CHG_ID", zcPProBalChg.getBalChgId());
    	
    	list = zcEbBaseServiceDelegate.queryDataForList("ZC_BAL_CHG_HT_BI.ibatorgenerated_selectBiByChgCode", map, requestMeta);
    }

    if (list == null) {
      throw new NullPointerException();
    } else {
      //填充ZcXmcgHtBi的ZcPProMitemBi
      for (ZcXmcgHtBi bi : list) {
        ElementConditionDto dto = new ElementConditionDto();

        dto.setZcText1(bi.getZcProBiSeq());
        ZcPProMitemBi zcPProMitemBi = (ZcPProMitemBi) zcEbBaseServiceDelegate.queryObject("ZC_P_PRO_MITEM_BI.getMitemBiWithHtBi", dto, requestMeta);

        if (zcPProMitemBi != null) {

          zcPProMitemBi.setZcBiUsedSum(zcPProMitemBi.getZcBiJhuaSum());
          bi.setZcPProMitemBi(zcPProMitemBi);
        }
      }
      zcXmcgHt.setBiList(list);
      biTablePanel.setTableModel(ZcXmcgHtToTableModelConverter.convertSubBiTableDataForSubHt(list, new HashMap()));
      ZcUtil.translateColName(biTablePanel.getTable(), ZcXmcgHtToTableModelConverter.biInfoForSubHtWihtBugdet);
      setTableEditor(biTablePanel.getTable());
    }
  }

  private void setTableEditor(final JPageableFixedTable table) {
    ZcXmcgHtToTableModelConverter.setBiTableEditor(table);

    final String columNames[] = { "指标来源", "指标编号", "资金性质", "支付方式", "指标金额", "预算金额" };
    IForeignEntityHandler handler = new IForeignEntityHandler() {
      @Override
      public void excute(List selectedDatas) {
        BeanTableModel model = (BeanTableModel) table.getModel();
        int k = table.getSelectedRow();
        if (k < 0)
          return;
        int k2 = table.convertRowIndexToModel(k);
        ZcXmcgHtBi htBi = (ZcXmcgHtBi) model.getBean(k2);
        if (selectedDatas.size() > 0) {

          ZcPProBalChgBi chgBi = (ZcPProBalChgBi) selectedDatas.get(0);
          fillHtBi(htBi, chgBi);
        }
        model.fireTableRowsUpdated(k, k);
      }

      /*

      * 清空外部实体对应的数据

      */
      public void afterClear() {
        BeanTableModel model = (BeanTableModel) table.getModel();
        int k = table.getSelectedRow();
        if (k < 0)
          return;
        int k2 = table.convertRowIndexToModel(k);
        ZcXmcgHtBi htBi = (ZcXmcgHtBi) model.getBean(k2);
        fillHtBi(htBi, null);
        model.fireTableRowsUpdated(k, k);
      }

      @Override
      public TableModel createTableModel(List showDatas) {
        Object data[][] = new Object[showDatas.size()][columNames.length];
        for (int i = 0; i < showDatas.size(); i++) {
          ZcPProBalChgBi chgBi = (ZcPProBalChgBi) showDatas.get(i);
          int col = 0;
          data[i][col++] = AsValDataCache.getName("ZC_VS_ORIGIN_NAME", chgBi.getOriginCode());
          data[i][col++] = chgBi.getZcBiNo();
          data[i][col++] = AsValDataCache.getName("ZC_VS_FUND_NAME", chgBi.getFundCode());
          data[i][col++] = AsValDataCache.getName("ZC_VS_PAYTYPE_NAME", chgBi.getPaytypeCode());
          data[i][col++] = chgBi.getZcBiSum();
          data[i][col++] = chgBi.getZcBiJhuaSum();

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
    };
    ElementConditionDto dto = new ElementConditionDto();
    dto.setZcText0(zcPProBalChg.getBalChgId());
    ForeignEntityFieldCellEditor biEditor = new ForeignEntityFieldCellEditor("ZC_P_PRO_BAL_CHG_BI.abatorgenerated_selectByElementChgId", dto, 20,
      handler, columNames, "计划资金", "zcBiNo");

    biEditor.getEditor().setEditable(false);

    //设置
    SwingUtil.setTableCellEditor(table, "ZC_BI_NO", biEditor);
    //    SwingUtil.setTableCellRenderer(table, ZcElementConstants.FIELD_TRANS_FUND_CODE, new AsValCellRenderer("ZC_VS_FUND_NAME"));

  }

  /**
   * 创建合同中选择资金的弹出框，弹出款的内容是新变更采购资金构成中的资金。
   */
  private void fillHtBi(ZcXmcgHtBi htbi, ZcPProBalChgBi chgBi) {
    BeanUtil.commonFieldsCopy(chgBi, htbi);

    htbi.setZcBiNo(chgBi.getZcBiNo());
    htbi.setZcProBiSeq(chgBi.getZcProBiSeq());

  }

  public void setEnedit(boolean flag){
	  this.bottomToolBar1.setVisible(flag);
	  this.biTablePanel.getTable().setEnabled(flag);
  }
}
