package com.ufgov.zc.client.common.converter.zc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.client.datacache.CompanyDataCache;
import com.ufgov.zc.client.datacache.OrgDataCache;
import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.model.AsFile;
import com.ufgov.zc.common.system.util.DateUtil;
import com.ufgov.zc.common.zc.model.TreeNodeValueObject;
import com.ufgov.zc.common.zc.model.ZcEbEntrust;
import com.ufgov.zc.common.zc.model.ZcEbEntrustBullin;
import com.ufgov.zc.common.zc.model.ZcEbPackReq;

public class ZcEbEntrustBulletinToTableModelConverter{
	
	public static DefaultTableModel convertToTableModel(List dataList) {
		
		MyTableModel tableModel = null;
		
		Vector names = new Vector();
		
		Vector values = new Vector();

		names.add("状态");
		
		names.add("任务编号");
		
		names.add("项目名称");

		names.add("投标时间");

		names.add("开标时间");

		names.add("开标地点");

		names.add("文件编制负责人");

		names.add("文件负责人联系电话");
		
		names.add("开标经办人");

		names.add("开标人联系电话");

		names.add("标书下载");
		
//		names.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_BULLETIN_STATUS));
		
		if (dataList != null && dataList.size() > 0) {
			
			for (int i = 0; i < dataList.size(); i++) {
				
				Vector rowData = new Vector();
				
				ZcEbEntrustBullin data = (ZcEbEntrustBullin) dataList.get(i);
				
				rowData.add(AsValDataCache.getName("ZC_VS_BULL_OPT_STATUS", data.getOptStatus()));
				
				rowData.add(data.getSnCode());

				rowData.add(data.getProjName());

				rowData.add(data.getShellStartTime());

				rowData.add(data.getOpenBidTime());

				rowData.add(data.getOpenBidAddress());

				rowData.add(data.getSuperintendentName());

				rowData.add(data.getsPhone());

				rowData.add(data.getAttnName());

				rowData.add(data.getaPhone());
				
				rowData.add(data.getFileId());
				
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
	
	public static TableModel convertBulletinToTableModel(List<ZcEbEntrustBullin> dataList) {

	    BeanTableModel<ZcEbEntrustBullin> tm = new BeanTableModel<ZcEbEntrustBullin>() {

	      private static final long serialVersionUID = 6888363838628062064L;

	      @Override
	      public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

	    	  ZcEbEntrustBullin bean = getDataBeanList().get(rowIndex);

	        if ("FILE_NAME".equals(this.getColumnIdentifier(columnIndex))) {

	          if (aValue == null) {

	            bean.setFileId(null);

	            bean.setFileName(null);
	          } else {

	            bean.setFileId(((AsFile) aValue).getFileId());

	            bean.setFileName(((AsFile) aValue).getFileName());

	          }

	          putEditedData(bean);

	          fireTableCellUpdated(rowIndex, columnIndex);

	        } else {

	          super.setValueAt(aValue, rowIndex, columnIndex);

	        }

	      }

	      @Override
	      public boolean isCellEditable(int row, int column) {
	    	  
	    	  String columnId = this.getColumnIdentifier(column);

	    	  if ("FILE_NAME".equals(columnId) || "FILE_ID".equals(columnId)) {
	    		  
	    		  return true;
	    		  
	    	  }

	        return false;

	      }

	    };
	    
	    tm.setOidFieldName("snCode");

	    for (ZcEbEntrustBullin data : dataList) {

//	      data.setSnCode(Guid.genID());
	    	data.setOptStatus(AsValDataCache.getName("ZC_VS_BULL_OPT_STATUS", data.getOptStatus()));
	    	
	    	data.setCoName((CompanyDataCache.getName(data.getCoCode())));

	    	data.setSuperintendentOrg((OrgDataCache.getName(data.getSuperintendentOrg())));
	    	
	    	data.setShellStartTimeStr(data.getShellStartTime()==null? "":DateUtil.dateToSsString(data.getShellStartTime()));

	    	data.setOpenBidTimeStr(data.getOpenBidTime()==null? "":DateUtil.dateToSsString(data.getOpenBidTime()));

	    }

	    tm.setDataBean(dataList, bulletinTableColumnInfo);

	    return tm;

	  }
	
	private static List<ColumnBeanPropertyPair> bulletinTableColumnInfo = new ArrayList<ColumnBeanPropertyPair>();
	
	  static {
		  
		bulletinTableColumnInfo.add(new ColumnBeanPropertyPair("OPT_STATUS", "optStatus", "状态"));

	    bulletinTableColumnInfo.add(new ColumnBeanPropertyPair("SN_CODE", "snCode", "任务编号"));

	    bulletinTableColumnInfo.add(new ColumnBeanPropertyPair("PROJ_NAME", "projName", "项目名称"));

	    bulletinTableColumnInfo.add(new ColumnBeanPropertyPair("CO_NAME", "coName", "采购单位"));

	    bulletinTableColumnInfo.add(new ColumnBeanPropertyPair("SUPERINTENDENT_ORG", "superintendentOrg", "负责处室"));

	    bulletinTableColumnInfo.add(new ColumnBeanPropertyPair("SELL_START_TIME", "shellStartTimeStr", "投标时间"));

	    bulletinTableColumnInfo.add(new ColumnBeanPropertyPair("OPEN_BID_TIME", "openBidTimeStr", "开标时间"));

	    bulletinTableColumnInfo.add(new ColumnBeanPropertyPair("OPEN_BID_ADDRESS", "openBidAddress", "开标地点"));

	    bulletinTableColumnInfo.add(new ColumnBeanPropertyPair("SUPERINTENDENT_NAME", "superintendentName", "文件编制负责人"));

	    bulletinTableColumnInfo.add(new ColumnBeanPropertyPair("SPHONE", "sPhone", "文件负责人联系电话"));

	    bulletinTableColumnInfo.add(new ColumnBeanPropertyPair("ATTN_NAME", "attnName", "开标经办人"));

	    bulletinTableColumnInfo.add(new ColumnBeanPropertyPair("APHONE", "aPhone", "开标人联系电话"));

//	    bulletinTableColumnInfo.add(new ColumnBeanPropertyPair("FILE_ID", "fileId", "标书下载"));
	    
	    bulletinTableColumnInfo.add(new ColumnBeanPropertyPair("FILE_NAME", "fileName", "标书下载"));

	  }

	public static List<ColumnBeanPropertyPair> getBulletinTableColumnInfo() {
		return bulletinTableColumnInfo;
	}

	public static void setBulletinTableColumnInfo(
			List<ColumnBeanPropertyPair> bulletinTableColumnInfo) {
		ZcEbEntrustBulletinToTableModelConverter.bulletinTableColumnInfo = bulletinTableColumnInfo;
	}
	  
	  
}
