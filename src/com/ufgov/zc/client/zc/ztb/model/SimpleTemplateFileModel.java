package com.ufgov.zc.client.zc.ztb.model;import java.util.ArrayList;import java.util.List;public class SimpleTemplateFileModel {  /**   * 模板文件名称   */  private String fileName = null;  /**   * 模板文件的中文名称   */  private String fileChName = "";  private String fileID = null;  /**   * 模板文件类型，例如：DOC,XML,XLS,CONFIG等等   */  private String fileType = null;  /**   * 文件是否可以编辑   */  private boolean editable = true;  /**   * 文件扩展名   */  private String fileExtension = null;  private List<SimpleTemplateFileModel> subFileList = new ArrayList<SimpleTemplateFileModel>();  public String getFileName() {    return fileName;  }  public void setFileName(String fileName) {    this.fileName = fileName;  }  public String getFileChName() {    return fileChName;  }  public void setFileChName(String fileChName) {    this.fileChName = fileChName;  }  public String getFileID() {    return fileID;  }  public void setFileID(String fileID) {    this.fileID = fileID;  }  public String getFileType() {    if (fileType != null) {      return fileType.toUpperCase();    } else {      return fileType;    }  }  public void setFileType(String fileType) {    this.fileType = fileType;  }  public boolean isEditable() {    return editable;  }  public void setEditable(boolean editable) {    this.editable = editable;  }  public String getFileExtension() {    if (this.fileExtension != null) {      return fileExtension.toLowerCase();    } else {      return this.fileExtension;    }  }  public void setFileExtension(String fileExtension) {    this.fileExtension = fileExtension;  }  public List<SimpleTemplateFileModel> getSubFileList() {    return subFileList;  }  public void setSubFileList(List<SimpleTemplateFileModel> subFileList) {    this.subFileList = subFileList;  }}