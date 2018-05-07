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

    $('#replyMessageForm').validate({
        rules: {
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
        submitHandler: function() { sendMessage() }
    });

    function sendMessage() {
        var sendMessageDTO = { "to":$('#sender').val(),
                               "subject":$('#subject').val(),
                               "text":$('#text').val()};
        $.ajax({
            type: 'POST',
            url: '/api/v1.0/messages',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify(sendMessageDTO),
            complete: function (event) {
                if(event.status === 201) {
                    window.location.replace('/messages');
                } else {
                    var obj = JSON.parse(event.responseText);
                    for(var i = 0; i < obj.field_errors.length; ++i) {
                        var objError = obj.field_errors[i];

                        $('#' + objError.field)
                            .after('<label id="' + objError.field + '-error" class="error" for="' + objError.field + '">' + objError.message + '</label>');
                    }
                }
            }
        });
    }
});
