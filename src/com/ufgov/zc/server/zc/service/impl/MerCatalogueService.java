package com.ufgov.zc.server.zc.service.impl;import java.util.List;import com.ufgov.zc.common.zc.model.ZcBMerchandise;import com.ufgov.zc.common.zc.model.ZcXyghZbjg;import com.ufgov.zc.server.zc.dao.IZcBCatalogueDao;import com.ufgov.zc.server.zc.service.IMerCatalogueService;public class MerCatalogueService implements IMerCatalogueService {  private IZcBCatalogueDao zcBCatalogueDao;  public IZcBCatalogueDao getZcBCatalogueDao() {    return zcBCatalogueDao;  }  public void setZcBCatalogueDao(IZcBCatalogueDao zcBCatalogueDao) {    this.zcBCatalogueDao = zcBCatalogueDao;  }  public List getCatalogueForMenuGoods() {    return zcBCatalogueDao.getCatalogueForMenuGoods();  }  public List getZcXyghZbjgList() {    return zcBCatalogueDao.getZcXyghZbjgList();  }  public List getCataloguePeopForID(String catalogueID) {    return zcBCatalogueDao.getCataloguePeopForID(catalogueID);  }  public List getCatalogueForNW() {    return zcBCatalogueDao.getCatalogueForNW();  }  public List getCatalogueForNW(String zcSuCode) {    return zcBCatalogueDao.getCatalogueForNW(zcSuCode);  }  public List getMerCataloguePeopValueForID(ZcBMerchandise zcBMerchandise) {    return zcBCatalogueDao.getMerCataloguePeopValueForID(zcBMerchandise);  }  public ZcXyghZbjg getZcXyghZbjgByID(String zcProjID) {    return zcBCatalogueDao.getZcXyghZbjgByID(zcProjID);  }}