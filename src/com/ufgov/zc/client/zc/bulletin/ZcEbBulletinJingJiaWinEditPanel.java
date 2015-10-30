package com.ufgov.zc.client.zc.bulletin;import java.util.List;import com.ufgov.zc.client.common.ListCursor;import com.ufgov.zc.client.common.ServiceFactory;import com.ufgov.zc.common.zc.ZcEbBulletinConstants;import com.ufgov.zc.common.zc.model.ZcEbBulletin;import com.ufgov.zc.common.zc.model.ZcEbPack;import com.ufgov.zc.common.zc.publish.IZcPProMakeServiceDelegate;public class ZcEbBulletinJingJiaWinEditPanel extends AbstractZcEbBulletinEditPanel {  public IZcPProMakeServiceDelegate ZcPProMakeServiceDelegate = (IZcPProMakeServiceDelegate) ServiceFactory.create(IZcPProMakeServiceDelegate.class,  "zcPProMakeServiceDelegate");  public ZcEbBulletinJingJiaWinEditPanel(ZcEbBulletinJingJiaWinEditDialog parent, ListCursor listCursor, String tabStatus,  ZcEbBulletinJingJiaWinListPanel listPanel) {    super(parent, listCursor, tabStatus, listPanel, ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_JING_JIA_WID);  }  public String getSqlMapSelectedProj() {    return "ZcEbProj.getZcEbMakeForBulletinWin";  }  @Override  protected String getBulletinType() {    // TODO Auto-generated method stub    return ZcEbBulletinConstants.TYPE_BULLETIN_JING_JIA_WID;  }  public String getSqlMapSelectedMold() {    return "ZcEbBulletinWordMold.getZcEbBulletinWordMoldJingJiaWid";  }  protected String getCompId() {    // TODO Auto-generated method stub    return ZcEbBulletinConstants.COMPO_ZC_EB_BULLETIN_JING_JIA_WID;  }  public String fetchSn(ZcEbBulletin sheet) {    List<ZcEbPack> pack = zcEbBaseServiceDelegate.queryDataForList("ZcEbProj.getZcEbPackListByProjCode", sheet.getProjCode(), requestMeta);    return pack.get(0).getEntrustCode();  }  @Override  protected String getModelName() {    // TODO Auto-generated method stub    return ZcEbBulletinConstants.TITLE_ZC_EB_BULLETIN_JING_JIA_WID;  }}