/**
 * 
 */
package com.ufgov.zc.client.component.button;

/**
 * @author Administrator
 *
 */
public class ZcUnFinishCaiGouButton extends FuncButton {

  /**

   * 

   */

  private static final long serialVersionUID = -2564446291342163428L;

  public ZcUnFinishCaiGouButton() {

    super();

  }

  protected void init() {

    this.funcId = "antiffinishitem";

    this.defaultText = "取消结项";

    this.iconName = "untread.jpg";

    super.init();

  }

}

