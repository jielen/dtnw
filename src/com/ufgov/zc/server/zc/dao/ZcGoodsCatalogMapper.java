package com.ufgov.zc.server.zc.dao;

import com.ufgov.zc.common.zc.model.ZcGoodsCatalog;

public interface ZcGoodsCatalogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ZC_GOODS_CATALOG
     *
     * @mbggenerated Tue Jan 19 22:16:18 CST 2016
     */
    int deleteByPrimaryKey(String goodsCatalogCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ZC_GOODS_CATALOG
     *
     * @mbggenerated Tue Jan 19 22:16:18 CST 2016
     */
    int insert(ZcGoodsCatalog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ZC_GOODS_CATALOG
     *
     * @mbggenerated Tue Jan 19 22:16:18 CST 2016
     */
    int insertSelective(ZcGoodsCatalog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ZC_GOODS_CATALOG
     *
     * @mbggenerated Tue Jan 19 22:16:18 CST 2016
     */
    ZcGoodsCatalog selectByPrimaryKey(String goodsCatalogCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ZC_GOODS_CATALOG
     *
     * @mbggenerated Tue Jan 19 22:16:18 CST 2016
     */
    int updateByPrimaryKeySelective(ZcGoodsCatalog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ZC_GOODS_CATALOG
     *
     * @mbggenerated Tue Jan 19 22:16:18 CST 2016
     */
    int updateByPrimaryKey(ZcGoodsCatalog record);
}