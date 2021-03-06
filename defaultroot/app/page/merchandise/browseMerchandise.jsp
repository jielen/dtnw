<%@page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@page import="com.ufgov.zc.common.zc.model.ZcBMerchandise"%>
<%@page import="com.ufgov.zc.common.zc.model.ZcXyghZbjg"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>浏览商品</title>
<script type="text/javascript" src="../../js/jquery.js"></script>
<script type="text/javascript" src="../../js/jquery.tools.js"></script>
<script type="text/javascript" src="../../js/jquery.metadata.js"></script>
<script type="text/javascript" src="../../js/jquery.validate.js"></script>
<script type="text/javascript" src="../../js/jquery.validate.methods.js"></script>
<script type="text/javascript" src="../../js/jquery.validate.cn.js"></script>
<script type="text/javascript" src="../../js/jquery.livequery.js"></script>
<script type="text/javascript" src="../../js/jquery.datepicker.js"></script>
<script type="text/javascript" charset="gb2312" src="../../js/editor/kindeditor.js"></script>
<script type="text/javascript" src="../../js/base.js"></script>
<script type="text/javascript" src="../../js/admin.js"></script>
<link href="../../css/webstyle.css" rel="stylesheet" type="text/css" />
<link href="../../css/admin.css" rel="stylesheet" type="text/css" />
<link href="../../css/jquery.datepicker.css" rel="stylesheet" type="text/css" />

<script>
//var dis = document.getElementById("editor");
//dis.readOnly = true;
//dis.disabled=true;
</script>

<script type="text/javascript"> 
$().ready(function() {
 
	var $tab = $("#tab");
	var $goodsForm = $("#goodsForm");
	
	var $goodsPriceTr = $("#goodsPriceTr");
	var $goodsCostTr = $("#goodsCostTr");
	var $goodsMarketPriceTr = $("#goodsMarketPriceTr");
	var $goodsProductSnTr = $("#goodsProductSnTr");
	var $goodsWeightTr = $("#goodsWeightTr");
	var $goodsStoreTr = $("#goodsStoreTr");
	
	var $isHasGoodsSpecification = $("#isHasGoodsSpecification");
	var $goodsProductId = $("#goodsProductId");
	var $goodsIsMarketable = $("#goodsIsMarketable");
	
	var $goodsPrice = $("#goodsPrice");
	var $goodsCost = $("#goodsCost");
	var $goodsMarketPrice = $("#goodsMarketPrice");
	var $goodsProductSn = $("#goodsProductSn");
	var $goodsWeight = $("#goodsWeight");
	var $goodsWeightUnit = $("#goodsWeightUnit");
	var $goodsStore = $("#goodsStore");
	var $goodsStorePlace = $("input:[name='goods.storePlace']");
	
	var $goodsSpecificationIds = $("input[name='goodsSpecificationIds']");
	var $goodsSpecificationPreview = $("#goodsSpecificationPreview");
	var $addProductButton = $("#addProductButton");
	var $goodsTypeId = $("#goodsTypeId");
	var $addScoreButton = $("#addScoreButton");
 
	// Tab效果
	$tab.tabs(".tabContent", {
		tabs: "input"
	});
	
	// 商品图片预览滚动栏
	$(".goodsImageArea .scrollable").scrollable({
		speed: 600
	});
	
	// 显示商品图片预览操作层
	$(".goodsImageArea li").livequery("mouseover", function() {
		$(this).find(".goodsImageOperate").show();
	});
	
	// 隐藏商品图片预览操作层
	$(".goodsImageArea li").livequery("mouseout", function() {
		$(this).find(".goodsImageOperate").hide();
	});
	
	// 商品图片左移
	$(".left").livequery("click", function() {
		var $goodsImageLi = $(this).parent().parent().parent();
		var $goodsImagePrevLi = $goodsImageLi.prev("li");
		if ($goodsImagePrevLi.length > 0) {
			$goodsImagePrevLi.insertAfter($goodsImageLi);
		}
	});
	
	// 商品图片右移
	$(".right").livequery("click", function() {
		var $goodsImageLi = $(this).parent().parent().parent();
		var $goodsImageNextLi = $goodsImageLi.next("li");
		if ($goodsImageNextLi.length > 0) {
			$goodsImageNextLi.insertBefore($goodsImageLi);
		}
	});
	
	// 商品图片删除
	$(".delete").livequery("click", function() {
		var $this = $(this);
		var $goodsImageLi = $(this).parent().parent().parent();
		var $goodsImagePreview = $goodsImageLi.find(".goodsImagePreview");
		var $goodsImageIds = $goodsImageLi.find("input[name='goodsImageIds']");
		var $goodsImageFiles = $goodsImageLi.find("input[name='goodsImages']");
		var $goodsImageParameterTypes = $goodsImageLi.find("input[name='goodsImageParameterTypes']");
		$goodsImageIds.remove();
		$goodsImageFiles.remove();
		$goodsImageParameterTypes.remove();
		
		$goodsImagePreview.html("暂无图片");
		$goodsImagePreview.removeAttr("title");
		if ($.browser.msie) {
			if(window.XMLHttpRequest) {
				$goodsImagePreview[0].style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod = 'scale', src='')";
			}
		}
		var $img = $this.find(".imageID");
		var imgid = $img.attr("value");
		var merID = document.getElementById("zcBMerchandise.zcMerCode").value;
		if(imgid != ""){
			//location.href="/GB/portal/page/merchandise/DeletePicture.do?merid="+merID+"&imgid="+imgid;
			var url = getRootPath()+"/portal/page/merchandise/DeletePicture.do?merid="+merID+"&imgid="+imgid;
			executeAjax1(url);
		}
		
	});
	
	function executeAjax1(url) {
		createXMLHttpRequest();// 创建XMLHttpRequest对象
		//xmlHttp.onreadystatechange = handleStateChangeOption1;// 回调函数
		xmlHttp.open("GET", url, true);
		xmlHttp.send(null);
	}
	
	
	// 商品图片选择预览
	var $goodsImageScrollable = $(".goodsImageArea .scrollable").scrollable();
	i=1;
	var goodsImageLiHtml = '<li><div class="goodsImageBox"><div class="goodsImagePreview">暂无图片</div><div class="goodsImageOperate"><a class="left" href="javascript: void(0);" alt="左移" hidefocus="true"></a><a class="right" href="javascript: void(0);" title="右移" hidefocus="true"></a><a class="delete" href="javascript: void(0);" title="删除" hidefocus="true"></a></div><a class="goodsImageUploadButton" href="javascript: void(0);"><input type="file" name="goodsImages'+i+'" hidefocus="true" /><div>上传新图片</div></a></div></li>';
	$(".goodsImageUploadButton input").livequery("change", function() {
		var $this = $(this);
		var $goodsImageLi = $this.parent().parent().parent();
		var $goodsImagePreview = $goodsImageLi.find(".goodsImagePreview");
		var fileName = $this.val().substr($this.val().lastIndexOf("\\") + 1);
		if (/()$/i.test($this.val()) == false) {
			$.message({type: "warn", content: "您选择的文件格式错误!"});
			return false;
		}
		$goodsImagePreview.empty();
		$goodsImagePreview.attr("title", fileName);
		if ($.browser.msie) {
			if(!window.XMLHttpRequest) {
				$goodsImagePreview.html('<img src="' + $this.val() + '" />');
			} else {
				$this[0].select();
				var imgSrc = document.selection.createRange().text;
				$goodsImagePreview[0].style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod = 'scale', src='" + imgSrc + "')";
			}
		} else if ($.browser.mozilla) {
			$goodsImagePreview.html('<img src="' + $this[0].files[0].getAsDataURL() + '" />');
		} else {
			$goodsImagePreview.html(fileName);
		}
		if ($goodsImageLi.next().length == 0) {
			$goodsImageLi.after(goodsImageLiHtml);
			//为了实现不同的name值
			i++;  
			if ($goodsImageScrollable.getSize() > 5) {
				$goodsImageScrollable.next();
			}
		}
		var $goodsImageIds = $goodsImageLi.find("input[name='goodsImageIds']");
		var $goodsImageParameterTypes = $goodsImageLi.find("input[name='goodsImageParameterTypes']");
		var $goodsImageUploadButton = $goodsImageLi.find(".goodsImageUploadButton");
		$goodsImageIds.remove();
		if ($goodsImageParameterTypes.length > 0) {
			$goodsImageParameterTypes.val("goodsImageFile");
		} else {
			$goodsImageUploadButton.append('<input type="hidden" name="goodsImageParameterTypes" value="goodsImageFile" />');
		}
	});
	
	
	// 日期选择框
	var $currentDatePicker;
	var datePickerOptions = {
        format: "Y-m-d",
		date: new Date(),
		calendars: 1,
		starts: 1,
		position: "right",
		prev: "<<",
		next: ">>",
		locale: {
			days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
			daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
			daysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
			months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			weekMin: ' '
		},
		onBeforeShow: function(){
			$currentDatePicker = $(this);
			var currentDate = $.trim($currentDatePicker.val());
			if (currentDate != "") {
				var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/;
				if(currentDate.match(reg) != null) {
					$currentDatePicker.DatePickerSetDate($currentDatePicker.val(), true);
				}
			}
		},
		onChange: function(formated, dates){
			$currentDatePicker.val(formated);
		}
    };
    $("input.datePicker").DatePicker(datePickerOptions);
	
	// 重新绑定日期选择框
	$.bindDatePicker = function () {
		$("input.datePicker").DatePicker(datePickerOptions);
	}
	
	// 查询商品属性
	$goodsTypeId.change( function() {
		$(".goodsAttributeContentTr").remove();
		var id = $("#goodsTypeId").val();
		$.ajax({
			url: "goods!ajaxGoodsAttribute.action",
			dataType: "json",
			data: {id: id},
			async: false,
			success: function(data) {
				var goodsAttributeTrHtml = "";
				$.each(data, function(i) {
					if(data[i]["attributeType"] == "text") {
						goodsAttributeTrHtml += '<tr class="goodsAttributeContentTr"><th>' + data[i].name + ':</th><td><input type="text" name="' + data[i].id + '"' + ((data[i].isRequired == true) ? ' class="formText {required: true}"' : ' class="formText"') + ' />' + ((data[i].isRequired == true) ? '<label class="requireField">*</label>' : '') + '</td></tr>';
					} else if(data[i]["attributeType"] == "number") {
						goodsAttributeTrHtml += '<tr class="goodsAttributeContentTr"><th>' + data[i].name + ':</th><td><input type="text" name="' + data[i].id + '"' + ((data[i].isRequired == true) ? ' class="formText {required: true, number: true}"' : ' class="formText {number: true}"') + ' />' + ((data[i].isRequired == true) ? '<label class="requireField">*</label>' : '') + '</td></tr>';
					} else if(data[i]["attributeType"] == "alphaint"){
						goodsAttributeTrHtml += '<tr class="goodsAttributeContentTr"><th>' + data[i].name + ':</th><td><input type="text" name="' + data[i].id + '"' + ((data[i].isRequired == true) ? ' class="formText {required: true, lettersonly: true}"' : ' class="formText {lettersonly: true}"') + ' />' + ((data[i].isRequired == true) ? '<label class="requireField">*</label>' : '') + '</td></tr>';
					} else if(data[i]["attributeType"] == "select") {
						var goodsAttributeOption = '<option value="">请选择...</option>';
						for(var key in data[i]["attributeOptionList"]) {
							goodsAttributeOption += ('<option value="' + data[i]["attributeOptionList"][key] + '">' + data[i]["attributeOptionList"][key] + '</option>');
						}
						goodsAttributeTrHtml += '<tr class="goodsAttributeContentTr"><th>' + data[i].name + ':</th><td><select name="' + data[i].id + '"' + ((data[i].isRequired == true) ? ' class="{required: true}"' : '') + '>' + goodsAttributeOption + '</select>' + ((data[i].isRequired == true) ? '<label class="requireField">*</label>' : '') + '</td></tr>';
					} else if(data[i]["attributeType"] == "checkbox") {
						var goodsAttributeOption = "";
						for(var key in data[i]["attributeOptionList"]) {
							goodsAttributeOption += '<label><input type="checkbox" name="' + data[i].id + '" value="' + data[i]["attributeOptionList"][key] + '"' + ((data[i].isRequired == true) ? ' class="{required: true, messagePosition: \'#' + data[i].id + 'MessagePosition\'}"' : '') +' />' + data[i]["attributeOptionList"][key] + '</label>&nbsp;&nbsp;';
						}
						goodsAttributeTrHtml += '<tr class="goodsAttributeContentTr"><th>' + data[i].name + ':</th><td>' + goodsAttributeOption + ((data[i].isRequired == true) ? '<span id="' + data[i].id + 'MessagePosition"></span><label class="requireField">*</label>' : '') + '</td></tr>';
					} else if(data[i]["attributeType"] == "date") {
						goodsAttributeTrHtml += '<tr class="goodsAttributeContentTr"><th>' + data[i].name + ':</th><td><input type="text" name="' + data[i].id + '"' + ((data[i].isRequired == true) ? ' class="formText datePicker {required: true, dateISO: true}"' : ' class="formText datePicker {dateISO: true}"') + ' />' + ((data[i].isRequired == true) ? '<label class="requireField">*</label>' : '') + '</td></tr>';
					}
				})
				$("#goodsTypeTr").after(goodsAttributeTrHtml);
				$.bindDatePicker();
			}
		});
	});
	
	
	var goodsSpecificationValueSelectedDatas = {};

	//添加供应商折扣率信息
	var scoreindex = 0;
	$addScoreButton.click( function() {
		var $goodsSpecificationTableTr = $("#agencyScoreTable tr:eq(1)").clone().removeClass("hidden");
		$goodsSpecificationTableTr.find(":input").attr("disabled", false);
		$goodsSpecificationTableTr.find(":select").attr("disabled", false);
		var goodsSpecificationTableTrHtml = '<tr class="{\'index\': \'' + scoreindex + '\'}">' + $goodsSpecificationTableTr.html().replace(/\[(\-?)(\d+)\]/ig, "[" + scoreindex + "]") + '</tr>';
		$("#agencyScoreTable tr:last").after(goodsSpecificationTableTrHtml);
		scoreindex ++;
	});
	
	
	
	// 删除供应商折扣率信息
	$(".deleteScore").live("click", function() {
		var $this = $(this);
		if (confirm("您确定要删除吗？") == true) {
			//从数据库删除数据
			//var sucode = $this.parent().parent().find(".zcEbSupplier_option.supplierCode").attr("value");
			//var lower = $this.parent().parent().find(".zcEbSupplier.lower").attr("value");
			//var upper = $this.parent().parent().find(".zcEbSupplier.upper").attr("value");
			//var mercode = document.getElementById("zcBMerchandise.zcMerCode");
			//alert("--["+sucode+"]---["+lower+"]--["+upper+"]------["+mercode+"]-------");
			//if(sucode != "" && lower!="" && upper!="" && mercode!=""){
			//	var url = getRootPath()+"/portal/page/merchandise/DeleteMerDiscount.do?sucode="+sucode+"&lower="+lower+"&upper"+upper+"&mercode"+mercode;
			//	executeAjax1(url);
			//}
			$this.parent().parent().remove();
			var index = $this.parent().parent().metadata().index;
			if(goodsSpecificationValueSelectedDatas[index] != null) {
				goodsSpecificationValueSelectedDatas[index] = null;
			}
		}
	});
	
	// 表单验证
	$goodsForm.validate({
		ignore: ".ignoreValidate",
		invalidHandler: function(form, validator) {
			$.each(validator.invalid, function(key, value){
				$.message({type: "warn", content: value});
				return false;
			});
		},
		errorPlacement:function(error, element) {},
		submitHandler: function(form) {
			var isRepeated = false;
			if ($isHasGoodsSpecification.val() == "true") {
				$("#goodsSpecificationTable tr:gt(1)").each(function() {
					var goodsSpecificationValueSelectedDatasString = "";
					$(this).find(".goodsSpecificationValueSelect input").each(function() {
						goodsSpecificationValueSelectedDatasString += $(this).val();
					});
					var existCount = 0;
					$.each(goodsSpecificationValueSelectedDatas, function(i) {
						if (goodsSpecificationValueSelectedDatas[i] == goodsSpecificationValueSelectedDatasString) {
							existCount++;
							if (existCount > 1) {
								isRepeated = true;
								return false;
							}
						}
					});
					if (existCount > 1) {
						return false;
					}
				});
			}
			$goodsForm.find(":submit").attr("disabled", true);
			form.action = "/GB/portal/page/merchandise/UpdateMerchandise.do";
			form.submit();
			alert("保存成功！");
		}
	});
 
})

//AJAX实现商品品目和品牌的二级联动
	var xmlHttp;
	
	function createXMLHttpRequest() {
		// IE
		if (window.ActiveXObject) {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		// FireFox
		else if (window.XMLHttpRequest) {
			xmlHttp = new XMLHttpRequest();
		}
	}
	//获取项目的根路径
	function getRootPath(){
		var curWwwPath=window.document.location.href;
		var pathName=window.document.location.pathname;    
		var pos=curWwwPath.indexOf(pathName);  
		var localhostPaht=curWwwPath.substring(0,pos);
		var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);    
		return(localhostPaht+projectName);
	}
	
	function ctlent() {
		var key = document.getElementById("zcCatalogueCode").value;
		var url = getRootPath()+"/portal/page/merchandise/SearchMerPinPai.do?key="
				+ key;
		executeAjax(url);
	}

	function executeAjax(url) {
		createXMLHttpRequest();// 创建XMLHttpRequest对象
		xmlHttp.onreadystatechange = handleStateChangeOption;// 回调函数
		xmlHttp.open("GET", url, true);
		xmlHttp.send(null);
	}

	function handleStateChangeOption() {
		if (xmlHttp.readyState == 4) {
			if (xmlHttp.status == 200) {
				var xmlDoc = xmlHttp.responseXML;// 获取Ajax返回的结果
				var values = xmlDoc.getElementsByTagName("value");// value为返回的XML里的节点
				var texts = xmlDoc.getElementsByTagName("text");

				var selectObj = document.getElementById("childNode");
				selectObj.length = 0;
				for ( var i = 0; i < values.length; i++) {
					var childOption = new Option(texts[i].firstChild.data,
							values[i].firstChild.data);
					selectObj.options[selectObj.length++] = childOption;
				}
			}
		}
	}
	
	function  checkND(){
		var ND=document.getElementById("zcBMerchandise.zcYear").value;
		var NDReg = /^2[0-9]{3}$/;
		if(ND==""){
			alert("财政年度输入不能为空！");
		}else{
			if(ND.match(NDReg))
				return true;
			else
 				alert("财政年度输入非法，应为以2开头的4位数字！");
		}
	}
	
	
</script>
</head>

<body class="input goods">
	<div class="bar">
		<input type="button" class="formButton" onclick="location.href='/GB/portal/page/merchandise/MerchandiseList.do'" value="返 回" />
	</div>
	<div class="bar" align="center">
		<font color="blue" size="3"><B>浏览商品信息页面</B></font>
	</div>
	<div class="body">
		<form id="goodsForm" action="/GB/portal/page/merchandise/UpdateMerchandise.do" method="post" enctype="multipart/form-data">
			<input type="hidden" name="id" value="" />
			<input type="hidden" id="isHasGoodsSpecification" name="goods.isHasGoodsSpecification" value="false" />
			<ul id="tab" class="tab">
				<li>
					<input type="button" value="基本信息" hidefocus="true" />
				</li>
				<li>
					<input type="button" value="商品描述" hidefocus="true" />
				</li>
				<li>
					<input type="button" value="商品属性" hidefocus="true" />
				</li>
				<li>
					<input type="button" value="商品折扣信息" hidefocus="true" />
				</li>
			</ul>
			<% 
				ZcBMerchandise zcBMerchandise = (ZcBMerchandise)request.getAttribute("zcBMerchandise");
				ZcXyghZbjg zcXyghZbjg = (ZcXyghZbjg)request.getAttribute("zcXyghZbjg");
			%>
			<table class="inputTable tabContent">
				<tr>
					<th>
						商品编号:
					</th>
					<td>
						<% if (zcBMerchandise.getZcMerCode()!= null){ %>
							<bean:define id="beanWriteValue" name="zcBMerchandise" property="zcMerCode"/>
							<input type="text" id="zcBMerchandise.zcMerCode" name="zcBMerchandise.zcMerCode" value="<%=beanWriteValue.toString() %>"  disabled="disabled"  title="若留空则由系统自动生成" />
						<% } else{%>
							<input type="text" id="zcBMerchandise.zcMerCode" name="zcBMerchandise.zcMerCode" value=""  disabled="disabled"  title="若留空则由系统自动生成" />
						<%} %>
					</td>
				</tr>
				<tr>
					<th>
						商品名称:
					</th>
					<td>
						<% if (zcBMerchandise.getZcMerName()!= null){ %>
							<bean:define id="beanWriteValue" name="zcBMerchandise" property="zcMerName"/>
							<input type="text" name="zcBMerchandise.zcMerName" class="formText {required: true, messages: {required: '请填写商品名称!'}}" disabled="disabled" value="<%=beanWriteValue.toString() %>" />
							<label class="requireField">*</label>
						<% } else{%>
							<input type="text" name="zcBMerchandise.zcMerName" class="formText {required: true, messages: {required: '请填写商品名称!'}}" disabled="disabled" value="" />
							<label class="requireField">*</label>
						<%} %>
					</td>
				</tr>
				<tr>
					<th>
						商品品目:
					</th>
					<td>
						<% if (zcBMerchandise.getZcCatalogueCode()!= null){ %>
							<bean:define id="beanWriteValue" name="zcBMerchandise" property="zcCatalogueCode"/>
							<bean:define id="beanWriteValue1" name="zcBMerchandise" property="zcCatalogueName"/>
							<select name="zcBMerchandise.zcCatalogueCode" id="zcCatalogueCode" size="1" onchange="ctlent()" disabled="disabled" class="{required: true, messages: {required: '请选择商品品目!'}}">
								<option selected="selected" value="<%=beanWriteValue.toString() %>"><%=beanWriteValue1.toString() %></option>	
								<logic:notEmpty name="spForm">
									<logic:iterate id="sp"  name="spForm" property="catalogueList" type="com.ufgov.zc.common.zc.model.ZcBCatalogue" scope="request">
										<option value='<bean:write name="sp" property="zcCatalogueCode"/>'><bean:write name="sp" property="zcTypeName"/></option>
									</logic:iterate>
								</logic:notEmpty>
							</select>
							<label class="requireField">*</label>
						<% }%>
					</td>
				</tr>
				<tr>
					<th>
						商品品牌:
					</th>
					<td>
						<% if (zcBMerchandise.getZcBraCode()!= null){ %>
							<bean:define id="beanWriteValue" name="zcBMerchandise" property="zcBraCode"/>
							<bean:define id="beanWriteValue1" name="zcBMerchandise" property="zcBraName"/>
							<select name="zcBMerchandise.zcBraCode" id="childNode" disabled="disabled">
								<option selected="selected" value="<%=beanWriteValue.toString() %>"><%=beanWriteValue1.toString() %></option>
							</select>
						<% } else{%>
							<select name="zcBMerchandise.zcBraCode" id="childNode" disabled="disabled">
								<option value="">请选择...</option>
							</select>
						<%} %>
					</td>
				</tr>
				<tr>
					<th>
						商品型号:
					</th>
					<td>
						<% if (zcBMerchandise.getZcMerSpec()!= null){ %>
							<bean:define id="beanWriteValue" name="zcBMerchandise" property="zcMerSpec"/>
							<input type="text" name="zcBMerchandise.zcMerSpec" disabled="disabled" class="formText {required: true, messages: {required: '请填写商品型号!'}}" value="<%=beanWriteValue.toString() %>" />
							<label class="requireField">*</label>
						<% } else{%>
							<input type="text" name="zcBMerchandise.zcMerSpec" disabled="disabled" class="formText {required: true, messages: {required: '请填写商品型号!'}}" value="" />
							<label class="requireField">*</label>
						<%} %>
					</td>
				</tr>
				<tr id="zcDiyuDaima">
					<th>
						地域名称:
					</th>
					<td>
						<% if ( zcBMerchandise.getZcDiyuDaima()!= null && zcBMerchandise.getZcDiyuDaima().equals("850001") ){%>
							<label><input type="radio" name="zcBMerchandise.zcDiyuDaima" disabled="disabled" value="850000" />省内</label>
							<label><input type="radio" name="zcBMerchandise.zcDiyuDaima" disabled="disabled" value="850001" checked="checked" />省外</label>
						<%} else{%>
							<label><input type="radio" name="zcBMerchandise.zcDiyuDaima" disabled="disabled" value="850000" checked="checked" />省内</label>
							<label><input type="radio" name="zcBMerchandise.zcDiyuDaima" disabled="disabled" value="850001"/>省外</label>
						<%} %>
					</td>
				</tr>
				<tr id="zcProjID">
					<th>
						本次商品招标项目代码:
					</th>
					<td>
						<% if (zcBMerchandise.getZcProjID()!= null ){ %>
							<bean:define id="beanWriteValue" name="zcBMerchandise" property="zcProjID"/>
							<bean:define id="beanWriteValue1" name="zcXyghZbjg" property="zcProjNa"/>
							<select name="zcBMerchandise.zcProjID" disabled="disabled" class="{required: true, messages: {required: '请选择本次商品招标项目名称!'}}" size="1" >
								<option selected="selected" value='<%=beanWriteValue.toString() %>'><%=beanWriteValue1.toString() %></option>
								<logic:iterate id="sp"  name="spForm" property="zcXyghZbjgList" type="com.ufgov.zc.common.zc.model.ZcXyghZbjg" scope="request">
									<option value='<bean:write name="sp" property="zcProjId"/>'><bean:write name="sp" property="zcProjNa"/></option>
								</logic:iterate>
							</select>
							<label class="requireField">*</label>
						<% }%>
					</td>
				</tr>
				<tr id="zcYear">
					<th>
						财政年度:
					</th>
					<td>
						<% if (zcBMerchandise.getZcYear()!= null ){ %>
							<bean:define id="beanWriteValue" name="zcBMerchandise" property="zcYear"/>
							<input type="text" id="zcBMerchandise.zcYear" name="zcBMerchandise.zcYear" disabled="disabled" class="formText {required: true, messages: {required: '请填写财政年度!'}}" value="<%=beanWriteValue.toString() %>" title="前台不会显示，仅供后台使用"  onblur="checkND()"/>
							<label class="requireField">*</label>
						<% } else{%>
							<input type="text" id="zcBMerchandise.zcYear" name="zcBMerchandise.zcYear" disabled="disabled" class="formText {required: true, messages: {required: '请填写财政年度!'}}" value="" title="前台不会显示，仅供后台使用"  onblur="checkND()"/>
							<label class="requireField">*</label>
							<font color="red" size="2">(财政年度为四位数字年份，如“2011年”，请填写“2011”)</font>
						<%} %>
					</td>
				</tr>
				<tr id="zcMerTax">
					<th>
						市场价（元）:
					</th>
					<td>
						<% if (zcBMerchandise.getZcMerTax()!= null ){ %>
							<bean:define id="beanWriteValue" name="zcBMerchandise" property="zcMerTax"/>
							<input type="text" id="zcMerTax" name="zcBMerchandise.zcMerTax" disabled="disabled" class="formText {required: true, min: 0, messages: {required: '请填写市场价（元）!', min: '市场价（元）不允许小于0!'}}" value="<%=beanWriteValue.toString() %>" />
							<label class="requireField">*</label>
						<% } else{%>
							<input type="text" id="zcMerTax" name="zcBMerchandise.zcMerTax" disabled="disabled" class="formText {required: true, min: 0, messages: {required: '请填写市场价（元）!', min: '市场价（元）不允许小于0!'}}" value="" />
							<label class="requireField">*</label>
						<%} %>
					</td>
				</tr>
				<tr id="zcMerMPrice">
					<th>
						协议价（元）:
					</th>
					<td>
						<% if (zcBMerchandise.getZcMerMPrice()!= null ){ %>
							<bean:define id="beanWriteValue" name="zcBMerchandise" property="zcMerMPrice"/>
							<input type="text" id="zcBMerchandise.zcMerMPrice" name="zcBMerchandise.zcMerMPrice" disabled="disabled" class="formText {required: true, min: 0, messages: {required: '请填写协议价（元）!', min: '协议价（元）不允许小于0!'}}" value="<%=beanWriteValue.toString() %>" />
							<label class="requireField">*</label>
						<% } else{%>
							<input type="text" id="zcBMerchandise.zcMerMPrice" name="zcBMerchandise.zcMerMPrice" disabled="disabled" class="formText {required: true, min: 0, messages: {required: '请填写协议价（元）!', min: '协议价（元）不允许小于0!'}}" value="" />
							<label class="requireField">*</label>
						<%} %>
					</td>
				</tr>
				<tr id="zcMerUnit">
					<th>
						计量单位:
					</th>
					<td>
						<% if (zcBMerchandise.getZcMerUnit()!= null ){ %>
							<bean:define id="beanWriteValue" name="zcBMerchandise" property="zcMerUnit"/>
							<input type="text" id="zcMerUnit" name="zcBMerchandise.zcMerUnit" disabled="disabled" value="<%=beanWriteValue.toString() %>"  class="formText {required: true, messages: {required: '请填写计量单位!'}}" />
							<label class="requireField">*</label>
						<% } else{%>
							<input type="text" id="zcMerUnit" name="zcBMerchandise.zcMerUnit" disabled="disabled" class="formText {required: true, messages: {required: '请填写计量单位!'}}" value=""  />
							<label class="requireField">*</label>
						<%} %>
					</td>
				</tr>
				<tr>
					<th>
						是否市区共享商品:
					</th>
					<td>
						<% if (zcBMerchandise.getZcIsShared()!= null && zcBMerchandise.getZcIsShared().equals("N")){ %>
							<label><input type="radio" name="zcBMerchandise.zcIsShared" disabled="disabled" value="Y" />是</label>
							<label><input type="radio" name="zcBMerchandise.zcIsShared" disabled="disabled" value="N" checked="checked" />否</label>
						<% } else{%>
							<label><input type="radio" name="zcBMerchandise.zcIsShared" disabled="disabled" value="Y" checked="checked" />是</label>
							<label><input type="radio" name="zcBMerchandise.zcIsShared" disabled="disabled" value="N" />否</label>
						<%} %>
					</td>
				</tr>
				<tr>
					<th>
						是否自主创新产品:
					</th>
					<td>
						<% if (zcBMerchandise.getZcMerIsZzcx()!= null && zcBMerchandise.getZcMerIsZzcx().equals("N")){ %>
							<label><input type="radio" name="zcBMerchandise.zcMerIsZzcx" disabled="disabled" value="Y" />是</label>
							<label><input type="radio" name="zcBMerchandise.zcMerIsZzcx" disabled="disabled" value="N" checked="checked" />否</label>
						<% } else{%>
							<label><input type="radio" name="zcBMerchandise.zcMerIsZzcx" disabled="disabled" value="Y" checked="checked" />是</label>
							<label><input type="radio" name="zcBMerchandise.zcMerIsZzcx" disabled="disabled" value="N" />否</label>
						<%} %>
					</td>
				</tr>
				<tr>
					<th>
						是否绿色环保产品:
					</th>
					<td>
						<% if (zcBMerchandise.getZcMerIsLshb()!= null && zcBMerchandise.getZcMerIsLshb().equals("N")){ %>
							<label><input type="radio" name="zcBMerchandise.zcMerIsLshb" disabled="disabled" value="Y" />是</label>
							<label><input type="radio" name="zcBMerchandise.zcMerIsLshb" disabled="disabled" value="N" checked="checked" />否</label>
						<% } else {%>
							<label><input type="radio" name="zcBMerchandise.zcMerIsLshb" disabled="disabled" value="Y" checked="checked" />是</label>
							<label><input type="radio" name="zcBMerchandise.zcMerIsLshb" disabled="disabled" value="N" />否</label>
						<%} %>
					</td>
				</tr>
				<tr>
					<th>
						是否节能节水产品 :
					</th>
					<td>
						<% if (zcBMerchandise.getZcIsJnjs()!= null && zcBMerchandise.getZcIsJnjs().equals("N")){ %>
							<label><input type="radio" name="zcBMerchandise.zcIsJnjs" disabled="disabled" value="Y" />是</label>
							<label><input type="radio" name="zcBMerchandise.zcIsJnjs" disabled="disabled" value="N" checked="checked" />否</label>
						<% } else {%>
							<label><input type="radio" name="zcBMerchandise.zcIsJnjs" disabled="disabled" value="Y" checked="checked" />是</label>
							<label><input type="radio" name="zcBMerchandise.zcIsJnjs" disabled="disabled" value="N" />否</label>
						<%} %>
					</td>
				</tr>
				<tr>
					<th>
						上传商品图片
					</th>
					<td>
						<div class="goodsImageArea">
							<div class="example"></div>
							<a class="prev browse" href="javascript:void(0);" hidefocus="true"></a>
							<div class="scrollable">
								<ul class="items">
									<logic:notEmpty name="spForm" property="asFileList">
										<logic:iterate id="sp"  name="spForm" property="asFileList" type="com.ufgov.zc.common.zc.model.ZcBMerPic" scope="request" indexId="num">
										<bean:size id="count" name="spForm" property="asFileList"/>
											<li>
												<div class="goodsImageBox">
													<div class="goodsImagePreview belatedPNG">
														<img src="/GB/portal/page/merchandise/PictureDisplay.do?fileID=<bean:write name="sp" property="zcPicID"/>" alt=""/>
													</div>
													<a class="goodsImageUploadButton" href="javascript: void(0);">
		                                                <div>图片<%=num %></div>
													</a>
												</div>
											</li>
										</logic:iterate>
									</logic:notEmpty>
								</ul>
							</div>
							<a class="next " href="javascript:void(0);" hidefocus="true"></a>
							<div class="blank"></div>
						</div>
					</td>
				</tr>
				<tr>
					<th>
						备注:
					</th>
					<td>
						<% if (zcBMerchandise.getZcRemark()!= null){ %>
							<bean:define id="beanWriteValue" name="zcBMerchandise" property="zcRemark"/>
							<textarea name="zcBMerchandise.zcRemark" disabled="disabled" class="formTextarea"><%=beanWriteValue.toString() %></textarea>
						<%} else {%>
							<textarea name="zcBMerchandise.zcRemark" disabled="disabled" class="formTextarea"></textarea>
						<%} %>
					</td>
				</tr>
			</table>
			
			<table class="inputTable tabContent">
				<tr>
					<% if (zcBMerchandise.getZcMerCollocate()!= null){ %>
						<td colspan="2">
							<bean:define id="beanWriteValue" name="zcBMerchandise" property="zcMerCollocate"/>
							<textarea readonly="readonly" id="editor" name="zcBMerchandise.zcMerCollocate" disabled="disabled" class="editor" style="width: 100%; height: 450px;"><%=beanWriteValue.toString() %></textarea>
						</td>
					<%} else{ %>
						<td colspan="2">
							<textarea readonly="readonly" id="editor" name="zcBMerchandise.zcMerCollocate" disabled="disabled" class="editor" style="width: 100%; height: 450px;"></textarea>
						</td>
					<%} %>
				</tr>
			</table>
			<table class="inputTable tabContent">
				<tr id="goodsTypeTr">
					<th>
						商品类型:
					</th>
					<td>
						<% if (zcBMerchandise.getZcCatalogueCode()!= null){ %>
							<bean:define id="beanWriteValue" name="zcBMerchandise" property="zcCatalogueName"/>
							<input name = "zcBCatalogueProp.zcCatalogueName" id="zcBCatalogueProp.zcCatalogueName" disabled="disabled" value="<%=beanWriteValue.toString() %>" disabled="disabled" size="47" style="border-style:none" disabled="disabled"/>
						<%}%>
					</td>
				</tr>
				<logic:notEmpty name="spForm" property="zcCatalogueProp">
					<logic:iterate id="sp"  name="spForm" property="zcCatalogueProp" type="com.ufgov.zc.common.zc.model.ZcBMerCatalogueProp" scope="request">
						<tr id="CatalogueProp">
							<th>
								<bean:write name="sp" property="zcCataPropChName"/>:
							</th>
							<td>
								<input type="text" disabled="disabled" id="zcBCatalogueProp.<bean:write name="sp" property="zcCataPropEnName"/>" name="zcBCatalogueProp.<bean:write name="sp" property="zcCataPropEnName"/>" value="<bean:write name="sp" property="zcCataPropValue"/>" class="formText {required: true, messages: {required: '请填写<bean:write name="sp" property="zcCataPropChName"/>!'}}" />
								<label class="requireField">*</label>
							</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
			</table>
			<table class="inputTable tabContent">		
				<tr>
					<td>
						<table id="agencyScoreTable" class="inputTable goodsSpecificationTable">
							<tr>
								<th align="center">
									<b>供应商</b>
								</th>
								<th align="center">
									<b>折扣下限</b>
								</th>
								<th align="center">
									<b>折扣上限</b>
								</th>
								<th align="center">
									<b>折扣率</b>
								</th>
								<th align="center">
									<b>备 注</b>
								</th>
							</tr>
							<logic:notEmpty name="spForm" property="zcBMerDiscountWebList">
								<logic:iterate id="sp"  name="spForm" property="zcBMerDiscountWebList" type="com.ufgov.zc.common.zc.model.ZcBMerDiscountWeb" scope="request" indexId="num">
									<tr>
										<td  align="center">
											<select class="zcEbSupplier.supplierCode" disabled="disabled" name="zcEbSupplier1[<bean:write name="num"/>].supplierCode" size="1">
												<option  id="zcEbSupplier_option.supplierCode" value='<bean:write name="sp" property="zcSuCode"/>'><bean:write name="sp" property="zcSuName"/></option>
											</select>
										</td>
										<td  align="center">
											<input id="zcEbSupplier.lower" type="text" disabled="disabled" name="zcEbSupplier1[<bean:write name="num"/>].lower" class="formText {required: true, min: 0, messages: {required: '请填写折扣下限!', min: '折扣下限不允许小于0!'}}" value="<bean:write name="sp" property="zcTreatyLowerLimit"/>"  style="width: 50px;" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
										</td>
										<td  align="center">
											<input id="zcEbSupplier.upper" type="text" disabled="disabled" name="zcEbSupplier1[<bean:write name="num"/>].upper" class="formText {required: true, min: 0, messages: {required: '请填写折扣上限!', min: '折扣上限不允许小于0!'}}" value="<bean:write name="sp" property="zcTreatyUpperLimit"/>"  style="width: 50px;" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
										</td>
										<td  align="center">
											<input id="zcEbSupplier.discount" type="text" disabled="disabled" name="zcEbSupplier1[<bean:write name="num"/>].discount" class="formText {required: true, min: 0, max: 100, messages: {required: '请填写折扣率!', min: '折扣率不允许小于0!', max: '折扣率不允许大于100!'}}" value="<bean:write name="sp" property="zcTreatyDiscountRate"/>"  style="width: 50px;" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/>%
										</td>
										<td  align="center">
											<input id="zcEbSupplier.memo" type="text" disabled="disabled" name="zcEbSupplier1[<bean:write name="num"/>].memo" class="formText" value="<bean:write name="sp" property="zcTreatyMemo"/>" />
										</td>
									</tr>
								</logic:iterate>
							</logic:notEmpty>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>

