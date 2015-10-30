package com.ufgov.zc.client.zc.infoscreen.console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.zc.model.AnimationMeta;
import com.ufgov.zc.common.zc.publish.IZcAnimationServiceDelegate;

public class AnimationConsole extends JPanel implements MouseListener, ActionListener {
  private JFuncToolBar toolbar;

  private AnimationTreeViewer treeViewer;

  private JPanel metaEditorPanel;

  private AnimationMeta currentMeta;

  private String compoId = "ZC_ANIMATION_CONSOLE";

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  public AnimationConsole() {
    this.setLayout(new BorderLayout());
    this.add(initToolBar(), BorderLayout.NORTH);
    JSplitPane splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    treeViewer = new AnimationTreeViewer();
    treeViewer.setMinimumSize(new Dimension(200, 0));
    treeViewer.setPreferredSize(new Dimension(200, 0));
    treeViewer.addTreeListener(this);
    metaEditorPanel = new JPanel();
    metaEditorPanel.setLayout(new BorderLayout());
    splitPanel.setLeftComponent(treeViewer);
    splitPanel.setRightComponent(metaEditorPanel);
    this.add(splitPanel, BorderLayout.CENTER);
    initAnimationTreeViewer();
  }

  static {
    LangTransMeta.init("ZC%");
  }

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    JTree tree = (JTree) e.getSource();
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
    if (node != null) {
      if (node.getUserObject() instanceof AnimationMeta) {
        currentMeta = (AnimationMeta) node.getUserObject();
        try {
          AnimationEditor editor = (AnimationEditor) currentMeta.getEditorViewer();
          editor.setAnimationMeta(currentMeta);
          metaEditorPanel.removeAll();
          metaEditorPanel.add(editor, BorderLayout.CENTER);
          metaEditorPanel.revalidate();
          metaEditorPanel.repaint();
        } catch (Exception ex) {
          ex.printStackTrace();
          JOptionPane.showMessageDialog(null, ex.getMessage());
        }
      }
    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  private JToolBar initToolBar() {
    toolbar = new JFuncToolBar();
    toolbar.setModuleCode("ZC");
    toolbar.setCompoId(compoId);
    toolbar.setFloatable(false);
    SaveButton saveBtn = new SaveButton();
    saveBtn.setActionCommand("save");
    saveBtn.addActionListener(this);
    toolbar.add(saveBtn);
    return toolbar;
  }

  private void initAnimationTreeViewer() {

    IZcAnimationServiceDelegate service = (IZcAnimationServiceDelegate) ServiceFactory.create(IZcAnimationServiceDelegate.class,
      "animationServiceDelegate");
    AnimationMeta meta = new AnimationMeta();
    meta.setType("G");
    List metas = service.select(meta, requestMeta);
    treeViewer.setGeneralAnimationMetas(metas);
    meta.setType("R");
    metas = service.select(meta, requestMeta);
    treeViewer.setRealAnimationMetas(metas);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    try {
      if (e.getActionCommand().equals("save")) {
        AnimationEditor editor = (AnimationEditor) currentMeta.getEditorViewer();
        editor.save();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void main(String[] args) {
    JFrame f = new JFrame();
    f.getContentPane().setLayout(new BorderLayout());
    Properties p = new Properties();
    AnimationConsole console = new AnimationConsole();
    f.getContentPane().add(console, BorderLayout.CENTER);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setSize(800, 600);
    f.setVisible(true);
  }
}
