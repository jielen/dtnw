package com.ufgov.zc.client.zc.project;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.util.List;

import org.apache.log4j.Logger;

import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.common.system.constants.ZcElementConstants;

@SuppressWarnings("unchecked")
public class ZcEbRequirementEditDialog extends GkBaseDialog {
  protected static final Logger logger = Logger.getLogger(ZcEbRequirementEditDialog.class);

  private static final long serialVersionUID = -56873481859200532L;

  private ZcEbRequirementEditPanel editPanel;

  public ZcEbRequirementEditDialog(ZcEbRequirementListPanel listPanel, List beanList, int editingRow, String tabStatus) {
    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);
    editPanel = new ZcEbRequirementEditPanel(this, new ListCursor(beanList, editingRow), tabStatus, listPanel);
    setLayout(new BorderLayout());
    add(editPanel);
    this.setTitle(LangTransMeta.translate(ZcElementConstants.TITLE_TRANS_ZC_EB_REQUIREMENT));
    this.setSize(UIConstants.DIALOG_0_LEVEL_WIDTH, UIConstants.DIALOG_0_LEVEL_HEIGHT);
    this.moveToScreenCenter();
    this.setVisible(true);

  }

  @Override
  public void closeDialog() {

    this.editPanel.doExit();

  }

}
