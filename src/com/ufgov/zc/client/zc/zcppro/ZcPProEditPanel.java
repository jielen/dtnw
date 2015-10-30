package com.ufgov.zc.client.zc.zcppro;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.NextButton;
import com.ufgov.zc.client.component.button.PreviousButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.IntFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.zc.ButtonStatus;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcPProBalConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.DigestUtil;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcPPro;
import com.ufgov.zc.common.zc.publish.IZcPProDelegate;

public class ZcPProEditPanel extends AbstractMainSubEditPanel {

	private static final Logger logger = Logger
			.getLogger(ZcPProEditPanel.class);

	private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

	private String compoId = "ZC_P_PRO";

	private ArrayList<ButtonStatus> btnStatusList = new ArrayList<ButtonStatus>();

	private FuncButton saveButton = new SaveButton();

	private EditButton editButton = new EditButton();

	private FuncButton deleteButton = new DeleteButton();

	private FuncButton previousButton = new PreviousButton();

	private FuncButton nextButton = new NextButton();

	private FuncButton exitButton = new ExitButton();

	private String pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

	private ListCursor listCursor;

	private ZcPPro oldZcPPro;

	private ZcPProListPanel listPanel;

	private ZcPProEditPanel self = this;

	private GkBaseDialog parent;

	private ElementConditionDto elementCondtiontDto = new ElementConditionDto();

	private IZcPProDelegate zcPProDelegate = (IZcPProDelegate) ServiceFactory
			.create(IZcPProDelegate.class, "zcPProDelegate");

	public ZcPProEditPanel(GkBaseDialog parent, ListCursor listCursor,
			String tabStatus, ZcPProListPanel listPanel) {
		super(ZcPPro.class, listPanel.getBillElementMeta());
		this.listCursor = listCursor;
		this.listPanel = listPanel;
		this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), LangTransMeta
				.translate(ZcSettingConstants.FIELD_TRANS_ZC_P_PRO_TITLE),
				TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体",
						Font.BOLD, 15), Color.BLUE));
		this.parent = parent;
		this.colCount = 2;
		init();
		requestMeta.setCompoId(compoId);

		refreshData();
	}

	@Override
	public List<AbstractFieldEditor> createFieldEditors() {
		List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();
		TextFieldEditor chrName = new TextFieldEditor("采购项目", "chrName");
		editorList.add(chrName);

		TextFieldEditor enName = new TextFieldEditor("预算单位", "enName");
		editorList.add(enName);

		AsValFieldEditor bi = new AsValFieldEditor("项目分类", "biCode",
				"ELE_BUDGET_ITEM");
//		editorList.add(bi);

//		IntFieldEditor nd = new IntFieldEditor("作业时间", "nd");
//		editorList.add(nd);

//		TextFieldEditor createUser = new TextFieldEditor("录入人", "createUser");
//		editorList.add(createUser);

//		DateFieldEditor zcInputDate = new DateFieldEditor(
//				LangTransMeta
//						.translate(ZcPProBalConstants.FIELD_TRANS_ZC_INPUT_DATE),
//				"createDate");
//		editorList.add(zcInputDate);

		return editorList;
	}

	@Override
	public void initToolBar(JFuncToolBar toolBar) {
		toolBar.setModuleCode("ZC");
		toolBar.setCompoId(compoId);
		toolBar.add(saveButton);
		toolBar.add(editButton);
		toolBar.add(deleteButton);
		toolBar.add(previousButton);
		toolBar.add(nextButton);
		toolBar.add(exitButton);

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
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doSave();
			}
		});

		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doEdit();
			}
		});
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doDelete();
			}
		});

	}

	private void doPrevious() {
		if (isDataChanged()) {
			int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存",
					"保存确认", 0);
			if (num == JOptionPane.YES_OPTION) {
				if (!doSave()) {
					return;
				}
			} else {
				listCursor.setCurrentObject(oldZcPPro);
			}
		}
		listCursor.previous();
		refreshData();
	}

	private void doNext() {
		if (isDataChanged()) {
			int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存",
					"保存确认", 0);
			if (num == JOptionPane.YES_OPTION) {
				if (!doSave()) {
					return;
				}
			} else {
				listCursor.setCurrentObject(oldZcPPro);
			}
		}
		listCursor.next();
		refreshData();
	}

	public void doExit() {
		if (isDataChanged()) {
			int num = JOptionPane.showConfirmDialog(this, "当前页面数据已修改，是否要保存",
					"保存确认", 0);
			if (num == JOptionPane.YES_OPTION) {
				if (!doSave()) {
					return;
				}
			}
		}
		this.parent.dispose();
	}

	/**
	 * 保存前校验
	 * 
	 * @param cpApply
	 * @return
	 */
	private boolean checkBeforeSave() {
		ZcPPro zcPPro = (ZcPPro) this.listCursor.getCurrentObject();
		StringBuilder errorInfo = new StringBuilder();
		if (zcPPro.getChrName() == null
				|| "".equals(zcPPro.getChrName().trim())) {
			JOptionPane.showMessageDialog(this, "请填写采购项目！", "提示",
					JOptionPane.WARNING_MESSAGE);
			return true;
		}

		if (errorInfo.length() != 0) {
			JOptionPane.showMessageDialog(this, errorInfo.toString(), "提示",
					JOptionPane.WARNING_MESSAGE);
			return true;
		}

		return false;
	}

	public void doEdit() {
		this.saveButton.setEnabled(true);
		this.editButton.setEnabled(false);
		pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;
		this.updatestatus();
	}

	public boolean doSave() {
		if (checkBeforeSave()) {
			return false;
		}
		if (!isDataChanged()) {
			JOptionPane.showMessageDialog(this, "数据没有发生改变，不用保存.", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			this.saveButton.setEnabled(false);
			this.editButton.setEnabled(true);
			pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;
			this.updatestatus();
			return true;
		}
		boolean success = true;
		String errorInfo = "";
		try {
			requestMeta.setFuncId(saveButton.getFuncId());
			ZcPPro inData = (ZcPPro) ObjectUtil.deepCopy(this.listCursor
					.getCurrentObject());
			if (inData.getChrId() == null || "".equals(inData.getChrId())) {
				this.zcPProDelegate.saveZcPPro(inData, requestMeta);
			} else {
				this.zcPProDelegate.updateZcPPro(inData, requestMeta);
			}
			listCursor.setCurrentObject(inData);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			success = false;
			errorInfo += e.getMessage();
		}
		if (success) {
			JOptionPane.showMessageDialog(this, "保存成功！", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;
			refreshData();
			this.saveButton.setEnabled(false);
			this.editButton.setEnabled(true);
			this.listPanel.refreshCurrentTabData();
		} else {
			JOptionPane.showMessageDialog(this, "保存失败 ！\n" + errorInfo, "错误",
					JOptionPane.ERROR_MESSAGE);
		}
		return true;
	}

	protected void doDelete() {
		requestMeta.setFuncId(deleteButton.getFuncId());
		ZcPPro zcPPro = (ZcPPro) this.listCursor.getCurrentObject();
		if (zcPPro.getChrId() == null || "".equalsIgnoreCase(zcPPro.getChrId())) {
			JOptionPane.showMessageDialog(this, "尚未保存到数据库，无需删除！", "提示",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		int num = JOptionPane.showConfirmDialog(this, "是否删除当前数据", "删除确认", 0);
		if (num == JOptionPane.YES_OPTION) {
			boolean success = true;
			String errorInfo = "";
			try {
				requestMeta.setFuncId(deleteButton.getFuncId());
				if (zcPPro.getIsDeleted() != 0
						|| !zcPPro.getChrId().startsWith("ZC")) {
					JOptionPane.showMessageDialog(this, "非预算单位录入数据，不可以删除！",
							"提示", JOptionPane.ERROR_MESSAGE);
					return;
				}
				this.zcPProDelegate
						.deleteZcPPro(zcPPro.getChrId(), requestMeta);
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
				JOptionPane.showMessageDialog(this, "数据失败 ！\n" + errorInfo,
						"错误", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public boolean isDataChanged() {
		return !DigestUtil.digest(oldZcPPro).equals(
				DigestUtil.digest(listCursor.getCurrentObject()));
	}

	private void refreshData() {
		ZcPPro zcPPro = (ZcPPro) listCursor.getCurrentObject();
		if (zcPPro != null && !"".equals(ZcUtil.safeString(zcPPro.getChrId()))) {
			try {
				zcPPro = this.zcPProDelegate.getZcPProById(zcPPro.getChrId(),
						requestMeta);
				pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;
			} catch (Exception e) {
				e.printStackTrace();
			}
			listCursor.setCurrentObject(zcPPro);
		} else {
			pageStatus = ZcSettingConstants.PAGE_STATUS_EDIT;
			if (zcPPro == null) {
				zcPPro = new ZcPPro();
			}

			zcPPro.setEnId(requestMeta.getSvCoCode());
			zcPPro.setEnName(requestMeta.getSvCoName());
			zcPPro.setCreateUser(requestMeta.getSvUserID());
			zcPPro.setCreateDate(requestMeta.getSysDate());
			zcPPro.setNd(requestMeta.getSvNd());

			listCursor.getDataList().add(zcPPro);
			editButton.setEnabled(false);
		}
		if(ZcSettingConstants.PAGE_STATUS_BROWSE.equals(this.pageStatus)){
			this.saveButton.setEnabled(false);
			this.editButton.setEnabled(true);
		}else{
			this.saveButton.setEnabled(true);
			this.editButton.setEnabled(false);
		}

		this.setEditingObject(zcPPro);
		setOldObject();
		updatestatus();
	}

	private void setOldObject() {
		oldZcPPro = (ZcPPro) ObjectUtil.deepCopy(listCursor.getCurrentObject());
	}

	@Override
	public JComponent createSubBillPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	private void updatestatus() {
		for (AbstractFieldEditor fd : this.fieldEditors) {
			if (ZcSettingConstants.PAGE_STATUS_EDIT.equals(this.pageStatus) 
					&& ("chrName".equals(fd.getFieldName())
					|| "biCode".equals(fd.getFieldName()))) {
				fd.setEnabled(true);
			} else {
				fd.setEnabled(false);
			}

		}
	}

}
