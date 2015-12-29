package com.ufgov.zc.client.zc.infoscreen.util;

import java.util.ArrayList;
import java.util.List;

import com.ufgov.zc.common.system.util.ObjectUtil;

public class MemoryMessageFactory implements MessageFactory {

  private static List message = new ArrayList();

  @Override
  public void init() {
    // TCJLODO Auto-generated method stub

  }

  @Override
  public boolean hasMessage() {
    return message.size() > 0;
  }

  @Override
  public List getTextMessage() {
    Object copy = ObjectUtil.deepCopy(message);
    message.clear();
    return (List)copy;
  }

  public static void sendMess(List mess) {
    message.addAll(mess);
  }

}
