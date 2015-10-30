package com.ufgov.zc.client.zc.zcproend;

import java.math.BigDecimal;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.common.budget.model.VwBudgetGp;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;
import com.ufgov.zc.common.zc.model.ZcYearPlan;

public class ZcProOrYearBudgetHandler implements IForeignEntityHandler {

  private String columNames[];

  private JTablePanel biTablePanel;

  private AbstractMainSubEditPanel edit;

  private ListCursor listCursor;

  private ElementConditionDto getDto;

  public ZcProOrYearBudgetHandler(String columNames[], JTablePanel biTablePanel, AbstractMainSubEditPanel edit, ListCursor listCursor,
    ElementConditionDto getDto) {

    this.columNames = columNames;
    this.biTablePanel = biTablePanel;
    this.edit = edit;
    this.listCursor = listCursor;
    this.getDto = getDto;

  }

  public void excute(List selectedDatas) {

    if (!edit.budgetExcuce(selectedDatas)) {
      return;
    }

    JTable table = biTablePanel.getTable();

    BeanTableModel model = (BeanTableModel) table.getModel();

    int k = table.getSelectedRow();

    if (k < 0)

      return;

    int k2 = table.convertRowIndexToModel(k);

    if (selectedDatas.size() > 0) {

      VwBudgetGp gp = (VwBudgetGp) selectedDatas.get(0);
      ZcYearPlan make = (ZcYearPlan) listCursor.getCurrentObject();
      ZcPProMitemBi bi = (ZcPProMitemBi) model.getBean(k2);
      String sumId = "";
      if (bi.getZcBiNo() != null) {
        sumId = bi.getZcBiNo();
      }

      bi.setZcBiNo(gp.getSumId() + "");
      bi.setZcBiDoSum(gp.getCanuseMoney());
      bi.setZcBiSum(gp.getBudgetMoney());
      // 预算单位
      if (gp.getEnCode() != null) {
        bi.setCoCode(gp.getEnCode());
        bi.setCoName(gp.getEnName());
      }
      // 资金性质
      if (gp.getMkCode() != null) {
        bi.setFundCode(gp.getMkCode());
        bi.setFundName(gp.getMkName());
      }
      // 功能分类
      if (gp.getBsCode() != null) {
        bi.setbAccCode(gp.getBsCode());
        bi.setbAccName(gp.getBsName());
      }
      // 项目类别
      if (gp.getBiCode() != null) {
        bi.setProjectTypeCode(gp.getBiCode());
        bi.setProjectTypeName(gp.getBiName());
      }
      // 付款方式
      if (gp.getPkCode() != null) {
        bi.setPaytypeCode(gp.getPkCode());
        bi.setPaytypeName(gp.getPkName());
      }
      // 指标来源
      if (gp.getBlCode() != null) {
        bi.setOriginCode(gp.getBlCode());
        bi.setOriginName(gp.getBlName());
      }
      // 业务处室
      if (gp.getMbId() != null) {
        bi.setOrgCode(gp.getMbId());
        bi.setOrgName(gp.getMbName());
      }
      // 年度
      bi.setNd(gp.getSetYear() + "");
      // 文号
      if (gp.getFileCode() != null) {
        bi.setSenddocCode(gp.getFileCode());
        bi.setSenddocName(gp.getFileName());
      }

      // 指标流向
      if (gp.getBtCode() != null) {
        bi.setBiTargetCode(gp.getBtCode());
      }
      // 预算项目
      if (gp.getBisCode() != null) {
        bi.setProjectCode(gp.getBisCode());
      }

      if (getDto.getZcText3() != null && !"".equals(getDto.getZcText3())) {
        getDto
          .setZcText3(getDto.getZcText3().replaceAll(",'" + sumId + "'", "").replaceAll("'" + sumId + "',", "").replaceAll("'" + sumId + "'", ""));
      }
      if (getDto.getZcText3() == null || "".equals(getDto.getZcText3())) {
        getDto.setZcText3("'" + gp.getSumId() + "'");
      } else {
        getDto.setZcText3(getDto.getZcText3() + ",'" + gp.getSumId() + "'");
      }
      model.fireTableDataChanged();
    }

  }

  public void afterClear() {
    if (!edit.budgetAfterClear()) {
      return;
    }

    JTable table = biTablePanel.getTable();

    BeanTableModel model = (BeanTableModel) table.getModel();

    int k = table.getSelectedRow();

    if (k < 0)

      return;

    int k2 = table.convertRowIndexToModel(k);

    ZcYearPlan make = (ZcYearPlan) listCursor.getCurrentObject();
    ZcPProMitemBi bi = (ZcPProMitemBi) model.getBean(k2);
    if (bi.getZcBiNo() != null && !"".equals(bi.getZcBiNo())) {
      getDto.setZcText3(getDto.getZcText3().replaceAll(",'" + bi.getZcBiNo() + "'", "").replaceAll("'" + bi.getZcBiNo() + "',", "")
        .replaceAll("'" + bi.getZcBiNo() + "'", ""));
    }
    bi.setZcBiNo("");
    bi.setZcBiDoSum(null);
    bi.setZcBiSum(null);
    // 预算单位
    bi.setCoCode("");
    bi.setCoName("");
    // 资金性质
    bi.setFundCode("A");
    bi.setFundName("");
    // 功能分类
    bi.setbAccCode("");
    bi.setbAccName("");
    // 项目类别
    bi.setProjectTypeCode("");
    bi.setProjectTypeName("");
    // 付款方式
    bi.setPaytypeCode("A");
    bi.setPaytypeName("");
    // 指标来源
    bi.setOriginCode("4");
    bi.setOriginName("");
    // 年度
    bi.setNd("");
    // 文号
    bi.setSenddocCode("");
    bi.setSenddocName("");
    // 业务处室
    bi.setOrgCode("");
    bi.setOrgName("");

    edit.setEditingObject(make);

  }

  public TableModel createTableModel(List showDatas) {

    Object data[][] = new Object[showDatas.size()][columNames.length];

    for (int i = 0; i < showDatas.size(); i++) {

      VwBudgetGp rowData = (VwBudgetGp) showDatas.get(i);

      int col = 0;

      data[i][col++] = rowData.getSumId();

      data[i][col++] = rowData.getCanuseMoney();

      data[i][col++] = rowData.getMkName();

      data[i][col++] = rowData.getBpName();

      data[i][col++] = rowData.getBlName();

      data[i][col++] = rowData.getMbName();

      data[i][col++] = rowData.getSmName();

      data[i][col++] = rowData.getFileName();

      data[i][col++] = rowData.getBsName();

    }

    MyTableModel model = new MyTableModel(data, columNames) {

      public boolean isCellEditable(int row, int colum) {

        return false;

      }

      public Class getColumnClass(int column) {

        if ((column >= 0) && (column < getColumnCount()) && this.getRowCount() > 0) {

          for (int row = 0; row < this.getRowCount(); row++) {

            if (getValueAt(row, column) != null) {

              return getValueAt(row, column).getClass();

            }

          }

        }

        return Object.class;

      }

    };

    return model;

  }

  public boolean isMultipleSelect() {

    return false;

  }

}
