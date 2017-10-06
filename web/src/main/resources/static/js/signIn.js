document.addEventListener('DOMContentLoaded', function () {
    $('#signInForm').validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            }
        },
        submitHandler: function () { signIn(); }
    });

    function signIn() {
        $('.alert').remove();
        var signInDTO = {"username":$('#username').val(),
                         "password":$('#password').val(),
                         "remember-me":$('#remember-me').val()};
        $.ajax({
            type: 'POST',
            url: '/signIn',
            data: signInDTO,
            success: function (result, status, xhr) {
                window.location.replace(xhr.getResponseHeader("Location"));
            },
            error: function (error) {
                $('#signInForm')
                    .before('<div class="alert alert-dismissable alert-danger">'
                          + '<span th:text="#{forgotPassword.error}">The username or password is incorrect!</span>'
                          + '</div>');
            }
        });
    }
});