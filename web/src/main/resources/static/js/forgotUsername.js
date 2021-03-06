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
                    url: '/api/v1.0/users/check/email',
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
            url: '/api/v1.0/users/attributes/username_recovery',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify(forgotUsernameDTO),
            complete: function (event) {
                if(event.status === 204) {
                    $('#forgotUsernameForm')
                        .before('<div class="alert alert-dismissable alert-success">'
                              + '<span th:text="#{forgotUsername.success}">An e-mail has been successfully sent. Please allow a few minutes for it to get to your inbox!</span>'
                              + '</div>');
                } else {
                    $('#forgotUsernameForm')
                        .before('<div class="alert alert-dismissable alert-danger">'
                              + '<span th:text="#{forgotUsername.error}">No username found associated with that e-mail!</span>'
                              + '</div>');
                }
            }
        });
    }
});