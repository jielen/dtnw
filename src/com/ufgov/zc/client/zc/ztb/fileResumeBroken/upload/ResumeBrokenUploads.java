package com.ufgov.zc.client.zc.ztb.fileResumeBroken.upload;import com.ufgov.zc.client.zc.ztb.fileResumeBroken.authentication.Transfer;import java.io.File;import java.io.FileInputStream;import java.io.FileNotFoundException;import java.io.IOException;import java.util.HashMap;import java.util.Map;public class ResumeBrokenUploads {  private Map parameterMap = new HashMap();  private String fileName;  private String filePath;  public String getFilePath() {    return filePath;  }  public void setFilePath(String filePath) {    this.filePath = filePath;  }  public Map getParameterMap() {    return parameterMap;  }  public void setParameterMap(Map parameterMap) {    this.parameterMap = parameterMap;  }  public String getFileName() {    return fileName;  }  public void setFileName(String fileName) {    this.fileName = fileName;  }  public ResumeBrokenUploads(String fileName, String url, String filePath, String toPath) {    setFileName(fileName);    setFilePath(filePath);    parameterMap.put("FILENAME", fileName);    parameterMap.put("URL", url);    parameterMap.put("TOPATH", toPath);  }  public ResumeBrokenUploads(String fileName, String url, String filePath) {    setFileName(fileName);    setFilePath(filePath);    parameterMap.put("FILENAME", fileName);    parameterMap.put("URL", url);  }  public Map fetchFile() {    Map returnMap = new HashMap();    Transfer transfer = new Transfer(parameterMap, "fetchTempFile");    transfer.startTransfer();    returnMap = transfer.getReturnMap();    return returnMap;  }  public Map uploadFile() {    Map returnMap = new HashMap();    returnMap = fetchFile();    if (returnMap.get("ERRORMESSAGE") != null && !"".equals(returnMap.get("ERRORMESSAGE"))) {      System.out.println(returnMap.get("ERRORMESSAGE"));      return returnMap;    } else {      System.out.println(returnMap.get("SERVERFILELEN"));      File file = new File(filePath + "/" + fileName);      int localFileLen = 0;      int buffLen = 1024 * 1024 * 5;      byte[] buff = new byte[buffLen];      int len = -1;      int serverFileLen = Integer.parseInt((String) returnMap.get("SERVERFILELEN"));      try {        FileInputStream in = new FileInputStream(file);        localFileLen = in.available();        if (serverFileLen != 0) {          in.skip(serverFileLen);        }        while (serverFileLen < localFileLen) {          if ((len = in.read(buff)) != -1) {            parameterMap.put("FILENAME", fileName);            parameterMap.put("CLIENTFILELEN", String.valueOf(localFileLen));            byte[] buf = new byte[len];            if (len < buffLen) {              for (int i = 0; i < len; i++) {                buf[i] = buff[i];              }              parameterMap.put("BUFFLEN", len);              parameterMap.put("BUFFCONTENT", buf);            } else {              parameterMap.put("BUFFLEN", buffLen);              parameterMap.put("BUFFCONTENT", buff);            }            Transfer transfer = new Transfer(parameterMap, "uploadFile");            transfer.startTransfer();            returnMap = transfer.getReturnMap();            serverFileLen = serverFileLen + buffLen;          }        }        in.close();      } catch (FileNotFoundException e) {        e.printStackTrace();      } catch (IOException e) {        e.printStackTrace();      }      return returnMap;    }  }  public static void main(String[] args) {    ResumeBrokenUploads upload = new ResumeBrokenUploads("foxmail65.exe", "http://10.76.28.22:7003/ZC/", "I:\\pwh\\出差常用软件", "fileUploads");    Map map = upload.uploadFile();    System.out.println(map.get("success"));  }}