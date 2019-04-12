const ajaxUrl = "ajax/admin/users/";
let datatableApi;

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
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


function setEnabled(id, enabled) {
    let tr = $("#" + id);
    $.ajax({
        type: "POST",
        url: ajaxUrl + "checked/" + id + "/" + enabled,
    }).done(function () {
        if (tr.find(":checkbox").is(":checked") === true) {
            tr.css("color", "green");
            successNoty("user enabled");
        } else {
            tr.css("color", "red");
            successNoty("user disabled");
        }
    });

    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        tr.find(":checkbox").prop("checked", !tr.find(":checkbox").is(":checked"));
        (tr.find(":checkbox").is(":checked") === true) ? tr.css("color", "green") : tr.css("color", "red");
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
    $.ajaxSetup({cache: false});
}

function save() {
    let form = $("#detailsForm");
    $.ajax({
        type: "POST",
        url: ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        updateTable();
        successNoty("Saved");
    });
}