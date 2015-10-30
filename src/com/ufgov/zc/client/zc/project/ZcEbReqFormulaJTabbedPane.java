package com.ufgov.zc.client.zc.project;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import com.ufgov.zc.client.zc.ztb.ChangeChineseNumber;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.model.AsVal;
import com.ufgov.zc.common.zc.model.ZcBaseBill;
import com.ufgov.zc.common.zc.model.ZcEbFormula;
import com.ufgov.zc.common.zc.model.ZcEbFormulaPack;
import com.ufgov.zc.common.zc.model.ZcEbPack;
import com.ufgov.zc.common.zc.model.ZcEbProj;
import com.ufgov.zc.common.zc.model.ZcEbRequirement;

public class ZcEbReqFormulaJTabbedPane extends JTabbedPane implements MouseListener {

  private List formulaPackList;

  private List packList;

  private ZcBaseBill zcBaseBill;

  private boolean saveButtonisVisible = false;

  private boolean formulaCanDelete = false;

  public Map<String, ZcEbPack> packMap = new HashMap<String, ZcEbPack>();

  public Map valMap;

  public List<AsVal> valList;

  public ZcEbReqFormulaJTabbedPane(List formulaPackList, List packList, ZcBaseBill zcBaseBill, boolean saveButtonisVisible, boolean formulaCanDelete) {
    this.formulaPackList = formulaPackList;
    this.packList = packList;
    this.zcBaseBill = zcBaseBill;
    this.saveButtonisVisible = saveButtonisVisible;
    this.formulaCanDelete = formulaCanDelete;
    this.valList = converterListToValList(packList);
    this.valMap = getValMap(valList);
    initComponet();
    addMouseListener(this);
  }

  private void initComponet() {
    if (formulaPackList.size() == 0) {
      ZcEbFormula formula = new ZcEbFormula();
      setFormulaValue(formula);
      ZcEbFormulaPack zcEbFormulaPack = new ZcEbFormulaPack();
      zcEbFormulaPack.setZcEbFormula(formula);
      ZcEbReqFormulaPanel panel = new ZcEbReqFormulaPanel(zcEbFormulaPack, this.packList, this, saveButtonisVisible);
      formulaPackList.add(zcEbFormulaPack);
      this.addTab("评标方法" + ChangeChineseNumber.doChange(1 + ""), panel);
    } else {
      for (int i = 0; i < formulaPackList.size(); i++) {
        ZcEbFormulaPack zcEbFormulaPack = (ZcEbFormulaPack) formulaPackList.get(i);
        ZcEbReqFormulaPanel panel = new ZcEbReqFormulaPanel(zcEbFormulaPack, this.packList, this, saveButtonisVisible);

        if (i == 0) {
          this.addTab("评标方法" + ChangeChineseNumber.doChange(i + 1 + ""), panel);
        } else {
          if (formulaCanDelete) {
            this.addTab("评标方法" + ChangeChineseNumber.doChange(i + 1 + ""), panel, null);
          } else {
            this.addTab("评标方法" + ChangeChineseNumber.doChange(i + 1 + ""), panel);
          }
        }
      }
    }
  }

  public boolean isDataChanged() {
    int tabSize = this.getComponentCount();
    boolean isDataChanged = false;
    for (int i = 0; i < tabSize; i++) {
      ZcEbReqFormulaPanel panel = (ZcEbReqFormulaPanel) this.getComponentAt(i);
      if (panel.isDataChanged()) {
        isDataChanged = true;
      }
    }
    return isDataChanged;
  }

  public void setOldObject() {
    int tabSize = this.getComponentCount();
    for (int i = 0; i < tabSize; i++) {
      ZcEbReqFormulaPanel panel = (ZcEbReqFormulaPanel) this.getComponentAt(i);
      panel.setOldObject();
    }
  }

  public boolean checkBeforeSave() {
    int tabSize = this.getComponentCount();
    if (tabSize == 0) {
      JOptionPane.showMessageDialog(this, "评标方法还没有定制！", "提示", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    /**
     * 校验标段是否选择重复
     */

    int packNum = 0;
    for (int i = 0; i < tabSize; i++) {
      ZcEbReqFormulaPanel panel = (ZcEbReqFormulaPanel) this.getComponentAt(i);
      if (!panel.checkBeforeSave()) {
        return false;
      }
      packNum = packNum + panel.zcEbFormulaPack.getPackList().size();
    }
    if (packNum > packList.size()) {
      JOptionPane.showMessageDialog(this, "存在一个分包定义了多个评标方法，请检查！", "提示", JOptionPane.ERROR_MESSAGE);
      return false;
    } else if (packNum < packList.size()) {
      JOptionPane.showMessageDialog(this, "存在有的分包没有定义评标方法，请检查！", "提示", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  public void addTab(String title, Component component, Icon extraIcon) {

    super.addTab(title, new CloseTabIcon(extraIcon), component);

  }

  public void addFormula() {
    ZcEbFormula formula = new ZcEbFormula();
    setFormulaValue(formula);
    ZcEbFormulaPack zcEbFormulaPack = new ZcEbFormulaPack();
    zcEbFormulaPack.setZcEbFormula(formula);
    this.formulaPackList.add(zcEbFormulaPack);
    ZcEbReqFormulaPanel panel = new ZcEbReqFormulaPanel(zcEbFormulaPack, this.packList, this, saveButtonisVisible);
    if (formulaCanDelete) {
      this.addTab("评标方法" + ChangeChineseNumber.doChange(this.getComponentCount() + 1 + ""), panel, null);
    } else {
      this.addTab("评标方法" + ChangeChineseNumber.doChange(this.getComponentCount() + 1 + ""), panel);

    }
  }

  private void setFormulaValue(ZcEbFormula zcEbFormula) {

    String cglx = "";
    if (zcBaseBill instanceof ZcEbRequirement) {
      zcEbFormula.setProjectName(((ZcEbRequirement) zcBaseBill).getZcEbEntrust().getZcMakeName());
      zcEbFormula.setProjectCode(((ZcEbRequirement) zcBaseBill).getSn());
      cglx = ((ZcEbRequirement) zcBaseBill).getZcEbEntrust().getZcPifuCgfs();

    } else if (zcBaseBill instanceof ZcEbProj) {
      zcEbFormula.setProjectName(((ZcEbProj) zcBaseBill).getProjCode());
      zcEbFormula.setProjectCode(((ZcEbProj) zcBaseBill).getProjName());
      cglx = ((ZcEbProj) zcBaseBill).getPurType();
    }
    /**
     * 通过采购方式判断评标方法的类型:
     * 竞争性谈判、单一来源，询价最低报价法
     */
       if (cglx.equals(ZcSettingConstants.PITEM_OPIWAY_JZXTP) || cglx.equals(ZcSettingConstants.PITEM_OPIWAY_DYLY)) {
      zcEbFormula.setFactorType(ZcSettingConstants.FORMULA_FACTOR_LOWERPRICE_TYPE);
    }

  }

  public void doEdit(boolean isVisiable) {
    for (int i = 0; i < this.getComponentCount(); i++) {
      ZcEbReqFormulaPanel panel = (ZcEbReqFormulaPanel) this.getComponentAt(i);
      panel.doEidt(isVisiable);
    }
  }

  public void refreshZcEbReqFormulaJTabbedPanePackInfo(List packList) {
    this.packList = packList;
    int tabSize = this.getComponentCount();
    if (tabSize == 0) {
      return;
    }
    this.valList = converterListToValList(packList);
    this.valMap = getValMap(valList);
    for (int i = 0; i < tabSize; i++) {
      ZcEbReqFormulaPanel panel = (ZcEbReqFormulaPanel) this.getComponentAt(i);
      panel.refreshPackTablePanelTableProperTy(packList, valList, getValMap(valList));
    }
  }

  public List converterListToValList(List packList) {
    List<AsVal> valList = new ArrayList<AsVal>();
    packMap.clear();
    for (Object obj : packList) {
      ZcEbPack pack = (ZcEbPack) obj;
      AsVal val = new AsVal();
      val.setValId(pack.getPackCode());
      val.setVal(pack.getPackName());
      valList.add(val);
      packMap.put(pack.getPackCode(), pack);
    }
    return valList;
  }

  //将list转成map

  private Map getValMap(List list) {
    Map valMap = new HashMap();
    for (int i = 0; i < list.size(); i++) {
      AsVal asVal = (AsVal) list.get(i);
      valMap.put(asVal.getValId(), asVal.getVal());
    }
    return valMap;
  }

  public void mouseClicked(MouseEvent e) {

    int tabNumber = getUI().tabForCoordinate(this, e.getX(), e.getY());

    System.out.println(tabNumber + "==========");

    if (tabNumber < 0) {
      return;
    }
    if (getIconAt(tabNumber) == null) {
      return;
    }

    if (!formulaCanDelete) {
      return;
    }

    Rectangle rect = ((CloseTabIcon) getIconAt(tabNumber)).getBounds();

    if (rect.contains(e.getX(), e.getY())) {

      int flag = JOptionPane.showConfirmDialog(this, "确定删除 该评标方法吗 ？", "删除确认", 0);

      if (flag != 0) {

        return;

      }

      this.removeTabAt(tabNumber);

    }

  }

  public void mouseEntered(MouseEvent e) {

  }

  public void mouseExited(MouseEvent e) {

  }

  public void mousePressed(MouseEvent e) {

  }

  public void mouseReleased(MouseEvent e) {

  }

}

/**  

 * The class which generates the 'X' icon for the tabs. The constructor  

 * accepts an icon which is extra to the 'X' icon, so you can have tabs  

 * like in JBuilder. This value is null if no extra icon is required.  

 */

class CloseTabIcon implements Icon {

  private int x_pos;

  private int y_pos;

  private int width;

  private int height;

  private Icon fileIcon;

  public CloseTabIcon(Icon fileIcon) {

    this.fileIcon = fileIcon;

    width = 16;

    height = 16;

  }

  public void paintIcon(Component c, Graphics g, int x, int y) {

    this.x_pos = x;

    this.y_pos = y;

    Color col = g.getColor();

    g.setColor(Color.black);

    int y_p = y + 2;

    g.drawLine(x + 1, y_p, x + 12, y_p);

    g.drawLine(x + 1, y_p + 13, x + 12, y_p + 13);

    g.drawLine(x, y_p + 1, x, y_p + 12);

    g.drawLine(x + 13, y_p + 1, x + 13, y_p + 12);

    g.drawLine(x + 3, y_p + 3, x + 10, y_p + 10);

    g.drawLine(x + 3, y_p + 4, x + 9, y_p + 10);

    g.drawLine(x + 4, y_p + 3, x + 10, y_p + 9);

    g.drawLine(x + 10, y_p + 3, x + 3, y_p + 10);

    g.drawLine(x + 10, y_p + 4, x + 4, y_p + 10);

    g.drawLine(x + 9, y_p + 3, x + 3, y_p + 9);

    g.setColor(col);

    if (fileIcon != null) {

      fileIcon.paintIcon(c, g, x + width, y_p);

    }

  }

  public int getIconWidth() {

    return width + (fileIcon != null ? fileIcon.getIconWidth() : 0);

  }

  public int getIconHeight() {

    return height;

  }

  public Rectangle getBounds() {

    return new Rectangle(x_pos, y_pos, width, height);

  }

}
