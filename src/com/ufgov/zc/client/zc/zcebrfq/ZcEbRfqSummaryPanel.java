package com.ufgov.zc.client.zc.zcebrfq;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ListCursor;
import com.ufgov.zc.client.common.ParentWindowAware;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.zc.ZcXunjiaDetailToTableModelConverter;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.JTablePanel;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.RejectBidButton;
import com.ufgov.zc.client.component.table.ColumnBeanPropertyPair;
import com.ufgov.zc.client.component.table.celleditor.DateCellEditor;
import com.ufgov.zc.client.component.table.cellrenderer.DateCellRenderer;
import com.ufgov.zc.client.component.table.codecelleditor.AsValComboBoxCellEditor;
import com.ufgov.zc.client.component.table.codecellrenderer.AsValCellRenderer;
import com.ufgov.zc.client.component.ui.fieldeditor.AbstractFieldEditor;
import com.ufgov.zc.client.component.zc.AbstractMainSubEditPanel;
import com.ufgov.zc.client.component.zc.fieldeditor.AsValFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.CompanyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.DateFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.MoneyFieldEditor;
import com.ufgov.zc.client.component.zc.fieldeditor.TextFieldEditor;
import com.ufgov.zc.client.util.SwingUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcEbRfqPack;
import com.ufgov.zc.common.zc.model.ZcEbXunJiaBaoJia;
import com.ufgov.zc.common.zc.model.ZcXmcgHt;
import com.ufgov.zc.common.zc.model.ZcXmcgHtExample;
import com.ufgov.zc.common.zc.model.ZcXunJiaDetail;
import com.ufgov.zc.common.zc.model.ZcXunJiaSummary;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbXjbjServiceDelegate;

/**
 * 开标后的界面
 * @author Administrator
 *
 */
public class ZcEbRfqSummaryPanel extends AbstractMainSubEditPanel implements ParentWindowAware {

  private Window parentWindow;

  private FuncButton exitButton = new ExitButton();

  //废标
  private FuncButton rejectBidButton = new RejectBidButton();

  JTablePanel summaryTablePanel = null;

  private List<ZcEbXunJiaBaoJia> xjbjList;

  private List<ZcXunJiaDetail> xjdList;

  private List<ZcXunJiaSummary> xjSummaryList = new ArrayList<ZcXunJiaSummary>();

  private String compoId = "ZC_EB_RFQ";

  private ZcEbRfqListPanel listPanel;

  private ZcEbRfqPack zcEbRfqPack;

  private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private static List<ColumnBeanPropertyPair> SummaryInfo = new ArrayList<ColumnBeanPropertyPair>();

  private IZcEbXjbjServiceDelegate zcebXjbjServiceDelegate = (IZcEbXjbjServiceDelegate) ServiceFactory.create(IZcEbXjbjServiceDelegate.class,

  "zcebXjbjServiceDelegate");

  private IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,

  "zcEbBaseServiceDelegate");

  public ZcEbRfqSummaryPanel(ZcEbRfqPack zcEbRfqPack, ZcEbRfqListPanel listPanel) {
    this.zcEbRfqPack = zcEbRfqPack;
    this.listPanel = listPanel;
    this.workPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "询价开标管理", TitledBorder.CENTER, TitledBorder.TOP,
      new Font("宋体", Font.BOLD, 15), Color.BLUE));
    this.colCount = 3;
    requestMeta.setCompoId(compoId);
    init();
    this.setEditingObject(zcEbRfqPack);
  }

  public Window getParentWindow() {
    return parentWindow;
  }

  public void setParentWindow(Window parentWindow) {
    this.parentWindow = parentWindow;
  }

  public void initToolBar(JFuncToolBar toolBar) {
    toolBar.add(rejectBidButton);
    toolBar.add(exitButton);

    exitButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doExit();

      }

    }); 
    rejectBidButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        doRejectBid();

      }

    });

  }

  /**
   * 这里的废标，用于生产评标报告以后，有可能已经发了中标公告、通知书等，所以要将这些都删除，如果已经有了合同，则提示先作废合同，再进行作废
   */
  protected void doRejectBid() {
    // TCJLODO Auto-generated method stub

    JTextField reason = new JTextField();

    JLabel label = new JLabel();

    Object[] message = { "请输入【废标】原因:", reason, label };

    int num = this.getRejectReason(message, label, reason);

    if (num == JOptionPane.YES_OPTION) {
      
      //检查有无采购对应的采购合同存在
      Object htCount=zcEbBaseServiceDelegate.queryObject("ZC_XMCG_HT.selectHtCountByPack", zcEbRfqPack.getPackCode(), requestMeta);
      if(htCount!=null){
        int c=((Integer)htCount).intValue();
        if(c>0){
          JOptionPane.showMessageDialog(this, "该中标已经存在合同，请先删除！", "提示", JOptionPane.ERROR_MESSAGE);
          return;
        }
      }
      zcEbRfqPack.setOpenBidStatus(ZcSettingConstants.XUNJIA_STATUS_OPEN_BID_CRAP);

      zcEbRfqPack.setStatus(ZcSettingConstants.XUNJIA_STATUS_OPEN_BID_CRAP);

      zcEbRfqPack.setIsGoonAudit(ZcSettingConstants.RFQ_AGREE_TRASH_BID);

      zcEbRfqPack.setWinBidProviderCode(null);

      zcEbRfqPack.setWinBidProviderName(null);

      zcEbRfqPack.setWinBidSum(null);

      try {
        this.listPanel.zcEbRfqServiceDelegate.crapBidFN(zcEbRfqPack, requestMeta);
        setEditingObject(zcEbRfqPack);
        this.listPanel.refreshCurrentTabData();
        JOptionPane.showMessageDialog(this, "废标成功", "提示", JOptionPane.INFORMATION_MESSAGE);
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "废标出错：\n" + e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
      }
    } else {

      return;

    }
    
  }
  private int getRejectReason(Object[] message, JLabel label, JTextField reason) {

    int res = JOptionPane.showConfirmDialog(this, message, "确认【废标】吗？", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

    if (res == JOptionPane.OK_OPTION && reason.getText().trim().length() < 5) {

      label.setText("原因太简单，请补充.");

      res = this.getRejectReason(message, label, reason);

    }

    if (res == JOptionPane.OK_OPTION) {

      zcEbRfqPack.setReason("【废标原因】" + reason.getText());

      label.setText("");

    }

    return res;

  }
  public List<AbstractFieldEditor> createFieldEditors() {

    final List<AbstractFieldEditor> editorList = new ArrayList<AbstractFieldEditor>();

    TextFieldEditor editor0;

    editor0 = new TextFieldEditor("项目编号", "projCode");

    editorList.add(editor0);

    editor0 = new TextFieldEditor("项目名称", "projName");

    editorList.add(editor0);

    editor0 = new TextFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_PACK_NAME), "packName");

    editorList.add(editor0);

    CompanyFieldEditor editor1 = new CompanyFieldEditor(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_CO_NAME), "coCode");

    editorList.add(editor1);

    AsValFieldEditor editor3 = new AsValFieldEditor("报价状态", "openBidStatus", ZcElementConstants.VS_ZC_EB_RFQ_OPEN_BID_STATUS);

    editorList.add(editor3);

    editor0 = new TextFieldEditor("报价供应商个数", "signUpedProviderNum");

    editorList.add(editor0);

    MoneyFieldEditor editor2 = new MoneyFieldEditor("最低报价", "bidMinPrice");

    editorList.add(editor2);

    editor0 = new TextFieldEditor("最低报价供应商", "bidMinPriceProvider");

    editorList.add(editor0);

    DateFieldEditor editor5 = new DateFieldEditor("报价截止时间", "bidEndTime", DateFieldEditor.TimeTypeH24);

    editorList.add(editor5);

    editor5 = new DateFieldEditor("报价开启时间", "openBidDate", DateFieldEditor.TimeTypeH24);

    editorList.add(editor5);

    editor0 = new TextFieldEditor("经办人", "creator");

    editorList.add(editor0);

    editor0 = new TextFieldEditor("报价延期/失败原因", "reason");

    editorList.add(editor0);

    for (AbstractFieldEditor edit : editorList) {
      edit.setEnabled(false);
    }

    return editorList;

  }

  public JComponent createSubBillPanel() {

    JTabbedPane p = new JTabbedPane();
    p.add("供应商投标情况汇总表", createSummaryTablePanel());

    return p;

  }

  private JTablePanel createSummaryTablePanel() {

    ElementConditionDto dto = new ElementConditionDto();

    dto.setZcText0(zcEbRfqPack.getProjCode());

    dto.setPackCode(zcEbRfqPack.getPackCode());

    dto.setProjCode(zcEbRfqPack.getProjCode());

    dto.setStatus("done");

    xjbjList = zcebXjbjServiceDelegate.getXunJiaBaoJiaByCondition(dto, requestMeta);

    /**

     * 获取询价报价的明细

     */

    xjdList = listPanel.zcEbRfqServiceDelegate.getXunjiaDetaiList(dto, requestMeta);
    if (xjdList == null) {
      xjdList = new ArrayList<ZcXunJiaDetail>();
    }

    //如果没有中标人，那么暂时将暂时中标人设置为一个不可能存在的值

    String winnerCode = zcEbRfqPack.getWinBidProviderCode() == null ? "##@##" : zcEbRfqPack.getWinBidProviderCode();
    ZcXunJiaSummary sum = null;

    /**

     * 先按供应商进行分组，同一个供应商的报价明细分到一组，然后汇总每个供应商报价的总和。

     */

    for (int n = 0; n < xjbjList.size(); n++) {

      ZcEbXunJiaBaoJia bj = xjbjList.get(n);

      sum = new ZcXunJiaSummary();

      List<ZcXunJiaDetail> xunJiaDetailList = new ArrayList<ZcXunJiaDetail>();

      for (int m = 0; m < xjdList.size(); m++) {

        ZcXunJiaDetail xj1 = xjdList.get(m);

        if (bj.getProjCode().equals(xj1.getProjCode()) && bj.getSupplierCode().equals(xj1.getProviderCode())) {

          xunJiaDetailList.add(xj1);

        }

      }

      sum.setXunJiaDetailList(xunJiaDetailList);

      //询价报价存在多个明细的话：只要有一个明细有现货，汇总结果就是有现货；供货时间取最早的供货时间

      String haveXianhuo = "N";

      Date gonghuoDate = xunJiaDetailList.get(0).getGongHuoDate();

      BigDecimal totalPrice = new BigDecimal(0.0);

      for (ZcXunJiaDetail detail : xunJiaDetailList) {

        if ("Y".equals(detail.getHaveXianHuo())) {

          haveXianhuo = "Y";

        }

        if (gonghuoDate != null) {

          if (detail.getGongHuoDate() != null && gonghuoDate.after(detail.getGongHuoDate())) {

            gonghuoDate = detail.getGongHuoDate();

          }

        }

        totalPrice = totalPrice.add(detail.getSpTotalSum());

      }

      sum.setHaveXianHuo(haveXianhuo);

      sum.setGongHuoDate(gonghuoDate);

      sum.setIsClosedDeal(winnerCode.equals(bj.getSupplierCode()) ? "Y" : "N");

      sum.setProviderCode(bj.getSupplierCode());

      sum.setProviderName(bj.getSupplierName());

      sum.setTotalPrice(totalPrice);

      sum.setLinkMan(bj.getManager());

      sum.setLinkTel(bj.getPhone());

      sum.setRemark(bj.getRemark());

      sum.setBjDate(bj.getBjDate());

      xjSummaryList.add(sum);

    }

    zcEbRfqPack.setSignUpedProviderNum(this.xjSummaryList.size() + "");

    getLowestPrice(xjSummaryList, zcEbRfqPack);

    setEditingObject(zcEbRfqPack);

    summaryTablePanel = new JTablePanel();

    summaryTablePanel.init();

    summaryTablePanel.setTableModel(ZcXunjiaDetailToTableModelConverter.convertSummaryTableData(xjSummaryList, SummaryInfo));

    summaryTablePanel.getSearchBar().setVisible(false);

    ZcUtil.translateColName(summaryTablePanel.getTable(), SummaryInfo);

    SwingUtil.setTableCellEditor(summaryTablePanel.getTable(), "IS_CLOSED_DEAL", new AsValComboBoxCellEditor("VS_Y/N"));

    SwingUtil.setTableCellRenderer(summaryTablePanel.getTable(), "IS_CLOSED_DEAL", new AsValCellRenderer("VS_Y/N"));

    SwingUtil.setTableCellEditor(summaryTablePanel.getTable(), "HAVE_XIAN_HUO", new AsValComboBoxCellEditor("VS_Y/N"));

    SwingUtil.setTableCellRenderer(summaryTablePanel.getTable(), "HAVE_XIAN_HUO", new AsValCellRenderer("VS_Y/N"));

    SwingUtil.setTableCellEditor(summaryTablePanel.getTable(), "GONG_HUO_DATE", new DateCellEditor());

    SwingUtil.setTableCellRenderer(summaryTablePanel.getTable(), "BJ_DATE", new DateCellRenderer(DateCellRenderer.SS));

    return summaryTablePanel;

  }

  public void doExit() {

    this.parentWindow.dispose();

  }

  static {

    SummaryInfo.add(new ColumnBeanPropertyPair("PROVIDER_NAME", "providerName", "供应商名称"));

    SummaryInfo.add(new ColumnBeanPropertyPair("TOTAL_PRICE", "totalPrice", "总报价(元)"));

    //    SummaryInfo.add(new ColumnBeanPropertyPair("HAVE_XIAN_HUO", "haveXianHuo", "是否现货"));

    SummaryInfo.add(new ColumnBeanPropertyPair("GONG_HUO_DATE", "gongHuoDate", "供货时间"));

    SummaryInfo.add(new ColumnBeanPropertyPair("BJ_DATE", "bjDate", "报价时间"));

    SummaryInfo.add(new ColumnBeanPropertyPair("IS_CLOSED_DEAL", "isClosedDeal", "是否成交"));

    //    SummaryInfo.add(new ColumnBeanPropertyPair("LINK_MAN", "linkMan", "联系人"));
    //
    //    SummaryInfo.add(new ColumnBeanPropertyPair("LINK_TEL", "linkTel", "联系电话"));
    //
    //    SummaryInfo.add(new ColumnBeanPropertyPair("REMARK", "remark", "备注信息"));

  }

  private void getLowestPrice(List<ZcXunJiaSummary> list, ZcEbRfqPack zcEbRfqPack) {

    if (list.size() > 0) {

      ZcXunJiaSummary lowestPrice = this.getMinPriceProvider(list);

      zcEbRfqPack.setBidMinPriceProvider(lowestPrice.getProviderName());

      zcEbRfqPack.setBidMinPrice(lowestPrice.getTotalPrice());

    }

  }

  /**

   * 获取最低价最早供货的供应商

   * @param list

   * @return

   */

  private ZcXunJiaSummary getMinPriceProvider(List list) {

    ZcXunJiaSummary min = null;

    for (int i = 0; i < list.size(); i++) {

      ZcXunJiaSummary curr = (ZcXunJiaSummary) list.get(i);

      if (min == null) {

        min = curr;

        continue;

      }

      if (curr.getTotalPrice() == null) {

        curr.setTotalPrice(new BigDecimal("0"));

      }

      int compare = min.getTotalPrice().compareTo(curr.getTotalPrice());

      if (compare > 0) {

        min = curr;

      } else if (compare == 0) {

        if (min.getGongHuoDate() != null && curr.getGongHuoDate() != null) {

          if (min.getGongHuoDate().after(curr.getGongHuoDate())) {

            min = curr;

          }

        } else if (min.getGongHuoDate() == null && curr.getGongHuoDate() != null) {

          min = curr;

        }

      }

    }

    return min;

  }
}
