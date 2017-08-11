document.addEventListener('DOMContentLoaded', function () {
    $.validator.setDefaults({
        highlight: function(element) {
            $(element)
                .closest('.form-group')
                .addClass('has-warning');
        },
        unhighlight: function(element) {
            $(element)
                .closest('.form-group')
                .removeClass('has-warning');
        }
    });

    $('#forgotPasswordForm').validate({
        rules: {
            username: {
                required: true
            },
            email: {
                required: true
            }
        }
    });
});