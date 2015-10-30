package com.ufgov.zc.client.zc.project.integration.zbbook;

import com.ufgov.zc.client.zc.ztb.component.MainPanel;
import com.ufgov.zc.client.zc.ztb.component.ZTBButton;
import com.ufgov.zc.client.zc.ztb.service.ServerProjectService;
import com.ufgov.zc.client.zc.ztb.table.builder.GridColumnBuilder;
import com.ufgov.zc.client.zc.ztb.table.entity.GridColumn;
import com.ufgov.zc.client.zc.ztb.table.tablecomponent.JTableColumnModel;
import com.ufgov.zc.client.zc.ztb.table.tablecomponent.JTableModel;
import com.ufgov.zc.client.zc.ztb.table.tablecomponent.JTableRowSorter;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.common.zc.model.ZcEbProj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectImportPanel extends JPanel {
  private static final long serialVersionUID = -3673893045375937678L;

  private ServerProjectService serverProjectService;

  private JTableModel tableModel;

  private JDialog parentDialog = null;
  private com.ufgov.zc.client.zc.ztb.table.tablecomponent.JTable table;

  private MainPanel mainPanel;

  private JLabel tipLabel = new JLabel();

  private int projListCount = 0;

  public ProjectImportPanel(MainPanel mainPanel, JDialog parentDialog) throws Exception {
    this.mainPanel = mainPanel;
    this.parentDialog = parentDialog;
    serverProjectService = new ServerProjectService();
    initPanel();
  }

  public void initPanel() {
    this.setLayout(new BorderLayout());
    ZTBButton impBtn = new ZTBButton("dbimport");
    JToolBar toolBar = new JToolBar();
    toolBar.setFloatable(false);
    impBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (table.getCheckSelectRows().length == 0) {
          GV.showMessageDialog(null, GV.getOperateMsg("dbimportProject.nodata", null));
          return;
        }
        StringBuffer content = new StringBuffer("");
        int index = 1;
        for (int i : table.getCheckSelectRows()) {
          content.append("\n[" + index + "]  " + tableModel.get(i).get("PROJ_NAME") + "(" + tableModel.get(i).get("PROJ_CODE") + ");");
          index++;
        }
        JCheckBox cb = new JCheckBox("成功后自动关闭项目列表窗口.");
        cb.setSelected(true);
        Object[] message = { GV.getOperateMsg("dbimportProject.confirm", content.toString()), "\r\n", cb };
        if (JOptionPane.showConfirmDialog(null, message, "", JOptionPane.YES_OPTION) == JOptionPane.NO_OPTION) {
          return;
        }
        List<String> checkedProCodes = new ArrayList<String>();
        for (int i : table.getCheckSelectRows()) {
          checkedProCodes.add(tableModel.get(i).get("PROJ_CODE"));
        }
        boolean exist = serverProjectService.checkExistProject(checkedProCodes);
        if (exist) {
          if (GV.showConfirmDialog(null, "您所选项目已经存在于本地系统内，是否覆盖？", JOptionPane.YES_OPTION) == JOptionPane.NO_OPTION) {
            return;
          }
        }
        try {
          serverProjectService.importProjectFromServer(checkedProCodes);
          mainPanel.getSplitPane().setDividerLocation(198);
          mainPanel.loadRightInitInfoPanel(checkedProCodes.get(0));
          if (cb.isSelected()) {
            parentDialog.dispose();
          }
          mainPanel.refreshLeftFilesTreePanel();
          mainPanel.getSplitPane().setDividerLocation(200);
        } catch (Exception e1) {
          GV.showMessageDialog(null, "项目导入出错，原因：" + e1.getMessage());
          e1.printStackTrace();
        }
      }
    });
    toolBar.add(impBtn);
    this.add(toolBar, BorderLayout.NORTH);
    try {
      JScrollPane scrollpane = new JScrollPane(this.createTable());
      this.add(scrollpane, BorderLayout.CENTER);
      toShowTipLabel(this);
    } catch (Exception e1) {
      GV.showMessageDialog(this.mainPanel, "查找项目列表时出错，原因：" + e1.getMessage());
    }
  }

  private void toShowTipLabel(JPanel thiz) {
    tipLabel.setPreferredSize(new Dimension(700, 25));
    String info = null;
    if (projListCount <= 0) {
      info = "温馨提示：当前没有需要制作电子招标书的项目...";
    } else {
      info = "温馨提示：当前查询到【" + projListCount + "】个项目可以制作电子招标书...";
    }
    tipLabel.setText("<html><a><font size='3' color='red'>" + info + "</font></a></html>");
    this.remove(tipLabel);
    this.add(tipLabel, BorderLayout.SOUTH);
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
    projListCount = rowset.size();
    tableModel.add(rowset);
    return table;
  }

  private List<Map<String, String>> buildTableRowData() throws Exception {
    return serverProjectService.findProjectsList();
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
    dataList.add(buildRow("PROJ_CODE", "", "项目编号", "80", "L", "TEXT", "TEXT", "", "", "Y"));
    dataList.add(buildRow("PROJ_NAME", "", "项目名称", "280", "L", "TEXT", "TEXT", "", "", "Y"));
    dataList.add(buildRow("PROJ_SUM", "", "预算(元)", "90", "R", "NUM", "TEXT", "2", "", "Y"));
    dataList.add(buildRow("PUR_TYPE", "", "采购类型", "80", "L", "TEXT", "TEXT", "", "", "Y"));
    dataList.add(buildRow("MANAGER", "", "负责人", "50", "L", "TEXT", "TEXT", "", "", "Y"));
    dataList.add(buildRow("PROJ_DATE", "", "立项时间", "80", "L", "DATE", "TEXT", "", "", "Y"));
    //dataList.add(buildRow("PHONE", "", "电话", "50", "L", "TEXT", "TEXT", "", "", "Y"));
    //dataList.add(buildRow("EMAIL", "", "邮件", "100", "L", "TEXT", "TEXT", "", "", "Y"));
    //dataList.add(buildRow("FAX", "", "传真", "80", "L", "TEXT", "TEXT", "", "", "Y"));
    dataList.add(buildRow("IS_HAVE_ZBFILE", "", "电子标书", "80", "L", "TEXT", "TEXT", "", "", "Y"));
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

  public static void main(String[] args) throws Exception {
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } catch (InstantiationException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    JFrame frame = new JFrame(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle(GV.DB_IMPORT_PROJECT_LIST);
    //frame.add(new ProjectImportPanel(new ZBPanel(frame,null), new JDialog(),null), BorderLayout.CENTER);
    frame.pack();
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.setVisible(true);
  }
}