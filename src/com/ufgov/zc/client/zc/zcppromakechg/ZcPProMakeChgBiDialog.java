/**
 * 
 */
package com.ufgov.zc.client.zc.zcppromakechg;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.util.List;

import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.zc.zcpprobichange.ZcPProBalChgEditPanel;
import com.ufgov.zc.common.system.constants.ZcPProBalChgConstants;

/**
 * @author Administrator
 *
 */
public class ZcPProMakeChgBiDialog extends GkBaseDialog {
  private static final long serialVersionUID = 1L;

  private ZcPProMakeChgBiDialog self = this;

  private ZcPProMakeChgBiEditPanel editPanel;

  private ZcPProMakeChgBiListPanel listPanel;

  public ZcPProMakeChgBiDialog(ZcPProMakeChgBiListPanel listPanel, List beanList, int editingRow, String tabStatus) {
    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);
    this.listPanel = listPanel;
    editPanel = new ZcPProMakeChgBiEditPanel(this.self, new ListCursor(beanList, editingRow), tabStatus, listPanel);
    setLayout(new BorderLayout());
    add(editPanel);
    this.setTitle(LangTransMeta.translate(ZcPProBalChgConstants.FIELD_TRANS_ZC_P_PRO_MAKE_CHG_BI));
    this.setSize(UIConstants.DIALOG_0_LEVEL_WIDTH, UIConstants.DIALOG_0_LEVEL_HEIGHT);
    this.moveToScreenCenter();
    this.setVisible(true);
  }

  @Override
  public void closeDialog() {
    this.editPanel.doExit();
  }

}
