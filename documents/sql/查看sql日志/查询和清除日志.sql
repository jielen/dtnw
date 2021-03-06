select to_char(timestamp, 'yyyy-mm-dd hh24:mi:ss') times,
       object_name,
       sql_text,
       sql_bind,
       statement_type
  from dba_fga_audit_trail
 where db_user='DTRUN'
 and to_char(timestamp, 'yyyy-mm-dd hh24:mi:ss') >= '2012-03-18 00:32:00' 
 and object_name not in ('SYS_ROLE_MENU_MODULE','SYS_MESSAGE','SYS_ORGTYPE','SYS_USER','SYS_RULE','SYS_USER_ORG','SYS_MENU_MODULE','SYS_ROLE','SYS_SESSION','SYS_LOGINFO','SYS_USERMANAGE','SYS_ROLE_MENU','SYS_MENU','SYS_MODULE','SYS_USER_ROLE_RULE')
 order by times desc
 
 --run in sys 
/*truncate table fga_log$*/
