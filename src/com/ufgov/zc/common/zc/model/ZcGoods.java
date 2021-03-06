package com.ufgov.zc.common.zc.model;

import java.math.BigDecimal;

public class ZcGoods extends ZcBaseBill{
    /**
   * 
   */
  private static final long serialVersionUID = 1643684889878757949L;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_GOODS.GOODS_CODE
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    private String goodsCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_GOODS.BRAND_CODE
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    private String brandCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_GOODS.GOODS_NAME
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    private String goodsName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_GOODS.IS_ENABLE
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    private String isEnable;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_GOODS.PRICE_MAKET
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    private BigDecimal priceMaket;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_GOODS.PRICE_XY
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    private BigDecimal priceXy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_GOODS.SPEC1
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    private String spec1;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_GOODS.SPEC2
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    private String spec2;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_GOODS.SPEC3
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    private String spec3;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_GOODS.SPEC4
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    private String spec4;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_GOODS.SPEC5
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    private String spec5;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_GOODS.SPEC6
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    private String spec6;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_GOODS.SPEC7
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    private String spec7;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_GOODS.SPEC8
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    private String spec8;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_GOODS.SPEC9
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    private String spec9;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_GOODS.GOODS_CATALOG_CODE
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    private String goodsCatalogCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ZC_GOODS.THUMBNAIL_ID
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    private String thumbnailId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_GOODS.GOODS_CODE
     *
     * @return the value of ZC_GOODS.GOODS_CODE
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public String getGoodsCode() {
        return goodsCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_GOODS.GOODS_CODE
     *
     * @param goodsCode the value for ZC_GOODS.GOODS_CODE
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_GOODS.BRAND_CODE
     *
     * @return the value of ZC_GOODS.BRAND_CODE
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public String getBrandCode() {
        return brandCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_GOODS.BRAND_CODE
     *
     * @param brandCode the value for ZC_GOODS.BRAND_CODE
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_GOODS.GOODS_NAME
     *
     * @return the value of ZC_GOODS.GOODS_NAME
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public String getGoodsName() {
        return goodsName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_GOODS.GOODS_NAME
     *
     * @param goodsName the value for ZC_GOODS.GOODS_NAME
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_GOODS.IS_ENABLE
     *
     * @return the value of ZC_GOODS.IS_ENABLE
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public String getIsEnable() {
        return isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_GOODS.IS_ENABLE
     *
     * @param isEnable the value for ZC_GOODS.IS_ENABLE
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_GOODS.PRICE_MAKET
     *
     * @return the value of ZC_GOODS.PRICE_MAKET
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public BigDecimal getPriceMaket() {
        return priceMaket;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_GOODS.PRICE_MAKET
     *
     * @param priceMaket the value for ZC_GOODS.PRICE_MAKET
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public void setPriceMaket(BigDecimal priceMaket) {
        this.priceMaket = priceMaket;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_GOODS.PRICE_XY
     *
     * @return the value of ZC_GOODS.PRICE_XY
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public BigDecimal getPriceXy() {
        return priceXy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_GOODS.PRICE_XY
     *
     * @param priceXy the value for ZC_GOODS.PRICE_XY
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public void setPriceXy(BigDecimal priceXy) {
        this.priceXy = priceXy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_GOODS.SPEC1
     *
     * @return the value of ZC_GOODS.SPEC1
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public String getSpec1() {
        return spec1;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_GOODS.SPEC1
     *
     * @param spec1 the value for ZC_GOODS.SPEC1
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public void setSpec1(String spec1) {
        this.spec1 = spec1;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_GOODS.SPEC2
     *
     * @return the value of ZC_GOODS.SPEC2
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public String getSpec2() {
        return spec2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_GOODS.SPEC2
     *
     * @param spec2 the value for ZC_GOODS.SPEC2
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public void setSpec2(String spec2) {
        this.spec2 = spec2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_GOODS.SPEC3
     *
     * @return the value of ZC_GOODS.SPEC3
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public String getSpec3() {
        return spec3;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_GOODS.SPEC3
     *
     * @param spec3 the value for ZC_GOODS.SPEC3
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public void setSpec3(String spec3) {
        this.spec3 = spec3;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_GOODS.SPEC4
     *
     * @return the value of ZC_GOODS.SPEC4
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public String getSpec4() {
        return spec4;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_GOODS.SPEC4
     *
     * @param spec4 the value for ZC_GOODS.SPEC4
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public void setSpec4(String spec4) {
        this.spec4 = spec4;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_GOODS.SPEC5
     *
     * @return the value of ZC_GOODS.SPEC5
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public String getSpec5() {
        return spec5;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_GOODS.SPEC5
     *
     * @param spec5 the value for ZC_GOODS.SPEC5
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public void setSpec5(String spec5) {
        this.spec5 = spec5;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_GOODS.SPEC6
     *
     * @return the value of ZC_GOODS.SPEC6
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public String getSpec6() {
        return spec6;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_GOODS.SPEC6
     *
     * @param spec6 the value for ZC_GOODS.SPEC6
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public void setSpec6(String spec6) {
        this.spec6 = spec6;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_GOODS.SPEC7
     *
     * @return the value of ZC_GOODS.SPEC7
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public String getSpec7() {
        return spec7;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_GOODS.SPEC7
     *
     * @param spec7 the value for ZC_GOODS.SPEC7
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public void setSpec7(String spec7) {
        this.spec7 = spec7;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_GOODS.SPEC8
     *
     * @return the value of ZC_GOODS.SPEC8
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public String getSpec8() {
        return spec8;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_GOODS.SPEC8
     *
     * @param spec8 the value for ZC_GOODS.SPEC8
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public void setSpec8(String spec8) {
        this.spec8 = spec8;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_GOODS.SPEC9
     *
     * @return the value of ZC_GOODS.SPEC9
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public String getSpec9() {
        return spec9;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_GOODS.SPEC9
     *
     * @param spec9 the value for ZC_GOODS.SPEC9
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public void setSpec9(String spec9) {
        this.spec9 = spec9;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_GOODS.GOODS_CATALOG_CODE
     *
     * @return the value of ZC_GOODS.GOODS_CATALOG_CODE
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public String getGoodsCatalogCode() {
        return goodsCatalogCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_GOODS.GOODS_CATALOG_CODE
     *
     * @param goodsCatalogCode the value for ZC_GOODS.GOODS_CATALOG_CODE
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public void setGoodsCatalogCode(String goodsCatalogCode) {
        this.goodsCatalogCode = goodsCatalogCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ZC_GOODS.THUMBNAIL_ID
     *
     * @return the value of ZC_GOODS.THUMBNAIL_ID
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public String getThumbnailId() {
        return thumbnailId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ZC_GOODS.THUMBNAIL_ID
     *
     * @param thumbnailId the value for ZC_GOODS.THUMBNAIL_ID
     *
     * @mbggenerated Tue Jan 19 21:34:00 CST 2016
     */
    public void setThumbnailId(String thumbnailId) {
        this.thumbnailId = thumbnailId;
    }
}