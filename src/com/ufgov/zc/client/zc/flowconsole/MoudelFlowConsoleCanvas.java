package com.ufgov.zc.client.zc.flowconsole;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.ufgov.smartclient.plaf.BlueLookAndFeel;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.StringToModel;
import com.ufgov.zc.client.common.UIConstants;
import com.ufgov.zc.client.component.ui.AbstractDataDisplay;
import com.ufgov.zc.client.component.ui.AbstractEditListBill;
import com.ufgov.zc.client.zc.flowconsole.business.INodeBusiness;
import com.ufgov.zc.client.zc.flowconsole.events.ButtonMouseEvent;
import com.ufgov.zc.client.zc.flowconsole.events.NodeMouseEvent;
import com.ufgov.zc.client.zc.flowconsole.parts.Button;
import com.ufgov.zc.client.zc.flowconsole.parts.Node;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;
import com.ufgov.zc.common.zc.publish.IZcEbEntrustServiceDelegate;

public class MoudelFlowConsoleCanvas extends FlowConsoleCanvas {

  /**
   * 
   */
  private static final long serialVersionUID = 8448663483751019247L;

  private MoudelFlowConsoleCanvas self = this;

  private IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,
    "zcEbBaseServiceDelegate");

  private IZcEbEntrustServiceDelegate zcEbEntrustServiceDelegate = (IZcEbEntrustServiceDelegate) ServiceFactory.create(
    IZcEbEntrustServiceDelegate.class, "zcEbEntrustServiceDelegate");

  private ActionListener defaultAction = new ActionListener() {

    public void actionPerformed(ActionEvent e) {
      //String command = e.getActionCommand();

      if (e instanceof NodeMouseEvent) {
      } else if (e instanceof ButtonMouseEvent) {
        Button bt = ((ButtonMouseEvent) e).getButton();
        if ("list".equals(bt.getId())) {
          if (bt.getParam() != null) {
            String listPanelClassName = getListPanelClassName(bt.getParam());
            if (listPanelClassName != null) {
              showList(listPanelClassName);
            } else {
              JOptionPane.showMessageDialog(self, "获取部件【" + bt.getParam() + "】的列表页面失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
          }
        } else if ("add".equals(bt.getId())) {
          INodeBusiness bus = bt.getParentNode().getNodeBusiness();
          bus.showAddWindow();
        } else if ("view".equals(bt.getId())) {
          doView(bt);
        }
      }
    }
  };

  public MoudelFlowConsoleCanvas() {
//    super.loadConfig("flow_config.xml");
    super.loadConfig("public_flow_config.xml");
  }

  public MoudelFlowConsoleCanvas(String configXml) {
    super.loadConfig(configXml);
    initNodesFlags();
  }

  private void initNodesFlags() {
    Map para = new HashMap();
    para.put("svUserID", requestMeta.getSvUserID());
    para.put("svNd", requestMeta.getSvNd() + "");
    List list = zcEbBaseServiceDelegate.queryDataForList("consoleChart.getCompoCurrentTaskByUserId", para, this.requestMeta);

    if (list == null || list.size() == 0) {
      return;
    }

    List<String> todo = new ArrayList<String>();
    for (Iterator iterator = list.iterator(); iterator.hasNext();) {
      Map record = (Map) iterator.next();
      Node node = nodeMap.get(record.get("COMPO_ID"));
      if (node != null) {
        todo.add(record.get("COMPO_ID").toString());
      }
    }
    if (todo.size() == 0) {
      return;
    }

    todo = zcEbEntrustServiceDelegate.getTodoListByUser(todo, requestMeta.getSvUserID(), requestMeta);
    if (todo == null) {
      return;
    }

    for (int i = 0; i < todo.size(); i++) {
      Node node = nodeMap.get(todo.get(i));
      if (node != null) {
        node.setFlag(true);
      }
    }
  }

  public void doView(Button bt) {
    final String compoId = bt.getParentNode().getId();
    String listPanelClassName = getListPanelClassName(compoId);
    final Component compo = UfidaUtil.getInstanceByStr(listPanelClassName);
    compo.addPropertyChangeListener("initfinish", new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        AbstractDataDisplay display = ((AbstractEditListBill) compo).getTopDataDisplay();
        JTabbedPane tabp = display.getTabbedPane();
        Component[] comps = tabp.getComponents();
        for (int i = 0; i < comps.length; i++) {
          if (comps[i].getName() != null && comps[i].getName().lastIndexOf("待审") > -1) {
            tabp.setSelectedIndex(i);
            break;
          }
        }
      }
    });

    JFrame frame = new JFrame("待审核列表");
    frame.setSize(UIConstants.REPORT_VIEW_DIALOG_WIDTH, UIConstants.REPORT_VIEW_DIALOG_HEIGHT);
    frame.setLocationRelativeTo(null);
    frame.getContentPane().add(compo);
    frame.setVisible(true);
  }

  private void showList(String param) {
    JFrame frame = new JFrame("列表页面");

    frame.setSize(UIConstants.REPORT_VIEW_DIALOG_WIDTH, UIConstants.REPORT_VIEW_DIALOG_HEIGHT);

    frame.setLocationRelativeTo(null);
    try {
      frame.getContentPane().add((Component) StringToModel.stringToInstance(param));
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    frame.setVisible(true);
  }

  public String getListPanelClassName(String compoId) {
    List list = zcEbBaseServiceDelegate.queryDataForList("asMenuCompo.getAsMenuCompoByCompoId", compoId, this.requestMeta);
    if (list != null && list.size() > 0) {
      Map asMenus = (Map) list.get(0);
      String url = (String) asMenus.get("PAGE_URL");
      String[] urlarr = url.split("=");
      if (urlarr.length > 1) {
        return urlarr[1];
      }
    }
    return null;
  }

  protected ActionListener getNodeAction() {
    return defaultAction;
  }

  protected ActionListener getNodeButtonAction() {
    return defaultAction;
  }

  protected ActionListener getShortcutAction() {
    return defaultAction;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          UIManager.setLookAndFeel(new BlueLookAndFeel());
        } catch (Exception e) {
          e.printStackTrace();
        }
        MoudelFlowConsoleCanvas bill = new MoudelFlowConsoleCanvas();
        JFrame frame = new JFrame("frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(bill);
        frame.setVisible(true);
      }
    });
  }

  @Override
  public List<String> getEnableElements() {
    List ids = zcEbBaseServiceDelegate.queryDataForList("asMenuCompo.getCompoIdsByUserInfo", this.requestMeta, this.requestMeta);

    return ids;
  }

}
