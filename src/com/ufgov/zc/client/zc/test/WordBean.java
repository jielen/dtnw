package com.ufgov.zc.client.zc.test;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class WordBean {
  // word文档  
  static Dispatch doc;

  // word运行程序对象  
  private ActiveXComponent word;

  // 所有word文档集合  
  private Dispatch documents;

  // 选定的范围或插入点  
  private Dispatch selection;

  private boolean saveOnExit = true;

  public WordBean() throws Exception {
    if (word == null) {
      word = new ActiveXComponent("Word.Application");
      word.setProperty("Visible", new Variant(false)); // 不可见打开word  
      word.setProperty("AutomationSecurity", new Variant(3)); // 禁用宏  
    }
    if (documents == null)
      documents = word.getProperty("Documents").toDispatch();
  }

  /** 
   * 重载，制定文档用户名称，主要用于痕迹保留时候显示对应的用户 
   * @param username 
   * @throws Exception 
   */
  public WordBean(String username) throws Exception {
    if (word == null) {
      word = new ActiveXComponent("Word.Application");
      word.setProperty("UserName", new Variant(username));
      word.setProperty("Visible", new Variant(true)); // 不可见打开word  
      word.setProperty("AutomationSecurity", new Variant(3)); // 禁用宏  
    }
    if (documents == null)
      documents = word.getProperty("Documents").toDispatch();
  }

  /** 
   * * 设置退出时参数 
   *  
   * @param saveOnExit * 
   *            boolean true-退出时保存文件，false-退出时不保存文件 
   *  
   */

  public void setSaveOnExit(boolean saveOnExit) {
    this.saveOnExit = saveOnExit;
  }

  /** 
   * * 创建一个新的word文档 
   */

  public void createNewDocument() {
    doc = Dispatch.call(documents, "Add").toDispatch();
    selection = Dispatch.get(word, "Selection").toDispatch();
  }

  /** 
   * * 打开一个已存在的文档 * * 
   *  
   * @param docPath 
   *  
   */

  public void openDocument(String docPath) {
    closeDocument();
    doc = Dispatch.call(documents, "Open", docPath).toDispatch();
    Dispatch.put(doc, "TrackRevisions", new Variant(true));
    Dispatch.put(doc, "PrintRevisions", new Variant(true));
    Dispatch.put(doc, "ShowRevisions", new Variant(false));
    selection = Dispatch.get(word, "Selection").toDispatch();
  }

  /** 
   *    
   * 只读 打开一个保护文档,
   * @param docPath-文件全名 * 
   * @param pwd-密码 
   *  
   */

  public void openDocumentOnlyRead(String docPath, String pwd) throws Exception {
    closeDocument();
    doc = Dispatch.callN(documents, "Open",
      new Object[] { docPath, new Variant(false), new Variant(true), new Variant(true), pwd, "", new Variant(false) }).toDispatch();
    selection = Dispatch.get(word, "Selection").toDispatch();
  }

  public void openDocument(String docPath, String pwd) throws Exception {
    closeDocument();
    doc = Dispatch.callN(documents, "Open", new Object[] { docPath, new Variant(false), new Variant(false), new Variant(true), pwd }).toDispatch();
    selection = Dispatch.get(word, "Selection").toDispatch();
  }

  /** 
   * * 把选定的内容或者插入点向右移动 * * 
   *  
   * @param pos * 
   *            移动的距离 
   *  
   */

  public void moveRight(int pos) {

    if (selection == null)

      selection = Dispatch.get(word, "Selection").toDispatch();

    for (int i = 0; i < pos; i++)

      Dispatch.call(selection, "MoveRight");

  }

  /** 
   * * 把插入点移动到文件首位置 * 
   *  
   */

  public void moveStart() {

    if (selection == null)

      selection = Dispatch.get(word, "Selection").toDispatch();

    Dispatch.call(selection, "HomeKey", new Variant(6));

  }

  /** 
   * * 从选定内容或插入点开始查找文本 * * 
   *  
   * @param toFindText * 
   *            要查找的文本 * 
   * @return boolean true-查找到并选中该文本，false-未查找到文本 
   *  
   */
  @SuppressWarnings("static-access")
  public boolean find(String toFindText) {
    Dispatch selection = Dispatch.get(word, "Selection").toDispatch(); // 输入内容需要的对象 
    if (toFindText == null || toFindText.equals(""))

      return false;

    // 从selection所在位置开始查询  

    Dispatch find = word.call(selection, "Find").toDispatch();

    // 设置要查找的内容  

    Dispatch.put(find, "Text", toFindText);

    // 向前查找  

    Dispatch.put(find, "Forward", "True");

    // 设置格式  

    Dispatch.put(find, "Format", "True");

    // 大小写匹配  

    Dispatch.put(find, "MatchCase", "True");

    // 全字匹配  

    Dispatch.put(find, "MatchWholeWord", "True");

    // 查找并选中  

    return Dispatch.call(find, "Execute").getBoolean();

  }

  /** 
  246.  * * 把选定选定内容设定为替换文本 * * 
  247.  *  
  248.  * @param toFindText * 
  249.  *            查找字符串 * 
  250.  * @param newText * 
  251.  *            要替换的内容 * 
  252.  * @return 
  253.  *  
  254.  */

  public boolean replaceText(String toFindText, String newText, Dispatch selection) {

    if (!find(toFindText))
      return false;
    Dispatch.put(selection, "Text", newText);

    return true;

  }

  /** 
  269.  * * 全局替换文本 * * 
  270.  *  
  271.  * @param toFindText * 
  272.  *            查找字符串 * 
  273.  * @param newText * 
  274.  *            要替换的内容 
  275.  *  
  276.  */

  public void replaceAllText(String toFindText, String newText) {
    while (find(toFindText)) {
      Dispatch selection = Dispatch.get(word, "Selection").toDispatch(); // 输入内容需要的对象 
      Dispatch.put(selection, "Text", newText);
      Dispatch.call(selection, "MoveRight");

    }

  }

  /**
   *
   * @param tableIndex
   * @param cellRowIdx
   * @param cellColIdx
   * @return
   * @author lijf 
   * @description 获取表格的某个单元格内容
   * @update 2012-7-27 下午04:22:15
   */
  public String getTxtFromCell(int tableIndex, int cellRowIdx, int cellColIdx) {

    // 所有表格  

    Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();

    // 要填充的表格  

    Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex))

    .toDispatch();

    Dispatch cell = Dispatch.call(table, "Cell", new Variant(cellRowIdx),

    new Variant(cellColIdx)).toDispatch();

    Dispatch.call(cell, "Select");

    String ret = "";

    ret = Dispatch.get(selection, "Text").toString();

    ret = ret.substring(0, ret.length() - 1); // 去掉最后的回车符;  

    return ret;

  }

  /**
   * @author lijf 
   * @description 关闭打开的word
   * @update 2012-7-27 下午04:21:10
   */
  public void closeDocument() {
    if (doc != null) {
      // Dispatch.call(doc, "ShowRevisions", new Variant(saveOnExit));  
      Dispatch.call(doc, "Save");
      Dispatch.call(doc, "Close", new Variant(saveOnExit));
      doc = null;
    }
  }

  /**
   * 增加一行
   * 
   * @param tableIndex
   *            word文档中的第N张表(从1开始)
   */
  public void addRow(int tableIndex) {
    Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
    // 要填充的表格
    Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
    // 表格的所有行
    Dispatch rows = Dispatch.get(table, "Rows").toDispatch();
    Dispatch.call(rows, "Add").toDispatch();
    System.out.println("添加一行");
  }

  /**
   *
   * @param tableIndex  第N个表格
   * @return
   * @author lijf 
   * @description 获取第N个表格的总行数
   * @update 2012-7-27 下午04:05:47
   */
  public int getRowCount(int tableIndex) {
    int count = 0;
    Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
    // 要填充的表格
    Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
    Dispatch rows = Dispatch.get(table, "Rows").toDispatch();
    count = Dispatch.get(rows, "Count").getInt();
    return count;
  }

  /**
   *
   * @param tableIndex  第N个表格
   * @param cell 一行的单元格数量
   * @param object 填充内容的对象
   * @author lijf 
   * @description 向第N个表格中添加一行数据
   * @update 2012-7-27 下午03:25:45
   */
  public void addRowData(int tableIndex, int cell, Object object, String[] strs) {
    addRow(tableIndex);//添加一行
    int rowIndex = getRowCount(tableIndex);
    Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
    // 要填充的表格
    Dispatch table = Dispatch.call(tables, "Item", new Variant(tableIndex)).toDispatch();
    //为当前行添加内容
    for (int i = 1; i <= cell; i++) {
      putTxtToCell(table, rowIndex, i, strs[i - 1]);
    }
  }

  // 打开一个存在的word文档,并用document 引用 引用它 
  public void openFile(String wordFilePath) {
    Dispatch documents = Dispatch.get(word, "Documents").toDispatch();
    doc = Dispatch.call(documents, "Open", wordFilePath, new Variant(true)/* 是否进行转换ConfirmConversions */, new Variant(false)/* 是否只读 */)
      .toDispatch();

  }

  /**
   *
   * @param tableTitle
   * @param row
   * @param column
   * @author lijf 
   * @description 创建一个table
   * @update 2012-7-27 下午03:07:53
   */
  public void insertTable(String tableTitle, int row, int column) {
    Dispatch selection = Dispatch.get(word, "Selection").toDispatch(); // 输入内容需要的对象 
    Dispatch.call(selection, "TypeText", tableTitle); // 写入标题内容 // 标题格行 
    Dispatch.call(selection, "TypeParagraph"); // 空一行段落 
    Dispatch.call(selection, "TypeParagraph"); // 空一行段落 
    Dispatch.call(selection, "MoveDown"); // 游标往下一行 
    // 建立表格 
    Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
    // int count = Dispatch.get(tables, 
    // "Count").changeType(Variant.VariantInt).getInt(); // document中的表格数量 
    // Dispatch table = Dispatch.call(tables, "Item", new Variant( 
    // 1)).toDispatch();//文档中第一个表格 
    Dispatch range = Dispatch.get(selection, "Range").toDispatch();// /当前光标位置或者选中的区域 
    Dispatch newTable = Dispatch.call(tables, "Add", range, new Variant(row), new Variant(column), new Variant(1)).toDispatch(); // 设置row,column,表格外框宽度 
    Dispatch cols = Dispatch.get(newTable, "Columns").toDispatch(); // 此表的所有列， 
    int colCount = Dispatch.get(cols, "Count").getInt();
    //.changeType(Variant.VariantInt).getInt();// 一共有多少列 实际上这个数==column  
    System.out.println(colCount + "列");
    for (int i = 1; i <= colCount; i++) { // 循环取出每一列 
      Dispatch col = Dispatch.call(cols, "Item", new Variant(i)).toDispatch();
      Dispatch cells = Dispatch.get(col, "Cells").toDispatch();// 当前列中单元格 
      int cellCount = Dispatch.get(cells, "Count").getInt();// 当前列中单元格数 实际上这个数等于row

      for (int j = 1; j <= cellCount; j++) {// 每一列中的单元格数 
        // Dispatch cell = Dispatch.call(cells, "Item", new 
        // Variant(j)).toDispatch(); //当前单元格 
        // Dispatch cell = Dispatch.call(newTable, "Cell", new 
        // Variant(j) , new Variant(i) ).toDispatch(); //取单元格的另一种方法 
        // Dispatch.call(cell, "Select");//选中当前单元格 
        // Dispatch.put(selection, "Text", 
        // "第"+j+"行，第"+i+"列");//往选中的区域中填值，也就是往当前单元格填值 
        putTxtToCell(newTable, j, i, "第" + j + "行，第" + i + "列");// 与上面四句的作用相同 
      }
    }
  }

  /**
   * 在指定的单元格里填写数据
   *
   * @param tableIndex
   * @param cellRowIdx
   * @param cellColIdx
   * @param txt
   */
  public void putTxtToCell(Dispatch table, int cellRowIdx, int cellColIdx, String txt) {
    Dispatch cell = Dispatch.call(table, "Cell", new Variant(cellRowIdx), new Variant(cellColIdx)).toDispatch();
    Dispatch.call(cell, "Select");
    Dispatch selection = Dispatch.get(word, "Selection").toDispatch(); // 输入内容需要的对象 
    Dispatch.put(selection, "Text", txt);
  }

  public static void main(String[] args) {
    try {
      WordBean wordutil = new WordBean();
      //wordutil.openDocumentOnlyRead("c:/a.docx", "123");
      //wordutil.openDocument("c:/a.docx");
      wordutil.openFile("d:/b.docx");
      /*Dispatch selection = Dispatch.get(wordutil.word, "Selection").toDispatch(); // 输入内容需要的对象 
      wordutil.replaceText("453", "2",selection);
      wordutil.replaceText("44", "e",selection);
      wordutil.replaceText("ee", "1",selection);*/
      //wordutil.addRow(6);
      String[] strs = { "1", "2", "中华", "2", "3", "3" };
      wordutil.addRowData(6, 6, null, strs);
      //wordutil.replaceAllText("1234", " 你好");
      //wordutil.createTable();
      //wordutil.putTxtToCell(6,2,1,"sss");
      //wordutil.copyRow(6, 2);
      wordutil.closeDocument();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}