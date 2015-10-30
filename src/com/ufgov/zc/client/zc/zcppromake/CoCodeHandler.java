/**
 * 
 */
package com.ufgov.zc.client.zc.zcppromake;

import java.util.List;

import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcEbDaibian;
import com.ufgov.zc.common.zc.model.ZcPPro;
import com.ufgov.zc.common.zc.model.ZcPProMake;

/**
 * @author Administrator
 *
 */
public class CoCodeHandler implements IForeignEntityHandler {


  private String columNames[] = {"采购单位"};

  protected ElementConditionDto getBiDto = new ElementConditionDto();
  
  public String[] getColumNames() {
    return columNames;
  }

  private AbstractMainSubEditPanel edit;
  private ListCursor listCursor;
  private RequestMeta meta;

  public CoCodeHandler(AbstractMainSubEditPanel edit, ListCursor listCursor,ElementConditionDto getBiDto, RequestMeta requestMeta) {

    this.edit = edit;
    this.listCursor = listCursor;
    this.getBiDto=getBiDto;
    this.meta=requestMeta;
  }

  public CoCodeHandler(String columNames[], AbstractMainSubEditPanel edit, ListCursor listCursor) {

    this.columNames = columNames;
    this.edit = edit;
    this.listCursor = listCursor;

  }

  public void excute(List selectedDatas) {

    if(!edit.budgetExcuce(selectedDatas)){
      return;
    }

    if (selectedDatas.size() > 0) {

      ZcEbDaibian pro = (ZcEbDaibian) selectedDatas.get(0);
      ZcPProMake make = (ZcPProMake) listCursor.getCurrentObject();

      make.setCoCode(pro.getCoCode());
      make.setCoName(pro.getCoName());
      make.setOrgCode(pro.getOrgCode());
      getBiDto.setCoCode(pro.getCoCode());
//      make.setOrgName(pro.getOrgName());

      this.meta.setSvUserID(pro.getCoUser());
      this.meta.setSvUserName(pro.getEmpName());
      this.meta.setSvCoCode(pro.getCoCode());
      this.meta.setSvCoName(pro.getCoName());
      this.meta.setEmpCode(pro.getCoUser());
      this.meta.setEmpName(pro.getEmpName());   
      meta.setSvPoCode(pro.getPosiCode());
      meta.setSvOrgPoCode(pro.getOrgPosiCode());

      edit.setEditingObject(make);

    }

  }

  public void afterClear() {
    ZcPProMake make = (ZcPProMake) listCursor.getCurrentObject();


    make.setCoCode("");
    make.setCoName("");
    make.setOrgCode("");
    getBiDto.setZcText3(null);
    
    if(make.getBiList()!=null){
      make.getBiList().clear();
    }
    edit.setEditingObject(make);

  }
  public Boolean beforeSelect(ElementConditionDto dto) {

    afterClear();
    
    return true;
  }
  public TableModel createTableModel(List showDatas) {

    Object data[][] = new Object[showDatas.size()][columNames.length];

    for (int i = 0; i < showDatas.size(); i++) {

      ZcEbDaibian rowData = (ZcEbDaibian) showDatas.get(i);
      int col = 0;
      data[i][col++] = rowData.getCoName();

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
