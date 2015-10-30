package com.ufgov.zc.client.zc.zcebsupplier;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityDialog;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;

import javax.swing.*;

/**
 * 供应商自己维护自己的页面
 */
public class ZcEbSupplierSelfEditPanel extends ZcEbSupplierEditPanel {
    public ZcEbSupplierSelfEditPanel(ZcEbSupplierSelfDialog dialog, ListCursor listCursor, String tabStatus, ZcEbSupplierListPanel listPanel) {
        super(dialog, listCursor, tabStatus, listPanel);

    }

    public ZcEbSupplierSelfEditPanel(ZcEbSupplierSelfDialog dialog, ListCursor listCursor, String tabStatus, ZcEbSupplierListPanel listPanel,
                                     ForeignEntityDialog forenEntityDialog) {
        super(dialog, listCursor, tabStatus, listPanel, forenEntityDialog);

    }

    public JComponent createSubBillPanel() {
        tabPane = new JTabbedPane();
        this.createSubTabPane(tablePanelMoreInfo, "供应商更多信息", false, null);
        this.createSubTabPane(tablePanelZyxm, "主营项目", true, "zyxm");
        this.createSubTabPane(tablePanelSpBsType, "经营类别", true, "spBsType");
        this.createSubTabPane(tablePanelQualify, "相关资质", true, "qualify");
        initTablePanelSingupPanel();
        return tabPane;
    }

    protected void createSubTabPane(final JTablePanel subPanel, String title, boolean needToolBar, final String which) {
        subPanel.init();
        subPanel.getSearchBar().setVisible(false);
        subPanel.setTablePreferencesKey(this.getClass().getName() + "_dt");
        tabPane.addTab(title, subPanel);

    }

    /**
     * 基本信息可以编辑字段
     *
     * @param
     */
    private void setFieldEnable(boolean enable) {
        for (AbstractFieldEditor fd : this.fieldEditors) {
            if (fd.getFieldName() != null && (fd.getFieldName().equals("fax") || fd.getFieldName().equals("address") ||
                    fd.getFieldName().equals("zipCode") || fd.getFieldName().equals("url") || fd.getFieldName().equals("email") ||
                    fd.getFieldName().equals("legalPersonFax") || fd.getFieldName().equals("legalPersonTel") ||
                    fd.getFieldName().equals("xyFile"))) {
                fd.setEnabled(enable);
            } else {
                fd.setEnabled(false);
            }
        }
    }

    private void setMoreFieldEnable(boolean enable) {
        for (AbstractFieldEditor fd : tool.getFieldEditorList()) {
            if (fd.getFieldName() != null && (fd.getFieldName().equals("linkMan") || fd.getFieldName().equals("linkManPhone") ||
                    fd.getFieldName().equals("linkManMobile") || fd.getFieldName().equals("zipCode") || fd.getFieldName().equals("url") ||
                    fd.getFieldName().equals("email") || fd.getFieldName().equals("legalPersonAddr") ||
                    fd.getFieldName().equals("legalPersonFax") || fd.getFieldName().equals("legalPersonTel") ||
                    fd.getFieldName().equals("legalPersonMobile") || fd.getFieldName().equals("mainBusinesses") ||
                    fd.getFieldName().equals("description"))) {
                fd.setEnabled(enable);
            } else {
                fd.setEnabled(false);
            }
        }
    }

    @Override
    protected void updateFieldEditorsEditable() {
        super.updateFieldEditors();
        if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW) || this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {
            setFieldEnable(true);
            setMoreFieldEnable(true);
            this.updateSubTableEditable(false);
        } else if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_BROWSE)) {
            for (AbstractFieldEditor fd : this.fieldEditors) {
                fd.setEnabled(false);
            }
            for (AbstractFieldEditor fd : this.tool.getFieldEditorList()) {
                fd.setEnabled(false);
            }
            this.updateSubTableEditable(false);
        }
    }

    protected void initButtonStatus() {
        super.initButtonStatus();
        ButtonStatus bs = new ButtonStatus();
        bs.setButton(this.editButton);
        bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
        bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);
        btnStatusList.add(bs);

    }
}
