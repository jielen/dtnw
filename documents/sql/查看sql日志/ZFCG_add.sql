spool fga_add_policy_1382583017126.log
SET SERVEROUTPUT ON
--AP_ARTICLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_ARTICLE',
policy_name     => 'AP_ARTICLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_ARTICLE_ATTACH--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_ARTICLE_ATTACH',
policy_name     => 'AP_ARTICLE_ATTACH',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_ARTICLE_BACKUP0512--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_ARTICLE_BACKUP0512',
policy_name     => 'AP_ARTICLE_BACKUP0512',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_ARTICLE_PORTLET--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_ARTICLE_PORTLET',
policy_name     => 'AP_ARTICLE_PORTLET',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_DAILY_PLAN--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_DAILY_PLAN',
policy_name     => 'AP_DAILY_PLAN',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_EXPRESS_APP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_EXPRESS_APP',
policy_name     => 'AP_EXPRESS_APP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_GROUP_CONFIG--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_GROUP_CONFIG',
policy_name     => 'AP_GROUP_CONFIG',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_GROUP_PAGE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_GROUP_PAGE',
policy_name     => 'AP_GROUP_PAGE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_LINK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_LINK',
policy_name     => 'AP_LINK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_MAINTAIN--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_MAINTAIN',
policy_name     => 'AP_MAINTAIN',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_MAINTAIN_ADDI--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_MAINTAIN_ADDI',
policy_name     => 'AP_MAINTAIN_ADDI',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_MAINTAIN_RECORD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_MAINTAIN_RECORD',
policy_name     => 'AP_MAINTAIN_RECORD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_MENU--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_MENU',
policy_name     => 'AP_MENU',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_MENU_COMPO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_MENU_COMPO',
policy_name     => 'AP_MENU_COMPO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_MESSAGE_BOARD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_MESSAGE_BOARD',
policy_name     => 'AP_MESSAGE_BOARD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_PAGE_LAYOUT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_PAGE_LAYOUT',
policy_name     => 'AP_PAGE_LAYOUT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_PAGE_PORTLET--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_PAGE_PORTLET',
policy_name     => 'AP_PAGE_PORTLET',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_PORTAL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_PORTAL',
policy_name     => 'AP_PORTAL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_PORTAL_THEME--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_PORTAL_THEME',
policy_name     => 'AP_PORTAL_THEME',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_PORTLET--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_PORTLET',
policy_name     => 'AP_PORTLET',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_UPLOAD_PORTLET--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_UPLOAD_PORTLET',
policy_name     => 'AP_UPLOAD_PORTLET',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AP_USER_AREA--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AP_USER_AREA',
policy_name     => 'AP_USER_AREA',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_ADMIN--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_ADMIN',
policy_name     => 'AS_ADMIN',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_ANYISERVER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_ANYISERVER',
policy_name     => 'AS_ANYISERVER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_COCODE_USERID--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_COCODE_USERID',
policy_name     => 'AS_COCODE_USERID',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_COCODE_ZH--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_COCODE_ZH',
policy_name     => 'AS_COCODE_ZH',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_COL_RELATION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_COL_RELATION',
policy_name     => 'AS_COL_RELATION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_COMPO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_COMPO',
policy_name     => 'AS_COMPO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_COMPO_FUNC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_COMPO_FUNC',
policy_name     => 'AS_COMPO_FUNC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_CUSTOM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_CUSTOM',
policy_name     => 'AS_CUSTOM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_CUSTOM_CONFIG--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_CUSTOM_CONFIG',
policy_name     => 'AS_CUSTOM_CONFIG',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_CUSTOM_FOREIGN_ENTITY--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_CUSTOM_FOREIGN_ENTITY',
policy_name     => 'AS_CUSTOM_FOREIGN_ENTITY',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_DB_CONF--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_DB_CONF',
policy_name     => 'AS_DB_CONF',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_DB_OPTIMIZER_MODE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_DB_OPTIMIZER_MODE',
policy_name     => 'AS_DB_OPTIMIZER_MODE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_DB_VER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_DB_VER',
policy_name     => 'AS_DB_VER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_DESKTOP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_DESKTOP',
policy_name     => 'AS_DESKTOP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_DESKTOP_AREA--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_DESKTOP_AREA',
policy_name     => 'AS_DESKTOP_AREA',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_DESKTOP_IMG--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_DESKTOP_IMG',
policy_name     => 'AS_DESKTOP_IMG',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_EMP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_EMP',
policy_name     => 'AS_EMP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_EMP_POSITION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_EMP_POSITION',
policy_name     => 'AS_EMP_POSITION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_EMP_ROLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_EMP_ROLE',
policy_name     => 'AS_EMP_ROLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_FAVORITE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_FAVORITE',
policy_name     => 'AS_FAVORITE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_FILE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_FILE',
policy_name     => 'AS_FILE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_FILEIMP_FIELD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_FILEIMP_FIELD',
policy_name     => 'AS_FILEIMP_FIELD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_FILEIMP_STYLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_FILEIMP_STYLE',
policy_name     => 'AS_FILEIMP_STYLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_FILE_EXP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_FILE_EXP',
policy_name     => 'AS_FILE_EXP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_FILE_EXP_FIELD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_FILE_EXP_FIELD',
policy_name     => 'AS_FILE_EXP_FIELD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_FILE_EXP_TABLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_FILE_EXP_TABLE',
policy_name     => 'AS_FILE_EXP_TABLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_FOREIGN_ENTITY--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_FOREIGN_ENTITY',
policy_name     => 'AS_FOREIGN_ENTITY',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_FUNC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_FUNC',
policy_name     => 'AS_FUNC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_GETDATA_RULE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_GETDATA_RULE',
policy_name     => 'AS_GETDATA_RULE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_GROUP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_GROUP',
policy_name     => 'AS_GROUP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_GX--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_GX',
policy_name     => 'AS_GX',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_INFO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_INFO',
policy_name     => 'AS_INFO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_IP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_IP',
policy_name     => 'AS_IP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_LANG--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_LANG',
policy_name     => 'AS_LANG',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_LANG_TRANS--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_LANG_TRANS',
policy_name     => 'AS_LANG_TRANS',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_LOG--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_LOG',
policy_name     => 'AS_LOG',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_MENU--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_MENU',
policy_name     => 'AS_MENU',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_MENU_COMPO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_MENU_COMPO',
policy_name     => 'AS_MENU_COMPO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_NO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_NO',
policy_name     => 'AS_NO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_NO_RULE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_NO_RULE',
policy_name     => 'AS_NO_RULE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_NO_RULE_SEG--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_NO_RULE_SEG',
policy_name     => 'AS_NO_RULE_SEG',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_NO_TEMP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_NO_TEMP',
policy_name     => 'AS_NO_TEMP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_NUM_LIM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_NUM_LIM',
policy_name     => 'AS_NUM_LIM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_NUM_TOOL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_NUM_TOOL',
policy_name     => 'AS_NUM_TOOL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_NUM_TOOL_NO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_NUM_TOOL_NO',
policy_name     => 'AS_NUM_TOOL_NO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_OPTION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_OPTION',
policy_name     => 'AS_OPTION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_ORG--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_ORG',
policy_name     => 'AS_ORG',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_ORG_POSITION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_ORG_POSITION',
policy_name     => 'AS_ORG_POSITION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_PAGE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_PAGE',
policy_name     => 'AS_PAGE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_PAGE_AREA--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_PAGE_AREA',
policy_name     => 'AS_PAGE_AREA',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_PAGE_AREA_FIELD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_PAGE_AREA_FIELD',
policy_name     => 'AS_PAGE_AREA_FIELD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_PAGE_AREA_GROUP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_PAGE_AREA_GROUP',
policy_name     => 'AS_PAGE_AREA_GROUP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_PAGE_FUNC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_PAGE_FUNC',
policy_name     => 'AS_PAGE_FUNC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_POSITION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_POSITION',
policy_name     => 'AS_POSITION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_POSI_ROLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_POSI_ROLE',
policy_name     => 'AS_POSI_ROLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_POSI_ROLE_GRANTED--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_POSI_ROLE_GRANTED',
policy_name     => 'AS_POSI_ROLE_GRANTED',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_PRINT_JASPERPRINTSET--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_PRINT_JASPERPRINTSET',
policy_name     => 'AS_PRINT_JASPERPRINTSET',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_PRINT_JASPERTEMP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_PRINT_JASPERTEMP',
policy_name     => 'AS_PRINT_JASPERTEMP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_PRINT_SERVER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_PRINT_SERVER',
policy_name     => 'AS_PRINT_SERVER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_PRINT_TEMPLATE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_PRINT_TEMPLATE',
policy_name     => 'AS_PRINT_TEMPLATE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_PRINT_XML--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_PRINT_XML',
policy_name     => 'AS_PRINT_XML',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_PRODUCT_VER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_PRODUCT_VER',
policy_name     => 'AS_PRODUCT_VER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_PUBLISH_CONTENT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_PUBLISH_CONTENT',
policy_name     => 'AS_PUBLISH_CONTENT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_PUBLISH_TEMPLATE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_PUBLISH_TEMPLATE',
policy_name     => 'AS_PUBLISH_TEMPLATE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_REGISTER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_REGISTER',
policy_name     => 'AS_REGISTER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_REGISTER_BAK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_REGISTER_BAK',
policy_name     => 'AS_REGISTER_BAK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_REGISTER_OLD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_REGISTER_OLD',
policy_name     => 'AS_REGISTER_OLD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_REPORT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_REPORT',
policy_name     => 'AS_REPORT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_REPORT_PARAMS--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_REPORT_PARAMS',
policy_name     => 'AS_REPORT_PARAMS',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_ROLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_ROLE',
policy_name     => 'AS_ROLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_ROLE_FUNC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_ROLE_FUNC',
policy_name     => 'AS_ROLE_FUNC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_ROLE_GROUP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_ROLE_GROUP',
policy_name     => 'AS_ROLE_GROUP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_ROLE_NUM_LIM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_ROLE_NUM_LIM',
policy_name     => 'AS_ROLE_NUM_LIM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_ROLE_SUB_NUMLIM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_ROLE_SUB_NUMLIM',
policy_name     => 'AS_ROLE_SUB_NUMLIM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_SETUP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_SETUP',
policy_name     => 'AS_SETUP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_STATINFO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_STATINFO',
policy_name     => 'AS_STATINFO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_TABLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_TABLE',
policy_name     => 'AS_TABLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_TAB_COL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_TAB_COL',
policy_name     => 'AS_TAB_COL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_TEM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_TEM',
policy_name     => 'AS_TEM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_TEMP_GOCOM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_TEMP_GOCOM',
policy_name     => 'AS_TEMP_GOCOM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_TIMERTASK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_TIMERTASK',
policy_name     => 'AS_TIMERTASK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_TRAD_VER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_TRAD_VER',
policy_name     => 'AS_TRAD_VER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_UPLOAD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_UPLOAD',
policy_name     => 'AS_UPLOAD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_USER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_USER',
policy_name     => 'AS_USER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_USER_CONVERT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_USER_CONVERT',
policy_name     => 'AS_USER_CONVERT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_USER_FUNC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_USER_FUNC',
policy_name     => 'AS_USER_FUNC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_USER_GRANT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_USER_GRANT',
policy_name     => 'AS_USER_GRANT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_USER_GROUP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_USER_GROUP',
policy_name     => 'AS_USER_GROUP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_USER_NUM_LIM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_USER_NUM_LIM',
policy_name     => 'AS_USER_NUM_LIM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_USER_SCHE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_USER_SCHE',
policy_name     => 'AS_USER_SCHE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_USER_SESSION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_USER_SESSION',
policy_name     => 'AS_USER_SESSION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_USER_SUB_NUMLIM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_USER_SUB_NUMLIM',
policy_name     => 'AS_USER_SUB_NUMLIM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_USER_TICKET--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_USER_TICKET',
policy_name     => 'AS_USER_TICKET',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_VAL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_VAL',
policy_name     => 'AS_VAL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_VALSET--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_VALSET',
policy_name     => 'AS_VALSET',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_WF_ACTION_RMD_HISTORY--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_WF_ACTION_RMD_HISTORY',
policy_name     => 'AS_WF_ACTION_RMD_HISTORY',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_WF_ACTIVITY_COMPO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_WF_ACTIVITY_COMPO',
policy_name     => 'AS_WF_ACTIVITY_COMPO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_WF_ACTIVITY_FIELD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_WF_ACTIVITY_FIELD',
policy_name     => 'AS_WF_ACTIVITY_FIELD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_WF_BIND_STATE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_WF_BIND_STATE',
policy_name     => 'AS_WF_BIND_STATE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_WF_BIND_VARIABLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_WF_BIND_VARIABLE',
policy_name     => 'AS_WF_BIND_VARIABLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_WF_BUSINESS_SUPERIOR--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_WF_BUSINESS_SUPERIOR',
policy_name     => 'AS_WF_BUSINESS_SUPERIOR',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_WF_BUSINESS_SUPERIOR_0228--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_WF_BUSINESS_SUPERIOR_0228',
policy_name     => 'AS_WF_BUSINESS_SUPERIOR_0228',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_WF_BUSINESS_SUPERIOR_BAK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_WF_BUSINESS_SUPERIOR_BAK',
policy_name     => 'AS_WF_BUSINESS_SUPERIOR_BAK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_WF_BUSINESS_SUPERIOR_BAK2--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_WF_BUSINESS_SUPERIOR_BAK2',
policy_name     => 'AS_WF_BUSINESS_SUPERIOR_BAK2',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_WF_BUSINESS_SUPERIOR_OLD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_WF_BUSINESS_SUPERIOR_OLD',
policy_name     => 'AS_WF_BUSINESS_SUPERIOR_OLD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_WF_DRAFT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_WF_DRAFT',
policy_name     => 'AS_WF_DRAFT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_WF_FUNC_ACTIVITY--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_WF_FUNC_ACTIVITY',
policy_name     => 'AS_WF_FUNC_ACTIVITY',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_WF_TEMPLATE_TYPE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_WF_TEMPLATE_TYPE',
policy_name     => 'AS_WF_TEMPLATE_TYPE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_WF_TEMP_COMMENT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_WF_TEMP_COMMENT',
policy_name     => 'AS_WF_TEMP_COMMENT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ELE_BUDGET_SUBJECT_ITEM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ELE_BUDGET_SUBJECT_ITEM',
policy_name     => 'ELE_BUDGET_SUBJECT_ITEM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_BILL_SERVER_LIST--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_BILL_SERVER_LIST',
policy_name     => 'EM_BILL_SERVER_LIST',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_B_EXPERT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_B_EXPERT',
policy_name     => 'EM_B_EXPERT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_B_EXPERT_TYPE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_B_EXPERT_TYPE',
policy_name     => 'EM_B_EXPERT_TYPE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_B_MAJOR--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_B_MAJOR',
policy_name     => 'EM_B_MAJOR',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_B_PROFESSIONALTITLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_B_PROFESSIONALTITLE',
policy_name     => 'EM_B_PROFESSIONALTITLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_CALL_EXPERT_RECORD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_CALL_EXPERT_RECORD',
policy_name     => 'EM_CALL_EXPERT_RECORD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_CALL_SERVER_LIST--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_CALL_SERVER_LIST',
policy_name     => 'EM_CALL_SERVER_LIST',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_EVALUATION_CONDITION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_EVALUATION_CONDITION',
policy_name     => 'EM_EVALUATION_CONDITION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_EXPERT_BILL_FILTER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_EXPERT_BILL_FILTER',
policy_name     => 'EM_EXPERT_BILL_FILTER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_EXPERT_EVALUATION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_EXPERT_EVALUATION',
policy_name     => 'EM_EXPERT_EVALUATION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_EXPERT_FILTER_PATH--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_EXPERT_FILTER_PATH',
policy_name     => 'EM_EXPERT_FILTER_PATH',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_EXPERT_PRO_BILL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_EXPERT_PRO_BILL',
policy_name     => 'EM_EXPERT_PRO_BILL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_EXPERT_PRO_BILL_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_EXPERT_PRO_BILL_PACK',
policy_name     => 'EM_EXPERT_PRO_BILL_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_EXPERT_PRO_BILL_RULES--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_EXPERT_PRO_BILL_RULES',
policy_name     => 'EM_EXPERT_PRO_BILL_RULES',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_EXPERT_SERVICE_AREA--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_EXPERT_SERVICE_AREA',
policy_name     => 'EM_EXPERT_SERVICE_AREA',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_EXPERT_SERVICE_CATALOGUE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_EXPERT_SERVICE_CATALOGUE',
policy_name     => 'EM_EXPERT_SERVICE_CATALOGUE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_EXPERT_TYPE_JOIN--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_EXPERT_TYPE_JOIN',
policy_name     => 'EM_EXPERT_TYPE_JOIN',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_FUNDPAY_BILL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_FUNDPAY_BILL',
policy_name     => 'EM_FUNDPAY_BILL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_FUNDPAY_BILL_DETAIL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_FUNDPAY_BILL_DETAIL',
policy_name     => 'EM_FUNDPAY_BILL_DETAIL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_FUNDPAY_REPORT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_FUNDPAY_REPORT',
policy_name     => 'EM_FUNDPAY_REPORT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EXPERT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EXPERT',
policy_name     => 'EXPERT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EXPERT_JION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EXPERT_JION',
policy_name     => 'EXPERT_JION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EXPERT_TEMP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EXPERT_TEMP',
policy_name     => 'EXPERT_TEMP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EXPERT_TYPE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EXPERT_TYPE',
policy_name     => 'EXPERT_TYPE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--MA_ACC_CALENDAR_VAR--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'MA_ACC_CALENDAR_VAR',
policy_name     => 'MA_ACC_CALENDAR_VAR',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--MA_BACC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'MA_BACC',
policy_name     => 'MA_BACC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--MA_COMPANY--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'MA_COMPANY',
policy_name     => 'MA_COMPANY',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--MA_CP_BILL_ELEMENT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'MA_CP_BILL_ELEMENT',
policy_name     => 'MA_CP_BILL_ELEMENT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--MA_DICT_ORG_CO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'MA_DICT_ORG_CO',
policy_name     => 'MA_DICT_ORG_CO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--MA_EACC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'MA_EACC',
policy_name     => 'MA_EACC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--RP_COMPANY--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'RP_COMPANY',
policy_name     => 'RP_COMPANY',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--SYS_TAB_SEQUENCE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'SYS_TAB_SEQUENCE',
policy_name     => 'SYS_TAB_SEQUENCE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--VW_BUDGET_GB_BACKUP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'VW_BUDGET_GB_BACKUP',
policy_name     => 'VW_BUDGET_GB_BACKUP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--VW_BUDGET_GB_TABLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'VW_BUDGET_GB_TABLE',
policy_name     => 'VW_BUDGET_GB_TABLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_ACTION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_ACTION',
policy_name     => 'WF_ACTION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_ACTION_HISTORY--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_ACTION_HISTORY',
policy_name     => 'WF_ACTION_HISTORY',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_ACTION_HISTORY_RELATION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_ACTION_HISTORY_RELATION',
policy_name     => 'WF_ACTION_HISTORY_RELATION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_CURRENT_TASK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_CURRENT_TASK',
policy_name     => 'WF_CURRENT_TASK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_CURRENT_TASK_RELATION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_CURRENT_TASK_RELATION',
policy_name     => 'WF_CURRENT_TASK_RELATION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_DELEGATION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_DELEGATION',
policy_name     => 'WF_DELEGATION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_DELEGATION_HISTORY--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_DELEGATION_HISTORY',
policy_name     => 'WF_DELEGATION_HISTORY',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_DOCUMENT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_DOCUMENT',
policy_name     => 'WF_DOCUMENT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_EXECUTOR_ORDER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_EXECUTOR_ORDER',
policy_name     => 'WF_EXECUTOR_ORDER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_EXECUTOR_SOURCE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_EXECUTOR_SOURCE',
policy_name     => 'WF_EXECUTOR_SOURCE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_INSTANCE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_INSTANCE',
policy_name     => 'WF_INSTANCE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_LINK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_LINK',
policy_name     => 'WF_LINK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_LINK_STATE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_LINK_STATE',
policy_name     => 'WF_LINK_STATE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_NODE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_NODE',
policy_name     => 'WF_NODE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_NODE_STATE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_NODE_STATE',
policy_name     => 'WF_NODE_STATE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_PASS--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_PASS',
policy_name     => 'WF_PASS',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_STATE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_STATE',
policy_name     => 'WF_STATE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_STATE_VALUE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_STATE_VALUE',
policy_name     => 'WF_STATE_VALUE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_TASK_EXECUTOR--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_TASK_EXECUTOR',
policy_name     => 'WF_TASK_EXECUTOR',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_TASK_GRANTER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_TASK_GRANTER',
policy_name     => 'WF_TASK_GRANTER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_TASK_TERM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_TASK_TERM',
policy_name     => 'WF_TASK_TERM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_TEMPLATE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_TEMPLATE',
policy_name     => 'WF_TEMPLATE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_VARIABLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_VARIABLE',
policy_name     => 'WF_VARIABLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_VARIABLE_VALUE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_VARIABLE_VALUE',
policy_name     => 'WF_VARIABLE_VALUE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_ANIMATION_META--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_ANIMATION_META',
policy_name     => 'ZC_ANIMATION_META',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_BAL_CHG_HT_BI--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_BAL_CHG_HT_BI',
policy_name     => 'ZC_BAL_CHG_HT_BI',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_BI_AUDIT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_BI_AUDIT',
policy_name     => 'ZC_BI_AUDIT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_BUSINESS_LOG--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_BUSINESS_LOG',
policy_name     => 'ZC_BUSINESS_LOG',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_BW_DC_BASEDICT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_BW_DC_BASEDICT',
policy_name     => 'ZC_BW_DC_BASEDICT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_BW_D_STOCKMODE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_BW_D_STOCKMODE',
policy_name     => 'ZC_BW_D_STOCKMODE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_B_AGENCY--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_B_AGENCY',
policy_name     => 'ZC_B_AGENCY',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_B_AGENCY_APTD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_B_AGENCY_APTD',
policy_name     => 'ZC_B_AGENCY_APTD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_B_AGENCY_LIST_APTD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_B_AGENCY_LIST_APTD',
policy_name     => 'ZC_B_AGENCY_LIST_APTD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_B_CATALOGUE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_B_CATALOGUE',
policy_name     => 'ZC_B_CATALOGUE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_B_CATALOGUE_RULE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_B_CATALOGUE_RULE',
policy_name     => 'ZC_B_CATALOGUE_RULE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_B_EXPERT_TYPE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_B_EXPERT_TYPE',
policy_name     => 'ZC_B_EXPERT_TYPE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_B_MERCHANDISE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_B_MERCHANDISE',
policy_name     => 'ZC_B_MERCHANDISE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_B_MER_DISCOUNT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_B_MER_DISCOUNT',
policy_name     => 'ZC_B_MER_DISCOUNT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_B_SUPPLIER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_B_SUPPLIER',
policy_name     => 'ZC_B_SUPPLIER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_B_SUPPLIER_FEEDBACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_B_SUPPLIER_FEEDBACK',
policy_name     => 'ZC_B_SUPPLIER_FEEDBACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_B_SUPPLIER_GUARANTEE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_B_SUPPLIER_GUARANTEE',
policy_name     => 'ZC_B_SUPPLIER_GUARANTEE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_B_SUPPLIER_JUDGE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_B_SUPPLIER_JUDGE',
policy_name     => 'ZC_B_SUPPLIER_JUDGE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_B_SUPPLIER_MARK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_B_SUPPLIER_MARK',
policy_name     => 'ZC_B_SUPPLIER_MARK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_B_SUPPLIER_QUALIFY--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_B_SUPPLIER_QUALIFY',
policy_name     => 'ZC_B_SUPPLIER_QUALIFY',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_B_SUPPLIER_ZYXM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_B_SUPPLIER_ZYXM',
policy_name     => 'ZC_B_SUPPLIER_ZYXM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_CGFS--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_CGFS',
policy_name     => 'ZC_CGFS',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_DATA_EXCHANGE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_DATA_EXCHANGE',
policy_name     => 'ZC_DATA_EXCHANGE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_DATA_EXCHANGE_LOG--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_DATA_EXCHANGE_LOG',
policy_name     => 'ZC_DATA_EXCHANGE_LOG',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_DATA_EXCHANGE_REDO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_DATA_EXCHANGE_REDO',
policy_name     => 'ZC_DATA_EXCHANGE_REDO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_DDCG_HT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_DDCG_HT',
policy_name     => 'ZC_DDCG_HT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_DELEG_JCZW_DATA_EXCHANGE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_DELEG_JCZW_DATA_EXCHANGE',
policy_name     => 'ZC_DELEG_JCZW_DATA_EXCHANGE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_DIYU_CTG--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_DIYU_CTG',
policy_name     => 'ZC_DIYU_CTG',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_BID_CONDITION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_BID_CONDITION',
policy_name     => 'ZC_EB_BID_CONDITION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_BULLETIN--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_BULLETIN',
policy_name     => 'ZC_EB_BULLETIN',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_BULLETIN_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_BULLETIN_PACK',
policy_name     => 'ZC_EB_BULLETIN_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_BULLETIN_WORD_MOLD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_BULLETIN_WORD_MOLD',
policy_name     => 'ZC_EB_BULLETIN_WORD_MOLD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_BULLETIN_WORD_MOLD_PARAM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_BULLETIN_WORD_MOLD_PARAM',
policy_name     => 'ZC_EB_BULLETIN_WORD_MOLD_PARAM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_CONSULT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_CONSULT',
policy_name     => 'ZC_EB_CONSULT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_DLY_BID_INFO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_DLY_BID_INFO',
policy_name     => 'ZC_EB_DLY_BID_INFO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_DUTY_AUDIT_SHEET--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_DUTY_AUDIT_SHEET',
policy_name     => 'ZC_EB_DUTY_AUDIT_SHEET',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_ECBJ_ITEM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_ECBJ_ITEM',
policy_name     => 'ZC_EB_ECBJ_ITEM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_ECBJ_PLAN--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_ECBJ_PLAN',
policy_name     => 'ZC_EB_ECBJ_PLAN',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_ENTRUST--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_ENTRUST',
policy_name     => 'ZC_EB_ENTRUST',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_ENTRUST_CANCEL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_ENTRUST_CANCEL',
policy_name     => 'ZC_EB_ENTRUST_CANCEL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_ENTRUST_CANCEL_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_ENTRUST_CANCEL_PACK',
policy_name     => 'ZC_EB_ENTRUST_CANCEL_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_ENTRUST_DETAIL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_ENTRUST_DETAIL',
policy_name     => 'ZC_EB_ENTRUST_DETAIL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_EVALUATION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_EVALUATION',
policy_name     => 'ZC_EB_EVALUATION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_EVAL_EXPERT_COMMENT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_EVAL_EXPERT_COMMENT',
policy_name     => 'ZC_EB_EVAL_EXPERT_COMMENT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_EVAL_EXPERT_SIGN--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_EVAL_EXPERT_SIGN',
policy_name     => 'ZC_EB_EVAL_EXPERT_SIGN',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_EVAL_PARAM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_EVAL_PARAM',
policy_name     => 'ZC_EB_EVAL_PARAM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_EVAL_RECORD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_EVAL_RECORD',
policy_name     => 'ZC_EB_EVAL_RECORD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_EVAL_REPORT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_EVAL_REPORT',
policy_name     => 'ZC_EB_EVAL_REPORT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_EVAL_RESULT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_EVAL_RESULT',
policy_name     => 'ZC_EB_EVAL_RESULT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_EXPERT_CHENGNUO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_EXPERT_CHENGNUO',
policy_name     => 'ZC_EB_EXPERT_CHENGNUO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_EXPERT_OPINION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_EXPERT_OPINION',
policy_name     => 'ZC_EB_EXPERT_OPINION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_EXPERT_SESSION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_EXPERT_SESSION',
policy_name     => 'ZC_EB_EXPERT_SESSION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_FORMULA--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_FORMULA',
policy_name     => 'ZC_EB_FORMULA',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_FORMULA_ITEM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_FORMULA_ITEM',
policy_name     => 'ZC_EB_FORMULA_ITEM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_FORMULA_ITEM_TEMPLATE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_FORMULA_ITEM_TEMPLATE',
policy_name     => 'ZC_EB_FORMULA_ITEM_TEMPLATE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_FORMULA_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_FORMULA_PACK',
policy_name     => 'ZC_EB_FORMULA_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_FORMULA_PARAM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_FORMULA_PARAM',
policy_name     => 'ZC_EB_FORMULA_PARAM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_FORMULA_TEMPLATE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_FORMULA_TEMPLATE',
policy_name     => 'ZC_EB_FORMULA_TEMPLATE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_GONGHUO_HT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_GONGHUO_HT',
policy_name     => 'ZC_EB_GONGHUO_HT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_GUIDANG_BILL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_GUIDANG_BILL',
policy_name     => 'ZC_EB_GUIDANG_BILL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_GUIDANG_ITEM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_GUIDANG_ITEM',
policy_name     => 'ZC_EB_GUIDANG_ITEM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_NOTICE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_NOTICE',
policy_name     => 'ZC_EB_NOTICE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_OPENBIDTEAM_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_OPENBIDTEAM_PACK',
policy_name     => 'ZC_EB_OPENBIDTEAM_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_OPENBID_PERSON--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_OPENBID_PERSON',
policy_name     => 'ZC_EB_OPENBID_PERSON',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_OPENBID_TEAM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_OPENBID_TEAM',
policy_name     => 'ZC_EB_OPENBID_TEAM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_OPENBID_TEAM_MEMBER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_OPENBID_TEAM_MEMBER',
policy_name     => 'ZC_EB_OPENBID_TEAM_MEMBER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_OPTIONS--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_OPTIONS',
policy_name     => 'ZC_EB_OPTIONS',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PACK',
policy_name     => 'ZC_EB_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PACK_EVAL_RESULT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PACK_EVAL_RESULT',
policy_name     => 'ZC_EB_PACK_EVAL_RESULT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PACK_EXPERT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PACK_EXPERT',
policy_name     => 'ZC_EB_PACK_EXPERT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PACK_PLAN--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PACK_PLAN',
policy_name     => 'ZC_EB_PACK_PLAN',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PACK_PRE_AUDIT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PACK_PRE_AUDIT',
policy_name     => 'ZC_EB_PACK_PRE_AUDIT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PACK_QUA--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PACK_QUA',
policy_name     => 'ZC_EB_PACK_QUA',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PACK_REQ--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PACK_REQ',
policy_name     => 'ZC_EB_PACK_REQ',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PACK_REQ_DETAIL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PACK_REQ_DETAIL',
policy_name     => 'ZC_EB_PACK_REQ_DETAIL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PACK_SUM_CALCULATOR--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PACK_SUM_CALCULATOR',
policy_name     => 'ZC_EB_PACK_SUM_CALCULATOR',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PACK_SUPPLIER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PACK_SUPPLIER',
policy_name     => 'ZC_EB_PACK_SUPPLIER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PLAN--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PLAN',
policy_name     => 'ZC_EB_PLAN',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PRE_AUDIT_ITEM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PRE_AUDIT_ITEM',
policy_name     => 'ZC_EB_PRE_AUDIT_ITEM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PRE_AUDIT_RESULT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PRE_AUDIT_RESULT',
policy_name     => 'ZC_EB_PRE_AUDIT_RESULT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PRE_AUDIT_RESULT_ITEM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PRE_AUDIT_RESULT_ITEM',
policy_name     => 'ZC_EB_PRE_AUDIT_RESULT_ITEM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PROJ--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PROJ',
policy_name     => 'ZC_EB_PROJ',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PROJECT_LIVING_CHANGE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PROJECT_LIVING_CHANGE',
policy_name     => 'ZC_EB_PROJECT_LIVING_CHANGE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PROJ_CHG--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PROJ_CHG',
policy_name     => 'ZC_EB_PROJ_CHG',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PROJ_CHG_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PROJ_CHG_PACK',
policy_name     => 'ZC_EB_PROJ_CHG_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PROJ_PRINT_PERMIT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PROJ_PRINT_PERMIT',
policy_name     => 'ZC_EB_PROJ_PRINT_PERMIT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PROJ_ZBFILE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PROJ_ZBFILE',
policy_name     => 'ZC_EB_PROJ_ZBFILE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PROMAKE_OUTER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PROMAKE_OUTER',
policy_name     => 'ZC_EB_PROMAKE_OUTER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PROMAKE_OUTER_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PROMAKE_OUTER_PACK',
policy_name     => 'ZC_EB_PROMAKE_OUTER_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PROTOCOL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PROTOCOL',
policy_name     => 'ZC_EB_PROTOCOL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PRO_ARGUE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PRO_ARGUE',
policy_name     => 'ZC_EB_PRO_ARGUE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PRO_ARGUE_DETAIL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PRO_ARGUE_DETAIL',
policy_name     => 'ZC_EB_PRO_ARGUE_DETAIL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_PRO_COMPLETION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_PRO_COMPLETION',
policy_name     => 'ZC_EB_PRO_COMPLETION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_QUESTION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_QUESTION',
policy_name     => 'ZC_EB_QUESTION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_QUESTION_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_QUESTION_PACK',
policy_name     => 'ZC_EB_QUESTION_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_REQUIREMENT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_REQUIREMENT',
policy_name     => 'ZC_EB_REQUIREMENT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_REQUIREMENT_CONFIRM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_REQUIREMENT_CONFIRM',
policy_name     => 'ZC_EB_REQUIREMENT_CONFIRM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_REQUIREMENT_DETAIL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_REQUIREMENT_DETAIL',
policy_name     => 'ZC_EB_REQUIREMENT_DETAIL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_REQ_CHANGE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_REQ_CHANGE',
policy_name     => 'ZC_EB_REQ_CHANGE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_REQ_FILE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_REQ_FILE',
policy_name     => 'ZC_EB_REQ_FILE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_REQ_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_REQ_PACK',
policy_name     => 'ZC_EB_REQ_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_RFQ_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_RFQ_PACK',
policy_name     => 'ZC_EB_RFQ_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_SECURITY_RECORD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_SECURITY_RECORD',
policy_name     => 'ZC_EB_SECURITY_RECORD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_SIGNUP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_SIGNUP',
policy_name     => 'ZC_EB_SIGNUP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_SIGNUP_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_SIGNUP_PACK',
policy_name     => 'ZC_EB_SIGNUP_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_SUP_BS_TYPE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_SUP_BS_TYPE',
policy_name     => 'ZC_EB_SUP_BS_TYPE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_SUP_PRICE_DETAIL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_SUP_PRICE_DETAIL',
policy_name     => 'ZC_EB_SUP_PRICE_DETAIL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_SUP_QUALIFICATION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_SUP_QUALIFICATION',
policy_name     => 'ZC_EB_SUP_QUALIFICATION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_SUP_QUALIFICATION_LEV--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_SUP_QUALIFICATION_LEV',
policy_name     => 'ZC_EB_SUP_QUALIFICATION_LEV',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_TEMPLATE_PARAM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_TEMPLATE_PARAM',
policy_name     => 'ZC_EB_TEMPLATE_PARAM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_WEN_HAO_TOOL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_WEN_HAO_TOOL',
policy_name     => 'ZC_EB_WEN_HAO_TOOL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_XB_PERSION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_XB_PERSION',
policy_name     => 'ZC_EB_XB_PERSION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_XUNJIA--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_XUNJIA',
policy_name     => 'ZC_EB_XUNJIA',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_XUNJIA_BAOJIA--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_XUNJIA_BAOJIA',
policy_name     => 'ZC_EB_XUNJIA_BAOJIA',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_XUNJIA_BAOJIA_DETAIL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_XUNJIA_BAOJIA_DETAIL',
policy_name     => 'ZC_EB_XUNJIA_BAOJIA_DETAIL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_XUNJIA_BAOJIA_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_XUNJIA_BAOJIA_PACK',
policy_name     => 'ZC_EB_XUNJIA_BAOJIA_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_XY_BAOJIA--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_XY_BAOJIA',
policy_name     => 'ZC_EB_XY_BAOJIA',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_YANSHOU_BILL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_YANSHOU_BILL',
policy_name     => 'ZC_EB_YANSHOU_BILL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_YANSHOU_BILL_ITEM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_YANSHOU_BILL_ITEM',
policy_name     => 'ZC_EB_YANSHOU_BILL_ITEM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_ZB--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_ZB',
policy_name     => 'ZC_EB_ZB',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EB_ZB_ITEM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EB_ZB_ITEM',
policy_name     => 'ZC_EB_ZB_ITEM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_ELEMENT_CONFIG--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_ELEMENT_CONFIG',
policy_name     => 'ZC_ELEMENT_CONFIG',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EM_B_EXPERT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EM_B_EXPERT',
policy_name     => 'ZC_EM_B_EXPERT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EM_EXPERT_ABILITY_HISTORY--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EM_EXPERT_ABILITY_HISTORY',
policy_name     => 'ZC_EM_EXPERT_ABILITY_HISTORY',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EM_EXPERT_EVALUATION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EM_EXPERT_EVALUATION',
policy_name     => 'ZC_EM_EXPERT_EVALUATION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EM_EXPERT_PRO_BILL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EM_EXPERT_PRO_BILL',
policy_name     => 'ZC_EM_EXPERT_PRO_BILL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EM_EXPERT_PRO_BILL_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EM_EXPERT_PRO_BILL_PACK',
policy_name     => 'ZC_EM_EXPERT_PRO_BILL_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_EM_EXPERT_TYPE_JOIN--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_EM_EXPERT_TYPE_JOIN',
policy_name     => 'ZC_EM_EXPERT_TYPE_JOIN',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_FILE_RESUME_BROKEN_UPLOAD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_FILE_RESUME_BROKEN_UPLOAD',
policy_name     => 'ZC_FILE_RESUME_BROKEN_UPLOAD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_FN_R_STOCK_FUNC_L--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_FN_R_STOCK_FUNC_L',
policy_name     => 'ZC_FN_R_STOCK_FUNC_L',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_GEN_DESC_SQL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_GEN_DESC_SQL',
policy_name     => 'ZC_GEN_DESC_SQL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_HT_PRE_PAY_BILL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_HT_PRE_PAY_BILL',
policy_name     => 'ZC_HT_PRE_PAY_BILL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_HT_PRE_PAY_BILL_ITEM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_HT_PRE_PAY_BILL_ITEM',
policy_name     => 'ZC_HT_PRE_PAY_BILL_ITEM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_HT_PRE_PAY_BILL_ITEM_DTL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_HT_PRE_PAY_BILL_ITEM_DTL',
policy_name     => 'ZC_HT_PRE_PAY_BILL_ITEM_DTL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_NOTEPAD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_NOTEPAD',
policy_name     => 'ZC_NOTEPAD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_NUM_NO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_NUM_NO',
policy_name     => 'ZC_NUM_NO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_NUM_TOOL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_NUM_TOOL',
policy_name     => 'ZC_NUM_TOOL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_PKG_PROJECT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_PKG_PROJECT',
policy_name     => 'ZC_PKG_PROJECT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_PORTAL_CATALOGUE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_PORTAL_CATALOGUE',
policy_name     => 'ZC_PORTAL_CATALOGUE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_P_BIDSUPPLIER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_P_BIDSUPPLIER',
policy_name     => 'ZC_P_BIDSUPPLIER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_P_BIDSUPPLIER_BI--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_P_BIDSUPPLIER_BI',
policy_name     => 'ZC_P_BIDSUPPLIER_BI',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_P_CHARGEOFF--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_P_CHARGEOFF',
policy_name     => 'ZC_P_CHARGEOFF',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_P_CHARGEOFF_BI--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_P_CHARGEOFF_BI',
policy_name     => 'ZC_P_CHARGEOFF_BI',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_P_CONTRACT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_P_CONTRACT',
policy_name     => 'ZC_P_CONTRACT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_P_PRO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_P_PRO',
policy_name     => 'ZC_P_PRO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_P_PRO_BAL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_P_PRO_BAL',
policy_name     => 'ZC_P_PRO_BAL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_P_PRO_BAL_BI--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_P_PRO_BAL_BI',
policy_name     => 'ZC_P_PRO_BAL_BI',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_P_PRO_BAL_CHG--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_P_PRO_BAL_CHG',
policy_name     => 'ZC_P_PRO_BAL_CHG',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_P_PRO_BAL_CHG_BI--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_P_PRO_BAL_CHG_BI',
policy_name     => 'ZC_P_PRO_BAL_CHG_BI',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_P_PRO_MAKE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_P_PRO_MAKE',
policy_name     => 'ZC_P_PRO_MAKE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_P_PRO_MAKE_SUPPLEMENT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_P_PRO_MAKE_SUPPLEMENT',
policy_name     => 'ZC_P_PRO_MAKE_SUPPLEMENT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_P_PRO_MAKE_SUPPLEMENT_BI--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_P_PRO_MAKE_SUPPLEMENT_BI',
policy_name     => 'ZC_P_PRO_MAKE_SUPPLEMENT_BI',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_P_PRO_MITEM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_P_PRO_MITEM',
policy_name     => 'ZC_P_PRO_MITEM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_P_PRO_MITEM_BI--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_P_PRO_MITEM_BI',
policy_name     => 'ZC_P_PRO_MITEM_BI',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_P_PRO_MITEM_BI_HISTORY--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_P_PRO_MITEM_BI_HISTORY',
policy_name     => 'ZC_P_PRO_MITEM_BI_HISTORY',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_P_PRO_MITEM_MER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_P_PRO_MITEM_MER',
policy_name     => 'ZC_P_PRO_MITEM_MER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_P_PRO_RETURN_BI--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_P_PRO_RETURN_BI',
policy_name     => 'ZC_P_PRO_RETURN_BI',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_QB--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_QB',
policy_name     => 'ZC_QB',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_QB_BI--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_QB_BI',
policy_name     => 'ZC_QB_BI',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_QB_ITEM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_QB_ITEM',
policy_name     => 'ZC_QB_ITEM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_QX--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_QX',
policy_name     => 'ZC_QX',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_QX_BI--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_QX_BI',
policy_name     => 'ZC_QX_BI',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_QX_ITEM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_QX_ITEM',
policy_name     => 'ZC_QX_ITEM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_REAL_MESSAGE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_REAL_MESSAGE',
policy_name     => 'ZC_REAL_MESSAGE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_ROLE_SEARCH_CONDITION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_ROLE_SEARCH_CONDITION',
policy_name     => 'ZC_ROLE_SEARCH_CONDITION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_SEARCH_CONDITION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_SEARCH_CONDITION',
policy_name     => 'ZC_SEARCH_CONDITION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_SP_ITEM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_SP_ITEM',
policy_name     => 'ZC_SP_ITEM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_SUP_QUA_TYPE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_SUP_QUA_TYPE',
policy_name     => 'ZC_SUP_QUA_TYPE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_SYS_BILL_ELEMENT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_SYS_BILL_ELEMENT',
policy_name     => 'ZC_SYS_BILL_ELEMENT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_SYS_NUM_LIM_COMPO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_SYS_NUM_LIM_COMPO',
policy_name     => 'ZC_SYS_NUM_LIM_COMPO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_SYS_VAL_TRANSITION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_SYS_VAL_TRANSITION',
policy_name     => 'ZC_SYS_VAL_TRANSITION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_T_BCHT_ITEM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_T_BCHT_ITEM',
policy_name     => 'ZC_T_BCHT_ITEM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_USER_MESSAGE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_USER_MESSAGE',
policy_name     => 'ZC_USER_MESSAGE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_USER_PREFERENCES--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_USER_PREFERENCES',
policy_name     => 'ZC_USER_PREFERENCES',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_USER_RULE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_USER_RULE',
policy_name     => 'ZC_USER_RULE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_USER_SEARCH_CONDITION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_USER_SEARCH_CONDITION',
policy_name     => 'ZC_USER_SEARCH_CONDITION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WCMS_CHANNEL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WCMS_CHANNEL',
policy_name     => 'ZC_WCMS_CHANNEL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WCMS_CONTENT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WCMS_CONTENT',
policy_name     => 'ZC_WCMS_CONTENT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WCMS_CONTENT_DECLARATION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WCMS_CONTENT_DECLARATION',
policy_name     => 'ZC_WCMS_CONTENT_DECLARATION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WCMS_CONTENT_LAW--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WCMS_CONTENT_LAW',
policy_name     => 'ZC_WCMS_CONTENT_LAW',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WCMS_CONTENT_NEW--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WCMS_CONTENT_NEW',
policy_name     => 'ZC_WCMS_CONTENT_NEW',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WCMS_CONTENT_PRODUCT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WCMS_CONTENT_PRODUCT',
policy_name     => 'ZC_WCMS_CONTENT_PRODUCT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WCMS_COUPLET--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WCMS_COUPLET',
policy_name     => 'ZC_WCMS_COUPLET',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WCMS_DECLARATION_REGION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WCMS_DECLARATION_REGION',
policy_name     => 'ZC_WCMS_DECLARATION_REGION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WCMS_LAWTYPE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WCMS_LAWTYPE',
policy_name     => 'ZC_WCMS_LAWTYPE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WCMS_NEWCHANNEL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WCMS_NEWCHANNEL',
policy_name     => 'ZC_WCMS_NEWCHANNEL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WCMS_NEWCHANNEL_USER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WCMS_NEWCHANNEL_USER',
policy_name     => 'ZC_WCMS_NEWCHANNEL_USER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WCMS_PAGE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WCMS_PAGE',
policy_name     => 'ZC_WCMS_PAGE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WCMS_USER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WCMS_USER',
policy_name     => 'ZC_WCMS_USER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WCMS_USER_REGION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WCMS_USER_REGION',
policy_name     => 'ZC_WCMS_USER_REGION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WCMS_ZX_ARTICLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WCMS_ZX_ARTICLE',
policy_name     => 'ZC_WCMS_ZX_ARTICLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WCMS_ZX_ARTICLE_ANSWER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WCMS_ZX_ARTICLE_ANSWER',
policy_name     => 'ZC_WCMS_ZX_ARTICLE_ANSWER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WF_CURRENT_TASK_CANCEL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WF_CURRENT_TASK_CANCEL',
policy_name     => 'ZC_WF_CURRENT_TASK_CANCEL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WORK_PLAN--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WORK_PLAN',
policy_name     => 'ZC_WORK_PLAN',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WORK_PLAN_DETAIL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WORK_PLAN_DETAIL',
policy_name     => 'ZC_WORK_PLAN_DETAIL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WORK_PLAN_DYNAMIC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WORK_PLAN_DYNAMIC',
policy_name     => 'ZC_WORK_PLAN_DYNAMIC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_WORK_PLAN_DYNAMIC_DETAIL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_WORK_PLAN_DYNAMIC_DETAIL',
policy_name     => 'ZC_WORK_PLAN_DYNAMIC_DETAIL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_XMCG_HT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_XMCG_HT',
policy_name     => 'ZC_XMCG_HT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_XMCG_HT_BI--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_XMCG_HT_BI',
policy_name     => 'ZC_XMCG_HT_BI',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_XYGH_SPPC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_XYGH_SPPC',
policy_name     => 'ZC_XYGH_SPPC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_ZB_PINP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_ZB_PINP',
policy_name     => 'ZC_ZB_PINP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_ZB_TEMPLATES--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_ZB_TEMPLATES',
policy_name     => 'ZC_ZB_TEMPLATES',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_ZJGC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_ZJGC',
policy_name     => 'ZC_ZJGC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--AS_COMPANY--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'AS_COMPANY',
policy_name     => 'AS_COMPANY',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--EM_VS_BILL_RULES--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'EM_VS_BILL_RULES',
policy_name     => 'EM_VS_BILL_RULES',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--SELECT_PROJECT_FOR_CONTRACT_V--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'SELECT_PROJECT_FOR_CONTRACT_V',
policy_name     => 'SELECT_PROJECT_FOR_CONTRACT_V',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--VW_BUDGET_GB--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'VW_BUDGET_GB',
policy_name     => 'VW_BUDGET_GB',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--VW_BUDGET_SHOURU--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'VW_BUDGET_SHOURU',
policy_name     => 'VW_BUDGET_SHOURU',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--VW_E_ACC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'VW_E_ACC',
policy_name     => 'VW_E_ACC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--VW_GP_BUDGET--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'VW_GP_BUDGET',
policy_name     => 'VW_GP_BUDGET',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AP_ARTICLE_PUB--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AP_ARTICLE_PUB',
policy_name     => 'V_AP_ARTICLE_PUB',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AP_GROUP_PAGE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AP_GROUP_PAGE',
policy_name     => 'V_AP_GROUP_PAGE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AP_MENU--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AP_MENU',
policy_name     => 'V_AP_MENU',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AP_MENU_COMPO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AP_MENU_COMPO',
policy_name     => 'V_AP_MENU_COMPO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_COCODE_USER--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_COCODE_USER',
policy_name     => 'V_AS_COCODE_USER',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_COCODE_ZH--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_COCODE_ZH',
policy_name     => 'V_AS_COCODE_ZH',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_COMPO_FUNC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_COMPO_FUNC',
policy_name     => 'V_AS_COMPO_FUNC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_EMP_FUNC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_EMP_FUNC',
policy_name     => 'V_AS_EMP_FUNC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_MENU_COMPO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_MENU_COMPO',
policy_name     => 'V_AS_MENU_COMPO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_NO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_NO',
policy_name     => 'V_AS_NO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_ORGANIZATION_NEW_TREE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_ORGANIZATION_NEW_TREE',
policy_name     => 'V_AS_ORGANIZATION_NEW_TREE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_ORGANIZATION_TREE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_ORGANIZATION_TREE',
policy_name     => 'V_AS_ORGANIZATION_TREE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_ORGANIZATION_TREE_OLD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_ORGANIZATION_TREE_OLD',
policy_name     => 'V_AS_ORGANIZATION_TREE_OLD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_ORGBACC_TREE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_ORGBACC_TREE',
policy_name     => 'V_AS_ORGBACC_TREE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_ORG_POSITION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_ORG_POSITION',
policy_name     => 'V_AS_ORG_POSITION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_ROLE_FUNC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_ROLE_FUNC',
policy_name     => 'V_AS_ROLE_FUNC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_SA_FUNC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_SA_FUNC',
policy_name     => 'V_AS_SA_FUNC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_STAFF--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_STAFF',
policy_name     => 'V_AS_STAFF',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_USER_CO_CODE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_USER_CO_CODE',
policy_name     => 'V_AS_USER_CO_CODE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_USER_GROUP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_USER_GROUP',
policy_name     => 'V_AS_USER_GROUP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_USER_ROLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_USER_ROLE',
policy_name     => 'V_AS_USER_ROLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_USR_FUNC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_USR_FUNC',
policy_name     => 'V_AS_USR_FUNC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_USR_NUM_LIM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_USR_NUM_LIM',
policy_name     => 'V_AS_USR_NUM_LIM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_AS_USR_ROLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_AS_USR_ROLE',
policy_name     => 'V_AS_USR_ROLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_CP_ORG--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_CP_ORG',
policy_name     => 'V_CP_ORG',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_MA_DEPT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_MA_DEPT',
policy_name     => 'V_MA_DEPT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_SYS_AS--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_SYS_AS',
policy_name     => 'V_SYS_AS',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_SYS_AS_EMP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_SYS_AS_EMP',
policy_name     => 'V_SYS_AS_EMP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_SYS_AS_EMP_POSITION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_SYS_AS_EMP_POSITION',
policy_name     => 'V_SYS_AS_EMP_POSITION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_SYS_AS_ORG--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_SYS_AS_ORG',
policy_name     => 'V_SYS_AS_ORG',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_SYS_AS_ORG_POSITION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_SYS_AS_ORG_POSITION',
policy_name     => 'V_SYS_AS_ORG_POSITION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_SYS_AS_POSITION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_SYS_AS_POSITION',
policy_name     => 'V_SYS_AS_POSITION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_SYS_AS_POSI_ROLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_SYS_AS_POSI_ROLE',
policy_name     => 'V_SYS_AS_POSI_ROLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_SYS_AS_ROLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_SYS_AS_ROLE',
policy_name     => 'V_SYS_AS_ROLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_SYS_AS_ROLE_FUNC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_SYS_AS_ROLE_FUNC',
policy_name     => 'V_SYS_AS_ROLE_FUNC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_SYS_AS_ROLE_NUM_LIM--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_SYS_AS_ROLE_NUM_LIM',
policy_name     => 'V_SYS_AS_ROLE_NUM_LIM',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_SYS_AS_STATINFO_IP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_SYS_AS_STATINFO_IP',
policy_name     => 'V_SYS_AS_STATINFO_IP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_SYS_AS_USER_GROUP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_SYS_AS_USER_GROUP',
policy_name     => 'V_SYS_AS_USER_GROUP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_SYS_AS_USER_ROLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_SYS_AS_USER_ROLE',
policy_name     => 'V_SYS_AS_USER_ROLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_SYS_AS_WF_BUSINESS_SUPERIOR--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_SYS_AS_WF_BUSINESS_SUPERIOR',
policy_name     => 'V_SYS_AS_WF_BUSINESS_SUPERIOR',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_SYS_MA_COMPANY--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_SYS_MA_COMPANY',
policy_name     => 'V_SYS_MA_COMPANY',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_SYS_MA_DICT_ORG_CO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_SYS_MA_DICT_ORG_CO',
policy_name     => 'V_SYS_MA_DICT_ORG_CO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_WF_ACTION_HISTORY--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_WF_ACTION_HISTORY',
policy_name     => 'V_WF_ACTION_HISTORY',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_WF_ACTION_HISTORY_CGCJB--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_WF_ACTION_HISTORY_CGCJB',
policy_name     => 'V_WF_ACTION_HISTORY_CGCJB',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_WF_ACTION_HISTORY_GK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_WF_ACTION_HISTORY_GK',
policy_name     => 'V_WF_ACTION_HISTORY_GK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_WF_ACTION_HISTORY_GK53--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_WF_ACTION_HISTORY_GK53',
policy_name     => 'V_WF_ACTION_HISTORY_GK53',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_WF_ACTION_HISTORY_INVALID--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_WF_ACTION_HISTORY_INVALID',
policy_name     => 'V_WF_ACTION_HISTORY_INVALID',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_WF_ACTION_HISTORY_INVALID_GK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_WF_ACTION_HISTORY_INVALID_GK',
policy_name     => 'V_WF_ACTION_HISTORY_INVALID_GK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_WF_CURRENT_TASK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_WF_CURRENT_TASK',
policy_name     => 'V_WF_CURRENT_TASK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_WF_CURRENT_TASK_GK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_WF_CURRENT_TASK_GK',
policy_name     => 'V_WF_CURRENT_TASK_GK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_WF_CURRENT_TASK_GK53--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_WF_CURRENT_TASK_GK53',
policy_name     => 'V_WF_CURRENT_TASK_GK53',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_WF_CURRENT_TASK_INVALID--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_WF_CURRENT_TASK_INVALID',
policy_name     => 'V_WF_CURRENT_TASK_INVALID',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_WF_CURRENT_TASK_INVALID_GK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_WF_CURRENT_TASK_INVALID_GK',
policy_name     => 'V_WF_CURRENT_TASK_INVALID_GK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_WF_CURRENT_TASK_ZC_CANCEL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_WF_CURRENT_TASK_ZC_CANCEL',
policy_name     => 'V_WF_CURRENT_TASK_ZC_CANCEL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_WF_CURRENT_TASK_ZC_TODO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_WF_CURRENT_TASK_ZC_TODO',
policy_name     => 'V_WF_CURRENT_TASK_ZC_TODO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_WF_CURRENT_TASK_ZC_UNTREAD--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_WF_CURRENT_TASK_ZC_UNTREAD',
policy_name     => 'V_WF_CURRENT_TASK_ZC_UNTREAD',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_WF_EXECUTOR_SOURCE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_WF_EXECUTOR_SOURCE',
policy_name     => 'V_WF_EXECUTOR_SOURCE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_WF_EXECUTOR_SOURCE1--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_WF_EXECUTOR_SOURCE1',
policy_name     => 'V_WF_EXECUTOR_SOURCE1',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_BULLETIN_CHG_BASE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_BULLETIN_CHG_BASE',
policy_name     => 'V_ZC_EB_BULLETIN_CHG_BASE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_BULLETIN_DYLY--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_BULLETIN_DYLY',
policy_name     => 'V_ZC_EB_BULLETIN_DYLY',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_BULLETIN_OPN--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_BULLETIN_OPN',
policy_name     => 'V_ZC_EB_BULLETIN_OPN',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_BULLETIN_REPORT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_BULLETIN_REPORT',
policy_name     => 'V_ZC_EB_BULLETIN_REPORT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_BULLETIN_REPORT_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_BULLETIN_REPORT_PACK',
policy_name     => 'V_ZC_EB_BULLETIN_REPORT_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_BULLETIN_SPD_BASE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_BULLETIN_SPD_BASE',
policy_name     => 'V_ZC_EB_BULLETIN_SPD_BASE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_BULLETIN_WID_BASE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_BULLETIN_WID_BASE',
policy_name     => 'V_ZC_EB_BULLETIN_WID_BASE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_BULLETIN_WID_CO_INFO--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_BULLETIN_WID_CO_INFO',
policy_name     => 'V_ZC_EB_BULLETIN_WID_CO_INFO',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_BULLETIN_WID_EXP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_BULLETIN_WID_EXP',
policy_name     => 'V_ZC_EB_BULLETIN_WID_EXP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_BULLETIN_WID_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_BULLETIN_WID_PACK',
policy_name     => 'V_ZC_EB_BULLETIN_WID_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_BULLETIN_ZBTZ--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_BULLETIN_ZBTZ',
policy_name     => 'V_ZC_EB_BULLETIN_ZBTZ',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_BULLETIN_ZB_HUOWU--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_BULLETIN_ZB_HUOWU',
policy_name     => 'V_ZC_EB_BULLETIN_ZB_HUOWU',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_BULLETIN_ZB_JG--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_BULLETIN_ZB_JG',
policy_name     => 'V_ZC_EB_BULLETIN_ZB_JG',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_CHG_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_CHG_PACK',
policy_name     => 'V_ZC_EB_CHG_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_ENTRUSTITEMNAME_EXP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_ENTRUSTITEMNAME_EXP',
policy_name     => 'V_ZC_EB_ENTRUSTITEMNAME_EXP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_EVAL_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_EVAL_PACK',
policy_name     => 'V_ZC_EB_EVAL_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_EVAL_RESULT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_EVAL_RESULT',
policy_name     => 'V_ZC_EB_EVAL_RESULT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_EXPERT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_EXPERT',
policy_name     => 'V_ZC_EB_EXPERT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_PACK',
policy_name     => 'V_ZC_EB_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_PACK_PLAN--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_PACK_PLAN',
policy_name     => 'V_ZC_EB_PACK_PLAN',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_PACK_PRE_AUDIT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_PACK_PRE_AUDIT',
policy_name     => 'V_ZC_EB_PACK_PRE_AUDIT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_PROJECT_SUPPORT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_PROJECT_SUPPORT',
policy_name     => 'V_ZC_EB_PROJECT_SUPPORT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_PROJ_PACK_EXPERT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_PROJ_PACK_EXPERT',
policy_name     => 'V_ZC_EB_PROJ_PACK_EXPERT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_PROTOCOL_EXP--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_PROTOCOL_EXP',
policy_name     => 'V_ZC_EB_PROTOCOL_EXP',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EB_XUNJIA_BAOJIA_SUMMARY--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EB_XUNJIA_BAOJIA_SUMMARY',
policy_name     => 'V_ZC_EB_XUNJIA_BAOJIA_SUMMARY',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_EXPERT_TYPE_SELECTION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_EXPERT_TYPE_SELECTION',
policy_name     => 'V_ZC_EXPERT_TYPE_SELECTION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_NORMAL_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_NORMAL_PACK',
policy_name     => 'V_ZC_NORMAL_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_ORG--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_ORG',
policy_name     => 'V_ZC_ORG',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_PROJ_PACK_DETAIL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_PROJ_PACK_DETAIL',
policy_name     => 'V_ZC_PROJ_PACK_DETAIL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_PRO_MAKE_EXPERT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_PRO_MAKE_EXPERT',
policy_name     => 'V_ZC_PRO_MAKE_EXPERT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_P_PRO_MAKE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_P_PRO_MAKE',
policy_name     => 'V_ZC_P_PRO_MAKE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_REPORT_FORM_AND_MODE_AUX--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_REPORT_FORM_AND_MODE_AUX',
policy_name     => 'V_ZC_REPORT_FORM_AND_MODE_AUX',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_REPORT_FORM_AND_MODE_AUX1--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_REPORT_FORM_AND_MODE_AUX1',
policy_name     => 'V_ZC_REPORT_FORM_AND_MODE_AUX1',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_REPORT_FORM_AND_MODE_AUX2--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_REPORT_FORM_AND_MODE_AUX2',
policy_name     => 'V_ZC_REPORT_FORM_AND_MODE_AUX2',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_REPORT_HT_ITEM_933--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_REPORT_HT_ITEM_933',
policy_name     => 'V_ZC_REPORT_HT_ITEM_933',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_XUNJIA_BAOJIA_DETAIL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_XUNJIA_BAOJIA_DETAIL',
policy_name     => 'V_ZC_XUNJIA_BAOJIA_DETAIL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_XUNJIA_OPEN_BID--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_XUNJIA_OPEN_BID',
policy_name     => 'V_ZC_XUNJIA_OPEN_BID',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_ZTB_PROJECT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_ZTB_PROJECT',
policy_name     => 'V_ZC_ZTB_PROJECT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_ZX_BJMX--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_ZX_BJMX',
policy_name     => 'V_ZC_ZX_BJMX',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_ZX_HTMXDR--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_ZX_HTMXDR',
policy_name     => 'V_ZC_ZX_HTMXDR',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_ZX_RWSML--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_ZX_RWSML',
policy_name     => 'V_ZC_ZX_RWSML',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_ZX_XXFBQKTJBND--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_ZX_XXFBQKTJBND',
policy_name     => 'V_ZC_ZX_XXFBQKTJBND',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_ZX_XXFBQKTJBYWC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_ZX_XXFBQKTJBYWC',
policy_name     => 'V_ZC_ZX_XXFBQKTJBYWC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_ZX_YWCWCQKDBB--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_ZX_YWCWCQKDBB',
policy_name     => 'V_ZC_ZX_YWCWCQKDBB',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_ZX_ZJJJQKTJBND--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_ZX_ZJJJQKTJBND',
policy_name     => 'V_ZC_ZX_ZJJJQKTJBND',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--V_ZC_ZX_ZJJJQKTJBYWC--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'V_ZC_ZX_ZJJJQKTJBYWC',
policy_name     => 'V_ZC_ZX_ZJJJQKTJBYWC',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_COMPANY--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_COMPANY',
policy_name     => 'WF_COMPANY',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_ORG--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_ORG',
policy_name     => 'WF_ORG',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_ORG_POSITION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_ORG_POSITION',
policy_name     => 'WF_ORG_POSITION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_ORG_POSITION_LEVEL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_ORG_POSITION_LEVEL',
policy_name     => 'WF_ORG_POSITION_LEVEL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_POSITION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_POSITION',
policy_name     => 'WF_POSITION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_POSITION_ROLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_POSITION_ROLE',
policy_name     => 'WF_POSITION_ROLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_ROLE--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_ROLE',
policy_name     => 'WF_ROLE',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_STAFF--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_STAFF',
policy_name     => 'WF_STAFF',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--WF_STAFF_POSITION--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'WF_STAFF_POSITION',
policy_name     => 'WF_STAFF_POSITION',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_V_OPEN_BID--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_V_OPEN_BID',
policy_name     => 'ZC_V_OPEN_BID',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_V_PRO_PACK--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_V_PRO_PACK',
policy_name     => 'ZC_V_PRO_PACK',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_V_R_CONTRACT_INPUT--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_V_R_CONTRACT_INPUT',
policy_name     => 'ZC_V_R_CONTRACT_INPUT',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_V_R_CONTRACT_INPUT_DETAIL--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_V_R_CONTRACT_INPUT_DETAIL',
policy_name     => 'ZC_V_R_CONTRACT_INPUT_DETAIL',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

--ZC_V_YEAREND_PLAN--
begin
dbms_fga.add_policy (
object_schema   => 'DTRUN',
object_name     => 'ZC_V_YEAREND_PLAN',
policy_name     => 'ZC_V_YEAREND_PLAN',
statement_types => 'INSERT, UPDATE, DELETE, SELECT');
end;
/

SET SERVEROUTPUT OFF
spool off