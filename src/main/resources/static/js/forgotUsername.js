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

    $('#forgotUsernameForm').validate({
        rules: {
            email: {
                required: true,
                email: true,
                remote : {
                    url: '/checkUserData/checkEmail',
                    type: "GET",
                    data: {
                        email: function() {
                            return $('#email').val();
                        }
                    }
                }
            }
        },
        messages: {
            email: {
                remote: $.validator.format("Email {0} doesn't exist in the database")
            }
        },
        submitHandler: function () { forgotUsername(); }
    });

    function forgotUsername() {
        $('.alert').remove();
        var forgotUsernameDTO = {"email":$('#email').val()};
        $.ajax({
            type: 'PUT',
            url: '/forgotUsername',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify(forgotUsernameDTO),
            success: function (result) {
                $('#forgotUsernameForm')
                    .before('<div class="alert alert-dismissable alert-success">'
                            + '<span th:text="#{forgotUsername.success}">An e-mail has been successfully sent. Please allow a few minutes for it to get to your inbox!</span>'
                            + '</div>');
            },
            error: function (error) {
                $('#forgotUsernameForm')
                    .before('<div class="alert alert-dismissable alert-danger">'
                            + '<span th:text="#{forgotUsername.error}">No username found associated with that e-mail!</span>'
                            + '</div>');
            }
        });
    }
});