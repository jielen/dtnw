package com.ufgov.zc.client.zc.freemarker;

public class PackReqRow {
  private String packCode;
  private String name;
  private String desc;
  private String num;
  private String price;
  private String attachFileName;
  private String attachFileBlobId;
  private String hasAttach="0";
  public String getAttachFileUrl(){
    if(attachFileBlobId==null || attachFileBlobId.equals("")){
      return null;
    }
    
    return "./"+attachFileName;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDesc() {
    return desc;
  }
  public void setDesc(String desc) {
    this.desc = desc;
  }
  public String getNum() {
    return num;
  }
  public void setNum(String num) {
    this.num = num;
  }
  public String getAttachFileName() {
    return attachFileName;
  }
  public void setAttachFileName(String attachFileName) {
    this.attachFileName = attachFileName;
  }
  public String getAttachFileBlobId() {
    return attachFileBlobId;
  }
  public void setAttachFileBlobId(String attachFileBlobId) {
    this.attachFileBlobId = attachFileBlobId;
  }
  public String getPackCode() {
    return packCode;
  }
  public void setPackCode(String packCode) {
    this.packCode = packCode;
  }
  public String getHasAttach() {
    if(getAttachFileBlobId()!=null && getAttachFileBlobId()!=null){
      hasAttach="1";
    }else{
      hasAttach="0";
    }
    return hasAttach;
  }
  public void setHasAttach(String hasAttach) {
    this.hasAttach = hasAttach;
  }
  public String getPrice() {
    return price;
  }
  public void setPrice(String price) {
    this.price = price;
  }
  }
