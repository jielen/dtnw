package com.ufgov.zc.client.zc.zcebjiexiang;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.util.List;

import javax.swing.JDialog;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.component.GkBaseDialog;

public class ZcEbJieXiangDialog extends GkBaseDialog {

  private static final long serialVersionUID = 7634973014493149416L;

  private ZcEbJieXiangDialog self = this;

  private ZcEbJieXiangListPanel listPanel;

  private ZcEbJieXiangEditPanel editPanel;

  public ZcEbJieXiangDialog(ZcEbJieXiangListPanel listPanel, List beanList, int editingRow,

  String tabStatus) {

    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);

    this.listPanel = listPanel;

    editPanel = new ZcEbJieXiangEditPanel(this.self, new ListCursor(beanList, editingRow), tabStatus, this.listPanel);


    setLayout(new BorderLayout());

    add(editPanel);

    this.setTitle(listPanel.getTitle());

    //this.setMaxSizeWindow();

    this.setSize(UIConstants.DIALOG_0_LEVEL_WIDTH, UIConstants.DIALOG_0_LEVEL_HEIGHT);

    this.setMinimumSize(new Dimension(800, 600));

    setVisible(true);

    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    this.addWindowListener(new WindowAdapter() {

      //      public void windowClosing(WindowEvent e) {

      //        if (editPanel.timer != null) {

      //          editPanel.timer.cancel();

      //        }

      //        dispose();

      //      }

      //

      //      public void windowDeactivated(WindowEvent e) {

      //        if (editPanel.timer != null) {

      //          editPanel.timer.cancel();

      //        }

      //        dispose();

      //      }

    });

  }

}
