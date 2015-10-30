/**
 * 
 */
package com.ufgov.zc.client.zc.zcproend;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.util.List;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.component.GkBaseDialog;

/**
 * 支持挂接手工挂接资金的结转界面
 * @author Administrator
 *
 */
public class ZcProOrYearEndEditDialogDt extends GkBaseDialog {

  private ZcProOrYearEndEditDialogDt self = this;

  private ZcProOrYearEndListPanel listPanel;

  private ZcProOrYearEndEditPanelDt editPanel;

  public ZcProOrYearEndEditDialogDt(ZcProOrYearEndListPanel listPanel, List beanList, int editingRow, String tabStatus) {

    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);

    this.listPanel = listPanel;

    editPanel = new ZcProOrYearEndEditPanelDt(this.self, new ListCursor(beanList, editingRow), tabStatus, listPanel);

    setLayout(new BorderLayout());

    add(editPanel);

    this.setTitle("结转项目资金管理");

    this.setSize(UIConstants.DIALOG_0_LEVEL_WIDTH, UIConstants.DIALOG_0_LEVEL_HEIGHT);

    this.moveToScreenCenter();

    this.setVisible(true);

  }
}

