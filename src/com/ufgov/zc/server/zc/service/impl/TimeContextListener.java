/**
 * 
 */
package com.ufgov.zc.server.zc.service.impl;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 自动启动的任务，通过web。xml启动
 * @author Administrator
 *
 */
public class TimeContextListener implements ServletContextListener{

  private Timer timer = null;
  
  public void contextDestroyed(ServletContextEvent event) {
    timer.cancel();
  }
 
  public void contextInitialized(ServletContextEvent event) {

    timer = new Timer(true);
    // 设置任务计划，启动和间隔时间单位毫秒
    timer.schedule(new ZcXieYieJingJiaTask(event), 1000 * 2, 1000 * 60*60);
    
  }

}
