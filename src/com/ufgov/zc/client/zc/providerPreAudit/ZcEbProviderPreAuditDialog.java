/** *  */package com.ufgov.zc.client.zc.providerPreAudit;import com.ufgov.zc.client.common.ListCursor;import com.ufgov.zc.client.common.UIConstants;import com.ufgov.zc.client.component.GkBaseDialog;import java.awt.BorderLayout;import java.awt.Dialog;import java.util.List;/** * @author Administrator * */public class ZcEbProviderPreAuditDialog extends GkBaseDialog {  private static final long serialVersionUID = 1L;  private ZcEbProviderPreAuditDialog self = this;  private ZcEbProviderPreAuditEditPanel editPanel;  private ZcEbProviderPreAuditListPanel listPanel;  public ZcEbProviderPreAuditDialog(ZcEbProviderPreAuditListPanel listPanel, List beanList, int editingRow, String tabStatus) {    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);    this.listPanel = listPanel;    editPanel = new ZcEbProviderPreAuditEditPanel(this.self, new ListCursor(beanList, editingRow), tabStatus, listPanel);    setLayout(new BorderLayout());    add(editPanel);    this.setTitle("供应商资格初审");    this.setSize(UIConstants.DIALOG_1_LEVEL_WIDTH, UIConstants.DIALOG_1_LEVEL_HEIGHT);    this.moveToScreenCenter();    this.pack();    this.setVisible(true);  }  @Override  public void closeDialog() {    this.editPanel.doExit();  }}