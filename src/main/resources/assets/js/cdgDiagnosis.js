$(document).ready(function() {

    $('#filesupload').fileupload({
            url: '/api/ecg/upload',
            dataType: 'json',
            done: function(e, data) {
                if (data.result.code == 0) {
                    $("#progress_str").text("上传成功！");
                    $("#diagnosis").on("click", function() {
                        if ($("#progress_str").text() == "上传成功！") {
                            $.ajax({
                                "url": "api/ecg/hadoop",
                                "type": "GET",
                                "data": {"threadId":data.result.threadId},
                                "dataType": "json",
                                "success": function(data) {
                                    if (data.code == 0) {
                                        alert("CDG诊断完成！")
                                    }
                                    else {
                                        alert(data.message);
                                    }
                                }

                            });
                        } else {
                            alert("请先上传ECG数据！");
                        }
                    });
                } else {
                    $("#progress_str").text("上传失败：" + data.result.message);
                }
            },
            progressall: function(e, data) {
                var progress = parseInt(data.loaded / data.total * 100, 10);
                $('#progress .progress-bar').css(
                    'width',
                    progress + '%'
                );
                $('#progress_str').text(progress + '%');
            }
        }).prop('disabled', !$.support.fileInput)
        .parent().addClass($.support.fileInput ? undefined : 'disabled');

});
