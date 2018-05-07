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

    $('#registrationForm').validate({
        ignore: ".ignore",
        rules: {
            username: {
                noWhitespace: true,
                validateUsername: true,
                required: true,
                minlength: 6,
                maxlength: 36,
                remote : {
                    url: '/api/v1.0/users/check/username',
                    type: "GET",
                    data: {
                        username: function() {
                            return $('#username').val();
                        },
                        negation: function () {
                            return true;
                        }
                    }
                }
            },
            email: {
                noWhitespace: true,
                required: true,
                email: true,
                remote : {
                    url: '/api/v1.0/users/check/email',
                    type: "GET",
                    data: {
                        email: function() {
                            return $('#email').val();
                        },
                        negation: function () {
                            return true;
                        }
                    }
                }
            },
            password: {
                noWhitespace: true,
                required: true,
                minlength: 6,
                maxlength: 36,
                strongPassword: true
            },
            passwordAgain: {
                required: true,
                equalTo: '#password'
            },
            hiddenRecaptcha: {
                required: function () {
                    return grecaptcha.getResponse() === '';
                }
            }
        },
        messages: {
            username: {
                remote: $.validator.format('Username {0} exists in the database')
            },
            email: {
                remote: $.validator.format('Email {0} exists in the database')
            },
            hiddenRecaptcha: {
                required: 'Incorrect reCaptcha'
            }
        },
        submitHandler: function () { register(); }
    });

    function register() {
        var registerDTO = {"username":$('#username').val(),
                            "email":$('#email').val(),
                            "password":$('#password').val(),
                            "passwordAgain":$('#passwordAgain').val(),
                            "reCaptcha":grecaptcha.getResponse()};
        $.ajax({
            type: 'POST',
            url: '/api/v1.0/register',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify(registerDTO),
            complete: function (event) {
                if(event.status === 201) {
                    window.location.replace(event.getResponseHeader('Location'));
                } else {
                    var obj = JSON.parse(event.responseText);
                    for(var i = 0; i < obj.field_errors.length; ++i) {
                        var objError = obj.field_errors[i];

                        if(objError.field === "reCaptcha") {
                            grecaptcha.reset();
                            $('#recaptcha')
                                .after('<label id="hiddenRecaptcha-error" class="error" for="hiddenRecaptcha">' + objError.message + '</label>');
                        } else {
                            $('#' + objError.field)
                                .after('<label id="' + objError.field + '-error" class="error" for="' + objError.field + '">' + objError.message + '</label>');
                            $('#form-' + objError.field)
                                .addClass('has-warning');
                        }
                    }
                }
            }
        });
    }

    $.validator.addMethod( "noWhitespace", function( value, element ) {
        return this.optional( element ) || /^\S+$/i.test( value );
    }, "Please no enter white space" );

    $.validator.addMethod( "validateUsername", function( value, element ) {
        return this.optional( element ) || /^[a-zA-Z1-9_-]+$/i.test( value );
    }, "Please correct format" );

    $.validator.addMethod('strongPassword', function(value, element) {
        return this.optional(element)
            || value.length >= 6 && /\d/.test(value) && /[a-z]/i.test(value)
    }, "Your password must be at least 6 characters long and contain at least one letter and one number" );
});

function recaptchaCallback() {
    $('#hiddenRecaptcha').valid();
}