package com.ufgov.zc.client.zc.ztb.model;import java.io.File;public class FileToUploadItem {  private String fileFullPath;  private String fileName;  private String fileMD5;  public String getFileFullPath() {    return fileFullPath;  }  public void setFileFullPath(String fileFullPath) {    this.fileFullPath = fileFullPath;  }  public String getFileName() {    if (fileName == null && fileFullPath != null) {      return (new File(fileFullPath)).getAbsolutePath();    }    return fileName;  }  public void setFileName(String fileName) {    this.fileName = fileName;  }  public String getFileMD5() {    return fileMD5;  }  public void setFileMD5(String fileMD5) {    this.fileMD5 = fileMD5;  }}