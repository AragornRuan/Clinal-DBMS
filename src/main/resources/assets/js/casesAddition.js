$(document).ready(function() {
    //初始化时间选择器
    $(".time").datetimepicker({
        format: "yyyy-mm-dd",
        minView: 2,
        autoclose: true,
        todayBtn: true
    })

    $("#add").on("click", function() {
        $(this).css("cursor", "wait");

        var admissionnumber = $("#admissionnumber").val();
        var ecgNormal = document.getElementById("ecgNormal").checked;
        var ecgUnusual = document.getElementById("ecgUnusual").checked;
        var ctNothing = document.getElementById("ctNothing").checked;
        var ctNormal = document.getElementById("ctNormal").checked;
        var ctStricture = document.getElementById("ctStricture").checked;
        var radiographyNothing = document.getElementById("radiographyNothing").checked;
        var radiographyNormal = document.getElementById("radiographyNormal").checked;
        var ctStricture = document.getElementById("radiographyStricture").checked;

        if (admissionnumber.length != 6) {
            alert("请输入6位住院号!");
            $("#add").css("cursor", "pointer");
            return;
        }
        if (!ecgNormal && !ecgUnusual) {
            alert("请选择心电图类型！");
            $("#add").css("cursor", "pointer");
            return;
        }
        if (!ctNothing && !ctNormal && !ctStricture) {
            alert("请选择CT结果类型！");
            $("#add").css("cursor", "pointer");
            return;
        }
        if (!radiographyNothing && !radiographyNormal && !radiographyStricture) {
            alert("请选择造影结果类型！");
            $("#add").css("cursor", "pointer");
            return;
        }


        var patientsData = {
            "name": $("#name").val(),
            "sex": $("#sex").val(),
            "age": $("#age").val(),
            "admissionnumber": admissionnumber
        };

        $.ajax({
            //添加header，并设置以下属性，否则服务端不支持Media Type
            "headers": {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            "url": "api/patients/insert",
            "type": "POST",
            //要将JSON object转成string形式，否则服务端无法处理JSON
            "data": JSON.stringify(patientsData),
            "dataType": "json",

            "success": function(data) {
                if (data.code == 7) {
                    alert(data.message);
                    $("#add").css("cursor", "pointer");
                    return;
                };
                var casesData = {
                    "patientId": data.id,
                    "diagnosis": $("#diagnosis").val(),
                    "ecg": $("#ecg").val(),
                    "ecgTag": (ecgNormal ? 1 : 2),
                    "ct": $("#ct").val(),
                    "ctTag": (ctNothing ? 0 : (ctNormal ? 1 : 2)),
                    "complaint": $("#complaint").val(),
                    "radiography": $("#radiography").val(),
                    "radiographyTag": (radiographyNothing ? 0 : (radiographyNormal ? 1 : 2)),
                    "radiographyTime": "",
                    "ctTime": "",
                    "hosTime": $("#hosTime").val(),
                    "dischargedTime": $("#dischargedTime").val(),
                    "remarks": $("#remarks").val(),
                    "disease": $("#disease").val()
                };
                $.ajax({
                    "headers": {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    "url": "api/cases/insert",
                    "type": "POST",
                    "data": JSON.stringify(casesData),
                    "dataType": "json",
                    "success": function(data) {
                        $("#add").css("cursor", "pointer");
                        alert("添加病历成功！");
                    },
                    "error": function(jqXHR, exception) {
                        $("#add").css("cursor", "pointer");
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

            },
            "error": function(jqXHR, exception) {
                $("#add").css("cursor", "pointer");
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

    $("#reset").on("click", function() {
        location.reload();
    });
});
