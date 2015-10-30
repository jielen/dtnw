/** *  */package com.ufgov.zc.client.common.converter.zc;import java.util.ArrayList;import java.util.List;import java.util.Vector;import javax.swing.table.DefaultTableModel;import javax.swing.table.TableModel;import com.ufgov.zc.client.common.LangTransMeta;import com.ufgov.zc.client.common.MyTableModel;import com.ufgov.zc.client.component.table.BeanTableModel;import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;import com.ufgov.zc.client.datacache.OrgDataCache;import com.ufgov.zc.common.system.constants.ZcElementConstants;import com.ufgov.zc.common.zc.model.ZcEbPackPreAudit;import com.ufgov.zc.common.zc.model.ZcEbProviderPreAuditItem;/** * @author fanpl * */public class ZcEbProviderPreAuditToTableModelConverter {  @SuppressWarnings({ "unchecked", "serial" })  public static DefaultTableModel convertToTableModel(List billList) {    MyTableModel tableModel = null;    Vector names = new Vector();    Vector values = new Vector();    names.add("项目编号");    names.add("项目名称");    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_CODE));    names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_NAME));    names.add("审核组");    names.add("审核人");    names.add("审核时间");    if (billList != null && billList.size() > 0) {      for (int i = 0; i < billList.size(); i++) {        Vector rowData = new Vector();        ZcEbPackPreAudit bill = (ZcEbPackPreAudit) billList.get(i);        rowData.add(bill.getProjCode());        rowData.add(bill.getProjName());        rowData.add(bill.getPackCode());        rowData.add(bill.getPackName());        rowData.add(OrgDataCache.getName(bill.getOrgCode()));        //审核人不显示//        rowData.add(bill.getAuditorName());        rowData.add(bill.getAuditor());        rowData.add(bill.getAuditDate());        values.add(rowData);      }    }    tableModel = new MyTableModel(values, names) {      public Class getColumnClass(int column) {        if ((column >= 0) && (column < getColumnCount()) && this.getRowCount() > 0) {          for (int row = 0; row < this.getRowCount(); row++) {            if (getValueAt(row, column) != null) {              return getValueAt(row, column).getClass();            }          }        }        return Object.class;      }      public boolean isCellEditable(int row, int colum) {        return false;      }    };    tableModel.setList(billList);    return tableModel;  }  private static List<ColumnBeanPropertyPair> BillDetailInfo = new ArrayList<ColumnBeanPropertyPair>();  static {    //    BillDetailInfo.add(new ColumnBeanPropertyPair("ITEM_CODE", "itemCode", "明细编号"));    BillDetailInfo.add(new ColumnBeanPropertyPair("FORMULA_ITEM_NAME", "formulaItemName", "资质条件"));    BillDetailInfo.add(new ColumnBeanPropertyPair("AUDIT_VALUE", "auditValue", "初审结果"));    BillDetailInfo.add(new ColumnBeanPropertyPair("NO_PASS_REASON", "noPassReason", "不通过原因说明"));    BillDetailInfo.add(new ColumnBeanPropertyPair("REMARK", "remark", "备注"));  }  public static TableModel convertSubBiTableData(List<ZcEbProviderPreAuditItem> biList) {    BeanTableModel<ZcEbProviderPreAuditItem> tm = new BeanTableModel<ZcEbProviderPreAuditItem>() {      private static final long serialVersionUID = 6888363838628062064L;      @Override      public boolean isCellEditable(int row, int column) {        String columnId = this.getColumnIdentifier(column);        if ("ITEM_CODE".equals(columnId) || "FORMULA_ITEM_NAME".equals(columnId)) {          return false;        }        return super.isCellEditable(row, column);      }      @Override      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {        ZcEbProviderPreAuditItem bean = dataBeanList.get(rowIndex);        super.setValueAt(aValue, rowIndex, columnIndex);      }    };    tm.setOidFieldName("tempId");    for (ZcEbProviderPreAuditItem o : biList) {      //      o.setTempId(Guid.genID());    }    //    tm.setOidFieldName("detailCode");    tm.setDataBean(biList, BillDetailInfo);    return tm;  }  public static List<ColumnBeanPropertyPair> getBillDetailInfo() {    return BillDetailInfo;  }  public static void setBillDetailInfo(List<ColumnBeanPropertyPair> billDetailInfo) {    BillDetailInfo = billDetailInfo;  }}