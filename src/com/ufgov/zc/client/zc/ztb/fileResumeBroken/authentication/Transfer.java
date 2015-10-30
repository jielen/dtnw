package com.ufgov.zc.client.zc.ztb.fileResumeBroken.authentication;

import com.caucho.hessian.client.HessianProxyFactory;
import com.ufgov.zc.client.zc.ztb.util.GV;
import com.ufgov.zc.common.zc.Communication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class Transfer {
  private Map parameterMap = new HashMap();

  private Map returnMap = new HashMap();

  private String functionDo;

  public String getFunctionDo() {
    return functionDo;
  }

  public void setFunctionDo(String functionDo) {
    this.functionDo = functionDo;
  }

  public Map getParameterMap() {
    return parameterMap;
  }

  public void setParameterMap(Map parameterMap) {
    this.parameterMap = parameterMap;
  }

  public Transfer(Map paraMap, String functionDo) {
    if (paraMap.get("PROJCODE") != null || paraMap.get("PROJECTCODE") != null) {
      if (paraMap.get("PROJCODE") != null && !"".equals(paraMap.get("PROJCODE"))) {
        paraMap.put("PROJECTCODE", paraMap.get("PROJCODE"));
      }
      if (paraMap.get("PROJECTCODE") != null && !"".equals(paraMap.get("PROJECTCODE"))) {
        paraMap.put("PROJCODE", paraMap.get("PROJECTCODE"));
      }
    }
    setParameterMap(paraMap);
    setFunctionDo(functionDo);
  }

  public Map getReturnMap() {
    return returnMap;
  }

  public void setReturnMap(Map returnMap) {
    Iterator it = returnMap.keySet().iterator();
    while (it.hasNext()) {
      String key = (String) it.next();
      this.returnMap.put(key.toUpperCase(), returnMap.get(key));
    }
    this.returnMap.putAll(returnMap);
  }

  public void startTransfer() {
    String url = (String) parameterMap.get("URL");
    if (url == null && "".equals(url)) {
      Properties properties = new Properties();
      String propertiesPath = "../../url.properties";
      InputStream in = getClass().getResourceAsStream(propertiesPath);
      try {
        properties.load(in);
        url = properties.getProperty("url").toString();
        System.out.println(url);
      } catch (FileNotFoundException e3) {
        e3.printStackTrace();
      } catch (IOException e3) {
        e3.printStackTrace();
      }
    }
    url = url + "communication";
    try {
        ClassLoader oLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(Transfer.class.getClassLoader());
        HessianProxyFactory factory = new HessianProxyFactory();
        Communication communication = (Communication) factory.create(Communication.class, url, Transfer.class.getClassLoader());
        setReturnMap(communication.buildCommunication(parameterMap, functionDo));
        Thread.currentThread().setContextClassLoader(oLoader);
    } catch (Exception e) {
      e.printStackTrace();
      returnMap.put("ERRORMESSAGE", GV.getSimpleMsg("serverLinkErr") + e.getMessage());
      returnMap.put("FAILREASON", GV.getOperateMsg("serverLinkErrUrl", url) + e.getMessage());
    }
  }
}
