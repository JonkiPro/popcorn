document.addEventListener('DOMContentLoaded', function () {
    $('#signInForm').validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            }
        }
    });
});