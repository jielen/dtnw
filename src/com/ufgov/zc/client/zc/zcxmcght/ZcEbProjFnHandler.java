package com.ufgov.zc.client.zc.zcxmcght;import java.util.ArrayList;import java.util.List;import javax.swing.table.TableModel;import com.ufgov.zc.client.common.ListCursor;import com.ufgov.zc.client.common.MyTableModel;import com.ufgov.zc.client.common.WorkEnv;import com.ufgov.zc.client.zc.ZcUtil;import com.ufgov.zc.common.system.constants.ZcSettingConstants;import com.ufgov.zc.common.system.dto.ElementConditionDto;import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;import com.ufgov.zc.common.zc.model.ZcHtPrePayBillItem;import com.ufgov.zc.common.zc.model.ZcTBchtItem;import com.ufgov.zc.common.zc.model.ZcXmcgHt;import com.ufgov.zc.common.zc.model.ZcXmcgHtBi;public class ZcEbProjFnHandler implements IForeignEntityHandler {  private String columNames[];  private ListCursor listCursor;  private AbstractZcXmcgHtEditPanel zcXmcgHtEditPanel;  private ElementConditionDto dtoForBidWinner;  public ZcEbProjFnHandler(String columNames[], AbstractZcXmcgHtEditPanel zcXmcgHtEditPanel) {    this.columNames = columNames;    this.zcXmcgHtEditPanel = zcXmcgHtEditPanel;    this.listCursor = zcXmcgHtEditPanel.getListCursor();    this.dtoForBidWinner = zcXmcgHtEditPanel.getDtoForBidWinner();  }  //                                       public ZcEbProjFnHandler(String[] columNames2, ZcXmcgHtSubEditPanelDt self) {    // TCJLODO Auto-generated constructor stub  }  public void excute(List selectedDatas) {    // TCJLODO Auto-generated method stub    if (ZcUtil.isGys() ||ZcUtil.isCgzx() ) {      for (Object object : selectedDatas) {        ZcXmcgHt temp = (ZcXmcgHt) object;        temp.setZcHtStatus("0");        temp.setNd(WorkEnv.getInstance().getRequestMeta().getSvNd());        temp.setZcInputDate(WorkEnv.getInstance().getRequestMeta().getSysDate());         temp.setAgency(WorkEnv.getInstance().getCurrCoCode());        temp.setOrgCode(WorkEnv.getInstance().getOrgCode());        temp.setExecuteDate(WorkEnv.getInstance().getRequestMeta().getTransDate());        temp.setExecutor(WorkEnv.getInstance().getRequestMeta().getSvUserID());        temp.setExecutorName(WorkEnv.getInstance().getRequestMeta().getSvUserName());        temp.setBiList(new ArrayList<ZcXmcgHtBi>());        temp.setItemList(new ArrayList<ZcTBchtItem>());        temp.setPayBiList(new ArrayList<ZcHtPrePayBillItem>());                temp.setResetZijinList(new ArrayList<ZcXmcgHtBi>());        temp.setZcHtName(temp.getZcBidContent() == null ? temp.getProjName() : temp.getZcBidContent());        temp.setZcMakeCode(temp.getZcPProMake().getZcMakeCode());        this.listCursor.setCurrentObject(temp);        zcXmcgHtEditPanel.setEditingObject(temp);        zcXmcgHtEditPanel.zcMakeCodeChange();        zcXmcgHtEditPanel.setIsCar(false);              }    } else {    }  }  @Override  public TableModel createTableModel(List showDatas) {    Object data[][] = new Object[showDatas.size()][columNames.length];    for (int i = 0; i < showDatas.size(); i++) {      ZcXmcgHt rowData = (ZcXmcgHt) showDatas.get(i);      int col = 0;      data[i][col++] = rowData.getZcPProMake().getZcMakeCode();      data[i][col++] = rowData.getZcPProMake().getZcMakeName();      if (columNames.length == 5) {        data[i][col++] = rowData.getZcBidContent();      }      //      data[i][col++] = rowData.getZcBidContent();      data[i][col++] = rowData.getZcSuName();      data[i][col++] = rowData.getZcCgLeixing();      //data[i][col++] = rowData.getZcPProMake().getZcMoneyBiSum();    }    MyTableModel model = new MyTableModel(data, columNames) {      public boolean isCellEditable(int row, int colum) {        return false;      }    };    return model;  }  @Override  public boolean isMultipleSelect() {    // TCJLODO Auto-generated method stub    return false;  }}