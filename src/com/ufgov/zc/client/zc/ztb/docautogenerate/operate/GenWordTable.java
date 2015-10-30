package com.ufgov.zc.client.zc.ztb.docautogenerate.operate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.ufgov.zc.client.zc.ztb.docautogenerate.model.FormlaWorldDataMapGenaral;

import freemarker.template.Template;

public class GenWordTable {
  public void startGenWordTable(String projectId, String packId, String fileName, String localPath) throws Exception {
    FreemarkerTemplateConfiguration configuration = new FreemarkerTemplateConfiguration("wordTableTemplate.ftl");
    Template template = configuration.getTemplate();
    FormlaWorldDataMapGenaral wordTableModel = new FormlaWorldDataMapGenaral(projectId, packId);
    String genFileDirectory = localPath;
    mkDirs(genFileDirectory);
    File file = new File(genFileDirectory + "//" + fileName);
    OutputStream out = new FileOutputStream(file);
    Writer writer = new BufferedWriter(new OutputStreamWriter(out, "GBK"));
    template.process(wordTableModel.genScoreIteWordTableMap(), writer);
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
    GenWordTable genWordTable = new GenWordTable();
    try {
      genWordTable.startGenWordTable("180ZF2012-aa", "1449", "abc.doc", "e://wordTable");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
