package com.ufgov.zc.client.zc.infoscreen.console.editor;

import java.awt.BorderLayout;

import javax.swing.JOptionPane;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.zc.infoscreen.console.AnimationEditor;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.zc.publish.IZcAnimationServiceDelegate;

public class DefaultAnimationEditor extends AnimationEditor {

  protected IZcAnimationServiceDelegate metaService = (IZcAnimationServiceDelegate) ServiceFactory.create(IZcAnimationServiceDelegate.class,
    "animationServiceDelegate");

  protected RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  public DefaultAnimationEditor() {
    super();
    this.setLayout(new BorderLayout());
    this.add(metaViewer, BorderLayout.NORTH);
  }

  @Override
  public void save() {
    try {
      this.metaViewer.reWriteMeta();
      metaService.updateByKey(this.animationMeta, requestMeta);
      JOptionPane.showMessageDialog(null, "保存成功", "提示", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(null, "保存失败:" + ex.getMessage(), "提示:", JOptionPane.ERROR_MESSAGE);
    }
  }

  @Override
  public void delete() {
    try {

    } catch (Exception ex) {

    }
  }

}
