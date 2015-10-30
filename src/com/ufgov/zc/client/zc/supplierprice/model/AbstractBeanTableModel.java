package com.ufgov.zc.client.zc.supplierprice.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;

import com.ufgov.zc.common.system.IInitializeable;

public abstract class AbstractBeanTableModel extends AbstractTableModel {

  private static final long serialVersionUID = -943987009570836482L;

  protected Class targetClass;

  protected String[] columnNames;

  protected String[] propNames;

  protected List _records = new ArrayList();

  protected BeanPropSelector propSelector = new BeanPropSelector.DefaultPropSelector();

  private boolean editSignal = false;

  public boolean isUpdateSignal() {
    return editSignal;
  }

  public void setUpdateSignal(boolean updateSignal) {
    this.editSignal = updateSignal;
  }

  public boolean isEditSignal() {
    return editSignal;
  }

  public void setEditSignal(boolean editSignal) {
    this.editSignal = editSignal;
  }

  public AbstractBeanTableModel(Class clazz) throws Exception {
    this.init(clazz);
    this.propNames = this.initPropNames(clazz);
    this.columnNames = this.initColumnNames(clazz);
  }

  @Override
  public int getColumnCount() {
    if (columnNames == null)
      return 0;
    return columnNames.length;
  }

  @Override
  public String getColumnName(int column) {
    return columnNames[column];
  }

  public String getPropName(int column) {
    return propNames[column];
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    if (this._records == null || this._records.size() == 0) {
      return "";
    } else {
      Object record = this._records.get(rowIndex);
      return propSelector.select(record, columnIndex, propNames);
    }
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    if (this._records == null || this._records.size() == 0)
      return;
    Object record = this._records.get(rowIndex);
    Object oldValue = propSelector.select(record, columnIndex, propNames);

    if (oldValue == null || !oldValue.equals(aValue)) {
      propSelector.update(record, aValue, columnIndex, propNames);
    }
    fireTableCellUpdated(rowIndex, columnIndex);
  }

  @Override
  public Class getColumnClass(int column) {
    if ((column >= 0) && (column < getColumnCount()) && this.getRowCount() > 0) {
      Object record = this._records.get(0);
      Object v = propSelector.select(record, column, propNames);
      if (v != null) {
        return v.getClass();
      }
    }
    return Object.class;
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    if (editSignal)
      return true;
    return false;
  }

  @Override
  public int getRowCount() {
    // TODO Auto-generated method stub
    return _records.size();
  }

  /**
   * 从model外部更改model数据集的时候使用
   * @param records
   */
  public void setRecords(List records) {
    this._records = records;
    this.fireTableDataChanged();
  }
  
  public List getRecords() {
    return this._records;
  }

  public Object getRecord(int index) {
    return _records.get(index);
  }

  public Object appendRecord(Object record) {
    _records.add(record);
    this.fireTableDataChanged();
    return record;
  }

  public Object appendRecord() {
    try {
      Object record = targetClass.newInstance();
      if (record instanceof IInitializeable) {
        ((IInitializeable) record).initialize();
      }
      return appendRecord(record);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public void removeRecord(Object record) {
    _records.remove(record);
    this.fireTableDataChanged();
  }

  public void removeRecords(List records) {
    for (int i = 0; i < records.size(); i++) {
      _records.remove(records.get(i));
    }
    this.fireTableDataChanged();
  }

  public void refresh() {
    new SwingWorker() {
      @Override
      protected Object doInBackground() throws Exception {
        return doFresh();
      }

      @Override
      public void done() {
        try {
          setRecords((List) this.get());
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }

    }.execute();
  }

  /*
   * 关联table, 并且对table进行赋render和editor的操作, 及tab编辑态操作
   */
  public void linkTable(final JTable t) {
    if (t != null) {
      t.setModel(this);
      //更换列标识为相应的属性 
      for (int i = 0; i < columnNames.length; i++) {
        t.getColumn(columnNames[i]).setIdentifier(propNames[i]);
      }
    }
  }

  /**
   * 需要model自己维护动态更新数据集的时候覆盖, 默认返回初始Records
   * @return
   */
  protected List doFresh() throws Exception {
    return this._records;
  }

  //子类覆盖，用来进行初始化用
  protected void init(Class clazz) throws Exception {
    this.targetClass = clazz;
  }

  protected abstract String[] initPropNames(Class clazz);

  protected abstract String[] initColumnNames(Class clazz);

}
