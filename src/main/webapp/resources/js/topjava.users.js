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


function setEnabled(id) {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "checked/" + id,
    }).done(function () {
        let tr = $("#" + id)
        if (tr.find(":checkbox").is(":checked") === true) {
            tr.css("color", "green");
            successNoty("user enabled");
        } else {
            tr.css("color", "red");
            successNoty("user disabled");
        }
    });
}