/**
 * 
 */
package com.ufgov.zc.client.zc.bulletin.change;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Toolkit;
import java.util.List;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.component.GkBaseDialog;

/**
 * 招标公告/采购公告
 * 
 * @author Administrator
 * 
 */
public class ZcEbBulletinChangeDialog extends GkBaseDialog {

  private static final long serialVersionUID = 1L;

  private ZcEbBulletinChangeDialog self = this;

  private ZcEbBulletinChangeEditPanel editPanel;

  private ZcEbBulletinChangeListPanel listPanel;

  public ZcEbBulletinChangeDialog(ZcEbBulletinChangeListPanel listPanel, List beanList, int editingRow, String tabStatus) {

    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);

    this.listPanel = listPanel;

    editPanel = new ZcEbBulletinChangeEditPanel(this.self, new ListCursor(beanList, editingRow), tabStatus, listPanel);

    setLayout(new BorderLayout());

    add(editPanel);

    this.setTitle( listPanel.title);
    int WINDOW_WIDTH=Toolkit.getDefaultToolkit().getScreenSize().width-20;
    int WINDOW_HEIGHT=Toolkit.getDefaultToolkit().getScreenSize().height-50;
    int WINDOW_LEFT = 5;
    int WINDOW_TOP = 5;
//  this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
//  this.setLocation(WINDOW_LEFT, WINDOW_TOP);

    this.setSize(UIConstants.DIALOG_3_LEVEL_WIDTH, UIConstants.DIALOG_3_LEVEL_HEIGHT);

    this.moveToScreenCenter();
    this.pack();
    
    editPanel.refreshData();

    this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    this.setLocation(WINDOW_LEFT, WINDOW_TOP);
    
    this.setVisible(true);
  }

  /* (non-Javadoc)

   * @see com.ufgov.gk.client.component.GkBaseDialog#closeDialog()

   */

  @Override
  public void closeDialog() {

    this.editPanel.doExit();

  }

}
