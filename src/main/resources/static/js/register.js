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
        rules: {
            username: {
                noWhitespace: true,
                validateUsername: true,
                required: true,
                minlength: 6,
                maxlength: 36,
                remote : {
                    url: '/checkUserData/checkUsernameAtRegistering',
                    type: "GET",
                    data: {
                        username: function() {
                            return $('#username').val();
                        }
                    }
                }
            },
            email: {
                noWhitespace: true,
                required: true,
                email: true,
                remote : {
                    url: '/checkUserData/checkEmailAtRegistering',
                    type: "GET",
                    data: {
                        email: function() {
                            return $('#email').val();
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
            }
        },
        messages: {
            username: {
                remote: $.validator.format('Username {0} exists in the database')
            },
            email: {
                remote: $.validator.format('Email {0} exists in the database')
            }
        }
    });

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
