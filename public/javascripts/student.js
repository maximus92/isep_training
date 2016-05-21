$(document).ready(function(){

function hideDiv(divId){
	$("#" + divId).hide();
}

function showDiv(divId){
	$("#" + divId).show();
}

$("#pass-to-settings-second-part").click( function(){

	$("#student-qcm-settings-first-part").hide();
	$("#student-qcm-settings-second-part").show();
	
});

$("#back-to-settings-first-part").click( function(){

	$("#student-qcm-settings-first-part").show();
	$("#student-qcm-settings-second-part").hide();
	
});
	

});