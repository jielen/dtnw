/**
 * 
 */
package com.ufgov.zc.client.zc.zcxmcght;

import java.util.List;

import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcEbEvalReport;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;

/**
 * 录入合同时，供应商选择分包时的外部部件引用类
 * 
 * @author Administrator
 * 
 */
public class ZcPackSelectedHandler implements IForeignEntityHandler {

	private String columNames[];

	private ListCursor listCursor;

	private AbstractZcXmcgHtEditPanel zcXmcgHtEditPanel;

	public ZcPackSelectedHandler(String columNames[],
			AbstractZcXmcgHtEditPanel zcXmcgHtEditPanel) {

		this.columNames = columNames;

		this.zcXmcgHtEditPanel = zcXmcgHtEditPanel;

		this.listCursor = zcXmcgHtEditPanel.getListCursor();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler#excute(java
	 * .util.List)
	 */
	@Override
	public void excute(List selectedDatas) {
		// TODO Auto-generated method stub
		for (Object object : selectedDatas) {

			ZcEbEvalReport temp = (ZcEbEvalReport)object;

		      ZcXmcgHt zcXmcgHt = (ZcXmcgHt) listCursor.getCurrentObject();


//		      zcXmcgHt.setZcMakeCode(temp.getZcPProMake().getZcMakeCode());
//
//		      zcXmcgHt.getZcPProMake().setZcMakeName(temp.getZcPProMake().getZcMakeName());
//
//		      zcXmcgHt.getZcPProMake().setZcMakeCode(temp.getZcPProMake().getZcMakeCode());
//		      zcXmcgHt.getZcPProMake().setZcMoneyBiSum(temp.getZcPProMake().getZcMoneyBiSum());
//		      zcXmcgHt.getZcPProMake().setZcPifuCgfs(temp.getZcPProMake().getZcPifuCgfs());
//		      zcXmcgHt.getZcPProMake().setZcMakeLinkman(temp.getZcPProMake().getZcMakeLinkman());
//
//		      zcXmcgHt.setCoCode(temp.getCoCode());
//
////		      zcXmcgHt.setNd(temp.getNd());
//
//		      zcXmcgHt.setZcSuName(temp.getZcSuName());
//
//		      zcXmcgHt.setZcHtNum(temp.getZcHtNum());
//
//		      zcXmcgHt.setZcSuCode(temp.getZcSuCode());
//
//		      zcXmcgHt.setZcZbName(temp.getZcZbName());
//
//		      zcXmcgHt.setZcZbCode(temp.getZcZbCode());
//
//		      if(temp.getZcCgLeixing() != null && !"".equals(temp.getZcCgLeixing())){
//		        zcXmcgHt.setZcCgLeixing(temp.getZcCgLeixing());
//		      }
//
//		      zcXmcgHt.setZcBidContent(temp.getZcBidContent());
//
//		      zcXmcgHt.setZcBidCode(temp.getZcBidCode());
//
//		      zcXmcgHt.setZcHtName(temp.getZcPProMake().getZcMakeName());

		      zcXmcgHtEditPanel.setEditingObject(zcXmcgHt);

		      zcXmcgHtEditPanel.zcMakeCodeChange();

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler#isMultipleSelect
	 * ()
	 */
	@Override
	public boolean isMultipleSelect() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler#createTableModel
	 * (java.util.List)
	 */
	@Override
	public TableModel createTableModel(List showDatas) {

	    Object data[][] = new Object[showDatas.size()][columNames.length];

	    for (int i = 0; i < showDatas.size(); i++) {

	      ZcEbEvalReport rowData = (ZcEbEvalReport) showDatas.get(i);

	      int col = 0;

	      data[i][col++] = rowData.getProjCode();

	      data[i][col++] = rowData.getProjName();

	      data[i][col++] = rowData.getPackName();

	      data[i][col++] = rowData.getProviderName();

	      data[i][col++] = rowData.getBidSum();

	    }

	    MyTableModel model = new MyTableModel(data, columNames) {

	      public boolean isCellEditable(int row, int colum) {

	        return false;

	      }

	    };

	    return model;
	}

}
