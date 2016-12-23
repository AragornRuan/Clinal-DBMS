$(document).ready(function() {

    $('#patientsData').DataTable({
        language: {
            "sProcessing": "处理中...",
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

    $("#search").on("click", function() {
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

        $.ajax({
            "url": "api/patientsinfo",
            "type": "GET",
            "data": params,
            "dataType": "json",
            "success": function(data) {

                var patientsDataTable = $("#patientsData").DataTable();
                patientsDataTable.clear();
                for (var i = 0, len = data.length; i < len; i++) {
                    patientsDataTable.row.add(data[i]);
                }
                patientsDataTable.draw();
            }
        });
    });

    $("#patientsData tbody").on("click", "td.details-control", function() {

        var patientsDataTable = $("#patientsData").DataTable();
        var tr = $(this).closest("tr");
        var row = patientsDataTable.row(tr);

        var cdgInfo = {};
        $.ajax({
            "url": "api/cdgInfo/adnum",
            "type": "GET",
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
        }

    });
    
    $("#patientsData tbody").on("click", "td.content", function() {
        var patientsDataTable = $("#patientsData").DataTable();
        var tr = $(this).closest("tr");
        var row = patientsDataTable.row(tr);

        $.ajax({
            "url": "api/cases/adnum",
            "type": "GET",
            "data": { "admissionnumber": row.data().admissionnumber },
            "dataType": "json",
            "success": function(data) {
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


});




function generateCdgInfoTable(cdgInfo) {
    var cdgInfoData = '';

    for (var i = 0; i < cdgInfo.length; i++) {
        cdgInfoData += '<tr>' +
            '<td>' + cdgInfo[i].testId + '</td>' +
            '<td>' + cdgInfo[i].cdgResults + '</td>' +
            '</tr>';
    }

    return '<table class="table display" cellspaceing="0" width="100%" role="grid">' +
        '<thead>' +
        '<th> 测试号 </th>' +
        '<th> CDG结果 </th>' +
        '</thead>' +
        '<tbody>' +
        cdgInfoData +
        '</tbody>' +
        '</table>'
}

/**
 * @param  {[type]}
 * @return {[type]}
 */
/*function generateCase (data) {
    var table = $("#patientsData").DataTable();
    table.clear();
    for (var i = 0, len = data.length; i < len; i++) {
        table.row.add(data[i]);
    }
    table.draw();
}*/
