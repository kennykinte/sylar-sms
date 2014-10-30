<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>联系人管理</title>
	<%@ include file="common.jsp"%>
	<script type="text/javascript">
	$(function(){
		initUserTree();
		initUserGrid();
	});

	// 加载 联系人分组数据
    function initUserTree() {
        $.getJSON('usergroup/qry.htm', function (data) {
            // 初始化左侧联系人分组树
        	$('#userTree').tree({
        		data: data,
        		animate: true,
                onClick: function (node) {
                    queryByGroup(node.id);  //点击查询并加载DataGrid
                },
                onContextMenu: function (e, node){ // 右键菜单[添加,删除分组]
                	e.preventDefault();
                	$('#userTree').tree('select', node.target);
                	$('#mm').menu('show', {
    					left: e.pageX,
    					top: e.pageY
    				});
                },
                onAfterEdit: function (node){  // 结束编辑状态(按回车或鼠标点击别的地方)
                	$.ajax({
                        url: 'usergroup/upd.htm',
                        type: 'post',
                        async: false,
                        data: {id: node.id, groupname: node.text},
                        success: function (data) {
                            if (!data.isSuccess) {
                                $.messager.alert('提示', '新增分组失败', 'error');
                            } else {
                                $.messager.alert('提示', '新增分组成功', 'info', initUserTree);
                            }
                        }
                    });
                },
                onCancelEdit: function (node){ // 取消编辑状态(按Esc)
                    alert('取消编辑');
                }
            });
            // 初始化[移动分组]分组Combox
            $('#groupid').combobox({
                data: data[0].children,  // 注意这个controller返回的是JSON数组 
                valueField: 'id',
                textField: 'text',
                editable: false
            });
        });
    }

	//修改分组(名称)
    function updateGroup(){
        var node = $('#userTree').tree('getSelected');
        if(node && node.id != 999){
        	$('#userTree').tree('beginEdit', node.target);
        }
    }

    //添加分组
    function appendGroup(){
    	var node = $('#userTree').tree('getRoot');
		$('#userTree').tree('append',{
			parent: node.target,
			data:[{
				id: 998,
				//text:'新建分组',
				checked:true
			}]
		});
		var node2 = $('#userTree').tree('find', 998);
		$('#userTree').tree('beginEdit', node2.target);
    }

    //移除分组
    function removeGroup(){
    	var node = $('#userTree').tree('getSelected');
        if(node && node.id != 999 && node.id != 0){
        	$.ajax({
                url: 'usergroup/del.htm',
                type: 'post',
                async: false,
                data: {id: node.id},
                success: function (data) {
                    if (!data.isSuccess) {
                        $.messager.alert('提示', '移除分组失败, 错误信息: ' + data.errorMsg, 'error');
                    } else {
                        $.messager.alert('提示', '移除分组成功', 'info', initUserTree);
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown){
                	$.messager.alert('提示', 'Ajax调用失败', 'error');
                }
            });
        }else{
            if(node.id == 999)
        		$.messager.alert('提示', '不可以移除根节点', 'error');
            if(node.id == 0)
        		$.messager.alert('提示', '不可以移除默认组', 'error');
        }
    }

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------
    //联系人GRID
    
	//初始化联系人GRID
	function initUserGrid(){
	    $('#userGrid').datagrid({
            width: '100%',
            //height: grid_default_height,
            nowrap: true,
            striped: true,
            //singleSelect: true,
            pageNumber: 1,
            pageSize: 15,
            pageList: [5,10,15,20],
            queryParams: {groupid: 999},  // 为999时,查询所有联系人 
            url: 'userlist.htm',
            sortName: 'username',
            sortOrder: 'asc',
            //remoteSort: true,
            method: 'post',
            idField: 'id',
            fitColumns: true,
            rownumbers: true,
            columns: [[
		        { title: '选项框', field: 'id', width: $(this).width() * 0.1, sortable: true, checkbox: true },
                { title: '姓名', field: 'username', width: $(this).width() * 0.1 ,sortable: true },
                { title: '手机号码', field: 'telephone', width: $(this).width() * 0.1 ,sortable: true },
                { title: '分组', field: 'groupText', width: $(this).width() * 0.1 ,sortable: false },
                { title: '性别', field: 'sexText', width: $(this).width() * 0.1 ,sortable: false },
                { title: '备注', field: 'note', width: $(this).width() * 0.1 ,sortable: false }
		    ]],
            onLoadError: function () { $.messager.alert('提示', '数据加载出错', 'error'); },
            onDblClickRow: function(rowIndex, rowData){ open('修改联系人','user.htm?type=M&id=' + rowData.id); },
            onRowContextMenu: function(e, rowIndex, rowData){
            	e.preventDefault();
            	$('#gridmm').menu('show', {
					left: e.pageX,
					top: e.pageY
				});
            },
            pagination: true
        });
	}
	
	//按分组查询联系人
	function queryByGroup(groupid){
		 $('#userGrid').datagrid('options').queryParams = { groupid: groupid };	
		 $('#userGrid').datagrid('options').pageNumber = 1;
		 $("#userGrid").datagrid('reload');
         $('#userGrid').datagrid('clearSelections');
	}

	//-------------------不再使用Begin------------------------------------
	//查看联系人
	function ViewUser(){
		var selected = $('#userGrid').datagrid('getSelected');
        if (selected) {
        	open('查看联系人','user.htm?type=V&id=' + selected.id);
        } else {
            $.messager.alert("提示", "请选择要查看的联系人", "info");
            return false;
        }
	}
	//-------------------不再使用End------------------------------------
	
	//新增联系人
	function addUser(){
		var selected = $('#userTree').tree('getSelected');
		if(selected){
			open('新增联系人','user.htm?type=A&id=0&groupid=' + selected.id); 
		}else{
			open('新增联系人','user.htm?type=A&id=0&groupid=0'); // 未选择分组的情况下, 设置为0--"未分组好友" 
		}
	}

	//删除联系人 (可批量删除)
	function delUser(){
		var ids = getUserGridSelections();
		if (ids != '') {
        	$.ajax({
                url: 'useraction.htm?type=del',
                type: 'post',
                async: false,
                data: {id: ids},
                success: function (data) {
                    if (!data.isSuccess) {
                        $.messager.alert('提示', '删除联系人失败', 'error');
                    } else {
                        $.messager.alert('提示', '删除联系人成功', 'info',
			            	 function () {
                        		clearSelections();
			            	    reloadDatagrid();
			            	 }
                        );
                    }
                }
            });
        } else {
            $.messager.alert('提示', '请选择要删除的联系人', 'info');
            return false;
        }
	}

	//移动联系人(可批量移动) -- 开窗
	function moveUser(){
		var ids = getUserGridSelections();
		if (ids != '') {
			$('#moveGroup').window('open');
			$('#groupid').combobox('setValue', 0);
		} else {
            $.messager.alert('提示', '请选择要移动的联系人', 'info');
            return false;
        }	
	}

	//提交移动
	function commitMove(){
		var ids = getUserGridSelections();
		var groupid = $('#groupid').combobox('getValue');
		//alert(ids +'值:'+groupid);
		$.ajax({
            url: 'useraction.htm?type=move',
            type: 'post',
            async: false,
            data: {id: ids, groupid: groupid},
            success: function (data) {
                if (!data.isSuccess) {
                    $.messager.alert('提示', '移动联系人分组失败', 'error');
                } else {
                	closeMoveUser();
            		clearSelections();
            	    reloadDatagrid();
                }
            }
        });
	}

	//关闭移动联系人窗口
	function closeMoveUser(){
		$('#moveGroup').window('close');
	}

	//联系人Grid多选(批量选择联系人)
	function getUserGridSelections(){
		var Selections = $('#userGrid').datagrid('getSelections');
		var ids = '';
		$.each(Selections, function (index, domEle) {
			ids += domEle.id + ',';
		});
		if (ids.charAt(ids.length - 1) == ',') {
			ids = ids.substr(0, ids.length - 1);
        }
        return ids;
	}

	//开窗 - 联系人详细页
	function open(dialogTitle, path){
		openDialogIframe(dialogTitle, 'openDialog', 'openDialogContent', path);
	}

	//关窗 - 联系人详细页
	function closeDialog(){
		$('#openDialog').dialog('close');
	}

	//刷新联系人GRID
    function reloadDatagrid() {
        $('#userGrid').datagrid('load');
    }

    //清除联系人GRID选择框 
    function clearSelections() {
        $('#userGrid').datagrid('clearSelections');
    }
	
	</script>
</head>
<body class="easyui-layout">
	<div region="west" iconCls="icon-roles" title="联系人分组" split="true" style="width: 180px;padding-top: 10px;">
		<ul id="userTree"></ul>
		<div id="mm" class="easyui-menu" style="width:120px;">
			<div onclick="updateGroup()" iconCls="icon-edit">修改分组</div>
			<div class="menu-sep"></div>
			<div onclick="appendGroup()" iconCls="icon-add">添加分组</div>
			<div onclick="removeGroup()" iconCls="icon-remove">删除分组</div>
		</div>
	</div>
	<div region="center" style="overflow:hidden;padding: 10px;">
		<div style="padding: 5px;border: 1px solid #CCC;margin-bottom: 5px;">
			<a href="javascript:void(0);" id="btnAdd" class="easyui-linkbutton" iconcls="icon-add" onclick="addUser()">新增</a> 
        	<a href="javascript:void(0);" id="btnUpd" class="easyui-linkbutton" iconcls="icon-cancel" onclick="delUser()">删除</a> 
        	<a href="javascript:void(0);" id="btnDel" class="easyui-linkbutton" iconcls="icon-redo" onclick="moveUser()">移动</a>
        	<span style="font-weight:bold;color: red;">（双击进行修改）</span> 
		</div>
		<div>
			<table id="userGrid"></table>
			<div id="gridmm" class="easyui-menu" style="width:120px;">
				<div onclick="javascript:;" iconCls="icon-edit">修改</div>
			</div>
			<div id="openDialog" class="easyui-dialog" modal="true" closed="true" style="width: 500px; height: 300px;padding: 0;">
            	<iframe id="openDialogContent" scrolling="auto" frameborder="0" src="" style="width: 100%; height: 100%;"></iframe>
            </div>
            <div id="moveGroup" title="移动分组" class="easyui-window" 
            	iconCls="icon-redo" modal="true" closed="true" collapsible="false" minimizable="false" maximizable="false"
            	style="width: 300px; height: 150px; padding: 5px;" >
            	<div class="easyui-layout" fit="true">	
            		<div region="center" border="false" style="padding:10px;background:#fff;border:1px solid #ccc;">
            			移动分组到
            			<br />
            			<input id="groupid" name="groupid" panelHeight="auto" style="width:200px;" />
            		</div>
					<div region="south" border="false" style="text-align:right;padding:5px 0;">
						<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0);" onclick="commitMove()">确定</a>
						<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0);" onclick="closeMoveUser()">取消</a>
					</div>	
            	</div>	
            </div>
		</div>
	</div>
</body>
</html>