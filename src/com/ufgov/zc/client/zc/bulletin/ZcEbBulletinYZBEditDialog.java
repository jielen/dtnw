package com.ufgov.zc.client.zc.bulletin;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.util.List;

import org.apache.log4j.Logger;

import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.common.zc.ZcEbBulletinConstants;

public class ZcEbBulletinYZBEditDialog extends GkBaseDialog {

	private static final Logger logger = Logger
			.getLogger(ZcEbBulletinYZBEditDialog.class);

	private static final long serialVersionUID = 1L;

	private ZcEbBulletinYZBEditDialog self = this;

	private ZcEbBulletinYZBEditPanel editPanel;

	public ZcEbBulletinYZBEditDialog(ZcEbBulletinYZBListPanel listPanel,
			List beanList, int editingRow, String tabStatus) {

		super(listPanel.getParentWindow(),
				Dialog.ModalityType.APPLICATION_MODAL);

		editPanel = new ZcEbBulletinYZBEditPanel(this.self, new ListCursor(
				beanList, editingRow), tabStatus, listPanel);

		setLayout(new BorderLayout());

		add(editPanel);

		this.setTitle(ZcEbBulletinConstants.TITLE_ZC_EB_BULLETIN_YZB);

		this.setSize(UIConstants.DIALOG_0_LEVEL_WIDTH,
				UIConstants.DIALOG_0_LEVEL_HEIGHT);

		this.setMaxSizeWindow();

		this.moveToScreenCenter();

		this.pack();

		editPanel.refreshData();

		this.setMaxSizeWindow();

		this.setVisible(true);

	}

	@Override
	public void closeDialog() {

		this.editPanel.doExit();

	}

}
