package com.ufgov.zc.client.zc.qualification.quaType.Component;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultTreeModel;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.zc.model.ZcSupQuaType;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcQuaTypeServiceDelegate;

public class ZcQuaTypeEditPanel extends AbstractMainSubEditPanel {

  private FuncButton saveButton = new SaveButton();

  private FuncButton editButton = new EditButton();

  private FuncButton previousButton = new PreviousButton();

  private FuncButton nextButton = new NextButton();

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  public IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,

  "zcEbBaseServiceDelegate");

  public IZcQuaTypeServiceDelegate zcQuaTypeServiceDelegate = (IZcQuaTypeServiceDelegate) ServiceFactory.create(IZcQuaTypeServiceDelegate.class,

  "zcQuaTypeServiceDelegate");

  public boolean isInsert = false;

  private ZcSupQuaType zcSupQuaType;

  private ZcQuaTypeSplitPanel ZcQuaTypeSplitPanel;

  private String compoId = "ZC_SUP_QUA_TYPE";

  TextFieldEditor code = new TextFieldEditor("类别编号", "typeCode");

  TextFieldEditor name = new TextFieldEditor("类别名称", "typeName");

  public ZcQuaTypeEditPanel(ZcQuaTypeSplitPanel ZcQuaTypeSplitPanel) {
    super();
    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "资质类别", TitledBorder.CENTER, TitledBorder.TOP,
      new Font("宋体", Font.BOLD, 15), Color.BLUE));
    init();

    this.ZcQuaTypeSplitPanel = ZcQuaTypeSplitPanel;
  }

  public void initToolBar(JFuncToolBar toolBar) {

    toolBar.setModuleCode("ZC");

    toolBar.setCompoId(compoId);
    toolBar.add(saveButton);
    toolBar.add(editButton);

    toolBar.add(previousButton);

    toolBar.add(nextButton);

    saveButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doSave();

      }

    });

    editButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doEdit();

      }

    });

    previousButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 上一页

        // doPrevious();

      }

    });

    nextButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        // 下一页

        // doNext();

      }

    });

  }

  public List<AbstractFieldEditor> createFieldEditors() {

    List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    editorList.add(code);

    editorList.add(name);

    return editorList;
  }

  public JComponent createSubBillPanel() {
    // TCJLODO Auto-generated method stub
    return null;
  }

  public void refreshData(ZcSupQuaType zcSupQuaType) {
    this.zcSupQuaType = zcSupQuaType;
    setEditingObject(zcSupQuaType);
    updateFiledEdited();
  }

  public void doSave() {
    boolean isCorrect = checkBeforeSave();
    if (isCorrect) {
      afterSave();
    }

  }

  public void updateFiledEdited() {
    if (isInsert) {
      code.setEnabled(true);
      name.setEnabled(true);

    } else {
      code.setEnabled(false);
      name.setEnabled(true);
    }
  }

  public boolean doDelete() {
    int num = JOptionPane.showConfirmDialog(this, "确认将删除该资质类别吗？", "操作确认", 0);
    if (num == JOptionPane.YES_OPTION) {
      //父节点移除该节点。

      zcQuaTypeServiceDelegate.deleteZcSupQuaTypeByTypeCode(zcSupQuaType.getTypeCode(), requestMeta);
      JOptionPane.showMessageDialog(this, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      ((QuaTypeTreeNode) ZcQuaTypeSplitPanel.getSelectedNode().getParent()).getUserObject().getChildrenList().remove(zcSupQuaType);
      return true;

    } else {
      return false;
    }
  }

  public void doEdit() {

  }

  public void afterSave() {

    DefaultTreeModel model = (DefaultTreeModel) ZcQuaTypeSplitPanel.treePanel.selectTree.getModel();
    // 新增
    if (isInsert) {
      QuaTypeTreeNode newChild = new QuaTypeTreeNode();
      newChild.setUserObject(zcSupQuaType);
      QuaTypeTreeNode parent = ZcQuaTypeSplitPanel.getSelectedNode();
      zcSupQuaType.setParentTypeCode(parent.getUserObject().getTypeCode());
      zcSupQuaType.setParentTypeName(parent.getUserObject().getTypeName());
      zcQuaTypeServiceDelegate.insertZcSupQuaTypeFN(zcSupQuaType, requestMeta);
      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      // 树model、树节点的编辑对象、指标集。添加上新增的对象。
      parent.getUserObject().getChildrenList().add(zcSupQuaType);
      // 保存父类编码

      model.insertNodeInto(newChild, parent, parent.getChildCount());

      ZcQuaTypeSplitPanel.setSelectedNode(newChild);

    } else {

      zcQuaTypeServiceDelegate.updateZcSupQuaTypeFN(zcSupQuaType, requestMeta);

      JOptionPane.showMessageDialog(this, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

      QuaTypeTreeNode oldChild = ZcQuaTypeSplitPanel.getSelectedNode();

      oldChild.setUserObject(zcSupQuaType);

      model.reload(oldChild);
    }

    setEditingObject(zcSupQuaType);
    ZcQuaTypeSplitPanel.repaintTree();

  }

  public boolean checkBeforeSave() {
    //校验编号的问题
    //是否存在已经在的编号
    StringBuffer errStr = new StringBuffer();
    if (zcSupQuaType == null || zcSupQuaType.getTypeCode() == null) {
      errStr.append("<html><b><font size='3' color='red'>类别编号没有填写</font></b></html>\n");
    }
    if (zcSupQuaType == null || zcSupQuaType.getTypeName() == null) {
      errStr.append("<html><b><font size='3' color='red'>类别名称没有填写</font></b></html>\n");
    }
    //校验数据库中是否已经有该编号
    if (isInsert) {
      if (zcSupQuaType != null && zcSupQuaType.getTypeCode() != null) {
        Integer it = (Integer) zcEbBaseServiceDelegate.queryObject("ZC_SUP_QUA_TYPE._isExisSameCode", zcSupQuaType.getTypeCode(), requestMeta);
        if (it != null && it.intValue() > 0) {
          errStr.append("<html><b><font size='3' color='red'>类别编号已经存在</font></b></html>\n");
        }
      }
    }

    if (errStr != null && errStr.length() > 0) {
      JOptionPane.showMessageDialog(this, errStr, "提示", JOptionPane.ERROR_MESSAGE);
      return false;
    }

    return true;
  }
}
