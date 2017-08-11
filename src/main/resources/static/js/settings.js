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
            },
            error: function (error) {
                var obj = JSON.parse(error.responseText);
                console.log('Error: ' + obj);
                console.log('Obj length: ' +  obj.field_errors.length);
                console.log('Element: ' +  obj.field_errors[0].message);
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
            },
            error: function (error) {
                var obj = JSON.parse(error.responseText);
                console.log('Error: ' + obj);
                console.log('Obj length: ' +  obj.field_errors.length);
                console.log('Element: ' +  obj.field_errors[0].message);
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
