/**
 * 
 */
package com.ufgov.zc.client.zc.freemarker;

import com.ufgov.zc.common.zc.model.ZcEbBulletin;

/**
 * 
 * @author Administrator
 *
 */
public class TemplateToDocumentFactory {
  

  
  static TemplateToDocumentFactory self=new TemplateToDocumentFactory();
  
  public static TemplateToDocumentFactory getInstance(){
    return self;
  }
  private TemplateToDocumentFactory(){
    
  }
  public ITemplateToDocumentHandler getHandler(String type){
    
    if(ZcEbBulletin.ZHAOBIAO_XJ.equals(type)){
      return new XjZhaobiaoBulletinHandler();
    }else if(ZcEbBulletin.ZHAOBIAO_GKZB.equals(type) 
      ||ZcEbBulletin.ZHAOBIAO_DYLY.equals(type)  
      ||ZcEbBulletin.ZHAOBIAO_JZXTP.equals(type)  
      ||ZcEbBulletin.ZHAOBIAO_YQZB.equals(type)  
      ||ZcEbBulletin.ZHAOBIAO_QT.equals(type) ){
      return new ZhaobiaoBulletinHandler();
    }else if(ZcEbBulletin.ZHONGBIAO_XJ.equals(type)){
      return new XjZhongbiaoBulletinHandler();
    }else if(ZcEbBulletin.ZHONGBIAO_GKZB.equals(type) 
      ||ZcEbBulletin.ZHONGBIAO_DYLY.equals(type)  
      ||ZcEbBulletin.ZHONGBIAO_JZXTP.equals(type)  
      ||ZcEbBulletin.ZHONGBIAO_YQZB.equals(type)  
      ||ZcEbBulletin.ZHONGBIAO_QT.equals(type) ){
      return new ZhongbiaoBulletinHandler();
    }else if(ZcEbBulletin.ZHONGBIAO_NOTICE.equals(type)){
      return new ZhongbiaoNoticeHandler();
    }
    return null;
  }
}
