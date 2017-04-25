$(document).ready(function() {

    /**
     * 初始化表格
     * @type {Object}
     */
    $('#patientsData').DataTable({
        //将表格中的语言设置为中文
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
        //为每一列设置数据和列名
        columns: [{
            //第一列为加号，点击加号显示CDG诊断信息
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
        //点击按钮后鼠标变等待样式
        $(this).css("cursor", "wait");
        //获取筛选条件值
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
         * 异步获取病人信息，并填充表格。调用的API是PatientsInfoResources类中的queryPatientsInfo函数
         * @param  {[type]}
         * @return {[type]}
         */
        $.ajax({
            "url": "api/patientsinfo",
            "type": "GET",
            "data": params,
            "dataType": "json",
            "success": function(data) {
                //获取数据成功后恢复鼠标样式
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
     * 查看测试号与CDG诊断结果的事件，点击表格第一列的加号触发函数。
     */
    $("#patientsData tbody").on("click", "td.details-control", function() {

        var patientsDataTable = $("#patientsData").DataTable();
        var tr = $(this).closest("tr");
        var patientsDataTableRow = patientsDataTable.row(tr);

        var cdgInfo = {};
        $.ajax({
            "url": "api/diagnosis",
            "type": "GET",
            //关闭异步，确保获得数据后才能执行子表查看。
            "async": false,
            "data": { "admissionnumber": patientsDataTableRow.data().admissionnumber },
            "dataType": "json",

            "success": function(data) {
                cdgInfo = data;
            }
        });
        
        //子表显示在DataTables官方文档中有介绍.
        if (patientsDataTableRow.child.isShown()) {
            patientsDataTableRow.child.hide();
            tr.removeClass("shown");
        } else {
            //generateCdgInfoTable函数产生子表的HTML代码，在文件最后有定义
            patientsDataTableRow.child(generateCdgInfoTable(cdgInfo)).show();
            tr.addClass("shown");

            var cdgInfoTable = $(".cdgInfo").DataTable({
                "searching": false,
                "paging": false,
                "info": false,
                columns: [
                    { "className": "testId" },
                    { "className": "cdgResults" },
                    { "className": "ecgResults" },
                    { "className": "cdgDownload"},
                    { "className": "ecgDownload"}
                ]

            });

        }

        //获取CDG数据
        $(".cdgInfo tbody").on("click", "td.cdgResults", function() {
            $(this).css("cursor", "wait");
            var cdgInfoTable = $(".cdgInfo").DataTable();

            //获取鼠标点击所在行的数据
            var tr = $(this).closest("tr");
            var cdgInfoTableRow = cdgInfoTable.row(tr);
            
            //获取cdg数据，调用的是CDGResources类中的findByTestId函数
            $.ajax({
                "url": "api/cdg",
                "type": "GET",
                "data": { "testId": cdgInfoTableRow.data()[0] }, 
                "dataType": "json",

                "success": function(data) {
                    $(".cdgInfo").css("cursor", "default");
                    var cdgData = JSON.parse(data.cdgData);

                    //plotly.js画图
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

        //下载CDG数据，使用FileSaver.js插件，CDG文件的格式是txt格式，每一维数据一行
        $(".cdgInfo tbody").on("click", "td.cdgDownload",function() {
            var cdgInfoTable = $(".cdgInfo").DataTable();
            var tr = $(this).closest("tr");
            var cdgInfoTableRow = cdgInfoTable.row(tr);
            //获取cdg数据，调用的是CDGResources类中的findByTestId函数
            $.ajax({
                "url": "api/cdg",
                "type": "GET",
                "data": {"testId": cdgInfoTableRow.data()[0] },
                "dataType": "json",
                "success": function(data) {         
                    var cdgData = JSON.parse(data.cdgData);
                    //FileSaver.js插件使用方法，网上有介绍
                    var blob = new Blob([cdgData[0], '\r\n', cdgData[1], '\r\n', cdgData[2], '\r\n'], {type: "text/plain;charset=utf-8"});
                    var filename = 'CDG_' + patientsDataTableRow.data().admissionnumber + '_' + cdgInfoTableRow.data()[0];
                    saveAs(blob, filename);
                }
            });      
        });

        //下载ECG数据，使用FileSaver.js插件
        $(".cdgInfo tbody").on("click", "td.ecgDownload",function() {
            var cdgInfoTable = $(".cdgInfo").DataTable();
            var tr = $(this).closest("tr");
            var cdgInfoTableRow = cdgInfoTable.row(tr);
            //获取ecg数据，调用的是ECGResources类中的findByTestId函数
            $.ajax({
                "url": "api/ecg",
                "type": "GET",
                "data": {"testId": cdgInfoTableRow.data()[0] },
                "dataType": "json",
                "success": function(data) {              
                    var blob = new Blob([data.ecgData], {type: "text/plain;charset=utf-8"});
                    var filename = 'ECG_' + patientsDataTableRow.data().admissionnumber + '_' + cdgInfoTableRow.data()[0];
                    saveAs(blob, filename);
                }
            });      
        });

/*
        //获取ECG数据
        $(".cdgInfo tbody").on("click", "td.ecgResults", function() {
            $(this).css("cursor", "wait");
            var cdgInfoTable = $(".cdgInfo").DataTable();
            var tr = $(this).closest("tr");
            var cdgInfoTableRow = cdgInfoTable.row(tr);

            $.ajax({
                "url": "api/ecg",
                "type": "GET",
                "data": { "testId": cdgInfoTableRow.data()[0] }, //tr.children()[0].textContent
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
*/

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
        //获取病历信息，调用的API是CasesResources类中的findByAdnum函数
        $.ajax({
            "url": "api/cases/adnum",
            "type": "GET",
            "data": { "admissionnumber": row.data().admissionnumber },
            "dataType": "json",

            "success": function(data) {
                //填充病历显示窗口，并显示
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
        document.getElementById("ctNothing").checked = false;
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
            '<td>' + '<a class="btn btn-success">下载</a>' + '</td>' +
            '<td>' + '<a class="btn btn-success">下载</a>' + '</td>'
            '</tr>';
    }

    return '<div class="row">' +
        '<div class="col-md-8">' +
        '<table class="table display cdgInfo" cellspaceing="0" width="100%" role="grid">' +
        '<thead>' +
        '<th> 测试号 </th>' +
        '<th> CDG结果 </th>' +
        '<th> ECG结果 </th>' +
        '<th> CDG下载 </th>' +
        '<th> ECG下载 </th>' +
        '</thead>' +
        '<tbody>' +
        cdgInfoData +
        '</tbody>' +
        '</table>' +
        '</div>' +
        '<div class="col-md-4"></div>' +
        '</div>'
}
