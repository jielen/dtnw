package com.ufgov.zc.client.zc.zcpprobal;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.TableModel;

import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.zc.model.ZcPProBal;
import com.ufgov.zc.common.zc.model.ZcPProReturnBi;

public class ZcPProBalReturnBiPanel extends JPanel {

  protected List<AbstractFieldEditor> fieldEditors;

  protected JPanel fieldEditorPanel = new JPanel();

  private int colCount = 3;

  protected final JPanel workPanel = new JPanel();

  protected ZcPProBal zcPProBal;

  private JTablePanel biTablePanel = new JTablePanel();

  public ZcPProBalReturnBiPanel(ZcPProBal zcPProBal) {
    this.zcPProBal = zcPProBal;
    init();
  }

  private void init() {

    this.setLayout(new BorderLayout());
    this.add(workPanel, BorderLayout.CENTER);
    initFieldEditorPanel();
    workPanel.setLayout(new BorderLayout());
    workPanel.add(fieldEditorPanel, BorderLayout.NORTH);
    workPanel.add(biTablePanel, BorderLayout.CENTER);

  }

  public List<AbstractFieldEditor> createFieldEditors() {
    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();
    TextFieldEditor zcCoBankName = new TextFieldEditor("采购单位银行名称", "zcCoBankName");
    editorList.add(zcCoBankName);
    TextFieldEditor zcCoAccName = new TextFieldEditor("采购单位银行账户", "zcCoAccName");
    editorList.add(zcCoAccName);
    TextFieldEditor zcCoAccCode = new TextFieldEditor("采购单位银行账号", "zcCoAccCode");
    editorList.add(zcCoAccCode);
    return editorList;
  }

  protected void initFieldEditorPanel() {

    fieldEditors = createFieldEditors();

    int row = 0;

    int col = 0;

    fieldEditorPanel.setLayout(new GridBagLayout());

    for (int i = 0; i < fieldEditors.size(); i++) {

      AbstractFieldEditor comp = fieldEditors.get(i);

      if (comp.isVisible()) {

        JLabel label = new JLabel(comp.getName());

        comp.setPreferredSize(new Dimension(150, 23));

        fieldEditorPanel.add(label, new GridBagConstraints(col, row, 1,

        1, 1.0, 1.0, GridBagConstraints.EAST,

        GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));

        fieldEditorPanel.add(comp, new GridBagConstraints(col + 1, row,

        1, 1, 1.0, 1.0, GridBagConstraints.WEST,

        GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5),

        0, 0));

        if (col == colCount * 2 - 2) {

          row++;

          col = 0;

        } else {

          col += 2;

        }

      }

    }

  }

  public void setEditingObject(ZcPProBal editingObject) {

    this.zcPProBal = editingObject;

    updateFieldEditors();

  }

  protected void updateFieldEditors() {

    for (AbstractFieldEditor editor : fieldEditors) {

      editor.setEditObject(zcPProBal);

    }
  }

  public void refreshSubData(List biList) {
    biTablePanel.init();
    biTablePanel.getSearchBar().setVisible(false);
    biTablePanel.setTablePreferencesKey(this.getClass().getName() + "_table");
    biTablePanel.getTable().setShowCheckedColumn(true);
    biTablePanel.getTable().getTableRowHeader().setPreferredSize(new Dimension(50, 0));
    biTablePanel.setTableModel(convertSubBiTableData(biList));
    ZcUtil.translateColName(biTablePanel.getTable(), biInfo);
    biTablePanel.repaint();
  }

  public static TableModel convertSubBiTableData(List<ZcPProReturnBi> biList) {

    BeanTableModel<ZcPProReturnBi> tm = new BeanTableModel<ZcPProReturnBi>() {

      private static final long serialVersionUID = 6888363838628062064L;

      @Override
      public boolean isCellEditable(int row, int col) {
        return false;
      }

      @Override
      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        super.setValueAt(aValue, rowIndex, columnIndex);

      }

    };

    tm.setOidFieldName("tempId");

    for (Object o : biList) {

      ((ZcPProReturnBi) o).setTempId(Guid.genID());

    }
    tm.setDataBean(biList, biInfo);

    return tm;

  }

  private static List<ColumnBeanPropertyPair> biInfo = new ArrayList<ColumnBeanPropertyPair>();

  static {

    /*
     * 指标编号
     */
    biInfo.add(new ColumnBeanPropertyPair("ZC_BI_NO", "zcBiNo", "指标编号"));

    /*
     * 指标文号
     */
    biInfo.add(new ColumnBeanPropertyPair("SEND_DOC_NAME", "sendDocName", "指标文号"));

    /*

     * 返回余额

     */

    biInfo.add(new ColumnBeanPropertyPair("ZC_BI_BAL", "zcBiBal", "返回余额"));

  }

  public void setFieldEditorsEditable(boolean isEditable) {
    for (AbstractFieldEditor fd : this.fieldEditors) {
      fd.setEnabled(isEditable);

    }
  }
}
