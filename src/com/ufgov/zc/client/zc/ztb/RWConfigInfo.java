package com.ufgov.zc.client.zc.ztb;

import com.ufgov.zc.client.zc.ztb.util.DESEncodeAndDecode;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class RWConfigInfo {
  private HashMap<String, String> hashmap = null;

  private String configFilePath = "softconfig.ini";

  public RWConfigInfo() {
  }

  public RWConfigInfo(String path) {
    if (path != null && !"".equals(path)) {
      configFilePath = path;
    }
  }

  public static void main(String[] args) throws Exception {
    RWConfigInfo c = new RWConfigInfo("softconfig.ini");
    c.doWriteConfig(new HashMap<String, String>());
    Map<String, String> hashMap = c.readcfg();
    P.pm(hashMap);
  }

  public void doWriteConfig(Map<String, String> map) throws Exception {
    String info = this.composeSoftInfoBeforeEncoded(map);
    try {
      info = "info:" + DESEncodeAndDecode.encryptBASE64(info);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e);
    }
    this.writeConfig(info);
  }

  public HashMap<String, String> readcfg() {
    this.hashmap = new HashMap<String, String>();
    readConfig();
    return this.hashmap;
  }

  private void readConfig() {
    Properties properties = new Properties();
    InputStream in = RWConfigInfo.class.getResourceAsStream(this.configFilePath);
    try {
      properties.load(in);
      Iterator<Object> it = properties.keySet().iterator();
      while (it.hasNext()) {
        String key = (String) it.next();
        this.hashmap.put(key, properties.getProperty(key));
      }
      this.hashmap.put("info", DESEncodeAndDecode.encryptBASE64(this.composeSoftInfoBeforeEncoded(this.hashmap)));
      in.close();
    } catch (Exception e3) {
      e3.printStackTrace();
    }
  }

  private void writeConfig(String content) {
    OutputStream out = null;
    try {
      String path = getConfigFilePath().substring(0, getConfigFilePath().length());
      path = getClass().getResource(path).toString();
      path = path.substring(path.indexOf("file:") + 6, path.length());
      File file = new File(path);
      out = new FileOutputStream(file, false);
      byte[] bytes = content.getBytes();
      for (byte cb : bytes) {
        out.write(cb);
      }
      out.close();
    } catch (FileNotFoundException e3) {
      e3.printStackTrace();
    } catch (IOException e3) {
      e3.printStackTrace();
    }
  }

  public String composeSoftInfoBeforeEncoded(Map<String, String> map) {
    if (map == null) {
      return null;
    }
    StringBuffer buff = new StringBuffer();
    Iterator<String> it = map.keySet().iterator();
    while (it.hasNext()) {
      String key = it.next();
      buff.append(key);
      buff.append(":");
      buff.append(map.get(key));
      buff.append("@;@");
    }
    if (buff.length() > 2) {
      buff.delete(buff.length() - 3, buff.length());
    }
    return buff.toString();
  }

  public HashMap<String, String> getHashmap() {
    return hashmap;
  }

  public void setHashmap(HashMap<String, String> hashmap) {
    this.hashmap = hashmap;
  }

  public String getConfigFilePath() {
    return configFilePath;
  }

  public void setConfigFilePath(String configFilePath) {
    this.configFilePath = configFilePath;
  }
}
