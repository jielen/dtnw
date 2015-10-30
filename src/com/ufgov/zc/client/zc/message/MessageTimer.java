package com.ufgov.zc.client.zc.message;

import java.util.Timer;

public class MessageTimer implements Runnable {
  protected static final long REFRESH_SPEED = 600000;

  protected static final long START_TIME = 10;

  protected static final long CLOSE_SPEED = 20;

  protected static final long HOULD_SPEED = 20000;

  protected static final long SOUND_TIME = 3000;

  protected static boolean isRun = false;

  public void run() {
    if (!isRun) {
      isRun = true;
//      if (WorkEnv.getInstance().getCurrUserId() != null && !"".equals(WorkEnv.getInstance().getCurrUserId())) {
//        IMessageDelegate delegate = (IMessageDelegate) ServiceFactory.create(IMessageDelegate.class, "zcEbMessageDelegate");
//        delegate.logout(WorkEnv.getInstance().getCurrUserId(), WorkEnv.getInstance().getRequestMeta());
//      }
      Timer timer = new Timer();
      timer.schedule(new VersionUtil(timer), START_TIME, REFRESH_SPEED);
    }
  }

  public static void main(String str[]) {
    new Thread(new MessageTimer()).start();
  }
}
