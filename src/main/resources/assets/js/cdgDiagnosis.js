$(document).ready(function() {

    $('#filesupload').fileupload({
            url: '/api/ecg/upload',
            dataType: 'json',
            done: function(e, data) {
                if (data.result.code == 0) {

                    $("#progress_str").text("上传成功！");

                    $("#diagnosis").on("click", function() {

                        $("#cdgDiagnosisText").text("CDG诊断中...");
                        $("#loadingGif").css("display", "inline");
                        $("#diagnosis_str").text("");

                        $.ajax({
                            "url": "api/ecg/hadoop",
                            "type": "GET",
                            "data": { "threadId": data.result.threadId },
                            "dataType": "json",
                            "success": function(data) {

                                $("#cdgDiagnosisText").text("CDG诊断");
                                $("#loadingGif").css("display", "none");

                                if (data.code == 0) {
                                    $("#diagnosis_str").text("诊断完成！");
                                } else {
                                    $("#diagnosis_str").text("诊断失败：" + data.result.message);
                                }
                            },
                            "error": function(jqXHR, exception) {

                                $("#cdgDiagnosisText").text("CDG诊断");
                                $("#loadingGif").css("display", "none");
                                $("#diagnosis_str").text("诊断失败!");

                                if (jqXHR.status === 0) {
                                    alert('Not connect.\n Verify Network.');
                                } else if (jqXHR.status == 404) {
                                    alert('Requested page not found. [404]');
                                } else if (jqXHR.status == 500) {
                                    alert('Internal Server Error [500].');
                                } else if (exception === 'parsererror') {
                                    alert('Requested JSON parse failed.');
                                } else if (exception === 'timeout') {
                                    alert('Time out error.');
                                } else if (exception === 'abort') {
                                    alert('Ajax request aborted.');
                                } else {
                                    alert('Uncaught Error.\n' + jqXHR.responseText);
                                }
                            }

                        });

                    });
                } else {
                    $("#progress_str").text("上传失败：" + data.result.message);
                }
            },
            fail: function(e, data) {
                $("#progress_str").text("上传失败!");
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
