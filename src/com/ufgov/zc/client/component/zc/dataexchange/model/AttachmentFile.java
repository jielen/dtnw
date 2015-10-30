package com.ufgov.zc.client.component.zc.dataexchange.model;/** * 附件文件对象 * @author LEO * */public class AttachmentFile {  //文件ID  private String fileID;  //文件名称  private String fileName;  //所属记录对应的唯一标识  private String belongToIdentify;  //文件  private String memo;  //解压后文件所在的存储路径，相对于压缩包目录  private String sourceFilePath;  //文件将要写入的存储位置  private String targetFilePath;  //考虑到有些附件本身是以文件夹下带文件的形式，这里添加一个文件类型标志，如果是文件则是"file",如果是目录则值为"dir"；  private String fileType = "file";  private boolean saveAsFileTable;  public String getFileID() {    return fileID;  }  public void setFileID(String fileID) {    this.fileID = fileID;  }  public String getFileName() {    return fileName;  }  public void setFileName(String fileName) {    this.fileName = fileName;  }  public String getBelongToIdentify() {    return belongToIdentify;  }  public void setBelongToIdentify(String belongToIdentify) {    this.belongToIdentify = belongToIdentify;  }  public String getMemo() {    return memo;  }  public void setMemo(String memo) {    this.memo = memo;  }  public String getSourceFilePath() {    return sourceFilePath;  }  public void setSourceFilePath(String sourceFilePath) {    this.sourceFilePath = sourceFilePath;  }  public String getTargetFilePath() {    return targetFilePath;  }  public void setTargetFilePath(String targetFilePath) {    this.targetFilePath = targetFilePath;  }  public String getFileType() {    return fileType;  }  public void setFileType(String fileType) {    this.fileType = fileType;  }  public boolean isSaveAsFileTable() {    return saveAsFileTable;  }  public void setSaveAsFileTable(boolean saveAsFileTable) {    this.saveAsFileTable = saveAsFileTable;  }}