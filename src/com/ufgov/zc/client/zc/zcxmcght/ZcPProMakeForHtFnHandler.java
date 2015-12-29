/**
 * 
 */
package com.ufgov.zc.client.zc.zcxmcght;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.TableModel;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.datacache.AsValDataCache;
import com.ufgov.zc.client.datacache.CompanyDataCache;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcHtPrePayBillItem;
import com.ufgov.zc.common.zc.model.ZcPProMake;
import com.ufgov.zc.common.zc.model.ZcTBchtItem;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.model.ZcXmcgHtBi;
import com.ufgov.zc.common.zc.publish.IZcPProMakeServiceDelegate;

/**
 * @author Administrator
 *
 */
public class ZcPProMakeForHtFnHandler implements IForeignEntityHandler {

  private String columNames[];

  private ListCursor listCursor;

  private AbstractZcXmcgHtEditPanel zcXmcgHtEditPanel;

  private ElementConditionDto dtoForBidWinner;

  public ZcPProMakeForHtFnHandler(String columNames[], AbstractZcXmcgHtEditPanel zcXmcgHtEditPanel) {

    this.columNames = columNames;

    this.zcXmcgHtEditPanel = zcXmcgHtEditPanel;

    this.listCursor = zcXmcgHtEditPanel.getListCursor();

    this.dtoForBidWinner = zcXmcgHtEditPanel.getDtoForBidWinner();

  }

  //                                     

  public ZcPProMakeForHtFnHandler(String[] columNames2, ZcXmcgHtSubEditPanelDt self) {
    // TCJLODO Auto-generated constructor stub
  }

  public void excute(List selectedDatas) {

    // TCJLODO Auto-generated method stub
    if (ZcUtil.isYsdw()) {
      for (Object object : selectedDatas) {

        ZcPProMake zcPProMake = (ZcPProMake) object;
        IZcPProMakeServiceDelegate makeServiceDelegate = (IZcPProMakeServiceDelegate) ServiceFactory.create(IZcPProMakeServiceDelegate.class,
          "zcPProMakeServiceDelegate");
        ZcPProMake make = makeServiceDelegate.selectByPrimaryKey(zcPProMake.getZcMakeCode(), WorkEnv.getInstance().getRequestMeta());
        
        
        ZcXmcgHt ht=(ZcXmcgHt)listCursor.getCurrentObject();
        
      
        ht.setZcCgLeixing(ZcSettingConstants.PROJECT_BUY_CODE);
        ht.setAgency(make.getAgency());
        ht.setOrgCode(make.getOrgCode());
        ht.setCoCode(make.getCoCode());
        ht.setCoName(make.getCoName());
        ht.setExecuteDate(WorkEnv.getInstance().getRequestMeta().getTransDate());
        ht.setExecutor(WorkEnv.getInstance().getRequestMeta().getSvUserID());
        ht.setExecutorName(WorkEnv.getInstance().getRequestMeta().getSvUserName());

        ht.setBiList(createHtBiLst(make.getBiList()));

        ht.setItemList(createHtItemLst(make.getItemList()));

//        ht.setPayBiList(new ArrayList<ZcHtPrePayBillItem>());

        ht.setZcHtName(make.getZcMakeName());
        ht.setZcMakeCode(make.getZcMakeCode());
        
        ht.setZcPProMake(make);

        this.listCursor.setCurrentObject(ht);

        zcXmcgHtEditPanel.setEditingObject(ht);

        zcXmcgHtEditPanel.zcMakeCodeChange();
        zcXmcgHtEditPanel.setIsCar(true);
        
      }
    }

  }

  private List createHtItemLst(List itemList) {
    // TCJLODO Auto-generated method stub
    return null;
  }

  private List createHtBiLst(List biList) {
    // TCJLODO Auto-generated method stub
    return null;
  }

  @Override
  public TableModel createTableModel(List showDatas) {

    Object data[][] = new Object[showDatas.size()][columNames.length];

    for (int i = 0; i < showDatas.size(); i++) {
        ZcPProMake rowData = (ZcPProMake) showDatas.get(i);

      int col = 0;

      data[i][col++] = rowData.getZcMakeCode();

      data[i][col++] = rowData.getZcMakeName();

     

      //      data[i][col++] = rowData.getZcBidContent();


      data[i][col++] =AsValDataCache.getName("ZC_VS_PITEM_OPIWAY", rowData.getZcPifuCgfs());

      data[i][col++] = rowData.getZcMoneyBiSum();
      data[i][col++] = CompanyDataCache.getName(rowData.getCoCode());

    }

    MyTableModel model = new MyTableModel(data, columNames) {

      public boolean isCellEditable(int row, int colum) {

        return false;

      }

    };

    return model;

  }

  @Override
  public boolean isMultipleSelect() {

    // TCJLODO Auto-generated method stub

    return false;

  }

}
