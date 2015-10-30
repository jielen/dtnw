/**   
* @(#) project: zcxa
* @(#) file: ZcPProBalDialog.java
* 
* Copyright 2010 UFGOV, Inc. All rights reserved.
* UFGOV PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
* 
*/
package com.ufgov.zc.client.zc.zcpprobal;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.util.List;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.component.GkBaseDialog;

/**
* @ClassName: ZcPProBalDialog
* @Description: TODO(这里用一句话描述这个类的作用)
* @date: 2010-8-2 下午06:41:51
* @version: V1.0 
* @since: 1.0
* @author: Administrator
* @modify: 
*/
public class ZcPProBalDialog extends GkBaseDialog {
  private static final long serialVersionUID = 1L;

  private ZcPProBalDialog self = this;

  private ZcPProBalEditPanel editPanel;

  private ZcPProBalListPanel listPanel;

  public ZcPProBalDialog(ZcPProBalListPanel listPanel, List beanList, int editingRow, String tabStatus) {
    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);
    this.listPanel = listPanel;
    editPanel = new ZcPProBalEditPanel(this.self, new ListCursor(beanList, editingRow), tabStatus, listPanel);
    setLayout(new BorderLayout());
    add(editPanel);
    this.setTitle("资金支付申请单");
    this.setSize(UIConstants.DIALOG_0_LEVEL_WIDTH, UIConstants.DIALOG_2_LEVEL_HEIGHT);
    this.moveToScreenCenter();
    this.setVisible(true);
  }

  @Override
  public void closeDialog() {
    this.editPanel.doExit();
  }

}
