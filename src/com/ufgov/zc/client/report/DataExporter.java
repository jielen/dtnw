package com.ufgov.zc.client.report;import java.awt.Desktop;import java.io.File;import java.text.DateFormat;import java.text.SimpleDateFormat;import java.util.ArrayList;import java.util.Arrays;import java.util.Date;import java.util.List;import javax.swing.JComponent;import javax.swing.JFileChooser;import javax.swing.JOptionPane;import javax.swing.SwingUtilities;import javax.swing.filechooser.FileFilter;import org.apache.log4j.Logger;import com.ufgov.smartclient.component.table.JGroupableTable;import com.ufgov.smartclient.component.table.RowFilter;public class DataExporter {  /**  * Logger for this class  */  private static final Logger logger = Logger.getLogger(DataExporter.class);  public static void exportToExcel(final JComponent parent, final JGroupableTable table) {    exportToExcel(parent, table, null);  }  public static void exportToExcel(final JComponent parent, final JGroupableTable table, Integer[] selectedRows) {    List list = new ArrayList();    if (selectedRows != null) {      list = Arrays.asList(selectedRows);    }    final List selectedRowsList = list;    JFileChooser fileChooser = new JFileChooser() {      private boolean approveConfirm(File file) {        if (file.exists()) {          if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(parent, file          .getPath()          + "已经存在\n您要覆盖原始文件吗？", "提示", JOptionPane.YES_NO_OPTION)) {            return true;          } else {            return false;          }        } else {          return true;        }      }      public void approveSelection() {        if (!approveConfirm(this.getSelectedFile())) {          return;        }        super.approveSelection();      }    };    DateFormat ssFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");    String defaultFileName = "C:/" + ssFormat.format(new Date()) + ".xls";//默认的导出文件名    File defaultFile = new File(defaultFileName);    fileChooser.setSelectedFile(defaultFile);    fileChooser.setDialogTitle("导出excel");    fileChooser.setMultiSelectionEnabled(false);    fileChooser.setFileFilter(new FileFilter() {      public boolean accept(File f) {        return f.getAbsolutePath().toLowerCase().endsWith(".xls");      }      public String getDescription() {        return "Excel文档(*.xls)";      }    });    if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {      final File selectedFile = fileChooser.getSelectedFile();      String selecteFileName = selectedFile.getPath();      if (!selecteFileName.toLowerCase().endsWith(".xls"))        selecteFileName += ".xls";      final String fileName = selecteFileName;      SwingUtilities.invokeLater(new Runnable() {        public void run() {          try {            if (selectedRowsList.size() == 0) {              table.export2Excel(fileName, null);            } else {              table.export2Excel(fileName, null, new RowFilter() {                public boolean accept(int row, JGroupableTable table) {                  return selectedRowsList.contains(row);                }              });            }            int yesNoResult = JOptionPane.showConfirmDialog(parent, "文件已被成功保存到"            + selectedFile.getPath() + "\n您要打开此文件吗？", "提示",            JOptionPane.YES_NO_OPTION);            if (yesNoResult == JOptionPane.YES_OPTION) {              try {                Desktop.getDesktop().open(selectedFile);              } catch (Exception e) {                JOptionPane.showMessageDialog(parent, "抱歉！没有找到合适的程序来打开文件！", "提示",                JOptionPane.INFORMATION_MESSAGE);                return;              }            }          } catch (Exception e) {            logger.error(e);            JOptionPane.showMessageDialog(parent, "导出失败！", "错误",            JOptionPane.ERROR_MESSAGE);          }        }      });    }  }}