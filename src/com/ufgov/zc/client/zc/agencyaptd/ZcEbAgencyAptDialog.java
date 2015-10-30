package com.ufgov.zc.client.zc.agencyaptd;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.util.List;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.component.GkBaseDialog;

@SuppressWarnings("unchecked")
public class ZcEbAgencyAptDialog extends GkBaseDialog {
  private static final long serialVersionUID = 1L;

  private ZcEbAgencyAptDialog self = this;

  private ZcEbAgencyAptEditPanel editPanel;

  public ZcEbAgencyAptDialog(ZcEbAgencyAptdListPanel listPanel, List beanList, int editingRow,
    String tabStatus) {
    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);
    //editPanel = new ZcPProMakeXYEditPanel(this.self, new ListCursor(beanList, editingRow), tabStatus, listPanel);
    editPanel = listPanel.getEditPanel(this.self, new ListCursor(beanList, editingRow), tabStatus, listPanel);
    if (editPanel != null) {
      setLayout(new BorderLayout());
      add(editPanel);
      this.setTitle("资质信息登记");
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
