select ZC_BI_NO,
       ZC_MAKE_CODE,
       ZC_BI_SUM,
       ZC_BI_JHUA_SUM,
       case when(select m.zc_yep_flag from zc_p_pro_make m where m.zc_make_code=zc_make_code)!='00' then 
         nvl((select sum(CANUSE_MONEY)
             from vw_budget_gb
            where SUM_ID = ZC_P_PRO_MITEM_BI.ZC_BI_NO),
           0)
         else
       nvl((select sum(CANUSE_MONEY)
             from vw_budget_gb
            where SUM_ID = ZC_P_PRO_MITEM_BI.ZC_BI_NO),
           0) + nvl(ZC_BI_JHUA_SUM, 0) end ZC_BI_DO_SUM,
       ZC_BI_YJBA_SUM,
       BI_TARGET_CODE,
       B_ACC_CODE,
       B_ACC_NAME,
       CO_CODE,
       CO_NAME,
       DEC_MONEY,
       FUND_CODE,
       FUND_NAME,
       INCEPTDOC_CODE,
       INCEPTDOC_NAME,
       MANAGE_CODE,
       MANAGE_NAME,
       ND,
       ORG_CODE,
       ORG_NAME,
       ORIGIN_CODE,
       ORIGIN_NAME,
       OUTLAY_CODE,
       OUTLAY_NAME,
       PAYOUT_CODE,
       PAYOUT_NAME,
       PAYTYPE_CODE,
       PAYTYPE_NAME,
       PROJECT_CODE,
       PROJECT_NAME,
       PROJECT_TYPE_CODE,
       PROJECT_TYPE_NAME,
       SENDDOC_CODE,
       SENDDOC_NAME,
       SENDDOC_TYPE_CODE,
       SENDDOC_TYPE_NAME,
       ZC_BI_HTBA_SUM,
       ZC_CG_TYPE,
       ZC_PLAN_TYPE,
       ZC_SAVE_NUM,
       ZC_CATALOGUE_CODE,
       ZC_CATALOGUE_NAME,
       ZC_YEAR,
       ZC_YEP_SUM,
       ZC_BI_APD_FLAG,
       ZC_FUND_REMARK,
       ZC_FUND_FILE,
       ZC_FUND_FILE_BLOBID,
       SUPPLEMENT_AMOUNT,
       ZC_PRO_BI_SEQ,
       ZC_BI_JHUA_SUM as ZC_BI_USED_SUM,
       ZC_USE_BI_ID,
       bt_name,
       gb_name
  from ZC_P_PRO_MITEM_BI
 where ZC_MAKE_CODE = '丹财采计[20140325]-0704号'
--[丹财采计[20140325]-0704号]
