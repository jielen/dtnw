package com.ufgov.zc.client.zc.project;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsMapCellRenderer;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.formula.FormulaSetMainPanel;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.model.AsVal;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcEbFormula;
import com.ufgov.zc.common.zc.model.ZcEbFormulaPack;
import com.ufgov.zc.common.zc.model.ZcEbPack;

public class ZcEbReqFormulaPanel extends JPanel {

  JTablePanel packTablePanel = new JTablePanel();

  //  private JSaveableSplitPane splitPane;

  private JFuncToolBar subPackTableToolbar;

  private ZcEbFormula zcEbFormula;

  public ZcEbFormulaPack zcEbFormulaPack;

  private ZcEbFormulaPack oldZcEbFormulaPack;

  private ZcEbReqFormulaPanel self = this;

  public FormulaSetMainPanel formulaSetMainPanel;

  private ZcEbReqFormulaJTabbedPane parenPanel;

  private JButton addBtn1;

  private JButton insertBtn1;

  private JButton delBtn1;

  public ZcEbReqFormulaPanel(ZcEbFormulaPack zcEbFormulaPack, List packList, ZcEbReqFormulaJTabbedPane parentPanel, boolean saveButtonisVisible) {
    this.zcEbFormula = zcEbFormulaPack.getZcEbFormula();
    this.zcEbFormulaPack = zcEbFormulaPack;
    this.parenPanel = parentPanel;
    initComponet();
    setOldObject();
  }

  private void initComponet() {
    this.setLayout(new BorderLayout());
    refreshSubPanel(zcEbFormula);
    this.add(formulaSetMainPanel, BorderLayout.CENTER);
  }

  public void refreshSubPanel(ZcEbFormula zcEbFormula) {
    formulaSetMainPanel = new FormulaSetMainPanel(zcEbFormula, false);
    formulaSetMainPanel.getZcEbFormulaEditPanel().exitButton.setVisible(false);
    formulaSetMainPanel.getZcEbFormulaEditPanel().saveButton.setVisible(false);
    formulaSetMainPanel.getZcEbFormulaEditPanel().saveTemplateButton.setVisible(false);
    zcEbFormula = formulaSetMainPanel.getZcEbFormula();
    zcEbFormulaPack.setZcEbFormula(formulaSetMainPanel.getZcEbFormula());
    /**
     * 绘制标段选择TablePanel
     */
    JTabbedPane tabPane = new JTabbedPane();
    packTablePanel.init();
    packTablePanel.getSearchBar().setVisible(false);
    packTablePanel.setTablePreferencesKey(this.getClass().getName() + "_table");
    packTablePanel.getTable().setShowCheckedColumn(false);

    tabPane.addTab("采用该评标方法的分包", packTablePanel);

    this.subPackTableToolbar = new JFuncToolBar();

    addBtn1 = new SubaddButton(false);

    insertBtn1 = new SubinsertButton(false);

    delBtn1 = new SubdelButton(false);

    this.subPackTableToolbar.add(addBtn1);

    this.subPackTableToolbar.add(insertBtn1);

    this.subPackTableToolbar.add(delBtn1);

    packTablePanel.add(this.subPackTableToolbar, BorderLayout.SOUTH);

    addBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        ZcEbPack bean = new ZcEbPack();
        ZcUtil.addSub(packTablePanel, bean);
      }
    });

    insertBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        ZcEbPack bean = new ZcEbPack();
        ZcUtil.insertSub(packTablePanel, bean);
      }
    });

    delBtn1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        ZcUtil.deleteSub(packTablePanel, self);
      }
    });
    setTableProperty(packTablePanel.getTable(), parenPanel.valList, parenPanel.valMap);
    formulaSetMainPanel.treePanel.add(tabPane, BorderLayout.SOUTH);
    //默认是不可编辑的
    //    setSubButtonEnable(false);
  }

  public void setOldObject() {
    oldZcEbFormulaPack = (ZcEbFormulaPack) ObjectUtil.deepCopy(zcEbFormulaPack);
  }

  public boolean isDataChanged() {
    boolean b = !DigestUtil.digest(oldZcEbFormulaPack).equals(DigestUtil.digest(zcEbFormulaPack));
    zcEbFormulaPack.setDataChanged(b);
    return b;
  }

  private static List<ColumnBeanPropertyPair> packTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    packTableColumnInfo.add(new ColumnBeanPropertyPair("PACK_CODE", "packCode", "分包编号"));
    //    packTableColumnInfo.add(new ColumnBeanPropertyPair("PACK_NAME", "packName", "分包名称"));
    packTableColumnInfo.add(new ColumnBeanPropertyPair("PACK_DESC", "packDesc", "分包名称"));

  }

  public static TableModel convertPackToTableModel(List<ZcEbPack> packList) {

    BeanTableModel<ZcEbPack> tm = new BeanTableModel<ZcEbPack>() {

      private static final long serialVersionUID = 6888363838628062064L;

      @Override
      public boolean isCellEditable(int row, int column) {

        if (!this.isEditable()) {

          return false;

        }
        String columnId = this.getColumnIdentifier(column);

        if ("PACK_NAME".equals(columnId) || "PACK_DESC".equals(columnId)) {
          return false;
        }

        return true;

      }

    };

    tm.setOidFieldName("tempId");

    for (ZcEbPack data : packList) {

      data.setTempId(Guid.genID());

    }

    tm.setDataBean(packList, packTableColumnInfo);

    return tm;

  }

  private void setTableProperty(JTable table, List<AsVal> valList, Map<String, ZcEbPack> valMap) {

    if (zcEbFormulaPack.getPackList() == null) {
      List packList = new ArrayList();
      zcEbFormulaPack.setPackList(packList);
    }
    packTablePanel.setTableModel(convertPackToTableModel(zcEbFormulaPack.getPackList()));
    ZcUtil.translateColName(table, "ZC_EB_");
    SwingUtil.setTableCellEditor(table, "PACK_CODE", new AsValComboBoxCellEditor(false, valList) {
    });
    SwingUtil.setTableCellRenderer(table, "PACK_CODE", new AsMapCellRenderer(valMap));
    addPackTableLisenter(packTablePanel.getTable());
  }

  public void refreshPackTablePanelTableProperTy(List packList, List<AsVal> valList, Map<String, ZcEbPack> valMap) {
    SwingUtil.setTableCellEditor(packTablePanel.getTable(), "PACK_CODE", new AsValComboBoxCellEditor(false, valList));
    SwingUtil.setTableCellRenderer(packTablePanel.getTable(), "PACK_CODE", new AsMapCellRenderer(valMap));
  }

  private void addPackTableLisenter(final JPageableFixedTable table) {
    final BeanTableModel model = (BeanTableModel) table.getModel();
    table.getModel().addTableModelListener(new TableModelListener() {
      @Override
      public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
          if (e.getColumn() >= 0
            && ("".equals(model.getColumnIdentifier(e.getColumn())) || "PACK_CODE".equals(model.getColumnIdentifier(e.getColumn())))) {
            int k = table.getSelectedRow();
            if (k < 0)
              return;
            ZcEbPack pack = (ZcEbPack) model.getBean(table.convertRowIndexToModel(k));
            if (pack.getPackCode() != null) {
              if (parenPanel.packMap.containsKey(pack.getPackCode())) {
                String packDesc = parenPanel.packMap.get(pack.getPackCode()).getPackDesc();
                if (packDesc != null) {
                  pack.setPackDesc(packDesc);
                }
                pack.setZcEbFormula(zcEbFormula);
              } else {
                pack.setPackCode(null);
                pack.setPackDesc(null);
              }
            }
          }
        }
      }
    });
  }

  public boolean checkBeforeSave() {
    isDataChanged();

    boolean b = formulaSetMainPanel.getZcEbFormulaEditPanel().checkBeforeSave();
    if (b) {
      if (zcEbFormulaPack.getPackList().size() == 0) {

        JOptionPane.showMessageDialog(this, "采用该评标方法的分包没有选择！", "提示", JOptionPane.ERROR_MESSAGE);
        return false;
      } else {
        /**
         * 校验评标方法关联的标段和标段划分的标段的一致性。
         */
        boolean isDouble = false;
        List formulaPackList = zcEbFormulaPack.getPackList();
        for (int i = 0; i < formulaPackList.size(); i++) {
          ZcEbPack bean = (ZcEbPack) formulaPackList.get(i);

          if (bean.getPackCode() == null) {
            JOptionPane.showMessageDialog(this, "采用该评标方法的分包没有选择！", "提示", JOptionPane.ERROR_MESSAGE);
            return false;
          } else {
            for (int j = i + 1; j < formulaPackList.size(); j++) {
              ZcEbPack bean2 = (ZcEbPack) formulaPackList.get(j);
              if (bean.getPackCode().equals(bean2.getPackCode())) {
                isDouble = true;
                break;
              }
            }
            if (isDouble) {
              JOptionPane.showMessageDialog(this, "标段重复！", "提示", JOptionPane.ERROR_MESSAGE);
              return false;
            }
          }
        }
        return true;
      }
    } else {
      return b;
    }
  }

  public void setSubButtonEnable(boolean isVisiable) {
    BeanTableModel model = (BeanTableModel) packTablePanel.getTable().getModel();
    model.setEditable(isVisiable);
    addBtn1.setVisible(isVisiable);
    insertBtn1.setVisible(isVisiable);
    delBtn1.setVisible(isVisiable);
  }

  public void doEidt(boolean isVisiable) {
    formulaSetMainPanel.doEidt(isVisiable);
    setSubButtonEnable(isVisiable);

  }
}
