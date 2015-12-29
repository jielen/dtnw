package com.ufgov.zc.client.zc.infoscreen.console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ufgov.zc.common.zc.model.AnimationMeta;

public class AnimationMetaViewer extends JPanel {
  private AnimationMeta _meta;

  private final JTextField descF = new JTextField();

  private final JCheckBox usedF = new JCheckBox();

  private final JFormattedTextField indexF = new JFormattedTextField(NumberFormat.getIntegerInstance());

  private final JFormattedTextField resolutionF = new JFormattedTextField(NumberFormat.getIntegerInstance());

  private final JFormattedTextField repeatCountF = new JFormattedTextField(NumberFormat.getIntegerInstance());

  private final JFormattedTextField durationF = new JFormattedTextField(NumberFormat.getIntegerInstance());

  public AnimationMetaViewer() {
    //    this.setLayout(new FlowLayout(FlowLayout.LEADING));
    GridBagLayout layout = new GridBagLayout();
    this.setLayout(layout);
    JPanel p = null;
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(20, 10, 20, 10);
    c.anchor = GridBagConstraints.WEST;

    c.fill = GridBagConstraints.HORIZONTAL;

    c.gridx = 0;
    c.gridy = 0;
    p = createPanel("描述", descF);
    layout.setConstraints(p, c);
    this.add(p);

    c.gridx = 1;
    c.gridy = 0;
    p = createPanel("启用", usedF);
    layout.setConstraints(p, c);
    this.add(p);

    c.gridx = 2;
    c.gridy = 0;
    p = createPanel("播放顺序", indexF);
    layout.setConstraints(p, c);
    this.add(p);

    c.gridx = 0;
    c.gridy = 1;
    p = createPanel("持续时间(秒)", durationF);
    layout.setConstraints(p, c);
    this.add(p);

    c.gridx = 1;
    c.gridy = 1;
    p = createPanel("播放间隔(秒)", resolutionF);
    layout.setConstraints(p, c);
    this.add(p);

    c.gridx = 2;
    c.gridy = 1;
    p = createPanel("重复次数", repeatCountF);
    layout.setConstraints(p, c);
    this.add(p);

  }

  public void setAnimationMeta(AnimationMeta meta) {
    this._meta = meta;
    bindMeta(_meta);
  }

  public void reWriteMeta() {
    _meta.setDesc(descF.getText());
    _meta.setUsed(usedF.isSelected() == true ? "1" : "0");
    try {
      indexF.commitEdit();
      durationF.commitEdit();
      resolutionF.commitEdit();
      repeatCountF.commitEdit();

    } catch (ParseException e) {
      // TCJLODO Auto-generated catch block
      e.printStackTrace();
    }
    _meta.setIndex(Integer.parseInt(indexF.getText().toString()));
    _meta.setDuration((Integer.parseInt(durationF.getText().toString()) * 1000));
    _meta.setResolution((Integer.parseInt(resolutionF.getText().toString()) * 1000));
    _meta.setRepeatcount(Integer.parseInt(repeatCountF.getValue().toString()));

  }

  private void bindMeta(AnimationMeta meta) {
    descF.setText(meta.getDesc());
    usedF.setSelected(false);
    if ("1".equals(_meta.getUsed())) {
      usedF.setSelected(true);
    }
    indexF.setValue(meta.getIndex());
    durationF.setValue((int) Math.ceil(meta.getDuration() / 1000));
    resolutionF.setValue((int) Math.ceil(meta.getResolution() / 1000));
    repeatCountF.setValue(meta.getRepeatcount());
  }

  private JPanel createPanel(String label, JComponent c) {
    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    p.add(new JLabel(label + ":"), BorderLayout.WEST);
    p.add(c, BorderLayout.CENTER);
    p.setPreferredSize(new Dimension(200, 20));
    p.setMinimumSize(new Dimension(200, 20));
    p.setMaximumSize(new Dimension(200, 20));
    return p;
  }
}
