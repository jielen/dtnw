package com.ufgov.zc.client.zc.question;import java.awt.BorderLayout;import java.awt.Dialog;import java.util.List;import com.ufgov.zc.client.common.ListCursor;import com.ufgov.zc.client.component.GkBaseDialog;public class ZcEbComplainDialog extends GkBaseDialog {  private static final long serialVersionUID = 1L;  private ZcEbComplainDialog self = this;  private ZcEbComplainEditPanel editPanel;  @SuppressWarnings("unchecked")  public ZcEbComplainDialog(ZcEbComplainListPanel listPanel, List beanList,  int editingRow, String tabStatus) {    super(listPanel.getParentWindow(),    Dialog.ModalityType.APPLICATION_MODAL);    editPanel = new ZcEbComplainEditPanel(this.self, new ListCursor(    beanList, editingRow), tabStatus, listPanel);    setLayout(new BorderLayout());    add(editPanel);    this.setTitle("投诉");    this.setMaxSizeWindow();    this.moveToScreenCenter();    this.setVisible(true);  }}