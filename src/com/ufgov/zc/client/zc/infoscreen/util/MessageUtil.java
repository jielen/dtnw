package com.ufgov.zc.client.zc.infoscreen.util;

public class MessageUtil {
  private static MessageFactory factory;
  static {
    factory = new MemoryMessageFactory();
    factory.init();
  }

  public static MessageFactory getFactoryInstance() {
    return factory;
  }
}
