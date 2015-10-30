package com.ufgov.zc.client.component.table.codecelleditor;import java.awt.Component;import javax.swing.AbstractCellEditor;import javax.swing.JTable;import javax.swing.table.TableCellEditor;import com.ufgov.zc.client.component.element.FundForBiXJTableSelectField;import com.ufgov.zc.common.commonbiz.model.Fund;public class FundForBiXJSelectFieldCellEditor extends AbstractCellEditor implements TableCellEditor {  private static final long serialVersionUID = -393237123566353415L;  private FundForBiXJTableSelectField editorComponent = new FundForBiXJTableSelectField(20);  public FundForBiXJSelectFieldCellEditor() {  }  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,  int column) {    editorComponent.setValueByCode((String) value);    return editorComponent;  }  public Object getCellEditorValue() {    String code = null;    Fund value = editorComponent.getFund();    if (value != null) {      code = value.getCode();    }    return code;  }}