const ajaxUrl = "ajax/meals/";
let datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
    makeEditable();
});


function filter() {
    let form = $("#filterForm");
    $.ajax({
        type: "GET",
        url: ajaxUrl + "filter",
        data: form.serialize()
    }).done(function () {
        datatableApi.clear().rows.add(data).draw();
    });
}

