package com.ufgov.zc.client.zc.qualification.quaType.Component;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.ufgov.zc.client.component.JButtonTextField;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.common.zc.model.ZcSupQuaType;

public class ZcQuaTypeTreeSelectField extends JButtonTextField {

  public ZcQuaTypeTreeSelectField() {

    super(20);

    selectDialog = new ZcQuaTypeTreeSelectDialog(owner, true, this);

  }

  public ZcQuaTypeTreeSelectField(boolean selectedTailFlag) {

    super(20);

    selectDialog = new ZcQuaTypeTreeSelectDialog(owner, true, this, selectedTailFlag);

  }

  public void setValue(Object value) {

    this.value = value;

    if (value != null) {

      ZcSupQuaType data = (ZcSupQuaType) value;

      //this.setText(data.getZcCatalogueCode());

      this.setText("[" + data.getTypeCode() + "]" + data.getTypeName());

      this.setToolTipText("[" + data.getTypeCode() + "]" + data.getTypeName());

    } else {

      this.setText("");

      this.setToolTipText(null);

    }

    this.fireValueChanged();

  }

  public void setZcSupQuaType(ZcSupQuaType value) {

    this.setValue(value);

  }

  public ZcSupQuaType getZcSupQuaType() {

    return (ZcSupQuaType) this.getValue();

  }

  public void handleClick(JButtonTextField jButtonTextField) {

    await();

    selectDialog.setVisible(true);

  }

  public static void main(String[] args) {

    JFrame f = new JFrame();

    TextFieldEditor name = new TextFieldEditor("名称", "supQuaTypeName");

    ZcQuaTypeTreeSelectField code = new ZcQuaTypeTreeSelectField();

    code.setValueByCode("000101");

    code.setEditable(false);

    code.setEnabled(false);

    code.setEnabled(true);

    //    name.setEditable(false);

    name.setEnabled(true);

    JPanel panel = new JPanel(new GridLayout(1, 2));

    panel.add(code);

    panel.add(name);

    f.getContentPane().add(panel, BorderLayout.NORTH);

    // f.pack();

    // SwingUtilities.updateComponentTreeUI(panel);

    f.setSize(400, 300);

    f.setLocationRelativeTo(null);

    f.setVisible(true);

    f.addWindowListener(new WindowAdapter() {

      public void windowClosing(WindowEvent e) {

        System.exit(0);

      }

    });

  }

}
