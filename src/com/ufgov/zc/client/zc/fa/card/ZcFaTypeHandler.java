/**
 * 
 */
package com.ufgov.zc.client.zc.fa.card;

import java.util.HashMap;
import java.util.List;

import javax.swing.table.TableModel;

import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler;
import com.ufgov.zc.common.zc.model.ZcFaCard;
import com.ufgov.zc.common.zc.model.ZcFaCardType;

/**
 * @author Administrator
 *
 */
public class ZcFaTypeHandler implements IForeignEntityHandler {
  private final String columNames[];
  private ZcFaCardEditPanel mainPanel;

  public ZcFaTypeHandler(String columNames[],ZcFaCardEditPanel mainPanel) {

    this.columNames = columNames;
    this.mainPanel=mainPanel;

  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler#excute(java.util.List)
   */
  @Override
  public void excute(List selectedDatas) {
    // TODO Auto-generated method stub
    ZcFaCard card=mainPanel.getCurrentEditingObject();
    if(selectedDatas!=null && selectedDatas.size()>0){
      ZcFaCardType type=(ZcFaCardType) selectedDatas.get(0);
      card.setFatypeCode(type.getFatypeCode());
      card.setFatypeName(type.getFatypeName());
      card.setFaUnit(type.getFatypeDw());
      card.setUseLife(type.getUseLife());
      card.setResiRat(type.getResiRat());
      mainPanel.setEditingObject(card);
    }

  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler#isMultipleSelect()
   */
  @Override
  public boolean isMultipleSelect() {
    // TODO Auto-generated method stub
    return false;
  }

  /* (non-Javadoc)
   * @see com.ufgov.zc.common.zc.foreignentity.IForeignEntityHandler#createTableModel(java.util.List)
   */
  @Override
  public TableModel createTableModel(List showDatas) {
    // TODO Auto-generated method stub
    return null;
  }
  public void afterClear() {
    ZcFaCard card=mainPanel.getCurrentEditingObject();
    card.setFatypeCode(null);
    card.setFatypeName(null);
    card.setFaUnit(null);
    card.setUseLife(null);
    card.setResiRat(null);
    mainPanel.setEditingObject(card);
  }

  public boolean beforeSelect(ElementConditionDto dto) {
    
    return true;
  }
}
