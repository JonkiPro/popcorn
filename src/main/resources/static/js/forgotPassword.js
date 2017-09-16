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
        },
        submitHandler: function () { forgotPassword(); }
    });

    function forgotPassword() {
        $('.alert').remove();
        var forgotPasswordDTO = {"username":$('#username').val(),
                                 "email":$('#email').val()};
        $.ajax({
            type: 'PUT',
            url: '/api/v1.0/users/attributes/password_reset',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify(forgotPasswordDTO),
            success: function (result) {
                $('#forgotPasswordForm')
                    .before('<div class="alert alert-dismissable alert-success">'
                            + '<span th:text="#{forgotPassword.success}">An e-mail has been successfully sent. Please allow a few minutes for it to get to your inbox!</span>'
                            + '</div>');
            },
            error: function (error) {
                $('#forgotPasswordForm')
                    .before('<div class="alert alert-dismissable alert-danger">'
                            + '<span th:text="#{forgotPassword.error}">Incorrect username and/or email address!</span>'
                            + '</div>');
            }
        });
    }
});