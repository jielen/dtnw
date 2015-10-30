package com.ufgov.zc.client.zc.project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.FileFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextAreaFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.util.GridBagLayoutTools;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcEbRequirementConfirm;

public class ZcEbReqConfimPanel extends JPanel {
  private ZcEbRequirementConfirm zcEbRequirementConfirm;

  private ZcEbRequirementConfirm oldzcEbRequirementConfirm;

  private GridBagLayoutTools tool = new GridBagLayoutTools();

  private List<AbstractFieldEditor> fieldEditors = new ArrayList<AbstractFieldEditor>();

  private ZcEbReqConfirmJTabbedPane zcEbReqConfirmJTabbedPane;

  public ZcEbReqConfimPanel(ZcEbRequirementConfirm zcEbRequirementConfirm, ZcEbReqConfirmJTabbedPane parentPanel) {
    this.zcEbRequirementConfirm = zcEbRequirementConfirm;
    this.zcEbReqConfirmJTabbedPane = parentPanel;
    initComponet();
    setOldObject();
  }

  private void initComponet() {
    JPanel edPanel = new JPanel();
    createfieldEditorList(fieldEditors);
    tool.setColCount(3);
    tool.setFieldEditorList(fieldEditors);
    tool.layoutFieldEditorPanel(edPanel, ZcEbRequirementConfirm.class, "ZC_EB_REQUIREMENT");
    tool.setCurrEditingObject(zcEbRequirementConfirm);
    tool.setOldObject(zcEbRequirementConfirm);
    this.setLayout(new BorderLayout());
    edPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "采购需求", TitledBorder.CENTER, TitledBorder.TOP, new Font(
      "宋体", Font.BOLD, 15), Color.BLUE));
    this.add(edPanel, BorderLayout.CENTER);

  }

  public void refreshCurrentEditObject(ZcEbRequirementConfirm zcEbRequirementConfirm) {
    tool.setCurrEditingObject(zcEbRequirementConfirm);
    tool.setOldObject(zcEbRequirementConfirm);
  }

  private void createfieldEditorList(List<AbstractFieldEditor> editorList) {
    TextFieldEditor expertName = new TextFieldEditor("采购人评委代表", "expertName");
    editorList.add(expertName);
    TextFieldEditor expertDuty = new TextFieldEditor("采购人评委代表职务（职称）", "expertDuty");
    editorList.add(expertDuty);
    FileFieldEditor expertWeituoName = new FileFieldEditor("委托书附件", "expertWeituoName", "expertWeituoBlobid");
    editorList.add(expertWeituoName);
    AsValFieldEditor isBidBond = new AsValFieldEditor("是否收取履约保证金", "isBidBond", "VS_Y/N");
    editorList.add(isBidBond);
    AsValFieldEditor isMargin = new AsValFieldEditor("是否收取质量保证金", "isMargin", "VS_Y/N");
    TextFieldEditor marginPayType = new TextFieldEditor("质保金收取方式", "marginPayType");
    editorList.add(marginPayType);
    editorList.add(isMargin);
    AsValFieldEditor isOverBudget = new AsValFieldEditor("是否接受超过采购预算的投标", "isOverBudget", "VS_Y/N");
    editorList.add(isOverBudget);
    AsValFieldEditor isQa = new AsValFieldEditor("是否需要召开答疑会、考察现场、专家论证", "isQa", "VS_Y/N");
    editorList.add(isQa);
    //      AsValFieldEditor formulaType = new AsValFieldEditor("评标办法", "formulaType", "ZC_VS_FORMULA_FACTORTYPE");
    //      editorList.add(formulaType);
    TextFieldEditor guaranteeDate = new TextFieldEditor("对所需货物质量保证期限要求", "guaranteeDate");
    editorList.add(guaranteeDate);
    TextFieldEditor deliveryAddress = new TextFieldEditor("交货时间和地点", "deliveryAddress");
    editorList.add(deliveryAddress);
    TextFieldEditor zcBiDesc = new TextFieldEditor("付款时间和条件", "zcBiDesc");
    editorList.add(zcBiDesc);
    TextFieldEditor bondReturnCond = new TextFieldEditor("履约金返还时间和条件", "bondReturnCond");
    editorList.add(bondReturnCond);
    TextFieldEditor marginReturnCond = new TextFieldEditor("质保金返还时间和条件", "marginReturnCond");
    editorList.add(marginReturnCond);
    TextAreaFieldEditor afterSalesSer = new TextAreaFieldEditor("售后服务的要求", "afterSalesSer", 400, 3, 3);
    editorList.add(afterSalesSer);
    TextAreaFieldEditor privateQu = new TextAreaFieldEditor("供应商资格条件", "providerQu", 400, 3, 3);
    editorList.add(privateQu);
    FileFieldEditor providerQuName = new FileFieldEditor("供应商资格条件附件", "providerQuName", "providerQuBlobid");
    editorList.add(providerQuName);
    TextFieldEditor remark = new TextFieldEditor("其他要求", "remark");
    editorList.add(remark);
  }

  private void setOldObject() {
    oldzcEbRequirementConfirm = (ZcEbRequirementConfirm) ObjectUtil.deepCopy(zcEbRequirementConfirm);
  }

  public boolean isDataChanged() {
    boolean b = !DigestUtil.digest(oldzcEbRequirementConfirm).equals(DigestUtil.digest(zcEbRequirementConfirm));
    zcEbRequirementConfirm.isDataChanged = b;
    return b;
  }

  protected void updateFieldEditorsEditable() {

    for (AbstractFieldEditor fd : this.fieldEditors) {

    }
  }

  public boolean checkBeforeSave() {
    List notNullConfirmBillElementList = zcEbReqConfirmJTabbedPane.confirmBillElementMeta.getNotNullBillElement();
    String validateConfirmInfo = ZcUtil.validateBillElementNull(zcEbRequirementConfirm, notNullConfirmBillElementList);
    StringBuilder errorInfo = new StringBuilder();
    if (validateConfirmInfo.length() != 0) {
      errorInfo.append("业务需求").append("：\n").append(validateConfirmInfo.toString()).append("\n");
    }
    if (errorInfo.length() != 0) {
      JOptionPane.showMessageDialog(this, errorInfo.toString(), "提示", JOptionPane.WARNING_MESSAGE);
      return false;
    }
//    if (!isDataChanged()) {
//    	JOptionPane.showMessageDialog(this, "数据没有发生改变，不用保存.", "提示", JOptionPane.INFORMATION_MESSAGE);
//    	return false;
//    }
    return true;
  }

}
