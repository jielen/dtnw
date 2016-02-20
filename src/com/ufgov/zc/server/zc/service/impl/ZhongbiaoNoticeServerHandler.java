package com.ufgov.zc.server.zc.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ufgov.zc.common.system.Guid;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.model.AsFile;
import com.ufgov.zc.common.zc.model.ZcEbNotice;
import com.ufgov.zc.server.system.dao.IAsFileDao;
import com.ufgov.zc.server.zc.ZcSUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ZhongbiaoNoticeServerHandler {
  
  private static Logger logger=Logger.getLogger(ZhongbiaoNoticeServerHandler.class);
  private IAsFileDao asFileDao;
  private RequestMeta meta;
  public ZhongbiaoNoticeServerHandler(IAsFileDao asFileDao,RequestMeta meta){
    this.asFileDao=asFileDao;
    this.meta=meta;
  }

  public String createNoticeFile(ZcEbNotice notice){
    String fileName=createDocumnet(notice);
    AsFile af=createAsFile(fileName,notice);
    String fileId=saveAsFile(af);
    return fileId;
  }
  private String saveAsFile(AsFile af) {
    asFileDao.insertAsFile(af);
    return af.getFileId();
  }

  private AsFile createAsFile(String fileName, ZcEbNotice notice) {

    AsFile af=new AsFile();
    BufferedInputStream  in=null;
    ByteArrayOutputStream bos=null;
    try{

      File bulletinDocFile = new File(fileName);
      in = new BufferedInputStream(new FileInputStream(fileName)); 
      bos = new ByteArrayOutputStream((int) bulletinDocFile.length());        
      int buf_size = 1024;  
      byte[] buffer = new byte[buf_size];  
      int len = 0;  
      while (-1 != (len = in.read(buffer, 0, buf_size))) {  
          bos.write(buffer, 0, len);  
      }  
      af.setFileContent(bos.toByteArray());
      af.setFileName(fileName);
      af.setFileId(Guid.genID());
      in.close();
      bulletinDocFile.delete();
    } catch (Exception e) {
      // TCJLODO Auto-generated catch block
      e.printStackTrace();
      logger.error("生成文件出错:"+e.getMessage(), e);
    }  finally {
      try {
        if (in != null) {
          in.close();
        }       
      } catch (IOException e) {
      }

      if (bos != null) {
        try {
          bos.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      } 
    }
    return af;
  }

  private String createDocumnet(ZcEbNotice notice) { 
    AsFile asf = asFileDao.getAsFileById("zhongbiao_notice");

    String name = "bulletin_zhongbiao_xunjia";

    asf.setFileName(name + ".doc");
    String modname = name + "_mod.doc";

    //      String modname_new = name + "_mod_new.doc";

    // 先保存模版临时文件 

    boolean isSucceed =createFile(modname, asf.getFileContent());
    File srcFile = new File(modname); 

    // 要填入模本的数据文件
    Map dataMap = new HashMap();

    getNoticeResult(dataMap, notice);

    String bulletinFileName = notice.getProjCode() + "_notice.doc";
 
//    File f = new File(WordFileUtil.getDir());
    File modeFile=new File(modname);
    
    File bulletinDocFile = new File(bulletinFileName);

    Configuration configuration = new Configuration();
    configuration.setDefaultEncoding("utf-8");

    OutputStream out = null;
    Writer writer = null;
    try {
//      configuration.setDirectoryForTemplateLoading(modeFile.getParentFile());
      Template template = configuration.getTemplate(modname);
      out = new FileOutputStream(bulletinDocFile);
      writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
      template.process(dataMap, writer);
      writer.flush();
    } catch (Exception e) {
      // TCJLODO Auto-generated catch block
      e.printStackTrace();
      logger.error("生成文件出错:"+e.getMessage(), e);
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
        if (out != null) {
          out.close();
        }
        modeFile.delete();
      } catch (IOException e) {
      }
    }
//    af.setFileContent(bulletinDocFile.g)
    return bulletinFileName;
  }  
  
  private void getNoticeResult(Map dataMap, ZcEbNotice notice) {
    // TCJLODO Auto-generated method stub
    
    dataMap.put("supplier", ZcSUtil.freeMarkFillWordChar(notice.getProviderName()==null?"":notice.getProviderName()));
    dataMap.put("coname", ZcSUtil.freeMarkFillWordChar(notice.getCoName()==null?"":notice.getCoName()));
    dataMap.put("projname", ZcSUtil.freeMarkFillWordChar(notice.getProjName()==null?"":notice.getProjName()));
//    String purType=AsValDataCache.getName("ZC_VS_PITEM_OPIWAY", notice.getNoticeType());
//    dataMap.put("purtype", ZcSUtil.freeMarkFillWordChar(purType==null?"":purType));
    dataMap.put("purtype", ZcSUtil.freeMarkFillWordChar("1"));
    dataMap.put("projcode", ZcSUtil.freeMarkFillWordChar(notice.getProjCode()==null?"":notice.getProjCode()));
    dataMap.put("packcode", ZcSUtil.freeMarkFillWordChar(notice.getPackName()==null?"":notice.getPackName()));
    dataMap.put("bidsum", ZcSUtil.freeMarkFillWordChar(notice.getBidSum()==null?"":""+notice.getBidSum().doubleValue()));
    Date d =  Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    dataMap.put("date", df.format(d));
    
    
  }

  private boolean createFile(String filename, byte[] content) {
    boolean isSucceed = true; 
    FileOutputStream fos = null;
    OutputStreamWriter ow=null;
    try {
      // 先删除文件
      deleteFile(filename);
      // 创建文件
      fos = new FileOutputStream(filename);
      ow=new OutputStreamWriter(fos, "UTF-8");
      String cc=new String(content,"UTF-8");
      ow.write(cc);
      ow.flush();
    } catch (Exception ex) {
      isSucceed = false;
      logger.error("载入文件失败"+ex.getMessage(), ex); 
    } finally {
      if (ow != null) {
        try {
          ow.close();
        } catch (IOException e) {
          logger.error(e.getMessage(), e);
        }
      }
        if (fos != null) {
          try {
            fos.close();
          } catch (IOException e) {
            logger.error(e.getMessage(), e);
          }
      }
    }
    return isSucceed;
  }
  /**    
   * 删除单个文件    
   * @param   fileName    被删除文件的文件名    
   * @return 单个文件删除成功返回true,否则返回false    
   */
  private boolean deleteFile(String fileName) {
    File file = new File(fileName);
    if (file.isFile() && file.exists()) {
      file.delete();
      return true;
    } else {
      return false;
    }
  }
}
