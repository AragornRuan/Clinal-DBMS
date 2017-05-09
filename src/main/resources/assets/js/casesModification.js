$(document).ready(function() {
    //初始化时间选择器
    $(".time").datetimepicker({
        format: "yyyy-mm-dd",
        minView: 2,
        autoclose: true,
        todayBtn: true
    })

    /**
     * 根据住院号查询病人
     */
    $("#search").on("click", function() {
        $(this).css("cursor", "wait");

        //住院号必须为6位
        var admissionnumber = $("#admissionnumberSearch").val();
        if (admissionnumber.length != 6) {
            alert("请输入正确的住院号:六位数字");
            return;
        }
        //查询病人是否存在，调用的API述PatientsResources类中的findByAdmissionnumber函数
        $.ajax({
            "url": "api/patients/adnum",
            "type": "GET",
            "data": { "admissionnumber": admissionnumber },
            "dataType": "json",
            "success": function(patients) {
                //如果结果集为空，则警告并返回
                if (patients === undefined) {
                    alert("无此病人信息");
                    return;
                }

                //否则在修改窗口中填充病人基本信息，setPatients函数在本文件后面有定义
                setPatients(patients);
                //根据病人id获取病历信息，调用的类是casesResources类中的findByPatientsId函数
                $.ajax({
                    "url": "api/cases/patientId",
                    "type": "GET",
                    "data": { "patientId": patients.id },
                    "dataType": "json",
                    "success": function(cases) {
                        $("#search").css("cursor", "pointer");
                        //在修改窗口中填充病人病历信息，setCases函数在后面有定义
                        setCases(cases);
                        $("#modification").modal("show");
                        //根据paiemts和cases表中的id列，更新修改后的信息，函数在后面有定义
                        modifyCases(patients.id, cases.id);
                    },
                    "error": function(jqXHR, exception) {
                        $("#search").css("cursor", "pointer");
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
                $("#search").css("cursor", "pointer");
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

});

//在修改病历页面中设置病人基本信息
function setPatients(patients) {
    $("#name").val(patients.name);
    $("#age").val(patients.age);
    $("#sex").val(patients.sex);
    $("#admissionnumber").val(patients.admissionnumber);
}

//在修改病历页面中设置病人病历信息
function setCases(cases) {
    $("#disease").val(cases.disease);
    $("#hosTime").val(cases.hosTime);
    $("#dischargedTime").val(cases.dischargedTime);
    $("#complaint").val(cases.complaint);

    if (cases.ecgTag == 1) {
        document.getElementById("ecgNormal").checked = true;
    } else {
        document.getElementById("ecgUnusual").checked = true;
    }
    $("#ecg").val(cases.ecg);

    if (cases.ctTag == 0) {
        document.getElementById("ctNothing").checked = true;
    } else if (cases.ctTag == 1) {
        document.getElementById("ctNormal").checked = true;
    } else {
        document.getElementById("ctStricture").checked = true;
    }
    $("#ct").val(cases.ct);

    if (cases.radiographyTag == 0) {
        document.getElementById("radiographyNothing").checked = true;
    } else if (cases.radiographyTag == 1) {
        document.getElementById("radiographyNormal").checked = true;
    } else {
        document.getElementById("radiographyStricture").checked = true;
    }
    $("#radiography").val(cases.radiography);

    $("#diagnosis").val(cases.diagnosis);
    $("#remarks").val(cases.remarks);
}

//根据paiemts和cases表中的id列，更新修改后的信息
function modifyCases(patientId, caseId) {
    //点击修改按钮回调函数
    $("#modify").on("click", function() {
        //改变鼠标样式为等待
        $(this).css("cursor", "wait");

        var admissionnumber = $("#admissionnumber").val();
        var ecgNormal = document.getElementById("ecgNormal").checked;
        var ecgUnusual = document.getElementById("ecgUnusual").checked;
        var ctNothing = document.getElementById("ctNothing").checked;
        var ctNormal = document.getElementById("ctNormal").checked;
        var ctStricture = document.getElementById("ctStricture").checked;
        var radiographyNothing = document.getElementById("radiographyNothing").checked;
        var radiographyNormal = document.getElementById("radiographyNormal").checked;
        var radiographyStricture = document.getElementById("radiographyStricture").checked;

        if (admissionnumber.length != 6) {
            alert("请输入6位住院号!");
            $("#modify").css("cursor", "pointer");
            return;
        }
        if (!ecgNormal && !ecgUnusual) {
            alert("请选择心电图类型！");
            $("#modify").css("cursor", "pointer");
            return;
        }
        if (!ctNothing && !ctNormal && !ctStricture) {
            alert("请选择CT结果类型！");
            $("#modify").css("cursor", "pointer");
            return;
        }
        if (!radiographyNothing && !radiographyNormal && !radiographyStricture) {
            alert("请选择造影结果类型！");
            $("#modify").css("cursor", "pointer");
            return;
        }


        var patientsData = {
            "name": $("#name").val(),
            "sex": $("#sex").val(),
            "age": $("#age").val(),
            "admissionnumber": admissionnumber
        };
        //将病人基本信息插入patients表
        $.ajax({
            //添加header，并设置以下属性，否则服务端不支持Media Type
            "headers": {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            "url": "api/patients/update" + "?id=" + patientId,
            "type": "POST",
            //要将JSON object转成string形式，否则服务端无法处理JSON
            "data": JSON.stringify(patientsData),
            "dataType": "json",

            "success": function(data) {
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
                //将病历信息插入cases表，调用的API是casesResources类中的insert函数
                $.ajax({
                    "headers": {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    "url": "api/cases/update" + "?id=" + caseId,
                    "type": "POST",
                    "data": JSON.stringify(casesData),
                    "dataType": "json",
                    "success": function(data) {
                        $("#modify").css("cursor", "pointer");
                        alert("修改病历成功！");
                    },
                    "error": function(jqXHR, exception) {
                        $("#modify").css("cursor", "pointer");
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
    })
}
