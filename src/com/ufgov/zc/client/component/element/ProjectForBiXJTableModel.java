package com.ufgov.zc.client.component.element;import java.util.ArrayList;import java.util.List;import java.util.Map;import com.ufgov.zc.client.component.table.BeanTableModel;import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;import com.ufgov.zc.common.commonbiz.model.Project;public class ProjectForBiXJTableModel extends BeanTableModel<Project> {  private static final long serialVersionUID = -3683271040414699401L;  public ProjectForBiXJTableModel(List<Project> pDataList) {    refreshData(pDataList);  }  public ProjectForBiXJTableModel(List<Project> pDataList, boolean editable) {    this.editable = editable;    refreshData(pDataList);  }  public synchronized void refreshData(List<Project> list) {    this.setDataBean(list, createColumnBeanPropertyPairs());  }  private List<ColumnBeanPropertyPair> createColumnBeanPropertyPairs() {    List<ColumnBeanPropertyPair> pairList = new ArrayList<ColumnBeanPropertyPair>();    ColumnBeanPropertyPair pair = new ColumnBeanPropertyPair();    pair.setColumnIdentifier("项目代码");    pair.setBeanPropertyName("code");    pairList.add(pair);    pair = new ColumnBeanPropertyPair();    pair.setColumnIdentifier("年度");    pair.setBeanPropertyName("nd");    pairList.add(pair);    pair = new ColumnBeanPropertyPair();    pair.setColumnIdentifier("项目名称");    pair.setBeanPropertyName("name");    pairList.add(pair);    return pairList;  }  public void setValueAt(Project project, int row) {    if (project == null) {      return;    }    if (row >= 0 && row < this.getRowCount()) {      for (int col = 0; col < this.getBeanPropertyNames().size(); col++) {        super.setValueAt(project.get(this.getBeanPropertyNames().get(col)), row, col);      }    }  }  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {    super.setValueAt(aValue, rowIndex, columnIndex);  }  public boolean isCellEditable(int row, int column) {    if (this.editable) {      if (getBeanPropertyNames().get(column).equals("nd")) {        return false;      }      return true;    } else {      return false;    }  }  protected void putBeanToMap(Project project, Map<Object, Project> dataMap) {    if (project == null) {      return;    }    dataMap.put(project.getOid(), project);  }  public List<Project> getDataList() {    return this.getDataBeanList();  }}