/**
 * 
 */
package com.ufgov.zc.client.zc.auditsheet;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.ufgov.smartclient.plaf.BlueLookAndFeel;

/**
 * 另一种批办单，采购中心内部进行了分组，需要选择执行组和执行人
 * @author Administrator
 *
 */
public class ZcEbAuditSheetListPanelExtends extends ZcEbAuditSheetListPanel {
  public void openEditDialog(ArrayList viewList, Integer row, String status) {

    new ZcEbAuditSheetDialog(this, viewList, row, status);

  }

  public static void main(String[] args) throws Exception {

    SwingUtilities.invokeLater(new Runnable() {

      public void run() {

        try {

          //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

          UIManager.setLookAndFeel(new BlueLookAndFeel());

        } catch (Exception e) {

          e.printStackTrace();

        }

        ZcEbAuditSheetListPanelExtends bill = new ZcEbAuditSheetListPanelExtends();

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
