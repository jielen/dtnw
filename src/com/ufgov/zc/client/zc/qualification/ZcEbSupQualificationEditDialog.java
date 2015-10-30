package com.ufgov.zc.client.zc.qualification;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcEbSupQualifTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JSaveableSplitPane;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.EnableButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.StopButton;
import com.ufgov.zc.client.component.button.SubaddButton;
import com.ufgov.zc.client.component.button.SubdelButton;
import com.ufgov.zc.client.component.button.SubinsertButton;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.qualification.quaType.Component.ZcQuaTypeTreeSelectEditor;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcEbSupQualification;
import com.ufgov.zc.common.zc.model.ZcEbSupQualificationLev;

public class ZcEbSupQualificationEditDialog extends GkBaseDialog {

	private static final long serialVersionUID = 1L;

	private ZcEbSupQualificationEditDialog self = this;

	private ZcEbSupQualificationEditPanel editPanel;

	public ZcEbSupQualificationEditDialog(
			ZcEbSupQualificationListPanel listPanel, List beanList,
			int editingRow, String tabStatus) {
		super(listPanel.getParentWindow(),
				Dialog.ModalityType.APPLICATION_MODAL);

		editPanel = new ZcEbSupQualificationEditPanel(new ListCursor(beanList,
				editingRow), tabStatus, listPanel);

		setLayout(new BorderLayout());

		add(editPanel);

		this.setTitle("供应商资质库");

		this.setSize(UIConstants.DIALOG_2_LEVEL_WIDTH,
				UIConstants.DIALOG_2_LEVEL_HEIGHT);

		this.moveToScreenCenter();

		this.setVisible(true);

	}

	public ZcEbSupQualificationEditDialog(Window parentWindow, List viewList,
			int row, String tabStatus) {

	}

	public ZcEbSupQualificationEditDialog() {

	}

	private boolean yesConfirmed = true;

	protected boolean dialogIsClosing() {

		if (editPanel.isDataChanged() && yesConfirmed) {

			int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存",
					"保存确认", 0);

			if (num == JOptionPane.YES_OPTION) {

				return editPanel.doSave();

			} else {

				yesConfirmed = false;

			}

		}

		return true;

	}

	public class ZcEbSupQualificationEditPanel extends AbstractMainSubEditPanel {

		private RequestMeta requestMeta = WorkEnv.getInstance()
				.getRequestMeta();

		private String compoId = "ZC_EB_SUP_QUALIFICATION";

		private FuncButton previousButton = new PreviousButton();

		private FuncButton nextButton = new NextButton();

		private FuncButton exitButton = new ExitButton();

		private FuncButton saveButton = new SaveButton();

		private EditButton editButton = new EditButton();

		private DeleteButton deleteButton = new DeleteButton();

		private FuncButton enableButton = new EnableButton();

		private FuncButton stopButton = new StopButton();

		private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

		private ListCursor listCursor;

		private ZcEbSupQualification oldBean;

		private String tabStatus;

		private ZcEbSupQualificationListPanel listPanel;

		private String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

		private String checkSn = null;

		public JSaveableSplitPane splitPane;

		private JTablePanel tablePanel = new JTablePanel();

		private JFuncToolBar subTableToolbar = new JFuncToolBar();;

		public ZcEbSupQualificationEditPanel(ListCursor listCursor,
				String tabStatus, ZcEbSupQualificationListPanel listPanel) {

			super(ZcEbSupQualification.class, BillElementMeta
					.getBillElementMetaWithoutNd("ZC_EB_SUP_QUALIFICATION"));

			this.listCursor = listCursor;

			this.tabStatus = tabStatus;

			this.listPanel = listPanel;

			this.workPanel.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createEtchedBorder(), "供应商资质库",
					TitledBorder.CENTER, TitledBorder.TOP,

					new Font("宋体", Font.BOLD, 15), Color.BLUE));

			this.colCount = 3;

			requestMeta.setCompoId(compoId);

			init();

			refreshData();

		}

		public ZcEbSupQualificationEditPanel() {

		}

		private void setOldObject() {

			oldBean = (ZcEbSupQualification) ObjectUtil.deepCopy(listCursor
					.getCurrentObject());

		}

		@Override
		public List<AbstractFieldEditor> createFieldEditors() {
			List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

			TextFieldEditor editor0 = new TextFieldEditor("资质编号", "qualifCode");
			editorList.add(editor0);

			TextFieldEditor editor1 = new TextFieldEditor("资质名称", "qualifName");
			editorList.add(editor1);

			ZcQuaTypeTreeSelectEditor type = new ZcQuaTypeTreeSelectEditor(
					"资质类型", "qualifType", true);
			editorList.add(type);

			AsValFieldEditor status = new AsValFieldEditor("状态", "status",
					"ZC_VS_QUALIFICATION_STATUS");
			editorList.add(status);

			return editorList;

		}

		@Override
		public JComponent createSubBillPanel() {
			int m = 1;
			if (m == 1) {
				return null;
			}
			JTabbedPane tabPane = new JTabbedPane();

			tablePanel.init();

			tablePanel.getSearchBar().setVisible(false);

			tablePanel.setTablePreferencesKey(this.getClass().getName()
					+ "_table");

			tablePanel.getTable().setShowCheckedColumn(false);

			tablePanel.getTable().getTableRowHeader()
					.setPreferredSize(new Dimension(50, 100));

			tabPane.addTab("资质级别", tablePanel);

			subTableToolbar = new JFuncToolBar();

			JButton addBtn = new SubaddButton(false);
			JButton insertBtn = new SubinsertButton(false);
			JButton delBtn = new SubdelButton(false);

			subTableToolbar.add(addBtn);
			subTableToolbar.add(insertBtn);
			subTableToolbar.add(delBtn);

			tablePanel.add(subTableToolbar, BorderLayout.SOUTH);

			addBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					addSub(tablePanel, new ZcEbSupQualificationLev());
				}

			});

			insertBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					insertSub(tablePanel, new ZcEbSupQualificationLev());
				}

			});

			delBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					deleteSub(tablePanel);
				}

			});

			return tabPane;
		}

		@Override
		public void initToolBar(JFuncToolBar toolBar) {

			toolBar.setModuleCode("ZC");

			toolBar.setCompoId(compoId);

			toolBar.add(editButton);

			toolBar.add(deleteButton);

			toolBar.add(saveButton);

			toolBar.add(enableButton);

			toolBar.add(stopButton);

			toolBar.add(previousButton);

			toolBar.add(nextButton);

			toolBar.add(exitButton);

			editButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					doEdit();

				}

			});

			deleteButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					doDelete();

				}

			});

			enableButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					doEnable();

				}

			});

			stopButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					doStop();

				}

			});

			saveButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					// 保存

					doSave();

				}

			});

			previousButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					doPrevious();

				}

			});

			nextButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					doNext();

				}

			});

			exitButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					doExit();

				}

			});

		}

		protected void doEnable() {

			boolean success = true;

			String errorInfo = "";

			try {

				requestMeta.setFuncId(enableButton.getFuncId());

				ZcEbSupQualification bean = (ZcEbSupQualification) this.listCursor
						.getCurrentObject();

				listPanel.ZcEbSupQualificationServiceDelegate.enableById(
						bean.getQualifId(), requestMeta);

			} catch (Exception e) {

				logger.error(e.getMessage(), e);

				success = false;

				errorInfo += e.getMessage();

			}

			if (success) {

				JOptionPane.showMessageDialog(this, "启用成功！", "提示",
						JOptionPane.INFORMATION_MESSAGE);

				this.refreshData();

				this.listPanel.refreshCurrentTabData();

			} else {

				JOptionPane.showMessageDialog(this, "启用失败 ！\n" + errorInfo,
						"错误", JOptionPane.ERROR_MESSAGE);

			}

		}

		protected void doStop() {

			boolean success = true;

			String errorInfo = "";

			try {

				requestMeta.setFuncId(stopButton.getFuncId());

				ZcEbSupQualification bean = (ZcEbSupQualification) this.listCursor
						.getCurrentObject();

				listPanel.ZcEbSupQualificationServiceDelegate.freezeById(
						bean.getQualifId(), requestMeta);

			} catch (Exception e) {

				logger.error(e.getMessage(), e);

				success = false;

				errorInfo += e.getMessage();

			}

			if (success) {

				JOptionPane.showMessageDialog(this, "停用成功！", "提示",
						JOptionPane.INFORMATION_MESSAGE);

				this.refreshData();

				this.listPanel.refreshCurrentTabData();

			} else {

				JOptionPane.showMessageDialog(this, "停用失败 ！\n" + errorInfo,
						"错误", JOptionPane.ERROR_MESSAGE);

			}

		}

		protected void doDelete() {

			int num = JOptionPane
					.showConfirmDialog(this, "是否删除当前单据", "删除确认", 0);

			if (num == JOptionPane.YES_OPTION) {

				boolean success = true;

				String errorInfo = "";

				try {

					requestMeta.setFuncId(deleteButton.getFuncId());

					ZcEbSupQualification bean = (ZcEbSupQualification) this.listCursor
							.getCurrentObject();

					listPanel.ZcEbSupQualificationServiceDelegate.deleteById(
							bean.getQualifId(), requestMeta);

				} catch (Exception e) {

					logger.error(e.getMessage(), e);

					success = false;

					errorInfo += e.getMessage();

				}

				if (success) {

					this.listCursor.removeCurrentObject();

					JOptionPane.showMessageDialog(this, "删除成功！", "提示",
							JOptionPane.INFORMATION_MESSAGE);

					this.refreshData();

					this.listPanel.refreshCurrentTabData();

				} else {

					JOptionPane.showMessageDialog(this, "删除失败 ！\n" + errorInfo,
							"错误", JOptionPane.ERROR_MESSAGE);

				}

			}

		}

		protected void doEdit() {

			this.pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;

			setButtonStatus();

			updateFieldEditorsEditable();

		}

		private void setButtonStatus() {

			if (this.btnStatusList.size() == 0) {

				ButtonStatus bs = new ButtonStatus();

				bs.setButton(this.editButton);

				bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);
				// bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);
				bs.addBillStatus(ZcSettingConstants.STATUS_DRAFT);
				bs.addBillStatus(ZcSettingConstants.STATUS_ENABLE);

				btnStatusList.add(bs);

				bs = new ButtonStatus();

				bs.setButton(this.saveButton);

				bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_EDIT);

				bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_NEW);

				bs.addBillStatus(ZcSettingConstants.STATUS_DRAFT);
				bs.addBillStatus(ZcSettingConstants.STATUS_ENABLE);

				btnStatusList.add(bs);

				bs = new ButtonStatus();

				bs.setButton(this.enableButton);

				bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

				bs.addBillStatus(ZcSettingConstants.STATUS_STOP);
				bs.addBillStatus(ZcSettingConstants.STATUS_DRAFT);

				btnStatusList.add(bs);

				bs = new ButtonStatus();

				bs.setButton(this.stopButton);

				bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

				bs.addBillStatus(ZcSettingConstants.STATUS_ENABLE);
				bs.addBillStatus(ZcSettingConstants.STATUS_DRAFT);

				btnStatusList.add(bs);

				bs = new ButtonStatus();

				bs.setButton(this.deleteButton);

				bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

				bs.addBillStatus(ZcSettingConstants.STATUS_STOP);
				bs.addBillStatus(ZcSettingConstants.STATUS_DRAFT);

				btnStatusList.add(bs);

				bs = new ButtonStatus();

				bs.setButton(this.exitButton);

				bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_ALL);

				bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

				btnStatusList.add(bs);

				bs = new ButtonStatus();

				bs.setButton(this.previousButton);

				bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

				bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);

				btnStatusList.add(bs);

				bs = new ButtonStatus();

				bs.setButton(this.nextButton);

				bs.addPageStatus(ZcSettingConstants.PAGE_STATUS_BROWSE);

				bs.addBillStatus(ZcSettingConstants.BILL_STATUS_ALL);
				btnStatusList.add(bs);
			}

			ZcEbSupQualification obj = (ZcEbSupQualification) (this.listCursor
					.getCurrentObject());

			String billStatus = obj.getStatus();

			ZcUtil.setButtonEnable(this.btnStatusList, billStatus,
					this.pageStatus, this.compoId, obj.getProcessInstId());

		}

		private void doPrevious() {

			listCursor.previous();

			refreshData();

		}

		private void doNext() {

			listCursor.next();

			refreshData();

		}

		public void doExit() {

			self.closeDialog();

		}

		private boolean checkBeforeSave() {

			List notNullBillElementList = BillElementMeta
					.getBillElementMetaWithoutNd(compoId)
					.getNotNullBillElement();

			ZcEbSupQualification bean = (ZcEbSupQualification) this.listCursor
					.getCurrentObject();

			StringBuilder errorInfo = new StringBuilder();

			String validateInfo = ZcUtil.validateBillElementNull(bean,
					notNullBillElementList);
			StringBuffer subInfo = new StringBuffer();
			if (bean.getLevs() != null) {
				for (int i = 0; i < bean.getLevs().size(); i++) {
					ZcEbSupQualificationLev lv = (ZcEbSupQualificationLev) bean
							.getLevs().get(i);
					if (lv.getName() == null || "".equals(lv.getName())) {
						subInfo.append("第").append(i + 1).append("行等级名称不能为空\n");
					}
					if (lv.getLev() == null || "".equals(lv.getLev())) {
						subInfo.append("第").append(i + 1).append("行级别不能为空\n");
					}
				}
			}

			if (validateInfo.length() != 0) {

				errorInfo.append("供应商资质").append("：\n")
						.append(validateInfo.toString()).append("\n");

			}

			if (subInfo.length() > 0) {
				errorInfo.append("资质级别：\n").append(subInfo);
			}

			if (errorInfo.length() != 0) {

				JOptionPane.showMessageDialog(this, errorInfo.toString(), "提示",
						JOptionPane.WARNING_MESSAGE);

				return true;

			}

			return false;

		}

		public boolean doSave() {

			if (checkBeforeSave()) {

				return false;

			}

			// if (!isDataChanged()) {
			//
			// JOptionPane.showMessageDialog(this, "数据没有发生改变，不用保存.", "提示",
			// JOptionPane.INFORMATION_MESSAGE);
			//
			// return false;
			//
			// }

			ZcEbSupQualification afterSaveBill = (ZcEbSupQualification) this.listCursor
					.getCurrentObject();

			boolean success = true;

			String errorInfo = "";

			try {

				requestMeta.setFuncId(saveButton.getFuncId());

				afterSaveBill = listPanel.ZcEbSupQualificationServiceDelegate
						.save(afterSaveBill, requestMeta);

			} catch (Exception e) {

				logger.error(e.getMessage(), e);

				success = false;

				errorInfo += e.getMessage();

			}

			if (success) {

				this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

				this.listPanel.refreshCurrentTabData();
				this.refreshData();

				JOptionPane.showMessageDialog(this, "保存成功！", "提示",
						JOptionPane.INFORMATION_MESSAGE);

			} else {

				JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo,
						"错误", JOptionPane.ERROR_MESSAGE);

			}

			return success;

		}

		public boolean isDataChanged() {
			if (!saveButton.isVisible() || !saveButton.isEnabled()) {
				return false;
			}

			return !DigestUtil.digest(oldBean).equals(
					DigestUtil.digest(listCursor.getCurrentObject()));

		}

		private void refreshData() {

			ZcEbSupQualification bean = (ZcEbSupQualification) listCursor
					.getCurrentObject();

			if (bean == null || bean.getQualifId() == null
					|| "".equals(bean.getQualifId())) {

				// 新增页面

				bean = new ZcEbSupQualification();

				setDefualtValue(bean);

				this.pageStatus = ZcSettingConstants.PAGE_STATUS_NEW;

				List lst = new ArrayList();

				lst.add(bean);

				this.listCursor.setDataList(lst, -1);

			} else {
				bean = listPanel.ZcEbSupQualificationServiceDelegate
						.selectById(bean.getQualifId(), requestMeta);
			}

			listCursor.setCurrentObject(bean);
			setEditingObject(bean);

			refreshSubTableData(bean);

			updateFieldEditorsEditable();

			setOldObject();

			setButtonStatus();

		}

		@Override
		protected void updateFieldEditorsEditable() {
			super.updateFieldEditors();
			if (this.pageStatus.equals(ZcSettingConstants.PAGE_STATUS_NEW)
					|| this.pageStatus
							.equals(ZcSettingConstants.PAGE_STATUS_EDIT)) {
				AsValFieldEditor pf = null;
				for (AbstractFieldEditor fd : this.fieldEditors) {
					if ("qualifCode".equals(fd.getFieldName())
							|| "status".equals(fd.getFieldName())) {
						fd.setEnabled(false);
					} else {
						fd.setEnabled(true);
					}

				}
				this.tablePanel.getTable().setEnabled(true);
				this.subTableToolbar.setVisible(true);

			} else if (this.pageStatus
					.equals(ZcSettingConstants.PAGE_STATUS_BROWSE)) {

				for (AbstractFieldEditor fd : this.fieldEditors) {

					fd.setEnabled(false);

				}
				this.tablePanel.getTable().setEnabled(false);
				this.subTableToolbar.setVisible(false);
			}

		}

		private void setDefualtValue(ZcEbSupQualification bean) {

			bean.setStatus(ZcSettingConstants.STATUS_DRAFT);
			bean.setLevs(new ArrayList<ZcEbSupQualificationLev>());

		}

		private void refreshSubTableData(ZcEbSupQualification qf) {

			tablePanel.setTableModel(ZcEbSupQualifTableModelConverter
					.convertDetailToTableModel(qf.getLevs()));
			ZcUtil.translateColName(this.tablePanel.getTable(),
					ZcEbSupQualifTableModelConverter.getItemInfo());
			setTableProperty(this.tablePanel.getTable());
		}

		private void setTableProperty(JTable table) {

			AsValComboBoxCellEditor lev = new AsValComboBoxCellEditor(
					"ZC_VS_NUM_LIST");

			SwingUtil.setTableCellEditor(table, "LEV", lev);

		}

	}

}
