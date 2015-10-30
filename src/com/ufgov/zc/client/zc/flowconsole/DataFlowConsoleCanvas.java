package com.ufgov.zc.client.zc.flowconsole;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.component.SimpleRowFilter;
import com.ufgov.zc.client.component.ui.AbstractDataDisplay;
import com.ufgov.zc.client.component.ui.AbstractEditListBill;
import com.ufgov.zc.client.component.ui.TableDisplay;
import com.ufgov.zc.client.zc.flowconsole.business.INodeBusiness;
import com.ufgov.zc.client.zc.flowconsole.events.ButtonMouseEvent;
import com.ufgov.zc.client.zc.flowconsole.events.NodeMouseEvent;
import com.ufgov.zc.client.zc.flowconsole.parts.Button;
import com.ufgov.zc.client.zc.flowconsole.parts.Node;
import com.ufgov.zc.common.zc.publish.IZcEbBaseServiceDelegate;

public class DataFlowConsoleCanvas extends FlowConsoleCanvas {

  /**
   * 
   */
  private static final long serialVersionUID = 2324031372452242448L;

  private final DataFlowConsoleCanvas self = this;

  private String businessCompoId = null;

  private String businessId = null;

  private String zcMakeCode = null;

  private String projectCode = null;

  private String entrustCode = null;

  private final IZcEbBaseServiceDelegate zcEbBaseServiceDelegate = (IZcEbBaseServiceDelegate) ServiceFactory.create(IZcEbBaseServiceDelegate.class,
    "zcEbBaseServiceDelegate");

  private final ActionListener defaultAction = new ActionListener() {

    public void actionPerformed(ActionEvent e) {
      //String command = e.getActionCommand();
      if (e instanceof NodeMouseEvent) {

      } else if (e instanceof ButtonMouseEvent) {
        Button bt = ((ButtonMouseEvent) e).getButton();
        if ("list".equals(bt.getId())) {
          doList(bt);
        } else if ("add".equals(bt.getId())) {
          doAdd(bt);
        } else if ("view".equals(bt.getId())) {
          doView(bt);
        }
      }
    }
  };

  public void doList(Button bt) {
    if (bt.getParam() != null) {
      String listPanelClassName = getListPanelClassName(bt.getParam());
      if (listPanelClassName != null) {
        Component compo = UfidaUtil.getInstanceByStr(listPanelClassName);
        showList(compo, null);
      } else {
        JOptionPane.showMessageDialog(self, "获取部件【" + bt.getParam() + "】的列表页面失败！", "错误", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  public void doAdd(Button bt) {
    INodeBusiness bus = bt.getParentNode().getNodeBusiness();
    bus.showAddWindow();
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
          if (comps[i].getName() != null && comps[i].getName().endsWith("全部")) {

            String busId = null;
            if (isBeforMakeProject(compoId)) {
              busId = zcMakeCode;
            } else {
              busId = projectCode;
            }

            TableDisplay tt = (TableDisplay) comps[i];
            tabp.setSelectedIndex(i);
            tt.getSearchConditionTextField().setText(busId);
            tt.getTableSorter().setRowFilter(new SimpleRowFilter(busId));
            tt.revalidate();
            tt.repaint();
            break;
          }
        }
      }
    });

    JFrame frame = new JFrame(getFrameTile());
    frame.setSize(800, 600);
    frame.setLocationRelativeTo(null);
    frame.getContentPane().add(compo);
    frame.setVisible(true);
  }

  public DataFlowConsoleCanvas(String businessId, String compoId) {
    this.businessCompoId = compoId;
    this.businessId = businessId;
    String dataXml = null;

    //    String cgfs = null;
    //    if (isBeforMakeProject(compoId)) {
    //      cgfs = (String) zcEbBaseServiceDelegate.queryObject("consoleChart.getCgfsByMakeCode", businessId, this.requestMeta);
    //    } else {
    //      cgfs = (String) zcEbBaseServiceDelegate.queryObject("consoleChart.getCgfsByProjCode", businessId, this.requestMeta);
    //    }
    //    String dataXml = null;
    //    if (ZcSettingConstants.PITEM_OPIWAY_GKZB.equals(cgfs)) {
    //      dataXml = "public_flow_data_config.xml";
    //    } else if (ZcSettingConstants.PITEM_OPIWAY_JZXTP.equals(cgfs)) {
    //      dataXml = "talks_flow_data_config.xml";
    //    } else if (ZcSettingConstants.PITEM_OPIWAY_YQZB.equals(cgfs)) {
    //      dataXml = "invite_data_config.xml";
    //    } else if (ZcSettingConstants.PITEM_OPIWAY_XJ.equals(cgfs)) {
    //      dataXml = "ask_flow_data_config.xml";
    //    } else if (ZcSettingConstants.PITEM_OPIWAY_DYLY.equals(cgfs)) {
    //      dataXml = "single_data_config.xml";
    //    } else if (ZcSettingConstants.PITEM_OPIWAY_XYGH.equals(cgfs)) {
    //      dataXml = "second_talks_data_config.xml";
    //    } else {
    //      dataXml = "public_flow_data_config.xml";
    //    }
    dataXml = "public_flow_config.xml";

    super.loadConfig(dataXml);
    initNodesStatus();

  }

  private void initNodesStatus() {
    Collection<Node> vals = nodeMap.values();

    if (isBeforMakeProject(this.businessCompoId)) {
      zcMakeCode = this.businessId;
      projectCode = (String) zcEbBaseServiceDelegate.queryObject("consoleChart.getProCodeByMakeCode", zcMakeCode, this.requestMeta);
      entrustCode = (String) zcEbBaseServiceDelegate.queryObject("consoleChart.getEntrustCodeByMakeCode", zcMakeCode, this.requestMeta);

    } else {
      projectCode = this.businessId;
      zcMakeCode = (String) zcEbBaseServiceDelegate.queryObject("consoleChart.getMakeCodeByProCode", projectCode, this.requestMeta);
      entrustCode = (String) zcEbBaseServiceDelegate.queryObject("consoleChart.getEntrustCodeeByProCode", projectCode, this.requestMeta);
    }

    for (Iterator iterator = vals.iterator(); iterator.hasNext();) {
      Node node = (Node) iterator.next();
      INodeBusiness bus = node.getNodeBusiness();
      String id = null;
      if (isBeforMakeProject(node.getId())) {
        id = zcMakeCode;
      } else {
        id = projectCode;
      }
      if (id != null && bus.isArrieCurrentNode(id, this.requestMeta)) {
        node.setStatus(Node.GENERAL_STATUS);
      } else {
        node.setStatus(Node.DISENABLE_STATUS);
      }
    }
  }

  private boolean isBeforMakeProject(String comp) {

    if ("ZC_EB_ENTRUST".equals(comp) || "ZC_EB_AUDIT_SHEET".equals(comp) || "ZC_EB_AUDIT_SHEET".equals(comp) || "ZC_EB_PROTOCOL".equals(comp)
      || "ZC_PRO_ARGUE".equals(comp) || "ZC_EB_REQUIREMENT".equals(comp) || "ZC_EB_BULLETIN_OPN".equals(comp) || "ZC_EB_PROJ_CHG".equals(comp)) {
      return true;

    }
    return false;
  }

  private void showList(Component compo, WindowListener lis) {
    JFrame frame = new JFrame("列表页面");

    frame.setSize(800, 600);
    frame.setLocationRelativeTo(null);
    frame.getContentPane().add(compo);

    if (lis != null) {
      frame.addWindowListener(lis);
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

  @Override
  protected ActionListener getNodeAction() {
    return defaultAction;
  }

  @Override
  protected ActionListener getNodeButtonAction() {
    return defaultAction;
  }

  @Override
  protected ActionListener getShortcutAction() {
    return defaultAction;
  }

  public void showWindow() {
    JFrame frame = new JFrame(getFrameTile());
    frame.setSize(800, 600);
    frame.setLocationRelativeTo(null);
    frame.getContentPane().add(self);
    frame.setVisible(true);
  }

  @Override
  public List<String> getEnableElements() {
    List ids = zcEbBaseServiceDelegate.queryDataForList("asMenuCompo.getCompoIdsByUserInfo", this.requestMeta, this.requestMeta);

    return ids;
  }

  private String getFrameTile() {

    return "跟踪相关数据:任务单编号：" + entrustCode + ",项目编号:" + (projectCode == null ? "未立项" : projectCode);
  }

}
