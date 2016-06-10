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

			// selectionner automatiquement le premier module et chapitre
			if($('#module').length){
				$("#module li:first-child").css({
					'background-color' : '#0D6186',
					'color' : 'white'
				});
				
				$("#module li:first-child").addClass("selected-module");
				id_module = $("#module li:first-child").attr("id").substring(10);
				
				$("#chapters-module-" + id_module + " li:first-child").css({
					'background-color' : '#0D6186',
					'color' : 'white'
				});

				id_chapter = $("#chapters-module-" + id_module + " li:first-child").attr("id").substring(11);

			}
			
			
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
				$(".all-chapters .chapters-list:first-child").show();	
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
				
				var good_answer = parseInt($("#good-answer").val());
				var bad_answer = parseInt($("#bad-answer").val());
				var no_answer = parseInt($("#no-answer").val());

				qcm_json_settings = { 
					"id_chapter" : id_chapter,
					"question_num" : question_num,
					"qcm_time" : qcm_time,
					"question_level" : question_level,
					"good_answer" : good_answer,
					"bad_answer" : bad_answer,
					"no_answer" : no_answer
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
			
			if(getUrlParameter('question_num') <= 1){
				$("#last-question").hide();
			} else {
				$("#last-question").show();
			}
			if($('.last-question').length){
				if ($(".last-question").attr('id').substring(19) == getUrlParameter('question_num')){
				$("#next-question").text("valider");
			}
			}
			
			
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
			
			
			
			// Affichage minuteur et numéro de question
			
			var qcm_time = $('#qcm-time').html();
			
			$('#qcm-time').countdown({layout: '<b>{h<}{hn} : {h>}'+ 
			    '{mn} : {sn} </b>', until: +(qcm_time), onExpiry: timeOut});
			
			function timeOut(){
				window.location.replace("trainingQcm?question_num="+(parseInt($(".last-question").attr('id').substring(19) + 1)));
			}
			
			$('.question-num').html(getUrlParameter('question_num'))
			
			
			// Auto check des questions
			
			if($(".qcm-container").length){
				$.ajax({
					type: 'POST',
					url: '/student/checkbox',
					data: {id_qcm : $('.qcm-container').attr('id').substring(4), id_question : $('.student-qcm-question').attr('id').substring(8)},
					dataType: 'json',
					
					success: function(data){
						if(data != null){
							for (i in data){
								if (data[i].select){
									$("#answer"+data[i].id_answer).prop("checked", true);
								} else {
									$("#answer"+data[i].id_answer).prop("checked", false);
								}
							}
						}
					}
				});
			}
			

			/**********************Script pour la correction d'un qcm*************************/

			
			// affichage correction détaillée
			
			$(".question-correction-details").click(function(){
				var id_question = $(this).attr('id').substring(8);	
				var id_qcm = getUrlParameter('id_qcm');
				var question_number = $(this).data("number");
				$.ajax({
					type: 'POST',
					url: '/student/getCorrectionForQuestion',
					data: {id_qcm: id_qcm, id_question: id_question},
					dataType: 'json', 
					
					success: function(data){
						$('.modal-question').html(data[0].question);
						$('.modal-correction').html(data[0].correction);
						$('.modal-answer').remove()
						for(i in data[1]){
							var checked = "";
							var isgoodanswer = "";
							if(data[1][i].select){
								checked = "checked";
							} 
							
							if(data[1][i].istrue){
								isgoodanswer = "good-answer";
							} else {
								isgoodanswer = "bad-answer";
							}
							
							$('.modal-answers').append(
									'<div class="form-group modal-answer">'+
						               ' <label class="col-md-12">'+
					                    '<input type="checkbox" class="answer" id="answer' + data[1][i].id_answer + '" ' + checked + '  readonly >'+
					                    '<span class="col-md-offset-1 '+isgoodanswer+'" id="answer-' + data[1][i].id_answer + '">'+ data[1][i].answer + '</span>'+
						                '</label>' +
						            '</div>'
			                );
							$('.modal-question-number').html(question_number);
							
						}
					}
				})
			});
			
			
			/**************************Prévisualisation Qcm**************************/
			
			$('#qcm-preview').click(function(){
				var id_qcm = $('.qcm-container').attr('id').substring(4);
				$.ajax({
					type: "GET",
					url: '/student/qcmPreview',
					data: {id_qcm: id_qcm},
					dataType: 'json',
					
					success: function(data){
								$('.preview-question').remove();
								for(i in data){
									if(data[i].answered){
										$('.modal-preview-table').append(
											'<tr class="preview-question" data-num=' + i + '><td>' + data[i].question + '</td>'+
											'<td class="td-answered"> <div class="answered-question"></div> </td></tr>'
										);
									} else {
										$('.modal-preview-table').append(
											'<tr class="preview-question" data-num=' + i + '><td>' + data[i].question + '</td>'+
											'<td class="td-answered"> <div class="unanswered-question "> </div></td></tr>'
										);
									}
									
								}
					}
						
				});
			});
			
			$('.modal-preview-table').delegate('tr', 'click', function(){
				var id_question = $(this).data("num") + 1;
				var update_qcm_json = $('form').serialize();
				var time = $("#qcm-time").countdown('getTimes');
				var current_time = $.countdown.periodsToSeconds(time);
				
				$.ajax({
					type: 'POST',
					url: '/student/updateQcm',
					data: update_qcm_json + '&time='+current_time,
					
					success: function(){
						window.location.replace("trainingQcm?question_num="+id_question);
					}
				});
				
				
			});
			
			$('.preview-correct-qcm').click(function(){
				var update_qcm_json = $('form').serialize();
				
				$.ajax({
					type: 'POST',
					url: '/student/updateQcm',
					data: update_qcm_json + '&time=0',
					
					success: function(){
						window.location.replace("trainingQcm?question_num="+parseInt($(".last-question").attr('id').substring(19)) + 1);
					}
				});
			});
			
		});