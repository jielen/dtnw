package com.ufgov.zc.client.zc.flowconsole;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.zc.flowconsole.events.ButtonMouseEvent;
import com.ufgov.zc.client.zc.flowconsole.events.NodeMouseEvent;
import com.ufgov.zc.client.zc.flowconsole.parts.Button;
import com.ufgov.zc.client.zc.flowconsole.parts.Link;
import com.ufgov.zc.client.zc.flowconsole.parts.Node;
import com.ufgov.zc.client.zc.flowconsole.tip.TipThread;
import com.ufgov.zc.common.system.RequestMeta;

public abstract class FlowConsoleCanvas extends JPanel implements ICanvas {
  /**
   * 
   */
  private static final long serialVersionUID = -1321991767536693124L;

  private Node selectedNode = null;

  private Button moveOverButton = null;

  protected Map<String, Node> nodeMap = new HashMap<String, Node>();

  protected List<Link> arrowList = new ArrayList<Link>();

  protected RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  protected int canvasOffsetX = 0;//绘图的相对位移横坐标

  protected int canvasOffsetY = 0;//绘图的相对位移纵坐标

  protected boolean isCanvasAlign = true;//图形自动居中 开关

  private ActionListener defaultAction = new ActionListener() {

    public void actionPerformed(ActionEvent e) {
    }
  };

  protected void loadConfig(String configPath) {
    UfidaUtil.loadModelNode(configPath, this, getNodeAction(), getNodeButtonAction(), getShortcutAction());
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        panelMouseClicked(e);
      }
    });
    this.addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        panelMouseMoving(e);
      }
    });
  }

  /*
   * 计算要求居中时，图形的左边距
   */
  public int getCenterOffsetX() {
    ImageIcon jizuoIcon = UfidaUtil.getIcon(UfidaUtil.NODE_BACK_GRAND_IMG_PATH);
    Collection<Node> vals = nodeMap.values();
    int minx = Integer.MAX_VALUE, maxx = Integer.MIN_VALUE;
    //绘制背景
    for (Iterator iterator = vals.iterator(); iterator.hasNext();) {
      Node node = (Node) iterator.next();
      node.setWidth(jizuoIcon.getIconWidth());
      node.setHeight(jizuoIcon.getIconHeight());
      if (minx > node.getX()) {
        minx = node.getX();
      }
      if (maxx < node.getX()) {
        maxx = node.getX() + node.getWidth();
      }
    }
    int completeChartWidth = maxx - minx;
    int completeChartLeft = minx;
    return (this.getWidth() - completeChartWidth) / 2 - completeChartLeft;
  }

  public void paintComponent(Graphics g) {
    int cx = canvasOffsetX;
    if (isCanvasAlign) {//实现自动横向居中
      cx = getCenterOffsetX();
    }
    canvasOffsetX = cx;
    int cy = canvasOffsetY;
    g.clearRect(0, 0, this.getWidth(), this.getWidth());
    ImageIcon jizuoIcon = UfidaUtil.getIcon(UfidaUtil.NODE_BACK_GRAND_IMG_PATH);
    Image jizuoImage = jizuoIcon.getImage();
    Collection<Node> vals = nodeMap.values();
    //绘制背景
    for (Iterator iterator = vals.iterator(); iterator.hasNext();) {
      Node node = (Node) iterator.next();
      node.setWidth(jizuoIcon.getIconWidth());
      node.setHeight(jizuoIcon.getIconHeight());
      g.drawImage(jizuoImage, node.getX() + cx, node.getY() + cy, null);
    }

    //绘制节点上图片，显示文字
    Font defualtFont = g.getFont();
    for (Iterator iterator = vals.iterator(); iterator.hasNext();) {
      Node node = (Node) iterator.next();
      ImageIcon icon = UfidaUtil.getIcon(node.getIcon());
      Image iconImage = icon.getImage();
      g.drawImage(iconImage, node.getX() + node.getModuleImageOffsetX() + cx, node.getY() + node.getModuleImageOffsetY() + cy, null);
      g.setColor(node.getTextColor());
      g.setFont(node.getTextFont());
      g.drawString(node.getName(), node.getX() + node.getLabelXOffset() + cx, node.getY() + node.getLabelYOffset() + cy);
      g.setFont(defualtFont);

      List<Button> bts = node.getButtons();
      for (Iterator iterator2 = bts.iterator(); iterator2.hasNext();) {
        Button button = (Button) iterator2.next();
        ImageIcon bticon = UfidaUtil.getIcon(button.getIcon());
        Image image = bticon.getImage();
        g.drawImage(image, node.getX() + button.getOffsetX() + cx, node.getY() + button.getOffsetY() + cy, null);
        button.setWidth(bticon.getIconWidth());
        button.setHeight(bticon.getIconHeight());
        button.setX(node.getX() + button.getOffsetX() + cx);
        button.setY(node.getY() + button.getOffsetY() + cy);
      }
      if (node.isFlag()) {
        ImageIcon flagIcon = UfidaUtil.getIcon(UfidaUtil.NODE_FLAG_IMG_PATH);
        Image image = flagIcon.getImage();
        g.drawImage(image, node.getX() + jizuoIcon.getIconWidth() - flagIcon.getIconWidth() + cx, node.getY() + cy, null);
      }
    }
    //绘制箭头
    for (Iterator iterator = arrowList.iterator(); iterator.hasNext();) {
      Link link = (Link) iterator.next();
      ImageIcon linkIcon = UfidaUtil.getIcon(link.getDiretionUrl());
      Image linkImage = linkIcon.getImage();
      g.drawImage(linkImage, link.getX() + cx, link.getY() + cy, null);
    }
  }

  public void panelMouseClicked(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();
    Collection<Node> vals = nodeMap.values();
    boolean isdebug = false;
    for (Iterator iterator = vals.iterator(); iterator.hasNext();) {
      Node node = (Node) iterator.next();
      List<Button> bts = node.getButtons();
      for (Iterator iterator2 = bts.iterator(); iterator2.hasNext();) {
        Button button = (Button) iterator2.next();
        if (UfidaUtil.isInButtonRect(button, x, y) && button.getStatus() != Button.DISENABLE_STATUS) {
          button.getAction().actionPerformed(
            new ButtonMouseEvent(e.getSource(), e.getID(), ButtonMouseEvent.CLICK_MOUSE_COMMAND, node.getId(), button));
          isdebug = true;
          break;
        }
      }
      if (UfidaUtil.isInNodeRect(node, x - canvasOffsetX, y) && node.getStatus() != Node.DISENABLE_STATUS) {
        if (selectedNode != null) {
          selectedNode.setStatus(Node.GENERAL_STATUS);
        }
        node.setStatus(Node.SELECTED_STATUS);
        selectedNode = node;

        node.getAction().actionPerformed(new NodeMouseEvent(e.getSource(), e.getID(), NodeMouseEvent.CLICK_MOUSE_COMMAND, node.getId()));
        break;
      }
      if (isdebug) {
        break;
      }
    }
    repaint();
  }

  protected void panelMouseMoving(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();
    Collection<Node> vals = nodeMap.values();
    boolean isOverBt = false;
    for (Iterator iterator = vals.iterator(); iterator.hasNext();) {
      Node node = (Node) iterator.next();
      List<Button> bts = node.getButtons();
      for (Iterator iterator2 = bts.iterator(); iterator2.hasNext();) {
        Button button = (Button) iterator2.next();
        if (UfidaUtil.isInButtonRect(button, x, y) && button.getStatus() != Button.DISENABLE_STATUS) {
          isOverBt = true;
          if (moveOverButton == button) {
            break;
          }
          if (moveOverButton != null) {
            moveOverButton.setOffsetX(moveOverButton.getOffsetX() + 1);
            moveOverButton.setOffsetY(moveOverButton.getOffsetY() - 1);
            moveOverButton.setIcon(moveOverButton.getGenIcon());
          }

          moveOverButton = button;
          moveOverButton.setOffsetX(moveOverButton.getOffsetX() - 1);
          moveOverButton.setOffsetY(moveOverButton.getOffsetY() + 1);
          moveOverButton.setIcon(moveOverButton.getHighLightIcon());

          Point point = this.getLocationOnScreen();
          TipThread tipth = new TipThread(point.x + button.getX() + button.getWidth(), point.y + button.getY(), button.getToolTipText(), this);
          tipth.start();

        }
      }
    }
    if (!isOverBt && moveOverButton != null) {
      moveOverButton.setOffsetX(moveOverButton.getOffsetX() + 1);
      moveOverButton.setOffsetY(moveOverButton.getOffsetY() - 1);
      moveOverButton.setIcon(moveOverButton.getGenIcon());
      moveOverButton = null;

    }
    repaint();
  }

  protected ActionListener getNodeAction() {
    return defaultAction;
  }

  protected ActionListener getNodeButtonAction() {
    return defaultAction;
  }

  protected ActionListener getShortcutAction() {
    return defaultAction;
  }

  @Override
  public void addElement(Node node) {
    // TODO Auto-generated method stub
    nodeMap.put(node.getId(), node);
  }

  @Override
  public void addElement(Link arrow) {
    // TODO Auto-generated method stub
    arrowList.add(arrow);
  }

}
