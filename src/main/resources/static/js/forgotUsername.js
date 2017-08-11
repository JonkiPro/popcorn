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
        }
    });
});