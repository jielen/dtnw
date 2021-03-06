package com.ufgov.zc.server.zc.dao;

import java.util.List;

import com.ufgov.zc.common.system.dto.ElementConditionDto;
import com.ufgov.zc.common.zc.model.ZcGoods;

public interface ZcGoodsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ZC_GOODS
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    int deleteByPrimaryKey(String goodsCode);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ZC_GOODS
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    int insert(ZcGoods record);



    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ZC_GOODS
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    ZcGoods selectByPrimaryKey(String goodsCode);



    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ZC_GOODS
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    int updateByPrimaryKey(ZcGoods record);
    
    List getMainDataLst(ElementConditionDto elementConditionDto);
}