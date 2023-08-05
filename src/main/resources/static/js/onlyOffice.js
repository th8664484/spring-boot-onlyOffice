


function onprint(){
    layer.msg('正在加载中...<i class="layui-icon layui-icon-loading"></i>',{time: 100000});
    onPrintUrl('/print/'+id)
}
function dow(){
    download(id)
}

function onPrintUrl(t) {
    if (this.iframePrint && (this.iframePrint.parentNode.removeChild(this.iframePrint),
        this.iframePrint = null),
        !this.iframePrint) {
        var e = this;
        this.iframePrint = document.createElement("iframe"),
            this.iframePrint.id = "id-print-frame",
            this.iframePrint.style.display = "none",
            this.iframePrint.style.visibility = "hidden",
            this.iframePrint.style.position = "fixed",
            this.iframePrint.style.right = "0",
            this.iframePrint.style.bottom = "0",
            document.body.appendChild(this.iframePrint),
            this.iframePrint.onload = function() {
                try {
                    layer.closeAll();
                    e.iframePrint.contentWindow.focus(),
                        e.iframePrint.contentWindow.print(),
                        e.iframePrint.contentWindow.blur(),
                        window.focus()
                } catch (t) {
                    console.log("打印出错")
                }
            }
    }
    t && (this.iframePrint.src = t)
}


const innerAlert = function (message) {
    if (console && console.log){
        console.log(message);
        // docEditor.showMessage("文件加载完成....");
    }
};
const onReady = function () {
    innerAlert("文件加载完成....");
};
/**
 * 文档改变后的回调 onDocumentStateChange:{"target":{"frameOrigin":"http://192.168.31.201:9001"},"data":true}
 * @param event
 */
const onDocumentStateChange = function (event) {
    console.log("onDocumentStateChange:"+JSON.stringify(event));
    const title = document.title.replace(/\*$/g, "");
    console.log("onDocumentStateChange:"+JSON.stringify(title));
    document.title = title + (event.data ? "*" : "");
};
/**
 * 用户正在尝试通过单击“编辑文档”按钮将文档从查看模式切换到编辑模式。
 */
const onRequestEditRights = function () {
    location.href = location.href.replace(RegExp("action=view\&?", "i"), "");
};

/**
 * 发生错误或其他一些特定事件。
 * @param event
 */
const onError = function (event) {
    if (event) innerAlert(event.data);
};
/**
 * 显示错误后调用的函数，当使用旧文档打开文档进行编辑时.key值，该值用于编辑以前的文档版本并已成功保存。
 * 调用此事件时，必须使用新文档重新初始化编辑器.key。
 * @param event
 */
const onOutdatedVersion = function (event) {
    location.reload(true);
};
/**
 * 当用户尝试通过单击“版本历史记录”按钮显示文档版本历史记录时调用的函数。
 * 若要显示文档版本历史记录，必须调用refreshHistory方法。
 * 如果未声明该方法和onRequestHistoryData方法，则不会显示“版本历史记录”按钮。
 */
const onRequestHistory = function (){
    docEditor.refreshHistory();
}
/**
 * 当用户尝试通过单击“关闭历史记录”按钮从查看文档版本历史记录返回到文档时调用的函数。
 * 调用函数时，必须在编辑模式下再次初始化编辑器。
 * 如果未声明该方法，则不会显示“关闭历史记录”按钮。
 */
const onRequestHistoryClose = function () {
    document.location.reload();
};
/**
 * 当用户尝试单击文档版本历史记录中的特定文档版本时调用的函数。
 * 若要显示与特定文档版本对应的更改，必须调用setHistoryData方法。
 * 文档版本号在数据参数中发送。 如果未声明该方法和onRequestHistory方法，则不会显示“版本历史记录”按钮。
 */
const onRequestHistoryData = function (event) {
    var version = event.data;
    docEditor.setHistoryData()
};
/**
 * 当用户尝试通过单击“存储中的图像”按钮插入图像时调用的函数。 图像插入的类型在参数data.c 中指定。
 * 若要将图像插入到文件中，必须使用指定的命令调用insertImage方法。 如果未声明该方法，则不会显示“存储中的图像”按钮。
 * add, change, fill, watermark, slide.
 */
const onRequestInsertImage = function (event) {
    docEditor.insertImage({
        "c": "watermark",
        "images": [
            {
                "fileType": "jpg",
                "url": "http://192.168.49.1:8080/images/logo.jpg"
            }
        ]
    });
};
/**
 * 当用户尝试通过单击“将副本另存为”来保存文件时调用的函数...按钮。
 * 文档的标题、类型和要下载的文档的绝对 URL 在data参数中发送。
 * 如果未将该方法声明为“将副本另存为...按钮将不会显示。
 * @param event
 */
const onRequestSaveAs = function (event) {
    var title = event.data.title;
    var url = event.data.url;
    var data = {
        title: title,
        url: url
    };
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "IndexServlet?type=saveas");
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify(data));
    xhr.onload = function () {
        innerAlert(xhr.responseText);
        innerAlert(JSON.parse(xhr.responseText).file, true);
    }
};
/**
 * 通过meta命令更改文档的元信息时调用的函数。
 * 文档的名称在data.title参数中发送。
 * 收藏夹图标突出显示状态在data.favorite参数中发送。
 * 当用户单击收藏夹图标时，将调用setFavorite方法来更新有关收藏夹图标突出显示状态的信息。 如果未声明该方法，则不会更改“收藏夹”图标。
 * @param event
 */
const onMetaChange = function (event) {
    if (event.data.favorite) {
        var favorite = !!event.data.favorite;
        var title = document.title.replace(/^\☆/g, "");
        document.title = (favorite ? "☆" : "") + title;
        docEditor.setFavorite(favorite);  // change the Favorite icon state
    }
    innerAlert("onMetaChange: " + JSON.stringify(event.data));
};
