package com.ufgov.zc.client.zc.infoscreen.realmessage;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import com.ufgov.smartclient.common.DefaultInvokeHandler;
import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.JGroupableTable;
import com.ufgov.smartclient.plaf.BlueLookAndFeel;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.SendButton;
import com.ufgov.zc.client.component.ui.AbstractDataDisplay;
import com.ufgov.zc.client.component.ui.AbstractEditListBill;
import com.ufgov.zc.client.component.ui.AbstractSearchConditionArea;
import com.ufgov.zc.client.component.ui.MultiDataDisplay;
import com.ufgov.zc.client.component.ui.SaveableSearchConditionArea;
import com.ufgov.zc.client.component.ui.TableDisplay;
import com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem;
import com.ufgov.zc.client.component.ui.conditionitem.SearchConditionUtil;
import com.ufgov.zc.client.util.BalanceUtil;
import com.ufgov.zc.client.zc.ztb.util.Guid;
import com.ufgov.zc.common.commonbiz.model.SearchCondition;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.RealMessage;
import com.ufgov.zc.common.zc.model.ZcEbSignup;
import com.ufgov.zc.common.zc.publish.IZcAnimationServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbSignupServiceDelegate;

public class RealMesagePanel extends AbstractEditListBill {
  private final String compoId = "ZC_CALL_MESSAGE";

  private final ElementConditionDto elementConditionDto = new ElementConditionDto();

  private AbstractSearchConditionArea topSearchConditionArea;

  private final RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private final IZcEbSignupServiceDelegate zcEbSignupServiceDelegate = (IZcEbSignupServiceDelegate) ServiceFactory.create(
    IZcEbSignupServiceDelegate.class, "zcEbSignupServiceDelegate");

  private final IZcAnimationServiceDelegate messageService = (IZcAnimationServiceDelegate) ServiceFactory.create(IZcAnimationServiceDelegate.class,
    "animationServiceDelegate");

  private RealMesagePanel self;

  public RealMesagePanel() {
    super();
    self = this;
    requestMeta.setCompoId(compoId);
    elementConditionDto.setStatus("1");
    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {
      @Override
      public List<SearchCondition> execute() throws Exception {
        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonList(WorkEnv.getInstance()
          .getCurrUserId(), "zccallmessage_tab");
        return needDisplaySearchConditonList;
      }

      @Override
      public void success(List<SearchCondition> needDisplaySearchConditonList) {
        List<TableDisplay> showingDisplays = SearchConditionUtil.getNeedDisplayTableDisplay(needDisplaySearchConditonList);
        init(createDataDisplay(showingDisplays), null);//调用父类方法
        revalidate();
        repaint();
      }
    });
    requestMeta.setCompoId(compoId);
  }

  @Override
  protected void addToolBarComponent(JFuncToolBar toolBar) {

    SendButton sendMessBtn = new SendButton();
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(compoId);
    toolBar.add(sendMessBtn);

    sendMessBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doSendMessage();
      }
    });
  }

  private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {

    return new DataDisplay(SearchConditionUtil.getAllTableDisplay("zccallmessage_tab"), showingDisplays, createTopConditionArea(), true);

  }

  private AbstractSearchConditionArea createTopConditionArea() {

    Map defaultValueMap = new HashMap();

    topSearchConditionArea = new SaveableSearchConditionArea("zccallmessage", null, true, defaultValueMap, null);

    return topSearchConditionArea;

  }

  private final class DataDisplay extends MultiDataDisplay {

    private DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays, AbstractSearchConditionArea conditionArea,
      boolean showConditionArea) {
      super(displays, showingDisplays, conditionArea, showConditionArea, "zccallmessage_tab");
      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "呼叫供应商", TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体",
        Font.BOLD, 15), Color.BLUE));
    }

    @Override
    protected void handleTableDisplayActived(AbstractSearchConditionItem[] searchConditionItems, final TableDisplay tableDisplay) {
      elementConditionDto.setWfcompoId(compoId);
      elementConditionDto.setExecutor(WorkEnv.getInstance().getCurrUserId());
      elementConditionDto.setNd(WorkEnv.getInstance().getTransNd());
      elementConditionDto.setStatus(tableDisplay.getStatus());
      elementConditionDto.setMonth(BalanceUtil.getMonthIdBySysOption());
      for (AbstractSearchConditionItem item : searchConditionItems) {
        item.putToElementConditionDto(elementConditionDto);
      }
      final Container c = tableDisplay.getTable().getParent();
      UIUtilities.asyncInvoke(new DefaultInvokeHandler<TableModel>() {
        @Override
        public void before() {
          assert c != null;
          installLoadingComponent(c);
        }

        @Override
        public void after() {
          assert c != null;
          unInstallLoadingComponent(c);
          c.add(tableDisplay.getTable());
        }

        @Override
        public TableModel execute() throws Exception {
          ZcEbSignTableModel model = new ZcEbSignTableModel();
          model.setRecords(self.zcEbSignupServiceDelegate.getRealMessageSignupList(elementConditionDto, requestMeta));
          return model;
        }

        @Override
        public void success(TableModel model) {
          tableDisplay.setTableModel(model);
        }
      });
    }
  }

  private void doSendMessage() {
    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();
    ZcEbSignTableModel model = (ZcEbSignTableModel) table.getModel();
    Integer[] indexs = table.getCheckedRows();
    if (indexs.length == 0) {
      JOptionPane.showMessageDialog(this, "请选择供应商", "警告", JOptionPane.WARNING_MESSAGE);
    } else {
      try {
        String suppliesStr = "";
        ZcEbSignup signUp = null;
        int index = 0;
        for (int i = 0; i < indexs.length; i++) {
          index = table.convertRowIndexToModel(indexs[i]);
          signUp = (ZcEbSignup) model.getRecord(index);
          suppliesStr += "," + signUp.getProviderName();
        }
        if (suppliesStr.length() > 0)
          suppliesStr = suppliesStr.substring(1);
        RealMessage mess = new RealMessage();
        mess.setId(Guid.genID());
        mess.setType("ZC");
        mess.setBody(signUp.getProjName() + ";" + suppliesStr + ";" + this.elementConditionDto.getBiddingRoom());
        this.messageService.insertRealMessage(mess, requestMeta);
        JOptionPane.showMessageDialog(this, "发送消息成功", "提示", JOptionPane.INFORMATION_MESSAGE);
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "发送消息失败:" + ex.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  public static void main(String[] args) throws Exception {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(new BlueLookAndFeel());
        } catch (Exception e) {
          e.printStackTrace();
        }
        //        UIManager.getDefaults().put("SplitPaneUI", BigButtonSplitPaneUI.class.getName());
        RealMesagePanel bill = new RealMesagePanel();
        JFrame frame = new JFrame("frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(bill);
        frame.setVisible(true);
      }

    });
  }

  static {
    LangTransMeta.init("ZC%");
  }
}
