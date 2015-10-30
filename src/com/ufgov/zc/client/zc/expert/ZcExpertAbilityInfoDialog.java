package com.ufgov.zc.client.zc.expert;
import java.awt.BorderLayout;import com.ufgov.zc.client.common.ListCursor;import com.ufgov.zc.client.common.UIConstants;import com.ufgov.zc.client.component.GkBaseDialog;
public class ZcExpertAbilityInfoDialog extends GkBaseDialog {
  private ZcExpertAbilityInfoDialog self = this;
  private ZcExpertAbilityInfoEditPanel editPanel;    private GkBaseDialog parentDialog;  
  public ZcExpertAbilityInfoDialog( GkBaseDialog parentDialog,ListCursor listCursor) {       super(parentDialog,false);    this.parentDialog = parentDialog;     
    editPanel = new ZcExpertAbilityInfoEditPanel(listCursor, this);
    setLayout(new BorderLayout());
    add(editPanel);
    this.setTitle("专家评价");
    this.setSize(UIConstants.DIALOG_0_LEVEL_WIDTH, UIConstants.DIALOG_0_LEVEL_HEIGHT);    this.moveToScreenCenter();    this.setModal(false);    
    this.setVisible(true);         
  }
  @Override
  public void closeDialog() { 
    this.editPanel.doExit();        this.dispose(); 
  }
}
