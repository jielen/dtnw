/**
 * 
 */
package com.ufgov.zc.client.zc.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Administrator
 *
 */
public class DateTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    DateTest t=new DateTest();
    t.before();
  }

  void before(){
    Calendar c=Calendar.getInstance();
    DateFormat df=DateFormat.getDateInstance();
    try {
      Date d=df.parse("2013-10-11");
      Date dd=c.getTime();
      System.out.println(dd.before(d));
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
}
