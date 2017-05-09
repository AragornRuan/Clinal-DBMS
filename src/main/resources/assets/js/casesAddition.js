$(document).ready(function() {

    //初始化amazeui datetimepicker时间选择器
    $(".time").datetimepicker({
        format: "yyyy-mm-dd",
        minView: 2,
        autoclose: true,
        todayBtn: true
    })

    //点击添加按钮的回调函数
    $("#add").on("click", function() {
        $(this).css("cursor", "wait");

        //获取病历信息
        var admissionnumber = $("#admissionnumber").val();
        var ecgNormal = document.getElementById("ecgNormal").checked;
        var ecgUnusual = document.getElementById("ecgUnusual").checked;
        var ctNothing = document.getElementById("ctNothing").checked;
        var ctNormal = document.getElementById("ctNormal").checked;
        var ctStricture = document.getElementById("ctStricture").checked;
        var radiographyNothing = document.getElementById("radiographyNothing").checked;
        var radiographyNormal = document.getElementById("radiographyNormal").checked;
        var radiographyStricture = document.getElementById("radiographyStricture").checked;

        //填写内容错误时弹出警告
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
        //将病人的基本信息插入patients表，调用API为PatientsResources类中的insert函数
        $.ajax({
            //添加header，并设置以下属性，否则服务端出现不支持Media Type错误
            "headers": {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            "url": "api/patients/insert",
            "type": "POST",
            //要将JSON object转成string形式，否则服务端无法处理JSON
            "data": JSON.stringify(patientsData),
            "dataType": "json",

            //插入patients表成功后再将其余信息插入cases表
            "success": function(data) {
                //如果插入失败则显示失败信息，错误码7代表病人已经存在，错误码的定义详见后端程序的ErrorCode类中。
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
                //向cases表中插入数据，调用的API是CasesResources类中的insert函数
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
                    //处理请求错误情况
                    "error": function(jqXHR, exception) {
                        $("#add").css("cursor", "pointer");
                        if (jqXHR.status === 0) {
                            alert('Not connect.\n Verify Network.');
                        } else if (jqXHR.status == 401) {
                            $("#ModalLogin").modal("show");
                        } else if (jqXHR.status == 500) {
                            alert(jqXHR.responseJSON.message);
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
                } else if (jqXHR.status == 401) {
                    $("#ModalLogin").modal("show");
                } else if (jqXHR.status == 500) {
                    alert(jqXHR.responseJSON.message);
                } else {
                    alert('Uncaught Error.\n' + jqXHR.responseText);
                }
            }
        });
    });

    //重置则刷新页面
    $("#reset").on("click", function() {
        location.reload();
    });
});
