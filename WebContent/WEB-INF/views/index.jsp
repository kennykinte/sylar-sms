<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Sylar短信浏览器 - Java EE版</title>
	<%@ include file="common.jsp"%>
	<script type="text/javascript">
	$(function(){
	});

	//判断tab页是否存在
	function isTabExist(id) {
	    // 定义所有tab panel的id集合
	    var arr = new Array();
	    var pp = $('#tabs').tabs('tabs');
	    for (var i = 0; i < pp.length; i++) {
	        var p = pp[i].panel('options');
	        if (p.id) {
	            arr.push(p.id);
	        } else {
	            // 首页的场合
	            arr.push("0");
	        }
	    }
	    // 判断新增的tab的id是否在arr集合中
	    for (var j = 0; j < arr.length; j++) {
	        if (arr[j] == id) {
	            return true;
	        }
	    }
	    return false;
	}

	//新增TAB
	function addTab(title, url) {
        var tt = $('#tabs');
        if (tt.tabs('exists', title)) {//如果tab已经存在,则选中并刷新该tab
            tt.tabs('select', title);
        } else {
            if (url) {
                var content = '<iframe scrolling="auto" name="nnn" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>';
            } else {
                var content = '未实现';
            }
            tt.tabs('add', {
                title: title,
                closable: true,
                content: content
            });
        }
        var cfg = {
            tabTitle: title,
            url: url
        }
        refreshTab(cfg);
    }

	//刷新TAB
	function refreshTab(cfg) {
        var refresh_tab = cfg.tabTitle ? $('#tabs')
			.tabs('getTab', cfg.tabTitle) : $('#tabs').tabs('getSelected');
        if (refresh_tab && refresh_tab.find('iframe').length > 0) {
            var _refresh_ifram = refresh_tab.find('iframe')[0];
            var refresh_url = cfg.url ? cfg.url : _refresh_ifram.src;
            //_refresh_ifram.src = refresh_url;
            _refresh_ifram.contentWindow.location.href = refresh_url;
        }
    }

	</script>
	
</head>

<body class="easyui-layout">
	<div region="north" border="false" style="height: 65px;">
		<div style="position: relative; width: 100%; height:100%; background: url('${path}/resource/image/bg.gif'); ">
			<div style="text-align: center;"><img src="${path}/resource/image/logo.jpg" /></div>
			<div style="position:absolute;bottom:0;width:100%;">
				<a href="javascript:void(0);" id="btnRole0" class="easyui-linkbutton" plain="true" onclick="addTab('联系人管理','usermanager.htm');">联系人管理</a>|
				<a href="javascript:void(0);" id="btnRole1" class="easyui-linkbutton" plain="true" onclick="addTab('短信管理','smsmanager.htm');">短信管理</a>|
				<a href="javascript:void(0);" id="btnRole2" class="easyui-linkbutton" plain="true" onclick="addTab('分析与统计','analysis.htm');">分析与统计</a>|
				<a href="javascript:void(0);" id="btnRole3" class="easyui-linkbutton" plain="true" onclick="addTab('导入/导出','exchange.htm');">导入/导出</a>
			</div>
		</div>
	</div>
	
	<div region="center" style="overflow:auto;">
		<div id="tabs" class="easyui-tabs" fit="true" border="false">
			<div title="首页">
				<div style="width: 100%; height: 210px;">
					<div style="width: 400px; height: 200px; float: left; padding: 5px;">
						<div id="p" class="easyui-panel" iconCls="icon-config" title="概述" fit="true" style="padding: 10px; ">
							<ul>
								<li>联系人: 位</li>
								<li>短信合计: 条 </li>
								<li>第一条短信:</li>
								<li>最后条短信:</li>
							</ul>
						</div> 
					</div>
					<div style="width: 400px; height: 200px; float: left; padding: 5px;">
						&nbsp;
					</div>
				</div>
				<div style="width: 100%; height: 210px;">
					<div style="width: 400px; height: 200px; float: left; padding: 5px;">
						<div id="p2" class="easyui-panel" iconCls="icon-tip" title="版本说明" fit="true" style="padding: 10px; ">
							<ul>
								<li>基于Java EE MVC模式</li>
								<li>后台框架: Spring3, Hibernate4</li>
								<li>前端框架及插件: jQuery, jQuery EasyUI</li>
								<li>图形方案: JFreeChart</li>
								<li>数据库: PostGreSQL</li>
							</ul>
						</div>
					</div>
					<div style="width: 400px; height: 200px; float: left; padding: 5px;">
						&nbsp;
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div id="southdiv" region="south" style="height: 30px; background: url('${path}/resource/image/bg.gif') repeat-x; text-align: center; font-size: 14px; font-weight: bold; padding-top: 4px; ">
		Sylar短信管理系统 V3.0 JavaEE版  @2012-6
	</div>
</body>
</html>