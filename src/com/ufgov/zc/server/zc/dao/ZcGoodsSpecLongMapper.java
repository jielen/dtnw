package com.ufgov.zc.server.zc.dao;

import com.ufgov.zc.common.zc.model.ZcGoodsSpecLong; 

public interface ZcGoodsSpecLongMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ZC_GOODS_SPEC_LONG
     *
     * @mbggenerated Tue Jan 19 22:49:33 CST 2016
     */
    int deleteByPrimaryKey(ZcGoodsSpecLong key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ZC_GOODS_SPEC_LONG
     *
     * @mbggenerated Tue Jan 19 22:49:33 CST 2016
     */
    int insert(ZcGoodsSpecLong record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ZC_GOODS_SPEC_LONG
     *
     * @mbggenerated Tue Jan 19 22:49:33 CST 2016
     */
    int insertSelective(ZcGoodsSpecLong record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ZC_GOODS_SPEC_LONG
     *
     * @mbggenerated Tue Jan 19 22:49:33 CST 2016
     */
    ZcGoodsSpecLong selectByPrimaryKey(ZcGoodsSpecLong key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ZC_GOODS_SPEC_LONG
     *
     * @mbggenerated Tue Jan 19 22:49:33 CST 2016
     */
    int updateByPrimaryKeySelective(ZcGoodsSpecLong record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ZC_GOODS_SPEC_LONG
     *
     * @mbggenerated Tue Jan 19 22:49:33 CST 2016
     */
    int updateByPrimaryKeyWithBLOBs(ZcGoodsSpecLong record);
}