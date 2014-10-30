/*
 * 打开Dialog以及Iframe
 * divtitle 找开的Dialog的标题(title)
 * divName 打开的Dialog的id
 * framName Iframe的id
 * PageUrl Iframe的URL
 */
function openDialogIframe(divtitle, divName, framName, PageUrl) {
	var iframe01 = document.getElementById(framName);
	$('#' + divName).dialog('setTitle', divtitle);
	var url = PageUrl;
	iframe01.src = url;
	if (iframe01.attachEvent) {
		iframe01.attachEvent("onload", function() {
			$('#' + divName).dialog('open');
		});
	} else {
		iframe01.onload = function() {
			$('#' + divName).dialog('open');
		};
	}
}