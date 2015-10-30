package com.ufgov.zc.client.zc.catalogue;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.util.List;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.component.GkBaseDialog;

@SuppressWarnings("unchecked")
public class ZcEbCatalogueDialog extends GkBaseDialog {
	private static final long serialVersionUID = 1L;

	private ZcEbCatalogueDialog self = this;

	private ZcEbCatalogueEditPanel editPanel;

	public ZcEbCatalogueDialog(ZcEbCatalogueListPanel listPanel, List beanList,
			int editingRow, String tabStatus) {
		super(listPanel.getParentWindow(),
				Dialog.ModalityType.APPLICATION_MODAL);
		// editPanel = new ZcPProMakeXYEditPanel(this.self, new
		// ListCursor(beanList, editingRow), tabStatus, listPanel);
		editPanel = listPanel.getEditPanel(this.self, new ListCursor(beanList,
				editingRow), tabStatus, listPanel);
		if (editPanel != null) {
			setLayout(new BorderLayout());
			add(editPanel);
			this.setTitle("集中采购目录");
			this.setSize(UIConstants.PRINT_SETTING_DIALOG_WIDTH,
					UIConstants.PRINT_SETTING_DIALOG_HEIGHT);
			// this.setMaxSizeWindow();
			this.setSize(new Dimension(800, 400));
			this.moveToScreenCenter();
			this.setVisible(true);
		}
	}

	@Override
	public void closeDialog() {
		this.editPanel.doExit();
	}

}
