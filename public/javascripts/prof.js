	function removeDiv(divId) {
		$("#" + divId).remove();
	}
	
	function getfile() {
		document.getElementById('imgInp').click();
		document.getElementById('selectedfile').value = document.getElementById('imgInp').value;
	}
	
	addOption("#nbrQ",200);
	addOption("#minR",10);
	addOption("#maxR",10);
	addOption("#hour",10);
	addOption("#minute",60);
	
	function addOption(id_select,num){
		for (var i = 1; i <= num; i++) {
			$(id_select).append($('<option>', { 
		        value: i,
		        text : i 
		    }));
			
		}
	}
	

$(document).ready(function(){ 
		 var l = 0;
		 var v = $("#reponse_counter").val(); 
		  
		 $("#addAnswer").click(function(){
				l++;
				v++; 
				var newdiv = addAnswerDiv(l);
			 	$("#addQuestion").append(newdiv);
			 	$("#reponse_counter").val(v); 
		 });
		 
		 $("#addQuestion").on('click', ".delete_answer", function() {
			 var id = $(this).attr("id").substring(13);
			 $("#remove"+id).remove();
		 });
		 
	 $(".modal-content").on('click', "#addAnswer", function() {
		 alert("ok");
	 });  

	  /**** MODULE ***/
	  
		$("#module-form").submit(function(event){ 
		  event.preventDefault(); 
	  });
	  
	  $("#btn-ajout-module").click(function(){ 
		  var dataString = $("#module-form").serialize();
			ajaxBody("/add-module",dataString,function(data) { 
					$(".module-reponse").append(addModuleDiv(data.id,data.name)); 
			});
	  });
		
	  $(".module-reponse").on('click', ".module-delete", function() {
		  var id = $(this).attr('id').substring(17);
			var dataString = {id : id};
			ajaxBody("/delete-module",dataString,function(data) {
					var mod = ".module-display"+data.id_module;
					$(mod).css({display: "none"});
			});
		});
	
		$("#module-li").one("click",function(){ 
		  var dataString = $("#module-form").serialize();
			ajaxBody("/select-module",dataString,function(data) {
			 		for(var i=0; i<data.length;i++){ 
							$(".module-reponse").append(displayModuleDiv(data,i)); 
			 		}
			});
	  });
	  
	  $("#bdd-li").one("click",function(){
		  dataString = "";
		  
		  $.ajax({ 
			  type: "POST", 
			  url: "/select-question",
			  data: dataString, 
			  dataType: "json",
			  
			  success: function(data) {
					 for(var i=0; i<data.length;i++){ 
					  $(".question-select").append('<div class="padding-10 col-sm-8 question-display'+data[i].id_question+'">'+ data[i].question+ 
							  					'</div>'+
							  					'<div class="col-sm-2 question-display'+data[i].id_question+'"> <button type="button" class="btn btn-warning modifyQA" data-toggle="modal" data-target="#modifyQ" id="modifyQA'+data[i].id_question+'">Modifier</button></div> '+
							  					'<div class="col-sm-2 question-display'+data[i].id_question+'"> <button type="button" class="btn btn-danger question-delete" id="deleteQ'+data[i].id_question+'">Supprimer</button></div>');
					  
					 }
				  }
		  }); 
	  });
	  
	  $(".question-select").on('click', ".question-delete", function() {
		  var id = $(this).attr('id').substring(7);
			var dataString = {id : id};
			ajaxBody("/delete-question",dataString,function(data) {
					var ques = ".question-display"+data.id_question;
					$(ques).css({display: "none"});
			});
		});
	  
	  
	  
	  $(".question-select").on('click', ".modifyQA", function() {
		  var id = $(this).attr('id').substring(8);
		  $.ajax({ 
			  type: "POST", 
			  url: "/select-answer",
			  data: {id : id}, 
			  dataType: "json",
			  
			  success: function(data) {
				  $(".form_update_question").remove();
				 if(data[1][0].level == "0"){
					var difficulty = '<div class="col-xs-3">'+
					'<SELECT name="level">'+
					'<OPTION value="0" selected>Facile</OPTION>'+
					'<OPTION value="1">Moyen</OPTION>'+
					'<OPTION value="2" selected>Difficile</OPTION>'+
				'</SELECT>'+
			'</div>';	 
				 }
				 if(data[1][0].level == "1"){
						var difficulty = '<div class="col-xs-3">'+
						'<SELECT name="level">'+
						'<OPTION value="0" >Facile</OPTION>'+
						'<OPTION value="1" selected>Moyen</OPTION>'+
						'<OPTION value="2" >Difficile</OPTION>'+
					'</SELECT>'+
				'</div>';	 
					 }
				 if(data[1][0].level == "2"){
						var difficulty = '<div class="col-xs-3">'+
						'<SELECT name="level">'+
						'<OPTION value="0" >Facile</OPTION>'+
						'<OPTION value="1">Moyen</OPTION>'+
						'<OPTION value="2" selected>Difficile</OPTION>'+
					'</SELECT>'+
				'</div>';	 
					 }
				 if(data[1][0].forexam == "0"){
						var forexam = '<div class="col-xs-2">'+
						'<SELECT name="forexam">'+
						'<OPTION value="0" selected>Entrainement des élèves</OPTION>'+
						'<OPTION value="1">Examen</OPTION>'+
					'</SELECT>'+
				'</div>';	 
					 }
				 else{
					 var forexam = '<div class="col-xs-2">'+
						'<SELECT name="forexam">'+
						'<OPTION value="0" >Entrainement des élèves</OPTION>'+
						'<OPTION value="1" selected>Examen</OPTION>'+
					'</SELECT>'+
				'</div>';
					 
				 }
					  var formulaire1 = '<form method="post" class="form_update_question" action="@routes.ProfessorController.updateA()">'+
								'<div class="row">'+
								'<div class="col-xs-2">'+
									'<label>Difficulté : </label>'+
								'</div>'+difficulty+
								
								'<div class="col-xs-3">'+
									'<label>Question pour : </label>'+
								'</div>'+forexam+
								
							'</div>'+
							'</br>'+
							  '<div class="row">'+
						'<div class="col-xs-2 ">'+
							'<label for="module">Module: </label>'+
						'</div>'+
						'<div class="col-xs-3">'+
							'<select id="test_module" name="test_module" size="1"></select>'+
						'</div>'+
						'<div class="col-xs-2">'+
							'<label for="chapter">Chapitre: </label>'+
						'</div>'+
						'<div class="col-xs-3">'+
							'<select name="test_chapter" size="1">'+
								'<option value="">Aucun</option>'+
							'</select>'+
						'</div>'+
					'</div>'+
							'</br>'+


							'<div class="form-group">'+
								'<label for="question">Question</label> <input type="text"'+
									'class="form-control" id="question" name="question" value="'+data[1][0].question+'">'+

								'<div class="pull-right">'+
									
									'<button type="button" class="btn btn-success btn-sm">&</button>'+
								'</div>'+
							'</div>'+
							'</br> </br>'+

							'<div class="row">'+
								'<div class="col-xs-3 pull-right">'+
									'<label> Cocher les bonnes </br>réponses'+
									'</label>'+
								'</div>'+
							'</div>'+
							'</br>';

					  for(var i=0; i< data[0].length; i++){ 
						  if(data[0][i].istrue == "0"){
							var isTrue =  '<div class="col-xs-1">'+
								'<label> <input type="checkbox" value="0" name="goodA'+data[0][i].id_answer+'">'+
								'</label>'+
							'</div>';
						  }
						  else{
							  var isTrue =  '<div class="col-xs-1">'+
								'<label> <input type="checkbox" value="'+data[0][i].istrue+'" name="goodA'+data[0][i].id_answer+'"checked>'+
								'</label>'+
							'</div>';
						  }
						  formulaire1 = formulaire1 + '<div id="addQuestionA">'+
							'<input type="hidden" value="1" name="reponse_counter"'+
								'id="reponse_counter" />'+

							'<div id="remove0">'+
								'<label for="reponse">Réponse</label>'+
								'<div class="row">'+
									'<div class="col-xs-10">'+
										'<input type="text" class="form-control answer-label" id="reponse'+data[0][i].id_answer+'"'+
											'value="'+data[0][i].answer+'">'+
									'</div>'+isTrue+
									
									'<div class="col-xs-1">'+
										'<i class="fa fa-times delete_answer" id="delete_answer0"></i>'+
									'</div>'+
								'</div>'+
							'</div>'+
						'</div>';
					  }
					  
					  formulaire1 =formulaire1 +'</br>'+
						'<button type="button" class="btn btn-primary pull-left"'+
						'id="addAnswer">Ajouter une réponse</button>'+
					'</br> </br> </br> <label for="correction">Correction détaillée</label>'+
					'<textarea rows="4" cols="50" class="form-control" name="correction">'+data[1][0].correction+'</textarea>'+
					'</br>'+
				'</form>'; 
					  $(".answer-select").append(formulaire1);

					 
				  }
		  }); 
	  });
	  
	  $(".answer-select").on('click', "#validateModification", function() {
		  var id_answer = $(".answer-select").$(".answer-label").attr('id').substring(7);
		  $.ajax({ 
			  type: "POST", 
			  url: "/update-answer",
			  data: {id : id}, 
			  dataType: "json",
			  
			  success: function(data) {
				  alert("La reponse a été modifiée!");
			  }
		  }); 
		  
		});
	  
	  
	  
	  
	  
	  /** TEST DE COURS **/
		$("#add-test").hide();
	  $(".test-detail").hide();
	  
	  $("#create-test").click(function(){
	  		$(".test-info").hide();
	  		$("#add-test").fadeIn();
	  		ajaxBody("/select-module","",
				function(data){
						$('#test_module option').remove();
						$.each(data, function() {
								$("#test_module").append($("<option />").val(this.id_module).text(this.module_name));
						});
				});
	  	});
	  	
		$("#add-test-cancel").click(function(){
	  		$("#add-test").hide();
	  		$(".test-info").fadeIn();
	  		$(".Qpaire").remove();
	  		$(".Qimpaire").not(":first").remove();
	  		$("#test").find("input").val("");
	  		$('#question1_goodA1').attr('checked', false);
	  		return false;
	  });
	  	
	  	var answer_test_counter = $("#answer_test_counter").val();
	  	var question_test_counter = $("#question_test_counter").val(); 
	  	var i = 2;
	  	var m = 1;
		$("#ajouter-test-q").click(function(){
				m++;
				var paire_ou_impaire;
				if(i/2 == Math.round(i/2)){
						paire_ou_impaire = "Qpaire";
				}else{
						paire_ou_impaire = "Qimpaire";
				}
				var newdiv =  addTestQuestionDiv(paire_ou_impaire,i,m);
				$("#container_testQ").append(newdiv)
			 	i++;
				question_test_counter++;
				$("#question_test_counter").val(question_test_counter);
				answer_test_counter++;
				$("#answer_test_counter").val(answer_test_counter);
		});
		
		$("#container_testQ").on('click', ".ajouter-test-r", function() {
				var id = $(this).attr("id").substring(14);
				m++;
			 	var newdiv = addTestAnswerDiv(id,m);
				$("#question"+id).append(newdiv);
				answer_test_counter++;
				$("#answer_test_counter").val(answer_test_counter);
		});
		
		$("#container_testQ").on('click', ".test_cours_delete_answer", function() {
			  var id = $(this).attr("id").replace(/_delete/g, '');
			  $("#"+id).remove();
		});
		
		$("#container_testQ").on('click', ".delete-test-q", function() {
			  var id = $(this).attr("id").substring(13);
			  $("#Test_Q"+id).remove();
		});
		
		$("#test-li").click(function(){ 
			  ajaxBody("/select-test-prof","",
				function(data) {
					if(data.length >= 1){
						$('.nothing-in-test').remove();
						$.each(data, function() {
								$("#test-info").append(displayTestDiv(this.id_test,this.title));
						});
					}  
				});	  
		  });
		
			buttonEnable(".enable-test","1");
			buttonEnable(".disable-test","0");
			
			function buttonEnable(btn,isenable){
				$("#test-info").on('click', btn,function(){
					var id = $(this).attr("id").substring(11);
					var dataString = {id: id, isenable: isenable};
					ajaxBody("/enable-test-prof",dataString,
					function(data) {
						if(data.res != "1"){
							return false;
						}
						if(btn == ".enable-test"){
							$("#enable-test"+id).removeClass("enable-test");
							$("#enable-test"+id).addClass("disable-test");
							
							$("#dispo"+id).removeClass("red");
							$("#dispo"+id).addClass("green");
							$("#dispo"+id).text("Disponible");
							
							$("#enable-test"+id).removeClass("btn-success");
							$("#enable-test"+id).addClass("btn-danger");
							$("#enable-test"+id).text("Rendre indisponible");
						}else{
							$("#enable-test"+id).removeClass("disable-test");
							$("#enable-test"+id).addClass("enable-test");
							
							$("#dispo"+id).removeClass("green");
							$("#dispo"+id).addClass("red");
							$("#dispo"+id).text("Indisponible");
							
							$("#enable-test"+id).removeClass("btn-danger");
							$("#enable-test"+id).addClass("btn-success");
							$("#enable-test"+id).text("Rendre disponible");
						}	
					});
				});
			}
			
			$("#test-info").on('click', ".btn-test-detail",function(){
				$(".test-info").hide();
		  		$(".test-detail").fadeIn();
		  		var id_test = $(this).attr("id").substring(15);
						dataString = {id_test: id_test};
						ajaxBody("/select-test-prof",dataString,
								function(data){
									$("#test-detail-title").text(data[0].title);
						});			
			});
			
			$(".test-detail-back").click(function(){
				$(".test-info").fadeIn();
		  		$(".test-detail").hide();
			});
		
		function ajaxBody(url,dataString,successFunction){
			$.ajax({ 
					type: "POST", 
					url: url,
					data: dataString, 
					dataType: "json",
					success: function(data) {
						successFunction(data);
					}	 
				}); 
		}
		
});