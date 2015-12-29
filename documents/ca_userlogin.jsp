<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="ideabank.sap.*" %>
<%@ page import="javax.naming.Context" %> 
<%@ page import="javax.sql.DataSource"%> 
<%@ page import="javax.naming.InitialContext"%> 
<%@ page import="java.sql.*"%> 
<%@ page import="com.ufgov.gmap.pub.*"%> 
<%

	boolean login=false;
	String errorStr="";
	String caSerial="";
	String userId="";
	String userPw="";
	
		IAccount cc = new IAccount(request,response);
	cc.SetManager("61.177.144.130","61.177.144.130","test","289343554346847174302288","localhost:7003");
	cc.SetExecUrl("C:/php5.2.5/");
	try{
		if(cc.CheckCertLogin()){		
			UserBean bean = cc.getUserBean();
			caSerial=bean.getUserID();
			//out.println(caSerial+"<br>");
		}else{
			errorStr="CA无法通过校验!!";
			out.println(errorStr+"<br>");
		}
	}catch(Exception e){
		out.println("CA校验异常!<br>"+e.getMessage());
		errorStr="CA校验异常!";
	}
	if(caSerial!=null && caSerial.trim().length()>0){		
		Statement stmt = null;
		ResultSet rst = null; 
		String str_ts=null;
		String sql_str=null;
		try {
			Context ctx =new InitialContext();
			javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup ("webglDS");
			if(ds!=null) 
			 {
					//out.println("已经获得DataSource!"); 
					//out.println("<br>");
					java.sql.Connection conn = ds.getConnection();
			    stmt=conn.createStatement();
			    sql_str="select e.user_id from as_emp e where e.ca_serial='"+caSerial+"'";			
			    //out.println( sql_str+"<br>");   
			  	rst=stmt.executeQuery(sql_str);
					while(rst.next())
			    {
			    	userId=rst.getString(1);
			    	//out.println(userId+" "+userPw+"<br>");
			    }
			 		if(userId!=null && userId.trim().length()>0){			 			
						sql_str="select u.user_id,u.passwd from as_user u where u.user_id='"+userId+"'";			
			    	//out.println( sql_str+"<br>");   
				  	rst=stmt.executeQuery(sql_str); 
				  	while(rst.next())
				    {
			       userId=rst.getString(1);
			       userPw=rst.getString(2);
			       if(userPw!=null){
			       	userPw=GeneralFunc.recodePwd(userPw);
			     	}else{
			     		userPw="";
			     	}
			       //out.println(userId+" "+userPw+"<br>");
				     }
			 		}else{
			 			errorStr="当前key未和采购系统进行绑定，无法登陆，请联系采购系统管理员!!";
			 		}			 		
			 		rst.close();
			 		stmt.close();
			 		conn.close();
			  }else{ 
			 		out.println("数据库连接失败!"); 
			 		errorStr="数据库连接失败!";
			 }
			}catch(Exception ne)
			{
		 		out.println("登陆异常:"+ne.getMessage());
		 		errorStr="登陆异常!!";
		 }
	}else{
		errorStr="ca序列号为空，请联系系统管理员!";
	}
		
%>

	<script language=javascript event=OnObjectReady(objObject,objAsyncContext) for=foo> 
		if(objObject.IPEnabled != null && objObject.IPEnabled != "undefined" && objObject.IPEnabled == true){
			var rowData = new Array();
		    if(objObject.MACAddress != null && objObject.MACAddress != "undefined")
		    	rowData[0] = objObject.MACAddress;
		    if(objObject.IPEnabled && objObject.IPAddress(0) != null && objObject.IPAddress(0) != "undefined")
		    	rowData[1] = objObject.IPAddress(0);
		    ipData[ipData.length] = rowData;
	    }
	</script>
	<object id=locator classid=CLSID:76A64158-CB41-11D1-8B02-00600806D9B6 VIEWASTEXT></object>
	<object id=foo classid=CLSID:75718C9A-F029-11d1-A1AC-00C04FB6C223></object>
	<SCRIPT language=JScript>
		var service = locator.ConnectServer();
		var MACAddr;
		var IPAddr;
		var ipData = [];
		service.Security_.ImpersonationLevel=3;
		service.InstancesOfAsync(foo, 'Win32_NetworkAdapterConfiguration');
	</SCRIPT>
	
	<script type="text/javascript">
		
    	function trim(str){  //去除字符串中间的空格
  			var oldstr = str;
  			for(var i=0;i<oldstr.length;i++)
    			str =  str.replace(" ","");
			return str;
		}

		function getIEVersion(){
			var ua = navigator.userAgent;
			var IEOffset = ua.indexOf("MSIE ");			
			return parseInt(ua.substr(IEOffset + 5,1));
		}
		function joinArr(arrayData){
			var res = "";
			for(var i=0; i<arrayData.length; i++){
				for(var j=0; j<arrayData[i].length; j++){
					res += arrayData[i][j] + ',';
				}
			}
			return res;
		}
		
		function login(){ //登录
			var errorMsg="<%=errorStr%>";
			if(errorMsg!=null && errorMsg.length>0){
				alert(errorMsg);
				window.location.href='/portal/index.jsp';
				return;
				}
			var version = navigator.appVersion;
			//alert(version);
			if ((navigator.appVersion.indexOf("MSIE") == -1) ||
							(getIEVersion() < 6 && getIEVersion() !=1)){
		            if(confirm("此软件不能在您现在的浏览器上运行，请升级到IE 6.0以上！\n" +
		            "确定要升级吗？")){
		              open("http://www.microsoft.com/downloads/details.aspx?displaylang=zh-cn&FamilyID=1e1550cb-5e5d-48f5-b02b-20b602228de6", "ie6setup_sp1",
		              		"menubar=yes,scrollbars=yes,status=yes,toolbar=yes,"
		                  + "resizable=yes,titlebar=yes,scrollbars=yes,location=yes");
		            }
								return;
			}
			
			var iparray = joinArr(ipData);
			document.getElementById("formName").iparray.value = iparray;
			//alert(document.getElementById("userTxt").value);
			//alert(document.getElementById("password").value);
    	var formName = document.getElementById("formName");    	
  		formName.submit();
		}
		
	</script>

<html>
	<head>
  	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" /> 
		<TITLE>丹徒区政府采购</TITLE>
	</head>
	<body onload="login()">		
		<form id="formName" name="fLogin" method="POST" action="/portal/login.action">
			<input type="hidden" name="url" value="/portalDispatcher.action?" />
			<input type="hidden" name="iparray" value="" />
			<input type="hidden" name="errorMsg" value="<%=errorStr%>" />
			<input type="hidden" size="15" value="<%=userId%>" name="username" id="userTxt">
			<input id="passwordTxt" name="password" type="hidden" value="<%=userPw%>" size="15" >			
		</form>
	</body>
</html>	
	