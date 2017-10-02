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

    $('#sendMessageForm').validate({
        rules: {
            to: {
                required: true,
                remote: {
                    url: '/api/v1.0/users/check/username',
                    type: "GET",
                    data: {
                        username: function () {
                            return $('#to').val();
                        }
                    }
                }
            },
            subject: {
                required: true,
                minlength: 1,
                maxlength: 255
            },
            text: {
                required: true,
                minlength: 1,
                maxlength: 4000
            }
        },
        messages: {
            to: {
                remote: $.validator.format('User {0} doesn`t exist')
            }
        },
        submitHandler: function() { sendMessage() }
    });

    function sendMessage() {
        var sendMessageDTO = { "to":$('#to').val(),
                               "subject":$('#subject').val(),
                               "text":$('#text').val()};
        $.ajax({
            type: 'POST',
            url: '/api/v1.0/messages',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify(sendMessageDTO),
            success: function (result, status, xhr) {
                window.location.replace(xhr.getResponseHeader('Location'));
            },
            error: function (error) {
                if(error.status === 403) {
                    $('#to')
                        .after('<label id="to-error" class="error" for="to">' + 'You cannot send a message to yourself.' + '</label>');

                    return;
                }
                var obj = JSON.parse(error.responseText);
                for(var i = 0; i < obj.field_errors.length; ++i) {
                    var objError = obj.field_errors[i];

                    $('#' + objError.field)
                        .after('<label id="' + objError.field + '-error" class="error" for="' + objError.field + '">' + objError.message + '</label>');
                }
            }
        });
    }
});
