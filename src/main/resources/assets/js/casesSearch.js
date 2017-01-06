$(document).ready(function() {

    /**
     * 初始化表格
     * @type {Object}
     */
    $('#patientsData').DataTable({
        language: {
            "sProcessing": "<img src='../images/ajax-loader.gif'>",
            "sLengthMenu": "显示 _MENU_ 项结果",
            "sZeroRecords": "没有匹配结果",
            "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
            "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
            "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
            "sInfoPostFix": "",
            "sSearch": "搜索:",
            "sUrl": "",
            "sEmptyTable": "表中数据为空",
            "sLoadingRecords": "载入中...",
            "sInfoThousands": ",",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "上页",
                "sNext": "下页",
                "sLast": "末页"
            },
            "oAria": {
                "sSortAscending": ": 以升序排列此列",
                "sSortDescending": ": 以降序排列此列"
            }
        },

        columns: [{
            "className": 'details-control',
            "orderable": false,
            "data": null,
            "defaultContent": ''
        }, {
            "className": "content",
            "data": 'name'
        }, {
            "className": "content",
            "data": 'sex'
        }, {
            "className": "content",
            "data": 'age'
        }, {
            "className": "content",
            "data": 'admissionnumber'
        }, {
            "className": "content",
            "data": 'hosTime'
        }, {
            "className": "content",
            "data": 'source'
        }]

    });

    /**
     * 搜索事件，根据复选框筛选病人信息。
     * @param  {Object}
     * @return {[type]}
     */
    $("#search").on("click", function() {
        $(this).css("cursor", "wait");
        var params = {
            "name": $("#name").val(),
            "male": (document.getElementById("male").checked ? 1 : 0),
            "female": (document.getElementById("female").checked ? 1 : 0),
            "admissionnumber": $("#admissionnumber").val(),
            "ecgNormal": (document.getElementById("ecgNormal").checked ? 1 : 0),
            "ecgUnusual": (document.getElementById("ecgUnusual").checked ? 1 : 0),
            "ctNormal": (document.getElementById("ctNormal").checked ? 1 : 0),
            "ctStricture": (document.getElementById("ctStricture").checked ? 1 : 0),
            "ctNothing": (document.getElementById("ctNothing").checked ? 1 : 0),
            "radiographyNormal": (document.getElementById("radiographyNormal").checked ? 1 : 0),
            "radiographyStricture": (document.getElementById("radiographyStricture").checked ? 1 : 0),
            "radiographyNothing": (document.getElementById("radiographyNothing").checked ? 1 : 0),
            "negative": (document.getElementById("negative").checked ? 1 : 0),
            "positive": (document.getElementById("positive").checked ? 1 : 0),
            "probablePositive": (document.getElementById("probablePositive").checked ? 1 : 0),
            "times": (document.getElementById("times").checked ? 1 : 0),
            "mindray": (document.getElementById("mindray").checked ? 1 : 0),
            "aika": (document.getElementById("aika").checked ? 1 : 0),
            "cardis": (document.getElementById("cardis").checked ? 1 : 0),
            "im": (document.getElementById("im").checked ? 1 : 0),
            "ar": (document.getElementById("ar").checked ? 1 : 0)
        };

        /**
         * 异步获取病人信息，并填充表格
         * @param  {[type]}
         * @return {[type]}
         */
        $.ajax({
            "url": "api/patientsinfo",
            "type": "GET",
            "data": params,
            "dataType": "json",
            "success": function(data) {
                $("#search").css("cursor", "pointer");
                var patientsDataTable = $("#patientsData").DataTable();
                //先清空表格再填充。
                patientsDataTable.clear();
                for (var i = 0, len = data.length; i < len; i++) {
                    patientsDataTable.row.add(data[i]);
                }
                patientsDataTable.draw();
            }
        });
    });

    /**
     * 查看测试号与CDG诊断结果的事件。
     */
    $("#patientsData tbody").on("click", "td.details-control", function() {

        var patientsDataTable = $("#patientsData").DataTable();
        var tr = $(this).closest("tr");
        var row = patientsDataTable.row(tr);

        var cdgInfo = {};
        $.ajax({
            "url": "api/diagnosis",
            "type": "GET",
            //关闭异步，确保获得数据后才能执行子表查看。
            "async": false,
            "data": { "admissionnumber": row.data().admissionnumber },
            "dataType": "json",

            "success": function(data) {
                cdgInfo = data;
            }
        });

        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass("shown");
        } else {
            row.child(generateCdgInfoTable(cdgInfo)).show();
            tr.addClass("shown");

            var cdgInfoTable = $(".cdgInfo").DataTable({
                "searching": false,
                "paging": false,
                "info": false,
                columns: [
                    { "className": "testId" },
                    { "className": "cdgResults" },
                    { "className": "ecgResults" }
                ]

            });

        }

        //获取CDG数据
        $(".cdgInfo tbody").on("click", "td.cdgResults", function() {
            $(this).css("cursor", "wait");
            var cdgInfoTable = $(".cdgInfo").DataTable();
            var tr = $(this).closest("tr");
            var row = cdgInfoTable.row(tr);

            $.ajax({
                "url": "api/cdg",
                "type": "GET",
                "data": { "testId": row.data()[0] }, //tr.children()[0].textContent
                "dataType": "json",

                "success": function(data) {
                    $(".cdgInfo").css("cursor", "default");
                    var cdgData = JSON.parse(data.cdgData);
                    Plotly.purge(document.getElementById("graphCDG"));
                    Plotly.plot("graphCDG", [{
                        type: "scatter3d",
                        mode: "lines",
                        x: cdgData[0],
                        y: cdgData[1],
                        z: cdgData[2],
                        opacity: 1,
                        line: {
                            width: 3,
                            color: "red"
                        }
                    }], {
                        height: 640
                    });

                    $("#CDGData").modal("show");
                }

            });
        });

        //获取ECG数据
        $(".cdgInfo tbody").on("click", "td.ecgResults", function() {
            $(this).css("cursor", "wait");
            var cdgInfoTable = $(".cdgInfo").DataTable();
            var tr = $(this).closest("tr");
            var row = cdgInfoTable.row(tr);

            $.ajax({
                "url": "api/ecg",
                "type": "GET",
                "data": { "testId": row.data()[0] }, //tr.children()[0].textContent
                "dataType": "json",

                "success": function(data) {
                    $(".cdgInfo").css("cursor", "default");
                    var ecgData = JSON.parse(data.ecgData);

                    Plotly.purge(document.getElementById("graphECG"));
                    Plotly.plot("graphECG", [{
                        type: "scatter",
                        mode: "lines",
                        x: xAxis,
                        y: ecgData[6],
                        opacity: 1,
                        line: {
                            width: 1,
                            color: "red"
                        }
                    }], {
                        height: 640,
                    });
                    //       $("#ECGData").modal("show");
                }

            });
        });

        /**
         *  鼠标移动到CDGResults和ECGResults的单元格时改变样式
         */
        $(".cdgInfo tbody").on("mouseenter", "td.cdgResults", function() {
            $(this).css("background-color", "lightgray");
            $(this).css("cursor", "pointer");
        });

        $(".cdgInfo tbody").on("mouseleave", "td.cdgResults", function() {
            $(this).css("background-color", "white");
            $(this).css("cursor", "default");
        });

        $(".cdgInfo tbody").on("mouseenter", "td.ecgResults", function() {
            $(this).css("background-color", "lightgray");
            $(this).css("cursor", "pointer");
        });

        $(".cdgInfo tbody").on("mouseleave", "td.ecgResults", function() {
            $(this).css("background-color", "white");
            $(this).css("cursor", "default");
        });


    });

    /*
        查看病历的事件。
     */
    $("#patientsData tbody").on("click", "td.content", function() {
        $(this).css("cursor", "wait");
        var patientsDataTable = $("#patientsData").DataTable();
        var tr = $(this).closest("tr");
        var row = patientsDataTable.row(tr);

        $.ajax({
            "url": "api/cases/adnum",
            "type": "GET",
            "data": { "admissionnumber": row.data().admissionnumber },
            "dataType": "json",

            "success": function(data) {
                $(".content").css("cursor", "default");
                $("#caseTitle").text(row.data().name + "的病历");
                $("#complaintCase").text(data.complaint);
                $("#ecgCase").text((data.ecgTag === 1 ? "大致正常" : "未见异常"));
                $("#ctCase").html(data.ct.replace(/；+/g, "<br>"));
                $("#radiographyCase").html(data.radiography.replace(/；+/g, "<br>"));
                $("#diagnosisCase").html(data.diagnosis.replace(/；+/g, "<br>"));
                $("#patientCase").modal("show");
            }
        });
    });

    //重置筛选条件
    $("#reset").on("click", function() {
        $("#name").val("");
        document.getElementById("male").checked = false;
        document.getElementById("female").checked = false;
        $("#admissionnumber").val("");
        document.getElementById("ecgNormal").checked = false;
        document.getElementById("ecgUnusual").checked = false;
        document.getElementById("ctNormal").checked = false;
        document.getElementById("ctStricture").checked = false;
        document.getElementById("ctNothing").checked  = false;
        document.getElementById("radiographyNormal").checked = false;
        document.getElementById("radiographyStricture").checked = false;
        document.getElementById("radiographyNothing").checked = false;
        document.getElementById("negative").checked = false;
        document.getElementById("positive").checked = false;
        document.getElementById("probablePositive").checked = false;
        document.getElementById("times").checked = false;
        document.getElementById("mindray").checked = false;
        document.getElementById("aika").checked = false;
        document.getElementById("cardis").checked = false;
        document.getElementById("im").checked = false;
        document.getElementById("ar").checked = false;
    });

});



/**
 * 产生CDG信息表格
 * @param  {CDGInfo 对象，包含CDG诊断结果，测试号等信息。}
 * @return {返回表格的html}
 */
function generateCdgInfoTable(cdgInfo) {
    var cdgInfoData = '';

    for (var i = 0; i < cdgInfo.length; i++) {
        cdgInfoData += '<tr>' +
            '<td>' + cdgInfo[i].testId + '</td>' +
            '<td>' + cdgInfo[i].cdgResults + '</td>' +
            '<td>' + (cdgInfo[i].ecgTag == 1 ? '大致正常' : '可见异常') + '</td>' +
            '</tr>';
    }

    return '<div class="row">' +
        '<div class="col-md-8">' +
        '<table class="table display cdgInfo" cellspaceing="0" width="100%" role="grid">' +
        '<thead>' +
        '<th> 测试号 </th>' +
        '<th> CDG结果 </th>' +
        '<th> ECG结果 </th>' +
        '</thead>' +
        '<tbody>' +
        cdgInfoData +
        '</tbody>' +
        '</table>' +
        '</div>' +
        '<div class="col-md-4"></div>' +
        '</div>'
}