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
        ],
    });
    makeEditable();
});

function filter() {
    $.ajax({
        type: "GET",
        url: ajaxUrl + "filter",
        data: $("#filterForm").serialize()
    }).done(function (data) {
        datatableApi.clear().rows.add(data).draw();
    });
}

function resetFilter() {
    $("#filterForm")[0].reset();
    updateTable();
}

function save() {
    let form = $("#detailsForm");
    $.ajax({
        type: "POST",
        url: ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        filter();
        successNoty("Saved");
    });
}
