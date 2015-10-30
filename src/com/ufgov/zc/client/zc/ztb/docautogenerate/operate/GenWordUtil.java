package com.ufgov.zc.client.zc.ztb.docautogenerate.operate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.ufgov.zc.client.zc.ztb.docautogenerate.model.FormulaWordDataModel;
import com.ufgov.zc.client.zc.ztb.docautogenerate.model.IWordDataModel;
import com.ufgov.zc.common.system.Guid;

import freemarker.template.Template;

public class GenWordUtil {
  private IWordDataModel wordDataModel;

  private String templateName;

  private String defaultSavePath = "C:/ufgov/downfile/";

  public GenWordUtil(IWordDataModel wordDataModel, String templateName) {
    this.wordDataModel = wordDataModel;
    this.templateName = templateName;
  }

  public GenWordUtil(IWordDataModel wordDataModel, String templateName, String defaultSavePath) {
    this.defaultSavePath = defaultSavePath;
    this.wordDataModel = wordDataModel;
    this.templateName = templateName;
  }

  public File outWordFilePath() throws Exception {
    FreemarkerTemplateConfiguration configuration = new FreemarkerTemplateConfiguration(templateName);
    Template template = configuration.getTemplate();
    File dirFile = new File(defaultSavePath);
    if (!dirFile.exists()) {
      dirFile.mkdirs();
    }
    File file = null;
    if (((FormulaWordDataModel) wordDataModel).packId != null) {
      file = new File(defaultSavePath + "//" + ((FormulaWordDataModel) wordDataModel).packId + ".doc");

    } else {
      file = new File(defaultSavePath + "//" + Guid.genID() + ".doc");

    }
    OutputStream out = new FileOutputStream(file);
    Writer writer = new BufferedWriter(new OutputStreamWriter(out, "GBK"));
    template.process(wordDataModel.getFillWordMap(), writer);
    writer.flush();
    writer.close();
    return file;
  }
}
