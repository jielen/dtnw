package com.ufgov.zc.client.zc.qualification.quaType.Component;

import java.awt.Dialog;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.JButtonTextField;
import com.ufgov.zc.client.component.JTreeSelectDialog;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityDataCache;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.zc.qualification.quaType.service.InitQuaTypeTreeService;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcSupQuaType;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;

public class ZcQuaTypeTreeSelectDialog extends JTreeSelectDialog {

  protected TextFieldEditor triggerFieldName;

  protected boolean selectedTailFlag = false;

  private InitQuaTypeTreeService initQuaTypeTreeService;

  public IZcEbBaseServiceDelegate zcEbBaseServiceDelegate;

  public ZcQuaTypeTreeSelectDialog(Dialog dialog, boolean modal, JButtonTextField triggerField) {

    super(dialog, modal, triggerField);

    this.triggerFieldName = triggerFieldName;

  }

  public ZcQuaTypeTreeSelectDialog(Dialog dialog, boolean modal, JButtonTextField triggerField, boolean selectedTailFlag) {

    super(dialog, modal, triggerField, selectedTailFlag);

    this.triggerFieldName = triggerFieldName;

    this.selectedTailFlag = selectedTailFlag;

  }

  public void initTitle() {

    this.setTitle("供应商资质");

  }

  protected void initDataBufferList() {

    RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

    ElementConditionDto dto = new ElementConditionDto();

    dataBufferList = (List) ForeignEntityDataCache.getDataMap().get(ForeignEntityDataCache.CACHE_ZC_B_CATALOGUE);

    if (dataBufferList != null && dataBufferList.size() > 0) {

    } else {
      getService();

      dataBufferList = zcEbBaseServiceDelegate.queryDataForList("ZC_SUP_QUA_TYPE._selectSupQuaList", null, requestMeta);
      if (dataBufferList == null) {
        dataBufferList = new ArrayList();
      }

      ForeignEntityDataCache.setData(ForeignEntityDataCache.CACHE_ZC_B_CATALOGUE, dataBufferList);

    }
    if (dataBufferList != null) {
      for (int i = 0; i < dataBufferList.size(); i++) {
        ZcSupQuaType type = (ZcSupQuaType) dataBufferList.get(i);
        triggerField.dataMap.put(type.getTypeCode(), type);
      }
    }

    initSelectTree();

  }

  private synchronized void getService() {
    if (zcEbBaseServiceDelegate == null) {
      zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class, "zcEbBaseServiceDelegate");
    }
  }

  protected void initSelectTree() {

    QuaTypeTreeNode root = new QuaTypeTreeNode();

    ZcSupQuaType rootObject = new ZcSupQuaType();
    rootObject.setTypeCode("root");
    rootObject.setTypeName("供应商资质类型");

    root.setUserObject(rootObject);

    List companyTreeList = genTreeData();

    for (Object o : companyTreeList) {

      ZcSupQuaType nodeValueObj = (ZcSupQuaType) o;

      QuaTypeTreeNode node = new QuaTypeTreeNode(nodeValueObj);

      root.add(node);
      treeNodeMap.put(nodeValueObj, node);
      this.setChildNode(nodeValueObj, node);

    }

    this.getSelectTree().setModel(new DefaultTreeModel(root));
  }

  //展现树结点

  public void expandAll(JTree tree, TreePath parent, boolean expand) {

    QuaTypeTreeNode node = (QuaTypeTreeNode) parent.getLastPathComponent();

    if (node.getChildCount() >= 0) {

      for (Enumeration e = node.children(); e.hasMoreElements();) {

        QuaTypeTreeNode n = (QuaTypeTreeNode) e.nextElement();

        TreePath path = parent.pathByAddingChild(n);

        expandAll(tree, path, expand);

      }

    }

    if (expand) {

      tree.expandPath(parent);

    } else {

      tree.collapsePath(parent);

    }

  }

  private void setChildNode(ZcSupQuaType userObject, QuaTypeTreeNode node) {

    if (userObject.getChildrenList().size() > 0) {

      for (Object o : userObject.getChildrenList()) {
        ZcSupQuaType c = (ZcSupQuaType) o;
        QuaTypeTreeNode childNode = new QuaTypeTreeNode(c);
        node.add(childNode);
        treeNodeMap.put(c, childNode);
        setChildNode(c, childNode);
      }
    }
  }

  private List genTreeData() {

    List filteredList = dataBufferList;

    Map map = new HashMap();

    for (Object o : filteredList) {

      ZcSupQuaType type = (ZcSupQuaType) o;

      map.put(type.getTypeCode(), type);

    }

    List rootTypeList = new ArrayList();

    List childrenTypeList = new ArrayList();

    for (Object o : filteredList) {

      ZcSupQuaType cpy = (ZcSupQuaType) o;

      if (map.get(cpy.getParentTypeCode()) == null) {

        rootTypeList.add(cpy);

      } else {

        childrenTypeList.add(cpy);

      }

    }

    Map childrenMap = new HashMap();

    for (int i = 0; i < childrenTypeList.size(); i++) {

      ZcSupQuaType child = (ZcSupQuaType) childrenTypeList.get(i);

      List childrenList = (List) childrenMap.get(child.getParentTypeCode());

      if (childrenList != null) {

        childrenList.add(child);

      } else {

        List tempList = new ArrayList();

        tempList.add(child);

        childrenMap.put(child.getParentTypeCode(), tempList);

      }

    }

    for (int i = 0; i < rootTypeList.size(); i++) {

      ZcSupQuaType rootType = (ZcSupQuaType) rootTypeList.get(i);

      this.setTpyeChildren(rootType, childrenMap);

    }

    return rootTypeList;

  }

  private void setTpyeChildren(ZcSupQuaType rootType, Map childrenMap) {

    List childrenList = (List) childrenMap.get(rootType.getTypeCode());

    if (childrenList != null) {

      rootType.setChildrenList(childrenList);

      for (int i = 0; i < childrenList.size(); i++) {

        ZcSupQuaType c = (ZcSupQuaType) childrenList.get(i);

        setTpyeChildren(c, childrenMap);

      }

    }

  }
}
