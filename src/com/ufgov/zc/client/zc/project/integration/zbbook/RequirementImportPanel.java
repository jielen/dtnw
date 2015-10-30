package com.ufgov.zc.client.zc.project.integration.zbbook;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import com.ufgov.zc.client.zc.ztb.component.MainPanel;
import com.ufgov.zc.client.zc.ztb.component.ZTBButton;
import com.ufgov.zc.client.zc.ztb.model.SmartTreeNode;
import com.ufgov.zc.client.zc.ztb.service.RequirementImportService;
import com.ufgov.zc.client.zc.ztb.table.builder.GridColumnBuilder;
import com.ufgov.zc.client.zc.ztb.table.entity.GridColumn;
import com.ufgov.zc.client.zc.ztb.table.tablecomponent.JTableColumnModel;
import com.ufgov.zc.client.zc.ztb.table.tablecomponent.JTableModel;
import com.ufgov.zc.client.zc.ztb.table.tablecomponent.JTableRowSorter;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.client.zc.ztb.util.PubFunction;

public class RequirementImportPanel extends JPanel {
  private static final long serialVersionUID = -3673893045375937678L;

  private RequirementImportService dbImportRequirementService;

  private JTableModel tableModel;

  private com.ufgov.zc.client.zc.ztb.table.tablecomponent.JTable table;

  private MainPanel mainPanel;

  private JDialog dialog;

  public RequirementImportPanel(MainPanel mainPanel, JDialog dialog) throws Exception {
    this.mainPanel = mainPanel;
    this.dialog = dialog;
    dbImportRequirementService = new RequirementImportService();
    initPanel();
  }

  public void initPanel() throws Exception {
    this.setLayout(new BorderLayout());
    ZTBButton impBtn = new ZTBButton("dbImpReq");
    JToolBar toolBar = new JToolBar();
    toolBar.setFloatable(false);
    impBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (table.getCheckSelectRows().length == 0) {
          GV.showMessageDialog(null, GV.getOperateMsg("dbImportRequirement.nodata", null));
          return;
        }
        StringBuffer content = new StringBuffer("");
        int index = 1;
        for (int i : table.getCheckSelectRows()) {
          content.append("\n[" + index + "]  " + tableModel.get(i).get("FILE_NAME") + "(" + tableModel.get(i).get("FILE_ID") + ");");
          index++;
        }
        if (GV.showConfirmDialog(null, GV.getOperateMsg("dbImportRequirement.confirm", content.toString()), JOptionPane.YES_OPTION) == JOptionPane.NO_OPTION) {
          return;
        }
        dialog.dispose();
        List<String> checkedCodes = new ArrayList<String>();
        for (int i : table.getCheckSelectRows()) {
          checkedCodes.add(tableModel.get(i).get("FILE_ID"));
        }
        try {
          SmartTreeNode currentNode = mainPanel.getSingleSeletionTree().getCurrentNode();
          String filePath = currentNode.getNodesFullPath() + currentNode.getFileExtension();
          dbImportRequirementService.updateBagDetails(checkedCodes, filePath, mainPanel);
          GV.showMessageDialog(null, GV.getOperateMsg("dbImportRequirement.success", content.toString()));
        } catch (Exception e1) {
          GV.showMessageDialog(null, GV.getOperateMsg("dbImportRequirement.failure", e1.getMessage()));
          e1.printStackTrace();
          return;
        }
      }
    });
    toolBar.add(impBtn);
    this.add(toolBar, BorderLayout.NORTH);
    JScrollPane scrollpane = new JScrollPane(createTable());
    this.add(scrollpane, BorderLayout.CENTER);
  }

  public JTable createTable() throws Exception {
    table = new com.ufgov.zc.client.zc.ztb.table.tablecomponent.JTable();
    List<Map<String, String>> columnDataList = buildData();
    List<GridColumn> gridColumns = GridColumnBuilder.buildGridColumn(columnDataList);
    JTableColumnModel tableColumnModel = new JTableColumnModel(table, gridColumns);
    tableModel = new JTableModel(tableColumnModel, table);
    table.setModel(tableModel);
    table.setColumnModel(tableColumnModel);
    JTableRowSorter<JTableModel> sorter = new JTableRowSorter<JTableModel>(tableModel);
    sorter.setComparators(tableColumnModel);
    table.setRowSorter(sorter);
    List<Map<String, String>> rowset = buildTableRowData();
    tableModel.add(rowset);
    return table;
  }

  /**
   * 创建数据类型
   *
   * @return
   * @throws Exception
   */
  private List<Map<String, String>> buildTableRowData() throws Exception {

    String projCode = "";
    SmartTreeNode currNode = mainPanel.getOpenedLeavesNode();
    if (currNode == null || mainPanel.getWordPane() == null) {
      JOptionPane.showMessageDialog(null, GV.getOperateMsg("noMatchingDocument", null));
    }
    String nodeType = currNode.getNodeType();
    if (GV.NODE_TYPE_DOC.equals(nodeType)) {
      //      SmartTreeNode packNode = PubFunction.getPackNode(currNode);
      SmartTreeNode projNode = PubFunction.getNeedingNodeInParent(currNode, GV.NODE_TYPE_PROJECT);
      if (projNode.getNodeCode() == null) {
        //        String tipMeg = GV.getOperateMsg("没有找到对应的项目编号。", null);
        throw new Exception("没有找到对应的项目编号");
      } else {
        projCode = projNode.getNodeCode();
      }
    }
    return dbImportRequirementService.getReqFilesList(projCode);

  }

  /**
   * 表格表头列属性
   *
   * @return List<Map<String,String>>
   * @throws
   */
  public List<Map<String, String>> buildData() {
    List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
    dataList.add(buildRow("TABLE.CHECKBOX", "", "CHECKBOX", "30", "C", "", "CHECKBOX", "", "", "N"));
    //dataList.add(buildRow("TABLE.ROWNUM", "", "序号", "30", "C", "NUM", "TEXT", "", "", "Y"));
    dataList.add(buildRow("FILE_NAME", "", "文件名称", "300", "C", "TEXT", "TEXT", "", "", "Y"));
    dataList.add(buildRow("DETAILS_NAME", "", "明细需求名称", "300", "C", "TEXT", "TEXT", "2", "", "Y"));
    dataList.add(buildRow("FILE_ID", "", "文件ID", "155", "C", "TEXT", "TEXT", "", "", "Y"));
    return dataList;
  }

  private Map<String, String> buildRow(String columnId, String groupId, String caption, String width, String align, String dataType,
    String editorType, String decLen, String isThousandsSeparator, String isForceReadonly) {
    Map<String, String> row = new HashMap<String, String>();
    row.put("COLUMN_ID", columnId);//字段名
    row.put("GROUP_ID", groupId);
    row.put("CAPTION", caption);//列名（字段名的翻译）
    row.put("WIDTH", width);//列宽
    row.put("ALIGN", align);//水平对齐/L/C/R
    row.put("DATA_TYPE", dataType);//数据类型/NUM/TEXT/DATE
    row.put("EDITOR_TYPE", editorType);//编辑框类型
    row.put("DEC_LEN", decLen);//小数位
    row.put("IS_THOUSANDS_SEPARATOR", isThousandsSeparator);
    row.put("IS_FORCE_READONLY", isForceReadonly);
    return row;
  }
}