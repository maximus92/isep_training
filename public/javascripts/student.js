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
				if($(".selected-chapter").length){
					$("#student-qcm-settings-part-1").hide();
					$("#student-qcm-settings-part-2").show();
				} else {
					$(".container").append('<div class="pop-up center"><p>Veuillez sélectionner un chapitre !</p></div>')
					$(".pop-up").fadeOut(1500, function(){
						$(".pop-up").remove();
					});
				}

			});

			$("#back-to-settings-first-part").click(function() {

				$("#student-qcm-settings-part-1").show();
				$("#student-qcm-settings-part-2").hide();

			});

			
			
			// Sélection du modules
			
			var id_module;

			// selectionner automatiquement le premier module et chapitre
			if($('#module').length){
				if($('#student-qcm-settings-part-1').length){
					$("#module li:first-child").css({
						'background-color' : '#0D6186',
						'color' : 'white'
					});
					
					$("#module li:first-child").addClass("selected-module");
					id_module = $("#module li:first-child").attr("id").substring(10);
					
					$("#chapters-module-" + id_module + " li:first-child").addClass('selected-chapter');
				}
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

				// Cacher et deselectionner les Chapitres affichés
				$(".chapters-list").hide();
				$(".selected-chapter").removeClass("selected-chapter");
				// Afficher les chapitres correspondants au module selectionné
				$("#chapters-module-" + id_module).show();
				$("#chapters-module-" + id_module + " li:first-child").addClass("selected-chapter");

			});
		
			//recherche d'un module
			
			$('#search-module').keyup(function(){
				$("#module > li").hide();
				var search_string = $(this).val().toLowerCase();
				
				$("#module > li").each(function(){
					var module = $(this).text().toLowerCase();
					
					if(module.indexOf(search_string) >= 0){
						$(this).show();
					}
				});
			});
			
			// recherche d'un chapitre 
			
			$('#search-chapter').keyup(function(){
				$("#chapters-module-" + id_module + " > li").hide();
				var search_string = $(this).val().toLowerCase();
				
				$("#chapters-module-" + id_module + " > li").each(function(){
					var module = $(this).text().toLowerCase();
					
					if(module.indexOf(search_string) >= 0){
						$(this).show();
					}
				});
			});
			
			

			// selection du chapitre

			$(".chapters-list > li").click(function() {
				
				if($(this).hasClass("selected-chapter")){
					$(this).removeClass("selected-chapter");
				} else{
					$(this).addClass("selected-chapter");
				}

			});

			
			
			// Cacher les chapitres au chargement de la page

			$(window).load(function() {
				$(".chapters-list").hide();
				$(".all-chapters .chapters-list:first-child").show();	
			});
			
			
			
			// Récupération des paramètres selectionnés

			var qcm_json_settings;

			$("#send-qcm-settings").click(function() {

				var id_chapters = "";
				
				$(".selected-chapter").each(function(){
					id_chapters += " " + $(this).attr("id").substring(11);
				});
				
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
					"id_chapter" : id_chapters,
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
						if(data.noQuestion){
							$("body").append('<div class="pop-up center"><p>Pas de question selon les critères sélectionnés <br> Veuillez changer vos paramètres</p></div>')
							$(".pop-up").fadeOut(2500, function(){
								$(".pop-up").remove();
							});
						}else {
							window.location.href = "/student/trainingQcm/0?question_num=1", data;
						}
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
					$("#next-question").text("Valider");
					$("#next-question").attr("data-toggle", "modal").attr("data-target","#preview-modal");
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
				if ($(".last-question").attr('id').substring(19) != getUrlParameter('question_num') || $(this).attr("id") == 'last-question'){
					$.ajax({
						type: 'POST',
						url: '/student/updateQcm',
						data: update_qcm_json + '&time=' + current_time,
						
						success: function(){
							window.location.href = "?question_num=" + question_num;
						}
					});
				}
			});
			
			
			
			// Affichage minuteur et numéro de question
			
			var qcm_time = $('#qcm-time').html();
			
			$('#qcm-time').countdown({layout: '<b>{h<}{hn} : {h>}'+ 
			    '{mn} : {sn} </b>', until: +(qcm_time), onExpiry: timeOut});
			
			function timeOut(){
				window.location.href = "?question_num="+(parseInt($(".last-question").attr('id').substring(19) + 1));
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
			
			// sauvegarde au rafraichissement
			
			$(window).bind('beforeunload', function() {
				if($(".qcm-container").length){
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
						async: false,
						data: update_qcm_json + '&time=' + current_time,
						
					});
				}
				
			});

			window.onload = function () {
			    if ($(".result-container").length || $(".qcm-container").length) {
					if (typeof history.pushState === "function") {
						history.pushState("jibberish", null, null);
						window.onpopstate = function() {
							history.pushState('newjibberish', null, null);
							// Handle the back (or forward) buttons here
							// Will NOT le refresh, use onbeforeunload for this.
							
							if($(".qcm-container").length){
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
									async: false,
									data: update_qcm_json + '&time=' + current_time,
									
								});
							} else {
								window.location.href = "/student";
							}
						};
					} else {
						var ignoreHashChange = true;
						window.onhashchange = function() {
							if (!ignoreHashChange) {
								ignoreHashChange = true;
								window.location.hash = Math.random();
								// Detect and redirect change here
								// Works in older FF and IE9
								// * it does mess with your hash symbol (anchor?) pound sign
								// delimiter on the end of the URL
							} else {
								ignoreHashChange = false;
							}
						};
					}
				}
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
					                    '<input type="checkbox" class="answer" id="answer' + data[1][i].id_answer + '" ' + checked + '  readonly disabled>'+
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
			
			$('#qcm-preview, #next-question').click(function(){
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
						window.location.href = "?question_num="+id_question;
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
						window.location.href = "?question_num="+parseInt($(".last-question").attr('id').substring(19)) + 1;
					}
				});
			});
			
			/************************** script pour les tests de cours **************************/
			
			$("#test_module_select").change(function(){
				var id_module = $(this).val();
				$.ajax({
					type: 'POST',
					url: '/student/displayTestList',
					data: {id_module: id_module},
					success: function(data){
						var div="";
						for(var i=0; i<data.length;i++){
							div = '<div class="col-xs-6">'+data[i].title+'</div>'+
									'<div class="col-xs-6">'+
										'<button class="btn btn-primary btn-answer-to-test" data-title="' + data[i].title + '" id="btn-answer-to-test'+data[i].id_test+'">Répondre</button>'+
									'</div>';
						}
						$("#test_result_from_select").html(div);
					}
				});
			});
			
			$("#test_result_from_select").on("click",".btn-answer-to-test",function(){
				var id_test = $(".btn-answer-to-test").attr("id").substring(18);
				var title = $(".btn-answer-to-test").data("title");
				$("#test_cours_modal_password").modal("show");
				$("#test_student_password").val("");
				$("#student_test_id_test").val(id_test);
				$("#student_test_title").val(title);
			});
			
			$("#test-student-modal-btn-confirmation").click(function(){
				var dataString = $("#form-modal-test-student").serialize();
				$.ajax({
					type: 'POST',
					url: '/student/displayTest',
					data: dataString,
					success: function(data){
						if(data.password){
							window.location.href = "/student/trainingQcm/"+data.id_qcm+"?question_num=1";
						}else{
							alert("Mauvais mot de passe");
						}
						
					}
				});
			});
			
			$("#form-modal-test-student").submit(function(e){
				e.preventDefault();
			});
			
			/************************Scripts pour l'historique*********************/
			
			// Voir la correction d'un qcm
			
			$(".history-show-correction").click(function(){
				var id_qcm = $(this).data("id");
				
				window.location.href = "/student/resultat?id_qcm=" + id_qcm;
			});
			
			
			/**************************Scripts pour le mode exam*******************/
			
			$("#module > li").click(function(){
				var id_module = $(this).attr("id").substring(10);
				
				$.ajax({
					type: 'POST',
					url: '/student/displayExamList',
					data: {id_module: id_module},
					dataType : 'json',
					
					success: function(data){
						$("#exam-list > li").remove();
						for(var i = 0; i < data.length; i++){
							$("#exam-list").append('<li class="list-group-item" id="exam-' + data[i].id_qcm + '">' + data[i].title +'</li> ');
						}
					}
				})
			});
			
			$("#exam-list").delegate('li', 'click', function() {
				$(".selected-exam").css({
					'background-color' : 'white',
					'color' : '#333'
				});
				$(".selected-exam").removeClass("selected-exam");
				$(this).addClass("selected-exam");
				
				
				$(this).css({
					'background-color' : '#0D6186',
					'color' : 'white'
				});
				
			});
			
			$("#start-exam").click(function(){
				var id_exam = $(".selected-exam").attr("id").substring(5);
				
				$.ajax({
					type: 'POST',
					url: '/student/displayExam',
					data: {id_exam: id_exam},
					
					success: function(){
						window.location.href = "/student/trainingQcm/0?question_num=1";
					}
				});
			});
			
			
			/******************Script page d'accueil********************/
			
			if($(".currents-qcm").length){
				var dataString;
				$.ajax({
					type: 'POST',
					url: '/student/currentsQcm',
					data: dataString,
					dataType: 'json',
					
					success: function(data){
						for(var i = 0; i < data.length; i++){
							$(".currents-qcm").append('<tr class="continue-qcm" data-id="' + data[i].id_qcm + '"><td>' + (i+1) + '</td>' +
									'<td>' + data[i].module + '</td>' +
									'<td>' + data[i].createat + '</td>'+
									'<td>' + data[i].number_of_questions + '</td></tr></div>');
						}
					}
				});
				
				$(".table").delegate('tr', 'click', function(){
					window.location.href = "/student/trainingQcm/" + $(this).data("id") + "?question_num=1"
				});
			}
		});