<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>联系人管理</title>
<%@ include file="common.jsp"%>

<script type="text/javascript">

	// 由后台模型添加----------------------------------------------------------------------------
	// 开启页面操作类型:A:新增; M:修改; V:查看(暂不使用)
	// 字符串,要带引号...
	var type = '${type}';	

	// 新增状态: 存放一个数字,即分组id, 前端未选择分组的情况下,传递0(未分组好友)进来
	// 查看,修改状态: 存放JSON数据
	// 数字或JSON都不带引号...
	var model = ${model};	
	// 由后台模型添加----------------------------------------------------------------------------

	
	$(function(){
		initGroupCombox();
	});

	//初始化分组Combobox
    function initGroupCombox() {
        $.getJSON('usergroup/qry.htm', function (data) {
            $('#groupid').combobox({
                data: data[0].children,  // 注意这个controller返回的是JSON数组 
                valueField: 'id',
                textField: 'text',
                editable: false,
                onLoadSuccess: getData
            });
        });
    }

    //V,M模式下载入数据
    function getData(){
        if(type=='V' || type == 'M'){
            $('#id').val(model.user.id);
            $('#username').val(model.user.username);
            $('#telephone').val(model.user.telephone);
            $('#note').val(model.user.note);
            $('#groupid').combobox('setValue', model.user.groupid);
            $('input[name="sex"][value="' + model.user.sex + '"]').attr("checked", "checked");
        }else{
            if(model = 999)
                model = 0;
        	$('#groupid').combobox('setValue', model);  // 
        }	
    }

	//确定按钮
	function commit(){
		var param = $('form').serialize();	
		var url = 'useraction.htm?type=';  //ins,upd,del
		//新增联系人
		if(type=='A'){
			$.ajax({
                url: url+'ins',
                type: 'post',
                async: false,
                data: param,
                success: function (data) {
                    if (!data.isSuccess) {
                        $.messager.alert('提示', "添加联系人失败, 错误信息: " + data.errorMsg, "error");
                    } else {
                        $.messager.alert('提示', "添加联系人成功", "info",
			            	 function () {
                        		window.parent.closeDialog();	
                        		window.parent.clearSelections();
			            	    window.parent.reloadDatagrid();
			            	 }
                        );
                    }
                }
            });
        //更新联系人    
		}else if(type == 'M'){
			$.ajax({
                url: url+'upd',
                type: 'post',
                async: false,
                data: param,
                success: function (data) {
                    if (!data.isSuccess) {
                        $.messager.alert('提示', "更新联系人失败, 错误信息: " + data.errorMsg, "error");
                    } else {
                        $.messager.alert('提示', "更新联系人成功", "info",
			            	 function () {
                        		window.parent.closeDialog();	
			            	    window.parent.reloadDatagrid();
			            	    window.parent.clearSelections();
			            	 }
                        );
                    }
                }
            });
		}
	}

	
	//返回按钮
	function back(){
		window.parent.closeDialog();
	}
</script>

</head>
<body>
	<div style="padding:10px;background:#fff;border:1px solid #ccc;">
		<form id="form" name="form">
			<table>
				<tr>
					<td align="right">姓名</td>
					<td>
						<input type="hidden" id="id" name="id" />
						<input type="text" id="username" name="username" style="width:200px;" />
					</td>
				</tr>
				<tr>
					<td align="right">手机号码</td>
					<td><input type="text" id="telephone" name="telephone" style="width:200px;" /></td>
				</tr>
				<tr>
					<td align="right">分组</td>
					<td>
						<input id="groupid" name="groupid" panelHeight="auto" style="width:200px;" />
					</td>
				</tr>
				<tr>
					<td align="right">性别</td>
					<td>
						<input type="radio" id="sex_0" name="sex" value="0" checked="checked"/>男
						<input type="radio" id="sex_1" name="sex" value="1"/>女
					</td>
				</tr>
				<tr>
					<td align="right" style="vertical-align: top;">备注</td>
					<td>
						<textarea id="note" name="note" style="width:200px;font-size: 12px;"></textarea>
					</td>
				</tr>
			</table>
		</form>
    </div>
    <div style="text-align:right;padding:5px 0;">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="commit()" id="btnConfirm">确定</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="back()" id="btnBack">取消</a>
	</div>	
</body>
</html>