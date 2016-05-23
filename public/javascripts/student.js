$(document).ready(function(){

function hideDiv(divId){
	$("#" + divId).hide();
}

function showDiv(divId){
	$("#" + divId).show();
}

/************** Scripts pour la selection des paramètres d'un qcm *****************/

// Passer de la page de paramètre 1 à 2 et inversement

$("#pass-to-settings-second-part").click( function(){

	$("#student-qcm-settings-part-1").hide();
	$("#student-qcm-settings-part-2").show();
	
});

$("#back-to-settings-first-part").click( function(){

	$("#student-qcm-settings-part-1").show();
	$("#student-qcm-settings-part-2").hide();
	
});

// Sélection du modules
var id_module;
var id_chapter;

$("#module > li").click(function(){
	$("#module > li").css({
		'background-color': 'white',
		'color': '#333'
	});
	$(this).css({
		'background-color': '#0D6186',
		'color': 'white'
	});
	
	$(".selected-module").removeClass("selected-module");
	$(this).addClass("selected-module");
	
	// Récupération de l'id du module selectionné
	id_module = $(this).attr("id").substring(10,11);

	// Cacher les Chapitres affichés
	$(".chapters-list").hide();
	
	// Afficher les chapitres correspondants au module selectionné
	$("#chapters-module-" + id_module).show();
	

});

// selection du chapitre

$(".chapters-list > li").click(function(){
	$(".chapters-list > li").css({
		'background-color': 'white',
		'color': '#333'
	});
	$(this).css({
		'background-color': '#0D6186',
		'color': 'white'
	});
	
	// Récupération de l'id du chapitre selectionné
	id_chapter = $(this).attr("id").substring(11,12);
	
});

// Cacher les chapitres au chargement de la page

$(window).load(function(){
	$(".chapters-list").hide();
});
	
// Récupération des paramètres selectionnés

var qcm_json_settings;

$("#send-qcm-settings").click(function(){
	var question_num = parseInt($("#number-of-questions").find(":selected").text());
	var qcm_time = parseInt($("#qcm-time-hours").find(":selected").text())*60 + parseInt($("#qcm-time-minutes").find(":selected").text());
	var question_level = parseInt($("#question-level").find(":selected").text());
	
	qcm_json_settings = {
			"id_module": id_module,
			"id_chapter": id_chapter,
			"question_num": question_num,
			"qcm_time": qcm_time,
			"question_level": question_level
	};
	
	alert(JSON.stringify(qcm_json_settings, null, '\t'));
	
});


});