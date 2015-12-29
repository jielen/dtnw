package com.ufgov.zc.client.zc.agencyaptd;

import java.awt.Component;
import java.util.List;

import javax.swing.JOptionPane;

import com.ufgov.zc.client.common.AbstractObjectTableModel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcBAgencyListAptd;
import com.ufgov.zc.common.zc.model.ZcPProMake;
import com.ufgov.zc.common.zc.publish.IZcEBAgencyServiceDelegate;

public class ZcBAgencyAptdTableModel extends AbstractObjectTableModel {
  private IZcEBAgencyServiceDelegate agencyServiceDelegate = (IZcEBAgencyServiceDelegate) ServiceFactory.create(
    IZcEBAgencyServiceDelegate.class, "agencyServiceDelegate");
  	private Component panel;

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();
  

  public ZcBAgencyAptdTableModel(Component panel) {
    this.columnNames = new String[] { "资质编码", "资质名称" };
    this.propNames = new String[] { "zcAptdCode", "zcAptdName" };
    this.panel = panel;
  }

  @Override
  public void save() throws Exception {
    // TCJLODO Auto-generated method stub
    ZcBAgencyListAptd agency = null;
    for (int i = 0; i < this.deleteList.size(); i++) {
        agency = (ZcBAgencyListAptd) this.deleteList.get(i);
        agencyServiceDelegate.doDeleteApds(agency, requestMeta);
      }
    for (int i = 0; i < this.insertList.size(); i++) {
      agency = (ZcBAgencyListAptd) this.insertList.get(i);
      agencyServiceDelegate.doSaveAptd(agency, requestMeta);
    }
    for (int i = 0; i < this.updateList.size(); i++) {
      agency = (ZcBAgencyListAptd) this.updateList.get(i);
      agencyServiceDelegate.doSaveAptd(agency, requestMeta);
    }
  }

  @Override
  public List queryData() throws Exception {
    ElementConditionDto dto = new ElementConditionDto();
    dto.setZcText1(ZcPProMake.CAIGOU_TYPE_All);
    return agencyServiceDelegate.getZcBAgencyAptdAllList(dto, requestMeta);
  }

  @Override
  public void insertRecord() {
    // TCJLODO Auto-generated method stub
    this.insertRecord(new ZcBAgencyListAptd());
  }

  @Override
  public void insertRecord(int index) {
    // TCJLODO Auto-generated method stub
    this.insertRecord(index, new ZcBAgencyListAptd());
  }

  public boolean isCellEditable(int rowIndex, int columnIndex) {
    if (isUpdateSignal() && columnIndex > 0){
      return true;
    }
    if(isUpdateSignal() && columnIndex == 0 && this.insertList.contains(this.recordList.get(rowIndex))){
    	return true;
    }
    return false;
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    if (this.getRecordList() == null)
      return;
    if(columnIndex == 0 && !"".equals(aValue.toString().trim())){
    	for(int i = 0; i < this.recordList.size(); i++){
    		if(rowIndex != i && aValue.toString().trim().equals(((ZcBAgencyListAptd)recordList.get(i)).getZcAptdCode())){
	            JOptionPane.showMessageDialog(panel, "资质编码为"+aValue.toString().trim()+"的数据已存在！\n", "错误", JOptionPane.ERROR_MESSAGE);
	            return;
    		}
    	}
    }
    Object record = this.getRecordList().get(rowIndex);
    Object oldValue = propSelector.select(record, columnIndex, propNames);

    if (oldValue == null || !oldValue.equals(aValue.toString().trim())) {
      propSelector.update(record, aValue.toString().trim(), columnIndex, propNames);
      if (isUpdateSignal() && !this.getUpdateList().contains(record) && !this.getInsertList().contains(record)) {
        this.getUpdateList().add(this.getRecordList().get(rowIndex));
      }
    }
    fireTableCellUpdated(rowIndex, columnIndex);
  }
}
