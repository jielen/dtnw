package com.ufgov.zc.client.zc.qualification.quaType.Component;

import javax.swing.JComponent;

import com.ufgov.zc.client.component.event.ValueChangeEvent;
import com.ufgov.zc.client.component.event.ValueChangeListener;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.common.system.util.BeanUtil;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcEbSupQualification;
import com.ufgov.zc.common.zc.model.ZcSupQuaType;

public class ZcQuaTypeTreeSelectEditor extends AbstractFieldEditor {

  public ZcQuaTypeTreeSelectField coField = new ZcQuaTypeTreeSelectField();

  public ZcQuaTypeTreeSelectEditor(String name, String fieldName) {

    this.fieldName = fieldName;

    init(name);

  }

  public ZcQuaTypeTreeSelectEditor(String name, String fieldName, boolean selectedTailFlag) {

    this.fieldName = fieldName;

    coField = new ZcQuaTypeTreeSelectField(selectedTailFlag);

    init(name);

  }

  public ZcQuaTypeTreeSelectEditor(String name, boolean isEnabled, String fieldName) {

    this.fieldName = fieldName;

    init(name);

    coField.setEnabled(isEnabled);

    coField.setEditable(isEnabled);

  }

  protected JComponent createEditorComponent() {

    coField.addValueChangeListener(new ValueChangeListener() {

      public void valueChanged(ValueChangeEvent e) {

        syncEditObject();

      }

    });

    return coField;

  }

  protected void syncEditObject() {

    if (getEditObject() instanceof ZcBaseBill) {

      ZcBaseBill bill = (ZcBaseBill) getEditObject();

      if (bill != null) {

        ZcSupQuaType zcSupQuaType = coField.getZcSupQuaType();

        if (bill instanceof ZcEbSupQualification) {
          BeanUtil.set(fieldName, zcSupQuaType == null ? null : zcSupQuaType.getTypeCode(), getEditObject());

        } else {

          BeanUtil.set("supTypeCode", zcSupQuaType == null ? null : zcSupQuaType.getTypeCode(), getEditObject());

          BeanUtil.set("supTypeName", zcSupQuaType == null ? null : zcSupQuaType.getTypeName(), getEditObject());
        }

      }

    }

    this.fireEditSynced();

  }

  public Object getValue() {

    return coField.getZcSupQuaType();

  }

  public void setValue(Object value) {

    coField.setValueByCode((String) BeanUtil.get(fieldName, value));

  }

}
