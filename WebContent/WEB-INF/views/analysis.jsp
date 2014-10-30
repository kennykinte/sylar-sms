<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>统计图</title>
<%@ include file="common.jsp"%>
<script type="text/javascript">
	$(function() {
		initAnaGrid();
		getchart();
	});

	//获取分析
	function initAnaGrid() {
		$('#anaGrid').datagrid({
			width : '100%',
			nowrap : true,
			striped : true,
			pageNumber : 1,
			pageSize : 10,
			pageList : [ 5, 10, 15, 20 ],
			url : 'analysis/getanalysis.htm',
			queryParams : {
				smstype : 'all'
			},
			sortName : 'count',
			sortOrder : 'desc',
			remoteSort : true,
			method : 'post',
			idField : 'username',
			fitColumns : true,
			rownumbers : true,
			columns : [ [ {
				title : '姓名',
				field : 'username',
				width : $(this).width() * 0.1,
				sortable : false
			}, {
				title : '条数',
				field : 'count',
				width : $(this).width() * 0.1,
				sortable : true
			}, {
				title : '首条',
				field : 'first',
				width : $(this).width() * 0.1,
				sortable : true
			}, {
				title : '末条',
				field : 'latest',
				width : $(this).width() * 0.1,
				sortable : true
			} ] ],
			onLoadError : function() {
				$.messager.alert('提示', '数据加载出错', 'error');
			},
			pagination : true
		});
	}

	//按短信类别重分析
	function getanalysis() {
		$('#anaGrid').datagrid('options').queryParams = {
			smstype : $('input[name="ana_smstype"]:checked').val()
		};
		$('#anaGrid').datagrid('options').pageNumber = 1;
		$("#anaGrid").datagrid('reload');
	}

	//获取图表
	function getchart() {
		var showcount = $('#showcount').val();
		var smstype = $('input[name="smstype"]:checked').val();
		var charttype = $('input[name="charttype"]:checked').val();
		var url = 'analysis/getchart.htm?smstype=' + smstype + '&showcount='
				+ showcount + '&charttype=' + charttype;
		$('#chart').html('<image src="' + url + '" />');
	}

	//新增TAB
	function addTab(title) {
		var tt = $('#tabs');
		if (tt.tabs('exists', title)) {//如果tab已经存在,则选中并刷新该tab
			tt.tabs('select', title);
		} else {
			tt.tabs('add', {
				title : title,
				closable : true,
				content : '暂未实现...'
			});
		}
	}
</script>
</head>
<body class="easyui-layout">
	<div region="west" iconCls="icon-roles" title="功能菜单" split="true" style="width: 180px; padding: 10px;">
		<ul id="menu">
			<li><a href="javascript:void(0);" class="easyui-linkbutton" iconcls="icon-analysis" plain="true" onclick="addTab('分析')" style="width: 120px;">分析</a></li>
			<li><a href="javascript:void(0);" class="easyui-linkbutton" iconcls="icon-chart" plain="true" onclick="addTab('图表')" style="width: 120px;">图表</a></li>
		</ul>
	</div>
	<div region="center" style="overflow: hidden; padding: 0;">
		<div id="tabs" class="easyui-tabs" fit="true" border="false">
			<div title="分析" style="padding: 10px;">
				<table style="width: 100%; border-collapse: collapse; border-top: 2px solid #91ABD3; margin-bottom: 5px; ">
					<tr>
						<td style="width: 250px; border: 1px solid #CCC; padding: 4px;">
							短信类型: 
							<input type="radio" name="ana_smstype" value="all" checked="checked" />全部 
							<input type="radio" name="ana_smstype" value="rec" />发送 
							<input type="radio" name="ana_smstype" value="snd" />接收
						</td>
						<td style="border: 1px solid #CCC; padding: 4px;">
							<a href="javascript:void(0);" class="easyui-linkbutton" iconcls="icon-analysis" onclick="getanalysis()">分析</a>
						</td>
					</tr>
				</table>	
				<table id="anaGrid"></table>
			</div>
			<div title="图表" style="padding: 10px;">
				<table style="width: 100%; border-collapse: collapse; border-top: 2px solid #91ABD3; ">
					<tr>
						<td style="width: 145px; border: 1px solid #CCC; padding: 4px;">
							统计前: 
							<input type="text" id="showcount" name="showcount" value="10" style="width: 40px;" /> 
							位好友
						</td>
						<td style="width: 250px; border: 1px solid #CCC; padding: 4px;">
							短信类型: 
							<input type="radio" name="smstype" value="all" checked="checked" />全部 
							<input type="radio" name="smstype" value="rec" />发送 
							<input type="radio" name="smstype" value="snd" />接收
						</td>
						<td style="width: 200px; border: 1px solid #CCC; padding: 4px;">
							图表类型: 
							<input type="radio" name="charttype" value="bar" checked="checked" />条形图 
							<input type="radio" name="charttype" value="pie" />饼图
						</td>
						<td style="border: 1px solid #CCC; padding: 4px;">
							<a href="javascript:void(0);" class="easyui-linkbutton" iconcls="icon-chart" onclick="getchart()">统计</a>
						</td>
					</tr>
				</table>
				<div id="chart" style="padding: 5px;"></div>
			</div>
		</div>
	</div>
</body>
</html>