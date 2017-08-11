document.addEventListener('DOMContentLoaded', function() {
	var input = document.querySelector('.input-search');
	var btn = document.querySelector('#btnSearch');
	
	btn.disabled = true;
	
	input.addEventListener('keyup', function() {
		if(input.value.length >= 1) {
			btn.disabled = false;
		} else {
			btn.disabled = true;
		}
	});
});


