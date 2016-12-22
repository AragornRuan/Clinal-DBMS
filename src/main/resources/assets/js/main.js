$(document).ready(function() {

    var pData = [];

    $.ajax({
        "url": "api/patientsinfo",
        "type": "GET",
        "data": null,
        "dataType": "json",
        "success": function(data) {
            pData = data;
            var patientsDataTable = $('#patientsData').DataTable({
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

                data: data,

                columns: [
                    {
                        "className": 'details-control',
                        "orderable": false,
                        "data": null,
                        "defaultContent": ''
                    },
                    { data: 'name' },
                    { data: 'sex' },
                    { data: 'age' },
                    { data: 'admissionnumber' },
                    { data: 'hosTime' },
                    { data: 'source' }
                ]
            });
            
           $("#patientsData tbody").on("click", "td.details-control", function () {

                var tr = $(this).closest("tr");
                var row = patientsDataTable.row(tr);

                var cdgInfo = [];
//                var getData = {"admissionnumber" : row.data()}
                $.ajax({
                    "url": "api/cdgInfo/adnum",
                    "type": "GET",
                    "async": false,
                    "data": {"admissionnumber": row.data().admissionnumber},
                    "dataType": "json",
                    "success": function (data) {
                        cdgInfo = data;
                    }
                });

                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass("shown");
                }
                else {
                    row.child(generateCdgInfoTable(cdgInfo)).show();
                    tr.addClass("shown");
                }
            })
        }
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
