package com.ufgov.zc.client.zc.flowconsole.tip;

import java.awt.Container;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.Popup;
import javax.swing.PopupFactory;

public class TipThread extends Thread {
  private int x;

  private int y;

  private String tipText;

  private JComponent container;

  private JToolTip tip;

  private Popup tipWindow;

  public TipThread() {

  }

  public TipThread(int x, int y, String tipText, JComponent container) {
    this.x = x;
    this.y = y;
    this.tipText = tipText;
    this.container = container;
    
    view();
  }

  public void run() {
	  
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    hide();
  }

  public void view() {
	  PopupFactory popupFactory = PopupFactory.getSharedInstance();
	    tip = container.createToolTip();
	    tip.setTipText(tipText);
	    tip.setFont(new Font(null, Font.PLAIN, 12));

	    tipWindow = popupFactory.getPopup(container, tip, x, y);
	    tipWindow.show();
  }

  public void hide() {
    Container pa = tip.getParent() == null ? tip : tip.getParent();
    //    while (pa.getParent() != null) {
    //      pa = pa.getParent();
    //    }

    //    pa.setVisible(false);
    //    tip.setVisible(false);
    tipWindow.hide();
  }
}
