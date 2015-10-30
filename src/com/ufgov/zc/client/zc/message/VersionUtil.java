package com.ufgov.zc.client.zc.message;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.common.zc.publish.IMessageDelegate;

public class VersionUtil extends TimerTask {

  private TipWindow tw;//提示框 

  private Timer timer;
  private List<String> list = null;

  public VersionUtil(Timer timer) {
    this.timer = timer;
  }

  public void init() {
    try {
      tw = new TipWindow(300, 220, list);
    } catch (Exception e) {
      //      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    if (WorkEnv.getInstance() == null || WorkEnv.getInstance().getCurrUser() == null || WorkEnv.getInstance().getCurrUserId() == null) {
      timer.cancel();
      MessageTimer.isRun = false;
      return;
    }
    IMessageDelegate delegate = (IMessageDelegate) ServiceFactory.create(IMessageDelegate.class, "zcEbMessageDelegate");
    try {
      if (!delegate.isCgzxGroup(WorkEnv.getInstance().getCurrUserId(), WorkEnv.getInstance().getRequestMeta())) {
        timer.cancel();
        MessageTimer.isRun = false;
        return;
      }
      list = delegate.getMessage(WorkEnv.getInstance().getCurrUserId(), WorkEnv.getInstance().getRequestMeta());
      if(list == null || list.size() == 0){
        return;
      }
    } catch (Exception e) {
      return;
    }

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        init();
        tw.setAlwaysOnTop(true);
        tw.setUndecorated(true);
        tw.setResizable(false);
        tw.setVisible(true);
        tw.run();
      }
    });
    try {
      Thread.sleep(MessageTimer.HOULD_SPEED);
    } catch (InterruptedException e) {
    }
    tw.close();

  }

}
