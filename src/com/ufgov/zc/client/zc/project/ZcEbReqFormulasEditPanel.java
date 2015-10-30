package com.ufgov.zc.client.zc.project;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import com.ufgov.zc.client.common.AsOptionMeta;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.AddFormulaButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcEbRequirement;

public class ZcEbReqFormulasEditPanel extends JPanel {

  private final ZcEbReqFormulaJTabbedPane zcEbReqFormulaJTabbedPane;

  private final List formulaPackList;

  private List packList;

  private ZcBaseBill zcBaseBil;

  protected JFuncToolBar toolBar = new JFuncToolBar();

  public FuncButton addFormulaButton = new AddFormulaButton();

  public boolean saveButtonisVisible = false;

  public boolean formulaCanDelete = false;

  public ZcEbReqFormulasEditPanel(List formulaPackList, List packList, ZcBaseBill zcBaseBil, boolean saveButtonisVisible, boolean formulaCanDelete) {
    this.formulaPackList = formulaPackList;
    this.packList = packList;
    this.zcBaseBil = zcBaseBil;
    this.saveButtonisVisible = saveButtonisVisible;
    this.formulaCanDelete = formulaCanDelete;
    zcEbReqFormulaJTabbedPane = new ZcEbReqFormulaJTabbedPane(formulaPackList, packList, zcBaseBil, saveButtonisVisible, formulaCanDelete);
    initComponet();

  }

  public void refreshData(ZcEbRequirement zcEbRequirement) {
    this.zcBaseBil = zcEbRequirement;
    zcEbRequirement.setFormulaPackList(this.formulaPackList);
    refreshZcEbReqFormulaJTabbedPanePackInfo(zcEbRequirement.getPackList());

  }

  private void initComponet() {
    initToolBar();

    this.setLayout(new BorderLayout());

    this.add(toolBar, BorderLayout.NORTH);
    this.add(zcEbReqFormulaJTabbedPane, BorderLayout.CENTER);

  }

  public void initToolBar() {

    toolBar.add(addFormulaButton);

    addFormulaButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // 新增评标方法
        zcEbReqFormulaJTabbedPane.addFormula();
      }
    });

    setButtonVisible();
  }

  public void setButtonVisible() {
    //只有预算单位有新增评标方法的按钮，而是是在标段的数量大于1的情况下
    if (WorkEnv.getInstance().containRole((AsOptionMeta.getOptVal(ZcElementConstants.OPT_ZC_YSDWCG_ROLE))) && packList.size() > 1) {
      addFormulaButton.setVisible(true);
    } else {
      addFormulaButton.setVisible(false);
    }

  }

  public void doEdit(boolean isVisiable) {
    if (isVisiable) {
      setButtonVisible();
    } else {
      addFormulaButton.setVisible(false);
    }
    zcEbReqFormulaJTabbedPane.doEdit(isVisiable);

  }

  public boolean isDataChanged() {
    return zcEbReqFormulaJTabbedPane.isDataChanged();

  }

  public void setOldObject() {
    zcEbReqFormulaJTabbedPane.setOldObject();
  }

  public boolean checkBeforeSave() {
    return zcEbReqFormulaJTabbedPane.checkBeforeSave();
  }

  public void refreshZcEbReqFormulaJTabbedPanePackInfo(List packList) {
    this.packList = packList;
    setButtonVisible();
    zcEbReqFormulaJTabbedPane.refreshZcEbReqFormulaJTabbedPanePackInfo(packList);
  }
}
