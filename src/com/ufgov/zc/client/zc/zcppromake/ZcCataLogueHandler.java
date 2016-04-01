/**
 * 
 */
package com.ufgov.zc.client.zc.zcppromake;

import java.util.List;
import java.util.Map;

import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcBBrand;
import com.ufgov.zc.common.zc.model.ZcBCatalogue;

/**
 * @author Administrator
 *
 */
public abstract class ZcCataLogueHandler implements IForeignEntityHandler {

  public String columNames[]={"编号","名称"};
  

  public ZcCataLogueHandler() {
  }

  public abstract void excute(List selectedDatas);

  @Override
  public TableModel createTableModel(List showDatas) {

    Object data[][] = new Object[showDatas.size()][columNames.length];

    for (int i = 0; i < showDatas.size(); i++) {
      ZcBCatalogue rowData = (ZcBCatalogue) showDatas.get(i);
      int col = 0;
      data[i][col++] = rowData.getZcCatalogueCode();
      data[i][col++] = rowData.getZcCatalogueName();
    }

    MyTableModel model = new MyTableModel(data, columNames) {
      @Override
      public boolean isCellEditable(int row, int colum) {
        return false;
      }
    };
    return model;
  }

  @Override
  public boolean isMultipleSelect() {
    return false;
  }
}
