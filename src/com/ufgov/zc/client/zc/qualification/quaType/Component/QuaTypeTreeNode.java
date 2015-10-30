package com.ufgov.zc.client.zc.qualification.quaType.Component;

import javax.swing.tree.DefaultMutableTreeNode;

import com.ufgov.zc.common.zc.model.ZcSupQuaType;

public class QuaTypeTreeNode extends DefaultMutableTreeNode {

  protected String code;

  protected String parentCode;

  public final static int SINGLE_SELECTION = 0;

  public final static int DIG_IN_SELECTION = 4;

  public QuaTypeTreeNode() {

    this(null);

  }

  public QuaTypeTreeNode(Object userObject) {

    super(userObject, true);

  }

  public ZcSupQuaType getUserObject() {

    return (ZcSupQuaType) super.getUserObject();

  }

  public String getCode() {

    return getUserObject().getTypeCode();

  }

  public void setCode(String code) {

    this.code = code;

  }

  public String getParentCode() {

    return getUserObject().getParentTypeCode();

  }

  public void setParentCode(String parentCode) {

    this.parentCode = parentCode;

  }

  public String getName() {
    return getUserObject().getTypeName();
  }

  public String toString() {

    if (this.getCode() == null || "".equals(this.getCode().trim()) || "root".equals(this.getCode().trim())) {
      return this.getName();
    }

    return "[" + this.getCode() + "]" + this.getName();

  }
}
