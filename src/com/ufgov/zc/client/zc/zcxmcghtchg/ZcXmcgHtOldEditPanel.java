package com.ufgov.zc.client.zc.zcxmcghtchg;

import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_FUND_CODE;
import static com.ufgov.zc.common.system.constants.ZcElementConstants.FIELD_TRANS_ORIGIN_CODE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.ufgov.smartclient.component.table.fixedtable.JPageableFixedTable;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.converter.zc.ZcXmcgHtToTableModelConverter;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.table.BeanTableModel;
import com.ufgov.zc.client.component.table.celleditor.MoneyCellEditor;
import com.ufgov.zc.client.component.table.celleditor.TextCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.NumberCellRenderer;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecelleditor.FileCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsValCellRenderer;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.AutoNumFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.ForeignEntityFieldCellEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.IntFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.zcppromake.ZcBudgetHandler;
import com.ufgov.zc.client.zc.zcxmcght.ZcXmcgHtEditPanel;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcHtPrePayBillItem;
import com.ufgov.zc.common.zc.model.ZcPProMitemBi;
import com.ufgov.zc.common.zc.model.ZcTBchtItem;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.model.ZcXmcgHtBi;
import com.ufgov.zc.common.zc.model.ZcXmcgHtChg;

public class ZcXmcgHtOldEditPanel extends ZcXmcgHtEditPanel {

	private ZcXmcgHtChgEditPanel editPanel;


	public static String getCompoId() {
    return "ZC_XMCG_HT";
  }

  public ZcXmcgHtOldEditPanel(GkBaseDialog parent,
			ListCursor<ZcXmcgHt> listCursor, String tabStatus,
			ZcXmcgHtChgEditPanel editPanel) {
		super(parent, listCursor, tabStatus, null,getCompoId());
		this.editPanel = editPanel;
		editPanel.setOldObject();
		// TODO Auto-generated constructor stub
	}

	protected void loadCommonEditors(List<AbstractFieldEditor> editorList) {

		TextFieldEditor zcMakeName = new TextFieldEditor(
				LangTransMeta
						.translate(ZcElementConstants.FIELD_TRANS_ZC_MAKE_NAME),
				"zcPProMake.zcMakeName");

		MoneyFieldEditor zcMoneyBiSum = new MoneyFieldEditor(
				LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MONEY_BI_SUM),

				"zcPProMake.zcMoneyBiSum");

		ElementConditionDto elementCondtiontDto = new ElementConditionDto();

		elementCondtiontDto.setBillStatus("exec");

		elementCondtiontDto.setCoCode(requestMeta.getSvCoCode());

		TextFieldEditor zcMakeCode = new TextFieldEditor("选择项目", "zcMakeCode");

		zcMakeCode.setEnabled(false);

		zcMakeName.setEnabled(false);

		zcMoneyBiSum.setEnabled(false);

		editorList.add(zcMakeCode);

		editorList.add(zcMakeName);

		editorList.add(zcMoneyBiSum);

		AsValFieldEditor zcProjType = new AsValFieldEditor(
				LangTransMeta
						.translate(ZcElementConstants.FIELD_TRANS_ZC_FUKUAN_TYPE),
				"zcCgLeixing",

				"ZC_VS_FUKUAN_TYPE");

		zcProjType.setEnabled(false);

		editorList.add(zcProjType);

		AutoNumFieldEditor zcHtCode = new AutoNumFieldEditor(
				LangTransMeta
						.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_CODE),
				"zcHtCode");

		editorList.add(zcHtCode);

		TextFieldEditor zcHtName = new TextFieldEditor(
				LangTransMeta
						.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_NAME),
				"zcHtName");

		editorList.add(zcHtName);

		AsValFieldEditor zcHtStatus = new AsValFieldEditor(
				LangTransMeta
						.translate(ZcElementConstants.FIELD_TRANS_ZC_HT_STATUS),
				"zcHtStatus",

				getHtStatus());

		zcHtStatus.setEnabled(false);

		zcHtStatus.setVisible(false);

		editorList.add(zcHtStatus);

		CompanyFieldEditor zcCoCode = new CompanyFieldEditor(
				LangTransMeta
						.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_CODE),
				"coCode");

		zcCoCode.setEnabled(false);

		editorList.add(zcCoCode);

		IntFieldEditor zcCoCodeNd = new IntFieldEditor(
				LangTransMeta
						.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_CODE_ND),
				"nd", 4);

		zcCoCodeNd.setEnabled(false);

		editorList.add(zcCoCodeNd);

		if (isGys()) {

			zcCoCodeNd.setVisible(false);

			zcMoneyBiSum.setVisible(false);

		}

	}

	public void initToolBar(JFuncToolBar toolBar) {

		toolBar.setModuleCode("ZC");

		toolBar.setCompoId(getCompoId());

	}

	protected void refreshData() {

		ZcXmcgHtChg zcXmcgHtChg = (ZcXmcgHtChg) listCursor.getCurrentObject();
		ZcXmcgHt zcXmcgHt = zcXmcgHtChg != null ? zcXmcgHtChg.getZcXmht()
				: new ZcXmcgHt();

		this.pageStatus = ZcSettingConstants.PAGE_STATUS_BROWSE;

		if (zcXmcgHt != null
				&& !"".equals(ZcUtil.safeString(zcXmcgHt.getZcHtCode()))) {// 列表页面双击进入

			if(zcXmcgHtChg.getZcHtChgId() == null || "".equals(zcXmcgHtChg.getZcHtChgId())){
				zcXmcgHt = this.zcXmcgHtServiceDelegate.selectByPrimaryKey(
						zcXmcgHt.getZcHtCode(), this.requestMeta);
			}else{
				zcXmcgHt = this.zcXmcgHtChgServiceDelegate.selectZcXmcgHtByPrimaryKey(
					zcXmcgHtChg.getZcHtChgId(), this.requestMeta);
			}

			if (zcXmcgHt.getZcPProMake().getZcMakeCode() != null) {

				zcXmcgHt.setZcMakeCode(zcXmcgHt.getZcPProMake().getZcMakeCode());

			}

//			BigDecimal sum = new BigDecimal(0.00);

			List<ZcXmcgHtBi> tempList = new ArrayList<ZcXmcgHtBi>();

			for (int i = 0; i < zcXmcgHt.getBiList().size(); i++) {

				ZcXmcgHtBi bi = (ZcXmcgHtBi) zcXmcgHt.getBiList().get(i);

				ZcPProMitemBi zcPProMitemBi = bi.getZcPProMitemBi();

//				if (zcPProMitemBi.getZcBiUsedSum() != null) {
//
//					sum = sum.add(zcPProMitemBi.getZcBiUsedSum());
//
//				}

				bi.setZcProBiSeq(zcPProMitemBi.getZcProBiSeq());

				if (zcPProMitemBi.getZcBiUsedSum() == null) {

					zcPProMitemBi.setZcBiUsedSum(BigDecimal.ZERO);

				}

				if (bi.getZcBiBcsySum() == null) {

					bi.setZcBiBcsySum(BigDecimal.ZERO);

				}

				tempList.add(bi);

			}
			if (isSubHt()) {
				tempList = this.zcEbBaseServiceDelegate.queryDataForList(
						"ZC_XMCG_HT_BI.ibatorgenerated_selectBiByHtCode",
						zcXmcgHt.getZcHtCode(), this.requestMeta);

				for (int i = 0; i < tempList.size(); i++) {
					ZcXmcgHtBi bi = (ZcXmcgHtBi) tempList.get(i);
					if (bi.getZcBiNo() == null || "".equals(bi.getZcBiNo())) {
						bi.setZcBiDoSum(null);
						continue;
					}
				}
			}

//			zcXmcgHt.getZcPProMake().setZcMoneyBiSum(sum);

			zcXmcgHt.setBiList(tempList);

			findMainHt(zcXmcgHt);

			this.setEditingObject(zcXmcgHt);
		} else {// 新增按钮进入

			zcXmcgHt = new ZcXmcgHt();

			zcXmcgHt.setZcHtStatus("0");

			zcXmcgHt.setNd(this.requestMeta.getSvNd());

			zcXmcgHt.setZcCgLeixing(ZcSettingConstants.PROJECT_BUY_CODE);

			zcXmcgHt.setBiList(new ArrayList<ZcXmcgHtBi>());

			zcXmcgHt.setItemList(new ArrayList<ZcTBchtItem>());

			zcXmcgHt.setPayBiList(new ArrayList<ZcHtPrePayBillItem>());

			// 新增数据默认插入一行

			ZcXmcgHtBi bi = new ZcXmcgHtBi();

			setItemBiDefaultValue(bi);

			zcXmcgHt.getBiList().add(bi);

			this.setEditingObject(zcXmcgHt);

		}

		if (!isSubHt()) {
			biTablePanel.setTableModel(ZcXmcgHtToTableModelConverter
					.convertSubBiTableData(zcXmcgHt.getBiList(), false));
		} else {
			biTablePanel.setTableModel(ZcXmcgHtToTableModelConverter
					.convertSubBiTableDataForSubHt(zcXmcgHt.getBiList(),
							wfCanEditFieldMap));
		}

		itemTablePanel.setTableModel(ZcXmcgHtToTableModelConverter
				.convertSubItemTableData(zcXmcgHt.getItemList(),isCar()));

		// 翻译从表表头列
		if (!isSubHt()) {
			ZcUtil.translateColName(biTablePanel.getTable(),
					ZcXmcgHtToTableModelConverter.getBiInfo());
		} else {
			ZcUtil.translateColName(biTablePanel.getTable(),
					ZcXmcgHtToTableModelConverter.biInfoForSubHtWihtBugdet);

		}

		ZcUtil.translateColName(itemTablePanel.getTable(),
				ZcXmcgHtToTableModelConverter.getItemInfo());

		// 设置从表监听

		addItemTableLisenter(itemTablePanel.getTable());

		addBiTableLisenter(biTablePanel.getTable());

		setBiTableEditor(biTablePanel.getTable());

		setItemTableEditor(itemTablePanel.getTable());

		this.refreshSubData(zcXmcgHt.getPayBiList());

		// 根据工作流模版设置字段是否可编辑

		updateWFEditorEditable(zcXmcgHt, requestMeta);

		if (isGys()) {

			JPageableFixedTable ta = itemTablePanel.getTable();

			hideCol(ta, "ZC_ITEM_SUM");

			hideCol(ta, "BUDGET_BI_MONEY");

			hideCol(ta, "BUDGET_OTHER_MONEY");

			hideCol(ta, "ZC_ITEM_VAL");

			hideCol(ta, "ZC_HT_BI_MONEY");

			hideCol(ta, "ZC_HT_OTHER_MONEY");

			hideCol(ta, "ZC_HT_GK_MONEY");

		}

		JTablePanel[] subs = this.getSubTables();

		for (AbstractFieldEditor fd : this.fieldEditors) {

			fd.setEnabled(false);

		}

		for (JTablePanel tablePanel : subs) {

			setWFSubTableEditable(tablePanel, false);

		}

		setSumLabelText();
		zcXmcgHtChg.setZcXmht(zcXmcgHt);
		if(editPanel != null)
		this.editPanel.setOldObject();

	}
  public void doExit() {

    if(this.wordPane != null && wordPane.isDocOpened()){
      wordPane.closeNotSave();
    }

  }

	protected void setBiTableEditor(JTable table) {

		if (isSubHt()) {

			table.setDefaultEditor(String.class, new TextCellEditor());
			String colNames[] = { "指标余额表ID", "可用金额", "资金性质", "指标类型", "指标来源",
					"业务处室", "用途", "文号标题", "功能分类" };
			ZcBudgetHandler budgetHandler = new ZcBudgetHandler(colNames,
					biTablePanel, this, listCursor, getBiDto);

			ForeignEntityFieldCellEditor suEditor = new ForeignEntityFieldCellEditor(
					"VwBudgetGp.getVwBudgetGp", getBiDto, 20, budgetHandler,
					colNames, "资金构成", "sumId");

			SwingUtil.setTableCellEditor(table, "ZC_BI_NO", suEditor);

			SwingUtil.setTableCellEditor(table, "ZC_BI_SUM",
					new MoneyCellEditor(false));

			SwingUtil.setTableCellRenderer(table, "ZC_BI_SUM",
					new NumberCellRenderer());

			SwingUtil.setTableCellEditor(table, "ZC_BI_DO_SUM",
					new MoneyCellEditor(false));

			SwingUtil.setTableCellRenderer(table, "ZC_BI_DO_SUM",
					new NumberCellRenderer());

			SwingUtil.setTableCellEditor(table, "ZC_BI_JHUA_SUM",
					new MoneyCellEditor(false));

			SwingUtil.setTableCellRenderer(table, "ZC_BI_JHUA_SUM",
					new NumberCellRenderer());

			SwingUtil.setTableCellEditor(table, "ZC_BI_YJBA_SUM",
					new MoneyCellEditor(false));

			SwingUtil.setTableCellRenderer(table, "ZC_BI_YJBA_SUM",
					new NumberCellRenderer());

			SwingUtil.setTableCellEditor(table, FIELD_TRANS_FUND_CODE,
					new AsValComboBoxCellEditor("ZC_VS_FUND_NAME"));

			SwingUtil.setTableCellRenderer(table,
					ZcElementConstants.FIELD_TRANS_FUND_CODE,
					new AsValCellRenderer("ZC_VS_FUND_NAME"));

			SwingUtil.setTableCellEditor(table, FIELD_TRANS_ORIGIN_CODE,
					new AsValComboBoxCellEditor("ZC_VS_ORIGIN_NAME"));

			SwingUtil.setTableCellRenderer(table,
					ZcElementConstants.FIELD_TRANS_ORIGIN_CODE,
					new AsValCellRenderer("ZC_VS_ORIGIN_NAME"));

			SwingUtil.setTableCellEditor(table,
					ZcElementConstants.FIELD_TRANS_PAYTYPE_CODE,
					new AsValComboBoxCellEditor("ZC_VS_PAYTYPE_NAME"));

			SwingUtil.setTableCellRenderer(table,
					ZcElementConstants.FIELD_TRANS_PAYTYPE_CODE,
					new AsValCellRenderer("ZC_VS_PAYTYPE_NAME"));

			SwingUtil.setTableCellEditor(table, "ZC_FUND_FILE",
					new FileCellEditor("zcFundFileBlobid",
							(BeanTableModel) table.getModel()));

			String status = ((ZcXmcgHt) listCursor.getCurrentObject())
					.getZcHtStatus();

			if (!"exec".equals(status) && !"yjz".equals(status)) {

				table.getTableHeader().getColumnModel()
						.removeColumn(table.getColumn("ZC_BI_YJBA_SUM"));

			}
			return;
		}
		ZcXmcgHtToTableModelConverter.setBiTableEditor(table);

	}

	public void setSumLabelText() {

		JTablePanel tablePanel = biTablePanel;
		ZcXmcgHtChg zcXmcgHtChg = (ZcXmcgHtChg) listCursor.getCurrentObject();

		ZcXmcgHt zcXmcgHt = zcXmcgHtChg == null ? new ZcXmcgHt() : zcXmcgHtChg
				.getZcXmht();

		List<ZcXmcgHtBi> biList = zcXmcgHt.getBiList() == null ? new ArrayList<ZcXmcgHtBi>()
				: zcXmcgHt.getBiList();

		BigDecimal bisum = BigDecimal.ZERO;// 实际合计

		BigDecimal yssum = BigDecimal.ZERO;// 预算合计

		for (Iterator iterator = biList.iterator(); iterator.hasNext();) {

			ZcXmcgHtBi zcXmcgHtBi = (ZcXmcgHtBi) iterator.next();

			BigDecimal b = zcXmcgHtBi.getZcPProMitemBi().getZcBiUsedSum();

			b = b == null ? BigDecimal.ZERO : b;

			bisum = bisum.add(b);

			b = zcXmcgHtBi.getZcBiBcsySum();

			b = b == null ? BigDecimal.ZERO : b;

			yssum = yssum.add(b);

		}

		JPanel jPanel = (JPanel) tablePanel.getComponent(tablePanel
				.getComponentCount() - (isSubHt() ? 2 : 1));

		JLabel jLabel = (JLabel) jPanel
				.getComponent(jPanel.getComponentCount() - 1);

		jLabel.setText("合计：    预算合计金额(" + bisum + ")   实际合计金额(" + yssum + ") ");

	}
}
