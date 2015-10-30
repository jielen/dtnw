package com.ufgov.zc.client.zc.zcppro;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.util.List;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.component.GkBaseDialog;

public class ZcPProDialog extends GkBaseDialog {
  private static final long serialVersionUID = 1L;

  private ZcPProDialog self = this;

  private ZcPProEditPanel editPanel;

  private ZcPProListPanel listPanel;

  public ZcPProDialog(ZcPProListPanel listPanel, List beanList, int editingRow, String tabStatus) {
    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);
    this.listPanel = listPanel;
    editPanel = new ZcPProEditPanel(this.self, new ListCursor(beanList, editingRow), tabStatus, listPanel);
    setLayout(new BorderLayout());
    add(editPanel);
    this.setTitle("预算项目管理");
    this.setSize(UIConstants.DIALOG_4_LEVEL_WIDTH, UIConstants.DIALOG_4_LEVEL_HEIGHT);
    this.moveToScreenCenter();
    this.setVisible(true);
  }

  @Override
  public void closeDialog() {
    this.editPanel.doExit();
  }

}
