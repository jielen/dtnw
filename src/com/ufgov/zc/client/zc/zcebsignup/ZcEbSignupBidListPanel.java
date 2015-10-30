package com.ufgov.zc.client.zc.zcebsignup;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import com.ufgov.smartclient.common.DefaultInvokeHandler;
import com.ufgov.smartclient.common.UIUtilities;
import com.ufgov.smartclient.component.table.JGroupableTable;
import com.ufgov.smartclient.plaf.BlueLookAndFeel;
import com.ufgov.zc.client.common.LangTransMeta;
import com.ufgov.zc.client.common.ParentWindowAware;
import com.ufgov.zc.client.common.WorkEnv;
import com.ufgov.zc.client.common.converter.ZcEbSignupToTableModelConverter;
import com.ufgov.zc.client.common.converter.zc.ZcEbProjectToTableModelConverter;
import com.ufgov.zc.client.component.JFuncToolBar;
import com.ufgov.zc.client.component.ui.AbstractDataDisplay;
import com.ufgov.zc.client.component.ui.AbstractEditListBill;
import com.ufgov.zc.client.component.ui.AbstractSearchConditionArea;
import com.ufgov.zc.client.component.ui.MultiDataDisplay;
import com.ufgov.zc.client.component.ui.SaveableSearchConditionArea;
import com.ufgov.zc.client.component.ui.TableDisplay;
import com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem;
import com.ufgov.zc.client.component.ui.conditionitem.SearchConditionUtil;
import com.ufgov.zc.client.zc.zcebsignup.operation.ZcEbSignupBidOpt;
import com.ufgov.zc.common.commonbiz.model.SearchCondition;
import com.ufgov.zc.common.system.RequestMeta;
import com.ufgov.zc.common.system.dto.ElementConditionDto;

/**

 * @ClassName: ZcEbSignupBidListPanel

 * @Description: 供应商投标信息

 * @date: Sep 18, 2012 9:45:29 AM

 * @version: V1.0

 * @since: 1.0

 * @author: yuzz

 * @modify:

 */
public class ZcEbSignupBidListPanel  extends AbstractEditListBill implements ParentWindowAware{

	public static final String compoId = "ZC_EB_SIGNUP_BID";
	
	public static final String tabId = "ZcEbSignupBid_Tab";

	private RequestMeta requestMeta = WorkEnv.getInstance().getRequestMeta();
	
	private ElementConditionDto elementConditionDto = new ElementConditionDto();

	private AbstractSearchConditionArea topSearchConditionArea;

	private Window parentWindow = null;
	
	public static String titile = "投标项目";
	
	//业务操作实例
	private ZcEbSignupBidOpt opt = new ZcEbSignupBidOpt();
	
	public ZcEbSignupBidListPanel(){
		UIUtilities.asyncInvoke(new DefaultInvokeHandler<List<SearchCondition>>() {
			@Override
			public List<SearchCondition> execute() throws Exception {
				List<SearchCondition> needDisplaySearchConditonList = SearchConditionUtil.getNeedDisplaySearchConditonList(WorkEnv.getInstance()
						.getCurrUserId(), tabId);
				return needDisplaySearchConditonList;
			}
			
			@Override
			public void success(List<SearchCondition> needDisplaySearchConditonList) {
				List<TableDisplay> showingDisplays = SearchConditionUtil.getNeedDisplayTableDisplay(needDisplaySearchConditonList);
				init(createDataDisplay(showingDisplays), null);
				revalidate();
				repaint();
			}
		});
		
		requestMeta.setCompoId(compoId);
		
	}
	
	public void refreshCurrentTabData() {
		
		topSearchConditionArea.doSearch();
		
	}
	
	private AbstractDataDisplay createDataDisplay(List<TableDisplay> showingDisplays) {
		
		return new DataDisplay(SearchConditionUtil.getAllTableDisplay(tabId), showingDisplays, null, false);//true:显示收索条件区 false：不显示收索条件区
	
	}
	
	private AbstractSearchConditionArea createTopConditionArea() {
		
		topSearchConditionArea = new SaveableSearchConditionArea(tabId, null, true, new HashMap<String, String>(), null);
		
		return topSearchConditionArea;
		
	}

	private final class DataDisplay extends MultiDataDisplay {
		
		private DataDisplay(List<TableDisplay> displays, 
				List<TableDisplay> showingDisplays, 
				AbstractSearchConditionArea conditionArea,
				boolean showConditionArea) {
			
			super(displays, showingDisplays, conditionArea, showConditionArea, ZcEbSignupBidListPanel.tabId);
			
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), 
					titile, 
					TitledBorder.CENTER, TitledBorder.TOP, 
					new Font("宋体", Font.BOLD, 15), 
					Color.BLUE));
		}
		
		protected void preprocessShowingTableDisplay(List<TableDisplay> showingTableDisplays) {
			for (final TableDisplay d : showingTableDisplays) {
				final JGroupableTable table = d.getTable();
				table.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
						}
					}
				});
			}
		}
		
		/**
		 * <p> 标签切换事件 </p>
		 * @param searchConditionItems
		 * @param tableDisplay
		 * @see com.ufgov.zc.client.component.ui.AbstractDataDisplay#handleTableDisplayActived(com.ufgov.zc.client.component.ui.conditionitem.AbstractSearchConditionItem[], com.ufgov.zc.client.component.ui.TableDisplay)
		 * @author yuzz
		 * @since Sep 17, 2012 1:52:12 PM
		 */
		protected void handleTableDisplayActived(AbstractSearchConditionItem[] searchConditionItems, final TableDisplay tableDisplay) {
			
			elementConditionDto.setWfcompoId(ZcEbSignupBidListPanel.compoId);
			elementConditionDto.setNd(WorkEnv.getInstance().getTransNd());
			elementConditionDto.setStatus(tableDisplay.getStatus());
			elementConditionDto.setManageCode(WorkEnv.getInstance().getCurrUserId());
			
			for (AbstractSearchConditionItem item : searchConditionItems) {
				item.putToElementConditionDto(elementConditionDto);
			}
			
			final Container c = tableDisplay.getTable().getParent();
			UIUtilities.asyncInvoke(new DefaultInvokeHandler<TableModel>() {
				public void before() {
					assert c != null;
					installLoadingComponent(c);
				}
				
				public void after() {
					assert c != null;
					unInstallLoadingComponent(c);
					c.add(tableDisplay.getTable());
				}
				
				public TableModel execute() throws Exception {
					if ("no".equals(elementConditionDto.getStatus())) {
						return fetchDataForProj();
					} else if("todo".equals(elementConditionDto.getStatus())){
						return fetchDataForSignup();
					} else if("all".equals(elementConditionDto.getStatus())){
						return fetchDataForSignup();
					} else if("bid".equals(elementConditionDto.getStatus())){
            return fetchDataForSignup();
          } else {
						return null;
					}
				}
				
				public void success(TableModel model) {
					
					tableDisplay.setTableModel(model);
					
				}
				
			});
			
		}
		
	}
	
	/**
	 * <p> 获取项目信息 </p>
	 * @return TableModel
	 * @author yuzz
	 * @since Sep 17, 2012 1:49:53 PM
	 */
	private TableModel fetchDataForProj(){
		List dataList = opt.getZcEbSignupProj(elementConditionDto, this.requestMeta);
		return ZcEbProjectToTableModelConverter.convertToTableModel(dataList);
	}
	
	/**
	 * <p> 获取报名信息 </p>
	 * @return TableModel
	 * @author yuzz
	 * @since Sep 17, 2012 1:50:27 PM
	 */
	private TableModel fetchDataForSignup(){
		List dataList = opt.getZcEbSignup(elementConditionDto, this.requestMeta);
		ZcEbSignupToTableModelConverter converter = new ZcEbSignupToTableModelConverter();
		return converter.convertGysToTableModel(dataList);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					UIManager.setLookAndFeel(new BlueLookAndFeel());
				} catch (Exception e) {
					e.printStackTrace();
				}
				ZcEbSignupBidListPanel bill = new ZcEbSignupBidListPanel();
				JFrame frame = new JFrame(titile);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(800, 600);
				frame.setLocationRelativeTo(null);
				frame.getContentPane().add(bill);
				frame.setVisible(true);
			}
		});
	}
	
	@Override
	protected void addToolBarComponent(JFuncToolBar toolBar) {
		toolBar.setModuleCode("ZC_DATA");
		toolBar.setCompoId(compoId);
		
		toolBar.add(helpButton);
	}
	
	public Window getParentWindow() {
		
		return parentWindow;
	}
	
	public void setParentWindow(Window parentWindow) {
		this.parentWindow = parentWindow;
	}
	
	static {
		LangTransMeta.init("ZC%");
	}
}
