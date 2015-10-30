package com.ufgov.zc.client.component.table.codecellrenderer;import java.awt.Component;import javax.swing.JTable;import com.ufgov.zc.client.component.table.cellrenderer.LineWrapCellRenderer;import com.ufgov.zc.client.datacache.BalModeDataCache;public class BalModeCellRenderer extends LineWrapCellRenderer {  private static final long serialVersionUID = 3184261311169929819L;  public Component getTableCellRendererComponent(JTable table, Object value,  boolean isSelected, boolean hasFocus, int row, int column) {    String name = BalModeDataCache.getName((String) value);    if (value != null) {      this.setToolTipText("[" + value + "]" + name);    } else {      this.setToolTipText(null);    }    return super.getTableCellRendererComponent(table, name, isSelected, hasFocus,    row, column);  }}