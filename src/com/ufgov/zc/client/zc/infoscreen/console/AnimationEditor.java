package com.ufgov.zc.client.zc.infoscreen.console;

import javax.swing.JPanel;

import com.ufgov.zc.common.zc.model.AnimationMeta;

public abstract class AnimationEditor extends JPanel {
  protected AnimationMeta animationMeta;

  protected AnimationMetaViewer metaViewer;

  public AnimationEditor() {
    metaViewer = new AnimationMetaViewer();
  }

  public void setAnimationMeta(AnimationMeta meta) {
    metaViewer.setAnimationMeta(meta);
    this.animationMeta = meta;
  }

  abstract public void save();

  abstract public void delete();
}
