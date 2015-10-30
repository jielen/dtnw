package com.ufgov.zc.client.zc.supplierprice.component;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcEbSignup;
import com.ufgov.zc.common.zc.model.ZcEbSignupPackDetail;
import com.ufgov.zc.common.zc.publish.IZcEbSignupServiceDelegate;

public class SignupTreeView extends JPanel {
  private IZcEbSignupServiceDelegate zcEbSignupServiceDelegate = (IZcEbSignupServiceDelegate) ServiceFactory.create(
    IZcEbSignupServiceDelegate.class, "zcEbSignupServiceDelegate");

  private RequestMeta meta = WorkEnv.getInstance().getRequestMeta();

  private JTree tree;

  public JTree getTree() {
    return tree;
  }

  public void setTree(JTree tree) {
    this.tree = tree;
  }

  public SignupTreeView() {
    this.setLayout(new BorderLayout());
    tree = new JTree();
    tree.setShowsRootHandles(true);
    this.add(new JScrollPane(tree));
  }

  public void setSignup(ZcEbSignup sign) {
	    ElementConditionDto dto=new ElementConditionDto();
	    dto.setZcText1(sign.getSignupId());
    List<ZcEbSignupPackDetail> details = zcEbSignupServiceDelegate.getZcEbSignupPackDetail(dto, meta);
    sign.setSignupPacks(details);
    DefaultMutableTreeNode root = new DefaultMutableTreeNode();
    root.setUserObject(sign);
    for (ZcEbSignupPackDetail detail : details) {
      DefaultMutableTreeNode packNode = new DefaultMutableTreeNode();
      packNode.setUserObject(detail);
      root.add(packNode);
    }
    tree.setModel(new DefaultTreeModel(root));
  }
}
