package com.ufgov.zc.client.component.button;public class PrintFormulaButton extends FuncButton {  /**   *    */  private static final long serialVersionUID = 4783499025450922527L;  public PrintFormulaButton() {    super();  }  protected void init() {    this.funcId = "fprintformula";    this.defaultText = "打印评标方法";    this.iconName = "print.gif";    super.init();  }}