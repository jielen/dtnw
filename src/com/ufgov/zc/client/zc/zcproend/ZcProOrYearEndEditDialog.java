package com.ufgov.zc.client.zc.zcproend;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.util.List;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.component.GkBaseDialog;

public class ZcProOrYearEndEditDialog extends GkBaseDialog {

  private ZcProOrYearEndEditDialog self = this;

  private ZcProOrYearEndListPanel listPanel;

  private ZcProOrYearEndEditPanel editPanel;

  public ZcProOrYearEndEditDialog(ZcProOrYearEndListPanel listPanel, List beanList, int editingRow, String tabStatus) {

    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);

    this.listPanel = listPanel;

    editPanel = new ZcProOrYearEndEditPanel(this.self, new ListCursor(beanList, editingRow), tabStatus, listPanel);

    setLayout(new BorderLayout());

    add(editPanel);

    this.setTitle("手动结转");

    this.setSize(UIConstants.DIALOG_1_LEVEL_WIDTH, UIConstants.DIALOG_1_LEVEL_HEIGHT);

    this.moveToScreenCenter();

    this.setVisible(true);

  }
}
