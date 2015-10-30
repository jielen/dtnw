package com.ufgov.zc.client.zc.flowconsole;

import java.util.List;

import com.ufgov.zc.client.zc.flowconsole.parts.Link;
import com.ufgov.zc.client.zc.flowconsole.parts.Node;

public interface ICanvas {
  public void addElement(Node node);

  public void addElement(Link arrow);

  public List<String> getEnableElements();
}
