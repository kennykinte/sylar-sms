<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>联系人管理</title>
<%@ include file="common.jsp"%>
	<style type="text/css">
	/**
	 * 作用在短信显示上的CSS伪类:
	 * 1 - 4. smsDate(span: 日期), smsHR(hr: 日期下水平线), smsTime(span: 时间), smsContent(p: 短信内容)
	 * 5. smsListElement(li: 短信对象),这个还能用于jQuery做选择器添加特效	
	 * 6. timeNoFixed (div: 时间未修复) 在时间前加一个红色警告小图标
	 * 7. smsSelectOn 点击smsListElement(li: 短信对象)时添加CSS效果
	 */
	.smsDate {color: #00F; font-weight: bold;}
	.smsHR {border: 1px solid #00F;}
	.smsTime {color: #F00; font-weight: bold;}
	.smsContent{padding-left: 10px;}
	.smsListElement {border: 1px solid #FFF; padding: 2px;}
	.timeNoFixed {float: left; width: 16px; height: 16px; background-image: url("${path}/resource/plugin/jquery-easyui-1.2.6/themes/default/images/validatebox_warning.png");}
	.smsSelectOn {border: 1px solid #487BFF; background-color: #D4EAFF;}
	</style>

	<script type="text/javascript">
	$(function(){
		initUserTree();
	});

	// 初始化左侧联系人分组树
	function initUserTree(){
		$('#userTree').tree({
			url: 'sms/usergroup.htm?type=group&groupid=0',
			animate: true,
			onBeforeExpand: function(node, param){  
                $('#userTree').tree('options').url = 'sms/usergroup.htm?type=user&groupid=' + node.id;
            }, 
	        onClick: function (node) {
	            if(node.attributes){
		            $('#recSmsPagination').show();
		            $('#sndSmsPagination').show();
	            	loadSms(node.attributes, 1, 1, 50);
	            	loadSms(node.attributes, 0, 1, 50);
	            }else{  // attributes值为false时,表分组节点,点击时展开/折叠该节点
	            	$('#userTree').tree('toggle', node.target);
	            }
	        }
	    });
	}
	
	/**
	 * 加载短信
	 * @userTel		手机号码
	 * @smstype		1:接收的; 0:发送的
	 * @page		页码
	 * @pagesize2	页容  (起名叫2是怕和pagination的onSelectPage回调函数参数冲突)
	*/
	function loadSms(userTel, smstype, page, pagesize2){
		var contentDiv, paginationDiv;
		if(smstype==1){
			contentDiv = '#recSmsContent';
			paginationDiv = '#recSmsPagination';
		}else{
			contentDiv = '#sndSmsContent';
			paginationDiv = '#sndSmsPagination';
		}
		$.getJSON('sms/qry.htm', 
			{
				userTel: userTel,
				smstype: smstype,
				page: page,
				pagesize: pagesize2
			},	
			function(json){
				//短信内容填充到页面
				$(contentDiv).html(fillMsg(json.smsContent));
				//单击特效
				//$('.smsListElement').click(function(){
        			
        		//});
				$('.smsListElement').mousedown(function(e){ 
					if(3 == e.which){		//右键 -- 默认菜单阻止不了, 需要的菜单弹不出来...
						//return false;
						//$('.smsListElement').removeClass('smsSelectOn');
	        			//$(this).addClass('smsSelectOn');  
						//alert('右键');
						//e.preventDefault();
						//$('#smsMM').menu('show', {
	    				//	left: e.pageX,
	    				//	top: e.pageY
	    				//});
			        }else if(1 == e.which){	//左键
			        	$('.smsListElement').removeClass('smsSelectOn');
	        			$(this).addClass('smsSelectOn');
			        } 
				});
        		//滚动条回到顶部
				$(contentDiv).scrollTop(0);
				//设置EasyUI分页插件
				$(paginationDiv).pagination({
					total: json.smsCount,
					pageNumber: page,
					pageSize: pagesize2,
					pageList: [20,50,100],
					onSelectPage: function(pageNumber, pageSize){
						loadSms(userTel, smstype, pageNumber, pageSize);
					}
				});
			}
		);
	}
	
	/**
	 * 短信内容填充到页面
	 * @jsonData 短信内容, JSON格式, 包含日期(列表)-->日期内短信(列表)-->短信(ID,telephone,posttime等)
	 * 目前定位到单条短信ID, 尚未定位到单日内短信集合
	*/
	function fillMsg(jsonData) {
		var msgList = '<ul>';
		$.each(jsonData, function(i,ite){
			msgList += '<li>';
			msgList += '<span class="smsDate">';
			msgList += '日期: ' + i;										//日期
			msgList += '</span>';
			msgList += '<hr class="smsHR" />';							//水平线
			msgList += '<ul>';
			$.each(ite, function(i,ite){
				msgList += '<li id="sms' + ite.id + '" class="smsListElement">';
				msgList += '<span class="smsTime">';
				msgList += new Date(ite.posttime).format("hh:mm:ss");	// 时间 (依赖DateFormat.js)
				msgList += '</span>';
				if(ite.fixed == 0){										//时间是未修复, 添加一个div, 背景是红色警告小图标
					msgList += '<div class="timeNoFixed"></div>';
				}
				msgList += '<br />';
				msgList += '<p class="smsContent">';
				msgList += ite.smscontent;								// 短信内容
				msgList += '</p>';
				msgList += '</li>';
			});
			msgList += '</ul>';
			msgList += '</li>';
		});
		msgList += '</ul>';
		return msgList;
	}
	
	</script>
</head>
<body class="easyui-layout">
	<div region="west" iconCls="icon-roles" title="联系人" split="true" style="width: 220px;padding-top: 10px;">
		<ul id="userTree"></ul>
	</div>
	<div region="center" style="overflow:hidden;padding: 0px;">
		<div style="width: 50%; height:100%; float: left; ">
			<div id="recSms" class="easyui-panel" title="接收的短信" fit="true" iconCls="icon-email" 
				 style="width: 100%; height: 100%; padding: 5px; background: #fafafa; overflow: hidden;" >
				<div id="recSmsContent" style="width: 100%; height: 94%; overflow: auto;"></div>
				<div id="recSmsPagination" style="background: #efefef; border: 1px solid #ccc; display: none; "></div>  
			</div>
		</div>
		<div style="width: 50%; height:100%; float: left; ">
			<div id="sndSms" class="easyui-panel" title="发送的短信" fit="true" iconCls="icon-emailgo" 
				 style="width: 100%; height: 100%; padding: 5px; background: #fafafa; overflow: hidden;">
				<div id="sndSmsContent" style="width: 100%; height: 94%; overflow: auto;"></div>
				<div id="sndSmsPagination" style="background: #efefef; border: 1px solid #ccc; display: none; "></div> 
			</div>
		</div>
		<div id="smsMM" class="easyui-menu" style="width:120px;">
			<div onclick="javascript:;" iconCls="icon-edit">修改</div>
		</div>
	</div>
</body>
</html>