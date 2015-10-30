package com.ufgov.zc.client.zc.consult;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.util.List;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.component.GkBaseDialog;

public class ZcEbConsultDialog extends GkBaseDialog {

  private ZcEbConsultDialog self = this;

  private ZcEbConsultListPanel listPanel;

  private ZcEbConsultEditPanel editPanel;

  public ZcEbConsultDialog(ZcEbConsultListPanel listPanel, List beanList, int editingRow, String tabStatus) {

    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);

    this.listPanel = listPanel;

    editPanel = new ZcEbConsultEditPanel(this.self, new ListCursor(beanList, editingRow), tabStatus, listPanel);

    setLayout(new BorderLayout());

    add(editPanel);

    this.setTitle("咨询建议");

    this.setSize(UIConstants.DIALOG_1_LEVEL_WIDTH, UIConstants.DIALOG_1_LEVEL_HEIGHT);

    this.moveToScreenCenter();

    this.setVisible(true);

  }
}
