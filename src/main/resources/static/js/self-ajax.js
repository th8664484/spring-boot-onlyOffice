function ajax(url,data,type){
    return new Promise(function(resolve, reject) {
        $.ajax({
            type: type,
            url: url,
            data: data,
            synch:true,
            dataType:'json',
            success:function(res){
                resolve(res)
            },
            error: function(res) {
                console.log(res)
            }
        });
    });
}

function download(href){
    var a = document.createElement('a');
    a.setAttribute('href', href);
    a.setAttribute('id', 'startTelMedicine');

    // 防止反复添加
    if (document.getElementById('startTelMedicine')) {
        document.body.removeChild(document.getElementById('startTelMedicine'));
    }
    document.body.appendChild(a);
    a.click();
}