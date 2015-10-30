/**
 * 
 */
package com.ufgov.zc.client.zc.eval.result;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Window;
import java.util.List;

import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.common.zc.model.ZcEbProj;
import com.ufgov.zc.common.zc.model.ZcEbRfqPack;

/**
 * @author Administrator
 *
 */
public class ZcEbReportOffLineDialog extends GkBaseDialog {

  private static final long serialVersionUID = 1L;

  private ZcEbReportOffLineDialog self = this;

  private ZcEbEvalReportOffLineEditPanel editPanel;

  private ZcEbEvalReportOffLineListPanel listPanel;

  public ZcEbReportOffLineDialog(ZcEbEvalReportOffLineListPanel listPanel, List beanList, int editingRow, String tabStatus) {

    super(listPanel.getParentWindow(), Dialog.ModalityType.APPLICATION_MODAL);

    this.listPanel = listPanel;

    editPanel = new ZcEbEvalReportOffLineEditPanel(this.self, new ListCursor(beanList, editingRow), tabStatus, listPanel);

    setLayout(new BorderLayout());

    add(editPanel);

    this.setTitle(LangTransMeta.translate("ZC_EB_EVAL_REPORT_OFF_LINE"));

    this.setMaxSizeWindow();

    this.moveToScreenCenter();

    this.setVisible(true);

  }
  public ZcEbReportOffLineDialog(Window window, List beanList, int editingRow, String tabStatus,BillElementMeta billElementMeta, ZcEbProj zcEbProj, ZcEbRfqPack zcEbRfqPack) {

    super(window, Dialog.ModalityType.APPLICATION_MODAL);

//    this.listPanel = listPanel;

    editPanel = new ZcEbEvalReportOffLineEditPanel(this.self, new ListCursor(beanList, editingRow), tabStatus, listPanel,billElementMeta,zcEbProj,zcEbRfqPack);

    setLayout(new BorderLayout());

    add(editPanel);

    this.setTitle("评审结果报告");

    this.setMaxSizeWindow();

    this.moveToScreenCenter();

    this.setVisible(true);
    

  }


  @Override
  public void closeDialog() {

    this.editPanel.doExit();

  }
  public ZcEbEvalReportOffLineEditPanel getEditPanel() {
    return editPanel;
  }
  public void setEditPanel(ZcEbEvalReportOffLineEditPanel editPanel) {
    this.editPanel = editPanel;
  }

}

