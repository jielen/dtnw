package com.ufgov.zc.client.zc.supplierprice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.GkBaseDialog;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.BidButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.ExitButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.SaveButton;
import com.ufgov.zc.client.component.button.UnBidButton;
import com.ufgov.zc.client.zc.supplierprice.component.SupplierPriceTableView;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcEbSignup;
import com.ufgov.zc.common.zc.model.ZcEbSignupPackDetail;
import com.ufgov.zc.common.zc.publish.ISupplierPriceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbSignupServiceDelegate;

public class PriceDetailEditPanel extends JPanel implements ActionListener {
  private JFuncToolBar toolbar;

  private ISupplierPriceDelegate priceDelegeate = (ISupplierPriceDelegate) ServiceFactory.create(ISupplierPriceDelegate.class,
    "supplierPriceDelegate");

  private IZcEbBaseServiceDelegate zcEbBaseDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,
    "zcEbBaseServiceDelegate");

  private IZcEbSignupServiceDelegate zcEbSignupServiceDelegate = (IZcEbSignupServiceDelegate) ServiceFactory.create(IZcEbSignupServiceDelegate.class,
    "zcEbSignupServiceDelegate");

  private RequestMeta meta = WorkEnv.getInstance().getRequestMeta();

  private JTabbedPane packDetailPane = null;

  private List<SupplierPriceTableView> packDetailViews = new ArrayList();

  private JLabel projNameLabel = new JLabel();

  private JLabel projCodeLabel = new JLabel();

  private JLabel signUppersonLabel = new JLabel();

  private JLabel packCodeLabel = new JLabel();

  private SaveButton sButton = null;

  private EditButton uButton = null;

  private ExitButton exitButton = null;

  private FuncButton bidButton = null;

  private FuncButton unBidButton = null;

  private GkBaseDialog parentDialog;

  private ZcEbSignup signup = null;

  private SignupListPanel listPanel = null;

  public PriceDetailEditPanel(GkBaseDialog parentDialog) {
    super();
    // TCJLODO:
    meta.setCompoId("ZC_EB_SUP_PRICE");
    this.setLayout(new BorderLayout());
    this.add(createToolBar(), BorderLayout.NORTH);
    this.add(createContentPanel(), BorderLayout.CENTER);
    this.parentDialog = parentDialog;
  }

  public PriceDetailEditPanel(GkBaseDialog parentDialog, SignupListPanel listPanel) {
    super();
    // TCJLODO:
    meta.setCompoId("ZC_EB_SUP_PRICE");
    this.setLayout(new BorderLayout());
    this.add(createToolBar(), BorderLayout.NORTH);
    this.add(createContentPanel(), BorderLayout.CENTER);
    this.parentDialog = parentDialog;

    this.listPanel = listPanel;

  }

  public void setSignup(ZcEbSignup signUp) {
    this.signup = (ZcEbSignup) ObjectUtil.deepCopy(signUp);
    ElementConditionDto dto=new ElementConditionDto();
    dto.setZcText1(signup.getSignupId());
    List<ZcEbSignupPackDetail> details = zcEbSignupServiceDelegate.getZcEbSignupPackDetail(dto, meta);
    this.signup.setSignupPacks(details);
    for (ZcEbSignupPackDetail packDetail : details) {
      SupplierPriceTableView priceView = new SupplierPriceTableView();
      priceView.setSignPackDetail(packDetail);
      packDetailPane.addTab(packDetail.getPackName(), priceView);
      packDetailViews.add(priceView);
    }
    setSingupInfo(this.signup);
    setButtonEnabled(null);
    Double d = (Double) zcEbBaseDelegate.queryObject("ZcEbPlan.getBidEndTime", signup.getSignupId(), meta);

    if (d == null || d < 0) {
      //      sButton.setEnabled(false);
      //      uButton.setEnabled(false);
    }

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    if (command.equals("save")) {
      this.doSave();
    } else if (command.equals("update")) {
      this.doUpdate();
    } else if (command.equals("exit")) {
      this.doExit();
    } else if (command.equals("bid")) {
      this.doBid();
    } else if (command.equals("unbid")) {
      this.doUnBid();
    }
  }

  private JToolBar createToolBar() {
    toolbar = new JFuncToolBar();
    toolbar.setModuleCode("ZC");
    toolbar.setCompoId(meta.getCompoId());
    sButton = new SaveButton();
    sButton.setActionCommand("save");
    sButton.addActionListener(this);
    toolbar.add(sButton);
    uButton = new EditButton();
    uButton.setActionCommand("update");
    uButton.addActionListener(this);
    toolbar.add(uButton);

    bidButton = new BidButton();
    bidButton.setActionCommand("bid");
    bidButton.addActionListener(this);
    toolbar.add(bidButton);

    unBidButton = new UnBidButton();
    unBidButton.setActionCommand("unbid");
    unBidButton.addActionListener(this);
    toolbar.add(unBidButton);

    exitButton = new ExitButton();
    exitButton.setActionCommand("exit");
    exitButton.addActionListener(this);
    toolbar.add(exitButton);

    return toolbar;
  }

  private JComponent createContentPanel() {
    JPanel centerPanel = new JPanel();
    centerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "报价明细单", TitledBorder.CENTER, TitledBorder.TOP,
      new Font("宋体", Font.BOLD, 15), Color.BLUE));
    centerPanel.setLayout(new BorderLayout());
    centerPanel.add(createSignupInfoPanel(), BorderLayout.NORTH);
    centerPanel.add(createDetailPanel(), BorderLayout.CENTER);

    return centerPanel;
  }

  private JComponent createDetailPanel() {
    packDetailPane = new JTabbedPane();
    return packDetailPane;
  }

  private JPanel createSignupInfoPanel() {
    JPanel signupInfoPanel = new JPanel();
    signupInfoPanel.setLayout(new GridLayout(4, 2, 20, 20));
    signupInfoPanel.add(new JLabel());
    signupInfoPanel.add(new JLabel());
    signupInfoPanel.add(projNameLabel);// new JLabel("项目名称:")
    signupInfoPanel.add(projCodeLabel);// new JLabel("项目编号:")
    signupInfoPanel.add(signUppersonLabel);// new JLabel("投标人名称（加盖投标人公章）:")
    signupInfoPanel.add(packCodeLabel);// new JLabel("投标包号：第 100 包   ")
    signupInfoPanel.add(new JLabel());
    signupInfoPanel.add(new JLabel());
    return signupInfoPanel;
  }

  private void doSave() {
    try {

      StringBuffer sb = new StringBuffer();
      for (SupplierPriceTableView view : packDetailViews) {
        view.setUnEdit();
      }
      if (sb.length() > 0) {
        JOptionPane.showMessageDialog(this.parentDialog, sb.toString(), "提示", JOptionPane.WARNING_MESSAGE);
        return;
      }
      meta.setFuncId(this.sButton.getFuncId());
      priceDelegeate.updateSignUpPackPrice(signup, meta);
      JOptionPane.showMessageDialog(this, "保存成功", "提示", JOptionPane.INFORMATION_MESSAGE);
      setButtonEnabled("save");
      for (SupplierPriceTableView view : packDetailViews)
        view.setEditable(false);

      this.listPanel.refreshCurrentTabData();
    } catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, "保存失败:" + ex.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void doUpdate() {
    for (SupplierPriceTableView view : packDetailViews)
      view.setEditable(true);
    setButtonEnabled("update");

    this.listPanel.refreshCurrentTabData();
  }

  private void doExit() {
    this.parentDialog.dispose();
  }

  private void doBid() {

    int flag = JOptionPane.showConfirmDialog(this.parentDialog, "确定要投标吗？", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

    if (flag != 0)
      return;

    if (!checkBidEndDate()) {

      JOptionPane.showMessageDialog(this.parentDialog, "开标时间已结束，不能投标", "提示", JOptionPane.WARNING_MESSAGE);

      return;
    }

    StringBuffer sb = new StringBuffer();
    for (SupplierPriceTableView view : packDetailViews) {
      sb.append(view.check());
    }
    if (sb.length() > 0) {
      JOptionPane.showMessageDialog(this.parentDialog, sb.toString(), "提示", JOptionPane.WARNING_MESSAGE);
      return;
    }

    signup.setIsSubmitBidDoc("Y");

    try {
      meta.setFuncId(this.bidButton.getFuncId());

      zcEbSignupServiceDelegate.updateZcEbSignupPropertyFN(signup, meta);

      JOptionPane.showMessageDialog(this, "投标成功", "提示", JOptionPane.INFORMATION_MESSAGE);

      setButtonEnabled("bid");

    } catch (Exception e) {

      JOptionPane.showMessageDialog(this, "投标失败:" + e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
    }

    this.listPanel.refreshCurrentTabData();
  }

  private void doUnBid() {

    int flag = JOptionPane.showConfirmDialog(this.parentDialog, "确定要撤销投标吗？", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

    if (flag != 0)
      return;

    if (!checkBidEndDate()) {

      JOptionPane.showMessageDialog(this.parentDialog, "开标时间已结束，不能撤回", "提示", JOptionPane.WARNING_MESSAGE);

      return;
    }

    signup.setIsSubmitBidDoc("N");

    try {
      meta.setFuncId(this.unBidButton.getFuncId());

      zcEbSignupServiceDelegate.updateZcEbSignupPropertyFN(signup, meta);

      JOptionPane.showMessageDialog(this, "撤回成功", "提示", JOptionPane.INFORMATION_MESSAGE);

      setButtonEnabled("unbid");

    } catch (Exception e) {

      JOptionPane.showMessageDialog(this, "撤回失败:" + e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
    }

    this.listPanel.refreshCurrentTabData();
  }

  private void setSingupInfo(ZcEbSignup signUp) {
    if (signUp != null) {
      projNameLabel
        .setText("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><font color='blue' size='4'>项目名称：</font></b>"
          + "<b><font color='blue' size='4'>" + signUp.getProjName() + "<font></b></html>");
      projCodeLabel
        .setText("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><font color='blue' size='4'>项目编号：</font></b>"
          + "<b><font color='blue' size='4'>" + signUp.getProjCode() + "</font></b></html>");
      signUppersonLabel
        .setText("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><font color='blue' size='4'>投标人名称（加盖投标人公章）：</font></b>"
          + "<b><font color='blue' size='4'>" + signUp.getProviderName() + "</font></b></html>");
      // packCodeLabel
      // .setText("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><font color='blue' size='4'>投标包号：</font></b>"
      // + "<b><font color='blue' size='4'>" + packDetail.getPackName() +
      // "</font></b></html>");
    }
  }

  private void setButtonEnabled(String commond) {
    if ("save".equals(commond)) {
      sButton.setEnabled(false);
      uButton.setEnabled(true);
      bidButton.setEnabled(true);
      unBidButton.setEnabled(false);
    } else if ("update".equals(commond)) {
      sButton.setEnabled(true);
      uButton.setEnabled(false);
      bidButton.setEnabled(false);
      unBidButton.setEnabled(false);
    } else if ("bid".equals(commond)) {
      sButton.setEnabled(false);
      uButton.setEnabled(false);
      bidButton.setEnabled(false);
      unBidButton.setEnabled(true);
    } else if ("unbid".equals(commond)) {
      sButton.setEnabled(false);
      uButton.setEnabled(true);
      bidButton.setEnabled(true);
      unBidButton.setEnabled(false);
    } else {
      if ("Y".equals(signup.getIsSubmitBidDoc())) {
        sButton.setEnabled(false);
        uButton.setEnabled(false);
        bidButton.setEnabled(false);
        unBidButton.setEnabled(true);
      } else {
        sButton.setEnabled(false);
        uButton.setEnabled(true);
        bidButton.setEnabled(true);
        unBidButton.setEnabled(false);
      }
    }
  }

  public boolean checkBidEndDate() {
    //目前先不做校验，主要走纸质报价
    //	  Double d = (Double) zcEbBaseDelegate.queryObject("ZcEbPlan.getBidEndTime", signup.getSignupId(), meta);
    //
    //    if (d == null || d < 0) {
    //      return false;
    //    }

    return true;
  }

}
