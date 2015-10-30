package com.ufgov.zc.client.zc.zcppromake;

import java.util.List;

import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcPPro;
import com.ufgov.zc.common.zc.model.ZcPProMake;

public class ZcPProHandler implements IForeignEntityHandler {

	private String columNames[] = {"预算采购项目","预算单位","项目分类","录入人","录入时间"};
	public String[] getColumNames() {
		return columNames;
	}

	private AbstractMainSubEditPanel edit;
	private ListCursor listCursor;

	public ZcPProHandler(AbstractMainSubEditPanel edit, ListCursor listCursor) {

		this.edit = edit;
		this.listCursor = listCursor;

	}

	public ZcPProHandler(String columNames[], AbstractMainSubEditPanel edit, ListCursor listCursor) {

		this.columNames = columNames;
		this.edit = edit;
		this.listCursor = listCursor;

	}

	public void excute(List selectedDatas) {

		if(!edit.budgetExcuce(selectedDatas)){
			return;
		}

		if (selectedDatas.size() > 0) {

			ZcPPro pro = (ZcPPro) selectedDatas.get(0);
			ZcPProMake make = (ZcPProMake) listCursor.getCurrentObject();

			make.setProId(pro.getChrId());
			make.setProName(pro.getChrName());

			edit.setEditingObject(make);

		}

	}

	public void afterClear() {
		ZcPProMake make = (ZcPProMake) listCursor.getCurrentObject();

		make.setProId("");
		make.setProName("");

		edit.setEditingObject(make);

	}

	public TableModel createTableModel(List showDatas) {

		Object data[][] = new Object[showDatas.size()][columNames.length];

		for (int i = 0; i < showDatas.size(); i++) {

			ZcPPro rowData = (ZcPPro) showDatas.get(i);

			int col = 0;

			data[i][col++] = rowData.getChrName();

			data[i][col++] = rowData.getEnName();

			data[i][col++] = rowData.getBiName();

			data[i][col++] = rowData.getCreateUser();

			data[i][col++] = rowData.getCreateDate();

		}

		MyTableModel model = new MyTableModel(data, columNames) {

			public boolean isCellEditable(int row, int colum) {

				return false;

			}

			public Class getColumnClass(int column) {

				if ((column >= 0) && (column < getColumnCount())
						&& this.getRowCount() > 0) {

					for (int row = 0; row < this.getRowCount(); row++) {

						if (getValueAt(row, column) != null) {

							return getValueAt(row, column).getClass();

						}

					}

				}

				return Object.class;

			}

		};

		return model;

	}

	public boolean isMultipleSelect() {

		return false;

	}

}
