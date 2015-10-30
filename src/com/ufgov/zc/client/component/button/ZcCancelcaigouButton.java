/**
 * 
 */
package com.ufgov.zc.client.component.button;

/**
 * @author Administrator
 *
 */
public class ZcCancelcaigouButton extends FuncButton {

  /**

   * 

   */

  private static final long serialVersionUID = -2564446291342163428L;

  public ZcCancelcaigouButton() {

    super();

  }

  protected void init() {

    this.funcId = "fcancelcaigou";

    this.defaultText = "取消采购";

    this.iconName = "delete.gif";

    super.init();

  }

}
