package com.ufgov.zc.client.zc.workFlowList;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import com.ufgov.smartclient.plaf.BlueLookAndFeel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.component.button.PrintButton;
import com.ufgov.zc.client.component.table.combineTable.CombineData;
import com.ufgov.zc.client.component.table.combineTable.CombineTable;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.util.DateUtil;
import com.ufgov.zc.common.zc.model.WorkFlowAction;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;

public class WorkFlowListPanel extends JPanel {

  private String snCode;

  private CombineTable workFlowTable;

  public RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  public IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,
    "zcEbBaseServiceDelegate");

  public WorkFlowListPanel(String snCode) {
    this.setLayout(new BorderLayout());
    this.snCode = snCode;
    workFlowTable = createWorkFlowTable();
    this.add(initToolBar(), BorderLayout.NORTH);
    this.add(new JScrollPane(workFlowTable), BorderLayout.CENTER);
  }

  private JToolBar initToolBar() {
    JToolBar bar = new JToolBar();
    PrintButton printButton = new PrintButton();
    bar.add(printButton);
    printButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        doPrint();
      }

    });

    return bar;
  }

  private String[][] getWorkFlowData() {
    List workFlowList = zcEbBaseServiceDelegate.queryDataForList("ZcWfFlowAction.getWorkFlowActionList", snCode, requestMeta);
    String[][] datas = new String[workFlowList.size()][6];
    for (int i = 0; i < workFlowList.size(); i++) {
      WorkFlowAction workFlowAciton = (WorkFlowAction) workFlowList.get(i);
      datas[i][0] = workFlowAciton.getTemplatName();//功能模块
      datas[i][1] = workFlowAciton.getNodeName();//节点名称
      datas[i][2] = workFlowAciton.getUserName();//审核人

      datas[i][3] = DateUtil.dateToSsString(workFlowAciton.getExecuteTime());//审核时间
      if (workFlowAciton.getActionName().equals("untread_flow")) {
        datas[i][4] = "退回";//流向
      } else {
        datas[i][4] = "审批通过";
      }

      datas[i][5] = workFlowAciton.getDescription();//审批意见
    }
    return datas;
  }

  private CombineTable createWorkFlowTable() {
    String[][] datas = getWorkFlowData();
    ArrayList<Integer> combineColumns = new ArrayList<Integer>();
    combineColumns.add(0);
    CombineData m = new CombineData(datas, combineColumns);
    DefaultTableModel tm = new DefaultTableModel(datas, new String[] { "功能模块", "流程节点", "审核人", "审核时间", "流向", "审批意见" });
    CombineTable cTable = new CombineTable(m, tm);
    cTable.getTableHeader().setReorderingAllowed(false);
    cTable.getTableHeader().setResizingAllowed(false);

    //    cTable.getTableHeader().setd

    //    TableColumn column = cTable.getColumnModel().getColumn(0);
    //
    //    column.setCellRenderer(new CombineColumnRender());
    //
    //    column.setWidth(50);
    //    column.setMaxWidth(50);
    //    column.setMinWidth(50);
    //    cTable.setCellSelectionEnabled(true);

    //    String[][] datas = new String[10][6];
    //    for (int i = 0; i < datas.length; i++) {
    //      String[] data = datas[i];
    //      for (int j = 0; j < data.length; j++) {
    //        data[j] = "";
    //      }
    //      data[0] = String.valueOf((int) (i / 3));
    //    }
    //
    //    ArrayList<Integer> combineColumns = new ArrayList<Integer>();
    //    combineColumns.add(0);
    //    CombineData m = new CombineData(datas, combineColumns);
    //    DefaultTableModel tm = new DefaultTableModel(datas, new String[] { "1", "2", "3", "4", "5" });
    //    CombineTable cTable = new CombineTable(m, tm);

    //    TableColumn column = cTable.getColumnModel().getColumn(0);
    //    column.setCellRenderer(new CombineColumnRender());
    //    column.setWidth(50);
    //    column.setMaxWidth(50);
    //    column.setMinWidth(50);
    //    cTable.setCellSelectionEnabled(true);
    return cTable;

  }

  private void doPrint() {
    try {
      workFlowTable.print();
    } catch (PrinterException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          UIManager.setLookAndFeel(new BlueLookAndFeel());
        } catch (Exception e) {
          e.printStackTrace();
        }
        JFrame jf = new JFrame("工作审批流列表");
        WorkFlowListPanel bill = new WorkFlowListPanel("2013-0001");
        jf.getContentPane().add(bill);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(500, 500);
        jf.setVisible(true);
      }
    });

  }
}
