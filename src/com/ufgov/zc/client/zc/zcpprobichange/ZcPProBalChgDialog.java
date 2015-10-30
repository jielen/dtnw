package com.ufgov.zc.client.zc.zcpprobichange;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.util.List;

import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.common.system.constants.ZcPProBalChgConstants;

public class ZcPProBalChgDialog extends GkBaseDialog {
  private static final long serialVersionUID = 1L;

  private ZcPProBalChgDialog self = this;

  private ZcPProBalChgEditPanel editPanel;

  private ZcPProBalChgListPanel listPanel;

  public ZcPProBalChgDialog(ZcPProBalChgListPanel listPanel, List beanList, int editingRow, String tabStatus) {
    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);
    this.listPanel = listPanel;
    editPanel = new ZcPProBalChgEditPanel(this.self, new ListCursor(beanList, editingRow), tabStatus, listPanel);
    setLayout(new BorderLayout());
    add(editPanel);
    this.setTitle(LangTransMeta.translate(ZcPProBalChgConstants.FIELD_TRANS_ZC_P_PRO_BAL_CHG_TITLE));
    this.setSize(UIConstants.DIALOG_2_LEVEL_WIDTH, UIConstants.DIALOG_2_LEVEL_HEIGHT);
    this.moveToScreenCenter();
    this.setVisible(true);
  }

  @Override
  public void closeDialog() {
    this.editPanel.doExit();
  }

}
