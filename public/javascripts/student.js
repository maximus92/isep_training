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

$("#module > li").click(function(){
	$("#module > li").css({
		'background-color': 'white',
		'color': '#333'
	});
	$(this).css({
		'background-color': '#0D6186',
		'color': 'white'
	});
	
	// Récupération de l'id du module selectionné
	var moduleId = $(this).attr("id").substring(10,11);

	// Cacher les Chapitres affichés
	$(".chapters-list").hide();
	
	// Afficher les chapitres correspondants au module selectionné
	$("#chapters-module-" + moduleId).show();
	

});

// Cacher les chapitres au chargement de la page

$(window).load(function(){
	$(".chapters-list").hide();
});
	

});