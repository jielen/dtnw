package com.ufgov.zc.client.datacache;import java.util.Collections;import java.util.HashMap;import java.util.List;import java.util.Map;import com.ufgov.zc.client.common.WorkEnv;import com.ufgov.zc.common.commonbiz.model.BankAccount;import com.ufgov.zc.common.system.RequestMeta;public class ReceBankAccountDataCache {  private static Map<String, Map> dataMap = new HashMap<String, Map>();  private static Map<String, List<BankAccount>> dataListMap = new HashMap<String, List<BankAccount>>();  /**   * 按用户过滤供应商信息   * @param coCode   * @return   */  public static List getReceBankAccount(String userId) {    String key = userId;    List dataList = dataListMap.get(key);    if (dataList == null) {      refreshData(userId);      return dataListMap.get(key);    }    return dataList;  }  public static synchronized void refreshData(String userId) {    RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();    int nd = WorkEnv.getInstance().getTransNd();    List<BankAccount> dataList = Collections.unmodifiableList(Util.baseDataServiceDelegate    .getReceBankAccountList(nd, userId, requestMeta));    String key = userId;    dataListMap.put(key, dataList);    dataMap.put(key, createMap(dataList));  }  private static Map<String, BankAccount> createMap(List dataList) {    Map<String, BankAccount> dataMap = new HashMap<String, BankAccount>();    for (Object o : dataList) {      BankAccount v = (BankAccount) o;      dataMap.put(v.getBankAccCode(), v);    }    return dataMap;  }}