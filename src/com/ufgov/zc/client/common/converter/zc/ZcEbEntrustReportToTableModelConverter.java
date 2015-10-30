package com.ufgov.zc.client.common.converter.zc;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.client.datacache.CompanyDataCache;
import com.ufgov.zc.client.datacache.OrgDataCache;
import com.ufgov.zc.common.zc.model.ZcEbEntrust;
import com.ufgov.zc.common.zc.model.ZcEbEntrustReport;

public class ZcEbEntrustReportToTableModelConverter {
	
	public static DefaultTableModel convertToTableModel(List dataList) {
		
		MyTableModel tableModel = null;
		
		Vector names = new Vector();
		
		Vector values = new Vector();

		names.add("状态");
		
		names.add("任务编号");
		
		names.add("项目名称");

		names.add("采购单位");

		names.add("负责处室");

		names.add("定标时间");

		names.add("中标供应商");

		names.add("中标金额");

		names.add("文件编制负责人");

		names.add("文件负责人联系电话");
		
		names.add("开标经办人");

		names.add("开标人联系电话");
		
//		names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_BULLETIN_STATUS));
		
		if (dataList != null && dataList.size() > 0) {
			
			for (int i = 0; i < dataList.size(); i++) {
				
				Vector rowData = new Vector();
				
				ZcEbEntrustReport data = (ZcEbEntrustReport) dataList.get(i);
				
				rowData.add(AsValDataCache.getName("ZC_VS_BULL_OPT_STATUS", data.getOptStatus()));
				
				rowData.add(data.getSnCode());

				rowData.add(data.getProjName());

				rowData.add(CompanyDataCache.getName(data.getCoCode()));

				rowData.add(OrgDataCache.getName(data.getSuperintendentOrg()));

				rowData.add("");
				
				rowData.add(data.getProviderName());
				
				rowData.add(data.getBidSum());
				
				rowData.add(data.getSuperintendentName());

				rowData.add(data.getsPhone());
				
				rowData.add(data.getAttnName());
				
				rowData.add(data.getaPhone());
				
				values.add(rowData);
				
			}
			
		}
		
		tableModel = initModel(values, names);
		
		tableModel.setList(dataList);
		
		return tableModel;
		
	}
	
	public static MyTableModel initModel(Vector values, Vector names){
		
		return new MyTableModel(values, names) {
			
			public Class getColumnClass(int column) {
				
				if ((column >= 0) && (column < getColumnCount()) && this.getRowCount() > 0) {
					
					for (int row = 0; row < this.getRowCount(); row++) {
						
						if (getValueAt(row, column) != null) {
							
							return getValueAt(row, column).getClass();
							
						}
						
					}
					
				}
				
				return Object.class;
				
			}
			
			public boolean isCellEditable(int row, int colum) {
				
				return false;
				
			}
			
		};
	}
}
