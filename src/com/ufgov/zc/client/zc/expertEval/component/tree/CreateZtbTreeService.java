/**   * @(#) project: ZFCG_ST* @(#) file: CreateZtbTreeService.java* * Copyright 2011 UFGOV, Inc. All rights reserved.* UFGOV PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.* */package com.ufgov.zc.client.zc.expertEval.component.tree;import java.util.ArrayList;import java.util.Enumeration;import java.util.List;import javax.swing.JTree;import javax.swing.tree.DefaultTreeModel;import javax.swing.tree.TreePath;import com.ufgov.zc.client.zc.ztb.dao.LocalProjectDAO;import com.ufgov.zc.client.zc.ztb.dao.ReadObjectFileToObjectDao;import com.ufgov.zc.client.zc.ztb.model.SmartTreeNode;import com.ufgov.zc.client.zc.ztb.util.GV;/*** @ClassName: CreateZtbTreeService* @Description: TODO(这里用一句话描述这个类的作用)* @date: 2011-7-8 下午06:04:16* @version: V1.0 * @since: 1.0* @author: fanpl* @modify: */public class CreateZtbTreeService {  private LocalProjectDAO projectDAO = LocalProjectDAO.getInstance();  private ReadObjectFileToObjectDao readTbFileDao = ReadObjectFileToObjectDao.getInstance();  private String nodeSettingXmlPath;  public String getNodeSettingXmlPath() {    return nodeSettingXmlPath;  }  public void setNodeSettingXmlPath(String nodeSettingXmlPath) {    this.nodeSettingXmlPath = nodeSettingXmlPath;  }  public ZtbFileTree createZTBSTreePanel(String fileName, String packCode) {    ZtbFileTree ztbFileTree = new ZtbFileTree(createZTBSTreeRoot(fileName));    //多标段的隐藏    hideNode(ztbFileTree, packCode);    expandOrClose(ztbFileTree);    return ztbFileTree;  }  //创建指标响应树  public ZtbFileTree createResponseTreePanel(String fileName, String packCode) {    ZtbFileTree ztbFileTree = new ZtbFileTree(createResponseTreeRoot(fileName));    return ztbFileTree;  }  private void hideNode(ZtbFileTree ztbFileTree, String packCode) {    List<SmartTreeNode> hideNodes = new ArrayList();    SmartTreeNode rootNode = ztbFileTree.getTreeNode();    hidePackNode(rootNode, packCode, hideNodes);    for (int i = 0; i < hideNodes.size(); i++) {      ((DefaultTreeModel) ztbFileTree.getTree().getModel()).removeNodeFromParent(hideNodes.get(i));    }    ztbFileTree.getTree().validate();    ztbFileTree.getTree().repaint();  }  private void hidePackNode(SmartTreeNode projNode, String packCode, List<SmartTreeNode> hideNodes) {    Enumeration<?> enumeration = projNode.children();    while (enumeration.hasMoreElements()) {      SmartTreeNode node = (SmartTreeNode) enumeration.nextElement();      if (!node.getNodeCode().equals(packCode)) {        hideNodes.add(node);      } else {        hideZTBNode(node, hideNodes);      }    }  }  public SmartTreeNode createZTBSTreeRoot(String fileName) {    return projectDAO.readXmlToObject(fileName);  }  public SmartTreeNode createResponseTreeRoot(String fileName) {    try {      return readTbFileDao.readTreeNodes(fileName);    } catch (Exception e) {      e.printStackTrace();    }    return null;  }  public void expandOrClose(ZtbFileTree ztbFileTree) {    if (ztbFileTree != null) {      JTree jTree = ztbFileTree.getTree();      expandAll(jTree, new TreePath(jTree.getModel().getRoot()), true);    }  }  private void hideZTBNode(SmartTreeNode packCodeNode, List<SmartTreeNode> hideNodes) {    Enumeration<?> enumeration = packCodeNode.children();    while (enumeration.hasMoreElements()) {      SmartTreeNode node = (SmartTreeNode) enumeration.nextElement();      if (node.getNodeType().equals(GV.NODE_TYPE_ZB)) {        hideNodes.add(node);      }    }  }  public void expandAll(JTree tree, TreePath parent, boolean expand) {    SmartTreeNode node = (SmartTreeNode) parent.getLastPathComponent();    if (node.getChildCount() >= 0) {      for (Enumeration e = node.children(); e.hasMoreElements();) {        SmartTreeNode n = (SmartTreeNode) e.nextElement();        TreePath path = parent.pathByAddingChild(n);        expandAll(tree, path, expand);      }    }    if (expand) {      tree.expandPath(parent);    } else {      tree.collapsePath(parent);    }  }}