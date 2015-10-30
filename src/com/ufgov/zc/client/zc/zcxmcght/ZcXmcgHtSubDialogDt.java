package com.ufgov.zc.client.zc.zcxmcght;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.util.List;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.component.GkBaseDialog;

public class ZcXmcgHtSubDialogDt extends GkBaseDialog {

  private static final long serialVersionUID = 1L;

  private ZcXmcgHtSubDialogDt self = this;

  private ZcXmcgHtSubEditPanelDt editPanel;

  private ZcXmcgHtSubListPanelDt listPanel;

  public ZcXmcgHtSubDialogDt(ZcXmcgHtSubListPanelDt listPanel, List beanList, int editingRow, String tabStatus) {

    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);

    this.listPanel = listPanel;

    editPanel = new ZcXmcgHtSubEditPanelDt(this.self, new ListCursor(beanList, editingRow), tabStatus, listPanel,listPanel.getCompoId());

    setLayout(new BorderLayout());

    add(editPanel);

    this.setTitle("合同备案");

    this.setSize(UIConstants.DIALOG_2_LEVEL_WIDTH, UIConstants.DIALOG_2_LEVEL_HEIGHT);

    this.moveToScreenCenter();

    this.setMaxSizeWindow();

    this.setVisible(true);

  }

  @Override
  public void closeDialog() {

    this.editPanel.doExit();

  }
}
