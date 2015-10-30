package com.ufgov.zc.client.zc.experttype;import java.awt.BorderLayout;import java.awt.Dialog;import java.awt.Dimension;import java.util.List;import javax.swing.JDialog;import com.ufgov.zc.client.common.ListCursor;import com.ufgov.zc.client.component.GkBaseDialog;@SuppressWarnings("unchecked")public class ZcEmExpertTypeDialog extends GkBaseDialog {  private static final long serialVersionUID = -3578583561581905765L;  private ZcEmExpertTypeDialog self = this;  private ZcEmExpertTypeListPanel listPanel;  private ZcEmExpertTypeEditPanel editPanel;  public ZcEmExpertTypeDialog(ZcEmExpertTypeListPanel listPanel, List beanList, int editingRow,  String tabStatus) {    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);    this.listPanel = listPanel;    editPanel = new ZcEmExpertTypeEditPanel(this.self, new ListCursor(beanList, editingRow), tabStatus,    this.listPanel);    setLayout(new BorderLayout());    add(editPanel);    this.setTitle(listPanel.getTitle());    this.setMaxSizeWindow();    this.setMinimumSize(new Dimension(800, 600));    setVisible(true);    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);  }}