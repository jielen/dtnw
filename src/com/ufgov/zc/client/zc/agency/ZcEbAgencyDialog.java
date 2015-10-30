package com.ufgov.zc.client.zc.agency;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.util.List;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.component.GkBaseDialog;

@SuppressWarnings("unchecked")
public class ZcEbAgencyDialog extends GkBaseDialog {
  private static final long serialVersionUID = 1L;

  private ZcEbAgencyDialog self = this;

  private ZcEbAgencyEditPanel editPanel;

  public ZcEbAgencyDialog(ZcEbAgencyListPanel listPanel, List beanList, int editingRow, String tabStatus) {
    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);
    //editPanel = new ZcPProMakeXYEditPanel(this.self, new ListCursor(beanList, editingRow), tabStatus, listPanel);
    editPanel = listPanel.getEditPanel(this.self, new ListCursor(beanList, editingRow), tabStatus, listPanel);
    if (editPanel != null) {
      setLayout(new BorderLayout());
      add(editPanel);
      this.setTitle("代理机构登记");
      //this.setSize(UIConstants.SCREEN_WIDTH, UIConstants.SCREEN_HEIGHT - 25);
      this.setMaxSizeWindow();
      this.moveToScreenCenter();
      this.setVisible(true);
    }
  }

  @Override
  public void closeDialog() {
    this.editPanel.doExit();
  }

}
