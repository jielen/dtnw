package com.ufgov.zc.client.common.converter.zc;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.client.datacache.CompanyDataCache;
import com.ufgov.zc.client.datacache.OrgDataCache;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.zc.model.ZcEbBulletin;
import com.ufgov.zc.common.zc.model.ZcEbEntrust;
import com.ufgov.zc.common.zc.model.ZcEbEntrustBull;

public class ZcEbEntrustBullToTableModelConverter {
	
	public static DefaultTableModel convertToTableModel(List dataList) {
		
		MyTableModel tableModel = null;
		
		Vector names = new Vector();
		
		Vector values = new Vector();

		names.add("状态");
		
		names.add("下达时间");
		
		names.add("任务编号");
		
		names.add("项目名称");

		names.add("采购单位");

		names.add("负责处室");

		names.add("文件编制负责人");

		names.add("文件负责人联系电话");
		
		names.add("开标经办人");

		names.add("开标人联系电话");

		names.add("采购预算");

		names.add("采购方式");
		
//		names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_BULLETIN_STATUS));
		
		if (dataList != null && dataList.size() > 0) {
			
			for (int i = 0; i < dataList.size(); i++) {
				
				Vector rowData = new Vector();
				
				ZcEbEntrustBull data = (ZcEbEntrustBull) dataList.get(i);

				rowData.add(AsValDataCache.getName("ZC_VS_BULL_OPT_STATUS", data.getOptStatus()));
				
				rowData.add(data.getZcWeitoDate());

				rowData.add(data.getSnCode());

				rowData.add(data.getZcMakeName());
				
				rowData.add(CompanyDataCache.getName(data.getCoCode()));

				rowData.add(OrgDataCache.getName(data.getSuperintendentOrg()));

				rowData.add(data.getSuperintendentName());

				rowData.add(data.getsPhone());

				rowData.add(data.getAttnName());

				rowData.add(data.getaPhone());

				rowData.add(data.getZcMoneyBiSum());
				
				rowData.add(AsValDataCache.getName("ZC_VS_PITEM_OPIWAY", data.getZcPifuCgfs()));
				
				values.add(rowData);
				
			}
			
		}
		
		tableModel = new MyTableModel(values, names) {
			
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
		
		tableModel.setList(dataList);
		
		return tableModel;
		
	}
}
