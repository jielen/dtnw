package com.ufgov.zc.client.zc.ztb.model;import java.io.InputStream;import java.util.Date;public class BidFileBean {  private String providerId;  private String projectId;  private String packId;  private String fileId;  private String signupId;  private String signupPackId;  private String ifExport;  private String ifImport;  private InputStream fileContent;  private String fileName;  private Date submitBidDate;  public String getFileName() {    return fileName;  }  public void setFileName(String fileName) {    this.fileName = fileName;  }  public InputStream getFileContent() {    return fileContent;  }  public void setFileContent(InputStream fileContent) {    this.fileContent = fileContent;  }  public String getIfExport() {    return ifExport;  }  public void setIfExport(String ifExport) {    this.ifExport = ifExport;  }  public String getIfImport() {    return ifImport;  }  public void setIfImport(String ifImport) {    this.ifImport = ifImport;  }  public String getProviderId() {    return providerId;  }  public void setProviderId(String providerId) {    this.providerId = providerId;  }  public String getProjectId() {    return projectId;  }  public void setProjectId(String projectId) {    this.projectId = projectId;  }  public String getPackId() {    return packId;  }  public void setPackId(String packId) {    this.packId = packId;  }  public String getFileId() {    return fileId;  }  public void setFileId(String fileId) {    this.fileId = fileId;  }  public String getSignupId() {    return signupId;  }  public void setSignupId(String signupId) {    this.signupId = signupId;  }  public String getSignupPackId() {    return signupPackId;  }  public void setSignupPackId(String signupPackId) {    this.signupPackId = signupPackId;  }  public Date getSubmitBidDate() {    return submitBidDate;  }  public void setSubmitBidDate(Date submitBidDate) {    this.submitBidDate = submitBidDate;  }}