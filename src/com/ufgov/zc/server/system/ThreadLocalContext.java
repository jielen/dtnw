package com.ufgov.zc.server.system;import java.util.Map;public class ThreadLocalContext {  private final static ThreadLocal threadLocal = new ThreadLocal();  public synchronized static void set(Map value) {    threadLocal.set(value);  }  public synchronized static void setAttribute(Object key, Object value) {    ((Map) threadLocal.get()).put(key, value);  }  public synchronized static Object getAttribute(Object key) {    Object value = ((Map) threadLocal.get()).get(key);    return value;  }}