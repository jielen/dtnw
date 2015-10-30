package com.ufgov.zc.client.zc.ztb.docautogenerate.model;

import com.ufgov.zc.client.zc.ztb.table.entity.table.XmlTable;
import com.ufgov.zc.client.zc.ztb.table.entity.table.XmlTableCell;
import com.ufgov.zc.client.zc.ztb.table.entity.table.XmlTableColumn;
import com.ufgov.zc.client.zc.ztb.table.entity.table.XmlTableRow;
import com.ufgov.zc.client.zc.ztb.table.service.TableService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriceWordTableModel {
  private Map root = new HashMap();

  private String tableType;

  private String tableName;

  private String tableXmlPath;

  public Map getRoot() {
    return root;
  }

  public void setRoot(Map root) {
    this.root = root;
  }

  public String getTableType() {
    return tableType;
  }

  public void setTableType(String tableType) {
    this.tableType = tableType;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getTableXmlPath() {
    return tableXmlPath;
  }

  public void setTableXmlPath(String tableXmlPath) {
    this.tableXmlPath = tableXmlPath;
  }

  public PriceWordTableModel() {
  }

  public PriceWordTableModel(String tableType, String tableName, String tableXmlPath) {
    setTableType(tableType);
    setTableName(tableName);
    setTableXmlPath(tableXmlPath);
  }

  public void genWordTableModel() {
    //设置表名
    root.put("tableName", tableName);
    //构建xmlTable实例，以获得数据用于建立word表格
    XmlTable xt = new TableService().createXmlTable(tableXmlPath);
    //设置表头
    List tableTitleList = new ArrayList(); //list中每个对象为一行，第一个对象为父表头，从第二行开始，为子表头
    Map titleLevelMap = new HashMap();
    List titleLevelList1 = new ArrayList(); //list中为每一行表头的项
    List<XmlTableColumn> columns = xt.getColumns();
    for (int i = 0; i < columns.size(); i++) {
      if (columns.get(i).getColumnId().toUpperCase().equals("TABLE.ROWNUM")) {
        continue;
      }
      Map gridMessage = new HashMap();
      gridMessage.put("gridSize", "0");
      gridMessage.put("gridContent", columns.get(i).getColumnId());
      titleLevelList1.add(gridMessage); //给父表头赋值
    }
    titleLevelMap.put("titleLevel", titleLevelList1);
    tableTitleList.add(titleLevelMap); //将父表头放入
    root.put("tableTitle", tableTitleList); // 将表头放入模型
    root.put("tableColSize", String.valueOf(titleLevelList1.size()));//将表的最大列数放入模型
    //设置表内容
    List tableContent = new ArrayList();
    List<XmlTableRow> rows = xt.getRows();
    for (int i = 0; i < rows.size(); i++) {
      List rowItemList = new ArrayList();
      List<XmlTableCell> cells = rows.get(i).getCells();
      for (int j = 1; j < cells.size(); j++) {
        rowItemList.add(cells.get(j).getValue());
      }
      Map rowMap = new HashMap();
      rowMap.put("rowItemList", rowItemList);
      tableContent.add(rowMap);
    }
    root.put("tableContent", tableContent);
  }
}
