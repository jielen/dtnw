package com.ufgov.zc.client.zc.bulletin;

import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.plaf.BlueLookAndFeel;
import com.ufgov.zc.client.zc.flowconsole.DataFlowConsoleCanvas;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.zc.ZcEbBulletinConstants;
import com.ufgov.zc.common.zc.model.ZcEbBulletin;

public class ZcEbBulletinDlyListPanel extends AbstractZcEbBulletinBaseListPanel {

  /**
   * 
   */
  private static final long serialVersionUID = -2121945096654849453L;

  private static final Logger logger = Logger.getLogger(ZcEbBulletinDlyListPanel.class);

  private ZcEbBulletinDlyListPanel self = this;

  public ZcEbBulletinDlyListPanel() {

    super();

    this.setTitle(ZcEbBulletinConstants.TITLE_ZC_EB_BULLETIN_DLY);

    this.setCompoID(ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_DLY);

    //    this.setCompoID(ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_BID);

    this.setBulletinTab(ZcSettingConstants.TAB_ID_ZC_EB_BULLETIN);

    this.setBulletinCondition(ZcSettingConstants.CONDITION_ID_ZC_EB_BULLETIN);

    init();
  }

  protected void openDialog(Window parentWindow, List viewList, int row, String tabStatus) {

    new ZcEbBulletinDlyEditDialog(self, viewList, row, topDataDisplay.getActiveTableDisplay().getStatus());

  }

  protected void doAdd() {

    new ZcEbBulletinDlyEditDialog(self, new ArrayList(), this.topDataDisplay.getActiveTableDisplay().getTable().getRowCount(), topDataDisplay
      .getActiveTableDisplay().getStatus());

  }

  public String getBulletinType() {

    // TODO Auto-generated method stub

    return ZcEbBulletinConstants.TYPE_BULLETIN_DLY;

  }

  public void doTraceDataButton() {

    List beanList = getCheckedList();

    if (beanList.size() == 0) {

      JOptionPane.showMessageDialog(this, "请选择一条要进行跟踪的数据！", "错误", JOptionPane.ERROR_MESSAGE);

      return;

    }

    ZcEbBulletin sh = (ZcEbBulletin) beanList.get(0);

    DataFlowConsoleCanvas dfc = new DataFlowConsoleCanvas(sh.getProjCode(), ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_DLY);

    dfc.showWindow();

  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

    SwingUtilities.invokeLater(new Runnable() {

      public void run() {

        try {

          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

          UIManager.setLookAndFeel(new BlueLookAndFeel());

        } catch (Exception e) {

          e.printStackTrace();

        }

        ZcEbBulletinDlyListPanel bill = new ZcEbBulletinDlyListPanel();

        JFrame frame = new JFrame("frame");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(800, 600);

        frame.setLocationRelativeTo(null);

        frame.getContentPane().add(bill);

        frame.setVisible(true);

      }

    });

  }

}
