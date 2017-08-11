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
                maxlength: 4000
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
            url: '/messages/sendMessage',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify(sendMessageDTO),
            success: function () {
                window.location.replace('/messages');
            },
            error: function (error) {
                var obj = JSON.parse(error.responseText);
                console.log('Error: ' + obj);
                console.log('Obj length: ' +  obj.field_errors.length);
                console.log('Element: ' +  obj.field_errors[0].message);
            }
        });
    }
});
