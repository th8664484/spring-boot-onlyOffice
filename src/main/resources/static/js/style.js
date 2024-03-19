
var fileTable;
var element;
var laydate;
var upload;
var layer;
var table;
var form;

let pdf = ['doc','docx','xls','xlsx','ppt','pptx','txt'];

layui.use(['element', 'layer', 'util', 'carousel', 'form', 'table', 'upload', 'laydate'], function () {
    laydate = layui.laydate;
    element = layui.element;
    upload = layui.upload;
    layer = layui.layer;
    table = layui.table;
    form = layui.form;


    fileTable = table.render({
        elem: '#fileTable'
        , url: '/file/all'
        , toolbar: '#toolFile' //开启头部工具栏，并为其绑定左侧模板
        , defaultToolbar: []
        , height: 'full-170'
        , title: '文件列表'
        , cols: [[
            {title: '编号', type: 'numbers', align: 'center',width:50}
            , {field: 'fileName', title: '文件名称', align: 'center'}
            , {field: 'fileType', title: '文件类型', align: 'center'}
            , {field: 'fileSize', title: '大小', align: 'center'}
            , {field: 'version', title: '版本', align: 'center'}
            , {fixed: 'right', title: '操作',  align: 'center',templet:function (row){
                var html ='';
                var className = 'layui-bg-orange'
                if (!pdf.includes(row.fileType)) {
                    className = 'layui-btn-disabled'
                }

                html +='  <button class="layui-btn layui-btn-sm '+className+'" lay-event="converted">PDF</button>' +
                    '    <button class="layui-btn layui-bg-blue layui-btn-sm" lay-event="edit">编辑</button>' +
                    '    <button class="layui-btn layui-bg-blue layui-btn-sm" lay-event="view">查看</button>' +
                    '    <button class="layui-btn layui-btn-sm" lay-event="download">下载</button>' +
                    '    <button class="layui-btn layui-btn-danger layui-btn-sm"  lay-event="del" >删除</button>';
                return html;
            }}
        ]],
        skin: 'line',
        size: 'lg',
        page: true,
        done: function () {
            $(".layui-table-header tr").css("background-color", "#b0b0b0");
        }
    });

//监听行工具事件
    table.on('tool(fileTable)', function (obj) {
        var data = obj.data;
        if (obj.event == 'del') {
            layer.confirm('你确定要删除【' + data.fileName + '】这个文件吗？', {icon: 3, title: '提示'}, function (index) {
                ajax('/del', {id: data.fileId}, 'post').then(function (res) {
                    layer.msg(res.message)
                    table.reload("fileTable");
                })
            });
        } else if (obj.event == 'download') {
            download("/download/" + data.fileId+"?isBrowser=true")
        } else if(obj.event == 'converted'){
            download( "/converted?id=" + data.fileId+"&suffix=pdf")
        } else if(obj.event == 'edit'){
            // window.open('/onlyOffice/edit/'+data.id);
            openDocument(data.fileId,'edit','在线编辑')
        }else if (obj.event == 'view'){
            openDocument(data.fileId,'view','在线查看')
        }
    });
    //清空表单
    $(".clean").on('click', function () {
        $("form")[0].reset();

        $(".xm-select-parent .xm-select").attr("title", '')
        $("div[xm-select-skin=default] .xm-select-title div.xm-select-label>span").remove()
        $(".xm-select-parent .xm-select--suffix input").attr("placeholder", "请选择");
        $(".xm-select-parent .xm-form-select dl dd").removeClass("xm-select-this")

    })

    /**提交form表单*/
    form.on('submit(formDemo)', function (data) {
        var obj = data.field
        console.log(obj)
        ajax(data.form.action, obj, data.form.method).then(function (res) {
            layer.msg(res.msg, {time: 1000}, function () {
                layer.closeAll();
                table.reload(res.data);
            });
        })
        return false;
    });

});

function openDocument(id,mode,txt){
    var index =  layer.open({
        type: 2, // page 层类型
        offset: 'lb',
        area: ['150px', '50px'],
        title: txt,
        shade: 0, // 遮罩透明度
        shadeClose: true, // 点击遮罩区域，关闭弹层
        maxmin: true, // 允许全屏最小化
        anim: 0, // 0-6 的动画形式，-1 不开启
        content: `/onlyOffice/${mode}/${id}`,
        btn: ['保存（autosave/forcesave是默认值时生效）'],
        btnAlign: 'c',
        btn1: function(index, layero, that){
            ajax('/save',{id:id},"get").then(function (res) {
                layer.msg(res.message)
            })
            return false
        },

        success: function(layero, index){
            layer.full(index); // 最大化
        },
        cancel: function(index, layero){
            // 获取 iframe 中 body 元素的 jQuery 对象
            console.log(index,layero)
            layer.close(index);
        }
    });
}


/**格式化时间*/
function dateFormatter(date) {
    //Object对象转为日期格式
    var date = new Date(date);
    var strDate = date.getFullYear() + "-";
    var month = date.getMonth() + 1;
    var day = date.getDate();
    //格式化日期,月日时分秒保持两位
    strDate = strDate + (month > 9 ? month : "0" + month) + "-"
        + (day > 9 ? day : "0" + day) + "&nbsp"
    return strDate;
}

/**关闭弹窗*/
function closeEditWin(){
    layui.use('layer',function(){
        var layer = layui.layer ;
        layer.closeAll();
    });
}





