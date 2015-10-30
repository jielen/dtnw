package com.ufgov.zc.client.zc.ztb.docautogenerate.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ufgov.zc.client.util.StringUtil;
import com.ufgov.zc.common.zc.model.ZcEbFormulaItem;

public class FormulaComplicationWordGenaral {
  private final Integer colCount = new Integer(3);

  private List complicationItemList;

  private String tableName;

  public FormulaComplicationWordGenaral(List complicationItemList, String tableName) {
    this.complicationItemList = complicationItemList;
    this.tableName = tableName;
  }

  private Map getComplicationTabTitle() {
    List titleLevelList1 = new ArrayList();
    Map titleLevelMap = new HashMap();

    titleLevelList1.add(getGridMessageMap("0", "项别")); //给父表头赋值

    titleLevelList1.add(getGridMessageMap("0", "评审要素"));//给父表头赋值

    titleLevelList1.add(getGridMessageMap("0", "备注"));//给父表头赋值

    titleLevelMap.put("titleLevel", titleLevelList1);
    return titleLevelMap;
  }

  protected List getComplicationItemTableTitle() {
    List tableTitleList = new ArrayList();

    tableTitleList.add(getComplicationTabTitle());

    return tableTitleList;
  }

  public Map genWordTableModel() {
    //设置表名
    HashMap wordMap = new HashMap();
    wordMap.put("tableName", tableName);
    wordMap.put("tableTitle", getComplicationItemTableTitle()); // 将表头放入模型
    wordMap.put("tableColSize", colCount);//将表的最大列数放入模型
    //设置表内容
    List tableContent = new ArrayList();

    for (int i = 0; i < complicationItemList.size(); i++) {
      ZcEbFormulaItem zcEbFormulaItem = (ZcEbFormulaItem) complicationItemList.get(i);
      if ("CP".equals(zcEbFormulaItem.getParentItemCode())) {
        List childItemList = zcEbFormulaItem.getChildItemList();

        if (childItemList.size() > 0) {
          tableContent.addAll(getRowList(zcEbFormulaItem, childItemList));
        } else {
          Map rowMap = new HashMap();
          rowMap.put("rowItemList", getRowList(zcEbFormulaItem));
          tableContent.add(rowMap);
        }
      }
    }

    wordMap.put("tableContent", tableContent);
    return wordMap;
  }

  private List getRowList(ZcEbFormulaItem zcEbFormulaItem) {
    List rowItemList = new ArrayList();

	String str = zcEbFormulaItem.getName();
	if(str != null)
    rowItemList.add(StringUtil.freeMarkFillWordChar(str));
  	str = zcEbFormulaItem.getDescription();
  	if(str != null)
    rowItemList.add(StringUtil.freeMarkFillWordChar(str));
    if (zcEbFormulaItem.getRemark() != null && !"".equals(zcEbFormulaItem.getRemark()))
      rowItemList.add(StringUtil.freeMarkFillWordChar(zcEbFormulaItem.getRemark()));
    else
      rowItemList.add("NULL");
    return rowItemList;
  }

  private List getRowList(ZcEbFormulaItem zcEbFormulaItem, List childItemList) {
    List tableContent = new ArrayList();
    for (int j = 0; j < childItemList.size(); j++) {
      List rowItemList = new ArrayList();
      ZcEbFormulaItem childItem = (ZcEbFormulaItem) childItemList.get(j);
      String str;
      if (j == 0) {
    	str = zcEbFormulaItem.getName();
    	if(str != null)
        rowItemList.add(StringUtil.freeMarkFillWordChar(str));
      } else {
        rowItemList.add("NO");
      }
  	  str = childItem.getDescription();
  	  if(str != null)
      rowItemList.add(StringUtil.freeMarkFillWordChar(str));
      if (j == 0) {
        if (zcEbFormulaItem.getDescription() != null && !"".equals(zcEbFormulaItem.getDescription()))
          rowItemList.add(StringUtil.freeMarkFillWordChar(zcEbFormulaItem.getDescription()));
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

  private Map getGridMessageMap(String gridSize, String gridContent) {
    Map gridMessage = new HashMap();
    gridMessage.put("gridSize", gridSize);
    gridMessage.put("gridContent", gridContent);
    return gridMessage;
  }
}
