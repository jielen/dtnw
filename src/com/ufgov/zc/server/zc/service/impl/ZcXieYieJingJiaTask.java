/**
 * 
 */
package com.ufgov.zc.server.zc.service.impl;

import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.server.zc.service.IZcPProMakeService;

/**
 * 协议竞价自动成交定时类
 * @author Administrator
 *
 */
public class ZcXieYieJingJiaTask extends TimerTask{

  private static Logger log=Logger.getLogger(ZcXieYieJingJiaTask.class);
  protected ServletContextEvent event;
  protected WebApplicationContext wac;
  private IZcPProMakeService zcPProMakeService;
  private RequestMeta meta;
  
  public ZcXieYieJingJiaTask(ServletContextEvent event){
    this.event = event;
    ServletContext application = event.getServletContext();
    wac = WebApplicationContextUtils.getWebApplicationContext(application);
    zcPProMakeService = (IZcPProMakeService) wac.getBean("zcPProMakeService");
    meta=new RequestMeta();
    meta.setToken(Guid.genID()); 
  }
  public void run() {
    try {
      zcPProMakeService.autoChengJiaoXieYi(meta);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      log.error("执行自动成交出错:"+e.getMessage(), e);
    }   
  }

}
