package com.ufgov.zc.client.zc.project;

import java.util.List;

import javax.swing.JTabbedPane;

import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.model.ZcEbRequirementConfirm;

public class ZcEbReqConfirmJTabbedPane extends JTabbedPane {

  private List<ZcEbPack> packList;

  public BillElementMeta confirmBillElementMeta = BillElementMeta.getBillElementMetaWithoutNd(ZcEbRequirementListPanel.compoId + "_CONFIRM");

  public ZcEbReqConfirmJTabbedPane() {
  }

  public ZcEbReqConfirmJTabbedPane(List<ZcEbPack> packList) {
    this.packList = packList;

  }

  public boolean checkBeforeSave() {
    int tabSize = this.getComponentCount();
    for (int i = 0; i < tabSize; i++) {
      ZcEbReqConfimPanel panel = (ZcEbReqConfimPanel) this.getComponentAt(i);
      if (!panel.checkBeforeSave()) {
        return false;
      }
    }
    return true;
  }
  
  public boolean isDataChanged(){
	  int tabSize = this.getComponentCount();
	    for (int i = 0; i < tabSize; i++) {
	      ZcEbReqConfimPanel panel = (ZcEbReqConfimPanel) this.getComponentAt(i);
	      if (!panel.isDataChanged()) {
	        return false;
	      }
	    }
	    return true;
  }

  public void refreshConfirmJTabbedPane(List<ZcEbPack> packList) {
    this.removeAll();
    this.packList = packList;
    for (int i = 0; i < packList.size(); i++) {
      if (packList.get(i).getZcEbRequirementConfirm() == null) {
        ZcEbRequirementConfirm bean = new ZcEbRequirementConfirm();
        packList.get(i).setZcEbRequirementConfirm(bean);
      }
      ZcEbReqConfimPanel panel = new ZcEbReqConfimPanel(packList.get(i).getZcEbRequirementConfirm(), this);
      this.addTab(packList.get(i).getPackName() + "业务需求", panel);
    }
  }

}
