package com.ufgov.zc.client.component.table.celleditor;import java.awt.Component;import javax.swing.AbstractCellEditor;import javax.swing.JTable;import javax.swing.table.TableCellEditor;import com.ufgov.zc.client.component.ContractSelectField;import com.ufgov.zc.client.component.event.ValueChangeEvent;import com.ufgov.zc.client.component.event.ValueChangeListener;public class ContractCellEditor extends AbstractCellEditor implements TableCellEditor {  private ContractSelectField contractSelectField = new ContractSelectField();  private ContractCellEditor self = this;  public ContractCellEditor() {    contractSelectField.addValueChangeListener(new ValueChangeListener() {      public void valueChanged(ValueChangeEvent e) {        self.stopCellEditing();      }    });  }  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,  int column) {    if (value instanceof String) {      contractSelectField.setValue(null);    } else      contractSelectField.setValue(value);    return contractSelectField;  }  public Object getCellEditorValue() {    Object obj = contractSelectField.getContract();    if (obj != null) {      return contractSelectField.getContract();    } else {      return contractSelectField.getText();    }  }}