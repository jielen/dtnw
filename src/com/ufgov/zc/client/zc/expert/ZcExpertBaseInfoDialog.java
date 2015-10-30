package com.ufgov.zc.client.zc.expert;
import java.awt.BorderLayout;import java.awt.Dialog;import java.util.List;import com.ufgov.zc.client.common.ListCursor;import com.ufgov.zc.client.common.UIConstants;import com.ufgov.zc.client.component.GkBaseDialog;
public class ZcExpertBaseInfoDialog extends GkBaseDialog {
  private ZcExpertBaseInfoDialog self = this;
  private ZcExpertBaseInfoEditPanel editPanel;
  private ZcExpertBaseInfoListPanel listPanel;
  public ZcExpertBaseInfoDialog(ZcExpertBaseInfoListPanel listPanel, List beanList, int editingRow, String tabStatus) {
    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);
    this.listPanel = listPanel;
    editPanel = new ZcExpertBaseInfoEditPanel(this.self, new ListCursor(beanList, editingRow), tabStatus, listPanel);
    setLayout(new BorderLayout());
    add(editPanel);
    this.setTitle("专家登记");
    this.setSize(UIConstants.DIALOG_0_LEVEL_WIDTH, UIConstants.DIALOG_0_LEVEL_HEIGHT);    this.moveToScreenCenter();
    this.setVisible(true);         
  }
  @Override
  public void closeDialog() {
    this.editPanel.doExit();
  }
}
