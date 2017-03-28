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

        var admissionnumber = $("#admissionnumberSearch").val();
        if (admissionnumber.length != 6) {
            alert("请输入正确的住院号:六位数字");
            return;
        }
        $.ajax({
            "url": "api/patients/adnum",
            "type": "GET",
            "data": { "admissionnumber": admissionnumber },
            "dataType": "json",
            "success": function(patients) {
                if (patients === undefined) {
                    alert("无此病人信息");
                    return;
                }
                setPatients(patients);
                $.ajax({
                    "url": "api/cases/patientId",
                    "type": "GET",
                    "data": { "patientId": patients.id },
                    "dataType": "json",
                    "success": function(cases) {
                        $("#search").css("cursor", "pointer");
                        setCases(cases);
                        $("#modification").modal("show");
                        modifyCases(patients.id, cases.id);
                    },
                    "error": function(jqXHR, exception) {
                        $("#search").css("cursor", "pointer");
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
                $("#search").css("cursor", "pointer");
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
    })
}
