/**
 * 
 */
package com.ufgov.zc.common.system.exception;

/**
 * @author Administrator
 * 
 * 数据导入导入异常，用于两个不同网段间的数据同步
 *
 */
public class DataExchangeException extends BaseException {

  /**
   * 
   */
  private static final long serialVersionUID = -3631575085229779706L;
  
  public DataExchangeException(String message) {

    super(message);

  }
}
