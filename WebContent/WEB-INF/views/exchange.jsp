<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>数据交换(导入/导出)</title>
<%@ include file="common.jsp"%>
<script type="text/javascript">
	$(function() {

	});
	
	
</script>
</head>
<body class="easyui-layout">
	<div region="west" iconCls="icon-roles" title="功能列表" split="true" style="width: 180px; padding-top: 10px;">
	
	</div>
	
	<div region="center" style="overflow: hidden; padding: 10px;">
		<form action="${path}/exchange/import.htm" method="post" enctype="multipart/form-data">
			<input type="file" id="file" name="file" style="width: 300px;" /> 
			<input type="submit" id="btnUpload" name="btnUpload" value="上传" />
		</form>
		${data }
	</div>
</body>
</html>