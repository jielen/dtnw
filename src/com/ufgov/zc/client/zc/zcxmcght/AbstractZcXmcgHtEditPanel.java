package com.ufgov.zc.client.zc.zcxmcght;

import java.util.List;

import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcEbBulletinWordMold;

public abstract class AbstractZcXmcgHtEditPanel extends AbstractMainSubEditPanel{

  public AbstractZcXmcgHtEditPanel() {

  }

  public AbstractZcXmcgHtEditPanel(ZcBaseBill model, String compoId) {

    super(model, compoId);

  }

  public AbstractZcXmcgHtEditPanel(Class billClass, BillElementMeta eleMeta) {

    super(billClass, eleMeta);

  }

  public abstract String doOpenMold(List valueList, ZcEbBulletinWordMold bulletinMold);
  
  public abstract ElementConditionDto getDtoForBidWinner();
  
  public abstract ListCursor getListCursor();
  
  public abstract void zcMakeCodeChange();
  
  public abstract boolean isGys();

  public void setIsCar(boolean b) {
    // TCJLODO Auto-generated method stub
    
  }

}
