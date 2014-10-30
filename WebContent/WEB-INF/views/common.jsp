<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	var path="${path}";
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache" />  
<meta http-equiv="Cache-Control" content="no-cache" />  
<meta http-equiv="Expires" content="0" />  
<!--jQuery EasyUI-->
<link rel="stylesheet" type="text/css" href="${path }/resource/plugin/jquery-easyui-1.2.6/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${path }/resource/plugin/jquery-easyui-1.2.6/themes/icon.css" />
<script type="text/javascript" src="${path}/resource/plugin/jquery-easyui-1.2.6/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${path}/resource/plugin/jquery-easyui-1.2.6/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${path}/resource/plugin/jquery-easyui-1.2.6/locale/easyui-lang-zh_CN.js"></script>
<!-- 自定义 -->
<link rel="stylesheet" type="text/css" href="${path }/resource/css/common.css" />
<script type="text/javascript" src="${path}/resource/js/common.js"></script>
<script type="text/javascript" src="${path}/resource/js/DateFormat.js"></script>