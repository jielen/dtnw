package com.ufgov.zc.server.zc.dao;

import java.util.List;

import com.ufgov.zc.common.zc.model.ZcFaCardDoc;
import com.ufgov.zc.common.zc.model.ZcFaCardDocExample;

public interface IZcFaCardDocDao {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table DTRUN1.ZC_FA_CARD_DOC
     *
     * @mbggenerated Wed Mar 05 09:27:36 CST 2014
     */
    int countByExample(ZcFaCardDocExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table DTRUN1.ZC_FA_CARD_DOC
     *
     * @mbggenerated Wed Mar 05 09:27:36 CST 2014
     */
    int deleteByExample(ZcFaCardDocExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table DTRUN1.ZC_FA_CARD_DOC
     *
     * @mbggenerated Wed Mar 05 09:27:36 CST 2014
     */
    int insert(ZcFaCardDoc record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table DTRUN1.ZC_FA_CARD_DOC
     *
     * @mbggenerated Wed Mar 05 09:27:36 CST 2014
     */
    int insertSelective(ZcFaCardDoc record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table DTRUN1.ZC_FA_CARD_DOC
     *
     * @mbggenerated Wed Mar 05 09:27:36 CST 2014
     */
    List selectByExample(ZcFaCardDocExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table DTRUN1.ZC_FA_CARD_DOC
     *
     * @mbggenerated Wed Mar 05 09:27:36 CST 2014
     */
    int updateByExampleSelective( ZcFaCardDoc record,ZcFaCardDocExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table DTRUN1.ZC_FA_CARD_DOC
     *
     * @mbggenerated Wed Mar 05 09:27:36 CST 2014
     */
    int updateByExample(ZcFaCardDoc record, ZcFaCardDocExample example);

    void insertList(List docList);

    void deleteByCardId(String cardId);
}