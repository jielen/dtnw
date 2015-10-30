package com.ufgov.zc.client.zc.project.reqfile.component.reqFileLeftTree;

import java.io.File;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import com.ufgov.zc.client.zc.ztb.util.Guid;
import com.ufgov.zc.client.zc.ztb.util.PubFunction;

/**
 * 
* @ClassName: ReqTreeNode
* @Description: 业务需求树的树节点
* @date: Oct 14, 2012 3:28:36 AM
* @version: V1.0 
* @since: 1.0
* @author: Administrator
* @modify:
 */
public class ReqTreeNode extends DefaultMutableTreeNode implements Cloneable {

  private static final long serialVersionUID = -8184626129106817806L;

  protected boolean isVisible = true;

  private String nodeGUID = Guid.genID();

  private String nodeCode;

  private String nodeName;

  private String nodeType;

  //用该字段区分是公共需求还是分包需求,默认是公共需求
  private boolean isPackReq = false;

  /**
   * 当前节点及节点父节点的全路径
   */
  private String nodesFullPath = null;

  /**
   * 当前节点目录路径名称，一般和code相同
   */
  private String nodeDirPath = "";

  private String fileExtension;

  private boolean isLocked;

  private boolean isSelected;

  //节点是否处于展开状态
  private boolean isUnfold = true;

  public ReqTreeNode() {
    this.isVisible = true;
  }

  public String getNodeGUID() {
    return nodeGUID;
  }

  public void setNodeGUID(String nodeGUID) {
    this.nodeGUID = nodeGUID;
  }

  public String getNodeCode() {
    return nodeCode;
  }

  public void setNodeCode(String nodeCode) {
    this.nodeCode = nodeCode;
  }

  public String getNodeType() {
    return nodeType;
  }

  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  public boolean isLocked() {
    return isLocked;
  }

  public void setLocked(boolean isLocked) {
    this.isLocked = isLocked;
  }

  public String getNodeDirPath() {
    return nodeDirPath;
  }

  public void setNodeDirPath(String nodeDirPath) {
    this.nodeDirPath = nodeDirPath;
  }

  public String getNodesFullPath() {
    if (!this.isRoot()) {
      String parentPath = ((ReqTreeNode) this.getParent()).getNodesFullPath();
      this.nodesFullPath = parentPath + File.separator + this.nodeDirPath;
    } else {
      this.nodesFullPath = this.nodeDirPath;
    }
    return PubFunction.deleteSpecialCode(nodesFullPath);
  }

  public String getNodesFullPathWithExtIfExist() {
    if (!this.isRoot()) {
      this.nodesFullPath = ((ReqTreeNode) this.getParent()).getNodesFullPath() + File.separator + this.nodeDirPath;
    } else {
      this.nodesFullPath = this.nodeDirPath;
    }
    if (this.isLeaf()) {
      this.nodesFullPath = this.nodesFullPath + this.getFileExtension();
    }
    return PubFunction.deleteSpecialCode(nodesFullPath);
  }

  public void setNodesFullPath(String nodesFullPath) {
    this.nodesFullPath = nodesFullPath;
  }

  public boolean isUnfold() {
    return isUnfold;
  }

  public void setUnfold(boolean isUnfold) {
    this.isUnfold = isUnfold;
  }

  public TreeNode getChildAt(int index, boolean filterIsActive) {
    if (!filterIsActive) {
      return super.getChildAt(index);
    }
    if (children == null) {
      throw new ArrayIndexOutOfBoundsException("node has no children");
    }
    int realIndex = -1;
    int visibleIndex = -1;
    Enumeration e = children.elements();
    while (e.hasMoreElements()) {
      ReqTreeNode node = (ReqTreeNode) e.nextElement();
      if (node.isVisible()) {
        visibleIndex++;
      }
      realIndex++;
      if (visibleIndex == index) {
        return (TreeNode) children.elementAt(realIndex);
      }
    }
    throw new ArrayIndexOutOfBoundsException("index unmatched");
    //return (TreeNode)children.elementAt(index);
  }

  public int getChildCount(boolean filterIsActive) {
    if (!filterIsActive) {
      return super.getChildCount();
    }
    if (children == null) {
      return 0;
    }
    int count = 0;
    Enumeration e = children.elements();
    while (e.hasMoreElements()) {
      ReqTreeNode node = (ReqTreeNode) e.nextElement();
      if (node.isVisible()) {
        count++;
      }
    }
    return count;
  }

  public void setVisible(boolean visible) {
    this.isVisible = visible;
  }

  public boolean isVisible() {
    return isVisible;
  }

  public boolean isSelected() {
    return isSelected;
  }

  public void setSelected(boolean isSelected) {
    this.isSelected = isSelected;
  }

  /**
   * @return the fileExtension
   */
  public String getFileExtension() {
    return fileExtension;
  }

  /**
   * @param fileExtension the fileExtension to set
   */
  public void setFileExtension(String fileExtension) {
    this.fileExtension = fileExtension;
  }

  /**
   * @return the nodeName
   */
  public String getNodeName() {
    return nodeName;
  }

  /**
   * @param nodeName the nodeName to set
   */
  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

  public String toString() {
    return nodeName;
  }

  /**
   * @return the isPackReq
   */
  public boolean isPackReq() {
    return isPackReq;
  }

  /**
   * @param isPackReq the isPackReq to set
   */
  public void setPackReq(boolean isPackReq) {
    this.isPackReq = isPackReq;
  }

}
