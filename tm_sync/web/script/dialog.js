/**
 * 打开弹出框,通过获取当前浏览器的宽和高，乘以比例来打开弹出框的大小
 * @param title
 * @param width
 * @param height
 * @param url
 */
function openD(div, title, width, height, url){
    div.dialog({
        title: title,
        width: getWindowWidthSize() * width,
        height: getWindowHeightSize() * height,
        closed: false,
        cache: false,
        modal: true,
        href: url
    }).dialog("open");
}

/**
 * 关闭弹出框
 */
function closeD(div){
    div.dialog("close");
}

/**
 * 销毁弹出框
 * @param div
 */
function destroyD(div){
	div.dialog("destroy");
    var divId = div.attr("id");
    var createDiv = $("<div></div>");
	createDiv.attr("id",divId);
	createDiv.appendTo('body');
}

function removeD(div){
	var divId = div.attr("id");
	div.remove();
    var createDiv = $("<div></div>");
	createDiv.attr("id",divId);
	createDiv.appendTo('body');
}

/**
 * 刷新弹出框
 * @param div
 */
function refreshD(div){
    div.dialog('refresh');
}
