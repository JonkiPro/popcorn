document.addEventListener('DOMContentLoaded', function () {
    $.validator.setDefaults({
        highlight: function (element) {
            $(element)
                .closest('.form-group')
                .addClass('has-warning');
        },
        unhighlight: function (element) {
            $(element)
                .closest('.form-group')
                .removeClass('has-warning');
        }
    });

    $('#changeEmailForm').validate({
        rules: {
            password: {
                required: true,
                remote: {
                    url: '/checkUserData/checkPassword',
                    type: "GET",
                    data: {
                        password: function () {
                            return $('#password').val();
                        }
                    }
                }
            },
            email: {
                required: true,
                remote: {
                    url: '/checkUserData/checkEmailAtRegistering',
                    type: "GET",
                    data: {
                        email: function () {
                            return $('#email').val();
                        }
                    }
                }
            }
        },
        messages: {
            password: {
                remote: $.validator.format('Your password is incorrect')
            },
            email: {
                remote: $.validator.format('Email {0} exists in the database')
            }
        },
        submitHandler: function() { changeEmail() }
    });

    function changeEmail() {
        var changeEmailDTO = {"password":$('#password').val(),
                              "email":$('#email').val()};
        $.ajax({
            type: 'PUT',
            url: '/settings/changeEmail',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify(changeEmailDTO),
            success: function (result) {
                $('#password').val('');
                $('#email').val('');

                $('#changeEmailForm')
                    .before('<div class="alert alert-dismissable alert-success">'
                            + '<span th:text="#{settings.successChangeEmail}">An e-mail has been successfully sent on your new e-mail. Please allow a few minutes for it to get to your inbox!</span>'
                            + '</div>');

                setTimeout('hiddenAlertAfterSeconds()', 2000);
            },
            error: function (error) {
                var obj = JSON.parse(error.responseText);
                for(var i = 0; i < obj.field_errors.length; ++i) {
                    var objError = obj.field_errors[i];

                    $('#' + objError.field)
                        .after('<label id="' + objError.field + '-error" class="error" for="' + objError.field + '">' + objError.message + '</label>');
                    $('#form-' + objError.field)
                        .addClass('has-warning');
                }
            }
        });
    }

    $('#changePasswordForm').validate({
        rules: {
            oldPassword: {
                required: true,
                remote: {
                    url: '/checkUserData/checkPassword',
                    type: "GET",
                    data: {
                        password: function () {
                            return $('#oldPassword').val();
                        }
                    }
                }
            },
            newPassword: {
                noWhitespace: true,
                required: true,
                minlength: 6,
                maxlength: 36,
                strongPassword: true,
                remote: {
                    url: '/checkUserData/checkPasswordIsTheSame',
                    type: "GET",
                    data: {
                        password: function () {
                            return $('#newPassword').val();
                        }
                    }
                }
            },
            newPasswordAgain: {
                required: true,
                equalTo: '#newPassword'
            }
        },
        messages: {
            oldPassword: {
                remote: $.validator.format('Your password is incorrect')
            },
            newPassword: {
                remote: $.validator.format('The password is the same as the old one.')
            }
        },
        submitHandler: function() { changePassword() }
    });

    function changePassword() {
        var changePasswordDTO= { "oldPassword":$('#oldPassword').val(),
                                 "newPassword":$('#newPassword').val(),
                                 "newPasswordAgain":$('#newPasswordAgain').val()};
        $.ajax({
            type: 'PUT',
            url: '/settings/changePassword',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify(changePasswordDTO),
            success: function (result) {
                $('#oldPassword').val('');
                $('#newPassword').val('');
                $('#newPasswordAgain').val('');

                $('#changePasswordForm')
                    .before('<div class="alert alert-dismissable alert-success">'
                            + '<span th:text="#{settings.successChangePassword}">Your password has been changed.</span>'
                            + '</div>');

                setTimeout('hiddenAlertAfterSeconds()', 2000);
            },
            error: function (error) {
                var obj = JSON.parse(error.responseText);
                for(var i = 0; i < obj.field_errors.length; ++i) {
                    var objError = obj.field_errors[i];

                    $('#' + objError.field)
                        .after('<label id="' + objError.field + '-error" class="error" for="' + objError.field + '">' + objError.message + '</label>');
                    $('#form-' + objError.field)
                        .addClass('has-warning');
                }
            }
        });
    }

    $.validator.addMethod( "noWhitespace", function( value, element ) {
        return this.optional( element ) || /^\S+$/i.test( value );
    }, "Please no enter white space" );

    $.validator.addMethod('strongPassword', function(value, element) {
        return this.optional(element)
            || value.length >= 6 && /\d/.test(value) && /[a-z]/i.test(value)
    }, "Your password must be at least 6 characters long and contain at least one letter and one number" );
});


function hiddenAlertAfterSeconds() {
    $('.alert').remove();
}
