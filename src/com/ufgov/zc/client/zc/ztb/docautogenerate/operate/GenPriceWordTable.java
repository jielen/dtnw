package com.ufgov.zc.client.zc.ztb.docautogenerate.operate;

import com.ufgov.zc.client.zc.ztb.docautogenerate.model.PriceWordTableModel;
import freemarker.template.Template;

import java.io.*;

public class GenPriceWordTable {
  public void startGenPriceWordTable(String tableType, String tableName, String tableXmlPath, String fileName, String localPath) throws Exception {
    FreemarkerTemplateConfiguration configuration = new FreemarkerTemplateConfiguration("wordTableTemplate.ftl");
    Template template = configuration.getTemplate();
    PriceWordTableModel wordTableModel = new PriceWordTableModel(tableType, tableName, tableXmlPath);
    wordTableModel.genWordTableModel();
    String genFileDirectory = localPath;
    mkDirs(genFileDirectory);
    File file = new File(genFileDirectory + "//" + fileName);
    OutputStream out = new FileOutputStream(file);
    Writer writer = new BufferedWriter(new OutputStreamWriter(out,"GBK"));
    template.process(wordTableModel.getRoot(), writer);
    writer.flush();
    writer.close();
  }

  public void mkDirs(String filePath) {
    File file = new File(filePath);
    if (!file.exists()) {
      file.mkdirs();
    }
  }

  public static void main(String[] args) {
    GenPriceWordTable genWordTable = new GenPriceWordTable();
    try {
      String xmlPath = "E:\\workspace\\SHX_ST\\resource\\impfiles\\XCZX2011-236\\3115\\投标文件\\第3部分  唱标一览表\\开标一览表.config";
      genWordTable.startGenPriceWordTable("", "开标一览表", xmlPath, "abc.doc", "e://wordTableTest");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
