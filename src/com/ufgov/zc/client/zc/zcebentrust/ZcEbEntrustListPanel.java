package com.ufgov.zc.client.zc.zcebentrust;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableModel;

import jxl.Workbook;
import jxl.write.Alignment;
import jxl.write.Border;
import jxl.write.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.ufgov.smartclient.common.DefaultInvokeHandler;
import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.JGroupableTable;
import com.ufgov.smartclient.component.table.TableRowHeader.CheckedEvent;
import com.ufgov.smartclient.component.table.TableRowHeader.CheckedListener;
import com.ufgov.smartclient.plaf.BlueLookAndFeel;
import com.ufgov.zc.client.common.BillElementMeta;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.MyTableModel;
import com.ufgov.zc.client.common.ParentWindowAware;
import com.ufgov.zc.client.common.ServiceFactory;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.ZcEbEntrustToTableModelConverter;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.button.AcceptedButton;
import com.ufgov.zc.client.component.button.AddButton;
import com.ufgov.zc.client.component.button.AuditPassButton;
import com.ufgov.zc.client.component.button.BackButton;
import com.ufgov.zc.client.component.button.DeleteButton;
import com.ufgov.zc.client.component.button.EditButton;
import com.ufgov.zc.client.component.button.ExportButton;
import com.ufgov.zc.client.component.button.FuncButton;
import com.ufgov.zc.client.component.button.PrintButton;
import com.ufgov.zc.client.component.button.PrintPreviewButton;
import com.ufgov.zc.client.component.button.PrintSettingButton;
import com.ufgov.zc.client.component.button.SendBillButton;
import com.ufgov.zc.client.component.button.SendButton;
import com.ufgov.zc.client.component.button.TraceButton;
import com.ufgov.zc.client.component.button.UnauditButton;
import com.ufgov.zc.client.component.button.UntreadButton;
import com.ufgov.zc.client.component.event.ValueChangeEvent;
import com.ufgov.zc.client.component.event.ValueChangeListener;
import com.ufgov.zc.client.component.ui.AbstractDataDisplay;
import com.ufgov.zc.client.component.ui.AbstractEditListBill;
import com.ufgov.zc.client.component.ui.AbstractSearchConditionArea;
import com.ufgov.zc.client.component.ui.MultiDataDisplay;
import com.ufgov.zc.client.component.ui.SaveableSearchConditionArea;
import com.ufgov.zc.client.component.ui.SummarizedResultPanel;
import com.ufgov.zc.client.component.ui.TableDisplay;
import com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem;
import com.ufgov.zc.client.component.ui.conditionitem.SearchConditionUtil;
import com.ufgov.zc.client.print.PrintPreviewer;
import com.ufgov.zc.client.print.PrintSettingDialog;
import com.ufgov.zc.client.util.ListUtil;
import com.ufgov.zc.client.zc.ZcUtil;
import com.ufgov.zc.client.zc.flowconsole.DataFlowConsoleCanvas;
import com.ufgov.zc.common.commonbiz.model.SearchCondition;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.constants.WFConstants;
import com.ufgov.zc.common.system.constants.ZcElementConstants;
import com.ufgov.zc.common.system.constants.ZcSettingConstants;
import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.system.dto.PrintObject;
import com.ufgov.zc.common.system.exception.BaseException;
import com.ufgov.zc.common.system.exception.OtherException;
import com.ufgov.zc.common.system.util.ObjectUtil;
import com.ufgov.zc.common.zc.model.ZcEbEntrust;
import com.ufgov.zc.common.zc.publish.IZcEbEntrustServiceDelegate;

public class ZcEbEntrustListPanel extends AbstractEditListBill implements ParentWindowAware {

  private static final Logger logger = Logger.getLogger(ZcEbEntrustListPanel.class);

  private final ZcEbEntrustListPanel self = this;

  private Window parentWindow;

  private final String compoId = "ZC_EB_ENTRUST";

  private final RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();

  private final ElementConditionDto elementConditionDto = new ElementConditionDto();

  private final BillElementMeta billElementMeta = BillElementMeta.getBillElementMetaWithoutNd(compoId);

  public BillElementMeta getBillElementMeta() {
    return billElementMeta;
  }

  public IZcEbEntrustServiceDelegate zcEbEntrustServiceDelegate = (IZcEbEntrustServiceDelegate) ServiceFactory.create(
    IZcEbEntrustServiceDelegate.class, "zcEbEntrustServiceDelegate");

  public Window getParentWindow() {
    return parentWindow;
  }

  public void setParentWindow(Window parentWindow) {
    this.parentWindow = parentWindow;
  }

  private final class DataDisplay extends MultiDataDisplay {

    private DataDisplay(List<TableDisplay> displays, List<TableDisplay> showingDisplays, AbstractSearchConditionArea conditionArea,
      boolean showConditionArea) {
      super(displays, showingDisplays, conditionArea, showConditionArea, ZcSettingConstants.TAB_ID_ZC_EB_ENTRUST);
      setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "采购任务单", TitledBorder.CENTER, TitledBorder.TOP, new Font("宋体",
        Font.BOLD, 15), Color.BLUE));
    }

    @Override
    protected void preprocessShowingTableDisplay(List<TableDisplay> showingTableDisplays) {
      for (final TableDisplay d : showingTableDisplays) {
        final JGroupableTable table = d.getTable();
        table.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
              String tabStatus = d.getStatus();
              JGroupableTable table = d.getTable();
              MyTableModel model = (MyTableModel) table.getModel();
              int row = table.getSelectedRow();

              List viewList = (List) ObjectUtil.deepCopy(ListUtil.convertToTableViewOrderList(model.getList(), table));

              new ZcEbEntrustEditDialog(self, viewList, row, tabStatus);
            }
          }

        });
        //合计信息

        List sumFields = new ArrayList();

        sumFields.add(LangTransMeta.translate(ZcElementConstants.FIELD_TRANS_ZC_MONEY_BI_SUM));

        d.setSummarizedResultPanel(new SummarizedResultPanel(d.getTable(), sumFields));

        final JGroupableTable jTable = d.getTable();

        jTable.getTableRowHeader().addCheckedListener(new CheckedListener() {

          public void checkedChanged(CheckedEvent e) {

            d.getSummarizedResultPanel().clearSummarizedResult();

            d.getSummarizedResultPanel().refreshSummarizedResult();

          }

        });

        d.addSorterValueChangeListener(new ValueChangeListener() {

          public void valueChanged(ValueChangeEvent e) {

            d.getSummarizedResultPanel().clearSummarizedResult();

            d.getSummarizedResultPanel().refreshSummarizedResult();

          }

        });
      }
    }

    @Override
    protected void handleTableDisplayActived(AbstractSearchConditionItem[] searchConditionItems, final TableDisplay tableDisplay) {
      elementConditionDto.setWfcompoId(compoId);
      elementConditionDto.setExecutor(WorkEnv.getInstance().getCurrUserId());
      elementConditionDto.setNd(WorkEnv.getInstance().getTransNd());
      elementConditionDto.setStatus(tableDisplay.getStatus());
      elementConditionDto.setZcText1(WorkEnv.getInstance().getRequestMeta().getSvCoCode());//wangwei add 2011-6-3 任务单接受代理机构数据后，加代理机构进行过滤
      for (AbstractSearchConditionItem item : searchConditionItems) {
        item.putToElementConditionDto(elementConditionDto);
      }

      final Container c = tableDisplay.getTable().getParent();

      UIUtilities.asyncInvoke(new DefaultInvokeHandler<TableModel>() {

        @Override
        public void before() {
          assert c != null;
          installLoadingComponent(c);
        }

        @Override
        public void after() {
          assert c != null;
          unInstallLoadingComponent(c);
          c.add(tableDisplay.getTable());
        }

        @Override
        public TableModel execute() throws Exception {
          return ZcEbEntrustToTableModelConverter.convertToTableModel(self.zcEbEntrustServiceDelegate
            .getZcEbEntrust(elementConditionDto, requestMeta));
        }

        @Override
        public void success(TableModel model) {
          tableDisplay.setTableModel(model);
          setButtonStatus();
          tableDisplay.getSummarizedResultPanel().clearSummarizedResult();
          tableDisplay.getSummarizedResultPanel().refreshSummarizedResult();
        }

      });

    }
  }

  static {
    LangTransMeta.init("ZC%");
  }

  /**
   * 构造函数
   */
  public ZcEbEntrustListPanel() {
    UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {
      @Override
      public List<SearchCondition> execute() throws Exception {
        List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonList(WorkEnv.getInstance()
          .getCurrUserId(), ZcSettingConstants.TAB_ID_ZC_EB_ENTRUST);
        return needDisplaySearchConditonList;
      }

      @Override
      public void success(List<SearchCondition> needDisplaySearchConditonList) {
        List<TableDisplay> showingDisplays = SearchConditionUtil.getNeedDisplayTableDisplay(needDisplaySearchConditonList);

        init(createDataDisplay(showingDisplays), null);//调用父类方法
        revalidate();
        repaint();
      }
    });
    requestMeta.setCompoId(compoId);

  }

  private AbstractSearchConditionArea topSearchConditionArea;

  private AbstractSearchConditionArea createTopConditionArea() {
    Map defaultValueMap = new HashMap();
    topSearchConditionArea = new SaveableSearchConditionArea(ZcSettingConstants.CONDITION_ID_ZC_EB_ENTRUST, null, false, defaultValueMap, null);

    return topSearchConditionArea;
  }

  private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {
    return new DataDisplay(SearchConditionUtil.getAllTableDisplay(ZcSettingConstants.TAB_ID_ZC_EB_ENTRUST), showingDisplays,
      createTopConditionArea(), true);//true:显示收索条件区 false：不显示收索条件区
  }

  //  private FuncButton sendBill = new CommonButton("fsendBill", "sendBill.png");

  private final AddButton addButton = new AddButton();

  private final EditButton editButton = new EditButton();

  private final DeleteButton deleteButton = new DeleteButton();

  private final FuncButton sendBill = new SendBillButton();

  private final FuncButton acceptedButton = new AcceptedButton();

  private final FuncButton backButton = new BackButton();

  private final FuncButton sendButton = new SendButton();

  private final FuncButton auditPassButton = new AuditPassButton(); //审核通过

  private final FuncButton unAuditButton = new UnauditButton(); // 销审

  private final FuncButton unTreadButton = new UntreadButton(); // 退回

  private final FuncButton printButton = new PrintButton();

  private final FuncButton printPreviewButton = new PrintPreviewButton();

  private final PrintSettingButton printSettingButton = new PrintSettingButton();

  private final FuncButton traceButton = new TraceButton();

  //private HelpButton helpButton = new HelpButton();

  //add shijia 20111210 导出Excel文件
  public ExportButton exportButton = new ExportButton();

  @Override
  protected void addToolBarComponent(JFuncToolBar toolBar) {
    toolBar.setModuleCode("ZC");
    toolBar.setCompoId(compoId);
    toolBar.add(addButton);
    //    toolBar.add(editButton);
    //    toolBar.add(deleteButton);
    //    toolBar.add(sendBill);
    //    toolBar.add(acceptedButton);
    //    toolBar.add(backButton);
    //    toolBar.add(sendButton);
    //    toolBar.add(auditPassButton);
    //    toolBar.add(unAuditButton);
    //    toolBar.add(unTreadButton);
    //    toolBar.add(traceButton);
    toolBar.add(printButton);
    //    toolBar.add(printPreviewButton);
    //    toolBar.add(printSettingButton);
    toolBar.add(exportButton);//add shijia 20111210 导出Excel
    toolBar.add(helpButton);
    toolBar.add(traceDataButton);
    // 初始化按钮的action事件
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doAdd();
      }
    });
    editButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doEdit();
      }
    });
    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doDelete();
      }
    });

    sendBill.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doSendBill();
      }
    });

    acceptedButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doAccept();
      }
    });

    backButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doBack();
      }
    });

    sendButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doSend();
      }
    });

    auditPassButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doAudit();
      }

    });

    unAuditButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doUnaudit();

      }

    });

    unTreadButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doUntread();
      }

    });

    traceButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        doTrace();
      }
    });

    printButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        doPrint();
      }
    });
    printPreviewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        doPrintPreview();
      }
    });
    printSettingButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        doPrintSetting();
      }
    });

    exportButton.addActionListener(new ActionListener() { //add shijia 20111210
        public void actionPerformed(ActionEvent e) {
          doExport();
        }
      });
    traceDataButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        doTraceDataButton();
      }
    });
  }

  protected void doDelete() {
    // TODO Auto-generated method stub

  }

  protected void doEdit() {
    // TODO Auto-generated method stub

  }

  protected void doAdd() {
    // TODO Auto-generated method stub
    new ZcEbEntrustEditDialog(self, new ArrayList(), this.topDataDisplay.getActiveTableDisplay().getTable().getRowCount(), topDataDisplay
      .getActiveTableDisplay().getStatus());

  }

  public void refreshCurrentTabData() {
    topSearchConditionArea.doSearch();
  }

  public List getCheckedList() {
    List<ZcEbEntrust> beanList = new ArrayList<ZcEbEntrust>();
    JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();
    MyTableModel model = (MyTableModel) table.getModel();
    //Modal的数据
    List list = model.getList();
    Integer[] checkedRows = table.getCheckedRows();
    for (Integer checkedRow : checkedRows) {
      int accordDataRow = table.convertRowIndexToModel(checkedRow);
      ZcEbEntrust bean = (ZcEbEntrust) list.get(accordDataRow);
      beanList.add(bean);
    }
    return beanList;
  }

  private void doSendBill() {
    List beanList = getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    for (Object o : beanList) {
      ZcEbEntrust entrust = (ZcEbEntrust) o;
      entrust.setStatus(ZcEbEntrust.STATUS_WAIT_ACCEPT);
    }
    boolean success = true;
    try {
      requestMeta.setFuncId(this.sendBill.getFuncId());
      for (Object o : beanList) {
        ZcEbEntrust entrust = (ZcEbEntrust) o;
        this.zcEbEntrustServiceDelegate.saveFN(entrust, requestMeta);
      }
    } catch (BaseException ex) {
      success = false;
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    } catch (OtherException ex) {
      success = false;
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    } catch (Exception ex) {
      success = false;
      logger.error(ex.getMessage(), ex);
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "处理成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      refreshCurrentTabData();
    }
  }

  private void doAccept() {
    List beanList = getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    for (Object o : beanList) {
      ZcEbEntrust entrust = (ZcEbEntrust) o;
      entrust.setStatus(ZcEbEntrust.STATUS_ACCEPTED);
    }
    boolean success = true;
    try {
      requestMeta.setFuncId(acceptedButton.getFuncId());
      for (Object o : beanList) {
        ZcEbEntrust entrust = (ZcEbEntrust) o;
        entrust.setStatus(ZcEbEntrust.STATUS_ACCEPTED);
        this.zcEbEntrustServiceDelegate.saveFN(entrust, requestMeta);
      }
    } catch (BaseException ex) {
      success = false;
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    } catch (OtherException ex) {
      success = false;
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    } catch (Exception ex) {
      success = false;
      logger.error(ex.getMessage(), ex);
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "处理成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      refreshCurrentTabData();
    }
  }

  private void doBack() {
    List beanList = getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    for (Object o : beanList) {
      ZcEbEntrust entrust = (ZcEbEntrust) o;
      entrust.setStatus(ZcEbEntrust.STATUS_UNACCEPT);
    }
    boolean success = true;
    try {
      requestMeta.setFuncId(backButton.getFuncId());
      for (Object o : beanList) {
        ZcEbEntrust entrust = (ZcEbEntrust) o;
        this.zcEbEntrustServiceDelegate.saveFN(entrust, requestMeta);
      }
    } catch (BaseException ex) {
      success = false;
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    } catch (OtherException ex) {
      success = false;
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    } catch (Exception ex) {
      success = false;
      logger.error(ex.getMessage(), ex);
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "处理成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      refreshCurrentTabData();
    }

  }

  private void doSend() {
    List beanList = getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    boolean success = true;
    try {
      requestMeta.setFuncId(sendButton.getFuncId());
      this.zcEbEntrustServiceDelegate.newCommitFN(beanList, requestMeta);
    } catch (BaseException ex) {
      success = false;
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    } catch (OtherException ex) {
      success = false;
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    } catch (Exception ex) {
      success = false;
      logger.error(ex.getMessage(), ex);
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "处理成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      refreshCurrentTabData();
    }
  }

  private void doUnaudit() {
    List beanList = getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    boolean success = true;
    try {
      requestMeta.setFuncId(unAuditButton.getFuncId());
      this.zcEbEntrustServiceDelegate.unauditFN(beanList, requestMeta);
    } catch (BaseException ex) {
      success = false;
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    } catch (OtherException ex) {
      success = false;
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    } catch (Exception ex) {
      success = false;
      logger.error(ex.getMessage(), ex);
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "处理成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      refreshCurrentTabData();
    }
  }

  private void doAudit() {
    List beanList = getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    boolean success = true;
    try {
      requestMeta.setFuncId(this.auditPassButton.getFuncId());
      this.zcEbEntrustServiceDelegate.auditFN(beanList, requestMeta);
    } catch (BaseException ex) {
      success = false;
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    } catch (OtherException ex) {
      success = false;
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    } catch (Exception ex) {
      success = false;
      logger.error(ex.getMessage(), ex);
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "处理成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      refreshCurrentTabData();
    }

  }

  private void doUntread() {
    List beanList = getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    boolean success = true;
    try {
      requestMeta.setFuncId(this.unTreadButton.getFuncId());
      this.zcEbEntrustServiceDelegate.untreadFN(beanList, requestMeta);
    } catch (BaseException ex) {
      success = false;
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    } catch (OtherException ex) {
      success = false;
      logger.error(ex.getStackTraceMessage(), ex);
      success = false;
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    } catch (Exception ex) {
      success = false;
      logger.error(ex.getMessage(), ex);
      UIUtilities.showStaickTraceDialog(ex, this, "错误", ex.getMessage());
    }
    if (success) {
      JOptionPane.showMessageDialog(this, "处理成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
      refreshCurrentTabData();
    }

  }

  private void doTrace() {
    List beanList = getCheckedList();
    ZcUtil.showTraceDialog(beanList, this);
  }

  private void doPrint() {
    List beanList = getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    } else {
      if (beanList.size() > 1) {
        JOptionPane.showMessageDialog(this, "只能选择一条数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
    }

    this.requestMeta.setFuncId(this.printButton.getFuncId());

    this.requestMeta.setPageType(this.compoId + "_L");

    try {

      ZcEbEntrust entrust = (ZcEbEntrust) beanList.get(0);
      String condition = "ENTRUST_CODE='" + entrust.getSn() + "'";

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      JOptionPane.showMessageDialog(this, "打印出错！\n" + e.getMessage(), "错误", 0);

    }
  }

  private void doPrintPreview() {
    final List printList = getCheckedList();
    if (printList.size() == 0) {
      JOptionPane.showMessageDialog(this, "请选择需要打印预览的数据 ！", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    requestMeta.setFuncId(this.printPreviewButton.getFuncId());
    requestMeta.setPageType(this.compoId + "_L");
    try {
      PrintObject printObject = this.zcEbEntrustServiceDelegate.genMainSubPrintObjectFN(printList, requestMeta);
      PrintPreviewer previewer = new PrintPreviewer(printObject) {
        @Override
        protected void afterSuccessPrint() {
        }
      };

      previewer.preview();

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      JOptionPane.showMessageDialog(this, "打印预览出错！\n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
    }

  }

  private void doPrintSetting() {
    requestMeta.setFuncId(this.printSettingButton.getFuncId());
    requestMeta.setPageType(this.compoId + "_L");
    new PrintSettingDialog(requestMeta);
  }

  private void setButtonStatus() {
    String panelId = WFConstants.AUDIT_TAB_STATUS_TODO;
    if (topDataDisplay != null && topDataDisplay.getActiveTableDisplay() != null) {
      panelId = topDataDisplay.getActiveTableDisplay().getStatus();
    }

    if (WFConstants.EDIT_TAB_STATUS_DRAFT.equalsIgnoreCase(panelId)) {
      addButton.setVisible(true);
      auditPassButton.setVisible(false);
      unAuditButton.setVisible(false);
      unTreadButton.setVisible(true);
      acceptedButton.setVisible(false);
      backButton.setVisible(false);
      traceButton.setVisible(false);
      sendButton.setVisible(false);
      sendBill.setVisible(false);
      exportButton.setVisible(false);
    } else if (WFConstants.AUDIT_TAB_STATUS_TODO.equalsIgnoreCase(panelId)) {
      addButton.setVisible(false);
      auditPassButton.setVisible(true);
      unAuditButton.setVisible(false);
      unTreadButton.setVisible(true);
      acceptedButton.setVisible(false);
      backButton.setVisible(false);
      traceButton.setVisible(true);
      sendButton.setVisible(false);
      sendBill.setVisible(false);
      exportButton.setVisible(true);
    } else if ("accepted".equalsIgnoreCase(panelId) || "waitingaccepted".equalsIgnoreCase(panelId)) {
      addButton.setVisible(false);
      auditPassButton.setVisible(false);
      unAuditButton.setVisible(false);
      unTreadButton.setVisible(false);
      acceptedButton.setVisible(false);
      backButton.setVisible(false);
      sendButton.setVisible(false);
      sendBill.setVisible(false);
      exportButton.setVisible(false);
    } else if (WFConstants.EDIT_TAB_STATUS_ACCEPTED.equalsIgnoreCase(panelId)) {
      addButton.setVisible(false);
      auditPassButton.setVisible(false);
      unAuditButton.setVisible(false);
      unTreadButton.setVisible(false);
      acceptedButton.setVisible(false);
      backButton.setVisible(false);
      sendButton.setVisible(true);
      sendBill.setVisible(false);
      exportButton.setVisible(true);
    } else if ("send".equalsIgnoreCase(panelId)) {
      addButton.setVisible(false);
      sendBill.setVisible(true);
      auditPassButton.setVisible(false);
      unAuditButton.setVisible(false);
      unTreadButton.setVisible(false);
      acceptedButton.setVisible(false);
      traceButton.setVisible(false);
      backButton.setVisible(false);
      sendButton.setVisible(false);
      exportButton.setVisible(true);
    } else if (WFConstants.EDIT_TAB_STATUS_UNACCEPTED.equalsIgnoreCase(panelId)) {
      addButton.setVisible(false);
      auditPassButton.setVisible(false);
      unAuditButton.setVisible(false);
      unTreadButton.setVisible(false);
      acceptedButton.setVisible(true);
      traceButton.setVisible(false);
      backButton.setVisible(true);
      sendButton.setVisible(false);
      sendBill.setVisible(false);
      exportButton.setVisible(true);
    } else if (WFConstants.AUDIT_TAB_STATUS_DONE.equalsIgnoreCase(panelId)) {
      addButton.setVisible(false);
      auditPassButton.setVisible(false);
      unAuditButton.setVisible(true);
      unTreadButton.setVisible(false);
      acceptedButton.setVisible(false);
      traceButton.setVisible(true);
      backButton.setVisible(false);
      sendButton.setVisible(false);
      sendBill.setVisible(false);
      exportButton.setVisible(true);
    } else if (WFConstants.AUDIT_TAB_STATUS_ALL.equalsIgnoreCase(panelId)) {
      addButton.setVisible(false);
      auditPassButton.setVisible(false);
      unAuditButton.setVisible(false);
      unTreadButton.setVisible(false);
      acceptedButton.setVisible(false);
      traceButton.setVisible(true);
      backButton.setVisible(false);
      sendButton.setVisible(false);
      sendBill.setVisible(false);
      exportButton.setVisible(true);
    } else if (WFConstants.AUDIT_TAB_STATUS_AUDITED.equalsIgnoreCase(panelId)) {
      addButton.setVisible(false);
      auditPassButton.setVisible(false);
      unAuditButton.setVisible(false);
      unTreadButton.setVisible(false);
      acceptedButton.setVisible(true);
      traceButton.setVisible(true);
      backButton.setVisible(false);
      sendButton.setVisible(false);
      sendBill.setVisible(false);
      exportButton.setVisible(true);
    } else if (WFConstants.AUDIT_TAB_STATUS_CANCEL.equalsIgnoreCase(panelId)) {
      addButton.setVisible(false);
      auditPassButton.setVisible(false);
      unAuditButton.setVisible(false);
      unTreadButton.setVisible(false);
      acceptedButton.setVisible(false);
      traceButton.setVisible(false);
      backButton.setVisible(false);
      sendButton.setVisible(false);
      sendBill.setVisible(false);
      exportButton.setVisible(false);
    }

  }

  private void doTraceDataButton() {
    List beanList = getCheckedList();
    if (beanList.size() == 0) {
      JOptionPane.showMessageDialog(this, "请选择一条要进行跟踪的数据！", "错误", JOptionPane.ERROR_MESSAGE);
      return;
    }
    ZcEbEntrust sh = (ZcEbEntrust) beanList.get(0);
    DataFlowConsoleCanvas dfc = new DataFlowConsoleCanvas(sh.getZcMakeCode(), this.compoId);
    dfc.showWindow();
  }

  public static void main(String[] args) throws Exception {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          UIManager.setLookAndFeel(new BlueLookAndFeel());
        } catch (Exception e) {
          e.printStackTrace();
        }
        //        UIManager.getDefaults().put("SplitPaneUI", BigButtonSplitPaneUI.class.getName());
        ZcEbEntrustListPanel bill = new ZcEbEntrustListPanel();
        JFrame frame = new JFrame("frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(bill);
        frame.setVisible(true);
      }
    });

  }

  public String getMakeCodeArray(List beanlist) {// add shijia 20111210 导出EXCEL
    String code_array = "";
    for (int i = 0; i < beanlist.size(); i++) {

      ZcEbEntrust ZcEb = (ZcEbEntrust) beanlist.get(i);
      String makeCode = ZcEb.getZcMakeCode();
      if (i == beanlist.size() - 1)
        code_array = code_array + makeCode;
      else
        code_array = code_array + makeCode + "@";
    }
    return code_array;
  }

  // add shijia 20111210 导出EXCEL
  public void doExport() {
    try {
      List printList = getCheckedList();
      if (printList.size() == 0) {
        JOptionPane.showMessageDialog(this, "没有选择数据！", " 提示", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      String code_array = getMakeCodeArray(printList);
      FileSystemView fsv = FileSystemView.getFileSystemView();
      File file = fsv.getHomeDirectory();
      String filePath = file.getAbsolutePath();
      Calendar cale = Calendar.getInstance();
      String sysDate = cale.get(Calendar.YEAR) + "年" + cale.get(Calendar.MONTH) + "月" + cale.get(Calendar.DATE) + "日";
      String fileName = filePath + "\\采购任务单导出-" + sysDate + ".xls";
      WritableWorkbook book = Workbook.createWorkbook(new File(fileName));
      WritableSheet sheet = book.createSheet("采购任务单导出", 0);
      String[] zcMakeCodeArr;
      if (code_array.indexOf("@") != -1) {
        zcMakeCodeArr = code_array.split("@");
      } else {
        zcMakeCodeArr = new String[1];
        zcMakeCodeArr[0] = code_array;
      }
      Double moneySum = 0d;

      JGroupableTable table = topDataDisplay.getActiveTableDisplay().getTable();
      MyTableModel model = (MyTableModel) table.getModel();

      for (int i = 0; i < zcMakeCodeArr.length; i++) {
        String zcMakeCode = zcMakeCodeArr[i];
        int j = -1;
        //在Label对象的构造函数中指名是第一行第一列
        jxl.write.WritableCellFormat txtFmt = new jxl.write.WritableCellFormat();
        jxl.write.WritableCellFormat numFmt = new jxl.write.WritableCellFormat();
        jxl.write.WritableCellFormat titleFmt = new jxl.write.WritableCellFormat();
        //jxl.write.WritableCellFormat title2Fmt = new jxl.write.WritableCellFormat();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        DecimalFormat df = new DecimalFormat("#0.00");
        txtFmt.setBorder(Border.ALL, BorderLineStyle.THIN);
        txtFmt.setAlignment(Alignment.LEFT);
        numFmt.setBorder(Border.ALL, BorderLineStyle.THIN);
        numFmt.setAlignment(Alignment.RIGHT);
        //titleFmt.setAlignment(Alignment.CENTRE);
        WritableFont fontFmt1 = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD, true);
        WritableFont fontFmt = new WritableFont(WritableFont.ARIAL, 20, WritableFont.BOLD, true);
        WritableCellFormat font = new WritableCellFormat(fontFmt);
        WritableCellFormat font1 = new WritableCellFormat(fontFmt1);
        font.setAlignment(Alignment.CENTRE);
        font1.setBorder(Border.ALL, BorderLineStyle.THIN);
        sheet.mergeCells(0, 0, 20, 0);
        Label title = new Label(0, 0, "采购任务单导出数据", font);

        for (int x = 0; x < model.getRowCount(); x++) {//定位编号第几条
          String ls_temp = model.getValueAt(x, 1).toString();
          if (ls_temp.equals(zcMakeCode)) {
            j = x;
            break;
          }
        }

        String ls_type = "";
        for (int x = 0; x < model.getColumnCount(); x++) {
          Label label1 = null, title1 = null;
          String colName = model.getColumnName(x);
          title1 = new Label(x, 1, colName, font1);
          Object value = model.getValueAt(j, x);
          if (!"".equals(value) && value != null) {
            ls_type = model.getValueAt(i, x).getClass().getName();
          } else {
            ls_type = "java.lang.String";
          }
          if (ls_type.equals("java.lang.String")) {
            label1 = new Label(x, i + 2, value != null ? value.toString() : "", txtFmt);
          } else if (ls_type.equals("java.util.Date")) {
            label1 = new Label(x, i + 2, sdf.format((model.getValueAt(j, x))), txtFmt);
          } else if (ls_type.equals("java.math.BigDecimal")) {
            label1 = new Label(x, i + 2, df.format(model.getValueAt(j, x)), numFmt);
            BigDecimal lb_tmp = (BigDecimal) model.getValueAt(j, x);
            moneySum += lb_tmp.doubleValue();
          }
          sheet.addCell(title1);
          sheet.addCell(label1);
        }

        Label count1 = new Label(0, i + 3, "合计: ", txtFmt);
        Label count2 = new Label(1, i + 3, "共导出 " + zcMakeCodeArr.length + "条数据", txtFmt);
        Label count3 = new Label(2, i + 3, "采购预算: ", txtFmt);
        Label count4 = new Label(3, i + 3, df.format(moneySum), numFmt);
        Label count5 = new Label(4, i + 3, "元", txtFmt);

        sheet.addCell(title);

        sheet.addCell(count1);
        sheet.addCell(count2);
        sheet.addCell(count3);
        sheet.addCell(count4);
        sheet.addCell(count5);
      }
      for (int i = 0; i < sheet.getRows(); i++) {
        sheet.setRowView(i, 500);
      }
      book.write();
      book.close();
      JOptionPane.showMessageDialog(this, "【采购任务单】已导出至桌面,导出成功！");

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      JOptionPane.showMessageDialog(this, "导出【采购任务单】出错！\n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
    }
  }
}
