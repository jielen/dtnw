package com.ufgov.zc.client.zc.ztb.docautogenerate.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ufgov.zc.client.util.StringUtil;
import com.ufgov.zc.common.zc.model.ZcEbFormulaItem;

public class FormulaScoreItemWordGenaral {
  private final Integer colCount = new Integer(5);

  private List scoreItemList;

  private String tableName;

  public FormulaScoreItemWordGenaral(List scoreItemList, String tableName) {
    this.scoreItemList = scoreItemList;
    this.tableName = tableName;
  }

  /*
   * 获取技术性评标方法表头
   * */
  protected List getScoreItemTableTitle() {
    List tableTitleList = new ArrayList();

    tableTitleList.add(getSorceItemTabFirstRowTitle());
    tableTitleList.add(getSorceItemTabSecondRowTitle()); //将子表头放入

    return tableTitleList;
  }

  private Map getSorceItemTabSecondRowTitle() {
    List titleLevelList2 = new ArrayList(); //list中为每一行表头的项
    Map titleLevelMap = new HashMap();

    titleLevelList2.add(getGridMessageMap("0", "NO")); //给子表头赋值，NO代表没有子项

    titleLevelList2.add(getGridMessageMap("0", "100")); //给子表头赋值

    titleLevelList2.add(getGridMessageMap("0", "分项")); //给子表头赋值

    titleLevelList2.add(getGridMessageMap("0", "NO")); //给子表头赋值，NO代表没有子项

    titleLevelList2.add(getGridMessageMap("0", "NO")); //给子表头赋值，NO代表没有子项

    titleLevelMap.put("titleLevel", titleLevelList2);

    return titleLevelMap;
  }

  private Map getGridMessageMap(String gridSize, String gridContent) {
    Map gridMessage = new HashMap();
    gridMessage.put("gridSize", gridSize);
    gridMessage.put("gridContent", gridContent);
    return gridMessage;
  }

  private Map getSorceItemTabFirstRowTitle() {
    List titleLevelList1 = new ArrayList();
    Map titleLevelMap = new HashMap();

    titleLevelList1.add(getGridMessageMap("0", "项别")); //给父表头赋值

    titleLevelList1.add(getGridMessageMap("2", "总分值"));//给父表头赋值

    titleLevelList1.add(getGridMessageMap("0", "评审要素"));//给父表头赋值

    titleLevelList1.add(getGridMessageMap("0", "备注"));//给父表头赋值

    titleLevelMap.put("titleLevel", titleLevelList1);
    return titleLevelMap;
  }

  public Map genWordTableModel() {
    Map root = new HashMap();
    //设置表名
    root.put("tableName", tableName);
    //设置表头
    root.put("tableTitle", getScoreItemTableTitle()); // 将表头放入模型

    root.put("tableColSize", colCount);//将表的最大列数放入模型

    //设置表内容
    List tableContent = new ArrayList();

    for (int i = 0; i < scoreItemList.size(); i++) {
      ZcEbFormulaItem zcEbFormulaItem = (ZcEbFormulaItem) scoreItemList.get(i);
      if ("SC".equals(zcEbFormulaItem.getParentItemCode())) {
        List childItemList = zcEbFormulaItem.getChildItemList();
        if (childItemList.size() > 0) {
          tableContent.addAll(getScoreRowList(zcEbFormulaItem, childItemList));
        } else {
          Map rowMap = new HashMap();
          rowMap.put("rowItemList", getScoreRowList(zcEbFormulaItem));
          tableContent.add(rowMap);
        }
      }
    }
    root.put("tableContent", tableContent);
    return root;
  }

  private List getScoreRowList(ZcEbFormulaItem zcEbFormulaItem) {
    List rowItemList = new ArrayList();
    String str = zcEbFormulaItem.getName();
    if(str != null)
    rowItemList.add(StringUtil.freeMarkFillWordChar(str));
    str = String.valueOf(zcEbFormulaItem.getStandardScore());
    if(str != null)
    rowItemList.add(StringUtil.freeMarkFillWordChar(str));
    rowItemList.add("NULL");
    str = zcEbFormulaItem.getDescription();
    if(str != null)
    rowItemList.add(StringUtil.freeMarkFillWordChar(str));
    if (zcEbFormulaItem.getRemark() != null && !"".equals(zcEbFormulaItem.getRemark()))
      rowItemList.add(StringUtil.freeMarkFillWordChar(zcEbFormulaItem.getRemark()));
    else
      rowItemList.add("NULL");
    return rowItemList;
  }

  private List getScoreRowList(ZcEbFormulaItem zcEbFormulaItem, List childItemList) {
    List tableContent = new ArrayList();
    String str;
    for (int j = 0; j < childItemList.size(); j++) {
      ZcEbFormulaItem childItem = (ZcEbFormulaItem) childItemList.get(j);
      List rowItemList = new ArrayList();
      if (j == 0) {
    	str = zcEbFormulaItem.getName();
    	if(str != null)
        rowItemList.add(StringUtil.freeMarkFillWordChar(str));
    	str = String.valueOf(zcEbFormulaItem.getStandardScore());
    	if(str != null)
        rowItemList.add(StringUtil.freeMarkFillWordChar(str));
      } else {
        rowItemList.add("NO");
        rowItemList.add("NO");
      }
      str = String.valueOf(childItem.getStandardScore());
      if(str != null)
      rowItemList.add(StringUtil.freeMarkFillWordChar(str));
      str = childItem.getDescription();
      if(str != null)
      rowItemList.add(StringUtil.freeMarkFillWordChar(str));
      if (j == 0) {
        if (zcEbFormulaItem.getRemark() != null && !"".equals(zcEbFormulaItem.getRemark()))
          rowItemList.add(StringUtil.freeMarkFillWordChar(zcEbFormulaItem.getRemark()));
        else
          rowItemList.add("NULL");
      } else {
        rowItemList.add("NO");
      }

      Map rowMap = new HashMap();
      rowMap.put("rowItemList", rowItemList);
      tableContent.add(rowMap);
    }
    return tableContent;
  }
}
