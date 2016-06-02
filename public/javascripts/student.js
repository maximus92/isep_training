$(document).ready(
		function() {
			
			var getUrlParameter = function getUrlParameter(sParam) {
			    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
			        sURLVariables = sPageURL.split('&'),
			        sParameterName,
			        i;

			    for (i = 0; i < sURLVariables.length; i++) {
			        sParameterName = sURLVariables[i].split('=');

			        if (sParameterName[0] === sParam) {
			            return sParameterName[1] === undefined ? true : sParameterName[1];
			        }
			    }
			};

			/**************Scripts pour la selection des paramètres d'un qcm******************/
			
			
			
			// Passer de la page de paramètre 1 à 2 et inversement
			
			$("#pass-to-settings-second-part").click(function() {

				$("#student-qcm-settings-part-1").hide();
				$("#student-qcm-settings-part-2").show();

			});

			$("#back-to-settings-first-part").click(function() {

				$("#student-qcm-settings-part-1").show();
				$("#student-qcm-settings-part-2").hide();

			});

			
			
			// Sélection du modules
			var id_module;
			var id_chapter;

			$("#module > li").click(function() {
				$("#module > li").css({
					'background-color' : 'white',
					'color' : '#333'
				});
				$(this).css({
					'background-color' : '#0D6186',
					'color' : 'white'
				});

				$(".selected-module").removeClass("selected-module");
				$(this).addClass("selected-module");

				// Récupération de l'id du module selectionné
				id_module = $(this).attr("id").substring(10);

				// Cacher les Chapitres affichés
				$(".chapters-list").hide();

				// Afficher les chapitres correspondants au module selectionné
				$("#chapters-module-" + id_module).show();

			});
			
			

			// selection du chapitre

			$(".chapters-list > li").click(function() {
				$(".chapters-list > li").css({
					'background-color' : 'white',
					'color' : '#333'
				});
				$(this).css({
					'background-color' : '#0D6186',
					'color' : 'white'
				});

				// Récupération de l'id du chapitre selectionné
				id_chapter = $(this).attr("id").substring(11);

			});

			
			
			// Cacher les chapitres au chargement de la page

			$(window).load(function() {
				$(".chapters-list").hide();
			});
			
			
			
			// Récupération des paramètres selectionnés

			var qcm_json_settings;

			$("#send-qcm-settings").click(function() {
				var question_num = parseInt($("#number-of-questions")
						.find(":selected").text());
				var qcm_time = (parseInt($("#qcm-time-hours").find(
						":selected").text())
						* 60
						+ parseInt($("#qcm-time-minutes").find(
								":selected").text())) * 60;
				var question_level = parseInt($("#question-level")
						.find(":selected").text());

				qcm_json_settings = { 
					"id_chapter" : id_chapter,
					"question_num" : question_num,
					"qcm_time" : qcm_time,
					"question_level" : question_level
				};

				$.ajax({
					type : "POST",
					url : "/student/trainingQcmSettings",
					data : qcm_json_settings,
					dataType : "json",

					success : function(data) {
						window.location.replace("trainingQcm?question_num=1", data);
					}
				});

			});
			
			/*******************Scripts pour la réalisation du qcm*************************/
			
			
			
			// Navigation dans les questions d'un qcm
			
			var update_qcm_json;
			
			$(".qcm_question_nav").click(function(){
				
				var question_num;
				var update_qcm_json = $('form').serialize();
				var time = $("#qcm-time").countdown('getTimes');
				var current_time = $.countdown.periodsToSeconds(time);
				
				if($(this).attr("id") == "next-question"){
					question_num = parseInt(getUrlParameter('question_num')) + 1;
				} 
				if($(this).attr("id") == "last-question"){
					question_num = parseInt(getUrlParameter('question_num')) - 1;
				}
				
				$.ajax({
					type: 'POST',
					url: '/student/updateQcm',
					data: update_qcm_json + '&time=' + current_time,
					
					success: function(){
						window.location.replace("trainingQcm?question_num=" + question_num);
					}
				});
			});
			
			
			
			// Affichage minuteur 
			
			var qcm_time = $('#qcm-time').html();
			
			$('#qcm-time').countdown({layout: '<b>{h<}{hn} : {h>}'+ 
			    '{mn} : {sn} </b>', until: +(qcm_time)});
			
			
			
			// Auto check des questions
			
			
			id_question = $('.student-qcm-question').attr('id').substring(8);
			id_qcm = $('.qcm-container').attr('id').substring(4);
			
			$.ajax({
				type: 'POST',
				url: '/student/checkbox',
				data: {id_qcm : id_qcm, id_question : id_question, id_answer : {answer1: "d"}},
				dataType: 'json',
				
				success: function(data){
					if(data != null){
						for (i in data){
							if (data[i].is_select){
								$("#answer"+data[i].id_answer).prop("checked", true);
							} else {
								$("#answer"+data[i].id_answer).prop("checked", false);
							}
						}
					}
				}
			});
		});