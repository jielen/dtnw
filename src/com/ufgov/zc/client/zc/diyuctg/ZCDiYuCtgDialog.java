/*
 * *
 *  Copyright 2012 by Beijing UFIDA Government Affairs Software Co.,Ltd.,
 *  All rights reserved.
 *
 *  版权所有：北京用友政务软件有限公司
 *  未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 *  侵权者将受到法律追究。
 * /
 */

package com.ufgov.zc.client.zc.diyuctg;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.component.GkBaseDialog;

import java.awt.*;
import java.util.List;

/**
 * <p>PURPOSE:
 * <p>DESCRIPTION:
 * <p>CALLED BY:	qianmingjin
 * <p>CREATE DATE:  12-3-26
 * <p>UPDATE DATE: 12-3-26
 * <p>UPDATE USER: qianmingjin
 * <p>HISTORY:		1.0
 *
 * @author qianmingjin
 * @version 1.0
 * @see
 * @since java 1.5.0
 */
public class ZCDiYuCtgDialog extends GkBaseDialog {
  private ZCDiYuCtgDialog self = this;

  private ZCDiYuCtgEditPanel editPanel;

  private ZCDiYuCtgListPanel listPanel;

  public ZCDiYuCtgDialog(ZCDiYuCtgListPanel listPanel, List beanList, int editingRow, String tabStatus, String pageStatus) {
    super(listPanel.getParentWindow(), ModalityType.APPLICATION_MODAL);

    this.listPanel = listPanel;

    editPanel = new ZCDiYuCtgEditPanel(this.self, new ListCursor(beanList, editingRow), tabStatus, listPanel, pageStatus);

    setLayout(new BorderLayout());

    add(editPanel);

    this.setTitle("地域分类");

    this.setMaxSizeWindow();

    this.moveToScreenCenter();

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
